package data.record;

import data.Student;

import java.util.ArrayList;
import java.util.Collections;

public class Grade {
    ArrayList<ClassRoom> classRooms = new ArrayList<>();
    int grade;//学年
    int year;//年度

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

    /**
     * 得点率基準での順位を返す
     * @param student 順位を返す生徒
     * @param time 返す回
     * @return 順位
     */
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

    /**
     * 得点率基準での偏差値を返す
     * @param student 偏差値を返す生徒
     * @param time 返す回
     * @return 偏差値
     */
    public double getDValue(Student student, SchoolExamTime time){
        ArrayList<Double> score = new ArrayList<>();
        for(ClassRoom classRoom : classRooms){
            for(Student classStudent : classRoom.getStudents()){
                if(classStudent.getRecord().getExams().containsKey(time)){
                    score.add(classStudent.getRecord().getRate(time));
                }
            }
        }
        double sum = 0;
        for(Double s : score){
            sum += s;
        }
        double mean = sum / score.size();
        double ssum = 0.0;
        for(Double s : score){
            ssum += Math.pow(s-mean, 2);
        }
        double variance = ssum / score.size();
        double sd = Math.sqrt(variance);

        return ((student.getRecord().getRate(time) - mean) * 10 / sd) + 50;
    }
}
