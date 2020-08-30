package com.malyszaryczlowiek.shop.categories;

import com.malyszaryczlowiek.shop.products.ProductController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class CategoryModel extends RepresentationModel<CategoryModel> {

    public CategoryModel(List<Category> listOfCategories, boolean linksForSubcategories) {
        List<Link> links = new ArrayList<>(listOfCategories.size());
        if (linksForSubcategories) { // tutaj generuję tylko linki do podkategorii.
            // te linki zwracają produkty ukryte w kategoriakch
            for(Category cat: listOfCategories) {
                // linki do kategorii
                Link link = linkTo(methodOn(ProductController.class)
                        .getAllProductsInSubcategory(cat.getSectionDescriptor(),
                                cat.getCategoryDescriptor(), cat.getSubcategoryDescriptor()
                                // parametry wyświetlania strony
                                ,0, 20, "a", "productName"
                        ))
                        .withRel(cat.getCategoryDescriptor())
                        .withName(cat.getCategory());
                links.add(link);
            }
        } else {
            for(Category cat: listOfCategories) {
                Link link = linkTo(methodOn(ProductController.class)
                        .getAllSubcategoriesInCategory(cat.getSectionDescriptor(), cat.getCategoryDescriptor()))
                        .withRel(cat.getCategoryDescriptor())
                        .withName(cat.getCategory());
                links.add(link);
            }
            // tutaj jeszcze czyszczenie z duplikatów linków
            links = links.stream().distinct().collect(Collectors.toList());
        }
        // tuaj dodaje linki
        this.add(links);
    }
}

















/*
przykładowe linki

model.add(// link do kategorii
                linkTo(methodOn(ProductController.class)
                        .getAllCategoriesInSection(entity.getSectionDescriptor()))
                        .withSelfRel(),
                // link do podkategorii
                linkTo(methodOn(ProductController.class)
                        .getAllSubcategoriesInCategory(entity.getSectionDescriptor(), entity.getCategoryDescriptor()))
                        .withSelfRel().withRel(entity.getCategoryDescriptor()).withRel("sldgfj"));
 */

/*

// old implementation
w tej implementacji brałem pojedyńczą kategrie a nie listę
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

 */