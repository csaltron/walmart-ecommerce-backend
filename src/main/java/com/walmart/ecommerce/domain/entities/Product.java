package com.walmart.ecommerce.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

/**
 * Entidad de dominio que representa un producto en el ecommerce.
 * Implementa índices para búsqueda eficiente.
 */
@Document(collection = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    private String id;

    @TextIndexed(weight = 2)
    private String name;

    @TextIndexed
    private String description;

    @Indexed
    private String category;

    @Indexed
    private String brand;

    private BigDecimal price;

    private BigDecimal oldPrice;

    @Indexed
    private Integer stock;

    private List<String> tags;

    private String imageUrl;

    /**
     * Verifica si el producto está disponible en stock
     */
    public boolean isAvailable() {
        return stock != null && stock > 0;
    }

    /**
     * Verifica si el producto tiene descuento
     */
    public boolean hasDiscount() {
        return oldPrice != null && oldPrice.compareTo(price) > 0;
    }

    /**
     * Calcula el porcentaje de descuento
     */
    public Integer getDiscountPercentage() {
        if (!hasDiscount()) {
            return 0;
        }
        BigDecimal discount = oldPrice.subtract(price);
        return discount.multiply(BigDecimal.valueOf(100))
                .divide(oldPrice, 0, BigDecimal.ROUND_HALF_UP)
                .intValue();
    }
}
