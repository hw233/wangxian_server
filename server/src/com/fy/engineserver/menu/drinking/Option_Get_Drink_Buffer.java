package com.fy.engineserver.menu.drinking;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.buffsave.BuffSave;
import com.fy.engineserver.buffsave.BufferSaveManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_UseCancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;

/**
 * 取喝酒buff
 * 
 *
 */
public class Option_Get_Drink_Buffer extends Option{
	
	@Override
	public void doSelect(Game game, Player player) {
		BufferSaveManager bsm = BufferSaveManager.getInstance();
		List<BuffSave> list = bsm.getbuff(player.getId());
		if(list.size()>0){
			BuffSave old = list.get(0);
			//过期
			boolean isend = false;
			if(old.getEndtime()<System.currentTimeMillis()){
				bsm.delbuff(old);
				isend = true;
			}
			if(isend){
				player.sendError(Translate.没有可取的酒);
				return;
			}else{
				String 存储的酒的名字 = bsm.getNameByLevel(old.getBufflevel()); 
				if(!bsm.玩家是否能使用该酒(player.getLevel(), 存储的酒的名字)){
					player.sendError(Translate.等级超过不能使用);
					return;
				}
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
					if(bsm.取酒扣钱(player, old.getBufflevel())){
						BuffTemplateManager bm = BuffTemplateManager.getInstance();
						BuffTemplate bt = bm.getBuffTemplateByName(old.getBuffname());
						Buff newbuff = bt.createBuff(old.getBufflevel());
						newbuff.setStartTime(SystemTime.currentTimeMillis());
						newbuff.setInvalidTime(SystemTime.currentTimeMillis()+old.getSavetime());
						newbuff.setLevel(old.getBufflevel());
						newbuff.setCauser(player);
						newbuff.start(player);
						player.placeBuff(newbuff);
						bsm.delbuff(old);
						bsm.log.warn("[获取buff] [ok] [直接获取] ["+newbuff+"]");
					}else{
						player.sendError(Translate.余额不足);
					}
				}else{
					WindowManager windowManager = WindowManager.getInstance();
					MenuWindow mw = windowManager.createTempMenuWindow(600);
					String 酒的名字 = bsm.getNameByLevel(buff.getLevel()); 
					String 酒的颜色 = bsm.getJiuLevel(buff.getLevel()); 
					String 要替换酒的名字 = bsm.getNameByLevel(old.getBufflevel()); 
					String 要替换酒的颜色 = bsm.getJiuLevel(old.getBufflevel()); 
					String mes = Translate.translateString(Translate.身上已经有酒buff, new String[][] {{Translate.STRING_1, 酒的颜色}, {Translate.STRING_2, 酒的名字} , {Translate.STRING_3, 要替换酒的颜色}, {Translate.STRING_4, 要替换酒的名字}});
					mw.setDescriptionInUUB(mes);
					Option_Get_Drink_Buffer_Sure option = new Option_Get_Drink_Buffer_Sure();
					option.setText(Translate.替换);
					
					Option_UseCancel oc = new Option_UseCancel();
					oc.setText(Translate.取消);
					oc.setColor(0xffffff);
					
					mw.setOptions(new Option[]{option,oc});
					QUERY_WINDOW_RES req = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
					player.addMessageToRightBag(req);	
				}
			}
		}else{
			player.sendError(Translate.没有酒可取);
		}
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

}
