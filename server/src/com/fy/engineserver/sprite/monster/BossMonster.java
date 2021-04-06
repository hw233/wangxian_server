package com.fy.engineserver.sprite.monster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.activity.fairyRobbery.FairyRobberyManager;
import com.fy.engineserver.calculate.NumericalCalculator;
import com.fy.engineserver.carbon.devilSquare.DevilSquareManager;
import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.SET_POSITION_REQ;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.septstation.SeptStationMapTemplet;
import com.fy.engineserver.septstation.service.SeptStationManager;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.horse2.manager.Horse2Manager;
import com.fy.engineserver.sprite.npc.DoorNpc;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.sprite.pet.Pet;

public class BossMonster extends Monster {

	// public static Logger baLogger = Logger.getLogger(BossAction.class);
	public static Logger baLogger = LoggerFactory.getLogger(BossAction.class);

	private static final long serialVersionUID = -4393238389156456469L;

	// 是否免疫眩晕
	protected boolean immuneStunFlag = true;
	// 是否免疫定省
	protected boolean immuneHoldFlag = true;
	// 是否免疫沉默
	protected boolean immuneSilenceFlag = true;
	// 是否免疫减速
	protected boolean immuneSpeedDownFlag = true;
	// 是否免疫嘲讽
	protected boolean immuneSneerFlag = false;
	
	public boolean 是否有正在前往的攻击点 = false;
	public int 攻击点组 = 0;
	public int 攻击点 = 0;
	
	public static final int[][] 第一组攻击点 = {
		{-80,0},{-60,45},{-45,60},{0,80},{45,60},{60,45},
		{80,0},{60,-45},{45,-60},{0,-80},{-45,-60},{-60,-45}
	};
	
	public static final int[][] 第二组攻击点 = {
		{-48,13},{-35,35},{-13,48},{13,48},{35,35},{48,13},
		{48,-13},{35,-35},{13,-48},{-13,-48},{-35,-35},{-48,-13}
	};

	// 按固定路线巡逻，巡逻路径中的关键点，系统会自动找出一条经过这个点的路径
	protected int patrolingX[] = new int[0];
	protected int patrolingY[] = new int[0];

	protected boolean initialized = false;

	private long bournTime;// 怪物出生时间

	// //////////////////////////////////////////////////////////////////////////////////////////////////
	// 召唤boss的家族ID
	protected long jiazuId;

	// //////////////////////////////////////////////////////////////////////////////////////////////////
	// 内部数据结构

	public int[] getPatrolingX() {
		return patrolingX;
	}

	public void setPatrolingX(int[] patrolingX) {
		this.patrolingX = patrolingX;
	}

	public int[] getPatrolingY() {
		return patrolingY;
	}

	public void setPatrolingY(int[] patrolingY) {
		this.patrolingY = patrolingY;
	}

	public long getJiazuId() {
		return jiazuId;
	}

	public void setJiazuId(long jiazuId) {
		this.jiazuId = jiazuId;
	}

	// 开启标记
	protected boolean turnOnFlag = false;
	protected long turnOnTime = 0;

	// BOSS执行条目
	protected BossExecuteItem items[] = new BossExecuteItem[0];

	protected Element additionData;

	protected BossFightingAgent agent = new BossFightingAgent(this);

	// 巡逻的下一个点的下标
	protected int patrolingIndex = 0;
	// 巡逻的方向
	protected boolean followPathDirection = true;

	protected ArrayList<Monster> flushMonsterList = new ArrayList<Monster>();

	protected long disappearTime;// 初始化后消失时间
	protected int dropLevelLimit;// 击杀者等级限制，超过这个等级不掉落

	private transient long lastCheckDisappearTime;
	/** 最后一次受到伤害时间 */
	public transient long lastCauseDamageTime;

	public ArrayList<Monster> getFlushMonsterList() {
		return flushMonsterList;
	}

	protected ArrayList<NPC> flushNPCList = new ArrayList<NPC>();

	public ArrayList<NPC> getFlushNPCList() {
		return flushNPCList;
	}

	public BossFightingAgent getBossFightingAgent() {
		return agent;
	}

	public BossExecuteItem[] getBossExecuteItems() {
		return items;
	}

	public BossExecuteItem[] getItems() {
		return items;
	}

	public void setItems(BossExecuteItem[] items) {
		this.items = items;
	}
	
	public int[] targetPoint;

	/**
	 * 怪出生后，调用此初始化方法
	 */
	public void init() {
		super.init();
		if (initialized) return;
		initialized = true;

		// this.viewHeight = 480;
		// this.viewWidth = 320;
		//
		this.setViewWidth(this.getActivationWidth());
		this.setViewHeight(this.getActivationWidth());

		this.hp = this.maxHP;
		this.x = this.bornPoint.x;
		this.y = this.bornPoint.y;
		this.aura = -1;
		this.state = Sprite.STATE_STAND;

		this.stun = false;
		this.hold = false;
		this.immunity = false;
		this.invulnerable = false;
		this.poison = false;
		this.weak = false;
		this.silence = false;
		this.bournTime = SystemTime.currentTimeMillis();
	}

	protected void constructPatrolingPath() {
		if (this.traceType != 2) return;

	}

	/**
	 * 设置Boss的属性
	 */
	public void setAdditionData(Element e) {
		additionData = e;
	}

	public boolean needDisppear(long time) {
		if (disappearTime == -1) {
			return false;
		}
		return time - bournTime > disappearTime;
	}

	/**
	 * 巡逻
	 * 
	 * 运动轨迹
	 * 0 原地不动
	 * 1 按一定的轨迹巡逻
	 * 2 按给定的一条路径巡逻
	 * @param game
	 */
	protected void patroling(Game game) {
		if (traceType == 1) {
			super.patroling(game);
		} else {
			if (this.patrolingX.length == 0) {
				return;
			}
			if (patrolingIndex >= 0 && this.patrolingIndex < patrolingX.length) {
				agent.pathFinding(game, patrolingX[patrolingIndex], patrolingY[patrolingIndex]);
				if (followPathDirection) {
					patrolingIndex++;
					if (patrolingIndex == patrolingX.length) {
						patrolingIndex = patrolingX.length - 1;
						followPathDirection = false;
					}
				} else {
					patrolingIndex--;
					if (patrolingIndex < 0) {
						patrolingIndex = 0;
						followPathDirection = true;
					}
				}
			}
		}
	}

