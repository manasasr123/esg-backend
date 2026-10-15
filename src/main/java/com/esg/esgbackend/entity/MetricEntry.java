
package com.esg.esgbackend.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "metric_entries")
public class MetricEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reporting_period_id", nullable = false)
    private ReportingPeriod reportingPeriod;

    @ManyToOne
    @JoinColumn(name = "metric_definition_id", nullable = false)
    private MetricDefinition metricDefinition;

    @Column(nullable = true, precision = 19, scale = 4)
    private BigDecimal reportedValue;

    private String notes;
    
    @Column(length = 1000)
    private String unavailableReason;
    
    @Column(nullable = false)
    private String dataState = "PROVIDED";

    @Column(nullable = false)
    private String measurementStatus = "MEASURED";

    private String method;
    private String dataSource;
    private String assumptions;
    private String unit;
    
    public String getDataState() {
        return dataState;
    }

    public void setDataState(String dataState) {
        this.dataState = dataState;
    }

    public String getMeasurementStatus() {
        return measurementStatus;
    }

    public void setMeasurementStatus(String measurementStatus) {
        this.measurementStatus = measurementStatus;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getAssumptions() {
        return assumptions;
    }

    public void setAssumptions(String assumptions) {
        this.assumptions = assumptions;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public MetricEntry() {
    }
    

public void setId(Long id) {
    this.id = id;
}


    public Long getId() {
        return id;
    }

    public ReportingPeriod getReportingPeriod() {
        return reportingPeriod;
    }

    public void setReportingPeriod(ReportingPeriod reportingPeriod) {
        this.reportingPeriod = reportingPeriod;
    }

    public MetricDefinition getMetricDefinition() {
        return metricDefinition;
    }

    public void setMetricDefinition(MetricDefinition metricDefinition) {
        this.metricDefinition = metricDefinition;
    }

    public BigDecimal getReportedValue() {
        return reportedValue;
    }

    public void setReportedValue(BigDecimal reportedValue) {
        this.reportedValue = reportedValue;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public String getUnavailableReason() {
        return unavailableReason;
    }

    public void setUnavailableReason(String unavailableReason) {
        this.unavailableReason = unavailableReason;
    }
}
