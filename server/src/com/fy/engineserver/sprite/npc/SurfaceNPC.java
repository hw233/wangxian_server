package com.fy.engineserver.sprite.npc;

/**
 * 地表NPC
 * 不显示名字.
 * 不能被选中
 * 不遮挡地表外的东西
 * 
 * 
 */
public interface SurfaceNPC {

	int getGrade();

	void setGrade(int grade);

	public String[] getAvatas();

	public void setAvatas(String[] avatas);

	public int getShowIndex();

	public void setShowIndex(int showIndex);

}
