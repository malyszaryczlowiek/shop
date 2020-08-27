package com.malyszaryczlowiek.shop.shoppingCart;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

public class ShoppingCartModelAssembler implements RepresentationModelAssembler<ShoppingCart, ShoppingCartModel> {


    not implemented
    @Override
    public ShoppingCartModel toModel(ShoppingCart entity) {
        ShoppingCartModel model = new ShoppingCartModel(entity);
        model.add(dodać linki);
        return model;
    }

    @Override
    public CollectionModel<ShoppingCartModel> toCollectionModel(Iterable<? extends ShoppingCart> entities) {
        return null;
    }
}
