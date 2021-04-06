package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;


/**
 * 家族喝酒经验加成 
 * 
 *
 */
public class BuffTemplate_JiazuHejiu extends BuffTemplate{
	/**  千分比  */
	private int[] immuType;

	public BuffTemplate_JiazuHejiu(){
		setName("家族喝酒经验");
		setDescription("家族喝酒经验");
		immuType = new int[]{10,35,100,40,50,60,70,80,90,100};
	}
	
	public Buff createBuff(int level) {
		Buff_ExtraJiazuHejiu buff = new Buff_ExtraJiazuHejiu();
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
		if (level < getImmuType().length) {
			des = String.format(Translate.家族喝酒经验加成, (getImmuType()[level] / 10) + "%");
		}
		buff.setDescription(des);
		return buff;
	}

	public int[] getImmuType() {
		return immuType;
	}

	public void setImmuType(int[] immuType) {
		this.immuType = immuType;
	}


}
