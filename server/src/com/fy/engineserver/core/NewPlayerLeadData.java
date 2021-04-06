package com.fy.engineserver.core;

/**
 * 新手引导数据
 * 
 */
public class NewPlayerLeadData {

	/** ID */
	private long id;

	/** 类型 */
	private int type;
	/** 窗口名字 */
	private String windowName;
	/** 控件名称 */
	private String activeXName;
	/** 显示文字 */
	private String showMessage;
	/** 出栈事件 */
	private int outStackEvent;
	/** 左右优先 */
	private int priority;
	
	/** 强制引导 */
	private int forceType;


	public NewPlayerLeadData(long id, int type, String windowName, String activeXName, String showMessage, int outStackEvent, int priority,int forceType) {
		this.id = id;
		this.type = type;
		this.windowName = windowName;
		this.activeXName = activeXName;
		this.showMessage = showMessage;
		this.outStackEvent = outStackEvent;
		this.priority = priority;
		this.forceType=forceType;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getWindowName() {
		return windowName;
	}

	public void setWindowName(String windowName) {
		this.windowName = windowName;
	}

	public String getActiveXName() {
		return activeXName;
	}

	public void setActiveXName(String activeXName) {
		this.activeXName = activeXName;
	}

	public String getShowMessage() {
		return showMessage;
	}

	public void setShowMessage(String showMessage) {
		this.showMessage = showMessage;
	}

	public int getOutStackEvent() {
		return outStackEvent;
	}

	public void setOutStackEvent(int outStackEvent) {
		this.outStackEvent = outStackEvent;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public int getForceType() {
		return forceType;
	}

	public void setForceType(int forceType) {
		this.forceType = forceType;
	}
	@Override
	public String toString() {
		return "NewPlayerLeadData [id=" + id + ", type=" + type + ", windowName=" + windowName + ", activeXName=" + activeXName + ", showMessage=" + showMessage + ", outStackEvent=" + outStackEvent + ", priority=" + priority + "]";
	}

}
