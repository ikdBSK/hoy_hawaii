package data.record;

import data.Student;

import java.util.HashMap;

public class SchoolTest {
    private SchoolTime time;
    private SubjectClass subject;
    private HashMap<Student, TestResult> result;

    public SchoolTest(SchoolTime time, SubjectClass subject, HashMap<Student, TestResult> result) {
        this.time = time;
        this.subject = subject;
        this.result = result;
    }

    //getter and setter
    public SchoolTime getTime() {
        return time;
    }

    public void setTime(SchoolTime time) {
        this.time = time;
    }

    public SubjectClass getSubject() {
        return subject;
    }

    public void setSubject(SubjectClass subject) {
        this.subject = subject;
    }

    public HashMap<Student, TestResult> getResult() {
        return result;
    }

    public void setResult(HashMap<Student, TestResult> result) {
        this.result = result;
    }
}
