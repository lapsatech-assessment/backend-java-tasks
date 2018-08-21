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

import com.backend.tasks.model.User;
import com.backend.tasks.service.user.UserService;

@RestController
@RequestMapping("/orgs/{orgId}/users")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * Post to /orgs/{orgId}/users endpoint should create and return user for
     * organization with id=orgId. Response status should be 201.
     */
    @PostMapping
    public ResponseEntity<?> create(@PathVariable(value = "orgId") @NotNull Long orgId,
            @RequestBody @NotNull User user) {
        try {
            final User result = userService.create(orgId, user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Put to /orgs/{orgId}/users/{userId} endpoint should update, save and return
     * user with id=userId for organization with id=orgId.
     */
    @PutMapping("/{userId}")
    public ResponseEntity<?> update(@PathVariable(value = "orgId") @NotNull Long orgId,
            @PathVariable(value = "userId") @NotNull Long userId,
            @RequestBody @NotNull User user) {
        try {
            final User result = userService.update(orgId, userId, user);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get to /orgs/{orgId}/users/{userId} endpoint should fetch and return user
     * with id=userId for organization with id=orgId.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> get(@PathVariable(value = "orgId") @NotNull Long orgId,
            @PathVariable(value = "userId") @NotNull Long userId) {
        try {
            final User result = userService.getSingle(orgId, userId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete to /orgs/{orgId}/users/{userId} endpoint should delete user with
     * id=userId for organization with id=orgId. Response status should be 204.
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> delete(@PathVariable(value = "orgId") @NotNull Long orgId,
            @PathVariable(value = "userId") @NotNull Long userId) {
        try {
            userService.delete(orgId, userId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get to /orgs/{orgId}/users endpoint should return list of all users for
     * organization with id=orgId
     */
    @GetMapping
    public ResponseEntity<?> all(@PathVariable(value = "orgId") @NotNull Long orgId) {
        try {
            final Iterable<User> result = userService.getAll(orgId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
