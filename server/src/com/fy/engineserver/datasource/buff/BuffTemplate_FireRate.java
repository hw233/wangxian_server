package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 篝火经验系数
 * 
 * 
 *
 */
public class BuffTemplate_FireRate extends BuffTemplate{

	/**
	 * 各个等级增加暴击的百分比，10表示增加10%
	 */
	protected int rate[];

	public int[] getRate() {
		return rate;
	}

	public void setRate(int[] rate) {
		this.rate = rate;
	}

	public BuffTemplate_FireRate(){
		setName(Translate.text_3178);
		setDescription(Translate.text_3178);
		rate = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_FireRate buff = new Buff_FireRate();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		buff.setDescription("");
		return buff;
	}

}
