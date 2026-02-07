package com.walmart.ecommerce.infrastructure.config;

import com.walmart.ecommerce.domain.entities.Product;
import com.walmart.ecommerce.domain.repositories.ProductRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

/**
 * Inicializador de datos de prueba.
 * Carga productos desde catalog.json al iniciar la aplicación.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        long count = productRepository.count();
        
        if (count == 0) {
            log.info("Base de datos vacía. Cargando datos iniciales...");
            loadInitialData();
        } else {
            log.info("Base de datos ya contiene {} productos", count);
        }
    }

    private void loadInitialData() {
        try {
            ClassPathResource resource = new ClassPathResource("catalog.json");
            InputStream inputStream = resource.getInputStream();
            
            List<Product> products = objectMapper.readValue(
                    inputStream,
                    new TypeReference<>() {
                    }
            );
            
            productRepository.saveAll(products);
            log.info("Se cargaron {} productos exitosamente", products.size());
            
        } catch (Exception e) {
            log.error("Error cargando datos iniciales", e);
        }
    }
}
