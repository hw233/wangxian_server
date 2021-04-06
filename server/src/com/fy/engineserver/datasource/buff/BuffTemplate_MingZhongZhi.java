package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 加命中
 * 
 * 
 *
 */
public class BuffTemplate_MingZhongZhi extends BuffTemplate{

	/**
	 * 命中率的值
	 */
	protected int[] attackRatingB;

	public int[] getAttackRatingB() {
		return attackRatingB;
	}

	public void setAttackRatingB(int[] attackRatingB) {
		this.attackRatingB = attackRatingB;
	}

	public BuffTemplate_MingZhongZhi(){
		setName(Translate.text_3326);
		setDescription(Translate.text_3327);
		attackRatingB = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_MingZhongZhi buff = new Buff_MingZhongZhi();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(attackRatingB != null && attackRatingB.length > level){
			if(attackRatingB[level] > 0)
				buff.setDescription(Translate.text_3324+attackRatingB[level]+Translate.text_1469);
			else if(attackRatingB[level] < 0)
				buff.setDescription(Translate.text_3153+(-attackRatingB[level])+Translate.text_1469);
		}else{
			buff.setDescription(Translate.text_3324);
		}
		return buff;
	}

}
