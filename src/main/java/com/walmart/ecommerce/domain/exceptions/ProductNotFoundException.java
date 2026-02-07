package com.walmart.ecommerce.domain.exceptions;

/**
 * Excepción personalizada para indicar que un producto no fue encontrado
 */
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) {
        super(message);
    }

}