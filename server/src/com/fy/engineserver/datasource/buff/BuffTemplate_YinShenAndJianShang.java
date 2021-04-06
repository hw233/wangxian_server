package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 隐身且减少受到伤害
 * 
 * 
 *
 */
public class BuffTemplate_YinShenAndJianShang extends BuffTemplate{

	/**
	 * 减少受到伤害百分比
	 */
	protected int decreaseDamage[];

	public int[] getDecreaseDamage() {
		return decreaseDamage;
	}

	public void setDecreaseDamage(int[] decreaseDamage) {
		this.decreaseDamage = decreaseDamage;
	}

	public BuffTemplate_YinShenAndJianShang(){
		setName(Translate.text_3280);
		setDescription(Translate.text_3281);
		decreaseDamage = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_YinShenAndJianShang buff = new Buff_YinShenAndJianShang();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(decreaseDamage != null && decreaseDamage.length > level){
			buff.setDescription(Translate.translateString(Translate.隐身且减少受到伤害详细, new String[][]{{Translate.COUNT_1,decreaseDamage[level]/10.0+""}}));
		}else{
			buff.setDescription(Translate.隐身且减少受到伤害);
		}
		return buff;
	}

}
