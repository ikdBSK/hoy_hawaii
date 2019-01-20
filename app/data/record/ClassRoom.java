package data.record;

import data.Student;

import java.util.ArrayList;

public class ClassRoom {
    ArrayList<Student> students;
    Grade grade;
    int year;

    public ClassRoom(Grade grade, int year) {
        this.grade = grade;
        this.year = year;
    }

    public ClassRoom(ArrayList<Student> students, Grade grade, int year) {
        this.students = students;
        this.grade = grade;
        this.year = year;
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

    //method
    public int getRank(Student student, SchoolExamTime time){
        return grade.getRank(student, time);
    }
}
