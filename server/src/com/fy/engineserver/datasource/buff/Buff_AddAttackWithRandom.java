package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;
/**
 * 
 *
 */
@SimpleEmbeddable
public class Buff_AddAttackWithRandom extends Buff{
	private int phyAttackY;
	
	private int magicAttackY;
	
	@Override
	public void start(Fighter owner) {
		// TODO Auto-generated method stub
		if (owner instanceof Player) {
			Player p = (Player) owner;
			if (magicAttackY <= 0 && phyAttackY <= 0) {				//此buff有可能会是程序直接赋值属性
				BuffTemplate_addAttackWithRandom template = (BuffTemplate_addAttackWithRandom) this.getTemplate();
				if (template.getMaxMagicAttackYs()[this.getLevel()] > 0) {
					int min = template.getMinMagicAttackYs()[this.getLevel()];
					int max = template.getMaxMagicAttackYs()[this.getLevel()];
					int ran = 0;
					if (max > min) {
						ran = p.random.nextInt((max-min));
					}
					magicAttackY = min + ran;
				}
				if (template.getMaxPhyAttackYs()[this.getLevel()] > 0) {
					int min = template.getMinPhyAttackYs()[this.getLevel()];
					int max = template.getMaxPhyAttackYs()[this.getLevel()];
					int ran = 0;
					if (max > min) {
						ran = p.random.nextInt((max-min));
					}
					phyAttackY = min + ran;
				}
			}
			p.setPhyAttackY(p.getPhyAttackY() + phyAttackY);
			p.setMagicAttackY(p.getMagicAttackY() + magicAttackY);
			StringBuffer sb = new StringBuffer();
			if (phyAttackY > 0) {
				sb.append(String.format(Translate.增加物攻比例, phyAttackY + ""));
			}
			if (magicAttackY > 0) {
				sb.append(String.format(Translate.增加物攻比例, magicAttackY + ""));
			}
			this.setDescription(sb.toString());
			p.setDirty(true, "buffs");
		}
	}

	@Override
	public void end(Fighter owner) {
		// TODO Auto-generated method stub
		if (owner instanceof Player) {
			Player p = (Player) owner;
			p.setPhyAttackY(p.getPhyAttackY() - phyAttackY);
			p.setMagicAttackY(p.getMagicAttackY() - magicAttackY);
		}
	}

	@Override
	public void heartbeat(Fighter owner, long heartBeatStartTime, long interval, Game game) {
		// TODO Auto-generated method stub
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	}

	public int getPhyAttackY() {
		return phyAttackY;
	}

	public void setPhyAttackY(int phyAttackY) {
		this.phyAttackY = phyAttackY;
	}

	public int getMagicAttackY() {
		return magicAttackY;
	}

	public void setMagicAttackY(int magicAttackY) {
		this.magicAttackY = magicAttackY;
	}
	
	
}
