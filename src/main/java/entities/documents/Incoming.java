package entities.documents;

import entities.orgstuff.Person;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Incoming extends Document {

    private Person sender;
    private Person recipient;
    private String outgoingNumber;
    private Date outgoingRegistrationDate;

    public Incoming(UUID id, String name, String text, Date registrationDate, Person author, Person sender, Person recipient,
                    String outgoingNumber, Date outgoingRegistrationDate){
        super(id, name, text, registrationDate, author);
        this.sender = sender;
        this.recipient = recipient;
        this.outgoingNumber = outgoingNumber;
        this.outgoingRegistrationDate = outgoingRegistrationDate;
    }

    public Person getSender() {
        return sender;
    }

    public void setSender(Person sender) {
        this.sender = sender;
    }

    public Person getRecipient() {
        return recipient;
    }

    public void setRecipient(Person recipient) {
        this.recipient = recipient;
    }

    public String getOutgoingNumber() {
        return outgoingNumber;
    }

    public void setOutgoingNumber(String outgoingNumber) {
        this.outgoingNumber = outgoingNumber;
    }

    public Date getOutgoingRegistrationDate() {
        return outgoingRegistrationDate;
    }

    public void setOutgoingRegistrationDate(Date outgoingRegistrationDate) {
        this.outgoingRegistrationDate = outgoingRegistrationDate;
    }


    public StringBuffer print() {
        StringBuffer str = new StringBuffer();
        str.append("Incoming{" +
                super.toString() +
                "sender=" + sender +
                ", recipient=" + recipient +
                ", outgoingNumber='" + outgoingNumber + '\'' +
                ", outgoingRegistrationDate=" + outgoingRegistrationDate +
                '}');
        return str;
    }

    @Override
    public String printDocument() {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        return "Входящий №" + getRegistrationNumber() + " от " + format.format(getRegistrationDate()) + ". " + getName();
    }
}