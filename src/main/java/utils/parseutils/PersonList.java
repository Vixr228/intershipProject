package utils.parseutils;

import utils.parseutils.adapters.PersonAdapter;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Класс PersonList используется для парсинга массива данных Person из XML источника.
 */

@XmlRootElement(name = "personList")
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonList {


    @XmlElement(name = "person", type = PersonAdapter.AdaptedPerson.class)
    private List<PersonAdapter.AdaptedPerson> personList = null;

    public List<PersonAdapter.AdaptedPerson> getPersonList() {
        return personList;
    }

    public void setPersonList(List<PersonAdapter.AdaptedPerson> personList) {
        this.personList = personList;
    }


}