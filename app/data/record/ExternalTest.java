package data.record;

import data.Student;

import java.util.HashMap;

public class ExternalTest {
	private ExternalExam exam;
	private ExternalTime time;
	private Subject subject;
	private ExternalExamType type;
	private HashMap<Student,ExternalTestResult> result;

	public ExternalTest(ExternalExam exam, ExternalTime time, Subject subject, ExternalExamType type, HashMap<Student, ExternalTestResult> result) {
		this.exam = exam;
		this.time = time;
		this.subject = subject;
		this.type = type;
		this.result = result;
	}

	public ExternalTest(ExternalExam exam, ExternalTime time, Subject subject, ExternalExamType type) {
		this.exam = exam;
		this.time = time;
		this.subject = subject;
		this.type = type;
		result = new HashMap<>();
	}

	//getter and setter
	public ExternalExam getExam() {
		return exam;
	}

	public void setExam(ExternalExam exam) {
		this.exam = exam;
	}

	public ExternalTime getTime() {
		return time;
	}

	public void setTime(ExternalTime time) {
		this.time = time;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public HashMap<Student, ExternalTestResult> getResult() {
		return result;
	}

	public void setResult(HashMap<Student, ExternalTestResult> result) {
		this.result = result;
	}

	public ExternalExamType getType() {
		return type;
	}

	public void setType(ExternalExamType type) {
		this.type = type;
	}

	//method(途中)
	public void addResult(Student student,ExternalTestResult result){
		result.setSubject(subject);
		this.result.put(student,result);

	}
}