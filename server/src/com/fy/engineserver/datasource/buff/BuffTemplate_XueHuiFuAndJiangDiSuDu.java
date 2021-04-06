package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_XueHuiFuAndJiangDiSuDu extends BuffTemplate{

	/**
	 * 回复血量百分比
	 */
	protected int[] hpC;

	/**
	 * 降低速度百分比
	 */
	protected int[] speedC;

	/**
	 * 时间间隔
	 */
	protected int[] lastingTime;
	
	public int[] getHpC() {
		return hpC;
	}

	public void setHpC(int[] hpC) {
		this.hpC = hpC;
	}

	public int[] getSpeedC() {
		return speedC;
	}

	public void setSpeedC(int[] speedC) {
		this.speedC = speedC;
	}

	public int[] getLastingTime() {
		return lastingTime;
	}

	public void setLastingTime(int[] lastingTime) {
		this.lastingTime = lastingTime;
	}

	public BuffTemplate_XueHuiFuAndJiangDiSuDu(){
		setName(Translate.回复血量且降低速度);
		setDescription(Translate.回复血量且降低速度);
		hpC = new int[]{1,3,5,7,9,11,13,15,17,19};
		speedC = new int[]{1,3,5,7,9,11,13,15,17,19};
		lastingTime = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_XueHuiFuAndJiangDiSuDu buff = new Buff_XueHuiFuAndJiangDiSuDu();
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
		if(hpC != null && hpC.length > level){
			if(hpC[level] > 0){
				sb.append(Translate.translateString(Translate.回复血量且降低速度详细描述,new String[][]{{Translate.COUNT_1,lastingTime[level]*1f/1000+""},{Translate.COUNT_2,hpC[level]+"%"},{Translate.STRING_1,speedC[level]+"%"}}));
			}
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
