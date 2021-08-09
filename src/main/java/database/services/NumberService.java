package database.services;

import database.DatabaseConnector;
import database.interfaces.NumberDAO;
import entities.PhoneNumber;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Класс который осущевствляет доступ к базе данных номеров.
 */
public class NumberService extends DatabaseConnector implements NumberDAO {
    Connection connection = getConnection();
    private Logger log = Logger.getLogger(getClass().getName());

    /**
     * Добавление номера
     * @param id - id сущности, чей номер добавляем
     * @param number
     * @throws SQLException
     */
    @Override
    public void add(UUID id, String number) throws SQLException {
        PreparedStatement preparedStatement = null;

        String sql = "INSERT INTO APP.NUMBERS(ID, NUMBER) VALUES(?, ?)";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id.toString());
            preparedStatement.setString(2, number);

            preparedStatement.executeUpdate();
        } catch (SQLException ex){
            log.severe("Возникла проблема при добавление номера в БД");
            ex.printStackTrace();
        } finally {
            if(preparedStatement != null){
                preparedStatement.close();
            }
        }
    }

    /**
     * Получение всех номеров привязнных к id
     * @param id
     * @return
     * @throws SQLException
     */
    public List<PhoneNumber> getAllWithId(UUID id) throws SQLException {
        List<PhoneNumber> phoneNumberList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        String sql = "SELECT * FROM APP.NUMBERS WHERE ID=?";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id.toString());

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                phoneNumberList.add(new PhoneNumber(resultSet.getString("number")));
            }
        } catch (SQLException throwable) {
            log.severe("Возникла проблема при добавление номеров в БД");
            throwable.printStackTrace();
        } finally {
            if(preparedStatement != null){
                preparedStatement.close();
            }
        }

        return phoneNumberList;
    }

    /**
     * Получение одного номера по id
     * @param id
     * @return
     * @throws SQLException
     */
    @Override
    public PhoneNumber getById(UUID id) throws SQLException {
        PreparedStatement preparedStatement = null;
        String sql = "SELECT * FROM APP.NUMBERS WHERE ID=?";

        PhoneNumber phoneNumber = new PhoneNumber();
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id.toString());

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                phoneNumber.setNumber(resultSet.getString("number"));
            } else {
                log.severe("Номера с таким id нет в таблице - " + id.toString());
                throw new SQLException();
            }

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } finally {
            if(preparedStatement != null) {
                preparedStatement.close();
            }
        }

        return phoneNumber;
    }

    /**
     * Удаление номера по id
     * @param id
     * @throws SQLException
     */
    @Override
    public void removeByID(UUID id) throws SQLException {
        PreparedStatement preparedStatement = null;
        String sql = "DELETE FROM APP.NUMBERS WHERE ID=?";

        try{
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id.toString());

            preparedStatement.executeUpdate();
        } catch (SQLException throwable) {
            log.severe("Возникла проблема при удаление номера из БД");
            throwable.printStackTrace();
        } finally {
            if(preparedStatement != null){
                preparedStatement.close();
            }
        }
    }

    /**
     * Обновление номера одной из сущности
     * @param id
     * @param number
     * @throws SQLException
     */
    @Override
    public void update(UUID id, String number) throws SQLException {
        PreparedStatement preparedStatement = null;
        String sql = "UPDATE APP.NUMBERS SET NUMBER=? WHERE ID=?";

        try{
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, number);
            preparedStatement.setString(2, id.toString());

            preparedStatement.executeUpdate();
        } catch (SQLException throwable) {
            log.severe("Возникла проблема при обновление номера в БД");
            throwable.printStackTrace();
        } finally {
            if(preparedStatement != null){
                preparedStatement.close();
            }
        }
    }

    /**
     * Обновление нескольких номеров
     * @param id
     * @param phoneNumberList
     * @throws SQLException
     */
    public void updateList(UUID id, List<PhoneNumber> phoneNumberList) throws SQLException {
        PreparedStatement preparedStatement = null;
        String sql = "UPDATE APP.NUMBERS SET NUMBER=? WHERE ID=?";

        for(PhoneNumber number : phoneNumberList) {
            try {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, number.getNumber());
                preparedStatement.setString(2, id.toString());

                preparedStatement.executeUpdate();
            } catch (SQLException throwable) {
                log.severe("Возникла проблема при обновление номеров в БД");
                throwable.printStackTrace();
            } finally {
                if(preparedStatement != null){
                    preparedStatement.close();
                }
            }
        }
    }
}
