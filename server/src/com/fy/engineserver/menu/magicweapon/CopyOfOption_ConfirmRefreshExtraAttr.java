package com.fy.engineserver.menu.magicweapon;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.NewMagicWeaponEntity;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponManager;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.RESET_MAGICWEAPON_EXTRAATTR_RES;
import com.fy.engineserver.sprite.Player;

public class CopyOfOption_ConfirmRefreshExtraAttr extends Option{
	
	/** 法宝id */
	private long articleId;
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public void doSelect(Game game, Player player) {
		String str = MagicWeaponManager.instance.refreshExtraAttr(player, articleId, true);
		try {
			ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(articleId);
			String str2 = ((NewMagicWeaponEntity)ae).getExtraAttrDes(player);
			if (str == null) {
				RESET_MAGICWEAPON_EXTRAATTR_RES resp = new RESET_MAGICWEAPON_EXTRAATTR_RES(GameMessageFactory.nextSequnceNum(), MagicWeaponManager.getRefreshCost(((NewMagicWeaponEntity)ae).getIllusionLevel()), str2);
				player.addMessageToRightBag(resp);
			}
		} catch (Exception e) {
			MagicWeaponManager.logger.warn("[刷法宝附加属性] [异常] [" + player.getLogString() + "] [aeId:" + articleId + "]", e);
		}
	}

	public long getArticleId() {
		return articleId;
	}

	public void setArticleId(long articleId) {
		this.articleId = articleId;
	}
}
