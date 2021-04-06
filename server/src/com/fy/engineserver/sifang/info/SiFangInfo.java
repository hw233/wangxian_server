package com.fy.engineserver.sifang.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.ParticleData;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.res.Constants;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_PLAY_PARTICLE_RES;
import com.fy.engineserver.message.PLAYER_REVIVED_RES;
import com.fy.engineserver.message.SIFANG_OVER_MSG_RES;
import com.fy.engineserver.message.SIFANG_SHOW_START_TIME_RES;
import com.fy.engineserver.playerTitles.PlayerTitlesManager;
import com.fy.engineserver.sifang.SiFangManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
public class SiFangInfo {

	public static final int SIFANG_TYPE_QINGLONG = 0; // 青龙
	public static final int SIFANG_TYPE_ZHUQUE = 1; // 朱雀
	public static final int SIFANG_TYPE_BAIHU = 2; // 白虎
	public static final int SIFANG_TYPE_XUANWU = 3; // 玄武
	public static final int SIFANG_TYPE_QILIN = 4; // 麒麟

	public static final int SIFANG_STATE_START_ENTER = 0; // 报名
	public static final int SIFANG_STATE_START_NOTICE = 1; // 公告
	public static final int SIFANG_STATE_START_JOIN = 2; // 进场
	public static final int SIFANG_STATE_START_START = 3; // 正式开始
	public static final int SIFANG_STATE_OVER = 4; // 结束

	@SimpleId
	private long Id;

	@SimpleVersion
	private int versionSI;

	private int infoType; // 类型，是5个圣地的哪个

	private long jiaZuID;

	private transient int state; // 纪录进行中的各个状态，开始报名0 公告1 进场2 正式开始3 结束4

	private transient int gonggaoNum; // 公告次数
	private transient long gonggaoTime;
	private transient int jingchangNum; // 进场次数
	private transient long jingchangTime;
	private transient boolean startCountDown = false; // 开始倒计时

	private transient long startTime;
	private transient Game game;
	private transient long levelTime;
	private transient int[] killNum;

	private transient Game bossGame;

	private transient long[] jiaZuList;

	private ArrayList<Long> enListID = new ArrayList<Long>(); // 报名家族ID

	private ArrayList<ArrayList<Long>> enListPlayerID = new ArrayList<ArrayList<Long>>(); // 参加比赛玩家ID

	public SiFangInfo() {

	}

	public void setId(long id) {
		Id = id;
	}

	public long getId() {
		return Id;
	}

	public void setInfoType(int infoType) {
		this.infoType = infoType;
	}

	public int getInfoType() {
		return infoType;
	}

	public void setJiaZuID(long jiaZuID) {
		this.jiaZuID = jiaZuID;
		SiFangManager.em.notifyFieldChange(this, "jiaZuID");
	}

	public long getJiaZuID() {
		return jiaZuID;
	}

	public void setVersionSI(int versionSI) {
		this.versionSI = versionSI;
	}

	public int getVersionSI() {
		return versionSI;
	}

	// 报名
	public void addEnList(long JiaZuID) {
		enListID.add(JiaZuID);
		ArrayList<Long> a = new ArrayList<Long>();
		enListPlayerID.add(a);
		SiFangManager.em.notifyFieldChange(this, "enListID");
		SiFangManager.em.notifyFieldChange(this, "enListPlayerID");
	}

	public void setEnListID(ArrayList<Long> enListID) {
		this.enListID = enListID;
	}

	public ArrayList<Long> getEnListID() {
		return enListID;
	}

	public void setEnListPlayerID(ArrayList<ArrayList<Long>> enListPlayerID) {
		this.enListPlayerID = enListPlayerID;
	}

	public ArrayList<ArrayList<Long>> getEnListPlayerID() {
		return enListPlayerID;
	}

	// 清除报名列表
	public void clearEnList() {
		enListID.clear();
		enListPlayerID.clear();
		SiFangManager.em.notifyFieldChange(this, "enListID");
		SiFangManager.em.notifyFieldChange(this, "enListPlayerID");
	}

	public void clearBossGamePlayer() {
		if (bossGame != null) {
			LivingObject[] living = bossGame.getLivingObjects();
			for (int i = 0; i < living.length; i++) {
				if (living[i] instanceof Player) {
					Player player = (Player) living[i];
					player.getCurrentGame().transferGame(player, new TransportData(0, 0, 0, 0, player.getHomeMapName(), player.getHomeX(), player.getHomeY()));
				}
			}
		}
	}

