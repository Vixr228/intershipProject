package utils;

import entities.PhoneNumber;
import entities.orgstuff.Department;
import entities.orgstuff.Organization;
import entities.orgstuff.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import utils.parse_utils.PhoneNumbersList;
import javax.xml.bind.JAXBException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class XMLParserTest {
    XMLParser xmlParser = new XMLParser();

    /**
     * Заранее приготовленные листы для сравнения того, что спарсили из xml
     */
    List<PhoneNumber> phoneNumberList = new ArrayList<PhoneNumber>(){{
        add(new PhoneNumber("123123123"));
        add(new PhoneNumber("6969969"));
    }};

    List<Organization> organizations = new ArrayList<Organization>(){{
        add(new Organization("Гуглее", "Гугл",
                new Person("Anton", "Антонов", "Антонович", "Работяга", new Date(1994 - 1900, 8 - 1, 31), new PhoneNumber("89135489634")),
                new PhoneNumbersList(phoneNumberList)));
        add(new Organization("Яндекс", "Ян",
                new Person("Сергей", "Антонов", "ФЫв", "Работяга", new Date(1994 - 1900, 8 - 1, 31), new PhoneNumber("89135489634")),
                new PhoneNumbersList(phoneNumberList)));
        add(new Organization("Амазон", "А",
                new Person("Джефф", "Безос", "Джефович", "Босс", new Date(1985 - 1900, 4 - 1, 21), new PhoneNumber("79581236587")),
                new PhoneNumbersList(new ArrayList<PhoneNumber>(){{
                    add(new PhoneNumber("111123665"));
                    add(new PhoneNumber("222245569"));
                }})));
    }};

    List<Person> people = new ArrayList<Person>(){{
        add(new Person("Антон", "Антонов", "Антонович", "Работяга", new Date(1994 - 1900, 8 - 1, 31), new PhoneNumber("89135489634")));
        add(new Person("Сергей", "Сергеев", "Сергеевич", "Начальник", new Date(1991 - 1900, 1 - 1, 21), new PhoneNumber("89165639434")));
        add(new Person("Константин", "Кубиков", "Константинович", "Директор", new Date(1997 - 1900, 8 - 1, 31), new PhoneNumber("89165639434")));

    }};


    List<Department> departments = new ArrayList<Department>(){{
        add(new Department("Гуглее", "Гугл",
                new Person("Anton", "Антонов", "Антонович", "Работяга", new Date(1994 - 1900, 8 - 1, 31), new PhoneNumber("89135489634")),
                new PhoneNumbersList(phoneNumberList)));
        add(new Department("Яндекс", "Ян",
                new Person("Сергей", "Антонов", "ФЫв", "Работяга", new Date(1994 - 1900, 8 - 1, 31), new PhoneNumber("89135489634")),
                new PhoneNumbersList(new ArrayList<PhoneNumber>(){{
                        add(new PhoneNumber("488823"));
                        add(new PhoneNumber("7897897987"));
                    }})));
        add(new Department("Амазон", "А",
                new Person("Джефф", "Безос", "Джефович", "Босс", new Date(1985 - 1900, 4 - 1, 21), new PhoneNumber("79581236587")),
                new PhoneNumbersList(new ArrayList<PhoneNumber>(){{
                    add(new PhoneNumber("111123665"));
                    add(new PhoneNumber("222245569"));
                }})));
    }};

    /**
     * Парсит xml файл, проверяет, что поля заполнились (то есть не null)
     * Проверяет, что у всех объектов класс соотествует классу Person
     * Сравнивает распаршенный список с заранее подгтовленным
     * @throws Exception
     */
    @Test
    void parsePersonsShouldBeTrue() throws Exception {
        List<Person> personList = xmlParser.parsePersons("src/main/resources/xml/testPersonList.xml");
        personList.forEach(person -> {
            assertNotNull(person.getName());
            assertNotNull(person.getSurname());
            assertNotNull(person.getPatronymic());
            assertNotNull(person.getBirthDate());
            assertNotNull(person.getId());
            assertNotNull(person.getPhoneNumber());
        });

        personList.forEach(person -> {
            assertEquals(person.getClass(), Person.class);
        });

        for(int i = 0; i < personList.size(); i++){
            assertTrue(personList.get(i).equals(people.get(i)));
        }
    }

    /**
     * Парсит xml файл, проверяет, что поля заполнились (то есть не null)
     * Проверяет, что у всех объектов класс соотествует классу Department
     * Сравнивает распаршенный список с заранее подгтовленным
     * @throws Exception
     */
    @Test
    void parseDepartmentsShouldBeTrue() throws Exception {
        List<Department> departmentList = xmlParser.parseDepartments("src/main/resources/xml/DepartmentList.xml");

        departmentList.forEach(department -> {
            assertNotNull(department.getFullName());
            assertNotNull(department.getShortName());
            assertNotNull(department.getId());
            assertNotNull(department.getContactList());
            assertNotNull(department.getId());
            assertNotNull(department.getDirector());
        });

        departmentList.forEach(department -> {
            assertEquals(department.getClass(), Department.class);
        });

        for(int i = 0; i < departmentList.size(); i++){
            assertTrue(departmentList.get(i).equals(departments.get(i)));
        }
    }

    /**
     * Парсит xml файл, проверяет, что поле FullName отсутствует
     * Сравнивает распаршенный список с заранее подгтовленным
     * @throws Exception
     */
    @Test
    void parseDepartmentsShouldBeFalse() throws JAXBException, SQLException {
        List<Department> departmentList = xmlParser.parseDepartments("src/main/resources/xml/testDepartmentList.xml");
        departmentList.forEach(department -> {
            assertNull(department.getFullName());
        });

        for(int i = 0; i < departmentList.size(); i++){
            assertFalse(departmentList.get(i).equals(departments.get(i)));
        }

    }
    /**
     * Парсит xml файл, проверяет, что поля заполнились (то есть не null)
     * Проверяет, что у всех объектов класс соотествует классу Organization
     * Сравнивает распаршенный список с заранее подгтовленным
     * @throws Exception
     */
    @Test
    void parseOrganizationsShouldBeTrue() throws Exception {
        List<Organization> organizationList = xmlParser.parseOrganizations("src/main/resources/xml/OrganizationList.xml");

        organizationList.forEach(organization -> {
            assertNotNull(organization.getFullName());
            assertNotNull(organization.getShortName());
            assertNotNull(organization.getId());
            assertNotNull(organization.getContactList());
            assertNotNull(organization.getId());
            assertNotNull(organization.getDirector());
        });

        organizationList.forEach(organization -> {
            assertEquals(organization.getClass(), Organization.class);
        });

        for(int i = 0; i < organizationList.size(); i++){
            assertTrue(organizationList.get(i).equals(organizations.get(i)));
        }
    }
    /**
     * Парсит xml файл, проверяет, что поле FullName отсутствует
     * Сравнивает распаршенный список с заранее подгтовленным
     * @throws Exception
     */
    @Test
    void parseOrganizationsShouldBeFalse() throws JAXBException, SQLException {
        List<Organization> organizationList = xmlParser.parseOrganizations("src/main/resources/xml/testOrganizationList.xml");
        organizationList.forEach(organization -> {
            assertNull(organization.getFullName());
        });

        for(int i = 0; i < organizationList.size(); i++){
            assertFalse(organizationList.get(i).equals(organizations.get(i)));
        }

    }

}