package data.record;

import data.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ExternalRecord {
	private Student student;
	private HashMap<ExternalTime,ExternalExam> exams;

	public ExternalRecord(Student student) {
		this.student = student;
		this.exams = new HashMap<>();
	}

	public ExternalRecord(Student student, HashMap<ExternalTime, ExternalExam> exams) {
		this.student = student;
		this.exams = exams;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public HashMap<ExternalTime, ExternalExam> getExams() {
		return exams;
	}

	public void setExams(HashMap<ExternalTime, ExternalExam> exams) {
		this.exams = exams;
	}

	//method

	public ArrayList<ExternalTestResult> getExam(ExternalTime time){
		ArrayList<>
	}

}