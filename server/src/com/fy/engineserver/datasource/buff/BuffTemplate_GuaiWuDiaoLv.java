package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_GuaiWuDiaoLv extends BuffTemplate{
	protected int flopratePercent[];
	
	public BuffTemplate_GuaiWuDiaoLv(){
		setName(Translate.text_3190);
		setDescription(Translate.text_3191);
		flopratePercent = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_GuaiWuDiaoLv buff = new Buff_GuaiWuDiaoLv();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(flopratePercent != null && flopratePercent.length > level){
			if(flopratePercent[level] > 0){
				if(flopratePercent[level] == 100){
					buff.setDescription(Translate.text_3192);
				}else{
					buff.setDescription(Translate.text_3193);
				}
				
			}else if(flopratePercent[level] < 0){
				buff.setDescription(Translate.text_3194);
			}
		}else{
			buff.setDescription(Translate.text_3195);
		}
		return buff;
	}

	public int[] getFlopratePercent() {
		return flopratePercent;
	}

	public void setFlopratePercent(int[] flopratePercent) {
		this.flopratePercent = flopratePercent;
	}

}
