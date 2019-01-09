package data.record;

public class SubjectGrade {
    private int grade;
    private boolean passed;
    private Subject subject;

    public SubjectGrade(int grade, boolean passed, Subject subject) {
        this.grade = grade;
        this.passed = passed;
        this.subject = subject;
    }

    //getter and setter

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
