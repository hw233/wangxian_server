package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_JiaXueFaGong extends BuffTemplate{

	/**
	 * 系数
	 */
	protected int[] modulus;

	private long[] lastingTime;

	public int[] getModulus() {
		return modulus;
	}

	public void setModulus(int[] modulus) {
		this.modulus = modulus;
	}

	public long[] getLastingTime() {
		return lastingTime;
	}

	public void setLastingTime(long[] lastingTime) {
		this.lastingTime = lastingTime;
	}

	public BuffTemplate_JiaXueFaGong(){
		setName(Translate.text_3294);
		setDescription(Translate.text_3294);
	}
	
	public Buff createBuff(int level) {
		Buff_JiaXueFaGong buff = new Buff_JiaXueFaGong();
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
		if(modulus != null && modulus.length > level && lastingTime != null && lastingTime.length > level){
			sb.append(Translate.text_3295+((double)lastingTime[level]/1000)+Translate.text_3296);
			buff.setLastingTime(lastingTime[level]);
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
