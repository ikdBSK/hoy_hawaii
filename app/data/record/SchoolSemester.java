package data.record;

public class SchoolSemester {
    private int year;
    private int semester;

    public SchoolSemester(int year, int semester) {
        this.year = year;
        this.semester = semester;
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
}
