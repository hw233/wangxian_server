package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 加蓝
 * 
 * 
 *
 */
public class BuffTemplate_JiaLan extends BuffTemplate{

	/**
	 * 各个级别抽取的百分比
	 */
	protected int mpRecoverExtend[];
	
	public int[] getMpRecoverExtend() {
		return mpRecoverExtend;
	}

	public void setMpRecoverExtend(int[] mpRecoverExtend) {
		this.mpRecoverExtend = mpRecoverExtend;
	}

	public BuffTemplate_JiaLan(){
		setName(Translate.text_3224);
		setDescription(Translate.text_3225);
		mpRecoverExtend = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_JiaLan buff = new Buff_JiaLan();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(mpRecoverExtend != null && mpRecoverExtend.length > level){
			buff.setDescription(Translate.text_3226+mpRecoverExtend[level]+Translate.text_1469);
		}else{
			buff.setDescription(Translate.text_3227);
		}
		return buff;
	}

}
