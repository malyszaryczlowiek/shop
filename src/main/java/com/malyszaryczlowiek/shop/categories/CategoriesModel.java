package com.malyszaryczlowiek.shop.categories;

import com.malyszaryczlowiek.shop.products.Product;
import com.malyszaryczlowiek.shop.products.ProductModel;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.stream.Collectors;

public class CategoriesModel extends RepresentationModel<CategoriesModel> {

    private final Page<ProductModel> pageOfProducts;
    private final List<CategoryModel> listOfCategories;


    public CategoriesModel(
            List<Category> listOfCategories, Page<Product> listOfProducts) {
        this.pageOfProducts = listOfProducts//.stream()
                .map(pro -> new ProductModel(pro, false));
                //.collect(Collectors.toList());
        this.listOfCategories = listOfCategories.stream()
                .map(CategoryModel::new)
                .collect(Collectors.toList());
    }

    public List<CategoryModel> getListOfCategories() {
        return listOfCategories;
    }

    public Page<ProductModel> getPageOfProducts() {
        return pageOfProducts;
    }
}
