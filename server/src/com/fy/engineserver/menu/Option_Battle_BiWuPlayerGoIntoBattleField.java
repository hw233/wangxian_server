package com.fy.engineserver.menu;

import com.fy.engineserver.battlefield.BattleField;
import com.fy.engineserver.battlefield.concrete.BattleFieldManager;
import com.fy.engineserver.battlefield.concrete.TournamentField;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.tournament.manager.TournamentManager;

/**
 * 比武进比武场
 * 
 * 
 *
 */
public class Option_Battle_BiWuPlayerGoIntoBattleField extends Option {

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		if(player == null){
			if(TournamentManager.logger.isWarnEnabled())
				TournamentManager.logger.warn("[进入比武赛场] [失败] [player == null]");
			return;
		}
		BattleFieldManager bfm = BattleFieldManager.getInstance();
		if(bfm == null){
			if(TournamentManager.logger.isWarnEnabled())
				TournamentManager.logger.warn("[进入比武赛场] [失败] [bfm == null]");
			return;
		}

		TournamentManager tm = TournamentManager.getInstance();
		BattleField battleTemp = tm.比武场分配Map.get(player.getId());
		if(battleTemp == null){
			if(TournamentManager.logger.isWarnEnabled())
				TournamentManager.logger.warn("[进入比武赛场] [失败] [battleTemp == null]");
		}else{
			if(TournamentManager.logger.isWarnEnabled())
				TournamentManager.logger.warn("[进入比武赛场] [成功] ["+battleTemp.getClass()+"]");
		}
		if(battleTemp instanceof TournamentField){
			TournamentField battle = (TournamentField)battleTemp;
			if(battle != null){
				if(battle.getState() == TournamentField.STATE_WAITING_ENTER){
					if(battle.sideA == player.getId()){
						player.setBattleField(battle);
						player.setBattleFieldSide((byte)BattleField.BATTLE_SIDE_A);
					}else if(battle.sideB == player.getId()){
						player.setBattleField(battle);
						player.setBattleFieldSide((byte)BattleField.BATTLE_SIDE_B);
					}
					Game battleGame = battle.getGame();
					Point point = null;
					if(player.getBattleFieldSide() == BattleField.BATTLE_SIDE_A){
						point = ((TournamentField)battle).getRandomBornPoint(BattleField.BATTLE_SIDE_A);
					}else if(player.getBattleFieldSide() == BattleField.BATTLE_SIDE_B){
						point = ((TournamentField)battle).getRandomBornPoint(BattleField.BATTLE_SIDE_B);
					}
					if(battleGame != null && battleGame.getGameInfo() != null && point != null){
						TransportData transportData = new TransportData(0,0,0,0, battleGame.getGameInfo().getMapName(), point.x, point.y);
						game.transferGame(player, transportData);
						if(TournamentManager.logger.isWarnEnabled())
							TournamentManager.logger.warn("[进入比武赛场] [成功] [{}] [{}] [{}]", new Object[]{player.getUsername(),player.getId(),player.getName()});
					}else{
						player.setBattleField(null);
						player.setBattleFieldSide((byte)BattleField.BATTLE_SIDE_C);
						if(TournamentManager.logger.isWarnEnabled())
							TournamentManager.logger.warn("[进入比武赛场] [失败] [{}] [{}] [{}] [battleGame == null || battleGame.getGameInfo() == null || point == null]", new Object[]{player.getUsername(),player.getId(),player.getName()});
					}
				}else{
					String des = Translate.战场已经过了进入时间;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, des);
					player.addMessageToRightBag(hreq);
					if(TournamentManager.logger.isWarnEnabled())
						TournamentManager.logger.warn("[进入比武赛场] [失败] [状态:{}] [{}] [{}] [{}] [{}]", new Object[]{battle.getState(),player.getUsername(),player.getId(),player.getName(),des});
					return;
				}
			}else{
				String des = Translate.没有战场;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, des);
				player.addMessageToRightBag(hreq);
				if(TournamentManager.logger.isWarnEnabled())
					TournamentManager.logger.warn("[进入比武赛场] [失败] [{}] [{}] [{}] [{}]", new Object[]{player.getUsername(),player.getId(),player.getName(),des});
				return;
			}
		}else{
			String des = Translate.您没有比武;
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, des);
			player.addMessageToRightBag(hreq);
			if(TournamentManager.logger.isWarnEnabled())
				TournamentManager.logger.warn("[进入比武赛场] [失败] [{}] [{}] [{}] [{}]", new Object[]{player.getUsername(),player.getId(),player.getName(),des});
			return;
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
