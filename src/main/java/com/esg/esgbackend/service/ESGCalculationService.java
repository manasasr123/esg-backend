
package com.esg.esgbackend.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

@Service
public class ESGCalculationService {

    // Employee turnover percentage
    public BigDecimal calculateEmployeeTurnover(
            BigDecimal departures,
            BigDecimal averageHeadcount) {

        if (averageHeadcount == null
                || averageHeadcount.compareTo(BigDecimal.ZERO) <= 0) {
        	throw new IllegalArgumentException(
        	        "Average headcount must be greater than zero");
        }

        return departures
                .multiply(BigDecimal.valueOf(100))
                .divide(averageHeadcount, 2, RoundingMode.HALF_UP);
    }

    // Training hours per employee
    public BigDecimal calculateTrainingHoursPerEmployee(
            BigDecimal trainingHours,
            BigDecimal eligibleEmployees) {

        if (eligibleEmployees == null
                || eligibleEmployees.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }

        return trainingHours.divide(
                eligibleEmployees, 2, RoundingMode.HALF_UP);
    }

    // Independent board member percentage
    public BigDecimal calculateBoardIndependence(
            BigDecimal independentMembers,
            BigDecimal totalMembers) {

        if (totalMembers == null
                || totalMembers.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }

        return independentMembers
                .multiply(BigDecimal.valueOf(100))
                .divide(totalMembers, 2, RoundingMode.HALF_UP);
    }

    // Anti-corruption training completion percentage
    public BigDecimal calculateTrainingCompletion(
            BigDecimal completed,
            BigDecimal eligible) {

        if (eligible == null
                || eligible.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }

        return completed
                .multiply(BigDecimal.valueOf(100))
                .divide(eligible, 2, RoundingMode.HALF_UP);
    }

    // Waste recycling percentage
    public BigDecimal calculateRecyclingPercentage(
            BigDecimal recycledWaste,
            BigDecimal totalWaste) {

        if (totalWaste == null
                || totalWaste.compareTo(BigDecimal.ZERO) <= 0) {
        	throw new IllegalArgumentException(
        	        "Total waste must be greater than zero");
        }

        return recycledWaste
                .multiply(BigDecimal.valueOf(100))
                .divide(totalWaste, 2, RoundingMode.HALF_UP);
    }
}
