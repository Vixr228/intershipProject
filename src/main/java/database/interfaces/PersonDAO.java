package database.interfaces;

import entities.orgstuff.Person;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * Основные функции для работы с БД у работников
 */
public interface PersonDAO {

    void addList(List<Person> personList) throws SQLException;

    void add(Person person) throws SQLException;

    List<Person> getAll() throws SQLException;

    Person getById(UUID id) throws SQLException;

    void update(Person person) throws SQLException;

    void remove(Person person) throws SQLException;
}
