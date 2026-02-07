package com.walmart.ecommerce.application.dto;

import com.walmart.ecommerce.domain.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre entidades de dominio y DTOs
 */
@Component
public class ProductMapper {

    /**
     * Convierte una entidad Product a ProductResponse
     */
    public ProductResponse toResponse(Product product) {
        if (product == null) {
            return null;
        }

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .brand(product.getBrand())
                .price(product.getPrice())
                .oldPrice(product.getOldPrice())
                .stock(product.getStock())
                .tags(product.getTags())
                .imageUrl(product.getImageUrl())
                .available(product.isAvailable())
                .discountPercentage(product.hasDiscount() ? product.getDiscountPercentage() : null)
                .build();
    }

    /**
     * Convierte una página de productos a PageResponse
     */
    public PageResponse<ProductResponse> toPageResponse(Page<Product> page) {
        return PageResponse.<ProductResponse>builder()
                .content(page.getContent().stream()
                        .map(this::toResponse)
                        .toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
}
