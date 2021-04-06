package com.fy.engineserver.menu.activity.doomsday;

import java.util.ArrayList;

import com.fy.engineserver.activity.doomsday.DoomsdayManager;
import com.fy.engineserver.activity.doomsday.DoomsdayManager.ExchageData;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;

/**
 * 选特定道具然后出1 10 100的兑换比例
 * 
 *
 */
public class Option_ExchargeShowProp extends Option {

	private String requestPropName;// 要消耗的物品名字
	
	public Option_ExchargeShowProp() {
		
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		try{
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
			mw.setTitle("捐献" + requestPropName);
			mw.setDescriptionInUUB("捐献<f color='#1da2dd'> 1个" + requestPropName + "</f>可增加<f color='#f7e354'>本国方舟"+requestPropName+"的成长值</f>捐献的同时并可获得<f color='#f7e354'>贡献卡1张</f>，捐献获取比例<f color='#f7e354'> 1:1</f>。同时捐献三种材料各1次，累计1点方舟成长值。");
			ArrayList<Option> options = new ArrayList<Option>();
			for (ExchageData exchageData : DoomsdayManager.materialExchageDatas) {
				if (exchageData.getRequestPropName().equals(requestPropName)) {
					Option_ExchargeCard card = new Option_ExchargeCard(requestPropName, exchageData.getRequestPropNum());
					card.setText("捐献" + exchageData.getRequestPropNum() + "个" + requestPropName);
					options.add(card);
				}
			}
			Option[] ops = options.toArray(new Option[0]);
			mw.setOptions(ops);
			CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
			player.addMessageToRightBag(creq);
		}catch(Exception e) {
			DoomsdayManager.logger.error("Option_ExchargeShowProp", e);
		}
	}

	public void setRequestPropName(String requestPropName) {
		this.requestPropName = requestPropName;
	}

	public String getRequestPropName() {
		return requestPropName;
	}
}
