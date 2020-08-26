package com.malyszaryczlowiek.shop.feature;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface FeatureRepository extends JpaRepository<Feature, Long> {



}
