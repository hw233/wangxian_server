package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 减少天劫雷劫伤害buff
 * 
 *
 */
public class BuffTemplate_RayDamage extends BuffTemplate{

	/** 免伤比例 */
	private int[] rate;
	/** 抵挡次数 */
	private int[] ddAmount;
	
	public BuffTemplate_RayDamage(){
		setRate(new int[]{10,20,30,40,50});
		setDdAmount(new int[]{5,5,5,5,5});
	}
	
	@Override
	public Buff createBuff(int level) {
		// TODO Auto-generated method stub
		Buff_RayDamage buff = new Buff_RayDamage();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		String str =  Translate.translateString(Translate.减免渡劫雷伤描述, new String[][] {{Translate.STRING_1, ddAmount[level]+""}, {Translate.STRING_2, rate[level]+""}});
		buff.setDescription(str);
		return buff;
	}

	public int[] getRate() {
		return rate;
	}

	public void setRate(int[] rate) {
		this.rate = rate;
	}

	public int[] getDdAmount() {
		return ddAmount;
	}

	public void setDdAmount(int[] ddAmount) {
		this.ddAmount = ddAmount;
	}


}
