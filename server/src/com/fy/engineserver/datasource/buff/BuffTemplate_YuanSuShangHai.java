package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;
/**
 * 
 * 
 *
 */
public class BuffTemplate_YuanSuShangHai extends BuffTemplate{
	/**
	 * 风、火、雷、冰四中属性增加的强度（百分比）
	 */
	protected int[] fengDamage;
	protected int[] huoDamage;
	protected int[] leiDamage;
	protected int[] bingDamage;
	private String[] improveType;
	
	public BuffTemplate_YuanSuShangHai(){
		setName(Translate.增强元素buff);
		setDescription(Translate.增强元素buff描述);
		fengDamage = new int[]{10,20,30,40,50,60,70,80,90,100};
		huoDamage = new int[]{10,20,30,40,50,60,70,80,90,100};
		leiDamage = new int[]{10,20,30,40,50,60,70,80,90,100};
		bingDamage = new int[]{10,20,30,40,50,60,70,80,90,100};
		setImproveType(new String[]{"1111"});				//1风 2火 3雷 4冰
	}
	
	@Override
	public Buff createBuff(int level) {
		// TODO Auto-generated method stub
		Buff_YuanSuShangHai buff = new Buff_YuanSuShangHai();
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
				String str = Translate.translateString(Translate.增强风元素伤害,new String[][]{{Translate.STRING_1,fengDamage[level]+""}});
				sb.append(str);
			} else if(i == Buff_YuanSuShangHai.HUO){
				String str = Translate.translateString(Translate.增强火元素伤害,new String[][]{{Translate.STRING_1,huoDamage[level]+""}});
				sb.append("," + str);
			} else if(i == Buff_YuanSuShangHai.LEI){
				String str = Translate.translateString(Translate.增强雷元素伤害,new String[][]{{Translate.STRING_1,leiDamage[level]+""}});
				sb.append("," + str);
			} else if(i == Buff_YuanSuShangHai.BING){
				String str = Translate.translateString(Translate.增强冰元素伤害,new String[][]{{Translate.STRING_1,bingDamage[level]+""}});
				sb.append("," + str);
			} else {
				System.out.println("类型配多了！！");
			}
		}
		if(flag) {
			buff.setDescription(Translate.增加所有元素伤害);
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

	public String[] getImproveType() {
		return improveType;
	}

	public void setImproveType(String[] improveType) {
		this.improveType = improveType;
	}

}
