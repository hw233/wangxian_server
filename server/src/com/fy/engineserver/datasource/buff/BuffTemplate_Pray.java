package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_Pray extends BuffTemplate {
	/** 1人物经验 2元气 3工资4神兽碎片5技能碎片6宠物经验 */
	private byte[] prayType;

	public BuffTemplate_Pray() {
		setName(Translate.祈福buff名);
		setDescription(Translate.祈福buff描述);
	}

	public Buff createBuff(int level) {
		Buff_Pray buff = new Buff_Pray();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		String des = Translate.buff描述;
//		if (level < prayType.length && prayType[level] <= Translate.宝箱祈福描述.length) {
//			des = Translate.宝箱祈福描述[prayType[level]];
//		}
		buff.setDescription(des);
		return buff;
	}

	public byte[] getPrayType() {
		return prayType;
	}

	public void setPrayType(byte[] prayType) {
		this.prayType = prayType;
	}

}
