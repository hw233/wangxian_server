package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_didangshanghai extends BuffTemplate{
	/** 抵挡次数 */
	private int[] amount;
	
	public BuffTemplate_didangshanghai(){
		setAmount(new int[]{5,5,5});
	}
	
	@Override
	public Buff createBuff(int level) {
		// TODO Auto-generated method stub
		Buff_didangshanghai buff = new Buff_didangshanghai();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		String dsp =  Translate.translateString(Translate.元素精华描述, new String[][] {{Translate.STRING_1, amount[level] + ""}});
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
