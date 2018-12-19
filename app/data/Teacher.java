package data;

public class Teacher extends Account {
    public Teacher(String id, String digested_password, String name, SexTag sex, String address) {
        super(id, digested_password, name, sex, address);
    }
}
