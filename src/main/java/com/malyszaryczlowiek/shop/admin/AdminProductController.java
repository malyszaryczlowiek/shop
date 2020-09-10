package com.malyszaryczlowiek.shop.admin;

import com.malyszaryczlowiek.shop.products.Product;
import com.malyszaryczlowiek.shop.products.ProductRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/admin/product")
public class AdminProductController {

    private final Logger logger = LoggerFactory.getLogger(AdminProductController.class);
    private final ProductRepository productRepository;


    public AdminProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }



    /**
     * dodaje produkt do bazy danych o ile ten w niej już nie istnieje.
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addProduct(
            @Valid @RequestBody Product product, // obiekt produktu który dodajemy
            HttpServletResponse response) throws IOException { // response posłuży na przekierowanie od razu na stronę nowo dodanego produktu
        // produkt który dodajemy jest już poddany walidacji
        Example<Product> example = Example.of(product);
        if (productRepository.exists(example))
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build(); // nie akceptowalne, produkt już istnieje w bazie.
        Product addedProduct = productRepository.saveAndFlush(product);
        response.sendRedirect("/" + addedProduct.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build(); // product utworzony
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
        if ( !modifiedProduct.getId().equals(id) )
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // jeśli id się nie zgadzają to występuje conflikt
        Example<Product> example = Example.of(modifiedProduct);
        if (productRepository.exists(example)) { // sprawdź czy produkt już nie istnieje
            Product returned = productRepository.saveAndFlush(modifiedProduct);
            // accepted dajemy gdy wprowadzone zmiany są zwalidowane i zostały zaakceptowane.
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(returned); // accepted - zmiany zaakceptowane
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build(); // not acceptable , partial content
    }



    /**
     * Usuwanie produktu. generalnie
     *
     * @param id produktu do usunięcia
     * @return status of operation.
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Product> removeProduct(@PathVariable(name = "id") Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).build();// operacja wykonana prawidłowo
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // nie można usunąć z bazy produktu którego nie ma
    }
}
