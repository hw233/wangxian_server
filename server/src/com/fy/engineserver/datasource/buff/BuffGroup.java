package com.fy.engineserver.datasource.buff;

/**
 * buff组
 * 
 * 在一个人身上的buff有如下的限制：
 * 1. 同一组里面的buff，同一个施法者的，只能存在一个，后加的顶替前面加的buff
 * 2. 同一组里面的buff，不同施法者施加的同一个buff，级别高的顶替级别低的
 * 
 * 
 *
 */
public class BuffGroup {

	protected int groupId;
	
	protected String groupName;
	
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
}
