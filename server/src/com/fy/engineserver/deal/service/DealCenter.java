package com.fy.engineserver.deal.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.deal.Deal;
import com.fy.engineserver.exception.KnapsackFullException;
import com.fy.engineserver.sprite.Player;

/**
 * 交易中心
 * 
 * 
 */
public abstract class DealCenter {

//	public static Logger logger = Logger.getLogger(DealCenter.class);
public	static Logger logger = LoggerFactory.getLogger(DealCenter.class);

	protected static DealCenter self;
	
	public static boolean 使用新日志格式 = true;

	public static DealCenter getInstance() {
		return self;
	}

	/**
	 * 创建两个玩家的交易
	 * @param playerA
	 * @param playerB
	 * @return
	 */
	public abstract Deal createDeal(Player playerA, Player playerB) throws Exception;

	/**
	 * 通过id获得deal
	 * @param id
	 * @return
	 */
	public abstract Deal getDeal(String id);

	/**
	 * 得到用户所在的交易
	 * @param player
	 * @return
	 */
	public abstract Deal getDeal(Player player);

	/**
	 * 获取两个玩家之间的交易
	 * @param playerA
	 * @param playerB
	 * @return
	 */
	public abstract Deal getDeal(Player playerA, Player playerB);

	/**
	 * 增加一个背包栏物品到交易栏
	 * @param player
	 * @param index
	 * @param entityId
	 * @param count
	 * @return
	 */
	public abstract boolean addArticle(Player player, byte pakType, int index, long entityId, int count) throws Exception;

	/**
	 * 从交易栏删物品
	 * @param player
	 * @param index
	 */
	public abstract boolean deleteArticle(Player player, int index);

	/**
	 * 修改金钱
	 * @param player
	 */
	public abstract boolean updateCoins(Player player, int coins);

	/**
	 * 玩家更改交易条件
	 * @param deal
	 * @param player
	 * @param cells
	 * @param coins
	 */
	public abstract boolean updateConditions(Deal deal, Player player, int indexes[], long entityIds[], int counts[], int coins) throws Exception;

	public abstract void lockDeal(Deal deal, Player player, boolean isLock);
	
	/**
	 * 玩家同意交易
	 * @param deal
	 * @param player
	 * @return 是否已经双方都同意并且完成交易
	 */
	public abstract boolean agreeDeal(Deal deal, Player player) throws KnapsackFullException, Exception;

	/**
	 * 玩家取消同意交易状态
	 * @param deal
	 * @param player
	 */
	public abstract void disagreeDeal(Deal deal, Player player) throws Exception;

	/**
	 * 玩家取消交易
	 * @param deal
	 * @param player
	 */
	public abstract void cancelDeal(Deal deal, Player player);

	/**
	 * 获得存在的交易
	 * @return
	 */
	public abstract Map<String, Deal> getDealMap();

	/**
	 * 获得交易总量
	 * @return
	 */
	public abstract int getTotalDeal();

	/**
	 * 达成的交易数
	 * @return
	 */
	public abstract int getAgreed();

	/**
	 * 未达成的交易数
	 * @return
	 */
	public abstract int getCanceled();
}
