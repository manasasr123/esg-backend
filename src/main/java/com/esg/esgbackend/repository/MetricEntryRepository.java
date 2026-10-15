
package com.esg.esgbackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esg.esgbackend.entity.MetricEntry;

public interface MetricEntryRepository
        extends JpaRepository<MetricEntry, Long> {

    List<MetricEntry> findByReportingPeriodId(Long reportingPeriodId);

    List<MetricEntry> findByMetricDefinitionId(Long metricDefinitionId);
}
