package com.malyszaryczlowiek.shop.admin;

import com.malyszaryczlowiek.shop.products.ProductController;

import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class AdminPanelLinkProvider extends RepresentationModel<AdminPanelLinkProvider> {

    /**
     * W konstruktorze zapisuję wszystkie potrzebne linki
     */
    public AdminPanelLinkProvider() {
        this.add(
                // link do siebie samego
                linkTo(AdminCategoryController.class).withSelfRel(),
                // link do panela z użytkownikami
                linkTo(AdminUserController.class).withRel("admin_user_panel"),
                // main page
                linkTo(ProductController.class).withRel("main_page")
        );
    }

}
