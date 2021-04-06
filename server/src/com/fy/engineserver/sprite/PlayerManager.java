package com.fy.engineserver.sprite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.jiazu.Jiazu;

/**
 * 角色管理器
 * 
 * 
 * 
 */
public abstract class PlayerManager {

	public static final long 每人每天可以使用绑银 = 1000000;
	public static final int 保护最大级别 = 39;
	public static final String[] 初级活动等级礼包 = Translate.初级活动等级礼包;
	public static final String[] classlevel = Translate.classlevel;
	public static boolean 开启赐福标记 = false;
	public static final String 赐福buff = Translate.封印赐福;
	protected static PlayerManager self;
	private int abc;
	/**
	 * 三维数组
	 * 第一维职业 0通用，1修罗，2影魅，3仙心，4九黎
	 * 第二维级别
	 * 第三维属性值 顺序MHP(最大生命)，MMP(最大魔法)，AP(物理攻击强度)，AP2(法术攻击强度)，HITP(命中)，DGP(闪躲)，CHP(暴击)
	 */
	public static int[][][] 各个职业各个级别的基础属性值 = new int[5][300][7];

	/** 普通用户的体力值上限 (相对于VIP) */
	public static final int VITALITY_MAX = 360;
	/** 普通用户的每天体力值增加 (相对于VIP) */
	public static final int VITALITY_EVERYDAY_ADD = 120;
	/** 
	 * 三维数组
	 * 第一维 为职业，分别为"无门无派装备取值","修罗装备取值","影魅装备取值","仙心装备取值","九黎装备取值"
	 * 第二维 为级别
	 * 第三维 为各个属性在相应级别的理论最大数值
	 * 顺序MHP血,AP物攻,AP2法功,AC物防,AC2法防,MMP蓝,DAC破甲,HITP命中,DGP闪避,ACTP精准,CHP爆机,DCHP破爆,FAP火属性攻,IAP冰属性攻,WAP风属性攻,TAP雷属性攻,FRT火属性抗,IRT冰属性抗,WRT风属性抗,TRT雷属性抗,DFRT火属性减抗,DIRT冰属性减抗
	 * ,DWRT风属性减抗,DTRT雷属性减抗
	 */
	public static int[][][] 各个职业各个级别的所有战斗属性理论最大数值 = new int[5][300][23];
	
	public static Map<Integer, Integer> combatFNums = new HashMap<Integer, Integer>();

	public static PlayerManager getInstance() {
		return self;
	}
	
	public abstract int getPlayerCountByCountry(int countryType);
	
	/**
	 * 根据传入的时间拿到所有lastRequestTime大于这个时间参数的角色id
	 * @param lastRequestTime
	 * @return
	 */
	public abstract long[] getPlayerIdsByLastingRequestTime(long lastRequestTime);
	
	/**
	 * 根据传入的国家和时间段拿到所有lastRequestTime在这个时间段之中的角色id
	 * @param lastRequestTime
	 * @return
	 */
	public abstract long getPlayerNumByLastingRequestTime(int country,long fromLastRequestTime, long toLastRequestTime);

	public abstract boolean isOnline(long id);

	public abstract boolean isOnline(String name);

	public abstract boolean isUserOnline(String username);

	/**
	 * 判断一个角色是否存在
	 * @param name
	 * @return
	 */
	public abstract boolean isExists(String name);

	/**
	 * 判断一个角色是否存在
	 * @param id
	 * @return
	 */
	public abstract boolean isExists(long id);

	/**
	 * 通过昵称获得角色
	 * @param name
	 * @return
	 */
	public abstract Player getPlayer(String name) throws Exception;

	/**
	 * 通过角色的Id获得角色
	 */
	public abstract Player getPlayer(long id) throws Exception;

	/**
	 * 通过角色的Id获得角色
	 */
	public abstract Player getPlayerInCache(long id);
	/**
	 * 通过用户获得用户创建过的，还处于正常状态的角色
	 * @param username
	 * @return
	 */
	public abstract Player[] getPlayerByUser(String username) throws Exception;

	/**
	 * 获得对应的帐号上，在线的玩家
	 * @param username
	 * @return
	 * @throws Exception
	 */
	public abstract Player[] getOnlinePlayerByUser(String username) throws Exception;


	/**
	 * 获得在线的家族成员
	 * @param jiazu
	 * @return
	 */
	public abstract Player[] getOnlinePlayerByJiazu(Jiazu jiazu);
	
