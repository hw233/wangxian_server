package com.fy.engineserver.homestead.cave;

import java.util.HashMap;
import java.util.Map;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager.BindType;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.homestead.cave.resource.PlantCfg;
import com.fy.engineserver.homestead.cave.resource.Point;
import com.fy.engineserver.homestead.faery.service.CaveSubSystem;
import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.newtask.actions.TaskAction;
import com.fy.engineserver.newtask.actions.TaskActionOfCavePlant;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.npc.CaveNPC;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.util.CompoundReturn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 田地
 * 
 * 
 */
@SimpleEmbeddable
public class CaveField extends CaveBuilding implements FaeryConfig {

	/** 开启状态 */
	private int assartStatus;
	/** 种植物状态 */
	private PlantStatus plantStatus;
	/** 植物摘取记录 */
	private HashMap<Long, Integer> pickMap = new HashMap<Long, Integer>();

	/** 植物上的炸弹信息 */
	private CaveFieldBombData caveFieldBombData;

	public CaveField() {

	}

	public CaveField(int type) {
		setType(type);
	}

	/**
	 * 给植物放置炸弹
	 * @param bombConfig
	 */
	public CompoundReturn plantBomb(CaveFieldBombConfig bombConfig, Player player) {
		CompoundReturn cr = CompoundReturn.create();

		if (bombConfig == null) {
			return cr.setBooleanValue(false).setStringValue(Translate.炸弹信息异常);
		}

		if (this.getCaveFieldBombData() != null && this.getCaveFieldBombData().isValid()) {
			if (Cave.logger.isInfoEnabled()) Cave.logger.info("[埋炸弹] [失败:已经埋了炸弹] [主人信息:" + this.getCave().getOwnerId() + " " + this.getCave().getOwnerName() + "]");
			return cr.setBooleanValue(false).setStringValue(Translate.已经埋了炸弹);
		}
		// 开垦过的土地才能操作
		if (assartStatus == FaeryConfig.FIELD_STATUS_DESOLATION) {
			if (Cave.logger.isInfoEnabled()) Cave.logger.info("[埋炸弹] [失败:土地还未开垦] [主人信息:" + this.getCave().getOwnerId() + " " + this.getCave().getOwnerName() + "]");
			return cr.setBooleanValue(false).setStringValue(Translate.土地需要开垦才能操作);
		}
		// 对播种了的土地才能操作
		if (this.getPlantStatus() == null) {
			if (Cave.logger.isInfoEnabled()) Cave.logger.info("[埋炸弹] [失败:需要种植才能操作] [主人信息:" + player.getLogString() + "]");

			return cr.setBooleanValue(false).setStringValue(Translate.种植后才能操作哦);
		}

		if (getPlantStatus().getOutShowStatus() == PLANT_AVATA_STATUS_GROWNUP) {
			if (Cave.logger.isInfoEnabled()) Cave.logger.info("[埋炸弹] [失败:作物已经成熟不能操作] [主人信息:" + player.getLogString() + "]");

			return cr.setBooleanValue(false).setStringValue(Translate.作物已经成熟不能操作);
		}
		ArticleEntity removeAe = player.removeArticleByNameColorAndBind(bombConfig.getArticleName(), bombConfig.getArticleColor(), BindType.BOTH, "仙府放置炸弹", true);

		if (removeAe == null) {
			if (Cave.logger.isInfoEnabled()) Cave.logger.info("[埋炸弹] [失败:没有炸弹:" + bombConfig.getArticleName() + " " + bombConfig.getArticleColor() + "] [主人信息:" + player.getLogString() + "]");

			return cr.setBooleanValue(false).setStringValue(Translate.你没有这个物品 + ":" + "<f color='" + ArticleManager.color_article[bombConfig.getArticleColor()] + "'>" + bombConfig.getArticleName() + "</f>");
		}
		setCaveFieldBombData(CaveFieldBombData.creatCaveFieldBombData(bombConfig));
		getCave().notifyFieldChange(this.getType());
		if (Cave.logger.isWarnEnabled()) Cave.logger.warn("[埋炸弹:" + bombConfig.toString() + "] [成功] [主人信息:" + player.getLogString() + "]");

		return cr.setBooleanValue(true).setStringValue(Translate.放置炸弹成功);
	}

	@Override
	public void initNpc(Cave cave) {
		PlantStatus plantStatus = getPlantStatus();
		if (plantStatus != null) {
			int templetNPCId = plantStatus.getPlantCfg().getTempletNpc();
			CaveNPC npc = (CaveNPC) ((MemoryNPCManager) MemoryNPCManager.getNPCManager()).createNPC(templetNPCId);
			if (getNpc() != null) {
				getCave().getFaery().getGame().removeSprite(getNpc());
				cave.getBuildings().remove(getNpc().getId());
			}
			if (npc == null) {
				FaeryManager.logger.error("[NPCID:{}]", new Object[] { templetNPCId });
				return;
			}
			Point point = FaeryManager.getInstance().getPoint(cave.getIndex(), getType());
			// 设置坐标形象
			npc.setX(point.getX());
			npc.setY(point.getY());
			setNpc(npc);
			setCave(cave);
			npc.setCave(cave);
			cave.getFaery().getGame().addSprite(npc);
			npc.setGrade(getPlantStatus().getOutShowStatus());
			cave.getBuildings().put(npc.getId(), this);
			modifyName();
			if (inBuilding()) {
				npc.setInBuilding(true);
			}
			ResourceManager.getInstance().setAvata(npc);
			// 加载炸弹
			initBomb();
		} else {
			super.initNpc(cave);
		}
	}

