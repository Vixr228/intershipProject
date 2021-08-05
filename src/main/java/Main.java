import entities.documents.Document;
import entities.documents.Incoming;
import entities.documents.Outgoing;
import entities.documents.Task;
import entities.orgstuff.Department;
import entities.orgstuff.Organization;
import entities.orgstuff.Person;
import utils.DocumentFactory;
import utils.JSONWriter;
import utils.XMLParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


public class Main {
    public static final String PERSONS_XML_PATH = "src/main/resources/xml/PersonList.xml";
    public static final String DEPARTMENTS_XML_PATH = "src/main/resources/xml/DepartmentList.xml";
    public static final String ORGANIZATIONS_XML_PATH = "src/main/resources/xml/OrganizationList.xml";
    public static Logger logger = LogManager.getRootLogger();

    public static void main(String[] args) throws Exception {

        XMLParser xmlParser = new XMLParser();
        JSONWriter jsonWriter = new JSONWriter();

        /**
         * Парсим XML файлы
         */
        List<Person> personList = xmlParser.parsePersons(PERSONS_XML_PATH);
        List<Organization> organizationList = xmlParser.parseOrganizations(ORGANIZATIONS_XML_PATH);
        List<Department> departmentList = xmlParser.parseDepartments(DEPARTMENTS_XML_PATH);

        personList.forEach(person -> {
            logger.info(person.toString());
            System.out.println(person.getId() + " " + person);
        });

        organizationList.forEach(organization -> {
            logger.info(organization.toString());
            System.out.println(organization.getDirector().getId() + " " +organization.getDirector());
        });
        departmentList.forEach(department -> {
            logger.info(department.toString());
        });



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

        /**
         * Заполняем документы
         */
        List<Document> documents = new ArrayList<>();
        DocumentFactory documentFactory = new DocumentFactory(texts, personList, deliveryMethods);
        /**
         * Лист с классами для того, чтобы выбирать случайный класс и подставлять его во входные параметры
         */
        List<Class<? extends Document>> classes = new ArrayList<Class<? extends Document>>(){{
            add(Task.class);
            add(Incoming.class);
            add(Outgoing.class);
        }};
        for(int i = 0; i < 10; i++){
            int index = (int) (Math.random() * 3);
            documents.add(documentFactory.createDocument(classes.get(index)));
        }

        /**
         * Пишем отчет в JSON файлы
         */
        jsonWriter.writeDocumentsToJson(documents);






    }
}
