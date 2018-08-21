package com.backend.tasks.service.org.impl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.backend.tasks.model.Organization;
import com.backend.tasks.repository.OrganizationRepository;
import com.backend.tasks.service.org.OrganizationService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { OrganizationServiceImpl.class })
public class OrganizationServiceImplTest {

    @Autowired
    OrganizationService service;

    @MockBean
    OrganizationRepository organizationRepositoryMock;

    // getAll

    @Test
    public void givenExistingOrganization_whenGetAll_thenReturnIterable() {

        // given
        final Organization org1 = Organization.of(200L, "Organization 1");
        final Organization org2 = Organization.of(300L, "Organization 2");

        given(organizationRepositoryMock.findAll())
                .willReturn(Arrays.asList(org1, org2));

        // when
        final Iterable<Organization> result = service.getAll();

        // then
        assertThat(result)
                .asList()
                .containsExactlyInAnyOrder(org1, org2);
    }

    // getSingle

    @Test
    public void givenExistingOrganization_whenGetSingle_thenReturnOrganization() {

        // given
        final Long orgId = 200L;
        final Organization org = Organization.of(orgId, "Organization 1");

        given(organizationRepositoryMock.findById(orgId))
                .willReturn(Optional.of(org));

        // when
        final Organization result = service.getSingle(orgId);

        // then
        assertThat(result)
                .isNotNull()
                .isEqualTo(org);
    }

    @Test
    public void givenNonExistingOrganization_whenGetSingle_thenThrowsException() {

        // given
        final Long orgId = 200L;

        // when
        final Throwable result = catchThrowable(() -> service.getSingle(orgId));

        // then
        assertThat(result)
                .isNotNull()
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void givenNullParameter_whenGetSingle_thenThrowException() {

        // given
        final Long orgId = null;

        // when
        final Throwable result = catchThrowable(() -> service.getSingle(orgId));

        // then
        assertThat(result)
                .isNotNull()
                .isInstanceOf(NullPointerException.class);

    }

    // create

    @Test
    public void givenExistingOrganization_whenCreate_thenThrowException() {

        // given
        final Long orgId = 200L;
        final Organization org = Organization.of(orgId, "Organization 1");

        given(organizationRepositoryMock.existsById(orgId))
                .willReturn(true);

        // when
        final Throwable result = catchThrowable(() -> service.create(org));

        // then
        assertThat(result)
                .isNotNull()
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void givenNonExistingOrganization_whenCreate_thenReturnSaved() {

        // given
        final Long orgId = 200L;
        final Organization creatingOrganization = Organization.of("Organization 1");
        final Organization createdOrganization = Organization.copyOf(orgId, creatingOrganization);

        given(organizationRepositoryMock.save(creatingOrganization))
                .willReturn(createdOrganization);

        // when
        final Organization result = service.create(creatingOrganization);

        // then
        assertThat(result)
                .isNotNull()
                .isEqualTo(createdOrganization);
    }

    @Test
    public void givenNullParameter_whenCreate_thenThrowException() {

        // given
        final Organization org = null;

        // when
        final Throwable result1 = catchThrowable(() -> service.create(org));

        // then
        assertThat(result1)
                .isNotNull()
                .isInstanceOf(NullPointerException.class);
    }

    // update

    @Test
    public void givenExistingOrganization_whenUpdate_thenReturnSaved() {

        // given
        final Long orgId = 200L;
        final Organization updatingOrg = Organization.of("UPDATED");
        final Organization updatedOrg = Organization.copyOf(orgId, updatingOrg);

        given(organizationRepositoryMock.save(updatingOrg))
                .willReturn(updatedOrg);

        final Organization origin = Organization.of(orgId, "Origin");

        given(organizationRepositoryMock.findById(orgId))
                .willReturn(Optional.of(origin));

        // when
        final Organization result = service.update(orgId, updatingOrg);

        // then
        assertThat(result)
                .isNotNull()
                .isEqualTo(updatedOrg);
    }

    @Test
    public void givenNonExistingOrganization_whenUpdate_thenThrowException() {

        // given
        final Long orgId = 200L;
        final Organization updatingOrg = Organization.of("UPDATED");

        // when
        final Throwable result = catchThrowable(() -> service.update(orgId, updatingOrg));

        // then
        assertThat(result)
                .isNotNull()
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void givenNullParameter_whenUpdate_thenThrowException() {

        {
            // given
            final Long orgId = 1L;
            final Organization org = null;

            // when
            final Throwable result = catchThrowable(() -> service.update(orgId, org));

            // then
            assertThat(result)
                    .isNotNull()
                    .isInstanceOf(NullPointerException.class);
        }

        {
            // given
            final Long orgId = null;
            final Organization org = Organization.of("");

            // when
            final Throwable result = catchThrowable(() -> service.update(orgId, org));

            // then
            assertThat(result)
                    .isNotNull()
                    .isInstanceOf(NullPointerException.class);
        }
    }
    // delete

    @Test
    public void givenExistingOrganization_whenDelete_thenDeleteAndFinish() {

        // given
        final Long orgId = 200L;
        final Organization origin = Organization.of(orgId, "Origin");

        given(organizationRepositoryMock.findById(orgId))
                .willReturn(Optional.of(origin));

        // when
        service.delete(orgId);

        // then
        then(organizationRepositoryMock)
                .should()
                .delete(origin);
    }

    @Test
    public void givenNonExistingOrganization_whenDelete_thenThrowException() {

        // given
        final Long orgId = 200L;

        // when
        final Throwable result = catchThrowable(() -> service.delete(orgId));

        // then
        assertThat(result)
                .isNotNull()
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void givenNullParameter_whenDelete_thenThrowException() {

        // given
        final Long orgId = null;

        // when
        final Throwable result1 = catchThrowable(() -> service.delete(orgId));

        // then
        assertThat(result1)
                .isNotNull()
                .isInstanceOf(NullPointerException.class);
    }
}