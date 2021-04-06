package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_QiangZhuang extends BuffTemplate{

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

	public BuffTemplate_QiangZhuang(){
		setName(Translate.text_1143);
		setDescription(Translate.text_3329);
		defence = new int[]{1,3,5,7,9,11,13,15,17,19};
		resistance = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level, int v1, int v2) {
		Buff_QiangZhuang buff = createBase(level);
		StringBuffer sb = new StringBuffer();
		buff.setDescription(sb.toString());
		buff.defence = v1;
		buff.resistance = v2;
		sb.append(Translate.text_3330+v1+"%");
		sb.append(Translate.text_3331+v2+"%");
		return buff;
	}
	public Buff createBuff(int level) {
		Buff_QiangZhuang buff = createBase(level);
		StringBuffer sb = new StringBuffer();
		if(defence != null && defence.length > level){
			if(defence[level] > 0){
				sb.append(Translate.text_3330+defence[level]+"%");
			}
		}
		if(resistance != null && resistance.length > level){
			if(resistance[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.text_3331+resistance[level]+"%");
			}
		}
		buff.setDescription(sb.toString());
		return buff;
	}

	public Buff_QiangZhuang createBase(int level) {
		Buff_QiangZhuang buff = new Buff_QiangZhuang();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		return buff;
	}

}
