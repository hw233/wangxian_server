package com.fy.engineserver.guozhan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.fy.engineserver.carbon.devilSquare.DevilSquareManager;
import com.fy.engineserver.chat.ChatChannelType;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageItem;
import com.fy.engineserver.chat.ChatMessageTask;
import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.country.data.Country;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.SavingFailedException;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.guozhan.exception.GuozhanAlreadyEndException;
import com.fy.engineserver.guozhan.exception.NoLeftTimeException;
import com.fy.engineserver.guozhan.exception.OperationNotPermitException;
import com.fy.engineserver.guozhan.exception.ResourceNotEnoughException;
import com.fy.engineserver.guozhan.menu.Option_Guozhan_Pull_Cancel;
import com.fy.engineserver.guozhan.menu.Option_Guozhan_Pull_OK;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CHAT_MESSAGE_REQ;
import com.fy.engineserver.message.GUOZHAN_MAP_ORDER_MAKED_REQ;
import com.fy.engineserver.message.GUOZHAN_RESULT_NEW_REQ;
import com.fy.engineserver.message.GUOZHAN_RESULT_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.newBillboard.BillboardsManager;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.npc.GuozhanNPC;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.tool.GlobalTool;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.StringUtil;

/**
 * 国战的实体
 * 
 * 
 * @version 创建时间：May 3, 2012 2:32:20 PM
 * 
 */
public class Guozhan {

	public static final int STATE_结束 = 0;
	public static final int STATE_开启 = 1;
	
	public static double 龙象加成 = 4;
	public static double 护卫加成 = 1.5;

	public static final String 黄色S = "<f color='0xf39808'>";
	public static final String 紫色S = "<f color='0xfe82e5'>";

	public static final int 国战线路[][] = new int[][] { { 0, 1, 3, 4, 5 }, { 0, 2, 3, 4, 5 } };

	/**
	 * 进攻国
	 */
	private Country attacker;

	/**
	 * 防守国
	 */
	private Country defender;

	/**
	 * 攻方初始复活点
	 */
	private GuozhanBornPoint attackerDefaultBornPoint;

	/**
	 * 攻方第一复活点
	 */
	private GuozhanBornPoint attackerLongyaguanBornPoint;

	/**
	 * 攻方第二复活点
	 */
	private GuozhanBornPoint attackerYaosaiBornPoint;

	/**
	 * 攻方王城复活点
	 */
	private GuozhanBornPoint attackerWangchengBornPoint;

	/**
	 * 守方初始复活点
	 */
	private GuozhanBornPoint defenderBornPoint;

	/**
	 * 守方败守复活点
	 */
	private GuozhanBornPoint defenderLoseBornPoint;

	/**
	 * 开始时间
	 */
	private long startTime;

	/**
	 * 结束时间
	 */
	private long endTime;

	/**
	 * 当前的状态
	 */
	private int state;

	/**
	 * 国战的结果, 0-进攻方获胜， 1-防守方获胜
	 */
	private int result;

	/**
	 * 进攻方延长次数
	 */
	private int delayedCount;

	/**
	 * 防守方治疗boss次数
	 */
	private int cureCount;

	/**
	 * 进攻方拉人次数
	 */
	private int attackerPullCount;

	/**
	 * 防守方拉人次数
	 */
	private int defenderPullCount;

	private GuozhanNPC 龙牙关边城守将 = null;

	private GuozhanNPC 昆仑要塞边城守将 = null;

	private GuozhanNPC 龙牙关边城攻将 = null;

	private GuozhanNPC 昆仑要塞边城攻将 = null;

	private GuozhanNPC 王城戌卫官 = null;

	private GuozhanNPC 王城戌攻官 = null;

	private GuozhanNPC 龙象卫长 = null;

	private GuozhanNPC 龙象释帝 = null;

	private List<FastMessage> attackerOrders;

	private List<FastMessage> defenderOrders;

	private HashMap<Long, GuozhanPlayer> attendPlayers;

	private LinkedHashMap<Integer, GuozhanMapPoint> mapPointMap;

	private int currentAttackerMapAimPoint = -1;

	private int currentDefenderMapAimPoint = -1;
	
	private long lastNotifyNPCAttackTime = 0;

	private GuozhanOrganizer organizer;

