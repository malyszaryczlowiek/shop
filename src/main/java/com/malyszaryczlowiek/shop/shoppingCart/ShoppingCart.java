package com.malyszaryczlowiek.shop.shoppingCart;

import com.malyszaryczlowiek.shop.products.Product;
import com.malyszaryczlowiek.shop.products.ProductIdOrder;
import com.malyszaryczlowiek.shop.products.ProductRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;
import java.util.stream.Collectors;


/**
 * komponent oznaczony z zasięgiem sesji co oznacza,
 * że dla każdej nowej założonej sesji będzie tworzony
 * nowy obiekt tego typu.
 */
@Component
@Scope(scopeName = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)//, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ShoppingCart {


    private final Logger logger = LoggerFactory.getLogger(ShoppingCart.class);


    private final List<ProductIdOrder> productIdOrderList = new LinkedList<>();

    /**
     * nie możywmy użyć tutaj Product order ponieważ odnośi się to tylko
     * do obiektów juz zamówionych i zrealizowanych albo w trakcie realizacji
     *
     * LinkHashMapa powala nam mieć już produkty ułożone w kolejności dodawania do koszyka.
     */
    //private final Map<Long, Integer> productsInCart = new LinkedHashMap<>();
    private final ProductRepository productRepository;

    public ShoppingCart(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

/*

    public void addProduct(Long productId, Integer number) {
        logger.debug("rozmiar koszyka przed dodaniem wynosi: " + productsInCart.size());
        if (productsInCart.containsKey(productId)) {
            Integer total = productsInCart.get(productId) + number;
            productsInCart.replace(productId, total);
            logger.debug("W koszyku zmieniono ilość produktu na " + total);
        }
        else productsInCart.put(productId, number);
        logger.debug("rozmiar koszyka po dodaniu wynosi: " + productsInCart.size());
    }

 */
    public void addProduct(ProductIdOrder product) {
        boolean ifContains = productIdOrderList.stream()
                .anyMatch(productIdOrder -> productIdOrder.getId().equals(product.getId()));
        if (ifContains) {
            productIdOrderList.forEach(productIdOrder -> {
                if (product.getId().equals(productIdOrder.getId())) {
                    Integer newAmountInCart = productIdOrder.getAmount() + product.getAmount();
                    productIdOrder.setAmount(newAmountInCart);
                }

            });
        } else productIdOrderList.add(product);
    }

    public boolean removeProduct(Long productId) {
        Optional<ProductIdOrder> result = productIdOrderList.stream()
                .filter(productIdOrder -> productIdOrder.getId().equals(productId))
                .findFirst();
        return result.map(productIdOrderList::remove).orElse(false);
        //return productsInCart.remove(productId) != null;
    }


    public Map<Product, Integer> getAllProductsInShoppingCart() {
        Set<Long> setOfIds = productIdOrderList.stream().map(ProductIdOrder::getId).collect(Collectors.toSet());
        List<Product> productList = productRepository.findAllById(setOfIds);
        Map<Product, Integer> resultMap = new LinkedHashMap<>(setOfIds.size());
        productIdOrderList.forEach( productIdOrder ->  productList.stream()
                        .filter(product -> product.getId().equals(productIdOrder.getId()))
                        .findFirst()
                        .ifPresent(foundProduct -> resultMap.put(foundProduct, productIdOrder.getAmount()))
        );
        return resultMap;
    }


    public boolean isProductPutInShoppingCart(Long productToDelete) {
        //logger.error("obiekt w koszyku ma id: " + productsInCart.containsKey(productToDelete));
        Optional<ProductIdOrder> result = productIdOrderList.stream()
                .filter(productIdOrder -> productIdOrder.getId().equals(productToDelete))
                .findFirst();
        return result.isPresent();
    }
}












/*
// old implementation works as well

private final Map<Long, Integer> productsInCart = new LinkedHashMap<>();
    private final ProductRepository productRepository;

    public ShoppingCart(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public void addProduct(Long productId, Integer number) {
        logger.debug("rozmiar koszyka przed dodaniem wynosi: " + productsInCart.size());
        if (productsInCart.containsKey(productId)) {
            Integer total = productsInCart.get(productId) + number;
            productsInCart.replace(productId, total);
            logger.debug("W koszyku zmieniono ilość produktu na " + total);
        }
        else productsInCart.put(productId, number);
        logger.debug("rozmiar koszyka po dodaniu wynosi: " + productsInCart.size());
    }

    public boolean removeProduct(Long productId) {
        return productsInCart.remove(productId) != null;
    }

    public Map<Product, Integer> getAllProductsInShoppingCart() {
        List<Product> productList = productRepository.findAllById(productsInCart.keySet());
        Map<Product, Integer> resultMap = new LinkedHashMap<>(productsInCart.size());
        productsInCart.forEach(
                (productId, amount) -> productList.stream()
                        .filter(product -> product.getId().equals(productId))
                        .findFirst()
                        .ifPresent(foundProduct -> resultMap.put(foundProduct, amount))
        );
        return resultMap;
    }

    public boolean isProductPutInShoppingCart(Long productToDelete) {
        logger.error("obiekt w koszyku ma id: " + productsInCart.containsKey(productToDelete));
        return productsInCart.containsKey(productToDelete);
    }
 */