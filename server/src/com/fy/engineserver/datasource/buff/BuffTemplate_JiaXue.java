package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_JiaXue extends BuffTemplate{

	/**
	 * 加血千分比
	 */
	protected int[] hp;

	protected long[] lastingTime;

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

	public BuffTemplate_JiaXue(){
		setName(Translate.text_3280);
		setDescription(Translate.text_3280);
	}
	
	public Buff createBuff(int level) {
		Buff_JiaXue buff = new Buff_JiaXue();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		fixDesc(level, buff, 0);
		return buff;
	}

	public void fixDesc(int level, Buff_JiaXue buff, int fix) {
		StringBuffer sb = new StringBuffer();
		String skEn = "";
		if(fix>0){
			skEn = "(+"+fix+")";
		}
		if(hp != null && hp.length > level && lastingTime != null && lastingTime.length > level){
			sb.append(Translate.translateString(Translate.每多少秒增加血百分比, new String[][]{{Translate.COUNT_1,((double)lastingTime[level]/1000)+""},{Translate.COUNT_2,((double)hp[level]/10)+skEn}}));
		}
		buff.setDescription(sb.toString());
	}

}
