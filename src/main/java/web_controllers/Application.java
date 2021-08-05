package web_controllers;

import database.DatabaseConnector;
import entities.documents.Document;
import entities.documents.Incoming;
import entities.documents.Outgoing;
import entities.documents.Task;
import entities.orgstuff.Person;
import repositories.DepartmentRepository;
import repositories.OrganizationRepository;
import repositories.PersonRepository;
import utils.DocumentFactory;

import javax.ws.rs.ApplicationPath;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Класс, который делает инициализацию данных на странице первоначальной странице (http://localhost:8080/intershipProject-1.0-SNAPSHOT/)
 */

@ApplicationPath("/")
public class Application extends javax.ws.rs.core.Application {

    Logger log = Logger.getLogger(getClass().getName());
    /**
     * Заранее подгтовленные массивы с данными
     */
    public static PersonRepository personRepository;
    public static List<Document> documents;
    public static OrganizationRepository organizationRepository;
    public static DepartmentRepository departmentRepository;
    List<String> texts = new ArrayList<String>(){{
        add("Купить хлеб");
        add("Помыть машину");
        add("Выкинуть мусор");
        add("Купить телефон");
        add("Продать квартиру");
        add("Купить клей");
        add("Помыть окна");
        add("Покормить голубя");
        add("Вернуть телефон");
        add("Сломать ногу");
    }};

    List<String> deliveryMethods = new ArrayList<String>(){{
        add("Самолет");
        add("Поезд");
        add("Машина");
        add("Корабль");
    }};

    DocumentFactory documentFactory;
    /**
     * Лист с классами для того, чтобы выбирать случайный класс и подставлять его во входные параметры
     */
    List<Class<? extends Document>> classes = new ArrayList<Class<? extends Document>>(){{
        add(Task.class);
        add(Incoming.class);
        add(Outgoing.class);
    }};

    public Application() throws IOException, SQLException {
        personRepository = new PersonRepository();
        organizationRepository = new OrganizationRepository();
        departmentRepository = new DepartmentRepository();
        documents = new ArrayList<>();
        documentFactory  = new DocumentFactory(texts, personRepository.getPersonList(), deliveryMethods);
        for(int i = 0; i < 10; i++){
            int index = (int) (Math.random() * 3);
            documents.add(documentFactory.createDocument(classes.get(index)));
        }


//        personRepository.getPersonList().forEach(person -> {
//            log.severe("PERSON: " + person.getId() + " " + person);
//        });
//
//        organizationRepository.getOrganizationList().forEach(organization -> {
//            log.severe("ORG: " + organization.getDirector().getId() + " " + organization.getDirector());
//        });

        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.addPerson();
        databaseConnector.addOrganizations();
        databaseConnector.addDepartment();
        databaseConnector.fetchPersonList();
    }

}
