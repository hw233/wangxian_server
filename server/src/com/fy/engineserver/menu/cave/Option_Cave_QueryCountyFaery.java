package com.fy.engineserver.menu.cave;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.CAVE_OPEN_COUNTYLIST_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;

public class Option_Cave_QueryCountyFaery extends CaveOption implements FaeryConfig ,NeedCheckPurview{

	public Option_Cave_QueryCountyFaery() {

	}

	@Override
	public void doSelect(Game game, Player player) {

		// String[] 国家名称 = CountryManager.国家名称;
		//
		// byte[] 国家index = new byte[国家名称.length];
		// for (byte i = CountryManager.国家A; i <= CountryManager.国家C; i++) {
		// 国家index[i] = (byte) (i + 1);
		// }
		CAVE_OPEN_COUNTYLIST_RES res = new CAVE_OPEN_COUNTYLIST_RES(GameMessageFactory.nextSequnceNum(), CountryManager.国BYTE,  CountryManager.国家名称);
		player.addMessageToRightBag(res);

	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public boolean canSee(Player player) {
		if (player.getCaveId() <= 0) {
			return false;
		}
		Cave cave = FaeryManager.getInstance().getCave(player);
		if (cave == null) {
			 return false;
		}
		return true;
	}
}
