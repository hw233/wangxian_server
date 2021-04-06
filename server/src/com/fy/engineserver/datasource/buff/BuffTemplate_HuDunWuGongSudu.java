package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_HuDunWuGongSudu extends BuffTemplate{

	/**
	 * 系数
	 */
	protected int[] modulus;
	/**
	 * 类型
	 */
	protected byte shield = -1;

	protected int speed[];
	
	public int[] getSpeed() {
		return speed;
	}

	public void setSpeed(int[] speed) {
		this.speed = speed;
	}

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

	public BuffTemplate_HuDunWuGongSudu(){
		setName(Translate.text_3223);
		speed = new int[]{1,3,5,7,9,11,13,15,17,19};
		setDescription(Translate.text_3223);
	}
	
	public Buff createBuff(int level) {
		Buff_HuDunWuGongSudu buff = new Buff_HuDunWuGongSudu();
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
		if(speed != null && speed.length > level){
			if(speed[level] > 0)
				sb.append(Translate.text_3210+speed[level]+"%");
			else if(speed[level] < 0)
				sb.append(Translate.text_3211+(-speed[level])+"%");
		}else{
			sb.append(Translate.text_3212);
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
