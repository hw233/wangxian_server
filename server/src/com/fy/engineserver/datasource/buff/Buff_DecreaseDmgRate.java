package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.articleEnchant.EnchantManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;
/**
 * 与法攻有关的中毒伤害（计算目标法防） 并且附加中毒状态，根据此状态兽魁有些技能需要特殊处理
 * @author Administrator
 *
 */
@SimpleEmbeddable
public class Buff_DecreaseDmgRate extends Buff{
	
	private int decreaseRate;
	
	@Override
	public void start(Fighter owner) {
		// TODO Auto-generated method stub
		if (owner instanceof Player) {
			Player p = (Player) owner;
			if (decreaseRate <= 0) {
				BuffTemplate_DecreaseDmgRate template = (BuffTemplate_DecreaseDmgRate) this.getTemplate();
				decreaseRate = template.getDecreaseRate()[this.getLevel()];
			}
			if (p.decreaseDmgRate > 0) {
				if (EnchantManager.logger.isInfoEnabled()) {
					try {
						throw new Exception();
					} catch (Exception e) {
						EnchantManager.logger.info("[" + this.getClass().getSimpleName() + "] [" + p.getLogString() + "] [p:" + p.decreaseDmgRate + "]", e);
					}
				}
			}
			p.decreaseDmgRate = decreaseRate;
			p.setDirty(true, "buffs");
		}
	}

	@Override
	public void end(Fighter owner) {
		// TODO Auto-generated method stub
		if (owner instanceof Player) {
			Player p = (Player) owner;
			p.decreaseDmgRate = 0;
			if (decreaseRate <= 0) {
				if (EnchantManager.logger.isInfoEnabled()) {
					EnchantManager.logger.info("[" + this.getClass().getSimpleName() + "] [" + p.getLogString() + "] [buff结束时记录数值错误:" + decreaseRate + "]");
				}
			}
		}
	}

	@Override
	public void heartbeat(Fighter owner, long heartBeatStartTime, long interval, Game game) {
		// TODO Auto-generated method stub
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	}

	public int getDecreaseRate() {
		return decreaseRate;
	}

	public void setDecreaseRate(int decreaseRate) {
		this.decreaseRate = decreaseRate;
	}

	
}
