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
/*
 Feature cpu = new Feature(false, "cpu", "Processor", "i9-9600");
        Feature cpu2 = new Feature(false, "cpu", "Processor", "i9-12000");
        Feature prize = new Feature(true, "prize", "Prize", "8999.00");
        featureRepository.save(cpu);
        featureRepository.save(cpu2);
        featureRepository.saveAndFlush(prize);
 */





        Category compCategory = new Category("Komputery i Podzespoły", "komputery_i_podzespoly",
                "Komputery", "komputery",
                "Desktopy", "desktopy");

        Category c = categoryRepository.saveAndFlush(compCategory);

        Feature producer = new Feature(true, "prod", "Producent", "Dell");
        Feature productName = new Feature(true, "model", "Model", "XPS-cośtam");
        Feature prize = new Feature(true, "prize", "Cena", "2999.00");
        // Feature accessed = new Feature(true, "access", "Dostępność", "true");
        Feature amount = new Feature(true, "amount", "Liczba na stanie", "5");

        // specyfikacja
        Feature socket = new Feature(false, "usb", "Łącze Usb", "Usb-C 2x" );
        List<Feature> specification = List.of(socket);

        Product komputerDell = new Product(c, producer, productName, prize, amount, List.of());
        komputerDell.setSpecification(specification);

        productRepository.saveAndFlush(komputerDell);
    }
}
















