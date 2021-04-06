package com.fy.engineserver.menu.activity;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;

public class OfOption_SaveDrink_Help extends Option{

	public OfOption_SaveDrink_Help() {
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public void doSelect(Game game, Player player) {
			String title = "聚灵阵存取功能说明";
			String des = "<f color='0xff0000'>存酒功能：</f>\n"
+"1、存放喝酒BUFF只能存放当前一瓶酒的BUFF，不能叠加，每次存酒都是免费的。\n"
+"2、如储存的酒BUFF超过人物使用等级，将无法领取存储的喝酒BUFF。\n"
+"3、存放的酒BUFF有效期为3天。\n"
+"<f color='0xff0000'>取酒功能：</f>\n"
+"1、身上有喝酒BUFF，如在聚灵阵领取已存的喝酒BUFF，身上的BUFF将会被替换。\n"
+"2、取酒时收取的费用，按白、绿、蓝、紫、橙来收取，分别是10两、20两、30两、40两、50两绑银。\n";
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
			mw.setDescriptionInUUB(des);
			mw.setTitle(title);
			Option_Cancel oc = new Option_Cancel();
			oc.setText("确定");
			mw.setOptions(new Option[]{oc});
			QUERY_WINDOW_RES req = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
			player.addMessageToRightBag(req);
	}
}
