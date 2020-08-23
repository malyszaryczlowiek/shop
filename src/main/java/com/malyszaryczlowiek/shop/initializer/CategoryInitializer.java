package com.malyszaryczlowiek.shop.initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;


/**
 * inicjalizuję categorie, które następnie w koljnym inicjalizerze
 * zostaną wykorzystane w Produktach.
 */
@Configuration
@Order(2)
public class CategoryInitializer implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(CategoryInitializer.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.debug("starting initialization of Category");
        // todo to implement
        /*
        smartfony: Android i iOS
        computery: Desktop i AiO
        televizory:
         */



    }
}
