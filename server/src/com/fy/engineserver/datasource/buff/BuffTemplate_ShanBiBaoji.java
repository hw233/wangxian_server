package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 闪避百分比暴击百分比
 * 
 * 
 *
 */
public class BuffTemplate_ShanBiBaoji extends BuffTemplate{

	/**
	 * 闪避率的百分比
	 */
	protected int[] dodge;

	public int[] getDodge() {
		return dodge;
	}

	public void setDodge(int[] dodge) {
		this.dodge = dodge;
	}

	/**
	 * 各个等级增加暴击的百分比，10表示增加10%
	 */
	protected int criticalHit[];
	
	public int[] getCriticalHit() {
		return criticalHit;
	}

	public void setCriticalHit(int[] criticalHit) {
		this.criticalHit = criticalHit;
	}

	public BuffTemplate_ShanBiBaoji(){
		setName(Translate.增加闪避暴击百分比);
		setDescription(Translate.增加闪避暴击百分比);
		dodge = new int[]{1,3,5,7,9,11,13,15,17,19};
		criticalHit = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_ShanBiBaoji buff = new Buff_ShanBiBaoji();
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
		if(dodge != null && dodge.length > level){
			if(dodge[level] > 0)
				sb.append(Translate.translateString(Translate.增加闪避百分比详细, new String[][]{{Translate.STRING_1,dodge[level]+"%"}}));
			else if(dodge[level] < 0)
				sb.append(Translate.translateString(Translate.降低闪避百分比详细, new String[][]{{Translate.STRING_1,-dodge[level]+"%"}}));
		}
		if(criticalHit != null && criticalHit.length > level){
			if(criticalHit[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.translateString(Translate.增加暴击百分比详细, new String[][]{{Translate.STRING_1,criticalHit[level]+"%"}}));
			}else if(criticalHit[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.translateString(Translate.降低暴击百分比详细, new String[][]{{Translate.STRING_1,-criticalHit[level]+"%"}}));
			}
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
