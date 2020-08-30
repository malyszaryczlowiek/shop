package com.malyszaryczlowiek.shop.client;


import com.malyszaryczlowiek.shop.shoppingCart.ShoppingCart;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * do koszyka dostęp ma tylko użytkownik,
 * natomiast do zamówień dostęp będzie miał zarówno
 * admin jak i client
 */
@RestController
@RequestMapping("/shoppingCart")
public class ClientShoppingCartController {

    private final ShoppingCart shoppingCart;

    public ClientShoppingCartController(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getShoppingCart() {
        return ResponseEntity.status(HttpStatus.OK).body("shopping cart");
    }

    // usuń zamówienie


    // dodaj liczbę produktów



}
