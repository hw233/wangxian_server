package com.fy.engineserver.activity.dice;

public enum ActivityState {
	READY(0,"未开启"),
	START_SIGN(1,"开始报名"),
	READY_FIGHT(2,"准备战斗"),
	START_FIGHT(3,"开始战斗"),
	DICE_POINT(4,"摇骰子"),
	SHOW_BILLBORAD(5,"显示排行"),
	SHOW_RESULT(6,"胜利者"),
	GAME_OVER(7,"副本即将结束");
	
	public int id;
	public String name;
	
	ActivityState(int id,String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public static ActivityState valueOf(int index) {
		for (ActivityState d : ActivityState.values()) {
			if (d.getId() == index) {
				return d;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "id="+id+",name="+name;
	}
}
