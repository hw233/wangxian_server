package com.fy.engineserver.activity.quiz;

public class Subject {

	private int subjectId;
	private String trunk;
	private String branchA;
	private String branchB;
	private String branchC;
	private String branchD;
	
	private int rightAnswer;
	
	
	
	public boolean isRight(int result){
		if(result == rightAnswer)
		return true;
		return false;
	}

	
	/**********************************************************/

	public String getTrunk() {
		return trunk;
	}

	public int getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}


	public void setTrunk(String trunk) {
		this.trunk = trunk;
	}

	public String getBranchA() {
		return branchA;
	}

	public void setBranchA(String branchA) {
		this.branchA = branchA;
	}

	public String getBranchB() {
		return branchB;
	}

	public void setBranchB(String branchB) {
		this.branchB = branchB;
	}

	public String getBranchC() {
		return branchC;
	}

	public void setBranchC(String branchC) {
		this.branchC = branchC;
	}

	public String getBranchD() {
		return branchD;
	}

	public void setBranchD(String branchD) {
		this.branchD = branchD;
	}

	public int getRightAnswer() {
		return rightAnswer;
	}

	public void setRightAnswer(int rightAnswer) {
		this.rightAnswer = rightAnswer;
	}
	
}
