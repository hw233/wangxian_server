package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 增加暴击
 * 
 * 
 *
 */
public class BuffTemplate_BaoJi extends BuffTemplate{

	/**
	 * 各个等级增加暴击的百分比，1000为基数，10表示增加1%
	 */
	protected int criticalHit[];
	
	public int[] getCriticalHit() {
		return criticalHit;
	}

	public void setCriticalHit(int[] criticalHit) {
		this.criticalHit = criticalHit;
	}

	public BuffTemplate_BaoJi(){
		setName(Translate.text_3139);
		setDescription(Translate.text_3140);
		criticalHit = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_BaoJi buff = new Buff_BaoJi();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(criticalHit != null && criticalHit.length > level){
			if(criticalHit[level] > 0)
				buff.setDescription(Translate.text_3141+criticalHit[level]*1f/10+"%");
			else if(criticalHit[level] < 0)
				buff.setDescription(Translate.text_3142+(-criticalHit[level]*1f/10)+"%");
		}else{
			buff.setDescription(Translate.text_3141);
		}
		return buff;
	}

}
