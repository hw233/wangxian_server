package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 禁止使用国王技能
 * 
 * @author Administrator
 *
 */
public class BuffTemplate_JinZhiUseKingSkill extends BuffTemplate {

	public BuffTemplate_JinZhiUseKingSkill(){
		setName(Translate.禁用国王专用技能);
		setDescription(Translate.禁用国王专用技能);
	}
	
	/* (non-Javadoc)
	 * @see com.fy.engineserver.datasource.buff.BuffTemplate#createBuff(int)
	 */
	public Buff createBuff(int level) {
		Buff_JinZhiUseKingSkill buff = new Buff_JinZhiUseKingSkill();
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
		buff.setDescription(sb.toString());
		return buff;
	}
}
