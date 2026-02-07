package com.walmart.ecommerce.domain.repositories;

import com.walmart.ecommerce.domain.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio del dominio para la entidad Product.
 * Define el contrato de persistencia independiente de la tecnología.
 */
public interface ProductRepository {

    /**
     * Busca un producto por su ID
     */
    Optional<Product> findById(String id);

    /**
     * Obtiene todos los productos paginados
     */
    Page<Product> findAll(Pageable pageable);

    /**
     * Busca productos por texto en nombre o descripción
     */
    Page<Product> searchByText(String searchText, Pageable pageable);
}
