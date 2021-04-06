package com.fy.engineserver.newtask.targets;

import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.actions.TaskAction;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;

public class TaskTargetOfRelation extends TaskTarget implements TaskConfig {

	private int type;

	public TaskTargetOfRelation(int type) {
		setTargetType(TargetType.RELATION);
		setTargetByteType(getTargetType().getIndex());
		setType(type);
		setTargetNum(1);
	}

	@Override
	public CompoundReturn dealAction(TaskAction action) {
		// TODO 根据不同类型获取角色的属性.
		return super.dealAction(action);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	@Override
	public CompoundReturn dealOnGet(Player player,Task task) {
		// TODO 根据不同类型获取角色的属性.
		return super.dealOnGet(player,task);
	}
	
	@Override
	public String toHtmlString(String cssClass) {
		StringBuffer sbf = new StringBuffer("<table class='");
		sbf.append(cssClass).append("'>");
		sbf.append("<tr><td>");
		sbf.append(getTargetType().getName()).append("</td></tr><tr>");
		sbf.append("<td>");
		sbf.append("需要玩家拥有社会关系").append(getType());
		sbf.append("</td>");
		sbf.append("</tr>");
		sbf.append("</table>");
		return sbf.toString();
	}
	
	public static void main(String[] args) {
		TaskTargetOfRelation ofRelation = new TaskTargetOfRelation(1);
//		System.out.println(ofRelation.toHtmlString("c"));
	}
}
