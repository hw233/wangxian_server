package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 抽血
 * 
 * 
 *
 */
public class BuffTemplate_JiaXie extends BuffTemplate{

	/**
	 * 每半秒钟恢复血
	 */
	protected int hpRecoverExtend[];
	
	public int[] getHpRecoverExtend() {
		return hpRecoverExtend;
	}

	public void setHpRecoverExtend(int[] hpRecoverExtend) {
		this.hpRecoverExtend = hpRecoverExtend;
	}

	public BuffTemplate_JiaXie(){
		setName(Translate.text_3280);
		setDescription(Translate.text_3281);
		hpRecoverExtend = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_JiaXie buff = new Buff_JiaXie();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(hpRecoverExtend != null && hpRecoverExtend.length > level){
			buff.setDescription(Translate.text_3282+hpRecoverExtend[level]+Translate.text_1469);
		}else{
			buff.setDescription(Translate.text_3283);
		}
		return buff;
	}

}
