package com.malyszaryczlowiek.shop.shoppingCart;

import com.malyszaryczlowiek.shop.order.OrderController;
import com.malyszaryczlowiek.shop.productOrder.ProductOrder;
import com.malyszaryczlowiek.shop.productOrder.ProductOrderModel;
import com.malyszaryczlowiek.shop.productOrder.ProductOrderModelAssembler;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


/**
 * Klasa ma za zadanie przedstawić model z zamówieniami, czylicoś
 */
public class ShoppingCartModel extends RepresentationModel<ShoppingCartModel> {

    /**
     * Ważne to nie są product ordery z bazy danych więc nie mają np. ID
     */
    private final Page<ProductOrderModel> productsInShoppingCart;


    public ShoppingCartModel(ShoppingCart shoppingCart, int page, int size) {
        ProductOrderModelAssembler assembler = new ProductOrderModelAssembler();
        List<ProductOrderModel> productList = new ArrayList<>();
        shoppingCart.getAllProductsInShoppingCart().forEach(
                (product, integer) -> productList.add(assembler.toModel(new ProductOrder(product, integer)))
        );
        Pageable pageable = PageRequest.of(page,size);
        productsInShoppingCart = new PageImpl<>(productList, pageable, productList.size());
        addLinks();
    }

    // dodaje do linków link do już wykonanych zmaówień
    // TODO sprawdzić czy ten link będzie działał bezpośrednio do jedynej metody.
    // ważne ma być dodany jeden link do całego modelu a nie do każdego zamówiania
    private void addLinks() {
        this.add(
                // link do zamówień użytkownika, ale dostęp do tego linku wymaga uwieerzytelnienia.
                linkTo(OrderController.class).withRel("my_orders")
        );
    }

    public Page<ProductOrderModel> getProductsInShoppingCart() {
        return productsInShoppingCart;
    }
}
