package com.fy.engineserver.sprite.pet.suit.effect;
/**
 * 宠物套装效果
 * 
 * @date on create 2016年8月26日 上午9:53:45
 */
public enum SuitEffectEnum {
	EFFECT_ADDPROPERTY(0, "增加宠物属性", new EffectAddPropty()),
	EFFECT_ADDDAMAGERATEBYHP(1, "目标血量高于一定比例增加对应比例伤害结果", new EffectAddDamageByHp()),
	EFFECT_ADDCRITICALBYLEVEL(3, "等级相关增加暴击值", new AddCriticalByLevel()),
	EFFECT_ADDDAMAGERATEBYHP2(4, "目标血量低于一定比例增加对应比例伤害结果", new EffectAddDamageByHp2()),
	;
	
	private int index;
	
	private String des;
	
	private AbstractSuitEffect effect;
	
	private SuitEffectEnum(int index, String des, AbstractSuitEffect effect) {
		this.index = index;
		this.des = des;
		this.effect = effect;
	}
	
	public static SuitEffectEnum valueOf(int index) {
		for (SuitEffectEnum e : SuitEffectEnum.values()) {
			if (e.getIndex() == index) {
				return e;
			}
		}
		return null;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public AbstractSuitEffect getEffect() {
		return effect;
	}

	public void setEffect(AbstractSuitEffect effect) {
		this.effect = effect;
	}
	
}
