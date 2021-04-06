package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_GuaiWuMoneyDiaoLv extends BuffTemplate{
	protected int flopMoneyPercent[];
	
	public BuffTemplate_GuaiWuMoneyDiaoLv(){
		setName(Translate.text_3196);
		setDescription(Translate.text_3197);
		flopMoneyPercent = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_GuaiWuMoneyDiaoLv buff = new Buff_GuaiWuMoneyDiaoLv();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(flopMoneyPercent != null && flopMoneyPercent.length > level){
			if(flopMoneyPercent[level] > 0)
				buff.setDescription(Translate.text_3196+flopMoneyPercent[level]+"%");
			else if(flopMoneyPercent[level] < 0)
				buff.setDescription(Translate.text_3198+(-flopMoneyPercent[level])+"%");
		}else{
			buff.setDescription(Translate.text_3196);
		}
		return buff;
	}

	public int[] getFlopMoneyPercent() {
		return flopMoneyPercent;
	}

	public void setFlopMoneyPercent(int[] flopMoneyPercent) {
		this.flopMoneyPercent = flopMoneyPercent;
	}

}
