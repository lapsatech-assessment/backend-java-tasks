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
import com.backend.tasks.model.User;
import com.backend.tasks.service.user.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService userService;

    // get org users

    @Test
    public void givenCorrectParameters_whenGetAllUsers_thenReturnOkAndJsonStructure() throws Exception {

        // given
        final Long orgId = 4L;

        final Organization organization1 = Organization.of(orgId, "Organization 1");
        final User user1 = User.of(200L, "user1", "+++", organization1);
        final User user2 = User.of(300L, "user2", "---", organization1);

        given(userService.getAll(organization1.getId()))
                .willReturn(Arrays.asList(user1, user2));

        // when
        final ResultActions thenResult = mvc.perform(get("/orgs/{orgId}/users", orgId));

        // then
        thenResult.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(user1.getId().intValue())))
                .andExpect(jsonPath("$[0].username", is(user1.getUsername())))
                .andExpect(jsonPath("$[0].password", is(user1.getPassword())))
                .andExpect(jsonPath("$[1].id", is(user2.getId().intValue())))
                .andExpect(jsonPath("$[1].username", is(user2.getUsername())))
                .andExpect(jsonPath("$[1].password", is(user2.getPassword())))
                .andDo(print());

    }

    @Test
    public void givenWrongParameters_whenGetAllUsers_thenReturnNotFound() throws Exception {

        // given
        final Long orgId = 4L;

        given(userService.getAll(orgId))
                .willThrow(IllegalArgumentException.class);

        // when
        final ResultActions thenResult = mvc.perform(get("/orgs/{orgId}/users", orgId));

        // then
        thenResult.andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    public void givenInvalidParameters_whenGetAllUsers_thenReturnBadRequest() throws Exception {

        // given
        final String orgId = "notANumber";

        // when
        final ResultActions thenResult = mvc.perform(get("/orgs/{orgId}/users", orgId));

        // then
        thenResult.andExpect(status().isBadRequest())
                .andDo(print());

    }

    // get org single user

    @Test
    public void givenCorrectParameters_whenGetSingleUser_thenReturnOkAndJsonStructure() throws Exception {

        // given
        final Long orgId = 4L;
        final Long userId = 5L;

        final Organization org = Organization.of(orgId, "Organization 1");
        final User user1 = User.of(userId, "user1", "+++", org);

        given(userService.getSingle(org.getId(), user1.getId()))
                .willReturn(user1);

        // when
        final ResultActions thenResult = mvc.perform(get("/orgs/{orgId}/users/{userId}", orgId, userId));

        // then
        thenResult.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(user1.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user1.getUsername())))
                .andDo(print());

    }

    @Test
    public void givenWrongParameters_whenGetSingleUser_thenReturnNotFound() throws Exception {

        // given
        final Long orgId = 4L;
        final Long userId = 5L;

        given(userService.getSingle(orgId, userId))
                .willThrow(IllegalArgumentException.class);

        // when
        final ResultActions thenResult = mvc.perform(get("/orgs/{orgId}/users/{userId}", orgId, userId));

        // then
        thenResult.andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    public void givenInvalidParameters_whenGetSingleUser_thenReturnBadRequest() throws Exception {

        {
            // given
            final String orgId = "4";
            final String userId = "notANumber";

            // when
            final ResultActions thenResult = mvc.perform(get("/orgs/{orgId}/users/{userId}", orgId, userId));

            // then
            thenResult.andExpect(status().isBadRequest())
                    .andDo(print());
        }

        {
            // given
            final String orgId = "notANumber";
            final String userId = "5";

            // when
            final ResultActions thenResult = mvc.perform(get("/orgs/{orgId}/users/{userId}", orgId, userId));

            // then
            thenResult.andExpect(status().isBadRequest())
                    .andDo(print());
        }

    }

    // create user

    @Test
    public void givenCorrectParameters_whenCreateUser_thenReturnOkAndJsonStructure() throws Exception {

        // given
        final Long orgId = 100L;

        final User creatingUser = User.of("new.user", "***");
        final String creatingUserJson = "{"
                + "\"username\": \"" + creatingUser.getUsername() + "\", "
                + "\"password\": \"" + creatingUser.getPassword() + "\" "
                + "}";

        final User createdUser = User.copyOf(1L, creatingUser);

        given(userService.create(orgId, creatingUser))
                .willReturn(createdUser);

        // when
        final ResultActions thenResult = mvc.perform(post("/orgs/{orgId}/users", orgId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(creatingUserJson));

        // then
        thenResult.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(createdUser.getId().intValue())))
                .andExpect(jsonPath("$.username", is(createdUser.getUsername())))
                .andExpect(jsonPath("$.password", is(createdUser.getPassword())))
                .andDo(print());
    }

    @Test
    public void givenWrongParameters_whenCreateUser_thenReturnNotFound() throws Exception {

        // given
        final Long orgId = 4L;
        final User creatingUser = User.of("username", "password");
        final String creatingUserJson = "{"
                + "\"username\": \"username\", "
                + "\"password\": \"password\" "
                + "}";

        given(userService.create(orgId, creatingUser))
                .willThrow(IllegalArgumentException.class);

        // when
        final ResultActions thenResult = mvc.perform(post("/orgs/{orgId}/users", orgId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(creatingUserJson));

        // then
        thenResult.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenInvalidParameter_whenCreateUser_thenReturnBadRequest() throws Exception {

        {
            // given
            final String orgId = "notAString";
            final String creatingUserJson = "{"
                    + "\"username\": \"username\", "
                    + "\"password\": \"password\" "
                    + "}";

            // when
            final ResultActions thenResult = mvc.perform(post("/orgs/{orgId}/users", orgId)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(creatingUserJson));

            // then
            thenResult.andExpect(status().isBadRequest())
                    .andDo(print());
        }

        {
            // given
            final String orgId = "4";
            final String creatingUserJson = "notAJason";

            // when
            final ResultActions thenResult = mvc.perform(post("/orgs/{orgId}/users", orgId)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(creatingUserJson));

            // then
            thenResult.andExpect(status().isBadRequest())
                    .andDo(print());
        }

        {
            // given
            final String orgId = "4";
            final String creatingUserJson = "";

            // when
            final ResultActions thenResult = mvc.perform(post("/orgs/" + orgId + "/users")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(creatingUserJson));

            // then
            thenResult.andExpect(status().isBadRequest())
                    .andDo(print());
        }

        {
            // given
            final String orgId = "4";

            // when
            final ResultActions thenResult = mvc.perform(post("/orgs/{orgId}/users", orgId));

            // then
            thenResult.andExpect(status().isBadRequest())
                    .andDo(print());
        }
    }

    // update org

    @Test
    public void givenWrongParameters_whenUpdateUser_thenReturnNotFound() throws Exception {

        // given
        final Long orgId = 1L;
        final Long userId = 2L;
        final User updatingUser = User.of("username", "password");
        final String updatingUserJson = "{"
                + "\"username\": \"username\", "
                + "\"password\": \"password\" "
                + "}";

        given(userService.update(orgId, userId, updatingUser))
                .willThrow(IllegalArgumentException.class);

        // when
        final ResultActions thenResult = mvc.perform(put("/orgs/{orgId}/users/{userId}", orgId, userId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(updatingUserJson));

        // then
        thenResult.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenCorrectParameters_whenUpdateUser_thenReturnOkAndJsonStructure() throws Exception {

        // given
        final Long orgId = 300L;
        final Long userId = 100L;

        final User updatingUser = User.of("user.name.2", "888");
        final String updatingUserJson = "{"
                + "\"username\": \"" + updatingUser.getUsername() + "\", "
                + "\"password\": \"" + updatingUser.getPassword() + "\"  "
                + "}";

        final User updatedUser = User.copyOf(userId, updatingUser);

        given(userService.update(orgId, userId, updatingUser))
                .willReturn(updatedUser);

        // when
        final ResultActions thenResult = mvc.perform(put("/orgs/{orgId}/users/{userId}", orgId, userId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(updatingUserJson));

        // then
        thenResult.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(updatedUser.getId().intValue())))
                .andExpect(jsonPath("$.username", is(updatedUser.getUsername())))
                .andExpect(jsonPath("$.password", is(updatedUser.getPassword())))
                .andDo(print());
    }

    @Test
    public void givenInvalidParameters_whenUpdateUser_thenReturnBadRequest() throws Exception {

        {
            // given
            final String orgId = "notANumber";
            final String userId = "100";

            final String updatingUserJson = "{"
                    + "\"username\": \"user.name.2\", "
                    + "\"password\": \"***\"  "
                    + "}";

            // when
            final ResultActions thenResult = mvc.perform(put("/orgs/{orgId}/users/{userId}", orgId, userId)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(updatingUserJson));

            // then
            thenResult.andExpect(status().isBadRequest())
                    .andDo(print());
        }

        {
            // given
            final String orgId = "100";
            final String userId = "notANumber";

            final String updatingUserJson = "{"
                    + "\"username\": \"user.name.2\", "
                    + "\"password\": \"***\"  "
                    + "}";

            // when
            final ResultActions thenResult = mvc.perform(put("/orgs/{orgId}/users/{userId}", orgId, userId)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(updatingUserJson));

            // then
            thenResult.andExpect(status().isBadRequest())
                    .andDo(print());
        }

        {
            // given
            final String orgId = "100";
            final String userId = "100";

            final String updatingUserJson = "notAJson";

            // when
            final ResultActions thenResult = mvc.perform(put("/orgs/{orgId}/users/{userId}", orgId, userId)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(updatingUserJson));

            // then
            thenResult.andExpect(status().isBadRequest())
                    .andDo(print());

        }

        {
            // given
            final String orgId = "100";
            final String userId = "100";

            // when
            final ResultActions thenResult = mvc.perform(put("/orgs/{orgId}/users/{userId}", orgId, userId));

            // then
            thenResult.andExpect(status().isBadRequest())
                    .andDo(print());

        }
    }

    // delete user

    @Test
    public void givenCorrectParameters_whenDeleteUser_thenReturnNoContent() throws Exception {

        // given
        final Long orgId = 100L;
        final Long userId = 200L;

        willDoNothing()
                .given(userService)
                .delete(orgId, userId);

        // when
        final ResultActions thenResult = mvc.perform(delete("/orgs/{orgId}/users/{userId}", orgId, userId));

        // then
        thenResult.andExpect(status().isNoContent())
                .andDo(print());

        then(userService)
                .should()
                .delete(orgId, userId);
    }

    @Test
    public void givenWrongParameters_whenDeleteOrganization_thenReturnNotFound()
            throws Exception {

        // given
        final Long orgId = 100L;
        final Long userId = 200L;

        willThrow(IllegalArgumentException.class)
                .given(userService)
                .delete(orgId, userId);

        // when
        final ResultActions thenResult = mvc.perform(delete("/orgs/{orgId}/users/{userId}", orgId, userId));

        // then
        thenResult.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenInvalidParameters_whenDeleteOrganization_thenReturnBadRequest()
            throws Exception {

        {
            // given
            final String orgId = "100";
            final String userId = "notANumber";

            // when
            final ResultActions thenResult = mvc.perform(delete("/orgs/{orgId}/users/{userId}", orgId, userId));

            // then
            thenResult.andExpect(status().isBadRequest())
                    .andDo(print());
        }

        {
            // given
            final String orgId = "notANumber";
            final String userId = "200";

            // when
            final ResultActions thenResult = mvc.perform(delete("/orgs/{orgId}/users/{userId}", orgId, userId));

            // then
            thenResult.andExpect(status().isBadRequest())
                    .andDo(print());
        }
    }
}