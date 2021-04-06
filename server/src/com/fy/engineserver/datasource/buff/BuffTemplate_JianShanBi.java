package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 降低闪避
 * 
 * 
 *
 */
public class BuffTemplate_JianShanBi extends BuffTemplate{

	/**
	 * 降低闪避率的百分比
	 */
	protected int[] dodge;

	public int[] getDodge() {
		return dodge;
	}

	public void setDodge(int[] dodge) {
		this.dodge = dodge;
	}

	public BuffTemplate_JianShanBi(){
		setName(Translate.text_3154);
		setDescription(Translate.text_3263);
		dodge = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_JianShanBi buff = new Buff_JianShanBi();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(dodge != null && dodge.length > level){
			if(dodge[level] > 0)
				buff.setDescription(Translate.text_3264+dodge[level]+"%");
			else if(dodge[level] < 0)
				buff.setDescription(Translate.text_3265+(-dodge[level])+"%");
		}else{
			buff.setDescription(Translate.text_3264);
		}
		return buff;
	}

}
