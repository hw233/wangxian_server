package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;
/**
 * 增加宠物血量和反伤比例（反正只对宠物和玩家有效）
 * @author Administrator
 *
 */
@SimpleEmbeddable
public class Buff_addHpAndAttack extends Buff {
	
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player) owner;
			int hpNum = ((BuffTemplate_addHpAndAttack)this.getTemplate()).getHpNums()[this.getLevel()];
			int attackNum = ((BuffTemplate_addHpAndAttack)this.getTemplate()).getAttackNum()[this.getLevel()];
			p.setMaxHPB(p.getMaxHPB() + hpNum);
			p.setMagicAttackB(p.getMagicAttackB() + attackNum);
			p.setPhyAttackB(p.getPhyAttackB() + attackNum);
		}
	}


	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if (owner instanceof Player) {
			Player p = (Player) owner;
			int hpNum = ((BuffTemplate_addHpAndAttack)this.getTemplate()).getHpNums()[this.getLevel()];
			int attackNum = ((BuffTemplate_addHpAndAttack)this.getTemplate()).getAttackNum()[this.getLevel()];
			p.setMaxHPB(p.getMaxHPB() - hpNum);
			p.setMagicAttackB(p.getMagicAttackB() - attackNum);
			p.setPhyAttackB(p.getPhyAttackB() - attackNum);
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	
	}
	
}
