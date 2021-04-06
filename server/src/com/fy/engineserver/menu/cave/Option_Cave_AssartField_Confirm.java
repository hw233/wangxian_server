package com.fy.engineserver.menu.cave;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.newChongZhiActivity.NewChongZhiActivityManager;
import com.fy.engineserver.activity.newChongZhiActivity.NewXiaoFeiActivity;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.cave.CaveBuilding;
import com.fy.engineserver.homestead.cave.CaveField;
import com.fy.engineserver.homestead.cave.resource.FieldAssartCfg;
import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.CaveNPC;
import com.fy.engineserver.stat.ArticleStatManager;

/**
 * 当田地令不足的时候直接扣除田地令+银子
 * 
 * 
 */
public class Option_Cave_AssartField_Confirm extends Option implements FaeryConfig {

	private CaveNPC caveNPC;
	private int need;
	private int has;
	private long needSilver;

	public Option_Cave_AssartField_Confirm(CaveNPC caveNPC, int need, int has, long needSilver) {
		this.caveNPC = caveNPC;
		this.need = need;
		this.has = has;
		this.needSilver = needSilver;
	}

	public CaveNPC getCaveNPC() {
		return caveNPC;
	}

	public void setCaveNPC(CaveNPC caveNPC) {
		this.caveNPC = caveNPC;
	}

	public int getNeed() {
		return need;
	}

	public void setNeed(int need) {
		this.need = need;
	}

	public int getHas() {
		return has;
	}

	public void setHas(int has) {
		this.has = has;
	}

	public long getNeedSilver() {
		return needSilver;
	}

	public void setNeedSilver(long needSilver) {
		this.needSilver = needSilver;
	}

	@Override
	public void doSelect(Game game, Player player) {
		Cave cave = getCaveNPC().getCave();
		if (cave == null) {
			player.sendError(Translate.text_cave_047);
			return;
		}
		CaveBuilding building = cave.getCaveBuildingByNPCId(getCaveNPC().getId());
		if (building.getType() < CAVE_BUILDING_TYPE_FIELD1) {
			return;
		}
		int hasFiledNum = 0;
		for (CaveField field : cave.getFields()) {
			if (field.getGrade() > 0) {
				hasFiledNum++;
			}
		}
		int fieldLimit = FaeryManager.getInstance().getMainCfgs()[cave.getMainBuilding().getGrade() - 1].getFieldNumLimit();
		if (fieldLimit <= hasFiledNum) {
			if (FaeryManager.logger.isInfoEnabled()) {
				FaeryManager.logger.info("[确认开垦] [开垦田地] [失败] [庄园等级:{}] [已有田地:{}] [田地上限:{}]", new Object[] { cave.getMainBuilding().getGrade(), hasFiledNum, fieldLimit });
			}
			return;
		}
		// 田地开垦消耗
		FieldAssartCfg assartCfg = FaeryManager.getInstance().getFieldAssartCfgs()[hasFiledNum];
		String articleName = assartCfg.getArticleName();
		int costNum = assartCfg.getCostNum();
		Article article = ArticleManager.getInstance().getArticle(articleName);
		if (article == null) {
			return;
		}
		int hasNum = player.getArticleEntityNum(articleName);
		synchronized (player) {
			if (hasNum < has) {
				player.sendError(Translate.translateString(Translate.text_cave_106, new String[][] { { Translate.STRING_1, articleName } }));
				if (FaeryManager.logger.isInfoEnabled()) {
					FaeryManager.logger.info(player.getLogString() + "[确认开垦] [道具不足] [记录:" + has + "个] [实际" + hasNum + "个]");
				}
				return;
			}
			if (player.getSilver() + player.getShopSilver() < needSilver) {
				player.sendError(Translate.text_cave_107);
				BillingCenter.银子不足时弹出充值确认框(player, Translate.translateString(Translate.你的银子不足是否去充值_金额, new String[][] { { Translate.STRING_1, BillingCenter.得到带单位的银两(needSilver) } }));
				if (FaeryManager.logger.isWarnEnabled()) {
					FaeryManager.logger.warn(player.getLogString() + "[确认开垦] [银子不足]");
				}
				return;
			}
			for (int i = 0; i < has; i++) {
				ArticleEntity ae = player.getArticleEntity(articleName);
				player.removeFromKnapsacks(ae, "洞府", true);
				ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0L, (byte) 0, 1L, "庄园开垦田地扣除", "");
			}
			try {
				BillingCenter.getInstance().playerExpense(player, needSilver, CurrencyType.SHOPYINZI, ExpenseReasonType.CAVE_FIELD, "");
				NewChongZhiActivityManager.instance.doXiaoFeiActivity(player, needSilver, NewXiaoFeiActivity.XIAOFEI_TYPE_SHOP);
				building.setGrade(1);
				cave.getFaery().getGame().removeSprite(building.getNpc());
				((CaveField) building).setAssartStatus(FIELD_STATUS_UNPLANTING);
				building.initNpc(cave);
				building.modifyName();

				cave.notifyFieldChange(building.getType());

				player.sendNotice(Translate.translateString(Translate.text_cave_087, new String[][] { { Translate.COUNT_1, String.valueOf(costNum) }, { Translate.STRING_1, articleName }, { Translate.STRING_2, building.getNpc().getName() } }));
				{
					AchievementManager.getInstance().record(player, RecordAction.仙府开垦土地次数);
				}

			} catch (Exception e) {
				FaeryManager.logger.error(player.getLogString() + "[确认扣钱开地] [异常]", e);
			}
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