	// 创建BOSS场景
	public void buildBossGame() {
		Game bossGame = new Game(GameManager.getInstance(), GameManager.getInstance().getGameInfo(SiFangManager.getInstance().bossMmapName[infoType]));
		try {
			bossGame.init();
		} catch (Exception e) {
			SiFangManager.logger.error("四方BOSS地图创建出问题:", e);
		}
		setBossGame(bossGame);
	}

	// 开始新的比赛
	public void startNew() {
		setJiaZuID(-1);
		setState(SiFangInfo.SIFANG_STATE_START_NOTICE);
		setGonggaoNum(0);
		setGonggaoTime(0);
		setJingchangNum(0);
		setJingchangTime(0);
		setStartCountDown(false);
		clearBossGamePlayer();
		jiaZuPlayer.clear();
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}

	public void setGonggaoNum(int gonggaoNum) {
		this.gonggaoNum = gonggaoNum;
	}

	public int getGonggaoNum() {
		return gonggaoNum;
	}

	public void setJingchangNum(int jingchangNum) {
		this.jingchangNum = jingchangNum;
	}

	public int getJingchangNum() {
		return jingchangNum;
	}

	public void setGonggaoTime(long gonggaoTime) {
		this.gonggaoTime = gonggaoTime;
	}

	public long getGonggaoTime() {
		return gonggaoTime;
	}

	public void setJingchangTime(long jingchangTime) {
		this.jingchangTime = jingchangTime;
	}

