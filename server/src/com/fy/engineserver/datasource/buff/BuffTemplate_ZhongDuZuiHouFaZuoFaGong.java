package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_ZhongDuZuiHouFaZuoFaGong extends BuffTemplate{

	/**
	 * 系数
	 */
	protected int[] modulus;

	public int[] getModulus() {
		return modulus;
	}

	public void setModulus(int[] modulus) {
		this.modulus = modulus;
	}

	public BuffTemplate_ZhongDuZuiHouFaZuoFaGong(){
		setName(Translate.text_3392);
		setDescription(Translate.text_3392);
	}
	
	public Buff createBuff(int level) {
		Buff_ZhongDuZuiHouFaZuoFaGong buff = new Buff_ZhongDuZuiHouFaZuoFaGong();
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
			sb.append(Translate.text_3396);
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
