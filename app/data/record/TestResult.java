package data.record;

import data.*;

public class TestResult {
    private int score;
    /*
    private boolean attendance;
    private boolean late;
    */
    private Student student;
    private SubjectClass subject;
    private SchoolExamTime time;

    /*
    public TestResult(int score, boolean attendance, boolean late, Student student, SubjectClass subject) {
        this.score = score;
        this.attendance = attendance;
        this.late = late;
        this.student = student;
        this.subject = subject;
    }*/

    public TestResult(int score, Student student, SubjectClass subject, SchoolExamTime time) {
        this.score = score;
        this.student = student;
        this.subject = subject;
        this.time = time;
    }

    //getter and setter
    public int getScore() {
        return score;
    }

    public SchoolExamTime getTime() {
        return time;
    }

    public void setScore(int score) {
        this.score = score;
    }

    /*
    public boolean isAttendance() {
        return attendance;
    }

    public void setAttendance(boolean attendance) {
        this.attendance = attendance;
    }

    public boolean isLate() {
        return late;
    }

    public void setLate(boolean late) {
        this.late = late;
    }
    */

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public SubjectClass getSubject() {
        return subject;
    }

    public void setSubject(SubjectClass subject) {
        this.subject = subject;
    }
}
