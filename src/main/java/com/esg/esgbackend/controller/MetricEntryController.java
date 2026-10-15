package com.esg.esgbackend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.esg.esgbackend.entity.MetricEntry;
import com.esg.esgbackend.service.MetricEntryService;

@RestController
@RequestMapping("/api/metric-entries")
public class MetricEntryController {

    private final MetricEntryService service;

    public MetricEntryController(MetricEntryService service) {
        this.service = service;
    }

    @GetMapping
    public List<MetricEntry> getAllEntries() {
        return service.getAllEntries();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetricEntry> getEntryById(
            @PathVariable Long id) {
        MetricEntry entry = service.getEntryById(id);
        return entry != null
                ? ResponseEntity.ok(entry)
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public MetricEntry createEntry(@RequestBody MetricEntry entry) {
        return service.saveEntry(entry);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MetricEntry> updateEntry(
            @PathVariable Long id,
            @RequestBody MetricEntry entry) {
        if (service.getEntryById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        entry.setId(id);
        return ResponseEntity.ok(service.saveEntry(entry));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable Long id) {
        if (service.getEntryById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        service.deleteEntry(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reporting-period/{reportingPeriodId}")
    public List<MetricEntry> getByReportingPeriod(
            @PathVariable Long reportingPeriodId) {
        return service.getEntriesByReportingPeriod(reportingPeriodId);
    }

    @GetMapping("/metric-definition/{metricDefinitionId}")
    public List<MetricEntry> getByMetricDefinition(
            @PathVariable Long metricDefinitionId) {
        return service.getEntriesByMetricDefinition(metricDefinitionId);
    }
}
