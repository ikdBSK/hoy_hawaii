
package data.record;

import data.*;

public class ExternalTestResult {
	private int score;
	private Student student;
	private Subject subject;

	public ExternalTestResult(int score, Student student, Subject subject){
		this.score = score;
		this.student = student;
		this.subject = subject;
	}

	public int getScore(){ return score; }

	public void setScore(int score){ this.score = score; }

	public Student getStudent(){ return student; }

	public void setStudent(Student student){ this.student = student; }

	public Subject getSubject(){ return subject; }

	public void setSubject(Subject subject) { this.subject = subject; }
}