package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_addDamage extends BuffTemplate{

	protected int damage[];

	public int[] getDamage() {
		return damage;
	}

	public void setDamage(int[] damage) {
		this.damage = damage;
	}

	public BuffTemplate_addDamage(){
		setName(Translate.text_3317);
		setDescription(Translate.text_3387);
		damage = new int[]{5,10,15,20,25,30,35,40,45,50,60,70,80,90,100};
	}
	
	public Buff createBuff(int level) {
		Buff_AddDamage buff = new Buff_AddDamage();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId()); 
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(damage != null && damage.length > level){
			if(damage[level] > 0)
				buff.setDescription(Translate.目标受到的伤害提升+damage[level]+"%");
		}else{
			buff.setDescription(Translate.目标受到的伤害提升);
		}
		return buff;
	}

}
