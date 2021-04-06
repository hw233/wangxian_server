package com.fy.engineserver.sprite;

import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.PropsEntity;

/**
 * 任务事件监听器，玩家都实现此接口
 * 当系统发生这些事件的时候后，会调用当事的玩家。
 * 
 * 
 * 任务事件包括：
 * 杀死一个怪
 * 杀死一个玩家
 * 获得一个物品
 * 与一个人对话
 * 使用一个道具
 * 到达一个地方
 * 
 * 
 */
public interface TaskEventListener {

	/**
	 * 杀死一个怪
	 * @param s
	 */
	public void killOneSprite(Sprite s);

	/**
	 * 杀死一个玩家
	 * @param p
	 */
	public void killOnePlayer(Player p);

	/**
	 * 获得一个物品，此物品必须已经放到背包中
	 * @param ae
	 */
	public void obtainOneArticle(ArticleEntity ae);

	/**
	 * 减少一个物品，此物品从背包中移走
	 * @param ae
	 */
	public void discardOneArticle(ArticleEntity ae);

	/**
	 * 与某个npc对话
	 * @param npcName
	 */
	public void talkWithOneNPC(String npcName);

	/**
	 * 到达某个地方
	 * @param placeName
	 */
	public void reachOnePlace(String placeName);

	/**
	 * 使用某个道具
	 * @param pe
	 */
	public void useOneProps(PropsEntity pe, Player player);

	/**
	 * 成功护送一个NPC到达目的地
	 * @param npcName
	 */
	public void convoyOneNPC(String npcName,int npcColorGrade);

	/**
	 * 完成其他任务
	 * @param taskName
	 */
	public void deliverTask(String taskName);
}
