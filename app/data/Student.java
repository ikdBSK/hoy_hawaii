package data;

import data.record.SchoolRecord;

import java.util.HashMap;

public class Student extends Account {
    SchoolRecord record;
    public Student(String id, String digested_password, String name, SexTag sex, String address) {
        super(id, digested_password, name, sex, address);
        this.record = new SchoolRecord(this);
    }
}
