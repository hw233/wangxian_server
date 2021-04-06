package com.fy.engineserver.validate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 选择答案题目模板
 * 
 *
 */
public class ChooseAnswerAskData {
	//题目信息
	private String answerMsg;
	//答案
	private String[] answers;
	//正确答案位置
	private int rightAnswerIndex;
	
	private ArrayList<String> clientMsg;
	
	public void setAnswerMsg(String answerMsg) {
		this.answerMsg = answerMsg;
	}

	public String getAnswerMsg() {
		return answerMsg;
	}

	public void setAnswers(String[] answers) {
		this.answers = answers;
	}

	public String[] getAnswers() {
		return answers;
	}

	public void setRightAnswerIndex(int rightAnswerIndex) {
		this.rightAnswerIndex = rightAnswerIndex;
	}

	public int getRightAnswerIndex() {
		return rightAnswerIndex;
	}
	
	@Override
	public String toString() {
		return "["+answerMsg + "] [" +Arrays.toString(answers)+ "] [答案:"+rightAnswerIndex+"]";
	}

	public void setClientMsg(ArrayList<String> clientMsg) {
		this.clientMsg = clientMsg;
	}

	public String[] getClientMsg() {
		if (clientMsg == null) {
			clientMsg = new ArrayList<String>();
			clientMsg.add(Translate.答题标题);
			clientMsg.add(answerMsg);
			for (int i = 0; i < answers.length; i++) {
				clientMsg.add(answers[i]);
			}
		}
		//打乱答案顺序
		Random r = new Random();
		for (int i = 0; i < 2; i++) {
			int index = r.nextInt(clientMsg.size()-2) + 2;
			String v = clientMsg.remove(index);
			clientMsg.add(v);
		}
		
		return clientMsg.toArray(new String[0]);
	}
}
