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
        Category aioCategory = new Category("Komputery i Podzespoły", "komputery_i_podzespoly",
                "Komputery", "komputery",
                "All in One", "aio");

        Category monitors = new Category("Komputery i Podzespoły", "komputery_i_podzespoly",
                "Urządzenia peryferyjne", "urzadzenia_peryferyjne",
                "Monitory", "monitory");

        categoryRepository.deleteAll();
        Category c = categoryRepository.save(compCategory);
        Category aio = categoryRepository.save(aioCategory);
        Category monitory = categoryRepository.save(monitors);

        // specyfikacja
        Feature socket = new Feature( "usb", "Usb", "Usb-C 2x" );
        Feature socket2 = new Feature( "usb", "Usb", "Thunderbolt 2x" );
        featureRepository.deleteAll();
        Feature persistedSocket = featureRepository.save(socket);
        Feature persistedSocket2 = featureRepository.saveAndFlush(socket2);
        List<Feature> specificationDell = List.of(persistedSocket);
        List<Feature> specificationApple = List.of(persistedSocket, persistedSocket2);


        Product komputerDell = new Product(c, "Dell", "XPS cośtam",
                new BigDecimal("2999.00"), 5);
        komputerDell.setSpecification(specificationDell);


        Product imac = new Product(aio, "Apple", "iMac i5",
                new BigDecimal("9999.00"), 3);
        imac.setSpecification(specificationApple);


        Product monitorDell = new Product(monitory, "Dell", "P2419H",
                new BigDecimal("1999.99"), 10);


        productRepository.save(komputerDell);
        productRepository.save(imac);
        productRepository.save(monitorDell);
        logger.debug("DB initialized with products.");
    }
}
















