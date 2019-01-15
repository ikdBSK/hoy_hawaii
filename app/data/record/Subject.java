package data.record;

import java.util.ArrayList;

public class Subject {
    private String name;
    private int credits;
    private ArrayList<SubjectClass> classes;

    public Subject(String name, int credits){
        this.name = name;
        this.credits = credits;
        classes = new ArrayList<>();
    }

    //getter and setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public ArrayList<SubjectClass> getClasses() {
        return classes;
    }

    public void setClasses(ArrayList<SubjectClass> classes) {
        this.classes = classes;
    }
}
