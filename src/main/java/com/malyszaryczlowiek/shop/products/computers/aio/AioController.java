package com.malyszaryczlowiek.shop.products.computers.aio;


import com.malyszaryczlowiek.shop.products.ProductModel;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * W kontrolerach obsługujących poszczególne subcategorie,
 * czyli odpowidającym będą tylko dwie metody:
 * 1. ta która szuka produktu po jego charakterystycznych kryteriach
 * 2. ta która zwraca obiekt produktu w jego pełnej wersji (fianlny
 * obiekt klasy)
 *
 */
@RestController
@RequestMapping("/product/computers/aio")
public class AioController {

    private final AioRepository aioRepository;


    public AioController(AioRepository aioRepository) {
        this.aioRepository = aioRepository;
    }



    /**
     * podaj wszystkie wyniki spełniające kryteria wyszukiwania
     *
     *
     * TODO zaimplementować szukanie i jako parametry metody
     * path variable odpowiadające
     * atrybutom entity.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<? extends ProductModel>> searchAio() {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }




}
