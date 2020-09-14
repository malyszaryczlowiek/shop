package com.malyszaryczlowiek.shop.mainPageController;

import com.malyszaryczlowiek.shop.client.ClientAccountController;
import com.malyszaryczlowiek.shop.order.OrderController;

import com.malyszaryczlowiek.shop.shoppingCart.ShoppingCartController;
import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


public class MainPageLinkSupplier {

    private final List<Link> listOfLinks = new ArrayList<>();

    /**
     * TODO sprawdzić czy tak utworzona lista nie będzie dodawała
     * ponownie już dodanych linków przy każdym autowiringu.
     *
     */
    public MainPageLinkSupplier() {
        // link do głównej strony
        listOfLinks.add(linkTo(methodOn(MainPageController.class).welcomePage())
                .withRel("main_page"));

        // link do strony użytkownika
        listOfLinks.add(linkTo(ClientAccountController.class)
                .withRel("client_side"));

        // link do koszyka
        listOfLinks.add(linkTo(methodOn(ShoppingCartController.class).getShoppingCart(0,10))
                .withRel("shopping_cart"));

        // link do moich zamówień
        /*
        listOfLinks.add(linkTo(methodOn(OrderController.class).getMyOrders(0,10,"d", "orderDate",null,null,))
                .withRel("shopping_cart"));
         */
    }


    public List<Link> supplyLinks() {
        return listOfLinks;
    }
}
