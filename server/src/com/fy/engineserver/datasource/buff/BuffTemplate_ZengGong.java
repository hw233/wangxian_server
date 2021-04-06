package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_ZengGong extends BuffTemplate{

	/**
	 * 外功攻击强度百分比
	 */
	protected int[] physicalDamage;
	

	/**
	 * 内法攻击强度百分比
	 */
	protected int[] spellDamage;
	
	public int[] getPhysicalDamage() {
		return physicalDamage;
	}

	public void setPhysicalDamage(int[] physicalDamage) {
		this.physicalDamage = physicalDamage;
	}

	public int[] getSpellDamage() {
		return spellDamage;
	}

	public void setSpellDamage(int[] spellDamage) {
		this.spellDamage = spellDamage;
	}

	public BuffTemplate_ZengGong(){
		setName(Translate.text_2471);
		setDescription(Translate.text_3386);
		physicalDamage = new int[]{1,3,5,7,9,11,13,15,17,19};
		spellDamage = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_ZhengGong buff = new Buff_ZhengGong();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		StringBuffer sb = new StringBuffer();
		if(physicalDamage != null && physicalDamage.length > level){
			if(physicalDamage[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				
				sb.append(Translate.text_3245+physicalDamage[level]+"%");
			}else if(physicalDamage[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				
				sb.append(Translate.text_3244+(-physicalDamage[level])+"%");
			}
		}
		if(spellDamage != null && spellDamage.length > level){
			if(spellDamage[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				
				sb.append(Translate.text_3247+spellDamage[level]+"%");
			}else if(spellDamage[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				
				sb.append(Translate.text_3246+(-spellDamage[level])+"%");
			}
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
