package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_GongQiangFangYu extends BuffTemplate{

	/**
	 * 外功攻击强度
	 */
	protected int[] physicalDamage;

	/**
	 * 内法攻击强度
	 */
	protected int[] magicDamage;

	/**
	 * 物理防御
	 */
	protected int[] phyDefence;

	/**
	 * 法术防御
	 */
	protected int[] magicDefence;
	
	public int[] getPhysicalDamage() {
		return physicalDamage;
	}

	public void setPhysicalDamage(int[] physicalDamage) {
		this.physicalDamage = physicalDamage;
	}

	public int[] getMagicDamage() {
		return magicDamage;
	}

	public void setMagicDamage(int[] magicDamage) {
		this.magicDamage = magicDamage;
	}

	public int[] getPhyDefence() {
		return phyDefence;
	}

	public void setPhyDefence(int[] phyDefence) {
		this.phyDefence = phyDefence;
	}

	public int[] getMagicDefence() {
		return magicDefence;
	}

	public void setMagicDefence(int[] magicDefence) {
		this.magicDefence = magicDefence;
	}

	public BuffTemplate_GongQiangFangYu(){
		setName(Translate.text_3183);
		setDescription(Translate.text_3243);
		physicalDamage = new int[]{1,3,5,7,9,11,13,15,17,19};
		magicDamage = new int[]{1,3,5,7,9,11,13,15,17,19};
		phyDefence = new int[]{1,3,5,7,9,11,13,15,17,19};
		magicDefence = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_GongQiangFangYu buff = new Buff_GongQiangFangYu();
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
			int value = physicalDamage[level];
			if(value > 0){
				if(sb.length() > 0)
					sb.append("，");
				String str = Translate.translateString(Translate.增加物理攻击描述,new String[][]{{Translate.COUNT_1,value+""}});
				sb.append(str);
			}else if(value < 0){
				if(sb.length() > 0)
					sb.append("，");
				String str = Translate.translateString(Translate.降低物理攻击描述,new String[][]{{Translate.COUNT_1,(-value)+""}});
				sb.append(str);
			}
			buff.physicalDamage = value;
		}
		if(phyDefence != null && phyDefence.length > level){
			int value = phyDefence[level];
			if(value > 0){
				if(sb.length() > 0)
					sb.append("，");
				String str = Translate.translateString(Translate.增加物理防御描述,new String[][]{{Translate.COUNT_1,value+""}});
				sb.append(str);
			}else if(value < 0){
				if(sb.length() > 0)
					sb.append("，");
				String str = Translate.translateString(Translate.降低物理防御描述,new String[][]{{Translate.COUNT_1,(-value)+""}});
				sb.append(str);
			}
			buff.phyDefence = value;
		}
		if(magicDamage != null && magicDamage.length > level){
			int value = magicDamage[level];
			if(value > 0){
				if(sb.length() > 0)
					sb.append("，");
				String str = Translate.translateString(Translate.增加法术攻击描述,new String[][]{{Translate.COUNT_1,value+""}});
				sb.append(str);
			}else if(value < 0){
				if(sb.length() > 0)
					sb.append("，");
				String str = Translate.translateString(Translate.降低法术攻击描述,new String[][]{{Translate.COUNT_1,(-value)+""}});
				sb.append(str);
			}
			buff.magicDamage = value;
		}
		if(magicDefence != null && magicDefence.length > level){
			int value = magicDefence[level];
			if(value > 0){
				if(sb.length() > 0)
					sb.append("，");
				String str = Translate.translateString(Translate.增加法术防御描述,new String[][]{{Translate.COUNT_1,value+""}});
				sb.append(str);
			}else if(value < 0){
				if(sb.length() > 0)
					sb.append("，");
				String str = Translate.translateString(Translate.降低法术防御描述,new String[][]{{Translate.COUNT_1,(-value)+""}});
				sb.append(str);
			}
			buff.magicDefence = value;
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
