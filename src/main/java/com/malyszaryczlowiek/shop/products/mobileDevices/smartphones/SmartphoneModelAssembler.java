package com.malyszaryczlowiek.shop.products.mobileDevices.smartphones;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

public class SmartphoneModelAssembler implements RepresentationModelAssembler<Smartphone, SmartphoneModel> {
    @Override
    public SmartphoneModel toModel(Smartphone entity) {
        SmartphoneModel model = new SmartphoneModel(entity);
        model.add(); // TODO tutaj trzeba dodać jeszcze linki
        return model;
    }

    @Override
    public CollectionModel<SmartphoneModel> toCollectionModel(Iterable<? extends Smartphone> entities) {


        return null;
    }

    // TODO sprawdzić czy można extendować ProductModelAssembler?

}
