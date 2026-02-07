package com.walmart.ecommerce.application.service;

import com.walmart.ecommerce.application.dto.ProductMapper;
import com.walmart.ecommerce.application.dto.ProductResponse;
import com.walmart.ecommerce.domain.entities.Product;
import com.walmart.ecommerce.domain.exceptions.ProductNotFoundException;
import com.walmart.ecommerce.domain.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Servicio de aplicación que orquesta la lógica de negocio del ecommerce.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    /**
     * Obtiene un producto por su ID
     */
    public ProductResponse findById(String id) {
        log.debug("Buscando producto con ID: {}", id);
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado: " + id));
        
        return productMapper.toResponse(product);
    }


}