	public void initBomb() {
		if (this.getCaveFieldBombData() == null) {
			return;
		} else {
			CaveFieldBombConfig caveFieldBombConfig = FaeryManager.getInstance().getCaveFieldBombConfig(getCaveFieldBombData().getBombArticleName(), getCaveFieldBombData().getBombArticleColor());
			if (caveFieldBombConfig == null) {
				this.setCaveFieldBombData(null);
				getCave().notifyFieldChange(this.getType());
				if (Cave.logger.isWarnEnabled()) Cave.logger.warn("启动清除无效的炸弹信息:PLAYERID:" + getCave().getOwnerId() + "[炸弹信息:" + getCaveFieldBombData().getBombArticleName() + " 颜色:" + getCaveFieldBombData().getBombArticleColor() + "]");
			} else {
				getCaveFieldBombData().setBombConfig(caveFieldBombConfig);
			}
		}
	}

	/**
	 * 种植一个植物
	 * @param cfg
	 */
	public synchronized void doPlant(PlantCfg cfg) {
		try {
			PlantStatus.createPlantStatus(cfg, this);
			initNpc(getCave());
			modifyName();
			CaveSchedule caveSchedule = new CaveSchedule(Translate.田地种植 + cfg.getName(), getType(), OPTION_PLANT, cfg.getGrownUpTime());
			getSchedules().add(caveSchedule);
			getCave().getSchedules().add(caveSchedule);

			// 对种植的任务处理
			TaskAction action = TaskActionOfCavePlant.createTaskAction(cfg.getType());
			Player player = GamePlayerManager.getInstance().getPlayer(getCave().getOwnerId());
			player.dealWithTaskAction(action);
			if (CaveSubSystem.logger.isInfoEnabled()) {
				CaveSubSystem.logger.info(player.getLogString() + "[种植了植物:{}]", new Object[] { cfg.toString() });
			}
			getPickMap().clear();
			{
				// 统计
				AchievementManager.getInstance().record(player, RecordAction.种植农作物次数);
			}
			player.addMessageToRightBag(CaveSubSystem.getInstance().getCave_DETAILED_INFO_RES(player, null));
		} catch (Exception e) {
			CaveSubSystem.logger.error("种植植物错误", e);
		}
	}

	/**
	 * 土地重置,回到无种植状态-取消种植
	 */
	public synchronized void reset(Player player) {
		getCave().getBuildings().remove(getNpc().getId());
		getCave().getFaery().getGame().removeSprite(getNpc());

		this.getPickMap().clear();
		this.setPlantStatus(null);
		this.setCaveFieldBombData(null);
		removeScheduleForDone(OPTION_PLANT);
		initNpc(getCave());

		getCave().notifyFieldChange(this.getType());
		player.addMessageToRightBag(CaveSubSystem.getInstance().getCave_DETAILED_INFO_RES(player, null));
	}

	@Override
	public void modifyName() {
		StringBuffer sbf = new StringBuffer();
		if (getPlantStatus() != null) {
			int color = getPlantStatus().getOutputColor();
			String plantName = getPlantStatus().getPlantCfg().getName();
			if (getPlantStatus().getOutShowStatus() == PLANT_AVATA_STATUS_DEFAULT) {
				((CaveNPC) getNpc()).setName(plantName);
				((CaveNPC) getNpc()).setGrade(getPlantStatus().getOutShowStatus());
			} else if (getPlantStatus().getOutShowStatus() == PLANT_AVATA_STATUS_CHANGED) {
			} else if (getPlantStatus().getOutShowStatus() == PLANT_AVATA_STATUS_GROWNUP) {
				sbf.append(plantName);
				((CaveNPC) getNpc()).setGrade(getPlantStatus().getOutShowStatus());
				sbf.append("(").append(getPlantStatus().getLeftOutput()).append("/").append(getPlantStatus().getTotalOutput()).append(")");
				getNpc().setName(sbf.toString());
				getNpc().setNameColor(PlantCfg.color[getPlantStatus().getTreeColor()]);
			}
		} else {
			// 田地本来的名字
			getNpc().setName(Translate.田地 + FIELD_INDEX_NAME[(getType() - 5)]);
			super.modifyName();
		}
	}

	@Override
	public void removeScheduleForCancel(int optionType) {
		super.removeScheduleForCancel(optionType);
		if (optionType == OPTION_PLANT) {
			this.setPlantStatus(null);
			this.setCaveFieldBombData(null);
			getCave().getBuildings().remove(getNpc().getId());
			getCave().getFaery().getGame().removeSprite(this.getNpc());
			initNpc(getCave());
			modifyName();
		}
	}

	/**
	 * 收获完成
	 */
	public void onHarvestOver(Cave cave) {
		super.initNpc(cave);
	}

	public int getAssartStatus() {
		return assartStatus;
	}

	public void setAssartStatus(int assartStatus) {
		this.assartStatus = assartStatus;
	}

	public PlantStatus getPlantStatus() {
		return plantStatus;
	}

	public void setPlantStatus(PlantStatus plantStatus) {
		this.plantStatus = plantStatus;
	}

	public Map<Long, Integer> getPickMap() {
		return pickMap;
	}

	public void setPickMap(HashMap<Long, Integer> pickMap) {
		this.pickMap = pickMap;
	}

	public CaveFieldBombData getCaveFieldBombData() {
		return caveFieldBombData;
	}

	public void setCaveFieldBombData(CaveFieldBombData caveFieldBombData) {
		this.caveFieldBombData = caveFieldBombData;
	}

	@Override
	public String toString() {
		return "CaveField [assartStatus=" + assartStatus + ", plantStatus=" + plantStatus + ", pickMap=" + pickMap + "]";
	}
}
