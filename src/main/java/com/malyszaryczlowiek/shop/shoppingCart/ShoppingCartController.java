package com.malyszaryczlowiek.shop.shoppingCart;

import com.malyszaryczlowiek.shop.client.Client;
import com.malyszaryczlowiek.shop.client.ClientRepository;
import com.malyszaryczlowiek.shop.order.Order;
import com.malyszaryczlowiek.shop.order.OrderModel;
import com.malyszaryczlowiek.shop.order.OrderModelAssembler;
import com.malyszaryczlowiek.shop.order.OrderRepository;
import com.malyszaryczlowiek.shop.productOrder.ProductOrder;
import com.malyszaryczlowiek.shop.productOrder.ProductOrderRepository;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

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
@Scope(scopeName = WebApplicationContext.SCOPE_SESSION)
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
     * metoda przekształca product ordery w ordery.
     *
     * z zabezpieczania za pomocą {@link Secured @Secured} można zrezygnować
     * i wpisać wszystko w
     */
    @Secured("ROLE_CLIENT")
    @RequestMapping(value = "/payment", method = RequestMethod.GET)
    ResponseEntity<OrderModel> goToPayment(Authentication authentication) {
        String email = authentication.getName();
        Client client = clientRepository.findByEmail(email);
        if (client != null) {
            Map<Product, Integer> orderedProducts = shoppingCart.getAllProductsInShoppingCart();
            List<ProductOrder> listOfProductOrder = new ArrayList<>(orderedProducts.size());
            orderedProducts.forEach( (prod, numb) -> listOfProductOrder.add(new ProductOrder(prod, numb)));
            List<ProductOrder> finalList = productOrderRepository.saveAll(listOfProductOrder);
            Order order = new Order(finalList, "Completed", System.currentTimeMillis());
            order.setClient(client);
            Order injectedOrder = orderRepository.saveAndFlush(order);
            OrderModelAssembler assembler = new OrderModelAssembler(); trzeba jeszcze zaimplementować OrderModel
            return ResponseEntity.status(HttpStatus.OK).body(assembler.toModel(injectedOrder));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}





















