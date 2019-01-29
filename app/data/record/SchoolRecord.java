package data.record;

import data.*;

import java.util.ArrayList;
import java.util.HashMap;

public class SchoolRecord {
    private Student student;
    private HashMap<SchoolExamTime, SchoolExam> exams;
    private ArrayList<SubjectClass> classes;

    public SchoolRecord(Student student) {
        this.student = student;
        exams = new HashMap<>();
        classes = new ArrayList<>();
    }

    public SchoolRecord(Student student, HashMap<SchoolExamTime, SchoolExam> exams, ArrayList<SubjectClass> classes) {
        this.student = student;
        this.exams = exams;
        this.classes = classes;
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

    public ArrayList<SubjectClass> getClasses() {
        return classes;
    }

    public void setClasses(ArrayList<SubjectClass> classes) {
        this.classes = classes;
    }

    //method

    /**
     * 指定された回の試験結果一覧を返す
     * @param time 返す回
     * @return 指定された回の試験結果一覧
     */
    public ArrayList<TestResult> getExam(SchoolExamTime time){
        ArrayList<TestResult> results;
        try{
            results = exams.get(time).getExam(student);
        }catch (Exception e){
            return new ArrayList<TestResult>();
        }
        return results;
    }

    /**
     * 指定された科目の試験結果一覧を返す
     * @param subject 返す科目
     * @return 指定された科目の試験結果一覧
     */
    public ArrayList<TestResult> getExam(Subject subject){
        ArrayList<TestResult> results = new ArrayList<>();
        for (SchoolExamTime time : exams.keySet()){
            TestResult result = exams.get(time).getExam(student, subject);
            if (result != null){
                results.add(result);
            }
        }
        return results;
    }

    /**
     * 指定された回の合計点を返す
     * @param time 返す回
     * @return 合計点
     */
    public int getTotalScore(SchoolExamTime time){
        ArrayList<TestResult> results = getExam(time);
        int score = 0;
        for(TestResult result : results){
            score += result.getScore();
        }
        return score;
    }

    /**
     * 指定された回の得点率(%)を返す
     * @param time 返す回
     * @return 得点率
     */
    public double getRate(SchoolExamTime time){
        ArrayList<TestResult> results = getExam(time);
        if(results == null){
            return 0;
        }
        double score = 0;
        for(TestResult result : results){
            score += (double)result.getScore();
        }
        score = score / (double)results.size();
        return score;
    }

    /**
     * 指定された回、科目の偏差値を返す
     */
    public double getDValue(SchoolExamTime time, Subject subject){
        double DValue = 0;
        try{
            DValue = exams.get(time).getDValue(student, subject);
        }catch (Exception e){
            return 0;
        }
        return DValue;
    }

    /**
     * 指定された回、科目の順位
     */
    public int getRank(SchoolExamTime time, Subject subject){
        int Rank = 0;
        try{
            Rank = exams.get(time).getRank(student, subject);
        }catch (Exception e){
            return 0;
        }
        return Rank;
    }

    /**
     * 受けたテストを登録する
     * @param exam 受けたテスト
     */
    public void addExam(SchoolExam exam){
        if(!exams.containsValue(exam)){
            exams.put(exam.getTime(), exam);
        }
    }
}
