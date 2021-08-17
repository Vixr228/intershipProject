package web_controllers;

import database.DatabaseConnector;
import database.services.DepartmentService;
import database.services.OrganizationService;
import database.services.PersonService;
import entities.documents.Document;
import entities.documents.Incoming;
import entities.documents.Outgoing;
import entities.documents.Task;
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
    public static PersonService personService;
    public static  OrganizationService organizationService;
    public static  DepartmentService departmentService;
    DatabaseConnector databaseConnector = new DatabaseConnector();
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

    /**
     * Инициализируем все данные. Парсим XML, заполняем БД из этих данных. Создаем документы.
     * @throws IOException
     * @throws SQLException
     */
    public Application() throws Exception {
        personService = new PersonService();
        organizationService = new OrganizationService();
        departmentService = new DepartmentService();

        personRepository = new PersonRepository();
        organizationRepository = new OrganizationRepository();
        departmentRepository = new DepartmentRepository();

        personService.addList(Application.personRepository.getPersonListFromXML());
        organizationService.addList(Application.organizationRepository.getOrganizationListFromXML());
        departmentService.addList(Application.departmentRepository.getDepartmentListFromXML());

        documents = new ArrayList<>();
        documentFactory  = new DocumentFactory(texts, personService.getAll(), deliveryMethods);
        for(int i = 0; i < 10; i++){
            int index = (int) (Math.random() * 3);
            documents.add(documentFactory.createDocument(classes.get(index)));
        }
    }
}
