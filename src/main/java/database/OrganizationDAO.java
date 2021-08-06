package database;

import entities.orgstuff.Organization;
import entities.orgstuff.Person;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface OrganizationDAO {

    void addList(List<Organization> organizationList) throws SQLException;

    void add(Organization organization) throws SQLException;

    List<Organization> getAll() throws SQLException;

    Organization getById(UUID id) throws SQLException;

    void update(Organization organization) throws SQLException;

    void remove(Organization organization) throws SQLException;
}
