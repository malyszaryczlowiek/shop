package com.malyszaryczlowiek.shop.products;


import com.malyszaryczlowiek.shop.categories.Category;

import com.malyszaryczlowiek.shop.feature.Feature;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product, Long> {


    @Query("SELECT p FROM Product p WHERE p.productCategory=:category")
    Page<Product> findAllProductsInThisCategory(
            @Param(value = "category") Category category,
            Pageable pageable);


    @Query("SELECT p FROM Product p WHERE p.productCategory=:category")
    List<Product> findAllProductsInThisCategory(
            @Param(value = "category") Category category);





    /**
     * tu można chyba zaminić listę na iterable
     */
    @Query("SELECT p FROM Product p WHERE p.productCategory IN (:categoryList)")
    Page<Product> findAllProductsInTheseCategories(
            @Param(value = "categoryList") List<Category> categoryList, Pageable pageable);


    @Query("SELECT p FROM Product p WHERE p.productCategory IN (:categoryList)")
    List<Product> findAllProductsInTheseCategories(
            @Param(value = "categoryList") List<Category> categoryList);


    /**
     * metody służące do wyłuskania produktów w danej kategorii.
     */
    @Query("SELECT p FROM Product p WHERE p.productCategory.section=:section")
    Page<Product> findAllProductsInMainCategory(
            @Param(value = "section") String section, Pageable pageable);


    @Query("SELECT p FROM Product p WHERE p.productCategory.category=:subcategory1")
    Page<Product> findAllProductsInSubCategory(
            @Param(value = "subcategory1") String subcategory1, Pageable pageable);


    @Query("SELECT p FROM Product p WHERE p.productCategory.subcategory=:subcategory2")
    Page<Product> findAllProductsInSubSubCategory(
            @Param(value = "subcategory2") String subcategory2, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.specification IN(:spec)")
    Page<Product> findAllProductsWithParameters(
            @Param(value = "spec") List<Feature> spec, Pageable pageable);



}









