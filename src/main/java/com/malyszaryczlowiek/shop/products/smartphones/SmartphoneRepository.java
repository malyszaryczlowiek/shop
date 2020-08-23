package com.malyszaryczlowiek.shop.products.smartphones;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface SmartphoneRepository extends JpaRepository<Smartphone, Long> {

}
