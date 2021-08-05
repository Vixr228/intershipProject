package repositories;

import entities.orgstuff.Organization;
import entities.orgstuff.Person;
import utils.XMLParser;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class OrganizationRepository {
    public static final String ORGANIZATIONS_XML_PATH = "/Users/user/Desktop/intershiprepository/src/main/resources/xml/OrganizationList.xml";
    Logger log = Logger.getLogger(getClass().getName());
    XMLParser xmlParser;
    List<Organization> organizationList;

    public OrganizationRepository(){
        xmlParser = new XMLParser();
        try {
            organizationList = xmlParser.parseOrganizations(ORGANIZATIONS_XML_PATH);
        } catch (Exception e) {
            log.severe("Проблема при парсинге организация из XML файла");
            e.printStackTrace();
        }
    }

    /**
     * Возвращает список все организации
     * @return List<Organization>
     */
    public List<Organization> getOrganizationList() {
        return organizationList;
    }

    public Organization getOrganizationById(UUID id) {
        Organization organization = null;
        for (Organization o : organizationList) {
            if (o.getId().equals(id)) {
                organization = o;
                break;
            }
        }
        return organization;
    }


}
