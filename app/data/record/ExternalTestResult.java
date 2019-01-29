
package data.record;

import data.*;

public class ExternalTestResult {
	private int score;
	private Student student;
	private Subject subject;
	private double dValue;
	private int rank;

	public ExternalTestResult(int score, Student student, Subject subject, double dValue, int rank){
		this.score = score;
		this.student = student;
		this.subject = subject;
		this.dValue = dValue;
		this.rank = rank;
	}

	public int getScore(){ return score; }

	public void setScore(int score){ this.score = score; }

	public Student getStudent(){ return student; }

	public void setStudent(Student student){ this.student = student; }

	public Subject getSubject(){ return subject; }

	public void setSubject(Subject subject) { this.subject = subject; }

	public double getDValue() { return dValue; }

	public void setDValue(double dValue) { this.dValue = dValue; }

	public int getRank() { return rank; }

	public void setRank(int rank) { this.rank = rank; }
}
