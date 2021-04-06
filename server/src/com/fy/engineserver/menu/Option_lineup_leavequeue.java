package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.lineup.LineupManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;

public class Option_lineup_leavequeue extends Option {
	
	public Option_lineup_leavequeue() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		
		LineupManager lum = LineupManager.getInstance();
		boolean leaved = lum.deLineup(player);
		
		if(!leaved) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0, Translate.text_5306);
			player.addMessageToRightBag(req);
		} else {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0, Translate.text_5307);
			player.addMessageToRightBag(req);
		}
		
		//以下为跨服跳地图测试，上线时去除
//		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
//		try {
//			if(player.isOnline()) {
//				DefaultCrossServerAgent.getInstance().playerEnterCrossServer(player, "太原", 10, 10);
//				if(Game.logger.isInfoEnabled()) {
//					Game.logger.info("[玩家进入跨服地图] ["+player.getId()+"] ["+player.getName()+"] [太原] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)+"ms]");
//				}
//			} else {
//				Game.logger.warn("[玩家进入跨服地图] [玩家不在线] ["+player.getId()+"] ["+player.getName()+"] [大都] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)+"ms]");
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			Game.logger.error("[玩家进入跨服地图] [异常] ["+player.getName()+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)+"ms]", e);
//		}
		
//		//一下为跨服战场测试
//		WindowManager wm = WindowManager.getInstance();
//		MenuWindow mw = wm.createTempMenuWindow(120);
//		mw.setTitle("跨服战场测试");
//		Option_Battle_list oc = new Option_Battle_list();
//		oc.setText("跨服战场测试");
//		oc.setOptionId(StringUtil.randomString(10));
//		oc.setColor(0xffffff);
//		oc.setIconId(153);
//		mw.setOptions(new Option[]{oc});
//		mw.setDescriptionInUUB("");
//		QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(),mw,mw.getOptions());
//		player.addMessageToRightBag(res);
		
		//DefaultCrossServerAgent.getInstance().playerEnterCrossServer(player, "水下奇城", 1600, 800);
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_排队进入副本选择副本;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_5114;
	}

}
