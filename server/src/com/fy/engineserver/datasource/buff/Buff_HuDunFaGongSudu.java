package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.npc.NPC;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 
 * 护盾法攻相关和速度
 * 
 *
 */
@SimpleEmbeddable
public class Buff_HuDunFaGongSudu extends Buff{
	int modulus;
	int speed = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			
			byte shield = -1;
			BuffTemplate_HuDunFaGongSudu bt = (BuffTemplate_HuDunFaGongSudu)this.getTemplate();

			if(bt.speed != null && bt.speed.length > getLevel()){
				speed = bt.speed[getLevel()];
			}
			p.setSpeedC((p.getSpeedC() + speed));

			if(bt.modulus != null && bt.modulus.length > getLevel()){
				modulus = bt.modulus[getLevel()];
				shield = bt.getShield();
			}
			int damage = 0;
			if(getCauser() instanceof Player){
				//除以100是因为在编辑器里编辑的系数为整数，约定除以100为真正的系数
				damage = (int)(((Player)getCauser()).getMagicAttack()*modulus/100);	
			}else if(getCauser() instanceof Monster){
				Monster m = (Monster)getCauser();
				damage = (int)(m.getMagicAttack()*modulus/100);
			}else if(getCauser() instanceof NPC){
				NPC n = (NPC)getCauser();
				damage = (int)(n.getMagicAttack()*modulus/100);
			}
			p.setShield(shield);
			p.setHuDunDamage(damage);
			
		}else if(owner instanceof Sprite){
			Sprite p = (Sprite)owner;
			byte shield = -1;
			
			
			BuffTemplate_HuDunFaGongSudu bt = (BuffTemplate_HuDunFaGongSudu)this.getTemplate();

			if(bt.speed != null && bt.speed.length > getLevel()){
				speed = bt.speed[getLevel()];
			}
			p.setSpeedC((p.getSpeedC() + speed));

			if(bt.modulus != null && bt.modulus.length > getLevel()){
				modulus = bt.modulus[getLevel()];
				shield = bt.getShield();
			}
			int damage = 0;
			if(getCauser() instanceof Player){
				//除以100是因为在编辑器里编辑的系数为整数，约定除以100为真正的系数
				damage = (int)(((Player)getCauser()).getMagicAttack()*modulus/100);	
			}else if(getCauser() instanceof Monster){
				Monster m = (Monster)getCauser();
				damage = (int)(m.getMagicAttack()*modulus/100);
			}else if(getCauser() instanceof NPC){
				NPC n = (NPC)getCauser();
				damage = (int)(n.getMagicAttack()*modulus/100);
			}
			p.setShield(shield);
			p.setHuDunDamage(damage);
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner) {
		if (owner instanceof Player) {
			Player p = (Player) owner;
			p.setShield(Player.SHIELD_NONE);
			p.setSpeedC((p.getSpeedC()- speed));
			p.setHuDunDamage(0);

		} else if (owner instanceof Sprite) {
			Sprite p = (Sprite) owner;
			p.setShield(Player.SHIELD_NONE);
			p.setSpeedC((p.getSpeedC() - speed));
			p.setHuDunDamage(0);
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
		
		if(owner instanceof Player){
			Player p = (Player)owner;
			if(p.getHuDunDamage() <= 0){
				this.setInvalidTime(0);//立即失效
			}
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			if(s.getHuDunDamage() <= 0){
				this.setInvalidTime(0);//立即失效
			}
		}
	}
	
	public String getDescription(){
		BuffTemplate_HuDunFaGongSudu bt = (BuffTemplate_HuDunFaGongSudu)this.getTemplate();
		int damage = 0;
		if(bt != null && bt.modulus != null && bt.modulus.length > getLevel()){
			if(getCauser() instanceof Player){
				//除以100是因为在编辑器里编辑的系数为整数，约定除以100为真正的系数
				damage = (int)(((Player)getCauser()).getMagicAttack()*bt.modulus[getLevel()]/100);	
			}else if(getCauser() instanceof Monster){
				Monster m = (Monster)getCauser();
				damage = (int)(m.getMagicAttack()*bt.modulus[getLevel()]/100);
			}else if(getCauser() instanceof NPC){
				NPC n = (NPC)getCauser();
				damage = (int)(n.getMagicAttack()*bt.modulus[getLevel()]/100);
			}
			StringBuffer sb = new StringBuffer();
			sb.append(Translate.text_3398+damage+Translate.text_3201);
			if(bt.speed != null && bt.speed.length > getLevel()){
				if(bt.speed[getLevel()] > 0)
					sb.append(Translate.text_3402+bt.speed[getLevel()]+"%");
				else if(bt.speed[getLevel()] < 0)
					sb.append(Translate.text_3403+(-bt.speed[getLevel()])+"%");
			}
			return sb.toString();
		}
		return super.getDescription();
	}

}
