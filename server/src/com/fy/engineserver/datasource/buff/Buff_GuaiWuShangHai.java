package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.monster.Monster;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 
 *
 */
@SimpleEmbeddable
public class Buff_GuaiWuShangHai extends Buff{
	int reduceRate;
	long monsterId;
	String sceneName;

	@Override
	public void start(Fighter owner) {
		// TODO Auto-generated method stub
		if(owner instanceof Monster){
			Monster s = (Monster) owner;
			BuffTemplate_GuaiWuShangHai bt = (BuffTemplate_GuaiWuShangHai) getTemplate();
			sceneName = bt.getSceneName()[this.getLevel()];
//			monsterId = bt.getMonsterId()[this.getLevel()];
//			if(!s.getGameName().equals(sceneName) || s.getSpriteCategoryId() != monsterId){
//				System.out.println("buff上错怪了");
//				return;
//			}
			
			if(getLevel() >= bt.getReduceRate().length){
				TransitRobberyManager.logger.info("[减少怪物伤害buff错误][level=" + getLevel() + "  buffRate.length=" + bt.getReduceRate().length + "]");
				return;
			}
			reduceRate = bt.getReduceRate()[getLevel()];
			if(reduceRate > 100){
				TransitRobberyManager.logger.info("[减少怪物伤害buff错误][有点太狠了，减少比率超过1][reduceRate=" + reduceRate + "]");
				reduceRate = 0;
				return;
			}
			s.setPhyAttack((int) (s.getPhyAttack() * (100 - reduceRate) * 0.01));
			s.setMagicAttack((int) (s.getMagicAttack() * (100 - reduceRate) * 0.01));
			TransitRobberyManager.logger.info("[给怪物上buff][怪物id=" + monsterId + "]");
		}
	}

	@Override
	public void end(Fighter owner) {
		// TODO Auto-generated method stub
		if(owner instanceof Monster){
			Monster s = (Monster) owner;
//			if(!s.getGameName().equals(sceneName) || s.getId() != monsterId){
//				System.out.println("buff上错怪了");
//				return;
//			}
			s.setPhyAttack((int) (s.getPhyAttack() / ((100 - reduceRate) * 0.01)));
			s.setMagicAttack((int) (s.getMagicAttack() / ((100 - reduceRate) *0.01)));
		}
	}
}
