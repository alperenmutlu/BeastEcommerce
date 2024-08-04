package com.alperendev.MrBeastECommerce.repository;

import com.alperendev.MrBeastECommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByNameOrDescriptionContaining(String name, String description);
}
