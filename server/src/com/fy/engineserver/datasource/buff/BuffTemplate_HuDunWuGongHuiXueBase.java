package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_HuDunWuGongHuiXueBase extends BuffTemplate{

	/**
	 * 系数
	 */
	protected int[] modulus;
	/**
	 * 类型
	 */
	protected byte shield = -1;

	public int[] getModulus() {
		return modulus;
	}

	public void setModulus(int[] modulus) {
		this.modulus = modulus;
	}

	public byte getShield() {
		return shield;
	}

	public void setShield(byte shield) {
		this.shield = shield;
	}

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

	public BuffTemplate_HuDunWuGongHuiXueBase(){
		setName(Translate.text_3221);
		setDescription(Translate.text_3221);
		hpRecoverBase = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_HuDunWuGongHuiXueBase buff = new Buff_HuDunWuGongHuiXueBase();
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
		if(hpRecoverBase != null && hpRecoverBase.length > level){
			sb.append(Translate.text_3205+hpRecoverBase[level]+Translate.text_1469);
		}else{
			sb.append(Translate.text_3206);
		}
		if(modulus != null && modulus.length > level){
			if(sb.length() > 0)
				sb.append("，");
			sb.append(Translate.text_3220);
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
