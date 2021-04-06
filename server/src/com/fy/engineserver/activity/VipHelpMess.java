package com.fy.engineserver.activity;

public class VipHelpMess {
	
	public int viplevel;
	public String title;
	public String content;
	
	public VipHelpMess(int viplevel, String title, String content) {
		this.viplevel = viplevel;
		this.title = title;
		this.content = content;
	}

	@Override
	public String toString() {
		return "VipHelpMess [viplevel=" + this.viplevel + ", title=" + this.title + "]";
	}
	
}
