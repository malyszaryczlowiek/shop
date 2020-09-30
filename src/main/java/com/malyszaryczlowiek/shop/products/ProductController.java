package com.malyszaryczlowiek.shop.products;

import com.malyszaryczlowiek.shop.shoppingCart.ShoppingCart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.Valid;
import javax.validation.constraints.Positive;


/**
 * sprawdzić czy nie będzie trzeba oznaczyć go jako
 * WebApplicationContext.SCOPE_SESSION
 */
@Scope(scopeName = WebApplicationContext.SCOPE_REQUEST)
@RestController
@RequestMapping(path = "/product")
public class ProductController {

    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductRepository productRepository;
    private final ShoppingCart shoppingCart;


    @Autowired
    public ProductController(
            ProductRepository productRepository,
            ShoppingCart shoppingCart) {
        this.productRepository = productRepository;
        this.shoppingCart = shoppingCart;
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
     * Dodaj produkt do koszyka.
     * <p></P>
     * TODO przeprojektować tak aby zamiast ścieżki i liczby
     * obiektów do dodania do koszyka brał odpowiedni obiekt restowy i to z niego
     * wyłuskiwać informacje.
     *
     * @param id identyfikator produktu
     * @return np informacja o dodaniu do koszyka
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> putProductToShoppingCart(
            @Valid @RequestBody ProductIdOrder productIdOrder, @PathVariable Long id) {
        if (!id.equals(productIdOrder.getId()))
            ResponseEntity.status(HttpStatus.CONFLICT).build();

        if (productRepository.existsById(id)) {
            shoppingCart.addProduct(productIdOrder.getId(), productIdOrder.getAmount());
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body("Product in number of " + productIdOrder.getAmount() + " added to shopping cart.");
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
    public ResponseEntity<String> deleteProductFromCart(@Positive @PathVariable Long id) {
        if (productRepository.existsById(id)) {
            //Product productToDelete = productRepository.getOne(id);
            logger.debug("rozmiar koszyka przed usunięciem produktu wynosi: " + shoppingCart.getAllProductsInShoppingCart().size());
            if (shoppingCart.isProductPutInShoppingCart(id)) {
                logger.debug("Produkt znajduje się w koszyku.");
                if (shoppingCart.removeProduct(id)) {
                    logger.debug("produkt usunięto z koszyka.");
                    return ResponseEntity.status(HttpStatus.OK)
                            .body("Product successfully removed form shopping Cart.");
                }
                else return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Bad Request. There is no such product to remove from shopping cart");
            }
            logger.debug("Nie ma takiego produktu w koszyku.");
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


/*
    @RequestMapping(path = "/{section}/{category}/{subcategory}", method = RequestMethod.PUT)
    public ResponseEntity<String> addProductToShoppingCart(
            @RequestBody Product product) {
        return addProd(product);
    }

    @RequestMapping(path = "/{section}/{category}/{subcategory}/search", method = RequestMethod.PUT)
    public ResponseEntity<String> addProductToShoppingCartSearch(
            @RequestBody Product product) {
        return addProd(product);
    }

    private ResponseEntity<String> addProd(Product product) {
        Example<Product> example = Example.of(product);
        if (productRepository.exists(example)) {
            productRepository.findOne(example).ifPresent(
                    realProduct -> shoppingCart.addProduct(realProduct, 1));
            logger.debug("product added to shopping cart");
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                "Nie można dodać do koszyka produktu, którego nie ma w bazie.");
    }

    @RequestMapping(path = "/{section}/{category}/{subcategory}/search", method = RequestMethod.DELETE)
    public ResponseEntity<String> removeProductFromCart(
            @RequestBody Product product) {
        Example<Product> example = Example.of(product);
        if (productRepository.exists(example)) {
            productRepository.findOne(example).ifPresent(shoppingCart::removeProduct);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                "nie można usunąć produktu z koszyka którego nie ma w bazie");
    }

 */
