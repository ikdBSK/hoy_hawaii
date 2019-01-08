package data.record;

import data.*;
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

    //method

    /**
     * 指定された生徒のテスト結果を返却
     * @param student テストを返却したい対象生徒
     * @return 該当する生徒のテスト結果のリスト
     */
    public ArrayList<TestResult> getExam(Student student){
        ArrayList<TestResult> results = new ArrayList<TestResult>();
        for(SchoolTest test : tests){
            TestResult result = test.getResult().get(student);
            if(result != null){
                results.add(result);
            }
        }
        return results;
    }

    public TestResult getExam(Student student, Subject subject){
        for(SchoolTest test : tests){
            if(test.getSubject().equals(subject)){
                TestResult result = test.getResult().get(student);
                if(result != null){
                    return result;
                }
            }
        }
        return null;
    }
}
