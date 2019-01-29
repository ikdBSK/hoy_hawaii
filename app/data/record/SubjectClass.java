package data.record;

import data.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SubjectClass {
    private Subject subject;
    private Teacher teacher;
    private ArrayList<Student> students;
    private SchoolSemester semester;
    private int grade;//学年
    private HashMap<Student, SubjectGrade> grades;
    private ArrayList<SchoolTest>tests = new ArrayList<>();
    static int nextId = 0;
    int id;

    public SubjectClass(Subject subject, Teacher teacher, ArrayList<Student> students, SchoolSemester semester, int grade, HashMap<Student, SubjectGrade> grades) {
        this.subject = subject;
        this.teacher = teacher;
        this.students = students;
        this.semester = semester;
        this.grade = grade;
        this.grades = grades;
        subject.getClasses().add(this);
        id = nextId;
        nextId++;
        addStudentSubject(students);
        teacher.getClasses().add(this);
    }

    public SubjectClass(Subject subject, Teacher teacher, ArrayList<Student> students, SchoolSemester semester, int grade) {
        this.subject = subject;
        this.teacher = teacher;
        this.students = students;
        this.semester = semester;
        this.grade = grade;
        subject.getClasses().add(this);
        id = nextId;
        nextId++;
        addStudentSubject(students);
        teacher.getClasses().add(this);
    }

    public SubjectClass(Subject subject, Teacher teacher, SchoolSemester semester, int grade) {
        this.subject = subject;
        this.teacher = teacher;
        this.semester = semester;
        this.grade = grade;
        subject.getClasses().add(this);
        id = nextId;
        nextId++;
        addStudentSubject(students);
        teacher.getClasses().add(this);
    }

    //getter and setter
    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public SchoolSemester getSemester() {
        return semester;
    }

    public void setSemester(SchoolSemester semester) {
        this.semester = semester;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public HashMap<Student, SubjectGrade> getGrades() {
        return grades;
    }

    public void setGrades(HashMap<Student, SubjectGrade> grades) {
        this.grades = grades;
    }

    public ArrayList<SchoolTest> getTests() {
        return tests;
    }

    public void setTests(ArrayList<SchoolTest> tests) {
        this.tests = tests;
    }

    public static int getNextId() {
        return nextId;
    }

    public static void setNextId(int nextId) {
        SubjectClass.nextId = nextId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //method

    public boolean equals(Subject subject){
        return this.subject.equals(subject);
    }

    /**
     * 追加生徒に対して履修登録
     * @param students
     */
    public void addStudentSubject(ArrayList<Student> students){
        for (Student s : students){
            s.getRecord().getClasses().add(this);
        }
    }

    /**
     * 生徒追加
     * @param s
     */
    public void addStudent(Student s){
        students.add(s);
        s.getRecord().getClasses().add(this);
    }
}
