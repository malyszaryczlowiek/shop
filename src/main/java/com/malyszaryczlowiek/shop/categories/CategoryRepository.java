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
    List<Category> findAllCategoriesInGivenSection(
            @Param(value = "section") String section);


    @Query("SELECT c FROM Category c WHERE c.category=:category AND c.section=:section")
    List<Category> findAllSubcategoriesInGivenSectionAndCategory(
            @Param(value = "section") String section,
            @Param(value = "category") String category);

    @Query("SELECT c FROM Category c WHERE c.category=:category AND c.section=:section " +
            "AND c.subcategory=:subcategory")
    List<Category> findSubcategory(
            @Param(value = "section") String section,
            @Param(value = "category") String category,
            @Param(value = "subcategory") String subcategory);


}
