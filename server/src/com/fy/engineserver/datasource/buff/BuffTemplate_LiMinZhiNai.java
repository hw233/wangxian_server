package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 智力/敏捷/耐力/力量
 * 
 * @author Administrator
 *
 */
public class BuffTemplate_LiMinZhiNai extends BuffTemplate {
	int[] strengthB;
	/**
	 * 敏捷点
	 */
	protected int[] dexterityB;
	protected int spellpowerB[];
	int[] constitutionB;
	int[] speedC;

	public int[] getStrengthB() {
		return strengthB;
	}

	public void setStrengthB(int[] strengthB) {
		this.strengthB = strengthB;
	}

	public int[] getDexterityB() {
		return dexterityB;
	}

	public void setDexterityB(int[] dexterityB) {
		this.dexterityB = dexterityB;
	}

	public int[] getSpellpowerB() {
		return spellpowerB;
	}

	public void setSpellpowerB(int[] spellpowerB) {
		this.spellpowerB = spellpowerB;
	}

	public int[] getConstitutionB() {
		return constitutionB;
	}

	public void setConstitutionB(int[] constitutionB) {
		this.constitutionB = constitutionB;
	}

	public int[] getSpeedC() {
		return speedC;
	}

	public void setSpeedC(int[] speedC) {
		this.speedC = speedC;
	}

	public BuffTemplate_LiMinZhiNai(){
		setName(Translate.text_3315);
		setDescription(Translate.text_3316);
		setAdvantageous(true);
		strengthB = new int[]{1,3,5,7,9,11,13,15,17,19};
		dexterityB = new int[]{1,3,5,7,9,11,13,15,17,19};
		spellpowerB = new int[]{1,3,5,7,9,11,13,15,17,19};
		constitutionB = new int[]{1,3,5,7,9,11,13,15,17,19};
		speedC = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	/* (non-Javadoc)
	 * @see com.fy.engineserver.datasource.buff.BuffTemplate#createBuff(int)
	 */
	public Buff createBuff(int level) {
		Buff_LiMinZhiNai buff = new Buff_LiMinZhiNai();
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
		if(strengthB != null && strengthB.length > level){
			if(strengthB[level] > 0)
				sb.append(Translate.text_3255+strengthB[level]+Translate.text_1469);
			else if(strengthB[level] < 0)
				sb.append(Translate.text_3313+(-strengthB[level])+Translate.text_1469);
		}
		if(constitutionB != null && constitutionB.length > level){
			if(constitutionB[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.text_3273+constitutionB[level]+Translate.text_1469);
			}else if(constitutionB[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.text_3271+(-constitutionB[level])+Translate.text_1469);
			}
		}
		if(spellpowerB != null && spellpowerB.length > level){
			if(spellpowerB[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.text_3279+spellpowerB[level]+Translate.text_1469);
			}else if(spellpowerB[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.text_3278+(-spellpowerB[level])+Translate.text_1469);
			}
		}
		if(dexterityB != null && dexterityB.length > level && dexterityB[level] > 0){
			if(dexterityB[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.text_3262+dexterityB[level]+Translate.text_1469);
			}else if(dexterityB[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.text_3260+(-dexterityB[level])+Translate.text_1469);
			}
		}
		if(speedC != null && speedC.length > level && speedC[level] > 0){
			if(speedC[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.text_3317+speedC[level]+"%");
			}else if(speedC[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.text_3318+(-speedC[level])+"%");
			}
		}
		buff.setDescription(sb.toString());
		return buff;
	}
}
