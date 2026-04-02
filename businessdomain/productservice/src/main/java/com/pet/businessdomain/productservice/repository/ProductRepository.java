package com.pet.businessdomain.productservice.repository;

import com.pet.businessdomain.productservice.entities.Product;
import com.pet.businessdomain.shareddto.enumentities.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByActiveTrue();

    List<Product> findByCategory(ProductCategory category);

    List<Product> findByCategoryAndActiveTrue(ProductCategory category);

    boolean existsByName(String name);

    List<Product> findByName(String name);
}
