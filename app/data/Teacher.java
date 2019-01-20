package data;

import data.record.ClassRoom;
import data.record.SubjectClass;

import java.util.HashMap;
import java.util.ArrayList;

public class Teacher extends Account {
    private ArrayList<SubjectClass> classes;
    private HashMap<Integer, ClassRoom> classRoom = new HashMap<>();

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

    public HashMap<Integer, ClassRoom> getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(HashMap<Integer, ClassRoom> classRoom) {
        this.classRoom = classRoom;
    }
}
