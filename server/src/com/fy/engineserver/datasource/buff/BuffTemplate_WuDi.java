package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 无敌Buff
 * 
 *
 */
public class BuffTemplate_WuDi extends BuffTemplate{


	public BuffTemplate_WuDi(){
		setName(Translate.text_3361);
		setDescription(Translate.text_3362);
	}
	
	public Buff createBuff(int level) {
		Buff_WuDi buff = new Buff_WuDi();
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
		
		sb.append(Translate.text_3363);
		buff.setDescription(sb.toString());
		return buff;
	}

}
