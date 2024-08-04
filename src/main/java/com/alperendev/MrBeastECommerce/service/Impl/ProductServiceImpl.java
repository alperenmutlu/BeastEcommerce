package com.alperendev.MrBeastECommerce.service.Impl;

import com.alperendev.MrBeastECommerce.dto.ProductDto;
import com.alperendev.MrBeastECommerce.dto.Response;
import com.alperendev.MrBeastECommerce.entity.Category;
import com.alperendev.MrBeastECommerce.entity.Product;
import com.alperendev.MrBeastECommerce.exception.NotFoundException;
import com.alperendev.MrBeastECommerce.mapper.EntityDtoMapper;
import com.alperendev.MrBeastECommerce.repository.CategoryRepository;
import com.alperendev.MrBeastECommerce.repository.ProductRepository;
import com.alperendev.MrBeastECommerce.service.AwsS3Service;
import com.alperendev.MrBeastECommerce.service.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final EntityDtoMapper entityDtoMapper;
    private final AwsS3Service awsS3Service;

    @Override
    public Response createProduct(Long categoryId, MultipartFile image, String name, String description, BigDecimal price) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category Not Found"));
        String productImageUrl = awsS3Service.saveImageToS3(image);

        Product product = new Product();
        product.setCategory(category);
        product.setPrice(price);
        product.setName(name);
        product.setDescription(description);
        product.setImageUrl(productImageUrl);

        productRepository.save(product);
        return Response.builder()
                .status(200)
                .message("Product is created successfully!")
                .build();
    }

    @Override
    public Response updateProduct(Long productId, Long categoryId, MultipartFile image, String name, String description, BigDecimal price) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product Not Found"));
        Category category = null;
        String productImageUrl = null;

        if(categoryId != null) {
            category = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category Not Found"));
        }
        if(image != null && !image.isEmpty()) {
            product.setImageUrl(awsS3Service.saveImageToS3(image));
        }

        if(category != null) product.setCategory(category);
        if(name != null) product.setName(name);
        if(price != null) product.setPrice(price);
        if(description != null) product.setDescription(description);
        if(productImageUrl != null) product.setImageUrl(productImageUrl);

        productRepository.save(product);
        return Response.builder()
                .status(200)
                .message("Product is updated successfully!")
                .build();

    }

    @Override
    public Response deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product Not Found"));
        productRepository.delete(product);
        return Response.builder()
                .status(200)
                .message("Product is deleted successfully!")
                .build();
    }

    @Override
    public Response getProductById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product Not Found"));
        ProductDto productDto = entityDtoMapper.mapProductToDtoBasic(product);
        return Response.builder()
                .status(200)
                .product(productDto)
                .build();
    }

    @Override
    public Response getAllProducts() {
        List<ProductDto> productList = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(entityDtoMapper::mapProductToDtoBasic)
                .collect(Collectors.toList());
        return Response.builder()
                .status(200)
                .productList(productList)
                .build();

    }

    @Override
    public Response getProductsByCategory(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        if(products.isEmpty()){
            throw new NotFoundException("No Products Found for Category Id: " + categoryId);
        }
        List<ProductDto> productDtoList = products.stream()
                .map(entityDtoMapper::mapProductToDtoBasic)
                .collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .productList(productDtoList)
                .build();

    }

    @Override
    public Response searchProduct(String searchValue) {
        List<Product> products = productRepository.findByNameOrDescriptionContaining(searchValue, searchValue);
        if(products.isEmpty()){
            throw new NotFoundException("No Products Found for Search Value: " + searchValue);
        }
        List<ProductDto> productDtoList = products.stream()
                .map(entityDtoMapper::mapProductToDtoBasic)
                .collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .productList(productDtoList)
                .build();

    }
}
