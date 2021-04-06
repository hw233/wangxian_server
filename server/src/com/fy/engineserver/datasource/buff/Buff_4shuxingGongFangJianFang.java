package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class Buff_4shuxingGongFangJianFang extends Buff {
	protected int fireAttackB;
	protected int fireDefenceB;
	protected int fireIgnoreDefenceB;
	protected int blizzardAttackB;
	protected int blizzardDefenceB;
	protected int blizzardIgnoreDefenceB;
	protected int windAttackB;
	protected int windDefenceB;
	protected int windIgnoreDefenceB;
	protected int thunderAttackB;
	protected int thunderDefenceB;
	protected int thunderIgnoreDefenceB;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		BuffTemplate_4shuxingGongFangJianFang bt = (BuffTemplate_4shuxingGongFangJianFang)this.getTemplate();
		calc(bt);
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setFireAttackB((p.getFireAttackB() + fireAttackB));
			fireDefenceB = check(fireDefenceB, p.getFireDefenceB());
			blizzardDefenceB = check(blizzardDefenceB, p.getBlizzardDefenceB());
			windDefenceB = check(windDefenceB, p.getWindDefenceB());
			thunderDefenceB = check(windDefenceB, p.getThunderDefenceB());
			p.setFireDefenceB((p.getFireDefenceB() + fireDefenceB));
			p.setFireIgnoreDefenceB((p.getFireIgnoreDefenceB() + fireIgnoreDefenceB));
			p.setBlizzardAttackB((p.getBlizzardAttackB() + blizzardAttackB));
			p.setBlizzardDefenceB((p.getBlizzardDefenceB() + blizzardDefenceB));
			p.setBlizzardIgnoreDefenceB((p.getBlizzardIgnoreDefenceB() + blizzardIgnoreDefenceB));
			p.setWindAttackB((p.getWindAttackB() + windAttackB));
			p.setWindDefenceB((p.getWindDefenceB() + windDefenceB));
			p.setWindIgnoreDefenceB((p.getWindIgnoreDefenceB() + windIgnoreDefenceB));
			p.setThunderAttackB((p.getThunderAttackB() + thunderAttackB));
			p.setThunderDefenceB((p.getThunderDefenceB() + thunderDefenceB));
			p.setThunderIgnoreDefenceB((p.getThunderIgnoreDefenceB() + thunderIgnoreDefenceB));
		}else if(owner instanceof Sprite){
			Sprite p = (Sprite)owner;
			fireDefenceB = check(fireDefenceB, p.getFireDefenceB());
			blizzardDefenceB = check(blizzardDefenceB, p.getBlizzardDefenceB());
			windDefenceB = check(windDefenceB, p.getWindDefenceB());
			thunderDefenceB = check(windDefenceB, p.getThunderDefenceB());
			p.setFireAttackB((p.getFireAttackB() + fireAttackB));
			p.setFireDefenceB((p.getFireDefenceB() + fireDefenceB));
			p.setFireIgnoreDefenceB((p.getFireIgnoreDefenceB() + fireIgnoreDefenceB));
			p.setBlizzardAttackB((p.getBlizzardAttackB() + blizzardAttackB));
			p.setBlizzardDefenceB((p.getBlizzardDefenceB() + blizzardDefenceB));
			p.setBlizzardIgnoreDefenceB((p.getBlizzardIgnoreDefenceB() + blizzardIgnoreDefenceB));
			p.setWindAttackB((p.getWindAttackB() + windAttackB));
			p.setWindDefenceB((p.getWindDefenceB() + windDefenceB));
			p.setWindIgnoreDefenceB((p.getWindIgnoreDefenceB() + windIgnoreDefenceB));
			p.setThunderAttackB((p.getThunderAttackB() + thunderAttackB));
			p.setThunderDefenceB((p.getThunderDefenceB() + thunderDefenceB));
			p.setThunderIgnoreDefenceB((p.getThunderIgnoreDefenceB() + thunderIgnoreDefenceB));
		}
	}


	/**
	 * 检查是否会导致数值为负数。 
	 * @param fireDefenceB2
	 * @param fireDefenceB3
	 * @return
	 */
	public int check(int diff, int base) {
		if(diff<0 && base>= 0 && diff + base<0){
			Skill.logger.debug("防止属性成为负数 diff{} base{}",diff,base);
			return -base;//保证数值降低时，最低降到0
		}
		return diff;
	}


	public void calc(BuffTemplate_4shuxingGongFangJianFang bt) {
		if(bt.fireAttackB != null && bt.fireAttackB.length > getLevel()){
			fireAttackB = bt.fireAttackB[getLevel()];
		}
		if(bt.fireDefenceB != null && bt.fireDefenceB.length > getLevel()){
			fireDefenceB = bt.fireDefenceB[getLevel()];
		}
		if(bt.fireIgnoreDefenceB != null && bt.fireIgnoreDefenceB.length > getLevel()){
			fireIgnoreDefenceB = bt.fireIgnoreDefenceB[getLevel()];
		}
		if(bt.blizzardAttackB != null && bt.blizzardAttackB.length > getLevel()){
			blizzardAttackB = bt.blizzardAttackB[getLevel()];
		}
		if(bt.blizzardDefenceB != null && bt.blizzardDefenceB.length > getLevel()){
			blizzardDefenceB = bt.blizzardDefenceB[getLevel()];
		}
		if(bt.blizzardIgnoreDefenceB != null && bt.blizzardIgnoreDefenceB.length > getLevel()){
			blizzardIgnoreDefenceB = bt.blizzardIgnoreDefenceB[getLevel()];
		}
		if(bt.windAttackB != null && bt.windAttackB.length > getLevel()){
			windAttackB = bt.windAttackB[getLevel()];
		}
		if(bt.windDefenceB != null && bt.windDefenceB.length > getLevel()){
			windDefenceB = bt.windDefenceB[getLevel()];
		}
		if(bt.windIgnoreDefenceB != null && bt.windIgnoreDefenceB.length > getLevel()){
			windIgnoreDefenceB = bt.windIgnoreDefenceB[getLevel()];
		}
		if(bt.thunderAttackB != null && bt.thunderAttackB.length > getLevel()){
			thunderAttackB = bt.thunderAttackB[getLevel()];
//			System.out.println("thunderAttackB"+thunderAttackB);
		}
		if(bt.thunderDefenceB != null && bt.thunderDefenceB.length > getLevel()){
			thunderDefenceB = bt.thunderDefenceB[getLevel()];
		}
		if(bt.thunderIgnoreDefenceB != null && bt.thunderIgnoreDefenceB.length > getLevel()){
			thunderIgnoreDefenceB = bt.thunderIgnoreDefenceB[getLevel()];
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setFireAttackB((p.getFireAttackB() - fireAttackB));
			p.setFireDefenceB((p.getFireDefenceB() - fireDefenceB));
			p.setFireIgnoreDefenceB((p.getFireIgnoreDefenceB() - fireIgnoreDefenceB));
			p.setBlizzardAttackB((p.getBlizzardAttackB() - blizzardAttackB));
			p.setBlizzardDefenceB((p.getBlizzardDefenceB() - blizzardDefenceB));
			p.setBlizzardIgnoreDefenceB((p.getBlizzardIgnoreDefenceB() - blizzardIgnoreDefenceB));
			p.setWindAttackB((p.getWindAttackB() - windAttackB));
			p.setWindDefenceB((p.getWindDefenceB() - windDefenceB));
			p.setWindIgnoreDefenceB((p.getWindIgnoreDefenceB() - windIgnoreDefenceB));
			p.setThunderAttackB((p.getThunderAttackB() - thunderAttackB));
			p.setThunderDefenceB((p.getThunderDefenceB() - thunderDefenceB));
			p.setThunderIgnoreDefenceB((p.getThunderIgnoreDefenceB() - thunderIgnoreDefenceB));
		}else if(owner instanceof Sprite){
			Sprite p = (Sprite)owner;
			p.setFireAttackB((p.getFireAttackB() - fireAttackB));
			p.setFireDefenceB((p.getFireDefenceB() - fireDefenceB));
			p.setFireIgnoreDefenceB((p.getFireIgnoreDefenceB() - fireIgnoreDefenceB));
			p.setBlizzardAttackB((p.getBlizzardAttackB() - blizzardAttackB));
			p.setBlizzardDefenceB((p.getBlizzardDefenceB() - blizzardDefenceB));
			p.setBlizzardIgnoreDefenceB((p.getBlizzardIgnoreDefenceB() - blizzardIgnoreDefenceB));
			p.setWindAttackB((p.getWindAttackB() - windAttackB));
			p.setWindDefenceB((p.getWindDefenceB() - windDefenceB));
			p.setWindIgnoreDefenceB((p.getWindIgnoreDefenceB() - windIgnoreDefenceB));
			p.setThunderAttackB((p.getThunderAttackB() - thunderAttackB));
			p.setThunderDefenceB((p.getThunderDefenceB() - thunderDefenceB));
			p.setThunderIgnoreDefenceB((p.getThunderIgnoreDefenceB() - thunderIgnoreDefenceB));
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	
	}
}
