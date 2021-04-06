package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_JingYan extends BuffTemplate{
	protected int expPercent[];
	
	public BuffTemplate_JingYan(){
		setName(Translate.text_3306);
		setDescription(Translate.text_3307);
		expPercent = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_JingYan buff = new Buff_JingYan();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(expPercent != null && expPercent.length > level){
			if(expPercent[level] > 0)
				buff.setDescription(Translate.text_3306+expPercent[level]+"%");
			else if(expPercent[level] < 0)
				buff.setDescription(Translate.text_3308+(-expPercent[level])+"%");
		}else{
			buff.setDescription(Translate.text_3306);
		}
		return buff;
	}

	public int[] getExpPercent() {
		return expPercent;
	}

	public void setExpPercent(int[] expPercent) {
		this.expPercent = expPercent;
	}

}
