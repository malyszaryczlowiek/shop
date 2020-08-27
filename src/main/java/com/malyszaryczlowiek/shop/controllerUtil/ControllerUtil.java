package com.malyszaryczlowiek.shop.controllerUtil;

import com.malyszaryczlowiek.shop.categories.Category;
import com.malyszaryczlowiek.shop.categories.CategoryRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.util.List;




@Component
public class ControllerUtil {

    /*
    private final Logger logger = LoggerFactory.getLogger(ControllerUtil.class);
    private final CategoryRepository categoryRepository;

    public ControllerUtil(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
     */


    /**
     * TODO zmienić jeszcze sortowanie (sortBy) po odpowienich paramtrach
     * ustaawia paging wyświtlanej storny z danymi
     */
    public Pageable setPaging(int page, int size, String sorting, String... sortBy) {
        Sort sort;
        if (sorting.equals("a")) sort = Sort.by(Sort.Direction.ASC, sortBy);
        else sort = Sort.by(Sort.Direction.DESC, sortBy);
        return  PageRequest.of(page, size, sort);
    }
}


/*

     * wyłuskuje z db obiekty categorii jakie spełniją dana ścieżka

public List<Category> getListOfCategories(String section, String category, String subcategory) {
    //Category cat = new Category(section, category, subcategory);
    Example<Category> example = Example.of(cat);
    List<Category> categoryList = categoryRepository.findAll(example);
    logger.debug("Number of Categories object : " + categoryList.size());
    return categoryList;
}
 */