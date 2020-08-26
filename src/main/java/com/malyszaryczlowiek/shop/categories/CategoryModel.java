package com.malyszaryczlowiek.shop.categories;

import org.springframework.hateoas.RepresentationModel;

public class CategoryModel extends RepresentationModel<CategoryModel> {

    private final String sectionName;
    private final String categoryName;
    private final String subcategoryName;


    public CategoryModel(Category category) {
        this.sectionName = category.getSection();
        this.categoryName = category.getCategory();
        this.subcategoryName = category.getSubcategory();
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getSubcategoryName() {
        return subcategoryName;
    }
}
