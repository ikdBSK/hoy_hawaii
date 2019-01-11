package data.record;

import data.*;
import java.util.ArrayList;

public class SchoolExam {
    private SchoolExamTime time;
    private ArrayList<SchoolTest> tests;
    private boolean release = false;

    public SchoolExam(SchoolExamTime time) {
        this.time = time;
        this.tests = new ArrayList<>();
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
     * @return 該当する生徒のテスト結果のリスト、未公開の場合は空リスト
     */
    public ArrayList<TestResult> getExam(Student student){
        ArrayList<TestResult> results = new ArrayList<TestResult>();
        if(release==false){
            return results;
        }
        for(SchoolTest test : tests){
            TestResult result = test.getResult().get(student);
            if(result != null){
                results.add(result);
            }
        }
        return results;
    }

    /**
     * 指定された生徒の指定された科目のテスト結果を返却
     * @param student テストを返却したい対象生徒
     * @return 該当する生徒のテスト結果、存在しないまたは未公開の場合はnull
     */
    public TestResult getExam(Student student, Subject subject){
        if(release==false){
            return null;
        }
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

    /**
     * 成績公開を行う
     */
    public void release(){
        release = true;
    }
}
