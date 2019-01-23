package data.record;

import data.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ExternalRecord {
	private Student student;
	private HashMap<ExternalExamTime,ExternalExam> exams;

	public ExternalRecord(Student student) {
		this.student = student;
		exams = new HashMap<>();
	}

	public ExternalRecord(Student student, HashMap<ExternalExamTime, ExternalExam> exams) {
		this.student = student;
		this.exams = exams;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public HashMap<ExternalExamTime, ExternalExam> getExams() {
		return exams;
	}

	public void setExams(HashMap<ExternalExamTime, ExternalExam> exams) {
		this.exams = exams;
	}


}