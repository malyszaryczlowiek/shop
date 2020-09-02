package com.malyszaryczlowiek.shop.shoppingCart;


import com.malyszaryczlowiek.shop.productOrder.ProductOrder;
import com.malyszaryczlowiek.shop.products.Product;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
     * do obiektów juz zamówionych i zrealizowanych albo w trakcie realizacji.
     */
    private final Map<Product, Integer> productsInCart = new HashMap<>();

    public void addProduct(Product product, Integer number) {
        if (productsInCart.containsKey(product)) {
            Integer total = productsInCart.get(product) + number;
            productsInCart.replace(product, total);
        }
        else
            productsInCart.putIfAbsent(product, number);
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



    /*
    Wersja druga gdzie przechowujemy ProductOrdery zamiast mapy productów i ich liczby
    */

    /*
    private final List<ProductOrder> productOrderList = new ArrayList<>(2);

    public List<ProductOrder> getProductOrderList() {
        return productOrderList;
    }

    public void addProductOrder(ProductOrder productOrder) {

    }
     */

}


























