package com.fy.engineserver.activity.doomsday;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.septstation.JiazuBossDamageRecord;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.monster.Monster.AttackRecord;
import com.fy.engineserver.sprite.pet.Pet;

public class DoomsdayBossGame {

	public static int STATE_START = 1;					//开始
	public static int STATE_OVER = 100;					//结束
	
	public static long lifeTime = 1000L * 60 * 60;		//1小时的存活时间
	
	private int countryType;						//国家
	private Game game;								//game
	private long startTime;							//启动时间
	private int state;								//状态
	private int bossId;								//bossId
	private int level;								//等级
	private Monster monster;						//怪物
	
	private long overTime;							//结束时间
	
	private ArrayList<JiazuBossDamageRecord> bossDamages = new ArrayList<JiazuBossDamageRecord>();
	private long lastRefTime;
	public static long REF_SPACE = 1000 * 5;			//5秒
	
	public DoomsdayBossGame(String mapName, int countryType, int bossId, int level) {
		setLevel(level);
		GameInfo gi = null;
		GameManager gm = GameManager.getInstance();
		gi = gm.getGameInfo(mapName);
		if (gi == null) {
			DoomsdayManager.logger.error("[BOSS地图不存在] [{}]", new Object[]{mapName});
		}
		game = new Game(gm,gi);
		
		try {
			game.init();
		}catch(Exception e) {
			DoomsdayManager.logger.error("game.init出错:", e);
			game = null;
		}
		setBossId(bossId);
		int bossX = 300;
		int bossY = 300;
		MapArea area = game.gi.getMapAreaByName("boss");
		if (area != null) {
			Random random = new Random();
			bossX = area.getX() + random.nextInt(area.getWidth());
			bossY = area.getY() + random.nextInt(area.getHeight());
		}
		MemoryMonsterManager mmm = (MemoryMonsterManager)MemoryMonsterManager.getMonsterManager();
		Monster monster = mmm.createMonster(bossId);
		if (monster != null) {
			monster.setLevel((int)(level * 1.1));
			mmm.设置sprite属性值(monster);
			monster.setX(bossX);
			monster.setY(bossY);
			monster.setBornPoint(new Point(bossX, bossY));
			monster.setHp(monster.getMaxHP());
			monster.setGameNames(game.gi);
			game.addSprite(monster);
		}
		setMonster(monster);
		
		state = STATE_START;
		startTime = System.currentTimeMillis();
		this.countryType = countryType;
	}
	
