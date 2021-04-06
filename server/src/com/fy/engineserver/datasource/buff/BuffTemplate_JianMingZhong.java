package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 降低命中
 * 
 * 
 *
 */
public class BuffTemplate_JianMingZhong extends BuffTemplate{

	/**
	 * 降低命中率的百分比
	 */
	protected int[] attackRating;

	public int[] getAttackRating() {
		return attackRating;
	}

	public void setAttackRating(int[] attackRating) {
		this.attackRating = attackRating;
	}

	public BuffTemplate_JianMingZhong(){
		setName(Translate.text_3153);
		setDescription(Translate.text_3257);
		attackRating = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff_JianMingZhong createBuff(int level) {
		Buff_JianMingZhong buff = new Buff_JianMingZhong();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(attackRating != null && attackRating.length > level){
			if(attackRating[level] > 0)
				buff.setDescription(Translate.text_3258+attackRating[level]+"%");
			else if(attackRating[level] < 0)
				buff.setDescription(Translate.text_3259+(-attackRating[level])+"%");
		}else{
			buff.setDescription(Translate.text_3258);
		}
		return buff;
	}

}
