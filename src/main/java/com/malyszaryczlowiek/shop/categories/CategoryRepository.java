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


    @Query("SELECT c FROM Category c WHERE c.sectionDescriptor=:sectionDescriptor")
    List<Category> findAllCategoriesInGivenSection(
            @Param(value = "sectionDescriptor") String sectionDescriptor);


    @Query("SELECT c FROM Category c WHERE c.categoryDescriptor=:categoryDescriptor " +
            "AND c.sectionDescriptor=:sectionDescriptor")
    List<Category> findAllSubcategoriesInGivenSectionAndCategory(
            @Param(value = "sectionDescriptor") String sectionDescriptor,
            @Param(value = "categoryDescriptor") String categoryDescriptor);

    @Query("SELECT c FROM Category c WHERE c.categoryDescriptor=:categoryDescriptor " +
            "AND c.sectionDescriptor=:sectionDescriptor AND c.subcategoryDescriptor=:subcategoryDescriptor")
    List<Category> findSubcategory(
            @Param(value = "sectionDescriptor") String sectionDescriptor,
            @Param(value = "categoryDescriptor") String categoryDescriptor,
            @Param(value = "subcategoryDescriptor") String subcategoryDescriptor);


}
