package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.RequestBuySubSystem;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.thirdpart.migu.MiGuTradeServiceWorker;
import com.fy.engineserver.message.GET_PINGTAI_PARAM_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.society.Relation;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.unite.UniteManager;
import com.fy.boss.authorize.model.Passport;
import com.fy.boss.client.BossClientService;
import com.xuanzhi.boss.game.GameConstants;

/**
 * 打开米谷交易助手时
 * @author Administrator
 *
 */
public class Option_Migu_Get extends Option implements NeedCheckPurview{

	public void doSelect(Game game,Player player){
			String op = "NEED_TOKEN";
			String[] realResults = new String[1];
			realResults[0] = op;
			if(RequestBuySubSystem.logger.isInfoEnabled())
			{
				RequestBuySubSystem.logger.info("[平台需要获取参数] [成功] [ok] ["+op+"] ["+realResults.length+"]");
			}
			GET_PINGTAI_PARAM_RES res = new GET_PINGTAI_PARAM_RES(GameMessageFactory.nextSequnceNum(), realResults);
			player.addMessageToRightBag(res);
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		Passport passport = player.getPassport();
		if(passport == null)
		{
			return false;
		}
		return MiGuTradeServiceWorker.isOpenMigu(passport.getLastLoginChannel(), player) ;
	}

}
