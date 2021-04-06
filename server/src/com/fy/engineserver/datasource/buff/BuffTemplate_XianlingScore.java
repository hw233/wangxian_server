package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_XianlingScore extends BuffTemplate {

	private int rate[]; // 积分加成（基数100）

	public BuffTemplate_XianlingScore() {
		setName("");
		setDescription("");
	}

	@Override
	public Buff createBuff(int level) {
		Buff_XianlingScore buff = new Buff_XianlingScore();
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
		// if (level < prayType.length && prayType[level] <= Translate.宝箱祈福描述.length) {
		// des = Translate.宝箱祈福描述[prayType[level]];
		// }
//		buff.setDescription(des);
		return buff;
	}

	public int[] getRate() {
		return rate;
	}

	public void setRate(int[] rate) {
		this.rate = rate;
	}

}
