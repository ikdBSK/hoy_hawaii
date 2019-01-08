package data.record;

import data.*;
import play.api.Mode;

import java.util.ArrayList;
import java.util.HashMap;

public class SchoolRecord {
    private Student student;
    private HashMap<SchoolExamTime, SchoolExam> exams;
    private ArrayList<SubjectClass> record;

    public SchoolRecord(Student student, HashMap<SchoolExamTime, SchoolExam> exams, ArrayList<SubjectClass> record) {
        this.student = student;
        this.exams = exams;
        this.record = record;
    }

    //getter and setter
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public HashMap<SchoolExamTime, SchoolExam> getExams() {
        return exams;
    }

    public void setExams(HashMap<SchoolExamTime, SchoolExam> exams) {
        this.exams = exams;
    }

    public ArrayList<SubjectClass> getRecord() {
        return record;
    }

    public void setRecord(ArrayList<SubjectClass> record) {
        this.record = record;
    }

    //method

    /**
     * 指定された回の試験結果一覧を返す
     * @param time 返す回
     * @return 指定された回の試験結果一覧
     */
    public ArrayList<TestResult> getExam(SchoolExamTime time){
        ArrayList<TestResult> results;
        results = exams.get(time).getExam(student);
        return results;
    }
}
