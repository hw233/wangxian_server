package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;

/**
 * 简单的道具，直接用于修改属性值
 *
 */
public class TiliProps extends Props{

	/**
	 * 此简单道具要修改的属性，以及增加的值
	 * 修改都是加法运算
	 * 按照规定的顺序给玩家修改属性
	 * 数组顺序:hp,mp,exp
	 */
	protected int value;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * 使用道具
	 * @param player
	 */
	public boolean use(Game game,Player player, ArticleEntity ae){
		if(!super.use(game,player,ae)){
			return false;
		}
		player.addVitality(value);
		HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)5, Translate.translateString(Translate.使用成功您的体力值变为, new String[][]{{Translate.STRING_1,name},{Translate.COUNT_1,player.getVitality()+""}}));
		player.addMessageToRightBag(hreq);
		if(ArticleManager.logger.isDebugEnabled()){
//			ArticleManager.logger.debug("[使用道具] [简单道具] [成功] ["+player.getName()+"] ["+this.getName()+"] ["+getComment()+"] [直接改变人物属性]");
			if(ArticleManager.logger.isDebugEnabled())
				ArticleManager.logger.debug("[使用道具] [体力值道具] [成功] [{}] [{}] [{}] [直接改变人物属性]", new Object[]{player.getName(),this.getName(),getComment()});
		}
		return true;
	}

	@Override
	public String canUse(Player p) {
		// TODO Auto-generated method stub
		String resultStr = super.canUse(p);
		if(resultStr == null){
			if(p.getVitality() + value > 360){
				resultStr = Translate.您的体力值已经够用了体力宝贵不要浪费;
			}
		}
		return resultStr;
	}
}
