package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 护盾，一段时间内能吸收多少伤害
 * 
 * 
 *
 */
public class BuffTemplate_HuDun extends BuffTemplate{


	/**
	 * 吸收多少伤害
	 */
	protected int dmamges[];
	
	protected byte shield = -1;
	
	public BuffTemplate_HuDun(){
		setName(Translate.text_3176);
		setDescription(Translate.text_3177);
	}
	
	public Buff createBuff(int level) {
		Buff_HuDun buff = new Buff_HuDun();
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
		if(dmamges != null && dmamges.length > level){
			sb.append(Translate.text_977+dmamges[level]+Translate.text_3201);
		}
		buff.setDescription(sb.toString());
		return buff;
	}

	public int[] getDmamges() {
		return dmamges;
	}

	public void setDmamges(int[] dmamges) {
		this.dmamges = dmamges;
	}

	public byte getShield() {
		return shield;
	}

	public void setShield(byte shield) {
		this.shield = shield;
	}

}
