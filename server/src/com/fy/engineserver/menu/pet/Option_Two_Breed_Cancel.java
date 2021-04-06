package com.fy.engineserver.menu.pet;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.pet.PetManager;
import com.fy.engineserver.sprite.pet.PetMating;
import com.fy.engineserver.sprite.pet.PetMatingManager;

/**
 * A请求B繁殖,B不同意
 * @author Administrator
 *
 */
public class Option_Two_Breed_Cancel extends Option {
	
	private long matingId;
	private Player playerA;
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		
		PetMating mating = PetMatingManager.getInstance().getPlayerMating(matingId);
		if(mating != null){
			PetMatingManager.getInstance().cancelMating(player, matingId);
		}
		
		if(playerA.isOnline()){
			playerA.sendError(Translate.translateString(Translate.text_xx拒绝了你的宠物繁殖请求, new String[][] {{Translate.STRING_1,player.getName()}}));
		}
		
		player.sendError(Translate.translateString(Translate.text_你拒绝了xx的宠物繁殖请求, new String[][] {{Translate.STRING_1,playerA.getName()}}));
		
		if (PetManager.logger.isWarnEnabled()){
			PetManager.logger.warn("[B方拒绝了防止请求] ["+player.getLogString()+"]");
		}
	}

	public Player getPlayerA() {
		return playerA;
	}

	public void setPlayerA(Player playerA) {
		this.playerA = playerA;
	}

	public long getMatingId() {
		return matingId;
	}

	public void setMatingId(long matingId) {
		this.matingId = matingId;
	}

	
}


