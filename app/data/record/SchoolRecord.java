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
        results = exams.get(time).getExam(student);
        return results;
    }

    /**
     * 指定された科目の試験結果一覧を返す
     * @param subject 返す科目
     * @return 指定された科目の試験結果一覧
     */
    public ArrayList<TestResult> getExam(Subject subject){
        ArrayList<TestResult> results = new ArrayList<TestResult>();
        for (SchoolExamTime time : exams.keySet()){
            TestResult result = exams.get(time).getExam(student, subject);
            if (result != null){
                results.add(result);
            }
        }
        return results;
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
