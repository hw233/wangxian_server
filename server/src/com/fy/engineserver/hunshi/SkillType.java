package com.fy.engineserver.hunshi;

public enum SkillType {
	攻击怪物时有几率触发暴击(1),
	受伤害不超过最大生命比例(2),
	被控制时降低受伤害比例(3),
	自身生命值X低于X受伤害降低Y(4),
	自身生命值高于X增加伤害Y(5),
	被攻击有几率增加移动速度(6),
	目标生命低于X则攻击目标有几率出最高伤害值Y(7),
	被攻击有几率免受伤害且回复伤害量一定比例的生命(8),
	攻击有几率使目标在X时间内无法回血(9),
	自身生命剩余X时自动清除所有不良状态且不受任何负面BUFF(10), 
	被控制技能攻击时传染给攻击方相同的控制状态1(11), 
	被控制技能攻击时传染给攻击方相同的控制状态2(12), ;

	private SkillType(int index) {
		this.index = index;
	}

	private int index;

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

}
