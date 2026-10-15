
package com.esg.esgbackend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.esg.esgbackend.entity.MetricDefinition;
import com.esg.esgbackend.repository.MetricDefinitionRepository;

@RestController
@RequestMapping("/api/metric-definitions")
public class MetricDefinitionController {

    private final MetricDefinitionRepository repository;

    public MetricDefinitionController(
            MetricDefinitionRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<MetricDefinition> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public MetricDefinition create(
            @RequestBody MetricDefinition definition) {

        if (definition.getCode() == null ||
            definition.getCode().trim().isEmpty() ||
            definition.getName() == null ||
            definition.getName().trim().isEmpty() ||
            definition.getCategory() == null ||
            definition.getCategory().trim().isEmpty() ||
            definition.getUnit() == null ||
            definition.getUnit().trim().isEmpty()) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Code, name, category, and unit are required."
            );
        }

        if (repository.findByCode(definition.getCode().trim())
                .isPresent()) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "A metric with this code already exists."
            );
        }

        definition.setCode(definition.getCode().trim());
        definition.setName(definition.getName().trim());
        definition.setCategory(
            definition.getCategory().trim().toUpperCase());
        definition.setUnit(definition.getUnit().trim());

        return repository.save(definition);
    }
}
