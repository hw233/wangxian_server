package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class Buff_YuanSuShangHai extends Buff{
	int fengDamage = 0;
	int huoDamage = 0;
	int leiDamage = 0;
	int bingDamage = 0;
	public static final int FENG = 0;
	public static final int HUO = 1;
	public static final int LEI = 2;
	public static final int BING = 3;

	@Override
	public void start(Fighter owner) {
		// TODO Auto-generated method stub
		if(owner instanceof Player){
			Player p = (Player) owner;
			TransitRobberyManager.logger.info("[加元素伤害buff][" + p.getLogString() + "]" );
			
			BuffTemplate_YuanSuShangHai bt = (BuffTemplate_YuanSuShangHai) this.getTemplate();
			if(getLevel() > bt.getFengDamage().length){
				return;
			}
			char[] temp = null;
			if(bt.getImproveType() != null){
				temp = bt.getImproveType()[this.getLevel()].trim().toCharArray();
			}
			for(int i=0; i<temp.length; i++){			//0代表没有 1代表有   四位分别代表风火雷冰
				if(!"1".equals(String.valueOf(temp[i]))){
					continue;
				}
				if(i == FENG){
					fengDamage = bt.getFengDamage()[getLevel()];
					p.setWindAttackC(fengDamage);
				} else if(i == HUO){
					huoDamage = bt.getHuoDamage()[getLevel()];
					p.setFireAttackC(huoDamage);
				} else if(i == LEI){
					leiDamage = bt.getLeiDamage()[getLevel()];
					p.setThunderAttackC(leiDamage);
				} else if(i == BING){
					bingDamage = bt.getBingDamage()[getLevel()];
					p.setBlizzardAttackC(bingDamage);
				} else {
					TransitRobberyManager.logger.info("[增强元素buff失败]类型配多了！！" + i);
				}
			}
		} else if(owner instanceof Sprite){
			Sprite p = (Sprite) owner;
			BuffTemplate_YuanSuShangHai bt = (BuffTemplate_YuanSuShangHai) this.getTemplate();
			if(getLevel() > bt.getFengDamage().length){
				System.out.println("[增强元素buff失败][配置level错误][level=" + getLevel() + "]");
				return;
			}
			char[] temp = null;
			if(bt.getImproveType() != null){
				temp = bt.getImproveType()[this.getLevel()].trim().toCharArray();
			}
			for(int i=0; i<temp.length; i++){			//0代表没有 1代表有   四位分别代表风火雷冰
				if(!"1".equals(temp[i])){
					continue;
				}
				if(i == FENG){
					fengDamage = bt.getFengDamage()[getLevel()];
					p.setWindAttackC(p.getWindAttackC() + fengDamage);
				} else if(i == HUO){
					huoDamage = bt.getHuoDamage()[getLevel()];
					p.setFireAttackC(p.getFireAttackC() + huoDamage);
				} else if(i == LEI){
					leiDamage = bt.getLeiDamage()[getLevel()];
					p.setThunderAttackC(p.getThunderAttackC() + leiDamage);
				} else if(i == BING){
					bingDamage = bt.getBingDamage()[getLevel()];
					p.setBlizzardAttackC(p.getBlizzardAttackC() + bingDamage);
				} else {
					System.out.println("类型配多了！！");
				}
			}
		}
	}
	
	@Override
	public void end(Fighter owner) {
		// TODO Auto-generated method stub
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setWindAttackC(p.getWindAttackC() - fengDamage);
			p.setFireAttackC(p.getFireAttackC() - huoDamage);
			p.setThunderAttackC(p.getThunderAttackC() - leiDamage);
			p.setBlizzardAttackC(p.getBlizzardAttackC() - bingDamage);
		 } else if(owner instanceof Sprite){
			 Sprite p = (Sprite) owner;
			 p.setWindAttackC(p.getWindAttackC() - fengDamage);
			 p.setFireAttackC(p.getFireAttackC() - huoDamage);
			 p.setThunderAttackC(p.getThunderAttackC() - leiDamage);
			 p.setBlizzardAttackC(p.getBlizzardAttackC() - bingDamage);
		 }
	}

}
