package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.activity.xianling.XianLingManager;
import com.fy.engineserver.articleEnchant.AbnormalStateBuff;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.pet.Pet;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 
 * 中毒，每设定秒钟减少若干HP
 * 
 * 
 * 
 *
 */
@SimpleEmbeddable
public class Buff_ZhongDu extends Buff implements AbnormalStateBuff{
	
	private long lastExeTime = 0;
	
	private long LastingTime;
	
	public int hpFix;//提前计算好的伤害，大师技能用。

	public void setLastingTime(long lastingTime) {
		LastingTime = lastingTime;
	}


	
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			if(p.isImmunity() && !this.getTemplateName().trim().equals(RobberyConstant.火神buff)){
				this.setInvalidTime(0);//立即失效
			}else{
				p.setPoison(true);
			}
		}else if(owner instanceof Sprite){
			Sprite p = (Sprite)owner;
			if(p.isImmunity()){
				this.setInvalidTime(0);//立即失效
			}else{
				p.setPoison(true);
			}
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setPoison(false);
		}else if(owner instanceof Sprite){
			Sprite p = (Sprite)owner;
			p.setPoison(false);
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
		if(heartBeatStartTime - lastExeTime >= LastingTime){
			lastExeTime = heartBeatStartTime;
			if(owner instanceof Player){
				Player p = (Player)owner;
				p.setPoison(true);
				if(p.isImmunity() && !this.getTemplateName().trim().equals(RobberyConstant.火神buff)){		//渡劫火神buff不可以被无敌抵消掉
					this.setInvalidTime(0);//立即失效
				}else{
					int hp = 0;
					BuffTemplate_ZhongDu bt = (BuffTemplate_ZhongDu)this.getTemplate();
					if(bt.hp != null && bt.hp.length > getLevel()){
						hp = bt.hp[getLevel()];
					}
					if(hpFix>0){
						hp = hpFix;
					}
					if(this.getCauser() != null){
						//护盾吸收毒的伤害
						int hudun = owner.getHuDunDamage();
						if(hudun > 0){
							int damage = hp;
							if (hudun >= damage) {
								owner.setHuDunDamage(hudun - damage);

								owner.causeDamage(getCauser(), damage, 10,Fighter.DAMAGETYPE_XISHOU);
								getCauser().damageFeedback(owner, damage, 10,Fighter.DAMAGETYPE_XISHOU);
							} else {
							
								owner.setHuDunDamage(0);

								owner.causeDamage(getCauser(), hudun, 10,Fighter.DAMAGETYPE_XISHOU);
								getCauser().damageFeedback(owner, hudun, 10,Fighter.DAMAGETYPE_XISHOU);

								owner.causeDamage(getCauser(), damage - hudun, 10,Fighter.DAMAGETYPE_ZHAONGDU);
								getCauser().damageFeedback(owner, damage - hudun, 10,Fighter.DAMAGETYPE_ZHAONGDU);
							}
						}else{
							//读取玩家火炕----计算伤害。。减少
							if(this.getTemplateName().trim().equals(RobberyConstant.火神buff)) {
								int temp = -1;
								for(int i=0; i<RobberyConstant.HUODEFANCE.length; i++) {
									if(p.getFireDefence() >= RobberyConstant.HUODEFANCE[i]){
										temp = i;
									}
								}
								if(temp >= 0) {
									hp = (int) (hp - (hp * (RobberyConstant.BASEDEFANCE + RobberyConstant.NEXTDEFANCE*temp)) / 100);
								}
							}
							owner.causeDamage(getCauser(), hp, 10,Fighter.DAMAGETYPE_ZHAONGDU);
							getCauser().damageFeedback(owner, hp, 10,Fighter.DAMAGETYPE_ZHAONGDU);
						}
						
						//统计
						if(getCauser() instanceof Player){
							p.notifyAttack((Player)getCauser(), this.getTemplateName(), getLevel(),Fighter.DAMAGETYPE_ZHAONGDU, hp);
						}
					}
				}
			}else if(owner instanceof Sprite){
				Sprite s = (Sprite)owner;
				s.setPoison(true);
				if(s.isImmunity()){
					this.setInvalidTime(0);//立即失效
				}else{
					int hp = 0;
					BuffTemplate_ZhongDu bt = (BuffTemplate_ZhongDu)this.getTemplate();
					if(bt.hp != null && bt.hp.length > getLevel()){
						hp = bt.hp[getLevel()];
					}
					if(hpFix>0){
						hp = hpFix;
					}
					if (owner instanceof Pet) {
						hp = ((Pet)owner).checkInjuryAndPosiDamage(hp);
					}
					if(this.getCauser() != null){
						//护盾吸收毒的伤害
						int hudun = owner.getHuDunDamage();
						if(hudun > 0){
							int damage = hp;
							if (hudun >= damage) {
								owner.setHuDunDamage(hudun - damage);

								owner.causeDamage(getCauser(), damage, 10,Fighter.DAMAGETYPE_XISHOU);
								getCauser().damageFeedback(owner, damage, 10,Fighter.DAMAGETYPE_XISHOU);
							} else {
							
								owner.setHuDunDamage(0);

								owner.causeDamage(getCauser(), hudun, 10,Fighter.DAMAGETYPE_XISHOU);
								getCauser().damageFeedback(owner, hudun, 10,Fighter.DAMAGETYPE_XISHOU);

								owner.causeDamage(getCauser(), damage - hudun, 10,Fighter.DAMAGETYPE_ZHAONGDU);
								getCauser().damageFeedback(owner, damage - hudun, 10,Fighter.DAMAGETYPE_ZHAONGDU);
							}
						}else{
							owner.causeDamage(getCauser(), hp, 10,Fighter.DAMAGETYPE_ZHAONGDU);
							getCauser().damageFeedback(owner, hp, 10,Fighter.DAMAGETYPE_ZHAONGDU);
						}
					}
					
					//统计
					if(getCauser() instanceof Player && owner instanceof Monster){
						Monster m = (Monster)owner;
						m.notifyAttack((Player)getCauser(), this.getTemplateName(), getLevel(),Fighter.DAMAGETYPE_ZHAONGDU, hp);
					}
				}
			}
		}
	}

}
