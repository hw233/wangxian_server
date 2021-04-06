package com.fy.engineserver.menu;


import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;

/**
 * 领取角色到一个新账号
 * 
 * 
 *
 */
public class Option_TakePlayerToOtherUserQueRenAndSecondQuXiao extends Option{

	Player owner;

	String inputUserName;

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public String getInputUserName() {
		return inputUserName;
	}

	public void setInputUserName(String inputUserName) {
		this.inputUserName = inputUserName;
	}

	/**
	 * 确认
	 */
//	public void doInput(Game game, Player player, String inputContent) {
//
//		if(player == owner){
//			PlayerManager pm = PlayerManager.getInstance();
//			if(pm == null){
//				return;
//			}
//			Player[] players = null;
//			try{
//				players = pm.getPlayerByUser(player.getUsername());
//			}catch(Exception ex){
//				ex.printStackTrace();
//				return;
//			}
//			if(players == null || players.length <= 5){
//				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, "您的账号内没有超过5个角色，不能进行角色领取操作");
//				player.addMessageToRightBag(hreq);
//				return;
//			}
//			if(inputUserName == null || inputUserName.trim().equals("")){
//				return;
//			}
//			if(inputUserName.equals(inputContent)){
//				Player[] inputPlayers = null;
//				try{
//					inputPlayers = pm.getPlayerByUser(inputUserName);
//				}catch(Exception ex){
//					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, "该账号不存在，请创建此账号后再输入");
//					player.addMessageToRightBag(hreq);
//					ex.printStackTrace();
//					return;
//				}
//				if(inputPlayers == null || inputPlayers.length >= 5){
//					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, inputUserName+"账号内已有5个或5个以上角色，不能进行角色领取操作");
//					player.addMessageToRightBag(hreq);
//					return;
//				}
//				WindowManager windowManager = WindowManager.getInstance();
//				if(windowManager == null){
//					return;
//				}
//				Player[] optionPlayers = getOptionPlayers(player);
//				if(optionPlayers == null || optionPlayers.length == 0){
//					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, "账号内没有可以领取的角色，不能进行角色领取操作");
//					player.addMessageToRightBag(hreq);
//					return;
//				}
//				MenuWindow mw = windowManager.createTempMenuWindow(600);
//				mw.setTitle("领取角色");
//				mw.setDescriptionInUUB("下面是可以领取的角色\n请选择角色进行领取");
//				Option_TakePlayerToOtherUserQueRenAndSecondQuXiao[] options = new Option_TakePlayerToOtherUserQueRenAndSecondQuXiao[optionPlayers.length];
//				for(int i = 0; i < optionPlayers.length; i++){
//					if(optionPlayers[i] != null){
//						Option_TakePlayerToOtherUserQueRenAndSecondQuXiao oa = new Option_TakePlayerToOtherUserQueRenAndSecondQuXiao();
//						oa.setOwner(player);
//						oa.setInputUserName(inputUserName);
//						oa.setSelectPlayer(optionPlayers[i]);
//						oa.setText(optionPlayers[i].getName());
//						options[i] = oa;
//					}
//				}
//				mw.setOptions(options);
//				QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
//				player.addMessageToRightBag(res);
//			}else{
//				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, "输入的账号不一致");
//				player.addMessageToRightBag(hreq);
//			}
//		}
//	
//	}
	public static Player[] getOptionPlayers(Player p){
		Player[] players = null;
		PlayerManager pm = PlayerManager.getInstance();
		if(pm == null){
			return players;
		}
		try{
			players = pm.getPlayerByUser(p.getUsername());
		}catch(Exception ex){
			ex.printStackTrace();
		}
		if(players != null){
			List<Player> tempList = new ArrayList<Player>();
			for(Player pl : players){
				if(pl != p){
					tempList.add(pl);
				}
			}
			players = tempList.toArray(new Player[0]);
		}
		return players;
	}
	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		if(player == owner){
//			PlayerManager pm = PlayerManager.getInstance();
//			if(pm == null){
//				
//				return;
//			}
//			Player[] players = null;
//			try{
//				players = pm.getPlayerByUser(player.getUsername());
//			}catch(Exception ex){
//				ex.printStackTrace();
//				return;
//			}
//			if(players == null || players.length <= 5){
////				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, "您的账号内没有超过5个角色，不能进行角色领取操作");
////				player.addMessageToRightBag(hreq);
//				return;
//			}
//			if(inputUserName == null || inputUserName.trim().equals("")){
//				return;
//			}
//			Player[] inputPlayers = null;
//			try{
//				BossClientService bcs = BossClientService.getInstance();
//				if(bcs.getPassport(inputUserName) == null){
//					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, "该账号不存在，请创建此账号后再输入");
//					player.addMessageToRightBag(hreq);
//					return;
//				}
//				inputPlayers = pm.getPlayerByUser(inputUserName);
//			}catch(Exception ex){
//				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, "该账号不存在，请创建此账号后再输入");
//				player.addMessageToRightBag(hreq);
//				ex.printStackTrace();
//				return;
//			}
//			if(inputPlayers == null || inputPlayers.length >= 5){
////				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, inputUserName+"账号内已有5个或5个以上角色，不能进行角色领取操作");
////				player.addMessageToRightBag(hreq);
//				return;
//			}
			Option_TakePlayerToOtherUserQueRenAndSecondQueRen.getWindowForContinue(player,inputUserName,"");
		}
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
