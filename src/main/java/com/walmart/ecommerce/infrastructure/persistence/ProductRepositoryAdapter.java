package com.walmart.ecommerce.infrastructure.persistence;



import com.walmart.ecommerce.domain.entities.Product;
import com.walmart.ecommerce.domain.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.support.PageableExecutionUtils;
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

    private final MongoProductRepository mongoRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public Product save(Product product) {
        return mongoRepository.save(product);
    }

    @Override
    public List<Product> saveAll(List<Product> products) {
        return mongoRepository.saveAll(products);
    }

    @Override
    public Optional<Product> findById(String id) {
        return mongoRepository.findById(id);
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return mongoRepository.findAll(pageable);
    }

    @Override
    public Page<Product> searchByText(String searchText, Pageable pageable) {
        TextCriteria criteria = TextCriteria.forDefaultLanguage().matching(searchText);
        Query query = new Query(criteria).with(pageable);

        List<Product> products = mongoTemplate.find(query, Product.class);
        long count = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Product.class);

        return PageableExecutionUtils.getPage(products, pageable, () -> count);
    }

    @Override
    public Page<Product> findByFilters(
            String searchText,
            String category,
            String brand,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean inStock,
            List<String> tags,
            Pageable pageable) {

        Query query = buildFilterQuery(searchText, category, brand, minPrice, maxPrice, inStock, tags);
        query.with(pageable);

        List<Product> products = mongoTemplate.find(query, Product.class);
        long count = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Product.class);

        return PageableExecutionUtils.getPage(products, pageable, () -> count);
    }

    @Override
    public long count() {
        return mongoRepository.count();
    }

    @Override
    public void deleteAll() {
        mongoRepository.deleteAll();
    }

    @Override
    public List<String> findDistinctCategories() {
        return mongoRepository.findAllCategories().stream()
                .map(Product::getCategory)
                .distinct()
                .filter(c -> c != null && !c.isEmpty())
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findDistinctBrands() {
        return mongoRepository.findAllBrands().stream()
                .map(Product::getBrand)
                .distinct()
                .filter(b -> b != null && !b.isEmpty())
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Construye una query de MongoDB aplicando filtros dinámicamente
     */
    private Query buildFilterQuery(
            String searchText,
            String category,
            String brand,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean inStock,
            List<String> tags) {

        Query query;

        // Búsqueda de texto
        if (searchText != null && !searchText.isEmpty()) {
            TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matching(searchText);
            query = new Query(textCriteria);
        } else {
            query = new Query();
        }

        List<Criteria> criteria = new ArrayList<>();

        // Filtro por categoría
        if (category != null && !category.isEmpty()) {
            criteria.add(Criteria.where("category").is(category));
        }

        // Filtro por marca
        if (brand != null && !brand.isEmpty()) {
            criteria.add(Criteria.where("brand").is(brand));
        }

        // Filtro por rango de precio
        if (minPrice != null || maxPrice != null) {
            Criteria priceCriteria = Criteria.where("price");
            if (minPrice != null) {
                priceCriteria.gte(minPrice);
            }
            if (maxPrice != null) {
                priceCriteria.lte(maxPrice);
            }
            criteria.add(priceCriteria);
        }

        // Filtro por disponibilidad
        if (inStock != null && inStock) {
            criteria.add(Criteria.where("stock").gt(0));
        }

        // Filtro por tags
        if (tags != null && !tags.isEmpty()) {
            criteria.add(Criteria.where("tags").in(tags));
        }

        if (!criteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
        }

        return query;
    }
}
