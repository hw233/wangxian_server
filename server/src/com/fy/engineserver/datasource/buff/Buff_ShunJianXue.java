package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 瞬间减血
 *
 */
@SimpleEmbeddable
public class Buff_ShunJianXue extends Buff{

	int hp = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			if(!p.isImmunity()){
				BuffTemplate_ShunJianXue bt = (BuffTemplate_ShunJianXue)this.getTemplate();
				if(bt.hp != null && bt.hp.length > getLevel()){
					hp = bt.hp[getLevel()];
					hp = p.getMaxHP()*hp/100;
				}

				if(this.getCauser() != null){
					//护盾吸收伤害
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

							owner.causeDamage(getCauser(), damage - hudun, 10,Fighter.DAMAGETYPE_PHYSICAL);
							getCauser().damageFeedback(owner, damage - hudun, 10,Fighter.DAMAGETYPE_PHYSICAL);
						}
					}else{
						owner.causeDamage(getCauser(), hp, 10,Fighter.DAMAGETYPE_PHYSICAL);
						getCauser().damageFeedback(owner, hp, 10,Fighter.DAMAGETYPE_PHYSICAL);
					}
					
				}
			}

		}else if(owner instanceof Sprite){
			Sprite p = (Sprite)owner;
			if(!p.isImmunity()){
				BuffTemplate_ShunJianXue bt = (BuffTemplate_ShunJianXue)this.getTemplate();
				if(bt.hp != null && bt.hp.length > getLevel()){
					hp = bt.hp[getLevel()];
					hp = p.getMaxHP()*hp/100;
				}

				if(this.getCauser() != null){
					//护盾吸收伤害
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

							owner.causeDamage(getCauser(), damage - hudun, 10,Fighter.DAMAGETYPE_PHYSICAL);
							getCauser().damageFeedback(owner, damage - hudun, 10,Fighter.DAMAGETYPE_PHYSICAL);
						}
					}else{
						owner.causeDamage(getCauser(), hp, 10,Fighter.DAMAGETYPE_PHYSICAL);
						getCauser().damageFeedback(owner, hp, 10,Fighter.DAMAGETYPE_PHYSICAL);
					}
				}
			}
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){

		}else if(owner instanceof Sprite){
		
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	
	}

}
