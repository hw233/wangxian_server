package com.fy.engineserver.menu.lookWindow;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.sprite.Player;

public class LookRedNameRule extends Option{

	
	private int windowId;
	
	@Override
	public void doSelect(Game game, Player player) {
		
		MenuWindow mw = WindowManager.getInstance().getWindowById(windowId);
		if(mw != null){
			QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
			player.addMessageToRightBag(res);
			if(SocialManager.logger.isInfoEnabled()){
				SocialManager.logger.info("[查看说明窗口] ["+player.getLogString()+"] [windowId:"+windowId+"]");
			}
		}else{
			SocialManager.logger.error("[查看说明窗口异常] ["+player.getLogString()+"] [windowId:"+windowId+"]");
		}
		
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public int getWindowId() {
		return windowId;
	}

	public void setWindowId(int windowId) {
		this.windowId = windowId;
	}


}

