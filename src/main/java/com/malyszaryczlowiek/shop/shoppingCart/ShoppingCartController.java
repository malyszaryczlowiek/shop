package com.malyszaryczlowiek.shop.shoppingCart;

import com.malyszaryczlowiek.shop.products.ProductModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    private final ShoppingCart shoppingCart;

    public ShoppingCartController(ShoppingCart shoppingCart) {
        this.shoppingCart= shoppingCart;
    }


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<Map.Entry<ProductModel, Integer>>> getShoppingCart(
            // parametry wyświetlania strony
            @RequestParam(name = "page", defaultValue = "0", required = false) @PositiveOrZero int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) @PositiveOrZero int size)
    {
        Pageable pageable = PageRequest.of(page,size);
        List<Map.Entry<ProductModel, Integer>> list = shoppingCart.getListOfProductModels();
        Page<Map.Entry<ProductModel, Integer>> ordersPage = new PageImpl<>(list, pageable, list.size());
        return ResponseEntity.status(HttpStatus.OK).body(ordersPage);
    }


    /**
     * z zabezpieczania za pomocą {@link Secured @Secured} można zrezygnować
     * i wpisać wszystko w
     * @return
     */
    @Secured("ROLE_CLIENT")
    @RequestMapping(value = "/payment", method = RequestMethod.GET)
    ResponseEntity<?> goToPayment() {

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
