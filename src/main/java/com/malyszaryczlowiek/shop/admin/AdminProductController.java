package com.malyszaryczlowiek.shop.admin;

import com.malyszaryczlowiek.shop.products.Product;
import com.malyszaryczlowiek.shop.products.ProductRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;



@RestController
@RequestMapping("/admin")
public class AdminProductController {

    private Logger logger = LoggerFactory.getLogger(AdminProductController.class);
    private final ProductRepository productRepository;



    public AdminProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }





    @RequestMapping(method = RequestMethod.GET)
    String welcomeAdminPanel() {
        return "Welcome Admin Panel";
    }


    /*
     * Metody do tworzenia kategorii
     */


    /**
     * metoda zwraca wsystkie sekcje i kategorie wraz z linkami
     *
     */
    @RequestMapping(path = "", method = RequestMethod.GET)
    public ResponseEntity<?> getListOfCategories() {

    }


    /**
     * Stwórz sekcję kategorię oraz podkategorie.
     * @return
     */
    @RequestMapping(path = "", method = )
    public ResponseEntity<?> createCategory() {

    }








    /**
     *
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addProduct(
            @Valid @RequestBody Product product, // obiekt produktu który dodajemy
            HttpServletResponse response) throws IOException { // response posłuży na przekierowanie od razu na stronę nowo dodanego produktu
        // produkt który dodajemy jest już podany walidacji
        Product addedProduct = productRepository.saveAndFlush(product);
        response.sendRedirect("/" + addedProduct.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /*
     * Tutaj otwiera mi się stronka z konkretnym produktem, który mogę zmodyfikować albo usunąć
     */


    /**
     * Method called when product was modified by frontend.
     * @param id product id
     * @return status
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<Product> modifyProduct(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody Product modifiedProduct) {

        // accepted dajemy gdy wprowadzone zmiany są zwalidowane i zostały zaakceptowane.
        return ResponseEntity.status(HttpStatus.ACCEPTED).build(); // not acceptable , partial content

    }

    /**
     * Usuwanie produktu. generalnie
     * @param id
     * @return
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Product> removeProduct(@PathVariable(name = "id") Long id) {



        ResponseEntity.status(HttpStatus.OK).build();// operacja wykonana prawidłowo
    }
}





/*
Najważniejsze informacje o sesji:


 - Unieważnianie sesji
authentication.setAuthenticated(false); // WAŻNE to unieważnia sesję.

natomiast:
sessionStatus.setComplete(); // to nie unieważnia sesji
 */





























