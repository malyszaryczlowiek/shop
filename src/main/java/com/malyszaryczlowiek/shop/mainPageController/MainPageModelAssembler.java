package com.malyszaryczlowiek.shop.mainPageController;

import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.Nullable;


public class MainPageModelAssembler implements RepresentationModelAssembler<Object,MainPageModel> {

    private final MainPageLinkSupplier linkSupplier;

    public MainPageModelAssembler(MainPageLinkSupplier linkSupplier) {
        this.linkSupplier = linkSupplier;
    }

    @Override
    public MainPageModel toModel(@Nullable Object entity) {
        MainPageModel model = new MainPageModel();
        model.add(linkSupplier.supplyLinks());
        return model;
    }
}
