package utils.parse_utils.adapters;

import entities.orgstuff.Department;
import entities.orgstuff.Person;
import utils.parse_utils.DepartmentList;
import utils.parse_utils.PhoneNumbersList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import web_controllers.Application;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * DepartmentAdapter нужен для преобразования AdaptedDepartment в Department.
 */
public class DepartmentAdapter extends XmlAdapter<DepartmentAdapter.AdaptedDepartment, Department> {
    /**
     * При операции unmarshall AdaptedDepartment передается в эту функцию и вызывается конструктор Department на основне полей AdaptedDepartment
     * @param v - AdaptedDepartment
     * @return Department
     * @throws Exception
     */
    public static Logger logger = LogManager.getRootLogger();
    DepartmentList departments;
    List<Person> personList = Application.personRepository.getPersonList();
    public DepartmentAdapter(DepartmentList departmentList){
        this.departments = departmentList;
    }

    /**
     * Класс AdapterDepartment - заменяет сущность Department при парсинге в XML. Для того, чтобы не нагружать модель сущности Department.
     */
    @XmlRootElement(name = "department")
    @XmlJavaTypeAdapter(DepartmentAdapter.class)
    public static class AdaptedDepartment {
        private String fullName;
        private String shortName;
        private Person director;
        private PhoneNumbersList contactList;

        public AdaptedDepartment(){}
        public AdaptedDepartment(String fullName, String shortName, Person director, PhoneNumbersList contactList){
            this.fullName = fullName;
            this.shortName = shortName;
            this.director = director;
            this.contactList = contactList;

        }
        public String getFullName() {
            return fullName;
        }

        @XmlElement(name = "fullName")
        public void setFullName(String fullName) {
            this.fullName = fullName;
        }


        public String getShortName() {
            return shortName;
        }
        @XmlElement(name = "shortName")
        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public Person getDirector() {
            return director;
        }

        @XmlElement(name = "person")
        public void setDirector(Person director) {
            this.director = director;
        }

        public PhoneNumbersList getContactList(){
            return contactList;
        }

        @XmlElement(name = "phoneNumberList")
        public void setContactList(PhoneNumbersList numbersList){
            this.contactList = numbersList;
        }
    }

    @Override
    public Department unmarshal(AdaptedDepartment v) throws Exception {
        if(!personList.contains(v.getDirector())){
            logger.error(v.getDirector() + " - Такого подразделения не существует");
            throw new IllegalArgumentException("There is no such person in the company");
        }
        for(Person person : personList) {
            if(person.equals(v.getDirector())){
                System.out.println(person.getId() + " " + person);
                v.setDirector(person);
                v.getDirector().setId(person.getId());
                break;
            }
        }
        return new Department(v.getFullName(), v.getShortName(), v.getDirector(), v.getContactList());
    }

    @Override
    public AdaptedDepartment marshal(Department v) throws Exception {
        return new AdaptedDepartment(v.getFullName(), v.getShortName(), v.getDirector(), v.getContactList());
    }

    public List<Department> adaptedDepartmentsToDepartments(){
        List<Department> departmentList = new ArrayList<>();
        departments.getDepartmentList().forEach(d -> {
            try {
                departmentList.add(unmarshal(d));
            } catch (Exception e) {
                logger.error("При парсинге подразделения возникла ошибка.");
                e.printStackTrace();
            }
        });
        return departmentList;
    }


}


