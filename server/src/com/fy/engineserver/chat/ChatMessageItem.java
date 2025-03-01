package com.fy.engineserver.chat;


/**
 * 
 * 消息中包含的元素 元素可以是字符串，道具，任务或图标
 * 
 * 
 * @author Administrator
 *
 */
public class ChatMessageItem {
	
	/**
	 * 附件显示时的颜色
	 */
	private int color = 0;
	
	/**
	 * 附件的名称
	 */
	private String name = "";
	
	/**
	 * 附件的Id
	 */
	private long id = -1;
	
	public long getId(){
		return id;
	}
	public void setId(long id){
		this.id = id;
	}
	
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	
	public int getColor(){
		return color;
	}
	public void setColor(int color){
		this.color = color;
	}
}
