package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_ShunJiaXueZhi extends BuffTemplate{

	/**
	 * 加血
	 */
	protected int[] hpB;

	public int[] getHpB() {
		return hpB;
	}

	public void setHpB(int[] hpB) {
		this.hpB = hpB;
	}

	public BuffTemplate_ShunJiaXueZhi(){
		setName(Translate.text_3353);
		setDescription(Translate.text_3353);
	}
	
	public Buff createBuff(int level) {
		Buff_ShunJiaXueZhi buff = new Buff_ShunJiaXueZhi();
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
		if(hpB != null && hpB.length > level){
			sb.append(Translate.text_3350+hpB[level]+Translate.text_3275);
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
