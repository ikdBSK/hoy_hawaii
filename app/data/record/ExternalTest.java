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
		for(Student student : result.keySet()){
			student.getExRecord().addExam(exam);
		}
		exam.getTests().add(this);
	}

	public ExternalTest(ExternalExam exam, ExternalTime time, Subject subject, ExternalExamType type) {
		this.exam = exam;
		this.time = time;
		this.subject = subject;
		this.type = type;
		result = new HashMap<>();
		exam.getTests().add(this);
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

	//method
	public void addResult(Student student,ExternalTestResult result){
		result.setSubject(subject);
		this.result.put(student,result);
		student.getExRecord().addExam(exam);
	}

	public void addResult(ExternalTestResult result){
		Student student = result.getStudent();
		this.result.put(student,result);
		student.getExRecord().addExam(exam);
	}
}