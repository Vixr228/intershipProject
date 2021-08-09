package database.interfaces;

import entities.orgstuff.Department;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * Основные функции для работы с БД у подразделений
 */
public interface DepartmentDAO {


    void addList(List<Department> departmentList) throws SQLException;

    void add(Department department) throws SQLException;

    List<Department> getAll() throws SQLException;

    Department getById(UUID id) throws SQLException;

    void update(Department department) throws SQLException;

    void remove(Department department) throws SQLException;
}
