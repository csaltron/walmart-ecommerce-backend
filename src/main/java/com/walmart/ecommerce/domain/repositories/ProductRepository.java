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
     * Guarda un producto
     */
    Product save(Product product);

    /**
     * Guarda múltiples productos
     */
    List<Product> saveAll(List<Product> products);

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

    /**
     * Busca productos aplicando filtros múltiples
     */
    Page<Product> findByFilters(
            String searchText,
            String category,
            String brand,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean inStock,
            List<String> tags,
            Pageable pageable
    );

    /**
     * Cuenta el total de productos
     */
    long count();

    /**
     * Elimina todos los productos
     */
    void deleteAll();

    /**
     * Obtiene todas las categorías únicas
     */
    List<String> findDistinctCategories();

    /**
     * Obtiene todas las marcas únicas
     */
    List<String> findDistinctBrands();
}
