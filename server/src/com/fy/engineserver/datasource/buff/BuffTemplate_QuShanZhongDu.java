package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_QuShanZhongDu extends BuffTemplate{

	public BuffTemplate_QuShanZhongDu(){
		setName(Translate.text_3339);
		setDescription(Translate.text_3339);
	}
	
	public Buff createBuff(int level) {
		Buff_QuShanZhongDu buff = new Buff_QuShanZhongDu();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setDescription(Translate.text_3339);
		buff.setIconId(iconId);
		return buff;
	}

}
