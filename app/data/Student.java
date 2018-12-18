package data;

public class Student extends Account {
    public Student(String id, String digested_password, String name, SexTag sex, String address) {
        super(id, digested_password, name, sex, address);
    }
}
