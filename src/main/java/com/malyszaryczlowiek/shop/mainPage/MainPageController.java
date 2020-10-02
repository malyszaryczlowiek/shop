package com.malyszaryczlowiek.shop.mainPage;


import com.malyszaryczlowiek.shop.categories.*;
import com.malyszaryczlowiek.shop.controllerUtil.ControllerUtil;
import com.malyszaryczlowiek.shop.products.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.PositiveOrZero;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/")
public class MainPageController {

    private final Logger logger = LoggerFactory.getLogger(MainPageController.class);


    private final ControllerUtil controllerUtil;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public MainPageController(ControllerUtil controllerUtil,
                              ProductRepository productRepository,
                              CategoryRepository categoryRepository) {
            this.controllerUtil = controllerUtil;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }


    /**
     * Strona startowa aplikacji
     *
     * @return powitanie
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> welcomePage(
            @RequestParam(name = "phrase", required = false) String phrase,
            @RequestParam(name = "page", defaultValue = "0", required = false) @PositiveOrZero int page,
            @RequestParam(name = "size", defaultValue = "20", required = false) @PositiveOrZero int size,
            @RequestParam(name = "sort", defaultValue = "d", required = false) String sorting,
            @RequestParam(name = "sortBy", defaultValue = "popularity", required = false) String sortBy) {

        if ( !List.of(10,20,40).contains(size) || !List.of("popularity", "prize").contains(sortBy))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        Pageable pageable = controllerUtil.setPaging(page, size, sorting, sortBy);
        if (phrase != null) {
            if (phrase.isBlank() || phrase.isEmpty())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            return getProductsFromPhrase(phrase, pageable);
        }
        else {
            List<Product> listOfProducts = productRepository.findProductsWithDescPopularity()
                    .stream().limit(size).collect(Collectors.toList());
            Page<Product> pageOfProducts = new PageImpl<>(listOfProducts, pageable, listOfProducts.size());
            //MainPageModel model = new MainPageModel(categoryRepository.findAll(), pageOfProducts);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new CategoriesModel(categoryRepository.findAll(), pageOfProducts));
        }
    }

//przerefaktoryzować tak aby zwracały jednolicie page produktów z listą kategorii;

    /**
     * to jest metoda z null string jako kategorią.
     */
    @RequestMapping(path = "/{section}", method = RequestMethod.GET)
    public ResponseEntity<CategoriesModel> getAllCategoriesInSection(
            @PathVariable(name = "section") String section,
            @RequestParam(name = "page", defaultValue = "0", required = false) @PositiveOrZero int page,
            @RequestParam(name = "size", defaultValue = "20", required = false) @PositiveOrZero int size,
            @RequestParam(name = "sort", defaultValue = "d", required = false) String sorting,
            @RequestParam(name = "sortBy", defaultValue = "popularity", required = false) String sortBy) {

        if ( !List.of(10,20,40).contains(size) || !List.of("popularity", "prize").contains(sortBy))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        Pageable pageable = controllerUtil.setPaging(page, size, sorting, sortBy);
        Page<Product> pageOfProducts = productRepository.findProductsInSection(section, pageable);
        List<Category> categories = categoryRepository.findAllCategoriesInGivenSection(section);
        return getListOfCategories(categories, pageOfProducts);
    }


    /**
     * to jest metoda z empty string jako kategorią.
     */
    @RequestMapping(path = "/{section}/{category}", method = RequestMethod.GET)
    public ResponseEntity<CategoriesModel> getAllSubcategoriesInCategory(
            @PathVariable(name = "section") String section,
            @PathVariable(name = "category") String category,

            @RequestParam(name = "page", defaultValue = "0", required = false) @PositiveOrZero int page,
            @RequestParam(name = "size", defaultValue = "20", required = false) @PositiveOrZero int size,
            @RequestParam(name = "sort", defaultValue = "d", required = false) String sorting,
            @RequestParam(name = "sortBy", defaultValue = "popularity", required = false)String sortBy) {

        if ( !List.of(10,20,40).contains(size) || !List.of("popularity", "prize").contains(sortBy))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        Pageable pageable = controllerUtil.setPaging(page, size, sorting, sortBy);
        List<Category> categories = categoryRepository.findAllSubcategoriesInGivenSectionAndCategory(section, category);
        Page<Product> productList = productRepository.findAllProductsInCategory(category, pageable);
        return getListOfCategories(categories, productList);
    }


    /**
     * Defaultowa metoda która wyłuskuje wszystkie produkty w danej podkategorii,
     * bez jakiegokolwiek sortowania i  ustawień pagingu.
     */
    @RequestMapping(path = "/{section}/{category}/{subcategory}", method = RequestMethod.GET)
    public ResponseEntity<Page<ProductModel>> getAllProductsInSubcategoryWithPaging(
            @PathVariable(name = "section") String section,
            @PathVariable(name = "category") String category,
            @PathVariable(name = "subcategory") String subcategory,
            // parametry wyświetlania strony
            @RequestParam(name = "page", defaultValue = "0", required = false) @PositiveOrZero int page,
            @RequestParam(name = "size", defaultValue = "20", required = false) @PositiveOrZero int size,
            @RequestParam(name = "sort", defaultValue = "d", required = false) String sorting,
            @RequestParam(name = "sortBy", defaultValue = "popularity", required = false) String sortBy) {

        Pageable pageable = controllerUtil.setPaging(page, size, sorting, sortBy);
        List<Category> categories = categoryRepository.findSubcategory(section, category, subcategory);
        return getProducts(categories, null, pageable);
    }


