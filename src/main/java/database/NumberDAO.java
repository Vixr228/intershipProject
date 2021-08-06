package database;

import entities.PhoneNumber;
import entities.orgstuff.Person;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface NumberDAO {

    void add(UUID id, String number) throws SQLException;


    PhoneNumber getById(UUID id) throws SQLException;

    void update(UUID id, String number) throws SQLException;

    void removeByID(UUID id) throws SQLException;

}
