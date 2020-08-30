package com.malyszaryczlowiek.shop.controllerUtil;


import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

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
        if (sorting.equals("d")) sort = Sort.by(Sort.Direction.DESC, sortBy);
        else sort = Sort.by(Sort.Direction.ASC, sortBy);
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