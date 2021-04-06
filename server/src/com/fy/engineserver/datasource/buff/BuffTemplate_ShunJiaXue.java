package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_ShunJiaXue extends BuffTemplate{

	/**
	 * 加血
	 */
	protected int[] hp;

	public int[] getHp() {
		return hp;
	}

	public void setHp(int[] hp) {
		this.hp = hp;
	}

	public BuffTemplate_ShunJiaXue(){
		setName(Translate.text_3353);
		setDescription(Translate.text_3353);
	}
	
	public Buff createBuff(int level) {
		Buff_ShunJiaXue buff = new Buff_ShunJiaXue();
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
		if(hp != null && hp.length > level){
			sb.append(Translate.text_3350+hp[level]+Translate.text_3287);
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
