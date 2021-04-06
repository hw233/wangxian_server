package com.fy.engineserver.datasource.buff;

/**
 * 单纯靠名字去区分的buff，如囚禁，禁言
 * 
 * 
 *
 */
public class BuffTemplate_State extends BuffTemplate{

	public BuffTemplate_State(){
		setName("");
		setDescription("");
	}
	
	public Buff createBuff(int level) {
		Buff_State buff = new Buff_State();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		buff.setDescription("");
		return buff;
	}

}
