package com.malyszaryczlowiek.shop.initializer;

import com.malyszaryczlowiek.shop.feature.Feature;
import com.malyszaryczlowiek.shop.feature.FeatureRepository;
import com.malyszaryczlowiek.shop.products.ProductRepository;
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


    //private final ProductRepository productRepository;
    private final FeatureRepository featureRepository;


    ProductInitializer(//ProductRepository productRepository,
                       FeatureRepository featureRepository) {
        //this.productRepository = productRepository;
        this.featureRepository = featureRepository;
    }


    private final Logger logger = LoggerFactory.getLogger(ProductInitializer.class);


    @Override
    public void run(ApplicationArguments args) throws Exception {
        Feature cpu = new Feature(false, "cpu", "Processor", "i9-9600");
        Feature cpu2 = new Feature(false, "cpu", "Processor", "i9-12000");
        Feature prize = new Feature(true, "prize", "Prize", "8999.00");
        featureRepository.save(cpu);
        featureRepository.save(cpu2);
        featureRepository.saveAndFlush(prize);


        logger.debug("categories added");

        logger.debug("products initialized");
    }
}
