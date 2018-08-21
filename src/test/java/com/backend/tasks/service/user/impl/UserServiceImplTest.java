package com.backend.tasks.service.user.impl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.backend.tasks.model.Organization;
import com.backend.tasks.model.User;
import com.backend.tasks.repository.OrganizationRepository;
import com.backend.tasks.repository.UserRepository;
import com.backend.tasks.service.user.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { UserServiceImpl.class })
public class UserServiceImplTest {

    @Autowired
    UserService service;

    @MockBean
    UserRepository userRepositoryMock;

    @MockBean
    OrganizationRepository organizationRepositoryMock;

    @Test
    public void givenNonExistingAll_whenGetAll_thenThrowException() {

        // given
        final Long orgId = 100L;

        // when
        final Throwable result = catchThrowable(() -> service.getAll(orgId));

        // then
        assertThat(result)
                .isNotNull()
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void givenExistingAll_whenGetAll_thenReturnIterable() {

        // given
        final Long orgId = 100L;
        final Organization org = Organization.of(orgId, "Organization 1");

        final User user1 = User.of(200L, "user1", "+++", org);
        final User user2 = User.of(300L, "user2", "---", org);

        given(organizationRepositoryMock.findById(orgId))
                .willReturn(Optional.of(org));

        // when
        final Iterable<User> result = service.getAll(orgId);

        // then
        assertThat(result)
                .contains(user1, user2)
                .isNotNull();
    }

    @Test
    public void givenNullParameter_whenGetAll_thenThrowException() {

        // given
        final Long orgId = null;

        // when
        final Throwable result = catchThrowable(() -> service.getAll(orgId));

        // then
        assertThat(result)
                .isNotNull()
                .isInstanceOf(NullPointerException.class);
    }

    // create

    @Test
    public void givenNonExistingUser_whenCreate_thenReturnCreated() {

        // given
        final Long orgId = 100L;
        final Organization org = Organization.of(orgId, "Organization 1");

        given(organizationRepositoryMock.findById(orgId))
                .willReturn(Optional.of(org));

        final Long userId = 200L;
        final User creatingUser = User.of("username", "password");
        final User createdUser = User.copyOf(userId, creatingUser);

        given(userRepositoryMock.save(creatingUser))
                .willReturn(createdUser);

        // when
        final User result = service.create(orgId, creatingUser);

        // then
        assertThat(result)
                .isNotNull()
                .isEqualTo(createdUser);
    }

    @Test
    public void givenExistingUser_whenCreate_thenThrowException() {

        // given
        final Long orgId = 100L;
        final Organization org = Organization.of(orgId, "Organization 1");

        given(organizationRepositoryMock.findById(orgId))
                .willReturn(Optional.of(org));

        final Long userId = 200L;
        final User user = User.of(userId, "username", "password", org);

        given(userRepositoryMock.existsById(userId))
                .willReturn(true);

        // when
        final Throwable result = catchThrowable(() -> service.create(orgId, user));

        // then
        assertThat(result)
                .isNotNull()
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void givenNonExistingAll_whenCreate_thenThrowException() {

        // given
        final Long userId = 100L;
        final User user = User.of(200L, "username", "password");

        // when
        final Throwable result = catchThrowable(() -> service.create(userId, user));

        // then
        assertThat(result)
                .isNotNull()
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void givenNullParameter_whenCreate_thenThrowException() {

        {
            // given
            final Long organizationId = 100L;
            final User user = null;

            // when
            final Throwable result = catchThrowable(() -> service.create(organizationId, user));

            // then
            assertThat(result)
                    .isNotNull()
                    .isInstanceOf(NullPointerException.class);
        }

        {
            // given
            final Long organizationId = null;
            final User user = User.of("", "");

            // when
            final Throwable result = catchThrowable(() -> service.create(organizationId, user));

            // then
            assertThat(result)
                    .isNotNull()
                    .isInstanceOf(NullPointerException.class);
        }
    }

    // update

    @Test
    public void givenExistingAll_whenUpdate_thenReturnUpdated() {

        // given
        final Long orgId = 100L;
        final Organization org = Organization.of(orgId, "Organization 1");

        given(organizationRepositoryMock.findById(orgId))
                .willReturn(Optional.of(org));

        final Long userId = 100L;

        final User updatingUser = User.of("user.name.2", "888");
        final User updatedUser = User.copyOf(userId, updatingUser);

        given(userRepositoryMock.save(updatingUser))
                .willReturn(updatedUser);

        final User originUser = User.of(userId, "user.name.1", "666", org);

        given(userRepositoryMock.findById(userId))
                .willReturn(Optional.of(originUser));

        // when
        final User result = service.update(orgId, userId, updatingUser);

        // then
        assertThat(result)
                .isNotNull()
                .isEqualTo(updatedUser);
    }

    @Test
    public void givenNonExistingAll_whenUpdate_thenThrowException() {

        // given
        final Long orgId = 100L;
        final Long userId = 100L;
        final User user = User.of(userId, "username", "password");

        // when
        final Throwable result = catchThrowable(() -> service.update(orgId, userId, user));

        // then
        assertThat(result)
                .isNotNull()
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void givenNullParameter_whenUpdate_thenThrowException() {

        {
            // given
            final Long orgId = null;
            final Long userId = 200L;
            final User user = User.of(userId, "username", "password");

            // when
            final Throwable result = catchThrowable(() -> service.update(orgId, userId, user));

            // then
            assertThat(result)
                    .isNotNull()
                    .isInstanceOf(NullPointerException.class);
        }

        {
            // given
            final Long orgId = 100L;
            final Long userId = null;
            final User user = User.of(userId, "username", "password");

            // when
            final Throwable result = catchThrowable(() -> service.update(orgId, userId, user));

            // then
            assertThat(result)
                    .isNotNull()
                    .isInstanceOf(NullPointerException.class);
        }

        {
            // given
            final Long orgId = 100L;
            final Long userId = 200L;
            final User user = null;

            // when
            final Throwable result = catchThrowable(() -> service.update(orgId, userId, user));

            // then
            assertThat(result)
                    .isNotNull()
                    .isInstanceOf(NullPointerException.class);
        }
    }

    // delete

    @Test
    public void givenExistingAll_whenDelete_thenFinished() {

        // given
        final Long orgId = 100L;
        final Long userId = 100L;

        final User originUser = User.of(userId, "user.name.1", "666", Organization.of(orgId, "Organization 1"));

        given(userRepositoryMock.findById(userId))
                .willReturn(Optional.of(originUser));

        // when
        service.delete(orgId, userId);

        // then
        then(userRepositoryMock)
                .should()
                .delete(originUser);
    }

    @Test
    public void givenNonExistingAll_whenDelete_thenThrowException() {

        // given
        final Long orgId = 100L;
        final Long userId = 200L;

        // when
        final Throwable result = catchThrowable(() -> service.delete(orgId, userId));

        // then
        assertThat(result)
                .isNotNull()
                .isInstanceOf(IllegalArgumentException.class);
    }

    // getSingle

    @Test
    public void givenExistingAll_whenGetSingle_thenReturnUser() {

        // given
        final Long orgId = 100L;
        final Organization org = Organization.of(orgId, "Organization 1");
        final Long userId = 200L;
        final User user = User.of(userId, "user1", "+++", org);

        given(userRepositoryMock.findById(userId))
                .willReturn(Optional.of(user));

        // when
        final User result = service.getSingle(orgId, userId);

        // then
        assertThat(result)
                .isNotNull()
                .isEqualTo(user);
    }

    @Test
    public void givenNonExistingAll_whenGetSingle_thenThrowException() {

        // given
        final Long orgId = 100L;
        final Long userId = 200L;

        // when
        final Throwable result = catchThrowable(() -> service.getSingle(orgId, userId));

        // then
        assertThat(result)
                .isNotNull()
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void givenNullParameter_whenGetSingle_thenThrowException() {

        {
            // given
            final Long orgId = 100L;
            final Long userId = null;

            // when
            final Throwable result = catchThrowable(() -> service.getSingle(orgId, userId));

            // then
            assertThat(result)
                    .isNotNull()
                    .isInstanceOf(NullPointerException.class);
        }

        {
            // given
            final Long orgId = null;
            final Long userId = 200L;

            // when
            final Throwable result = catchThrowable(() -> service.getSingle(orgId, userId));

            // then
            assertThat(result)
                    .isNotNull()
                    .isInstanceOf(NullPointerException.class);
        }

    }

}