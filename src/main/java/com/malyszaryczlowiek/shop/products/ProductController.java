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

import javax.validation.constraints.PositiveOrZero;
import java.util.Iterator;
import java.util.List;


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
        return getListOfCategories(categories, false);
    }



    /**
     * to jest metoda z empty string jako kategorią.
     */
    @RequestMapping(path = "/{section}/{category}", method = RequestMethod.GET)
    public ResponseEntity<CategoryModel> getAllSubcategoriesInCategory(
            @PathVariable(name = "section") String section,
            @PathVariable(name = "category") String category) {
        List<Category> categories = categoryRepository.findAllSubcategoriesInGivenSectionAndCategory(section, category);
        return getListOfCategories(categories, true);
    }



    private ResponseEntity<CategoryModel> getListOfCategories(List<Category> categories, boolean showSubcategories) {
        if (categories.isEmpty()) {
            logger.debug("jest empty - nie ma takiej sekcji");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        CategoryModelAssembler assembler = new CategoryModelAssembler();
        assembler.setSubcategoriesLinksFlag(showSubcategories);
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
            @PathVariable(name = "subcategory") String subcategory,
            // parametry wyświetlania strony
            @RequestParam(name = "page", defaultValue = "0", required = false) @PositiveOrZero int page,
            @RequestParam(name = "size", defaultValue = "20", required = false) @PositiveOrZero int size,
            @RequestParam(name = "sort", defaultValue = "a", required = false) String sorting,
            @RequestParam(name = "sortBy", defaultValue = "productName", required = false) String sortBy) {
        //Pageable pageable = controllerUtil.setPaging(0, 20, "a", "productName");
        Pageable pageable = controllerUtil.setPaging(page, size, sorting, sortBy);
        return getProducts(section, category, subcategory, null, pageable);
    }



    @RequestMapping(path = "/{section}/{category}/{subcategory}/search", method = RequestMethod.POST)
    public ResponseEntity<Page<ProductModel>> getProductsFromSearchingCriteria(
            @RequestBody SearchingCriteria searchingCriteria,
            @PathVariable(name = "section") String section,
            @PathVariable(name = "category") String category,
            @PathVariable(name = "subcategory") String subcategory,
            // parametry wyświetlania strony
            @RequestParam(name = "page", defaultValue = "0", required = false) @PositiveOrZero int page,
            @RequestParam(name = "size", defaultValue = "20", required = false) @PositiveOrZero int size,
            @RequestParam(name = "sort", defaultValue = "a", required = false) String sorting,
            @RequestParam(name = "sortBy", defaultValue = "productName", required = false) String sortBy) {
        Pageable pageable = controllerUtil.setPaging(page, size, sorting, sortBy);
        return getProducts(section, category, subcategory, searchingCriteria, pageable);
    }



    /**
     *
     *
     * ta metoda bardziej obciąża pamięć bo wczytuje dużo danych, które następnie odfiltrowuje,
     * jest natomiast szybsza pod kątem wczytania danych z DB?
     */
    private ResponseEntity<Page<ProductModel>> getProducts(String section, String category,
            String subcategory, SearchingCriteria searchingCriteria, Pageable pageable) {
        List<Category> categories = categoryRepository.findSubcategory(section, category, subcategory);
        if (categories.isEmpty()) {
            logger.debug("jest empty - nie ma takiej sekcji");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<Product> listOfProducts =
                productRepository.findAllProductsInTheseCategories(categories);
        if (listOfProducts.size() == 0) {
            logger.debug("nie ma produktów w tej kategorii. ");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        ProductModelAssembler assembler = new ProductModelAssembler();
        // jeśli istnieją jakieś niezerowe zadane kryteria wyszukiwania to
        // należy wg. nich odfiltrować wyniki
        if (searchingCriteria != null && searchingCriteria.getSearchingParameters().size() > 0) {
            logger.debug("SearchingCriteria nie jest null i ma mapę większa od 0.");
            searchingCriteria.getSearchingParameters().forEach( (descriptorToFind, listOfValues) -> {
                Iterator<Product> productIterator = listOfProducts.iterator();
                while (productIterator.hasNext()) {
                    boolean doesProductFulfilSearchingCriteria = productIterator.next().getSpecification().stream().anyMatch(
                            // pierwszy warunek sprawdza czy feature ma ten descryptor
                            feature -> feature.getFeatureSearchingDescriptor().equals(descriptorToFind)
                                    // jeśli go ma to sprawdza czy wartość tego descryptora
                                    // jest w szukanych wartościach.
                                    && listOfValues.contains(feature.getFeatureValue())
                            // jeśli to zwróci true to znaczy, że produkt zawiera feature ze wskazaną wartością
                            // i nie należy go usówać
                    );
                    if ( !doesProductFulfilSearchingCriteria )
                        productIterator.remove();
                }
            });
        }
        if (listOfProducts.size() == 0) {
            logger.debug("po przefiltrowaniu produktów nie znaleziono żadnego pasującego wyniku.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        Page<Product> pageOfProducts = new PageImpl<>(listOfProducts, pageable,listOfProducts.size());
        return ResponseEntity.status(HttpStatus.OK)
                .body(pageOfProducts.map(assembler::toModel));
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
            assembler.setAdditionalSpecification(true);
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



/*
 * ta metoda wczytuje mniej danych z DB przez co mniej obciąża pamięć,
 * ale za to zapytania do bazy są koszmarne i wyszukanie trwa
 * dłużej.
 */
    /*
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
     */

