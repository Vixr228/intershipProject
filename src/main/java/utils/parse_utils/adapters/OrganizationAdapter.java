package utils.parse_utils.adapters;

import entities.orgstuff.Organization;
import entities.orgstuff.Person;
import repositories.PersonRepository;
import utils.parse_utils.OrganizationList;
import utils.parse_utils.PhoneNumbersList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import web_controllers.Application;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * OrganizationAdapter нужен для преобразования AdaptedOrganization в Organization.
 */

public class OrganizationAdapter extends XmlAdapter<OrganizationAdapter.AdaptedOrganization, Organization> {
    /**
     * При операции unmarshall AdaptedOrganization передается в эту функцию и вызывается конструктор Organization на основне полей AdaptedOrganization
     * @param v - AdaptedOrganization
     * @return Organization
     * @throws Exception
     */
    public static Logger logger = LogManager.getRootLogger();
    OrganizationList organizationList;
    List<Person> personList = Application.personRepository.getPersonList();


    public OrganizationAdapter(OrganizationList organizationList){
        this.organizationList = organizationList;

    }

    /**
     * Класс AdaptedOrganization - заменяет сущность Organization при парсинге в XML. Для того, чтобы не нагружать модель сущности Organization.
     */
    @XmlRootElement(name = "organization")
    public static class AdaptedOrganization {
        private String fullName;
        private String shortName;
        private Person director;
        private PhoneNumbersList contactList;

        public AdaptedOrganization(){}
        public AdaptedOrganization(String fullName, String shortName, Person director, PhoneNumbersList contactList){
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
    public Organization unmarshal(AdaptedOrganization v) throws Exception {

        if(!personList.contains(v.getDirector())){
            logger.error(v.getDirector() + " - Такого работника не существует");
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

        return new Organization(v.getFullName(), v.getShortName(), v.getDirector(), v.getContactList());

    }

    @Override
    public AdaptedOrganization marshal(Organization v) throws Exception {
        return new AdaptedOrganization(v.getFullName(), v.getShortName(), v.getDirector(), v.getContactList());
    }

    public List<Organization> adaptedOrganizationsToOrganizations(){
        List<Organization> organizations = new ArrayList<>();
        organizationList.getOrganizationList().forEach(o -> {
            try {
                organizations.add(unmarshal(o));
            } catch (Exception e) {
                logger.error("При парсинге организации возникла ошибка.");
                e.printStackTrace();
            }
        });
        return organizations;
    }


}


