package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 抽血
 * 
 * 
 *
 */
public class BuffTemplate_JiaXieLan extends BuffTemplate{

	/**
	 * 每半秒钟恢复血多少点
	 */
	protected int hpRecoverExtend[];
	
	/**
	 * 每半秒钟恢复蓝多少点
	 */
	protected int mpRecoverExtend[];
	
	public int[] getHpRecoverExtend() {
		return hpRecoverExtend;
	}

	public void setHpRecoverExtend(int[] hpRecoverExtend) {
		this.hpRecoverExtend = hpRecoverExtend;
	}

	public int[] getMpRecoverExtend() {
		return mpRecoverExtend;
	}

	public void setMpRecoverExtend(int[] mpRecoverExtend) {
		this.mpRecoverExtend = mpRecoverExtend;
	}

	public BuffTemplate_JiaXieLan(){
		setName(Translate.text_3285);
		setDescription(Translate.text_3286);
		hpRecoverExtend = new int[]{1,3,5,7,9,11,13,15,17,19};
		mpRecoverExtend = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_JiaXieLan buff = new Buff_JiaXieLan();
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
		if(hpRecoverExtend != null && hpRecoverExtend.length > level && hpRecoverExtend[level] > 0){
			sb.append(Translate.text_3282+hpRecoverExtend[level]+Translate.text_1469);
		}
		if(mpRecoverExtend != null && mpRecoverExtend.length > level && mpRecoverExtend[level] > 0){
			if(sb.length() > 0)
				sb.append("，");
			sb.append(Translate.text_3226+mpRecoverExtend[level]+Translate.text_1469);
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
