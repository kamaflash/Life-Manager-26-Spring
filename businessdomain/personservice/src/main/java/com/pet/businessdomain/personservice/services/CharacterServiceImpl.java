package com.pet.businessdomain.personservice.services;

import com.pet.businessdomain.personservice.entities.CharacterEntity;
import com.pet.businessdomain.personservice.entities.CharacterStats;
import com.pet.businessdomain.personservice.entities.SkillStateEmbeddable;
import com.pet.businessdomain.personservice.mapper.CharacterMapper;
import com.pet.businessdomain.personservice.repository.CharacterRepository;
import com.pet.businessdomain.personservice.transactions.BusinessTransactions;
import com.pet.businessdomain.shareddto.dto.*;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import com.pet.businessdomain.shareddto.enumentities.NotificationEventType;
import com.pet.businessdomain.shareddto.enumentities.NotificationResourceType;
import com.pet.businessdomain.shareddto.enumentities.NotificationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class CharacterServiceImpl implements CharacterService {

    private static final int BASE_XP_PER_LEVEL = 100;
    private static final int MAX_SKILL_LEVEL = 10;
    private static final int MIN_XP_PER_SKILL = 10;
    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private CharacterMapper characterMapper;

    @Autowired
    private BusinessTransactions businessTransactions;

    @Override
    public CharacterDto createCharacter(CharacterDto characterDto) {
        CharacterEntity entity = characterMapper.toEntity(characterDto);
        entity.setCreatedAt(LocalDateTime.now());
        CharacterEntity saved = characterRepository.save(entity);
        CharacterDto dto = characterMapper.toDto(saved);
        SFinanceAccountResponseDto sfinanceAccountResponseDto = businessTransactions.setAccount(createAccountMock(dto.getId(), characterDto),characterDto.getNewIncome(),characterDto.getNewExpense());
        List<SFinanceAccountResponseDto> listAccount = new ArrayList<>();
        listAccount.add(sfinanceAccountResponseDto);
        saved.setAccounts(listAccount);
        CharacterTrainingDto trainerDto = businessTransactions.setEducation(characterDto.getEducation().getFirst(),saved.getId());
        SystemDto systemDto = new SystemDto();
        systemDto.setUid(saved.getUid());
        systemDto.setVeces(0);
        SystemDto systemDtoSave = businessTransactions.setSystem(systemDto);
        dto.setId(saved.getId());
        setNotification(dto);
        return characterMapper.toDto(saved);
    }

    @Override
    public CharacterDto getCharacterById(Long id) {
        CharacterEntity entity = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Character not found with id " + id));

        entity.setAccounts(businessTransactions.getAccount(id));
        CharacterDto dto = characterMapper.toDto(entity);
        dto.setEducation(businessTransactions.getEducation(entity.getId()));

        // 🔥 NUEVO: Obtener todos los trabajos del personaje directamente
        List<CharacterJobDTO> characterJobs = businessTransactions.getCharacterJobs(entity.getId());
        dto.setJobs(characterJobs != null ? characterJobs : List.of()); // Setear lista de trabajos

        dto.setInventory(businessTransactions.getInventory(id));

        return dto;
    }

    @Override
    public CharacterDto getCharacterByUid(Long uid) {
        CharacterEntity entity = characterRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("Character not found with id " + uid));

        entity.setAccounts(businessTransactions.getAccount(entity.getId()));
        CharacterDto dto = characterMapper.toDto(entity);
        dto.setEducation(businessTransactions.getEducation(entity.getId()));

        // 🔥 Obtener todos los trabajos y filtrar el activo (o tomar el primero)
        List<CharacterJobDTO> characterJobs = businessTransactions.getCharacterJobs(entity.getId());

        if (characterJobs != null && !characterJobs.isEmpty()) {
            // Opción 1: Tomar el trabajo activo
            CharacterJobDTO activeJob = characterJobs.stream()
                    .findFirst()
                    .orElse(characterJobs.get(0)); // O tomar el primero

            dto.setJobs(List.of(activeJob));

            // Opción 2: Setear todos los trabajos
            // dto.setJobs(characterJobs);
        } else {
            dto.setJobs(List.of()); // Lista vacía si no tiene trabajos
        }

        dto.setInventory(businessTransactions.getInventory(entity.getId()));

        return dto;
    }

    @Override
    public List<CharacterDto> getAllCharacters() {
        return characterRepository.findAll()
                .stream()
                .map(characterMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CharacterDto updateCharacterName(Long id, String name) {
        CharacterEntity entity = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Character not found with id " + id));

        // Mapeamos los campos del DTO a la entidad existente
        entity.setName(name);
        entity.setUpdatedAt(LocalDateTime.now());

        CharacterEntity updated = characterRepository.save(entity);
        return characterMapper.toDto(updated);
    }

    @Override
    public CharacterDto updateCharacter(Long id, CharacterDto characterDto) {

        CharacterEntity entity = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Character not found with id " + id));

        // ===== CAMPOS BÁSICOS =====
        entity.setName(characterDto.getName());
        entity.setAvatar(characterDto.getAvatar());
        entity.setAge(characterDto.getAge());
        entity.setBirthDate(characterDto.getBirthDate());
        entity.setGender(characterDto.getGender());
        entity.setCity(characterDto.getCity());
        entity.setResidentialCity(characterDto.getResidentialCity());

        entity.setXpAcademy(characterDto.getXpAcademy());
        entity.setXpJobs(characterDto.getXpJobs());

        // ===== STATS =====
        if (characterDto.getStats() != null) {

            CharacterStats stats = entity.getStats();

            stats.setIntelligence(characterDto.getStats().getIntelligence());
            stats.setCharisma(characterDto.getStats().getCharisma());
            stats.setCreativity(characterDto.getStats().getCreativity());
            stats.setResilience(characterDto.getStats().getResilience());
            stats.setHealth(characterDto.getStats().getHealth());
            stats.setEnergy(characterDto.getStats().getEnergy());
            stats.setHappiness(characterDto.getStats().getHappiness());
            stats.setStress(characterDto.getStats().getStress());
            stats.setFinances(characterDto.getStats().getFinances());
        }

        // ===== SKILLS =====
        if (characterDto.getSkills() != null) {
            entity.setSkills(
                    characterDto.getSkills().entrySet().stream()
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    e -> {
                                        SkillStateDto dto = e.getValue();
                                        SkillStateEmbeddable skill = new SkillStateEmbeddable();
                                        skill.setKeyValue(dto.getKeyValue());
                                        skill.setLevel(dto.getLevel());
                                        skill.setXp(dto.getXp());
                                        skill.setLocked(dto.getLocked());
                                        return skill;
                                    }
                            ))
            );
        }

        // ===== INTERESTS =====
        entity.setInterests(
                characterDto.getInterests() != null
                        ? new ArrayList<>(characterDto.getInterests())
                        : null
        );

        // ===== FRIENDS (String -> Long) =====
        entity.setFriends(new ArrayList<>(
                characterDto.getFriends().stream()
                        .map(Long::valueOf)
                        .toList()
        ));

        entity.setFamilyIds(new ArrayList<>(
                characterDto.getFamilyIds().stream()
                        .map(Long::valueOf)
                        .toList()
        ));

        // ===== PARTNER =====
        if (characterDto.getPartnerId() != null) {
            entity.setPartnerId(Long.valueOf(characterDto.getPartnerId()));
        }

        // ===== INVENTORY =====


        // ===== TIMESTAMP =====
        entity.setUpdatedAt(LocalDateTime.now());

        CharacterEntity updated = characterRepository.save(entity);

        return characterMapper.toDto(updated);
    }

    @Override
    public void deleteCharacter(Long id) {
        CharacterEntity entity = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Character not found with id " + id));
        characterRepository.delete(entity);
    }

    private SFinanceAccountResponseDto createAccountMock(Long id, CharacterDto characterDto) {
        SFinanceAccountResponseDto sfinanceAccountResponseDto = new SFinanceAccountResponseDto();
        sfinanceAccountResponseDto.setOwnerId(id);
        sfinanceAccountResponseDto.setOwnerType(EnumAll.OwnerType.CHARACTER);

        sfinanceAccountResponseDto.setBalance(
                characterDto.getNewIncome()
                        .subtract(characterDto.getNewExpense())
                        .subtract(BigDecimal.valueOf(200))
        );
        return sfinanceAccountResponseDto;
    }

    private void setNotification(CharacterDto dto) {

        NotificationDTO notificationDTO = new NotificationDTO();

        notificationDTO.setUserId(dto.getId());
        notificationDTO.setFromUserId(dto.getUid());
        notificationDTO.setType(NotificationType.SYSTEM);

        notificationDTO.setTitle("Creación de nuevo personaje");
        notificationDTO.setSubTitle("Comienza tu aventura");
        notificationDTO.setMessage("Visita tu perfil para obtener tus datos.");

        // 🔥 Nuevo sistema
        notificationDTO.setResourceType(NotificationResourceType.USER);
        notificationDTO.setResourceId(dto.getId());
        notificationDTO.setEventType(NotificationEventType.WELCOME);
        // 🔥 Navegación directa frontend
        notificationDTO.setActionUrl("/profile" );

        // 🔥 Metadata (opcional pero muy recomendable)
        notificationDTO.setMetadata("""
        {
            "characterId": %d
        }
    """.formatted(dto.getId()));

        notificationDTO.setRead(false);

        businessTransactions.setNotifications(notificationDTO);
    }

    @Override
    public CharacterSkillsUpdateResponseDto updateCharacterSkills(CharacterSkillsUpdateRequestDto request) {
        CharacterSkillsUpdateResponseDto response = new CharacterSkillsUpdateResponseDto();
        response.setCharacterId(request.getCharacterId());

        try {
            CharacterEntity character = characterRepository.findById(request.getCharacterId())
                    .orElseThrow(() -> new RuntimeException("Personaje no encontrado"));

            // 1. Actualizar STATS
            if (request.getStatRewards() != null && !request.getStatRewards().isEmpty()) {
                updateStats(character, request.getStatRewards());
            }

            // 2. Actualizar SKILLS
            Map<String, SkillStateEmbeddable> updatedSkills = updateSkills(
                    character,
                    request.getSkillsToUnlock(),
                    request.getTotalXpReward()
            );

            // 3. Convertir a DTO para respuesta
            Map<String, SkillStateDto> responseSkills = new HashMap<>();
            for (Map.Entry<String, SkillStateEmbeddable> entry : updatedSkills.entrySet()) {
                SkillStateEmbeddable s = entry.getValue();
                responseSkills.put(entry.getKey(), new SkillStateDto());
            }

            // 4. Actualizar XP académico
            int currentXp = character.getXpAcademy() != null ? character.getXpAcademy() : 0;
            character.setXpAcademy(currentXp + request.getAcademicXpReward());

            characterRepository.save(character);

            response.setUpdatedSkills(responseSkills);
            response.setNewAcademicXp(character.getXpAcademy());
            response.setUpdatedStats(mapToStatsDto(character.getStats()));
            response.setSuccess(true);
            response.setMessage("Skills actualizados correctamente");

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }
    @Override
    public SkillPaginatedResponseDTO getCharacterSkills(SkillFiltersDTO filters) {
        log.info("Obteniendo habilidades del personaje: {}", filters);

        // Limpiar filtros vacíos
        filters.cleanEmptyFilters();

        CharacterEntity character = characterRepository.findById(filters.getCharacterId())
                .orElseThrow(() -> new RuntimeException("Personaje no encontrado"));

        List<SkillStateDto> allSkills = new ArrayList<>();

        if (character.getSkills() != null && !character.getSkills().isEmpty()) {
            for (Map.Entry<String, SkillStateEmbeddable> entry : character.getSkills().entrySet()) {
                SkillStateEmbeddable skillState = entry.getValue();

                // 🔥 FILTRAR POR SKILLS ESPECÍFICAS
                if (filters.hasSkillsFilter()) {
                    if (!filters.getSkills().contains(skillState.getKeyValue())) {
                        continue;
                    }
                }

                // 🔥 FILTRAR POR NIVEL
                if (filters.getMinLevel() != null && skillState.getLevel() < filters.getMinLevel()) {
                    continue;
                }
                if (filters.getMaxLevel() != null && skillState.getLevel() > filters.getMaxLevel()) {
                    continue;
                }

                // 🔥 FILTRAR POR ESTADO (locked)
                if (filters.getLocked() != null && skillState.isLocked() != filters.getLocked()) {
                    continue;
                }

                // Calcular progreso
                int xpNeeded = skillState.getLevel() * 100;
                int progress = 0;
                if (xpNeeded > 0) {
                    progress = Math.min(100, (int) Math.round((skillState.getXp() * 100.0) / xpNeeded));
                }

                // 🔥 FILTRAR POR PROGRESO
                if (filters.getMinProgress() != null && progress < filters.getMinProgress()) {
                    continue;
                }
                if (filters.getMaxProgress() != null && progress > filters.getMaxProgress()) {
                    continue;
                }

                // 🔥 BÚSQUEDA GLOBAL
                if (filters.getSearch() != null && !filters.getSearch().isEmpty()) {
                    String skillName = getSkillLabel(skillState.getKeyValue()).toLowerCase();
                    if (!skillName.contains(filters.getSearch().toLowerCase())) {
                        continue;
                    }
                }

                SkillStateDto skillDTO = SkillStateDto.builder()
                        .keyValue(skillState.getKeyValue())
                        .name(getSkillLabel(skillState.getKeyValue()))
                        .level(skillState.getLevel())
                        .xp(skillState.getXp())
                        .locked(skillState.isLocked())
                        .progress(progress)
                        .xpToNextLevel(Math.max(0, xpNeeded - skillState.getXp()))
                        .build();

                allSkills.add(skillDTO);
            }
        }

        // Ordenamiento
        Comparator<SkillStateDto> comparator = getSkillComparator(filters.getSortBy());
        if ("desc".equalsIgnoreCase(filters.getSortDir())) {
            comparator = comparator.reversed();
        }
        allSkills.sort(comparator);

        // Paginación
        int totalItems = allSkills.size();
        int totalPages = (int) Math.ceil((double) totalItems / filters.getSize());
        int start = filters.getPage() * filters.getSize();
        int end = Math.min(start + filters.getSize(), totalItems);

        List<SkillStateDto> pagedSkills = allSkills.subList(start, end);

        return SkillPaginatedResponseDTO.builder()
                .skills(pagedSkills)
                .currentPage(filters.getPage())
                .totalItems(totalItems)
                .totalPages(totalPages)
                .pageSize(filters.getSize())
                .build();
    }

    /**
     * Obtiene una habilidad específica del personaje.
     *
     * @param characterId ID del personaje
     * @param skillKey Clave de la habilidad
     * @return SkillDTO con los datos de la habilidad
     */
    public SkillStateDto getCharacterSkill(Long characterId, String skillKey) {
        log.info("Obteniendo habilidad {} del personaje {}", skillKey, characterId);

        CharacterEntity character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("Personaje no encontrado: " + characterId));

        if (character.getSkills() == null || !character.getSkills().containsKey(skillKey)) {
            throw new RuntimeException("Habilidad no encontrada: " + skillKey);
        }

        SkillStateEmbeddable skillState = character.getSkills().get(skillKey);

        int xpNeeded = skillState.getLevel() * 100;
        int progress = 0;
        int xpToNextLevel = 0;

        if (xpNeeded > 0) {
            progress = Math.min(100, (int) Math.round((skillState.getXp() * 100.0) / xpNeeded));
            xpToNextLevel = Math.max(0, xpNeeded - skillState.getXp());
        }

        return SkillStateDto.builder()
                .keyValue(skillState.getKeyValue())
                .name(getSkillLabel(skillState.getKeyValue()))
                .level(skillState.getLevel())
                .xp(skillState.getXp())
                .locked(skillState.isLocked())
                .progress(progress)
                .xpToNextLevel(xpToNextLevel)
                .build();
    }

    /**
     * Aplica los filtros a la lista de habilidades.
     *
     * @param skills Lista de habilidades
     * @param filters Filtros a aplicar
     * @return Lista filtrada
     */
    private List<SkillStateDto> applyFilters(List<SkillStateDto> skills, SkillFiltersDTO filters) {
        List<SkillStateDto> filtered = new ArrayList<>(skills);

        // Búsqueda global por nombre
        if (filters.getSearch() != null && !filters.getSearch().isEmpty()) {
            String searchLower = filters.getSearch().toLowerCase();
            filtered = filtered.stream()
                    .filter(skill -> skill.getName().toLowerCase().contains(searchLower) ||
                            skill.getKeyValue().toLowerCase().contains(searchLower))
                    .collect(Collectors.toList());
        }

        // Filtro por nivel mínimo
        if (filters.getMinLevel() != null) {
            filtered = filtered.stream()
                    .filter(skill -> skill.getLevel() >= filters.getMinLevel())
                    .collect(Collectors.toList());
        }

        // Filtro por nivel máximo
        if (filters.getMaxLevel() != null) {
            filtered = filtered.stream()
                    .filter(skill -> skill.getLevel() <= filters.getMaxLevel())
                    .collect(Collectors.toList());
        }

        // Filtro por bloqueado/disponible
        if (filters.getLocked() != null) {
            filtered = filtered.stream()
                    .filter(skill -> skill.getLocked().equals(filters.getLocked()))
                    .collect(Collectors.toList());
        }

        // Filtro por progreso mínimo
        if (filters.getMinProgress() != null) {
            filtered = filtered.stream()
                    .filter(skill -> skill.getProgress() >= filters.getMinProgress())
                    .collect(Collectors.toList());
        }

        // Filtro por progreso máximo
        if (filters.getMaxProgress() != null) {
            filtered = filtered.stream()
                    .filter(skill -> skill.getProgress() <= filters.getMaxProgress())
                    .collect(Collectors.toList());
        }

        return filtered;
    }

    /**
     * Aplica el ordenamiento a la lista de habilidades.
     *
     * @param skills Lista de habilidades
     * @param sortBy Campo por el cual ordenar
     * @param sortDir Dirección de ordenamiento (asc/desc)
     * @return Lista ordenada
     */

    private List<SkillStateDto> applySorting(List<SkillStateDto> skills, String sortBy, String sortDir) {
        Comparator<SkillStateDto> comparator;

        switch (sortBy) {
            case "level":
                comparator = Comparator.comparing(SkillStateDto::getLevel);
                break;
            case "xp":
                comparator = Comparator.comparing(SkillStateDto::getXp);
                break;
            case "progress":
                comparator = Comparator.comparing(SkillStateDto::getProgress);
                break;
            case "locked":
                comparator = Comparator.comparing(SkillStateDto::getLocked);
                break;
            case "keyValue":
            default:
                comparator = Comparator.comparing(SkillStateDto::getKeyValue);
                break;
        }

        if ("desc".equalsIgnoreCase(sortDir)) {
            comparator = comparator.reversed();
        }

        return skills.stream().sorted(comparator).collect(Collectors.toList());
    }

    /**
     * Obtiene la etiqueta traducida de la habilidad.
     *
     * @param skillKey Clave de la habilidad
     * @return Nombre legible de la habilidad
     */
    private String getSkillLabel(String skillKey) {
        Map<String, String> skillLabels = new HashMap<>();
        // Tecnología
        skillLabels.put("basic_programming", "Programación básica");
        skillLabels.put("math_logic", "Lógica matemática");
        skillLabels.put("problem_solving", "Resolución de problemas");
        skillLabels.put("data_analysis", "Análisis de datos");
        skillLabels.put("statistics", "Estadística");
        skillLabels.put("web_design", "Diseño web");
        skillLabels.put("frontend_development", "Desarrollo frontend");
        skillLabels.put("python_basics", "Python básico");
        skillLabels.put("java_advanced", "Java avanzado");
        skillLabels.put("sql", "SQL");
        skillLabels.put("big_data", "Big Data");
        skillLabels.put("machine_learning", "Machine Learning");
        skillLabels.put("ai_basics", "IA básica");
        skillLabels.put("cybersecurity_basics", "Ciberseguridad básica");
        skillLabels.put("cloud_basics", "Cloud básico");
        skillLabels.put("aws", "AWS");
        skillLabels.put("azure", "Azure");
        skillLabels.put("google_cloud", "Google Cloud");
        skillLabels.put("devops_basics", "DevOps básico");
        skillLabels.put("ci_cd", "CI/CD");
        skillLabels.put("automation", "Automatización");
        skillLabels.put("react", "React");
        skillLabels.put("angular", "Angular");
        skillLabels.put("react_native", "React Native");
        skillLabels.put("flutter", "Flutter");
        skillLabels.put("mobile_development", "Desarrollo móvil");
        skillLabels.put("robotics", "Robótica");
        skillLabels.put("physics_applied", "Física aplicada");
        skillLabels.put("object_oriented_design", "Diseño orientado a objetos");
        skillLabels.put("web_security", "Seguridad web");

        // Creatividad
        skillLabels.put("creativity", "Creatividad");
        skillLabels.put("graphic_design", "Diseño gráfico");

        // Habilidades blandas
        skillLabels.put("teamwork", "Trabajo en equipo");
        skillLabels.put("leadership", "Liderazgo");
        skillLabels.put("adaptability", "Adaptabilidad");
        skillLabels.put("responsibility", "Responsabilidad");
        skillLabels.put("verbal_communication", "Comunicación verbal");

        // Otras
        skillLabels.put("basic_computer", "Informática básica");
        skillLabels.put("office", "Office");
        skillLabels.put("mechanics", "Mecánica");
        skillLabels.put("electricity", "Electricidad");
        skillLabels.put("cooking", "Cocina");
        skillLabels.put("languages", "Idiomas");

        return skillLabels.getOrDefault(skillKey, skillKey.replace("_", " "));
    }
    private void updateStats(CharacterEntity character, Map<String, Integer> rewards) {
        // Obtener o crear las estadísticas
        CharacterStats stats = character.getStats();
        if (stats == null) {
            stats = new CharacterStats();
            character.setStats(stats);
        }

        final CharacterStats finalStats = stats;

        // Mapeo de claves a setters
        rewards.forEach((key, value) -> {
            switch (key.toLowerCase()) {
                case "intelligence" -> finalStats.setIntelligence(finalStats.getIntelligence() + value);
                case "charisma" -> finalStats.setCharisma(finalStats.getCharisma() + value);
                case "creativity" -> finalStats.setCreativity(finalStats.getCreativity() + value);
                case "resilience" -> finalStats.setResilience(finalStats.getResilience() + value);
                case "finances" -> finalStats.setFinances(finalStats.getFinances() + value);
            }
        });
    }

    private Map<String, SkillStateEmbeddable> updateSkills(CharacterEntity character, List<String> skillsToUnlock, int totalXpReward) {
        if (skillsToUnlock == null || skillsToUnlock.isEmpty()) {
            return character.getSkills();
        }

        int xpPerSkill = Math.max(MIN_XP_PER_SKILL, totalXpReward / skillsToUnlock.size());

        Map<String, SkillStateEmbeddable> skills = character.getSkills();
        if (skills == null) {
            skills = new HashMap<>();
            character.setSkills(skills);
        }

        for (String skillKey : skillsToUnlock) {
            SkillStateEmbeddable skill = skills.get(skillKey);
            if (skill == null) {
                skill = new SkillStateEmbeddable();
                skill.setKeyValue(skillKey);
                skill.setLevel(1);
                skill.setXp(0);
                skill.setLocked(false);
                skills.put(skillKey, skill);
            }
            addSkillXp(skill, xpPerSkill);
        }
        return skills;
    }

    private void addSkillXp(SkillStateEmbeddable skill, int xpGained) {
        if (Boolean.TRUE.equals(skill.isLocked())) return;

        int newXp = (skill.getXp() != 0 ? skill.getXp() : 0) + xpGained;
        int newLevel = skill.getLevel() != 0 ? skill.getLevel() : 1;

        while (newXp >= BASE_XP_PER_LEVEL * newLevel && newLevel < MAX_SKILL_LEVEL) {
            newXp -= BASE_XP_PER_LEVEL * newLevel;
            newLevel++;
        }

        skill.setXp(newXp);
        skill.setLevel(newLevel);
    }

    private StatsDto mapToStatsDto(CharacterStats stats) {
        if (stats == null) return new StatsDto();
        StatsDto dto = new StatsDto();
        dto.setIntelligence(stats.getIntelligence());
        dto.setCharisma(stats.getCharisma());
        dto.setCreativity(stats.getCreativity());
        dto.setResilience(stats.getResilience());
        dto.setFinances(stats.getFinances());
        dto.setHealth(stats.getHealth());
        dto.setEnergy(stats.getEnergy());
        dto.setHappiness(stats.getHappiness());
        dto.setStress(stats.getStress());
        return dto;
    }

    /**
     * Obtiene el comparador para ordenar habilidades según el campo especificado.
     *
     * @param sortBy Campo por el cual ordenar (keyValue, level, xp, progress, locked)
     * @return Comparator para ordenar habilidades
     */
    private Comparator<SkillStateDto> getSkillComparator(String sortBy) {
        switch (sortBy) {
            case "level":
                return Comparator.comparing(SkillStateDto::getLevel, Comparator.nullsLast(Comparator.naturalOrder()));
            case "xp":
                return Comparator.comparing(SkillStateDto::getXp, Comparator.nullsLast(Comparator.naturalOrder()));
            case "progress":
                return Comparator.comparing(SkillStateDto::getProgress, Comparator.nullsLast(Comparator.naturalOrder()));
            case "locked":
                return Comparator.comparing(SkillStateDto::getLocked, Comparator.nullsLast(Comparator.naturalOrder()));
            case "name":
                return Comparator.comparing(SkillStateDto::getName, Comparator.nullsLast(String::compareTo));
            case "keyValue":
            default:
                return Comparator.comparing(SkillStateDto::getKeyValue, Comparator.nullsLast(String::compareTo));
        }
    }
}

