package com.fy.engineserver.menu;

import java.util.List;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
/**
 * 申请传送
 * 
 * 
 *
 */
public class Option_Country_jianyuchuansong extends Option{

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
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.越狱看多了吧);
							player.addMessageToRightBag(hreq);
							return;
						}
					}
				}
				// 释放到指定的位置
				String mName = TransportData.getMainCityMap(player.getCountry());
				Game game1 = GameManager.getInstance().getGameByName(mName, player.getCountry());

				if (game1 == null) {
					game1 = GameManager.getInstance().getGameByName(mName, CountryManager.中立);
				}
				TransportData transportData = new TransportData(0, 0, 0, 0, mName, CountryManager.释放X坐标, CountryManager.释放Y坐标);
				if(game1 != null){
					MapArea area = game1.gi.getMapAreaByName(Translate.出生点);
					if(area != null){
						transportData = new TransportData(0, 0, 0, 0,
								mName,
								(int)(area.getX() + Math.random()*area.getWidth()),
								(int)(area.getY() + Math.random()*area.getHeight()));
					}	
				}
				if (game1 != null) {
					game1.transferGame(player, transportData);
				}
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
