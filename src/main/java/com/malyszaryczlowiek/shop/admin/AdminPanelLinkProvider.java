package com.malyszaryczlowiek.shop.admin;

import com.malyszaryczlowiek.shop.mainPage.MainPageController;
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
                linkTo(AdminMainController.class).withSelfRel(),
                // link do panulu z categoriami
                linkTo(AdminCategoriesController.class).withRel("admin_categories_panel"),
                // link do panulu z categoriami
                linkTo(AdminProductController.class).withRel("admin_product_panel"),
                // link do panela z użytkownikami
                linkTo(AdminUserController.class).withRel("admin_user_panel"),
                // main page
                linkTo(MainPageController.class).withRel("main_page")
        );
    }

}
