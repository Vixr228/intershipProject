package repositories;

import entities.orgstuff.Organization;
import utils.XMLParser;
import web_controllers.Application;

import java.sql.SQLException;
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
    public List<Organization> getOrganizationListFromXML() {
        return organizationList;
    }

    public Organization getOrganizationById(UUID id) throws SQLException {
        for (Organization o : Application.organizationService.getAll()) {
            if (o.getId().equals(id)) {
                return o;
            }
        }
        return null;
    }


}
