package com.malyszaryczlowiek.shop;

import com.malyszaryczlowiek.shop.admin.AdminCategoriesModel;
import com.malyszaryczlowiek.shop.categories.Category;
import com.malyszaryczlowiek.shop.categories.CategoryExchanger;
import com.malyszaryczlowiek.shop.categories.CategoryRepository;

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

    private final CategoryRepository categoryRepository;

    @Autowired
    public AdminCategoriesController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
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























