package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 命中和暴击值
 * 
 * 
 *
 */
public class BuffTemplate_MingZhongBaojiZhi extends BuffTemplate{

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

	/**
	 * 各个等级增加暴击的值，10表示增加10%
	 */
	protected int criticalHitB[];
	
	public int[] getCriticalHitB() {
		return criticalHitB;
	}

	public void setCriticalHitB(int[] criticalHitB) {
		this.criticalHitB = criticalHitB;
	}

	public BuffTemplate_MingZhongBaojiZhi(){
		setName(Translate.text_3322);
		setDescription(Translate.text_3325);
		attackRatingB = new int[]{1,3,5,7,9,11,13,15,17,19};
		criticalHitB = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_MingZhongBaojiZhi buff = new Buff_MingZhongBaojiZhi();
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
		if(attackRatingB != null && attackRatingB.length > level){
			if(attackRatingB[level] > 0)
				sb.append(Translate.text_3324+attackRatingB[level]+Translate.text_1469);
			else if(attackRatingB[level] < 0)
				sb.append(Translate.text_3153+(-attackRatingB[level])+Translate.text_1469);
		}else{
			sb.append(Translate.text_3324);
		}
		if(criticalHitB != null && criticalHitB.length > level){
			if(criticalHitB[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.text_3139+criticalHitB[level]+Translate.text_1469);
			}else if(criticalHitB[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.text_3146+(-criticalHitB[level])+Translate.text_1469);
			}
		}else{
			sb.append(Translate.text_3139);
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