	protected void calculateExpFlop(Game game) {
		if (owner != null) {
			int teamMemberNum = 0;
			int teamMemberLevel = 0;
			Player players[] = owner.getTeamMembers();
			for (int i = 0; i < players.length; i++) {
				if (game.contains(players[i]) && players[i].isDeath() == false) {
					teamMemberNum++;
					teamMemberLevel += players[i].getLevel();
				}
			}
			for (int i = 0; i < players.length; i++) {
				if (game.contains(players[i]) && players[i].isDeath() == false) {

					int exp = 0;

					boolean masterInTeam = false;

					if (masterInTeam == false) {
						if (this.getMonsterType() == Sprite.MONSTER_TYPE_BOSS) {
							exp = NumericalCalculator.计算杀死BOSS怪物经验值(players[i].getLevel(), this.getLevel(), teamMemberNum, teamMemberLevel);
						} else {
							exp = NumericalCalculator.计算杀死副本精英怪物经验值(players[i].getLevel(), this.getLevel(), teamMemberNum, teamMemberLevel);
						}
					} else {
						if (this.getMonsterType() == Sprite.MONSTER_TYPE_BOSS) {
							exp = NumericalCalculator.计算杀死BOSS怪物经验值(players[i].getLevel(), this.getLevel(), 1, players[i].getLevel());
						} else {
							exp = NumericalCalculator.计算杀死副本精英怪物经验值(players[i].getLevel(), this.getLevel(), 1, players[i].getLevel());
						}
					}
					if(DevilSquareManager.mulMonsterIds != null && DevilSquareManager.mulMonsterIds.containsKey(this.getSpriteCategoryId())) {
						exp = (int) (exp * DevilSquareManager.mulMonsterIds.get(this.getSpriteCategoryId()));
					}
					players[i].addExp(exp, ExperienceManager.ADDEXP_REASON_FIGHTING);
				}
			}
		}
	}

	protected int calculateMoneyFlop(Game game) {
		if (this.getMonsterType() == Sprite.MONSTER_TYPE_BOSS) {
			return NumericalCalculator.计算杀死BOSS怪物掉落的金币(this.getLevel());
		} else {
			return NumericalCalculator.计算杀死副本精英掉落的金币(this.getLevel());
		}
	}