	public void heatbeat() {
		try{
			if (game != null) {
				game.heartbeat();
			}else {
				setState(STATE_OVER);
				DoomsdayManager.logger.warn(DoomsdayManager.doomsdayLoggerHead + "[game为空] [{}]", new Object[]{countryType});
			}
			if (monster == null) {
				setState(STATE_OVER);
				DoomsdayManager.logger.warn(DoomsdayManager.doomsdayLoggerHead + "[monster不存在] [{}] [{}]", new Object[]{countryType, getBossId()});
			}
			if (state == STATE_START) {
				if (monster.isDeath()) {
					if (lastRefTime != 0 ){
						refDamageBillboard();
						lastRefTime = 0;
						if (DoomsdayManager.boss1Mapname.equals(game.gi.name)) {
							//头等舱
							//击杀奖励
							int boatIndex = DoomsdayManager.getInstance().getBoatHoldIndex((byte)countryType);
							String killPropName = DoomsdayManager.boss1KillPrize[boatIndex];
							if (monster.getLastAttacker() != null) {
								sendBoss1Mail(monster.getLastAttacker().getId(), 0,killPropName, 0);
							}
							//排名和参与奖励
							String[] billboardPropNames = DoomsdayManager.boss1BillboardPrize[boatIndex];
							String joinPropName = DoomsdayManager.boss1JoinPrize[boatIndex];
							{
								Player[] players = PlayerManager.getInstance().getOnlinePlayers();
								String msg = "<f color='0xffff00'>本次挑战【"+getMonster().getName()+"】伤害最高前三名</f>";
								for (int j = 0; j < players.length; j++) {
									players[j].sendNotice(msg);
								}
							}
							for (int i = 0; i < bossDamages.size(); i++) {
								JiazuBossDamageRecord damage = bossDamages.get(i);
								if (i < billboardPropNames.length) {
									sendBoss1Mail(damage.getPlayerId(), 1, billboardPropNames[i], i);
								}else {
									sendBoss1Mail(damage.getPlayerId(), 2, joinPropName, i);
								}
								if (i ==0) {
									Player[] players = PlayerManager.getInstance().getOnlinePlayers();
									String msg = "<f color='0xffff00'>伤害第一名:"+damage.getPlayerName()+"获得"+billboardPropNames[i]+"</f>";
									for (int j = 0; j < players.length; j++) {
										players[j].sendNotice(msg);
									}
								}else if (i == 1) {
									Player[] players = PlayerManager.getInstance().getOnlinePlayers();
									String msg = "<f color='0xffff00'>伤害第二名:"+damage.getPlayerName()+"获得"+billboardPropNames[i]+"</f>";
									for (int j = 0; j < players.length; j++) {
										players[j].sendNotice(msg);
									}
								}else if (i == 2) {
									Player[] players = PlayerManager.getInstance().getOnlinePlayers();
									String msg = "<f color='0xffff00'>伤害第三名:"+damage.getPlayerName()+"获得"+billboardPropNames[i]+"</f>";
									for (int j = 0; j < players.length; j++) {
										players[j].sendNotice(msg);
									}
								}
							}
							if (monster.getLastAttacker() != null) {
								Player[] players = PlayerManager.getInstance().getOnlinePlayers();
								String msg = "<f color='0xffff00'>"+monster.getLastAttacker().getName() + "成功击杀BOSS获得" + killPropName+"</f>";
								for (int j = 0; j < players.length; j++) {
									players[j].sendNotice(msg);
								}
							}
						}else if (DoomsdayManager.boss2Mapname.equals(game.gi.name)) {
							//船长室
							//击杀奖励
							int boatIndex = DoomsdayManager.getInstance().getBoatHoldIndex((byte)countryType);
							String killPropName = DoomsdayManager.boss2KillPrize[boatIndex];
							if (monster.getLastAttacker() != null) {
								sendBoss2Mail(monster.getLastAttacker().getId(), 0,killPropName, 0);
							}
							//排名和参与奖励
							String[] billboardPropNames = DoomsdayManager.boss2BillboardPrize[boatIndex];
							String joinPropName = DoomsdayManager.boss2JoinPrize[boatIndex];
							
							{
								Player[] players = PlayerManager.getInstance().getOnlinePlayers();
								String msg = "<f color='0xffff00'>本次挑战【"+getMonster().getName()+"】伤害最高前三名</f>";
								for (int j = 0; j < players.length; j++) {
									players[j].sendNotice(msg);
								}
							}
							for (int i = 0; i < bossDamages.size(); i++) {
								JiazuBossDamageRecord damage = bossDamages.get(i);
								if (i < billboardPropNames.length) {
									sendBoss2Mail(damage.getPlayerId(), 1, billboardPropNames[i], i);
								}else {
									sendBoss2Mail(damage.getPlayerId(), 2, joinPropName, i);
								}
								if (i ==0) {
									Player[] players = PlayerManager.getInstance().getOnlinePlayers();
									String msg = "<f color='0xffff00'>伤害第一名:"+damage.getPlayerName()+"获得"+billboardPropNames[i]+"</f>";
									for (int j = 0; j < players.length; j++) {
										players[j].sendNotice(msg);
									}
								}else if (i == 1) {
									Player[] players = PlayerManager.getInstance().getOnlinePlayers();
									String msg = "<f color='0xffff00'>伤害第二名:"+damage.getPlayerName()+"获得"+billboardPropNames[i]+"</f>";
									for (int j = 0; j < players.length; j++) {
										players[j].sendNotice(msg);
									}
								}else if (i == 2) {
									Player[] players = PlayerManager.getInstance().getOnlinePlayers();
									String msg = "<f color='0xffff00'>伤害第三名:"+damage.getPlayerName()+"获得"+billboardPropNames[i]+"</f>";
									for (int j = 0; j < players.length; j++) {
										players[j].sendNotice(msg);
									}
								}
							}
							if (monster.getLastAttacker() != null) {
								Player[] players = PlayerManager.getInstance().getOnlinePlayers();
								String msg = "<f color='0xffff00'>" + monster.getLastAttacker().getName() + "成功击杀BOSS获得" + killPropName+"</f>";
								for (int j = 0; j < players.length; j++) {
									players[j].sendNotice(msg);
								}
							}
						}
					}
					if (game.getNumOfPlayer() == 0) {
						setStartTime(0);
					}
					setState(STATE_OVER);
				}else {
					if (System.currentTimeMillis() - lastRefTime > REF_SPACE) {
						refDamageBillboard();
						lastRefTime = System.currentTimeMillis();
					}
				}
				if (isTimeOver()) {
					//时间到，停止活动
					setState(STATE_OVER);
					DoomsdayManager.logger.warn(DoomsdayManager.doomsdayLoggerHead + "[BOSS未能死亡，活动超过1小时] [地图={}] [国家={}] [启动时间={}]", new Object[]{game.gi.name, countryType, startTime});
				}
			}else if (state == STATE_OVER) {
				if (isTimeOver()) {
					if (game != null) {
						LivingObject[] livings = game.getLivingObjects();
						for (int i = 0; i < livings.length ; i++) {
							if (livings[i] instanceof Player) {
								Player player = (Player)livings[i];
								Game chuanCangGame = GameManager.getInstance().getGameByName(DoomsdayManager.kunlunshengdian, player.getCountry());
								MapArea area = chuanCangGame.gi.getMapAreaByName(Translate.出生点);
								int bornX = 300;
								int bornY = 300;
								if (area != null) {
									Random random = new Random();
									bornX = area.getX()+random.nextInt(area.getWidth());
									bornY = area.getY() + random.nextInt(area.getHeight());
								}
								TransportData transportData = new TransportData(0,0,0,0,DoomsdayManager.kunlunshengdian,bornX,bornY);
								player.setTransferGameCountry(player.getCountry());
								player.getCurrentGame().transferGame(player, transportData);
							}
						}
					}
					if (overTime <= 0) {
						overTime = System.currentTimeMillis();
					}
				}
			}
		}catch(Exception e) {
			DoomsdayManager.logger.error("心跳出错:" + countryType +"st="+state, e);
		}
	}
	
