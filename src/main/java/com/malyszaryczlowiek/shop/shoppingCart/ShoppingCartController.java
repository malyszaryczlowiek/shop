package com.malyszaryczlowiek.shop.shoppingCart;

import com.malyszaryczlowiek.shop.client.Client;
import com.malyszaryczlowiek.shop.client.ClientRepository;
import com.malyszaryczlowiek.shop.order.Order;
import com.malyszaryczlowiek.shop.order.OrderModel;
import com.malyszaryczlowiek.shop.order.OrderRepository;
import com.malyszaryczlowiek.shop.productOrder.ProductOrder;
import com.malyszaryczlowiek.shop.productOrder.ProductOrderRepository;
import com.malyszaryczlowiek.shop.products.Product;
import com.malyszaryczlowiek.shop.products.ProductIdOrder;
import com.malyszaryczlowiek.shop.products.ProductRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * kontroller musi mieć zasięg sesji ponieważ wstrzykiwany jest do niego
 * koszyk który tez ma zasięg sesji.
 */
@Scope(scopeName = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    private final Logger logger = LoggerFactory.getLogger(ShoppingCartController.class);

    private final ShoppingCart shoppingCart;
    private final ProductRepository productRepository;
    private final ProductOrderRepository productOrderRepository;
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;

    public ShoppingCartController(ShoppingCart shoppingCart,
                                  ProductRepository productRepository,
                                  ProductOrderRepository productOrderRepository,
                                  OrderRepository orderRepository,
                                  ClientRepository clientRepository) {
        this.shoppingCart= shoppingCart;
        this.productRepository = productRepository;
        this.productOrderRepository = productOrderRepository;
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
    }


    /**
     *
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ShoppingCartModel> getShoppingCart(
            @RequestParam(name = "page", defaultValue = "0", required = false) @PositiveOrZero int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) @PositiveOrZero int size) {
        return returnContentOfShoppingCart(page,size);
    }



    private ResponseEntity<ShoppingCartModel> returnContentOfShoppingCart(int page, int size) {
        return ResponseEntity.status(HttpStatus.OK).body(new ShoppingCartModel(shoppingCart, page, size));
    }



    /**
     * metoda wywołaywana przy dodaniu produktu do koszyka po wyszukaniu go w wyszukiwarce,
     * albo bezpośrednio w koszyku
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<ShoppingCartModel> addProductToShoppingCart(
            @RequestBody @Valid ProductIdOrder productIdOrder) {
        if (productRepository.existsById(productIdOrder.getId())) {
            Optional<Product> optionalProduct = productRepository.findById(productIdOrder.getId());
            if (optionalProduct.isPresent()) {
                shoppingCart.addProduct(productIdOrder);
                logger.debug("product added to shopping cart");
                return returnContentOfShoppingCart(0, 10);
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
    public ResponseEntity<ShoppingCartModel> removeProductFromShoppingCart(
            @RequestBody @PositiveOrZero Long id) {
        if (productRepository.existsById(id)) {
            Optional<Product> optionalProduct = productRepository.findById(id);
            if (optionalProduct.isPresent()) {
                shoppingCart.removeProduct(optionalProduct.get().getId());
                logger.debug("product removed to shopping cart");
                return returnContentOfShoppingCart(0, 10);
            }
        }
        logger.debug("Cannot remove product from shopping cart because it not exists in database.");
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }



    /**
     * metoda przekształca product ordery w ordery.
     *
     * z zabezpieczania za pomocą {@link Secured @Secured} można zrezygnować
     * i wpisać wszystko w
     *
     * W uuproszczeniu zakładam, ze zakupy może wykonać tylko osoba zalgowana.??
     * wyłuskuję informacjie o użytkowniku.
     *
     * @param authentication obiekt wymagany do wyłuskania informacji o nazwie użytkowanika
     * @return zwraca wykonane zamówienie.
     */
    @RequestMapping(value = "/payment", method = RequestMethod.GET)
    ResponseEntity<OrderModel> goToPayment(Authentication authentication, HttpServletResponse response) {
        if (authentication != null) {
            String email = authentication.getName();
            // tutaj producty to już są obiekty w stanie persistence.
            Map<Product, Integer> orderedProducts = shoppingCart.getAllProductsInShoppingCart();
            int cartSize = orderedProducts.size();
            if ( cartSize == 0)
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            List<ProductOrder> listOfProductOrder = new ArrayList<>(cartSize);


            // TODO zrobić coś jeszcze na wypadek zmiany ilości produktów oraz ich braku w magazynie;


            orderedProducts.forEach( (prod, numb) -> listOfProductOrder.add(new ProductOrder(prod, numb)));
            // tutaj lista finalList jest persistence - to są obiekty zapisane już w bazie danych.
            List<ProductOrder> finalList = productOrderRepository.saveAll(listOfProductOrder);
            // towrzę obiekt zamówienia - nie jest persistence
            Order order = new Order(finalList, "Completed", System.currentTimeMillis());
            Client client = clientRepository.findByEmail(email);
            // wczytuję obiekt clienta który jest persistence
            order.setClient(client);
            // tutaj mam już obiekt który jest persistence - nastąpiło zapisanie zamówienia do bazy danych
            Order injectedOrder = orderRepository.saveAndFlush(order);
            // czyszczę produkty z koszyka.
            shoppingCart.clearShoppingCart();
            return ResponseEntity.status(HttpStatus.OK).body(new OrderModel(injectedOrder));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}





















