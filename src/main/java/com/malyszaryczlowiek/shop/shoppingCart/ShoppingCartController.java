package com.malyszaryczlowiek.shop.shoppingCart;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    private final ShoppingCart shoppingCart;

    public ShoppingCartController(ShoppingCart shoppingCart) {
        this.shoppingCart= shoppingCart;
    }


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getShoppingCart() {

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
