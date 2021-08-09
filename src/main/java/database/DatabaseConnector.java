package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Класс устанавливает соединение с БД
 */
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

}
