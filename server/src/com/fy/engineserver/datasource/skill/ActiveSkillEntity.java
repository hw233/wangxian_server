package com.fy.engineserver.datasource.skill;

//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.activeskills.CommonAttackSkill;
import com.fy.engineserver.datasource.skill.master.SkBean;
import com.fy.engineserver.datasource.skill.master.SkEnConf;
import com.fy.engineserver.datasource.skill.master.SkEnhanceManager;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.monster.BossMonster;

/**
 * 技能体
 * 
 * 技能体对应 一个玩家或者怪物 以及 一个主动技能
 * 
 * 技能体是有状态的，分为 未开始，僵直，攻击（施法），冷却，结束
 * 
 * 
 * 
 * 
 */
public class ActiveSkillEntity {

	public static Logger logger = LoggerFactory.getLogger(Skill.class);

	public static final String STATESTR[] = new String[] { Translate.text_1052, Translate.text_3100, Translate.text_3865, Translate.text_3866, Translate.text_3867 };
	public static final int STATUS_START = 0;
	public static final int STATUS_INTONATE = 1;
	public static final int STATUS_ATTACK = 2;
	public static final int STATUS_COOLDOWN = 3;
	public static final int STATUS_END = 4;

	ActiveSkill skill;
	Fighter owner;
	Fighter target;
	int status;
	int x, y;
	byte direction;
	private int level;
	public byte[] targetTypes;
	public long[] targetIds;

	public ActiveSkillEntity() {

	}

	@Override
	protected void finalize() throws Throwable {

	}

	public byte[] getTargetTypes() {
		return targetTypes;
	}

	public void setTargetTypes(byte[] targetTypes) {
		this.targetTypes = targetTypes;
	}

	public long[] getTargetIds() {
		return targetIds;
	}

	public void setTargetIds(long[] targetIds) {
		this.targetIds = targetIds;
	}

	private long startTime;
	private long lastHeartBeatTime;

	protected long intonateTime;
	private long attackTime;
	private long cooldownTime;

	private boolean calculateMP = false;

	public ActiveSkillEntity(Fighter owner, ActiveSkill skill, Fighter target, int level, int x, int y, byte[] targetTypes, long[] targetIds, byte direction) {
		this();
		this.owner = owner;
		this.skill = skill;
		this.target = target;
		this.level = level;
		status = STATUS_START;
		this.x = x;
		this.y = y;
		this.direction = direction;
		intonateTime = skill.getDuration1();
		attackTime = skill.getDuration2();
		cooldownTime = skill.getDuration3();
		cooldownTime -= SkEnhanceManager.getInst().calcCDSub(owner, skill);
		// 为防止客户端冷却完毕，服务器端还没冷却完，对于长时间冷却的技能，服务器端冷却时间变短
		if (cooldownTime >= 120000) {
			cooldownTime = cooldownTime - 20000;
		}
		this.targetIds = targetIds;
		this.targetTypes = targetTypes;

		if ((skill instanceof CommonAttackSkill) && (owner instanceof Player)) {
			if (owner instanceof Player) {
				Player p = (Player) owner;
				int caSpeed = p.getCommonAttackSpeed();

				intonateTime = 0;
				if (caSpeed > skill.getDuration2()) {
					attackTime = skill.getDuration2();
					cooldownTime = caSpeed - attackTime;
				} else {
					attackTime = caSpeed;
					cooldownTime = 0;
				}
				if (skill.isBianshenSkill() && p.getCareer() == 5) {			//兽形态使用变身技能无cd
					cooldownTime = 0;
				}
			} else if (owner instanceof Sprite) {
				Sprite p = (Sprite) owner;
				int caSpeed = p.getCommonAttackSpeed();

				intonateTime = 0;
				if (caSpeed > skill.getDuration2()) {
					attackTime = skill.getDuration2();
					cooldownTime = caSpeed - attackTime;
				} else {
					attackTime = caSpeed;
					cooldownTime = 0;
				}
			}
		}
	}

	public String getStateStr() {
		return STATESTR[status];
	}

	public int getStatus() {
		return status;
	}

	public void start() {
		if (status == STATUS_START) {
			status = STATUS_INTONATE;
			startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			lastHeartBeatTime = startTime;

			owner.setState(LivingObject.STATE_CAST_PREPAIR);
			if (owner instanceof Player) {
				// ((Player) owner).setStyle((byte) skill.getStyle1());
			} else if (owner instanceof Sprite) {
				// ((Sprite) owner).setStyle((byte) skill.getStyle1());
			}
			if (owner instanceof LivingObject) {
				boolean b = false;
				if (owner instanceof BossMonster) {
					if (((BossMonster)owner).getMonsterLocat() == 2) {
						b = true;
					}
				}
				if (!b) {
					((LivingObject) owner).face(x, y);
				}
			}

			skill.start(this);
		}
	}

	public void end() {
		skill.end(this);
	}

