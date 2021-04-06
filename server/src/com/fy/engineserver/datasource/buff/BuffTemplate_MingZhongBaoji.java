package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 命中和暴击百分比
 * 
 * 
 *
 */
public class BuffTemplate_MingZhongBaoji extends BuffTemplate{

	/**
	 * 命中率的百分比
	 */
	protected int[] attackRating;

	public int[] getAttackRating() {
		return attackRating;
	}

	public void setAttackRating(int[] attackRating) {
		this.attackRating = attackRating;
	}

	/**
	 * 各个等级增加暴击的百分比，10表示增加10%
	 */
	protected int criticalHit[];
	
	public int[] getCriticalHit() {
		return criticalHit;
	}

	public void setCriticalHit(int[] criticalHit) {
		this.criticalHit = criticalHit;
	}

	public BuffTemplate_MingZhongBaoji(){
		setName(Translate.text_3322);
		setDescription(Translate.text_3323);
		attackRating = new int[]{1,3,5,7,9,11,13,15,17,19};
		criticalHit = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_MingZhongBaoji buff = new Buff_MingZhongBaoji();
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
		if(attackRating != null && attackRating.length > level){
			if(attackRating[level] > 0)
				sb.append(Translate.text_3324+attackRating[level]+"%");
			else if(attackRating[level] < 0)
				sb.append(Translate.text_3153+(-attackRating[level])+"%");
		}else{
			sb.append(Translate.text_3324);
		}
		if(criticalHit != null && criticalHit.length > level){
			if(criticalHit[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.text_3139+criticalHit[level]+"%");
			}else if(criticalHit[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.text_3146+(-criticalHit[level])+"%");
			}
		}else{
			sb.append(Translate.text_3139);
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
