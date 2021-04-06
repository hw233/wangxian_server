package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_CouHenDiYi extends BuffTemplate{


	public BuffTemplate_CouHenDiYi(){
		setName(Translate.text_3168);
		setDescription(Translate.text_3169);
	}
	
	public Buff createBuff(int level) {
		Buff_CouHenDiYi buff = new Buff_CouHenDiYi();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		buff.setDescription(Translate.text_3169);
		return buff;
	}

}
