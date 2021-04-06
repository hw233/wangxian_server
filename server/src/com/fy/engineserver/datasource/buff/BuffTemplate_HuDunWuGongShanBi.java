package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_HuDunWuGongShanBi extends BuffTemplate{

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
	 * 闪避率的百分比
	 */
	protected int[] dodge;

	public int[] getDodge() {
		return dodge;
	}

	public void setDodge(int[] dodge) {
		this.dodge = dodge;
	}

	public BuffTemplate_HuDunWuGongShanBi(){
		setName(Translate.text_3222);
		setDescription(Translate.text_3222);
		dodge = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_HuDunWuGongShanBi buff = new Buff_HuDunWuGongShanBi();
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
		if(modulus != null && modulus.length > level){
			if(sb.length() > 0)
				sb.append("，");
			sb.append(Translate.text_3220);
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
