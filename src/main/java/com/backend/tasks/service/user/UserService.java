package com.backend.tasks.service.user;

import com.backend.tasks.model.User;

public interface UserService {

    /**
     * Retreives all Users attached to the Organization with given ID
     * 
     * @param organizationId
     *            ID of the Organization. Must nut be <code>null</code>
     * 
     * @return Iterable containing User entities. Should not be <code>null</code>
     *         value
     * 
     * @throws NullPointerException
     *             when any of parameter is <code>null</code>
     * @throws IllegalArgumentException
     *             when any of entites wasn't found with given IDs
     */
    Iterable<User> getAll(Long organizationId);

    /**
     * Retreives a single User with given ID attached to the Organization with given
     * ID
     * 
     * @param organizationId
     *            ID of the Organization. Must nut be <code>null</code> value
     * 
     * @param userId
     *            ID of the User. Must nut be <code>null</code>
     * 
     * @return entity User entity. Should not be <code>null</code> value
     * 
     * @throws NullPointerException
     *             when any of parameter is <code>null</code>
     * @throws IllegalArgumentException
     *             when any of entites wasn't found with given IDs or ID's doesn't
     *             match
     */
    User getSingle(Long organizationId, Long userId);

    /**
     * Store new User data and attach it to the Organization with given ID
     * 
     * @param organizationId
     *            ID of the Organization. Must nut be <code>null</code>
     * @param user
     *            User data to be stored. Must nut be <code>null</code>
     * 
     * @return successfuly stored User entity with generated ID. Should not be
     *         <code>null</code> value
     * 
     * @throws NullPointerException
     *             when any of parameter is <code>null</code>
     * @throws IllegalArgumentException
     *             when another organization is registered with the same ID. It
     *             could occurs only when passed entity id is set (not
     *             <code>null</code>)
     */
    User create(Long organizationId, User user);

    /**
     * Updates User with given ID and new data. Origin User must be attached to the
     * Organization with given ID.
     * 
     * @param organizationId
     *            ID of the Organization. Must nut be <code>null</code>
     * @param userId
     *            ID of the User. Must nut be <code>null</code>
     * @param user
     *            User entity to be updated from. Must nut be <code>null</code>
     * 
     * @return updated updated User entity. Should not be <code>null</code> value
     * 
     * @throws NullPointerException
     *             when any of parameter is <code>null</code>
     * @throws IllegalArgumentException
     *             when any of entites wasn't found with given IDs or ID's doesn't
     *             match
     */
    User update(Long organizationId, Long userId, User user);

    /**
     * Remove User with given ID. Origin User must be attached to the Organization
     * with given ID.
     * 
     * @param organizationId
     *            ID of the Organization. Must nut be <code>null</code>
     * @param userId
     *            ID of the User. Must nut be <code>null</code>
     * 
     * @throws NullPointerException
     *             when any of parameter is <code>null</code>
     * @throws IllegalArgumentException
     *             when any of entites wasn't found with given IDs or ID's doesn't
     *             match
     */
    void delete(Long organizationId, Long userId);
}
