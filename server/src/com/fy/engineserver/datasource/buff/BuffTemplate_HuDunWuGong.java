package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_HuDunWuGong extends BuffTemplate{

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

	public BuffTemplate_HuDunWuGong(){
		setName(Translate.text_3219);
		setDescription(Translate.text_3219);
	}
	
	public Buff createBuff(int level) {
		Buff_HuDunWuGong buff = new Buff_HuDunWuGong();
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
			sb.append(Translate.text_3220);
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
