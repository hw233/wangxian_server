package com.fy.engineserver.activity.wolf;

public enum ActivityState {
	
	等待进入(0),
	准备战斗(1),
	战斗开始(2),
	通知结果(3),
	战斗结束(4);
	
	int id;
	ActivityState(int id){
		this.id = id;
	}
}
