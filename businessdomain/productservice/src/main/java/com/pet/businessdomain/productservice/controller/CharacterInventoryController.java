package com.pet.businessdomain.productservice.controller;

import com.pet.businessdomain.productservice.entities.CharacterInventory;
import com.pet.businessdomain.productservice.services.CharacterInventoryService;
import com.pet.businessdomain.shareddto.dto.products.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestionar el inventario de productos de los personajes.
 *
 * <p>Este controlador proporciona endpoints para:
 * <ul>
 *   <li>Comprar productos (tienda)</li>
 *   <li>Usar productos consumibles o reutilizables</li>
 *   <li>Equipar y desequipar productos</li>
 *   <li>Consultar el inventario del personaje</li>
 *   <li>Fabricar productos mediante crafting</li>
 *   <li>Vender productos del inventario</li>
 *   <li>Gestionar ofertas especiales de la tienda</li>
 *   <li>Aplicar y eliminar efectos pasivos de productos equipados</li>
 * </ul>
 * </p>
 *
 * @author Life Manager Team
 * @version 1.0
 * @since 2026-04-27
 */
@Slf4j
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class CharacterInventoryController {
    private static final int DEFAULT_PAGE_SIZE = 10;

    private final CharacterInventoryService inventoryService;

    // ============================================================
    // SECCIÓN 1: COMPRA DE PRODUCTOS
    // ============================================================

    /**
     * Compra un producto enviando los datos en el cuerpo de la petición.
     *
     * <p>Este endpoint es útil cuando se necesita enviar datos adicionales
     * como la cantidad o información de pago.</p>
     *
     * @param request Objeto con los datos de la compra (characterId, productId, quantity)
     * @return ResponseEntity con el resultado de la compra incluyendo el estado, mensaje y datos del producto adquirido
     *
     * @example Request Body:
     * {
     *   "characterId": 1,
     *   "productId": 10,
     *   "quantity": 2
     * }
     */
    @PostMapping("/buy")
    public ResponseEntity<BuyProductResponseDTO> buyProduct(@RequestBody BuyProductRequestDTO request) {
        log.info("POST /api/inventory/buy - characterId: {}, productId: {}",
                request.getCharacterId(), request.getProductId());
        BuyProductResponseDTO response = inventoryService.buyProduct(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Versión simplificada de compra usando parámetros en la URL.
     *
     * <p>Ideal para integraciones rápidas o cuando se prefiere usar path variables.</p>
     *
     * @param characterId ID del personaje que realiza la compra
     * @param productId ID del producto a comprar
     * @param quantity Cantidad de unidades a comprar (por defecto 1)
     * @return ResponseEntity con el resultado de la compra
     */
    @PostMapping("/buy/{characterId}/{productId}")
    public ResponseEntity<BuyProductResponseDTO> buyProductSimple(
            @PathVariable(name = "characterId") Long characterId,
            @PathVariable(name = "productId") Long productId,
            @RequestParam(name = "quantity", defaultValue = "1") Integer quantity) {

        BuyProductRequestDTO request = BuyProductRequestDTO.builder()
                .characterId(characterId)
                .productId(productId)
                .quantity(quantity)
                .build();

        BuyProductResponseDTO response = inventoryService.buyProduct(request);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // SECCIÓN 2: USO DE PRODUCTOS
    // ============================================================

    /**
     * Usa un producto del inventario del personaje.
     *
     * <p>Los efectos del producto se aplican automáticamente y pueden incluir:
     * restauración de salud, energía, reducción de estrés, aumento de XP, etc.</p>
     *
     * @param request Objeto con los datos del uso (characterId, inventoryId o productId, quantity)
     * @return ResponseEntity con el resultado del uso incluyendo cambios en estadísticas y XP ganado
     */
    @PostMapping("/use")
    public ResponseEntity<UseProductResponseDTO> useProduct(@RequestBody UseProductRequestDTO request) {
        log.info("POST /api/inventory/use - characterId: {}, inventoryId: {}",
                request.getCharacterId(), request.getInventoryId());
        UseProductResponseDTO response = inventoryService.useProduct(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Versión simplificada para usar un producto mediante parámetros en URL.
     *
     * @param characterId ID del personaje que usa el producto
     * @param inventoryId ID del item en el inventario
     * @param quantity Cantidad a usar (por defecto 1)
     * @return ResponseEntity con el resultado del uso
     */
    @PostMapping("/use/{characterId}/{inventoryId}")
    public ResponseEntity<UseProductResponseDTO> useProductSimple(
            @PathVariable(name = "characterId") Long characterId,
            @PathVariable(name = "inventoryId") Long inventoryId,
            @RequestParam(name = "quantity", defaultValue = "1") Integer quantity) {

        UseProductRequestDTO request = UseProductRequestDTO.builder()
                .characterId(characterId)
                .inventoryId(inventoryId)
                .quantity(quantity)
                .build();

        UseProductResponseDTO response = inventoryService.useProduct(request);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // SECCIÓN 3: EQUIPAR Y DESEQUIPAR PRODUCTOS
    // ============================================================

    /**
     * Equipa un producto en el personaje.
     *
     * <p>Los productos equipables (como vehículos, herramientas o accesorios)
     * activan efectos pasivos mientras están equipados. Al equipar un producto
     * en la misma ranura, se desequipa automáticamente el anterior.</p>
     *
     * @param inventoryId ID del item en inventario a equipar
     * @param characterId ID del personaje
     * @return ResponseEntity con el item actualizado (equipado = true)
     */
    @PostMapping("/equip/{inventoryId}/{characterId}")
    public ResponseEntity<CharacterInventoryDTO> equipProduct(
            @PathVariable(name = "inventoryId") Long inventoryId,
            @PathVariable(name = "characterId") Long characterId) {
        log.info("POST /api/inventory/equip/{}/{}", inventoryId, characterId);
        CharacterInventoryDTO response = inventoryService.equipProduct(inventoryId, characterId);
        return ResponseEntity.ok(response);
    }

    /**
     * Desequipa un producto del personaje.
     *
     * <p>Al desequipar, los efectos pasivos del producto dejan de aplicarse.</p>
     *
     * @param inventoryId ID del item en inventario a desequipar
     * @param characterId ID del personaje
     * @return ResponseEntity con el item actualizado (equipado = false)
     */
    @PostMapping("/unequip/{inventoryId}/{characterId}")
    public ResponseEntity<CharacterInventoryDTO> unequipProduct(
            @PathVariable(name = "inventoryId") Long inventoryId,
            @PathVariable(name = "characterId") Long characterId) {
        log.info("POST /api/inventory/unequip/{}/{}", inventoryId, characterId);
        CharacterInventoryDTO response = inventoryService.unequipProduct(inventoryId, characterId);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // SECCIÓN 4: CONSULTA DE INVENTARIO
    // ============================================================

    /**
     * Obtiene todo el inventario de un personaje.
     *
     * <p>Retorna la lista completa de productos que posee el personaje,
     * incluyendo cantidad, estado de equipamiento, durabilidad, etc.</p>
     *
     * @param characterId ID del personaje
     * @return ResponseEntity con la lista de items del inventario
     */
    @GetMapping("/{characterId}")
    public ResponseEntity<List<CharacterInventoryDTO>> getInventory(
            @PathVariable(name = "characterId") Long characterId) {
        log.info("GET /api/inventory/{}", characterId);
        List<CharacterInventoryDTO> inventory = inventoryService.getInventory(characterId);
        return ResponseEntity.ok(inventory);
    }

    /**
     * Obtiene únicamente los productos que el personaje tiene equipados.
     *
     * @param characterId ID del personaje
     * @return ResponseEntity con la lista de items equipados
     */
    @GetMapping("/{characterId}/equipped")
    public ResponseEntity<List<CharacterInventoryDTO>> getEquippedInventory(
            @PathVariable(name = "characterId") Long characterId) {
        log.info("GET /api/inventory/{}/equipped", characterId);
        List<CharacterInventoryDTO> equipped = inventoryService.getEquippedInventory(characterId);
        return ResponseEntity.ok(equipped);
    }

    /**
     * Obtiene un item específico del inventario por su ID.
     *
     * @param inventoryId ID del item en inventario
     * @return ResponseEntity con el item solicitado
     */
    @GetMapping("/item/{inventoryId}")
    public ResponseEntity<CharacterInventoryDTO> getInventoryItem(
            @PathVariable(name = "inventoryId") Long inventoryId) {
        log.info("GET /api/inventory/item/{}", inventoryId);
        CharacterInventoryDTO item = inventoryService.getInventoryItem(inventoryId);
        return ResponseEntity.ok(item);
    }

    /**
     * Obtiene un resumen estadístico del inventario del personaje.
     *
     * <p>Incluye: total de items, items únicos, items equipados,
     * distribución por categoría, rareza y ranura, adquisiciones recientes, etc.</p>
     *
     * @param characterId ID del personaje
     * @return ResponseEntity con el resumen del inventario
     */
    @GetMapping("/{characterId}/summary")
    public ResponseEntity<InventorySummaryDTO> getInventorySummary(
            @PathVariable(name = "characterId") Long characterId) {
        log.info("GET /api/inventory/{}/summary", characterId);
        InventorySummaryDTO summary = inventoryService.getInventorySummary(characterId);
        return ResponseEntity.ok(summary);
    }

    // ============================================================
    // SECCIÓN 5: CRAFTING (FABRICACIÓN)
    // ============================================================

    /**
     * Fabrica un producto a partir de una receta.
     *
     * <p>El personaje debe tener los ingredientes necesarios en su inventario.
     * Al fabricar, se consumen los ingredientes y se añade el producto resultante.</p>
     *
     * @param request Objeto con los datos del crafting (characterId, recipeId, quantity)
     * @return ResponseEntity con el resultado del crafting y los ingredientes consumidos
     */
    @PostMapping("/craft")
    public ResponseEntity<CraftProductResponseDTO> craftProduct(@RequestBody CraftProductRequestDTO request) {
        log.info("POST /api/inventory/craft - characterId: {}, recipeId: {}",
                request.getCharacterId(), request.getRecipeId());
        CraftProductResponseDTO response = inventoryService.craftProduct(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Versión simplificada de crafting usando parámetros en URL.
     *
     * @param characterId ID del personaje que fabrica
     * @param recipeId ID de la receta a utilizar
     * @param quantity Cantidad a fabricar (por defecto 1)
     * @return ResponseEntity con el resultado del crafting
     */
    @PostMapping("/craft/{characterId}/{recipeId}")
    public ResponseEntity<CraftProductResponseDTO> craftProductSimple(
            @PathVariable(name = "characterId") Long characterId,
            @PathVariable(name = "recipeId") Long recipeId,
            @RequestParam(name = "quantity", defaultValue = "1") Integer quantity) {

        CraftProductRequestDTO request = CraftProductRequestDTO.builder()
                .characterId(characterId)
                .recipeId(recipeId)
                .quantity(quantity)
                .build();

        CraftProductResponseDTO response = inventoryService.craftProduct(request);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // SECCIÓN 6: VENTA DE PRODUCTOS
    // ============================================================

    /**
     * Vende un producto del inventario.
     *
     * <p>El producto se elimina del inventario y el personaje recibe
     * la mitad de su precio original como compensación.</p>
     *
     * @param inventoryId ID del item en inventario a vender
     * @param characterId ID del personaje
     * @param quantity Cantidad a vender (por defecto 1)
     * @return ResponseEntity con el resultado de la venta y el saldo actualizado
     */
    @PostMapping("/sell/{inventoryId}/{characterId}")
    public ResponseEntity<SellProductResponseDTO> sellProduct(
            @PathVariable(name = "inventoryId") Long inventoryId,
            @PathVariable(name = "characterId") Long characterId,
            @RequestParam(name = "quantity", defaultValue = "1") Integer quantity) {
        log.info("POST /api/inventory/sell/{}/{} - quantity: {}", inventoryId, characterId, quantity);
        SellProductResponseDTO response = inventoryService.sellProduct(inventoryId, characterId, quantity);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // SECCIÓN 7: OFERTAS ESPECIALES
    // ============================================================

    /**
     * Obtiene todas las ofertas activas de la tienda.
     *
     * <p>Las ofertas pueden incluir descuentos, ofertas flash o paquetes especiales.</p>
     *
     * @return ResponseEntity con la lista de ofertas activas
     */
    @GetMapping("/offers/active")
    public ResponseEntity<List<ProductShopOfferDTO>> getActiveShopOffers() {
        log.info("GET /api/inventory/offers/active");
        List<ProductShopOfferDTO> offers = inventoryService.getActiveShopOffers();
        return ResponseEntity.ok(offers);
    }

    /**
     * Compra un producto directamente desde una oferta especial.
     *
     * <p>Aplica automáticamente el precio de oferta y descuenta del stock disponible.</p>
     *
     * @param characterId ID del personaje
     * @param offerId ID de la oferta
     * @param quantity Cantidad a comprar (por defecto 1)
     * @return ResponseEntity con el resultado de la compra
     */
    @PostMapping("/offers/buy/{characterId}/{offerId}")
    public ResponseEntity<BuyProductResponseDTO> buyFromOffer(
            @PathVariable(name = "characterId") Long characterId,
            @PathVariable(name = "offerId") Long offerId,
            @RequestParam(name = "quantity", defaultValue = "1") Integer quantity) {
        log.info("POST /api/inventory/offers/buy/{}/{} - quantity: {}", characterId, offerId, quantity);
        BuyProductResponseDTO response = inventoryService.buyFromOffer(characterId, offerId, quantity);
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // SECCIÓN 8: EFECTOS PASIVOS
    // ============================================================

    /**
     * Aplica todos los efectos pasivos de los productos equipados.
     *
     * <p>Este método se ejecuta automáticamente al equipar/desequipar productos
     * y al iniciar el día, pero también puede ser invocado manualmente.</p>
     *
     * @param characterId ID del personaje
     * @return ResponseEntity sin contenido (operación exitosa)
     */
    @PostMapping("/passive-effects/{characterId}/apply")
    public ResponseEntity<Void> applyPassiveEffects(
            @PathVariable(name = "characterId") Long characterId) {
        log.info("POST /api/inventory/passive-effects/{}/apply", characterId);
        inventoryService.applyPassiveEffects(characterId);
        return ResponseEntity.ok().build();
    }

    /**
     * Elimina los efectos pasivos de un producto específico.
     *
     * <p>Útil cuando un producto caduca o el personaje desea dejar de recibir sus beneficios.</p>
     *
     * @param characterId ID del personaje
     * @param productId ID del producto cuyos efectos se eliminarán
     * @return ResponseEntity sin contenido (operación exitosa)
     */
    @DeleteMapping("/passive-effects/{characterId}/{productId}")
    public ResponseEntity<Void> removePassiveEffects(
            @PathVariable(name = "characterId") Long characterId,
            @PathVariable(name = "productId") Long productId) {
        log.info("DELETE /api/inventory/passive-effects/{}/{}", characterId, productId);
        inventoryService.removePassiveEffects(characterId, productId);
        return ResponseEntity.ok().build();
    }

    // ============================================================
// SECCIÓN 9: PRODUCTOS DISPONIBLES (BÚSQUEDA AVANZADA)
// ============================================================

    /**
     * Obtiene los productos disponibles para un personaje con filtros avanzados y paginación.
     *
     * <p>Este endpoint permite buscar productos con múltiples criterios de filtrado:
     * <ul>
     *   <li>Búsqueda por texto en nombre o descripción</li>
     *   <li>Filtro por categoría (TRANSPORTE, SALUD, ALIMENTACION, etc.)</li>
     *   <li>Filtro por rareza (COMMON, RARE, EPIC, LEGENDARY, MYTHIC)</li>
     *   <li>Filtro por ranura (VEHICLE, TOOL, HEAD, BODY, etc.)</li>
     *   <li>Filtro por rango de nivel requerido</li>
     *   <li>Filtro por rango de precio</li>
     *   <li>Filtro por tipo de uso (CONSUMABLE, REUSABLE, SUBSCRIPTION)</li>
     *   <li>Filtro por temporada (SPRING, SUMMER, AUTUMN, WINTER)</li>
     *   <li>Filtro por productos de evento</li>
     *   <li>Ordenamiento por diferentes campos (name, price, requiredLevel, etc.)</li>
     * </ul>
     * </p>
     *
     * @param characterId ID del personaje (para filtrar productos que puede comprar según su nivel)
     * @param filters Objeto con los filtros de búsqueda, paginación y ordenamiento
     * @return ResponseEntity con la página de productos disponibles y metadatos de paginación
     *
     * @example Request Body:
     * {
     *   "search": "bici",
     *   "category": "TRANSPORTE",
     *   "rarity": "RARE",
     *   "minPrice": 100,
     *   "maxPrice": 1000,
     *   "page": 0,
     *   "size": 10,
     *   "sortBy": "price",
     *   "sortDir": "asc"
     * }
     */
    @PostMapping("/character/{characterId}/search")
    public ResponseEntity<Map<String, Object>> getInventoryByFilters(
            @PathVariable(name = "characterId") Long characterId,
            @RequestBody ProductSearchFiltersDTO filters) {

        log.info("Obteniendo inventario para personaje: {} con filtros: {}", characterId, filters);


        // Mapear campos de ordenamiento
        String sortByEntity = mapInventorySortField(filters.getSortBy() != null ? filters.getSortBy() : "acquiredAt");

        // Crear Sort y Pageable
        Sort sort = Sort.by(filters.getSortDir() != null && filters.getSortDir().equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC, sortByEntity);

        Pageable pageable = PageRequest.of(
                filters.getPage() != null ? filters.getPage() : 0,
                filters.getSize() != null ? filters.getSize() : DEFAULT_PAGE_SIZE,
                sort);

        // Llamar al service con todos los filtros
        Page<CharacterInventoryDTO> inventoryPage = inventoryService.getIventoryForCharacter(
                characterId,
                pageable,
                filters.getSearch(),
                filters.getCategory(),
                filters.getRarity(),
                filters.getSlot(),
                filters.getMinLevel(),
                filters.getMaxLevel(),
                filters.getMinPrice(),
                filters.getMaxPrice(),
                filters.getIsConsumable(),
                filters.getIsEventItem(),
                filters.getSeason()
        );

        if (inventoryPage.isEmpty()) {
            return ResponseEntity.ok().build();
        }

        // Construir respuesta con metadata
        Map<String, Object> response = new HashMap<>();
        response.put("items", inventoryPage.getContent());
        response.put("currentPage", inventoryPage.getNumber());
        response.put("totalItems", inventoryPage.getTotalElements());
        response.put("totalPages", inventoryPage.getTotalPages());
        response.put("pageSize", inventoryPage.getSize());

        return ResponseEntity.ok(response);
    }

    /**
     * Mapea el campo de ordenamiento recibido al nombre del campo en la entidad CharacterInventory.
     *
     * @param sortBy Campo por el que se quiere ordenar
     * @return Nombre del campo correspondiente en la entidad CharacterInventory
     */
    private String mapInventorySortField(String sortBy) {
        if (sortBy == null || sortBy.isEmpty()) return "acquiredAt";

        return switch (sortBy) {
            case "acquiredAt" -> "acquiredAt";
            case "quantity" -> "quantity";
            case "equipped" -> "equipped";
            case "product.name" -> "product.name";
            case "product.price" -> "product.price";
            case "product.rarity" -> "product.rarity";
            case "product.category" -> "product.category";
            default -> "acquiredAt";
        };
    }

    /**
     * Mapea el campo de ordenamiento recibido al nombre del campo en la entidad Product.
     *
     * @param sortBy Campo por el que se quiere ordenar
     * @return Nombre del campo correspondiente en la entidad Product
     */
    private String mapProductSortField(String sortBy) {
        if (sortBy == null || sortBy.isEmpty()) return "name";

        return switch (sortBy) {
            case "name" -> "name";
            case "price" -> "price";
            case "requiredLevel" -> "requiredLevel";
            case "cooldownHours" -> "cooldownHours";
            case "durability" -> "durability";
            case "category" -> "category";
            case "rarity" -> "rarity";
            case "discountPrice" -> "discountPrice";
            case "id" -> "id";
            default -> {
                log.warn("Ordenamiento no válido para productos: {}. Usando ordenamiento por nombre.", sortBy);
                yield "name";
            }
        };
    }
}