package com.malyszaryczlowiek.shop.categories;

import javax.validation.Valid;

public class CategoryExchanger {

    @Valid
    private Category oldCategory;
    @Valid
    private Category newCategory;

    public CategoryExchanger() {}

    public Category getOldCategory() {
        return oldCategory;
    }

    public void setOldCategory(Category oldCategory) {
        this.oldCategory = oldCategory;
    }

    public Category getNewCategory() {
        return newCategory;
    }

    public void setNewCategory(Category newCategory) {
        this.newCategory = newCategory;
    }
}
