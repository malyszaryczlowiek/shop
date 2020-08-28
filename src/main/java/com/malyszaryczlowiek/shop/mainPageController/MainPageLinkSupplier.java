package com.malyszaryczlowiek.shop.mainPageController;

import com.malyszaryczlowiek.shop.client.ClientAccountController;
import com.malyszaryczlowiek.shop.client.ClientShoppingCartController;
import com.malyszaryczlowiek.shop.order.OrderController;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

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
        listOfLinks.add(linkTo(methodOn(ClientAccountController.class))
                .withRel("client_side"));

        // link do koszyka
        listOfLinks.add(linkTo(methodOn(ClientShoppingCartController.class).getShoppingCart())
                .withRel("client_shopping_cart"));

        // link do moich zamówień
        listOfLinks.add(linkTo(methodOn(OrderController.class))
                .withRel("shopping_cart"));
    }


    public List<Link> supplyLinks() {
        return listOfLinks;
    }


    /**
     * tego nie ma na razie sensu implementować
     */
    @Deprecated
    public void addLink() {

    }
}
