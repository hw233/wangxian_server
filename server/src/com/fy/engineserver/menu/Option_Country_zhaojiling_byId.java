package com.fy.engineserver.menu;

import java.util.Hashtable;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.country.data.CountryManagerData;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
/**
 * 使用官员释放功能
 * 
 * 
 *
 */
public class Option_Country_zhaojiling_byId extends Option{

	long playerId;
	
	long sendTime;
	
	String mapName;
	
	public int country;
	
	int x;
	
	int y;
	
	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public long getSendTime() {
		return sendTime;
	}

	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		CountryManager cm = CountryManager.getInstance();
		if(CountryManager.logger.isDebugEnabled())
			CountryManager.logger.debug("[1] [{}] [{}] [{}] [{}] [{},{}] [{}] [{},{}]", new Object[]{player.getUsername(),player.getId(),player.getName(),mapName,x,y,player.getGame(),player.getX(),player.getY()});
		if(cm != null){
			CountryManagerData cmd = cm.getData();
			if(cmd == null){
				if(CountryManager.logger.isDebugEnabled())
					CountryManager.logger.debug("[2] [{}] [{}] [{}] [{}] [{},{}] [{}] [{},{}]", new Object[]{player.getUsername(),player.getId(),player.getName(),mapName,x,y,player.getGame(),player.getX(),player.getY()});
				return;
			}
			int count = 0;
			if(cmd.使用一次召集令召集来的人的数量Map != null){
				if(cmd.使用一次召集令召集来的人的数量Map.get(sendTime) != null){
					count = cmd.使用一次召集令召集来的人的数量Map.get(sendTime);
				}
			}else{
				cmd.使用一次召集令召集来的人的数量Map = new Hashtable<Long, Integer>();
			}
			if(count >= CountryManager.国家召集每次召集的人数){
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.人数已满不能传送);
				player.addMessageToRightBag(hreq);
				if(CountryManager.logger.isInfoEnabled())
					CountryManager.logger.info("[{}] [{}] [{}] [{}] [{}] [{},{}] [{}] [{},{}]", new Object[]{Translate.人数已满不能传送,player.getUsername(),player.getId(),player.getName(),mapName,x,y,player.getGame(),player.getX(),player.getY()});
				return;
			}
			cmd.使用一次召集令召集来的人的数量Map.put(sendTime, count+1);
			//给人加一个国王令传送状态
			BuffTemplateManager btm = BuffTemplateManager.getInstance();
			BuffTemplate buffTemplate = btm.getBuffTemplateByName(CountryManager.国王令传送状态);
			Buff buff = buffTemplate.createBuff(1);
			buff.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + 120 * 1000);
			player.placeBuff(buff);
			//传送到指定的位置
			player.setTransferGameCountry(country);
			TransportData transportData = new TransportData(0, 0, 0, 0, mapName, x, y);
			game.transferGame(player, transportData);
			if(CountryManager.logger.isDebugEnabled())
				CountryManager.logger.debug("[3] [{}] [{}] [{}] [{}] [{},{}] [{}] [{},{}]", new Object[]{player.getUsername(),player.getId(),player.getName(),mapName,x,y,player.getGame(),player.getX(),player.getY()});
		}else{
			if(CountryManager.logger.isInfoEnabled())
				CountryManager.logger.info("[4] [{}] [{}] [{}] [{}] [{},{}] [{}] [{},{}]", new Object[]{player.getUsername(),player.getId(),player.getName(),mapName,x,y,player.getGame(),player.getX(),player.getY()});
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
