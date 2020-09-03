package com.malyszaryczlowiek.shop.shoppingCart;

import com.malyszaryczlowiek.shop.products.Product;
import com.malyszaryczlowiek.shop.products.ProductModel;
import com.malyszaryczlowiek.shop.products.ProductModelAssembler;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;


/**
 * komponent oznaczony z zasięgiem sesji co oznacza,
 * że dla każdej nowej założonej sesji będzie tworzony
 * nowy obiekt tego typu.
 */
@Component
@Scope(scopeName = WebApplicationContext.SCOPE_SESSION,
        proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ShoppingCart {

    /**
     * nie możywmy użyć tutaj Product order ponieważ odnośi się to tylko
     * do obiektów juz zamówionych i zrealizowanych albo w trakcie realizacji
     *
     * LinkHashMapa powala nam mieć już produkty ułożone w kolejności dodawania do koszyka.
     */
    ///*
    private final Map<Product, Integer> productsInCart = new LinkedHashMap<>();

    public void addProduct(Product product, Integer number) {
        if (productsInCart.containsKey(product)) {
            Integer total = productsInCart.get(product) + number;
            productsInCart.replace(product, total);
        }
        else productsInCart.put(product, number);
    }

    public boolean removeProduct(Product product) {
        return productsInCart.remove(product) != null;
    }

    public Map<Product, Integer> getAllProductsInShoppingCart() {
        return this.productsInCart;
    }

    public boolean isProductPutInShoppingCart(Product productToDelete) {
        return productsInCart.containsKey(productToDelete);
    }

    public List<Map.Entry<ProductModel,Integer>> getListOfProductModels() {
        List<Map.Entry<ProductModel,Integer>> entryList = new ArrayList<>(productsInCart.size());
        ProductModelAssembler assembler = new ProductModelAssembler();
        productsInCart.forEach((k,v) -> {
            Map.Entry<ProductModel,Integer> entry = Map.entry(assembler.toModel(k), v);
            entryList.add(entry);
        });
        return entryList;
    }

     //*/



    /*
    Wersja druga gdzie przechowujemy ProductOrdery zamiast mapy productów i ich liczby


    private final List<ProductOrder> productOrderList = new ArrayList<>(2);
    private final ProductOrderRepository productOrderRepository;

    public ShoppingCart(ProductOrderRepository productOrderRepository) {
        this.productOrderRepository = productOrderRepository;

    }


    public List<ProductOrder> getProductOrderList() {
        return productOrderList;
    }


    public void addProduct(Product product, Integer number) {
        if (productOrderList.stream().filter(productOrder -> productOrder.getProduct().equals(product)).count() == 0) {
            Integer total = productsInCart.get(product) + number;
            productsInCart.replace(product, total);
        }
        else productsInCart.put(product, number);
    }

     */

}


























