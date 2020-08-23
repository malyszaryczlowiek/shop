package com.malyszaryczlowiek.shop.products;

import org.springframework.hateoas.CollectionModel;
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
public class ProductModelAssembler implements RepresentationModelAssembler<Product, ProductModel> {

    private Integer amountToCart = 1;

    // paging parameters
    private int page = 0;
    private int size = 20;
    private String sorting = "desc";
    private String sortBy = "time";

    private String category;


    @Override
    public ProductModel toModel(Product entity) {

        ProductModel model = new ProductModel(entity);
        model.add(); // TODO dodać linki

        /*
        model.add(
                // link do produktu samego w sobie
                linkTo(methodOn(ProductController.class).getProduct(entity.getId())).withSelfRel()
                // link do wszystkich produktów
                ,linkTo(methodOn(ProductController.class).getAllProductsInCategory(0, 20, "desc", "time"))
                        .withRel("all_products")
                // link do dodana do koszyka
                ,linkTo(methodOn(ProductController.class)
                        .putProductToShoppingCart(amountToCart, entity.getId()))
                        .withRel("put_product_in_cart"));
                 */
        return model;
    }

    /**
     * TODO napisać test sprawdzający poprawność działania metody.
     *
     * @param entities
     * @return
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


    public void setAmountToCart(Integer amount) {
        this.amountToCart = amount;
    }

    public void setPagingParameters(int page, int size, String sorting, String sortBy) {
        this.page = page;
        this.size = size;
        this.sorting = sorting;
        this.sortBy = sortBy;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}



