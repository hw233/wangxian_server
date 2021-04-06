package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;

/**
 * 简单的道具，直接用于修改属性值
 * 
 */
public class ClearRedProps extends Props {

	/**
	 * 此简单道具要修改的属性，以及增加的值
	 * 修改都是加法运算
	 * 按照规定的顺序给玩家修改属性
	 * 数组顺序:hp,mp,exp
	 */
	protected double value;

	private int type;
	private int limit;

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * 使用道具
	 * @param player
	 */
	public boolean use(Game game, Player player, ArticleEntity ae) {
		if (!super.use(game, player, ae)) {
			return false;
		}

		if (player.getEvil() > 0) {
			reduceEvil(player);
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.translateString(Translate.使用成功您剩余罪恶值, new String[][] { { Translate.STRING_1, name }, { Translate.COUNT_1, player.getEvil() + "" } }));
			player.addMessageToRightBag(hreq);
		}
		if (ArticleManager.logger.isDebugEnabled()) {
			// ArticleManager.logger.debug("[使用道具] [简单道具] [成功] ["+player.getName()+"] ["+this.getName()+"] ["+getComment()+"] [直接改变人物属性]");
			if (ArticleManager.logger.isDebugEnabled()) ArticleManager.logger.debug("[使用道具] [清红名道具] [成功] [{}] [{}] [{}] [直接改变人物属性]", new Object[] { player.getName(), this.getName(), getComment() });
		}
		return true;
	}

	public void reduceEvil(Player player) {
		// TODO
		int reduce = 0;
		switch (type) {
		case 0:
			reduce = (int) this.value;
			break;
		case 1:
			reduce = (int) (player.getEvil() * this.value);
			break;
		default:
			break;
		}
		if (reduce <= 0) {
			// TODO log
			if (ArticleManager.logger.isInfoEnabled()) ArticleManager.logger.info(player.getLogString()+"[使用道具] [清红名道具] [减少红名值异常:"+ reduce +"]");
		} else {
			player.setEvil(player.getEvil() - reduce);
			player.setLastActivePkTime(SystemTime.currentTimeMillis());
			if (player.getEvil() < 0) {
				player.setEvil(0);
			}
			if (ArticleManager.logger.isInfoEnabled()) ArticleManager.logger.info(player.getLogString()+"[使用道具] [清红名道具] [减少红名值:"+ reduce +"] [当前红名值:"+ player.getEvil() +"]");
		}
	}

	@Override
	public String canUse(Player p) {
		String resultStr = super.canUse(p);
		if (resultStr == null) {
			if (this.limit == -1) {
				if (p.getEvil() <= 0) {
					resultStr = Translate.罪恶值必须大于0才能使用;
				}
			} else {
				resultStr = this.limit <= p.getEvil() ? resultStr : Translate.translateString(Translate.罪恶值必须大于X才能使用, new String[][] { { Translate.COUNT_1, String.valueOf(this.limit) } });
			}
		}
		return resultStr;
	}
}
