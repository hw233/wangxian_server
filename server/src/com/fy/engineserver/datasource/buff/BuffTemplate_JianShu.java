package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_JianShu extends BuffTemplate{

	protected int speed[];
	
	public int[] getSpeed() {
		return speed;
	}

	public void setSpeed(int[] speed) {
		this.speed = speed;
	}

	public BuffTemplate_JianShu(){
		setName(Translate.text_3267);
		setDescription(Translate.text_3268);
		speed = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_JianShu buff = new Buff_JianShu();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(speed != null && speed.length > level){
			buff.setDescription(Translate.text_3269+speed[level]+"%");
		}else{
			buff.setDescription(Translate.text_3270);
		}
		return buff;
	}

}
