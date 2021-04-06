package com.fy.engineserver.menu.seal;

import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.COLLECT_MATERIAL_FOR_BOSS_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.seal.SealCityBossInfo;
import com.fy.engineserver.seal.SealManager;
import com.fy.engineserver.seal.SealManager.BossDeadInfo;
import com.fy.engineserver.seal.data.Seal;
import com.fy.engineserver.seal.data.SealTaskInfo;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.TimeTool;

public class Option_Open_SealLevel extends Option implements NeedCheckPurview {

	private int takeTaskLevel;
	private int takeTaskStep;
	private int bossType; // 1;boss技能--2;boss属性
	private int selectType; // 1;boss技能--2;boss属性

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		int currSealLevel = SealManager.getInstance().getSealLevel();
		int currSealStep = SealManager.getInstance().getSeal().getSealStep();
		SealCityBossInfo bossinfo = null;
		if (currSealStep >= 3) {
			currSealStep = 2;
		}
		for (SealCityBossInfo info : SealManager.getInstance().infos) {
			if (info.sealLevel == currSealLevel && info.sealLayer == currSealStep + 1) {
				bossinfo = info;
				break;
			}
		}

		if (bossinfo == null) {
			player.sendError(Translate.出现异常);
			return;
		}
		player.optionSealStep = takeTaskStep + 1;

		String key = currSealLevel + player.optionSealStep + SealManager.getInstance().KEY_BOSS_DEAD_NUM + player.getId();
		BossDeadInfo bossdeadinfo = (BossDeadInfo) ActivityManagers.getInstance().getDdc().get(key);
		if (bossdeadinfo != null && bossdeadinfo.getDeadNums() >= SealManager.getInstance().MAX_BOSS_DEAD_NUM) {
			if (ActivityManagers.isSameDay(bossdeadinfo.getFirstDeadTime(), System.currentTimeMillis())) {
				player.sendError(Translate.击杀boss次数达上限);
				return;
			}
		}

		SealTaskInfo taskInfo = SealManager.getInstance().getTakInfo(currSealLevel, currSealStep, bossType);

