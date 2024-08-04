package com.alperendev.MrBeastECommerce.repository;

import com.alperendev.MrBeastECommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
