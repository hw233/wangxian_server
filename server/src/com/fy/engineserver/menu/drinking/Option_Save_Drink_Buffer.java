package com.fy.engineserver.menu.drinking;

import java.util.*;

import com.fy.engineserver.buffsave.BuffSave;
import com.fy.engineserver.buffsave.BufferSaveManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.Option_UseCancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;

/**
 * 存喝酒buff
 * 
 *
 */
public class Option_Save_Drink_Buffer extends Option{

	
	@Override
	public void doSelect(Game game, Player player) {
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
		
		BufferSaveManager bsm = BufferSaveManager.getInstance();
		List<BuffSave> list = bsm.getbuff(player.getId());
		if(list.size()>0){
			BuffSave old = list.get(0);
			//过期处理
			boolean isend = false;
			if(old.getEndtime()<System.currentTimeMillis()){
				bsm.delbuff(old);
				isend = true;
				player.sendError(Translate.删除已过期的buff);
			}
			if(isend){
				long savetime = buff.getInvalidTime()-System.currentTimeMillis();
				String 剩余时间 = savetime/(60*1000)+Translate.分钟+savetime/1000%60+Translate.text_49;
				String 酒的名字 = bsm.getNameByLevel(buff.getLevel()); 
				String 酒的颜色 = bsm.getJiuLevel(buff.getLevel()); 	
				long 消耗的钱 = bsm.取酒需消耗的钱(buff.getLevel());
				WindowManager windowManager = WindowManager.getInstance();
				MenuWindow mw = windowManager.createTempMenuWindow(600);
				String mes = Translate.translateString(Translate.存酒成功描述, new String[][] {{Translate.STRING_1, 酒的颜色}, {Translate.STRING_2, 酒的名字} , {Translate.STRING_3, 剩余时间}, {Translate.STRING_4, 消耗的钱+""}});
				mw.setDescriptionInUUB(mes);
				Option_Save_Drink_Buff_Cost_Sure oc = new Option_Save_Drink_Buff_Cost_Sure();
				oc.setText(Translate.确定);
				oc.setColor(0xffffff);
				Option_Cancel oc2 = new Option_Cancel();
				oc2.setText(Translate.取消);
				oc2.setColor(0xffffff);
				mw.setOptions(new Option[]{oc,oc2});
				QUERY_WINDOW_RES req = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
				player.addMessageToRightBag(req);
			}else{
				long savetime = old.getSavetime();
				String 剩余时间 = savetime/(60*1000)+Translate.分钟+savetime/1000%60+Translate.text_49;
				String 酒的名字 = bsm.getNameByLevel(old.getBufflevel()); 
				String 酒的颜色 = bsm.getJiuLevel(old.getBufflevel()); 
				String 要替换酒的名字 = bsm.getNameByLevel(buff.getLevel()); 
				String 要替换酒的颜色 = bsm.getJiuLevel(buff.getLevel()); 
				WindowManager windowManager = WindowManager.getInstance();
				MenuWindow mw = windowManager.createTempMenuWindow(600);
				String mes = Translate.translateString(Translate.您之前已经存储过, new String[][] {{Translate.STRING_1, 酒的颜色}, {Translate.STRING_2, 酒的名字} , {Translate.STRING_3, 剩余时间}, {Translate.STRING_4, 要替换酒的颜色},{Translate.STRING_5, 要替换酒的名字}});
				mw.setDescriptionInUUB(mes);
				Option_Save_Drink_Buffer_Sure option = new Option_Save_Drink_Buffer_Sure();
				option.setText(Translate.替换);
				
				Option_UseCancel oc = new Option_UseCancel();
				oc.setText(Translate.取消);
				oc.setColor(0xffffff);
				
				mw.setOptions(new Option[]{option,oc});
				QUERY_WINDOW_RES req = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
				player.addMessageToRightBag(req);
			}
			
		}else{
			if(buff!=null){
				long savetime = buff.getInvalidTime()-System.currentTimeMillis();
				String 剩余时间 = savetime/(60*1000)+Translate.分钟+savetime/1000%60+Translate.text_49;
				String 酒的名字 = bsm.getNameByLevel(buff.getLevel()); 
				String 酒的颜色 = bsm.getJiuLevel(buff.getLevel()); 	
				long 消耗的钱 = bsm.取酒需消耗的钱(buff.getLevel());
				WindowManager windowManager = WindowManager.getInstance();
				MenuWindow mw = windowManager.createTempMenuWindow(600);
				String mes = Translate.translateString(Translate.存酒成功描述, new String[][] {{Translate.STRING_1, 酒的颜色}, {Translate.STRING_2, 酒的名字} , {Translate.STRING_3, 剩余时间}, {Translate.STRING_4, 消耗的钱+""}});
				mw.setDescriptionInUUB(mes);
				Option_Save_Drink_Buff_Cost_Sure oc = new Option_Save_Drink_Buff_Cost_Sure();
				oc.setText(Translate.确定);
				oc.setColor(0xffffff);
				Option_Cancel oc2 = new Option_Cancel();
				oc2.setText(Translate.取消);
				oc2.setColor(0xffffff);
				mw.setOptions(new Option[]{oc,oc2});
				QUERY_WINDOW_RES req = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
				player.addMessageToRightBag(req);
			}
		}
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
}
