package com.malyszaryczlowiek.shop.admin;

import org.springframework.hateoas.RepresentationModel;

public class AdminPanelLinkProvider extends RepresentationModel<AdminPanelLinkProvider> {

    /**
     * W konstruktorze zapisuję wszystkie potrzebne linki
     */
    public AdminPanelLinkProvider() {

        this.add(); // dodaje linki
    }

}
