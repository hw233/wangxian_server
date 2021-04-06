package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;


/**
 * 百分比减少受到伤害
 * @author Administrator
 *
 */
public class BuffTemplate_DecreaseDmgRate extends BuffTemplate{
	/** 法攻下限 */
	private int[] decreaseRate;
	
	public BuffTemplate_DecreaseDmgRate() {
		setDecreaseRate(new int[]{10,20,30,40,50,60,70,80,90,100});
	}

	@Override
	public Buff createBuff(int level) {
		// TODO Auto-generated method stub
		Buff_DecreaseDmgRate buff = new Buff_DecreaseDmgRate();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		int decreaseRate = this.getDecreaseRate()[level];
		buff.setDescription(String.format(Translate.减少百分比受到伤害, (decreaseRate/10)+""));
		return buff;
	}

	public int[] getDecreaseRate() {
		return decreaseRate;
	}

	public void setDecreaseRate(int[] decreaseRate) {
		this.decreaseRate = decreaseRate;
	}

	
	
}
