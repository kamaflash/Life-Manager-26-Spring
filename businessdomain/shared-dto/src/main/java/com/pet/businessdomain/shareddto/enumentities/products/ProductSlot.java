package com.pet.businessdomain.shareddto.enumentities.products;

public enum ProductSlot {
    HEAD("Cabeza"),
    BODY("Cuerpo"),
    LEGS("Piernas"),
    FEET("Pies"),
    ACCESSORY("Accesorio"),
    VEHICLE("Vehículo"),
    TOOL("Herramienta"),
    NONE("Sin ranura");

    private final String displayName;

    ProductSlot(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() { return displayName; }
}
