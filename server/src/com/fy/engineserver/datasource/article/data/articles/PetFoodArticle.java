package com.fy.engineserver.datasource.article.data.articles;

import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;

/**
 * 特殊道具：宠物食物
 * 
 */
public class PetFoodArticle extends Article {

	// private int foodLevel;
	private byte type;

	// public int getValue(int level ,int color){
	// return 100;
	// }

	// 1 快乐 2 寿命
	public boolean addProperty(Pet pet, ArticleEntity ae, Player player) {
		int num = this.getValueByColor(ae.getColorType());
		if (type == 1) {
			int h = pet.getHappyness();
			if (h >= pet.getMaxHappyness()) {
				if (PetManager.logger.isDebugEnabled()) {
					PetManager.logger.debug("[喂养宠物增加快乐值] [宠物以达到最高快乐值] [" + player.getLogString() + "]");
				}
				return false;
			}
			if (h + num >= pet.getMaxHappyness()) {
				// player.send_HINT_REQ(pet.getName()+"增加快乐值"+(pet.getMaxHappyness()-h));
				player.send_HINT_REQ(Translate.translateString(Translate.xx增加快乐值aa, new String[][] { { Translate.STRING_1, pet.getName() }, { Translate.STRING_2, (pet.getMaxHappyness() - h) + "" } }));
				pet.setHappyness(pet.getMaxHappyness());
			} else {
				pet.setHappyness(h + num);
				// player.send_HINT_REQ(pet.getName()+"增加快乐值"+num);
				player.send_HINT_REQ(Translate.translateString(Translate.xx增加快乐值aa, new String[][] { { Translate.STRING_1, pet.getName() }, { Translate.STRING_2, num + "" } }));
			}
			if (PetManager.logger.isDebugEnabled()) {
				PetManager.logger.debug("[喂养宠物增加快乐值] [" + num + "] [" + player.getLogString() + "]");
			}
			return true;
		} else if (type == 2) {
			int h = pet.getLifeTime();
			if (h >= pet.getMaxLifeTime()) {
				if (PetManager.logger.isDebugEnabled()) {
					PetManager.logger.debug("[喂养宠物增加寿命值] [宠物以达到最高寿命值] [" + player.getLogString() + "]");
				}
				return false;
			}
			if (h + num >= pet.getMaxLifeTime()) {
				// player.send_HINT_REQ(pet.getName()+"增加寿命值"+(pet.getMaxHappyness()-h));
				player.send_HINT_REQ(Translate.translateString(Translate.xx增加寿命值aa, new String[][] { { Translate.STRING_1, pet.getName() }, { Translate.STRING_2, (pet.getMaxLifeTime() - h) + "" } }));
				pet.setLifeTime(pet.getMaxLifeTime());
			} else {
				// player.send_HINT_REQ(pet.getName()+"增加寿命值"+num);
				player.send_HINT_REQ(Translate.translateString(Translate.xx增加寿命值aa, new String[][] { { Translate.STRING_1, pet.getName() }, { Translate.STRING_2, num + "" } }));
				pet.setLifeTime(h + num);
			}
			if (PetManager.logger.isDebugEnabled()) {
				PetManager.logger.debug("[喂养宠物增加寿命值] [" + num + "] [" + player.getLogString() + "]");
			}
			return true;
		}
		return false;
	}

	public int getValueByColor(int color) {
		if (type == 2) {
			switch (color) {
			case 3:
				return 200;

			default:
				break;
			}
		}
		return 100;
	}

	// /**
	// * 得到食物等级
	// * @return
	// */
	// public int getFoodLevel() {
	// return foodLevel;
	// }
	//
	// public void setFoodLevel(int foodLevel) {
	// this.foodLevel = foodLevel;
	// }

	public boolean isUsedUndisappear() {
		return true;
	}

	public String getComment() {
		StringBuffer sb = new StringBuffer();
		return sb.toString();
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}
}
