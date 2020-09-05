package com.malyszaryczlowiek.shop.products;

import com.malyszaryczlowiek.shop.categories.Category;
import com.malyszaryczlowiek.shop.converters.ModelToEntityParser;

public class ProductFromModelParser implements ModelToEntityParser<Product, ProductModel> {

    @Override
    public Product parseToEntity(ProductModel model) {
        Product product = new Product();
        Category category = new Category();


        return product;
    }
}
