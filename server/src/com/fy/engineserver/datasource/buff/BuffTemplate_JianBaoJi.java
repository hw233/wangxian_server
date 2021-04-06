package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 降低暴击
 * 
 * 
 *
 */
public class BuffTemplate_JianBaoJi extends BuffTemplate{

	/**
	 * 各个等级降低暴击的百分比，10表示降低10%
	 */
	protected int criticalHit[];
	
	public int[] getCriticalHit() {
		return criticalHit;
	}

	public void setCriticalHit(int[] criticalHit) {
		this.criticalHit = criticalHit;
	}

	public BuffTemplate_JianBaoJi(){
		setName(Translate.text_3146);
		setDescription(Translate.text_3239);
		criticalHit = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff_JianBaoJi createBuff(int level) {
		Buff_JianBaoJi buff = new Buff_JianBaoJi();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(criticalHit != null && criticalHit.length > level){
			if(criticalHit[level] > 0)
				buff.setDescription(Translate.text_3142+criticalHit[level]+"%");
			else if(criticalHit[level] < 0)
				buff.setDescription(Translate.text_3141+(-criticalHit[level])+"%");
		}else{
			buff.setDescription(Translate.text_3142);
		}
		return buff;
	}

}
