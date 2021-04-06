package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 定身流血（百分比）
 * 
 *
 */
public class BuffTemplate_DingShengLiuXue extends BuffTemplate{
	/**
	 * 加血千分比
	 */
	protected int[] hpD;

	protected long[] lastingTime;

	public int[] getHpD() {
		return hpD;
	}

	public void setHpD(int[] hpD) {
		this.hpD = hpD;
	}

	public long[] getLastingTime() {
		return lastingTime;
	}

	public void setLastingTime(long[] lastingTime) {
		this.lastingTime = lastingTime;
	}

	public BuffTemplate_DingShengLiuXue(){
		setName(Translate.text_3170);
		setDescription(Translate.text_3170);
	}
	
	public Buff createBuff(int level) {
		Buff_DingShengLiuXue buff = new Buff_DingShengLiuXue();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		StringBuffer sb = new StringBuffer();
		sb.append(Translate.定身+"，");
		if(hpD != null && hpD.length > level && lastingTime != null && lastingTime.length > level){
			sb.append(Translate.translateString(Translate.每多少秒减少血百分比, new String[][]{{Translate.COUNT_1,((double)lastingTime[level]/1000)+""},{Translate.COUNT_2,((double)hpD[level]/10)+""}}));
		}
		buff.setDescription(sb.toString());
		buff.setIconId(iconId);
		return buff;
	}

}
