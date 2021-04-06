package com.fy.engineserver.menu.pet;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.PET_MATING_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.pet.PetManager;
import com.fy.engineserver.sprite.pet.PetMating;
import com.fy.engineserver.sprite.pet.PetMatingManager;

/**
 * A请求B繁殖,B 同意
 * @author Administrator
 * 
 */
public class Option_Two_Breed_Confirm extends Option {

	private Player playerA;
	private long matingId;

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {

		PetMating mating = PetMatingManager.getInstance().getPlayerMating(matingId);
		if (mating != null) {
			if (PetManager.logger.isWarnEnabled()) {
				PetManager.logger.warn("[B方同意繁殖] [" + player.getLogString() + "] ");
			}
			PET_MATING_RES res = new PET_MATING_RES(GameMessageFactory.nextSequnceNum(), "", mating.getId());

			if (playerA.isOnline()) {
				playerA.addMessageToRightBag(res);
				player.addMessageToRightBag(res);

				if (PetManager.logger.isWarnEnabled()) {
					PetManager.logger.warn("[发送同意繁殖成功] [" + player.getLogString() + "]");
				}
				return;
			}
		}

		if (PetManager.logger.isWarnEnabled()) {
			PetManager.logger.warn("[同意繁殖错误] [对方关闭了繁殖]");
		}
		player.send_HINT_REQ(Translate.text_pet_51);
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
