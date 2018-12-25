package data.record;

public class SubjectGrade {
    private int grade;
    private boolean passed;

    public SubjectGrade(int grade, boolean passed) {
        this.grade = grade;
        this.passed = passed;
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
}
