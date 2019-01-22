package data.record;

import data.*;

import java.util.ArrayList;

public class ClassRoom {
    Teacher teacher;
    ArrayList<Student> students;
    Grade grade;
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
    }

    /**
     * メインで使うコンストラクタ
     * @param teacher
     * @param students
     * @param grade
     * @param year
     */
    public ClassRoom(Teacher teacher, ArrayList<Student> students, Grade grade, int year) {
        this.teacher = teacher;
        this.students = students;
        this.grade = grade;
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
    public int getRank(Student student, SchoolExamTime time){
        return grade.getRank(student, time);
    }

    public boolean addStudent(Student student){
        if(student.getClassRoom().containsKey(year)){
            students.add(student);
            return true;
        }
        return false;
    }
}
