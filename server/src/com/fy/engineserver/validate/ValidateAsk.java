package com.fy.engineserver.validate;

/**
 * 一个验证问题，包括答案
 * 
 * @version 创建时间：Dec 26, 2012 7:00:11 PM
 * 
 */
public class ValidateAsk {
	
	private String ask;
	
	private String answer;
	
	private int askFormType;
	
	private Object[] askFormParameters;

	public String getAsk() {
		return ask;
	}

	public void setAsk(String ask) {
		this.ask = ask;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	/**
	 * 输入的答案是否正确
	 * @param input
	 * @return
	 */
	public boolean isRight(String input) {
		return input.equalsIgnoreCase(answer);
	}

	@Override
	public String toString() {
		return "ValidateAsk [ask=" + ask + ", answer=" + answer + "]";
	}

	public void setAskFormType(int askFormType) {
		this.askFormType = askFormType;
	}

	public int getAskFormType() {
		return askFormType;
	}

	public void setAskFormParameters(Object[] askFormParameters) {
		this.askFormParameters = askFormParameters;
	}

	public Object[] getAskFormParameters() {
		return askFormParameters;
	}
}
