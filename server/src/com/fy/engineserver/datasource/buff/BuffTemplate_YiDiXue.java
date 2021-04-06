package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_YiDiXue extends BuffTemplate{

	public BuffTemplate_YiDiXue(){
		setName(Translate.text_3384);
		setDescription(Translate.text_3384);
	}
	
	public Buff createBuff(int level) {
		Buff_YiDiXue buff = new Buff_YiDiXue();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		buff.setDescription(Translate.text_3385);
		return buff;
	}

}
