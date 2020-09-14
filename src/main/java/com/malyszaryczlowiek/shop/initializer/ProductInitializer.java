package com.malyszaryczlowiek.shop.initializer;

import com.malyszaryczlowiek.shop.categories.Category;
import com.malyszaryczlowiek.shop.categories.CategoryRepository;
import com.malyszaryczlowiek.shop.feature.Feature;
import com.malyszaryczlowiek.shop.feature.FeatureRepository;
import com.malyszaryczlowiek.shop.products.Product;
import com.malyszaryczlowiek.shop.products.ProductRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.math.BigDecimal;
import java.util.List;

/**
 * Wyjaśnienie analogiczne jak dla {@link UserInitializer}.
 */
@Configuration
@Order(2)
public class ProductInitializer implements ApplicationRunner {


    private final ProductRepository productRepository;
    private final FeatureRepository featureRepository;
    private final CategoryRepository categoryRepository;


    public ProductInitializer(ProductRepository productRepository,
                       FeatureRepository featureRepository,
                       CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.featureRepository = featureRepository;
        this.categoryRepository = categoryRepository;
    }


    private final Logger logger = LoggerFactory.getLogger(ProductInitializer.class);


    @Override
    public void run(ApplicationArguments args) throws Exception {

        Category compCategory = new Category("Komputery i Podzespoły", "komputery_i_podzespoly",
                "Komputery", "komputery",
                "Desktopy", "desktopy");

        categoryRepository.deleteAll();
        Category c = categoryRepository.save(compCategory);

        // specyfikacja
        Feature socket = new Feature( "usb", "Łącze Usb", "Usb-C 2x" );
        featureRepository.deleteAll();
        Feature persistedSocket = featureRepository.saveAndFlush(socket);
        List<Feature> specification = List.of(persistedSocket);

        Product komputerDell = new Product(c, "Dell", "XPS-cośtam",
                new BigDecimal("2999.00"), 5);
        komputerDell.setSpecification(specification);
        Product acerDesktop = new Product(c, "Acer", "Nitro-5",
                new BigDecimal("3999.00"), 3);
        acerDesktop.setSpecification(specification);

        productRepository.save(komputerDell);
        productRepository.save(acerDesktop);
        logger.debug("DB initialized with products.");
    }
}
















