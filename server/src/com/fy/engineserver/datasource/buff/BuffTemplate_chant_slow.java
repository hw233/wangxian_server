package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 吟唱减速BUFF
 * @author Administrator
 *         2014-5-16
 * 
 */
public class BuffTemplate_chant_slow extends BuffTemplate {

	/** 吟唱减速百分比 10表示吟唱延长10% */
	protected int[] chant_slow_rate;

	public BuffTemplate_chant_slow() {
		setName(Translate.text_3139);
		setDescription(Translate.text_3140);
		chant_slow_rate = new int[] { 10, 20, 30, 40, 50, 60 };
	}

	public int[] getChant_slow_rate() {
		return chant_slow_rate;
	}

	public void setChant_slow_rate(int[] chant_slow_rate) {
		this.chant_slow_rate = chant_slow_rate;
	}

	@Override
	public Buff createBuff(int level) {
		Buff_chant_slow buff = new Buff_chant_slow();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if (chant_slow_rate != null && chant_slow_rate.length > level) {
			if (chant_slow_rate[level] > 0) {
				buff.setDescription(Translate.延长偷菜时间 + chant_slow_rate[level] * 1f / 10 + "%");
			} else if (chant_slow_rate[level] < 0) {
				buff.setDescription(Translate.减少偷菜时间 + (chant_slow_rate[level] * 1f / 10) + "%");
			}
		} else {
			buff.setDescription(Translate.延长偷菜时间);
		}
		return buff;
	}

}
