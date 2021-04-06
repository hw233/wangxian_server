package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;
/**
 * 抵挡并吸收伤害护盾
 * @author Administrator
 *
 */
public class BuffTemplate_RecoverShield extends BuffTemplate{
	/** 护盾吸收伤害比例（触发buff时的血上限计算） */
	private int[] amount;
	
	public BuffTemplate_RecoverShield(){
		setAmount(new int[]{5,5,5});
	}
	
	@Override
	public Buff createBuff(int level) {
		// TODO Auto-generated method stub
		Buff_RecoverShield buff = new Buff_RecoverShield();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		String dsp =  String.format(Translate.吸收伤害并转换成生命值, amount[level]);
		buff.setDescription(dsp);
		return buff;
	}

	public int[] getAmount() {
		return amount;
	}

	public void setAmount(int[] amount) {
		this.amount = amount;
	}

}
