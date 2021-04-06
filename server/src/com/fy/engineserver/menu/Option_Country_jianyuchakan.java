package com.fy.engineserver.menu;

import java.util.List;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
/**
 * 查询剩余时间
 * 
 * 
 *
 */
public class Option_Country_jianyuchakan extends Option{

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		CountryManager cm = CountryManager.getInstance();
		PlayerManager pm = PlayerManager.getInstance();
		if(cm != null && pm != null){
			try{
				List<Buff> buffs = player.getAllBuffs();
				if (buffs != null) {
					for (Buff buff : buffs) {
						if (buff != null && buff.getTemplate() != null && CountryManager.囚禁buff名称.equals(buff.getTemplate().getName())) {
							long time = (buff.getInvalidTime() - SystemTime.currentTimeMillis())/(60*1000);
							String des = Translate.translateString(Translate.您的坐牢剩余时间, new String[][]{{Translate.COUNT_1,time+""}});
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, des);
							player.addMessageToRightBag(hreq);
							return;
						}
					}
				}
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.您现在可以点击离开选项离开监狱了);
				player.addMessageToRightBag(hreq);
			}catch(Exception ex){
				
			}
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_TUOYUN;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.服务器选项;
	}
}
