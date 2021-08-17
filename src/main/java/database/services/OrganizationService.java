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
    private Logger log = Logger.getLogger(getClass().getName());
    NumberService numberService = new NumberService();
    PersonService personService = new PersonService();

    public OrganizationService() throws Exception {
    }

    /**
     * Добавляет коллекцию организаций в базу данных
     * @param organizationList
     * @throws SQLException
     */
    @Override
    public void addList(List<Organization> organizationList) throws SQLException {
        String sql = "INSERT INTO APP.ORGANIZATIONS(id, full_name, short_name, director_id) VALUES(?, ?, ?, ?)";

        for(Organization organization : organizationList) {
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

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
    }

    /**
     * Добавляет одну организацию в БД
     * @param organization
     * @throws SQLException
     */
    @Override
    public void add(Organization organization) throws SQLException {
        String sql = "INSERT INTO APP.ORGANIZATIONS(id, full_name, short_name, director_id) VALUES(?, ?, ?, ?)";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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

        try(Statement statement = connection.createStatement()) {

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
        String sql = "SELECT * FROM APP.ORGANIZATIONS WHERE id=?";

        Organization organization = new Organization();
        try( PreparedStatement preparedStatement = connection.prepareStatement(sql)){

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
        String sql = "UPDATE APP.ORGANIZATIONS SET full_name=?, short_name=?, director_id=? WHERE id=?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setString(1, organization.getFullName());
            preparedStatement.setString(2, organization.getShortName());
            preparedStatement.setString(3, organization.getDirector().getId().toString());
            preparedStatement.setString(4, organization.getId().toString());
            numberService.updateList(organization.getId(), organization.getContactList().getNumberList());

            preparedStatement.executeUpdate();
        } catch (SQLException throwable) {
            log.severe("Возникла проблема при обновление организации в БД");
            throwable.printStackTrace();
        }
    }

    /**
     * Удаление организации из БД
     * @param organization
     * @throws SQLException
     */
    @Override
    public void remove(Organization organization) throws SQLException {

        String sql = "DELETE FROM APP.ORGANIZATIONS WHERE id=?";

        try( PreparedStatement preparedStatement = connection.prepareStatement(sql)){

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
        }
    }
}
