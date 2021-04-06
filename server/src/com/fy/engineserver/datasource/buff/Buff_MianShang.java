package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class Buff_MianShang extends Buff{
	int fengDamage = 0;
	int huoDamage = 0;
	int leiDamage = 0;
	int bingDamage = 0;
	int phyDamage = 0;
	int magDamage = 0;
	public static final int WULI = 4;
	public static final int FASHU = 5;
	@Override
	public void start(Fighter owner) {
		// TODO Auto-generated method stub
		if(owner instanceof Player){
			Player p = (Player) owner;
			BuffTemplate_MianShang bt = (BuffTemplate_MianShang) getTemplate();
			if(getLevel() > bt.getFengDamage().length){
				System.out.println("[面上buff失败][配置level错误][level=" + getLevel() + "]");
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
				if(i == Buff_YuanSuShangHai.FENG){
					fengDamage = bt.getFengDamage()[getLevel()] / 100;
					p.setWindDefenceC(p.getWindDefenceC() + fengDamage);
				} else if(i == Buff_YuanSuShangHai.HUO){
					huoDamage = bt.getHuoDamage()[getLevel()];
					p.setFireDefenceC(huoDamage);
				} else if(i == Buff_YuanSuShangHai.LEI){
					leiDamage = bt.getLeiDamage()[getLevel()];
					p.setThunderDefenceC(leiDamage);
				} else if(i == Buff_YuanSuShangHai.BING){
					bingDamage = bt.getBingDamage()[getLevel()];
					p.setBlizzardDefenceC(bingDamage);
				} else if(i == WULI){
					phyDamage = bt.getPhyDamage()[getLevel()];
					p.setPhyDefenceC(phyDamage);
				} else if(i == FASHU) {
					magDamage = bt.getMagDamage()[getLevel()];
					p.setMagicDefenceC(magDamage);
				} else {
					TransitRobberyManager.logger.error("[加免伤buff2]-出错！！！！");
				}
			}
		} else if(owner instanceof Sprite){
			Sprite p = (Sprite) owner;
			BuffTemplate_MianShang bt = (BuffTemplate_MianShang) getTemplate();
			if(getLevel() > bt.getFengDamage().length){
				System.out.println("[面上buff失败][配置level错误][level=" + getLevel() + "]");
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
				if(i == Buff_YuanSuShangHai.FENG){
					fengDamage = bt.getFengDamage()[getLevel()];
					p.setWindDefenceC(p.getWindDefenceC() + fengDamage);
				} else if(i == Buff_YuanSuShangHai.HUO){
					huoDamage = bt.getHuoDamage()[getLevel()];
					p.setFireDefenceC(p.getFireDefenceC() + huoDamage);
				} else if(i == Buff_YuanSuShangHai.LEI){
					leiDamage = bt.getLeiDamage()[getLevel()];
					p.setThunderDefenceC(p.getThunderDefenceC() + leiDamage);
				} else if(i == Buff_YuanSuShangHai.BING){
					bingDamage = bt.getBingDamage()[getLevel()];
					p.setBlizzardDefenceC(p.getBlizzardDefenceC() + bingDamage);
				} else if(i == WULI){
					phyDamage = bt.getPhyDamage()[getLevel()];
					p.setPhyDefenceC(p.getPhyDefenceC() + phyDamage);
				} else if(i == FASHU) {
					magDamage = bt.getMagDamage()[getLevel()];
					p.setMagicDefenceC(p.getMagicDefenceC() + magDamage);
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
			Player p = (Player) owner;
			p.setWindDefenceC(p.getWindDefenceC() - fengDamage);
			p.setFireDefenceC(p.getFireDefenceC() - huoDamage);
			p.setThunderDefenceC(p.getThunderDefenceC() - leiDamage);
			p.setBlizzardDefenceC(p.getBlizzardDefenceC() - bingDamage);
			p.setPhyDefenceC(p.getPhyDefenceC() - phyDamage);
			p.setMagicDefenceC(p.getMagicDefenceC() - magDamage);
		} else if(owner instanceof Sprite){
			Sprite p = (Sprite) owner;
			p.setWindDefenceC(p.getWindDefenceC() - fengDamage);
			p.setFireDefenceC(p.getFireDefenceC() - huoDamage);
			p.setThunderDefenceC(p.getThunderDefenceC() - leiDamage);
			p.setBlizzardDefenceC(p.getBlizzardDefenceC() - bingDamage);
			p.setPhyDefenceC(p.getPhyDefenceC() - phyDamage);
			p.setMagicDefenceC(p.getMagicDefenceC() - magDamage);
		}
	}
}
