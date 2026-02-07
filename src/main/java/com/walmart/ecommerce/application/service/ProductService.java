package com.walmart.ecommerce.application.service;

import com.walmart.ecommerce.application.dto.PageResponse;
import com.walmart.ecommerce.application.dto.ProductMapper;
import com.walmart.ecommerce.application.dto.ProductResponse;
import com.walmart.ecommerce.application.dto.ProductSearchFilter;
import com.walmart.ecommerce.domain.entities.Product;
import com.walmart.ecommerce.domain.exceptions.ProductNotFoundException;
import com.walmart.ecommerce.domain.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * Servicio de aplicación que orquesta la lógica de negocio del ecommerce.
 */
@Service
@Slf4j
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Obtiene un producto por su ID
     */
    public ProductResponse findById(String id) {
        log.debug("Buscando producto con ID: {}", id);
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado: " + id));
        
        return productMapper.toResponse(product);
    }

    /**
     * Busca productos aplicando filtros y paginación
     */
    public PageResponse<ProductResponse> searchProducts(
            ProductSearchFilter filter,
            int page,
            int size,
            String sortBy,
            String sortDirection) {
        
        log.debug("Buscando productos con filtros: {}", filter);

        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        
        Page<Product> productPage;
        
        if (filter.hasFilters()) {
            productPage = productRepository.findByFilters(
                    filter.getSearchText(),
                    filter.getCategory(),
                    filter.getBrand(),
                    filter.getMinPrice(),
                    filter.getMaxPrice(),
                    filter.getInStock(),
                    filter.getTags(),
                    pageable
            );
        } else {
            productPage = productRepository.findAll(pageable);
        }

        log.debug("Se encontraron {} productos", productPage.getTotalElements());
        
        return productMapper.toPageResponse(productPage);
    }

    /**
     * Obtiene todas las categorías disponibles
     */
    public List<String> getCategories() {
        return productRepository.findDistinctCategories();
    }

    /**
     * Obtiene todas las marcas disponibles
     */
    public List<String> getBrands() {
        return productRepository.findDistinctBrands();
    }

    /**
     * Crea un Pageable con ordenamiento
     */
    private Pageable createPageable(int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.unsorted();
        
        if (sortBy != null && !sortBy.isEmpty()) {
            Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection) 
                    ? Sort.Direction.DESC 
                    : Sort.Direction.ASC;
            sort = Sort.by(direction, sortBy);
        }
        
        return PageRequest.of(page, size, sort);
    }

    /**
     * Excepción personalizada para producto no encontrado
     */
    public static class ProductNotFoundException extends RuntimeException {
        public ProductNotFoundException(String message) {
            super(message);
        }
    }
}
