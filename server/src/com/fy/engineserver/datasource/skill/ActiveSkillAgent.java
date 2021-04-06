package com.fy.engineserver.datasource.skill;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.skill.activeskills.CommonAttackSkill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.pet.Pet;
import com.xuanzhi.stat.model.PlayerActionFlow;

/**
 * 技能代理
 * 
 * 负责帮助玩家或者怪物实现技能
 * 
 * 此代理保留三个技能或者技能集合
 * 
 * 第一个：正在执行的技能体， 第二个：正在等待执行的技能体， 第三个：正在冷却的技能体集合
 * 
 * 
 * 
 */
public class ActiveSkillAgent {

	// static Logger logger = Logger.getLogger(ActiveSkill.class);
	public static Logger logger = LoggerFactory.getLogger(ActiveSkill.class);

	private Fighter owner;

	private ActiveSkillEntity executing;
	private ActiveSkillEntity waitting;
	private Map<Integer, ActiveSkillEntity> cooldown = new HashMap<Integer, ActiveSkillEntity>();

	public ActiveSkillAgent() {

	}

	@Override
	protected void finalize() throws Throwable {

	}

	public ActiveSkillAgent(Fighter owner) {
		this();
		this.owner = owner;
	}

	/**
	 * 判断是否在执行
	 * @return
	 */
	public boolean isExecuting() {
		return executing != null;
	}

	/**
	 * 判断是否可以被打断，如果当前执行的技能已经处于攻击状态，不能被打断
	 * @return
	 */
	public boolean canBreakExecuting() {
		return executing == null || executing.status == ActiveSkillEntity.STATUS_INTONATE;
	}

	public void clearCooldown() {
		cooldown.clear();
	}

	/**
	 * 移动打断
	 * 同时判断当前是否存在要执行的瞬发技能，如果有，执行此技能
	 */
	public void breakExecutingByPlayerMove(Game game) {

		if (executing != null && executing.skill != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("[技能被强制终止] [角色移动] [{}] [{}] [Lv:{}] [{}] [{}]", new Object[] { (executing.skill.getClass().getName().substring(getClass().getName().lastIndexOf(".") + 1)), executing.skill.getName(), (executing.getLevel()), owner.getName(), (executing.getTarget() != null ? executing.getTarget().getName() : "无目标") });
			}
		}

		// 执行人物的瞬发技能
		if ((owner instanceof Player || owner instanceof Pet) && executing != null && executing.status == ActiveSkillEntity.STATUS_START && executing.intonateTime == 0) {
			executing.start();
			executing.heartbeat(game);
		}

		if (executing != null && executing.status == ActiveSkillEntity.STATUS_INTONATE) {
			// 取消当前技能，通知服务器
			executing.end();
		} else if (executing != null && executing.status == ActiveSkillEntity.STATUS_ATTACK) {
			executing.status = ActiveSkillEntity.STATUS_COOLDOWN;
			cooldown.put(executing.skill.getId(), executing);
			executing.end();

		} else if (executing != null) {
			executing.end();
		}

