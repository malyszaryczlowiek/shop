package com.malyszaryczlowiek.shop.products;

import javax.validation.constraints.PositiveOrZero;

public class ProductIdOrder {

    @PositiveOrZero
    private Long id;

    @PositiveOrZero
    private Integer amount = 1;

    public ProductIdOrder() {}

    public ProductIdOrder(Long id) {
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
