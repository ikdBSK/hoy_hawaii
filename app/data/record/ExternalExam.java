package data.record;

import data.Student;

import javax.rmi.CORBA.Stub;
import java.util.HashMap;

public class ExternalExam {
    private ExternalTime time;
    private ExternalExamType type;
    private Subject subject;
    private HashMap<Student,TestResult> tests;

    public ExternalExam(ExternalTime time, ExternalExamType type, Subject subject, HashMap<Student, TestResult> tests) {
        this.time = time;
        this.type = type;
        this.subject = subject;
        this.tests = tests;
    }

    public ExternalTime getTime() {
        return time;
    }

    public void setTime(ExternalTime time) {
        this.time = time;
    }

    public ExternalExamType getType() {
        return type;
    }

    public void setType(ExternalExamType type) {
        this.type = type;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public HashMap<Student, TestResult> getTests() {
        return tests;
    }

    public void setTests(HashMap<Student, TestResult> tests) {
        this.tests = tests;
    }
}
