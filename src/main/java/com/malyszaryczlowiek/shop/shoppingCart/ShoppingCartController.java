package com.malyszaryczlowiek.shop.shoppingCart;

import com.malyszaryczlowiek.shop.products.Product;
import com.malyszaryczlowiek.shop.products.ProductModel;
import com.malyszaryczlowiek.shop.products.ProductRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * kontroller musi mieć zasięg sesji ponieważ wstrzykiwany jest do niego
 * koszyk który tez ma zasięg sesji.
 */
@Scope(scopeName = WebApplicationContext.SCOPE_SESSION)
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    private final Logger logger = LoggerFactory.getLogger(ShoppingCartController.class);

    private final ShoppingCart shoppingCart;
    private final ProductRepository productRepository;

    public ShoppingCartController(ShoppingCart shoppingCart,
                                  ProductRepository productRepository) {
        this.shoppingCart= shoppingCart;
        this.productRepository = productRepository;
    }



    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<Map.Entry<ProductModel, Integer>>> getShoppingCart(
            @RequestParam(name = "page", defaultValue = "0", required = false) @PositiveOrZero int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) @PositiveOrZero int size)
    {
        return returnContentOfShoppingCart(page,size);
    }



    private ResponseEntity<Page<Map.Entry<ProductModel, Integer>>> returnContentOfShoppingCart(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        List<Map.Entry<ProductModel, Integer>> list = shoppingCart.getListOfProductModels();
        Page<Map.Entry<ProductModel, Integer>> ordersPage = new PageImpl<>(list, pageable, list.size());
        return ResponseEntity.status(HttpStatus.OK).body(ordersPage);
    }



    /**
     * metoda wywołaywana przy dodaniu produktu do koszyka po wyszukaniu go w wyszukiwarce,
     * albo bezpośrednio w koszyku
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Page<Map.Entry<ProductModel, Integer>>> addProductToShoppingCart(
            @Valid @RequestBody Product product,
            @RequestParam(name = "page", defaultValue = "0", required = false) @PositiveOrZero int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) @PositiveOrZero int size) {

        Example<Product> example = Example.of(product);
        if (productRepository.exists(example)) {
            Optional<Product> optionalProduct = productRepository.findOne(example);
            if (optionalProduct.isPresent()) {
                shoppingCart.addProduct(optionalProduct.get(), 1);
                logger.debug("product added to shopping cart");
                return returnContentOfShoppingCart(page, size);
            }
        }
        logger.debug("There is no such product in database.");
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }



    /**
     * metoda wywołaywana przy dodaniu produktu do koszyka po wyszukaniu go w wyszukiwarce,
     * albo bezpośrednio w koszyku
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Page<Map.Entry<ProductModel, Integer>>> removeProductFromShoppingCart(
            @Valid @RequestBody Product product,
            @RequestParam(name = "page", defaultValue = "0", required = false) @PositiveOrZero int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) @PositiveOrZero int size) {

        Example<Product> example = Example.of(product);
        if (productRepository.exists(example)) {
            Optional<Product> optionalProduct = productRepository.findOne(example);
            if (optionalProduct.isPresent()) {
                shoppingCart.removeProduct(optionalProduct.get());
                logger.debug("product removed to shopping cart");
                return returnContentOfShoppingCart(page, size);
            }
        }
        logger.debug("Cannot remove product from shopping cart because it not exists in database.");
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }















    /**
     * z zabezpieczania za pomocą {@link Secured @Secured} można zrezygnować
     * i wpisać wszystko w
     */
    @Secured("ROLE_CLIENT")
    @RequestMapping(value = "/payment", method = RequestMethod.GET)
    ResponseEntity<?> goToPayment() {

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
