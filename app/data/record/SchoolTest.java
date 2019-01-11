package data.record;

import data.Student;

import java.util.HashMap;

public class SchoolTest {
    private SchoolExam exam;
    private SchoolTime time;
    private SubjectClass subject;
    private HashMap<Student, TestResult> result;

    public SchoolTest(SchoolExam exam, SchoolTime time, SubjectClass subject, HashMap<Student, TestResult> result) {
        this.exam = exam;
        this.time = time;
        this.subject = subject;
        this.result = result;
        for(Student student : result.keySet()){
            student.getRecord().addExam(exam);
        }
    }

    public SchoolTest(SchoolExam exam, SchoolTime time, SubjectClass subject){
        this.exam = exam;
        this.time = time;
        this.subject = subject;
        result = new HashMap<>();
    }

    //getter and setter
    public SchoolExam getExam() {
        return exam;
    }

    public void setExam(SchoolExam exam) {
        this.exam = exam;
    }

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

    //method
    public void addResult(Student student, TestResult result){
        this.result.put(student, result);
        student.getRecord().addExam(exam);
    }
}
