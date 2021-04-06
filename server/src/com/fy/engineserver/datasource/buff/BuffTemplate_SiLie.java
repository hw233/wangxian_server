package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 固定时间减血，但如果中buff者血满后buff消失
 * 
 *
 */
public class BuffTemplate_SiLie extends BuffTemplate{

	/**
	 * 减血
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

	public BuffTemplate_SiLie(){
		setName(Translate.减血);
		setDescription(Translate.减血);
	}
	
	public Buff createBuff(int level) {
		Buff_SiLie buff = new Buff_SiLie();
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
			sb.append(Translate.translateString(Translate.撕裂描述, new String[][]{{Translate.COUNT_1,((double)lastingTime[level]/1000)+""},{Translate.COUNT_2,hp[level]+""}}));
			buff.setLastingTime(lastingTime[level]);
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
