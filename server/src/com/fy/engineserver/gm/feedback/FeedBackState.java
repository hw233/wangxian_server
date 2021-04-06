package com.fy.engineserver.gm.feedback;

public enum FeedBackState {

	
	未处理(0,"未处理"),
	等待反馈(1,"等待反馈"),
	新反馈(2,"新反馈"),
	已关闭(3,"已关闭"),
	评价(4,"评价");
	
	public int state;
	public String name;
	
	private FeedBackState(int state,String name){
		this.state = state;
		this.name = name;
	}
}