		executing = null;
		waitting = null;

	}

	/**
	 * 打断
	 */
	public void breakExecutingByDead() {

		if (executing != null && executing.skill != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("[技能被强制终止] [角色死亡] [{}] [{}] [Lv:{}] [{}] [{}]", new Object[] { (executing.skill.getClass().getName().substring(getClass().getName().lastIndexOf(".") + 1)), executing.skill.getName(), (executing.getLevel()), owner.getName(), (executing.getTarget() != null ? executing.getTarget().getName() : "无目标") });
			}
		}

		if (executing != null && executing.status == ActiveSkillEntity.STATUS_INTONATE) {
			// 取消当前技能，通知服务器
			executing.end();
		} else if (executing != null && executing.status == ActiveSkillEntity.STATUS_ATTACK) {
			executing.status = ActiveSkillEntity.STATUS_COOLDOWN;
			cooldown.put(executing.skill.getId(), executing);
			executing.end();

		} else if (executing != null) {
			executing.end();
		}

		executing = null;
		waitting = null;

	}

	/**
	 * 使用某个主动技能。需要检查是否满足技能使用条件！
	 * 
	 * @param skill
	 * @param level
	 * @param target
	 * @param x
	 * @param y
	 */
	public boolean usingSkill(ActiveSkill skill, int level, Fighter target, int x, int y, byte[] targetTypes, long[] targetIds, byte direction) {
		int r = skill.check(owner, target, level);
		if (r == 0) {
			
			if (owner instanceof Player) {
				Player p = (Player) owner;
				
				if ((p.isSilence() && !(skill instanceof CommonAttackSkill)) || (p.isStun() && !skill.isIgnoreStun() && !skill.isLostStun(owner)) || (p.isIceState() && !skill.isIgnoreStun() && !skill.isLostStun(owner))) {
					return false;
				}
			} else if (owner instanceof Sprite) {
				Sprite p = (Sprite) owner;
				if ((p.isSilence() && !(skill instanceof CommonAttackSkill)) || (p.isStun() && !skill.isIgnoreStun())) {
					return false;
				}
			}

			ActiveSkillEntity e = (ActiveSkillEntity) cooldown.get(skill.getId());
			if (e != null && e.status == ActiveSkillEntity.STATUS_COOLDOWN) {
				waitting = new ActiveSkillEntity(owner, skill, target, level, x, y, targetTypes, targetIds, direction);
			} else {
				if (executing != null) {
					if (executing.status == ActiveSkillEntity.STATUS_ATTACK) {
						waitting = new ActiveSkillEntity(owner, skill, target, level, x, y, targetTypes, targetIds, direction);
					} else {
						executing = new ActiveSkillEntity(owner, skill, target, level, x, y, targetTypes, targetIds, direction);
					}
				} else {
					executing = new ActiveSkillEntity(owner, skill, target, level, x, y, targetTypes, targetIds, direction);
				}
			}
		}
		if (owner instanceof Player && owner.getLevel() <= 20) {
			Player.sendPlayerAction((Player) owner, PlayerActionFlow.行为类型_技能使用, skill.getName(), 0, new Date(), GamePlayerManager.isAllowActionStat());
		}
		return true;
	}

	public void heartbeat(Game g) {
		try {
			boolean isStun = false;
			boolean isSilence = false;
			boolean isIceState = false;

			if (owner instanceof Player) {
				Player p = (Player) owner;
				isStun = p.isStun();
				isSilence = p.isSilence();
				isIceState = p.isIceState();
			} else if (owner instanceof Sprite) {
				Sprite p = (Sprite) owner;
				isStun = p.isStun();
				isSilence = p.isSilence();
			}

			// ActiveSkillEntity entities[] = cooldown.values().toArray(new ActiveSkillEntity[0]);
			Iterator<ActiveSkillEntity> it = cooldown.values().iterator();
			while (it.hasNext()) {
				ActiveSkillEntity e = it.next();
				if(e == null){
					it.remove();
					logger.warn("[技能代理] [移除空的entity]");
					continue;
				}
				e.heartbeat(g);
				if (e.status == ActiveSkillEntity.STATUS_END) {
					// cooldown.remove(e.skill.getId());
					it.remove();
					if (waitting != null && waitting.skill.getId() == e.skill.getId() && executing == null) {
						executing = waitting;
						waitting = null;
					}
				}
			}

			if (executing == null) {
				if (waitting != null) {
					ActiveSkillEntity e = (ActiveSkillEntity) cooldown.get(waitting.skill.getId());
					if (e == null || e.status == ActiveSkillEntity.STATUS_END) {
						executing = waitting;
						waitting = null;
					}
				}
			}

			if (executing != null) {
				if (executing.status == ActiveSkillEntity.STATUS_START) {
					if ((isStun && !executing.skill.isIgnoreStun() && !executing.skill.isLostStun(owner)) || (isIceState && !executing.skill.isIgnoreStun() && !executing.skill.isLostStun(owner)) || (isSilence && !executing.skill.isIgnoreStun() && !(executing.skill instanceof CommonAttackSkill))) {
						executing = null;
						waitting = null;
					} else {
						executing.start();

						// 通知服务器，使用技能攻击
						if (owner instanceof Sprite) {
							Sprite sprite = (Sprite) owner;
							if (sprite.getMoveTrace() != null) sprite.stopAndNotifyOthers();
							if (executing.target != null) {
								g.doTargetSkillReqForSprite(sprite, executing.target, executing.skill);
							}
						}
					}
				}

				if (executing != null && (executing.status == ActiveSkillEntity.STATUS_COOLDOWN || executing.status == ActiveSkillEntity.STATUS_END)) {
					cooldown.put(executing.skill.getId(), executing);
					executing.end();
					executing = null;
				}
			}

			if (executing != null) {
				executing.heartbeat(g);
				if (executing.status == ActiveSkillEntity.STATUS_COOLDOWN || executing.status == ActiveSkillEntity.STATUS_END) {
					cooldown.put(executing.skill.getId(), executing);
					executing.end();
					executing = null;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			if (this.getExecuting() != null && this.getExecuting().getSkill() != null) {
				if (logger.isWarnEnabled()) logger.warn("技能异常:" + this.getExecuting().getSkill().getName(), ex);
			}
		}

	}

	public Fighter getOwner() {
		return owner;
	}

	public void setOwner(Fighter owner) {
		this.owner = owner;
	}

	public ActiveSkillEntity getExecuting() {
		return executing;
	}

	public void setExecuting(ActiveSkillEntity executing) {
		this.executing = executing;
	}

	public ActiveSkillEntity getWaitting() {
		return waitting;
	}

	public void setWaitting(ActiveSkillEntity waitting) {
		this.waitting = waitting;
	}

	public boolean isDuringCoolDown(int skillId) {
		ActiveSkillEntity ae = (ActiveSkillEntity) cooldown.get(skillId);
		if (ae != null && ae.status == ActiveSkillEntity.STATUS_COOLDOWN) {
			return true;
		}
		if (executing != null && executing.status == ActiveSkillEntity.STATUS_ATTACK) {
			return true;
		}

		return false;
	}

	/**
	 * 得到真正CD的所有的技能
	 * @return
	 */
	public ActiveSkillEntity[] getInCoolDownSkills() {
		return cooldown.values().toArray(new ActiveSkillEntity[0]);
	}
}