	public long getJingchangTime() {
		return jingchangTime;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Game getGame() {
		return game;
	}

	// 地图上还活着的且在线的玩家，按家族ID分
	private transient HashMap<Long, ArrayList<Player>> jiaZuPlayer = new HashMap<Long, ArrayList<Player>>();
	private transient ArrayList<Long> removeJiaZu = new ArrayList<Long>();
	private transient ArrayList<Player> remove = new ArrayList<Player>();

	public transient static String BACK_MAP_NAME = "kunlunshengdian";
	public transient static int[] BACK_POS = new int[] { 4880, 520 };

	public transient static int LEAVE_TIME = 40 * 1000;

	public void heartbeat() {
		if (bossGame != null) {
			bossGame.heartbeat();
		}
		if (game != null) {
			game.heartbeat();
			if (state == SIFANG_STATE_OVER || state == SIFANG_STATE_START_ENTER) {
				if (levelTime < SystemTime.currentTimeMillis()) {
					LivingObject[] living = game.getLivingObjects();
					for (int i = 0; i < living.length; i++) {
						if (living[i] instanceof Player) {
							Player player = (Player) living[i];
							if (player.isOnline() && player.getCurrentGame() != null) {
								player.getCurrentGame().transferGame(player, new TransportData(0, 0, 0, 0, BACK_MAP_NAME, BACK_POS[0], BACK_POS[1]));
							}
						}
					}
				}
			}
			if (state == SIFANG_STATE_START_START) {
				try {
					removeJiaZu.clear();
					for (Long key : jiaZuPlayer.keySet()) {
						ArrayList<Player> pLists = jiaZuPlayer.get(key);
						remove.clear();
						for (int i = 0; i < pLists.size(); i++) {
							Player player = pLists.get(i);
							if (player.isOnline() && player.getHp() > 0 && player.getJiazuId() > 0 && player.getCurrentGame() == game) {

							} else {
								remove.add(player);
								if (player.getHp() <= 0) {
									int index = 0;
									for (int j = 0; j < getJiaZuList().length; j++) {
										if (getJiaZuList()[j] == player.getJiazuId()) {
											index = j;
										}
									}
									player.setHp(player.getMaxHP());
									player.setMp(player.getMaxMP());
									player.setState(Constants.STATE_STAND);
									int[] XY = SiFangManager.getInstance().pkMapXY[index];
									player.getCurrentGame().transferGame(player, new TransportData(0, 0, 0, 0, player.getCurrentGame().gi.name, XY[0], XY[1]));
									PLAYER_REVIVED_RES res = new PLAYER_REVIVED_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, "", player.getMaxHP(), player.getMaxMP());
									player.addMessageToRightBag(res);
								} else {
									if (player.getCurrentGame() == game) {
										player.getCurrentGame().transferGame(player, new TransportData(0, 0, 0, 0, player.getHomeMapName(), player.getHomeX(), player.getHomeY()));
									} else {
										if (isStartCountDown()) {
											SIFANG_SHOW_START_TIME_RES res = new SIFANG_SHOW_START_TIME_RES(GameMessageFactory.nextSequnceNum(), 0);
											player.addMessageToRightBag(res);
										}
									}
								}
							}
						}
						for (int j = 0; j < remove.size(); j++) {
							pLists.remove(remove.get(j));
						}
						if (pLists.size() == 0) {
							removeJiaZu.add(key);
						}
					}
					for (int j = 0; j < removeJiaZu.size(); j++) {
						jiaZuPlayer.remove(removeJiaZu.get(j));
					}
					if (jiaZuPlayer.size() == 0) {
						setJiaZuID(-1);
						setState(SIFANG_STATE_OVER);
						if (SiFangManager.logger.isWarnEnabled()) SiFangManager.logger.warn("[一个人都么有] [{}] [报名家族数={}] [家族成员数={}]", new Object[] { SiFangManager.getInstance().infoName[infoType], getEnListID().size(), getLogCanSaiNum() });
						clearEnList();
					} else if (jiaZuPlayer.size() == 1) {
						levelTime = SystemTime.currentTimeMillis() + LEAVE_TIME;
						long jiazuID = jiaZuPlayer.keySet().iterator().next().longValue();
						setJiaZuID(jiazuID);
						to成就(jiazuID);
						to称号(jiazuID);
						setState(SIFANG_STATE_OVER);
						Jiazu jiazu = JiazuManager.getInstance().getJiazu(jiazuID);
						if (jiazu == null) {
							SiFangManager.logger.error("[错误] [胜利的家族不存在??] [t={}] [jid={}] [EJID={}] [CPID={}]", new Object[] { infoType, jiazuID, getLogJiaZuID(), getLogCanSai() });
							return;
						}
						for (Player pp : PlayerManager.getInstance().getOnlinePlayers()) {
							String msg = Translate.text_sifang_031 + "<f color='0x00ffff'>" + jiazu.getName() + "</f>"+ Translate.text_sifang_032 +"<f color='0x00ffff'>" + SiFangManager.getInstance().infoName[infoType] + "</f>" + Translate.text_sifang_033;
							pp.send_HINT_REQ(msg, (byte) 2);
						}

						for (int i = 0; i < enListID.size(); i++) {
							if (enListID.get(i) == jiazu.getJiazuID()) {
								for (int j = 0; j < enListPlayerID.get(i).size(); j++) {
									Player player = PlayerManager.getInstance().getPlayer(enListPlayerID.get(i).get(j));
									ParticleData[] particleDatas = new ParticleData[1];
									particleDatas[0] = new ParticleData(player.getId(), "任务光效/家族圣坛开启文字", -1, 2, 1, 1);
									NOTICE_CLIENT_PLAY_PARTICLE_RES client_PLAY_PARTICLE_RES = new NOTICE_CLIENT_PLAY_PARTICLE_RES(GameMessageFactory.nextSequnceNum(), particleDatas);
									player.addMessageToRightBag(client_PLAY_PARTICLE_RES);
								}
								break;
							}
						}

						LivingObject[] living = game.getLivingObjects();
						boolean isDo = true;
						for (int i = 0; i < living.length; i++) {
							if (living[i] instanceof Player) {
								Player player = (Player) living[i];
								player.addMessageToRightBag(buildMsg(player, true, isDo));
								isDo = false;
							}
						}
						if (SiFangManager.logger.isWarnEnabled()) SiFangManager.logger.warn("[家族胜利] [t={}] [jID={}] [EJID={}] [CPID={}] [killNum={}]", new Object[] { infoType, jiazuID, getLogJiaZuID(), getLogCanSai(), getLogKillNum() });
						clearEnList();
						buildBossGame();
					} else {
						long nowTime = SystemTime.currentTimeMillis();
						// 如果时间到了，没到不用管
						if (startTime + SiFangManager.getInstance().战斗时间 < nowTime) {
							levelTime = SystemTime.currentTimeMillis() + LEAVE_TIME;
							setState(SIFANG_STATE_OVER);
							// 判断谁赢了
							int maxKillNum = -1;

							ArrayList<Long> jzIDs = new ArrayList<Long>();
							for (int i = 0; i < killNum.length; i++) {
								long id = enListID.get(i);
								ArrayList<Player> aa = jiaZuPlayer.get(id);
								if (aa != null) {
									if (maxKillNum < killNum[i]) {
										maxKillNum = killNum[i];
										jzIDs.clear();
										jzIDs.add(enListID.get(i));
									} else if (maxKillNum == killNum[i]) {
										jzIDs.add(enListID.get(i));
									}
								}
							}
							if (jzIDs.size() == 1) {
								// 有一个家族杀人最多，按这个算
								Jiazu jiazu = JiazuManager.getInstance().getJiazu(jzIDs.get(0));
								if (jiazu == null) {
									SiFangManager.logger.error("[错误] [胜利的家族不存在??] [t={}] [jid={}] [EJID={}] [CPID={}]", new Object[] { infoType, jzIDs.get(0), getLogJiaZuID(), getLogCanSai() });
									return;
								}
								setJiaZuID(jzIDs.get(0));
								to成就(jzIDs.get(0));
								to称号(jzIDs.get(0));
								for (Player pp : PlayerManager.getInstance().getOnlinePlayers()) {
									String msg = Translate.text_sifang_031 + "<f color='0x00ffff'>" + jiazu.getName() + "</f>"+Translate.text_sifang_032+"<f color='0x00ffff'>" + SiFangManager.getInstance().infoName[infoType] + "</f>"+Translate.text_sifang_033;
									pp.send_HINT_REQ(msg, (byte) 2);
								}

								for (int i = 0; i < enListID.size(); i++) {
									if (enListID.get(i) == jiazu.getJiazuID()) {
										for (int j = 0; j < enListPlayerID.get(i).size(); j++) {
											Player player = PlayerManager.getInstance().getPlayer(enListPlayerID.get(i).get(j));
											ParticleData[] particleDatas = new ParticleData[1];
											particleDatas[0] = new ParticleData(player.getId(), "任务光效/家族圣坛开启文字", -1, 2, 1, 1);
											NOTICE_CLIENT_PLAY_PARTICLE_RES client_PLAY_PARTICLE_RES = new NOTICE_CLIENT_PLAY_PARTICLE_RES(GameMessageFactory.nextSequnceNum(), particleDatas);
											player.addMessageToRightBag(client_PLAY_PARTICLE_RES);
										}
										break;
									}
								}

								LivingObject[] living = game.getLivingObjects();
								boolean isDo = true;
								for (int i = 0; i < living.length; i++) {
									if (living[i] instanceof Player) {
										Player player = (Player) living[i];
										player.addMessageToRightBag(buildMsg(player, true, isDo));
										isDo = false;
									}
								}

								if (SiFangManager.logger.isWarnEnabled()) SiFangManager.logger.warn("[家族胜利] [多个家族存活] [杀人数多] [t={}] [jID={}] [EJID={}] [overInfo={}]", new Object[] { infoType, jzIDs.get(0), getLogJiaZuID(), getLogOverInfo(overInfos.toArray(new SiFangOverInfo[0])) });
								clearEnList();
								buildBossGame();
							} else {
								// 有多个
								ArrayList<Integer> jsPs = new ArrayList<Integer>();
								ArrayList<Long> jsJIDS = new ArrayList<Long>();
								int maxLiveP = -1;
								for (int i = 0; i < jzIDs.size(); i++) {
									ArrayList<Player> aa = jiaZuPlayer.get(jzIDs.get(i));
									if (maxLiveP < aa.size()) {
										maxLiveP = aa.size();
										jsPs.clear();
										jsJIDS.clear();
										jsPs.add(aa.size());
										jsJIDS.add(jzIDs.get(i));
									} else if (maxLiveP == aa.size()) {
										jsPs.add(aa.size());
										jsJIDS.add(jzIDs.get(i));
									}
								}
								int index = 0;
								if (jsPs.size() == 1) {
									// 只有一个家族的活着的人最多
								} else {
									// 随机
									Random random = new Random();
									index = random.nextInt(jsJIDS.size());
								}

								Jiazu jiazu = JiazuManager.getInstance().getJiazu(jsJIDS.get(index));
								if (jiazu == null) {
									SiFangManager.logger.error("[错误] [胜利的家族不存在??] [t={}] [jid={}] [EJID={}] [CPID={}]", new Object[] { infoType, jzIDs.get(index), getLogJiaZuID(), getLogCanSai() });
									return;
								}
								setJiaZuID(jsJIDS.get(index));
								to成就(jsJIDS.get(index));
								to称号(jsJIDS.get(index));
								for (Player pp : PlayerManager.getInstance().getOnlinePlayers()) {
									String msg = Translate.text_sifang_031 + "<f color='0x00ffff'>" + jiazu.getName() + "</f>"+Translate.text_sifang_032+"<f color='0x00ffff'>" + SiFangManager.getInstance().infoName[infoType] + "</f>" + Translate.text_sifang_033;
									pp.send_HINT_REQ(msg, (byte) 2);
								}

								for (int i = 0; i < enListID.size(); i++) {
									if (enListID.get(i) == jiazu.getJiazuID()) {
										for (int j = 0; j < enListPlayerID.get(i).size(); j++) {
											Player player = PlayerManager.getInstance().getPlayer(enListPlayerID.get(i).get(j));
											ParticleData[] particleDatas = new ParticleData[1];
											particleDatas[0] = new ParticleData(player.getId(), "任务光效/家族圣坛开启文字", -1, 2, 1, 1);
											NOTICE_CLIENT_PLAY_PARTICLE_RES client_PLAY_PARTICLE_RES = new NOTICE_CLIENT_PLAY_PARTICLE_RES(GameMessageFactory.nextSequnceNum(), particleDatas);
											player.addMessageToRightBag(client_PLAY_PARTICLE_RES);
										}
										break;
									}
								}

								LivingObject[] living = game.getLivingObjects();
								boolean isDo = true;
								for (int i = 0; i < living.length; i++) {
									if (living[i] instanceof Player) {
										Player player = (Player) living[i];
										player.addMessageToRightBag(buildMsg(player, true, isDo));
										isDo = false;
									}
								}

								if (SiFangManager.logger.isWarnEnabled()) SiFangManager.logger.warn("[家族胜利] [多个家族存活] [t={}] [jID={}] [EJID={}] [overInfo={}]", new Object[] { infoType, jsJIDS.get(index), getLogJiaZuID(), getLogOverInfo(overInfos.toArray(new SiFangOverInfo[0])) });
								clearEnList();
								buildBossGame();
							}
						}
					}
				} catch (Exception e) {
					SiFangManager.logger.error("四方神兽heartbeat出错:", e);
				}
			}
		}
	}

