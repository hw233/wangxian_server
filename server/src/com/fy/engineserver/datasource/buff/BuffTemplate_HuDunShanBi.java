package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 闪避和护盾，一段时间内能吸收多少伤害
 * 
 * 
 *
 */
public class BuffTemplate_HuDunShanBi extends BuffTemplate{


	/**
	 * 吸收多少伤害
	 */
	protected int dmamges[];
	
	protected byte shield = -1;

	/**
	 * 闪避率的百分比
	 */
	protected int[] dodge;

	public int[] getDodge() {
		return dodge;
	}

	public void setDodge(int[] dodge) {
		this.dodge = dodge;
	}
	
	public BuffTemplate_HuDunShanBi(){
		setName(Translate.text_3215);
		setDescription(Translate.text_3216);
		dodge = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_HuDunShanBi buff = new Buff_HuDunShanBi();
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
		if(dodge != null && dodge.length > level){
			if(dodge[level] > 0)
				sb.append(Translate.text_3208+dodge[level]+"%");
			else if(dodge[level] < 0)
				sb.append(Translate.text_3154+(-dodge[level])+"%");
		}else{
			sb.append(Translate.text_3208);
		}
		if(dmamges != null && dmamges.length > level){
			if(sb.length() > 0)
				sb.append("，");
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
