
package com.esg.esgbackend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esg.esgbackend.entity.MetricDefinition;

public interface MetricDefinitionRepository
        extends JpaRepository<MetricDefinition, Long> {

    Optional<MetricDefinition> findByCode(String code);

    List<MetricDefinition> findByCategory(String category);
}
