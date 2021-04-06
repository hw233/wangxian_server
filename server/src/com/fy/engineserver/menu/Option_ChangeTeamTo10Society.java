package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Team;

/**
 * 将队伍转化为10人的团队
 * 
 * 只有队长才能这么做。
 * 
 * 
 *
 */
public class Option_ChangeTeamTo10Society extends Option{

	public Option_ChangeTeamTo10Society(){
		
	}

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		Team team = player.getTeam();
		if(team == null){
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)1,Translate.text_5154);
			player.addMessageToRightBag(req);
		}else{
			Player p = team.getCaptain();
			if(p != player){
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)1,Translate.text_5155);
				player.addMessageToRightBag(req);
			}else{
				if(team.isSocietyFlag() && team.getMembers().size() > 10){
					HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)1,Translate.text_5156);
					player.addMessageToRightBag(req);
				}else{
					team.setSociety(true,10);
					
					for(Player pp : team.getMembers()){
						HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_5157);
						pp.addMessageToRightBag(req);
					}
					
				}
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
		return OptionConstants.SERVER_FUNCTION_转化为10人团队;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return "" ;
	}
}
