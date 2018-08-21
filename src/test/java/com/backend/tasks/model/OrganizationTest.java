package com.backend.tasks.model;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class OrganizationTest {

    @Test
    public void hashCodeEqualsContractTest() {
        EqualsVerifier.forClass(Organization.class)
                .withPrefabValues(User.class, User.of(1L, "1", "1"), User.of(2L, "2", "2"))
                .withIgnoredFields("users")
                .verify();
    }
}
