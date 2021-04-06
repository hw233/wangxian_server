package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_ShunJiaLan extends BuffTemplate{

	/**
	 * 加蓝
	 */
	protected int[] mp;

	public int[] getMp() {
		return mp;
	}

	public void setMp(int[] mp) {
		this.mp = mp;
	}

	public BuffTemplate_ShunJiaLan(){
		setName(Translate.text_3349);
		setDescription(Translate.text_3349);
	}
	
	public Buff createBuff(int level) {
		Buff_ShunJiaLan buff = new Buff_ShunJiaLan();
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
		if(mp != null && mp.length > level){
			sb.append(Translate.text_3350+mp[level]+Translate.text_3233);
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
