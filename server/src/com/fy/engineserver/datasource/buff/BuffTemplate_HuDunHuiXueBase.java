package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 每5秒钟恢复血和护盾，一段时间内能吸收多少伤害
 * 
 * 
 *
 */
public class BuffTemplate_HuDunHuiXueBase extends BuffTemplate{


	/**
	 * 吸收多少伤害
	 */
	protected int dmamges[];
	
	protected byte shield = -1;

	/**
	 * 每5秒钟恢复血
	 */
	protected int hpRecoverBase[];

	public int[] getHpRecoverBase() {
		return hpRecoverBase;
	}

	public void setHpRecoverBase(int[] hpRecoverBase) {
		this.hpRecoverBase = hpRecoverBase;
	}

	public BuffTemplate_HuDunHuiXueBase(){
		setName(Translate.text_3213);
		setDescription(Translate.text_3214);
		hpRecoverBase = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_HuDunHuiXueBase buff = new Buff_HuDunHuiXueBase();
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
		if(hpRecoverBase != null && hpRecoverBase.length > level){
			if(sb.length() > 0)
				sb.append("，");
			sb.append(Translate.text_3205+hpRecoverBase[level]+Translate.text_1469);
		}else{
			if(sb.length() > 0)
				sb.append("，");
			sb.append(Translate.text_3206);
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
