package data.record;

import data.*;

import java.util.ArrayList;

public class ClassRoom {
    Teacher teacher;
    ArrayList<Student> students;
    Grade grade;
    int class_num;
    int year;

    public ClassRoom(Grade grade, int year) {
        this.grade = grade;
        this.year = year;
        grade.getClassRooms().add(this);
    }

    public ClassRoom(ArrayList<Student> students, Grade grade, int year) {
        this.students = students;
        this.grade = grade;
        this.year = year;
        grade.getClassRooms().add(this);
        for(Student s : students){
            s.getClassRoom().put(year, this);
        }
    }

    /**
     * メインで使うコンストラクタ
     * @param teacher
     * @param students
     * @param grade
     * @param year
     */
    public ClassRoom(Teacher teacher, ArrayList<Student> students, Grade grade, int class_num, int year) {
        this.teacher = teacher;
        this.students = students;
        this.grade = grade;
        this.class_num = class_num;
        this.year = year;
        teacher.getClassRoom().put(year, this);
        for(Student s : students){
            s.getClassRoom().put(year, this);
        }
        grade.getClassRooms().add(this);
    }

    //getter and setter

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    //method
    /**
     * 得点率基準での順位を返す
     * @param student 順位を返す生徒
     * @param time 返す回
     * @return 順位
     */
    public int getRank(Student student, SchoolExamTime time){
        return grade.getRank(student, time);
    }

    /**
     * 得点率基準での偏差値を返す
     * @param student 偏差値を返す生徒
     * @param time 返す回
     * @return 偏差値
     */
    public double getDValue(Student student, SchoolExamTime time){
        return grade.getDValue(student, time);
    }

    /**
     * 生徒追加
     * @param student 追加する生徒
     * @return 追加できたかどうか
     */
    public boolean addStudent(Student student){
        if(student.getClassRoom().containsKey(year)){
            students.add(student);
            return true;
        }
        return false;
    }
}
