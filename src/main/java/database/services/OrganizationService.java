package database.services;

import database.DatabaseConnector;
import database.interfaces.OrganizationDAO;
import entities.PhoneNumber;
import entities.orgstuff.Organization;
import utils.parse_utils.PhoneNumbersList;

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
 * Класс который осущесвляет работу с базой организаций
 */
public class OrganizationService extends DatabaseConnector implements OrganizationDAO {
    Connection connection = getConnection();
    //TODO Добавить логгирование
    private Logger log = Logger.getLogger(getClass().getName());
    NumberService numberService = new NumberService();
    PersonService personService = new PersonService();

    /**
     * Добавляет коллекцию организаций в базу данных
     * @param organizationList
     * @throws SQLException
     */
    @Override
    public void addList(List<Organization> organizationList) throws SQLException {
        PreparedStatement preparedStatement = null;
        String sql = "INSERT INTO APP.ORGANIZATIONS(ID, FULL_NAME, SHORT_NAME, DIRECTOR_ID) VALUES(?, ?, ?, ?)";

        for(Organization organization : organizationList) {
            try {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, organization.getId().toString());
                preparedStatement.setString(2, organization.getFullName());
                preparedStatement.setString(3, organization.getShortName());
                preparedStatement.setString(4, organization.getDirector().getId().toString());

                for(PhoneNumber phoneNumber : organization.getContactList().getNumberList()){
                    numberService.add(organization.getId(), phoneNumber.getNumber());
                }

                preparedStatement.executeUpdate();

            } catch (SQLException ex) {
                log.severe("Возникла проблема при добавление организаций в БД");
                ex.printStackTrace();
            }
        }
        if(preparedStatement != null){
            preparedStatement.close();
        }
    }

    /**
     * Добавляет одну организацию в БД
     * @param organization
     * @throws SQLException
     */
    @Override
    public void add(Organization organization) throws SQLException {
        PreparedStatement preparedStatement = null;
        String sql = "INSERT INTO APP.ORGANIZATIONS(ID, FULL_NAME, SHORT_NAME, DIRECTOR_ID) VALUES(?, ?, ?, ?)";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, organization.getId().toString());
            preparedStatement.setString(2, organization.getFullName());
            preparedStatement.setString(3, organization.getShortName());
            preparedStatement.setString(4, organization.getDirector().getId().toString());

            for(PhoneNumber phoneNumber : organization.getContactList().getNumberList()){
                numberService.add(organization.getId(), phoneNumber.getNumber());
            }

            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            log.severe("Возникла проблема при добавление организации в БД");
            ex.printStackTrace();
        } finally {
            if(preparedStatement != null){
                preparedStatement.close();
            }
        }
    }

    /**
     * Получение всех организаций из БД
     * @return
     * @throws SQLException
     */
    @Override
    public List<Organization> getAll() throws SQLException {
        List<Organization> organizationList = new ArrayList<>();

        String sql = "SELECT * FROM APP.ORGANIZATIONS";
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                Organization organization = new Organization();
                organization.setId(UUID.fromString(resultSet.getString("id")));
                organization.setFullName(resultSet.getString("full_name"));
                organization.setShortName(resultSet.getString("short_name"));
                organization.setDirector(personService.getById(UUID.fromString(resultSet.getString("director_id"))));

                PhoneNumbersList contactList = new PhoneNumbersList(numberService.getAllWithId(UUID.fromString(resultSet.getString("id"))));
                organization.setContactList(contactList);

                organizationList.add(organization);
            }

        } catch (SQLException ex){
            log.severe("Возникла проблема при получение организаций из БД");
            ex.printStackTrace();
        } finally {
            if(statement != null){
                statement.close();
            }
        }
        return organizationList;

    }

    /**
     * Получение организации по его id
     * @param id
     * @return
     * @throws SQLException
     */
    @Override
    public Organization getById(UUID id) throws SQLException {
        PreparedStatement preparedStatement = null;
        String sql = "SELECT * FROM APP.ORGANIZATIONS WHERE ID=?";

        Organization organization = new Organization();
        try{
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id.toString());

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                organization.setId(UUID.fromString(resultSet.getString("id")));
                organization.setFullName(resultSet.getString("full_name"));
                organization.setShortName(resultSet.getString("short_name"));
                organization.setDirector(personService.getById(UUID.fromString(resultSet.getString("director_id"))));

                PhoneNumbersList contactList = new PhoneNumbersList(numberService.getAllWithId(UUID.fromString(resultSet.getString("id"))));
                organization.setContactList(contactList);
            }
        } catch (SQLException throwable) {
            log.severe("Возникла проблема при получение организации из БД");
            throwable.printStackTrace();
        } finally {
            if(preparedStatement != null){
                preparedStatement.close();
            }
        }

        return organization;
    }

    /**
     * Обновление организации в БД
     * @param organization
     * @throws SQLException
     */
    @Override
    public void update(Organization organization) throws SQLException {
        PreparedStatement preparedStatement = null;
        String sql = "UPDATE APP.ORGANIZATIONS SET FULL_NAME=?, SHORT_NAME=?, DIRECTOR_ID=? WHERE ID=?";

        try{
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, organization.getFullName());
            preparedStatement.setString(2, organization.getShortName());
            preparedStatement.setString(3, organization.getDirector().getId().toString());
            preparedStatement.setString(4, organization.getId().toString());
            numberService.updateList(organization.getId(), organization.getContactList().getNumberList());

            preparedStatement.executeUpdate();
        } catch (SQLException throwable) {
            log.severe("Возникла проблема при обновление организации в БД");
            throwable.printStackTrace();
        } finally {
            if(preparedStatement != null){
                preparedStatement.close();
            }
        }
    }

    /**
     * Удаление организации из БД
     * @param organization
     * @throws SQLException
     */
    @Override
    public void remove(Organization organization) throws SQLException {
        PreparedStatement preparedStatement = null;

        String sql = "DELETE FROM APP.ORGANIZATIONS WHERE ID=?";

        try{
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, organization.getId().toString());
            organization.getContactList().getNumberList().forEach(num -> {
                try {
                    numberService.removeByID(organization.getId());
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
            });

            preparedStatement.executeUpdate();
        } catch (SQLException throwable) {
            log.severe("Возникла проблема при удаление организации в БД");
            throwable.printStackTrace();
        } finally {
            if(preparedStatement != null){
                preparedStatement.close();
            }
        }

    }
}
