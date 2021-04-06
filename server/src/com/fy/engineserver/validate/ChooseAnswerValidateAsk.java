package com.fy.engineserver.validate;

public class ChooseAnswerValidateAsk extends ValidateAsk {

	private int askType;

	private ChooseAnswerAskData data;
	
	/**
	 * 输入的答案是否正确
	 * 
	 * @param input
	 * @return
	 */
	public boolean isRight(String input) {
		if (data.getAnswers()[data.getRightAnswerIndex()].equals(input)) {
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "ValidateAsk [类型"+askType+"] [题目:"+data.toString()+"]";
	}

	public int getAskType() {
		return askType;
	}

	public void setAskType(int askType) {
		this.askType = askType;
	}
	
	public void setData(ChooseAnswerAskData data) {
		this.data = data;
	}

	public ChooseAnswerAskData getData() {
		return data;
	}
}
