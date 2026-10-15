package com.esg.esgbackend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.esg.esgbackend.entity.MetricEntry;
import com.esg.esgbackend.repository.MetricEntryRepository;

@Service
public class MetricEntryService {

    private final MetricEntryRepository metricEntryRepository;

    public MetricEntryService(
            MetricEntryRepository metricEntryRepository) {
        this.metricEntryRepository = metricEntryRepository;
    }

    public List<MetricEntry> getAllEntries() {
        return metricEntryRepository.findAll();
    }

    public MetricEntry getEntryById(Long id) {
        return metricEntryRepository.findById(id)
                .orElse(null);
    }


public MetricEntry saveEntry(MetricEntry entry) {

    String state = entry.getDataState();

    if (state == null ||
        !(state.equals("PROVIDED") ||
          state.equals("ESTIMATED") ||
          state.equals("UNAVAILABLE") ||
          state.equals("NOT_APPLICABLE"))) {

        throw new IllegalArgumentException(
            "Invalid data state. Use PROVIDED, ESTIMATED, UNAVAILABLE, or NOT_APPLICABLE."
        );
    }

    if ("UNAVAILABLE".equals(state) ||
        "NOT_APPLICABLE".equals(state)) {

        if (entry.getUnavailableReason() == null ||
            entry.getUnavailableReason().trim().isEmpty()) {

            throw new IllegalArgumentException(
                "A reason is required when data is unavailable or not applicable."
            );
        }

        entry.setReportedValue(null);
    } else {
        if (entry.getReportedValue() == null) {
            throw new IllegalArgumentException(
                "Reported value is required for provided or estimated data."
            );
        }

        if (entry.getReportedValue().signum() < 0) {
            throw new IllegalArgumentException(
                "Reported value cannot be negative."
            );
        }
    }

    if ("ESTIMATED".equals(state)) {
        entry.setMeasurementStatus("ESTIMATED");
    }

    return metricEntryRepository.save(entry);
}


    public void deleteEntry(Long id) {
        metricEntryRepository.deleteById(id);
    }

    public List<MetricEntry> getEntriesByReportingPeriod(
            Long reportingPeriodId) {
        return metricEntryRepository
                .findByReportingPeriodId(reportingPeriodId);
    }

    public List<MetricEntry> getEntriesByMetricDefinition(
            Long metricDefinitionId) {
        return metricEntryRepository
                .findByMetricDefinitionId(metricDefinitionId);
    }
}
