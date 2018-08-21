package com.backend.tasks.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.backend.tasks.model.Organization;
import com.backend.tasks.service.org.OrganizationService;

@RunWith(SpringRunner.class)
@WebMvcTest(OrganizationController.class)
public class OrganizationControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    OrganizationService organizationService;

    // get orgs

    @Test
    public void givenCorretParameters_whenGetAll_thenReturnOkAndJsonStructure() throws Exception {

        // given
        final Organization org1 = Organization.of(200L, "Organization 1");
        final Organization org2 = Organization.of(300L, "Organization 2");

        given(organizationService.getAll())
                .willReturn(Arrays.asList(org1, org2));

        // when
        final ResultActions thenResult = mvc.perform(get("/orgs"));

        // then
        thenResult.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(org1.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(org1.getName())))
                .andExpect(jsonPath("$[1].id", is(org2.getId().intValue())))
                .andExpect(jsonPath("$[1].name", is(org2.getName())))
                .andDo(print());

    }

    // get single org

    @Test
    public void givenCorrectParameters_whenGetSingle_thenReturnOkAndJsonStructure() throws Exception {

        // given
        final Long orgId = 200L;
        final Organization org = Organization.of(orgId, "Organization 1");

        given(organizationService.getSingle(orgId))
                .willReturn(org);

        // when
        final ResultActions thenResult = mvc.perform(get("/orgs/{orgId}", orgId));

        // then
        thenResult.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(org.getId().intValue())))
                .andExpect(jsonPath("$.name", is(org.getName())))
                .andDo(print());
    }

    @Test
    public void givenWrongParameters_whenGetSingle_thenReturnNotFound() throws Exception {

        // given
        final Long orgId = 200L;

        given(organizationService.getSingle(orgId))
                .willThrow(IllegalArgumentException.class);

        // when
        final ResultActions thenResult = mvc.perform(get("/orgs/{orgId}", orgId));

        // then
        thenResult.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenInvalidParameters_whenGetSingle_thenReturnBadRequest() throws Exception {

        // given
        final String orgId = "notANumber";

        // when
        final ResultActions thenResult = mvc.perform(get("/orgs/{orgId}", orgId));

        // then
        thenResult.andExpect(status().isBadRequest())
                .andDo(print());
    }

    // create org

    @Test
    public void givenCorrectParameters_whenCreate_thenReturnOkAndJsonStructure() throws Exception {

        // given
        final Organization creatingOrganization = Organization.of("Organization 1");
        final String creatingOrganizationJson = "{\"name\": \"" + creatingOrganization.getName() + "\"}";
        final Long orgId = 1L;

        final Organization createdOrganization = Organization.of(orgId, creatingOrganization.getName());

        given(organizationService.create(creatingOrganization))
                .willReturn(createdOrganization);

        // when
        final ResultActions thenResult = mvc.perform(post("/orgs")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(creatingOrganizationJson));

        // then
        thenResult.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(createdOrganization.getId().intValue())))
                .andExpect(jsonPath("$.name", is(createdOrganization.getName())))
                .andDo(print());
    }

    @Test
    public void givenInvalidParameters_whenCreate_thenReturnBadRequest() throws Exception {

        // given

        // when
        final ResultActions thenResult = mvc.perform(post("/orgs"));

        // then
        thenResult.andExpect(status().isBadRequest())
                .andDo(print());
    }

    // update org

    @Test
    public void givenWrongParameters_whenUpdate_thenReturnNotFound() throws Exception {

        // given
        final Long orgId = 200L;
        final Organization org = Organization.of("UPDATED");
        final String updatingOrganization1Json = "{\"name\": \"UPDATED\"}";

        willThrow(IllegalArgumentException.class)
                .given(organizationService)
                .update(orgId, org);

        // when
        final ResultActions thenResult = mvc.perform(put("/orgs/{orgId}", orgId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(updatingOrganization1Json));

        // then
        thenResult.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenCorrectParameters_whenUpdate_thenReturnOkAndJsonStructure() throws Exception {

        // given
        final Long orgId = 200L;

        final Organization updatingOrganization = Organization.of("UPDATED");
        final String updatingOrganizationJson = "{\"name\": \"" + updatingOrganization.getName() + "\"}";

        final Organization updatedOrganization = Organization.of(orgId, updatingOrganization.getName());

        given(organizationService.update(orgId, updatingOrganization))
                .willReturn(updatedOrganization);

        // when
        final ResultActions thenResult = mvc.perform(put("/orgs/{orgId}", orgId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(updatingOrganizationJson));

        // then
        thenResult.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(updatedOrganization.getId().intValue())))
                .andExpect(jsonPath("$.name", is(updatedOrganization.getName())))
                .andDo(print());
    }

    @Test
    public void givenInvalidParameters_whenUpdate_thenReturnBadRequest() throws Exception {

        {
            // given
            final String orgId = "notANumber";
            final String updatingOrganizationJson = "{\"name\": \"UPDATED\"}";

            // when
            final ResultActions thenResult = mvc.perform(put("/orgs/{orgId}", orgId)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(updatingOrganizationJson));

            // then
            thenResult.andExpect(status().isBadRequest())
                    .andDo(print());
        }

        {
            // given
            final Long orgId = 200L;
            final String updatingOrganizationJson = "";

            // when
            final ResultActions thenResult = mvc.perform(put("/orgs/{orgId}", orgId)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(updatingOrganizationJson));

            // then
            thenResult.andExpect(status().isBadRequest())
                    .andDo(print());
        }

        {
            // given
            final Long orgId = 200L;

            // when
            final ResultActions thenResult = mvc.perform(put("/orgs/{orgId}", orgId));

            // then
            thenResult.andExpect(status().isBadRequest())
                    .andDo(print());
        }
    }

    // delete org

    @Test
    public void givenCorrectParameters_whenDelete_thenReturnNoContent() throws Exception {

        // given
        final Long orgId = 200L;

        willDoNothing()
                .given(organizationService)
                .delete(orgId);

        // when
        final ResultActions thenResult = mvc.perform(delete("/orgs/{orgId}", orgId));

        // then
        then(organizationService)
                .should()
                .delete(orgId);

        thenResult.andExpect(status().isNoContent())
                .andDo(print());

    }

    @Test
    public void givenWrongParameters_whenDelete_thenReturnNotFound() throws Exception {

        // given
        final Long orgId = 200L;

        willThrow(IllegalArgumentException.class)
                .given(organizationService)
                .delete(orgId);

        // when
        final ResultActions thenResult = mvc.perform(delete("/orgs/{orgId}", orgId));

        // then
        thenResult.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenInvalidParameters_whenDelete_thenReturnBadRequest() throws Exception {

        // given
        final String orgId = "notANumber";

        // when
        final ResultActions thenResult = mvc.perform(delete("/orgs/{orgId}", orgId));

        // then
        thenResult.andExpect(status().isBadRequest())
                .andDo(print());
    }
}