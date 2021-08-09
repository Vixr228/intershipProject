package entities.documents;

import entities.orgstuff.Person;
import utils.XMLConverter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Task extends Document {
    private Date taskDate;
    private Date taskDeadline;
    private Person taskExecutor;
    private String controlSign;
    private Person taskController;

    public Task(){}
    public Task(UUID id, String name, String text, Date registrationDate, Person author, Date taskDate, Date taskDeadline,
                Person taskExecutor, String controlSign, Person taskController){
        super(id, name, text, registrationDate, author);
        this.taskDate = taskDate;
        this.taskDeadline = taskDeadline;
        this.taskExecutor = taskExecutor;
        this.controlSign = controlSign;
        this.taskController = taskController;
    }

    public Date getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(Date taskDate) {
        this.taskDate = taskDate;
    }

    public Date getTaskDeadline() {
        return taskDeadline;
    }

    public void setTaskDeadline(Date taskDeadline) {
        this.taskDeadline = taskDeadline;
    }

    public Person getTaskExecutor() {
        return taskExecutor;
    }

    public void setTaskExecutor(Person taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public String getControlSign() {
        return controlSign;
    }

    public void setControlSign(String controlSign) {
        this.controlSign = controlSign;
    }

    public Person getTaskController() {
        return taskController;
    }

    public void setTaskController(Person taskController) {
        this.taskController = taskController;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("Task{" + super.toString());
        sb.append("taskDate=" + taskDate);
        sb.append(", taskDeadline=" + taskDeadline);
        sb.append(", taskExecutor=" + taskExecutor);
        sb.append(", controlSign=" + controlSign);
        sb.append(", taskController=" + taskController);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public StringBuffer convertDocumentToXml() {
        StringBuffer str = new StringBuffer();
        str.append(super.convertDocumentToXml());
        str.append("\t\t<taskDate>" + taskDate + "</taskDate>\n");
        str.append("\t\t<taskDeadline>" + taskDeadline + "</taskDeadline>\n");
        str.append("\t\t<taskExecutor>\n");
        str.append(taskExecutor.toXML());
        str.append("\t\t</taskExecutor>\n");
        str.append("\t\t<taskController>\n");
        str.append(taskController.toXML());
        str.append("\t\t</taskController>\n");
        str.append("\t\t<controlSign>" + controlSign + "</controlSign>\n");
        return str;
    }

    @Override
    public String printDocument() {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        return "Поручение №" + getRegistrationNumber() + " от " + format.format(getRegistrationDate()) + ". Дедлайн: " + format.format(getTaskDeadline()) + ". " + getName();
    }
}
