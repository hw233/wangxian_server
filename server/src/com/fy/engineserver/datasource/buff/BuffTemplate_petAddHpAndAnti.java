package com.fy.engineserver.datasource.buff;



/**
 * 免疫各种状态  
 * 
 *
 */
public class BuffTemplate_petAddHpAndAnti extends BuffTemplate{
	private int[] hpRates;
	
	private int[] antiRates;

	public BuffTemplate_petAddHpAndAnti(){
		setName("宠物增加血上限和反伤比例");
		setDescription("宠物增加血上限和反伤比例");
	}
	
	public Buff createBuff(int level) {
		Buff_PetAddHpAndAnti buff = new Buff_PetAddHpAndAnti();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		String des = "";
//		if (level < immuType.length && immuType[level] <= Translate.免疫buff描述.length) {
//			des = Translate.免疫buff描述[immuType[level]-1];
//		}
		buff.setDescription(des);
		return buff;
	}

	public int[] getHpRates() {
		return hpRates;
	}

	public void setHpRates(int[] hpRates) {
		this.hpRates = hpRates;
	}

	public int[] getAntiRates() {
		return antiRates;
	}

	public void setAntiRates(int[] antiRates) {
		this.antiRates = antiRates;
	}
	

}
