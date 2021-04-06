package com.fy.boss.gm.gmuser;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class TransferQuestionForMax {
	
	public String questionMess;

	public String getQuestionMess() {
		return questionMess;
	}

	public void setQuestionMess(String questionMess) {
		this.questionMess = questionMess;
	}
	
}
