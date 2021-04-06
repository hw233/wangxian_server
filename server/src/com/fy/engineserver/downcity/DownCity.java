package com.fy.engineserver.downcity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.downcity.downcity2.DownCityManager2;
import com.fy.engineserver.downcity.stat.DownCityConsumeInfo;
import com.fy.engineserver.downcity.stat.DownCityOutputInfo;
import com.fy.engineserver.downcity.stat.DownCityPlayerInfo;
import com.fy.engineserver.downcity.stat.DownCitySchedule;
import com.fy.engineserver.downcity.stat.DownCityScheduleManager;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.Team;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;

/**
 * 副本
 * 
 * 
 */
public class DownCity {

	/**
	 * 副本ID
	 */
	protected String id;

	// 副本对应的配置信息
	protected DownCityInfo di;

	/**
	 * 副本重置状态，0代表不理会重置类型可以手动重置，1表示重置类型生效
	 * 此状态由该副本第一个boss的行为触发
	 */
	protected byte downCityState;

	// 副本对应的地图
	protected Game game;

	protected long createTime;

	protected long completeTime;

	/**
	 * 通关时间，这个时间只是把副本中所有的BOSS清掉的时间点
	 */
	protected long tongguanTimePoint = -1;

	public void 设置通关时间() {
		setTongguanTimePoint(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		DownCityScheduleManager mm = DownCityScheduleManager.getInstance();
		DownCitySchedule ds = mm.getDownCitySchedule(this.id);
		if (ds != null) {
			ds.setTongguanTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
			ds.setDirty(true);
		}
	}

	protected long endTime;

	/**
	 * 保留此副本进度的玩家
	 */
	protected ArrayList<Long> keepProcessPlayerList = new ArrayList<Long>();

	private boolean flag_10000 = false;
	private boolean flag_30000 = false;
	private boolean flag_60000 = false;
	private boolean flag_120000 = false;
	private boolean flag_15M = false;
	private boolean flag_10M = false;
	private boolean flag_5M = false;
	private boolean flag_3M = false;
	protected int threadIndex;

	/**
	 * 最后一次存盘的时间
	 */
	protected long lastSaveTime = 0;

	/**
	 * 准备被传送出副本的玩家
	 */
	protected HashMap<Player, Long> transferPlayerMap = new HashMap<Player, Long>();

	protected boolean isAllMonsterDead;

	long checkIsAllDeadInterval;

	// 检测用户在副本中的时间
	public long lastCheckLastingTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();

	public int getThreadIndex() {
		return threadIndex;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DownCityInfo getDi() {
		return di;
	}

	public void setDi(DownCityInfo di) {
		this.di = di;
	}

	public String getDownCityStateAsString() {
		if (downCityState == 0) {
			return Translate.text_3962;
		} else {
			return Translate.text_3963;
		}
	}

	public byte getDownCityState() {
		return downCityState;
	}

	public void setDownCityState(byte downCityState) {
		if (this.downCityState != downCityState) {
			// DownCityManager.logger.info("[设置副本进度重置标识] ["+Thread.currentThread().getName()+"] ["+this.id+"] ["+di.getName()+"] ["+this.downCityState+"-->"+downCityState+"]");
			if (DownCityManager.logger.isInfoEnabled()) DownCityManager.logger.info("[设置副本进度重置标识] [{}] [{}] [{}] [{}-->{}]", new Object[] { Thread.currentThread().getName(), this.id, di.getName(), this.downCityState, downCityState });
			this.downCityState = downCityState;
		}
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	protected DownCity(String id, DownCityInfo di) {
		this.id = id;
		this.di = di;
	}

	/**
	 * 判断副本是否还有效，被玩家或者系统重置的副本都无效
	 * 
	 * @return
	 */
	public boolean isValid() {
		if (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() < endTime) {
			return true;
		}
		return false;
	}

	public void init() {

	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getTongguanTimePoint() {
		return tongguanTimePoint;
	}

	public void setTongguanTimePoint(long tongguanTimePoint) {
		this.tongguanTimePoint = tongguanTimePoint;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	/**
	 * 在副本里的玩家
	 * @return
	 */
	public Player[] getPlayersInGame() {
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

	/**
	 * 保留此副本进度的玩家
	 * @return
	 */
	public Player[] getPlayersKeepingProcess() {
		PlayerManager pm = PlayerManager.getInstance();
		ArrayList<Player> al = new ArrayList<Player>();
		for (int i = 0; i < keepProcessPlayerList.size(); i++) {
			long id = keepProcessPlayerList.get(i);
			try {
				Player p = pm.getPlayer(id);
				al.add(p);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return al.toArray(new Player[0]);
	}

	public void addPlayerKeepingProcess(Player p) {
		if (keepProcessPlayerList.contains(p.getId()) == false) {
			keepProcessPlayerList.add(p.getId());
		}
	}

	private void removePlayerFromDownCity(Player p, String reason) {
		if (transferPlayerMap.containsKey(p) == false) {
			transferPlayerMap.put(p, com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_3964);
			p.addMessageToRightBag(req);
		} else {
			Long l = transferPlayerMap.get(p);
			if (l.longValue() + 5000 < com.fy.engineserver.gametime.SystemTime.currentTimeMillis()) {
				TransportData td = new TransportData(0, 0, 0, 0, p.getResurrectionMapName(), p.getResurrectionX(), p.getResurrectionY());
				game.transferGame(p, td);
				transferPlayerMap.remove(p);

				if (DownCityManager.logger.isWarnEnabled()) DownCityManager.logger.warn("[副本踢人] [玩家：{}][{}] [副本：{}] [{}] [{}] [{}]", new Object[] { p.getName(), p.getId(), getId(), di.getName(), di.getMapName(), reason });
			}
		}
	}

	private void saveDownCityProcess(HashMap<String, Long> map) {
		DownCityManager dcm = DownCityManager.getInstance();
		dcm.saveDownCityProcess(this, map);
	}

	public void heartbeat() {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (now < createTime) {
			return;
		}

		if (now - lastSaveTime > 60000) {
			if (game.getSpriteFlushAgent().isDirty()) {
				lastSaveTime = now;
				HashMap<String, Long> map = game.getSpriteFlushAgent().saveLastDisappearTime();
				game.getSpriteFlushAgent().setDirty(false);
				saveDownCityProcess(map);
			}
		}

		if (now > endTime && endTime > 0) {
			Player players[] = getPlayersInGame();
			for (int i = 0; i < players.length; i++) {
				TransportData td = new TransportData(0, 0, 0, 0, players[i].getResurrectionMapName(), players[i].getResurrectionX(), players[i].getResurrectionY());
				game.transferGame(players[i], td);
				// DownCityManager.logger.warn("[副本踢人] [玩家："+players[i].getName()+"] [玩家ID："+players[i].getId()+"] [副本ID："+getId()+"] ["+di.getName()+"] ["+di.getMapName()+"] [副本到时]");
				if (DownCityManager.logger.isWarnEnabled()) DownCityManager.logger.warn("[副本踢人] [玩家：{}] [玩家ID：{}] [副本ID：{}] [{}] [{}] [副本到时]", new Object[] { players[i].getName(), players[i].getId(), getId(), di.getName(), di.getMapName() });

			}
			return;
		}
		ArrayList<Team> teamList = new ArrayList<Team>();
		Player players[] = getPlayersInGame();
		for (int i = 0; i < players.length; i++) {
			Team t = players[i].getTeam();
			if (t != null && teamList.contains(t) == false) {
				teamList.add(t);
			}
		}

		if (teamList.size() == 1) {
			Team t = teamList.get(0);
			for (int i = 0; i < players.length; i++) {
				if (!t.contains(players[i]) && players[i].getEnterGameTime() + 5000 < now) {
					removePlayerFromDownCity(players[i], Translate.text_3965);
				}
			}
		} else {
			Player firstEnterPlayer = null;

			for (int i = 0; i < players.length; i++) {
				if (firstEnterPlayer == null) {
					if (players[i].getTeam() != null && players[i].getTeamMembers() != null && players[i].getTeamMembers().length > 1) {
						firstEnterPlayer = players[i];
					}
				} else {
					if (players[i].getTeam() != null && players[i].getTeamMembers() != null && players[i].getTeamMembers().length > 1) {
						if (firstEnterPlayer.getEnterGameTime() > players[i].getEnterGameTime()) {
							firstEnterPlayer = players[i];
						}
					}
				}
			}

			if (firstEnterPlayer == null) {
				for (int i = 0; i < players.length; i++) {
					removePlayerFromDownCity(players[i], Translate.副本里没有队伍);
				}
			} else {
				for (int i = 0; i < players.length; i++) {
					if (players[i] != firstEnterPlayer && !players[i].isSameTeam(firstEnterPlayer) && players[i].getEnterGameTime() + 5000 < now) {
						removePlayerFromDownCity(players[i], Translate.多个队伍进入同一副本);
					}
				}
			}
		}

		if (flag_10000 == false && endTime > 0 && endTime - now < 10000) {
			flag_10000 = true;

			for (int i = 0; i < players.length; i++) {
				players[i].send_HINT_REQ(Translate.text_3967);
			}
		} else if (flag_30000 == false && endTime > 0 && endTime - now < 30000) {
			flag_30000 = true;

			for (int i = 0; i < players.length; i++) {
				players[i].send_HINT_REQ(Translate.text_3968);
			}
		} else if (flag_60000 == false && endTime > 0 && endTime - now < 60000) {
			flag_60000 = true;

			for (int i = 0; i < players.length; i++) {
				players[i].send_HINT_REQ(Translate.text_3969);
			}
		} else if (flag_120000 == false && endTime > 0 && endTime - now < 120000) {
			flag_120000 = true;

			for (int i = 0; i < players.length; i++) {
				players[i].send_HINT_REQ(Translate.text_3970);
			}
		} else if (flag_3M == false && endTime > 0 && endTime - now < 180000) {
			flag_3M = true;

			for (int i = 0; i < players.length; i++) {
				players[i].send_HINT_REQ(Translate.text_3971);
			}
		} else if (flag_5M == false && endTime > 0 && endTime - now < 300000) {
			flag_5M = true;

			for (int i = 0; i < players.length; i++) {
				players[i].send_HINT_REQ(Translate.text_3972);
			}
		} else if (flag_10M == false && endTime > 0 && endTime - now < 600000) {
			flag_10M = true;

			for (int i = 0; i < players.length; i++) {
				players[i].send_HINT_REQ(Translate.text_3973);
			}
		} else if (flag_15M == false && endTime > 0 && endTime - now < 900000) {
			flag_15M = true;

			for (int i = 0; i < players.length; i++) {
				players[i].send_HINT_REQ(Translate.text_3974);
			}
		}

		flashBossRelyJYMonster(players);
		
		// 判断副本中的怪是否都被打死
		if (this.checkIsAllDeadInterval % 2 == 0 && !this.isAllMonsterDead && (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - this.createTime) > 30000) {
			LivingObject[] objects = this.game.getLivingObjects();
			if (objects != null && objects.length > 0) {
				boolean isAllDead = true;
				for (int i = 0; i < objects.length; i++) {
					if (objects[i] instanceof Monster) {
						if (objects[i].getState() != LivingObject.STATE_DEAD) {
							isAllDead = false;
							break;
						}
					}
				}
				if (isAllDead) {
					long t = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
					this.completeTime = t;
					if (t - this.createTime > 0) {
						// DungeonsRecordManager.getInstance().update(this,players, t-this.createTime);
					}
					this.isAllMonsterDead = isAllDead;
				}
			}
		}
		this.checkIsAllDeadInterval++;

		// /////////////////
		if (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - lastCheckLastingTime > 5000) {
			lastCheckLastingTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			Player ps[] = this.getPlayersInGame();
			for (int i = 0; i < ps.length; i++) {
				this.statPlayerAddLastingTime(ps[i], 5000);
			}
		}
	}

	public long getLastSaveTime() {
		return lastSaveTime;
	}

	public boolean bossBourn = false;
	
	public void flashBossRelyJYMonster(Player players[]){
		DownCityManager dcm = DownCityManager.getInstance();
		/**特殊副本处理,wangxiangmiejing,"wangxiangmiejingyuanshen","fengduguijing","fengdouguijingyuanshen"**/
		MemoryMonsterManager mmm = (MemoryMonsterManager)MemoryMonsterManager.getMonsterManager();
		for(int k=0;k<DownCityManager.limitFlyName.length;k++){
			if(DownCityManager.limitFlyName[k].equals(di.mapName)){
				LivingObject[] objects = this.game.getLivingObjects();
				if (objects != null && objects.length > 0) {
					boolean isAllDead = true;
					for (int i = 0; i < objects.length; i++) {
						if (objects[i] instanceof Monster) {
							Monster monster = (Monster) objects[i];
							int [] 应该死的精英怪 = DownCityManager.flashBossRelyJy.get(di.mapName);
							for(int a=0;a<应该死的精英怪.length;a++){
								if(monster.getSpriteCategoryId()==应该死的精英怪[a]){
//									System.out.println("怪物id:"+monster.getSpriteCategoryId()+"---应该死的精英怪："+Arrays.toString(应该死的精英怪)+"");
									if (objects[i].getState() != LivingObject.STATE_DEAD) {
										isAllDead = false;
										break;
									}
								}
							}
						}
					}
					//boss是否出生没有保存
					if (isAllDead && !bossBourn) {
						int monsterid = DownCityManager.limitFlushMonsterIds[k];
						int[] point = DownCityManager.通过副本英文名得到BOSS坐标(di.mapName);
						Monster m = mmm.createMonster(monsterid);
//						System.out.println("副本名："+di.mapName+"\n怪物id："+monsterid+"---"+"怪物名："+m.getName()+"出生点x："+point[0]+"出生点y："+point[1]);
						if(m != null){
							//测试1928,3755
							m.setX(point[0]);
							m.setY(point[1]);
							m.setBornPoint(new com.fy.engineserver.core.g2d.Point(point[0],point[1]));
							m.setAlive(true);
							game.addSprite(m);
							bossBourn = true;
							for (int i = 0; i < players.length; i++) {
								if(players[i]!=null){
									players[i].sendError("BOSS已经刷新，位置坐标是["+point[0]+","+point[1]+"].");
								}
							}
							
						}
					}
				}
			}
		}
	}
	
	public void setLastSaveTime(long lastSaveTime) {
		this.lastSaveTime = lastSaveTime;
	}

	// //////////////////////////////////////////////////////////////
	// 统计相关的方法
	//

	/**
	 * 伤害输出
	 */
	public void statPlayerAttack(Player p, int damageType, int damage) {
		DownCityPlayerInfo pi = this.getDownCityPlayerInfo(p);
		if (pi != null) {
			pi.updatePlayerInfo(p);
			pi.addShanghaiShuChu(damage);
		}
	}

	/**
	 * 治疗输出
	 * @param p
	 * @param damageType
	 * @param damage
	 */
	public void statPlayerEnrichHP(Player p, int hp) {
		DownCityPlayerInfo pi = this.getDownCityPlayerInfo(p);
		if (pi != null) {
			pi.updatePlayerInfo(p);
			pi.addZhiliaoShuChu(hp);
		}
	}

	/**
	 * 被治疗
	 * @param p
	 * @param hp
	 */
	public void statPlayerBeEnrichHP(Player p, int hp) {
		DownCityPlayerInfo pi = this.getDownCityPlayerInfo(p);
		if (pi != null) {
			pi.updatePlayerInfo(p);
			pi.addReceiveZhiliao(hp);
		}
	}

	/**
	 * 被攻击
	 * @param p
	 * @param damageType
	 * @param damage
	 */
	public void statPlayerBeAttack(Player p, int damageType, int damage) {
		DownCityPlayerInfo pi = this.getDownCityPlayerInfo(p);
		if (pi != null) {
			pi.updatePlayerInfo(p);
			pi.addReceiveDamage(damage);
		}
	}

	/**
	 * 死亡
	 * @param p
	 */
	public void statPlayerDead(Player p) {
		DownCityPlayerInfo pi = this.getDownCityPlayerInfo(p);
		if (pi != null) {
			pi.updatePlayerInfo(p);
			pi.addDeadCount(1);
		}
	}

	public void statPlayerHuanliaoHp(Player p, int hp) {
		DownCityPlayerInfo pi = this.getDownCityPlayerInfo(p);
		if (pi != null) {
			pi.updatePlayerInfo(p);
			pi.addHuanhuiHp(hp);
		}
	}

	public void statPlayerHuanliaoMp(Player p, int mp) {
		DownCityPlayerInfo pi = this.getDownCityPlayerInfo(p);
		if (pi != null) {
			pi.updatePlayerInfo(p);
			pi.addHuanhuiMp(mp);
		}
	}

	public void statPlayerShunliaoHp(Player p, int hp) {
		DownCityPlayerInfo pi = this.getDownCityPlayerInfo(p);
		if (pi != null) {
			pi.updatePlayerInfo(p);
			pi.addShunhuiHp(hp);
		}
	}

	public void statPlayerShunliaoMp(Player p, int mp) {
		DownCityPlayerInfo pi = this.getDownCityPlayerInfo(p);
		if (pi != null) {
			pi.updatePlayerInfo(p);
			pi.addShunhuiMp(mp);
		}
	}

	public void statPlayerAddLastingTime(Player p, long lastingTime) {
		DownCityPlayerInfo pi = this.getDownCityPlayerInfo(p);
		if (pi != null) {
			pi.updatePlayerInfo(p);
			pi.addLastingTime(lastingTime);
		}
	}

	/**
	 * 哪个装备掉了多少耐久
	 * @param p
	 * @param ee
	 * @param d
	 */
	public void statPlayerDurability(Player p, EquipmentEntity ee, int d) {
	}

	/**
	 * 刷新用户的状态
	 * @param p
	 */
	public void statUpdatePlayerStatus(Player p) {
		DownCityPlayerInfo pi = this.getDownCityPlayerInfo(p);
		if (pi != null) {
			pi.updatePlayerInfo(p);
		}
	}

	private DownCityPlayerInfo getDownCityPlayerInfo(Player p) {
		DownCityScheduleManager mm = DownCityScheduleManager.getInstance();
		if (mm != null) {
			DownCitySchedule ds = mm.getDownCitySchedule(this.id);
			if (ds != null) {
				DownCityPlayerInfo info = ds.getDownCityPlayerInfos().get(p.getId());
				if (info == null) {
					info = new DownCityPlayerInfo();
					info.updatePlayerInfo(p);
					ds.getDownCityPlayerInfos().put(p.getId(), info);
				}
				ds.setDirty(true);
				return info;
			}
		}
		return null;
	}

	/**
	 * 副本产出
	 * @param monsterName
	 * @param artileNames
	 */
	public void statDowncityOutput(String monsterName, String articleNames, int money) {
		DownCityScheduleManager mm = DownCityScheduleManager.getInstance();
		if (mm != null) {
			DownCitySchedule ds = mm.getDownCitySchedule(this.id);
			if (ds != null) {
				ArrayList<DownCityOutputInfo> infoList = ds.getDownCityOutputInfos();
				if (infoList == null) {
					infoList = new ArrayList<DownCityOutputInfo>();
					ds.setDownCityOutputInfos(infoList);
				}
				DownCityOutputInfo e = new DownCityOutputInfo();
				e.setFlopMoney(money);
				e.setMonsterName(monsterName);
				e.setOutputPoint(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
				e.setPropName(articleNames);
				infoList.add(e);
				ds.setDirty(true);
			}
		}
	}

	/**
	 * 副本消耗
	 * @param p
	 * @param propName
	 */
	public void statDowncityConsume(Player p, String propName) {
		DownCityScheduleManager mm = DownCityScheduleManager.getInstance();
		DownCitySchedule ds = mm.getDownCitySchedule(this.id);
		if (ds != null) {
			ArrayList<DownCityConsumeInfo> infoList = ds.getDownCityConsumeInfos();
			if (infoList == null) {
				infoList = new ArrayList<DownCityConsumeInfo>();
				ds.setDownCityConsumeInfos(infoList);
			}
			DownCityConsumeInfo e = new DownCityConsumeInfo();
			e.setId(p.getId());
			e.setPlayerName(p.getName());
			e.setUserName(p.getUsername());
			e.setPropName(propName);
			infoList.add(e);
			ds.setDirty(true);
		}
	}

	public void setThreadIndex(int threadIndex) {
		this.threadIndex = threadIndex;
	}

	boolean 通关标记 = false;

	public void 副本通关操作() {
		if (!通关标记) {
			通关标记 = true;
			设置通关时间();

			// 副本中的人都扣除副本所需物品
			LivingObject los[] = game.getLivingObjects();
			try {
				for (LivingObject lo : los) {
					if (lo instanceof Player) {
						try {
							((Player) lo).增加今天进入副本次数(di.mapName);
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.恭喜你成功通关副本);
							((Player) lo).addMessageToRightBag(hreq);
							DownCityManager.logger.error("[副本通关设置人物通关次数] [成功] [副本:" + di.name + "] [" + ((Player) lo).getLogString() + "] [次数:" + ((Player) lo).得到今天进入副本次数(di.mapName) + "]");
						} catch (Exception e) {
							DownCityManager.logger.error("[副本通关设置人物通关次数] [失败] [副本:" + di.name + "]  [" + ((Player) lo).getLogString() + "] [次数:" + ((Player) lo).得到今天进入副本次数(di.mapName) + "]", e);
						}
					}
				}
			} catch (Exception ex) {
				DownCityManager.logger.error("[副本通关设置人物通关次数] [失败] [副本:" + di.name + "]", ex);
			}
			RecordAction action = null;
			try {
				action = DownCityManager.通过副本统计action(di.mapName);
			} catch (Exception eex) {
				PlayerAimManager.logger.error("[目标系统] [统计通过副本异常] [地图名:" + di.mapName + "]");
			}
			String articleName = DownCityManager.通过副本英文名得到进入副本所需物品(di.mapName);
			ArticleManager am = ArticleManager.getInstance();
			Article a = am.getArticle(articleName);
			if (a != null) {
				try {
					for (LivingObject lo : los) {
						if (lo instanceof Player) {
							Player p = (Player) lo;
							int count = p.getArticleEntityNum(articleName);
							if (count > 0) {
								ArticleEntity ae = p.getArticleEntity(articleName);
								ArticleEntity removeEntity = p.removeArticleEntityFromKnapsackByArticleId(ae.getId(), "副本删除", true);
								if (removeEntity != null) {
									DownCityManager.logger.warn("[副本通关删除副本所需物品] [成功] [副本:" + di.name + "] [" + p.getLogString() + "] [删除物品名:" + articleName + "]");
								}
							} else {
								// 删除仓库中的物品
								Knapsack cangku = p.getKnapsacks_cangku();
								if (cangku != null) {
									int index = cangku.indexOf(articleName);
									if (index >= 0) {
										ArticleEntity removeEntity = cangku.remove(index, "副本删除", true);
										if (removeEntity != null) {
											DownCityManager.logger.warn("[副本通关删除副本所需物品] [成功] [副本:" + di.name + "] [" + p.getLogString() + "] [删除仓库物品名:" + articleName + "]");
										}
									} else {
										DownCityManager.logger.warn("[副本通关删除副本所需物品] [失败] [副本:" + di.name + "] [" + p.getLogString() + "] [删除仓库物品名:" + articleName + "]");
									}
								}
							}
							try {
								if(action != null) {
									EventWithObjParam evt3 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { p.getId(), action, 1L});
									EventRouter.getInst().addEvent(evt3);
								}
							} catch (Exception eex) {
								PlayerAimManager.logger.error("[目标系统] [统计通过副本异常] [" + p.getLogString() + "]");
							}
						}
					}
				} catch (Exception ex) {
					DownCityManager.logger.error("[副本通关删除副本所需物品] [失败] [副本:" + di.name + "]", ex);
				}
			} else {
				DownCityManager.logger.error("[副本通关删除副本所需物品] [失败] [副本:" + di.name + "] [不需要删除物品]");
			}

			// 可以给一些奖励
			try {
				MailManager mm = MailManager.getInstance();
				String aName = DownCityManager.通过副本英文名得到副本给的物品(di.mapName);
				if (aName != null) {
					ArticleEntityManager aem = ArticleEntityManager.getInstance();
					Article article = am.getArticle(aName);
					if (article != null) {
						for (LivingObject lo : los) {
							if (lo instanceof Player) {
								Player p = (Player) lo;
								if (p.getTeam() != null) {
									if (di.mapName.equals(p.getTeam().perpareEnterDownCityName)) {
										ArticleEntity ae = aem.createEntity(article, false, ArticleEntityManager.CREATE_REASON_DOWNCITY, p, article.getColorType(), 1, true);
										mm.sendMail(p.getId(), new ArticleEntity[] { ae }, Translate.恭喜通关副本成功, Translate.附件为您通关副本得到的奖励, 0, 0, 0, "通关副本奖励");
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.恭喜你成功通关副本获得通关奖励);
										p.addMessageToRightBag(hreq);
										DownCityManager.logger.error("[副本通关给人物发奖励] [成功] [副本:" + di.name + "] [" + ((Player) lo).getLogString() + "] [次数:" + ((Player) lo).得到今天进入副本次数(di.mapName) + "]");
										DownCityManager.logger.error("[副本通关给人物发奖励] [成功] [副本:" + di.name + "] [队伍信息:" + p.getTeamLogString() + "}]");
										try {
											DownCityManager2.instance.notifyPlayerPassDownCity(p, di.mapName);
										} catch (Exception e) {
											DownCityManager.logger.error("[副本通关弹出转盘] [失败] [副本:" + di.name + "] [" + ((Player) lo).getLogString() + "]", e);
										}
									} else {
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.您所在的队伍副本不是这个副本无法获得奖励);
										p.addMessageToRightBag(hreq);
										DownCityManager.logger.error("[副本通关给人物发奖励] [失败] [副本:" + di.name + "]  [" + ((Player) lo).getLogString() + "] [次数:" + ((Player) lo).得到今天进入副本次数(di.mapName) + "]");
									}
								} else {
									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.您没有队伍无法获得奖励);
									p.addMessageToRightBag(hreq);
									DownCityManager.logger.error("[副本通关给人物发奖励] [失败] [副本:" + di.name + "]  [" + ((Player) lo).getLogString() + "] [次数:" + ((Player) lo).得到今天进入副本次数(di.mapName) + "]");
								}
							}
						}
					}
				}

			} catch (Exception ex) {
				DownCityManager.logger.error("[副本通关给人物发奖励] [失败] [副本:" + di.name + "]", ex);
			}
			
			DownCityManager.logger.warn("[副本通关] [成功] [副本:" + di.name + "] [副本id:" + this.id + "]");
		}
	}
}
