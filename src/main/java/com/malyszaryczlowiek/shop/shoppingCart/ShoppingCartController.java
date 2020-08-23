package com.malyszaryczlowiek.shop.shoppingCart;


import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(path = "/shoppingCart")
public class ShoppingCartController {

    private ShoppingCart shoppingCart;

    @Autowired
    ShoppingCartController(ShoppingCart shoppingCart){
        this.shoppingCart = shoppingCart;
    }

    /**
     *
     * @return zwraca paging w postaci mapy produktów i ilości zamówionych
     */
    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<ShoppingCartModel> showCurrentShoppingCart() {


        return ResponseEntity.status(HttpStatus.OK).build();
    }





}
