package com.malyszaryczlowiek.shop.client;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shoppingCart")
public class ClientShoppingCartController {

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getShoppingCart() {
        return ResponseEntity.status(HttpStatus.OK).body("shopping cart");
    }

    // usuń zamówienie


    // dodaj liczbę produktów



}
