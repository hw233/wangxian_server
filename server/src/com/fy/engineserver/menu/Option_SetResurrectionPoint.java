package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;

/**
 * 设置复活点
 * 
 * 
 *
 */
public class Option_SetResurrectionPoint extends Option{

	Game oldGame;
	int oldX;
	int oldY;
	/**
	 * 为玩家copy一个选项出来，特殊的选项可以重载此方法
	 * 
	 * 如果传送，修改，治疗，可以根据玩家的具体信息来追加要花多少钱
	 * 
	 * 此方法是为了保留创建是的环境，
	 * 以便在doselect或者doinput方法中检查。
	 * 
	 * 如果不保留环境，很容易被外挂利用。
	 * 比如外挂在副本中，直接发送医生补血选项的窗口编号和选项编号。
	 * 如果不做任何检查，很可能将使用外挂的人血回满。
	 * 
	 * @param p
	 * @return
	 */
	public Option copy(Game game,Player p){
		Option_SetResurrectionPoint op = new Option_SetResurrectionPoint();
		copy(op);
		op.oldGame = game;
		op.oldX =(int) p.getX();
		op.oldY = (int)p.getY();
		
		return op;
	}
	

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		if(oldGame != null){
			player.setResurrectionMapName(oldGame.getGameInfo().getName());
			player.setResurrectionX((short)oldX);
			player.setResurrectionY((short)oldY);
		}
		
		HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)1,Translate.text_5422);
		player.addMessageToRightBag(hreq);
	}
	

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_设置复活点;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return "" ;
	}
}
