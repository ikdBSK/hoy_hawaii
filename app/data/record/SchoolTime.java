package data.record;

public class SchoolTime {
    private int year;
    private int month;
    private int day;
    private int division;

    public SchoolTime(int year, int month, int day, int division) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.division = division;
    }

    //getter and setter
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getDivision() {
        return division;
    }

    public void setDivision(int division) {
        this.division = division;
    }
}
