package com.fy.engineserver.menu.drinking;

import java.util.ArrayList;

import com.fy.engineserver.buffsave.BuffSave;
import com.fy.engineserver.buffsave.BufferSaveManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;

public class Option_Save_Drink_Buff_Cost_Sure extends Option{
	
	@Override
	public void doSelect(Game game, Player player) {
		//重新获取玩家身上的喝酒buff，防止玩家在确定的框一直停留
		BufferSaveManager bsm = BufferSaveManager.getInstance();
		ArrayList<Buff> buffs = player.getBuffs();
		//要存储的buff
		Buff buff = null;
		for(Buff b : buffs){ 
			if(b.getTemplateName().equals(Translate.喝酒经验)){
				buff = b;
				break;
			}
		}
		
		if(buff==null){
			player.sendError(Translate.没有可存储的喝酒BUFF);
			return;
		}
		
		BuffSave bs = bsm.saveBuff(player, buff);
		if(bs!=null){
			long savetime = bs.getSavetime();
			String 剩余时间 = savetime/(60*1000)+Translate.分钟+savetime/1000%60+Translate.text_49;
			String 酒的名字 = bsm.getNameByLevel(bs.getBufflevel()); 
			String 酒的颜色 = bsm.getJiuLevel(bs.getBufflevel()); 	
			WindowManager windowManager = WindowManager.getInstance();
			MenuWindow mw = windowManager.createTempMenuWindow(600);
			String mes = Translate.translateString(Translate.存酒成功描述_没有消耗提示, new String[][] {{Translate.STRING_1, 酒的颜色}, {Translate.STRING_2, 酒的名字} , {Translate.STRING_3, 剩余时间}});
			mw.setDescriptionInUUB(mes);
			Option_Cancel oc = new Option_Cancel();
			oc.setText(Translate.确定);
			oc.setColor(0xffffff);
			//删除buff效果
			player.removeBuff(buff);
			
			mw.setOptions(new Option[]{oc});
			QUERY_WINDOW_RES req = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
			player.addMessageToRightBag(req);
		}
		
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
