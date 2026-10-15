
package com.esg.esgbackend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.esg.esgbackend.entity.ReportingPeriod;
import com.esg.esgbackend.repository.ReportingPeriodRepository;

@RestController
@RequestMapping("/api/reporting-periods")
public class ReportingPeriodController {

    private final ReportingPeriodRepository repository;

    public ReportingPeriodController(
            ReportingPeriodRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<ReportingPeriod> getAllPeriods() {
        return repository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReportingPeriod createPeriod(
            @RequestBody ReportingPeriod period) {
        period.setStatus("DRAFT");
        return repository.save(period);
    }
}
