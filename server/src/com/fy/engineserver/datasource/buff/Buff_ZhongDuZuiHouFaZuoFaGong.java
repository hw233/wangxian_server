package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.articleEnchant.AbnormalStateBuff;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.npc.NPC;
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
public class Buff_ZhongDuZuiHouFaZuoFaGong extends Buff implements AbnormalStateBuff{

	//执行时间点
	long activeTime = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			if(p.isImmunity()){
				this.setInvalidTime(0);//立即失效
			}else{
				p.setPoison(true);
				activeTime = this.getInvalidTime() - 600;
			}
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			if(s.isImmunity()){
				this.setInvalidTime(0);//立即失效
			}else{
				s.setPoison(true);
				activeTime = this.getInvalidTime() - 600;
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
			Sprite s = (Sprite)owner;
			s.setPoison(false);
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
		if(owner instanceof Player){
			Player p = (Player)owner;
			if(p.isImmunity()){
				this.setInvalidTime(0);//立即失效
			}else{
				p.setPoison(true);
				if(activeTime > 0 && heartBeatStartTime >= activeTime){
					int hp = 0;
					BuffTemplate_ZhongDuZuiHouFaZuoFaGong bt = (BuffTemplate_ZhongDuZuiHouFaZuoFaGong)this.getTemplate();
					if(bt.modulus != null && bt.modulus.length > getLevel()){
						if(getCauser() instanceof Player){
							//除以100是因为在编辑器里编辑的系数为整数，约定除以100为真正的系数
							hp = (int)(((Player)getCauser()).getMagicAttack()*bt.modulus[getLevel()]/100);	
						}else if(getCauser() instanceof Monster){
							Monster m = (Monster)getCauser();
							hp = (int)(m.getMagicAttack()*bt.modulus[getLevel()]/100);
						}else if(getCauser() instanceof NPC){
							NPC n = (NPC)getCauser();
							hp = (int)(n.getMagicAttack()*bt.modulus[getLevel()]/100);
						}
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
					this.setInvalidTime(0);
				}
			}
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			if(s.isImmunity()){
				this.setInvalidTime(0);//立即失效
			}else{
				s.setPoison(true);
				if(activeTime > 0 && heartBeatStartTime >= activeTime){
					int hp = 0;
					BuffTemplate_ZhongDuZuiHouFaZuoFaGong bt = (BuffTemplate_ZhongDuZuiHouFaZuoFaGong)this.getTemplate();
					if(bt.modulus != null && bt.modulus.length > getLevel()){
						if(getCauser() instanceof Player){
							//除以100是因为在编辑器里编辑的系数为整数，约定除以100为真正的系数
							hp = (int)(((Player)getCauser()).getMagicAttack()*bt.modulus[getLevel()]/100);	
						}else if(getCauser() instanceof Monster){
							Monster m = (Monster)getCauser();
							hp = (int)(m.getMagicAttack()*bt.modulus[getLevel()]/100);
						}else if(getCauser() instanceof NPC){
							NPC n = (NPC)getCauser();
							hp = (int)(n.getMagicAttack()*bt.modulus[getLevel()]/100);
						}
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
					this.setInvalidTime(0);
				}
			}
		}
	}
	
	public String getDescription(){
		int hp = 0;
		BuffTemplate_ZhongDuZuiHouFaZuoFaGong bt = (BuffTemplate_ZhongDuZuiHouFaZuoFaGong)this.getTemplate();
		if(bt != null && bt.modulus != null && bt.modulus.length > getLevel()){
			if(getCauser() instanceof Player){
				//除以100是因为在编辑器里编辑的系数为整数，约定除以100为真正的系数
				hp = (int)(((Player)getCauser()).getMagicAttack()*bt.modulus[getLevel()]/100);	
			}else if(getCauser() instanceof Monster){
				Monster m = (Monster)getCauser();
				hp = (int)(m.getMagicAttack()*bt.modulus[getLevel()]/100);
			}else if(getCauser() instanceof NPC){
				NPC n = (NPC)getCauser();
				hp = (int)(n.getMagicAttack()*bt.modulus[getLevel()]/100);
			}
			return Translate.text_3352+hp+Translate.text_3275;
		}
		return super.getDescription();
	}

}
