package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_GongQiangFangYu_Percent extends BuffTemplate{

	/**
	 * 外功攻击强度
	 */
	protected int[] physicalDamage_percent;

	/**
	 * 内法攻击强度
	 */
	protected int[] magicDamage_percent;

	/**
	 * 物理防御
	 */
	protected int[] phyDefence_percent;

	/**
	 * 法术防御
	 */
	protected int[] magicDefence_percent;
	
	public int[] getPhysicalDamage_percent() {
		return physicalDamage_percent;
	}

	public void setPhysicalDamage_percent(int[] physicalDamage_percent) {
		this.physicalDamage_percent = physicalDamage_percent;
	}

	public int[] getMagicDamage_percent() {
		return magicDamage_percent;
	}

	public void setMagicDamage_percent(int[] magicDamage_percent) {
		this.magicDamage_percent = magicDamage_percent;
	}

	public int[] getPhyDefence_percent() {
		return phyDefence_percent;
	}

	public void setPhyDefence_percent(int[] phyDefence_percent) {
		this.phyDefence_percent = phyDefence_percent;
	}

	public int[] getMagicDefence_percent() {
		return magicDefence_percent;
	}

	public void setMagicDefence_percent(int[] magicDefence_percent) {
		this.magicDefence_percent = magicDefence_percent;
	}

	public BuffTemplate_GongQiangFangYu_Percent(){
		setName(Translate.text_3183);
		setDescription(Translate.text_3243);
		physicalDamage_percent = new int[]{1,3,5,7,9,11,13,15,17,19};
		magicDamage_percent = new int[]{1,3,5,7,9,11,13,15,17,19};
		phyDefence_percent = new int[]{1,3,5,7,9,11,13,15,17,19};
		magicDefence_percent = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_GongQiangFangYu_Percent buff = new Buff_GongQiangFangYu_Percent();
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
		if(physicalDamage_percent != null && physicalDamage_percent.length > level){
			if(physicalDamage_percent[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				String str = Translate.translateString(Translate.增加物理攻击百分比描述,new String[][]{{Translate.COUNT_1,physicalDamage_percent[level]+""}});
				sb.append(str);
			}else if(physicalDamage_percent[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				String str = Translate.translateString(Translate.降低物理攻击百分比描述,new String[][]{{Translate.COUNT_1,(-physicalDamage_percent[level])+""}});
				sb.append(str);
			}
		}
		if(phyDefence_percent != null && phyDefence_percent.length > level){
			if(phyDefence_percent[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				String str = Translate.translateString(Translate.增加物理防御百分比描述,new String[][]{{Translate.COUNT_1,phyDefence_percent[level]+""}});
				sb.append(str);
			}else if(phyDefence_percent[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				String str = Translate.translateString(Translate.降低物理防御百分比描述,new String[][]{{Translate.COUNT_1,(-phyDefence_percent[level])+""}});
				sb.append(str);
			}
		}
		if(magicDamage_percent != null && magicDamage_percent.length > level){
			if(magicDamage_percent[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				String str = Translate.translateString(Translate.增加法术攻击百分比描述,new String[][]{{Translate.COUNT_1,magicDamage_percent[level]+""}});
				sb.append(str);
			}else if(magicDamage_percent[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				String str = Translate.translateString(Translate.降低法术攻击百分比描述,new String[][]{{Translate.COUNT_1,(-magicDamage_percent[level])+""}});
				sb.append(str);
			}
		}
		if(magicDefence_percent != null && magicDefence_percent.length > level){
			if(magicDefence_percent[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				String str = Translate.translateString(Translate.增加法术防御百分比描述,new String[][]{{Translate.COUNT_1,magicDefence_percent[level]+""}});
				sb.append(str);
			}else if(magicDefence_percent[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				String str = Translate.translateString(Translate.降低法术防御百分比描述,new String[][]{{Translate.COUNT_1,(-magicDefence_percent[level])+""}});
				sb.append(str);
			}
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
