package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 增加暴击
 * 
 * 
 *
 */
public class BuffTemplate_BaoJiZhi extends BuffTemplate{

	/**
	 * 各个等级增加暴击的百分比，10表示增加10%
	 */
	protected int criticalHitB[];

	public int[] getCriticalHitB() {
		return criticalHitB;
	}

	public void setCriticalHitB(int[] criticalHitB) {
		this.criticalHitB = criticalHitB;
	}

	public BuffTemplate_BaoJiZhi(){
		setName(Translate.text_3147);
		setDescription(Translate.text_3147);
		criticalHitB = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_BaoJiZhi buff = new Buff_BaoJiZhi();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(criticalHitB != null && criticalHitB.length > level){
			if(criticalHitB[level] > 0)
				buff.setDescription(Translate.text_3147+criticalHitB[level]+Translate.text_1469);
			else if(criticalHitB[level] < 0)
				buff.setDescription(Translate.text_3148+(-criticalHitB[level])+Translate.text_1469);
		}else{
			buff.setDescription(Translate.text_3147);
		}
		return buff;
	}

}
