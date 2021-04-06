package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 护盾，一段时间内能吸收多少伤害
 * 
 * 
 *
 */
public class BuffTemplate_HuDunSudu extends BuffTemplate{


	/**
	 * 吸收多少伤害
	 */
	protected int dmamges[];
	
	protected byte shield = -1;

	protected int speed[];
	
	public int[] getSpeed() {
		return speed;
	}

	public void setSpeed(int[] speed) {
		this.speed = speed;
	}
	
	public BuffTemplate_HuDunSudu(){
		setName(Translate.text_3217);
		setDescription(Translate.text_3218);
		speed = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_HuDunSudu buff = new Buff_HuDunSudu();
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
