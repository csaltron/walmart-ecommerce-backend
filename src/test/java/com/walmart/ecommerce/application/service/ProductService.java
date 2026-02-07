package com.walmart.ecommerce.application.service;

import com.walmart.ecommerce.application.dto.PageResponse;
import com.walmart.ecommerce.application.dto.ProductMapper;
import com.walmart.ecommerce.application.dto.ProductResponse;
import com.walmart.ecommerce.application.dto.ProductSearchFilter;
import com.walmart.ecommerce.domain.entities.Product;
import com.walmart.ecommerce.domain.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return ProductResponse when product exists")
    void findById_returnsProductResponse_whenProductExists() {
        Product product = Product.builder()
                .id("p-001")
                .name("Camiseta Deportiva")
                .description("Camiseta de alta calidad para deportes")
                .price(new BigDecimal(29.99))
                .category("Ropa")
                .brand("SportCo")
                .build();
        product.setId("p-001");
        ProductResponse response = ProductResponse.builder()
                .id("p-001")
                .name("Camiseta Deportiva")
                .description("Camiseta de alta calidad para deportes")
                .price(new BigDecimal(29.99))
                .category("Ropa")
                .brand("SportCo")
                .build();
        when(productRepository.findById("p-001")).thenReturn(Optional.of(product));
        when(productMapper.toResponse(product)).thenReturn(response);

        ProductResponse result = productService.findById("p-001");

        assertEquals(response, result);
        verify(productRepository).findById("p-001");
        verify(productMapper).toResponse(product);
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when product does not exist")
    void findById_throwsException_whenProductNotFound() {
        when(productRepository.findById("p-999")).thenReturn(Optional.empty());

        assertThrows(ProductService.ProductNotFoundException.class, () -> productService.findById("p-999"));
        verify(productRepository).findById("p-999");
    }

    @Test
    @DisplayName("Should call findByFilters when filters are present")
    void searchProducts_withFilters_callsFindByFilters() {
        ProductSearchFilter filter = mock(ProductSearchFilter.class);
        when(filter.hasFilters()).thenReturn(true);
        Page<Product> productPage = new PageImpl<>(List.of(new Product()));
        PageResponse<ProductResponse> pageResponse = new PageResponse<>();
        when(productRepository.findByFilters(
                any(), any(), any(), any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(productPage);
        when(productMapper.toPageResponse(productPage)).thenReturn(pageResponse);

        PageResponse<ProductResponse> result = productService.searchProducts(filter, 0, 10, "name", "asc");

        assertEquals(pageResponse, result);
        verify(productRepository).findByFilters(
                any(), any(), any(), any(), any(), any(), any(), any(Pageable.class));
        verify(productMapper).toPageResponse(productPage);
    }

    @Test
    @DisplayName("Should call findAll when no filters are present")
    void searchProducts_withoutFilters_callsFindAll() {
        ProductSearchFilter filter = mock(ProductSearchFilter.class);
        when(filter.hasFilters()).thenReturn(false);
        Page<Product> productPage = new PageImpl<>(List.of(new Product()));
        PageResponse<ProductResponse> pageResponse = new PageResponse<>();
        when(productRepository.findAll(any(Pageable.class))).thenReturn(productPage);
        when(productMapper.toPageResponse(productPage)).thenReturn(pageResponse);

        PageResponse<ProductResponse> result = productService.searchProducts(filter, 0, 10, null, null);

        assertEquals(pageResponse, result);
        verify(productRepository).findAll(any(Pageable.class));
        verify(productMapper).toPageResponse(productPage);
    }

    @Test
    @DisplayName("Should return list of categories")
    void getCategories_returnsList() {
        List<String> categories = Arrays.asList("Ropa", "Electrónica");
        when(productRepository.findDistinctCategories()).thenReturn(categories);

        List<String> result = productService.getCategories();

        assertEquals(categories, result);
        verify(productRepository).findDistinctCategories();
    }

    @Test
    @DisplayName("Should return list of brands")
    void getBrands_returnsList() {
        List<String> brands = Arrays.asList("SportCo", "ModaYa");
        when(productRepository.findDistinctBrands()).thenReturn(brands);

        List<String> result = productService.getBrands();

        assertEquals(brands, result);
        verify(productRepository).findDistinctBrands();
    }
}