	private void 按照伤害分配boss归属() {
		try {
			if (attackRecordList != null) {
				HashMap<Player, Integer> teamAttack = new HashMap<Player, Integer>();
				for (AttackRecord ar : attackRecordList) {
					if (ar != null) {
						LivingObject lo = ar.living;
						if (lo instanceof Player) {
							Player player = (Player) lo;
							if (teamAttack.get(player) == null) {
								boolean has = false;
								if (teamAttack.keySet() != null) {
									for (Player p : teamAttack.keySet()) {
										if (p.getTeam() == player.getTeam() && p.getTeam() != null) {
											teamAttack.put(p, teamAttack.get(p) + ar.damage);
											has = true;
										}
									}
								}
								if (!has) {
									teamAttack.put(player, ar.damage);
								}
							} else {
								teamAttack.put(player, teamAttack.get(player) + ar.damage);
							}
						} else if (lo instanceof Pet) {
							Player player = ((Pet) lo).getMaster();
							if (player != null) {
								if (teamAttack.get(player) == null) {
									boolean has = false;
									if (teamAttack.keySet() != null) {
										for (Player p : teamAttack.keySet()) {
											if (p.getTeam() == player.getTeam() && p.getTeam() != null) {
												teamAttack.put(p, teamAttack.get(p) + ar.damage);
												has = true;
											}
										}
									}
									if (!has) {
										teamAttack.put(player, ar.damage);
									}
								} else {
									teamAttack.put(player, teamAttack.get(player) + ar.damage);
								}
							}
						}
					}
				}
				Player player = null;
				int attack = 0;
				if (teamAttack.keySet() != null) {
					for (Player p : teamAttack.keySet()) {
						if (teamAttack.get(p) != null && teamAttack.get(p) > attack) {
							attack = teamAttack.get(p);
							player = p;
						}
					}
				}
				if (player != null) {
					owner = player;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void notifyAttack(Player player, String skillName, int skillLevel, int damageType, int damage) {
		AttackRecord ar = new AttackRecord(player, skillName, skillLevel, damageType, (this.getHp() < damage ? this.getHp() : damage), this.getHp());
		this.attackRecordList.add(ar);
	}

	public void notifyAttack(Pet pet, String skillName, int skillLevel, int damageType, int damage) {
		AttackRecord ar = new AttackRecord(pet, skillName, skillLevel, damageType, (this.getHp() < damage ? this.getHp() : damage), this.getHp());
		this.attackRecordList.add(ar);
	}
	
	/**
	 * 在心跳函数中调用此方法
	 * 
	 * 表明此怪被杀死，此方法只能被调用一次
	 * 
	 * 此方法将处理经验值掉落，物品掉落等。
	 */
	public void killed(long heartBeatStartTime, long interval, Game game) {
		if (this.spriteCategoryId == 20113387 && this.callerId > 0) {
			try {
				Player pp = GamePlayerManager.getInstance().getPlayer(callerId);
				Horse2Manager.instance.playerCallBossMaps.remove(callerId);
				if (pp.isOnline()) {
					Buff b = pp.getBuffByName(CountryManager.囚禁buff名称);
					if (b != null) {
						b.setInvalidTime(0);
						ArticleManager.logger.warn("[玩家越狱] [成功] [" + pp.getLogString() + "]");
					}
				}
			} catch (Exception e) {
				ArticleManager.logger.warn("[越狱boss死亡] [异常] [" + this.callerId + "] ", e);
			}
		}
		按照伤害分配boss归属();
		if (baLogger.isDebugEnabled()) {
			baLogger.debug("[{}] [BOSS死亡] [------------------]", new Object[] { getName() });
		}
		try{
			super.killed(heartBeatStartTime, interval, game);
		}catch (Exception e) {
			e.printStackTrace();
			Game.logger.warn("处理怪物死亡异常,",e);
		}
		try {
			FairyRobberyManager.inst.notifyKillBoss(this.getOwner(), this.getId());
		} catch (Exception e) {
			FairyRobberyManager.logger.warn("[boss死亡] [通知] [异常]", e);
		}
		String dcName = "--";
		if (game.getDownCity() != null) {
			dcName = game.getDownCity().getId();
		}
		for (int i = 0; i < items.length; i++) {
			if (items[i].action != null) {
				if (items[i].exeTimeLimit <= heartBeatStartTime - turnOnTime) {
					if (items[i].hpPercent == 0) {
						if (items[i].exeTimes < items[i].maxExeTimes) {
							if (items[i].lastExeTime + items[i].exeTimeGap <= heartBeatStartTime) {
								if (items[i].action.isExeAvailable(this, null)) {
									items[i].exeTimes++;
									items[i].lastExeTime = heartBeatStartTime;
									items[i].action.doAction(game, this, null);
									if (baLogger.isDebugEnabled()) {
										if (baLogger.isDebugEnabled()) baLogger.debug("[{}] [BOSS死亡前] [BOSS执行条目] [id:{}] [{}] [index:{}] [{}] [目标：-]", new Object[] { dcName, getId(), getName(), i, items[i].action.getDescription() });
									}
								}
							}
						}
					}
				}
			} else {
				if (baLogger.isDebugEnabled()) {
					baLogger.debug("[{}] [BOSS死亡1] [index:" + i + "] [" + items[i].actionId + "] [------------------]", new Object[] { getName() });
				}
			}
		}
		{
			// 在家族地图,且是配置的怪
			if (jiazuId > 0 && getGameName().contains(SeptStationManager.jiazuMapName)) {
				Jiazu jiazu = JiazuManager.getInstance().getJiazu(jiazuId);
				try {
					if (jiazu != null) {
						if (SeptStationMapTemplet.getInstance().isJiazuBoss(this)) {
							if (JiazuSubSystem.logger.isInfoEnabled()) {
								JiazuSubSystem.logger.info("[击杀BOSS:" + getName() + "]");
							}
							SeptStation septStation = SeptStationManager.getInstance().getSeptStationBySeptId(jiazuId);
							if (septStation != null) {
								septStation.noticeBossKilled(this);
							} else {
								JiazuSubSystem.logger.error("[家族boss击杀] [异常] [驻地不存在] [家族:" + jiazu.getName() + "]");
							}
							// 家族BOSS处理
						}
					}
				} catch (Exception e) {
					JiazuSubSystem.logger.error("[家族:" + jiazu.getName() + "][家族boss:" + getName() + "被击杀] [异常]", e);
				}
			}
		}
	}

	long lastExcuteActionTime;

	public void heartbeat(long heartBeatStartTime, long interval, Game game) {
		this.heartBeatStartTime = heartBeatStartTime;
		this.interval = interval;
		if (path != null) {
			path.moveFollowPath(heartBeatStartTime);
		}

		String dcName = "--";
		if (game.getDownCity() != null) {
			dcName = game.getDownCity().getId();
		}

		if (state != STATE_DEAD) {
			if (this.getHp() <= 0) {
				this.removeMoveTrace();
				this.state = STATE_DEAD;
				killed(heartBeatStartTime, interval, game);
				if (this.flopSchemeEntity != null && this.flopSchemeEntity.isEmpty() == false) {
					deadEndTime = heartBeatStartTime + this.deadLastingTimeForFloop;
				} else {
					deadEndTime = heartBeatStartTime + this.deadLastingTime;
				}

				this.setStun(false);
				this.setImmunity(false);
				this.setInvulnerable(false);
				this.setPoison(false);
				this.setAura((byte) -1);
				this.setHold(false);
				this.setWeak(false);
				this.setSilence(false);
				this.setShield((byte) -1);

				for (Monster m : flushMonsterList) {
					m.setAlive(false);
				}
				flushMonsterList.clear();

				for (NPC m : flushNPCList) {
					if (m instanceof DoorNpc) {
						DoorNpc dn = (DoorNpc) m;
						if (dn.isClosed()) {
							dn.openDoor(game);
						}
					}
					m.setAlive(false);
				}
				flushNPCList.clear();

				// 循环清楚buff
				if (buffs != null) {
					for (int i = buffs.size() - 1; i >= 0; i--) {
						Buff bu = buffs.get(i);
						if (bu != null) {
							bu.end(this);
							if (bu.isForover() || bu.isSyncWithClient()) {
								this.removedBuffs.add(bu);
							}
							buffs.remove(i);

							if (ActiveSkill.logger.isDebugEnabled()) {
								// ActiveSkill.logger.debug("[死亡去除BUFF] ["+getName()+"] [死亡] ["+bu.getClass().getName().substring(bu.getClass().getName().lastIndexOf(".")+1)+":"+bu.getTemplateName()+"] [time:"+bu.getInvalidTime()+"]");
								if (ActiveSkill.logger.isDebugEnabled()) ActiveSkill.logger.debug("[死亡去除BUFF] [{}] [死亡] [{}:{}] [time:{}]", new Object[] { getName(), bu.getClass().getName().substring(bu.getClass().getName().lastIndexOf(".") + 1), bu.getTemplateName(), bu.getInvalidTime() });
							}
						}
					}
				}
			}
		}

		if (state == STATE_DEAD) {

			if (flopSchemeEntity != null) {
				flopSchemeEntity.heartbeat(heartBeatStartTime, interval, game);

				if (flopEntityNotEmtpy && flopSchemeEntity.isEmpty()) {
					flopEntityNotEmtpy = false;
					if (heartBeatStartTime + deadLastingTime < this.deadEndTime) {
						this.deadEndTime = heartBeatStartTime + deadLastingTime;
					}

				}
			}

			if (heartBeatStartTime > deadEndTime) {
				this.setAlive(false);
			}

			return;
		}

		// 定身或者眩晕的情况下，停止移动
		if (this.isHold() || this.isStun()) {
			if (this.getMoveTrace() != null) {
				stopAndNotifyOthers();
			}
		}

		this.agent.heartbeat(heartBeatStartTime, game, this.getMonsterLocat());

		this.skillAgent.heartbeat(game);

		//
		// buff
		if (heartBeatStartTime - lastTimeForBuffs > 500) {
			lastTimeForBuffs = heartBeatStartTime;

			for (int i = buffs.size() - 1; i >= 0; i--) {
				Buff buff = buffs.get(i);
				if (buff != null) {
					if (buff.getInvalidTime() <= heartBeatStartTime) {
						buff.end(this);

						if (buff.isForover() && buff.isSyncWithClient()) {
							this.removedBuffs.add(buff);
						}
						buffs.remove(i);
					} else {
						buff.heartbeat(this, heartBeatStartTime, interval, game);
						if (buff.getInvalidTime() <= heartBeatStartTime) {
							buff.end(this);

							if (buff.isForover() && buff.isSyncWithClient()) {
								this.removedBuffs.add(buff);
							}
							buffs.remove(i);
						}
					}
				}
			}

			// 光环技能
			auraSkillAgent.heartbeat(heartBeatStartTime, interval, game);
		}
		{
			// 检查是否要消失
			if (heartBeatStartTime - lastCheckDisappearTime > 1000) {
				lastCheckDisappearTime = heartBeatStartTime;
				if (needDisppear(lastCheckDisappearTime)) {
					game.removeSprite(this);
					Horse2Manager.instance.playerCallBossMaps.remove(callerId);
					MemoryMonsterManager.logger.warn("[boss到期移除] [" + this.getName() + "] [出生时间:" + this.bournTime + "] [当前时间:" + lastCheckDisappearTime + "] [时间间隔:" + this.disappearTime + "]");
					return;
				} else if (this.getSpriteCategoryId() == 20113387) {				//写死，有个boss消失时间为一定时间内
					if (heartBeatStartTime - lastCauseDamageTime > 120000) {
						game.removeSprite(this);
						Horse2Manager.instance.playerCallBossMaps.remove(callerId);
						MemoryMonsterManager.logger.warn("[boss固定时间内没有受到伤害自动移除] [" + this.getName() + "] [出生时间:" + this.bournTime + "] [当前时间:" + lastCheckDisappearTime + "] [上次受到伤害时间:" + this.lastCauseDamageTime + "]");
						return;
					}
				}
			}
		}
		// 检查仇恨列表
		DamageRecord drs[] = hatridList.toArray(new DamageRecord[0]);
		for (int i = 0; i < drs.length; i++) {
			if (drs[i].f == null || drs[i].f.isDeath() || game.contains((LivingObject) drs[i].f) == false) {

				hatridList.remove(drs[i]);

				if (baLogger.isDebugEnabled()) {
					// baLogger.debug("["+dcName+"] ["+game.getGameInfo().getName()+"] [BOSS心跳] [清理仇恨列表] [id:"+getId()+"] ["+getName()+"] [对象："+drs[i].f+"] [对象死亡或者不在当前地图中]");
					if (baLogger.isDebugEnabled()) baLogger.debug("[{}] [{}] [BOSS心跳] [清理仇恨列表] [id:{}] [{}] [对象：{}] [对象死亡或者不在当前地图中]", new Object[] { dcName, game.getGameInfo().getName(), getId(), getName(), drs[i].f });
				}

			}
		}

		// 开启情况下，仇恨列表空了，重置
		if (hatridList.size() == 0 && this.turnOnFlag) {
			this.resetBoss(game, heartBeatStartTime);

			if (baLogger.isDebugEnabled()) {
				// baLogger.debug("["+dcName+"] ["+game.getGameInfo().getName()+"] [BOSS心跳] [重置BOSS] [id:"+getId()+"] ["+getName()+"] [开启："+turnOnFlag+"] [开启状态下，仇恨列表为空]");
				if (baLogger.isDebugEnabled()) baLogger.debug("[{}] [{}] [BOSS心跳] [重置BOSS] [id:{}] [{}] [开启：{}] [开启状态下，仇恨列表为空]", new Object[] { dcName, game.getGameInfo().getName(), getId(), getName(), turnOnFlag });
			}
			return;
		}

		// 巡逻
		if (hatridList.size() == 0 && this.turnOnFlag == false) {
			if (this.getMoveTrace() == null && this.traceType != 0) {
				if (this.isHold() == false && this.isStun() == false) {
					patroling(game);
				}
			}
		}

		// 正在执行技能
		if (this.skillAgent.isExecuting()) {

			if (baLogger.isDebugEnabled()) {
				// baLogger.debug("[BOSS心跳] [正在执行技能，不再执行下面的指令，此次心跳结束] ["+getName()+"] [开启："+turnOnFlag+"] [技能："+skillAgent.getExecuting().getSkill().getName()+"]");
				if (baLogger.isDebugEnabled()) baLogger.debug("[BOSS心跳] [正在执行技能，不再执行下面的指令，此次心跳结束] [{}] [开启：{}] [技能：{}]", new Object[] { getName(), turnOnFlag, skillAgent.getExecuting().getSkill().getName() });
			}

			return;
		}

		// 试图搜索视野中的目标，加入仇恨列表
		if (this.hatridList.size() == 0) {
			Fighter f = null;
			if (turnOnFlag) {
				f = findTargetInActivationRange(game);
				if (f != null) {
					updateDamageRecord(f, 0, 1);

					if (baLogger.isDebugEnabled()) {
						// baLogger.debug("["+dcName+"] ["+game.getGameInfo().getName()+"] [BOSS心跳] [从周围发现一个敌人，准备攻击] [id:"+getId()+"] ["+getName()+"] [开启："+turnOnFlag+"] [目标："+f.getName()+"]");
						if (baLogger.isDebugEnabled()) baLogger.debug("[{}] [{}] [BOSS心跳] [从周围发现一个敌人，准备攻击] [id:{}] [{}] [开启：{}] [目标：{}]", new Object[] { dcName, game.getGameInfo().getName(), getId(), getName(), turnOnFlag, f.getName() });
					}
				}
			} else {
				f = findVisiableTargetInActivationRange(game, heartBeatStartTime);
				if (f != null) {
					updateDamageRecord(f, 0, 1);

					if (baLogger.isDebugEnabled()) {
						// baLogger.debug("["+dcName+"] ["+game.getGameInfo().getName()+"] [BOSS心跳] [从周围发现一个可见的敌人，准备攻击] [id:"+getId()+"] ["+getName()+"] [开启："+turnOnFlag+"] [目标："+f.getName()+"]");
						if (baLogger.isDebugEnabled()) baLogger.debug("[{}] [{}] [BOSS心跳] [从周围发现一个可见的敌人，准备攻击] [id:{}] [{}] [开启：{}] [目标：{}]", new Object[] { dcName, game.getGameInfo().getName(), getId(), getName(), turnOnFlag, f.getName() });
					}
				}
			}

		}

		// 视野中无任何人，如果正在移动，返回
		if (this.hatridList.size() == 0) {
			if (this.getMoveTrace() != null) {

				if (baLogger.isDebugEnabled()) {
					// baLogger.debug("[BOSS心跳] [视野中无任何人，仇恨列表为空，且正在移动，此次心跳结束] ["+getName()+"] [开启："+turnOnFlag+"]");
					//baLogger.debug("[BOSS心跳] [视野中无任何人，仇恨列表为空，且正在移动，此次心跳结束] [{}] [开启：{}]", new Object[] { getName(), turnOnFlag });
				}

				return;
			}
		}

		if (this.hatridList.size() > 0 && this.turnOnFlag == false) {
			turnOnFlag = true;
			turnOnTime = heartBeatStartTime;

			if (baLogger.isDebugEnabled()) {
				// baLogger.debug("["+dcName+"] ["+game.getGameInfo().getName()+"] [BOSS心跳] [开启BOSS] [id:"+getId()+"] ["+getName()+"] [开启："+turnOnFlag+"]");
				if (baLogger.isDebugEnabled()) baLogger.debug("[{}] [{}] [BOSS心跳] [开启BOSS] [id:{}] [{}] [开启：{}]", new Object[] { dcName, game.getGameInfo().getName(), getId(), getName(), turnOnFlag });
			}
		}

		if (turnOnFlag == false) {

			if (baLogger.isDebugEnabled()) {
				// baLogger.debug("[BOSS心跳] [BOSS未开启，不进行任何行为，此次心跳结束] ["+getName()+"] [开启："+turnOnFlag+"]");
				// baLogger.debug("[BOSS心跳] [BOSS未开启，不进行任何行为，此次心跳结束] [{}] [开启：{}]", new Object[]{getName(),turnOnFlag});
			}

			return;
		}

		Fighter target = getMaxHatredFighter();
		if (this.getMonsterLocat() == 2) {
			List<Player> tempPlayerList = game.getPlayers();
			if (tempPlayerList.size() == 1) {
				target = tempPlayerList.get(0);
			} else if (tempPlayerList.size() > 1) {
				target = tempPlayerList.get(random.nextInt(tempPlayerList.size()));
			}
			/*long tempLen = 0;
			for (int i=0; i<tempPlayerList.size(); i++) {
				Player p = tempPlayerList.get(i);
				int targetx = p.getX();
				int targety = p.getY();
				long aa = (this.getX() - targetx) * (this.getX() - targetx) + (this.getY() - targety) * (this.getY() - targety);
				if (tempLen <= 0 || aa < tempLen) {
					tempLen = aa;
					target = p;
				}
			}*/
			
		}
		if (target == null) {

			if (baLogger.isDebugEnabled()) {
				// baLogger.debug("["+dcName+"] ["+game.getGameInfo().getName()+"] [BOSS心跳] [没有仇恨第一的目标，不进行任何行为，此次心跳结束] [id:"+getId()+"] ["+getName()+"] [开启："+turnOnFlag+"]");
				if (baLogger.isDebugEnabled()) baLogger.debug("[{}] [{}] [BOSS心跳] [没有仇恨第一的目标，不进行任何行为，此次心跳结束] [id:{}] [{}] [开启：{}]", new Object[] { dcName, game.getGameInfo().getName(), getId(), getName(), turnOnFlag });
			}

			return;
		}

		// 检查owner
		if (owner != null) {
			if (!game.contains(owner)) {
				if (target instanceof Player) {
					owner = (Player) target;
				} else {
					for (int i = 0; i < hatridList.size(); i++) {
						DamageRecord dr = hatridList.get(i);
						if ((dr.f instanceof Player) && game.contains((LivingObject) dr.f)) {
							owner = (Player) dr.f;
						}
					}
				}
			}
		}
		if(owner instanceof Player){
			Player pp = (Player) owner;
			if(this.getSpriteCategoryId() == TransitRobberyManager.HUANXIANG_BOSSID && ((hp * 100)/ getMaxHP()) <= RobberyConstant.SKILL_ACT_HP && TransitRobberyEntityManager.getInstance().isPlayerInPhantomRobbery(owner.getId(), 2)){
				if(phantomSkActTime <= 0){
					TransitRobberyManager.logger.info("触发第二劫技能");
					phantomSkActTime = System.currentTimeMillis();
					this.setParticleName(RobberyConstant.BUFF定身); 
					
				} else {
					if(System.currentTimeMillis() >= (phantomSkActTime + RobberyConstant.LAST_TIME * 1000) && this.hp > 0){
						hp = (int) (getMaxHP() * RobberyConstant.RECOVER_HP);
						phantomSkActTime = 0;
						this.setParticleName("");
						TransitRobberyManager.logger.info("[清除boss身上粒子][" + this.getParticleName() + "]");
					}
				}
			} else if(this.getSpriteCategoryId() == TransitRobberyManager.SHENHUN_BOSSID && ((hp *100L) / getMaxHP()) <= RobberyConstant.SUCCEED_HP_PERCENT_SHENHU &&  TransitRobberyEntityManager.getInstance().isPlayerInPhantomRobbery(owner.getId(), 8)) {
				TransitRobberyManager.logger.info("[触发神魂劫剧情 ]["  + pp.getId() + "][boss当前血量:" + hp + "] [最大血量:" + getMaxHP() + "]");
				TransitRobberyManager.getInstance().shenhun_juqing(pp);
			}  
//			else if(this.getSpriteCategoryId() == TransitRobberyManager.WUXIANG_BOSSID && (hp / getMaxHP()) <= RobberyConstant.SUCCEED_HP_PERCENT_WUXIANG &&  TransitRobberyEntityManager.getInstance().isPlayerInPhantomRobbery(owner.getId(), 9)) {
//				TransitRobberyManager.getInstance().wuxiang_juqing(pp);
//				TransitRobberyManager.logger.info("触发无相劫剧情 "  + pp.getId());
//			}
		}
		

		if (heartBeatStartTime - lastExcuteActionTime >= 1000) {
			lastExcuteActionTime = heartBeatStartTime;
			for (int i = 0; i < items.length; i++) {
				if (items[i].action != null) {
					if (items[i].exeTimeLimit <= heartBeatStartTime - turnOnTime) {
						int d = (int) Math.sqrt((x - target.getX()) * (x - target.getX()) + (y - target.getY()) * (y - target.getY()));
						if (d >= items[i].minDistance && d <= items[i].maxDistance) {
							if ((items[i].hpPercent > 0 && 100 * 1l * getHp() / this.getMaxHP() <= items[i].hpPercent) || (items[i].hpPercent == 0 && getHp() == 0)) {
								if (items[i].exeTimes < items[i].maxExeTimes) {
									if (items[i].lastExeTime + items[i].exeTimeGap <= heartBeatStartTime) {
										if (this.getMoveTrace() == null || items[i].isIgnoreMoving()) {
											if (items[i].action.isExeAvailable(this, target)) {
												items[i].exeTimes++;
												items[i].lastExeTime = heartBeatStartTime;
												items[i].action.doAction(game, this, target);
												if (baLogger.isDebugEnabled()) {
													if (baLogger.isDebugEnabled()) baLogger.debug("[{}] [{}] [BOSS心跳] [BOSS执行条目] [id:{}] [{}] [index:{}] [{}] [目标：{}]", new Object[] { dcName, game.getGameInfo().getName(), getId(), getName(), i, items[i].action.getDescription(), target.getName() });
												}
												return;
											} else {
												if (baLogger.isDebugEnabled()) {
													if (baLogger.isDebugEnabled()) baLogger.debug("[BOSS心跳] [BOSS执行条目失败] [不合法] [{}] [开启：{}] [目标：{}]", new Object[] { getName(), turnOnFlag, target.getName() });
												}
											}
										} else {
											if (baLogger.isDebugEnabled()) {
												if (baLogger.isDebugEnabled()) baLogger.debug("[BOSS心跳] [BOSS执行条目失败] [正在寻路，且不忽视移动] [{}] [开启：{}] [目标：{}]", new Object[] { getName(), turnOnFlag, target.getName() });
											}
										}
									} else {
										if (baLogger.isDebugEnabled()) {
											if (baLogger.isDebugEnabled()) baLogger.debug("[BOSS心跳] [BOSS执行条目失败] [执行的动作正在冷却] [{}] [开启：{}] [目标：{}]", new Object[] { getName(), turnOnFlag, target.getName() });
										}
									}
								} else {
									if (baLogger.isDebugEnabled()) {
										if (baLogger.isDebugEnabled()) baLogger.debug("[BOSS心跳] [BOSS执行条目失败] [执行的动作次数结束] [{}] [开启：{}] [目标：{}]", new Object[] { getName(), turnOnFlag, target.getName() });
									}
								}
							} else {
								if (baLogger.isDebugEnabled()) {
									if (baLogger.isDebugEnabled()) baLogger.debug("[BOSS心跳] [BOSS执行条目失败] [执行的动作血量不满足] [{}] [开启：{}] [目标：{}]", new Object[] { getName(), turnOnFlag, target.getName() });
								}
							}
						} else {
							if (baLogger.isDebugEnabled()) {
								if (baLogger.isDebugEnabled()) baLogger.debug("[BOSS心跳] [BOSS执行条目失败] [执行的动作距离不满足] [{}] [开启：{}] [目标：{}] [距离：{}] [坐标:{}]", new Object[] { getName(), turnOnFlag, target.getName(), d, getX() + "," + getY()});
							}
						}
					} else {
						if (baLogger.isDebugEnabled()) {
							if (baLogger.isDebugEnabled()) baLogger.debug("[BOSS心跳] [BOSS执行条目失败] [执行的动作开启时间不满足] [{}] [开启：{}] [目标：{}]", new Object[] { getName(), turnOnFlag, target.getName() });
						}
					}
				} else {
					if (baLogger.isDebugEnabled()) {
						if (baLogger.isDebugEnabled()) baLogger.debug("[BOSS心跳] [BOSS执行条目失败] [执行的动作为空] [{}] [开启：{}] [目标：{}]", new Object[] { getName(), turnOnFlag, target.getName() });
					}
				}
			}
		}

		if (baLogger.isDebugEnabled()) {
			if (baLogger.isDebugEnabled()) baLogger.debug("[BOSS心跳] [BOSS执行条目，所有条目都不满足执行条件] [{}] [开启：{}] [目标：{}] [上次执行时间:{}] [本次时间:{} [执行条目]]", new Object[] { getName(), turnOnFlag, target.getName() });
		}
	}
	
	public void causeDamage(Fighter caster, int damage, int hateParam, int damageType) {
		super.causeDamage(caster, damage, hateParam, damageType);
		this.lastCauseDamageTime = System.currentTimeMillis();
	}

	private long lastFindVisiableTargetTime = 0;
	private transient long phantomSkActTime = 0;
	
	/**
	 * boss没有开启的情况下 寻找可见目标
	 * @param game
	 * @return
	 */
	private Fighter findVisiableTargetInActivationRange(Game game, long heartbeatTime) {
		if (this.activationType == 0) return null;
		if (heartbeatTime - lastFindVisiableTargetTime < 1000) return null;

		Fighter fs[] = game.getVisbleFighter(this, false);
		Fighter f = null;
		float minD = 0;
		for (int i = 0; i < fs.length; i++) {
			if (fs[i] instanceof Player || fs[i] instanceof Pet) {
				if (!fs[i].isObjectOpacity() && fs[i].getX() >= this.x - this.activationWidth / 2 && fs[i].getX() <= this.x + this.activationWidth / 2 && fs[i].getY() >= this.y - this.activationHeight / 2 && fs[i].getY() <= this.y + this.activationHeight / 2) {

					if (game.gi.navigator.isVisiable(this.x, this.y, fs[i].getX(), fs[i].getY())) {
						float d = (fs[i].getX() - this.x) * (fs[i].getX() - this.x) + (fs[i].getY() - this.y) * (fs[i].getY() - this.y);
						if (f == null) {
							f = fs[i];
							minD = d;
						} else if (d < minD) {
							f = fs[i];
							minD = d;
						}
					}
				}
			}
		}
		if (f != null) return f;
		if (this.activationType == 1) return f;

		for (int i = 0; i < fs.length; i++) {
			if (fs[i] instanceof Monster) {
				Monster s = (Monster) fs[i];
				if (s.getX() >= this.x - this.activationWidth / 2 && s.getX() <= this.x + this.activationWidth / 2 && s.getY() >= this.y - this.activationHeight / 2 && s.getY() <= this.y + this.activationHeight / 2) {
					Fighter target = s.getMaxHatredFighter();
					if (target != null) {
						f = target;
						break;
					}
				}
			}
		}
		return f;

	}
	/**
	 * 设置boss为开启状态
	 */
	public void turnOnBoss() {
		turnOnFlag = true;
		turnOnTime = System.currentTimeMillis();
	}
	public void turnOffBoss() {
		turnOnFlag = false;
	}

	/**
	 * 重置Boss到未开启状态，
	 */
	public void resetBoss(Game game, long heartBeatStartTime) {
		if (this.getMonsterLocat() == 2) {			//空岛大冒险boss不需要重置
			return ;
		}
		
		if(game != null && game.gi != null && game.gi.name != null){
			if(game.gi.name.equals("jiefengBOSStiaozhanditu")){
				return;
			}
		}
		for (Monster m : flushMonsterList) {
			m.setAlive(false);
		}
		flushMonsterList.clear();

		for (NPC m : flushNPCList) {
			if (m instanceof DoorNpc) {
				DoorNpc dn = (DoorNpc) m;
				if (dn.isClosed()) {
					dn.openDoor(game);
				}
			}
			m.setAlive(false);
		}
		flushNPCList.clear();

		SET_POSITION_REQ req = new SET_POSITION_REQ(GameMessageFactory.nextSequnceNum(), (byte) Game.REASON_CLIENT_STOP, getClassType(), id, (short) bornPoint.x, (short) bornPoint.y);

		LivingObject los[] = game.getLivingObjects();
		for (int i = 0; i < los.length; i++) {
			if (los[i] instanceof Player) {
				Player p = (Player) los[i];
				p.addMessageToRightBag(req);
			}
		}

		this.turnOnFlag = false;
		this.owner = null;
		this.agent.cancel();
		this.skillAgent.breakExecutingByDead();
		this.skillAgent.clearCooldown();

		this.setX(bornPoint.x);
		this.setY(bornPoint.y);
		removeMoveTrace();
		this.setHp(this.getMaxHP());
		for (int i = buffs.size() - 1; i >= 0; i--) {
			Buff buff = buffs.get(i);
			buff.setInvalidTime(heartBeatStartTime);
		}

		for (int i = 0; i < items.length; i++) {
			items[i].exeTimes = 0;
			items[i].lastExeTime = 0;
		}
	}

	public boolean isTurnOnFlag() {
		return turnOnFlag;
	}

	public boolean isImmuneStunFlag() {
		return immuneStunFlag;
	}

	public void setImmuneStunFlag(boolean immuneStunFlag) {
		this.immuneStunFlag = immuneStunFlag;
	}

	public boolean isImmuneHoldFlag() {
		return immuneHoldFlag;
	}

	public void setImmuneHoldFlag(boolean immuneHoldFlag) {
		this.immuneHoldFlag = immuneHoldFlag;
	}

	public boolean isImmuneSilenceFlag() {
		return immuneSilenceFlag;
	}

	public void setImmuneSilenceFlag(boolean immuneSilenceFlag) {
		this.immuneSilenceFlag = immuneSilenceFlag;
	}

	public boolean isImmuneSpeedDownFlag() {
		return immuneSpeedDownFlag;
	}

	public void setImmuneSpeedDownFlag(boolean immuneSpeedDownFlag) {
		this.immuneSpeedDownFlag = immuneSpeedDownFlag;
	}

	public boolean isImmuneSneerFlag() {
		return immuneSneerFlag;
	}

	public void setImmuneSneerFlag(boolean immuneSneerFlag) {
		this.immuneSneerFlag = immuneSneerFlag;
	}

	public long getDisappearTime() {
		return disappearTime;
	}

	public void setDisappearTime(long disappearTime) {
		this.disappearTime = disappearTime;
	}

	public int getDropLevelLimit() {
		return dropLevelLimit;
	}

	public void setDropLevelLimit(int dropLevelLimit) {
		this.dropLevelLimit = dropLevelLimit;
	}

	/**
	 * clone出一个对象
	 */
	public Object clone() {
		BossMonster p = new BossMonster();

		p.cloneAllInitNumericalProperty(this);
		p.deadLastingTimeForFloop = this.deadLastingTimeForFloop;
		p.spriteCategoryId = spriteCategoryId;

		p.disappearTime = this.disappearTime;
		p.dropLevelLimit = this.dropLevelLimit;
		
		p.activationHeight = this.activationHeight;
		p.activationType = this.activationType;
		p.activationWidth = this.activationWidth;
		p.setActiveSkillIds(this.getActiveSkillIds());
		p.activeSkillLevels = this.activeSkillLevels;
		p.backBornPointHp = this.backBornPointHp;
		p.backBornPointMoveSpeedPercent = this.backBornPointMoveSpeedPercent;
		p.bornPoint = this.bornPoint;
		p.commonAttackRange = this.commonAttackRange;
		p.commonAttackSpeed = this.commonAttackSpeed;
		p.dialogContent = this.dialogContent;

		p.height = this.height;
		p.career = this.career;
		p.monsterRace = this.monsterRace;
		p.color = this.color;
		p.monsterMark = this.monsterMark;
		p.hpMark = this.hpMark;
		p.apMark = this.apMark;
		p.monsterType = this.monsterType;

		p.patrolingHeight = this.patrolingHeight;
		p.patrolingTimeInterval = this.patrolingTimeInterval;
		p.patrolingWidth = this.patrolingWidth;
		p.pursueHeight = this.pursueHeight;
		p.pursueWidth = this.pursueWidth;
		p.taskMark = this.taskMark;
		p.traceType = this.traceType;
		p.viewHeight = this.viewHeight;
		p.viewWidth = this.viewWidth;

		p.dialogContent = this.dialogContent + "";
		p.fsList = this.fsList;
		p.fsProbabilitis = this.fsProbabilitis;

		p.immuneHoldFlag = immuneHoldFlag;
		p.immuneSilenceFlag = immuneSilenceFlag;
		p.immuneSneerFlag = immuneSneerFlag;
		p.immuneSpeedDownFlag = immuneSpeedDownFlag;
		p.immuneStunFlag = immuneStunFlag;

		p.additionData = additionData;
		p.patrolingX = patrolingX;
		p.patrolingY = patrolingY;

		p.avataRace = avataRace;
		p.avataSex = avataSex;
		if (items != null) {
			p.items = new BossExecuteItem[this.items.length];
			for (int i = 0; i < items.length; i++) {
				if (items[i] != null) {
					try {
						p.items[i] = (BossExecuteItem) items[i].clone();
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		p.setMaxHPA(this.maxHPA);
		p.setPhyAttackA(this.phyAttackA);
		p.setMagicAttackA(this.magicAttackA);
		p.setPhyDefenceA(this.phyDefenceA);
		p.setMagicDefenceA(this.magicDefenceA);
		p.setMaxMPA(this.maxMPA);
		p.setBreakDefenceA(this.breakDefenceA);
		p.setHitA(this.hitA);
		p.setDodgeA(this.dodgeA);
		p.setAccurateA(this.accurateA);
		p.setCriticalHitA(this.criticalHitA);
		p.setCriticalDefenceA(this.criticalDefenceA);
		p.setFireAttackA(this.fireAttackA);
		p.setBlizzardAttackA(this.blizzardAttackA);
		p.setWindAttackA(this.windAttackA);
		p.setThunderAttackA(this.thunderAttackA);
		p.setFireDefenceA(this.fireDefenceA);
		p.setBlizzardDefenceA(this.blizzardDefenceA);
		p.setWindDefenceA(this.windDefenceA);
		p.setThunderDefenceA(this.thunderDefenceA);
		p.setFireIgnoreDefenceA(this.fireIgnoreDefenceA);
		p.setBlizzardIgnoreDefenceA(this.blizzardIgnoreDefenceA);
		p.setWindIgnoreDefenceA(this.windIgnoreDefenceA);
		p.setThunderIgnoreDefenceA(this.thunderIgnoreDefenceA);
		return p;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////
	// BOSS 对外提供的行为
	//
	// ///////////////////////////////////////////////////////////////////////////////////

	/**
	 * 清除当前目标的仇恨值到1
	 */
	public void clearTargetHatred(Fighter target) {
		DamageRecord drs[] = hatridList.toArray(new DamageRecord[0]);
		for (int i = 0; i < drs.length; i++) {
			if (drs[i].f == target) {
				drs[i].hatred = 1;
			}
		}
	}

	/**
	 * 保留仇恨最低，清除其他目标的仇恨值到1
	 */
	public void clearTargetHatredExceptMin() {
		int minHatred = Integer.MAX_VALUE;
		DamageRecord minDr = null;
		DamageRecord drs[] = hatridList.toArray(new DamageRecord[0]);
		for (int i = 0; i < drs.length; i++) {
			if (drs[i].hatred < minHatred) {
				minDr = drs[i];
				minHatred = drs[i].hatred;
			}
		}
		for (int i = 0; i < drs.length; i++) {
			if (drs[i] != minDr) {
				drs[i].hatred = 1;
			}
		}
	}

	public boolean canDrop(Player player) {
		if (dropLevelLimit == -1) {
			return true;
		}
		return this.dropLevelLimit > player.getLevel();
	}
}
