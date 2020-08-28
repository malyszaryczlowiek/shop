package com.malyszaryczlowiek.shop.products;

import com.malyszaryczlowiek.shop.categories.Category;
import com.malyszaryczlowiek.shop.categories.CategoryModel;
import com.malyszaryczlowiek.shop.categories.CategoryModelAssembler;
import com.malyszaryczlowiek.shop.categories.CategoryRepository;
import com.malyszaryczlowiek.shop.controllerUtil.ControllerUtil;
import com.malyszaryczlowiek.shop.shoppingCart.ShoppingCart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


@RestController
@RequestMapping(path = "/product")
public class ProductController {

    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ControllerUtil controllerUtil;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ShoppingCart shoppingCart;


    @Autowired
    public ProductController(
            ControllerUtil controllerUtil,
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            ShoppingCart shoppingCart) {
        this.controllerUtil = controllerUtil;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.shoppingCart = shoppingCart;
    }


    /**
     * to jest metoda z null string jako kategorią.
     */
    @RequestMapping(path = "/{section}", method = RequestMethod.GET)
    public ResponseEntity<CategoryModel> getAllCategoriesInSection(
            @PathVariable(name = "section") String section) {
        List<Category> categories = categoryRepository.findAllCategoriesInGivenSection(section);
        if (categories.isEmpty()) {
            logger.debug("jest empty - nie ma takiej sekcji");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        CategoryModelAssembler assembler = new CategoryModelAssembler();
        return ResponseEntity.status(HttpStatus.OK).body(assembler.toModel(categories));
    }


    /**
     * to jest metoda z empty string jako kategorią.
     */
    @RequestMapping(path = "/{section}/{category}", method = RequestMethod.GET)
    public ResponseEntity<CategoryModel> getAllSubcategoriesInCategory(
            @PathVariable(name = "section") String section,
            @PathVariable(name = "category") String category) {
        List<Category> categories = categoryRepository.findAllSubcategoriesInGivenSectionAndCategory(section, category);
        if (categories.isEmpty()) {
            logger.debug("jest empty - nie ma takiej sekcji");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        CategoryModelAssembler assembler = new CategoryModelAssembler();
        assembler.setSubcategoriesLinksFlag(true);
        return ResponseEntity.status(HttpStatus.OK).body(assembler.toModel(categories));
    }


    /**
     * Defaultowa metoda która wyłuskuje wszystkie produkty w danej podkategorii,
     * bez jakiegokolwiek sortowania i  ustawień pagingu.
     */
    @RequestMapping(path = "/{section}/{category}/{subcategory}", method = RequestMethod.GET)
    public ResponseEntity<Page<ProductModel>> getAllProductsInSubcategory(
            @PathVariable(name = "section") String section,
            @PathVariable(name = "category") String category,
            @PathVariable(name = "subcategory") String subcategory) {
        List<Category> categories = categoryRepository.findSubcategory(section, category, subcategory);
        if (categories.isEmpty()) {
            logger.debug("jest empty - nie ma takiej sekcji");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Pageable pageable = controllerUtil.setPaging(0, 20, "d", "productName");
        return getProducts(categories, pageable, null);
    }



    @RequestMapping(path = "/{section}/{category}/{subcategory}/search", method = RequestMethod.POST)
    public ResponseEntity<Page<ProductModel>> getProductsFromSearchingCriteria(
            @RequestBody SearchingCriteria searchingCriteria,
            @PathVariable(name = "section") String section,
            @PathVariable(name = "category") String category,
            @PathVariable(name = "subcategory") String subcategory) {
        List<Category> categories = categoryRepository.findSubcategory(section, category, subcategory);
        if (categories.isEmpty()) {
            logger.debug("jest empty - nie ma takiej sekcji");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Pageable pageable = controllerUtil.setPaging(0, 20, "d", "productName");
        logger.debug("buduję Page<ProductModel>");
        return getProducts(categories, pageable, searchingCriteria);
    }



    /**
     * ta metoda wczytuje mniej danych z DB przez co mniej obciąża pamięć,
     * ale za to zapytania do bazy są koszmarne i wyszukanie trwa
     * dłużej.
     */
    private ResponseEntity<Page<ProductModel>> getProducts(
            List<Category> listOfCategories, Pageable pageable, SearchingCriteria searchingCriteria) {

        if ( !listOfCategories.isEmpty() ) {
            Page<Product> listOfProducts = productRepository.findAllProductsInTheseCategories(
                    listOfCategories, pageable);
            if (listOfProducts.getTotalElements() == 0)
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            ProductModelAssembler assembler = new ProductModelAssembler();
            if (searchingCriteria != null) {


                // zaimplementować searching produktów w zależności od zadanych w
                // SearchingCriteria parametrach.
                Stream<Product> productStream = listOfProducts.stream();








            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(listOfProducts.map(assembler::toModel));
        }
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // bo to oznacza, że nie ma takiej kateogrii
    }



    /**
     * ta metoda bardziej obciąża pamięć bo wczytuje dużo danych, które następnie odfiltrowuje,
     * jest natomiast szybsza pod kątem wczytania danych z DB?
     */
    private ResponseEntity<Page<ProductModel>> getProductsWithFiltering(List<Category> listOfCategories, Pageable pageable) {
        if ( !listOfCategories.isEmpty() ) {
            List<Product> listOfProducts = productRepository.findAll();
            // filtrowanie którego tutaj nie ma
            Page<Product> page = new PageImpl<>(listOfProducts, pageable, listOfProducts.size());
            if (page.getTotalElements() == 0)
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            ProductModelAssembler assembler = new ProductModelAssembler();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(page.map(assembler::toModel));
        }
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // bo to oznacza, że nie ma takiej kateogrii
    }



    /**
     * Metoda zwraca produkt
     *
     * @param id identyfikator produktu
     * @return Obiekt Produktu
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ProductModel> getProduct(@PathVariable Long id) {
        if (productRepository.existsById(id)) {
            ProductModelAssembler assembler = new ProductModelAssembler();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(assembler.toModel(productRepository.getOne(id)));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }



    /**
     * Dodaj produkt do koszyka. TODO przeprojektować tak aby zamiast ścieżki i liczby
     * obiektów do dodania do koszyka brał odpowiedni obiekt restowy i to z niego
     * wyłuskiwać informacje.
     *
     * @param amountOfOrderedProducts liczba zamówionych produktów
     * @param id identyfikator produktu
     * @return np informacja o dodaniu do koszyka
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> putProductToShoppingCart(
            @RequestBody Integer amountOfOrderedProducts, @PathVariable Long id) {
        if (productRepository.existsById(id)) {
            Product product = productRepository.getOne(id);
            shoppingCart.addProduct(product,amountOfOrderedProducts);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body("Product in number of " + amountOfOrderedProducts + " added to shopping cart.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }



    /**
     * Usuń aktualny obiekt z koszyka
     *
     * @param id identyfiaktor produktu
     * @return Powiadomienie
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteProductFromCart(@PathVariable(name = "id") Long id) {
        if (productRepository.existsById(id)) {
            Product productToDelete = productRepository.getOne(id);
            if (shoppingCart.isProductPutInShoppingCart(productToDelete)) {
                if (shoppingCart.removeProduct(productToDelete))
                    return ResponseEntity.status(HttpStatus.OK)
                            .body("Product successfully removed form shopping Cart.");
                else
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Bad Request. There is no such product to remove from shopping cart");
            }
            else
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Bad Request. There is no current product in shopping cart");
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Page Not Found :( " +
                    "There is no such product.");
    }
}











/*
    // ustawienia strony
            @RequestParam(name = "page", defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(name = "size", defaultValue = "10") @PositiveOrZero int size,
            @RequestParam(name = "sort", defaultValue = "d") String sorting,
            @RequestParam(name = "sortBy", defaultValue = "productName") String sortBy
     */