package com.pet.businessdomain.productservice.services;

import com.pet.businessdomain.productservice.entities.ProductEffect;
import com.pet.businessdomain.productservice.mapper.ProductMapper;
import com.pet.businessdomain.productservice.transactions.BusinessTransactions;
import com.pet.businessdomain.shareddto.dto.CharacterDto;
import com.pet.businessdomain.shareddto.dto.CharacterInventoryResponseDTO;
import com.pet.businessdomain.productservice.entities.CharacterInventory;
import com.pet.businessdomain.productservice.entities.Product;
import com.pet.businessdomain.productservice.repository.CharacterInventoryRepository;
import com.pet.businessdomain.productservice.repository.ProductRepository;
import com.pet.businessdomain.shareddto.dto.NotificationDTO;
import com.pet.businessdomain.shareddto.dto.SExpenseResponseDto;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import com.pet.businessdomain.shareddto.enumentities.NotificationType;
import com.pet.businessdomain.shareddto.enumentities.ProductCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CharacterInventoryServiceImpl implements CharacterInventoryService {

    @Autowired
    private final CharacterInventoryRepository inventoryRepository;
    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final ProductMapper productMapper;
    @Autowired
    private final BusinessTransactions businessTransactions;
    @Override
    public CharacterDto buyProduct(Long characterId, Long productId, Integer quantity) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CharacterDto personDto = businessTransactions.getPerson(characterId);

        // 🔥 1. Lógica según tipo
        switch (product.getUsageType()) {

            case REUSABLE -> handleReusableProduct(characterId, product, quantity);

            case CONSUMABLE -> {
                // no se guarda en inventario
                personDto = applyEffects(personDto, product);
            }

            case SUBSCRIPTION -> handleSubscriptionProduct(characterId, product);
        }

        // 🔥 2. Actualizar stats
        personDto = businessTransactions.updateCharacterStats(characterId, personDto);

        // 🔥 3. Registrar gasto
        createExpense(personDto, product);

        // 🔥 4. Notificación
        setNotification(personDto, product);

        return personDto;
    }

    @Override
    public List<CharacterInventoryResponseDTO> getInventory(Long characterId) {

        return inventoryRepository.findByCharacterId(characterId)
                .stream()
                .map(inv -> CharacterInventoryResponseDTO.builder()
                        .productId(inv.getProduct().getId())
                        .productName(inv.getProduct().getName())
                        .category(inv.getProduct().getCategory())
                        .quantity(inv.getQuantity())
                        .product(productMapper.toDTO(productRepository.getById(inv.getProduct().getId())))
                        .build()
                )
                .toList();
    }

    public CharacterDto applyEffects(CharacterDto character, Product product) {

        for (ProductEffect effect : product.getEffects()) {

            switch (effect.getCategory()) {

                case "ENERGY":
                    character.getStats().setEnergy(
                            character.getStats().getEnergy() + effect.getValue()
                    );
                    break;

                case "CHARISMA":
                    character.getStats().setCharisma(
                            character.getStats().getCharisma() + effect.getValue()
                    );
                    break;

                case "INTELLIGENCE":
                    character.getStats().setIntelligence(
                            character.getStats().getIntelligence() + effect.getValue()
                    );
                    break;

                case "CREATIVITY":
                    character.getStats().setCreativity(
                            character.getStats().getCreativity() + effect.getValue()
                    );
                    break;

                case "RESILIENCE":
                    character.getStats().setResilience(
                            character.getStats().getResilience() + effect.getValue()
                    );
                    break;

                case "HEALTH":
                    character.getStats().setHealth(
                            character.getStats().getHealth() + effect.getValue()
                    );
                    break;

                case "HAPPINESS":
                    character.getStats().setHappiness(
                            character.getStats().getHappiness() + effect.getValue()
                    );
                    break;

                case "STRESS":
                    character.getStats().setStress(
                            character.getStats().getStress() + effect.getValue()
                    );
                    break;

                case "FINANCES":
                    character.getStats().setFinances(
                            character.getStats().getFinances() + effect.getValue()
                    );
                    break;
            }
        }
        return character;
    }

    public static EnumAll.ExpenseCategory toExpenseCategory(ProductCategory category) {

        if (category == null) {
            return EnumAll.ExpenseCategory.OTHER;
        }

        return switch (category) {
            case ALIMENTACION -> EnumAll.ExpenseCategory.FOOD;
            case VIVIENDA -> EnumAll.ExpenseCategory.HOUSING;
            case TRANSPORTE -> EnumAll.ExpenseCategory.TRANSPORT;
            case SALUD -> EnumAll.ExpenseCategory.HEALTH;
            case ENTRETENIMIENTO -> EnumAll.ExpenseCategory.LEISURE;
            case TECNOLOGIA -> EnumAll.ExpenseCategory.OTHER;
            case SOCIAL -> EnumAll.ExpenseCategory.LEISURE;
        };
    }

    private void setNotification(CharacterDto dto, Product product) {

        NotificationDTO notificationDTO = new NotificationDTO();

        notificationDTO.setUserId(dto.getId());
        notificationDTO.setFromUserId(dto.getUid());
        notificationDTO.setType(NotificationType.SYSTEM);

        notificationDTO.setTitle("Has adquirido un producto");
        notificationDTO.setSubTitle(product.getName());
        notificationDTO.setMessage(product.getDescription());

        // 🔥 Nuevo sistema
        notificationDTO.setResourceType(EnumAll.NotificationResourceType.PRODUCT);
        notificationDTO.setResourceId(product.getId());

        // 🔥 Navegación directa
        notificationDTO.setActionUrl("/products/" + product.getId());

        // 🔥 Metadata enriquecida
        notificationDTO.setMetadata("""
        {
            "productId": %d,
            "productName": "%s"
        }
    """.formatted(
                product.getId(),
                product.getName().replace("\"", "\\\"") // evitar romper JSON
        ));

        notificationDTO.setRead(false);

        businessTransactions.setNotifications(notificationDTO);
    }

    private void handleReusableProduct(Long characterId, Product product, Integer quantity) {

        CharacterInventory inventory = inventoryRepository
                .findByCharacterIdAndProductId(characterId, product.getId())
                .orElse(null);

        if (inventory != null) {
            inventory.setQuantity(inventory.getQuantity() + quantity);
        } else {
            inventory = new CharacterInventory();
            inventory.setCharacterId(characterId);
            inventory.setProduct(product);
            inventory.setQuantity(quantity);
        }

        inventoryRepository.save(inventory);
    }

    private void handleSubscriptionProduct(Long characterId, Product product) {

        CharacterInventory inventory = new CharacterInventory();
        inventory.setCharacterId(characterId);
        inventory.setProduct(product);
        inventory.setQuantity(1);

        inventoryRepository.save(inventory);
    }

    private void createExpense(CharacterDto personDto, Product product) {

        SExpenseResponseDto expense = new SExpenseResponseDto();

        expense.setActive(true);
        expense.setCategory(toExpenseCategory(product.getCategory()));
        expense.setConcept(product.getName());

        // 🔥 clave aquí
        if (product.getUsageType() == EnumAll.ProductUsageType.SUBSCRIPTION) {
            expense.setFrequency(EnumAll.Frequency.MONTHLY);
        } else {
            expense.setFrequency(EnumAll.Frequency.OTHER);
        }

        expense.setAmount(product.getPrice());
        expense.setStartDate(LocalDate.now());
        expense.setExternalRefId(product.getId());
        expense.setExternalRefType("Product");
        expense.setEssential(false);

        businessTransactions.setExpense(expense, personDto.getId());
    }
}
