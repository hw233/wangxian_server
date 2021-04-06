package com.fy.engineserver.guozhan.menu;

import java.util.Random;

import com.fy.engineserver.activity.xianling.XianLingChallengeManager;
import com.fy.engineserver.activity.xianling.XianLingManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.guozhan.Guozhan;
import com.fy.engineserver.guozhan.GuozhanBornPoint;
import com.fy.engineserver.guozhan.GuozhanOrganizer;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

/**
 * 玩家同意到出生点参与国战
 * 
 *
 */
public class Option_Guozhan_Pull_OK extends Option {
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		long start = System.currentTimeMillis();
		try {
			XianLingChallengeManager.getInstance().deleteXLChallenge(player);
		} catch (Exception e) {
			if(XianLingManager.logger.isErrorEnabled()) XianLingManager.logger.error("[仙灵] [副本中传送去国战] [异常]", e);
		}
		Guozhan guozhan = GuozhanOrganizer.getInstance().getPlayerGuozhan(player);
		if(guozhan != null && !player.isInPrison()) {
			GuozhanBornPoint bp = guozhan.getBornPoint(player);
			player.setTransferGameCountry(bp.getMapCountry());
			int x = bp.getX();
			int y = bp.getY();
			int ranX = new Random().nextInt(30);
			int ranY = new Random().nextInt(30);
			ranX -= 15;
			ranY -= 15;
			x += ranX;
			y += ranY;
			game.transferGame(player, new TransportData(0, 0, 0, 0, bp.getMapName(), x, y));
			player.setLastGame(bp.getMapName());
			player.setLastX(player.getX());
			player.setLastY(player.getY());
			player.setGame(bp.getMapName());
			player.setX(x);
			player.setY(y);
			if(GuozhanOrganizer.logger.isDebugEnabled())
				GuozhanOrganizer.logger.debug("[玩家接受召唤] ["+player.getLogString()+"] [出生点:"+bp.getLogStr()+"] ["+(System.currentTimeMillis()-start)+"ms]");
		}
		if(GuozhanOrganizer.logger.isDebugEnabled())
			GuozhanOrganizer.logger.debug("[玩家接受召唤失败：玩家不在国战中] ["+player.getLogString()+"] ["+(System.currentTimeMillis()-start)+"ms]");
	}

}
