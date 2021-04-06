package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 闪避值暴击值
 * 
 * 
 *
 */
public class BuffTemplate_ShanBiBaojiZhi extends BuffTemplate{

	/**
	 * 闪避率的值
	 */
	protected int[] dodgeB;

	public int[] getDodgeB() {
		return dodgeB;
	}

	public void setDodgeB(int[] dodgeB) {
		this.dodgeB = dodgeB;
	}

	/**
	 * 各个等级增加暴击的值
	 */
	protected int criticalHitB[];
	
	public int[] getCriticalHitB() {
		return criticalHitB;
	}

	public void setCriticalHitB(int[] criticalHitB) {
		this.criticalHitB = criticalHitB;
	}

	public BuffTemplate_ShanBiBaojiZhi(){
		setName(Translate.text_3343);
		setDescription(Translate.text_3344);
		dodgeB = new int[]{1,3,5,7,9,11,13,15,17,19};
		criticalHitB = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_ShanBiBaojiZhi buff = new Buff_ShanBiBaojiZhi();
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
		if(dodgeB != null && dodgeB.length > level){
			if(dodgeB[level] > 0)
				sb.append(Translate.text_3208+dodgeB[level]+Translate.text_1469);
			else if(dodgeB[level] < 0)
				sb.append(Translate.text_3154+(-dodgeB[level])+Translate.text_1469);
		}else{
			sb.append(Translate.text_3208);
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
