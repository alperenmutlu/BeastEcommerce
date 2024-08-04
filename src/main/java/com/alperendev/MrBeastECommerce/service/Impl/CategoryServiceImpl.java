package com.alperendev.MrBeastECommerce.service.Impl;

import com.alperendev.MrBeastECommerce.dto.CategoryDto;
import com.alperendev.MrBeastECommerce.dto.Response;
import com.alperendev.MrBeastECommerce.entity.Category;
import com.alperendev.MrBeastECommerce.exception.NotFoundException;
import com.alperendev.MrBeastECommerce.mapper.EntityDtoMapper;
import com.alperendev.MrBeastECommerce.repository.CategoryRepository;
import com.alperendev.MrBeastECommerce.service.interfaces.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    public Response createCategory(CategoryDto categoryRequest) {
        Category category = new Category();
        category.setName(categoryRequest.getName());
        categoryRepository.save(category);
        return Response.builder()
                .status(200)
                .message("Category created successfully!")
                .build();
    }

    @Override
    public Response updateCategory(Long categoryId, CategoryDto categoryRequest) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException("Category is not found!"));
        category.setName(categoryRequest.getName());
        categoryRepository.save(category);
        return Response.builder()
                .status(200)
                .message("Category is updated!")
                .build();
    }

    @Override
    public Response getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDto> categoryDtoList = categories.stream()
                .map(entityDtoMapper::mapCategoryToDtoBasic)
                .collect(Collectors.toList());
        return Response.builder()
                .status(200)
                .categoryList(categoryDtoList)
                .build();
    }

    @Override
    public Response getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category is not found!"));
        CategoryDto categoryDto = entityDtoMapper.mapCategoryToDtoBasic(category);
        return Response.builder()
                .status(200)
                .category(categoryDto)
                .build();
    }

    @Override
    public Response deleteCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category is not found!"));
        categoryRepository.delete(category);
        return Response.builder()
                .status(200)
                .message("Category is deleted with the id: " + category.getId())
                .build();
    }
}
