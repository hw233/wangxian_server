package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_GuaiWuShangHai extends BuffTemplate{
	protected int[] monsterId;
	/** 限制场景有效 */
	protected String[] sceneName;
	/** 减少比率 */
	protected int[] reduceRate;
	
	public BuffTemplate_GuaiWuShangHai(){
		setName(Translate.减少怪物伤害);
		setDescription(Translate.减少怪物伤害描述);
		sceneName = new String[]{"kunhuagucheng"};
		reduceRate = new int[]{10};
		monsterId = new int[]{900002};
	}
	
	@Override
	public Buff createBuff(int level) {
		// TODO Auto-generated method stub
		Buff_GuaiWuShangHai buff = new Buff_GuaiWuShangHai();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		return buff;
	}

	public int[] getReduceRate() {
		return reduceRate;
	}

	public void setReduceRate(int[] reduceRate) {
		this.reduceRate = reduceRate;
	}

	public int[] getMonsterId() {
		return monsterId;
	}

	public void setMonsterId(int[] monsterId) {
		this.monsterId = monsterId;
	}

	public String[] getSceneName() {
		return sceneName;
	}

	public void setSceneName(String[] sceneName) {
		this.sceneName = sceneName;
	}
}
