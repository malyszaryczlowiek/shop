package com.malyszaryczlowiek.shop.products.mobileDevices.smartphones;

import com.malyszaryczlowiek.shop.brand.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface SmartphoneRepository extends JpaRepository<Smartphone, Long> {

    @Query("SELECT s FROM Smartphone s WHERE s.brand=:brand ")
    List<Smartphone> findAllWithBrand(@Param(value = "brand") Brand brand);


    @Query("SELECT DISTINCT(s.screenRefreshing) FROM Smartphone s")
    List<String> findAllScreenRefreshingOptions();

}
