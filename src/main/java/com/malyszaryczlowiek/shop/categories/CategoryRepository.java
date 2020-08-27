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
    List<Category> findAllSubcategoriesInGivenCategory(
            @Param(value = "section") String section,
            @Param(value = "category") String category);


    @Query("SELECT c FROM Category c WHERE c.subcategory=:subcategory")
    List<Category> findAllSubcategoriesWithGivenCategoryAndSection(
            @Param(value = "subcategory") String subcategory);

    @Query("SELECT DISTINCT(c.category) FROM Category c WHERE c.section=:section ORDER BY c.category")
    List<String> getCategoryDescriptorsOfGivenSection(@Param(value = "section") String section);


}
