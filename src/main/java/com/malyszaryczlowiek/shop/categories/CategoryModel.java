package com.malyszaryczlowiek.shop.categories;

import com.malyszaryczlowiek.shop.mainPageController.MainPageController;

import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class CategoryModel extends RepresentationModel<CategoryModel> {

    private final String section;
    private final String category;
    private final String subcategory;


    public CategoryModel(Category category) {
        this.section = category.getSection();
        this.category = category.getCategory();
        this.subcategory = category.getSubcategory();

        this.add(
                // link do kategorii ale bez request parameters
                linkTo(MainPageController.class)
                        .slash(category.getSectionDescriptor())
                        .slash(category.getCategoryDescriptor())
                        .slash(category.getSubcategoryDescriptor())
                        .withSelfRel()
                        .withType("GET")
        );
    }

    public String getSection() {
        return section;
    }

    public String getCategory() {
        return category;
    }

    public String getSubcategory() {
        return subcategory;
    }
}






/*
                category.getSectionDescriptor(),
                        category.getCategoryDescriptor(),
                        category.getSubcategoryDescriptor()
,
                // linki do kategorii z request parameters
                linkTo(methodOn(MainPageController.class)
                        .getAllProductsInSubcategoryWithPaging(
                                category.getSectionDescriptor(),
                                category.getCategoryDescriptor(),
                                category.getSubcategoryDescriptor()
                                // deafoultowe parametry wyświetlania strony
                                ,0, 20, "d", "popularity"
                        ))
                        .withRel(category.getCategoryDescriptor())
                        .withType("GET")
                        //.withName(category.getCategory())
 */










/*
List<Link> links = new ArrayList<>(listOfCategories.size());
        if (linksForSubcategories) { // tutaj generuję tylko linki do podkategorii.
            // te linki zwracają produkty ukryte w kategoriakch
            for(Category cat: listOfCategories) {

                links.add(link);
            }
        } else {
            for(Category cat: listOfCategories) {
                Link link = linkTo(methodOn(MainPageController.class)
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
 */












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
