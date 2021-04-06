package com.fy.engineserver.sprite.npc;

import java.util.Map;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;

/**
 * 采集物NPC
 * 
 * 
 */
@SuppressWarnings("serial")
public interface Collectionable {

	public boolean canCollection(Player player);

	public int getOncePickupNum();

	public int getOncePickupColor();

	public int getTotalNum();

	public void collection(int num);

	public int getLeftNum();

	public Game getBelongs();

	public void onBeCollection(Player player);

	// 采集读条时间
	public long getReadTimebarTime();

	// 是否在可采集状态
	public boolean isInService();

	/**
	 * 采集者和采集时间的记录
	 * <platyerId,lastPickupTime>
	 * @return
	 */
	public Map<Long, Long> getReapers();

	public String getArticleName();

	/**
	 * 读条时间
	 * @return
	 */
	public long getCollectionBarTime();

	public void setCollectionBarTime(long collectionBarTime);
}
