package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 偷果实给物品buf
 * 
 *
 */
public class BuffTemplate_StealFruit extends BuffTemplate{

	/** 获得碎片比率 */
	private int[] rate;
	/** 偷取次数 */
	private int[] ddAmount;
	/** 给予物品数量 */
	private int[] giveNum;
	
	public BuffTemplate_StealFruit(){
		setRate(new int[]{10,20,30,40,50});
		setDdAmount(new int[]{5,5,5,5,5});
		setGiveNum(new int[]{1,1,1,1,1});
	}
	
	@Override
	public Buff createBuff(int level) {
		// TODO Auto-generated method stub
		Buff_StealFruit buff = new Buff_StealFruit();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		String str = Translate.translateString(Translate.偷果实buff, new String[][] {{Translate.STRING_1, (rate[level] / 10)+"%"}, {Translate.STRING_2, Translate.STRING_2}});
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

	public int[] getGiveNum() {
		return giveNum;
	}

	public void setGiveNum(int[] giveNum) {
		this.giveNum = giveNum;
	}


}
