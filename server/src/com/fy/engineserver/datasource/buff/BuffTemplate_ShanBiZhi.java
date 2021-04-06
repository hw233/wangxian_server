package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 闪避
 * 
 * 
 *
 */
public class BuffTemplate_ShanBiZhi extends BuffTemplate{

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

	public BuffTemplate_ShanBiZhi(){
		setName(Translate.text_3345);
		setDescription(Translate.text_3346);
		dodgeB = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_ShanBiZhi buff = new Buff_ShanBiZhi();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(dodgeB != null && dodgeB.length > level){
			if(dodgeB[level] > 0)
				buff.setDescription(Translate.text_3208+dodgeB[level]+Translate.text_1469);
			else if(dodgeB[level] < 0)
				buff.setDescription(Translate.text_3154+(-dodgeB[level])+Translate.text_1469);
		}else{
			buff.setDescription(Translate.text_3208);
		}
		return buff;
	}

}
