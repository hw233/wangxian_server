/**
 * 
 */
package com.fy.engineserver.battlefield.concrete;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.activity.activeness.ActivenessManager;
import com.fy.engineserver.activity.activeness.ActivenessType;
import com.fy.engineserver.battlefield.BattleField;
import com.fy.engineserver.battlefield.BattleFieldInfo;
import com.fy.engineserver.constants.Event;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.AuraBuff;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.message.DISPLAY_INFO_ON_SCREEN;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.NOTIFY_BUFF_REMOVED_REQ;
import com.fy.engineserver.message.NOTIFY_EVENT_REQ;
import com.fy.engineserver.message.PLAYER_REVIVED_REQ;
import com.fy.engineserver.message.PLAYER_REVIVED_RES;
import com.fy.engineserver.message.QUERY_BATTLEFIELD_INFO_RES;
import com.fy.engineserver.message.TOURNAMENT_END_REQ;
import com.fy.engineserver.message.TOURNAMENT_QUERY_PLAYER_ICON_INFO_RES;
import com.fy.engineserver.message.TOURNAMENT_QUERY_SIDE_REQ;
import com.fy.engineserver.message.TOURNAMENT_START_REQ;
import com.fy.engineserver.message.TOURNAMENT_TIME_REQ;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.Team;
import com.fy.engineserver.sprite.npc.DoorNpc;
import com.fy.engineserver.tournament.data.OneTournamentData;
import com.fy.engineserver.tournament.manager.TournamentManager;

/**
 * @author Administrator
 * 
 */
public class TournamentField implements BattleField {

	public static Logger logger = LoggerFactory.getLogger(TournamentManager.class);

	public static String areaName = "比武擂台";

	BattleFieldInfo bi;

	String id;

	long startTime;

	long startWaitFightTime;

	long startFightingTime;

	long showResultTime;

	long closingTime;

	// 踢出玩家的时间
	long endTime;

	// 每场比武的时间
	private final long FIGHTING_TIME = 180000;

	private final long END_TIME = 12 * 60 * 1000;

	public static final int 比赛开始前进入时间 = 60000;

	public static final int 每场开始前倒计时 = 5000;
	public static final int 每场结束前倒计时 = 30000;

	public static final int 每场结束后宣布结果时间 = 6000;

	public static final int STATE_WAITING_ENTER = 0;

	public static final int STATE_WAITING_FIGHT = 1;

	public static final int STATE_FIGHTING = 2;

	public static final int STATE_SHOW_RESULT = 3;

	public static final int STATE_CLOSING = 4;

	public static final int STATE_CLOSED = 5;

	public static final String 传送出比武场地图 = Translate.传送出比武场地图;

	Random r;

	Game game;

	protected int winSide = BATTLE_SIDE_C;

	protected int transientWinerSide = BATTLE_SIDE_C;

	// 战场的状态
	int state = 0;

	boolean 未到场结束 = false;

	// 结束通知标记
	boolean battle_over_flag_60000 = false;

	boolean battle_over_flag_30000 = false;

	boolean battle_over_flag_10000 = false;

	boolean battle_start_flag_5000 = false;

	boolean battle_start_flag_4000 = false;

	boolean battle_start_flag_3000 = false;

	boolean battle_start_flag_2000 = false;

	boolean battle_start_flag_1000 = false;

	boolean showResultFlag = false;

	boolean showResult2Flag = false;

	// 统计每个玩家
	protected Hashtable<Long, BattleFieldStatData> statDataMap = new Hashtable<Long, BattleFieldStatData>();

	// A方的资源数
	protected int scoreForSideA = 2;

	// B方的资源数
	protected int scoreForSideB = 2;

	protected String sideNames[] = new String[3];

	// //////////////////////////////////////////////////////////////
	// 配置信息

	// 出生区域，复活区域
	protected int bornRegionXForB;

	protected int bornRegionYForB;

	protected int bornRegionXForA;

	protected int bornRegionYForA;

	protected int fightRegionXForB;

	protected int fightRegionYForB;

	protected int fightRegionXForA;

	protected int fightRegionYForA;

	// ///////////////////////////////////////////////////////////////////

	public int getFightRegionXForB() {
		return fightRegionXForB;
	}

	public void setFightRegionXForB(int fightRegionXForB) {
		this.fightRegionXForB = fightRegionXForB;
	}

	public int getFightRegionYForB() {
		return fightRegionYForB;
	}

	public void setFightRegionYForB(int fightRegionYForB) {
		this.fightRegionYForB = fightRegionYForB;
	}

	public int getFightRegionXForA() {
		return fightRegionXForA;
	}

	public void setFightRegionXForA(int fightRegionXForA) {
		this.fightRegionXForA = fightRegionXForA;
	}

	public int getFightRegionYForA() {
		return fightRegionYForA;
	}

	public void setFightRegionYForA(int fightRegionYForA) {
		this.fightRegionYForA = fightRegionYForA;
	}

	public ArrayList<LivingObject> livingObj = new ArrayList<LivingObject>();

	public ArrayList<LivingObject> lastLivingObj = new ArrayList<LivingObject>();

	public Player playerA;

	public Player playerB;
	/**
	 * 比赛玩家A
	 */
	public long sideA;

	/**
	 * 比赛玩家B
	 */
	public long sideB;

	int aWinCount;

	int bWinCount;

	int fightRound = 0;

	int errorCount = 0;

	int[] sideADamage = new int[3];
	int[] sideBDamage = new int[3];

