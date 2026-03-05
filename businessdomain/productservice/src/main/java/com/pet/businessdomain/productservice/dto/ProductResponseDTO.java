package com.pet.businessdomain.productservice.dto;

import com.pet.businessdomain.productservice.entities.enumentities.ProductCategory;
import lombok.Data;

import java.util.List;

@Data
public class ProductResponseDTO {

    private Long id;
    private String name;
    private String description;
    private String img;
    private Integer price;
    private ProductCategory category;
    private Boolean active;
    private List<ProductEffectDTO> effects;
}
