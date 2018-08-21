package com.backend.tasks.service.user.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.backend.tasks.model.Organization;
import com.backend.tasks.model.User;
import com.backend.tasks.repository.OrganizationRepository;
import com.backend.tasks.repository.UserRepository;
import com.backend.tasks.service.user.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Iterable<User> getAll(Long organizationId) {
        Objects.requireNonNull(organizationId, "organizationId");
        final Organization organization = getAndCheckOrganization(organizationId);
        return organization.getUsers();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public User getSingle(Long organizationId, Long userId) {
        Objects.requireNonNull(organizationId, "organizationId");
        Objects.requireNonNull(userId, "userId");

        final User result = userRepository.findById(userId).orElse(null);
        if (result == null
                || result.getOrganization() == null
                || !organizationId.equals(result.getOrganization().getId()))
            throw new IllegalArgumentException("User not exists or organization is differ");
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public User create(Long organizationId, User user) {
        Objects.requireNonNull(organizationId, "organizationId");
        Objects.requireNonNull(user, "user");

        if (user.getId() != null && userRepository.existsById(user.getId()))
            throw new IllegalArgumentException("User exists with given id");

        final Organization organization = organizationRepository.findById(organizationId).orElse(null);
        if (organization == null)
            throw new IllegalArgumentException("Organization not exists");

        user.setOrganization(organization);
        return userRepository.save(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public User update(Long organizationId, Long userId, User user) {
        Objects.requireNonNull(organizationId, "organizationId");
        Objects.requireNonNull(userId, "userId");
        Objects.requireNonNull(user, "user");

        if (user.getId() != null && !userId.equals(user.getId()))
            throw new IllegalArgumentException("IDs doesn't match");

        final User originUser = getAndCheckUser(organizationId, userId);
        final Organization organization = originUser.getOrganization();

        user.setId(userId); // overrite or set id
        user.setOrganization(organization);
        return userRepository.save(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Long organizationId, Long userId) {
        Objects.requireNonNull(organizationId, "organizationId");
        Objects.requireNonNull(userId, "userId");

        final User originUser = getAndCheckUser(organizationId, userId);
        userRepository.delete(originUser);
    }

    // PRIVATE

    private Organization getAndCheckOrganization(Long organizationId) {
        assert organizationId != null;

        final Organization organization = organizationRepository.findById(organizationId).orElse(null);
        if (organization == null)
            throw new IllegalArgumentException("Organization not exists");
        return organization;
    }

    private User getAndCheckUser(Long organizationId, Long userId) {
        assert organizationId != null;
        assert userId != null;

        final User originUser = userRepository.findById(userId).orElse(null);

        if (originUser == null)
            throw new IllegalArgumentException("User doesn't exists with given ID");
        if (originUser.getOrganization() == null || !organizationId.equals(originUser.getOrganization().getId()))
            throw new IllegalArgumentException("Wrong organization ID provided");

        return originUser;
    }
}
