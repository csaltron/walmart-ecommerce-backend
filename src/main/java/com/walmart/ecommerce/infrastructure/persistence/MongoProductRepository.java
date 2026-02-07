package com.walmart.ecommerce.infrastructure.persistence;

import com.walmart.ecommerce.domain.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Repositorio de MongoDB para Product.
 * Extiende MongoRepository y define consultas personalizadas.
 */
public interface MongoProductRepository extends MongoRepository<Product, String> {

    /**
     * Búsqueda por texto usando índices de MongoDB
     */
    @Query("{ $text: { $search: ?0 } }")
    Page<Product> findByTextSearch(String searchText, Pageable pageable);

    /**
     * Obtiene categorías distintas
     */
    @Query(value = "{}", fields = "{ 'category': 1 }")
    List<Product> findAllCategories();

    /**
     * Obtiene marcas distintas
     */
    @Query(value = "{}", fields = "{ 'brand': 1 }")
    List<Product> findAllBrands();
}
