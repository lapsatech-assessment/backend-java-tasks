package com.backend.tasks.controller;

import org.springframework.web.bind.annotation.RestController;

/**
 * Implement create, read, update, delete  rest controller endpoints for user.
 * Map endpoints to /orgs/{orgId}/users path
 * 1. Post to /orgs/{orgId}/users endpoint should create and return user for organization with id=orgId. Response status should be 201.
 * 2. Put to /orgs/{orgId}/users/{userId} endpoint should update, save and return user with id=userId for organization with id=orgId.
 * 3. Get to /orgs/{orgId}/users/{userId} endpoint should fetch and return user with id=userId for organization with id=orgId.
 * 4. Delete to /orgs/{orgId}/users/{userId} endpoint should delete user with id=userId for organization with id=orgId. Response status should be 204.
 * 5. Get to /orgs/{orgId}/users endpoint should return list of all users for organization with id=orgId
 */
@RestController
public class UserController {
}
