package com.malyszaryczlowiek.shop.products.computers;

import com.malyszaryczlowiek.shop.categories.Category;
import com.malyszaryczlowiek.shop.products.Product;

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
public interface ComputerRepository extends JpaRepository<Computer, Long> {

    @Query("SELECT p FROM Computer p WHERE p.category=:category")
    Page<Computer> findAllProductsInThisCategory(
            @Param(value = "category") Category category,
            Pageable pageable);


    /**
     * tu można chyba zaminić listę na iterable
     */
    @Query("SELECT p FROM Computer p WHERE p.category IN (:categoryList)")
    Page<Product> findAllProductsInTheseCategories(
            @Param(value = "categoryList") List<Category> categoryList,
            Pageable pageable);


    @Query("SELECT p FROM Computer p WHERE p.category.categoryName=:categoryName")
    Page<Product> findAllProductsInMainCategory(
            @Param(value = "categoryName") String categoryName,
            Pageable pageable);


    @Query("SELECT p FROM Computer p WHERE p.category.subcategory1=:subcategory1")
    Page<Product> findAllProductsInSubCategory(
            @Param(value = "subcategory1") String subcategory1,
            Pageable pageable);


    @Query("SELECT p FROM Computer p WHERE p.category.subcategory2=:subcategory2")
    Page<Product> findAllProductsInSubSubCategory(
            @Param(value = "subcategory2") String subcategory2,
            Pageable pageable);
}
