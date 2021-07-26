package entities.orgstuff;

import utils.parseutils.PhoneNumbersList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.UUID;

@XmlRootElement(name = "organization")
public class Organization extends Staff{
    private UUID id;
    private String fullName;
    private String shortName;
    private Person director;
    private PhoneNumbersList contactList;

    public Organization(){}



    public Organization(String fullName, String shortName, Person director, PhoneNumbersList contactList){
        this.id = UUID.randomUUID();
        this.fullName = fullName;
        this.shortName = shortName;
        this.director = director;
        this.contactList = contactList;

    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
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

    public StringBuffer print() {
        StringBuffer str = new StringBuffer();
        str.append("Organization{" +
                "fullName='" + fullName + '\'' +
                ", shortName='" + shortName + '\'' +
                ", director=" + director +
                ", contactNumbers=" + contactList.getNumberList() +
                '}');
        return str;
    }
}
