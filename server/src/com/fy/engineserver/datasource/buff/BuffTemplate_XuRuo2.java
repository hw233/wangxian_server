package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;
/**
 * 
 * 
 * @date on create 2015年8月26日 下午4:47:58
 */
public class BuffTemplate_XuRuo2 extends BuffTemplate{

	/**
	 * 物理防御降低的百分比
	 */
	protected int[] defence;
	

	/**
	 * 法术防御降低的百分比
	 */
	protected int[] resistance;
	
	
	public int[] getDefence() {
		return defence;
	}

	public void setDefence(int[] defence) {
		this.defence = defence;
	}

	public int[] getResistance() {
		return resistance;
	}

	public void setResistance(int[] resistance) {
		this.resistance = resistance;
	}

	public BuffTemplate_XuRuo2(){
		setName(Translate.text_3380);
		setDescription(Translate.text_3381);
		defence = new int[]{1,3,5,7,9,11,13,15,17,19};
		resistance = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff_XuLuo2 createBuff(int level) {
		Buff_XuLuo2 buff = new Buff_XuLuo2();
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
		if(defence != null && defence.length > level && defence[level] > 0){
			sb.append(Translate.text_3382+(defence[level]/10)+"%");
		}
		if(resistance != null && resistance.length > level && resistance[level] > 0){
			if(sb.length() > 0)
				sb.append("，");
			sb.append(Translate.text_3383+(resistance[level]/10)+"%");
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
