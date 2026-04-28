package com.pet.businessdomain.shareddto.dto.products;

public enum ProductRarityDTO {
    COMMON(1.0, "Común", "#9e9e9e"),
    UNCOMMON(1.25, "Poco común", "#4caf50"),
    RARE(1.5, "Raro", "#2196f3"),
    EPIC(2.0, "Épico", "#9c27b0"),
    LEGENDARY(2.5, "Legendario", "#ff9800"),
    MYTHIC(3.0, "Mítico", "#f44336");

    private final double multiplier;
    private final String displayName;
    private final String color;

    ProductRarityDTO(double multiplier, String displayName, String color) {
        this.multiplier = multiplier;
        this.displayName = displayName;
        this.color = color;
    }

    public double getMultiplier() { return multiplier; }
    public String getDisplayName() { return displayName; }
    public String getColor() { return color; }
}
