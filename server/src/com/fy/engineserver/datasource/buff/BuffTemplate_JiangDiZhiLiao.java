package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;
/**
 * 根据大师技能人，地，天不同阶段降低不同比例的治疗效果
 * 
 *
 */
public class BuffTemplate_JiangDiZhiLiao extends BuffTemplate{

	protected int[] treatment;
	
	public BuffTemplate_JiangDiZhiLiao(){
		setName(Translate.降低治疗);
		setDescription(Translate.降低治疗效果);
		treatment = new int[]{100};
	}
	
	@Override
	public Buff createBuff(int level) {
		Buff_JiangDiZhiLiao buff = new Buff_JiangDiZhiLiao();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(treatment!=null){
			if(level>=treatment.length){
				level = treatment.length - 1;
			}
			buff.setDescription(Translate.降低治疗效果+treatment[level]+"%");
		}else{
			buff.setDescription(Translate.降低治疗效果);
		}
		return buff;
	}

	public int[] getTreatment() {
		return treatment;
	}

	public void setTreatment(int[] treatment) {
		this.treatment = treatment;
	}

}