	public void setJiaZuList(long[] jiaZuList) {
		this.jiaZuList = jiaZuList;
	}

	public long[] getJiaZuList() {
		return jiaZuList;
	}

	// 随机家族的位置，主要是在家族进场的时候进入不同的岛
	public void randomJizZuPost() {
		Random ram = new Random();
		jiaZuList = new long[SiFangManager.getInstance().JOIN_JIAZU_MAX];
		for (int i = 0; i < enListID.size(); i++) {
			int a = ram.nextInt(SiFangManager.getInstance().JOIN_JIAZU_MAX);
			if (jiaZuList[a] == 0) {
				jiaZuList[a] = enListID.get(i);
			} else {
				for (int j = 0; j < jiaZuList.length; j++) {
					if (jiaZuList[j] == 0) {
						jiaZuList[j] = enListID.get(i);
						break;
					}
				}
			}
		}
	}

	public String getLogJiaZuID() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < enListID.size(); i++) {
			long a = enListID.get(i);
			sb.append(a).append(",");
		}
		return sb.toString();
	}

	public String getLogCanSai() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < enListPlayerID.size(); i++) {
			ArrayList<Long> a = enListPlayerID.get(i);
			for (int j = 0; j < a.size(); j++) {
				sb.append(a.get(j)).append(",");
			}
			sb.append("|");
		}
		return sb.toString();
	}

	public String getLogCanSaiNum() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < enListPlayerID.size(); i++) {
			sb.append(enListPlayerID.get(i).size()).append(",");
		}
		return sb.toString();
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setKillNum(int[] killNum) {
		this.killNum = killNum;
	}

	public int[] getKillNum() {
		return killNum;
	}

	public String getLogKillNum() {
		String kill = "";
		if (killNum != null) {
			for (int i = 0; i < killNum.length; i++) {
				kill = kill + killNum[i] + ",";
			}
		}
		return kill;
	}

	public String getLogOverInfo(SiFangOverInfo[] infos) {
		String msg = "";
		for (int i = 0; i < infos.length; i++) {
			msg = msg + infos[i].getJiaZuName() + "|" + infos[i].getKillNum() + "|" + infos[i].getLeftNum() + ",";
		}
		return msg;
	}

	private void to成就(long jiazuID) {
		for (int aa = 0; aa < enListID.size(); aa++) {
			if (enListID.get(aa) == jiazuID) {
				for (int i = 0; i < enListPlayerID.get(aa).size(); i++) {
					Player player = null;
					try {
						player = PlayerManager.getInstance().getPlayer(enListPlayerID.get(aa).get(i));
					} catch (Exception e) {
						SiFangManager.logger.error("to成就Player出错:", e);
						continue;
					}
					switch (infoType) {
					case SIFANG_TYPE_QINGLONG:
						AchievementManager.getInstance().record(player, RecordAction.获得青龙圣地);
						break;
					case SIFANG_TYPE_ZHUQUE:
						AchievementManager.getInstance().record(player, RecordAction.获得朱雀圣地);
						break;
					case SIFANG_TYPE_BAIHU:
						AchievementManager.getInstance().record(player, RecordAction.获得白虎圣地);
						break;
					case SIFANG_TYPE_XUANWU:
						AchievementManager.getInstance().record(player, RecordAction.获得玄武圣地);
						break;
					case SIFANG_TYPE_QILIN:
						AchievementManager.getInstance().record(player, RecordAction.获得麒麟圣地);
						break;
					}
				}
				break;
			}
		}
	}
	private void to称号(long jiazuID) {
		for (int aa = 0; aa < enListID.size(); aa++) {
			if (enListID.get(aa) == jiazuID) {
				for (int i = 0; i < enListPlayerID.get(aa).size(); i++) {
					Player player = null;
					try {
						player = PlayerManager.getInstance().getPlayer(enListPlayerID.get(aa).get(i));
					} catch (Exception e) {
						SiFangManager.logger.error("to称号Player出错:", e);
						continue;
					}
					switch (infoType) {
					case SIFANG_TYPE_QINGLONG:
						PlayerTitlesManager.getInstance().addTitle(player,Translate.五方圣兽圣坛称号1,true);
						break;
					case SIFANG_TYPE_ZHUQUE:
						PlayerTitlesManager.getInstance().addTitle(player,Translate.五方圣兽圣坛称号3,true);
						break;
					case SIFANG_TYPE_BAIHU:
						PlayerTitlesManager.getInstance().addTitle(player,Translate.五方圣兽圣坛称号2,true);
						break;
					case SIFANG_TYPE_XUANWU:
						PlayerTitlesManager.getInstance().addTitle(player,Translate.五方圣兽圣坛称号4,true);
						break;
					case SIFANG_TYPE_QILIN:
						PlayerTitlesManager.getInstance().addTitle(player,Translate.五方圣兽圣坛称号5,true);
						break;
					}
				}
				break;
			}
		}
	}

	public void setBossGame(Game bossGame) {
		this.bossGame = bossGame;
	}

	public Game getBossGame() {
		return bossGame;
	}

	public void addPlayerToJiaZuMap(Player p) {
		ArrayList<Player> list = jiaZuPlayer.get(p.getJiazuId());
		if (list == null) {
			list = new ArrayList<Player>();
			jiaZuPlayer.put(p.getJiazuId(), list);
		}
		if(!list.contains(p)){
			list.add(p);
		}
	}

	private transient ArrayList<Long> overTempEnListID = new ArrayList<Long>(); // 报名家族ID备份

	public void createOverTemp() {
		overTempEnListID.clear();
		overTempEnListID.addAll(enListID);
	}

	private transient ArrayList<SiFangOverInfo> overInfos = new ArrayList<SiFangOverInfo>();

	public SIFANG_OVER_MSG_RES buildMsg(Player player, boolean isWin, boolean isDo) {
		long time = SystemTime.currentTimeMillis();
		if (isDo) {
			overInfos.clear();
			for (int i = 0; i < overTempEnListID.size(); i++) {
				SiFangOverInfo ov = new SiFangOverInfo();
				Jiazu jiazu = JiazuManager.getInstance().getJiazu(overTempEnListID.get(i));
				if (jiazu == null) {
					continue;
				}
				ov.setJiaZuId(overTempEnListID.get(i));
				ov.setKillNum(killNum[i]);
				ArrayList<Player> pLs = jiaZuPlayer.get(overTempEnListID.get(i));
				if (pLs == null) {
					ov.setLeftNum(0);
				} else {
					ov.setLeftNum(pLs.size());
				}
				ov.setJiaZuName(jiazu.getName());
				boolean isAdd = false;
				for (int j = 0; j < overInfos.size(); j++) {
					SiFangOverInfo info = overInfos.get(j);
					if (ov.getLeftNum() > 0) {
						if (info.getKillNum() > ov.getKillNum() && info.getLeftNum() > 0) {
							continue;
						} else if (info.getKillNum() == ov.getKillNum()) {
							if (info.getLeftNum() > ov.getLeftNum()) {
								continue;
							} else {
								isAdd = true;
								overInfos.add(j, ov);
								break;
							}
						} else {
							isAdd = true;
							overInfos.add(j, ov);
							break;
						}
					} else {
						if (info.getLeftNum() > 0) {
							continue;
						} else {
							if (info.getKillNum() > ov.getKillNum()) {
								continue;
							} else {
								isAdd = true;
								overInfos.add(j, ov);
								break;
							}
						}
					}
				}
				if (!isAdd) {
					overInfos.add(ov);
				}
			}
		}
		if (isDo) {
			for (int i = 0; i < overInfos.size(); i++) {
				if (isWin) {
					// 发放奖励
					long jiazuID = overInfos.get(i).getJiaZuId();
					for (int j = 0; j < enListID.size(); j++) {
						if (enListID.get(j) == jiazuID) {
							Jiazu jiazu = JiazuManager.getInstance().getJiazu(jiazuID);
							for (int k = 0; k < enListPlayerID.get(j).size(); k++) {
								JiazuMember jm = JiazuManager.getInstance().getJiazuMember(enListPlayerID.get(j).get(k), jiazuID);
								if (jm == null) continue;
								jm.setCurrentWeekContribution(jm.getCurrentWeekContribution() + SiFangManager.getInstance().reward_JiaZu_GongXian[i]);
								jm.setTotalcontribution(jm.getTotalcontribution() + SiFangManager.getInstance().reward_JiaZu_GongXian[i]);
								if (SiFangManager.logger.isInfoEnabled()) SiFangManager.logger.info("[四方家族贡献奖励] [t={}] [win={}] [i={}] [jid={}] [pid={}]", new Object[] { infoType, overInfos.get(0).getJiaZuId(), i, jiazuID, enListPlayerID.get(j).get(k) });
							}
							jiazu.initMember4Client();
						}
					}
				}
				overInfos.get(i).setReward(SiFangManager.getInstance().reward_JiaZu_GongXian[i]);
			}
		}

		int selfIndex = -1;
		for (int i = 0; i < overInfos.size(); i++) {
			if (overInfos.get(i).getJiaZuId() == player.getJiazuId()) {
				selfIndex = i;
				break;
			}
		}
		if (SiFangManager.logger.isWarnEnabled()) SiFangManager.logger.warn("[查询情况] [t={}] [p={}] [time={}] [over={}]", new Object[] { infoType, player.getName() + "~" + player.getJiazuId(), SystemTime.currentTimeMillis() - time, getLogOverInfo(overInfos.toArray(new SiFangOverInfo[0])) });

		return new SIFANG_OVER_MSG_RES(GameMessageFactory.nextSequnceNum(), infoType, ((isWin || getState() == SIFANG_STATE_OVER) ? 0 : -1), selfIndex, overInfos.toArray(new SiFangOverInfo[0]));
	}
	
	public void addTitleToWinner(){
		
	}

	public void setStartCountDown(boolean startCountDown) {
		this.startCountDown = startCountDown;
	}

	public boolean isStartCountDown() {
		return startCountDown;
	}
}
