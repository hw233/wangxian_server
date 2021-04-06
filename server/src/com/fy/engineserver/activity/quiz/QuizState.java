package com.fy.engineserver.activity.quiz;



public enum QuizState {
	
	开始((byte)0),
	通知((byte)1),
	运行((byte)2),
	答题后((byte)3),
	完成((byte)4);
	
	
	public byte type;
	private QuizState(byte type) {
		this.type = type;
	}
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}


}