    /**
     * Wyszukiwanie produktów po ich paramtrach. Parametry wyszukiwania zawarte są w
     * obiekcie {@link SearchingCriteria}.
     */
    @RequestMapping(path = "/{section}/{category}/{subcategory}", method = RequestMethod.POST)
    public ResponseEntity<Page<ProductModel>> getProductsFromSearchingCriteria(
            @RequestBody SearchingCriteria searchingCriteria, // kryteria wyszukiwawcze
            @PathVariable(name = "section") String section,
            @PathVariable(name = "category") String category,
            @PathVariable(name = "subcategory") String subcategory,
            // parametry wyświetlania strony
            @RequestParam(name = "page", defaultValue = "0", required = false) @PositiveOrZero int page,
            @RequestParam(name = "size", defaultValue = "20", required = false) @PositiveOrZero int size,
            @RequestParam(name = "sort", defaultValue = "d", required = false) String sorting,
            @RequestParam(name = "sortBy", defaultValue = "popularity", required = false) String sortBy) {

        Pageable pageable = controllerUtil.setPaging(page, size, sorting, sortBy);
        List<Category> categories = categoryRepository.findSubcategory(section, category, subcategory);
        return getProducts(categories, searchingCriteria, pageable);
    }


    /*
    ####################################
    helper methods
    ####################################
     */


    private ResponseEntity<Page<ProductModel>> getProductsFromPhrase(String phrase, Pageable pageable) {
        List<Product> listOfProducts = productRepository.findAllProductsWithThisPhrase(phrase);
        if (listOfProducts.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return convertListOfProductsToPageOfProductModel(listOfProducts,pageable);
    }


    private ResponseEntity<Page<ProductModel>> convertListOfProductsToPageOfProductModel(
            List<Product> listOfProducts, Pageable pageable) {
        Page<Product> pageOfProducts = new PageImpl<>(listOfProducts, pageable, listOfProducts.size());
        Page<ProductModel> finalPage = pageOfProducts.map(
                product -> new ProductModel(product, false));
        return ResponseEntity.status(HttpStatus.OK).body(finalPage);
    }


    private ResponseEntity<CategoriesModel> getListOfCategories(
            List<Category> categories, Page<Product> listOfProducts) {
        if (categories.isEmpty()) {
            logger.debug("jest empty - nie ma takiej sekcji");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(new CategoriesModel(categories,listOfProducts));
    }


    /**
     * DONE - przetestowane
     *
     * ta metoda bardziej obciąża pamięć bo wczytuje dużo danych, które następnie odfiltrowuje,
     * jest natomiast szybsza pod kątem wczytania danych z DB?
     */
    private ResponseEntity<Page<ProductModel>> getProducts(
            List<Category> categories, SearchingCriteria searchingCriteria, Pageable pageable) {
        if (categories.isEmpty()) {
            logger.debug("jest empty - nie ma takiej sekcji");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<Product> listOfProducts = productRepository.findAllProductsInTheseCategories(categories);
        if (listOfProducts.size() == 0) {
            logger.debug("nie ma produktów w tej kategorii. ");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        // jeśli istnieją jakieś niezerowe zadane kryteria wyszukiwania to
        // należy wg. nich odfiltrować wyniki
        // musi być większe od jeden
        if ( searchingCriteria != null && searchingCriteria.getSearchingParameters().size() > 0) {
            logger.debug("SearchingCriteria nie jest null i ma mapę większa od 0.");
            searchingCriteria.getSearchingParameters().forEach( (descriptorToFind, listOfValues) -> {
                Iterator<Product> productIterator = listOfProducts.iterator();
                while (productIterator.hasNext()) {
                    boolean doesProductFulfilSearchingCriteria = productIterator.next()
                            .getSpecification()
                            .stream()
                            .anyMatch(
                                    // pierwszy warunek sprawdza czy feature ma ten descryptor
                                    feature -> feature.getFeatureSearchingDescriptor().equals(descriptorToFind)
                                            // jeśli go ma to sprawdza czy wartość tego descryptora
                                            // jest w szukanych wartościach.
                                            && listOfValues.contains(feature.getFeatureValue())
                                    // jeśli to zwróci true to znaczy, że produkt zawiera feature ze wskazaną wartością
                                    // i nie należy go usówać
                            );
                    if ( !doesProductFulfilSearchingCriteria ) productIterator.remove();
                }
            });
        }
        if (listOfProducts.size() == 0) {
            logger.debug("po przefiltrowaniu produktów nie znaleziono żadnego pasującego wyniku.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        logger.debug("ilość produktów po odfiltrowaniu niepasujących wynosi: " + listOfProducts.size());
        return convertListOfProductsToPageOfProductModel(listOfProducts, pageable);
    }
}































    /*
        String phrase;
        List<Product> listOfProducts;
        if (searchingCriteria.getSearchingParameters().containsKey("phrase") &&
                searchingCriteria.getSearchingParameters().get("phrase").size() == 1) {
            phrase = searchingCriteria.getSearchingParameters().get("phrase").get(0);
            listOfProducts =
                    productRepository.findAllProductsInThisCategoryWithThisPhrase(categories, phrase);
            searchingCriteria.getSearchingParameters().remove("phrase"); // trzeba usunąć
        }
        else
         */





/*
    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    void logout(HttpServletRequest request,
                HttpServletResponse response,
                Authentication authentication) throws IOException {

        Cookie[] cookies = request.getCookies();
        if (cookies != null)
            for (Cookie cookie: cookies)
                cookie.setMaxAge(0);

        HttpSession session = request.getSession(false);
        if (session != null) {
            logger.debug("Invalidating session: " + session.getId());
            session.invalidate();
        }
        authentication.setAuthenticated(false);
        response.sendRedirect("/");
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(null);
		SecurityContextHolder.clearContext();
		logger.debug("proces wylogowania zakończony");
    }
 */