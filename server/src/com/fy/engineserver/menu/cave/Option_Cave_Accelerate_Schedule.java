package com.fy.engineserver.menu.cave;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.cave.CaveBuilding;
import com.fy.engineserver.homestead.cave.CaveSchedule;
import com.fy.engineserver.homestead.faery.service.CaveSubSystem;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.CAVE_ACCELERATE_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;

public class Option_Cave_Accelerate_Schedule extends Option {
	private int buildingType;
	private int optionType;

	public Option_Cave_Accelerate_Schedule(int buildingType, int optionType) {
		this.buildingType = buildingType;
		this.optionType = optionType;
	}

	@Override
	public void doSelect(Game game, Player player) {
		synchronized (player) {

			Cave cave = FaeryManager.getInstance().getCave(player);

			if (cave == null) {
				player.sendError(Translate.text_cave_007);
				return;
			}

			CaveBuilding cBuilding = cave.getCaveBuilding(buildingType);
			if (cBuilding == null) {
				FaeryManager.logger.error("[加速进度] [建筑没找到][player:{}] [buildingType={}] [optionType={}", new Object[] { player.getName(), buildingType, optionType });
				return;
			}
			ArticleEntity ae = player.getArticleEntity(FaeryManager.accelerateArticleName);
			if (ae == null) {
				player.sendError(Translate.translateString(Translate.text_cave_101, new String[][] { { Translate.STRING_1, FaeryManager.accelerateArticleName } }));
				if (CaveSubSystem.logger.isWarnEnabled()) {
					CaveSubSystem.logger.warn(player.getLogString() + "[加速] [没有道具:" + FaeryManager.accelerateArticleName + "]");
				}
				return;
			}
			CaveSchedule cSchedule = cBuilding.getScheduleByOptType(optionType);
			if (FaeryManager.logger.isWarnEnabled()) {
				FaeryManager.logger.warn(player.getLogString() + "[加速进度] [player:{}] [buildingType={}] [optionType={}] [进度:{}]", new Object[] { player.getName(), buildingType, optionType, cSchedule });
			}
			if (cSchedule != null) {
				// 扣除一个道具
				ae = player.removeArticleEntityFromKnapsackByArticleId(ae.getId(), "庄园加速", true);
				if (ae == null) {
					player.sendError(Translate.translateString(Translate.text_cave_101, new String[][] { { Translate.STRING_1, FaeryManager.accelerateArticleName } }));
					if (CaveSubSystem.logger.isWarnEnabled()) {
						CaveSubSystem.logger.warn(player.getLogString() + "[加速] [居然没有道具:" + FaeryManager.accelerateArticleName + "]");
					}
					return;
				}
				cSchedule.accelerate(FaeryManager.accelerateTime);
				cave.notifyFieldChange(cBuilding.getType());

				player.addMessageToRightBag(CaveSubSystem.getInstance().getCave_DETAILED_INFO_RES(player, null));
				
				CAVE_ACCELERATE_RES res = new CAVE_ACCELERATE_RES(GameMessageFactory.nextSequnceNum(), cave.getSchedules().size(), cave.getCurrMaxScheduleNum(), cave.getSchedules().toArray(new CaveSchedule[0]), cave.getCurrRes(), cave.getCurrMaxResource());
				player.addMessageToRightBag(res);
				if (CaveSubSystem.logger.isWarnEnabled()) {
					CaveSubSystem.logger.warn(player.getLogString() + "[庄园加速] [成功] [返回加速协议] [cBuilding:" + cBuilding.getNpc().getName() + "] [optionType:" + optionType + "]");
				}
				{
					AchievementManager.getInstance().record(player, RecordAction.使用秒时间次数);
				}
			} else {
				player.sendError(Translate.text_cave_100);
				return;
			}
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public int getBuildingType() {
		return buildingType;
	}

	public void setBuildingType(int buildingType) {
		this.buildingType = buildingType;
	}

	public int getOptionType() {
		return optionType;
	}

	public void setOptionType(int optionType) {
		this.optionType = optionType;
	}

}
