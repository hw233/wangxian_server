package com.fy.engineserver.menu;


import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.boss.client.BossClientService;

/**
 * 领取角色到一个新账号
 * 
 * 
 *
 */
public class Option_TakePlayerToOtherUserQueRenAndSecondQueRen extends Option{

	Player owner;

	String inputUserName;

	Player selectPlayer;

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

	public Player getSelectPlayer() {
		return selectPlayer;
	}

	public void setSelectPlayer(Player selectPlayer) {
		this.selectPlayer = selectPlayer;
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
//				Option_TakePlayerToOtherUserQueRenAndSecondQueRen[] options = new Option_TakePlayerToOtherUserQueRenAndSecondQueRen[optionPlayers.length];
//				for(int i = 0; i < optionPlayers.length; i++){
//					if(optionPlayers[i] != null){
//						Option_TakePlayerToOtherUserQueRenAndSecondQueRen oa = new Option_TakePlayerToOtherUserQueRenAndSecondQueRen();
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
//	public static Player[] getOptionPlayers(Player p){
//		Player[] players = null;
//		PlayerManager pm = PlayerManager.getInstance();
//		if(pm == null){
//			return players;
//		}
//		try{
//			players = pm.getPlayerByUser(p.getUsername());
//		}catch(Exception ex){
//			ex.printStackTrace();
//		}
//		if(players != null){
//			List<Player> tempList = new ArrayList<Player>();
//			for(Player pl : players){
//				if(pl != p){
//					tempList.add(pl);
//				}
//			}
//			players = tempList.toArray(new Player[0]);
//		}
//		return players;
//	}
	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
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
			if(inputUserName == null || inputUserName.trim().equals("")){
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.text_5178);
				player.addMessageToRightBag(hreq);
//				Game.logger.warn("[领取角色] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] [账号不能为空]");
				if(Game.logger.isWarnEnabled())
					Game.logger.warn("[领取角色] [失败] [{}] [{}] [{}] [账号不能为空]", new Object[]{player.getUsername(),player.getId(),player.getName()});
				return;
			}
			Player[] inputPlayers = null;
			long nowTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			try{
				BossClientService bcs = BossClientService.getInstance();
				if(bcs.getPassportByUserName(inputUserName) == null){
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.text_5179);
					player.addMessageToRightBag(hreq);
//					Game.logger.warn("[领取角色] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] [该账号不存在]");
					if(Game.logger.isWarnEnabled())
						Game.logger.warn("[领取角色] [失败] [{}] [{}] [{}] [该账号不存在]", new Object[]{player.getUsername(),player.getId(),player.getName()});
					return;
				}
				inputPlayers = pm.getPlayerByUser(inputUserName);
			}catch(Exception ex){
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.text_5179);
				player.addMessageToRightBag(hreq);
				ex.printStackTrace();
				if(Game.logger.isWarnEnabled())
					Game.logger.warn("[领取角色] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] [该账号不存在] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - nowTime)+"]",ex);
				return;
			}
			if(inputPlayers == null || inputPlayers.length >= 5){
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, inputUserName+Translate.text_5180);
				player.addMessageToRightBag(hreq);
//				Game.logger.warn("[领取角色] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] ["+inputUserName+"账号内已有5个或5个以上角色，不能进行角色领取操作]");
				if(Game.logger.isWarnEnabled())
					Game.logger.warn("[领取角色] [失败] [{}] [{}] [{}] [{}账号内已有5个或5个以上角色，不能进行角色领取操作]", new Object[]{player.getUsername(),player.getId(),player.getName(),inputUserName});
				return;
			}
			if(selectPlayer == null){
//				Game.logger.warn("[领取角色] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] [选择的角色不存在]");
				if(Game.logger.isWarnEnabled())
					Game.logger.warn("[领取角色] [失败] [{}] [{}] [{}] [选择的角色不存在]", new Object[]{player.getUsername(),player.getId(),player.getName()});
				return;
			}else{
				for(Player pl : players){
					if(pl == selectPlayer){
						pm.updatePlayerUsername(selectPlayer, inputUserName);
						getWindowForContinue(player,inputUserName,Translate.text_5455);
//						Game.logger.warn("[领取角色] [成功] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] [领取角色] ["+selectPlayer.getId()+"] ["+selectPlayer.getName()+"] [放入账号] ["+inputUserName+"]");
						if(Game.logger.isWarnEnabled())
							Game.logger.warn("[领取角色] [成功] [{}] [{}] [{}] [领取角色] [{}] [{}] [放入账号] [{}]", new Object[]{player.getUsername(),player.getId(),player.getName(),selectPlayer.getId(),selectPlayer.getName(),inputUserName});
						break;
					}
				}
			}
		}
	}

	public static void getWindowForContinue(Player player,String inputUserName,String successdes){
//		if(player == null){
//			return;
//		}
//		PlayerManager pm = PlayerManager.getInstance();
//		if(pm == null){
//			return;
//		}
//		Player[] players = null;
//		try{
//			players = pm.getPlayerByUser(player.getUsername());
//		}catch(Exception ex){
//			ex.printStackTrace();
//			return;
//		}
//		if(players == null || players.length <= 5){
//			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, "您的账号内没有超过5个角色，不能进行角色领取操作");
//			player.addMessageToRightBag(hreq);
//			return;
//		}
//		if(inputUserName == null || inputUserName.trim().equals("")){
//			return;
//		}
//		Player[] inputPlayers = null;
//		try{
//			inputPlayers = pm.getPlayerByUser(inputUserName);
//		}catch(Exception ex){
//			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, "该账号不存在，请创建此账号后再输入");
//			player.addMessageToRightBag(hreq);
//			ex.printStackTrace();
//			return;
//		}
//		if(inputPlayers == null || inputPlayers.length >= 5){
////			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, inputUserName+"账号内已有5个或5个以上角色，不能进行角色领取操作");
////			player.addMessageToRightBag(hreq);
//			return;
//		}
		WindowManager windowManager = WindowManager.getInstance();
		if(windowManager == null){
			return;
		}

		MenuWindow mw = windowManager.createTempMenuWindow(600);
		mw.setTitle(Translate.text_4925);
		mw.setDescriptionInUUB(successdes+Translate.text_5456);
		Option_ContinueTakePlayerToOtherUser option = new Option_ContinueTakePlayerToOtherUser();
		option.setOwner(player);
		option.setInputUserName(inputUserName);
		Option cancelOption = new Option_UseCancel();
		mw.setOptions(new Option[]{option,cancelOption});
		
		option.setText(Translate.text_5457);
		cancelOption.setText(Translate.text_364);
		
		CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(),
				mw.getId(),mw.getDescriptionInUUB(),mw.getOptions());
		player.addMessageToRightBag(req);
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
