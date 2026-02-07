package com.walmart.ecommerce.infrastructure.persistence;



import com.walmart.ecommerce.domain.entities.Product;
import com.walmart.ecommerce.domain.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador que implementa ProductRepository usando MongoDB.
 * Separa la lógica de persistencia del dominio (Dependency Inversion).
 */
@Repository
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {

    @Override
    public Optional<Product> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Page<Product> searchByText(String searchText, Pageable pageable) {
        return null;
    }
}
