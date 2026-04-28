package com.pet.businessdomain.shareddto.dto.products;

public enum ProductSlotDTO {
    HEAD("Cabeza"),
    BODY("Cuerpo"),
    LEGS("Piernas"),
    FEET("Pies"),
    ACCESSORY("Accesorio"),
    VEHICLE("Vehículo"),
    TOOL("Herramienta"),
    NONE("Sin ranura");

    private final String displayName;

    ProductSlotDTO(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() { return displayName; }
}
