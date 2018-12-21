package data.record;

public class TestResult {
    private int score;
    private boolean attendance;
    private boolean late;

    TestResult(int score, boolean attendance, boolean late){
        this.score = score;
        this.attendance = attendance;
        this.late = late;
    }

    //getter and setter
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

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

}
