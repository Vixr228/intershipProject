package database.services;

import database.DatabaseConnector;
import database.interfaces.PersonDAO;
import entities.orgstuff.Person;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;


/**
 * Класс который осущевствляет доступ к базе данных работников.
 */
public class PersonService extends DatabaseConnector implements PersonDAO {
    Connection connection = getConnection();
    private Logger log = Logger.getLogger(getClass().getName());
    NumberService numberService = new NumberService();

    public PersonService() throws Exception {
    }

    /**
     * Добавляет коллекцию работников в базу данных
     * @param personList
     * @throws SQLException
     */
    @Override
    public void addList(List<Person> personList) throws SQLException {



        String sql = "INSERT INTO APP.PERSONS(id, name, surname, patronymic, position , birth_date) VALUES(?, ?, ?, ?, ?, ?)";

        for(Person person : personList) {
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setString(1, person.getId().toString());
                preparedStatement.setString(2, person.getName());
                preparedStatement.setString(3, person.getSurname());
                preparedStatement.setString(4, person.getPatronymic());
                preparedStatement.setString(5, person.getPosition());
                preparedStatement.setDate(6, new Date(person.getBirthDate().getTime()));

                numberService.add(person.getId(), person.getPhoneNumber().getNumber());

                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                log.severe("Возникла проблема при добавление работников в БД");
                ex.printStackTrace();
            }
        }
    }

    /**
     * Добавление одного работника в БД
     * @param person
     * @throws SQLException
     */
    @Override
    public void add(Person person) throws SQLException {

        String sql = "INSERT INTO APP.PERSONS(id, name, surname, patronymic, position, birth_date) VALUES(?, ?, ?, ?, ?, ?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setString(1, person.getId().toString());
            preparedStatement.setString(2, person.getName());
            preparedStatement.setString(3, person.getSurname());
            preparedStatement.setString(4, person.getPatronymic());
            preparedStatement.setString(5, person.getPosition());
            preparedStatement.setDate(6, new Date(person.getBirthDate().getTime()));

            numberService.add(person.getId(), person.getPhoneNumber().getNumber());

            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            log.severe("Возникла проблема при добавление работника в БД");
            ex.printStackTrace();
        }
    }

    /**
     * Получение списка работников из БД
     * @return
     * @throws SQLException
     */
    @Override
    public List<Person> getAll() throws SQLException {
        List<Person> personList = new ArrayList<>();

        String sql = "SELECT * FROM APP.PERSONS";

        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                Person person = new Person();
                person.setId(UUID.fromString(resultSet.getString("id")));
                person.setName(resultSet.getString("name"));
                person.setSurname(resultSet.getString("surname"));
                person.setPatronymic(resultSet.getString("patronymic"));
                person.setPosition(resultSet.getString("position"));
                person.setBirthDate(resultSet.getDate("birth_date"));

                person.setPhoneNumber(numberService.getById(UUID.fromString(resultSet.getString("id"))));

                personList.add(person);
            }

        } catch (SQLException ex){
            log.severe("Возникла проблема при получение работников из БД");
            ex.printStackTrace();
        }

        return personList;
    }

    /**
     * Получение работника по его id
     * @param id
     * @return
     * @throws SQLException
     */
    @Override
    public Person getById(UUID id) throws SQLException {
        String sql = "SELECT * FROM APP.PERSONS WHERE id=?";

        Person person = new Person();
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, id.toString());

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                person.setId(UUID.fromString(resultSet.getString("id")));
                person.setName(resultSet.getString("name"));
                person.setSurname(resultSet.getString("surname"));
                person.setPatronymic(resultSet.getString("patronymic"));
                person.setPosition(resultSet.getString("position"));
                person.setBirthDate(resultSet.getDate("birth_date"));

                person.setPhoneNumber(numberService.getById(UUID.fromString(resultSet.getString("id"))));
            }
        } catch (SQLException throwable) {
            log.severe("Возникла проблема при получение работника из БД");
           throwable.printStackTrace();
        }

        return person;
    }

    /**
     * Обновление данных по работнику в БД
     * @param person
     * @throws SQLException
     */
    @Override
    public void update(Person person) throws SQLException {
        String sql = "UPDATE APP.PERSONS SET name=?, surname=?, patronymic=?, position =?, birth_date=? WHERE id=?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setString(1, person.getName());
            preparedStatement.setString(2, person.getSurname());
            preparedStatement.setString(3, person.getPatronymic());
            preparedStatement.setString(4, person.getPosition());
            preparedStatement.setDate(5, new Date(person.getBirthDate().getTime()));
            preparedStatement.setString(6, person.getId().toString());
            numberService.update(person.getId(), person.getPhoneNumber().getNumber());

            preparedStatement.executeUpdate();
        } catch (SQLException throwable) {
            log.severe("Возникла проблема при обновление работников в БД");
            throwable.printStackTrace();
        }
    }

    /**
     * Удаление работника из БД
     * @param person
     * @throws SQLException
     */
    @Override
    public void remove(Person person) throws SQLException {

        String sql = "DELETE FROM APP.PERSONS WHERE id=?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setString(1, person.getId().toString());
            numberService.removeByID(person.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException throwable) {
            log.severe("Возникла проблема при удаление работников из БД");
            throwable.printStackTrace();
        }
    }
}
