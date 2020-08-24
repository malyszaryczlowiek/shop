package com.malyszaryczlowiek.shop.products;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Opis i wyjaśnienia patrz {@link com.malyszaryczlowiek.shop.client.ClientModelAssembler
 * ClientModelAssembler}.
 */
public class ProductModelAssembler implements RepresentationModelAssembler<Product,ProductModel> {

    @Override
    public ProductModel toModel(Product entity) {
        ProductModel model = new ProductModel(entity);
        model.add(linkGenerator(entity));
        return model;
    }

    /*
    TODO aby móc korzystać z kontrolerów specyficznych dla danej podkategorii
    trzeba wyłuskiwać obiekt kategorii i porównywac go z

    sprawdzić czy rest controllery mogą dziedziczyć po jakiejść innej klasie
    i czy wtedy możemy skorzystać z polimorfizmu.
     */




    /**
     * TODO napisać test sprawdzający poprawność działania metody.
     */
    @Override
    public CollectionModel<ProductModel> toCollectionModel(Iterable<? extends Product> entities) {
        int sizeOfCollection = 0;
        for (Product entity : entities)
            ++sizeOfCollection;
        Iterator<? extends Product> iterator = entities.iterator();
        List<ProductModel> list = new ArrayList<>(sizeOfCollection);
        while (iterator.hasNext())
            list.add(toModel(iterator.next()));
        return CollectionModel.of(list);
    }


    private List<Link> linkGenerator(Product entity) {
        return List.of(
                // link do strony produktu
                linkTo(methodOn(ProductController.class)
                        .getProduct(entity.getId()))
                        .withSelfRel(),
                // link do dodania produktu do koszyka
                linkTo(methodOn(ProductController.class)
                        .putProductToShoppingCart(1, entity.getId()))
                        .withRel("shopping_cart").withName("add"),
                // link to usunięcia produktu jak zostanie dodany do koszyka
                linkTo(methodOn(ProductController.class)
                        .deleteProductFromCart(entity.getId()))
                        .withRel("shopping_cart").withName("remove")
        );
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


