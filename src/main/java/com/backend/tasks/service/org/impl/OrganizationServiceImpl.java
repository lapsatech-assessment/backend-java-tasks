package com.backend.tasks.service.org.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.backend.tasks.model.Organization;
import com.backend.tasks.repository.OrganizationRepository;
import com.backend.tasks.service.org.OrganizationService;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    OrganizationRepository organizationRepository;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Iterable<Organization> getAll() {
        return organizationRepository.findAll();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Organization getSingle(Long orgaizationId) {
        Objects.requireNonNull(orgaizationId);

        final Organization result = getAndCheckFromRepo(orgaizationId);

        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Organization create(Organization organization) {
        Objects.requireNonNull(organization);

        if (organization.getId() != null && organizationRepository.existsById(organization.getId()))
            throw new IllegalArgumentException("Organization exists with given id");
        return organizationRepository.save(organization);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Organization update(Long orgaizationId, Organization organization) {
        Objects.requireNonNull(orgaizationId);
        Objects.requireNonNull(organization);

        if (organization.getId() != null && !orgaizationId.equals(organization.getId()))
            throw new IllegalArgumentException("IDs doesn't match");

        getAndCheckFromRepo(orgaizationId); // checks that already exists

        organization.setId(orgaizationId);
        return organizationRepository.save(organization);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Long orgaizationId) {
        Objects.requireNonNull(orgaizationId);

        final Organization org = getAndCheckFromRepo(orgaizationId);
        organizationRepository.delete(org);
    }

    // PRIVATE

    private Organization getAndCheckFromRepo(Long organizationId) {
        assert organizationId != null;

        final Organization organization = organizationRepository.findById(organizationId).orElse(null);
        if (organization == null)
            throw new IllegalArgumentException("Organization not exists");
        return organization;
    }

}
