package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 迟钝
 * 
 * 
 *
 */
public class BuffTemplate_ChiDun extends BuffTemplate{

	/**
	 * 命中率降低的百分比
	 */
	protected int[] attackRating;
	

	/**
	 * 闪避率降低的百分比
	 */
	protected int[] dodge;
	
	

	public int[] getAttackRating() {
		return attackRating;
	}

	public void setAttackRating(int[] attackRating) {
		this.attackRating = attackRating;
	}

	public int[] getDodge() {
		return dodge;
	}

	public void setDodge(int[] dodge) {
		this.dodge = dodge;
	}

	public BuffTemplate_ChiDun(){
		setName(Translate.text_3151);
		setDescription(Translate.text_3152);
		attackRating = new int[]{1,3,5,7,9,11,13,15,17,19};
		dodge = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_ChiDun buff = new Buff_ChiDun();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		StringBuffer sb = new StringBuffer();
		if(attackRating != null && attackRating.length > level && attackRating[level] > 0){
			sb.append(Translate.text_3153+attackRating[level]+"%");
		}
		if(dodge != null && dodge.length > level && dodge[level] > 0){
			sb.append(Translate.text_3154+dodge[level]+"%");
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
