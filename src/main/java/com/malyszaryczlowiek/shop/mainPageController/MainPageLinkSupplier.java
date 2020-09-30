package com.malyszaryczlowiek.shop.mainPageController;

import com.malyszaryczlowiek.shop.client.ClientAccountController;
import com.malyszaryczlowiek.shop.client.CreateAccountController;
import com.malyszaryczlowiek.shop.shoppingCart.ShoppingCartController;

import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


public class MainPageLinkSupplier {

    private final List<Link> listOfLinks = new ArrayList<>();

    public MainPageLinkSupplier() {
        // link do głównej strony
        listOfLinks.add(linkTo(MainPageController.class)
                .withRel("main_page"));

        // link do strony użytkownika
        listOfLinks.add(linkTo(ClientAccountController.class)
                .withRel("client_side"));

        // link do koszyka
        listOfLinks.add(linkTo(ShoppingCartController.class)
                .withRel("shopping_cart"));

        //link do tworzenia konta
        listOfLinks.add(linkTo(methodOn(CreateAccountController.class).createClient())
                .withRel("create_client_account"));
    }


    public List<Link> supplyLinks() {
        return listOfLinks;
    }
}
