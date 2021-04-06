package com.fy.engineserver.menu.marriage;

import com.fy.engineserver.activity.xianling.XianLingChallengeManager;
import com.fy.engineserver.activity.xianling.XianLingManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.marriage.MarriageInfo;
import com.fy.engineserver.marriage.manager.MarriageManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

public class Option_joinHunLi extends Option{

	MarriageInfo info;
	public Option_joinHunLi(MarriageInfo info){
		super();
		this.info = info;
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public void doSelect(Game game,Player player){
		try {
			XianLingChallengeManager.getInstance().deleteXLChallenge(player);
		} catch (Exception e) {
			if(XianLingManager.logger.isErrorEnabled()) XianLingManager.logger.error("[仙灵] [副本中传送去婚礼] [异常]", e);
		}
		MarriageManager.getInstance().option_joinHunli(player, info);
	}
}
