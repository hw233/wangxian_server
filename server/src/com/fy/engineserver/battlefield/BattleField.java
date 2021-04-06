package com.fy.engineserver.battlefield;

import java.util.Hashtable;

import com.fy.engineserver.battlefield.concrete.BattleFieldStatData;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.message.PLAYER_REVIVED_REQ;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;

/**
 * 战场接口
 * 
 * 1. 每个战场都有一个独立的线程，每个战场对应一个Game
 * 2. 
 *
 */
public interface BattleField {

	public static final int STATE_OPEN = 0;
	public static final int STATE_FIGHTING = 1;
	public static final int STATE_STOPFIGHTING = 2;
	public static final int STATE_CLOSE = 3;
	
	/**
	 * 设置战场各方的名称
	 * 
	 * @param names
	 * @return
	 */
	public void setBattleFieldSideNames(String names[]);
	
	public String[] getBattleFieldSideNames();
	
	/**
	 * 返回战场的战况描述
	 * @return
	 */
	public String getBattleFieldSituation();
	
	/**
	 * 获得战场中所有可能出现的怪或者NPC的类型
	 * @return
	 */
	public byte[] getAllSpriteTypeOnGame();
	
	/**
	 * 获得一个随机出生点
	 * @param side
	 * @return
	 */
	public Point getRandomBornPoint(int side);
	
	/**
	 * 战场的状态
	 * @return
	 */
	public int getState();
	
	/**
	 * 战场唯一的ID
	 * @return
	 */
	public String getId();
	
	/**
	 * 获取战场的名字
	 * @return
	 */
	public String getName();
	
	/**
	 * 战场开始的时间
	 * @return
	 */
	public long getStartTime();
	
	/**
	 * 战场结束的时间
	 * @return
	 */
	public long getEndTime();
	
	/**
	 * 战场开始战斗的时间，此事件必须介于开始时间和结束时间之间
	 * @return
	 */
	public long getStartFightingTime();
	
	
	/**
	 * 玩家进入的最小等级
	 * @return
	 */
	public int getMinPlayerLevel();
	
	/**
	 * 玩家进入的最大等级
	 * @return
	 */
	public int getMaxPlayerLevel();
	
	
	/**
	 * 战场是否已经开启
	 * @return
	 */
	public boolean isOpen();
	
	
	public static final int BATTLE_FIGHTING_TYPE_对战 = 0;
	
	public static final int BATTLE_FIGHTING_TYPE_混战 = 1;
	
	/**
	 * 获得战场战斗的模式，对战还是混战
	 * @return
	 */
	public int getBattleFightingType();
	
	//中立方
	public static final int BATTLE_SIDE_C = 0;
	
	//战场中的A方，还是B方，A方在左下角
	public static final int BATTLE_SIDE_A = 1;
	
	//战场中的A方，还是B方，B方在右上角
	public static final int BATTLE_SIDE_B = 2;

	/**
	 * 获胜方  BATTLE_SIDE_C标识平局。
	 * 此变量只在对战模式下有用
	 * @return
	 */
	public int getWinnerSide();
	
	/**
	 * 得到某一个方玩家的数组，此数值不会为null
	 * 
	 * @param side
	 * @return
	 */
	public Player[] getPlayersBySide(int side);
	
	public Player[] getPlayers();
	
	/**
	 * 一方至少有多少人，
	 * 如果是混战模式，此变量表示战争中最少需要多少人
	 * @return
	 */
	public int getMinPlayerNumForStartOnOneSide();
	
	/**
	 * 当战场的某一方少于最少人数时，战场多长时间后关闭
	 * 
	 * @return
	 */
	public long getLastingTimeForNotEnoughPlayers();
	
	
	/**
	 * 一方对多有多少人，
	 * 如果是混战模式，此变量表示战争中最多可以多少人
	 * 
	 * @return
	 */
	public int getMaxPlayerNumOnOneSide();
	
	/**
	 * 战场对应的游戏场景
	 * @return
	 */
	public Game getGame();
	
	public void setGame(Game game);
	
	/**
	 * 初始化战场
	 */
	public void init();
	
	/**
	 * 战场心跳函数
	 */
	public void heartbeat();
	
	
	/**
	 * 玩家死亡，需要读秒，之后复活
	 * 处理玩家死亡
	 * @param player
	 */
	public void playerDead(Player player);
	
	/**
	 * 收到玩家复活指令
	 * 
	 * @param player
	 * @param req
	 */
	public void playerRevived(Player player,PLAYER_REVIVED_REQ req);
	
	
	
	
	//////////////////////////////////////////////////////////////////////////
	//以下是一些通知信息
	
	public Hashtable<Long,BattleFieldStatData> getStatDataMap();
	
	/**
	 * 玩家进入战场
	 * @param player
	 * @param mapName 玩家进入战场时所在的地图
	 * @param x 玩家进入战场是的位置
	 * @param y 玩家进入战场是的位置
	 */
	public void notifyPlayerEnter(Player player);
	
	/**
	 * 玩家从战场离开
	 * @param player
	 */
	public void notifyPlayerLeave(Player player);
	
	
	/**
	 * 战场意外结束
	 */
	public void notifyBattleFieldEndCauseSystemExit();
	
	/**
	 * 通知伤害量
	 * @param causter
	 * @param target
	 * @param damage
	 */
	public void notifyCauseDamage(Fighter causter,Fighter target,int damage);
	
	/**
	 * 实际承受的伤害量
	 * @param causter
	 * @param damage
	 */
	public void notifyCauseDamageOfReal(Fighter causter,int damage);
	
	/**
	 * 通知治疗量
	 * @param causter
	 * @param target
	 * @param damage
	 */
	public void notifyEnhenceHp(Fighter causter,Fighter target,int hp);
	
	/**
	 * 通知谁杀死了谁
	 * @param killer
	 * @param killed
	 */
	public void notifyKilling(Fighter killer,Fighter killed);
	
}
