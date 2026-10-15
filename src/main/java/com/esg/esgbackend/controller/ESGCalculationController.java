
package com.esg.esgbackend.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.esg.esgbackend.service.ESGCalculationService;

@RestController
@RequestMapping("/api/calculations")
public class ESGCalculationController {

    private final ESGCalculationService calculationService;

    public ESGCalculationController(
            ESGCalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @GetMapping("/turnover")
    public Map<String, BigDecimal> calculateTurnover(
            @RequestParam BigDecimal departures,
            @RequestParam BigDecimal averageHeadcount) {

        BigDecimal result =
                calculationService.calculateEmployeeTurnover(
                        departures, averageHeadcount);

        return Map.of("turnoverPercentage", result);
    }

    @GetMapping("/recycling")
    public Map<String, BigDecimal> calculateRecycling(
            @RequestParam BigDecimal recycledWaste,
            @RequestParam BigDecimal totalWaste) {

        BigDecimal result =
                calculationService.calculateRecyclingPercentage(
                        recycledWaste, totalWaste);

        return Map.of("recyclingPercentage", result);
    }
}
