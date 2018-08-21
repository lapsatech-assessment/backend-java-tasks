package com.backend.tasks.controller;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.tasks.model.Organization;
import com.backend.tasks.service.org.OrganizationService;

@RestController
@RequestMapping(path = "/orgs")
public class OrganizationController {

    @Autowired
    OrganizationService organizationService;

    /**
     * Post to /orgs endpoint should create and return organization. Response status
     * should be 201.
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody @NotNull Organization organization) {
        final Organization result = organizationService.create(organization);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(result);
    }

    /**
     * Put to /orgs/{orgId} endpoint should update, save and return organization
     * with id=orgId.
     */
    @PutMapping("/{orgId}")
    public ResponseEntity<?> update(@PathVariable(value = "orgId") Long orgaizationId,
            @RequestBody @NotNull Organization organization) {
        try {
            final Organization result = organizationService.update(orgaizationId, organization);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get to /orgs/{orgId} endpoint should fetch and return organization with
     * id=orgId.
     */
    @GetMapping("/{orgId}")
    public ResponseEntity<?> get(@PathVariable(value = "orgId") @NotNull Long orgId) {
        try {
            final Organization result = organizationService.getSingle(orgId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete to /orgs/{orgId} endpoint should delete organization with id=orgId.
     * Response status should be 204.
     */
    @DeleteMapping("/{orgId}")
    public ResponseEntity<?> delete(@PathVariable(value = "orgId") @NotNull Long orgId) {
        try {
            organizationService.delete(orgId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get to /orgs endpoint should return list of all organizations
     */
    @GetMapping
    public ResponseEntity<?> all() {
        final Iterable<Organization> result = organizationService.getAll();
        return ResponseEntity.ok(result);
    }
}