	public void heartbeat(Game game) {
		long currentTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (status == STATUS_INTONATE) {
			
			if ((owner.isStun() && !skill.isIgnoreStun() && !skill.isLostStun(owner)) || owner.isSilence() ||(owner.isIceState() && !skill.isIgnoreStun() && !skill.isLostStun(owner))) {
				status = STATUS_END;
				owner.setState(LivingObject.STATE_STAND);
			} else if (skill.check(owner, target, level) != Skill.OK) {
				status = STATUS_END;
				owner.setState(LivingObject.STATE_STAND);
				if (logger.isDebugEnabled()) {
					logger.debug("[技能施法过程中终止] [{}] [{}] [{}] [Lv:{}] [{}] [{}]", new Object[] { (Skill.getSkillFailReason(skill.check(owner, target, level))), (getClass().getName().substring(getClass().getName().lastIndexOf(".") + 1)), skill.getName(), (level), owner.getName(), (target != null ? target.getName() : "无目标") });
				}
			} else if (currentTime - startTime >= this.intonateTime) {
				status = STATUS_ATTACK;
				lastHeartBeatTime = startTime + intonateTime;

				owner.setState(LivingObject.STATE_CAST_FIRE);
				if (owner instanceof Player) {
					// ((Player) owner).setStyle((byte) skill.getStyle2());
				} else if (owner instanceof Sprite) {
					// ((Sprite) owner).setStyle((byte) skill.getStyle2());
				}

				if (owner instanceof Player) {
					Player p = (Player) owner;
					if (skill.nuqiFlag) {
						int aa = p.getXp();
						p.setXp(0);
						if (logger.isDebugEnabled()) {
							logger.debug("[技能施法结束] [耗怒气：{}] [{}] [{}] [Lv:{}] [{}] [{}]", new Object[] { aa, (getClass().getName().substring(getClass().getName().lastIndexOf(".") + 1)), skill.getName(), (level), owner.getName(), (target != null ? target.getName() : "无目标") });
						}
					} else {
						int aa = skill.calculateMp(p, level);
						p.setMp(p.getMp() - aa);
						if (logger.isDebugEnabled()) {
							logger.debug("[技能施法结束] [耗蓝：{}] [{}] [{}] [Lv:{}] [{}] [{}]", new Object[] { aa, (getClass().getName().substring(getClass().getName().lastIndexOf(".") + 1)), skill.getName(), (level), owner.getName(), (target != null ? target.getName() : "无目标") });
						}
					}
					if (skill.isDouFlag()) {
						int dou = skill.calculateDou(p, level);
						p.changeShoukuiDouNum(dou);
						if (logger.isDebugEnabled()) {
							logger.debug("[技能施法结束] [dou:" + dou + "] [剩余dou:" + p.getShoukuiDouNum() + "] [" + p.getLogString() + "]");
						}
					}
					
					Logger log = Skill.logger;
					int ets[] = skill.getEffectiveTimes();
					for (int i = 0; i < ets.length; i++) {
						skill.run(this, target, game, level, i, x, y, direction);
						log.debug("ActiveSkillEntity.heartbeat: {}", skill.getName());
					}
					{
						int pageIndex = skill.pageIndex;
						// 修罗 702 双舞 人阶10重:获得双舞连击+1、且增加所有技能附带1%几率使目标命中减少80%持续5秒
						if (pageIndex == 1 && p.getLevel() >= TransitRobberyManager.openLevel && p.getCareer() == 1) {
							SkBean bean = SkEnhanceManager.getInst().findSkBean(p);
							if (bean != null && bean.useLevels != null && bean.useLevels[pageIndex] >= 10) {
								int lv = bean.useLevels[pageIndex] / 10;
								for (int i = 0; i < lv; i++) {
									skill.run(this, target, game, level, i, x, y, direction);
								}
								if (logger.isDebugEnabled()) {
									logger.debug("ActiveSkillEntity.heartbeat:修罗702双舞 连击 {}", lv);
								}
							}
						}
					}
				}
				skill.duration2Start(this, game);
			}
		}

		if (status == STATUS_ATTACK) {
			if (!(owner instanceof Player)) {
				int ets[] = skill.getEffectiveTimes();
				for (int i = 0; i < ets.length; i++) {
					long attackTime = startTime + intonateTime + ets[i];
					if (attackTime >= lastHeartBeatTime && attackTime < currentTime) {
						skill.run(this, target, game, level, i, x, y, direction);
					}
				}
			}
			if (currentTime - startTime >= intonateTime + this.attackTime) {
				status = STATUS_COOLDOWN;

				owner.setState(LivingObject.STATE_STAND);
				if (owner instanceof Player) {
					// ((Player) owner).setStyle((byte)0);
				} else if (owner instanceof Sprite) {
					// ((Sprite) owner).setStyle((byte)0);
				}
				skill.duration3Start(this, game);
			}
		}
		// }

		if (status == STATUS_COOLDOWN) {
			if (currentTime - startTime >= intonateTime + attackTime + this.cooldownTime) {
				status = STATUS_END;
			}
		}

		lastHeartBeatTime = currentTime;
	}

	public void hitTarget(Fighter target, int effectIndex) {
		try{
			skill.hit(owner, target, level, effectIndex);
		}catch(Throwable e){
			e.printStackTrace();
			Skill.logger.error("[useskill error] ["+(this.getSkill()!=null?this.getSkill().getName():"--")+"]", e);
		}
	}

	public Fighter getOwner() {
		return owner;
	}

	public ActiveSkill getSkill() {
		return skill;
	}

	public void setSkill(ActiveSkill skill) {
		this.skill = skill;
	}

	public Fighter getTarget() {
		return target;
	}

	public void setTarget(Fighter target) {
		this.target = target;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getLastHeartBeatTime() {
		return lastHeartBeatTime;
	}

	public void setLastHeartBeatTime(long lastHeartBeatTime) {
		this.lastHeartBeatTime = lastHeartBeatTime;
	}

	public void setOwner(Fighter owner) {
		this.owner = owner;
	}

	public long getIntonateTime() {
		return intonateTime;
	}

	public long getAttackTime() {
		return attackTime;
	}

	public long getCooldownTime() {
		return cooldownTime;
	}

	public void setCooldownTime(long cooldownTime) {
		this.cooldownTime = cooldownTime;
	}
	
	
}
