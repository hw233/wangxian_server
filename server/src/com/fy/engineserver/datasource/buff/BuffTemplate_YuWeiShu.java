package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 御卫术
 * 国王拉人技能，每天最多10次，同时获得N秒免疫负面状态buff，国王立即回满血，并且持续回血N秒
 * 
 * @author Administrator
 *
 */
public class BuffTemplate_YuWeiShu extends BuffTemplate {

	public BuffTemplate_YuWeiShu(){
		setName(Translate.御卫术);
		setDescription(Translate.御卫术);
	}
	
	/* (non-Javadoc)
	 * @see com.fy.engineserver.datasource.buff.BuffTemplate#createBuff(int)
	 */
	public Buff createBuff(int level) {
		Buff_YuWeiShu buff = new Buff_YuWeiShu();
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
