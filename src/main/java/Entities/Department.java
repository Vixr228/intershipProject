package Entities;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class Department extends Staff {
    private String fullName;
    private String shortName;
    private Person director;



    public Department(){}
    public Department(String fullName, String shortName, Person director){
        this.fullName = fullName;
        this.shortName = shortName;
        this.director = director;

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



    @Override
    public String toString() {
        return "Department{" +
                "fullName='" + fullName + '\'' +
                ", shortName='" + shortName + '\'' +
                ", director=" + director +
                '}';
    }
}
