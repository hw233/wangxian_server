package com.fy.engineserver.datasource.buff;

import java.util.Arrays;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 
 * 增加攻击，增加物理攻击和法术攻击
 * 
 * 
 * 
 *
 */
@SimpleEmbeddable
public class Buff_ZhengGong extends Buff{

	int physicalDamage = 0;
	int spellDamage = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		
		if(owner instanceof Player){
			Player p = (Player)owner;
			
			BuffTemplate_ZengGong bt = (BuffTemplate_ZengGong)this.getTemplate();
			if(bt.physicalDamage != null && bt.physicalDamage.length > this.getLevel()){
				physicalDamage = bt.physicalDamage[getLevel()];
			}
			if(bt.spellDamage != null && bt.spellDamage.length > this.getLevel()){
				spellDamage = bt.spellDamage[getLevel()];
			}
			p.setPhyAttackC((p.getPhyAttackC() + physicalDamage));
			p.setMagicAttackC((p.getMagicAttackC() + spellDamage));
		
			if(GameConstants.getInstance().getServerName().equals("仙尊降世")){
				try{
					Skill.logger.warn("[冰陵] [加法功] [OK] [level:"+this.getLevel()+"] ["+Arrays.toString(bt.spellDamage)+"] [spellDamage:"+spellDamage+"] [old:"+p.getMagicAttack()+"]");
				}catch(Exception e){
					Skill.logger.warn("[冰陵] [加法功] [异常] [level:"+this.getLevel()+"] ["+Arrays.toString(bt.spellDamage)+"] [spellDamage:"+spellDamage+"] [old:"+p.getMagicAttack()+"]",e);
				}
			}
		}else if(owner instanceof Sprite){

			Sprite s = (Sprite)owner;
		
				BuffTemplate_ZengGong bt = (BuffTemplate_ZengGong)this.getTemplate();
				if(bt.physicalDamage != null && bt.physicalDamage.length > this.getLevel()){
					physicalDamage = bt.physicalDamage[getLevel()];
				}
				if(bt.spellDamage != null && bt.spellDamage.length > this.getLevel()){
					spellDamage = bt.spellDamage[getLevel()];
				}
				s.setPhyAttackC((s.getPhyAttackC() + physicalDamage));
				s.setMagicAttackC((s.getMagicAttackC() + spellDamage));

			
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setPhyAttackC((p.getPhyAttackC() - physicalDamage));
			p.setMagicAttackC((p.getMagicAttackC() - spellDamage));
		}else if(owner instanceof Sprite){

			Sprite s = (Sprite)owner;
			s.setPhyAttackC((s.getPhyAttackC() - physicalDamage));
			s.setMagicAttackC((s.getMagicAttackC() - spellDamage));

		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	}

}
