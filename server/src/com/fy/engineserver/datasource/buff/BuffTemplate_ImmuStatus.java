package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;


/**
 * 免疫各种状态  
 * 
 *
 */
public class BuffTemplate_ImmuStatus extends BuffTemplate{
	/**  1免疫减速  2免疫定身  3免疫晕眩  4免疫封魔  定身  眩晕  减速、抓人*/
	private byte[] immuType;

	public BuffTemplate_ImmuStatus(){
		setName("免疫各种状态");
		setDescription("免疫各种状态");
	}
	
	public Buff createBuff(int level) {
		Buff_ImmuStatus buff = new Buff_ImmuStatus();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		String des = "";
		if (level < immuType.length && immuType[level] <= Translate.免疫buff描述.length) {
			des = Translate.免疫buff描述[immuType[level]-1];
		}
		buff.setDescription(des);
		return buff;
	}

	public byte[] getImmuType() {
		return immuType;
	}

	public void setImmuType(byte[] immuType) {
		this.immuType = immuType;
	}

}
