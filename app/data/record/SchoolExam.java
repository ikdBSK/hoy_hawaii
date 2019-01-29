package data.record;

import data.*;
import java.util.ArrayList;
import java.util.Collections;

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
     * 指定された生徒の指定された科目の偏差値
     * @param student テストを返却したい対象生徒
     * @return 該当する生徒の偏差値、存在しないまたは未公開の場合は0
     */
    public double getDValue(Student student, Subject subject) {
        if(release==false){
            return 0;
        }
        for(SchoolTest test : tests){
            if(test.getSubject().equals(subject)){
                TestResult result = test.getResult().get(student);
                ArrayList<TestResult> results = new ArrayList<>(test.getResult().values());
                if(result != null){

                    double sum = 0;
                    for(TestResult t : results){
                        sum += t.getScore();
                    }
                    double mean = sum / results.size();
                    double ssum = 0.0;
                    for(TestResult t : results){
                        ssum += Math.pow(t.getScore()-mean, 2);
                    }
                    double variance = ssum / results.size();
                    double sd = Math.sqrt(variance);

                    return ((result.getScore() - mean) * 10 / sd) + 50;
                }
            }
        }
        return 0;
    }

    /**
     * 指定された生徒の指定された科目の順位
     * @param student テストを返却したい対象生徒
     * @return 該当する生徒の順位、存在しないまたは未公開の場合は0
     */
    public int getRank(Student student, Subject subject) {
        if(release==false){
            return 0;
        }
        for(SchoolTest test : tests){
            if(test.getSubject().equals(subject)){
                TestResult result = test.getResult().get(student);
                ArrayList<TestResult> results = new ArrayList<>(test.getResult().values());

                ArrayList<Integer> scores = new ArrayList<>();
                for(TestResult t : results){
                    scores.add(t.getScore());
                }
                Collections.sort(scores, Collections.reverseOrder());
                int i;
                for(i = 0; i < scores.size(); i++){
                    if(scores.get(i) <= result.getScore()){
                        return i+1;
                    }
                }
                return scores.size();
            }
        }
        return 0;
    }

    /**
     * 成績公開を行う
     */
    public void release(){
        release = true;
    }
}
