package com.malyszaryczlowiek.shop.feature;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
@Transactional
public interface FeatureRepository extends JpaRepository<Feature, Long> {

    @Query("SELECT  DISTINCT f.featureSearchingDescriptor, f.featureName FROM Feature f ")
    List<String[]> getDescriptorMap();

}
