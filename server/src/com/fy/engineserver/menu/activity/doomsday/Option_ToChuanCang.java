package com.fy.engineserver.menu.activity.doomsday;

import java.util.Random;

import com.fy.engineserver.activity.doomsday.DoomsdayManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager.BindType;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

public class Option_ToChuanCang extends Option {

	public Option_ToChuanCang() {
		
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		try{
			if (!DoomsdayManager.getInstance().isDoomsdayBoatBornStart()) {
				player.sendError("船还未造好，请再造好后再登船");
				return;
			}
			//TODO:船好了，可以进入
			String mapName = DoomsdayManager.getInstance().getBoatHoldMapName(player);
			if (mapName == null || "".equals(mapName)) {
				player.sendError("数据异常，请稍后再试!");
				DoomsdayManager.logger.error("[进入船舱地图未找到] [{}] [{}]", new Object[]{player.getLogString(), mapName});
				return;
			}
			String keyName = DoomsdayManager.mapAndKeys.get(mapName);
			if (keyName == null) {
				player.sendError("数据异常,请稍后再试!");
				DoomsdayManager.logger.error(player.getLogString() + DoomsdayManager.doomsdayLoggerHead + "[进门失败] [门对应的钥匙配置不存在,map:" + mapName + "]");
				return;
			}
			Article key = ArticleManager.getInstance().getArticle(keyName);
			if (key == null) {
				player.sendError("数据异常,请稍后再试!");
				DoomsdayManager.logger.error(player.getLogString() + DoomsdayManager.doomsdayLoggerHead + "[进门失败] [门对应的钥匙物品不存在,map:" + mapName + "] [key:" + keyName + "]");
				return;
			}
			int articleNum = player.getArticleNum(keyName, key.getColorType(), BindType.BIND);
			if (articleNum <= 0) {
				player.sendError("你没有钥匙:" + keyName + ",不能进入!");
				return;
			}
			if (player.getCurrentGame() != null) {
				player.removeArticleByNameColorAndBind(keyName, key.getColorType(), BindType.BIND, "进入活动地图扣除", true);
				Game chuanCangGame = GameManager.getInstance().getGameByName(mapName, player.getCountry());
				MapArea area = chuanCangGame.gi.getMapAreaByName(Translate.出生点);
				int bornX = 300;
				int bornY = 300;
				if (area != null) {
					Random random = new Random();
					bornX = area.getX()+random.nextInt(area.getWidth());
					bornY = area.getY() + random.nextInt(area.getHeight());
				}
				TransportData transportData = new TransportData(0,0,0,0,mapName,bornX,bornY);
				player.setTransferGameCountry(player.getCountry());
				player.getCurrentGame().transferGame(player, transportData);
				DoomsdayManager.logger.warn(DoomsdayManager.doomsdayLoggerHead + " [玩家进入船舱] " + player.getLogString());
			}
		}catch(Exception e) {
			DoomsdayManager.logger.error("Option_ToChuanCang", e);
		}
	}
}
