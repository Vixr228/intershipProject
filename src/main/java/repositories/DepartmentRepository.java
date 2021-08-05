package repositories;

import entities.orgstuff.Department;
import entities.orgstuff.Organization;
import utils.XMLParser;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class DepartmentRepository {
    public static final String DEPARTMENTS_XML_PATH = "/Users/user/Desktop/intershiprepository/src/main/resources/xml/DepartmentList.xml";
    Logger log = Logger.getLogger(getClass().getName());
    XMLParser xmlParser;
    List<Department> departmentList;

    public DepartmentRepository(){
        xmlParser = new XMLParser();
        try {
            departmentList = xmlParser.parseDepartments(DEPARTMENTS_XML_PATH);
        } catch (Exception e) {
            log.severe("Проблема при парсинге подразделений из XML файла");
            e.printStackTrace();
        }
    }

    /**
     * Возвращает список все организации
     * @return List<Organization>
     */
    public List<Department> getDepartmentList() {
        return departmentList;
    }

    public Department getDepartmentById(UUID id) {
        Department department = null;
        for (Department d : departmentList) {
            if (d.getId().equals(id)) {
                department = d;
                break;
            }
        }
        return department;
    }
}
