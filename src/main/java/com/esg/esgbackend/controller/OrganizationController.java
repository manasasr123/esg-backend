
package com.esg.esgbackend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.esg.esgbackend.entity.Organization;
import com.esg.esgbackend.repository.OrganizationRepository;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    private final OrganizationRepository repository;

    public OrganizationController(OrganizationRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Organization> getAllOrganizations() {
        return repository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Organization createOrganization(
            @RequestBody Organization organization) {
        return repository.save(organization);
    }
}
