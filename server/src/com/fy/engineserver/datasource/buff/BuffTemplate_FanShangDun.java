package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 反伤盾，一段时间内能反射多少伤害
 * 
 * 
 *
 */
public class BuffTemplate_FanShangDun extends BuffTemplate{


	/**
	 * 反射多少伤害百分比
	 */
	protected int ironMaidenPercent[];
	
	protected byte shield = -1;
	
	public BuffTemplate_FanShangDun(){
		setName(Translate.text_3176);
		setDescription(Translate.text_3177);
	}
	
	public Buff createBuff(int level) {
		Buff_FanShangDun buff = new Buff_FanShangDun();
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
		if(ironMaidenPercent != null && ironMaidenPercent.length > level){
			sb.append(Translate.text_3173+ironMaidenPercent[level]+Translate.text_3174);
		}
		buff.setDescription(sb.toString());
		return buff;
	}

	public int[] getIronMaidenPercent() {
		return ironMaidenPercent;
	}

	public void setIronMaidenPercent(int[] ironMaidenPercent) {
		this.ironMaidenPercent = ironMaidenPercent;
	}

	public byte getShield() {
		return shield;
	}

	public void setShield(byte shield) {
		this.shield = shield;
	}

}
