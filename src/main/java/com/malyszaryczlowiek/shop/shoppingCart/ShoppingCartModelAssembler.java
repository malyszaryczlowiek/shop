package com.malyszaryczlowiek.shop.shoppingCart;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

public class ShoppingCartModelAssembler implements RepresentationModelAssembler<ShoppingCart, ShoppingCartModel> {


    //TODO not implemented
    @Override
    public ShoppingCartModel toModel(ShoppingCart entity) {
        return new ShoppingCartModel(entity);
    }

    /**
     * Tego nie trzeba implementować bo nigdy nie będziemy przerabiali więcej niż jednego koszyka
     */
    @Override
    public CollectionModel<ShoppingCartModel> toCollectionModel(Iterable<? extends ShoppingCart> entities) {
        return null;
    }
}
