package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 根据剩余血量百分比触发另一个buff
 * 当触发另一个buff后，此buff消失
 * 
 *
 */
public class BuffTemplate_XuePercentTriggerOtherBuff extends BuffTemplate{

	/**
	 * 血量百分比
	 */
	protected int hpPercent;

	/**
	 * 触发的buff名字
	 */
	protected String triggerBuffName;

	/**
	 * 持续时间
	 */
	private long[] lastingTimes;

	public int getHpPercent() {
		return hpPercent;
	}

	public void setHpPercent(int hpPercent) {
		this.hpPercent = hpPercent;
	}

	public String getTriggerBuffName() {
		return triggerBuffName;
	}

	public void setTriggerBuffName(String triggerBuffName) {
		this.triggerBuffName = triggerBuffName;
	}

	public long[] getLastingTimes() {
		return lastingTimes;
	}

	public void setLastingTimes(long[] lastingTimes) {
		this.lastingTimes = lastingTimes;
	}

	public BuffTemplate_XuePercentTriggerOtherBuff(){
		setName(Translate.text_3373);
		setDescription(Translate.text_3373);
	}
	
	public Buff createBuff(int level) {
		Buff_XuePercentTriggerOtherBuff buff = new Buff_XuePercentTriggerOtherBuff();
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
		if(hpPercent > 0 && lastingTimes != null && lastingTimes.length > level){
			if(hpPercent > 0){
				sb.append(Translate.text_3374+hpPercent+Translate.text_3375+triggerBuffName+Translate.text_3376+((double)lastingTimes[level]/1000)+Translate.text_3377+this.getName()+Translate.text_3378);
				buff.setLastingTime(lastingTimes[level]);
			}
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
