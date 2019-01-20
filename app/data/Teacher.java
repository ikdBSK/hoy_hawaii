package data;

import data.record.SubjectClass;

import java.util.ArrayList;

public class Teacher extends Account {
    private ArrayList<SubjectClass> classes;

    public Teacher(String id, String password, String name, SexTag sex, String address) {
        super(id, password, name, sex, address);
        classes = new ArrayList<>();
    }

    //getter and setter
    public ArrayList<SubjectClass> getClasses() {
        return classes;
    }

    public void setClasses(ArrayList<SubjectClass> classes) {
        this.classes = classes;
    }
}
