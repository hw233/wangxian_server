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
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

/**
 * 取喝酒buff
 * 
 *
 */
public class Option_Get_Drink_Buffer_Sure extends Option{
	
	@Override
	public void doSelect(Game game, Player player) {
		BufferSaveManager bsm = BufferSaveManager.getInstance();
		List<BuffSave> list = bsm.getbuff(player.getId());
		ArrayList<Buff> buffs = player.getBuffs();
		//要存储的buff
		Buff buff = null;
		for(Buff b : buffs){ 
			if(b.getTemplateName().equals(Translate.喝酒经验)){
				buff = b;
				break;
			}
		}
		
		if(buff!=null){
			if(list.size()>0){
				BuffSave old = list.get(0);
				if(bsm.取酒扣钱(player, old.getBufflevel())){
					player.removeBuff(buff);
					BuffTemplateManager bm = BuffTemplateManager.getInstance();
					BuffTemplate bt = bm.getBuffTemplateByName(old.getBuffname());
					Buff newbuff = bt.createBuff(old.getBufflevel());
					newbuff.setStartTime(SystemTime.currentTimeMillis());
					newbuff.setInvalidTime(SystemTime.currentTimeMillis()+old.getSavetime());
					newbuff.setCauser(player);
					newbuff.setLevel(old.getBufflevel());
					player.placeBuff(newbuff);
					newbuff.start(player);
					bsm.delbuff(old);
					bsm.log.warn("[获取buff] [ok] [buff替换] ["+newbuff+"]");
					String 酒的颜色 = bsm.getJiuLevel(newbuff.getLevel()); 
					String 酒的名字 = bsm.getNameByLevel(newbuff.getLevel()); 
					String mes = Translate.translateString(Translate.替换成功, new String[][] {{Translate.STRING_1, 酒的颜色}, {Translate.STRING_2, 酒的名字}});
					player.sendError(mes);
				}else{
					player.sendError(Translate.余额不足);
				}
			}
		}
		
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

}
