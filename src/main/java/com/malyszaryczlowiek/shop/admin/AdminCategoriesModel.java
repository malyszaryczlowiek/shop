package com.malyszaryczlowiek.shop.admin;

import com.malyszaryczlowiek.shop.categories.Category;
import com.malyszaryczlowiek.shop.categories.CategoryModel;

import org.springframework.hateoas.RepresentationModel;

import java.util.LinkedList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class AdminCategoriesModel extends RepresentationModel<AdminCategoriesModel> {

    private final List<CategoryModel> listOfCategories;

    public AdminCategoriesModel(List<Category> listOfCategories) {
        this.listOfCategories = new LinkedList<>();

        listOfCategories.forEach(category ->
                this.listOfCategories.add(new CategoryModel(category)));
        this.add(
                // link do adminowej strony z kategoriami
                linkTo(AdminCategoriesController.class).withSelfRel(),
                // link do adminowej strony z kategoriami
                linkTo(AdminMainController.class).withRel("main_admin_page")
        );
    }

    public List<CategoryModel> getListOfCategories() {
        return listOfCategories;
    }
}


































