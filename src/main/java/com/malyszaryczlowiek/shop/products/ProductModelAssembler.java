package com.malyszaryczlowiek.shop.products;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Opis i wyjaśnienia patrz {@link com.malyszaryczlowiek.shop.client.ClientModelAssembler
 * ClientModelAssembler}.
 */
public class ProductModelAssembler implements RepresentationModelAssembler<Product,ProductModel> {

    private boolean additionalSpecification = false;

    @Override
    public ProductModel toModel(Product entity) {
        return new ProductModel(entity, additionalSpecification);
    }


    public void setAdditionalSpecification(boolean additionalSpecification) {
        this.additionalSpecification = additionalSpecification;
    }

    /**
     * TODO napisać test sprawdzający poprawność działania metody.
     */
    @Override
    public CollectionModel<ProductModel> toCollectionModel(Iterable<? extends Product> entities) {
        int sizeOfCollection = 0;
        for (Product entity : entities) ++sizeOfCollection;
        Iterator<? extends Product> iterator = entities.iterator();
        List<ProductModel> list = new ArrayList<>(sizeOfCollection);
        while (iterator.hasNext()) list.add(toModel(iterator.next()));
        return CollectionModel.of(list);
    }

}














/*
public void setPagingParameters(int page, int size, String sorting, String sortBy) {
        this.page = page;
        this.size = size;
        this.sorting = sorting;
        this.sortBy = sortBy;
    }

    public void setCategory(String category) {
        this.category = category;
    }
 */

/*
// link to concrete categories
                linkTo(methodOn(ProductController.class)
                        .getAllProductsInSubSubCategory(
                                entity.getCategory().getCategoryName(),
                                entity.getCategory().getSubcategory1(),
                                entity.getCategory().getSubcategory2(),
                                page, size, sorting, sortBy))
                        .withRel("categories").withName("subcategory2"),
                linkTo(methodOn(ProductController.class)
                        .getAllProductsInSubCategory(
                                entity.getCategory().getCategoryName(),
                                entity.getCategory().getSubcategory1(),
                                page, size, sorting, sortBy))
                        .withRel("categories").withName("subcategory1"),
                linkTo(methodOn(ProductController.class)
                        .getAllProductsInCategory(
                                entity.getCategory().getCategoryName(),
                                page, size, sorting, sortBy))
                        .withRel("categories").withName("mainCategory")
 */


