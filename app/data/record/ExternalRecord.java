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

	/**
	 * 指定された回の試験結果一覧を返す
	 * @param time 返す回
	 * @return 指定された回の試験結果一覧
	 */
	public ArrayList<ExternalTestResult> getExam(ExternalTime time){
		ArrayList<ExternalTestResult> results;
		results = exams.get(time).getExam(student);
		return results;
	}

	/**
	 * 指定された科目の試験結果一覧を返す
	 * @param subject 返す科目
	 * @return 指定された科目の試験結果一覧
	 */
	public ArrayList<ExternalTestResult> getExam(Subject subject){
		ArrayList<ExternalTestResult> results = new ArrayList<>();
		for (ExternalTime time : exams.keySet()){
			ExternalTestResult result = exams.get(time).getExam(student,subject);
			if(result != null){
				results.add(result);
			}
		}
		return results;
	}

	/**
	 * 指定された回の合計点を返す
	 * @param time 返す回
	 * @return 合計点
	 */
	public int getTotalScore(ExternalTime time){
		ArrayList<ExternalTestResult> results = getExam(time);
		int score = 0;
		for(ExternalTestResult result : results){
			score += result.getScore();
		}
		return score;
	}

	/**
	 * 指定された回の得点率(%)を返す
	 * @param time 返す回
	 * @return 得点率
	 */
	public double getRate(ExternalTime time){
		ArrayList<ExternalTestResult> results = getExam(time);
		double score = 0;
		for(ExternalTestResult result : results){
			score += (double)result.getScore();
		}
		score = score / (double)results.size();
		return score;
	}

	/**
	 * 受けたテストを登録する
	 * @param exam 受けたテスト
	 */
	public void addExam(ExternalExam exam){
		if(!exams.containsValue(exam)){
			exams.put(exam.getTime(), exam);
		}
	}
}