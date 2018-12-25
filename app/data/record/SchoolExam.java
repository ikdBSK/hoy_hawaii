package data.record;

import java.util.ArrayList;

public class SchoolExam {
    private SchoolExamTime time;
    private ArrayList<SchoolTest> tests;

    public SchoolExam(SchoolExamTime time, ArrayList<SchoolTest> tests) {
        this.time = time;
        this.tests = tests;
    }

    //getter and setter
    public SchoolExamTime getTime() {
        return time;
    }

    public void setTime(SchoolExamTime time) {
        this.time = time;
    }

    public ArrayList<SchoolTest> getTests() {
        return tests;
    }

    public void setTests(ArrayList<SchoolTest> tests) {
        this.tests = tests;
    }
}
