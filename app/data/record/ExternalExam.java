package data.record;

import data.*;
import java.util.ArrayList;

public class ExternalExam {
	private ExternalTime time;
	private ExternalExamType type;
	private ArrayList<ExternalTest> tests;
	private boolean release = false;

	public ExternalExam(ExternalTime time, ExternalExamType type) {
		this.time = time;
		this.type = type;
		this.tests = new ArrayList<>();
	}

	//getter and setter

	public ExternalTime getTime() {
		return time;
	}

	public void setTime(ExternalTime time) {
		this.time = time;
	}

	public ExternalExamType getType() {
		return type;
	}

	public void setType(ExternalExamType type) {
		this.type = type;
	}

	public ArrayList<ExternalTest> getTests() {
		return tests;
	}

	public void setTests(ArrayList<ExternalTest> tests) {
		this.tests = tests;
	}

	//method

	/**
	 * 指定されたテストの結果を返却
	 * @param student テストを返却したい対象の生徒
	 * @return 該当する生徒のテストの結果のリスト、未公開の場合は空リスト
	 */
	public ArrayList<ExternalTestResult> getExam(Student student){
		ArrayList<ExternalTestResult> results = new ArrayList<ExternalTestResult>();
		if(release==false){
			return results;
		}
		for(ExternalTest test : tests){
			ExternalTestResult result = test.getResult().get(student);
			if(result != null){
				results.add(result);
			}
		}
		return results;
	}

	/**
	 * 指定された生徒の指定された科目のテスト結果を返却
	 * @param student テストを返却したい対象の生徒
	 * @return 該当する生徒のテスト結果、存在しないまたは未公開の場合はnull
	 */
	public ExternalTestResult getExam(Student student,Subject subject){
		if(release==false){
			return null;
		}
		for(ExternalTest test : tests){
			if(test.getSubject().equals(subject)){
				ExternalTestResult result = test.getResult().get(student);
				if(result != null){
					return result;
				}
			}
		}
		return null;
	}

	/**
	 * 成績公表を行う
	 */
	public void release(){ release = true; }
}