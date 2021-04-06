package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_HuDunFaGong extends BuffTemplate{

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

	public BuffTemplate_HuDunFaGong(){
		setName(Translate.text_3202);
		setDescription(Translate.text_3202);
	}
	
	public Buff createBuff(int level) {
		Buff_HuDunFaGong buff = new Buff_HuDunFaGong();
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
		if(modulus != null && modulus.length > level){
			sb.append(Translate.text_3203);
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
