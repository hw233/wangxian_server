package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;



/**
 * 攻击附带一次与等级相关的属性攻击及百分比属性减抗
 * @author 
 *
 */
public class BuffTemplate_OnceAttributeAttack extends BuffTemplate{
	/** 增加额外属性攻的系数   最后增加数值为：施法者等级*attributeRate */
	private int[] attributeRate;
	/** 对应属性减抗比例(百分比) */
	private int[] ignoreRate;
	/** 属性类型  0:风   1:火  2:雷  3:冰 */
	private byte[] attributeType;

	public BuffTemplate_OnceAttributeAttack(){
		setName("攻击附带一次与等级相关的属性攻击及百分比属性减抗");
		setDescription("攻击附带一次与等级相关的属性攻击及百分比属性减抗");
	}
	
	public Buff createBuff(int level) {
		Buff_OnceAttributeAttack buff = new Buff_OnceAttributeAttack();
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
		if (level < attributeType.length && attributeType[level] <= Translate.增加额外属性攻.length) {
			des = Translate.增加额外属性攻[attributeType[level]];
		}
		buff.setDescription(des);
		return buff;
	}

	public int[] getAttributeRate() {
		return attributeRate;
	}

	public void setAttributeRate(int[] attributeRate) {
		this.attributeRate = attributeRate;
	}

	public int[] getIgnoreRate() {
		return ignoreRate;
	}

	public void setIgnoreRate(int[] ignoreRate) {
		this.ignoreRate = ignoreRate;
	}

	public byte[] getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(byte[] attributeType) {
		this.attributeType = attributeType;
	}

}
