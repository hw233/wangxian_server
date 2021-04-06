package com.fy.engineserver.activity.fireActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.Buff_FireRate;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.playerTitles.PlayerTitlesManager;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.horse.dateUtil.DateFormat;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.Utils;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class FireActivityNpcEntity {

	// public static String buffName = "家族篝火";
	public static FireManager fm = FireManager.getInstance();
	private long stationId;
	private transient SeptStation ss;
	// 上次点火时间
	private long lastFireTime;
	// 点火次数
	private int fireNum;
	// 加柴时间
	private long addWoodNumTime;
	// 加柴次数
	private int addWoodNum;
	// 状态
	private byte state = 0;
	private String fireActivityTemplate;
	private transient FireActivityTemplate template;

	private long updateTime;

	private int level = 0;

	private transient NPC nowNpc;

	public FireActivityNpcEntity() {

	}

	public FireActivityNpcEntity(long stationId) {
		super();
		this.stationId = stationId;
	}

	public void init(SeptStation septStation) {
		FireActivityTemplate ft = fm.fireMap.get(fireActivityTemplate);
		if (ft != null) {
			setTemplate(ft);
			this.ss = septStation;
			if (ss == null) {
				FireManager.logger.error("[初始化家族篝火活动npc失败] [指定的驻地为null] [" + stationId + "]");
				return;
			}
			Game game = ss.getGame();
			MemoryNPCManager mnm = (MemoryNPCManager) MemoryNPCManager.getNPCManager();
			int npcId = -1;
			if (state == FireActivityState.未开始.type || state == FireActivityState.完成.type) {
				// 点火前npc
				npcId = this.template.getNpcIds()[0];
			} else {
				// 点火后npc
				npcId = this.template.getNpcIds()[1];
			}
			NPC npc = null;
			if (npcId > 0) {
				npc = mnm.createNPC(npcId);
				if (npc == null) {
					FireManager.logger.error("[初始化家族篝火活动npc失败] [指定的npcId为null] [npcId:" + npcId + "]");
					return;
				} else {
					if (npc instanceof FireActivityNpc) {
						FireActivityNpc fn = (FireActivityNpc) npc;
						fn.setSeptId(septStation.getId());
						this.nowNpc = fn;
						npc.setX(template.getX());
						npc.setY(template.getY());
						game.addSprite(npc);
						if (FireManager.logger.isWarnEnabled()) {
							FireManager.logger.warn("[初始化家族篝火活动npc实体完成] [npcId:"+npcId+"] [x:"+template.getX()+"] [y:"+template.getY()+"] [" + septStation.getId() + "]");
						}
					} else {
						if (FireManager.logger.isInfoEnabled()) {
							FireManager.logger.info("[初始化家族篝火活动npc失败] [指定的npc不是FireActivityNpc]");
						}
					}
				}
			} else {
				FireManager.logger.error("[初始化家族篝火活动npc失败] [没有找到指定npcId] [npcId:" + npcId + "]");
			}
		} else {
			FireManager.logger.error("[初始化家族篝火活动npc失败] [没有指定的篝火实体模板name] [模板:" + fireActivityTemplate + "]");
		}
	}

	private transient List<Player> inSeptPlayers = new ArrayList<Player>();

	public void heartbeat() {

		if (this.state == FireActivityState.未开始.type) return;
		if (inSeptPlayers == null) {
			FireManager.logger.error("[家族篝火初始化inSeptPlayers] [inSeptPlayers 为Null]");
			inSeptPlayers = new ArrayList<Player>();
		}
		if (this.state == FireActivityState.完成.type) {

			Game game = ss.getGame();
			MemoryNPCManager mnm = (MemoryNPCManager) MemoryNPCManager.getNPCManager();
			int npcId = -1;
			npcId = this.template.getNpcIds()[0];
			NPC npc = null;
			if (npcId > 0) {
				npc = mnm.createNPC(npcId);
				if (npc == null) {
					FireManager.logger.error("[家族篝火活动完成还原npc失败] [指定的npcId为null] [npcId:" + npcId + "]");
					return;
				} else {
					if (npc instanceof FireActivityNpc) {
						FireActivityNpc fn = (FireActivityNpc) npc;
						fn.setSeptId(this.stationId);
						game.removeSprite(nowNpc);
						this.nowNpc = fn;
						npc.setX(template.getX());
						npc.setY(template.getY());
						game.addSprite(npc);
						setState(FireActivityState.未开始.type);
						if (FireManager.logger.isWarnEnabled()) {
							FireManager.logger.warn("[家族篝火活动完成还原npc] [npcId:" + npcId + "] [驻地Id:" + ss.getId() + "]");
						}
						return;
					} else {
						FireManager.logger.error("[家族篝火活动完成还原npc失败] [指定的npc不是FireActivityNpc] [npcId:" + npcId + "]");
					}
				}
			} else {
				FireManager.logger.error("[家族篝火活动完成还原npc失败] [没有找到指定npcId] [npcId:" + npcId + "]");
			}
		}

		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();

		if (now - updateTime < template.getExpInterval() * 1000) {
			return;
		}

		setUpdateTime(now);
		if (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - lastFireTime >= template.getLastTime() * 1000) {
			this.state = FireActivityState.完成.type;
			return;
		}
		LivingObject[] los = ss.getGame().getLivingObjects();
		inSeptPlayers.clear();
		for (LivingObject lo : los) {
			if (lo instanceof Player) {
				for (Buff buff : ((Player) lo).getBuffs()) {
					if (buff instanceof Buff_FireRate) {
						if (Utils.getDistanceA2B(lo.getX(), lo.getY(), nowNpc.getX(), nowNpc.getY()) <= template.getDistance()) {
							Buff_FireRate buf = (Buff_FireRate)buff;
							if(buf.getInvalidTime() > System.currentTimeMillis()){
								inSeptPlayers.add((Player) lo);
							}
						}
					}
				}
			}
		}
		int num = inSeptPlayers.size();
		if (this.state == FireActivityState.点火后.type) {
			if (num > 0) {
				for (Player player : inSeptPlayers) {
					for (Buff buff : player.getBuffs()) {
						if (buff instanceof Buff_FireRate) {
							this.firstAddExp(level, player, num, buff);
						}
					}
				}
			}
		} else if (this.state == FireActivityState.加柴后.type) {
			if (num > 0) {

				for (Player player : inSeptPlayers) {
					for (Buff buff : player.getBuffs()) {
						if (buff instanceof Buff_FireRate) {
							this.secondAddExp(level, player, num, buff);
						}
					}
				}
			}
		}
		try {
			if (this.state == FireActivityState.加柴后.type || this.state == FireActivityState.点火后.type) {
				if (nowNpc != null) {
					long time = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - lastFireTime;
					if (time < template.getLastTime() * 1000) {
						nowNpc.setTitle("<f color='0xFFFF00'>" + TimeTool.instance.getShowTime(template.getLastTime() * 1000 - time) + "</f>");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean 点火(Player player) {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		String nowst = DateFormat.getSimpleDay(new Date(now));
		String oldst = DateFormat.getSimpleDay(new Date(this.lastFireTime));
		if (nowst.equals(oldst)) {
			if (template.getFireNum() <= this.fireNum) {
				// player.send_HINT_REQ("今天布阵次数已够");
				player.send_HINT_REQ(Translate.今天布阵次数已够);
				if (FireManager.logger.isWarnEnabled()) {
					FireManager.logger.warn("[家族篝火活动点火失败] [" + player.getLogString() + "] [今天布阵次数已够]");
				}
				return false;
			}
		} else {
			this.fireNum = 0;
		}
		setLastFireTime(now);
		setFireNum(++this.fireNum);
		setLevel(0);
		int npcId = -1;
		npcId = this.template.getNpcIds()[1];
		NPC npc = null;
		Game game = ss.getGame();
		MemoryNPCManager mnm = (MemoryNPCManager) MemoryNPCManager.getNPCManager();
		if (npcId > 0) {
			npc = mnm.createNPC(npcId);
		}
		if (npc != null) {
			if (npc instanceof FireActivityNpc) {
				FireActivityNpc fn = (FireActivityNpc) npc;
				fn.setSeptId(this.stationId);
				game.removeSprite(nowNpc);
				this.nowNpc = fn;
				npc.setX(template.getX());
				npc.setY(template.getY());
				game.addSprite(npc);
				if (FireManager.logger.isWarnEnabled()) {
					FireManager.logger.warn("[家族篝火活动点火成功] [" + player.getLogString() + "]");
				}
			} else {
				FireManager.logger.error("[家族篝火活动点火失败] [指定的npc不是篝火活动npc] [npcId:" + npcId + "]");
			}
		} else {
			FireManager.logger.error("[家族篝火活动点火失败] [指定的npcId为null] [npcId:" + npcId + "]");
		}
		setState(FireActivityState.点火后.type);
		return true;
	}

	public synchronized boolean 加柴(Player player, int level) {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		String nowst = DateFormat.getSimpleDay(new Date(now));
		String oldst = DateFormat.getSimpleDay(new Date(this.addWoodNumTime));
		if (nowst.equals(oldst)) {
			if (template.getAddWoodNum() <= this.addWoodNum) {
				// player.send_HINT_REQ("今天添加灵石次数已够");
				player.send_HINT_REQ(Translate.今天添加灵石次数已够);
				if (FireManager.logger.isWarnEnabled()) {
					FireManager.logger.warn("[家族篝火活动加柴失败] [" + player.getLogString() + "] [今天加柴次数已够]");
				}
				return false;
			}
		} else {
			this.addWoodNum = 0;
		}
		setAddWoodNum(++this.addWoodNum);
		setAddWoodNumTime(now);
		setLastFireTime(getLastFireTime() + template.getExtraLastTime() * 1000);
		setState(FireActivityState.加柴后.type);
		this.level = level;
		try {
			if (!JiazuManager2.家族酒称号[level].equals("")) {
				PlayerTitlesManager.getInstance().addTitle(player, JiazuManager2.家族酒称号[level], true);
			}
		}  catch (Exception e) {
			JiazuManager2.logger.error("[新家族] [家族喝酒加柴给称号] [异常] [" + player.getLogString() + "] [level: " + level + "]", e);
		}

		if (FireManager.logger.isWarnEnabled()) {
			FireManager.logger.warn("[家族篝火活动加柴成功] [" + player.getLogString() + "]");
		}
		return true;
	}

	public static enum FireActivityState {

		未开始((byte) 0),
		点火后((byte) 1),
		加柴后((byte) 2),
		完成((byte) 3);

		public byte type;

		private FireActivityState(byte type) {
			this.type = type;
		}

		public byte getType() {
			return type;
		}

		public void setType(byte type) {
			this.type = type;
		}
	}

	private void firstAddExp(int level, Player player, int num, Buff buff) {

		CommonFireEntity.addExpJiazu(level, buff, player, num, true);
	}

	private void secondAddExp(int level, Player player, int num, Buff buff) {

		CommonFireEntity.addExpJiazu(level, buff, player, num, false);

	}

	/******************* get set **********************************/
	public long getStationId() {
		return stationId;
	}

	public void setStationId(long stationId) {
		this.stationId = stationId;
		ss.setFireActivityNpcEntity(this);
	}

	public long getLastFireTime() {
		return lastFireTime;
	}

	public void setLastFireTime(long lastFireTime) {
		this.lastFireTime = lastFireTime;
		ss.setFireActivityNpcEntity(this);
	}

	public int getFireNum() {
		return fireNum;
	}

	public void setFireNum(int fireNum) {
		this.fireNum = fireNum;
		ss.setFireActivityNpcEntity(this);
	}

	public int getAddWoodNum() {
		return addWoodNum;
	}

	public void setAddWoodNum(int addWoodNum) {
		this.addWoodNum = addWoodNum;
		ss.setFireActivityNpcEntity(this);
	}

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
		ss.setFireActivityNpcEntity(this);
	}

	public String getFireActivityTemplate() {
		return fireActivityTemplate;
	}

	public void setFireActivityTemplate(String fireActivityTemplate) {
		this.fireActivityTemplate = fireActivityTemplate;
		ss.setFireActivityNpcEntity(this);

	}

	public FireActivityTemplate getTemplate() {
		return template;
	}

	public void setTemplate(FireActivityTemplate template) {
		this.template = template;
	}

	public long getAddWoodNumTime() {
		return addWoodNumTime;
	}

	public void setAddWoodNumTime(long addWoodNumTime) {
		this.addWoodNumTime = addWoodNumTime;
		ss.setFireActivityNpcEntity(this);
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
		ss.setFireActivityNpcEntity(this);
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
		ss.setFireActivityNpcEntity(this);
	}

	public SeptStation getSs() {
		return ss;
	}

	public void setSs(SeptStation ss) {
		this.ss = ss;
	}

}
