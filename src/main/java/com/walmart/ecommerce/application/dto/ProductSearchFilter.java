package com.walmart.ecommerce.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO para recibir filtros de búsqueda de productos
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchFilter {

    private String searchText;
    private String category;
    private String brand;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Boolean inStock;
    private List<String> tags;

    /**
     * Verifica si hay filtros aplicados
     */
    public boolean hasFilters() {
        return searchText != null || category != null || brand != null ||
               minPrice != null || maxPrice != null || inStock != null ||
               (tags != null && !tags.isEmpty());
    }
}
