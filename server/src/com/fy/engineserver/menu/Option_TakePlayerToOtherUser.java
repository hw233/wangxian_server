package com.fy.engineserver.menu;


import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.INPUT_WINDOW_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;

/**
 * 领取角色到一个新账号
 * 
 * 
 *
 */
public class Option_TakePlayerToOtherUser extends Option{

	Player owner;

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}
	
	/**
	 * 确认
	 */
	public void doInput(Game game, Player player, String inputContent) {

		if(player == owner){
			PlayerManager pm = PlayerManager.getInstance();
			if(pm == null){
//				Game.logger.warn("[领取角色] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] [pm == null]");
				if(Game.logger.isWarnEnabled())
					Game.logger.warn("[领取角色] [失败] [{}] [{}] [{}] [pm == null]", new Object[]{player.getUsername(),player.getId(),player.getName()});
				return;
			}
			Player[] players = null;
			try{
				players = pm.getPlayerByUser(player.getUsername());
			}catch(Exception ex){
				ex.printStackTrace();
				if(Game.logger.isWarnEnabled())
					Game.logger.warn("[领取角色] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] [异常]",ex);
				return;
			}
			if(players == null || players.length <= 5){
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.text_5177);
				player.addMessageToRightBag(hreq);
//				Game.logger.warn("[领取角色] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] [您的账号内没有超过5个角色，不能进行角色领取操作]");
				if(Game.logger.isWarnEnabled())
					Game.logger.warn("[领取角色] [失败] [{}] [{}] [{}] [您的账号内没有超过5个角色，不能进行角色领取操作]", new Object[]{player.getUsername(),player.getId(),player.getName()});
				return;
			}
			if(inputContent == null || inputContent.trim().equals("")){
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.text_5178);
				player.addMessageToRightBag(hreq);
//				Game.logger.warn("[领取角色] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] [请输入账号]");
				if(Game.logger.isWarnEnabled())
					Game.logger.warn("[领取角色] [失败] [{}] [{}] [{}] [请输入账号]", new Object[]{player.getUsername(),player.getId(),player.getName()});
				return;
			}
			WindowManager windowManager = WindowManager.getInstance();
			if(windowManager == null){
//				Game.logger.warn("[领取角色] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] [windowManager == null]");
				if(Game.logger.isWarnEnabled())
					Game.logger.warn("[领取角色] [失败] [{}] [{}] [{}] [windowManager == null]", new Object[]{player.getUsername(),player.getId(),player.getName()});
				return;
			}
			MenuWindow mw = windowManager.createTempMenuWindow(600);
			mw.setTitle(Translate.text_4925);
			mw.setDescriptionInUUB(Translate.text_5449);
			Option_TakePlayerToOtherUserQueRen oa = new Option_TakePlayerToOtherUserQueRen();
			oa.setOwner(player);
			oa.setInputUserName(inputContent);
			oa.setText(Translate.text_5449);
			mw.setOptions(new Option_TakePlayerToOtherUserQueRen[]{oa});
			INPUT_WINDOW_REQ iwq = new INPUT_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getTitle(), mw.getDescriptionInUUB(), (byte)2, (byte)100,Translate.在这里输入, Translate.text_62, Translate.text_364, new byte[0]);
			player.addMessageToRightBag(iwq);
		}
	
	}
	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		PlayerManager pm = PlayerManager.getInstance();
		if(pm == null){
//			Game.logger.warn("[领取角色] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] [pm == null]");
			if(Game.logger.isWarnEnabled())
				Game.logger.warn("[领取角色] [失败] [{}] [{}] [{}] [pm == null]", new Object[]{player.getUsername(),player.getId(),player.getName()});
			return;
		}
		Player[] players = null;
		try{
			players = pm.getPlayerByUser(player.getUsername());
		}catch(Exception ex){
			ex.printStackTrace();
			if(Game.logger.isWarnEnabled())
				Game.logger.warn("[领取角色] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] [异常]",ex);
			return;
		}
		if(players == null || players.length <= 5){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.text_5177);
			player.addMessageToRightBag(hreq);
//			Game.logger.warn("[领取角色] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] [您的账号内没有超过5个角色，不能进行角色领取操作]");
			if(Game.logger.isWarnEnabled())
				Game.logger.warn("[领取角色] [失败] [{}] [{}] [{}] [您的账号内没有超过5个角色，不能进行角色领取操作]", new Object[]{player.getUsername(),player.getId(),player.getName()});
			return;
		}
		WindowManager windowManager = WindowManager.getInstance();
		if(windowManager == null){
//			Game.logger.warn("[领取角色] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] [windowManager == null]");
			if(Game.logger.isWarnEnabled())
				Game.logger.warn("[领取角色] [失败] [{}] [{}] [{}] [windowManager == null]", new Object[]{player.getUsername(),player.getId(),player.getName()});
			return;
		}
		MenuWindow mw = windowManager.createTempMenuWindow(600);
		mw.setTitle(Translate.text_4925);
		mw.setDescriptionInUUB(Translate.text_5450);
		Option_TakePlayerToOtherUser oa = new Option_TakePlayerToOtherUser();
		oa.setOwner(player);
		oa.setText(Translate.text_5450);
		mw.setOptions(new Option_TakePlayerToOtherUser[]{oa});
		INPUT_WINDOW_REQ iwq = new INPUT_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getTitle(), mw.getDescriptionInUUB(), (byte)2, (byte)100,Translate.在这里输入, Translate.text_62, Translate.text_364, new byte[0]);
		player.addMessageToRightBag(iwq);
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_领取角色;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_4925;
	}
}