	/**
	 * 获得在线的国家成员
	 * @param countryId
	 * @return
	 */
	public abstract Player[] getOnlinePlayerByCountry(byte countryId);

	/**
	 * 创建一个新的角色
	 * @param name
	 * @param career
	 * @return
	 */
	public abstract Player createPlayer(String username, String name, byte race, byte sex, byte country, byte politicalCamp, int career) throws Exception;

	/**
	 * 合服时创建一个空角色
	 * @param username
	 * @param name
	 * @param sex
	 * @param politicalCamp
	 * @param career
	 * @return
	 * @throws Exception
	 */
	public abstract Player createPurePlayeForHefu(String username, String name, byte sex, byte politicalCamp, int career) throws Exception;

	/**
	 * 保存一个新角色
	 * @param player
	 * @return
	 */
	public abstract Player savePlayer(Player player) throws Exception;

	/**
	 * 得到所有的角色的个数
	 * @return
	 */
	public abstract int getAmountOfPlayers() throws Exception;

	/**
	 * 得到用户的角色的个数
	 * @return
	 */
	public abstract int getAmountOfPlayers(String username) throws Exception;

	/**
	 * 分页获取角色，当没有更多角色时，返回长度为0的数组
	 * @param start
	 * @param size
	 * @return
	 */
	public abstract Player[] getPlayersByPage(int start, int size) throws Exception;

	/**
	 * 获取在线的用户
	 * @return
	 */
	public abstract Player[] getOnlinePlayers();

	/**
	 * 获得在线但不在游戏中的玩家
	 * @return
	 */
	public abstract Player[] getOnlinePlayersNotInGame();

	/**
	 * 获取在缓存中的玩家
	 * @return
	 */
	public abstract Player[] getCachedPlayers();

	/**
	 * 更新这个角色
	 * @param player
	 */
	public abstract void updatePlayer(Player player) throws Exception;

	/**
	 * 用户修改角色信息
	 * @param player
	 * @param name
	 * @param sex
	 * @param career
	 */
	public abstract void userModifyPlayer(Player player, String name, byte sex, byte career) throws Exception;

	/**
	 * 删除这个角色
	 * @param name
	 */
	public abstract void removePlayer(String name) throws Exception;

	/**
	 * 通过角色名和用户得到一个玩家
	 * @param name
	 * @param username
	 * @return
	 * @throws Exception
	 */
	public abstract Player getPlayer(String name, String username) throws Exception;

	/**
	 * 角色上线
	 * @param player
	 * @return
	 */
	public abstract void enterServerNotify(Player player);

	/**
	 * 角色下线
	 * @param player
	 * @return
	 */
	public abstract void leaveServerNotify(Player player);

	/**
	 * 
	 * @param player
	 * @param friend
	 */
	public abstract void playerAddFriend(Player player, Player friend);

	/**
	 * 踢玩家
	 * @param playerId
	 */
	public abstract void kickPlayer(long playerId);

	/**
	 * 获得大约等于此级别的角色个数
	 * @param level
	 * @return
	 */
	public abstract int getUpperLevelPlayerCount(int level);

	/**
	 * 获得大于等于此等级的角色
	 * @param start
	 * @param length
	 * @param level
	 */
	public abstract Player[] getUpperLevelPlayer(int start, int length, int level) throws Exception;


	/**
	 * 修改玩家的名字
	 * @param oldPlayerName
	 * @param newName
	 */
	public abstract void updatePlayerName(Player player, String newName);

	/**
	 * 修改玩家的用户名
	 * @param player
	 * @param newUsername
	 */
	public abstract void updatePlayerUsername(Player player, String newUsername);

	/**
	 * 得到在cache中所有的用户
	 * @return
	 */
	public abstract Object[] getAllPlayerInCache();

	/**
	 * 得到一个宗派所有的在线玩家
	 * @param zongpaiId
	 * @return
	 */
	public abstract Player[] getOnlineInZongpai(long zongpaiId);

	public abstract int[] getOnlineNumAsEveryMinute(String day);

	public abstract String[] getMemAsEveryFiveSec(String day);
	/**
	 * 获取简单player信息
	 * @param ids
	 * @return
	 */
	public abstract List<SimplePlayer4Load> getSimplePlayer4Load(long ids);
	
	public abstract List<SimplePlayer4Load> getSimplePlayer4Load(String name);

}
