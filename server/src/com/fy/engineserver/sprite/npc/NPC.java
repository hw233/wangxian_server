package com.fy.engineserver.sprite.npc;

import org.w3c.dom.Element;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.npc.npcaction.NpcExecuteItem;

public class NPC extends Sprite implements Cloneable {

	private static final long serialVersionUID = -4393238389156456469L;

	public byte getSpriteType() {
		return Sprite.SPRITE_TYPE_NPC;
	}

	/**
	 * NPC对应窗口的Id
	 */
	protected int windowId = -1;

	private int nPCCategoryId;

	protected long deadStartTime;

	//npc的能力调整
	double npcMark;

	//npc的能力调整
	double hpMark;

	//npc的能力调整
	double apMark;
	
	//npc的颜色
	byte color;

	NpcExecuteItem[] items = null;

	public NpcExecuteItem[] getItems() {
		return items;
	}

	public void setItems(NpcExecuteItem[] items) {
		this.items = items;
	}

	public int getNPCCategoryId() {
		return getnPCCategoryId();
	}

	public void setNPCCategoryId(int categoryId) {
		setnPCCategoryId(categoryId);
	}

	public double getNpcMark() {
		return npcMark;
	}

	public void setNpcMark(double npcMark) {
		this.npcMark = npcMark;
	}

	public double getHpMark() {
		return hpMark;
	}

	public void setHpMark(double hpMark) {
		this.hpMark = hpMark;
	}

	public double getApMark() {
		return apMark;
	}

	public void setApMark(double apMark) {
		this.apMark = apMark;
	}

	public byte getColor() {
		return color;
	}

	public void setColor(byte color) {
		this.color = color;
	}

	/**
	 * NPC出生后，调用此初始化方法
	 */
	public void init() {
		super.init();
	}

	public void setAdditionData(Element e) {

	}

	public void heartbeat(long heartBeatStartTime, long interval, Game game) {
		super.heartbeat(heartBeatStartTime, interval, game);

		if (state != STATE_DEAD) {
			if (this.getHp() <= 0) {
				this.removeMoveTrace();
				this.state = STATE_DEAD;
				deadStartTime = heartBeatStartTime;
				killed(heartBeatStartTime, interval, game);

				this.setStun(false);
				this.setImmunity(false);
				this.setInvulnerable(false);
				this.setPoison(false);
				this.setAura((byte) -1);
				this.setHold(false);
				this.setWeak(false);

			}
		}

		if (state == STATE_DEAD) {
			if (heartBeatStartTime - deadStartTime > deadLastingTime) {
				this.setAlive(false);
			}
			return;
		}

		// 下面是npc ai的部分，所有其他逻辑都需要放到此部分上面，避免执行不到
		Fighter target = getMaxHatredFighter();
		if (target == null) {
// return;
		}
		
		if (items != null) {
			for (int i = 0; i < items.length; i++) {
				NpcExecuteItem ni = items[i];
				if (ni != null && ni.action != null) {
					if(this.getMoveTrace() == null || ni.isIgnoreMoving()){
						ni.action.doAction(game, this, ni, target, heartBeatStartTime);
					}
				} else {
//					if (target != null && target instanceof Monster && ((Monster)target).getSpriteCategoryId() == DisasterConstant.TEMP_MONSTER_ID) {
//						DisasterManager.logger.debug("[测试] [" + this.getId() + "," + this.getName() + "] [" + target.getId() + "] [" + Arrays.toString(items) + "]");
//					}
				}
			}
		}
	}

	public Fighter getMaxHatredFighter() {
		return null;
	}

	/**
	 * 处理其它生物对此生物造成伤害的事件
	 * 
	 * @param caster
	 *            伤害施加者
	 * @param damage
	 *            预期伤害值
	 * @param damageType
	 *            伤害类型，如：普通物理伤害，魔法伤害，反噬伤害等
	 */
	public void causeDamage(Fighter caster, int damage, int hateParam, int damageType) {
		// debug dot
		super.causeDamage(caster, damage, hateParam, damageType);
	}

	/**
	 * 处理伤害反馈事件。当某个精灵（玩家、怪物等）受到攻击并造成伤害，<br>
	 * 该精灵会调用攻击者的这个方法，通知攻击者，它的攻击对其他精灵造成了伤害
	 * 
	 * @param target
	 *            受到伤害的目标精灵
	 * @param damage
	 *            真实伤害值
	 */
	public void damageFeedback(Fighter target, int damage, int damageType) {
		// this.fightingAgent.damageFeedback(target, damage, damageType);
	}

	protected void killed(long heartBeatStartTime, long interval, Game game) {

	}

	/**
	 * clone出一个对象
	 */
	public Object clone() {
		NPC p = new NPC();

		p.cloneAllInitNumericalProperty(this);

		p.country = country;

		p.npcMark = npcMark;
		p.hpMark = hpMark;
		p.apMark = apMark;
		p.setnPCCategoryId(this.getnPCCategoryId());

		if (items != null) {
			p.items = new NpcExecuteItem[this.items.length];
			for (int i = 0; i < items.length; i++) {
				if (items[i] != null) {
					try {
						p.items[i] = (NpcExecuteItem) items[i].clone();
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		p.windowId = windowId;
		p.id = nextId();
		return p;
	}

	public int getWindowId() {
		return windowId;
	}

	public void setWindowId(int windowId) {
		this.windowId = windowId;
		if (this instanceof SeptStationNPC) {
			if (JiazuSubSystem.logger.isDebugEnabled()) {
				try {
					throw new Exception("[设置wid] [" + windowId + "] [" + this.getnPCCategoryId() + "]");
				} catch (Exception e) {
					JiazuSubSystem.logger.debug("[测试] [设置npcWid] [" + this.getName() + "] [" + this.getnPCCategoryId() + "] [" + windowId + "]", e);
				}
			}
		}
	}

	public void setnPCCategoryId(int nPCCategoryId) {
		this.nPCCategoryId = nPCCategoryId;
	}

	public int getnPCCategoryId() {
		return nPCCategoryId;
	}
	
	
}
