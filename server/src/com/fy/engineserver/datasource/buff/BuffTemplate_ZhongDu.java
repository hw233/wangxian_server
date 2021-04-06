package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_ZhongDu extends BuffTemplate{

	/**
	 * 减少多少点血
	 */
	protected int[] hp;

	private long[] lastingTime;

	public int[] getHp() {
		return hp;
	}

	public void setHp(int[] hp) {
		this.hp = hp;
	}

	public long[] getLastingTime() {
		return lastingTime;
	}

	public void setLastingTime(long[] lastingTime) {
		this.lastingTime = lastingTime;
	}

	public BuffTemplate_ZhongDu(){
		setName(Translate.text_3389);
		setDescription(Translate.text_3390);
	}
	
	public Buff_ZhongDu createBuff(int level) {
		Buff_ZhongDu buff = new Buff_ZhongDu();
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
		if(hp != null && hp.length > level && lastingTime != null && lastingTime.length > level){
			sb.append(Translate.text_3391+((double)lastingTime[level]/1000)+Translate.text_3234+hp[level]+Translate.text_3275);
			buff.setLastingTime(lastingTime[level]);
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
