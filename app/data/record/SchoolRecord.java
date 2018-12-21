package data.record;

import java.util.ArrayList;

public class SchoolRecord {
    private ArrayList<SchoolExam> exams;
    private ArrayList<SubjectClass> record;

    public SchoolRecord(ArrayList<SchoolExam> exams, ArrayList<SubjectClass> record) {
        this.exams = exams;
        this.record = record;
    }

    //getter and setter
    public ArrayList<SchoolExam> getExams() {
        return exams;
    }

    public void setExams(ArrayList<SchoolExam> exams) {
        this.exams = exams;
    }

    public ArrayList<SubjectClass> getRecord() {
        return record;
    }

    public void setRecord(ArrayList<SubjectClass> record) {
        this.record = record;
    }
}
