package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 双防攻强
 * 
 * 
 *
 */
public class BuffTemplate_ShuangFangGongQiang extends BuffTemplate{

	/**
	 * 双防的百分比
	 */
	protected int[] shuangFang;

	/**
	 * 攻强的百分比，10表示增加10%
	 */
	protected int gongqiang[];

	public int[] getShuangFang() {
		return shuangFang;
	}

	public void setShuangFang(int[] shuangFang) {
		this.shuangFang = shuangFang;
	}

	public int[] getGongqiang() {
		return gongqiang;
	}

	public void setGongqiang(int[] gongqiang) {
		this.gongqiang = gongqiang;
	}

	public BuffTemplate_ShuangFangGongQiang(){
		setName(Translate.增加双防攻强);
		setDescription(Translate.增加双防攻强);
		shuangFang = new int[]{1,3,5,7,9,11,13,15,17,19};
		gongqiang = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_ShuangFangGongQiang buff = new Buff_ShuangFangGongQiang();
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
		if(shuangFang != null && shuangFang.length > level){
			if(shuangFang[level] > 0)
				sb.append(Translate.translateString(Translate.增加双防百分比详细, new String[][]{{Translate.STRING_1,shuangFang[level]+"%"}}));
			else if(shuangFang[level] < 0)
				sb.append(Translate.translateString(Translate.降低双防百分比详细, new String[][]{{Translate.STRING_1,-shuangFang[level]+"%"}}));
		}
		if(gongqiang != null && gongqiang.length > level){
			if(gongqiang[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.translateString(Translate.增加攻强百分比详细, new String[][]{{Translate.STRING_1,gongqiang[level]+"%"}}));
			}else if(gongqiang[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.translateString(Translate.降低攻强百分比详细, new String[][]{{Translate.STRING_1,-gongqiang[level]+"%"}}));
			}
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
