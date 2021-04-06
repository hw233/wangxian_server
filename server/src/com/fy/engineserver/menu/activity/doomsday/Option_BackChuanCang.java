package com.fy.engineserver.menu.activity.doomsday;

import java.util.Random;

import com.fy.engineserver.activity.doomsday.DoomsdayManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.datasource.article.manager.ArticleManager.BindType;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

public class Option_BackChuanCang extends Option {

	public Option_BackChuanCang() {
		
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public void doSelect(Game game, Player player) {
		if (!DoomsdayManager.getInstance().isDoomsdayBoatBornStart()) {
			player.sendError("船还未造好。");
			return;
		}
		if (player.getCurrentGame() != null) {
			String mapName = DoomsdayManager.getInstance().getBoatHoldMapName(player);
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
			DoomsdayManager.logger.warn(DoomsdayManager.doomsdayLoggerHead + " [玩家从贵宾室进入船舱] " + player.getLogString());
		}
	}
	
}
