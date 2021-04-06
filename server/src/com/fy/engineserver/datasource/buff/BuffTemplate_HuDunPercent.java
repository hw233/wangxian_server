package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 护盾，一段时间内能吸收多少伤害
 * 
 * 
 *
 */
public class BuffTemplate_HuDunPercent extends BuffTemplate{


	/**
	 * 吸收多少伤害
	 */
	protected int dmamges_percent[];
	
	protected byte shield = -1;
	
	public BuffTemplate_HuDunPercent(){
		setName(Translate.text_3176);
		setDescription(Translate.text_3177);
	}
	
	public Buff createBuff(int level) {
		Buff_HuDunPercent buff = new Buff_HuDunPercent();
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
		if(dmamges_percent != null && dmamges_percent.length > level){
			sb.append(Translate.text_977+dmamges_percent[level]+"%");
		}
		buff.setDescription(sb.toString());
		return buff;
	}

	public int[] getDmamges_percent() {
		return dmamges_percent;
	}

	public void setDmamges_percent(int[] dmamgesPercent) {
		dmamges_percent = dmamgesPercent;
	}

	public byte getShield() {
		return shield;
	}

	public void setShield(byte shield) {
		this.shield = shield;
	}

}
