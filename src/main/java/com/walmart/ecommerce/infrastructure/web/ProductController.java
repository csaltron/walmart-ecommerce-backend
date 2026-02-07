package com.walmart.ecommerce.infrastructure.web;

import com.walmart.ecommerce.application.dto.PageResponse;
import com.walmart.ecommerce.application.dto.ProductResponse;
import com.walmart.ecommerce.application.dto.ProductSearchFilter;
import com.walmart.ecommerce.application.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controlador REST para gestión del ecommerce de productos.
 * Expone endpoints siguiendo estándares RESTful.
 */
@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Products", description = "API de ecommerce de productos")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener producto por ID",
        description = "Retorna el detalle completo de un producto específico",
        responses = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
        }
    )
    public ResponseEntity<ProductResponse> getProductById(
            @Parameter(description = "ID del producto", required = true)
            @PathVariable String id) {
        
        log.info("GET /v1/products/{}", id);
        ProductResponse product = productService.findById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping
    @Operation(
        summary = "Buscar productos",
        description = "Busca y filtra productos con paginación y ordenamiento. Todos los parámetros son opcionales."
    )
    public ResponseEntity<PageResponse<ProductResponse>> searchProducts(
            @Parameter(description = "Texto a buscar en nombre o descripción")
            @RequestParam(required = false) String search,
            
            @Parameter(description = "Filtrar por categoría")
            @RequestParam(required = false) String category,
            
            @Parameter(description = "Filtrar por marca")
            @RequestParam(required = false) String brand,
            
            @Parameter(description = "Precio mínimo")
            @RequestParam(required = false) BigDecimal minPrice,
            
            @Parameter(description = "Precio máximo")
            @RequestParam(required = false) BigDecimal maxPrice,
            
            @Parameter(description = "Solo productos en stock")
            @RequestParam(required = false) Boolean inStock,
            
            @Parameter(description = "Filtrar por tags")
            @RequestParam(required = false) List<String> tags,
            
            @Parameter(description = "Número de página (empieza en 0)")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Tamaño de página")
            @RequestParam(defaultValue = "20") int size,
            
            @Parameter(description = "Campo para ordenar (price, name, stock)")
            @RequestParam(required = false) String sortBy,
            
            @Parameter(description = "Dirección de ordenamiento (asc, desc)")
            @RequestParam(defaultValue = "asc") String sortDirection) {
        
        log.info("GET /v1/products - search={}, category={}, brand={}", search, category, brand);

        ProductSearchFilter filter = ProductSearchFilter.builder()
                .searchText(search)
                .category(category)
                .brand(brand)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .inStock(inStock)
                .tags(tags)
                .build();

        PageResponse<ProductResponse> response = productService.searchProducts(
                filter, page, size, sortBy, sortDirection);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/categories")
    @Operation(
        summary = "Obtener categorías",
        description = "Retorna todas las categorías de productos disponibles"
    )
    public ResponseEntity<List<String>> getCategories() {
        log.info("GET /v1/products/categories");
        List<String> categories = productService.getCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/brands")
    @Operation(
        summary = "Obtener marcas",
        description = "Retorna todas las marcas de productos disponibles"
    )
    public ResponseEntity<List<String>> getBrands() {
        log.info("GET /v1/products/brands");
        List<String> brands = productService.getBrands();
        return ResponseEntity.ok(brands);
    }
}
