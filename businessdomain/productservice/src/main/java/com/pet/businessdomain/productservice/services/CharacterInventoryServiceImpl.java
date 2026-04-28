package com.pet.businessdomain.productservice.services;

import com.pet.businessdomain.productservice.entities.*;
import com.pet.businessdomain.productservice.mapper.*;
import com.pet.businessdomain.productservice.repository.*;
import com.pet.businessdomain.productservice.transactions.BusinessTransactions;
import com.pet.businessdomain.shareddto.dto.*;
import com.pet.businessdomain.shareddto.dto.products.*;
import com.pet.businessdomain.shareddto.enumentities.*;
import com.pet.businessdomain.shareddto.enumentities.products.ProductRarity;
import com.pet.businessdomain.shareddto.enumentities.products.ProductSlot;
import com.pet.businessdomain.shareddto.enumentities.products.TriggerCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CharacterInventoryServiceImpl implements CharacterInventoryService {

    private final CharacterInventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final ProductInstantEffectRepository instantEffectRepository;
    private final ProductPassiveEffectRepository passiveEffectRepository;
    private final ProductRecipeRepository recipeRepository;
    private final ProductShopOfferRepository shopOfferRepository;
    private final IngredientRepository ingredientRepository;

    private final ProductMapper productMapper;
    private final ProductInstantEffectMapper instantEffectMapper;
    private final ProductPassiveEffectMapper passiveEffectMapper;
    private final CharacterInventoryMapper inventoryMapper;
    private final ProductRecipeMapper recipeMapper;
    private final ProductShopOfferMapper shopOfferMapper;

    private final BusinessTransactions businessTransactions;

    // ========== COMPRA ==========
    @Override
    @Transactional
    public BuyProductResponseDTO buyProduct(BuyProductRequestDTO request) {
        log.info("Character {} buying product {} x{}", request.getCharacterId(), request.getProductId(), request.getQuantity());

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CharacterDto character = businessTransactions.getPerson(request.getCharacterId());

        // Validaciones
        if (!product.getActive()) {
            return BuyProductResponseDTO.builder()
                    .success(false)
                    .message("El producto no está disponible")
                    .build();
        }

        // ✅ CORREGIDO: Manejar level null
        Integer characterLevel = character.getLevel();
        if (characterLevel == null) {
            characterLevel = 1;
            character.setLevel(characterLevel);
        }

        if (characterLevel < product.getRequiredLevel()) {
            return BuyProductResponseDTO.builder()
                    .success(false)
                    .message(String.format("Necesitas nivel %d para comprar este producto", product.getRequiredLevel()))
                    .build();
        }

        BigDecimal finalPrice = calculateFinalPrice(product);
        BigDecimal totalPrice = finalPrice.multiply(BigDecimal.valueOf(request.getQuantity()));

        // Verificar saldo
        SFinanceAccountResponseDto account = getPrimaryAccount(character);
        if (account.getBalance().compareTo(totalPrice) < 0) {
            return BuyProductResponseDTO.builder()
                    .success(false)
                    .message("Saldo insuficiente")
                    .build();
        }

        // Descontar saldo
        account.setBalance(account.getBalance().subtract(totalPrice));
        businessTransactions.updatePerson(character);

        // Registrar gasto
        createExpense(character, product, totalPrice);

        // Añadir al inventario
        CharacterInventory inventory = addToInventory(character.getId(), product, request.getQuantity());

        // Notificar
        sendPurchaseNotification(character, product, request.getQuantity(), totalPrice);

        return BuyProductResponseDTO.builder()
                .success(true)
                .message(String.format("Has comprado %d %s por %,.2f€", request.getQuantity(), product.getName(), totalPrice))
                .inventoryId(inventory.getId())
                .product(productMapper.toDto(product))
                .quantity(request.getQuantity())
                .totalPrice(totalPrice)
                .newBalance(account.getBalance())
                .build();
    }

    // ========== USO ==========
    @Override
    @Transactional
    public UseProductResponseDTO useProduct(UseProductRequestDTO request) {
        log.info("Character {} using product", request.getCharacterId());

        CharacterInventory inventory;
        if (request.getInventoryId() != null) {
            inventory = inventoryRepository.findById(request.getInventoryId())
                    .orElseThrow(() -> new RuntimeException("Inventory item not found"));
        } else {
            inventory = inventoryRepository.findByCharacterIdAndProductId(request.getCharacterId(), request.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found in inventory"));
        }

        Product product = inventory.getProduct();
        CharacterDto character = businessTransactions.getPerson(request.getCharacterId());

        // Validaciones
        if (!product.getActive()) {
            return createErrorResponse("El producto no está disponible");
        }

        // Verificar cooldown
        if (inventory.getLastUsedAt() != null && product.getCooldownHours() != null && product.getCooldownHours() > 0) {
            LocalDateTime cooldownEnd = inventory.getLastUsedAt().plusHours(product.getCooldownHours());
            if (LocalDateTime.now().isBefore(cooldownEnd)) {
                long remainingMinutes = java.time.Duration.between(LocalDateTime.now(), cooldownEnd).toMinutes();
                return createErrorResponse(String.format("Producto en enfriamiento. Espera %d minutos", remainingMinutes));
            }
        }

        // Verificar durabilidad
        if (inventory.getDurabilityLeft() != null && inventory.getDurabilityLeft() == 0) {
            return createErrorResponse("El producto ya no tiene usos disponibles");
        }

        // Aplicar efectos
        List<String> effectsApplied = new ArrayList<>();
        List<StatChangeDTO> statChanges = new ArrayList<>();
        int xpGained = 0;
        int levelUps = 0;

        // Efectos instantáneos
        for (ProductInstantEffect effect : product.getInstantEffects()) {
            StatChangeDTO change = applyInstantEffect(character, effect, request.getQuantity());
            if (change != null) {
                statChanges.add(change);
                effectsApplied.add(change.getDescription());
                xpGained += effect.getAmount() != null ? effect.getAmount() : 0;
            }
        }

        // Verificar subida de nivel
        int oldLevel = character.getLevel();
        character = businessTransactions.updatePerson(character);
        int newLevel = character.getLevel() != null ? character.getLevel() : oldLevel;
        levelUps = newLevel - oldLevel;

        // Actualizar inventario
        inventory.setLastUsedAt(LocalDateTime.now());

        if (product.getIsConsumable() && request.getQuantity() != null) {
            int newQuantity = inventory.getQuantity() - request.getQuantity();
            if (newQuantity <= 0) {
                inventoryRepository.delete(inventory);
            } else {
                inventory.setQuantity(newQuantity);
                inventoryRepository.save(inventory);
            }
        } else {
            // Decrementar durabilidad
            if (inventory.getDurabilityLeft() != null && inventory.getDurabilityLeft() > 0) {
                inventory.setDurabilityLeft(inventory.getDurabilityLeft() - 1);
                inventoryRepository.save(inventory);
            }
        }

        // Registrar efecto pasivo si aplica
        if (product.getDurationHours() != null && product.getDurationHours() > 0) {
            inventory.setActiveUntil(LocalDateTime.now().plusHours(product.getDurationHours()));
            inventoryRepository.save(inventory);
        }

        return UseProductResponseDTO.builder()
                .success(true)
                .message(String.format("Has usado %s", product.getName()))
                .updatedInventory(inventoryMapper.toDto(inventory))
                .effectsApplied(effectsApplied)
                .xpGained(xpGained)
                .levelUps(levelUps)
                .statChanges(statChanges)
                .build();
    }

    // ========== EQUIPAR ==========
    @Override
    @Transactional
    public CharacterInventoryDTO equipProduct(Long inventoryId, Long characterId) {
        log.info("Character {} equipping product {}", characterId, inventoryId);

        CharacterInventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory item not found"));

        Product product = inventory.getProduct();

        // Desequipar producto del mismo slot
        List<CharacterInventory> equippedInSlot = inventoryRepository.findByCharacterIdAndEquippedTrue(characterId)
                .stream()
                .filter(i -> i.getProduct().getSlot() == product.getSlot())
                .toList();

        for (CharacterInventory eq : equippedInSlot) {
            eq.setEquipped(false);
            eq.setEquippedSince(null);
            inventoryRepository.save(eq);
        }

        inventory.setEquipped(true);
        inventory.setEquippedSince(LocalDateTime.now());

        if (product.getDurationHours() != null && product.getDurationHours() > 0) {
            inventory.setActiveUntil(LocalDateTime.now().plusHours(product.getDurationHours()));
        }

        inventoryRepository.save(inventory);

        // Aplicar efectos pasivos del slot
        applyPassiveEffects(characterId);

        return inventoryMapper.toDto(inventory);
    }

    @Override
    @Transactional
    public CharacterInventoryDTO unequipProduct(Long inventoryId, Long characterId) {
        log.info("Character {} unequipping product {}", characterId, inventoryId);

        CharacterInventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory item not found"));

        inventory.setEquipped(false);
        inventory.setEquippedSince(null);
        inventory.setActiveUntil(null);
        inventoryRepository.save(inventory);

        // Recalcular efectos pasivos
        applyPassiveEffects(characterId);

        return inventoryMapper.toDto(inventory);
    }

    // ========== INVENTARIO ==========
    @Override
    public List<CharacterInventoryDTO> getInventory(Long characterId) {
        List<CharacterInventory> inventoryItems = inventoryRepository.findByCharacterId(characterId);
        List<CharacterInventoryDTO> result = new ArrayList<>();

        for (CharacterInventory item : inventoryItems) {
            // Convertir a DTO básico
            CharacterInventoryDTO dto = inventoryMapper.toDto(item);

            // Cargar el producto completo con efectos
            if (item.getProduct() != null && item.getProduct().getId() != null) {
                // Recargar el producto completo con todas sus relaciones
                Product fullProduct = productRepository.findByIdWithEffects(item.getProduct().getId())
                        .orElse(item.getProduct());
                dto.setProduct(productMapper.toDto(fullProduct));
            }

            result.add(dto);
        }

        return result;
    }

    @Override
    public List<CharacterInventoryDTO> getEquippedInventory(Long characterId) {
        return inventoryRepository.findByCharacterIdAndEquippedTrue(characterId)
                .stream()
                .map(inventoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CharacterInventoryDTO getInventoryItem(Long inventoryId) {
        return inventoryRepository.findById(inventoryId)
                .map(inventoryMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Inventory item not found"));
    }

    // ========== CRAFTING ==========
    @Override
    @Transactional
    public CraftProductResponseDTO craftProduct(CraftProductRequestDTO request) {
        log.info("Character {} crafting product with recipe {}", request.getCharacterId(), request.getRecipeId());

        ProductRecipe recipe = recipeRepository.findById(request.getRecipeId())
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        Product resultProduct = productRepository.findById(recipe.getResultProductId())
                .orElseThrow(() -> new RuntimeException("Result product not found"));

        CharacterDto character = businessTransactions.getPerson(request.getCharacterId());

        // Verificar nivel
        if (character.getLevel() < recipe.getRequiredSkillLevel()) {
            return CraftProductResponseDTO.builder()
                    .success(false)
                    .message(String.format("Necesitas nivel %d para fabricar este objeto", recipe.getRequiredSkillLevel()))
                    .build();
        }

        // Verificar ingredientes
        List<Ingredient> ingredients = recipe.getIngredients();
        List<IngredientConsumedDTO> consumed = new ArrayList<>();

        // PRIMERA PASADA: Verificar que hay suficientes ingredientes
        for (Ingredient ingredient : ingredients) {
            CharacterInventory inventory = inventoryRepository
                    .findByCharacterIdAndProductId(request.getCharacterId(), ingredient.getProductId())
                    .orElse(null);

            int requiredQty = ingredient.getQuantity() * request.getQuantity();
            int availableQty = inventory != null ? inventory.getQuantity() : 0;

            if (availableQty < requiredQty) {
                // Obtener nombre del producto desde el repository
                String productName = productRepository.findById(ingredient.getProductId())
                        .map(Product::getName)
                        .orElse("ingrediente");

                return CraftProductResponseDTO.builder()
                        .success(false)
                        .message(String.format("Te falta %d de %s", requiredQty - availableQty, productName))
                        .build();
            }
        }

        // SEGUNDA PASADA: Consumir ingredientes
        for (Ingredient ingredient : ingredients) {
            CharacterInventory inventory = inventoryRepository
                    .findByCharacterIdAndProductId(request.getCharacterId(), ingredient.getProductId())
                    .orElse(null);

            int requiredQty = ingredient.getQuantity() * request.getQuantity();
            int newQuantity = inventory.getQuantity() - requiredQty;

            // Obtener nombre del producto
            String productName = productRepository.findById(ingredient.getProductId())
                    .map(Product::getName)
                    .orElse("unknown");

            IngredientConsumedDTO consumedDTO = IngredientConsumedDTO.builder()
                    .productId(ingredient.getProductId())
                    .productName(productName)
                    .quantityConsumed(requiredQty)
                    .quantityHad(inventory.getQuantity())
                    .build();
            consumed.add(consumedDTO);

            if (newQuantity <= 0) {
                inventoryRepository.delete(inventory);
            } else {
                inventory.setQuantity(newQuantity);
                inventoryRepository.save(inventory);
            }
        }

        // Añadir producto fabricado
        int craftedQuantity = request.getQuantity() * recipe.getResultQuantity();
        addToInventory(request.getCharacterId(), resultProduct, craftedQuantity);

        // Dar XP
        int currentXpJobs = character.getXpJobs() != null ? character.getXpJobs() : 0;
        character.setXpJobs(currentXpJobs + recipe.getXpReward());
        businessTransactions.updatePerson(character);

        return CraftProductResponseDTO.builder()
                .success(true)
                .message(String.format("Has fabricado %d %s", craftedQuantity, resultProduct.getName()))
                .craftedProduct(productMapper.toDto(resultProduct))
                .quantity(craftedQuantity)
                .xpGained(recipe.getXpReward())
                .ingredientsConsumed(consumed)
                .build();
    }

    // ========== VENTA ==========
    @Override
    @Transactional
    public SellProductResponseDTO sellProduct(Long inventoryId, Long characterId, Integer quantity) {
        log.info("Character {} selling product {}", characterId, inventoryId);

        CharacterInventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory item not found"));

        Product product = inventory.getProduct();

        if (!product.getSellable()) {
            return SellProductResponseDTO.builder()
                    .success(false)
                    .message("Este producto no se puede vender")
                    .build();
        }

        if (quantity > inventory.getQuantity()) {
            return SellProductResponseDTO.builder()
                    .success(false)
                    .message("No tienes suficientes unidades")
                    .build();
        }

        BigDecimal sellPrice = calculateSellPrice(product);
        BigDecimal totalSellPrice = sellPrice.multiply(BigDecimal.valueOf(quantity));

        // Ingresar dinero
        CharacterDto character = businessTransactions.getPerson(characterId);
        SFinanceAccountResponseDto account = getPrimaryAccount(character);
        account.setBalance(account.getBalance().add(totalSellPrice));
        businessTransactions.updatePerson(character);

        // Eliminar del inventario
        int newQuantity = inventory.getQuantity() - quantity;
        if (newQuantity <= 0) {
            inventoryRepository.delete(inventory);
        } else {
            inventory.setQuantity(newQuantity);
            inventoryRepository.save(inventory);
        }

        return SellProductResponseDTO.builder()
                .success(true)
                .message(String.format("Has vendido %d %s por %,.2f€", quantity, product.getName(), totalSellPrice))
                .productId(product.getId())
                .productName(product.getName())
                .quantitySold(quantity)
                .totalPrice(totalSellPrice)
                .remainingQuantity(newQuantity > 0 ? newQuantity : 0)
                .newBalance(account.getBalance())
                .build();
    }

    // ========== OFERTAS ==========
    @Override
    public List<ProductShopOfferDTO> getActiveShopOffers() {
        return shopOfferRepository.findActiveOffers(LocalDateTime.now())
                .stream()
                .map(shopOfferMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BuyProductResponseDTO buyFromOffer(Long characterId, Long offerId, Integer quantity) {
        ProductShopOffer offer = shopOfferRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Offer not found"));

        if (!offer.getActive() || LocalDateTime.now().isBefore(offer.getStartsAt()) ||
                LocalDateTime.now().isAfter(offer.getExpiresAt())) {
            return BuyProductResponseDTO.builder()
                    .success(false)
                    .message("La oferta ya no está disponible")
                    .build();
        }

        if (offer.getStockLimit() != -1 && offer.getRemainingStock() < quantity) {
            return BuyProductResponseDTO.builder()
                    .success(false)
                    .message("Stock insuficiente")
                    .build();
        }

        BuyProductRequestDTO request = BuyProductRequestDTO.builder()
                .characterId(characterId)
                .productId(offer.getProductId())
                .quantity(quantity)
                .build();

        BuyProductResponseDTO response = buyProduct(request);

        if (response.isSuccess()) {
            shopOfferRepository.decrementStock(offerId, quantity);
        }

        return response;
    }

    // ========== EFECTOS PASIVOS ==========
    @Override
    public void applyPassiveEffects(Long characterId) {
        log.info("Applying passive effects for character {}", characterId);

        List<CharacterInventory> equippedItems = inventoryRepository.findByCharacterIdAndEquippedTrue(characterId);
        CharacterDto character = businessTransactions.getPerson(characterId);

        // Resetear bonificaciones temporales
        resetTemporaryBonuses(character);

        for (CharacterInventory item : equippedItems) {
            for (ProductPassiveEffect effect : item.getProduct().getPassiveEffects()) {
                if (shouldApplyEffect(effect)) {
                    applyPassiveEffect(character, effect);
                }
            }
        }

        businessTransactions.updatePerson(character);
    }

    @Override
    public void removePassiveEffects(Long characterId, Long productId) {
        log.info("Removing passive effects for character {} from product {}", characterId, productId);
        applyPassiveEffects(characterId);
    }

    // ========== RESUMEN ==========
    @Override
    public InventorySummaryDTO getInventorySummary(Long characterId) {
        List<CharacterInventory> inventory = inventoryRepository.findByCharacterId(characterId);

        // Cambiar a Map<String, Integer> como espera el DTO
        Map<ProductCategory, Integer> byCategory = new HashMap<>();
        Map<ProductRarityDTO, Integer> byRarity = new HashMap<>();
        Map<ProductSlotDTO, Integer> bySlot = new HashMap<>();

        for (CharacterInventory item : inventory) {
            Product p = item.getProduct();
            byCategory.merge(ProductCategory.valueOf(p.getCategory().name()), item.getQuantity(), Integer::sum);
            byRarity.merge(ProductRarityDTO.valueOf(p.getRarity().name()), item.getQuantity(), Integer::sum);
            bySlot.merge(ProductSlotDTO.valueOf(p.getSlot().name()), item.getQuantity(), Integer::sum);
        }

        return InventorySummaryDTO.builder()
                .characterId(characterId)
                .totalItems(inventory.stream().mapToInt(CharacterInventory::getQuantity).sum())
                .uniqueItems((int) inventory.stream().map(i -> i.getProduct().getId()).distinct().count())
                .equippedItems((int) inventory.stream().filter(CharacterInventory::getEquipped).count())
                .itemsByCategory(byCategory)
                .itemsByRarity(byRarity)
                .itemsBySlot(bySlot)
                .recentAcquisitions(inventory.stream()
                        .sorted((a, b) -> b.getAcquiredAt().compareTo(a.getAcquiredAt()))
                        .limit(5)
                        .map(inventoryMapper::toDto)
                        .collect(Collectors.toList()))
                .equipped(inventory.stream()
                        .filter(CharacterInventory::getEquipped)
                        .map(inventoryMapper::toDto)
                        .collect(Collectors.toList()))
                .build();
    }

    // ========== MÉTODOS PRIVADOS ==========

    private BigDecimal calculateFinalPrice(Product product) {
        LocalDateTime now = LocalDateTime.now();
        if (product.getDiscountPrice() != null &&
                product.getDiscountStartDate() != null &&
                product.getDiscountEndDate() != null &&
                now.toLocalDate().isAfter(product.getDiscountStartDate()) &&
                now.toLocalDate().isBefore(product.getDiscountEndDate())) {
            return product.getDiscountPrice();
        }
        return product.getPrice();
    }

    private BigDecimal calculateSellPrice(Product product) {
        return product.getPrice().multiply(BigDecimal.valueOf(0.5));
    }

    private SFinanceAccountResponseDto getPrimaryAccount(CharacterDto character) {
        if (character.getAccounts() == null || character.getAccounts().isEmpty()) {
            throw new RuntimeException("Character has no bank account");
        }
        return character.getAccounts().get(0);
    }

    private CharacterInventory addToInventory(Long characterId, Product product, Integer quantity) {
        CharacterInventory inventory = inventoryRepository
                .findByCharacterIdAndProductId(characterId, product.getId())
                .orElse(null);

        if (inventory != null && product.getStackable()) {
            inventory.setQuantity(inventory.getQuantity() + quantity);
            return inventoryRepository.save(inventory);
        } else {
            inventory = new CharacterInventory();
            inventory.setCharacterId(characterId);
            inventory.setProduct(product);
            inventory.setQuantity(quantity);
            inventory.setDurabilityLeft(product.getDurability());
            return inventoryRepository.save(inventory);
        }
    }

    private StatChangeDTO applyInstantEffect(CharacterDto character, ProductInstantEffect effect, Integer multiplier) {
        // ✅ Manejar valores null
        boolean isRandom = effect.getIsRandom() != null && effect.getIsRandom();
        boolean isPercentage = effect.getIsPercentage() != null && effect.getIsPercentage();

        int value;
        if (isRandom) {
            int min = effect.getMinAmount() != null ? effect.getMinAmount() : 0;
            int max = effect.getMaxAmount() != null ? effect.getMaxAmount() : 10;
            value = new Random().nextInt(max - min + 1) + min;
        } else {
            value = effect.getAmount() != null ? effect.getAmount() : 0;
        }

        int multipliedValue = value * (multiplier != null ? multiplier : 1);
        int oldValue = 0;
        int newValue = 0;

        switch (effect.getType()) {
            case HEALTH_RESTORE:
                oldValue = character.getStats().getHealth();
                newValue = clamp(oldValue + multipliedValue, 0, 100);
                character.getStats().setHealth(newValue);
                break;
            case ENERGY_RESTORE:
                oldValue = character.getStats().getEnergy();
                newValue = clamp(oldValue + multipliedValue, 0, 100);
                character.getStats().setEnergy(newValue);
                break;
            case STRESS_REDUCTION:
                oldValue = character.getStats().getStress();
                newValue = clamp(oldValue - multipliedValue, 0, 100);
                character.getStats().setStress(newValue);
                break;
            case INTELLIGENCE_BOOST:
                oldValue = character.getStats().getIntelligence();
                newValue = oldValue + multipliedValue;
                character.getStats().setIntelligence(newValue);
                break;
            case CHARISMA_BOOST:
                oldValue = character.getStats().getCharisma();
                newValue = oldValue + multipliedValue;
                character.getStats().setCharisma(newValue);
                break;
            case CREATIVITY_BOOST:
                oldValue = character.getStats().getCreativity();
                newValue = oldValue + multipliedValue;
                character.getStats().setCreativity(newValue);
                break;
            case RESILIENCE_BOOST:
                oldValue = character.getStats().getResilience();
                newValue = oldValue + multipliedValue;
                character.getStats().setResilience(newValue);
                break;
            case HAPPINESS_BOOST:
                oldValue = character.getStats().getHappiness();
                newValue = clamp(oldValue + multipliedValue, 0, 100);
                character.getStats().setHappiness(newValue);
                break;
            case XP_ACADEMY_BOOST:
            case XP_JOB_BOOST:
                // XP se maneja aparte
                break;
            default:
                return null;
        }

        return StatChangeDTO.builder()
                .statName(effect.getType() != null ? effect.getType().name() : "UNKNOWN")
                .oldValue(oldValue)
                .newValue(newValue)
                .change(newValue - oldValue)
                .description(String.format("%s%d%s",
                        newValue > oldValue ? "+" : "",
                        Math.abs(newValue - oldValue),
                        isPercentage ? "%" : ""))
                .build();
    }
    private void applyPassiveEffect(CharacterDto character, ProductPassiveEffect effect) {
        int value = effect.getValue();
        if (effect.getIsPercentage()) {
            value = value * character.getLevel() / 100;
        }

        switch (effect.getStat().toUpperCase()) {
            case "ENERGY":
                character.getStats().setEnergy(clamp(character.getStats().getEnergy() + value, 0, 100));
                break;
            case "HEALTH":
                character.getStats().setHealth(clamp(character.getStats().getHealth() + value, 0, 100));
                break;
            case "HAPPINESS":
                character.getStats().setHappiness(clamp(character.getStats().getHappiness() + value, 0, 100));
                break;
            case "INTELLIGENCE":
                character.getStats().setIntelligence(character.getStats().getIntelligence() + value);
                break;
            case "CHARISMA":
                character.getStats().setCharisma(character.getStats().getCharisma() + value);
                break;
            case "CREATIVITY":
                character.getStats().setCreativity(character.getStats().getCreativity() + value);
                break;
            case "RESILIENCE":
                character.getStats().setResilience(character.getStats().getResilience() + value);
                break;
        }
    }

    private boolean shouldApplyEffect(ProductPassiveEffect effect) {
        if (effect.getTrigger() != TriggerCondition.ALWAYS) {
            // Aquí iría lógica según el trigger (hora del día, día de semana, etc.)
            return true;
        }

        if (effect.getChancePercentage() != null && effect.getChancePercentage() < 100) {
            return new Random().nextInt(100) < effect.getChancePercentage();
        }

        return true;
    }

    private void resetTemporaryBonuses(CharacterDto character) {
        // Resetear bonificaciones temporales
        // Esto dependerá de cómo manejes los buffs temporales
    }

    private void createExpense(CharacterDto character, Product product, BigDecimal amount) {
        SExpenseResponseDto expense = new SExpenseResponseDto();
        expense.setActive(true);
        expense.setCategory(toExpenseCategory(product.getCategory()));
        expense.setConcept(product.getName());
        expense.setFrequency(EnumAll.Frequency.OTHER);
        expense.setAmount(amount);
        expense.setStartDate(java.time.LocalDate.now());
        expense.setExternalRefId(product.getId());
        expense.setExternalRefType("PRODUCT");
        expense.setEssential(false);

        SFinanceAccountResponseDto account = getPrimaryAccount(character);
        businessTransactions.setExpense(expense, account.getId());
    }

    private void sendPurchaseNotification(CharacterDto character, Product product, Integer quantity, BigDecimal totalPrice) {
        NotificationDTO notification = new NotificationDTO();
        notification.setUserId(character.getId());
        notification.setFromUserId(character.getUid());
        notification.setType(NotificationType.NEW_CONTENT);
        notification.setPriority(NotificationPriority.MEDIUM);
        notification.setTitle("🛒 ¡Compraste un producto!");
        notification.setSubTitle(product.getName());
        notification.setMessage(String.format("Has comprado %d %s por %,.2f€", quantity, product.getName(), totalPrice));
        notification.setResourceType(NotificationResourceType.PRODUCT);
        notification.setEventType(NotificationEventType.NEW_PRODUCT);
        notification.setResourceId(product.getId());
        notification.setActionUrl("/products/" + product.getId());
        notification.setRead(false);

        businessTransactions.setNotifications(notification);
    }

    private UseProductResponseDTO createErrorResponse(String message) {
        return UseProductResponseDTO.builder()
                .success(false)
                .message(message)
                .build();
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private EnumAll.ExpenseCategory toExpenseCategory(ProductCategory category) {
        if (category == null) return EnumAll.ExpenseCategory.OTHER;
        return switch (category) {
            case ALIMENTACION -> EnumAll.ExpenseCategory.FOOD;
            case VIVIENDA -> EnumAll.ExpenseCategory.HOUSING;
            case TRANSPORTE -> EnumAll.ExpenseCategory.TRANSPORT;
            case SALUD -> EnumAll.ExpenseCategory.HEALTH;
            case ENTRETENIMIENTO -> EnumAll.ExpenseCategory.LEISURE;
            case TECNOLOGIA -> EnumAll.ExpenseCategory.OTHER;
            case SOCIAL -> EnumAll.ExpenseCategory.LEISURE;
            default -> EnumAll.ExpenseCategory.OTHER;
        };
    }

    // ============================================================
// SECCIÓN 9: PRODUCTOS DISPONIBLES (BÚSQUEDA AVANZADA)
// ============================================================
    @Override
    public Page<CharacterInventoryDTO> getIventoryForCharacter(
            Long characterId,
            Pageable pageable,
            String search,
            ProductCategory category,
            String rarity,
            String slot,
            Integer minLevel,
            Integer maxLevel,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean isConsumable,
            Boolean isEventItem,
            String season) {

        log.info("Buscando inventario para personaje: {} con filtros - search: {}, category: {}, rarity: {}, slot: {}, minLevel: {}, maxLevel: {}, minPrice: {}, maxPrice: {}, isConsumable: {}, isEventItem: {}, season: {}",
                characterId, search, category, rarity, slot, minLevel, maxLevel, minPrice, maxPrice, isConsumable, isEventItem, season);

        // Obtener el nivel del personaje para filtrar productos que puede comprar
        CharacterDto character = businessTransactions.getPerson(characterId);
        Integer characterLevel = character.getLevel() != null ? character.getLevel() : 1;

        // Convertir rarity de String a ProductRarity si viene
        ProductRarity productRarity = null;
        if (rarity != null && !rarity.isEmpty()) {
            try {
                productRarity = ProductRarity.valueOf(rarity.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.warn("Rareza no válida: {}", rarity);
            }
        }

        // Convertir slot de String a ProductSlot si viene
        ProductSlot productSlot = null;
        if (slot != null && !slot.isEmpty()) {
            try {
                productSlot = ProductSlot.valueOf(slot.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.warn("Ranura no válida: {}", slot);
            }
        }

        // Si no se especifica nivel máximo, usar el nivel del personaje como límite superior
        Integer effectiveMaxLevel = maxLevel != null ? maxLevel : characterLevel;

        // Si no se especifica nivel mínimo, usar 1
        Integer effectiveMinLevel = minLevel != null ? minLevel : 1;

        // Construir la consulta con los filtros - CORREGIDO
        Page<CharacterInventory> inventoryPage = inventoryRepository.findInventoryByCharacterIdAndFilters(
                characterId,
                search,
                category,
                productRarity,
                productSlot,
                effectiveMinLevel,
                effectiveMaxLevel,
                null, // equipped - puedes añadirlo después si lo necesitas
                pageable
        );

        // Convertir Page<CharacterInventory> a Page<CharacterInventoryDTO>
        return inventoryPage.map(inventory -> {
            CharacterInventoryDTO dto = inventoryMapper.toDto(inventory);

            // Cargar el producto completo con efectos si es necesario
            if (inventory.getProduct() != null && inventory.getProduct().getId() != null) {
                Product fullProduct = productRepository.findById(inventory.getProduct().getId()).orElse(inventory.getProduct());
                dto.setProduct(productMapper.toDto(fullProduct));
            }

            return dto;
        });
    }
    @Override
    public Page<ProductDTO> getAvailableProductsForCharacter(
            Long characterId,
            Pageable pageable,
            String search,
            ProductCategory category,
            String rarity,
            String slot,
            Integer minLevel,
            Integer maxLevel,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean isConsumable,
            Boolean isEventItem,
            String season) {

        log.info("Buscando productos disponibles para personaje: {} con filtros - search: {}, category: {}, rarity: {}, slot: {}, minLevel: {}, maxLevel: {}, minPrice: {}, maxPrice: {}, isConsumable: {}, isEventItem: {}, season: {}",
                characterId, search, category, rarity, slot, minLevel, maxLevel, minPrice, maxPrice, isConsumable, isEventItem, season);

        // Obtener el nivel del personaje para filtrar productos que puede comprar
        CharacterDto character = businessTransactions.getPerson(characterId);
        Integer characterLevel = character.getLevel() != null ? character.getLevel() : 1;

        // Convertir rarity de String a ProductRarity si viene
        ProductRarity productRarity = null;
        if (rarity != null && !rarity.isEmpty()) {
            try {
                productRarity = ProductRarity.valueOf(rarity.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.warn("Rareza no válida: {}", rarity);
            }
        }

        // Convertir slot de String a ProductSlot si viene
        ProductSlot productSlot = null;
        if (slot != null && !slot.isEmpty()) {
            try {
                productSlot = ProductSlot.valueOf(slot.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.warn("Ranura no válida: {}", slot);
            }
        }

        // Si no se especifica nivel máximo, usar el nivel del personaje como límite superior
        Integer effectiveMaxLevel = maxLevel != null ? maxLevel : characterLevel;

        // Si no se especifica nivel mínimo, usar 1
        Integer effectiveMinLevel = minLevel != null ? minLevel : 1;

        // Construir la consulta con los filtros
        Page<Product> productsPage = productRepository.findAvailableProductsForCharacter(
                search,
                category,
                productRarity,
                productSlot,
                effectiveMinLevel,
                effectiveMaxLevel,
                minPrice,
                maxPrice,
                isConsumable,
                isEventItem,
                season,
                pageable
        );

        // Convertir a DTO sin efectos (para la lista, no necesitamos los efectos)
        return productsPage.map(product -> {
            ProductDTO dto = productMapper.toDto(product);
            // Limpiar efectos para la lista (opcional, mejora rendimiento)
            dto.setInstantEffects(null);
            dto.setPassiveEffects(null);
            return dto;
        });
    }
}