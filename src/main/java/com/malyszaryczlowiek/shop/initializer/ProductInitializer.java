package com.malyszaryczlowiek.shop.initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * Wyjaśnienie analogiczne jak dla {@link UserInitializer}.
 */
@Configuration
@Order(3)
public class ProductInitializer implements ApplicationRunner {

    /*
    private final ProductRepository productRepository;


    ProductInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
*/

    private final Logger logger = LoggerFactory.getLogger(ProductInitializer.class);


    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.trace("products initialized");
    }
}
