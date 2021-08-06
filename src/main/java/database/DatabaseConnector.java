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

}
