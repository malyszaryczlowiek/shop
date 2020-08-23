package com.malyszaryczlowiek.shop.products;

import com.malyszaryczlowiek.shop.categories.Category;
import com.malyszaryczlowiek.shop.categories.CategoryRepository;
import com.malyszaryczlowiek.shop.shoppingCart.ShoppingCart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@RestController
@RequestMapping(path = "/product")
public class ProductController {


    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ShoppingCart shoppingCart;

    /**
     *
     */
    @Autowired
    public ProductController(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            ShoppingCart shoppingCart) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.shoppingCart = shoppingCart;
    }


    /**
     * to jest metoda z null string jako kategorią.
     */
    @RequestMapping(path = "/{category}", method = RequestMethod.GET)
    public ResponseEntity<Page<ProductModel>> getAllProductsInCategory(
            @PathVariable(name = "category") String category,
            @RequestParam(name = "page", defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(name = "size", defaultValue = "10") @PositiveOrZero int size,
            @RequestParam(name = "sort", defaultValue = "desc") String sorting,
            @RequestParam(name = "sortBy", defaultValue = "time") String sortBy)
    {
        List<Category> listOfCategories = getListOfCategories(category, null, null);
        Pageable pageable = setPaging(page, size, sorting, sortBy);
        return getProducts(listOfCategories, pageable);
    }


    /**
     * to jest metoda z empty string jako kategorią.
     */
    @RequestMapping(path = "/{category}/{subcategory1}", method = RequestMethod.GET)
    public ResponseEntity<Page<ProductModel>> getAllProductsInSubCategory(
            @PathVariable(name = "category") String category,
            @PathVariable(name = "subcategory1") String subcategory1,
            @RequestParam(name = "page", defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(name = "size", defaultValue = "10") @PositiveOrZero int size,
            @RequestParam(name = "sort", defaultValue = "desc") String sorting,
            @RequestParam(name = "sortBy", defaultValue = "time") String sortBy) // name prize  potem można dodac popularność
    {
        List<Category> listOfCategories = getListOfCategories(category, subcategory1, "");
        Pageable pageable = setPaging(page, size, sorting, sortBy);
        return getProducts(listOfCategories, pageable);
    }

    /**
     * w tej metodzie wszyskie kategorie są podane jako stringi
     */
    @RequestMapping(path = "/{category}/{subcategory1}/{subcategory2}", method = RequestMethod.GET)
    public ResponseEntity<Page<ProductModel>> getAllProductsInSubSubCategory(
            @PathVariable(name = "category") String category,
            @PathVariable(name = "subcategory1") String subcategory1,
            @PathVariable(name = "subcategory2") String subcategory2,
            @RequestParam(name = "page", defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(name = "size", defaultValue = "10") @PositiveOrZero int size,
            @RequestParam(name = "sort", defaultValue = "desc") String sorting,
            @RequestParam(name = "sortBy", defaultValue = "time") String sortBy)
    {
        List<Category> listOfCategories = getListOfCategories(category, subcategory1, subcategory2);
        Pageable pageable = setPaging(page, size, sorting, sortBy);
        return getProducts(listOfCategories, pageable);
    }


    /**
     * wyłuskuje z db obiekty categorii jakie spełniją dana ścieżka
     */
    private List<Category> getListOfCategories(String category, String subcategory1, String subcategory2) {
        Category cat = new Category(category, subcategory1, subcategory2);
        Example<Category> example = Example.of(cat);
        return categoryRepository.findAll(example);
    }


    /**
     * ustaawia paging wyświtlanej storny z danymi
     */
    private Pageable setPaging(int page, int size, String sorting, String... sortBy) {
        Sort sort;
        if (sorting.equals("asc"))
            sort = Sort.by(Sort.Direction.ASC, sortBy);
        else
            sort = Sort.by(Sort.Direction.DESC, sortBy);
        return  PageRequest.of(page, size, sort);
    }

    /**
     * ta metoda wczytuje mniej danych z DB przez co mniej obciąża pamięć,
     * ale za to zapytania do bazy są koszmarne i wyszukanie trwa
     * dłużej.
     */
    private ResponseEntity<Page<ProductModel>> getProducts(List<Category> listOfCategories, Pageable pageable) {
        if ( !listOfCategories.isEmpty() ) {
            Page<Product> listOfProducts = productRepository.findAllProductsInTheseCategories(
                    listOfCategories,pageable);
            if (listOfProducts.getTotalElements() == 0)
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            ProductModelAssembler assembler = new ProductModelAssembler();
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
     * @param id identyfikator produktu
     * @return Obiekt Produktu
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    ResponseEntity<? extends RepresentationModel<?>> getProduct(@PathVariable Long id) {
        if (productRepository.existsById(id)) {
            ProductModelAssembler assembler = new ProductModelAssembler();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(assembler.toModel(productRepository.getOne(id)));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    /**
     * Dodaj produkt do koszyka.
     *
     * @param number liczba zamówionych produktów
     * @param id identyfikator produktu
     * @return np informacja o dodaniu do koszyka
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> putProductToShoppingCart(@RequestBody Integer number, @PathVariable Long id) {
        if (productRepository.existsById(id)) {
            Product product = productRepository.getOne(id);
            shoppingCart.addProduct(product,number);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body("Product in number of " + number + " added to shopping cart.");
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
                        .body("Bad Request. There is no this product in shopping cart");
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Page Not Found :( " +
                    "There is no such product.");
    }
}
