package database;

import entities.orgstuff.Department;
import entities.orgstuff.Organization;
import entities.orgstuff.Person;
import web_controllers.Application;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class DatabaseConnector {
    private Logger log = Logger.getLogger(getClass().getName());
    private final String URL = "jdbc:derby:/Users/user/Desktop/database/db2;";
    public Connection getConnection(){
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(URL);
        } catch (SQLException throwable) {
            log.severe("Problem with connection to database");
            throwable.printStackTrace();
        }

        return connection;
    }

//    public void addPerson() throws SQLException {
//        Connection connection = DriverManager.getConnection("jdbc:derby:/Users/user/Desktop/database/db2;");
//        List<Person> personList = Application.personRepository.getPersonList();
//        personList.forEach(person -> {
//            PreparedStatement ps = null;
//
//            try {
//                ps = connection.prepareStatement("insert into app.persons(id, name, surname, patronymic, position, birth_date) " +
//                        "values('" + person.getId().toString() + "'," +
//                        " '" + person.getName() + "'," +
//                        " '" + person.getSurname() + "'," +
//                        " '" + person.getPosition() + "'," +
//                        " '" + person.getPosition() + "'," +
//                        " '" +  new Date(person.getBirthDate().getTime()) + "')");
//                ps.executeUpdate();
//                ps = connection.prepareStatement("insert into app.numbers(id, number)" +
//                        "values('"+ person.getId().toString() + "'," +
//                        " '" + person.getPhoneNumber().getNumber() + "')");
//                ps.executeUpdate();
//            } catch (SQLException throwable) {
//                throwable.printStackTrace();
//            }
//
//        });
//        connection.close();
//    }
//
//    public List<Person> fetchPersonList() throws SQLException {
//        Connection connection = DriverManager.getConnection("jdbc:derby:/Users/user/Desktop/database/db2;");
//        List<Person> personList = new ArrayList<>();
//        Statement statement = connection.createStatement();
//        ResultSet resultSet = statement.executeQuery("SELECT * FROM PERSONS");
//        while (resultSet.next()){
//            Person person = new Person();
//            person.setId(UUID.fromString(resultSet.getString("id")));
//            person.setName(resultSet.getString("name"));
//            person.setSurname(resultSet.getString("surname"));
//            person.setSurname(resultSet.getString("surname"));
//            person.setPatronymic(resultSet.getString("patronymic"));
//            person.setPosition(resultSet.getString("position"));
//            person.setBirthDate(resultSet.getDate("birth_date"));
//
//            personList.add(person);
//        }
//        connection.close();
//        return personList;
//
//    }
//
//    public void addOrganizations() throws SQLException {
//        Connection connection = DriverManager.getConnection("jdbc:derby:/Users/user/Desktop/database/db2;");
//        List<Organization> organizationList = Application.organizationRepository.getOrganizationList();
//        organizationList.forEach(organization -> {
//            PreparedStatement organizationPs = null;
//
//            try {
//                organizationPs = connection.prepareStatement("insert into app.organizations(id, full_name, short_name, director_id) " +
//                        "values('" + organization.getId().toString() + "'," +
//                        " '" + organization.getFullName() + "'," +
//                        " '" + organization.getShortName() + "'," +
//                        " '" + organization.getDirector().getId() + "')");
//                organization.getContactList().getNumberList().forEach(phoneNumber -> {
//                    PreparedStatement numberPs = null;
//                    try {
//                        numberPs = connection.prepareStatement("insert into app.numbers(id, number)" +
//                                "values('"+ organization.getId().toString() + "'," +
//                                " '" + phoneNumber.getNumber() + "')");
//                        numberPs.executeUpdate();
//                    } catch (SQLException throwable) {
//                        throwable.printStackTrace();
//                    }
//                });
//
//                organizationPs.executeUpdate();
//
//
//            } catch (SQLException throwable) {
//                throwable.printStackTrace();
//            }
//        });
//        connection.close();
//    }
//
//    public void addDepartment() throws SQLException {
//        Connection connection = DriverManager.getConnection("jdbc:derby:/Users/user/Desktop/database/db2;");
//        List<Department> organizationList = Application.departmentRepository.getDepartmentList();
//        organizationList.forEach(department -> {
//            PreparedStatement departmentPs = null;
//
//            try {
//                departmentPs = connection.prepareStatement("insert into app.departments(id, full_name, short_name, director_id) " +
//                        "values('" + department.getId().toString() + "'," +
//                        " '" + department.getFullName() + "'," +
//                        " '" + department.getShortName() + "'," +
//                        " '" + department.getDirector().getId() + "')");
//                department.getContactList().getNumberList().forEach(phoneNumber -> {
//                    PreparedStatement numberPs = null;
//                    try {
//                        numberPs = connection.prepareStatement("insert into app.numbers(id, number)" +
//                                "values('"+ department.getId().toString() + "'," +
//                                " '" + phoneNumber.getNumber() + "')");
//                        numberPs.executeUpdate();
//                    } catch (SQLException throwable) {
//                        throwable.printStackTrace();
//                    }
//                });
//
//                departmentPs.executeUpdate();
//
//
//            } catch (SQLException throwable) {
//                throwable.printStackTrace();
//            }
//        });
//        connection.close();
//    }
}
