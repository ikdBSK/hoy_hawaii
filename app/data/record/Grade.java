package data.record;

import data.Student;

import java.util.ArrayList;
import java.util.Collections;

public class Grade {
    ArrayList<ClassRoom> classRooms = new ArrayList<>();
    int grade;
    int year;

    public Grade(int grade, int year) {
        this.grade = grade;
        this.year = year;
    }

    //getter and setter

    public ArrayList<ClassRoom> getClassRooms() {
        return classRooms;
    }

    public void setClassRooms(ArrayList<ClassRoom> classRooms) {
        this.classRooms = classRooms;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
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
        ArrayList<Double> score = new ArrayList<>();
        for(ClassRoom classRoom : classRooms){
            for(Student classStudent : classRoom.getStudents()){
                score.add(classStudent.getRecord().getRate(time));
            }
        }
        Collections.sort(score, Collections.reverseOrder());
        int i;
        for(i = 0; i < score.size(); i++){
            if(score.get(i) <= student.getRecord().getRate(time)){
                return i+1;
            }
        }
        return score.size();
    }
}
