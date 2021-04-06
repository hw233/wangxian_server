package com.fy.engineserver.menu.transitrobbery;

import java.util.Random;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntity;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.minigame.MinigameConstant;
import com.fy.engineserver.sprite.Player;

public class Option_FeiSheng extends Option implements NeedCheckPurview{
	
	private int seeType;
	
	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		String str = TransitRobberyManager.getInstance().feishengPanduan(player);
		if(str == null){
			confirmFeiSheng(player);
		} else if(Translate.已经飞升过了.equals(str)){
			传送到天界(player);
		} else {
			player.sendError(str);
		}
	}
	
	private void 传送到天界(Player player){
		GameInfo gi = GameManager.getInstance().getGameInfo("wanhuaxiangu");
		Game currentGame = player.getCurrentGame();
		if(gi == null){
			TransitRobberyManager.logger.error("[传送][取得飞升gi出错==========" + player.getLogString() + "]");
			return;
		}
		if(currentGame == null){
			TransitRobberyManager.logger.error("[传送][玩家当前地图未空==========" + player.getLogString() + "]");
			return;
		}
		MapArea area = gi.getMapAreaByName(Translate.出生点);
		int bornX = 300;
		int bornY = 300;
		if (area != null) {
			Random random = new Random();
			bornX = area.getX() + random.nextInt(area.getWidth());
			bornY = area.getY() + random.nextInt(area.getHeight());
		}
		currentGame.transferGame(player, new TransportData(0, 0, 0, 0, "wanhuaxiangu", bornX, bornY));
	}
	
	private void confirmFeiSheng(Player player){
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		mw.setDescriptionInUUB(Translate.飞升确认);
		Option_Confirm_FeiSheng option1 = new Option_Confirm_FeiSheng();
		option1.setText(Translate.确定);
		Option_Cancel option2 = new Option_Cancel();
		Option[] options = new Option[] {option1, option2};
		mw.setOptions(options);
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		player.addMessageToRightBag(creq);
	}

	@Override
	public boolean canSee(Player player) {
		TransitRobberyEntity entity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(player.getId());
		if (this.seeType == 1) {
			if (entity != null && entity.getFeisheng() == 1) {
				return false;
			}
		} else {
			if (entity == null || entity.getFeisheng() != 1) {
				return false;
			}
		}
		// TODO Auto-generated method stub
//		if(TransitRobberyManager.getInstance().feishengPanduan(player) != null){
//			return false;
//		}
		return true;
	}

	public int getSeeType() {
		return seeType;
	}

	public void setSeeType(int seeType) {
		this.seeType = seeType;
	}
}
