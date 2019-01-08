package data.record;

import data.*;
import play.api.Mode;

import java.util.ArrayList;
import java.util.HashMap;

public class ExternalRecord {
    private ArrayList<ExternalExam> exams;

    public ExternalRecord(ArrayList<ExternalExam> exams) {
        this.exams = exams;
    }

    public ArrayList<ExternalExam> getExams() {
        return exams;
    }

    public void setExams(ArrayList<ExternalExam> exams) {
        this.exams = exams;
    }
}
