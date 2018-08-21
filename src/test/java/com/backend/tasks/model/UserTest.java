package com.backend.tasks.model;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class UserTest {

    @Test
    public void hashCodeEqualsContractTest() {
        EqualsVerifier.forClass(User.class)
                .withPrefabValues(Organization.class, Organization.of(1L, "1"), Organization.of(2L, "2"))
                .withIgnoredFields("organization")
                .verify();
    }
}
