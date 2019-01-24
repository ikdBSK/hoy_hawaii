package data.record;

public class ExternalTime {
	private int year;
	private int month;
	private int day;
	private ExternalExamType type;

	public ExternalTime(int year, int month, int day, ExternalExamType type) {
		this.year = year;
		this.month = month;
		this.day = day;
		this.type = type;
	}

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

	public void setDay(int day) { this.day = day; }

	public ExternalExamType getType() { return type; }

	public void setType(ExternalExamType type) { this.type = type; }
}