package com.backend.tasks;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.backend.tasks.model.Organization;
import com.backend.tasks.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void overallProcess() {

        // store org

        Organization o;
        {
            final Organization templ = Organization.of("organization");

            final ResponseEntity<Organization> response = restTemplate.postForEntity("/orgs", templ,
                    Organization.class);

            assertThat(response.getStatusCode())
                    .isEqualTo(HttpStatus.CREATED);

            final Organization test = response.getBody();

            assertThat(test)
                    .isNotNull();

            assertThat(test.getId())
                    .isNotNull();

            assertThat(test.getName())
                    .isNotNull()
                    .isEqualTo(templ.getName());

            o = test;
        }

        // update organization

        {
            final Organization templ = Organization.copyOf(o);
            templ.setName(o.getName() + " has changed");

            final ResponseEntity<Organization> response = restTemplate.exchange("/orgs/{orgId}",
                    HttpMethod.PUT,
                    new HttpEntity<>(templ),
                    Organization.class,
                    o.getId());

            assertThat(response.getStatusCode())
                    .isEqualTo(HttpStatus.OK);

            final Organization test = response.getBody();

            assertThat(test)
                    .isNotNull();

            assertThat(test.getId())
                    .isNotNull()
                    .isEqualTo(templ.getId());

            assertThat(test.getName())
                    .isNotNull()
                    .isEqualTo(templ.getName());

            o = test;
        }

        // get all orgs

        {
            final ResponseEntity<Organization[]> response = restTemplate.getForEntity("/orgs", Organization[].class);

            assertThat(response.getStatusCode())
                    .isEqualTo(HttpStatus.OK);

            final Organization[] test = response.getBody();

            assertThat(test)
                    .isNotNull();

            assertThat(test)
                    .isNotNull()
                    .extracting("id", "name")
                    .hasSize(1)
                    .containsExactly(tuple(o.getId(), o.getName()));
        }

        // get single org

        {
            final ResponseEntity<Organization> response = restTemplate.getForEntity("/orgs/{orgId}",
                    Organization.class, o.getId());

            assertThat(response.getStatusCode())
                    .isEqualTo(HttpStatus.OK);

            final Organization test = response.getBody();

            assertThat(test)
                    .isNotNull();

            assertThat(test.getId())
                    .isNotNull()
                    .isEqualTo(o.getId());

            assertThat(test.getName())
                    .isNotNull()
                    .isEqualTo(o.getName());
        }

        // store user

        User u;
        {
            final User templ = User.of("usrname", "password");

            final ResponseEntity<User> response = restTemplate.postForEntity("/orgs/{orgId}/users", templ,
                    User.class, o.getId());

            assertThat(response.getStatusCode())
                    .isEqualTo(HttpStatus.CREATED);

            final User test = response.getBody();

            assertThat(test)
                    .isNotNull();

            assertThat(test.getId())
                    .isNotNull();

            assertThat(test.getUsername())
                    .isNotNull()
                    .isEqualTo(templ.getUsername());

            assertThat(test.getPassword())
                    .isNotNull()
                    .isEqualTo(templ.getPassword());

            u = test;
        }

        // update user

        {
            final User templ = User.copyOf(u);
            templ.setUsername(templ.getUsername() + " has changed");

            final ResponseEntity<User> response = restTemplate.exchange("/orgs/{orgId}/users/{userId}",
                    HttpMethod.PUT,
                    new HttpEntity<>(templ),
                    User.class,
                    o.getId(),
                    u.getId());

            assertThat(response.getStatusCode())
                    .isEqualTo(HttpStatus.OK);

            final User test = response.getBody();

            assertThat(test)
                    .isNotNull();

            assertThat(test.getId())
                    .isNotNull()
                    .isEqualTo(templ.getId());

            assertThat(test.getUsername())
                    .isNotNull()
                    .isEqualTo(templ.getUsername());

            assertThat(test.getPassword())
                    .isNotNull()
                    .isEqualTo(templ.getPassword());

            u = test;
        }

        // get all users

        {
            final ResponseEntity<User[]> response = restTemplate.getForEntity("/orgs/{orgId}/users", User[].class,
                    o.getId());

            assertThat(response.getStatusCode())
                    .isEqualTo(HttpStatus.OK);

            final User[] test = response.getBody();

            assertThat(test)
                    .isNotNull()
                    .extracting("id", "username", "password")
                    .containsExactly(tuple(u.getId(), u.getUsername(), u.getPassword()));
        }

        // get single user

        {
            final ResponseEntity<User> response = restTemplate.getForEntity("/orgs/{orgId}/users/{userId}",
                    User.class, o.getId(), u.getId());

            assertThat(response.getStatusCode())
                    .isEqualTo(HttpStatus.OK);

            final User test = response.getBody();

            assertThat(test)
                    .isNotNull();

            assertThat(test.getId())
                    .isNotNull()
                    .isEqualTo(u.getId());

            assertThat(test.getUsername())
                    .isNotNull()
                    .isEqualTo(u.getUsername());

            assertThat(test.getPassword())
                    .isNotNull()
                    .isEqualTo(u.getPassword());
        }

        // delete user

        {
            final ResponseEntity<?> response = restTemplate.exchange("/orgs/{orgId}/users/{userId}",
                    HttpMethod.DELETE,
                    null,
                    Object.class,
                    o.getId(),
                    u.getId());

            assertThat(response.getStatusCode())
                    .isEqualTo(HttpStatus.NO_CONTENT);

            assertThat(restTemplate.getForEntity("/orgs/{orgId}/users/{userId}",
                    Object.class,
                    o.getId(),
                    u.getId()).getStatusCode())
                            .isEqualTo(HttpStatus.NOT_FOUND);

            u = null;
        }

        // delete org

        {
            final ResponseEntity<?> response = restTemplate.exchange("/orgs/{orgId}",
                    HttpMethod.DELETE,
                    null,
                    Object.class,
                    o.getId());

            assertThat(response.getStatusCode())
                    .isEqualTo(HttpStatus.NO_CONTENT);

            assertThat(restTemplate.getForEntity("/orgs/{orgId}",
                    Object.class,
                    o.getId()).getStatusCode())
                            .isEqualTo(HttpStatus.NOT_FOUND);

            o = null;
        }
    }
}
