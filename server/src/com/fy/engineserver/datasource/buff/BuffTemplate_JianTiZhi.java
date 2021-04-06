package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_JianTiZhi extends BuffTemplate{

	int[] constitution;

	public BuffTemplate_JianTiZhi(){
		setName(Translate.text_3271);
		setDescription(Translate.text_3272);
		setAdvantageous(true);
		constitution = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_JianTiZhi buff = new Buff_JianTiZhi();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(constitution != null && constitution.length > level){
			if(constitution[level] > 0)
				buff.setDescription(Translate.text_3271+constitution[level]+"%");
			else if(constitution[level] < 0)
				buff.setDescription(Translate.text_3273+(-constitution[level])+"%");
		}else{
			buff.setDescription(Translate.text_3271);
		}
		return buff;
	}

	public int[] getConstitution() {
		return constitution;
	}

	public void setConstitution(int[] constitution) {
		this.constitution = constitution;
	}
	
}
