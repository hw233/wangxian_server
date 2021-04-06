package com.fy.engineserver.datasource.skill;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.sprite.Fighter;

public class NewAuraBuff extends Buff {

	transient Buff buff;
	transient NewAuraSkillAgent agent;
	int level;

	public NewAuraBuff(NewAuraSkillAgent agent, Buff buff) {

		if (buff instanceof NewAuraBuff) {
			throw new java.lang.IllegalArgumentException("光环Buff内部不能嵌套光环Buff");
		}
		this.agent = agent;
		// this.skill = agent.getAuraSkill();
		this.level = buff.getLevel();
		this.buff = buff;
		this.setInvalidTime(buff.getInvalidTime());
		this.setAdvantageous(buff.isAdvantageous());
		this.setSyncWithClient(true);
		this.setForover(true);
	}

	public String getTemplateName() {
		return buff.getTemplateName();
	}

	public byte getBufferType() {
		return buff.getBufferType();
	}

	public String getDescription() {
		return buff.getDescription();
	}

	/**
	 * 级别，同一类型的Buff，高级别的Buff将替代低级别的Buff
	 * 替代的时候，低级别的buff的end方法将会被调用
	 * @return
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner) {
		buff.start(owner);
	}

	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner) {
		buff.end(owner);
	}

	public static int[] range = new int[] { 1000, 1000 };

	/**
	 * 心跳函数，此心跳函数每1秒钟跳一次
	 */
	public void heartbeat(Fighter owner, long heartBeatStartTime, long interval, Game game) {

		buff.heartbeat(owner, heartBeatStartTime, interval, game);

		if (agent.getOwner() == null) {
			this.setInvalidTime(heartBeatStartTime);
		} else if (!game.contains((LivingObject) agent.getOwner())) {
			this.setInvalidTime(heartBeatStartTime);
		} else if (agent.getOwner().isDeath()) {
			this.setInvalidTime(heartBeatStartTime);
		} else if (agent.getSkillId() <= 0) {
			this.setInvalidTime(heartBeatStartTime);
		} else {
			if (this.getInvalidTime() > heartBeatStartTime) {//
				if (!agent.isValid(owner)) {				//
					this.setInvalidTime(heartBeatStartTime);
				}
				/*// 检查范围
				float dx = owner.getX() - agent.getOwner().getX();
				float dy = owner.getY() - agent.getOwner().getY();
				if (dx < -range[0] || dx > range[0] || dy < -range[1] || dy > range[1]) {
					this.setInvalidTime(heartBeatStartTime);
				}*/
			}
		}
	}

}
