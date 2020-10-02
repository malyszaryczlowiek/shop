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

    @Query("SELECT COUNT(p) FROM Product p WHERE p.productCategory=:category ")
    int getNumberOfProductsInCategory(@Param("category") Category category);




    @Query("SELECT p FROM Product p ORDER BY p.popularity DESC ")
    List<Product> findProductsWithDescPopularity();

    /*
    @Query("SELECT p FROM Product p ORDER BY p.prize DESC ")
    Page<Product> findProductsWithDescPrize(Pageable pageable);

    @Query("SELECT p FROM Product p ORDER BY p.prize ASC ")
    List<Product> findProductsWithAscPrize();
     */




    @Query("SELECT p FROM Product p WHERE p.productCategory.sectionDescriptor=:section " +
            "ORDER BY p.popularity DESC ")
    List<Product> findProductsInSectionWithDescPopularity(@Param("section") String section);

    @Query("SELECT p FROM Product p WHERE p.productCategory.sectionDescriptor=:section " +
            "ORDER BY p.prize DESC ")
    List<Product> findProductsInSectionWithDescPrize(@Param("section") String section);

    @Query("SELECT p FROM Product p WHERE p.productCategory.sectionDescriptor=:section " +
            "ORDER BY p.prize ASC ")
    List<Product> findProductsInSectionWithAscPrize(@Param("section") String section);






    @Query("SELECT p FROM Product p WHERE p.productCategory.categoryDescriptor=:category " +
            "ORDER BY p.popularity DESC ")
    List<Product> findProductsInCategoryWithDescPopularity(@Param("category") String category);


    @Query("SELECT p FROM Product p WHERE p.productCategory.subcategoryDescriptor=:subcategory " +
            "ORDER BY p.popularity DESC ")
    List<Product> findProductsInSubcategoryWithDescPopularity(@Param("subcategory") String subcategory);





    @Query("SELECT p FROM Product p WHERE p.productCategory=:category")
    Page<Product> findAllProductsInThisCategory(
            @Param(value = "category") Category category,
            Pageable pageable);


    @Query("SELECT p FROM Product p WHERE p.productCategory=:category")
    List<Product> findAllProductsInThisCategory(
            @Param(value = "category") Category category);


    /**
     * Find all products with this phrase
     */
    @Query("SELECT p FROM Product p WHERE p.productCategory IN (:categories) AND " +
            "( p.brand LIKE %:phrase% OR p.productName LIKE %:phrase% )")
    List<Product> findAllProductsInThisCategoryWithThisPhrase(
            @Param(value = "categories") List<Category> categories,
            @Param(value = "phrase") String phrase);

    /**
     * Find all products with this phrase
     */
    @Query("SELECT p FROM Product p WHERE p.brand LIKE %:phrase% OR p.productName LIKE %:phrase% ")
    List<Product> findAllProductsWithThisPhrase(@Param(value = "phrase") String phrase);






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
    Page<Product> findProductsInSection(
            @Param(value = "section") String section, Pageable pageable);


    @Query("SELECT p FROM Product p WHERE p.productCategory.category=:category")
    Page<Product> findAllProductsInCategory(
            @Param(value = "category") String category, Pageable pageable);


    @Query("SELECT p FROM Product p WHERE p.productCategory.subcategory=:subcategory2")
    Page<Product> findAllProductsInSubCategory(
            @Param(value = "subcategory2") String subcategory2, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.specification IN(:spec)")
    Page<Product> findAllProductsWithParameters(
            @Param(value = "spec") List<Feature> spec, Pageable pageable);



}