	/**
	 *发头等舱的奖励
	 * @param playerId
	 * @param type   0 击杀  1前3  2参与
	 * @param propName
	 */
	public void sendBoss1Mail(long playerId, int type, String propName, int parm) {
		try{
			Article a = ArticleManager.getInstance().getArticle(propName);
			if (a != null) {
				ArticleEntity entity = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.末日活动, null, a.getColorType(), 1, true);
				String title = "方舟挑战奖励";
				String msg = "";
				if (type == 0) {
					title = "方舟击杀奖励";
					msg = "恭喜您击杀【"+getMonster().getName()+"】，您可以获得以下奖励";
				}else if (type == 1) {
					msg = "恭喜您荣得挑战【"+getMonster().getName()+"】伤害榜第"+(parm + 1)+"名，奖励请在附件中查收。";
				}else if (type == 2) {
					msg = "恭喜您获得挑战【"+getMonster().getName()+"】参与奖，排名:"+(parm + 1)+"，奖励请在附件中查收。";
				}else {
					DoomsdayManager.logger.error(DoomsdayManager.doomsdayLoggerHead + " 头等舱奖励类型不存在");
					return;
				}
				MailManager.getInstance().sendMail(playerId, new ArticleEntity[]{entity}, title, msg, 0, 0, 0, "末日活动头等舱奖励" + type);
				DoomsdayManager.logger.warn("[获得头等舱奖励] [{}] [{}] [{}] [{}]", new Object[]{playerId, type, propName, DoomsdayManager.doomsdayBoatBornTime});
			}else {
				DoomsdayManager.logger.error(DoomsdayManager.doomsdayLoggerHead + " [头等舱奖励物品不存在] [{}] [{}] [{}]", new Object[]{playerId, propName, type});
			}
		}catch(Exception e) {
			DoomsdayManager.logger.error(DoomsdayManager.doomsdayLoggerHead + " [头等舱发奖励出错]" + playerId + " type="+type, e);
		}
	}
	
	/**
	 *发船长室的奖励
	 * @param playerId
	 * @param type   0 击杀  1前3  2参与
	 * @param propName
	 */
	public void sendBoss2Mail(long playerId, int type, String propName, int parm) {
		try{
			Article a = ArticleManager.getInstance().getArticle(propName);
			if (a != null) {
				ArticleEntity entity = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.末日活动, null, a.getColorType(), 1, true);
				String title = "方舟挑战奖励";
				String msg = "";
				if (type == 0) {
					title = "方舟击杀奖励";
					msg = "恭喜您击杀【"+getMonster().getName()+"】，您可以获得以下奖励";
				}else if (type == 1) {
					msg = "恭喜您荣得挑战【"+getMonster().getName()+"】伤害榜第"+(parm + 1)+"名，奖励请在附件中查收。";
				}else if (type == 2) {
					msg = "恭喜您获得挑战【"+getMonster().getName()+"】参与奖，排名:"+(parm + 1)+"，奖励请在附件中查收。";
				}else {
					DoomsdayManager.logger.error(DoomsdayManager.doomsdayLoggerHead + " 船长室奖励类型不存在");
					return;
				}
				MailManager.getInstance().sendMail(playerId, new ArticleEntity[]{entity}, title, msg, 0, 0, 0, "末日活动船长奖励" + type);
				DoomsdayManager.logger.warn("[获得船长奖励] [{}] [{}] [{}] [{}]", new Object[]{playerId, type, propName, DoomsdayManager.doomsdayBoatBornTime});
			}else {
				DoomsdayManager.logger.error(DoomsdayManager.doomsdayLoggerHead + " [船长奖励物品不存在] [{}] [{}] [{}]", new Object[]{playerId, propName, type});
			}
		}catch(Exception e) {
			DoomsdayManager.logger.error(DoomsdayManager.doomsdayLoggerHead + " [船长发奖励出错]" + playerId + " type="+type, e);
		}
	}
	
	public void refDamageBillboard() {
		AttackRecord ars[] = monster.attackRecordList.toArray(new AttackRecord[0]);
		HashMap<Long, JiazuBossDamageRecord> mapDamage = new HashMap<Long, JiazuBossDamageRecord>();
		for (int i = 0; i < ars.length; i++) {
			AttackRecord a = ars[i];
			if (a.living instanceof Player) {
				Player player = (Player)a.living;
				JiazuBossDamageRecord record = mapDamage.get(player.getId());
				if (record == null) {
					record = new JiazuBossDamageRecord(player.getId(), player.getName(), a.damage);
				}else {
					record.setDamage(record.getDamage() + a.damage);
				}
				mapDamage.put(player.getId(), record);
			} else if (a.living instanceof Pet) {
				Pet pet = (Pet) a.living;
				Player p = pet.getMaster();
				if (p != null) {
					JiazuBossDamageRecord record = mapDamage.get(p.getId());
					if (record == null) {
						record = new JiazuBossDamageRecord(p.getId(), p.getName(), a.damage);
					}else {
						record.setDamage(record.getDamage() + a.damage);
					}
					mapDamage.put(p.getId(), record);
				}
			}
		}
		bossDamages.clear();
		bossDamages.addAll(mapDamage.values());
		Collections.sort(bossDamages);
	}
	
	public void setCountryType(int countryType) {
		this.countryType = countryType;
	}
	public int getCountryType() {
		return countryType;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	public Game getGame() {
		return game;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getState() {
		return state;
	}

	public void setMonster(Monster monster) {
		this.monster = monster;
	}

	public Monster getMonster() {
		return monster;
	}

	public void setBossId(int bossId) {
		this.bossId = bossId;
	}

	public int getBossId() {
		return bossId;
	}
	
	public boolean isRellOver() {
		if (overTime > 0 && System.currentTimeMillis() - overTime > 1000 * 20) {
			return true;
		}
		return false;
	}
	
	public boolean isOver() {
		return getState() == STATE_OVER;
	}
	
	public boolean isTimeOver() {
		long now = System.currentTimeMillis();
		if (now - startTime > lifeTime) {
			return true;
		}
		return false;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}
	
	public ArrayList<JiazuBossDamageRecord> getBossDamages () {
		return bossDamages;
	}
	
}