	public void init() {
		int level = 200;
		BillboardsManager bbm = BillboardsManager.getInstance();
		if(bbm != null){
			level = bbm.getMaxLevelByBillBoard();
		}
		organizer = GuozhanOrganizer.getInstance();
		Constants cons = organizer.getConstants();

		mapPointMap = new LinkedHashMap<Integer, GuozhanMapPoint>();
		// 攻方默认出生点
		GuozhanMapPoint gmp = new GuozhanMapPoint();
		gmp.setId(0);
		gmp.setMapName(cons.attackerBornMap);
		gmp.setName(Translate.国战_边境复活点);
		gmp.setOwnerType((byte) 0);
		gmp.setX(264);
		gmp.setY(362);
		gmp.setOwnerCountryId(attacker.getCountryId());
		mapPointMap.put(gmp.getId(), gmp);

		Game game = GameManager.getInstance().getGameByName((String) cons.getBianGuan(defender.getCountryId()), defender.getCountryId());
		MemoryNPCManager mnm = (MemoryNPCManager) MemoryNPCManager.getNPCManager();
		龙牙关边城守将 = (GuozhanNPC) mnm.createNPC((Integer) cons.NPC_边城守将A_data[1]);
		
		龙牙关边城守将.setLevel(level);
		mnm.设置sprite属性值(龙牙关边城守将);
		
		龙牙关边城守将.setX((Integer) cons.getBianGuanXY(defender.getCountryId())[0] );
		龙牙关边城守将.setY((Integer) cons.getBianGuanXY(defender.getCountryId())[1] );
		GuozhanOrganizer.logger.warn("[trace0] [" + (String) cons.getBianGuan(defender.getCountryId()) + "] [x:"+龙牙关边城守将.getX()+"] [y:"+龙牙关边城守将.getY()+"] [" + defender.getCountryId() + "] ["
				+ (game == null ? "NULL" : game.getGameInfo().getMapName()) + "]");
		龙牙关边城守将.setCountryId(defender.getCountryId());
		龙牙关边城守将.setEnemyCountryId(attacker.getCountryId());
		龙牙关边城守将.setName(龙牙关边城守将.getName() + "(" + defender.getName() + ")");
		game.addSprite(龙牙关边城守将);
		attackerLongyaguanBornPoint = new GuozhanBornPoint();
		attackerLongyaguanBornPoint.setBelongCountryId(defender.getCountryId());
		attackerLongyaguanBornPoint.setName(龙牙关边城守将.getName().substring(0, 龙牙关边城守将.getName().lastIndexOf("(")));
		attackerLongyaguanBornPoint.setX(龙牙关边城守将.getX());
		attackerLongyaguanBornPoint.setY(龙牙关边城守将.getY());
		attackerLongyaguanBornPoint.setMapName(game.getGameInfo().getMapName());
		attackerLongyaguanBornPoint.setMapCountry(game.country);
		gmp = new GuozhanMapPoint();
		gmp.setId(1);
		gmp.setMapName(game.getGameInfo().getMapName());
		gmp.setName(龙牙关边城守将.getName());
		gmp.setOwnerType((byte) 1);
		gmp.setX(176);
		gmp.setY(259);
		gmp.setOwnerCountryId(defender.getCountryId());
		mapPointMap.put(gmp.getId(), gmp);

		game = GameManager.getInstance().getGameByName((String) cons.getYaoSai(defender.getCountryId()), defender.getCountryId());
		昆仑要塞边城守将 = (GuozhanNPC) mnm.createNPC((Integer) cons.NPC_边城守将B_data[1]);
		
		昆仑要塞边城守将.setLevel(level);
		mnm.设置sprite属性值(昆仑要塞边城守将);
		
		昆仑要塞边城守将.setX((Integer) cons.getYaoSaiXY(defender.getCountryId())[0]);
		昆仑要塞边城守将.setY((Integer) cons.getYaoSaiXY(defender.getCountryId())[1]);
		GuozhanOrganizer.logger.warn("[trace1] [" + (String) cons.NPC_边城守将B_data[0] + "] [x:"+昆仑要塞边城守将.getX()+"] [y:"+昆仑要塞边城守将.getY()+"] [" + defender.getCountryId() + "] ["
				+ (game == null ? "NULL" : game.getGameInfo().getMapName()) + "]");
		昆仑要塞边城守将.setCountryId(defender.getCountryId());
		昆仑要塞边城守将.setEnemyCountryId(attacker.getCountryId());
		昆仑要塞边城守将.setName(昆仑要塞边城守将.getName() + "(" + defender.getName() + ")");
		game.addSprite(昆仑要塞边城守将);
		attackerYaosaiBornPoint = new GuozhanBornPoint();
		attackerYaosaiBornPoint.setBelongCountryId(defender.getCountryId());
		attackerYaosaiBornPoint.setName(昆仑要塞边城守将.getName().substring(0, 昆仑要塞边城守将.getName().lastIndexOf("(")));
		attackerYaosaiBornPoint.setX(昆仑要塞边城守将.getX());
		attackerYaosaiBornPoint.setY(昆仑要塞边城守将.getY());
		attackerYaosaiBornPoint.setMapName(game.getGameInfo().getMapName());
		attackerYaosaiBornPoint.setMapCountry(game.country);
		gmp = new GuozhanMapPoint();
		gmp.setId(2);
		gmp.setMapName(game.getGameInfo().getMapName());
		gmp.setName(昆仑要塞边城守将.getName());
		gmp.setOwnerType((byte) 1);
		gmp.setX(380);
		gmp.setY(152);
		gmp.setOwnerCountryId(defender.getCountryId());
		mapPointMap.put(gmp.getId(), gmp);

		game = GameManager.getInstance().getGameByName((String) cons.getMainString(defender.getCountryId()), defender.getCountryId());
		王城戌卫官 = (GuozhanNPC) mnm.createNPC((Integer) cons.NPC_王城戌卫官_data[1]);
		
		王城戌卫官.setLevel(level);
		mnm.设置sprite属性值(王城戌卫官);

		王城戌卫官.setX((Integer) cons.getMainXY1(defender.getCountryId())[0]);
		王城戌卫官.setY((Integer) cons.getMainXY1(defender.getCountryId())[1]);
		GuozhanOrganizer.logger.warn("[trace2] [" + (String) cons.getMainString(defender.getCountryId())+ "] [x:"+王城戌卫官.getX()+"] [y:"+王城戌卫官.getY()+"]  [" + defender.getCountryId() + "] ["
				+ (game == null ? "NULL" : game.getGameInfo().getMapName()) + "]");
		王城戌卫官.setCountryId(defender.getCountryId());
		王城戌卫官.setEnemyCountryId(attacker.getCountryId());
		王城戌卫官.setName(王城戌卫官.getName() + "(" + defender.getName() + ")");
		game.addSprite(王城戌卫官);
		attackerWangchengBornPoint = new GuozhanBornPoint();
		attackerWangchengBornPoint.setBelongCountryId(defender.getCountryId());
		attackerWangchengBornPoint.setName(王城戌卫官.getName().substring(0, 王城戌卫官.getName().lastIndexOf("(")));
		attackerWangchengBornPoint.setX(王城戌卫官.getX());
		attackerWangchengBornPoint.setY(王城戌卫官.getY());
		attackerWangchengBornPoint.setMapName(game.getGameInfo().getMapName());
		attackerWangchengBornPoint.setMapCountry(game.country);
		gmp = new GuozhanMapPoint();
		gmp.setId(3);
		gmp.setMapName(game.getGameInfo().getMapName());
		gmp.setName(王城戌卫官.getName());
		gmp.setOwnerType((byte) 1);
		gmp.setX(117);
		gmp.setY(173);
		gmp.setOwnerCountryId(defender.getCountryId());
		mapPointMap.put(gmp.getId(), gmp);

		game = GameManager.getInstance().getGameByName((String) cons.getMainString(defender.getCountryId()), defender.getCountryId());
		龙象卫长 = (GuozhanNPC) mnm.createNPC((Integer) cons.NPC_龙象卫长_data[1]);
		
		龙象卫长.setLevel(level);
		mnm.设置sprite属性值(龙象卫长);

		龙象卫长.setX((Integer) cons.getMainXY2(defender.getCountryId())[0]);
		龙象卫长.setY((Integer) cons.getMainXY2(defender.getCountryId())[1]);
		GuozhanOrganizer.logger.warn("[trace3] [" + (String) cons.getMainString(defender.getCountryId()) + "] [x:"+龙象卫长.getX()+"] [y:"+龙象卫长.getY()+"] [" + defender.getCountryId() + "] ["
				+ (game == null ? "NULL" : game.getGameInfo().getMapName()) + "]");
		龙象卫长.setCountryId(defender.getCountryId());
		龙象卫长.setEnemyCountryId(attacker.getCountryId());
		game.addSprite(龙象卫长);
		defenderBornPoint = new GuozhanBornPoint();
		defenderBornPoint.setBelongCountryId(defender.getCountryId());
		defenderBornPoint.setName(龙象卫长.getName());
		defenderBornPoint.setX(龙象卫长.getX());
		defenderBornPoint.setY(龙象卫长.getY());
		defenderBornPoint.setMapName(game.getGameInfo().getMapName());
		defenderBornPoint.setMapCountry(game.country);
		gmp = new GuozhanMapPoint();
		gmp.setId(4);
		gmp.setMapName(game.getGameInfo().getMapName());
		gmp.setName(龙象卫长.getName());
		gmp.setOwnerType((byte) 1);
		gmp.setX(139);
		gmp.setY(74);
		gmp.setLittleBoss(true);
		gmp.setOwnerCountryId(defender.getCountryId());
		mapPointMap.put(gmp.getId(), gmp);

		game = GameManager.getInstance().getGameByName((String) cons.getMainString(defender.getCountryId()), defender.getCountryId());
		GuozhanOrganizer.logger.warn("[trace4] [" + (String) cons.getMainString(defender.getCountryId()) + "] [" + defender.getCountryId() + "] ["
				+ (game == null ? "NULL" : game.getGameInfo().getMapName()) + "]");
		龙象释帝 = (GuozhanNPC) mnm.createNPC((Integer) cons.NPC_龙象释帝_data[1]);
		
		龙象释帝.setLevel(level);
		mnm.设置sprite属性值(龙象释帝);

		龙象释帝.setX((Integer) cons.getMainXY3(defender.getCountryId())[0]);
		龙象释帝.setY((Integer) cons.getMainXY3(defender.getCountryId())[1]);
		龙象释帝.setCountryId(defender.getCountryId());
		龙象释帝.setEnemyCountryId(attacker.getCountryId());
		game.addSprite(龙象释帝);
		gmp = new GuozhanMapPoint();
		gmp.setId(5);
		gmp.setMapName(game.getGameInfo().getMapName());
		gmp.setName(龙象释帝.getName());
		gmp.setOwnerType((byte) 1);
		gmp.setX(82);
		gmp.setY(63);
		gmp.setBigBoss(true);
		gmp.setOwnerCountryId(defender.getCountryId());
		mapPointMap.put(gmp.getId(), gmp);

		game = GameManager.getInstance().getGameByName((String) cons.attackerBornMap, attacker.getCountryId());
		GuozhanOrganizer.logger.warn("[trace5] [" + cons.attackerBornMap + "] [" + defender.getCountryId() + "] ["
				+ (game == null ? "NULL" : game.getGameInfo().getMapName()) + "]");
		GameInfo gi = game.gi;
		MapArea area = gi.getMapAreaByName(Translate.出生点);
		attackerDefaultBornPoint = new GuozhanBornPoint();
		attackerDefaultBornPoint.setBelongCountryId(attacker.getCountryId());
		attackerDefaultBornPoint.setName(Translate.国战_边境复活点);
		attackerDefaultBornPoint.setX(area.getX());
		attackerDefaultBornPoint.setY(area.getY());
		attackerDefaultBornPoint.setMapName(game.getGameInfo().getMapName());
		attackerDefaultBornPoint.setMapCountry(game.country);

		game = GameManager.getInstance().getGameByName(TransportData.getMainCityMap(defender.getCountryId()) , defender.getCountryId());
		GuozhanOrganizer.logger.warn("[trace6] [" + TransportData.getMainCityMap(defender.getCountryId()) + "] [" + defender.getCountryId() + "] ["
				+ (game == null ? "NULL" : game.getGameInfo().getMapName()) + "]");
		gi = game.gi;
		area = gi.getMapAreaByName(Translate.出生点);
		defenderLoseBornPoint = new GuozhanBornPoint();
		defenderLoseBornPoint.setBelongCountryId(defender.getCountryId());
		defenderLoseBornPoint.setName(Translate.国战_王城复活点);
		defenderLoseBornPoint.setX(area.getX());
		defenderLoseBornPoint.setY(area.getY());
		defenderLoseBornPoint.setMapName(game.getGameInfo().getMapName());
		defenderLoseBornPoint.setMapCountry(game.country);

		attackerOrders = new ArrayList<FastMessage>();
		defenderOrders = new ArrayList<FastMessage>();

		attendPlayers = new HashMap<Long, GuozhanPlayer>();
	}
	

	/**
	 * 国战心跳
	 */
	public void heartbeat() {
		long start = System.currentTimeMillis();
		if (start > endTime) {
			if (!龙象释帝.isDeath()) {
				result = 1;
			}
			this.state = STATE_结束;
			notifyEnd();
		}
	}

	public Country getAttacker() {
		return attacker;
	}

	public void setAttacker(Country attacker) {
		this.attacker = attacker;
	}

	public Country getDefender() {
		return defender;
	}

