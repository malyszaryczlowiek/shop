package com.malyszaryczlowiek.shop.admin;


import com.malyszaryczlowiek.shop.categories.Category;
import com.malyszaryczlowiek.shop.categories.CategoryRepository;
import com.malyszaryczlowiek.shop.controllerUtil.ControllerUtil;
import com.malyszaryczlowiek.shop.feature.Feature;
import com.malyszaryczlowiek.shop.feature.FeatureRepository;
import com.malyszaryczlowiek.shop.products.Product;
import com.malyszaryczlowiek.shop.products.ProductModel;
import com.malyszaryczlowiek.shop.products.ProductRepository;

import org.hibernate.jdbc.TooManyRowsAffectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/admin/product")
public class AdminProductController {

    private final Logger logger = LoggerFactory.getLogger(AdminProductController.class);

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final FeatureRepository featureRepository;
    private final ControllerUtil controllerUtil;


    public AdminProductController(ProductRepository productRepository,
                                  CategoryRepository categoryRepository,
                                  ControllerUtil controllerUtil,
                                  FeatureRepository featureRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.featureRepository = featureRepository;
        this.controllerUtil = controllerUtil;
    }



    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getNothing(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(name = "page", defaultValue = "0", required = false) @PositiveOrZero int page,
            @RequestParam(name = "size", defaultValue = "20", required = false) @PositiveOrZero int size,
            @RequestParam(name = "sort", defaultValue = "d", required = false) String sorting,
            @RequestParam(name = "sortBy", defaultValue = "popularity", required = false) String sortBy) {

        if ( !List.of(10,20,40).contains(size) || !List.of("popularity", "prize").contains(sortBy))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        Pageable pageable = controllerUtil.setPaging(page, size, sorting, sortBy);
        if (search != null) {
            if (search.isBlank() || search.isEmpty())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            return getProductsFromPhrase(search, pageable);
        }
        else
            return ResponseEntity.status(HttpStatus.OK).build();
    }

    private ResponseEntity<Page<ProductModel>> getProductsFromPhrase(String phrase, Pageable pageable) {
        List<Product> listOfProducts = productRepository.findAllProductsWithThisPhrase(phrase);
        if (listOfProducts.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return convertListOfProductsToPageOfProductModel(listOfProducts,pageable);
    }


    private ResponseEntity<Page<ProductModel>> convertListOfProductsToPageOfProductModel(
            List<Product> listOfProducts, Pageable pageable) {
        Page<Product> pageOfProducts = new PageImpl<>(listOfProducts, pageable, listOfProducts.size());
        Page<ProductModel> finalPage = pageOfProducts.map(
                product -> new ProductModel(product, false));
        return ResponseEntity.status(HttpStatus.OK).body(finalPage);
    }



    /**
     * dodaje produkt do bazy danych o ile ten w niej już nie istnieje.
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addProduct(
            @Valid @RequestBody Product product, // obiekt produktu który dodajemy
            HttpServletResponse response) throws IOException { // response posłuży na przekierowanie od razu na stronę nowo dodanego produktu

        // sprawdzam czy istnieje dana kategoria
        Category category = product.getProductCategory();
        Example<Category> exampleCategory = Example.of(category);
        boolean doesCategoryExist = categoryRepository.exists(exampleCategory);
        if (!doesCategoryExist) {
            logger.debug("kategoria nie istnieje więc ją dodaję");
            category = categoryRepository.saveAndFlush(category);
        }
        else {
            List<Category> categoryList = categoryRepository.findAll(exampleCategory);
            if (categoryList.size() > 1) {
                logger.debug("mamy więcej niż jedną pasującą kategorię");
                return ResponseEntity.status(HttpStatus.MULTIPLE_CHOICES).build();
            }
            logger.debug("kategoria istnieje i jest tylko jeden egzemplarz");
            category = categoryList.get(0); // i tutaj kategory jest już zarządzane
        }
        // obrabiam specyfikację
        List<Feature> productFeatures = product.getSpecification();
        List<Feature> persistedFeatures = productFeatures.stream().map(
                feature -> {
                    Example<Feature> example = Example.of(feature);
                    boolean existsInDb = featureRepository.exists(example);
                    if (existsInDb){
                        List<Feature> list = featureRepository.findAll(example);
                        if (list.size() == 1)
                            return featureRepository.findAll(example).get(0);
                        else
                            throw new TooManyRowsAffectedException("Za dużo wyników w bazie danych", 1, list.size());
                    }
                    else
                        return featureRepository.save(feature);
                })
                .collect(Collectors.toList());
        // featureRepository.flush(); // flushuję wszelkie dane do zapisania w DB
        // wklejam persisted features
        product.setSpecification(persistedFeatures);
        product.setProductCategory(category);
        logger.debug("ustawiam kategorię na tę z bazy danych");
        // produkt który dodajemy jest już poddany walidacji
        Example<Product> example = Example.of(product);
        if (productRepository.exists(example)) {
            logger.debug("produkt już istnieje w bazie danych i nie może zostać dodany");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build(); // nie akceptowalne, produkt już istnieje w bazie.
        }
        Product addedProduct = productRepository.saveAndFlush(product);
        //response.sendRedirect("/" + addedProduct.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(addedProduct); // product utworzony
    }



    // używać dużo logger ów żeby wyłapywać problemy;
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> duplicateProduct() {





        return ResponseEntity.status(HttpStatus.OK).build();
    }













    /*
     * Tutaj otwiera mi się stronka z konkretnym produktem, który mogę zmodyfikować albo usunąć
     */



    /**
     * Method called when product was modified by frontend.
     * @param id product id
     * @return status
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<Product> modifyProduct(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody Product modifiedProduct) {
        if ( !modifiedProduct.getId().equals(id) )
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // jeśli id się nie zgadzają to występuje conflikt
        Example<Product> example = Example.of(modifiedProduct);
        if (productRepository.exists(example)) { // sprawdź czy produkt już nie istnieje
            Product returned = productRepository.saveAndFlush(modifiedProduct);
            // accepted dajemy gdy wprowadzone zmiany są zwalidowane i zostały zaakceptowane.
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(returned); // accepted - zmiany zaakceptowane
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build(); // not acceptable , partial content
    }



    /**
     * Usuwanie produktu. generalnie
     *
     * @param id produktu do usunięcia
     * @return status of operation.
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Product> removeProduct(@PathVariable(name = "id") Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).build();// operacja wykonana prawidłowo
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // nie można usunąć z bazy produktu którego nie ma
    }
}
