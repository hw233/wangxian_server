package com.fy.engineserver.menu;

import com.fy.engineserver.battlefield.BattleFieldInfo;
import com.fy.engineserver.battlefield.concrete.BattleFieldManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.text.StringUtil;

public class Option_GetZhanChangList extends Option {
	public void doSelect(Game game, Player player) {
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(120);
		mw.setTitle(Translate.text_4918);

		BattleFieldManager dfm = BattleFieldManager.getInstance();
		BattleFieldInfo[] is = dfm.getBattleFieldInfos();
		
		Option ops[] = new Option[is.length * 2 + 1];
		
		for (int i = 0; i < is.length; i++) {
			BattleFieldInfo df = is[i];
			
			//加入A方
			Option_ChooseAndEnterZhanChang oc = new Option_ChooseAndEnterZhanChang();
			ops[2 * i] = oc;
			oc.setText(df.getName() + Translate.text_5262);
			oc.setZhanChangName(df.getName());
			oc.setSide((byte)1);
			
			oc.setOptionId(StringUtil.randomString(10));
			oc.setColor(0xffffff);

			
			//加入B方			
			oc = new Option_ChooseAndEnterZhanChang();
			ops[2 * i + 1] = oc;
			oc.setText(df.getName() + Translate.text_5263);
			oc.setZhanChangName(df.getName());
			oc.setSide((byte)2);
			
			oc.setOptionId(StringUtil.randomString(10));
			oc.setColor(0xffffff);
			
		}
		
		ops[ops.length - 1] = new Option_Cancel();
		ops[ops.length - 1].setOptionId(StringUtil.randomString(10));
		ops[ops.length - 1].setText(Translate.text_2901);
		ops[ops.length - 1].setColor(0xffffff);
		
		
		mw.setOptions(ops);
		
		mw.setDescriptionInUUB(Translate.text_5264);
		QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(),mw,mw.getOptions());
		player.addMessageToRightBag(res);
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_获取战场列表;
	}
}