	public void setDefender(Country defender) {
		this.defender = defender;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getDelayedCount() {
		return delayedCount;
	}

	public void setDelayedCount(int delayedCount) {
		this.delayedCount = delayedCount;
	}

	public int getCureCount() {
		return cureCount;
	}

	public void setCureCount(int cureCount) {
		this.cureCount = cureCount;
	}

	public int getAttackerPullCount() {
		return attackerPullCount;
	}

	public void setAttackerPullCount(int attackerPullCount) {
		this.attackerPullCount = attackerPullCount;
	}

	public int getDefenderPullCount() {
		return defenderPullCount;
	}

	public void setDefenderPullCount(int defenderPullCount) {
		this.defenderPullCount = defenderPullCount;
	}

	/**
	 * 产生伤害输出
	 * 
	 * @param player
	 * @param caster
	 * @param damage
	 */
	public void notifyDamage(Player player, int damage) {
		GuozhanPlayer gp = this.getGuozhanPlayer(player);
		if (gp != null) {
			synchronized (gp) {
				gp.damage += damage;
			}
		}
	}
	
	/**
	 * 通知NPC招受攻击
	 * @param caster
	 */
	public void notifyNPCAttacked(GuozhanNPC npc, Fighter caster) {
		if(npc == 龙象释帝) {
			long now = System.currentTimeMillis();
			if(now - lastNotifyNPCAttackTime > 5000) {
				lastNotifyNPCAttackTime = now;
				Player ps[] = PlayerManager.getInstance().getOnlinePlayerByCountry(defender.getCountryId());
				List<Player> plist = this.getPlayersInGuozhan(ps);
				String mes = Translate.国战_龙象释帝正在被攻击;
				for(Player p : plist) {
					this.sendSystemMessageToPlayer(p, mes);
				}
			}
		}
 	}

	/**
	 * 通知玩家杀死了对手
	 * 
	 * @param player
	 * @param killed
	 */
	public void notifyKill(Player player, Player killed) {
		if (player.getCountry() == killed.getCountry()) {
			GuozhanOrganizer.logger.warn("[玩家蛋疼杀自己国家刷功勋值] [" + player.getLogString() + "] [killed:" + killed.getLogString() + "]");
			return;
		}
		long start = System.currentTimeMillis();
		int 职位 = CountryManager.getInstance().官职(killed.getCountry(), killed.getId());
		int 官阶 = CountryManager.得到官阶(职位);
		int gongxun = organizer.getConstants().击杀功勋值表[官阶];
		GuozhanPlayer gp = this.getGuozhanPlayer(player);
		long old = player.getGongxun();
		if (gp != null) {
			gp.gongxun += gongxun;
			try {
				BillingCenter.getInstance().playerSaving(player, gongxun, CurrencyType.GONGXUN, SavingReasonType.GUOZHAN, "");
			} catch (SavingFailedException e) {
				// TODO Auto-generated catch block
				GuozhanOrganizer.logger.error("[玩家完成一次击杀增加功勋值时异常] [player:" + player.getLogString() + "] [enemy:" + killed.getLogString() + "]", e);
			}
			if (start - gp.lastKillTime < organizer.getConstants().连胜时间间隙 * 1000) {
				gp.lianshengNum++;
				if (organizer.getConstants().是否需要连胜播报(gp.lianshengNum)) {
					String message = Translate.translateString(Translate.国战_播报1, new String[][]{{"@player_country@",CountryManager.得到国家名(player.getCountry())}, 
							{"@player_name@", player.getName()}, {"@liansheng_num@", String.valueOf(gp.lianshengNum)}});
					List<Player> plist = getCountryPlayersInGuozhan();
					organizer.sendMessagesToPlayers(plist.toArray(new Player[0]), message, (byte) 2);
				}
			} else {
				gp.lianshengNum = 0;
			}
			gp.killList.add(killed.getId());
			gp = this.getGuozhanPlayer(killed);
			gp.beKilledList.add(player.getId());
		}
		if (职位 == CountryManager.国王) {
			CountryManager cm = CountryManager.getInstance();
			String content = Translate.translateString(Translate.国战_播报2, new String[][]{{"@player_name@", player.getName()},{"@killed_country@", cm.得到国家名(killed.getCountry())},{"@title_name@", CountryManager.得到官职名(CountryManager.国王)}});
			Player players[] = PlayerManager.getInstance().getOnlinePlayerByCountry(attacker.getCountryId());
			organizer.sendMessagesToPlayers(players, content, (byte) 5);
			players = PlayerManager.getInstance().getOnlinePlayerByCountry(defender.getCountryId());
			organizer.sendMessagesToPlayers(players, content, (byte) 5);
		}
			GuozhanOrganizer.logger.warn("[玩家完成一次击杀] [player:{}] [enemy:{}] [敌人官职:{}] [官阶:{}] [gongxun:{}] [{}ms]", new Object[] { player.getLogString(),
					killed.getLogString(), 职位, 官阶, old + "->" + player.getGongxun(), (System.currentTimeMillis() - start) });
	}

	/**
	 * 通知国战NPC被杀死了
	 * 
	 * @param player
	 * @param npc
	 */
	public void notifyNPCKilled(Player player, GuozhanNPC npc) {
		int level = 200;
		BillboardsManager bbm = BillboardsManager.getInstance();
		if(bbm != null){
			level = bbm.getMaxLevelByBillBoard();
		}
		long start = System.currentTimeMillis();
		Constants cons = organizer.getConstants();
		if (npc == 龙牙关边城守将) {
			// 攻方攻下来的，立即获得复活点，并且刷新为攻方的npc
			Game game = GameManager.getInstance().getGameByName((String) cons.getBianGuan(defender.getCountryId()), defender.getCountryId());
			MemoryNPCManager mnm = (MemoryNPCManager) MemoryNPCManager.getNPCManager();
			龙牙关边城攻将 = (GuozhanNPC) mnm.createNPC((Integer) cons.NPC_边城守将A_data[1]);
			
			龙牙关边城攻将.setLevel(level);
			mnm.设置sprite属性值(龙牙关边城攻将);

			String npcName = 龙牙关边城攻将.getName();
			龙牙关边城攻将.setX((Integer) cons.getBianGuanXY(defender.getCountryId())[0] );
			龙牙关边城攻将.setY((Integer) cons.getBianGuanXY(defender.getCountryId())[1] );
			龙牙关边城攻将.setCountryId(attacker.getCountryId());
			龙牙关边城攻将.setEnemyCountryId(defender.getCountryId());
			龙牙关边城攻将.setName(龙牙关边城攻将.getName() + "(" + attacker.getName() + ")");
			game.addSprite(龙牙关边城攻将);
			attackerLongyaguanBornPoint.setBelongCountryId(attacker.getCountryId());
			mapPointMap.get(1).setOwnerType((byte) 0);
			mapPointMap.get(1).setOwnerCountryId(attacker.getCountryId());
			龙牙关边城守将 = null;
			List<Player> plist = this.getCountryPlayersInGuozhan();
			String message = Translate.translateString(Translate.国战_播报3, new String[][]{{"@defender_name@", defender.getName()},{"@npc_name@", npcName},{"@player_name@", player.getName()},{"@attacker_name@", attacker.getName()}});
			organizer.sendMessagesToPlayers(plist.toArray(new Player[0]), message, (byte) 2);
		} else if (npc == 龙牙关边城攻将) {
			// 守方攻下来，立即刷新为己方NPC，并且取消攻方的复活点
			Game game = GameManager.getInstance().getGameByName((String) cons.getBianGuan(defender.getCountryId()), defender.getCountryId());
			MemoryNPCManager mnm = (MemoryNPCManager) MemoryNPCManager.getNPCManager();
			龙牙关边城守将 = (GuozhanNPC) mnm.createNPC((Integer) cons.NPC_边城守将A_data[1]);
			
			龙牙关边城守将.setLevel(level);
			mnm.设置sprite属性值(龙牙关边城守将);

			String npcName = 龙牙关边城守将.getName();
			龙牙关边城守将.setX((Integer) cons.getBianGuanXY(defender.getCountryId())[0] );
			龙牙关边城守将.setY((Integer) cons.getBianGuanXY(defender.getCountryId())[1] );
			龙牙关边城守将.setCountryId(defender.getCountryId());
			龙牙关边城守将.setEnemyCountryId(attacker.getCountryId());
			龙牙关边城守将.setName(龙牙关边城守将.getName() + "(" + defender.getName() + ")");
			game.addSprite(龙牙关边城守将);
			attackerLongyaguanBornPoint.setBelongCountryId(defender.getCountryId());
			mapPointMap.get(1).setOwnerType((byte) 1);
			mapPointMap.get(1).setOwnerCountryId(defender.getCountryId());
			龙牙关边城攻将 = null;
			List<Player> plist = this.getCountryPlayersInGuozhan();
			String message = Translate.translateString(Translate.国战_播报4, new String[][]{{"@attacker_name@", attacker.getName()},{"@npc_name@", npcName},{"@player_name@", player.getName()},{"@defender_name@", defender.getName()}});
			organizer.sendMessagesToPlayers(plist.toArray(new Player[0]), message, (byte) 2);
		} else if (npc == 昆仑要塞边城守将) {
			// 攻方攻下来的，立即获得复活点，并且刷新为攻方的npc
			Game game = GameManager.getInstance().getGameByName((String) cons.getYaoSai(defender.getCountryId()), defender.getCountryId());
			MemoryNPCManager mnm = (MemoryNPCManager) MemoryNPCManager.getNPCManager();
			昆仑要塞边城攻将 = (GuozhanNPC) mnm.createNPC((Integer) cons.NPC_边城守将B_data[1]);
			
			昆仑要塞边城攻将.setLevel(level);
			mnm.设置sprite属性值(昆仑要塞边城攻将);

			String npcName = 昆仑要塞边城攻将.getName();
			昆仑要塞边城攻将.setX((Integer) cons.getYaoSaiXY(defender.getCountryId())[0]);
			昆仑要塞边城攻将.setY((Integer) cons.getYaoSaiXY(defender.getCountryId())[1]);
			昆仑要塞边城攻将.setCountryId(attacker.getCountryId());
			昆仑要塞边城攻将.setEnemyCountryId(defender.getCountryId());
			昆仑要塞边城攻将.setName(昆仑要塞边城攻将.getName() + "(" + attacker.getName() + ")");
			game.addSprite(昆仑要塞边城攻将);
			attackerYaosaiBornPoint.setBelongCountryId(attacker.getCountryId());
			mapPointMap.get(2).setOwnerType((byte) 0);
			mapPointMap.get(2).setOwnerCountryId(attacker.getCountryId());
			昆仑要塞边城守将 = null;
			List<Player> plist = this.getCountryPlayersInGuozhan();
			String message = Translate.translateString(Translate.国战_播报5, new String[][]{{"@defender_name@", defender.getName()},{"@npc_name@", npcName},{"@player_name@", player.getName()},{"@attacker_name@", attacker.getName()}});
			organizer.sendMessagesToPlayers(plist.toArray(new Player[0]), message, (byte) 2);
		} else if (npc == 昆仑要塞边城攻将) {
			// 守方攻下来的，立即取消攻方复活点，并且刷新为守方的npc
			Game game = GameManager.getInstance().getGameByName((String) cons.getYaoSai(defender.getCountryId()), defender.getCountryId());
			MemoryNPCManager mnm = (MemoryNPCManager) MemoryNPCManager.getNPCManager();
			昆仑要塞边城守将 = (GuozhanNPC) mnm.createNPC((Integer) cons.NPC_边城守将B_data[1]);
			
			昆仑要塞边城守将.setLevel(level);
			mnm.设置sprite属性值(昆仑要塞边城守将);

			String npcName = 昆仑要塞边城守将.getName();
			昆仑要塞边城守将.setX((Integer) cons.getYaoSaiXY(defender.getCountryId())[0]);
			昆仑要塞边城守将.setY((Integer) cons.getYaoSaiXY(defender.getCountryId())[1]);
			昆仑要塞边城守将.setCountryId(defender.getCountryId());
			昆仑要塞边城守将.setEnemyCountryId(attacker.getCountryId());
			昆仑要塞边城守将.setName(昆仑要塞边城守将.getName() + "(" + defender.getName() + ")");
			game.addSprite(昆仑要塞边城守将);
			attackerYaosaiBornPoint.setBelongCountryId(defender.getCountryId());
			mapPointMap.get(2).setOwnerType((byte) 1);
			mapPointMap.get(2).setOwnerCountryId(defender.getCountryId());
			昆仑要塞边城攻将 = null;
			List<Player> plist = this.getCountryPlayersInGuozhan();
			String message = Translate.translateString(Translate.国战_播报6, new String[][]{{"@attacker_name@", attacker.getName()},{"@npc_name@", npcName},{"@player_name@", player.getName()},{"@defender_name@", defender.getName()}});
			organizer.sendMessagesToPlayers(plist.toArray(new Player[0]), message, (byte) 2);
		} else if (npc == 王城戌卫官) {
			// 攻方攻下来的，立即获得复活点，并且刷新为攻方的npc
			Game game = GameManager.getInstance().getGameByName((String) cons.getMainString(defender.getCountryId()), defender.getCountryId());
			MemoryNPCManager mnm = (MemoryNPCManager) MemoryNPCManager.getNPCManager();
			王城戌攻官 = (GuozhanNPC) mnm.createNPC((Integer) cons.NPC_王城戌卫官_data[1]);
			
			王城戌攻官.setLevel(level);
			mnm.设置sprite属性值(王城戌攻官);

			王城戌攻官.setX((Integer) cons.getMainXY1(defender.getCountryId())[0]);
			王城戌攻官.setY((Integer) cons.getMainXY1(defender.getCountryId())[1]);
			王城戌攻官.setCountryId(attacker.getCountryId());
			王城戌攻官.setEnemyCountryId(defender.getCountryId());
			王城戌攻官.setName(王城戌攻官.getName() + "(" + attacker.getName() + ")");
			game.addSprite(王城戌攻官);
			attackerWangchengBornPoint.setBelongCountryId(attacker.getCountryId());
			mapPointMap.get(3).setOwnerType((byte) 0);
			mapPointMap.get(3).setOwnerCountryId(attacker.getCountryId());
			String npcName = 王城戌卫官.getName();
			王城戌卫官 = null;
			List<Player> plist = this.getCountryPlayersInGuozhan();
			String message = Translate.translateString(Translate.国战_播报7, new String[][]{{"@defender_name@", defender.getName()},{"@npc_name@", npcName},{"@player_name@", player.getName()},{"@attacker_name@", attacker.getName()}, {"@longxiangshidi_name@", 龙象释帝.getName()}});
			organizer.sendMessagesToPlayers(plist.toArray(new Player[0]), message, (byte) 2);
		} else if (npc == 王城戌攻官) {
			// 守方攻下来的，立即取消攻方复活点，并且刷新为守方的npc
			Game game = GameManager.getInstance().getGameByName((String) cons.getMainString(defender.getCountryId()), defender.getCountryId());
			MemoryNPCManager mnm = (MemoryNPCManager) MemoryNPCManager.getNPCManager();
			王城戌卫官 = (GuozhanNPC) mnm.createNPC((Integer) cons.NPC_王城戌卫官_data[1]);
			
			王城戌卫官.setLevel(level);
			mnm.设置sprite属性值(王城戌卫官);

			String npcName = 王城戌卫官.getName();
			王城戌卫官.setX((Integer) cons.getMainXY1(defender.getCountryId())[0]);
			王城戌卫官.setY((Integer) cons.getMainXY1(defender.getCountryId())[1]);
			王城戌卫官.setCountryId(defender.getCountryId());
			王城戌卫官.setEnemyCountryId(attacker.getCountryId());
			王城戌卫官.setName(王城戌卫官.getName() + "(" + defender.getName() + ")");
			game.addSprite(王城戌卫官);
			attackerWangchengBornPoint.setBelongCountryId(defender.getCountryId());
			mapPointMap.get(3).setOwnerType((byte) 1);
			mapPointMap.get(3).setOwnerCountryId(defender.getCountryId());
			王城戌攻官 = null;
			List<Player> plist = this.getCountryPlayersInGuozhan();
			String message = Translate.translateString(Translate.国战_播报8, new String[][]{{"@attacker_name@", attacker.getName()},{"@npc_name@", npcName},{"@player_name@", player.getName()},{"@defender_name@", defender.getName()}});
			organizer.sendMessagesToPlayers(plist.toArray(new Player[0]), message, (byte) 2);
		} else if (npc == 龙象卫长) {
			defenderBornPoint.setBelongCountryId(attacker.getCountryId());
			List<Player> plist = this.getCountryPlayersInGuozhan();
			String message = Translate.translateString(Translate.国战_播报9, new String[][]{{"@attacker_name@", attacker.getName()}, {"@player_name@", player.getName()}, {"@defender_name@", defender.getName()}, {"@longxiangweizhang_name@", 龙象卫长.getName()}});
			organizer.sendMessagesToPlayers(plist.toArray(new Player[0]), message, (byte) 2);
			mapPointMap.get(4).setOwnerType((byte) 0);
			mapPointMap.get(4).setOwnerCountryId(attacker.getCountryId());
		} else if (npc == 龙象释帝) {
			// 国战结束
			mapPointMap.get(5).setOwnerType((byte) 0);
			mapPointMap.get(5).setOwnerCountryId(attacker.getCountryId());
			this.result = 0;
			this.state = STATE_结束;
			this.notifyEnd();
		}
			GuozhanOrganizer.logger.info("[杀死NPC] [hash:"+npc.hashCode()+"] [" + npc.getName() + "] [player:" + player.getLogString() + "] [" + (System.currentTimeMillis() - start)
					+ "ms]");
			//GuozhanOrganizer.logger.info(StringUtil.getStackTrace(Thread.currentThread().getStackTrace()));
	}

	/**
	 * 延长国战时间
	 * 
	 * 只有混元至圣、司命天王、纯阳真圣才能延长，只能延长两次国战，延长需要消耗国战资源
	 * 
	 * @param player
	 */
	public synchronized void delayEndTime(Player player) throws OperationNotPermitException, GuozhanAlreadyEndException, ResourceNotEnoughException,
			NoLeftTimeException {
		long start = System.currentTimeMillis();
		if (state == STATE_结束) {
			throw new GuozhanAlreadyEndException(Translate.国战已结束);
		}
		// 1.检查官职是否允许
		CountryManager cm = CountryManager.getInstance();
		int 官职 = cm.官职(player.getCountry(), player.getId());
		if (官职 != CountryManager.国王 && 官职 != CountryManager.元帅 && 官职 != CountryManager.大将军) {
			throw new OperationNotPermitException(Translate.玩家官职不够);
		}
		// 次数限制
		if (delayedCount >= organizer.getConstants().延长国战时间次数) {
			throw new NoLeftTimeException(Translate.已达使用次数上限);
		}
		int needResource = organizer.getConstants().延长国战时长消耗资源;
		if (attacker.getGuozhanResource() < needResource) {
			throw new ResourceNotEnoughException(Translate.资源不够);
		}
		delayedCount++;
		long shengyu = attacker.getGuozhanResource() - needResource;
		shengyu = shengyu < 0 ? 0 : shengyu;
		attacker.setGuozhanResource(shengyu);
		this.endTime += organizer.getConstants().延长国战时长 * 60 * 1000;

		String message = Translate.translateString(Translate.国战_播报10, new String[][]{{"@attacker_name@", attacker.getName()}, {"@defender_name@", defender.getName()}, {"@yanchangshijian@", String.valueOf(organizer.getConstants().延长国战时长)}});
		List<Player> plist = this.getCountryPlayersInGuozhan();
		organizer.sendMessagesToPlayers(plist.toArray(new Player[0]), message, (byte) 2);

			GuozhanOrganizer.logger.info("[延长结束时间] [结束时间:"+DateUtil.formatDate(new Date(endTime), "yyyy-MM-dd HH:mm:ss")+"] [name:" + attacker.getName() + "<->" + defender.getName() + "] [player:" + player.getLogString() + "] ["
					+ (System.currentTimeMillis() - start) + "ms]");
	}

	/**
	 * 从本国拉人，只有混元至圣、司命天王、纯阳真圣才能拉人，最多只能拉3次
	 * 
	 * @param player
	 */
	public synchronized void pullCountryCitizen(Player player, boolean useArticle) throws GuozhanAlreadyEndException, OperationNotPermitException,
			NoLeftTimeException, ResourceNotEnoughException {
		long start = System.currentTimeMillis();
		if (state == STATE_结束) {
			throw new GuozhanAlreadyEndException(Translate.国战已结束);
		}
		// 1.检查官职是否允许
		CountryManager cm = CountryManager.getInstance();
		int 官职 = cm.官职(player.getCountry(), player.getId());
		if (官职 != CountryManager.国王 && 官职 != CountryManager.元帅 && 官职 != CountryManager.大将军) {
			throw new OperationNotPermitException(Translate.玩家官职不够);
		}
		if (!useArticle) {
			// 次数限制
			if (player.getCountry() == attacker.getCountryId()) {
				if (this.attackerPullCount >= organizer.getConstants().国战拉人次数) {
					throw new NoLeftTimeException(Translate.已达使用次数上限);
				}
				int needResource = organizer.getConstants().国战拉人消耗资源;
				if (attacker.getGuozhanResource() < needResource) {
					throw new ResourceNotEnoughException(Translate.资源不够);
				}
				attackerPullCount++;
				long shengyu = attacker.getGuozhanResource() - needResource;
				shengyu = shengyu < 0 ? 0 : shengyu;
				attacker.setGuozhanResource(shengyu);
			} else {
				if (this.defenderPullCount >= organizer.getConstants().国战拉人次数) {
					throw new NoLeftTimeException(Translate.已达使用次数上限);
				}
				int needResource = organizer.getConstants().国战拉人消耗资源;
				if (defender.getGuozhanResource() < needResource) {
					throw new ResourceNotEnoughException(Translate.资源不够);
				}
				defenderPullCount++;
				long shengyu = defender.getGuozhanResource() - needResource;
				shengyu = shengyu < 0 ? 0 : shengyu;
				defender.setGuozhanResource(shengyu);
			}
		}

		// 拉本国40级以上玩家
		Player ps[] = PlayerManager.getInstance().getOnlinePlayerByCountry(player.getCountry());
		for (Player p : ps) {
			if (p.getLevel() >= organizer.getConstants().国战等级限制 && p.isIsGuozhan() && !p.isInPrison()) {
				DevilSquareManager inst = DevilSquareManager.instance;
				if(inst != null && inst.isPlayerInDevilSquare(player)) {//恶魔广场不弹框
					continue;
				}
				if(GlobalTool.verifyTransByOther(player.getId()) != null) {
					continue;
				}
				pullPlayer(p);
			}
		}

		if (GuozhanOrganizer.logger.isInfoEnabled())
			GuozhanOrganizer.logger.info("[国战拉人] [name:" + attacker.getName() + "<->" + defender.getName() + "] [player:" + player.getLogString() + "] ["
					+ (System.currentTimeMillis() - start) + "ms]");
	}

	public void pullPlayer(Player player) {
		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(60 * 2);
		mw.setTitle(Translate.国战信息);
		mw.setDescriptionInUUB(Translate.是否立即前往国战前线);

		Option_Guozhan_Pull_OK ok = new Option_Guozhan_Pull_OK();
		ok.setText("是");
		ok.setColor(0xffffff);

		Option_Guozhan_Pull_Cancel cancel = new Option_Guozhan_Pull_Cancel();
		cancel.setText("否");
		cancel.setColor(0xffffff);

		mw.setOptions(new Option[] { ok, cancel });

		QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
		player.addMessageToRightBag(res);
	}

	/**
	 * 给国战NPC加血，最多只能加3次血，每次加15%
	 * 
	 * @param player
	 * @param npcId
	 */
	public synchronized void cureBoss(Player player, int npcIndex) throws GuozhanAlreadyEndException, OperationNotPermitException, NoLeftTimeException,
			ResourceNotEnoughException {
		long start = System.currentTimeMillis();
		if (state == STATE_结束) {
			throw new GuozhanAlreadyEndException(Translate.国战已结束);
		}
		// 1.检查官职是否允许
		CountryManager cm = CountryManager.getInstance();
		int 官职 = cm.官职(player.getCountry(), player.getId());
		if (官职 != CountryManager.国王 && 官职 != CountryManager.元帅 && 官职 != CountryManager.大将军) {
			throw new OperationNotPermitException(Translate.玩家官职不够);
		}
		// 次数限制
		if (cureCount >= organizer.getConstants().国战加血次数) {
			throw new NoLeftTimeException(Translate.已达使用次数上限);
		}
		int needResource = organizer.getConstants().国战加血消耗资源;
		if (defender.getGuozhanResource() < needResource) {
			throw new ResourceNotEnoughException(Translate.资源不够);
		}
		cureCount++;
		long shengyu = defender.getGuozhanResource() - needResource;
		shengyu = shengyu < 0 ? 0 : shengyu;
		defender.setGuozhanResource(shengyu);
		if (npcIndex == 0) {
			if (!龙象释帝.isDeath()) {
				int hp = 龙象释帝.getHp() + (int) (龙象释帝.getMaxHP() * organizer.getConstants().国战每次加血量);
				if (hp > 龙象释帝.getMaxHP()) {
					hp = 龙象释帝.getMaxHP();
				}
				龙象释帝.setHp(hp);
				String message = Translate.translateString(Translate.国战_播报11, new String[][]{{"@defender_name@", defender.getName()},{"@longxiangshidi_name@", 龙象释帝.getName()}, {"@huifuxueliang@", String.valueOf((int) (organizer.getConstants().国战每次加血量 * 100))},{}});
				List<Player> plist = this.getCountryPlayersInGuozhan();
				organizer.sendMessagesToPlayers(plist.toArray(new Player[0]), message, (byte) 2);
			}
		} else {
			if (!龙象卫长.isDeath()) {
				int hp = 龙象卫长.getHp() + (int) (龙象释帝.getMaxHP() * organizer.getConstants().国战每次加血量);
				if (hp > 龙象卫长.getMaxHP()) {
					hp = 龙象卫长.getMaxHP();
				}
				龙象卫长.setHp(hp);
				String message = Translate.translateString(Translate.国战_播报11, new String[][]{{"@defender_name@", defender.getName()},{"@longxiangshidi_name@", 龙象卫长.getName()}, {"huifuxueliang", String.valueOf((int) (organizer.getConstants().国战每次加血量 * 100))},{}});
				List<Player> plist = this.getCountryPlayersInGuozhan();
				organizer.sendMessagesToPlayers(plist.toArray(new Player[0]), message, (byte) 2);
			}
		}
		if (GuozhanOrganizer.logger.isInfoEnabled())
			GuozhanOrganizer.logger.info("[给BOSS加血] [目前血量:" + 龙象释帝.getHp() + "] [name:" + attacker.getName() + "<->" + defender.getName() + "] [player:"
					+ player.getLogString() + "] [" + (System.currentTimeMillis() - start) + "ms]");
	}

	/**
	 * 手动关闭
	 */
	public void forceEnd() {
		if (!龙象释帝.isDeath()) {
			result = 1;
		}
		this.state = STATE_结束;
		notifyEnd();
	}

	/**
	 * 结束这次国战
	 */
	public synchronized void notifyEnd() {

		GuozhanStat attackStat = organizer.getGuozhanStat(attacker.getCountryId());
		GuozhanStat defendStat = organizer.getGuozhanStat(defender.getCountryId());
		if (result == 0) {
			attackStat.setWinNum(attackStat.getWinNum() + 1);
			attackStat.setAttackWinNum(attackStat.getAttackWinNum() + 1);
			defendStat.setLoseNum(defendStat.getLoseNum() + 1);
			defendStat.setDefendLoseNum(defendStat.getDefendLoseNum() + 1);
		} else {
			attackStat.setLoseNum(attackStat.getLoseNum() + 1);
			defendStat.setWinNum(defendStat.getWinNum() + 1);
		}
		organizer.updateGuozhanStat();

		// 清除国战相关NPC
		if (龙牙关边城守将 != null) {
			Game game = GameManager.getInstance().getGameByName(龙牙关边城守将.getGameName(), defender.getCountryId());
			GuozhanOrganizer.logger.warn("[traceEnd] [" + 龙牙关边城守将.getGameName() + "] [" + defender.getCountryId() + "] ["
					+ (game == null ? "NULL" : game.getGameInfo().getMapName()) + "]");
			game.removeSprite(龙牙关边城守将);
		}
		if (龙牙关边城攻将 != null) {
			Game game = GameManager.getInstance().getGameByName(龙牙关边城攻将.getGameName(), defender.getCountryId());
			game.removeSprite(龙牙关边城攻将);
		}
		if (昆仑要塞边城守将 != null) {
			Game game = GameManager.getInstance().getGameByName(昆仑要塞边城守将.getGameName(), defender.getCountryId());
			game.removeSprite(昆仑要塞边城守将);
		}
		if (昆仑要塞边城攻将 != null) {
			Game game = GameManager.getInstance().getGameByName(昆仑要塞边城攻将.getGameName(), defender.getCountryId());
			game.removeSprite(昆仑要塞边城攻将);
		}
		if (王城戌卫官 != null) {
			Game game = GameManager.getInstance().getGameByName(王城戌卫官.getGameName(), defender.getCountryId());
			game.removeSprite(王城戌卫官);
		}
		if (王城戌攻官 != null) {
			Game game = GameManager.getInstance().getGameByName(王城戌攻官.getGameName(), defender.getCountryId());
			game.removeSprite(王城戌攻官);
		}
		if (龙象卫长 != null) {
			Game game = GameManager.getInstance().getGameByName(龙象卫长.getGameName(), defender.getCountryId());
			game.removeSprite(龙象卫长);
		}
		if (龙象释帝 != null) {
			Game game = GameManager.getInstance().getGameByName(龙象释帝.getGameName(), defender.getCountryId());
			game.removeSprite(龙象释帝);
		}

		List<GuozhanPlayer> attackPlayers = new ArrayList<GuozhanPlayer>();
		List<GuozhanPlayer> defendPlayers = new ArrayList<GuozhanPlayer>();
		try {
			Set<Map.Entry<Long, GuozhanPlayer>> pes = attendPlayers.entrySet();
			for (Map.Entry<Long, GuozhanPlayer> p : pes) {
				if (p.getValue().player.getCountry() == attacker.getCountryId()) {
					attackPlayers.add(p.getValue());
				} else {
					defendPlayers.add(p.getValue());
				}
			}
		} catch (Exception e) {
			GuozhanOrganizer.logger.error("[结束国战设置玩家状态时异常]", e);
		}

		GuozhanHistory history = new GuozhanHistory();
		history.setId(Long.valueOf(StringUtil.randomIntegerString(12)));
		history.setAttackCountryName(attacker.getName());
		history.setDefendCountryName(defender.getName());
		history.setOpenTime(startTime);
		history.setResult(result);
		long pid = attacker.getKingId();
		try {
			Player p = PlayerManager.getInstance().getPlayer(pid);
			history.setAttackKing(p.getName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			history.setAttackKing(Translate.暂无);
		}
		pid = defender.getKingId();
		try {
			Player p = PlayerManager.getInstance().getPlayer(pid);
			history.setDefendKing(p.getName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			history.setDefendKing(Translate.暂无);
		}
		// 击杀榜
		Collections.sort(attackPlayers, new Comparator<GuozhanPlayer>() {
			public int compare(GuozhanPlayer o1, GuozhanPlayer o2) {
				if (o1.killList.size() > o2.killList.size()) {
					return -1;
				} else if (o1.killList.size() < o2.killList.size()) {
					return 1;
				}
				return 0;
			}
		});
		String attackNames[] = new String[attackPlayers.size()];
		int attackKillNums[] = new int[attackNames.length];
		for (int i = 0; i < attackNames.length; i++) {
			attackNames[i] = attackPlayers.get(i).player.getName();
			attackKillNums[i] = attackPlayers.get(i).killList.size();
		}
		Collections.sort(defendPlayers, new Comparator<GuozhanPlayer>() {
			public int compare(GuozhanPlayer o1, GuozhanPlayer o2) {
				if (o1.killList.size() > o2.killList.size()) {
					return -1;
				} else if (o1.killList.size() < o2.killList.size()) {
					return 1;
				}
				return 0;
			}
		});
		String defendNames[] = new String[defendPlayers.size()];
		int defendKillNums[] = new int[defendNames.length];
		for (int i = 0; i < defendNames.length; i++) {
			defendNames[i] = defendPlayers.get(i).player.getName();
			defendKillNums[i] = defendPlayers.get(i).killList.size();
		}
		history.setAttackTopPlayers(attackNames);
		history.setAttackTopKillNums(attackKillNums);
		history.setDefendTopPlayers(defendNames);
		history.setDefendTopKillNums(defendKillNums);

		organizer.getGuozhanHistoryList().add(history);
		organizer.updateHistoryList();

		// 进行奖励
		makeReward();

		String anames[] = new String[attackNames.length > 3 ? 3 : attackNames.length];
		System.arraycopy(attackNames, 0, anames, 0, anames.length);
		int akills[] = new int[anames.length];
		System.arraycopy(attackKillNums, 0, akills, 0, anames.length);
		String dnames[] = new String[defendNames.length > 3 ? 3 : defendNames.length];
		System.arraycopy(defendNames, 0, dnames, 0, dnames.length);
		int dkills[] = new int[dnames.length];
		System.arraycopy(defendKillNums, 0, dkills, 0, dnames.length);

		// 给两国在线玩家发送结果通告
		String message = Translate.translateString(Translate.国战_播报12, new String[][]{{"@win_name@", (result == 0 ? (Translate.攻方 + attacker.getName()) : (Translate.守方 + defender.getName()))},{"@lose_name@", (result == 1 ? (Translate.攻方 + attacker.getName()) : (Translate.守方 + defender.getName()))}});
		String npcNames[]=  new String[]{龙象释帝.getName(),龙象卫长.getName(),王城戌卫官!=null?王城戌卫官.getName():王城戌攻官.getName(),昆仑要塞边城守将!=null?昆仑要塞边城守将.getName():昆仑要塞边城攻将.getName(),龙牙关边城守将!=null?龙牙关边城守将.getName():龙牙关边城攻将.getName()};
		for(String s : npcNames) {
			if(s.indexOf("(") != -1) {
				s = s.split("\\(")[0];
			}
		}
		int npcStatus[] = new int[5];
		npcStatus[0] = 龙象释帝.getHp()>0?0:1;
		npcStatus[1] = 龙象卫长.getHp()>0?0:1;
		npcStatus[2] = 王城戌卫官!=null?0:1;
		npcStatus[3] = 昆仑要塞边城守将!=null?0:1;
		npcStatus[4] = 龙牙关边城守将!=null?0:1;

		for (GuozhanPlayer gp : attackPlayers) {
			if (gp.player.isOnline() && GlobalTool.verifyMapTrans(gp.player.getId()) == null) {
				GUOZHAN_RESULT_REQ req = new GUOZHAN_RESULT_REQ(GameMessageFactory.nextSequnceNum(), message, result == 0 ? attacker.getName() : defender
						.getName(), attacker.getName(), anames, akills, defender.getName(), dnames, dkills, gp.damage, gp.killList.size(), gp.gongxun, gp.exp,
						true);
				gp.player.addMessageToRightBag(req);
				GUOZHAN_RESULT_NEW_REQ req2 = new GUOZHAN_RESULT_NEW_REQ(GameMessageFactory.nextSequnceNum(), message, result == 0 ? attacker.getName() : defender
						.getName(), attacker.getName(), anames, akills, defender.getName(), dnames, dkills, gp.damage, gp.killList.size(), gp.gongxun, gp.exp,
						true, npcNames, npcStatus);
				gp.player.addMessageToRightBag(req2);
			}
			gp.player.setGuozhan(false);
		}
		for (GuozhanPlayer gp : defendPlayers) {
			if (gp.player.isOnline()) {
				GUOZHAN_RESULT_REQ req = new GUOZHAN_RESULT_REQ(GameMessageFactory.nextSequnceNum(), message, result == 0 ? attacker.getName() : defender
						.getName(), attacker.getName(), anames, akills, defender.getName(), dnames, dkills, gp.damage, gp.killList.size(), gp.gongxun, gp.exp,
						false);
				gp.player.addMessageToRightBag(req);
				GUOZHAN_RESULT_NEW_REQ req2 = new GUOZHAN_RESULT_NEW_REQ(GameMessageFactory.nextSequnceNum(), message, result == 0 ? attacker.getName() : defender
						.getName(), attacker.getName(), anames, akills, defender.getName(), dnames, dkills, gp.damage, gp.killList.size(), gp.gongxun, gp.exp,
						false, npcNames, npcStatus);
				gp.player.addMessageToRightBag(req2);
			}
			gp.player.setGuozhan(false);
		}

		attendPlayers.clear();

		Player players[] = PlayerManager.getInstance().getOnlinePlayers();
		String content = Translate.translateString(Translate.国战_播报13 , new String[][]{{"@attacker_name@", attacker.getName()}, {"@defender_name@", defender.getName()}, {"@result@", (result == 0 ? Translate.胜利 : Translate.失败)}});
		organizer.sendMessagesToPlayers(players, content, (byte) 5);

		//ActivityNoticeManager.getInstance().activityEnd(Activity.国战, players);
		
		if (GuozhanOrganizer.logger.isInfoEnabled())
			GuozhanOrganizer.logger.info("[国战结束] [" + (result == 0 ? "攻方获胜" : "守方获胜") + "] [进攻方:" + attacker.getName() + "] [防守方:" + defender.getName() + "]");
	}

	public void makeReward() {
		// 进行经验奖励
		try {
			Set<Map.Entry<Long, GuozhanPlayer>> pes = attendPlayers.entrySet();
			for (Map.Entry<Long, GuozhanPlayer> p : pes) {
				if (!p.getValue().player.isOnline()) {
					continue;
				}
				p.getValue().player.setGuozhan(false);
				
				if ((p.getValue().player.getCountry() == attacker.getCountryId() && result == 0)
						|| (p.getValue().player.getCountry() == defender.getCountryId() && result == 1)) {
					// 胜方
					Long[] exp = organizer.getExpMap().get(p.getValue().player.getSoulLevel());
					if (exp != null) {
						long exp0 = (long)(new Double(exp[0])/9.0625*龙象加成);
						long exp1 = 0;
						
						if(p.getValue().player.getCountry() == attacker.getCountryId()) {
							//如果是攻方，计算小boss的加成
							int factor = 0;
							if(龙牙关边城攻将 != null && 龙牙关边城攻将.getHp() > 0) {
								factor++;
							}
							if(昆仑要塞边城攻将 != null && 昆仑要塞边城攻将.getHp() > 0) {
								factor++;
							}
							if(王城戌攻官 != null && 王城戌攻官.getHp() > 0) {
								factor++;
							}
							if(龙象卫长 != null && !龙象卫长.isAlive()) {
								factor++;
							}
							exp1 = (long)(new Double(exp[0])/9.0625*Math.pow(护卫加成, factor));
						} else {
							//如果是守方，计算小boss的加成
							int factor = 0;
							if(龙牙关边城守将 != null && 龙牙关边城守将.getHp() > 0) {
								factor++;
							}
							if(昆仑要塞边城守将 != null && 昆仑要塞边城守将.getHp() > 0) {
								factor++;
							}
							if(王城戌卫官 != null && 王城戌卫官.getHp() > 0) {
								factor++;
							}
							if(龙象卫长 != null && 龙象卫长.isAlive()) {
								factor++;
							}
							exp1 = (long)(new Double(exp[0])/9.0625*Math.pow(护卫加成, factor));
						}
						
						p.getValue().player.addExp(exp0+exp1, ExperienceManager.ADDEXP_REASON_GUOZHAN);
						GuozhanPlayer gp = this.getGuozhanPlayer(p.getValue().player);
						if (gp != null) {
							gp.exp = exp0+exp1;
						}
					}
				} else if ((p.getValue().player.getCountry() == attacker.getCountryId() && result == 1)
						|| (p.getValue().player.getCountry() == defender.getCountryId() && result == 0)) {
					// 负方
					Long[] exp = organizer.getExpMap().get(p.getValue().player.getSoulLevel());
					if (exp != null) {
						long exp0 = 0;
						long exp1 = 0;
						
						if(p.getValue().player.getCountry() == attacker.getCountryId()) {
							//如果是攻方，计算小boss的加成
							int factor = 0;
							if(龙牙关边城攻将 != null && 龙牙关边城攻将.getHp() > 0) {
								factor++;
							}
							if(昆仑要塞边城攻将 != null && 昆仑要塞边城攻将.getHp() > 0) {
								factor++;
							}
							if(王城戌攻官 != null && 王城戌攻官.getHp() > 0) {
								factor++;
							}
							if(龙象卫长 != null && !龙象卫长.isAlive()) {
								factor++;
							}
							exp1 = (long)(new Double(exp[1])/9.0625*Math.pow(护卫加成, factor));
						} else {
							//如果是守方，计算小boss的加成
							int factor = 0;
							if(龙牙关边城守将 != null && 龙牙关边城守将.getHp() > 0) {
								factor++;
							}
							if(昆仑要塞边城守将 != null && 昆仑要塞边城守将.getHp() > 0) {
								factor++;
							}
							if(王城戌卫官 != null && 王城戌卫官.getHp() > 0) {
								factor++;
							}
							if(龙象卫长 != null && 龙象卫长.isAlive()) {
								factor++;
							}
							exp1 = (long)(new Double(exp[1])/9.0625*Math.pow(护卫加成, factor));
						}
						
						p.getValue().player.addExp(exp0+exp1, ExperienceManager.ADDEXP_REASON_GUOZHAN);
						GuozhanPlayer gp = this.getGuozhanPlayer(p.getValue().player);
						if (gp != null) {
							gp.exp = exp0+exp1;
						}
					}
				}
			}
		} catch (Exception e) {
			GuozhanOrganizer.logger.error("[结束国战发放经验时发生异常]", e);
		}

		if (GuozhanOrganizer.logger.isInfoEnabled())
			GuozhanOrganizer.logger.info("[国战奖励] [" + (result == 0 ? "攻方获胜" : "守方获胜") + "] [进攻方:" + attacker.getName() + "] [防守方:" + defender.getName() + "]");
	}

	/**
	 * 获得国战攻方玩家的三个复活点
	 * 
	 * @return
	 */
	public GuozhanBornPoint[] getAttackerGuozhanBornPoint() {
		GuozhanBornPoint p[] = new GuozhanBornPoint[] { attackerLongyaguanBornPoint, attackerYaosaiBornPoint, attackerWangchengBornPoint };
		return p;
	}

	/**
	 * 得到国战中的两个boss, 大boss在前，小boss在后
	 * 
	 * @return
	 */
	public GuozhanNPC[] getBoss() {
		return new GuozhanNPC[] { 龙象释帝, 龙象卫长, 王城戌卫官!=null?王城戌卫官:王城戌攻官, 昆仑要塞边城守将!=null?昆仑要塞边城守将:昆仑要塞边城攻将, 龙牙关边城守将!=null?龙牙关边城守将:龙牙关边城攻将 };
	}

	/**
	 * 是否可以治疗boss
	 * 
	 * @param player
	 * @return
	 */
	public boolean canCureBoss(Player player) {
		if (player.getCountry() == defender.getCountryId()) {
			int 职位 = CountryManager.getInstance().官职(player.getCountry(), player.getId());
			if (职位 == CountryManager.国王 || 职位 == CountryManager.元帅 || 职位 == CountryManager.大将军) {
				if (this.cureCount < organizer.getConstants().国战加血次数) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 是否可以延长国战时间
	 * 
	 * @param player
	 * @return
	 */
	public boolean canDelayTime(Player player) {
		if (player.getCountry() == attacker.getCountryId()) {
			int 职位 = CountryManager.getInstance().官职(player.getCountry(), player.getId());
			if (职位 == CountryManager.国王 || 职位 == CountryManager.元帅 || 职位 == CountryManager.大将军) {
				if (this.delayedCount < organizer.getConstants().延长国战时间次数) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 是否可以拉人
	 * 
	 * @param player
	 * @return
	 */
	public boolean canPullPlayer(Player player) {
		int 职位 = CountryManager.getInstance().官职(player.getCountry(), player.getId());
		if (职位 == CountryManager.国王 || 职位 == CountryManager.元帅 || 职位 == CountryManager.大将军) {
			if (player.getCountry() == attacker.getCountryId() && this.attackerPullCount < organizer.getConstants().国战拉人次数) {
				return true;
			} else if (player.getCountry() == defender.getCountryId() && this.defenderPullCount < organizer.getConstants().国战拉人次数) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 玩家是否可以发布命令
	 * 
	 * @param player
	 * @return
	 */
	public boolean playerCanMakeOrder(Player player) {
		int 职位 = CountryManager.getInstance().官职(player.getCountry(), player.getId());
		if (职位 == CountryManager.国王 || 职位 == CountryManager.元帅 || 职位 == CountryManager.大将军) {
			return true;
		}
		return false;
	}

	/**
	 * 玩家发布命令
	 * 
	 * @param player
	 * @param index
	 */
	public void playerMakeOrder(Player player, int index) throws OperationNotPermitException {
		long start = System.currentTimeMillis();
		if (playerCanMakeOrder(player)) {
			List<FastMessage> mlist = organizer.getFastMessages(player);
			if (mlist != null && mlist.size() > index) {
				FastMessage fm = mlist.get(index);
				if (player.getCountry() == attacker.getCountryId()) {
					attackerOrders.add(fm.cloneOne());
				} else {
					defenderOrders.add(fm.cloneOne());
				}
				Player players[] = PlayerManager.getInstance().getOnlinePlayerByCountry(player.getCountry());
				CountryManager cm = CountryManager.getInstance();
				String title = cm.得到官职名(cm.官职(player.getCountry(), player.getId()));
				String message = title + "命令： [" + fm.getName() + "] " + fm.getMessage();
				for (Player p : players) {
					if (p.isIsGuozhan()) {
						sendPlayerCountryMessage(p, message);
					}
				}
				if (GuozhanOrganizer.logger.isInfoEnabled())
					GuozhanOrganizer.logger.info("[玩家发布命令] [" + player.getLogString() + "] [" + fm.getName() + "] [" + (System.currentTimeMillis() - start)
							+ "ms]");
			}
			return;
		} else {
			throw new OperationNotPermitException("您没有权利发布命令");
		}
	}

	/**
	 * 发送一个命令
	 * @param player
	 * @param name
	 * @param content
	 * @throws OperationNotPermitException
	 */
	public void playerSendOrderMessage(Player player, String name, String content) throws OperationNotPermitException {
		long start = System.currentTimeMillis();
		if (playerCanMakeOrder(player)) {
			FastMessage fm = new FastMessage("hd_tongyong", name, content);
			if (player.getCountry() == attacker.getCountryId()) {
				attackerOrders.add(fm.cloneOne());
			} else {
				defenderOrders.add(fm.cloneOne());
			}
			Player players[] = PlayerManager.getInstance().getOnlinePlayerByCountry(player.getCountry());
			CountryManager cm = CountryManager.getInstance();
			String title = cm.得到官职名(cm.官职(player.getCountry(), player.getId()));
			String message = title + "命令： [" + fm.getName() + "] " + fm.getMessage();
			for (Player p : players) {
				if (p.isIsGuozhan()) {
					sendPlayerCountryMessage(p, message);
				}
			}
			if (GuozhanOrganizer.logger.isInfoEnabled())
				GuozhanOrganizer.logger
						.info("[玩家发布命令] [" + player.getLogString() + "] [" + fm.getName() + "] [" + (System.currentTimeMillis() - start) + "ms]");
		} else {
			throw new OperationNotPermitException("您没有权利发布命令");
		}
		if (GuozhanOrganizer.logger.isInfoEnabled())
			GuozhanOrganizer.logger.info("[玩家发布命令错误:玩家不在国战中] [" + player.getLogString() + "] [" + (System.currentTimeMillis() - start) + "ms]");

	}

	/**
	 * 向玩家国家频道发消息
	 * 
	 * @param player
	 * @param message
	 */
	public void sendPlayerCountryMessage(Player player, String message) {
		ChatMessage mes = new ChatMessage();
		mes.setMessageText(message);
		mes.setSenderId(-1);
		mes.setSenderName("");
		mes.setSort(ChatChannelType.COUNTRY);
		ChatMessageItem item = new ChatMessageItem();
		ChatMessageTask task = new ChatMessageTask();
		CHAT_MESSAGE_REQ newReq = new CHAT_MESSAGE_REQ(GameMessageFactory.nextSequnceNum(), mes, item, task);
		player.addMessageToRightBag(newReq);
	}

	/**
	 * 得到发布的命令
	 * 
	 * @param player
	 * @return
	 */
	public List<FastMessage> getPubMessages(Player player) {
		if (player.getCountry() == attacker.getCountryId()) {
			return attackerOrders;
		} else {
			return defenderOrders;
		}
	}

	/**
	 * 得到玩家参与国战的复活点
	 * 
	 * @param player
	 * @return
	 */
	public GuozhanBornPoint getBornPoint(Player player) {
		if (player.getCountry() == attacker.getCountryId()) {
			if (attackerWangchengBornPoint.getBelongCountryId() == player.getCountry()) {
				return attackerWangchengBornPoint;
			} else {
				if (attackerLongyaguanBornPoint.getBelongCountryId() == player.getCountry()
						&& attackerYaosaiBornPoint.getBelongCountryId() == player.getCountry()) {
					Game currentGame = player.getCurrentGame();
					if (currentGame.getGameInfo().getMapName().equals(attackerLongyaguanBornPoint.getMapName())) {
						return attackerLongyaguanBornPoint;
					} else if (currentGame.getGameInfo().getMapName().equals(attackerYaosaiBornPoint.getMapName())) {
						return attackerYaosaiBornPoint;
					} else {
						int ran = new Random().nextInt(2);
						if (ran == 0) {
							return attackerLongyaguanBornPoint;
						} else {
							return attackerYaosaiBornPoint;
						}
					}
				} else if (attackerLongyaguanBornPoint.getBelongCountryId() == player.getCountry()) {
					return attackerLongyaguanBornPoint;
				} else if (attackerYaosaiBornPoint.getBelongCountryId() == player.getCountry()) {
					return attackerYaosaiBornPoint;
				}
			}
			return attackerDefaultBornPoint;
		} else {
			if (defenderBornPoint.getBelongCountryId() == defender.getCountryId()) {
				return defenderBornPoint;
			}
			return defenderLoseBornPoint;
		}
	}

	/**
	 * 加入一个玩家
	 * 
	 * @param player
	 */
	public synchronized void addAttendPlayer(Player player) {
		GuozhanPlayer gp = new GuozhanPlayer(player, player.getCountry() == attacker.getCountryId() ? GuozhanPlayer.TYPE_进攻 : GuozhanPlayer.TYPE_防御);
		attendPlayers.put(player.getId(), gp);
	}

	public HashMap<Long, GuozhanPlayer> getAttendPlayers() {
		return attendPlayers;
	}

	public List<Player> getAttackPlayers() {
		List<Player> ps = new ArrayList<Player>();
		Set<Map.Entry<Long, GuozhanPlayer>> pes = attendPlayers.entrySet();
		for (Map.Entry<Long, GuozhanPlayer> p : pes) {
			if (p.getValue().player.getCountry() == attacker.getCountryId()) {
				ps.add(p.getValue().player);
			}
		}
		return ps;
	}

	public List<Player> getDefendPlayers() {
		List<Player> ps = new ArrayList<Player>();
		Set<Map.Entry<Long, GuozhanPlayer>> pes = attendPlayers.entrySet();
		for (Map.Entry<Long, GuozhanPlayer> p : pes) {
			if (p.getValue().player.getCountry() == defender.getCountryId()) {
				ps.add(p.getValue().player);
			}
		}
		return ps;
	}

	/**
	 * 得到玩家敌对国家的名称
	 * 
	 * @param player
	 * @return
	 */
	public String getEnemyCountryName(Player player) {
		if (player.getCountry() == attacker.getCountryId()) {
			return defender.getName();
		} else {
			return attacker.getName();
		}
	}

	public GuozhanPlayer getGuozhanPlayer(Player player) {
		return attendPlayers.get(player.getId());
	}

	/**
	 * 得到一个集合中处于国战中的玩家
	 * 
	 * @param players
	 * @return
	 */
	public List<Player> getPlayersInGuozhan(Player players[]) {
		List<Player> plist = new ArrayList<Player>();
		for (Player p : players) {
			if (p.isIsGuozhan()) {
				plist.add(p);
			}
		}
		return plist;
	}

	/**
	 * 得到国战双方所有在国战中并且在线的玩家
	 * 
	 * @return
	 */
	public List<Player> getCountryPlayersInGuozhan() {
		Player ps[] = PlayerManager.getInstance().getOnlinePlayerByCountry(attacker.getCountryId());
		List<Player> plist = new ArrayList<Player>();
		for (Player p : ps) {
			if (p.isIsGuozhan()) {
				plist.add(p);
			}
		}
		ps = PlayerManager.getInstance().getOnlinePlayerByCountry(defender.getCountryId());
		for (Player p : ps) {
			if (p.isIsGuozhan()) {
				plist.add(p);
			}
		}
		return plist;
	}

	public LinkedHashMap<Integer, GuozhanMapPoint> getMapPointMap() {
		return mapPointMap;
	}

	public void setMapPointMap(LinkedHashMap<Integer, GuozhanMapPoint> mapPointMap) {
		this.mapPointMap = mapPointMap;
	}

	/**
	 * 获得玩家在国战中的类型, 0-进攻方，1-防守方
	 * 
	 * @param player
	 * @return
	 */
	public byte getPlayerGuozhanType(Player player) {
		if (player.getCountry() == attacker.getCountryId()) {
			return 0;
		}
		return 1;
	}

	/**
	 * 是否玩家的命令合法
	 * 
	 * @param player
	 * @param pointId
	 * @return
	 */
	public boolean isPlayerOrderValid(Player player, int pointId) {
		if (!playerCanMakeOrder(player)) {
			return false;
		}
		return true;
	}

	/**
	 * 发布命令
	 * 
	 * @param player
	 * @param aimId
	 */
	public void makeMapOrder(Player player, int aimId) {
		if (!playerCanMakeOrder(player)) {
			return;
		}
		int guozhanType = this.getPlayerGuozhanType(player);
		if (guozhanType == 0) {
			this.currentAttackerMapAimPoint = aimId;
			String content = "";
			String attackDefendStr = "";
			if (mapPointMap.get(aimId).getOwnerCountryId() == attacker.getCountryId()) {
				attackDefendStr = Translate.防守;
				content = Translate.translateString(Translate.去防守, new String[][]{{"@place_name@", mapPointMap.get(aimId).getName()}});
			} else {
				attackDefendStr = Translate.进攻;
				content = Translate.translateString(Translate.去进攻, new String[][]{{"@place_name@", mapPointMap.get(aimId).getName()}});
			}
			List<Player> ps = this.getAttackPlayers();
			for (Player p : ps) {
				if (p.isOnline()) {
					GUOZHAN_MAP_ORDER_MAKED_REQ req = new GUOZHAN_MAP_ORDER_MAKED_REQ(GameMessageFactory.nextSequnceNum());
					p.addMessageToRightBag(req);
				}
			}
			try {
				playerSendOrderMessage(player, attackDefendStr, content);
			} catch (OperationNotPermitException e) {
				// TODO Auto-generated catch block
				GuozhanOrganizer.logger.error("发送命令出错", e);
			}
			//organizer.sendMessagesToPlayers(ps.toArray(new Player[0]), content, (byte) 5);
		} else {
			this.currentDefenderMapAimPoint = aimId;
			String content = "";
			String attackDefendStr = "";
			if (mapPointMap.get(aimId).getOwnerCountryId() == defender.getCountryId()) {
				attackDefendStr = Translate.防守;
				content = Translate.translateString(Translate.去防守, new String[][]{{"@place_name@", mapPointMap.get(aimId).getName()}});
			} else {
				attackDefendStr = Translate.进攻;
				content = Translate.translateString(Translate.去进攻, new String[][]{{"@place_name@", mapPointMap.get(aimId).getName()}});
			}
			List<Player> ps = this.getDefendPlayers();
			for (Player p : ps) {
				if (p.isOnline()) {
					GUOZHAN_MAP_ORDER_MAKED_REQ req = new GUOZHAN_MAP_ORDER_MAKED_REQ(GameMessageFactory.nextSequnceNum());
					p.addMessageToRightBag(req);
				}
			}
			try {
				playerSendOrderMessage(player, attackDefendStr, content);
			} catch (OperationNotPermitException e) {
				// TODO Auto-generated catch block
				GuozhanOrganizer.logger.error("发送命令出错", e);
			}
			//organizer.sendMessagesToPlayers(ps.toArray(new Player[0]), content, (byte) 5);
		}
	}

	public int getCurrentAttackerMapAimPoint() {
		return currentAttackerMapAimPoint;
	}

	public void setCurrentAttackerMapAimPoint(int currentAttackerMapAimPoint) {
		this.currentAttackerMapAimPoint = currentAttackerMapAimPoint;
	}

	public int getCurrentDefenderMapAimPoint() {
		return currentDefenderMapAimPoint;
	}

	public void setCurrentDefenderMapAimPoint(int currentDefenderMapAimPoint) {
		this.currentDefenderMapAimPoint = currentDefenderMapAimPoint;
	}
	
	public void sendSystemMessageToPlayer(Player player , String content) {
		ChatMessage message = new ChatMessage();
		message.setAccessoryItem(new ChatMessageItem());
		message.setAccessoryTask(new ChatMessageTask());
		message.setChatTime(System.currentTimeMillis());
		message.setMessageText(content);
		message.setSenderId(0);
		message.setSenderName("");
		message.setSort(ChatChannelType.SYSTEM);
		message.setChatType(0);
		CHAT_MESSAGE_REQ newReq = new CHAT_MESSAGE_REQ(GameMessageFactory.nextSequnceNum(), message, message.getAccessoryItem(), message.getAccessoryTask());
		player.addMessageToRightBag(newReq);
	}

	public GuozhanNPC get龙牙关边城守将() {
		return 龙牙关边城守将;
	}

	public void set龙牙关边城守将(GuozhanNPC 龙牙关边城守将) {
		this.龙牙关边城守将 = 龙牙关边城守将;
	}

	public GuozhanNPC get昆仑要塞边城守将() {
		return 昆仑要塞边城守将;
	}

	public void set昆仑要塞边城守将(GuozhanNPC 昆仑要塞边城守将) {
		this.昆仑要塞边城守将 = 昆仑要塞边城守将;
	}

	public GuozhanNPC get龙牙关边城攻将() {
		return 龙牙关边城攻将;
	}

	public void set龙牙关边城攻将(GuozhanNPC 龙牙关边城攻将) {
		this.龙牙关边城攻将 = 龙牙关边城攻将;
	}

	public GuozhanNPC get昆仑要塞边城攻将() {
		return 昆仑要塞边城攻将;
	}

	public void set昆仑要塞边城攻将(GuozhanNPC 昆仑要塞边城攻将) {
		this.昆仑要塞边城攻将 = 昆仑要塞边城攻将;
	}

	public GuozhanNPC get王城戌卫官() {
		return 王城戌卫官;
	}

	public void set王城戌卫官(GuozhanNPC 王城戌卫官) {
		this.王城戌卫官 = 王城戌卫官;
	}

	public GuozhanNPC get王城戌攻官() {
		return 王城戌攻官;
	}

	public void set王城戌攻官(GuozhanNPC 王城戌攻官) {
		this.王城戌攻官 = 王城戌攻官;
	}

	public GuozhanNPC get龙象卫长() {
		return 龙象卫长;
	}

	public void set龙象卫长(GuozhanNPC 龙象卫长) {
		this.龙象卫长 = 龙象卫长;
	}

	public GuozhanNPC get龙象释帝() {
		return 龙象释帝;
	}

	public void set龙象释帝(GuozhanNPC 龙象释帝) {
		this.龙象释帝 = 龙象释帝;
	}
	
	

}
