package com.malyszaryczlowiek.shop.categories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE c.section=:categoryName")
    List<Category> findAllCategoriesWithGivenCategoryName(
            @Param(value = "categoryName") String categoryName);


    @Query("SELECT c FROM Category c WHERE c.category=:subcategory1")
    List<Category> findAllCategoriesWithGivenSubcategory1(
            @Param(value = "subcategory1") String subcategory1);


    @Query("SELECT c FROM Category c WHERE c.subcategory=:subcategory2")
    List<Category> findAllCategoriesWithGivenSubcategory2(
            @Param(value = "subcategory2") String subcategory2);
}
