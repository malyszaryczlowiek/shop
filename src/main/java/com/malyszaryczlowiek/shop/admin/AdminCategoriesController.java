package com.malyszaryczlowiek.shop.admin;

import com.malyszaryczlowiek.shop.categories.Category;
import com.malyszaryczlowiek.shop.categories.CategoryExchanger;
import com.malyszaryczlowiek.shop.categories.CategoryRepository;
import com.malyszaryczlowiek.shop.products.ProductRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


/**
 * nie można usunąć kategorii - nie ma metody delete
 */
@RestController
@RequestMapping("/admin/categories")
public class AdminCategoriesController {

    private final Logger logger = LoggerFactory.getLogger(AdminCategoriesController.class);

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public AdminCategoriesController(CategoryRepository categoryRepository,
                                     ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    /**
     * zwraca wszystkie kategorie
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<AdminCategoriesModel> getAllCategories() {
        return returnSortedListOfCategories();
    }


    /**
     * dodawanie nowej kategorii
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<AdminCategoriesModel> addCategory(
            @Valid @RequestBody Category newCategory) {
        Example<Category> example = Example.of(newCategory);
        boolean existsInDb = categoryRepository.exists(example);
        if (existsInDb)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        categoryRepository.saveAndFlush(newCategory);
        return returnSortedListOfCategories();
    }


    /**
     * zmiana nazwy kategorii
     */
    @RequestMapping(method = RequestMethod.PATCH)
    public ResponseEntity<AdminCategoriesModel> renameCategory(
            @Valid @RequestBody CategoryExchanger exchanger) {

        Example<Category> exampleOld = Example.of(exchanger.getOldCategory());
        boolean existsInDb = categoryRepository.exists(exampleOld);
        if (!existsInDb)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // albo not found nie znaleziono starej kategorii


        // odrzuć jeśli kategoria którą chcemy stworzyć już istnieje
        Example<Category> exampleNew = Example.of(exchanger.getNewCategory());
        existsInDb = categoryRepository.exists(exampleNew);
        if (existsInDb)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build(); // odrzuć jeśli już istnieje w bazie


        List<Category> listOfExampleCategories = categoryRepository.findAll(exampleOld);
        if (listOfExampleCategories.size() != 1)
            return ResponseEntity.status(HttpStatus.MULTIPLE_CHOICES).build(); // jeśli jakimś cudem pasuje więcej niz tylko jedna
        swapCategoriesNames(listOfExampleCategories.get(0), exchanger.getNewCategory());
        return returnSortedListOfCategories();
    }


    /**
     * usunąć kategorię można ale tylko wtedy gdy nie jest do niej przypisany żaden
     * produkt.
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<AdminCategoriesModel> deleteCategory(
            @Valid @RequestBody Category toDelete) {
        Example<Category> exampleOld = Example.of(toDelete);
        boolean existsInDb = categoryRepository.exists(exampleOld);
        logger.debug("czy istnieje w db categoria którą usówamy: " + existsInDb);
        if (!existsInDb)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // nie ma co usunąć


        List<Category> listOfExampleCategories = categoryRepository.findAll(exampleOld);
        logger.debug("ile jest pasujących kategorii: " + listOfExampleCategories.size());
        if (listOfExampleCategories.size() != 1)
            return ResponseEntity.status(HttpStatus.MULTIPLE_CHOICES).build();


        // ta kategoria jest już zarządzana.
        Category categoryToDelete = listOfExampleCategories.get(0);
        int productsInCategory = productRepository.getNumberOfProductsInCategory(categoryToDelete);
        logger.debug("ilość produktów w kategorii którą chcę usunąć: " + productsInCategory);
        if (productsInCategory > 0)
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        // finalnie usuwam kategorię
        categoryRepository.delete(categoryToDelete);

        // i zwracam ostateczną listę
        return returnSortedListOfCategories();
    }



    /*

    Helper methods

     */


    private ResponseEntity<AdminCategoriesModel> returnSortedListOfCategories() {
        List<Category> sortedList = categoryRepository.findAll()
                .stream()
                .sorted()
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(new AdminCategoriesModel(sortedList));
    }

    /**
     * zamiana wartości
     * @param oldCategory tutaj obiekt jest już zarządzany przez persistent context
     * @param newCategory nie jest persistent
     */
    private void swapCategoriesNames(Category oldCategory, Category newCategory) {
        oldCategory.setSection(newCategory.getSection());
        oldCategory.setCategory(newCategory.getCategory());
        oldCategory.setSubcategory(newCategory.getSubcategory());

        oldCategory.setSectionDescriptor(newCategory.getSectionDescriptor());
        oldCategory.setCategoryDescriptor(newCategory.getCategoryDescriptor());
        oldCategory.setSubcategoryDescriptor(newCategory.getSubcategoryDescriptor());
    }
}