	/**
	 * 
	 */
	public TournamentField(String id, BattleFieldInfo bi, Game game, int bornRegionXForA, int bornRegionYForA, int bornRegionXForB, int bornRegionYForB) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.bi = bi;
		this.bornRegionXForA = bornRegionXForA;
		this.bornRegionYForA = bornRegionYForA;
		this.bornRegionXForB = bornRegionXForB;
		this.bornRegionYForB = bornRegionYForB;
		this.game = game;
		this.r = new Random();
	}

	/**
	 * 
	 */
	public TournamentField(String id, BattleFieldInfo bi) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.bi = bi;
		this.r = new Random();
	}

	public int getState() {
		return state;
	}

	/**
	 * 获胜方 BATTLE_SIDE_C标识平局。 此变量只在对战模式下有用
	 * 
	 * @return
	 */
	public int getWinnerSide() {
		return winSide;
	}

	/**
	 * 设置战场各方的名称
	 * 
	 * @param names
	 * @return
	 */
	public void setBattleFieldSideNames(String names[]) {
		sideNames = names;
	}

	public String[] getBattleFieldSideNames() {
		return sideNames;
	}

	/**
	 * 返回战场的战况描述
	 * 
	 * @return
	 */
	public String getBattleFieldSituation() {
		return "";
	}

	/**
	 * 获得战场中所有可能出现的怪或者NPC的类型
	 * 
	 * @return
	 */
	public byte[] getAllSpriteTypeOnGame() {
		return new byte[0];
	}

	/**
	 * 获得一个随机出生点
	 * 
	 * @param side
	 * @return
	 */
	public Point getRandomBornPoint(int side) {
		if (side == BattleField.BATTLE_SIDE_A) {
			return new Point((int) (bornRegionXForA), (int) (bornRegionYForA));
		} else {
			return new Point((int) (bornRegionXForB), (int) (bornRegionYForB));
		}
	}

	public BattleFieldInfo getBattleFieldInfo() {
		return bi;
	}

	public String getId() {
		return id;
	}

	public int getBattleFightingType() {
		return bi.getBattleFightingType();
	}

	public long getEndTime() {

		return endTime;
	}

	public Game getGame() {

		return game;
	}

	public long getLastingTimeForNotEnoughPlayers() {
		return bi.getLastingTimeForNotEnoughPlayers();
	}

	public int getMaxPlayerLevel() {
		return bi.getMaxPlayerLevel();
	}

	public int getMaxPlayerNumOnOneSide() {
		return bi.getMaxPlayerNumOnOneSide();
	}

	public int getMinPlayerLevel() {
		return bi.getMinPlayerLevel();
	}

	public int getMinPlayerNumForStartOnOneSide() {

		return bi.getMinPlayerNumForStartOnOneSide();
	}

	public String getName() {

		return bi.getName();
	}

	public Player[] getPlayersBySide(int side) {
		ArrayList<Player> al = new ArrayList<Player>();
		LivingObject los[] = game.getLivingObjects();
		for (int i = 0; i < los.length; i++) {
			if (los[i] instanceof Player) {
				Player p = (Player) los[i];
				if (p.getBattleFieldSide() == side) {
					al.add(p);
				}
			}
		}
		if (side > 0) {
			if (al.size() > 1) {
				if (logger.isWarnEnabled()) logger.warn("[比武] [比武场各方人数] [side：" + side + "] [人数：" + al.size() + "] [" + this.getBattleFieldInfo().getDescription() + "] [" + this.getId() + "]");
			}
		}
		return al.toArray(new Player[0]);
	}

	public Player[] getPlayers() {
		ArrayList<Player> al = new ArrayList<Player>();
		LivingObject los[] = game.getLivingObjects();
		for (int i = 0; i < los.length; i++) {
			if (los[i] instanceof Player) {
				Player p = (Player) los[i];
				al.add(p);
			}
		}
		return al.toArray(new Player[0]);
	}

	public long getStartFightingTime() {
		return this.startFightingTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public void init() {
		startTime = SystemTime.currentTimeMillis();
		endTime = startTime + END_TIME;
		this.startFightingTime = startTime + 比赛开始前进入时间 + 每场开始前倒计时;
		state = TournamentField.STATE_WAITING_ENTER;
		if (logger.isInfoEnabled()) {
			logger.info("[比武] [初始化战场] [" + this.getId() + "] [" + this.getBattleFieldInfo().getDescription() + "] [" + startTime + "] [" + startFightingTime + "]");
		}
	}

	public void heartbeat() {
		long now = SystemTime.currentTimeMillis();

		BattleFieldLineupService service = BattleFieldLineupService.getInstance();

		if (now < startTime) {
			return;
		}

		try {
			switch (this.state) {
			case TournamentField.STATE_WAITING_ENTER:
				if (now > startTime + 比赛开始前进入时间) {
					if (!this.checkAbsent()) {
						this.state = TournamentField.STATE_WAITING_FIGHT;
						每场开始前对玩家进行处理();
						通知玩家即将开始比赛();
					}
				}
				break;
			case TournamentField.STATE_WAITING_FIGHT:
				if (now > this.startFightingTime) {
					fightRound++;
					this.notifyBattleStartFighting();
					if (!this.checkAbsent()) {
						this.state = TournamentField.STATE_FIGHTING;
						复活();
						通知玩家开始比赛((byte) fightRound);
					}
				} else {
				}
				break;

			case TournamentField.STATE_FIGHTING:
				if (now > this.startFightingTime + this.FIGHTING_TIME) {
					if (!inFightingArea(playerA)) {
						playerA.setHp(0);
						NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, playerA.getId(), (byte) Event.HP_INCREASED, 0);
						playerA.addMessageToRightBag(req);
						if (Game.logger.isWarnEnabled()) {
							Game.logger.warn("[擂台结算] [playerA不在比武区域] [设置为死亡] " + playerA.getLogString());
						}
					}
					if (!inFightingArea(playerB)) {
						playerB.setHp(0);
						NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, playerB.getId(), (byte) Event.HP_INCREASED, 0);
						playerB.addMessageToRightBag(req);
						if (Game.logger.isWarnEnabled()) {
							Game.logger.warn("[擂台结算] [playerB不在比武区域] [设置为死亡] " + playerB.getLogString());
						}
					}
					if (!this.checkAbsent()) {
						// 当比赛终止时，如果尚未分出胜负，则造成较高伤害者胜出，伤害如果相等，剩余血量多者胜出，如果血量相等，则随机一名胜利者
						int damageA = this.getDamageByPlayer(this.playerA);
						int damageB = this.getDamageByPlayer(this.playerB);
						for (int i = 1; i < fightRound; i++) {
							damageA -= sideADamage[i - 1];
							damageB -= sideBDamage[i - 1];
						}
						if (fightRound > 0) {
							sideADamage[fightRound - 1] = damageA;
							sideBDamage[fightRound - 1] = damageB;
						}
						if (damageA > 0 || damageB > 0) {
							if (damageA > damageB) {
								this.aWinCount++;
								this.transientWinerSide = BattleField.BATTLE_SIDE_A;
							} else if (damageB > damageA) {
								this.bWinCount++;
								this.transientWinerSide = BattleField.BATTLE_SIDE_B;
							} else {
								if (this.playerA.getHp() > this.playerB.getHp()) {
									this.aWinCount++;
									this.transientWinerSide = BattleField.BATTLE_SIDE_A;
								} else if (this.playerB.getHp() > this.playerA.getHp()) {
									this.bWinCount++;
									this.transientWinerSide = BattleField.BATTLE_SIDE_B;
								} else {
									int n = Math.abs(this.r.nextInt()) % 2;
									if (n == 0) {
										this.aWinCount++;
										this.transientWinerSide = BattleField.BATTLE_SIDE_A;
									} else {
										this.bWinCount++;
										this.transientWinerSide = BattleField.BATTLE_SIDE_B;
									}
								}
							}
							String winerName = "";
							String loserName = "";
							int winerDamage = 0;
							int loserDamage = 0;
							if (this.transientWinerSide == BattleField.BATTLE_SIDE_A) {
								winerName = this.playerA.getName();
								loserName = this.playerB.getName();
								winerDamage = damageA;
								loserDamage = damageB;
								// 活跃度统计
								ActivenessManager.getInstance().record(this.playerA, ActivenessType.武圣争夺战);
							} else {
								winerName = this.playerB.getName();
								loserName = this.playerA.getName();
								winerDamage = damageB;
								loserDamage = damageA;
								// 活跃度统计
								ActivenessManager.getInstance().record(this.playerB, ActivenessType.武圣争夺战);
							}
							this.publicizeResult(Translate.text_1901 + 0xff0000 + "]" + winerName + Translate.text_1902 + 0xff0000 + "]" + winerDamage + "[/color]，[color=" + 0xff0000 + "]" + loserName + Translate.text_1902 + 0xff0000 + "]" + loserDamage + Translate.text_1903 + 0xff0000 + "]" + winerName + Translate.text_1904 + 0xff0000 + "]" + loserName + "[/color]！");

							this.showResultTime = SystemTime.currentTimeMillis();
							if (logger.isInfoEnabled()) {
								logger.info("[比武] [按照伤害判定胜负] [胜利者：" + winerName + "] [失利者：" + loserName + "] [玩家A：" + this.playerA.getName() + "] [玩家A ID：" + this.playerA.getId() + "] [玩家A的伤害：" + damageA + "] [玩家B：" + this.playerB.getName() + "] [玩家B ID：" + this.playerB.getId() + "] [玩家B的伤害：" + damageB + "]");
							}
						} else {

							this.transientWinerSide = BattleField.BATTLE_SIDE_C;
							this.publicizeResult(Translate.无胜利者);
							if (logger.isInfoEnabled()) {
								logger.info("[比武] [按照伤害均为0，消极比赛无胜利者]");
							}
						}
						this.state = TournamentField.STATE_SHOW_RESULT;
						this.showResultTime = SystemTime.currentTimeMillis();
					}

				} else {
					if (!this.checkAbsent()) {
						if (battle_over_flag_60000 == false && this.startFightingTime + this.FIGHTING_TIME - now < 每场结束前倒计时) {
							battle_over_flag_60000 = true;
							通知玩家即将结束本回合比赛();
						}
					}
				}
				break;

			case TournamentField.STATE_SHOW_RESULT:
				String playerAName = "";
				String playerBName = "";
				if (playerA != null) {
					playerAName = playerA.getName();
				}
				if (playerB != null) {
					playerBName = playerB.getName();
				}
				if (!this.showResultFlag) {
					Player[] ps = this.getPlayers();
					String winer = "";
					String loser = "";
					if (ps != null) {
						try {
							if (transientWinerSide == BATTLE_SIDE_A) {
								winer = playerAName;
								loser = playerBName;
								if (playerA != null && playerA.getCurrentGame() == game) {
									TOURNAMENT_END_REQ req = new TOURNAMENT_END_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0);
									playerA.addMessageToRightBag(req);
								}
								if (playerB != null && playerB.getCurrentGame() == game) {
									TOURNAMENT_END_REQ req = new TOURNAMENT_END_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1);
									playerB.addMessageToRightBag(req);
								}
							} else if (transientWinerSide == BATTLE_SIDE_B) {
								winer = playerBName;
								loser = playerAName;
								if (playerA != null && playerA.getCurrentGame() == game) {
									TOURNAMENT_END_REQ req = new TOURNAMENT_END_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1);
									playerA.addMessageToRightBag(req);
								}
								if (playerB != null && playerB.getCurrentGame() == game) {
									TOURNAMENT_END_REQ req = new TOURNAMENT_END_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0);
									playerB.addMessageToRightBag(req);
								}
							}
						} catch (Exception ex) {

						}

						String des = null;
						if (transientWinerSide == BATTLE_SIDE_C) {
							des = Translate.由于消极比赛双方均失败;
							if (playerA != null && playerA.getCurrentGame() == game) {
								TOURNAMENT_END_REQ req = new TOURNAMENT_END_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1);
								playerA.addMessageToRightBag(req);
							}
							if (playerB != null && playerB.getCurrentGame() == game) {
								TOURNAMENT_END_REQ req = new TOURNAMENT_END_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1);
								playerB.addMessageToRightBag(req);
							}
						} else {
							des = Translate.translateString(Translate.获胜者失败者, new String[][] { { Translate.PLAYER_NAME_1, winer }, { Translate.PLAYER_NAME_2, loser }, { Translate.COUNT_1, (fightRound == 0 ? 1 : fightRound) + "" } });
						}

						for (Player p : ps) {
							HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, des);
							p.addMessageToRightBag(req);
						}
					}

					this.showResultFlag = true;
					if (logger.isInfoEnabled()) {
						logger.info("[比武] [" + this.getId() + "] [" + this.getBattleFieldInfo().getDescription() + "] [宣布" + (fightRound == 0 ? 1 : fightRound) + "回合结果] [winside:" + transientWinerSide + "] [sideA:" + sideA + "] [sideA:" + playerAName + "] [sideB:" + sideB + "] [sideB:" + playerBName + "]");
					}
				}

				if (!showResult2Flag && now > this.showResultTime + 3000) {
					showResult2Flag = true;
					String des = null;
					if (未到场结束) {
						if (winSide == BATTLE_SIDE_A) {
							des = Translate.translateString(Translate.由于某人没到场, new String[][] { { Translate.PLAYER_NAME_1, playerBName }, { Translate.PLAYER_NAME_2, playerAName } });
						} else if (winSide == BATTLE_SIDE_B) {
							des = Translate.translateString(Translate.由于某人没到场, new String[][] { { Translate.PLAYER_NAME_1, playerAName }, { Translate.PLAYER_NAME_2, playerBName } });
						} else {
							des = Translate.双方都没到场;
						}
					} else if (this.aWinCount >= 2) {
						des = Translate.translateString(Translate.某人先胜两局, new String[][] { { Translate.PLAYER_NAME_1, playerAName }, { Translate.PLAYER_NAME_2, playerBName } });
					} else if (this.bWinCount >= 2) {
						des = Translate.translateString(Translate.某人先胜两局, new String[][] { { Translate.PLAYER_NAME_1, playerBName }, { Translate.PLAYER_NAME_2, playerAName } });
					} else if (fightRound >= 3) {
						// 三局比赛结束，没有人赢得两场，此次比赛双方均失败
						des = Translate.由于消极比赛本场双方均失败;
					} else {
						// 比赛尚未结束，开始下一回合比赛
						des = Translate.translateString(Translate.即将开始下一局比赛, new String[][] { { Translate.COUNT_1, (fightRound + 1) + "" } });
					}
					Player[] ps = this.getPlayers();
					if (ps != null) {
						for (Player p : ps) {
							HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, des);
							p.addMessageToRightBag(req);
						}
					}
					if (logger.isInfoEnabled()) {
						logger.info("[比武] [" + this.getId() + "] [" + this.getBattleFieldInfo().getDescription() + "] [宣布竞技结果] [" + des + "] [winside:" + transientWinerSide + "] [sideA:" + sideA + "] [sideA:" + playerAName + "] [sideB:" + sideB + "] [sideB:" + playerBName + "]");
					}
				}
				if (now > this.showResultTime + 每场结束后宣布结果时间) {
					int damageA = this.getDamageByPlayer(this.playerA);
					int damageB = this.getDamageByPlayer(this.playerB);
					for (int i = 1; i < fightRound; i++) {
						damageA -= sideADamage[i - 1];
						damageB -= sideBDamage[i - 1];
					}
					if (fightRound > 0) {
						sideADamage[fightRound - 1] = damageA;
						sideBDamage[fightRound - 1] = damageB;
					}
					if (未到场结束) {
						this.state = TournamentField.STATE_CLOSING;
						closingTime = now;
						复活();
					} else if (this.aWinCount >= 2) {
						this.matchEnd(this.playerA.getId());
						复活();
						this.state = TournamentField.STATE_CLOSING;
						closingTime = now;
					} else if (this.bWinCount >= 2) {
						this.matchEnd(this.playerB.getId());
						复活();
						this.state = TournamentField.STATE_CLOSING;
						closingTime = now;
					} else if (fightRound >= 3) {
						// 三局比赛结束，没有人赢得两场，此次比赛双方均失败
						this.matchEnd(0);
						复活();
						this.state = TournamentField.STATE_CLOSING;
						closingTime = now;
					} else {
						// 比赛尚未结束，开始下一回合比赛
						this.showResultFlag = false;
						this.showResult2Flag = false;
						this.state = TournamentField.STATE_WAITING_FIGHT;
						this.startFightingTime = SystemTime.currentTimeMillis() + 每场开始前倒计时;
						battle_start_flag_5000 = false;
						battle_start_flag_4000 = false;
						battle_start_flag_3000 = false;
						battle_start_flag_2000 = false;
						battle_start_flag_1000 = false;
						battle_over_flag_10000 = false;
						battle_over_flag_30000 = false;
						battle_over_flag_60000 = false;
						每场开始前对玩家进行处理();
						通知玩家即将开始比赛();
					}
				}

				break;

			case TournamentField.STATE_CLOSING:
				if (now > closingTime + 2000) {
					Player players[] = getPlayers();
					for (int i = 0; i < players.length; i++) {
						if (service.isCrossMode()) {

						} else {
							TransportData td = null;
							String mapName = TransportData.getMainCityMap(players[i].getCountry());
							Game game2 = GameManager.getInstance().getGameByName(mapName, players[i].getCountry());
							MapArea area = game2.gi.getMapAreaByName(Translate.出生点);
							if (area != null) {
								td = new TransportData(0, 0, 0, 0, mapName, (int) (area.getX() + Math.random() * area.getWidth()), (int) (area.getY() + Math.random() * area.getHeight()));
							} else {
								td = new TransportData(0, 0, 0, 0, mapName, 1660, 1026);
							}
							game.transferGame(players[i], td);
							players[i].setDuelFieldState(0);
							if (logger.isInfoEnabled()) {
								logger.info("[比武] [" + this.getId() + "] [" + this.getBattleFieldInfo().getDescription() + "] [踢出玩家] [game2:"+game2+"] [mapName:"+mapName+"] [x:"+(area!=null?area.getX():"null")+"] [x:"+(area!=null?area.getY():"null")+"] [DuelFieldState：" + players[i].getDuelFieldState() + "] [玩家：" + players[i].getName() + "] [玩家ID：" + players[i].getId() + "]");
							}
						}
					}
					this.endTime = now;
					BattleFieldManager m = BattleFieldManager.getInstance();
					m.notidyBattleFieldEnd(this);

					this.state = TournamentField.STATE_CLOSED;
					if (logger.isInfoEnabled()) {
						logger.info("[比武] [" + this.getId() + "] [" + this.getBattleFieldInfo().getDescription() + "] [结束]");
					}
				}
				break;

			case TournamentField.STATE_CLOSED:

				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("[比武] [" + this.getId() + "] [" + this.getBattleFieldInfo().getDescription() + "] [战场心跳出错] [" + e + "]", e);

		}

		long gameTime = SystemTime.currentTimeMillis();
		this.game.heartbeat();
		gameTime = SystemTime.currentTimeMillis() - gameTime;

		if (this.getState() != TournamentField.STATE_CLOSED) {
			if (now > this.endTime && this.endTime > 0) {
				Player players[] = getPlayers();
				for (int i = 0; i < players.length; i++) {
					if (service.isCrossMode()) {

					} else {
						TransportData td = new TransportData(0, 0, 0, 0, players[i].getEnterBattleFieldMapName(), players[i].getEnterBattleFieldX(), players[i].getEnterBattleFieldY());
						this.game.transferGame(players[i], td);
						players[i].setDuelFieldState(0);
						if (logger.isInfoEnabled()) {
							logger.info("[比武] [" + this.getId() + "] [" + this.getBattleFieldInfo().getDescription() + "] [踢出玩家2] [玩家：" + players[i].getName() + "] [玩家ID：" + players[i].getId() + "]");
						}
					}
				}
				BattleFieldManager m = BattleFieldManager.getInstance();
				m.notidyBattleFieldEnd(this);

				this.state = TournamentField.STATE_CLOSED;
				if (logger.isInfoEnabled()) {
					logger.info("[比武] [" + this.getId() + "] [" + this.getBattleFieldInfo().getDescription() + "] [结束2]");
				}
			}
		}

		long t = SystemTime.currentTimeMillis() - now;
		if (t > 20) {
			Player[] pp = this.getPlayers();
			int num = 0;
			if (pp != null) {
				num = pp.length;
			}
			if (logger.isWarnEnabled()) logger.warn("[比武] [" + this.getId() + "] [" + this.getBattleFieldInfo().getDescription() + "] [线程超时] [耗时：" + t + "] [" + gameTime + "] [人数：" + num + "]");
		}

	}

	public void 通知玩家开始比赛(byte roundIndex) {
		Player[] ps = this.getPlayers();
		if (ps != null) {
			String[] playerNames = new String[2];
			String playerAName = "";
			String playerBName = "";
			byte[] careers = new byte[2];
			if (this.playerA != null) {
				playerAName = playerA.getName();
				careers[0] = playerA.getCareer();
			}
			if (this.playerB != null) {
				playerBName = playerB.getName();
				careers[1] = playerB.getCareer();
			}
			playerNames[0] = playerAName;
			playerNames[1] = playerBName;
			TOURNAMENT_START_REQ req = new TOURNAMENT_START_REQ(GameMessageFactory.nextSequnceNum(), roundIndex, playerNames, careers);
			for (Player p : ps) {
				p.addMessageToRightBag(req);
			}
		}
	}

	public void 通知玩家即将开始比赛() {
		Player[] ps = this.getPlayers();
		int leftTime = (int) (每场开始前倒计时 / 1000);
		TOURNAMENT_TIME_REQ req = new TOURNAMENT_TIME_REQ(GameMessageFactory.nextSequnceNum(), leftTime, (byte) 1);
		if (ps != null) {
			for (Player p : ps) {
				p.addMessageToRightBag(req);
			}
		}

	}

	public void 通知玩家即将结束本回合比赛() {
		Player[] ps = this.getPlayers();
		int leftTime = (int) (每场结束前倒计时 / 1000);
		TOURNAMENT_TIME_REQ req = new TOURNAMENT_TIME_REQ(GameMessageFactory.nextSequnceNum(), leftTime, (byte) 2);
		if (ps != null) {
			for (Player p : ps) {
				p.addMessageToRightBag(req);
			}
		}

	}

	private boolean rewardExpFlagForA[] = new boolean[10];
	private boolean rewardExpFlagForB[] = new boolean[10];

	private void calculateScore() {

	}

	public void 每场开始前对玩家进行处理() {
		复活();
		每场开始前清除负面状态();
		每场开始前传送到进入点(this.playerA);
		每场开始前传送到进入点(this.playerB);
		上定身buff(this.playerA, 每场开始前倒计时);
		上定身buff(this.playerB, 每场开始前倒计时);
		if (this.playerA != null) {
			this.playerA.resetShouStat("武圣开场前");
		}
		if (this.playerB != null) {
			this.playerB.resetShouStat("武圣开场前");
		}
	}

	public void 复活() {
		if (this.playerA.getCurrentGame() == game) {
			this.playerA.setHp(this.playerA.getMaxHP());
			this.playerA.setMp(this.playerA.getMaxMP());
			if (this.playerA.getState() == Player.STATE_DEAD) {
				this.playerA.setState(Player.STATE_STAND);
				this.playerA.notifyRevived();
				PLAYER_REVIVED_RES res = new PLAYER_REVIVED_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.战场免费复活成功, this.playerA.getHp(), this.playerA.getMp());
				this.playerA.addMessageToRightBag(res);
			}
		}

		if (this.playerB.getCurrentGame() == game) {
			this.playerB.setHp(this.playerB.getMaxHP());
			this.playerB.setMp(this.playerB.getMaxMP());
			if (this.playerB.getState() == Player.STATE_DEAD) {
				this.playerB.setState(Player.STATE_STAND);
				this.playerB.notifyRevived();
				PLAYER_REVIVED_RES res = new PLAYER_REVIVED_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.战场免费复活成功, this.playerB.getHp(), this.playerB.getMp());
				this.playerB.addMessageToRightBag(res);
			}
		}
	}

	public void 上定身buff(Player player, long lastingTime) {
		// if(player == null){
		// return;
		// }
		// if(!player.isInBattleField()){
		// return;
		// }
		// if(player.getCurrentGame() != game){
		// return;
		// }
		// BuffTemplateManager btm = BuffTemplateManager.getInstance();
		// if(btm != null){
		// BuffTemplate bt = btm.getBuffTemplateByName(Translate.比武开始时给玩家上的buff);
		// if(bt != null){
		// Buff buff = bt.createBuff(1);
		// buff.setStartTime(SystemTime.currentTimeMillis());
		// buff.setInvalidTime(SystemTime.currentTimeMillis() + lastingTime);
		// if(player != null && game.contains(player)){
		// buff.setCauser(player);
		// player.placeBuff(buff);
		// }
		// }
		// }
	}

	public void 每场开始前传送到进入点(Player player) {
		if (player == null) {
			return;
		}
		if (!player.isInBattleField()) {
			return;
		}
		if (player.getCurrentGame() != game) {
			return;
		}
		if (player.getBattleFieldSide() == BATTLE_SIDE_A) {
			int x = (int) (fightRegionXForA);
			int y = (int) (fightRegionYForA);
			game.transferGame(player, new TransportData(0, 0, 0, 0, game.gi.getName(), x, y));
		} else if (player.getBattleFieldSide() == BATTLE_SIDE_B) {
			int x = (int) (fightRegionXForB);
			int y = (int) (fightRegionYForB);
			game.transferGame(player, new TransportData(0, 0, 0, 0, game.gi.getName(), x, y));
		}
	}

	private void 每场开始前清除负面状态() {
		if (playerA != null && playerA.isOnline() && game.contains(playerA)) {
			List<Buff> buffs = playerA.getAllBuffs();
			for (int i = buffs.size() - 1; i >= 0; i--) {
				Buff buff = buffs.get(i);
				try {
					if (buff.getTemplate().clearSkillPointNotdisappear) {
						if (logger.isInfoEnabled()) {
							logger.info("[每场开始比武保留buff:] [" + buff.getTemplateName() + "] [" + buff.getClass() + "] [level:" + buff.getLevel() + "] [" + playerA.getUsername() + "] [" + playerA.getId() + "] [" + playerA.getName() + "]");
						}

						continue;
					}
				} catch (Exception e) {

				}
				try {
					if (!buff.isAdvantageous() && !buff.getTemplateName().trim().equals(RobberyConstant.FAILBUFF)) {
						buff.end(playerA);
						if (buff.isForover() || buff.isSyncWithClient()) {
							NOTIFY_BUFF_REMOVED_REQ req = new NOTIFY_BUFF_REMOVED_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, playerA.getId(), buff.getSeqId());
							playerA.addMessageToRightBag(req);
						}
						buffs.remove(i);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					if (logger.isWarnEnabled()) logger.warn("[每场开始前清除负面状态] [异常]", ex);
				}
			}
		}
		if (playerB != null && playerB.isOnline() && game.contains(playerB)) {
			List<Buff> buffs = playerB.getAllBuffs();
			for (int i = buffs.size() - 1; i >= 0; i--) {
				Buff buff = buffs.get(i);
				try {
					if (buff.getTemplate().clearSkillPointNotdisappear) {
						if (logger.isInfoEnabled()) {
							logger.info("[每场开始比武保留buff:] [" + buff.getTemplateName() + "] [" + buff.getClass() + "] [level:" + buff.getLevel() + "] [" + playerB.getUsername() + "] [" + playerB.getId() + "] [" + playerB.getName() + "]");
						}

						continue;
					}
				} catch (Exception e) {

				}
				try {
					if (!buff.isAdvantageous() && !buff.getTemplateName().trim().equals(RobberyConstant.FAILBUFF)) {
						buff.end(playerB);
						if (buff.isForover() || buff.isSyncWithClient()) {
							NOTIFY_BUFF_REMOVED_REQ req = new NOTIFY_BUFF_REMOVED_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, playerB.getId(), buff.getSeqId());
							playerB.addMessageToRightBag(req);
						}
						buffs.remove(i);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					if (logger.isWarnEnabled()) logger.warn("[每场开始前清除负面状态] [异常]", ex);
				}
			}
		}
	}

	/**
	 * 战场开启，打开门， 刷出各个资源点的旗帜
	 */
	private void notifyBattleStartFighting() {
		LivingObject los[] = game.getLivingObjects();
		for (int i = 0; i < los.length; i++) {
			if (los[i] instanceof DoorNpc) {
				DoorNpc dn = (DoorNpc) los[i];
				dn.openDoor(game);
			}
		}
	}

	/**
	 * 通知客户端，战场结束
	 */
	private void notifyBattleOver() {
		long ct = SystemTime.currentTimeMillis();
		if (winSide == BATTLE_SIDE_C) {
			Player ps[] = this.getPlayers();
			for (int i = 0; i < ps.length; i++) {
				Player p = ps[i];

				int exp = (int) ((45 + p.getLevel() * 12) * (0.1 * p.getLevel() * p.getLevel() + 125) * 0.1 * bi.getHolidyRewardParam());
				// p.addExp(exp, Player.ADDEXP_REASON_BATTLE);

				BattleFieldStatData sd = getBattleFieldStatDataByPlayer(p);
				// sd.honorPoints += bi.getHolidyRewardParam()
				// * bi.getBattleRewardParam()
				// * (100 + p.getHonorPercent()) / 100;

				BillingCenter billing = BillingCenter.getInstance();
				try {
					// billing.playerSaving(p, bi.getHolidyRewardParam()
					// * bi.getBattleRewardParam()
					// * (100 + p.getHonorPercent()) / 100,
					// CurrencyType.HONOR,
					// SavingReasonType.BATTLE_FIELD_REWARD,
					// new Object[] { this.getName() });
				} catch (Exception e) {
					if (logger.isWarnEnabled()) logger.warn("[荣誉奖励] [失败] [" + p.getUsername() + "] [" + p.getName() + "] [" + this.getName() + "] [" + this.getId() + "] [荣誉值：" + (bi.getHolidyRewardParam() * bi.getBattleRewardParam()) + "]", e);
				}

				p.send_HINT_REQ(Translate.text_1775 + (bi.getHolidyRewardParam() * bi.getBattleRewardParam()) + Translate.text_1776);
			}
		} else if (winSide == BATTLE_SIDE_A) {
			Player ps[] = this.getPlayers();
			for (int i = 0; i < ps.length; i++) {
				Player p = ps[i];
				if (p.getBattleFieldSide() == BATTLE_SIDE_A) {
					int exp = (int) ((45 + p.getLevel() * 12) * (0.1 * p.getLevel() * p.getLevel() + 125) * 0.2 * bi.getHolidyRewardParam());
					// p.addExp(exp, Player.ADDEXP_REASON_BATTLE);

					BattleFieldStatData sd = getBattleFieldStatDataByPlayer(p);
					// sd.honorPoints += bi.getHolidyRewardParam()
					// * bi.getBattleRewardParam() * 2
					// * (100 + p.getHonorPercent()) / 100;

					BillingCenter billing = BillingCenter.getInstance();
					try {
						// billing.playerSaving(p, bi.getHolidyRewardParam()
						// * bi.getBattleRewardParam() * 2
						// * (100 + p.getHonorPercent()) / 100,
						// CurrencyType.HONOR,
						// SavingReasonType.BATTLE_FIELD_REWARD,
						// new Object[] { this.getName() });
					} catch (Exception e) {
						if (logger.isWarnEnabled()) logger.warn("[荣誉奖励] [失败] [" + p.getUsername() + "] [" + p.getName() + "] [" + this.getName() + "] [" + this.getId() + "] [荣誉值：" + (bi.getHolidyRewardParam() * bi.getBattleRewardParam() * 2) + "]", e);
					}

					p.send_HINT_REQ(Translate.text_1775 + (bi.getHolidyRewardParam() * bi.getBattleRewardParam() * 2) + Translate.text_1776);

					p.winOneBattleField(bi.getMapName());

					// StatDataUpdateManager.getInstance().update(p,
					// StatData.STAT_COMBAT_5V5_WIN_TIMES, 1, ct);
				} else if (p.getBattleFieldSide() == BATTLE_SIDE_B) {
					int exp = (int) ((45 + p.getLevel() * 12) * (0.1 * p.getLevel() * p.getLevel() + 125) * 0.1 * bi.getHolidyRewardParam());
					// p.addExp(exp, Player.ADDEXP_REASON_BATTLE);

					BattleFieldStatData sd = getBattleFieldStatDataByPlayer(p);
					// sd.honorPoints += bi.getHolidyRewardParam()
					// * bi.getBattleRewardParam()
					// * (100 + p.getHonorPercent()) / 100;

					BillingCenter billing = BillingCenter.getInstance();
					try {
						// billing.playerSaving(p, bi.getHolidyRewardParam()
						// * bi.getBattleRewardParam()
						// * (100 + p.getHonorPercent()) / 100,
						// CurrencyType.HONOR,
						// SavingReasonType.BATTLE_FIELD_REWARD,
						// new Object[] { this.getName() });
					} catch (Exception e) {
						if (logger.isWarnEnabled()) logger.warn("[荣誉奖励] [失败] [" + p.getUsername() + "] [" + p.getName() + "] [" + this.getName() + "] [" + this.getId() + "] [荣誉值：" + (bi.getHolidyRewardParam() * bi.getBattleRewardParam()) + "]", e);
					}

					p.send_HINT_REQ(Translate.text_1775 + (bi.getHolidyRewardParam() * bi.getBattleRewardParam()) + Translate.text_1776);
				}
			}
		} else if (winSide == BATTLE_SIDE_B) {
			Player ps[] = this.getPlayers();
			for (int i = 0; i < ps.length; i++) {
				Player p = ps[i];
				if (p.getBattleFieldSide() == BATTLE_SIDE_B) {
					int exp = (int) ((45 + p.getLevel() * 12) * (0.1 * p.getLevel() * p.getLevel() + 125) * 0.2 * bi.getHolidyRewardParam());
					// p.addExp(exp, Player.ADDEXP_REASON_BATTLE);

					BattleFieldStatData sd = getBattleFieldStatDataByPlayer(p);
					// sd.honorPoints += bi.getHolidyRewardParam()
					// * bi.getBattleRewardParam() * 2
					// * (100 + p.getHonorPercent()) / 100;

					BillingCenter billing = BillingCenter.getInstance();
					try {
						// billing.playerSaving(p, bi.getHolidyRewardParam()
						// * bi.getBattleRewardParam() * 2
						// * (100 + p.getHonorPercent()) / 100,
						// CurrencyType.HONOR,
						// SavingReasonType.BATTLE_FIELD_REWARD,
						// new Object[] { this.getName() });
					} catch (Exception e) {
						if (logger.isWarnEnabled()) logger.warn("[荣誉奖励] [失败] [" + p.getUsername() + "] [" + p.getName() + "] [" + this.getName() + "] [" + this.getId() + "] [荣誉值：" + (bi.getHolidyRewardParam() * bi.getBattleRewardParam() * 2) + "]", e);
					}
					p.winOneBattleField(bi.getMapName());

					p.send_HINT_REQ(Translate.text_1775 + (bi.getHolidyRewardParam() * bi.getBattleRewardParam() * 2) + Translate.text_1776);
					// StatDataUpdateManager.getInstance().update(p,
					// StatData.STAT_COMBAT_5V5_WIN_TIMES, 1, ct);
				} else if (p.getBattleFieldSide() == BATTLE_SIDE_A) {
					int exp = (int) ((45 + p.getLevel() * 12) * (0.1 * p.getLevel() * p.getLevel() + 125) * 0.1 * bi.getHolidyRewardParam());
					// p.addExp(exp, Player.ADDEXP_REASON_BATTLE);

					BattleFieldStatData sd = getBattleFieldStatDataByPlayer(p);
					// sd.honorPoints += bi.getHolidyRewardParam()
					// * bi.getBattleRewardParam()
					// * (100 + p.getHonorPercent()) / 100;

					BillingCenter billing = BillingCenter.getInstance();
					try {
						// billing.playerSaving(p, bi.getHolidyRewardParam()
						// * bi.getBattleRewardParam()
						// * (100 + p.getHonorPercent()) / 100,
						// CurrencyType.HONOR,
						// SavingReasonType.BATTLE_FIELD_REWARD,
						// new Object[] { this.getName() });
					} catch (Exception e) {
						if (logger.isWarnEnabled()) logger.warn("[荣誉奖励] [失败] [" + p.getUsername() + "] [" + p.getName() + "] [" + this.getName() + "] [" + this.getId() + "] [荣誉值：" + (bi.getHolidyRewardParam() * bi.getBattleRewardParam()) + "]", e);
					}
					p.send_HINT_REQ(Translate.text_1775 + (bi.getHolidyRewardParam() * bi.getBattleRewardParam()) + Translate.text_1776);
				}
			}
		}

		// 通知客户端
		Player ps[] = this.getPlayers();
		for (int i = 0; i < ps.length; i++) {
			Player p = ps[i];
			BattleFieldLineupService service = BattleFieldLineupService.getInstance();
			if (getWinnerSide() == BattleField.BATTLE_SIDE_A) {

				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 2, Translate.text_1777 + this.sideNames[BattleField.BATTLE_SIDE_A]);
				p.addMessageToRightBag(req);

				QUERY_BATTLEFIELD_INFO_RES res = new QUERY_BATTLEFIELD_INFO_RES(GameMessageFactory.nextSequnceNum(), getName(), true, this.sideNames[BattleField.BATTLE_SIDE_A] + Translate.text_1778, service.getBattleFieldStatDataByPlayer(p));
				p.addMessageToRightBag(res);
			} else if (getWinnerSide() == BattleField.BATTLE_SIDE_B) {

				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 2, Translate.text_1777 + this.sideNames[BattleField.BATTLE_SIDE_B]);
				p.addMessageToRightBag(req);

				QUERY_BATTLEFIELD_INFO_RES res = new QUERY_BATTLEFIELD_INFO_RES(GameMessageFactory.nextSequnceNum(), getName(), true, this.sideNames[BattleField.BATTLE_SIDE_B] + Translate.text_1778, service.getBattleFieldStatDataByPlayer(p));
				p.addMessageToRightBag(res);
			} else {

				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 2, Translate.text_1779);
				p.addMessageToRightBag(req);

				QUERY_BATTLEFIELD_INFO_RES res = new QUERY_BATTLEFIELD_INFO_RES(GameMessageFactory.nextSequnceNum(), getName(), true, Translate.text_1780, service.getBattleFieldStatDataByPlayer(p));
				p.addMessageToRightBag(res);
			}
		}
	}

	public boolean isOpen() {
		long now = SystemTime.currentTimeMillis();
		if (now >= startTime && now <= endTime) return true;
		return false;
	}

	/**
	 * 玩家的信息
	 */
	public Hashtable<Long, BattleFieldStatData> getStatDataMap() {
		return this.statDataMap;
	}

	public void notifyBattleFieldEndCauseSystemExit() {
		// TODO Auto-generated method stub

	}

	private synchronized BattleFieldStatData getBattleFieldStatDataByPlayer(Player p) {
		if (p == null) {
			return null;
		}
		BattleFieldStatData sd = statDataMap.get(p.getId());
		if (sd == null) {
			sd = new BattleFieldStatData();
			CareerManager cm = CareerManager.getInstance();
			sd.career = cm.getCareer(p.getCareer()).getName();
			sd.playerId = p.getId();
			sd.playerLevel = p.getLevel();

			sd.playerName = p.getName();
			sd.battleSide = p.getBattleFieldSide();
			sd.description = "";
			statDataMap.put(p.getId(), sd);
		}
		return sd;
	}

	public void notifyCauseDamage(Fighter causter, Fighter target, int damage) {
		if (state != TournamentField.STATE_FIGHTING) return;

		if (causter instanceof Player) {
			Player p = (Player) causter;

			BattleFieldStatData sd = getBattleFieldStatDataByPlayer(p);
			if (sd != null) {
				sd.totalDamage += damage;
			}
			if (logger.isInfoEnabled()) {
				logger.info("[比武] [" + this.getId() + "] [" + this.getBattleFieldInfo().getDescription() + "] [玩家造成伤害] [施加者：" + p.getName() + "] [施加者ID：" + p.getId() + "] [目标：" + target.getName() + "] [目标ID：" + target.getId() + "] [伤害量：" + damage + "]");
			}
		}

	}

	public void notifyEnhenceHp(Fighter causter, Fighter target, int hp) {

		if (state != TournamentField.STATE_FIGHTING) return;

		if (causter instanceof Player) {
			Player p = (Player) causter;

			BattleFieldStatData sd = getBattleFieldStatDataByPlayer(p);
			if (sd != null) {
				sd.totalEnhenceHp += hp;
			}
			if (logger.isInfoEnabled()) {
				logger.info("[比武] [" + this.getId() + "] [" + this.getBattleFieldInfo().getDescription() + "] [玩家施加治疗] [施加者：" + p.getName() + "] [施加者ID：" + p.getId() + "] [目标：" + target.getName() + "] [目标ID：" + target.getId() + "] [治疗量：" + hp + "]");
			}
		}

	}

	public void notifyKilling(Fighter killer, Fighter killed) {

	}

	/**
	 * 玩家进入战场
	 * 
	 * @param player
	 * @param mapName
	 *            玩家进入战场时所在的地图
	 * @param x
	 *            玩家进入战场是的位置
	 * @param y
	 *            玩家进入战场是的位置
	 */
	public void notifyPlayerEnter(Player player) {
		getBattleFieldStatDataByPlayer(player);

		// 使得Around_change通知客户端
		player.setBattleFieldSide(player.getBattleFieldSide());

		if (player.getBattleFieldSide() == BattleField.BATTLE_SIDE_A || player.getBattleFieldSide() == BattleField.BATTLE_SIDE_B) {
			player.setDuelFieldState(1);
			int leftTime = (int) ((startTime + 比赛开始前进入时间 - SystemTime.currentTimeMillis()) / 1000);
			TOURNAMENT_TIME_REQ req = new TOURNAMENT_TIME_REQ(GameMessageFactory.nextSequnceNum(), leftTime, (byte) 0);
			player.addMessageToRightBag(req);
		} else {
			player.setDuelFieldState(2);
		}

		GameInfo gi = this.game.getGameInfo();
		player.setViewWidth(gi.getWidth());
		player.setViewHeight(gi.getHeight());
		if (player.getDuelFieldState() == 1) {
			this.goIntoBattleField(player);
		}

		if (player.getDuelFieldState() == 1 && player.getTeamMark() != Player.TEAM_MARK_NONE) {
			Team team = player.getTeam();
			if (team != null) {

				Player ps[] = team.getMembers().toArray(new Player[0]);

				team.removeMember(player, 0);

				// 让客户端清空费队友
				for (int i = 0; i < ps.length; i++) {
					if (ps[i].getAroundNotifyPlayerNum() == 0) {
						ps[i].setNewlySetAroundNotifyPlayerNum(true);
					}
				}

				if (logger.isInfoEnabled()) {
					logger.info("[比武] [玩家离队] [成功] [" + this.getId() + "] [" + this.getBattleFieldInfo().getDescription() + "] [SIDE：" + player.getBattleFieldSide() + "] [DuelFieldState：" + player.getDuelFieldState() + "] [玩家：" + player.getName() + "] [玩家ID：" + player.getId() + "]");
				}

			}
		}

		// 如果乘坐飞行坐骑，那么下坐骑
		if (player.isIsUpOrDown() && player.isFlying()) {
			player.downFromHorse();
		}
		if (player.getId() == this.sideA) {
			this.playerA = player;
		} else if (player.getId() == this.sideB) {
			this.playerB = player;
		}
		TOURNAMENT_QUERY_SIDE_REQ req = new TOURNAMENT_QUERY_SIDE_REQ(GameMessageFactory.nextSequnceNum(), sideA, sideB);
		player.addMessageToRightBag(req);
		if (TournamentManager.getInstance().noticePlayerIcon) {
			TOURNAMENT_QUERY_PLAYER_ICON_INFO_RES resb = new TOURNAMENT_QUERY_PLAYER_ICON_INFO_RES(GameMessageFactory.nextSequnceNum(), playerB.getId(), playerB.getCareer(), playerB.getName(), playerB.getLevel());
			playerA.addMessageToRightBag(resb);
			TOURNAMENT_QUERY_PLAYER_ICON_INFO_RES resa = new TOURNAMENT_QUERY_PLAYER_ICON_INFO_RES(GameMessageFactory.nextSequnceNum(), playerA.getId(), playerA.getCareer(), playerA.getName(), playerA.getLevel());
			playerB.addMessageToRightBag(resa);
		}
		if (logger.isInfoEnabled()) {
			logger.info("[比武] [" + this.getId() + "] [" + this.getBattleFieldInfo().getDescription() + "] [玩家进入] [SIDE：" + player.getBattleFieldSide() + "] [DuelFieldState：" + player.getDuelFieldState() + "] [玩家：" + player.getName() + "] [玩家ID：" + player.getId() + "]");
		}
	}

	public void notifyPlayerLeave(Player player) {
		player.setDuelFieldState(0);
		player.setBattleField(null);
		player.setBattleFieldSide((byte) BattleField.BATTLE_SIDE_C);
		player.setInBattleField(false);
		player.currentGame = null;
		DISPLAY_INFO_ON_SCREEN req = new DISPLAY_INFO_ON_SCREEN(GameMessageFactory.nextSequnceNum(), new String[0], new int[0], false);
		player.addMessageToRightBag(req);
		if (logger.isInfoEnabled()) {
			logger.info("[比武] [" + this.getId() + "] [" + this.getBattleFieldInfo().getDescription() + "] [玩家离开] [SIDE：" + player.getBattleFieldSide() + "] [DuelFieldState：" + player.getDuelFieldState() + "] [玩家：" + player.getName() + "] [玩家ID：" + player.getId() + "]");
		}
	}

	public void playerDead(Player player) {
		int k = 5;
		// PLAYER_DEAD_REQ req = new PLAYER_DEAD_REQ(GameMessageFactory
		// .nextSequnceNum(), player.getId(), k);
		// player.addMessageToRightBag(req);

		if (this.state == TournamentField.STATE_FIGHTING) {
			Player[] playerA = this.getPlayersBySide(BattleField.BATTLE_SIDE_A);
			Player[] playerB = this.getPlayersBySide(BattleField.BATTLE_SIDE_B);
			if (player.getId() == this.sideA) {
				this.bWinCount++;
				this.transientWinerSide = BattleField.BATTLE_SIDE_B;
				String winerName = "";
				String loserName = "";
				winerName = playerB[0].getName();
				loserName = playerA[0].getName();
				this.publicizeResult("[color=" + 0xff0000 + "]" + winerName + Translate.text_1910 + 0xff0000 + "]" + loserName + Translate.text_1911);

				// 活跃度统计
				ActivenessManager.getInstance().record(playerB[0], ActivenessType.武圣争夺战);
			} else if (player.getId() == this.sideB) {
				this.aWinCount++;
				this.transientWinerSide = BattleField.BATTLE_SIDE_A;
				String winerName = "";
				String loserName = "";
				winerName = playerA[0].getName();
				loserName = playerB[0].getName();
				this.publicizeResult("[color=" + 0xff0000 + "]" + winerName + Translate.text_1910 + 0xff0000 + "]" + loserName + Translate.text_1911);
				// 活跃度统计
				ActivenessManager.getInstance().record(playerA[0], ActivenessType.武圣争夺战);
			}
			this.state = TournamentField.STATE_SHOW_RESULT;
			this.showResultTime = SystemTime.currentTimeMillis();
		}

		String nameA = "";
		String nameB = "";
		long idA = 0;
		long idB = 0;
		if (this.playerA != null) {
			nameA = this.playerA.getName();
			idA = this.playerA.getId();
		}
		if (this.playerB != null) {
			nameB = this.playerB.getName();
			idB = this.playerB.getId();
		}

		if (logger.isInfoEnabled()) {
			logger.info("[比武] [玩家死亡，回合结束] [" + this.getId() + "] [" + this.getBattleFieldInfo().getDescription() + "] [死亡玩家：" + player.getName() + "] [死亡玩家ID：" + player.getId() + "] [玩家A：" + nameA + "] [玩家A ID：" + idA + "] [玩家B：" + nameB + "] [玩家B ID：" + idB + "]");
		}
	}

	public void playerRevived(Player player, PLAYER_REVIVED_REQ req) {
		if (req.getRevivedType() == 0) {
			player.setHp(player.getMaxHP());
			player.setMp(player.getMaxMP());
			player.setState(Player.STATE_STAND);

			player.notifyRevived();

			PLAYER_REVIVED_RES res = new PLAYER_REVIVED_RES(req.getSequnceNum(), (byte) 0, Translate.战场免费复活成功, player.getHp(), player.getMp());
			player.addMessageToRightBag(res);
			//
			if (player.getBattleFieldSide() == BATTLE_SIDE_A) {
				int x = (int) (fightRegionXForA);
				int y = (int) (fightRegionYForA);
				game.transferGame(player, new TransportData(0, 0, 0, 0, game.gi.getName(), x, y));
			} else {
				int x = (int) (fightRegionXForB);
				int y = (int) (fightRegionYForB);
				game.transferGame(player, new TransportData(0, 0, 0, 0, game.gi.getName(), x, y));
			}

		}
	}

	public void setGame(Game game) {
		this.game = game;
	}

	/**
	 * 通知战场中某一方的玩家信息
	 * 
	 * @param message
	 * @param side
	 */
	protected void notifyOneSidePlayer(String message, int side) {
		LivingObject los[] = game.getLivingObjects();
		for (int i = 0; i < los.length; i++) {
			if (los[i] instanceof Player) {
				Player p = (Player) los[i];
				if (p.getBattleFieldSide() == side) {
					p.send_HINT_REQ(message);
				}
			}
		}
	}

	/**
	 * 有音效的提示
	 * 
	 * @param message
	 * @param side
	 */
	protected void notifySoundOneSidePlayer(String message, int side) {
		LivingObject los[] = game.getLivingObjects();
		for (int i = 0; i < los.length; i++) {
			if (los[i] instanceof Player) {
				Player p = (Player) los[i];
				if (p.getBattleFieldSide() == side) {
					// p.send_HINT_REQ(message);
					// HINT_REQ req = new HINT_REQ();
					HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 2, message);
					p.addMessageToRightBag(req);
				}
			}
		}
	}

	public DISPLAY_INFO_ON_SCREEN generateBattleFieldStatus(int watcherNum) {
		// watcherNum=(int)(watcherNum*BattleOrganizingCommitteeManager.watcherNumMultiple);
		// String lineContents[] = new String[3];
		// int lineColors[] = new int[3];
		// long ct = SystemTime.currentTimeMillis();
		// BattleOrganizingCommitteeManager bocm = BattleOrganizingCommitteeManager
		// .getInstance();
		// String matchState = "";
		// int a = 0;
		// int b = 0;
		// if (ct >= bocm.getFinal1BeginTime()) {
		// if (ct >= bocm.getFinal6BeginTime()) {
		// matchState = Translate.text_1912 + watcherNum;
		// } else {
		// // if (this.sideA.getFinalMatchFailRound() >= 1
		// // || this.sideB.getFinalMatchFailRound() >= 1) {
		// // matchState = Translate.translate.text_1913 + watcherNum;
		// // } else {
		// // matchState = Translate.translate.text_1914 + watcherNum;
		// // }
		// }
		// } else {
		// matchState = Translate.text_1915 + watcherNum;
		//
		// }
		//
		// lineContents[0] = matchState;
		// if (this.playerA != null) {
		// // lineContents[1] = this.playerA.getName() + Translate.translate.text_1916
		// // + this.sideB.getFailCount();
		// }else{
		// lineContents[1] = "";
		// }
		// if (this.playerB != null) {
		// // lineContents[2] = this.playerB.getName() + Translate.translate.text_1916
		// // + this.sideA.getFailCount();
		// }else{
		// lineContents[2] = "";
		// }
		//
		// lineColors[0] = 0x0000ff;
		// lineColors[1] = 0xff0000;
		// lineColors[2] = 0xff0000;

		// DISPLAY_INFO_ON_SCREEN req = new DISPLAY_INFO_ON_SCREEN(
		// GameMessageFactory.nextSequnceNum(), lineContents, lineColors,
		// false);
		// return req;
		return null;
	}

	/**
	 * 公布比赛结果
	 */
	private void publicizeResult(String result) {
		HINT_REQ req;
		Player[] allPlayers = this.getPlayers();
		Player pa = null;
		Player pb = null;
		ArrayList<BattleFieldStatData> bfsds = new ArrayList<BattleFieldStatData>();
		BattleFieldStatData bfsd = null;
		PlayerManager pm = PlayerManager.getInstance();
		try {
			pa = pm.getPlayer(this.sideA);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (pa != null) {
			bfsd = this.getBattleFieldStatDataByPlayer(pa);
			if (bfsd != null) {
				bfsds.add(bfsd);
			}
		}
		try {
			pb = pm.getPlayer(this.sideB);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (pb != null) {
			bfsd = this.getBattleFieldStatDataByPlayer(pb);
			if (bfsd != null) {
				bfsds.add(bfsd);
			}
		}
		// if (allPlayers != null) {
		// for (Player p : allPlayers) {
		//
		// MenuWindow mw = WindowManager.getInstance()
		// .createTempMenuWindow(360);
		// mw.setTitle(Translate.translate.text_1917);
		// mw.setDescriptionInUUB(result);
		//
		// Option_QuitDuelField oqdf = new Option_QuitDuelField(this
		// .getBattleFieldInfo().getDescription());
		// oqdf.setText(Translate.translate.text_1918);
		// oqdf.setColor(0xffffff);
		//
		// mw.setOptions(new Option[] { oqdf });
		// QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory
		// .nextSequnceNum(), mw, mw.getOptions());
		// p.addMessageToRightBag(res);
		// }
		// }

		String nameA = "";
		String nameB = "";
		long idA = 0;
		long idB = 0;
		if (this.playerA != null) {
			nameA = this.playerA.getName();
			idA = this.playerA.getId();
		}
		if (this.playerB != null) {
			nameB = this.playerB.getName();
			idB = this.playerB.getId();
		}

		if (logger.isInfoEnabled()) {
			logger.info("[比武] [比赛结果] [" + result + "] [" + this.getId() + "] [" + this.getBattleFieldInfo().getDescription() + "] [玩家A：" + nameA + "] [玩家A ID：" + idA + "] [玩家B：" + nameB + "] [玩家B ID：" + idB + "]");
		}
	}

	/**
	 * A方玩家是否缺席
	 * 
	 * @return
	 */
	private boolean isASideAbsent() {
		if (this.playerA != null) {
			Game game = this.playerA.getCurrentGame();
			if (game != null) {
				if (game == this.getGame()) {
					return false;
				}
			}
		}
		if (logger.isWarnEnabled()) logger.warn("[比武] [A方玩家缺席] [" + this.getId() + "] [" + this.getBattleFieldInfo().getDescription() + "] [玩家ID：" + this.sideA + "]");
		return true;
	}

	/**
	 * B方玩家是否缺席
	 * 
	 * @return
	 */
	private boolean isBSideAbsent() {
		if (this.playerB != null) {
			Game game = this.playerB.getCurrentGame();
			if (game != null) {
				if (game == this.getGame()) {
					return false;
				}
			}
		}
		if (logger.isWarnEnabled()) logger.warn("[比武] [B方玩家缺席] [" + this.getId() + "] [" + this.getBattleFieldInfo().getDescription() + "] [玩家ID：" + this.sideB + "]");
		return true;
	}

	/**
	 * 检查有无玩家缺席或中途退出
	 * 
	 * @return
	 */
	private boolean checkAbsent() {
		PlayerManager pm = PlayerManager.getInstance();
		if (!this.isASideAbsent() && this.isBSideAbsent()) {
			this.matchEnd(this.sideA);
			未到场结束 = true;
			this.transientWinerSide = BattleField.BATTLE_SIDE_A;
			this.winSide = BattleField.BATTLE_SIDE_A;
			Player b = null;
			try {
				b = pm.getPlayer(this.sideB);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			String name = "";
			if (b != null) {
				name = b.getName();
			}
			String wn = "";
			long wid = 0;
			if (this.playerA != null) {
				wn = this.playerA.getName();
				wid = this.playerA.getId();
				// 活跃度统计
				ActivenessManager.getInstance().record(this.playerA, ActivenessType.武圣争夺战);
			}
			String s = "[color=" + 0xff0000 + "]" + name + Translate.text_1919 + 0xff0000 + "]" + wn + Translate.text_1920;
			this.publicizeResult(s);
			this.state = TournamentField.STATE_SHOW_RESULT;
			this.showResultTime = SystemTime.currentTimeMillis();
			if (logger.isInfoEnabled()) {
				logger.info("[比武] [B方未到场] [" + this.getId() + "] [" + this.getBattleFieldInfo().getDescription() + "] [胜利者：" + wn + "] [胜利者ID" + wid + "] [失利者：" + name + "] [失利者ID：" + this.sideB + "]");
			}
			return true;
		} else if (this.isASideAbsent() && !this.isBSideAbsent()) {
			this.matchEnd(this.sideB);
			未到场结束 = true;
			this.transientWinerSide = BattleField.BATTLE_SIDE_B;
			this.winSide = BattleField.BATTLE_SIDE_B;
			Player a = null;
			try {
				a = pm.getPlayer(this.sideA);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			String name = "";
			if (a != null) {
				name = a.getName();
			}
			String wn = "";
			long wid = 0;
			if (this.playerB != null) {
				wn = this.playerB.getName();
				wid = this.playerB.getId();
			}
			String s = "[color=" + 0xff0000 + "]" + name + Translate.text_1919 + 0xff0000 + "]" + wn + Translate.text_1920;
			this.publicizeResult(s);
			this.state = TournamentField.STATE_SHOW_RESULT;
			this.showResultTime = SystemTime.currentTimeMillis();
			if (logger.isInfoEnabled()) {
				logger.info("[比武] [A方未到场] [" + this.getId() + "] [" + this.getBattleFieldInfo().getDescription() + "] [胜利者：" + wn + "] [胜利者ID" + wid + "] [失利者：" + name + "] [失利者ID：" + this.sideA + "]");
			}
			return true;
		} else if (this.isASideAbsent() && this.isBSideAbsent()) {
			this.matchEnd(0);
			未到场结束 = true;
			this.transientWinerSide = BattleField.BATTLE_SIDE_C;
			this.winSide = BattleField.BATTLE_SIDE_C;

			this.publicizeResult(Translate.无胜利者);
			this.state = TournamentField.STATE_SHOW_RESULT;
			this.showResultTime = SystemTime.currentTimeMillis();
			if (logger.isInfoEnabled()) {
				logger.info("[比武] [双方均未到场] [" + this.getId() + "] [" + this.getBattleFieldInfo().getDescription() + "] [无胜利者]");
			}
			return true;
		}
		return false;
	}

	private int getDamageByPlayer(Player p) {
		int d = 0;
		BattleFieldStatData stat = this.getBattleFieldStatDataByPlayer(p);
		if (stat != null) {
			d = stat.getTotalDamage();
		}
		return d;
	}

	public int getBornRegionXForB() {
		return bornRegionXForB;
	}

	public void setBornRegionXForB(int bornRegionXForB) {
		this.bornRegionXForB = bornRegionXForB;
	}

	public int getBornRegionYForB() {
		return bornRegionYForB;
	}

	public void setBornRegionYForB(int bornRegionYForB) {
		this.bornRegionYForB = bornRegionYForB;
	}

	public int getBornRegionXForA() {
		return bornRegionXForA;
	}

	public void setBornRegionXForA(int bornRegionXForA) {
		this.bornRegionXForA = bornRegionXForA;
	}

	public int getBornRegionYForA() {
		return bornRegionYForA;
	}

	public void setBornRegionYForA(int bornRegionYForA) {
		this.bornRegionYForA = bornRegionYForA;
	}

	/**
	 * 得到玩家全身装备的评分
	 * @param p
	 * @return
	 */
	private int getPlayerEquipmentPoints(Player p) {
		int point = 0;
		// EquipmentColumn[] ec = p.getEquipmentColumns();
		// if (ec != null) {
		// for (int i = 0; i < ec.length; i++) {
		// EquipmentEntity[] ee = ec[i].getEquipmentArrayByCopy();
		// if (ee != null) {
		// for (int j = 0; j < ee.length; j++) {
		// point += EquipmentBillboards.getArticlePoints(ee[j]);
		// }
		// }
		// }
		// }
		return point;
	}

	public ArrayList<LivingObject> getLivingObj() {
		return livingObj;
	}

	public void setLivingObj(ArrayList<LivingObject> livingObj) {
		this.livingObj = livingObj;
	}

	private void goIntoBattleField(Player player) {
		if (player == null) {
			return;
		}

		if (TournamentManager.isWaitTime()) {
			player.setHp(player.getMaxHP());
			player.setMp(player.getMaxMP());
		}
		List<Buff> buffs = player.getAllBuffs();

		for (int i = buffs.size() - 1; i >= 0; i--) {
			Buff buff = buffs.get(i);
			if (buff.getTemplate().clearSkillPointNotdisappear) {
				if (logger.isInfoEnabled()) {
					logger.info("[进入比武赛场保留buff:] [" + buff.getTemplateName() + "] [" + buff.getClass() + "] [level:" + buff.getLevel() + "] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]");
				}

				continue;
			}
			if (buff != null && !(buff instanceof AuraBuff) && !buff.getTemplateName().trim().equals(RobberyConstant.FAILBUFF)) {
				buff.end(player);
				if (buff.isForover() || buff.isSyncWithClient()) {
					NOTIFY_BUFF_REMOVED_REQ req = new NOTIFY_BUFF_REMOVED_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, player.getId(), buff.getSeqId());
					player.addMessageToRightBag(req);
				}
				buffs.remove(i);

				if (logger.isInfoEnabled()) {
					logger.info("[进入比武赛场去除BUFF] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + buff.getClass().getName().substring(buff.getClass().getName().lastIndexOf(".") + 1) + ":" + buff.getTemplateName() + "] [time:" + buff.getInvalidTime() + "]");
				}

			}
		}
		上定身buff(player, 比赛开始前进入时间);
		try {
			player.clearAllSkillCD("武圣");
			if (logger.isInfoEnabled()) {
				logger.info("[进入比武赛场去除技能CD] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void matchEnd(long playerId) {
		TournamentManager tm = TournamentManager.getInstance();
		if (tm != null) {
			int currentWeek = TournamentManager.得到一年中的第几个星期_周日并到上星期(System.currentTimeMillis());
			if (playerA != null && playerA.getId() == playerId) {
				if (game.contains(playerA)) {
					if (tm.onePlayerTournamentDataMap.get(playerA.getId()) == null) {
						OneTournamentData optd = tm.得到玩家竞技数据(playerA.getId());
						if (optd == null) {
							optd = new OneTournamentData(playerA.getId(), currentWeek);
						}
						tm.onePlayerTournamentDataMap.put(playerA.getId(), optd);
					}
					if (tm.onePlayerTournamentDataMap.get(playerA.getId()).getCurrentWeek() != currentWeek) {
						tm.玩家本星期竞技数据归零(tm.onePlayerTournamentDataMap.get(playerA.getId()));
						tm.onePlayerTournamentDataMap.get(playerA.getId()).setCurrentWeek(currentWeek);
					}
					tm.玩家赢一场竞技(tm.onePlayerTournamentDataMap.get(playerA.getId()));

					String des = Translate.translateString(Translate.恭喜您赢得了比赛获得积分, new String[][] { { Translate.COUNT_1, TournamentManager.FIGHT_WIN_POINT + "" } });
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, des);
					playerA.addMessageToRightBag(hreq);
				}
				if (playerB != null && game.contains(playerB)) {
					if (tm.onePlayerTournamentDataMap.get(playerB.getId()) == null) {
						OneTournamentData optd = tm.得到玩家竞技数据(playerB.getId());
						if (optd == null) {
							optd = new OneTournamentData(playerB.getId(), currentWeek);
						}
						tm.onePlayerTournamentDataMap.put(playerB.getId(), optd);
					}
					if (tm.onePlayerTournamentDataMap.get(playerB.getId()).getCurrentWeek() != currentWeek) {
						tm.玩家本星期竞技数据归零(tm.onePlayerTournamentDataMap.get(playerB.getId()));
						tm.onePlayerTournamentDataMap.get(playerB.getId()).setCurrentWeek(currentWeek);
					}
					tm.玩家输一场竞技(tm.onePlayerTournamentDataMap.get(playerB.getId()));
					String des = Translate.translateString(Translate.很遗憾您输了比赛获得积分, new String[][] { { Translate.COUNT_1, TournamentManager.FIGHT_LOST_POINT + "" } });
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, des);
					playerB.addMessageToRightBag(hreq);

				}
			} else if (playerB != null && playerB.getId() == playerId) {
				if (game.contains(playerB)) {
					if (tm.onePlayerTournamentDataMap.get(playerB.getId()) == null) {
						OneTournamentData optd = tm.得到玩家竞技数据(playerB.getId());
						if (optd == null) {
							optd = new OneTournamentData(playerB.getId(), currentWeek);
						}
						tm.onePlayerTournamentDataMap.put(playerB.getId(), optd);
					}
					if (tm.onePlayerTournamentDataMap.get(playerB.getId()).getCurrentWeek() != currentWeek) {
						tm.玩家本星期竞技数据归零(tm.onePlayerTournamentDataMap.get(playerB.getId()));
						tm.onePlayerTournamentDataMap.get(playerB.getId()).setCurrentWeek(currentWeek);
					}
					tm.玩家赢一场竞技(tm.onePlayerTournamentDataMap.get(playerB.getId()));
					String des = Translate.translateString(Translate.恭喜您赢得了比赛获得积分, new String[][] { { Translate.COUNT_1, TournamentManager.FIGHT_WIN_POINT + "" } });
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, des);
					playerB.addMessageToRightBag(hreq);
				}
				if (playerA != null && game.contains(playerA)) {
					if (tm.onePlayerTournamentDataMap.get(playerA.getId()) == null) {
						OneTournamentData optd = tm.得到玩家竞技数据(playerA.getId());
						if (optd == null) {
							optd = new OneTournamentData(playerA.getId(), currentWeek);
						}
						tm.onePlayerTournamentDataMap.put(playerA.getId(), optd);
					}
					if (tm.onePlayerTournamentDataMap.get(playerA.getId()).getCurrentWeek() != currentWeek) {
						tm.玩家本星期竞技数据归零(tm.onePlayerTournamentDataMap.get(playerA.getId()));
						tm.onePlayerTournamentDataMap.get(playerA.getId()).setCurrentWeek(currentWeek);
					}
					tm.玩家输一场竞技(tm.onePlayerTournamentDataMap.get(playerA.getId()));
					String des = Translate.translateString(Translate.很遗憾您输了比赛获得积分, new String[][] { { Translate.COUNT_1, TournamentManager.FIGHT_LOST_POINT + "" } });
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, des);
					playerA.addMessageToRightBag(hreq);
				}
			} else {
				if (playerA != null && game.contains(playerA)) {
					if (tm.onePlayerTournamentDataMap.get(playerA.getId()) == null) {
						OneTournamentData optd = tm.得到玩家竞技数据(playerA.getId());
						if (optd == null) {
							optd = new OneTournamentData(playerA.getId(), currentWeek);
						}
						tm.onePlayerTournamentDataMap.put(playerA.getId(), optd);
					}
					if (tm.onePlayerTournamentDataMap.get(playerA.getId()).getCurrentWeek() != currentWeek) {
						tm.玩家本星期竞技数据归零(tm.onePlayerTournamentDataMap.get(playerA.getId()));
						tm.onePlayerTournamentDataMap.get(playerA.getId()).setCurrentWeek(currentWeek);
					}
					tm.玩家输一场竞技(tm.onePlayerTournamentDataMap.get(playerA.getId()));
					String des = Translate.translateString(Translate.很遗憾您输了比赛获得积分, new String[][] { { Translate.COUNT_1, TournamentManager.FIGHT_LOST_POINT + "" } });
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, des);
					playerA.addMessageToRightBag(hreq);
				}
				if (playerB != null && game.contains(playerB)) {
					if (tm.onePlayerTournamentDataMap.get(playerB.getId()) == null) {
						OneTournamentData optd = tm.得到玩家竞技数据(playerB.getId());
						if (optd == null) {
							optd = new OneTournamentData(playerB.getId(), currentWeek);
						}
						tm.onePlayerTournamentDataMap.put(playerB.getId(), optd);
					}
					if (tm.onePlayerTournamentDataMap.get(playerB.getId()).getCurrentWeek() != currentWeek) {
						tm.玩家本星期竞技数据归零(tm.onePlayerTournamentDataMap.get(playerB.getId()));
						tm.onePlayerTournamentDataMap.get(playerB.getId()).setCurrentWeek(currentWeek);
					}
					tm.玩家输一场竞技(tm.onePlayerTournamentDataMap.get(playerB.getId()));
					String des = Translate.translateString(Translate.很遗憾您输了比赛获得积分, new String[][] { { Translate.COUNT_1, TournamentManager.FIGHT_LOST_POINT + "" } });
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, des);
					playerB.addMessageToRightBag(hreq);
				}
			}
		}

	}

	@Override
	public void notifyCauseDamageOfReal(Fighter causter, int damage) {
		// TODO Auto-generated method stub

	}

	public boolean inFightingArea(Player player) {
		for (String areaName : player.getCurrentMapAreaNames()) {
			if (Translate.比武擂台.equals(areaName)) {
				return true;
			}
		}
		return false;
	}

}
