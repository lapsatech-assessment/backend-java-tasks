package com.backend.tasks.repository;

import org.springframework.data.repository.CrudRepository;

import com.backend.tasks.model.Organization;

public interface OrganizationRepository extends CrudRepository<Organization, Long> {

}
