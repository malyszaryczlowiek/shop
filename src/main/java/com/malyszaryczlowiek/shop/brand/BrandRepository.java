package com.malyszaryczlowiek.shop.brand;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
@Transactional
public interface BrandRepository extends JpaRepository<Brand, Long> {

    @Query("SELECT DISTINCT(b.brandName) FROM Brand b")
    List<String> findAllDistinctBrandNames();
}
