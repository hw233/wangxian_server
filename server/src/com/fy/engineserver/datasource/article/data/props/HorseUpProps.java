package com.fy.engineserver.datasource.article.data.props;

import java.util.ArrayList;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.horse.Horse;
import com.fy.engineserver.sprite.horse.HorseManager;
import com.fy.engineserver.sprite.horse.HorseOtherData;
import com.fy.engineserver.sprite.horse2.manager.Horse2EntityManager;

public class HorseUpProps extends Props {
	private int canUseLevel;
	private int upLevel;

	@Override
	public String canUse(Player p) {
		return super.canUse(p);
	}

	@Override
	public boolean use(Game game, Player player, ArticleEntity ae) {
		
		if (!super.use(game, player, ae)) {
			return false;
		}
		ArrayList<Long> horseIdList = player.getHorseIdList();
		if (horseIdList != null && horseIdList.size() > 0) {
			for (Long horseId : horseIdList) {
				Horse horse = HorseManager.getInstance().getHorseById(horseId, player);
				if (horse != null && !horse.isFly()) {
//					HorseManager.logger.warn(player.getLogString() + "[坐骑升阶] [测试] [可升级到的最大阶:" + (upLevel + Horse2EntityManager.MAX_RANK_STAR) + "] [当前星:" + horse.getOtherData().getRankStar() + "] [canUseLevel:"+canUseLevel+"]");
					if (horse.getOtherData().getRankStar() == canUseLevel) {
//						HorseManager.logger.warn(player.getLogString() + "[坐骑升阶] [测试可以生阶] [可升级到的最大阶:" + (upLevel + Horse2EntityManager.MAX_RANK_STAR) + "] [当前星:" + horse.getOtherData().getRankStar() + "]");
						horse.getOtherData().setUpLevel(upLevel);
						// 升一阶
						HorseOtherData otherData=horse.getOtherData();
						otherData.setRankStar(otherData.getRankStar() + 1);
						horse.setOtherData(otherData);
						HorseManager.logger.warn(player.getLogString() + "[坐骑升阶] [horseId:" + horseId + "] [可升级到的最大阶:" + (upLevel + Horse2EntityManager.MAX_RANK_STAR) + "] [当前星:" + horse.getOtherData().getRankStar() + "]");
						horse.initBasicAttr();
//						HorseManager.logger.warn(player.getLogString() + "[坐骑升阶] [测试升阶后初始化属性] [horseId:" + horseId + "] [可升级到的最大阶:" + (upLevel + Horse2EntityManager.MAX_RANK_STAR) + "] [当前星:" + horse.getOtherData().getRankStar() + "]");
						player.sendError(Translate.进阶成功);
						return true;
					} else if(horse.getOtherData().getRankStar() < canUseLevel) {
						player.sendError(Translate.培养不足);
						HorseManager.logger.warn(player.getLogString() + "[培养不足] [horseId:" + horseId + "]");
					}else{
						player.sendError(Translate.不需要使用);
						HorseManager.logger.warn(player.getLogString() + "[等级过大] [horseId:" + horseId + "]");
					}
				} else {
					HorseManager.logger.warn(player.getLogString() + "[未获取到玩家的陆地坐骑] [horseId:" + horseId + "]");
				}
			}
		}else{
			player.sendError("您没有坐骑");
		}
		return false;
	}

	public int getCanUseLevel() {
		return canUseLevel;
	}

	public void setCanUseLevel(int canUseLevel) {
		this.canUseLevel = canUseLevel;
	}

	public int getUpLevel() {
		return upLevel;
	}

	public void setUpLevel(int upLevel) {
		this.upLevel = upLevel;
	}

}
