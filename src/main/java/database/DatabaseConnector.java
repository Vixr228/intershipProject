package database;

import entities.orgstuff.Organization;
import entities.orgstuff.Person;
import repositories.PersonRepository;
import utils.XMLParser;

import javax.xml.bind.JAXBException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class DatabaseConnector {
    PersonRepository personRepository = new PersonRepository();


    public void addPerson() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:derby:/Users/user/Desktop/database/db2;");
        List<Person> personList = personRepository.getPersonList();
        personList.forEach(person -> {
            PreparedStatement ps = null;
            try {
                ps = connection.prepareStatement("insert into app.persons(id, name, surname, patronymic, position) " +
                        "values('" + person.getId().toString() + "'," +
                        " '" + person.getName() + "'," +
                        " '" + person.getSurname() + "'," +
                        " '" + person.getPosition() + "'," +
                        " '" + person.getPosition() + "') ");
                ps.executeUpdate();
                ps = connection.prepareStatement("insert into app.numbers(id, number)" +
                        "values('"+ person.getId().toString() + "'," +
                        " '" + person.getPhoneNumber().getNumber() + "')");
                ps.executeUpdate();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }

        });
        connection.close();
    }

    public void addOrganizations() throws SQLException {
        XMLParser xmlParser = new XMLParser();
        Connection connection = DriverManager.getConnection("jdbc:derby:/Users/user/Desktop/database/db2;");
        List<Organization> organizationList = null;
        try {
            organizationList = xmlParser.parseOrganizations("/Users/user/Desktop/intershiprepository/src/main/resources/xml/OrganizationList.xml");
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        organizationList.forEach(organization -> {
            PreparedStatement organizationPs = null;

            try {
                organizationPs = connection.prepareStatement("insert into app.organizations(id, full_name, short_name, director_id) " +
                        "values('" + organization.getId().toString() + "'," +
                        " '" + organization.getFullName() + "'," +
                        " '" + organization.getShortName() + "'," +
                        " '" + organization.getDirector().getId() + "')");
                organization.getContactList().getNumberList().forEach(phoneNumber -> {
                    PreparedStatement numberPs = null;
                    try {
                        numberPs = connection.prepareStatement("insert into app.numbers(id, number)" +
                                "values('"+ organization.getId().toString() + "'," +
                                " '" + phoneNumber.getNumber() + "')");
                        numberPs.executeUpdate();
                    } catch (SQLException throwable) {
                        throwable.printStackTrace();
                    }
                });

                organizationPs.executeUpdate();


            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }

        });

    }
}
