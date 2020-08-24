package com.malyszaryczlowiek.shop.products.mobileDevices.smartphones;


import com.malyszaryczlowiek.shop.brand.Brand;
import com.malyszaryczlowiek.shop.brand.BrandRepository;
import com.malyszaryczlowiek.shop.controllerUtil.ControllerUtil;
import com.malyszaryczlowiek.shop.products.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/product/mobile/smartphones")
public class SmartphoneController {


    private final Logger logger = LoggerFactory.getLogger(SmartphoneController.class);

    private final ControllerUtil controllerUtil;
    private final SmartphoneRepository smartphoneRepository;
    private final BrandRepository brandRepository;


    private final List<String> brandOptions;
    private final List<String> screenRefreshingOptions;

    /**
     * kluczem są parametry rządania, a wartością lista możliwych wartości.
     */
    private final Map<String, List<String>> map = new HashMap<>();

    public SmartphoneController(ControllerUtil controllerUtil,
                                SmartphoneRepository smartphoneRepository,
                                BrandRepository brandRepository) {
        this.controllerUtil = controllerUtil;
        this.smartphoneRepository = smartphoneRepository;
        this.brandRepository = brandRepository;

        this.screenRefreshingOptions = smartphoneRepository.findAllScreenRefreshingOptions();
    }


    @RequestMapping(path = "/search",method = RequestMethod.GET)
    public ResponseEntity<Page<SmartphoneModel>> searchSmartphones(
            @RequestParam(name = "prl", defaultValue = "0", required = false) String prizeMin,
            @RequestParam(name = "prm", defaultValue = "", required = false) String prizeMax,
            @RequestParam(name = "br", defaultValue = "0", required = false) Long brand, // id w bazie danych zaczyna się od 1
            @RequestParam(name = "sr", defaultValue = "", required = false) String screenRefreshing,
            // dane do paginacji
            @RequestParam(name = "page", defaultValue = "0") @PositiveOrZero int pageSize,
            @RequestParam(name = "size", defaultValue = "10") @PositiveOrZero int size,
            @RequestParam(name = "sort", defaultValue = "d") String sorting,
            @RequestParam(name = "sortBy", defaultValue = "productName") String sortBy) { // zmieni

        // w pierwszej kolejności tworzymy obiekty encji potrzebne do przeprowadzenia
        // wyszukiwania w bazie danych, bo w przeciwnym przypadku w trakcie filtrowania
        // takich zapytań wykonalibyśmy tysiące co widocznie by spowolniło działanie systemu.
        Brand productBrand;
        Stream<Smartphone> productStream;
        if (brand < 1) {
            productBrand = brandRepository.getOne(brand);
            productStream = smartphoneRepository.findAllWithBrand(productBrand).parallelStream();
        } else productStream = smartphoneRepository.findAll().parallelStream();
        final BigDecimal pMin = new BigDecimal(prizeMin);
        BigDecimal pMax = null;
        if (!prizeMax.isEmpty()) pMax = new BigDecimal(prizeMax);
        if (pMax != null && pMax.compareTo(pMin) >= 0) { // jeśli maximal prize jest ustawione to:
            final BigDecimal finalPMax = pMax;
            productStream = productStream.filter(
                    s -> s.getPrize().compareTo(finalPMax) >= 0
                            && s.getPrize().compareTo(pMin) <= 0
            );
        } // jeśli maximal prize nie jest ustawione:
        else productStream = productStream.filter(s -> s.getPrize().compareTo(pMin) >= 0);
        if (screenRefreshingOptions.contains(screenRefreshing))
            productStream = productStream.filter(s -> s.getScreenRefreshing().equals(screenRefreshing));
        SmartphoneModelAssembler assembler = new SmartphoneModelAssembler();
        List<SmartphoneModel> foundProducts = productStream.map(assembler::toModel).collect(Collectors.toList());
        //TODO napisać weryfikację wprowadzonych parametrów.
        Pageable pageable = controllerUtil.setPaging(pageSize, size,sorting, sortBy);
        Page<SmartphoneModel> page = new PageImpl<>(foundProducts, pageable, foundProducts.size());
        if (page.getTotalElements() == 0)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();




        return null;
    }

    @RequestMapping(path = "/{id}")
    public ResponseEntity<SmartphoneModel> getProduct(@PathVariable Long id) {
        if (smartphoneRepository.existsById(id)){
            Smartphone smartphone = smartphoneRepository.getOne(id);
            SmartphoneModelAssembler assembler = new SmartphoneModelAssembler();
            return ResponseEntity.status(HttpStatus.OK).body(assembler.toModel(smartphone));
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
