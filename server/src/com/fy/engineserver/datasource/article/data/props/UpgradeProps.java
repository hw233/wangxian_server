package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;

/**
 * 升级道具
 *
 */
public class UpgradeProps extends Props{

	/**
	 * 使用道具
	 * @param player
	 */
	public boolean use(Game game,Player player, ArticleEntity ae){
		if(!super.use(game,player,ae)){
			return false;
		}
		long exp = 0;
		exp = player.getNextLevelExp() - player.getExp();
		player.addExp(exp, ExperienceManager.ADDEXP_REASON_UPGRADE_PROPS);
		return true;
	}
	@Override
	public String canUse(Player p) {
		// TODO Auto-generated method stub
		String resultStr = super.canUse(p);
		if(resultStr == null){
			if(p.getLevel() >= ExperienceManager.getInstance().maxLevel){
				resultStr = Translate.text_3848+this.name+Translate.text_3849;
			}
		}
		return resultStr;
	}
}
