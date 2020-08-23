package com.malyszaryczlowiek.shop.products.computers.aio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AioRepository extends JpaRepository<AiO, Long> {


}
