package com.fy.engineserver.menu.hunshi;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.hunshi.HunshiManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HUNSHI_JIANDING2_QINGQIU_RES;
import com.fy.engineserver.sprite.Player;

public class Option_Hunshi_Jianding2_Open extends Option implements NeedCheckPurview{
	@Override
	public void doSelect(Game game, Player player) {
		HunshiManager hm = HunshiManager.getInstance();
		if (hm == null) return;
		String 材料 = "";
		Article a = ArticleManager.getInstance().getArticle(hm.魂石洗炼符);
		if (a != null) {
			int colorValue = ArticleManager.getColorValue(a, a.getColorType());
			材料 = 材料 + Translate.translateString(Translate.带颜色的道具, new String[][] { { Translate.STRING_1, "" + colorValue }, { Translate.STRING_2, "[" + hm.魂石洗炼符 + "]" } });
		}
		String 魂石神隐洗炼提示 = Translate.translateString(Translate.魂石神隐洗炼提示, new String[][] { { Translate.STRING_1, 材料 } });
		player.addMessageToRightBag(new HUNSHI_JIANDING2_QINGQIU_RES(GameMessageFactory.nextSequnceNum(), 魂石神隐洗炼提示));
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		if (player.getLevel() < 151) {
			return false;
		}
		return true;
	}
}
