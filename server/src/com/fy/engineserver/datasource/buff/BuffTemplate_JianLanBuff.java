package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_JianLanBuff extends BuffTemplate{

	/**
	 * 减蓝
	 */
	protected int[] mp;

	private long[] lastingTime;

	public int[] getMp() {
		return mp;
	}

	public void setMp(int[] mp) {
		this.mp = mp;
	}

	public long[] getLastingTime() {
		return lastingTime;
	}

	public void setLastingTime(long[] lastingTime) {
		this.lastingTime = lastingTime;
	}

	public BuffTemplate_JianLanBuff(){
		setName(Translate.text_3248);
		setDescription(Translate.text_3248);
	}
	
	public Buff createBuff(int level) {
		Buff_JianLanBuff buff = new Buff_JianLanBuff();
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
		if(mp != null && mp.length > level && lastingTime != null && lastingTime.length > level){
			if(mp[level] > 0){
				sb.append(Translate.text_3231+((double)lastingTime[level]/1000)+Translate.text_3234+mp[level]+Translate.text_3233);
			}else if(mp[level] < 0){
				sb.append(Translate.text_3231+((double)lastingTime[level]/1000)+Translate.text_3232+(-mp[level])+Translate.text_3233);
			}
			
			buff.setLastingTime(lastingTime[level]);
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
