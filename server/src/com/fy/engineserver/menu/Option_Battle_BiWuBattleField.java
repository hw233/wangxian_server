package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;

/**
 * 玩家进比武场参观
 * 
 * 
 *
 */
public class Option_Battle_BiWuBattleField extends Option {

	/**
	 * 选择进场index
	 */
	private int selectIndex;

	public int getSelectIndex() {
		return selectIndex;
	}

	public void setSelectIndex(int selectIndex) {
		this.selectIndex = selectIndex;
	}

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		if(player == null){
			return;
		}
		GameManager gm = GameManager.getInstance();
		if(gm != null){
			TransportData transportData = null;
			if(selectIndex == 0){
				Game g = gm.getGameByName(Translate.text_1754,CountryManager.中立);
				if(g != null){
					if(g.getNumOfPlayer() >= g.gi.getMaxPlayerNum()){
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.text_5029);
						player.addMessageToRightBag(hreq);
						return;
					}
				}
				transportData = new TransportData(0,0,0,0, Translate.text_1754, 693, 369);
			}else if(selectIndex == 1){
				Game g = gm.getGameByName(Translate.text_1755,CountryManager.中立);
				if(g != null){
					if(g.getNumOfPlayer() >= g.gi.getMaxPlayerNum()){
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.text_5030);
						player.addMessageToRightBag(hreq);
						return;
					}
				}
				transportData = new TransportData(0,0,0,0, Translate.text_1755, 624, 391);
			}else if(selectIndex == 2){
				Game g = gm.getGameByName(Translate.text_1756,CountryManager.中立);
				if(g != null){
					if(g.getNumOfPlayer() >= g.gi.getMaxPlayerNum()){
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.text_5031);
						player.addMessageToRightBag(hreq);
						return;
					}
				}
				transportData = new TransportData(0,0,0,0, Translate.text_1756, 477, 303);
			}else if(selectIndex == 3){
				Game g = gm.getGameByName(Translate.text_1757,CountryManager.中立);
				if(g != null){
					if(g.getNumOfPlayer() >= g.gi.getMaxPlayerNum()){
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.text_5032);
						player.addMessageToRightBag(hreq);
						return;
					}
				}
				transportData = new TransportData(0,0,0,0, Translate.text_1757, 460, 336);
			}else if(selectIndex == 4){
				Game g = gm.getGameByName(Translate.text_1758,CountryManager.中立);
				if(g != null){
					if(g.getNumOfPlayer() >= g.gi.getMaxPlayerNum()){
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.text_5033);
						player.addMessageToRightBag(hreq);
						return;
					}
				}
				transportData = new TransportData(0,0,0,0, Translate.text_1758, 660, 300);
			}else if(selectIndex == 5){
				Game g = gm.getGameByName(Translate.text_1759,CountryManager.中立);
				if(g != null){
					if(g.getNumOfPlayer() >= g.gi.getMaxPlayerNum()){
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.text_5034);
						player.addMessageToRightBag(hreq);
						return;
					}
				}
				transportData = new TransportData(0,0,0,0, Translate.text_1759, 536, 283);
			}
			try{
				if(transportData != null){
					game.transferGame(player, transportData);			
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doInput(Game game, Player player, String inputContent){}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_比武进比武场;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_5035 ;
	}

}
