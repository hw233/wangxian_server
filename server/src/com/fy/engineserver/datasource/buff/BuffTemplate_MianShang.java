package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_MianShang extends BuffTemplate{
	/**
	 * 风、火、雷、冰、魔法、物理防御增加比率
	 */
	protected int[] fengDamage;
	protected int[] huoDamage;
	protected int[] leiDamage;
	protected int[] bingDamage;
	protected int[] phyDamage;
	protected int[] magDamage;
	private String[] improveType;
	
	public BuffTemplate_MianShang(){
		setName(Translate.增防buff);
		setDescription(Translate.增防buff描述);
		fengDamage = new int[]{100};
		huoDamage = new int[]{100};
		leiDamage = new int[]{100};
		bingDamage = new int[]{100};
		phyDamage = new int[]{100};
		magDamage = new int[]{100};
		setImproveType(new String[]{"111111"});				//风、火、雷、冰、物理、魔法
	}
	
	@Override
	public Buff createBuff(int level) {
		// TODO Auto-generated method stub
		Buff_MianShang buff = new Buff_MianShang();
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
		char[] temp = getImproveType()[level].trim().toCharArray();
		boolean flag = true;
		for(int i=0; i<temp.length; i++){			//0代表没有 1代表有   四位分别代表风火雷冰
			if(!"1".equals(String.valueOf(temp[i]))){
				flag = false;
				continue;
			}
			if(i == Buff_YuanSuShangHai.FENG){
				String str = Translate.translateString(Translate.增强风元素防御,new String[][]{{Translate.STRING_1,fengDamage[level]+""}});
				sb.append(str);
			} else if(i == Buff_YuanSuShangHai.HUO){
				String str = Translate.translateString(Translate.增强火元素防御,new String[][]{{Translate.STRING_1,huoDamage[level]+""}});
				sb.append("," + str);
			} else if(i == Buff_YuanSuShangHai.LEI){
				String str = Translate.translateString(Translate.增强雷元素防御,new String[][]{{Translate.STRING_1,leiDamage[level]+""}});
				sb.append("," + str);
			} else if(i == Buff_YuanSuShangHai.BING){
				String str = Translate.translateString(Translate.增强冰元素防御,new String[][]{{Translate.STRING_1,bingDamage[level]+""}});
				sb.append("," + str);
			} else if(i == Buff_MianShang.WULI) {
				String str = Translate.translateString(Translate.增强物理防御,new String[][]{{Translate.STRING_1,bingDamage[level]+""}});
				sb.append("," + str);
			} else if (i == Buff_MianShang.FASHU) {
				String str = Translate.translateString(Translate.增强法术防御,new String[][]{{Translate.STRING_1,bingDamage[level]+""}});
				sb.append("," + str);
			} else {
				System.out.println("类型配多了！！");
			}
		}
		if(flag) {
			buff.setDescription(Translate.translateString(Translate.增加元素防御百分比,new String[][]{{Translate.STRING_1,bingDamage[level]+""}}));
		} else {
			buff.setDescription(sb.toString());
		}
		return buff;
	}

	public int[] getFengDamage() {
		return fengDamage;
	}

	public void setFengDamage(int[] fengDamage) {
		this.fengDamage = fengDamage;
	}

	public int[] getHuoDamage() {
		return huoDamage;
	}

	public void setHuoDamage(int[] huoDamage) {
		this.huoDamage = huoDamage;
	}

	public int[] getLeiDamage() {
		return leiDamage;
	}

	public void setLeiDamage(int[] leiDamage) {
		this.leiDamage = leiDamage;
	}

	public int[] getBingDamage() {
		return bingDamage;
	}

	public void setBingDamage(int[] bingDamage) {
		this.bingDamage = bingDamage;
	}

	public int[] getPhyDamage() {
		return phyDamage;
	}

	public void setPhyDamage(int[] phyDamage) {
		this.phyDamage = phyDamage;
	}

	public int[] getMagDamage() {
		return magDamage;
	}

	public void setMagDamage(int[] magDamage) {
		this.magDamage = magDamage;
	}

	public String[] getImproveType() {
		return improveType;
	}

	public void setImproveType(String[] improveType) {
		this.improveType = improveType;
	}

}
