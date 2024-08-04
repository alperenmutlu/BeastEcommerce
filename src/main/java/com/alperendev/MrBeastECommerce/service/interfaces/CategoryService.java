package com.alperendev.MrBeastECommerce.service.interfaces;

import com.alperendev.MrBeastECommerce.dto.CategoryDto;
import com.alperendev.MrBeastECommerce.dto.Response;

public interface CategoryService {
    Response createCategory(CategoryDto categoryRequest);
    Response updateCategory(Long categoryId, CategoryDto categoryRequest);
    Response getAllCategories();
    Response getCategoryById(Long categoryId);
    Response deleteCategoryById(Long categoryId);
}
