package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_4shuxingGongFangJianFang extends BuffTemplate{
	protected int fireAttackB[];
	protected int fireDefenceB[];
	protected int fireIgnoreDefenceB[];
	protected int blizzardAttackB[];
	protected int blizzardDefenceB[];
	protected int blizzardIgnoreDefenceB[];
	protected int windAttackB[];
	protected int windDefenceB[];
	protected int windIgnoreDefenceB[];
	protected int thunderAttackB[];
	protected int thunderDefenceB[];
	protected int thunderIgnoreDefenceB[];
	
	public int[] getFireAttackB() {
		return fireAttackB;
	}

	public void setFireAttackB(int[] fireAttackB) {
		this.fireAttackB = fireAttackB;
	}

	public int[] getFireDefenceB() {
		return fireDefenceB;
	}

	public void setFireDefenceB(int[] fireDefenceB) {
		this.fireDefenceB = fireDefenceB;
	}

	public int[] getFireIgnoreDefenceB() {
		return fireIgnoreDefenceB;
	}

	public void setFireIgnoreDefenceB(int[] fireIgnoreDefenceB) {
		this.fireIgnoreDefenceB = fireIgnoreDefenceB;
	}

	public int[] getBlizzardAttackB() {
		return blizzardAttackB;
	}

	public void setBlizzardAttackB(int[] blizzardAttackB) {
		this.blizzardAttackB = blizzardAttackB;
	}

	public int[] getBlizzardDefenceB() {
		return blizzardDefenceB;
	}

	public void setBlizzardDefenceB(int[] blizzardDefenceB) {
		this.blizzardDefenceB = blizzardDefenceB;
	}

	public int[] getBlizzardIgnoreDefenceB() {
		return blizzardIgnoreDefenceB;
	}

	public void setBlizzardIgnoreDefenceB(int[] blizzardIgnoreDefenceB) {
		this.blizzardIgnoreDefenceB = blizzardIgnoreDefenceB;
	}

	public int[] getWindAttackB() {
		return windAttackB;
	}

	public void setWindAttackB(int[] windAttackB) {
		this.windAttackB = windAttackB;
	}

	public int[] getWindDefenceB() {
		return windDefenceB;
	}

	public void setWindDefenceB(int[] windDefenceB) {
		this.windDefenceB = windDefenceB;
	}

	public int[] getWindIgnoreDefenceB() {
		return windIgnoreDefenceB;
	}

	public void setWindIgnoreDefenceB(int[] windIgnoreDefenceB) {
		this.windIgnoreDefenceB = windIgnoreDefenceB;
	}

	public int[] getThunderAttackB() {
		return thunderAttackB;
	}

	public void setThunderAttackB(int[] thunderAttackB) {
		this.thunderAttackB = thunderAttackB;
	}

	public int[] getThunderDefenceB() {
		return thunderDefenceB;
	}

	public void setThunderDefenceB(int[] thunderDefenceB) {
		this.thunderDefenceB = thunderDefenceB;
	}

	public int[] getThunderIgnoreDefenceB() {
		return thunderIgnoreDefenceB;
	}

	public void setThunderIgnoreDefenceB(int[] thunderIgnoreDefenceB) {
		this.thunderIgnoreDefenceB = thunderIgnoreDefenceB;
	}

	public BuffTemplate_4shuxingGongFangJianFang(){
		setName(Translate.text_2367);
		setDescription(Translate.text_3388);
		fireAttackB = new int[]{1,3,5,7,9,11,13,15,17,19};
		fireDefenceB = new int[]{1,3,5,7,9,11,13,15,17,19};
		fireIgnoreDefenceB = new int[]{1,3,5,7,9,11,13,15,17,19};
		blizzardAttackB = new int[]{1,3,5,7,9,11,13,15,17,19};
		blizzardDefenceB = new int[]{1,3,5,7,9,11,13,15,17,19};
		blizzardIgnoreDefenceB = new int[]{1,3,5,7,9,11,13,15,17,19};
		windAttackB = new int[]{1,3,5,7,9,11,13,15,17,19};
		windDefenceB = new int[]{1,3,5,7,9,11,13,15,17,19};
		windIgnoreDefenceB = new int[]{1,3,5,7,9,11,13,15,17,19};
		thunderAttackB = new int[]{1,3,5,7,9,11,13,15,17,19};
		thunderDefenceB = new int[]{1,3,5,7,9,11,13,15,17,19};
		thunderIgnoreDefenceB = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_4shuxingGongFangJianFang buff = new Buff_4shuxingGongFangJianFang();
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
		if(fireAttackB != null && fireAttackB.length > level){
			if(fireAttackB[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.translateString(Translate.火属性攻击增加描述, new String[][]{{Translate.COUNT_1,fireAttackB[level]+""}}));
			}
			else if(fireAttackB[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.translateString(Translate.火属性攻击减少描述, new String[][]{{Translate.COUNT_1,-fireAttackB[level]+""}}));
			}
		}
		if(fireDefenceB != null && fireDefenceB.length > level){
			if(fireDefenceB[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.translateString(Translate.火属性防御增加描述, new String[][]{{Translate.COUNT_1,fireDefenceB[level]+""}}));
			}
			else if(fireDefenceB[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.translateString(Translate.火属性防御减少描述, new String[][]{{Translate.COUNT_1,-fireDefenceB[level]+""}}));
			}
		}
		if(fireIgnoreDefenceB != null && fireIgnoreDefenceB.length > level){
			if(fireIgnoreDefenceB[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.translateString(Translate.减少对方火属性防御描述, new String[][]{{Translate.COUNT_1,fireIgnoreDefenceB[level]+""}}));
			}
			else if(fireIgnoreDefenceB[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.translateString(Translate.增加对方火属性防御描述, new String[][]{{Translate.COUNT_1,-fireIgnoreDefenceB[level]+""}}));
			}
		}

		if(blizzardAttackB != null && blizzardAttackB.length > level){
			if(blizzardAttackB[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.translateString(Translate.冰属性攻击增加描述, new String[][]{{Translate.COUNT_1,blizzardAttackB[level]+""}}));
			}
			else if(blizzardAttackB[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.translateString(Translate.冰属性攻击减少描述, new String[][]{{Translate.COUNT_1,-blizzardAttackB[level]+""}}));
			}
		}
		if(blizzardDefenceB != null && blizzardDefenceB.length > level){
			if(blizzardDefenceB[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.translateString(Translate.冰属性防御增加描述, new String[][]{{Translate.COUNT_1,blizzardDefenceB[level]+""}}));
			}
			else if(blizzardDefenceB[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.translateString(Translate.冰属性防御减少描述, new String[][]{{Translate.COUNT_1,-blizzardDefenceB[level]+""}}));
			}
		}
		if(blizzardIgnoreDefenceB != null && blizzardIgnoreDefenceB.length > level){
			if(blizzardIgnoreDefenceB[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.translateString(Translate.减少对方冰属性防御描述, new String[][]{{Translate.COUNT_1,blizzardIgnoreDefenceB[level]+""}}));
			}
			else if(blizzardIgnoreDefenceB[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.translateString(Translate.增加对方冰属性防御描述, new String[][]{{Translate.COUNT_1,-blizzardIgnoreDefenceB[level]+""}}));
			}
		}
		
		if(windAttackB != null && windAttackB.length > level){
			if(windAttackB[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.translateString(Translate.风属性攻击增加描述, new String[][]{{Translate.COUNT_1,windAttackB[level]+""}}));
			}
			else if(windAttackB[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.translateString(Translate.风属性攻击减少描述, new String[][]{{Translate.COUNT_1,-windAttackB[level]+""}}));
			}
		}
		if(windDefenceB != null && windDefenceB.length > level){
			if(windDefenceB[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.translateString(Translate.风属性防御增加描述, new String[][]{{Translate.COUNT_1,windDefenceB[level]+""}}));
			}
			else if(windDefenceB[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.translateString(Translate.风属性防御减少描述, new String[][]{{Translate.COUNT_1,-windDefenceB[level]+""}}));
			}
		}
		if(windIgnoreDefenceB != null && windIgnoreDefenceB.length > level){
			if(windIgnoreDefenceB[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.translateString(Translate.减少对方风属性防御描述, new String[][]{{Translate.COUNT_1,windIgnoreDefenceB[level]+""}}));
			}
			else if(windIgnoreDefenceB[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.translateString(Translate.增加对方风属性防御描述, new String[][]{{Translate.COUNT_1,-windIgnoreDefenceB[level]+""}}));
			}
		}
		
		if(thunderAttackB != null && thunderAttackB.length > level){
			if(thunderAttackB[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.translateString(Translate.雷属性攻击增加描述, new String[][]{{Translate.COUNT_1,thunderAttackB[level]+""}}));
			}
			else if(thunderAttackB[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.translateString(Translate.雷属性攻击减少描述, new String[][]{{Translate.COUNT_1,-thunderAttackB[level]+""}}));
			}
		}
		if(thunderDefenceB != null && thunderDefenceB.length > level){
			if(thunderDefenceB[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.translateString(Translate.雷属性防御增加描述, new String[][]{{Translate.COUNT_1,thunderDefenceB[level]+""}}));
			}
			else if(thunderDefenceB[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.translateString(Translate.雷属性防御减少描述, new String[][]{{Translate.COUNT_1,-thunderDefenceB[level]+""}}));
			}
		}
		if(thunderIgnoreDefenceB != null && thunderIgnoreDefenceB.length > level){
			if(thunderIgnoreDefenceB[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.translateString(Translate.减少对方雷属性防御描述, new String[][]{{Translate.COUNT_1,thunderIgnoreDefenceB[level]+""}}));
			}
			else if(thunderIgnoreDefenceB[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.translateString(Translate.增加对方雷属性防御描述, new String[][]{{Translate.COUNT_1,-thunderIgnoreDefenceB[level]+""}}));
			}
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
