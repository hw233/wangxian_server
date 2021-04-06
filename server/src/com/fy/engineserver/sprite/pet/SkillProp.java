package com.fy.engineserver.sprite.pet;

public class SkillProp {
	public String skillDesc = "";		//技能描述
	public int propType;			//属性类型
	public int propValue;			//属性值
	public int valueType;			//属性值类型
	public int effectTarget;		//作用目标1:宠物本身2:主人
	@Override
	public String toString() {
		return "SkillProp [effectTarget=" + effectTarget + ", propType="
				+ propType + ", propValue=" + propValue + ", skillDesc="
				+ skillDesc + ", valueType=" + valueType + "]";
	}
	
}
