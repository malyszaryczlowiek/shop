package com.malyszaryczlowiek.shop.order;

import com.malyszaryczlowiek.shop.shoppingCart.ShoppingCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Do kontrolera ma dostęp tylko USER.
 */
@RestController
@RequestMapping("/myOrders")
public class OrderController {

    private final ShoppingCart shoppingCart;

    @Autowired
    public OrderController(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }


    /**
     * Metoda zwraca info o zamówieniach
     * @param size
     * @param page
     * @param sorting
     * @param sortBy
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public String getShoppingCart(@RequestParam(name = "size", defaultValue = "10") int size,
                                  @RequestParam(name = "page", defaultValue = "0") int page,
                                  @RequestParam(name = "sort", defaultValue = "desc") String sorting,
                                  @RequestParam(name = "sortBy", defaultValue = "time") String sortBy) {

        return "myorders";
    }


    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String getConcreteOrder(@PathVariable(name = "id") Long id) {



        return "concrete order: " + id;
    }





}