		if (currSealLevel == 150 || (currSealLevel == 190 && selectType == 2 && (currSealStep == 1 || currSealStep == 2))) { // 封印副本
			try {
				int index = SealManager.getInstance().getIndex(takeTaskLevel);
				String mapName = SealManager.MAP_NAMES[index];
				int xyPoints[] = SealManager.BORN_POINTS[index];
				TransportData transportData = new TransportData(0, 0, 0, 0, mapName, xyPoints[0], xyPoints[1]);
				player.getCurrentGame().transferGame(player, transportData);
				if (Game.logger.isWarnEnabled()) {
					Game.logger.warn("[查看破封任务] 【封印副本】 [成功] [currSealLevel:{}] [currSealStep:{}] [{}]", new Object[] { currSealLevel, currSealStep, player.getLogString() });
				}
			} catch (Exception e) {
				Game.logger.error("[查看破封任务] 【封印副本】 [失败:异常] [查看等级:{}] [类型:{}] [玩家信息:{}] [{}]", new Object[] { takeTaskLevel, bossType, player.getLogString(), e });
			}
		} else if ((currSealStep == 0 && currSealLevel == 220) || (currSealLevel == 190 && selectType == 1 && (currSealStep == 0 || currSealStep == 2))) { // 上交材料
			String bossBuffInfo = "";
			String bossBuffName = "";
			long currPoints = 0;
			long allPoints = bossinfo.allPoints;
			try {
				int buffId = bossinfo.buffid;
				Buff buff = null;
				ArticleEntity ae = null;
				BuffTemplate bt = BuffTemplateManager.getInstance().getBuffTemplateById(buffId);
				if (bt != null) {
					buff = bt.createBuff(taskInfo.getBuffLevel());
				}
				int stype = 0;
				String resuleMess = bossinfo.resultMess;
				if (bossType == 1) {
					resuleMess = Translate.捐献材料描述_220;
				} else if (bossType == 2) {
					resuleMess = Translate.捐献材料描述_220_2;
					stype = 1;
				}
				if (currSealLevel == 190 && currSealStep == 2) {
					stype = 1;
				}
				Article a = ArticleManager.getInstance().getArticle(bossinfo.needMaterialName);
				if(a==null){
					if (Game.logger.isWarnEnabled()) {
						Game.logger.warn("[查看破封任务] 【上交材料】[a==null]  [currSealLevel:{}] [currSealStep:{}] [bossBuffName:{}] [类型：{}] [points:{}] [] [{}]", new Object[] { currSealLevel, currSealStep, bossBuffName, bossType, currPoints + "-->" + allPoints, player.getLogString() });
					}
					return;
				}
				ae = DefaultArticleEntityManager.getInstance().createTempEntity(a, true, ArticleEntityManager.封印捐献物品, player, a.getColorType());
				if (buff != null) {
					bossBuffInfo = buff.getDescription();
					if (buff.getLevel() == 0) {
						bossBuffInfo = Translate.捐献buff级别是0;
					}
					bossBuffName = buff.getTemplateName();
					
					currPoints = taskInfo.getPoints();
					COLLECT_MATERIAL_FOR_BOSS_RES res = new COLLECT_MATERIAL_FOR_BOSS_RES(GameMessageFactory.nextSequnceNum(), resuleMess, ae.getId(), bossBuffName, buff.getIconId(), bossBuffInfo, currPoints, allPoints, stype, bossType, a.getName());
					player.addMessageToRightBag(res);
					if (Game.logger.isWarnEnabled()) {
						Game.logger.warn("[查看破封任务] 【上交材料】[有buff效果]  [currSealLevel:{}] [currSealStep:{}] [bossBuffName:{}] [类型：{}] [points:{}] [] [{}]", new Object[] { currSealLevel, currSealStep, bossBuffName, bossType, currPoints + "-->" + allPoints, player.getLogString() });
					}
				} else {
					currPoints = taskInfo.getPoints();
					COLLECT_MATERIAL_FOR_BOSS_RES res = new COLLECT_MATERIAL_FOR_BOSS_RES(GameMessageFactory.nextSequnceNum(), resuleMess, (ae == null ? -1 : ae.getId()), bossBuffName, "", bossBuffInfo, currPoints, allPoints, stype, bossType, (ae == null ? "" : ae.getArticleName()));
					player.addMessageToRightBag(res);
					if (Game.logger.isWarnEnabled()) {
						Game.logger.warn("[查看破封任务] 【上交材料】[无buff效果]  [currSealLevel:{}] [currSealStep:{}] [bossBuffName:{}] [类型：{}] [points:{}] [] [{}]", new Object[] { currSealLevel, currSealStep, bossBuffName, bossType, currPoints + "-->" + allPoints, player.getLogString() });
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Game.logger.error("[查看破封任务] 【上交材料】 [失败:异常] [查看等级:{}] [类型：{}] [玩家信息:{}] [{}]", new Object[] { takeTaskLevel, bossType, player.getLogString(), e });
				return;
			}
		} else {
			if (Game.logger.isWarnEnabled()) {
				Game.logger.warn("[查看破封任务] 【规则无效】 [currSealLevel:{}] [currSealStep:{}] [{}]", new Object[] { currSealLevel, currSealStep, player.getLogString() });
			}
		}

	}

	@Override
	public boolean canSee(Player player) {
		int currSealLevel = SealManager.getInstance().getSealLevel();
		int currSealStep = SealManager.getInstance().getSeal().getSealStep();

		if (SealManager.getInstance().isCloseCurrSeal()) {
			if (Game.logger.isInfoEnabled()) {
				Game.logger.info("[查看破封任务] [已经关闭] [查看等级:{}] [当前等级:{}] [查看阶段:{}] [当前阶段:{}] [玩家信息:{}]", new Object[] { takeTaskLevel, currSealLevel, takeTaskStep, currSealStep, player.getLogString() });
			}
			return false;
		}

		if (takeTaskLevel != currSealLevel) {
			if (Game.logger.isInfoEnabled()) {
				Game.logger.info("[查看破封任务] [等级不符] [查看等级:{}] [当前等级:{}] [查看阶段:{}] [当前阶段:{}] [玩家信息:{}]", new Object[] { takeTaskLevel, currSealLevel, takeTaskStep, currSealStep, player.getLogString() });
			}
			return false;
		}

		if (!SealManager.getInstance().isStartSealTask()) {
			return false;
		}

		/**
		 * 150:(1,2,3个阶段依次开，但是同时保留，同时关闭，3个阶段都是boss挑战,每击杀一次boss，替换buff,知道达到要求的上限)
		 */
		if (currSealLevel == 150) {
			if (takeTaskStep > currSealStep) {
				return false;
			}
		}
		/**
		 * 190:(1阶段收集，如果满的话，才开启2阶段boss挑战，同时1阶段关闭，2阶段开启天数过期的话，就开启3阶段boss挑战和收集同时存在，收集到某一程度给boss种buff，关闭2阶段)
		 * 220:(第一阶段收集削弱boss，有2个选择按钮，固定天数过期后，关闭1阶段，开启2阶段，2阶段是刷世界boss，boss是有刷新cd，跟随任务关闭)
		 */
		if (currSealLevel == 190 || currSealLevel == 220) {
			if (takeTaskStep != currSealStep) {
				return false;
			}
		}

		return true;
	}

	public int getTakeTaskLevel() {
		return takeTaskLevel;
	}

	public void setTakeTaskLevel(int takeTaskLevel) {
		this.takeTaskLevel = takeTaskLevel;
	}

	public int getTakeTaskStep() {
		return takeTaskStep;
	}

	public void setTakeTaskStep(int takeTaskStep) {
		this.takeTaskStep = takeTaskStep;
	}

	public int getBossType() {
		return bossType;
	}

	public void setBossType(int bossType) {
		this.bossType = bossType;
	}

	public int getSelectType() {
		return this.selectType;
	}

	public void setSelectType(int selectType) {
		this.selectType = selectType;
	}

}
