package data;

import data.record.SchoolRecord;

public class Student extends Account {
    private SchoolRecord record;
    private ExternalRecord exRecord;

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
}
