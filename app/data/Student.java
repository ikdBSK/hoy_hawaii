package data;

import data.record.ClassRoom;
import data.record.SchoolRecord;
import data.record.*;

import java.util.HashMap;

public class Student extends Account {
    private SchoolRecord record;
    private ExternalRecord exRecord;
    private HashMap<Integer, ClassRoom> classRoom = new HashMap<>();

    public Student(String id, String password, String name, SexTag sex, String address) {
        super(id, password, name, sex, address);
        this.record = new SchoolRecord(this);
    }

    public SchoolRecord getRecord() {
        return record;
    }

    public ExternalRecord getExRecord() {
        return exRecord;
    }

    public HashMap<Integer, ClassRoom> getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(HashMap<Integer, ClassRoom> classRoom) {
        this.classRoom = classRoom;
    }
}
