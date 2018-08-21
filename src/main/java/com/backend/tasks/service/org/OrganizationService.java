package com.backend.tasks.service.org;

import com.backend.tasks.model.Organization;

public interface OrganizationService {

    /**
     * Retreives all Organization
     * 
     * @return Iterable containing Organization entities. Should not be
     *         <code>null</code> value
     */
    Iterable<Organization> getAll();

    /**
     * Retreives a single Organization with given ID
     * 
     * @param organizationId
     *            ID of the Organization. Must nut be <code>null</code>
     * 
     * @return entity Organization entity. Should not be <code>null</code> value
     * 
     * @throws NullPointerException
     *             when any of parameter is <code>null</code>
     * @throws IllegalArgumentException
     *             when any of entites wasn't found with given IDs
     */
    Organization getSingle(Long organizationId);

    /**
     * Store new Organization data
     * 
     * @param organization
     *            Organization data to be stored. Must nut be <code>null</code>
     * 
     * @return successfuly stored Organization entity with generated ID. Should not
     *         be <code>null</code> value
     * 
     * @throws NullPointerException
     *             when any of parameter is <code>null</code>
     * @throws IllegalArgumentException
     *             when another organization is registered with the same ID. It
     *             could occurs only when passed entity id is set (not
     *             <code>null</code>)
     */
    Organization create(Organization organization);

    /**
     * Updates Organization with given ID and new data
     * 
     * @param organizationId
     *            ID of the Organization. Must nut be <code>null</code>
     * @param organization
     *            Organization entity to be updated from. Must nut be
     *            <code>null</code>
     * 
     * @return updated User entity. Should not be <code>null</code> value
     * 
     * @throws NullPointerException
     *             when any of parameter is <code>null</code>
     * @throws IllegalArgumentException
     *             when any of entites wasn't found with given IDs or ID's doesn't
     *             match
     */
    Organization update(Long orgaizationId, Organization organization);

    /**
     * Remove Organization with given ID. It also removes all Users attached
     * 
     * @param organizationId
     *            ID of the Organization. Must nut be <code>null</code>
     * 
     * @throws NullPointerException
     *             when any of parameter is <code>null</code>
     * @throws IllegalArgumentException
     *             when any of entites wasn't found with given IDs
     */
    void delete(Long orgaizationId);
}
