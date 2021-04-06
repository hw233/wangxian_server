package com.fy.engineserver.menu;

import com.fy.engineserver.compensation.CompensationManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.text.StringUtil;

public class Option_CompensatePlayer extends Option {
	public void doSelect(Game game, Player player) {
		CompensationManager cm = CompensationManager.getInstance();
		
		if(cm.isPlayerCompensated(player.getId())){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_5158);
			player.addMessageToRightBag(hreq);
			
			return;
		}
		
		if(!cm.hasConsumeRecord(player.getId())){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_5163);
			player.addMessageToRightBag(hreq);
			
			return;
		}
		
		int yuanBaoCount = cm.getCompensation(player.getId());
		
		if(yuanBaoCount <= 0){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_5164);
			player.addMessageToRightBag(hreq);
			
			return;
		}
		
		WindowManager wm = WindowManager.getInstance();
		MenuWindow w = wm.createTempMenuWindow(300);
		
		w.setTitle(Translate.text_5165);

		StringBuffer sb = new StringBuffer();
		sb.append(Translate.text_5166+0x00ff00+"]"+yuanBaoCount+Translate.text_5167);
		String ss[] = cm.getConsumeRecords(player.getId());
		for(int i = 0 ; i < ss.length ; i++){
			sb.append(ss[i]+"\n");
		}
		w.setDescriptionInUUB(sb.toString());
		
		Option ops[] = new Option[2];
		Option_DoCompensatePlayer oc = new Option_DoCompensatePlayer();
		ops[0] = oc;
		oc.setText(Translate.text_5168);
		oc.setOptionId(StringUtil.randomString(10));
		oc.setColor(0xffffff);
		oc.setIconId("155");
		
		ops[1] = new Option();
		ops[1].setOType(Option.OPTION_TYPR_CLIENT_FUNCTION);
		ops[1].setOptionId(OptionConstants.CLIENT_FUNCTION_CLOSE+"");
		ops[1].setText(Translate.text_2901);
		ops[1].setColor(0xffffff);
		ops[1].setIconId("172");
		
		w.setOptions(ops);
		
		QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(),w,w.getOptions());
		player.addMessageToRightBag(res);
	}
	
	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_补偿玩家;
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
