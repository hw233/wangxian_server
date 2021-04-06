package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_JiaXueLan extends BuffTemplate{

	/**
	 * 加血
	 */
	protected int[] hp;
	/**
	 * 加蓝
	 */
	protected int[] mp;
	private long[] lastingTime;

	public int[] getHp() {
		return hp;
	}

	public void setHp(int[] hp) {
		this.hp = hp;
	}

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

	public BuffTemplate_JiaXueLan(){
		setName(Translate.text_3299);
		setDescription(Translate.text_3299);
	}
	
	public Buff createBuff(int level) {
		Buff_JiaXueLan buff = new Buff_JiaXueLan();
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
			if(hp[level] > 0){
				sb.append(Translate.text_3231+((double)lastingTime[level]/1000)+Translate.text_3232+hp[level]+Translate.text_3275);
				buff.setLastingTime(lastingTime[level]);
			}
		}
		if(mp != null && mp.length > level && lastingTime != null && lastingTime.length > level){
			if(mp[level] > 0){
				if(sb.length() != 0){
					sb.append(Translate.text_3300+mp[level]+Translate.text_3301);		
				}else{
					sb.append(Translate.text_3231+((double)lastingTime[level]/1000)+Translate.text_3232+mp[level]+Translate.text_3301);
				}
			buff.setLastingTime(lastingTime[level]);
			}
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
