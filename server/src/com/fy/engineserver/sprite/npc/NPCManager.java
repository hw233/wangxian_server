package com.fy.engineserver.sprite.npc;

public interface NPCManager {

	public NPC createNPC(int categoryId);
	
	public NPC createNPC(NPC template);
	
	public void removeNPC(NPC s);
	
	/**
	 * 判断一个精灵是否存在
	 * 
	 * @param autoId
	 * @return
	 */
	public boolean isExists(int autoId);

	/**
	 * 通过精灵的Id获得精灵
	 */
	public NPC getNPC(long autoId);

	/**
	 * 得到所有的精灵的个数
	 * 
	 * @return
	 */
	public int getAmountOfNPCs();

	/**
	 * 分页获取精灵，当没有更多精灵时，返回长度为0的数组
	 * 
	 * @param start
	 * @param size
	 * @return
	 */
	public NPC[] getNPCsByPage(int start, int size);

}
