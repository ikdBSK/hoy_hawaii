package data;

import data.record.SchoolRecord;

public class Student extends Account {
    SchoolRecord record;
    public Student(String id, String password, String name, SexTag sex, String address) {
        super(id, password, name, sex, address);
        this.record = new SchoolRecord(this);
    }
}
