package com.fy.boss.gm.gmuser.server;

import java.util.ArrayList;
import java.util.List;

import com.fy.boss.gm.gmuser.TransferQuestion;
import com.fy.boss.gm.gmuser.server.TransferQuestionManager;

public class ListForHelp {

	public static List<TransferQuestion> newQuestions  = new ArrayList<TransferQuestion>();
	
	public ListForHelp()
	{
		TransferQuestionManager.logger.warn("[ListForHelp 初始化调用] [newQuestions hashcode:"+newQuestions.hashCode()+"] [自身的hashcode:"+this.hashCode()+"]");
	}
	
	
}
