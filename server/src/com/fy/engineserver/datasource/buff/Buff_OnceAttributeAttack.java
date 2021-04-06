package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.core.CoreSubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;
/**
 * 附加单次属性攻伤害（与施法者等级相关）及增加百分比同属性减抗
 * @author Administrator
 *
 */
@SimpleEmbeddable
public class Buff_OnceAttributeAttack extends Buff {
	/** 附加属性攻比例   总伤害=causer.level * attributeRate */
	private long addAttributeNum;
	/** 减抗百分比 */
	private int ignoreRate;
	/** 类型  0:风   1:火  2:雷  3:冰 */
	private byte attributeType;
	/** 攻击次数 */
	private int attackTimes;
	
	private volatile boolean hasDeduct;
	
	public static final byte wind = 0;
	public static final byte fire = 1;
	public static final byte thund = 2;
	public static final byte ice = 3;
	
	public static double addRate = 2;
	
	public void start(Fighter owner){
		if (this.getCauser() == null) {
			this.setInvalidTime(0);
			return ;
		}
		
		this.addAttributeNum = (long) (1L * addRate * ((BuffTemplate_OnceAttributeAttack)this.getTemplate()).getAttributeRate()[this.getLevel()] * this.getCauser().getLevel());
		this.attributeType = ((BuffTemplate_OnceAttributeAttack)this.getTemplate()).getAttributeType()[this.getLevel()];
		this.ignoreRate = ((BuffTemplate_OnceAttributeAttack)this.getTemplate()).getIgnoreRate()[this.getLevel()];
		if (this.addAttributeNum > Integer.MAX_VALUE) {
			this.setInvalidTime(0);
			CoreSubSystem.logger.warn("["+this.getClass().getSimpleName()+"] [增加属性攻异常] [buffLevel:" + this.getLevel() + "] [addRate:" + ((BuffTemplate_OnceAttributeAttack)this.getTemplate()).getAttributeRate() + "] [施法者等级:" + this.getLevel() + "]");
			return ;
		}
		attackTimes = 0;
		if(owner instanceof Sprite){
			Sprite s = (Sprite) owner;
			int addNum = (int) this.addAttributeNum;
			int oldNum = 0 ;
			switch (this.attributeType) {
			case wind:
				oldNum = s.getWindAttack();
				s.setWindAttackB(addNum);
				s.setWindIgnoreDefenceRateOther(ignoreRate);
				break;
			case fire:
				oldNum = s.getFireAttack();
				s.setFireAttackB(addNum);
				s.setFireIgnoreDefenceRateOther(ignoreRate);
				break;
			case thund:
				oldNum = s.getThunderAttack();
				s.setThunderAttackB(addNum);
				s.setThunderIgnoreDefenceRateOther(ignoreRate);
				break;
			case ice:
				oldNum = s.getBlizzardAttack();
				s.setBlizzardAttackB(addNum);
				s.setBlizzardIgnoreDefenceRateOther(ignoreRate);
				break;
			default:break;
			}
			if (Skill.logger.isDebugEnabled()) {
				Skill.logger.debug("["+this.getClass().getSimpleName()+"] [增加buff属性] [" + owner.getId() + "] [attributeType:" + attributeType + "] [addNum:" + addNum + "]");
			}
			if (oldNum > 0) {
				try {
					throw new Exception("[宠物属性攻异常]");
				} catch (Exception e) {
					if (TransitRobberyManager.logger.isInfoEnabled()) {
						TransitRobberyManager.logger.info("[id:"+s.getId()+"] [name:" + s.getName() + "] [level:"+s.getLevel()+"] [" + oldNum + "]", e);
					}
				}
			}
		} 
	}
	
	/**
	 * 攻击一次后失效
	 */
	public void notifyAttack(Fighter owner) {
		this.setInvalidTime(0);
		attackTimes ++ ;
		if(owner instanceof Sprite){
			Sprite s = (Sprite) owner;
			int addNum = (int) this.addAttributeNum;
			if (addNum <= 0) {
				addNum = (int) (1L * addRate * ((BuffTemplate_OnceAttributeAttack)this.getTemplate()).getAttributeRate()[this.getLevel()] * this.getCauser().getLevel());
			}
			if (ignoreRate <= 0) {
				ignoreRate = ((BuffTemplate_OnceAttributeAttack)this.getTemplate()).getIgnoreRate()[this.getLevel()];
			}
			switch (this.attributeType) {
			case wind:
				s.setWindAttackB(0);
				s.setWindIgnoreDefenceRateOther(0);
				break;
			case fire:
				s.setFireAttackB(0);
				s.setFireIgnoreDefenceRateOther(0);
				break;
			case thund:
				s.setThunderAttackB(0);
				s.setThunderIgnoreDefenceRateOther(0);
				break;
			case ice:
				s.setBlizzardAttackB(0);
				s.setBlizzardIgnoreDefenceRateOther(0);
				break;
			default:break;
			}
			if (Skill.logger.isDebugEnabled()) {
				Skill.logger.debug("["+this.getClass().getSimpleName()+"] [清除buff附加属性] [" + owner.getId() + "] [attributeType:" + attributeType + "] [addNum:" + addNum + "]");
			}
		} 
	}


	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if (Skill.logger.isDebugEnabled()) {
			Skill.logger.debug("["+this.getClass().getSimpleName()+"] [buff结束] [不用做任何操作] [" + owner.getId() + "] [attackTimes:" + attackTimes + "]");
		}
		if(owner instanceof Sprite){
			Sprite s = (Sprite) owner;
			int addNum = (int) this.addAttributeNum;
			if (addNum <= 0) {
				addNum = (int) (1L * addRate * ((BuffTemplate_OnceAttributeAttack)this.getTemplate()).getAttributeRate()[this.getLevel()] * this.getCauser().getLevel());
			}
			if (ignoreRate <= 0) {
				ignoreRate = ((BuffTemplate_OnceAttributeAttack)this.getTemplate()).getIgnoreRate()[this.getLevel()];
			}
			switch (this.attributeType) {
			case wind:
				s.setWindAttackB(0);
				s.setWindIgnoreDefenceRateOther(0);
				break;
			case fire:
				s.setFireAttackB(0);
				s.setFireIgnoreDefenceRateOther(0);
				break;
			case thund:
				s.setThunderAttackB(0);
				s.setThunderIgnoreDefenceRateOther(0);
				break;
			case ice:
				s.setBlizzardAttackB(0);
				s.setBlizzardIgnoreDefenceRateOther(0);
				break;
			default:break;
			}
			if (Skill.logger.isDebugEnabled()) {
				Skill.logger.debug("["+this.getClass().getSimpleName()+"] [清除buff附加属性] [" + owner.getId() + "]");
			}
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	
	}

	public boolean isHasDeduct() {
		return hasDeduct;
	}

	public void setHasDeduct(boolean hasDeduct) {
		this.hasDeduct = hasDeduct;
	}

	
}
