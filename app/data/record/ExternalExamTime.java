package data.record;


// SchoolExamTimeをとりあえずコピーしただけ
public class ExternalExamTime {
    private int year;
    private int semester;
    private int term;

    public ExternalExamTime(int year, int semester, int term) {
        this.year = year;
        this.semester = semester;
        this.term = term;
    }

    //getter and setter

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }
}
