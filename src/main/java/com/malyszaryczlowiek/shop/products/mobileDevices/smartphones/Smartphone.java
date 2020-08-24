package com.malyszaryczlowiek.shop.products.mobileDevices.smartphones;

import com.malyszaryczlowiek.shop.categories.Category;
import com.malyszaryczlowiek.shop.products.Product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "smartphones")
public final class Smartphone extends Product {

    @Column(name = "screen_refreshing")
    private String screenRefreshing;


    // TODO nie wiem czy dzidzicząć po klasie w pustym konstruktorze nie
    // nie trzeba w jego środku wywołać super()
    public Smartphone() {}

    public Smartphone(String productName, BigDecimal prize, Category category, String screenRefreshing) {
        super(productName, prize, category);
        this.screenRefreshing = screenRefreshing;
    }

    public String getScreenRefreshing() {
        return screenRefreshing;
    }

    public void setScreenRefreshing(String screenRefreshing) {
        this.screenRefreshing = screenRefreshing;
    }
}
