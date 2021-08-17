package database.services;

import database.DatabaseConnector;
import database.interfaces.DepartmentDAO;
import entities.PhoneNumber;
import entities.orgstuff.Department;
import utils.parse_utils.PhoneNumbersList;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Класс который осущевствляет доступ к базе данных подразделений.
 */
public class DepartmentService extends DatabaseConnector implements DepartmentDAO {
    private Logger log = Logger.getLogger(getClass().getName());
    Connection connection = getConnection();
    NumberService numberService = new NumberService();
    PersonService personService = new PersonService();

    public DepartmentService() throws Exception {
    }

    /**
     * Добавление коллекции подразделений в БД
     * @param departmentList
     * @throws SQLException
     */
    @Override
    public void addList(List<Department> departmentList) throws SQLException {
        String sql = "INSERT INTO APP.DEPARTMENTS(id, full_name, short_name, director_id) VALUES(?, ?, ?, ?)";

        for(Department department : departmentList) {
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setString(1, department.getId().toString());
                preparedStatement.setString(2, department.getFullName());
                preparedStatement.setString(3, department.getShortName());
                preparedStatement.setString(4, department.getDirector().getId().toString());

                for(PhoneNumber phoneNumber : department.getContactList().getNumberList()){
                    numberService.add(department.getId(), phoneNumber.getNumber());
                }

                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                log.severe("Возникла проблема при добавление подразделений в БД");
                ex.printStackTrace();
            }
        }
    }

    /**
     * Добавление одного подразделения в БД
     * @param department
     * @throws SQLException
     */
    @Override
    public void add(Department department) throws SQLException {

        String sql = "INSERT INTO APP.DEPARTMENTS(id, full_name, short_name, director_id) VALUES(?, ?, ?, ?)";

        try( PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, department.getId().toString());
            preparedStatement.setString(2, department.getFullName());
            preparedStatement.setString(3, department.getShortName());
            preparedStatement.setString(4, department.getDirector().getId().toString());

            for(PhoneNumber phoneNumber : department.getContactList().getNumberList()){
                numberService.add(department.getId(), phoneNumber.getNumber());
            }

            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            log.severe("Возникла проблема при добавление подразделения в БД");
            ex.printStackTrace();
        }
    }

    /**
     * Получение списка подразделений из БД
     * @return
     * @throws SQLException
     */
    @Override
    public List<Department> getAll() throws SQLException {
        List<Department> departmentList = new ArrayList<>();

        String sql = "SELECT * FROM APP.DEPARTMENTS";

        try(Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                Department department = new Department();
                department.setId(UUID.fromString(resultSet.getString("id")));
                department.setFullName(resultSet.getString("full_name"));
                department.setShortName(resultSet.getString("short_name"));
                department.setDirector(personService.getById(UUID.fromString(resultSet.getString("director_id"))));

                PhoneNumbersList contactList = new PhoneNumbersList(numberService.getAllWithId(UUID.fromString(resultSet.getString("id"))));
                department.setContactList(contactList);

                departmentList.add(department);
            }

        } catch (SQLException ex){
            log.severe("Возникла проблема при получении подразделений в БД");
            ex.printStackTrace();
        }
        return departmentList;

    }

    /**
     * Получени подразделения по его id из БД
     * @param id
     * @return
     * @throws SQLException
     */
    @Override
    public Department getById(UUID id) throws SQLException {
        String sql = "SELECT * FROM APP.DEPARTMENTS WHERE id=?";

        Department department = new Department();
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setString(1, id.toString());

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                department.setId(UUID.fromString(resultSet.getString("id")));
                department.setFullName(resultSet.getString("full_name"));
                department.setShortName(resultSet.getString("short_name"));
                department.setDirector(personService.getById(UUID.fromString(resultSet.getString("director_id"))));

                PhoneNumbersList contactList = new PhoneNumbersList(numberService.getAllWithId(UUID.fromString(resultSet.getString("id"))));
                department.setContactList(contactList);
            }
        } catch (SQLException throwable) {
            log.severe("Возникла проблема при получении подразделения по ID из БД");
            throwable.printStackTrace();
        }

        return department;
    }

    /**
     * Обновление данных о подразделение
     * @param department
     * @throws SQLException
     */
    @Override
    public void update(Department department) throws SQLException {
        String sql = "UPDATE APP.DEPARTMENTS SET full_name=?, short_name=?, director_id=? WHERE id=?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setString(1, department.getFullName());
            preparedStatement.setString(2, department.getShortName());
            preparedStatement.setString(3, department.getDirector().getId().toString());
            preparedStatement.setString(4, department.getId().toString());
            numberService.updateList(department.getId(), department.getContactList().getNumberList());

            preparedStatement.executeUpdate();
        } catch (SQLException throwable) {
            log.severe("Возникла проблема при обновлении подразделения в БД");
            throwable.printStackTrace();
        }
    }

    /**
     * Удаление данных о подразделение
     * @param department
     * @throws SQLException
     */
    @Override
    public void remove(Department department) throws SQLException {


        String sql = "DELETE FROM APP.DEPARTMENTS WHERE id=?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setString(1, department.getId().toString());
            department.getContactList().getNumberList().forEach(num -> {
                try {
                    numberService.removeByID(department.getId());
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
            });

            preparedStatement.executeUpdate();
        } catch (SQLException throwable) {
            log.severe("Возникла проблема при удаление подразделения из БД");
            throwable.printStackTrace();
        }
    }
}
