package database;

import entities.PhoneNumber;
import entities.orgstuff.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class NumberService extends DatabaseConnector implements NumberDAO{
    Connection connection = getConnection();
    private Logger log = Logger.getLogger(getClass().getName());

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
            ex.printStackTrace();
        } finally {
            if(preparedStatement != null){
                preparedStatement.close();
            }
        }
    }

    public void addList(UUID id, List<PhoneNumber> numberList) throws SQLException {
        PreparedStatement preparedStatement = null;

        String sql = "INSERT INTO APP.NUMBERS(ID, NUMBER) VALUES(?, ?)";

        for(PhoneNumber number : numberList) {
            try {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, id.toString());
                preparedStatement.setString(2, number.getNumber());

                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            }
        }
    }


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
            throwable.printStackTrace();
        } finally {
            if(preparedStatement != null){
                preparedStatement.close();
            }
        }

        return phoneNumberList;
    }

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

    @Override
    public void removeByID(UUID id) throws SQLException {
        PreparedStatement preparedStatement = null;
        String sql = "DELETE FROM APP.NUMBERS WHERE ID=?";

        try{
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id.toString());

            preparedStatement.executeUpdate();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } finally {
            if(preparedStatement != null){
                preparedStatement.close();
            }
        }
    }

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
            throwable.printStackTrace();
        } finally {
            if(preparedStatement != null){
                preparedStatement.close();
            }
        }
    }

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
                throwable.printStackTrace();
            } finally {
                if(preparedStatement != null){
                    preparedStatement.close();
                }
            }
        }
    }
}
