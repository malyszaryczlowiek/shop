package com.malyszaryczlowiek.shop.admin;

import com.malyszaryczlowiek.shop.categories.Category;
import com.malyszaryczlowiek.shop.categories.CategoryRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/admin/category")
public class AdminCategoryController {

    private final Logger logger = LoggerFactory.getLogger(AdminCategoryController.class);

    private final CategoryRepository categoryRepository;

    public AdminCategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }





    /**
     * DONE
     * Metoda zwraca wsystkie sekcje i kategorie wraz z linkami
     * Żeby móc używac metody sorted() klasa musi implementować interfejs
     * {@link Comparable}.
     *
     * @return nie zwracam tutaj Page<> ponieważ nie ma tutaj takiej potrzeby
     */
    @RequestMapping(path = "/c", method = RequestMethod.GET)
    public ResponseEntity<List<Category>> getListOfCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<Category> sortedCategories = categories.stream().sorted().collect(Collectors.toList());
        if (sortedCategories.size() == 0)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else
            return ResponseEntity.status(HttpStatus.OK).body(sortedCategories);
    }


    /**
     * DONE
     * Stwórz sekcję kategorię oraz podkategorie.
     */
    @RequestMapping(path = "/c", method = RequestMethod.POST)
    public ResponseEntity<?> createCategory(@Valid @RequestBody Category category) {
        // sprawdzamy czy kategoria która próbujemy strorzyć już nie istnieje.
        Example<Category> example = Example.of(category);
        if (categoryRepository.exists(example))
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build(); // not acceptable bo już istnieje
        categoryRepository.saveAndFlush(category);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



    /**
     * zmień nazwę kategorii
     */
    @RequestMapping(path = "/c/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<Category> renameCategoryById(
            @Valid @RequestBody Category newCategory,
            @PathVariable(name = "id") Long id) {
        if (categoryRepository.existsById(id)) {
            Optional<Category> renamedCatOptional = categoryRepository.findById(id);
            if (renamedCatOptional.isPresent()) {
                Category oldCat = renamedCatOptional.get();
                return replaceDataInCategory(oldCat, newCategory);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    /**
     * zmień nazwę kategorii i ewentualnie jej deskryptory
     */
    @RequestMapping(path = "/c", method = RequestMethod.PATCH)
    public ResponseEntity<Category> renameCategory(
            @Valid @RequestBody Category oldCategory,
            @Valid @RequestBody Category newCategory) {
        Example<Category> example = Example.of(oldCategory);
        if (categoryRepository.exists(example)) {
            Optional<Category> renamedCatOptional = categoryRepository.findOne(example);// categoryRepository.findById(id);
            if (renamedCatOptional.isPresent()) {
                Category oldCat = renamedCatOptional.get();
                return replaceDataInCategory(oldCat, newCategory);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    private ResponseEntity<Category> replaceDataInCategory(Category oldCategory, Category newCategory) {
        oldCategory.setSection(newCategory.getSection());
        oldCategory.setSectionDescriptor(newCategory.getSectionDescriptor());
        oldCategory.setCategory(newCategory.getCategory());
        oldCategory.setCategoryDescriptor(newCategory.getCategoryDescriptor());
        oldCategory.setSubcategory(newCategory.getSubcategory());
        oldCategory.setSubcategoryDescriptor(newCategory.getSubcategoryDescriptor());
        // categoryRepository.saveAndFlush(cat); // to chyba będzie mogło być usunięte
        // bo skoro pracujemy na encji, która jest w stanie managed to wszelkie zmiany
        // w niej wykonywane będą zapisywane automatycznie
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(oldCategory);
    }


    /**
     * usuwam daną kategorię, w przypadku gdy kategoria zawiera jakieś
     * produkty to należy najpierw, zapytać czy usuwamy tez wszystkie
     * produkty w kategorii czy przenosimy je do innej kategorii.
     *
     * jeśli tak to przekierowujemy na stronę dodawania kategorii.
     *
     * @return
     */
    @RequestMapping(path = "/c/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeCategory(@PathVariable(name = "id") Long id) {
        // todo to trzeba jakoś zaimpelemtnować


        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}











/*
Najważniejsze informacje o sesji:


 - Unieważnianie sesji
authentication.setAuthenticated(false); // WAŻNE to unieważnia sesję.

natomiast:
sessionStatus.setComplete(); // to nie unieważnia sesji
 */





























