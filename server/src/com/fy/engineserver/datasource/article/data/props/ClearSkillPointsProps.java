package com.fy.engineserver.datasource.article.data.props;

import java.util.Arrays;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;

/**
 * 
 * 清除
 * 
 */
public class ClearSkillPointsProps extends Props {

	/** 技能点返回比例 */
	private double reBackRate = 1;

	public static int[][] 每级技能返还的修法值 = new int[][] { { 0, 582, 873, 1164, 1455, 1746, 2037, 2328, 2619, 2910, 3201, 3492, 3783, 4074, 4365 }, { 0, 999, 1498, 1997, 2496, 2995, 3494, 3993, 4492, 4991, 5490, 5990, 6489, 6988, 7487 }, { 0, 1527, 2291, 3054, 3818, 4581, 5345, 6108, 6872, 7635, 8399, 9162, 9925, 10689, 11452 }, { 0, 2169, 3253, 4337, 5421, 6505, 7589, 8673, 9757, 10841, 11925, 13010, 14094, 15178, 16262 }, { 0, 2922, 4383, 5844, 7305, 8766, 10227, 11688, 13149, 14610, 16071, 17532, 18993, 20454, 21915 }, { 0, 3789, 5683, 7577, 9471, 11365, 13259, 15153, 17047, 18941, 20835, 22730, 24624, 26518, 28412 }, { 0, 4767, 7151, 9534, 11918, 14301, 16685, 19068, 21452, 23835, 26219, 28602, 30985, 33369, 35752 }, { 0, 5859, 8788, 11717, 14646, 17575, 20504, 23433, 26362, 29291, 32220, 35150, 38079, 41008, 43937 }, { 0, 7062, 10593, 14124, 17655, 21186, 24717, 28248, 31779, 35310, 38841, 42372, 45903, 49434, 52965 }, { 0, 8379, 12568, 16757, 20946, 25135, 29324, 33513, 37702, 41891, 46080, 50270, 54459, 58648, 62837 }, { 0, 4904, 9807, 14711, 19614, 24518, 29421, 34325, 39228, 44132, 49035, 53939, 58842, 63745, 68649, 73552 }, { 0, 5675, 11349, 17023, 22697, 28371, 34045, 39719, 45393, 51067, 56741, 62415, 68090, 73764, 79438, 85112 }, };

	/**
	 * 清除玩家技能点
	 * 
	 * @param player
	 */
	public boolean use(Game game, Player player, ArticleEntity ae) {
		if (!super.use(game, player, ae)) {
			return false;
		}
		if (player.isFighting()) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_3552);
			player.addMessageToRightBag(req);
			return false;
		} else {
			byte[] oldSkillLevels = player.getSkillOneLevels();
			int yuanqiTotal = 0;
			// 返还部分修法值
			{
				try {
					for (int i = 0; i < oldSkillLevels.length; i++) {
						if (i < 每级技能返还的修法值.length) {
							int level = oldSkillLevels[i];
							for (int skillLevel = 0; skillLevel < level; skillLevel++) {
								yuanqiTotal += 每级技能返还的修法值[i][skillLevel];
							}
						}
					}
					// for (int i = 0; i < 每级技能返还的修法值.length; i++) {
					// if (每级技能返还的修法值.length < oldSkillLevels.length) {
					// if (oldSkillLevels[i] > 0) {
					// yuanqiTotal += 每级技能返还的修法值[i][oldSkillLevels[i - 1]];
					// }
					// }
					// }
				} catch (Exception ex) {
					Game.logger.error(player.getLogString() + "[使用洗髓丹异常] [" + ae.getArticleName() + "]", ex);
				}
			}
			if (yuanqiTotal == 0) {
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.进阶技能没有加点);
				player.addMessageToRightBag(req);
				return false;
			}
			// 还原技能
			player.resetSkillPoints();

			if (yuanqiTotal > 0) {
//				if (this.getReBackRate() > 1) this.setReBackRate(1);

				int yuanqiReback = (int) (yuanqiTotal * this.getReBackRate());
				if (yuanqiReback <= 1) yuanqiReback = 1;
				player.setEnergy(player.getEnergy() + yuanqiReback);

				String des = Translate.translateString(Translate.使用洗髓丹得到修法值, new String[][] { { Translate.STRING_1, this.getName() }, { Translate.COUNT_1, yuanqiReback + "" } });
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, des);
				player.addMessageToRightBag(req);
				ArticleManager.logger.warn(player.getLogString() + " [使用洗髓丹] [" + ae.getArticleName() + "] [颜色:" + ae.getColorType() + "] [总元气:" + yuanqiTotal + "] [返还比例:" + this.getReBackRate() + "] [返还元气:" + yuanqiReback + "] [原来的技能等级:" + Arrays.toString(oldSkillLevels) + "]");
			} else {
				HINT_REQ req1 = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_3553);
				player.addMessageToRightBag(req1);
			}

		}
		return true;
	}

	public double getReBackRate() {
		return reBackRate;
	}

	public void setReBackRate(double reBackRate) {
		this.reBackRate = reBackRate;
	}

}
