package com.fy.engineserver.homestead.cave;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.activeness.ActivenessManager;
import com.fy.engineserver.activity.activeness.ActivenessType;
import com.fy.engineserver.activity.cave.MaintanceActivity;
import com.fy.engineserver.activity.cave.OutputActivity;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.constants.Event;
import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.core.PetExperienceManager;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.PetPropsEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.economic.SavingFailedException;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.homestead.cave.CaveDynamic.Dynamic;
import com.fy.engineserver.homestead.cave.resource.DropsArticle;
import com.fy.engineserver.homestead.cave.resource.FieldAssartCfg;
import com.fy.engineserver.homestead.cave.resource.PlantCfg;
import com.fy.engineserver.homestead.cave.resource.Point;
import com.fy.engineserver.homestead.cave.resource.ResourceLevelCfg;
import com.fy.engineserver.homestead.exceptions.OutOfMaxLevelException;
import com.fy.engineserver.homestead.faery.Faery;
import com.fy.engineserver.homestead.faery.service.CaveSubSystem;
import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.activity.exchange.ExchangeActivityManager;
import com.fy.engineserver.message.CAVE_DYNAMIC_NOTICE_REQ;
import com.fy.engineserver.message.CAVE_QUERY_RESOURCECOLLECTION_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTIFY_EVENT_REQ;
import com.fy.engineserver.newBillboard.BillboardStatDate;
import com.fy.engineserver.newBillboard.BillboardStatDateManager;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.actions.TaskAction;
import com.fy.engineserver.newtask.actions.TaskActionOfCaveBuild;
import com.fy.engineserver.newtask.actions.TaskActionOfCaveHarvest;
import com.fy.engineserver.newtask.actions.TaskActionOfCaveSteal;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.PlayerSimpleInfo;
import com.fy.engineserver.sprite.PlayerSimpleInfoManager;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.horse.HorseManager;
import com.fy.engineserver.sprite.npc.CaveDoorNPC;
import com.fy.engineserver.sprite.npc.CaveNPC;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;
import com.fy.engineserver.sprite.pet.SingleForPetPropsEntity;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager.Function;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.TimeTool.TimeDistance;
import com.fy.boss.authorize.exception.BillFailedException;
import com.fy.boss.authorize.exception.NoEnoughMoneyException;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 个人洞府
 * 
 * 
 */
@SimpleEntity
@SimpleIndices({ @SimpleIndex(members = { "status" }), @SimpleIndex(members = { "caveScore" }) })
public class Cave implements FaeryConfig {

	public static Logger logger = CaveSubSystem.logger;
	@SimpleId
	private long id;
	@SimpleVersion
	private int version;

	private long ownerId;
	@SimpleColumn(length = 64)
	private String ownerName;
	private long ownerGrade;
	/** 在地图中的索引 */
	private transient int index;

	private CaveMainBuilding mainBuilding;
	private CaveStorehouse storehouse;
	private CavePethouse pethouse;
	private CaveFence fence;
	private CaveDoorplate doorplate;
	private ArrayList<CaveField> fields = new ArrayList<CaveField>();
	private Map<String, PlantRecord> plantRecord = new HashMap<String, PlantRecord>();
	/** 当前资源 */
	@SimpleColumn(saveInterval = 15)
	private ResourceCollection currRes;
	/** 洞府状态 */
	private int status;
	/** 主人最后一次上线时间 */
	@SimpleColumn(saveInterval = 600)
	private long ownerLastVisitTime;
	/** 封印时间 */
	private long khatamTime;
	/** 最后一次维护时间 */
	private long lastMaintenanceTime;
	private transient Faery faery;

	/** <npid,CaveBuilding> */
	private transient Hashtable<Long, CaveBuilding> buildings = new Hashtable<Long, CaveBuilding>();
	/** 庄园工作进度 */
	private transient ArrayList<CaveSchedule> schedules = new ArrayList<CaveSchedule>();
	/** 额外增加工作进度开始时间 */
	private long increaseScheduleStart;
	/** 额外增加工作进度持续时间 */
	private long increaseScheduleLast;
	/** 额外增加工作进度数量 */
	private int increaseScheduleNum;
	/** 记录动态记录 */
	private transient List<CaveDynamic> recordList = new ArrayList<CaveDynamic>();
	/** 记录最大条数 */
	static int maxRecordNum = 50;
	/** 主人最后一次阅读动态 */
	private transient long ownerLastSeeDynamic = 0;

	/**
	 * 奖励系数
	 */
	private transient int rewardTimes = 1;

	public Cave() {

	}

	/**
	 * 唤醒洞府 （对封印状态的洞府有效）
	 * @return
	 */
	public CompoundReturn notifyCave() {
		return FaeryManager.getInstance().notifyCave(this);
	}

	public synchronized void heartBeat(Faery faery, int index) {

		try {
			long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			if (getStatus() == CAVE_STATUS_OPEN || getStatus() == CAVE_STATUS_LOCKED) { // 开放状态
				if (!GamePlayerManager.getInstance().isOnline(ownerId) && getOwnerLastVisitTime() > 0 && (now - getOwnerLastVisitTime()) >= FaeryManager.getInstance().getMainCfgs()[this.getMainBuilding().getGrade() - 1].getKhatamDay() * 1000L * 60 * 60 * 24) {
					khatam(faery, index);
					return;
				}
				if (UnitServerFunctionManager.needCloseFunctuin(Function.仙府)) {
					khatam(faery, index);
					return;
				}
				if (getMainBuilding().inBuilding()) {
					CaveSchedule caveSchedule = getMainBuilding().getScheduleByOptType(OPTION_LEVEL_UP);
					if (getMainBuilding().getLvUpStartTime() > 0 && caveSchedule != null && caveSchedule.hasDone()) {
						// 更改状态
						getMainBuilding().setGrade(getMainBuilding().getGrade() + 1);
						((CaveNPC) getMainBuilding().getNpc()).setGrade(getMainBuilding().getGrade());
						((CaveNPC) getMainBuilding().getNpc()).setInBuilding(false);
						ResourceManager.getInstance().setAvata(((CaveNPC) getMainBuilding().getNpc()));
						getMainBuilding().removeScheduleForDone(OPTION_LEVEL_UP);
						getMainBuilding().setStatus(CAVE_BUILDING_STATUS_SERVICE);
						if (getMainBuilding().getGrade() == FaeryManager.maintenanceGrade) {
							setLastMaintenanceTime(now);
						}
						Player owner = GamePlayerManager.getInstance().getPlayer(ownerId);
						owner.dealWithTaskAction(TaskActionOfCaveBuild.createTaskAction(getMainBuilding().getType(), getMainBuilding().getGrade()));

						if (GamePlayerManager.getInstance().isOnline(ownerId)) {
							owner.sendNotice(Translate.translateString(Translate.text_cave_064, new String[][] { { Translate.STRING_1, getMainBuilding().getNpc().getName() }, { Translate.STRING_2, String.valueOf(getMainBuilding().getGrade()) } }));
						}
						if (logger.isWarnEnabled()) {
							logger.warn("[建筑升级完成] [{}] [升级后等级:{}] [主人:{}] [建筑ID:{}]", new Object[] { getMainBuilding().getNpc().getName(), getMainBuilding().getGrade(), ownerId, getMainBuilding().getNpc().getId() });
						}

						notifyFieldChange(getMainBuilding().getType());
						{
							// 主建筑级别统计
							AchievementManager.getInstance().record(owner, RecordAction.仙府主建筑达到等级, getMainBuilding().getGrade());
						}
					}
				}
				if (getStorehouse().inBuilding()) {
					CaveSchedule caveSchedule = getStorehouse().getScheduleByOptType(OPTION_LEVEL_UP);
					if (getStorehouse().getLvUpStartTime() > 0 && caveSchedule != null && caveSchedule.hasDone()) {
						int mainBuildLevel = getMainBuilding().getGrade();
						int resultLevel = getStorehouse().getGrade() + 1;
						if (getStorehouse().getGrade() >= mainBuildLevel) {
							resultLevel = mainBuildLevel;
						}
						getStorehouse().setGrade(resultLevel);
						((CaveNPC) getStorehouse().getNpc()).setGrade(getStorehouse().getGrade());
						((CaveNPC) getStorehouse().getNpc()).setInBuilding(false);
						ResourceManager.getInstance().setAvata(((CaveNPC) getStorehouse().getNpc()));
						getStorehouse().removeScheduleForDone(OPTION_LEVEL_UP);
						getStorehouse().setStatus(CAVE_BUILDING_STATUS_SERVICE);

						Player owner = GamePlayerManager.getInstance().getPlayer(ownerId);
						owner.dealWithTaskAction(TaskActionOfCaveBuild.createTaskAction(getStorehouse().getType(), getStorehouse().getGrade()));
						if (GamePlayerManager.getInstance().isOnline(ownerId)) {
							owner.sendNotice(Translate.translateString(Translate.text_cave_064, new String[][] { { Translate.STRING_1, getStorehouse().getNpc().getName() }, { Translate.STRING_2, String.valueOf(getStorehouse().getGrade()) } }));
						}
						if (logger.isWarnEnabled()) {
							logger.warn("[建筑升级完成] [{}] [升级后等级:{}] [主人:{}] [建筑ID:{}]", new Object[] { getStorehouse().getNpc().getName(), getStorehouse().getGrade(), ownerId }, getStorehouse().getNpc().getId());
						}
						notifyFieldChange(getStorehouse().getType());
					}
				}
				// 提升资源等级
				CaveSchedule schedule = getStorehouse().getScheduleByOptType(OPTION_FOOD_SIZEUP);
				if (schedule != null) {
					if (schedule.hasDone()) {
						// 升级成功
						getStorehouse().removeScheduleForDone(OPTION_FOOD_SIZEUP);
						getStorehouse().setFoodLevel(getStorehouse().getFoodLevel() + 1);

						notifyFieldChange(getStorehouse().getType());
						addDynamic(new CaveDynamic(Translate.系统, Dynamic.资源等级提升, schedule.getName()));
						if (GamePlayerManager.getInstance().isOnline(ownerId)) {
							Player owner = GamePlayerManager.getInstance().getPlayer(ownerId);
							owner.sendNotice(Translate.translateString(Translate.text_cave_065, new String[][] { { Translate.STRING_1, String.valueOf(getStorehouse().getWoodLevel()) } }));
						}
					}
				}
				schedule = getStorehouse().getScheduleByOptType(OPTION_WOOD_SIZEUP);
				if (schedule != null) {
					if (schedule.hasDone()) {
						// 升级成功
						getStorehouse().removeScheduleForDone(OPTION_WOOD_SIZEUP);
						getStorehouse().setWoodLevel(getStorehouse().getWoodLevel() + 1);
						notifyFieldChange(getStorehouse().getType());
						addDynamic(new CaveDynamic(Translate.系统, Dynamic.资源等级提升, schedule.getName()));
						if (GamePlayerManager.getInstance().isOnline(ownerId)) {
							Player owner = GamePlayerManager.getInstance().getPlayer(ownerId);
							owner.sendNotice(Translate.translateString(Translate.text_cave_066, new String[][] { { Translate.STRING_1, String.valueOf(getStorehouse().getWoodLevel()) } }));
						}
					}
				}
				schedule = getStorehouse().getScheduleByOptType(OPTION_STONE_SIZEUP);
				if (schedule != null) {
					if (schedule.hasDone()) {
						// 升级成功
						getStorehouse().removeScheduleForDone(OPTION_STONE_SIZEUP);
						getStorehouse().setStoneLevel(getStorehouse().getStoneLevel() + 1);
						addDynamic(new CaveDynamic(Translate.系统, Dynamic.资源等级提升, schedule.getName()));
						notifyFieldChange(getStorehouse().getType());

						if (GamePlayerManager.getInstance().isOnline(ownerId)) {
							Player owner = GamePlayerManager.getInstance().getPlayer(ownerId);
							owner.sendNotice(Translate.translateString(Translate.text_cave_067, new String[][] { { Translate.STRING_1, String.valueOf(getStorehouse().getWoodLevel()) } }));
						}
					}
				}

				if (getPethouse().inBuilding()) {
					CaveSchedule caveSchedule = getPethouse().getScheduleByOptType(OPTION_LEVEL_UP);
					if (getPethouse().getLvUpStartTime() > 0 && caveSchedule != null && caveSchedule.hasDone()) {
						int mainBuildLevel = getMainBuilding().getGrade();
						int resultLevel = getPethouse().getGrade() + 1;
						if (getPethouse().getGrade() >= mainBuildLevel) {
							resultLevel = mainBuildLevel;
						}
						getPethouse().setGrade(resultLevel);
						((CaveNPC) getPethouse().getNpc()).setGrade(getPethouse().getGrade());
						getPethouse().removeScheduleForDone(OPTION_LEVEL_UP);

						((CaveNPC) getPethouse().getNpc()).setInBuilding(false);
						ResourceManager.getInstance().setAvata(((CaveNPC) getPethouse().getNpc()));
						getPethouse().setStatus(CAVE_BUILDING_STATUS_SERVICE);

						Player owner = GamePlayerManager.getInstance().getPlayer(ownerId);
						owner.dealWithTaskAction(TaskActionOfCaveBuild.createTaskAction(getPethouse().getType(), getPethouse().getGrade()));
						if (GamePlayerManager.getInstance().isOnline(ownerId)) {
							owner.sendNotice(Translate.translateString(Translate.text_cave_064, new String[][] { { Translate.STRING_1, getPethouse().getNpc().getName() }, { Translate.STRING_2, String.valueOf(getPethouse().getGrade()) } }));
						}
						if (logger.isWarnEnabled()) {
							logger.warn("[建筑升级完成] [{}] [升级后:{}] [主人:{}] [建筑ID:{}]", new Object[] { getPethouse().getNpc().getName(), getPethouse().getGrade(), ownerId }, getPethouse().getNpc().getId());
						}
						notifyFieldChange(getPethouse().getType());
					}
				}

				if (getFence().inBuilding()) {
					CaveSchedule caveSchedule = getFence().getScheduleByOptType(OPTION_LEVEL_UP);
					if (getFence().getLvUpStartTime() > 0 && caveSchedule != null && caveSchedule.hasDone()) {
						int mainBuildLevel = getMainBuilding().getGrade();
						int resultLevel = getFence().getGrade() + 1;
						if (getFence().getGrade() >= mainBuildLevel) {
							resultLevel = mainBuildLevel;
						}
						getFence().setGrade(resultLevel);
						getFence().removeScheduleForDone(OPTION_LEVEL_UP);
						((CaveNPC) getFence().getNpc()).setGrade(getFence().getGrade());

						((CaveNPC) getFence().getNpc()).setInBuilding(false);
						ResourceManager.getInstance().setAvata(((CaveNPC) getFence().getNpc()));
						getFence().setStatus(CAVE_BUILDING_STATUS_SERVICE);
						if (logger.isWarnEnabled()) {
							logger.warn("[建筑升级完成] [{}] [升级后:{}] [主人:{}] [建筑ID:{}]", new Object[] { getFence().getNpc().getName(), getFence().getGrade(), ownerId }, getFence().getNpc().getId());
						}
						notifyFieldChange(getFence().getType());
					}
				}

				for (CaveField caveField : fields) {
					if (caveField.getAssartStatus() == FIELD_STATUS_DESOLATION) {
						continue;
					}
					if (caveField.inBuilding()) {
						CaveSchedule caveSchedule = caveField.getScheduleByOptType(OPTION_LEVEL_UP);
						if (caveSchedule != null && caveSchedule.hasDone()) {

							int mainBuildLevel = getMainBuilding().getGrade();
							int resultLevel = caveField.getGrade() + 1;
							if (caveField.getGrade() >= mainBuildLevel) {
								resultLevel = mainBuildLevel;
							}
							caveField.setGrade(resultLevel);

							((CaveNPC) caveField.getNpc()).setGrade(caveField.getGrade());

							((CaveNPC) caveField.getNpc()).setInBuilding(false);
							ResourceManager.getInstance().setAvata(((CaveNPC) caveField.getNpc()));
							caveField.removeScheduleForDone(OPTION_LEVEL_UP);
							caveField.setStatus(CAVE_BUILDING_STATUS_SERVICE);

							Player owner = GamePlayerManager.getInstance().getPlayer(ownerId);
							owner.dealWithTaskAction(TaskActionOfCaveBuild.createTaskAction(caveField.getType(), caveField.getGrade()));
							if (GamePlayerManager.getInstance().isOnline(ownerId)) {
								owner.sendNotice(Translate.translateString(Translate.text_cave_064, new String[][] { { Translate.STRING_1, caveField.getNpc().getName() }, { Translate.STRING_2, String.valueOf(caveField.getGrade()) } }));
							}
							notifyFieldChange(caveField.getType());
							if (logger.isWarnEnabled()) {
								logger.warn(getOwnerId() + "[建筑升级完成] [" + caveField.getNpc().getName() + "] [升级后:" + caveField.getGrade() + "] [主人:" + getOwnerName() + "] [建筑ID:" + caveField.getNpc().getId() + "]");
							}
						}
					} else if (caveField.getPlantStatus() != null) { // 在种植状态的
						PlantStatus plantStatus = caveField.getPlantStatus();
						CaveSchedule caveSchedule = caveField.getScheduleByOptType(OPTION_PLANT);
						if (caveSchedule != null && plantStatus.getOutShowStatus() == PLANT_AVATA_STATUS_DEFAULT && (plantStatus.getHarvestTime() - caveSchedule.getLeftTime()) >= plantStatus.getChangeAvtaTime()) {// 可变形象了
							// 需要更换形象
							plantStatus.setOutShowStatus(PLANT_AVATA_STATUS_CHANGED);
							int treeColor = FaeryManager.randomColor(plantStatus.getPlantCfg().getColorRate());
							plantStatus.setTreeColor(treeColor);
							plantStatus.setOutputColor(plantStatus.getPlantCfg().getOutputArticleColor()[treeColor]);
							caveField.modifyName();
							((CaveNPC) caveField.getNpc()).setGrade(1);
							ResourceManager.getInstance().setAvata((CaveNPC) caveField.getNpc());
							notifyFieldChange(caveField.getType());
						} else if (plantStatus.getOutShowStatus() == PLANT_AVATA_STATUS_CHANGED) {
							if (caveSchedule.getLeftTime() <= 0) {
								((CaveNPC) caveField.getNpc()).setGrade(2);
								ResourceManager.getInstance().setAvata((CaveNPC) caveField.getNpc());
								plantStatus.setOutShowStatus(PLANT_AVATA_STATUS_GROWNUP);

								int treeColor = plantStatus.getTreeColor();

								// 设置产出和产出颜色2014-5-19 10:50:20
								{
									ExchangeActivityManager eam=ExchangeActivityManager.getInstance();
									Calendar ca=Calendar.getInstance();
									ca.setTimeInMillis(plantStatus.getPlantStartTime());
									OutputActivity oa=eam.getCurrOutputActivity(ca);
									int totalOutput=plantStatus.getPlantCfg().getOutputNumArr()[treeColor];
									if(null!=oa){
										totalOutput=(int)Math.floor(totalOutput*(1+oa.getOutputActivity(ca).getDoubleValue()));
									}
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
									CaveSubSystem.logger.info("[设置总产量] [种植时间:" + sdf.format(ca.getTimeInMillis()) + "]");
									plantStatus.setTotalOutput(totalOutput);
									plantStatus.setLeftOutput(plantStatus.getTotalOutput());
								}

								int articleColor = plantStatus.getPlantCfg().getOutputArticleColor()[treeColor];
								caveField.removeScheduleForDone(OPTION_PLANT);
								notifyFieldChange(caveField.getType());
								caveField.modifyName();
								if (treeColor >= ArticleManager.equipment_color_紫) {
									Player owner = GamePlayerManager.getInstance().getPlayer(ownerId);
									if (owner != null) {

										ChatMessage msg = new ChatMessage();
										String mes = Translate.translateString(Translate.橙色果子熟了, new String[][] { { Translate.STRING_1, Integer.toHexString(ArticleManager.color_article[treeColor]) }, { Translate.STRING_2, getFaery().getName() }, { Translate.STRING_3, owner.getName() }, { Translate.STRING_4, ArticleManager.color_article_Strings[treeColor] } });
										msg.setMessageText(mes);
										ChatMessageService.getInstance().sendMessageToSystem(msg);
										owner.sendNotice(mes);
									}
								}
								addDynamic(new CaveDynamic(Translate.系统, Dynamic.果实成熟, Translate.果实成熟 + ":<f color='" + ArticleManager.color_article[treeColor] + "'>" + caveField.getNpc().getName() + "</f>"));
								if (logger.isWarnEnabled()) {
									logger.warn(getOwnerId() + "[植物成熟] [" + caveField.getNpc().getName() + "] [颜色:" + articleColor + "]");
								}
							}
						}
					}
				}
				/************************************** 检测增加队列是否结束 **************************************/
				if (getIncreaseScheduleNum() > 0) {// 有使用的队列.检查时间
					if (now >= (getIncreaseScheduleStart() + getIncreaseScheduleLast())) {
						setIncreaseScheduleLast(0);
						setIncreaseScheduleNum(0);
						// 通知主人,队列到期
						addDynamic(new CaveDynamic(Translate.系统, Dynamic.魯班令到期, Translate.鲁班令到期));
						if(GamePlayerManager.getInstance().isOnline(this.getOwnerId())) {
							Player player = GamePlayerManager.getInstance().getPlayer(this.getOwnerId());
							player.addMessageToRightBag(CaveSubSystem.getInstance().getCave_DETAILED_INFO_RES(player, null));
						}
					}
				}
			}
			maintenance();
		} catch (Exception e) {
			CaveSubSystem.logger.error("Cave心跳异常了!!", e);
		}
	}

	/**
	 * 封印庄园
	 */
	public synchronized void khatam(Faery faery, int index) {
		long start = SystemTime.currentTimeMillis();
		try {
			if (CaveSubSystem.logger.isWarnEnabled()) {
				CaveSubSystem.logger.warn("[执行了封印:" + faery.getName() + "] [index = " + index + "] [ID:" + getId() + "] [ownerId:" + ownerId + "]");
			}
			this.setStatus(CAVE_STATUS_KHATAM);
			this.setKhatamTime(SystemTime.currentTimeMillis());
			FaeryManager.caveEm.flush(this);
			Player owner = GamePlayerManager.getInstance().getPlayer(ownerId);
			FaeryManager.getInstance().getKhatamMap().put(getOwnerId(), this.getId());
			FaeryManager.getInstance().getPlayerCave().remove(getOwnerId());
			faery.initSize();
			if (CaveSubSystem.logger.isWarnEnabled()) {
				CaveSubSystem.logger.warn("[执行了封印:" + faery.getName() + "] [封印完毕] [index = " + index + "] [ID:" + getId() + "] [ownerId:" + ownerId + "] [耗时:" + (SystemTime.currentTimeMillis() - start) + "]");
			}
		} catch (Exception e) {
			CaveSubSystem.logger.error("[封印仙府异常] [faery.ID:" + faery.getId() + "] [index:" + index + "]", e);
		}
	}

	/**
	 * 提升资源等级<BR/>
	 * 1.资源等级达到当前最大值,不能再升级了,需要升级主建筑<BR/>
	 * 2.当前资源不足以升级资源<BR/>
	 * 3.操作队列已满<BR/>
	 * 4.已经有相同队列存在了<BR/>
	 * @param resourceType
	 * @param player
	 * @return
	 */
	public CompoundReturn storeSizeUp(int resourceType, Player player) {
		int resLevel = getStorehouse().getCurrResourceLevel(resourceType);
		int storeLevel = getStorehouse().getGrade();

		// StorehouseCfg cfg = FaeryManager.getInstance().getStoreCfgs()[storeLevel - 1];

		ResourceLevelCfg cfg = FaeryManager.getInstance().getResLvCfg()[resLevel - 1];

		// 是否有操作队列的问题
		if (!hasScheduleLeft()) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(3);
		}
		// 资源不足升级
		if (!getCurrRes().isEnough(cfg.getLvUpCost())) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(2);
		}
		int optType = getStoreSizeUpOption(resourceType);
		if (getStorehouse().hasSameSchedule(optType)) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(4);
		}
		getCurrRes().deduct(cfg.getLvUpCost());
		notifyFieldChange("currRes");
		getStorehouse().setLvUpStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		CaveSchedule schedule = new CaveSchedule(Translate.提升资源等级升级 + FRUIT_RES_NAMES[resourceType] + " " + resLevel + Translate.级, getStorehouse().getType(), optType, cfg.getLvUpTime() * 1000);
		getStorehouse().getSchedules().add(schedule);
		getSchedules().add(schedule);
		notifyFieldChange(getStorehouse().getType());

		player.addMessageToRightBag(CaveSubSystem.getInstance().getCave_DETAILED_INFO_RES(player, null));
		return CompoundReturn.createCompoundReturn().setBooleanValue(true);
	}

	private int getStoreSizeUpOption(int resourceType) {
		if (resourceType == FRUIT_RES_FOOD) {
			return OPTION_FOOD_SIZEUP;
		} else if (resourceType == FRUIT_RES_WOOD) {
			return OPTION_WOOD_SIZEUP;

		} else if (resourceType == FRUIT_RES_STONE) {
			return OPTION_STONE_SIZEUP;
		} else {
			throw new IllegalStateException();
		}
	}

	/**
	 * 种植<BR/>
	 * 1.所选植物不存在<BR/>
	 * 2.所选的土地不存在<BR/>
	 * 3.土地在升级中不能种植<BR/>
	 * 4.植物还没有收获不能种植<BR/>
	 * 5.目标不是土地<BR/>
	 * 6.作物要求土地等级高于选择的土地等级<BR/>
	 * 7.种植的需要的资源不足<BR/>
	 * 8.没有可用进度<BR/>
	 * 9.超出最大同时种植上限<BR/>
	 * 10.同组超过同时种植上限<BR/>
	 * 11.银子不足<BR/>
	 * @param player
	 * @param plantName
	 * @param fieldType
	 * @return
	 */
	public CompoundReturn planting(Player player, int id, long NPCId) {
		PlantCfg cfg = FaeryManager.getInstance().getPlantCfg(id);

		if (getSchedules().size() >= getCurrMaxScheduleNum()) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(8);
		}

		if (cfg == null) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(1);
		}

		CaveBuilding cbBuilding = getCaveBuildingByNPCId(NPCId);
		if (cbBuilding == null) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(2);
		}
		if (cbBuilding.getType() < CAVE_BUILDING_TYPE_FIELD1) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(5);
		}
		CaveField caveField = (CaveField) cbBuilding;
		if (cfg.getFieldLvNeed() > caveField.getGrade()) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(6);
		}
		if (caveField.inBuilding()) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(3);
		}
		if (caveField.getPlantStatus() != null) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(4);
		}
		if (!getCurrRes().isEnough(cfg.getCost())) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(7);
		}
		if (player.getSilver() < cfg.getMoneyCost()) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(11);
		}
		int oneTimeMaxTimes = cfg.getAtOneTimeMaxTimes();// 最大同时种植限制

		int currentTimes = 0;
		for (CaveField cField : getFields()) {
			if (cField.getPlantStatus() != null) {
				if (cField.getPlantStatus().getPlantCfgId() == cfg.getId()) {
					currentTimes++;
				}
			}
		}
		if (currentTimes >= oneTimeMaxTimes) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(9);
		}

		CompoundReturn cr = modifyTodayPlantTimes(player, cfg);
		if (!cr.getBooleanValue()) {
			return cr;
		}

		try {
			if (cfg.getMoneyCost() > 0) {
				BillingCenter.getInstance().playerExpense(player, cfg.getMoneyCost(), CurrencyType.YINZI, ExpenseReasonType.仙府种植, "仙府种植-" + cfg.getName());
			}
		} catch (Exception e) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(1000);
		}
		getCurrRes().deduct(cfg.getCost());
		// TODO 真正执行操作
		caveField.doPlant(cfg);

		notifyFieldChange("currRes");
		notifyFieldChange(caveField.getType());

		if (CaveSubSystem.logger.isWarnEnabled()) {
			CaveSubSystem.logger.warn(player.getName() + "[种植完毕] [" + cfg.getName() + "] [种植状态:" + caveField.getPlantStatus() + "]");
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(true);
	}

	/**
	 * 取消种植某个田地上的植物<BR/>
	 * 1.所选的土地不存在<BR/>
	 * 2.土地正在升级<BR/>
	 * 3.没有正在生长的植物<BR/>
	 * 4.已经成熟了不能取消<BR/>
	 * @param player
	 * @param fieldType
	 * @return
	 */
	public CompoundReturn cancelPlanting(long NPCId, Player player) {
		CaveBuilding building = getCaveBuildingByNPCId(NPCId);
		if (building == null || !(building instanceof CaveField)) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(1);
		}
		CaveField caveField = (CaveField) building;
		if (caveField.inBuilding()) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(2);
		}
		if (caveField.getPlantStatus() == null) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(3);
		}
		if (caveField.getPlantStatus().getOutShowStatus() == PLANT_AVATA_STATUS_GROWNUP) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(4);
		}
		caveField.reset(player);
		if (CaveSubSystem.logger.isWarnEnabled()) {
			CaveSubSystem.logger.warn(player.getLogString() + "[取消种植{" + building.getNpc().getName() + "}] [当前资源:" + getCurrRes().toString() + "]");
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(true);
	}

	/**
	 * 摘取果实
	 * 1.土地不存在<BR/>
	 * 2.土地没有种植<BR/>
	 * 3.果实没有成熟<BR/>
	 * 4.果实已经摘完了<BR/>
	 * 5.剩余太少了别摘了<BR/>
	 * 6.你摘的太多了<BR/>
	 * 7.你的包太满了 请清理一下<BR/>
	 * 8.你今天偷取的果实太多了<BR/>
	 * 9.触发炸弹<BR/>
	 * @param fieldType
	 * @param isSelf
	 * @return
	 */
	public synchronized CompoundReturn pickFruit(Player player, long npcId) {
		long now = SystemTime.currentTimeMillis();
		CaveBuilding cBuilding = getCaveBuildingByNPCId(npcId);
		if (cBuilding == null || !(cBuilding instanceof CaveField)) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(1);
		}
		CaveField caveField = (CaveField) cBuilding;
		PlantStatus plantStatus = caveField.getPlantStatus();
		if (plantStatus == null) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(2);
		}
		if (plantStatus.getOutShowStatus() != PLANT_AVATA_STATUS_GROWNUP) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(3);
		}

		int leftNum = plantStatus.getLeftOutput();
		if (leftNum <= 0) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(4);
		}
		boolean isArticleFruit = plantStatus.getPlantCfg().getOutputType() == OUTPUT_TYPE_ARTICLE;
		boolean hasRandom = plantStatus.getPlantCfg().getExcessArticle() != null && !plantStatus.getPlantCfg().getExcessArticle().isEmpty();
		// 包满了[摘取物品](至少有2个位置才能放进去)
		Article article = ArticleManager.getInstance().getArticle(plantStatus.getPlantCfg().getOutputName());
		if (hasRandom && isArticleFruit && player.getKnapsack_common().getEmptyNum() < 2) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(7);
		}
		if (isArticleFruit && player.getKnapsack_common().getEmptyNum() < 1) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(7);
		}
		boolean isSelfCave = false;
		if (player.getId() != getOwnerId()) {// 不是主人摘取 偷取

			// 判断当天摘取次数,如果太多,则不让偷了每天100次
			if (!TimeTool.instance.isSame(player.getLastStealFruitTime(), now, TimeDistance.DAY)) {
				// 上次偷和现在不是一天,置零
				player.setDailyStealFruitNum(0);
			}
			if (player.getDailyStealFruitNum() >= DAILY_STEAL_MAXNUM) {
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(8);
			}

			if (leftNum <= (plantStatus.getTotalOutput() + 1) / 2) {
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(5);
			}

			// 是否有炸弹,有炸弹的是否触发 2014-5-14 10:07:11
			{
				CaveFieldBombData bombData = caveField.getCaveFieldBombData();
				if (bombData != null && bombData.isValid()) {// 有效的炸弹,判断几率
					CaveFieldBombConfig bombConfig = bombData.getBombConfig();
					if (bombConfig != null) {
						int rate = CaveFieldBombConfig.random.nextInt(100);
						if (rate <= bombConfig.getRate()) {
							/**
							 * 引发炸弹
							 * 1.给偷菜角色加一个buff
							 * 2.通知偷菜人
							 * 3.在动态里通知主人.
							 * 4.减少一次炸弹次数
							 */
							bombData.setLeftTimes(bombData.getLeftTimes() - 1);
							notifyFieldChange("fields");
							player.placeBuff(bombConfig.getBuffName(), bombConfig.getBuffLevel(), bombConfig.getBuffLast(), player);

							addDynamic(new CaveDynamic(player.getName(), Dynamic.被炸, Translate.translateString(Translate.偷菜被炸, new String[][] { { Translate.STRING_1, player.getName() }, { Translate.STRING_2, "<f color='" + ArticleManager.color_article[plantStatus.getOutputColor()] + "'>" + plantStatus.getPlantCfg().getName() + "</f>" }, { Translate.COUNT_1, String.valueOf(bombData.getLeftTimes()) } })));

							if (logger.isInfoEnabled()) {
								logger.info(player.getLogString() + " [偷菜] [被炸] [主人:" + getOwnerId() + " " + getOwnerName() + "] [炸弹剩余次数:" + bombData.getLeftTimes() + "] [炸弹信息: random:" + rate + " " + bombConfig.toString() + "]");
							}

							return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(9);
						} else {
							if (logger.isInfoEnabled()) logger.info(player.getLogString() + " [偷菜] [没有] [主人:" + getOwnerId() + " " + getOwnerName() + "] [炸弹剩余次数:" + bombData.getLeftTimes() + "] [炸弹信息: random:" + rate + " " + bombConfig.toString() + "]");
						}
					}
				}
			}

			if (caveField.getPickMap().get(player.getId()) != null) {
				if (caveField.getPickMap().get(player.getId()) >= PICK_MAX_NUM) {
					return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(6);
				}
			} else {
				caveField.getPickMap().put(player.getId(), 0);
			}
			// 判断通过 记录摘取个数
			caveField.getPickMap().put(player.getId(), caveField.getPickMap().get(player.getId()) + 1);

			TaskAction caveSteal = TaskActionOfCaveSteal.createTaskAction(plantStatus.getPlantCfg().getType());
			player.dealWithTaskAction(caveSteal);
			CaveDynamic caveDynamic = new CaveDynamic(player.getName(), Dynamic.偷菜, player.getName() + Translate.偷取了一个 + "<f color='" + ArticleManager.color_article[plantStatus.getOutputColor()] + "'>" + plantStatus.getPlantCfg().getName() + "</f>");
			addDynamic(caveDynamic);
			notifyFieldChange("fields");
			player.setLastStealFruitTime(now);
			player.setDailyStealFruitNum(player.getDailyStealFruitNum() + 1);
			{
				AchievementManager.getInstance().record(player, RecordAction.偷取农作物个数);
				if (plantStatus.getTreeColor() == 4) {// 橙色
					AchievementManager.getInstance().record(player, RecordAction.偷取橙色果实次数);
				}
				if (plantStatus.getTreeColor() == 3) {// 橙色
					ActivenessManager.getInstance().record(player, ActivenessType.偷紫色果实);
				}

				BillboardStatDate bsd = BillboardStatDateManager.getInstance().getBillboardStatDate(player.getId());
				int score = 0;
				if (bsd != null) {
					boolean isSelfCountry = true;
					if (getFaery() != null) {
						isSelfCountry = getFaery().getCountry() == player.getCountry();
					}
					score = FaeryManager.getFruitScore(plantStatus.getOutputColor(), isSelfCountry);
					bsd.setStealFruitNum(bsd.getStealFruitNum() + score);
				} else {
					CaveSubSystem.logger.error(player.getLogString() + "[偷取果子] [得分:" + score + "][记录不存在]");
				}

			}

		} else {// 自己庄园
			isSelfCave = true;
			TaskAction caveHarvest = TaskActionOfCaveHarvest.createTaskAction(plantStatus.getPlantCfg().getType());
			player.dealWithTaskAction(caveHarvest);
			{
				AchievementManager.getInstance().record(player, RecordAction.收获自己农作物个数);
			}
		}
		plantStatus.setLeftOutput(plantStatus.getLeftOutput() - 1);
		if (isArticleFruit) {
			try {
				ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.CREATE_REASON_PICK_CAVE_PLANT, player, plantStatus.getOutputColor(), 1, true);
				ArticleEntity[] aes = null;
				DropsArticle dropsArticle = plantStatus.getPlantCfg().getDropsArticle();
				if (dropsArticle == null) {// 没获得随机物品
					aes = new ArticleEntity[rewardTimes];
					for (int i = 0; i < rewardTimes; i++) {
						aes[i] = ae;
					}
				} else {
					Article articleDrop = ArticleManager.getInstance().getArticle(dropsArticle.getName());
					if (articleDrop != null) {
						ArticleEntity aedrop = ArticleEntityManager.getInstance().createEntity(articleDrop, dropsArticle.isBind(), ArticleEntityManager.CREATE_REASON_PICK_CAVE_PLANT, player, dropsArticle.getColor(), 1, true);
						aes = new ArticleEntity[rewardTimes + 1];
						for (int i = 0; i < rewardTimes; i++) {
							aes[i] = ae;
						}
						aes[rewardTimes] = aedrop;
					} else {
						aes = new ArticleEntity[rewardTimes];
						for (int i = 0; i < rewardTimes; i++) {
							aes[i] = ae;
						}
					}
				}
				for (ArticleEntity ee : aes) {
					if (ee != null) {
						ArticleEntity oldArticle = player.getArticleEntity(ee.getArticleName());
						long id = ee.getId();
						if (oldArticle != null) {
							id = oldArticle.getId();
						}
						NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, player.getId(), (byte) Event.GET_ARTICLE, id);
						player.addMessageToRightBag(req);
					}
				}
				boolean success = player.putAll(aes, "洞府");
				if (success) {
					for (int i = 0; i < aes.length; i++) {
						ArticleStatManager.addToArticleStat(player, null, aes[i], ArticleStatManager.OPERATION_物品获得和消耗, 0L, (byte) 0, 1L, isSelfCave ? "摘取庄园果实" : "偷取庄园果实", "");
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			if (plantStatus.getPlantCfg().getOutputType() == OUTPUT_TYPE_EXP) {
				// 加经验
				// TODO 经验计算公式
				int exp = (int) (plantStatus.getPlantCfg().getBaseExchangeNum() * FaeryManager.getInstance().getExchangeRate()[plantStatus.getOutputColor()]);
				player.addExp(exp * rewardTimes, ExperienceManager.ADDEXP_REASON_PICK_CAVE_PLANT);
			} else if (plantStatus.getPlantCfg().getOutputType() == OUTPUT_TYPE_MONEY) {
				// 增加绑银
				long addMoney = (long) (plantStatus.getPlantCfg().getBaseExchangeNum() * FaeryManager.getInstance().getExchangeRate()[plantStatus.getOutputColor()]);
				player.sendNotice(Translate.translateString(Translate.text_cave_059, new String[][] { { Translate.STRING_1, BillingCenter.得到带单位的银两(addMoney * rewardTimes) } }));
				try {
					BillingCenter.getInstance().playerSaving(player, addMoney * rewardTimes, CurrencyType.BIND_YINZI, SavingReasonType.CAVE_HAR, "");
					CaveSubSystem.logger.error(player.getLogString() + "[收获庄园摇钱树] [拾取成功] [树的名字:{}] [要保存的钱数:{}] [rewardTimes:{}]", new Object[] { cBuilding.getNpc().getName(), addMoney, rewardTimes });
				} catch (SavingFailedException e) {
					CaveSubSystem.logger.error(player.getLogString() + "[收获庄园摇钱树] [保存钱失败] [树的名字:{}] [要保存的钱数:{}]", new Object[] { cBuilding.getNpc().getName(), addMoney }, e);
				}
			}
		}
		if (plantStatus.getLeftOutput() <= 0) {
			caveField.setPlantStatus(null);
			caveField.setCaveFieldBombData(null);
			getBuildings().remove(caveField.getNpc().getId());
			getFaery().getGame().removeSprite(caveField.getNpc());
			caveField.initNpc(this);
		}
		caveField.modifyName();

		notifyFieldChange(caveField.getType());
		try {
			EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.PICK_FRUIT, new Object[] { player, plantStatus.getOutputColor() });
			EventRouter.getInst().addEvent(evt);
		} catch (Exception e) {
			HorseManager.logger.error("[新坐骑系统] [摘取果实伴生] [异常] [" + player.getLogString() + "]", e);
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(true);// .setStringValue(notice.toString());
	}

	private CaveField getCaveField(int fieldType) {
		for (CaveField caveField : getFields()) {
			if (caveField.getType() == fieldType) {
				return caveField;
			}
		}
		return null;
	}

	/**
	 * 通知玩家门的状态变化
	 */
	public void noticeDoorStatus() {
		ResourceManager.getInstance().setAvata((CaveDoorNPC) getFence().getNpc());
	}

	/**
	 * 关门
	 * @return
	 */
	public CompoundReturn closeDoor(Player player) {
		if (getFence().getOpenStatus() == FENCE_STATUS_CLOSE) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(1);
		}
		boolean isSelf = false;
		if (getOwnerId() == player.getId()) {
			isSelf = true;
		}
		getFence().setOpenStatus(FENCE_STATUS_CLOSE);
		notifyFieldChange("fence");
		((CaveDoorNPC) getFence().getNpc()).setIsClosed(true);
		((CaveDoorNPC) getFence().getNpc()).closeDoor(getFaery().getGame());
		noticeDoorStatus();
		if (!isSelf) {
			addDynamic(new CaveDynamic(player.getName(), Dynamic.关门, Translate.translateString(Translate.仙府关门, new String[][] { { Translate.STRING_2, player.getName() } })));
			if (logger.isInfoEnabled()) {
				logger.info("[" + this.getOwnerId() + " " + this.getOwnerName() + "] [仙府被关门] [关开门者:" + player.getLogString() + "]");
			}
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(true);
	}

	/**
	 * 打开庄园的门
	 * 1.钱不够支付进门费用
	 * 2.
	 * @param player
	 * @return
	 */
	public CompoundReturn openDoor(Player player) {
		// 门开着或者主人都不需要支付费用
		if (getFence().getStatus() == FENCE_STATUS_OPEN) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(true).setIntValue(2);
		}
		boolean isSelf = false;
		String openCost = "";
		if (getOwnerId() == player.getId()) {
			isSelf = true;
		} else {
			int cost = getFence().getEnterCost();

			if (!player.bindSilverEnough(cost)) {// 钱不够
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(1);
			}
			// 扣钱
			try {
				BillingCenter.getInstance().playerExpense(player, cost, CurrencyType.BIND_YINZI, ExpenseReasonType.ENTER_CAVE, "仙府开门");
				// 今天可以使用的绑银不够，用银子代替
				long todayCanUseBindSilver = player.getTodayCanUseBindSilver();
				if (todayCanUseBindSilver < cost) {
					player.sendNotice(Translate.translateString(Translate.text_cave_112, new String[][] { { Translate.STRING_1, BillingCenter.得到带单位的银两(cost) } }));
				} else {
					player.sendNotice(Translate.translateString(Translate.text_cave_073, new String[][] { { Translate.STRING_1, BillingCenter.得到带单位的银两(cost) } }));
				}
				openCost = BillingCenter.得到带单位的银两(cost);
			} catch (NoEnoughMoneyException e) {
				e.printStackTrace();
			} catch (BillFailedException e) {
				e.printStackTrace();
			}
		}
		getFence().setOpenStatus(FENCE_STATUS_OPEN);
		notifyFieldChange("fence");
		((CaveDoorNPC) getFence().getNpc()).setIsClosed(false);
		((CaveDoorNPC) getFence().getNpc()).openDoor(getFaery().getGame());
		notifyFieldChange(getFence().getType());
		noticeDoorStatus();
		if (!isSelf) {
			addDynamic(new CaveDynamic(player.getName(), Dynamic.开门, Translate.translateString(Translate.仙府开门, new String[][] { { Translate.STRING_1, player.getName() }, { Translate.STRING_2, openCost } })));
			if (logger.isInfoEnabled()) {
				logger.info("[" + this.getOwnerId() + " " + this.getOwnerName() + "] [仙府被开门] [开门者:" + player.getLogString() + "]");
			}
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(true);
	}

	/**
	 * 取出存放的宠物<BR/>
	 * 1.不能在其他人家取存放的宠物<BR/>
	 * 2.你的宠物包满了
	 * 3.所选的宠物不存在
	 * @param player
	 * @param articleId
	 * @return
	 */
	public synchronized CompoundReturn takeOutStorePet(Player player, long articleId) {

		if (player.getId() != getOwnerId()) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(1);
		}
		if (player.getPetKnapsack().isFull()) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(2);
		}

		int index = -1;
		for (int i = 0; i < getPethouse().getStorePets().length; i++) {
			if (getPethouse().getStorePets()[i] == articleId) {
				index = i;
				break;
			}
		}
		ArticleEntity ppe = ArticleEntityManager.getInstance().getEntity(articleId);
		if (index == -1) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(3);
		}
		if (ppe == null) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(3);
		}
		getPethouse().getStorePets()[index] = 0L;
		player.getPetKnapsack().put(ppe, "洞府");
		notifyFieldChange(getPethouse().getType());
		if (CaveSubSystem.logger.isWarnEnabled()) {
			CaveSubSystem.logger.warn(player.getLogString() + "[取出存储宠物] [成功] [储藏位置:" + index + "] [宠物ID:--] [宠物名字:--] [物品ID:" + ppe.getId() + "] [物品名字 :" + ppe.getArticleName() + "]");
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(true);
	}

	/**
	 * 存放一个宠物<BR/>
	 * 1.只能在自己家存放宠物<BR/>
	 * 2.出战状态的宠物不能存放<BR/>
	 * 3.挂机状态的宠物不能存放<BR/>
	 * 4.储存位已经满了<BR/>
	 * 5.选择的不是宠物<BR/>
	 * 6.选的位置已经有宠物了<BR/>
	 * 7.该位置还未开启<BR/>
	 * @param player
	 * @param pet
	 * @return
	 */
	public synchronized CompoundReturn storePet(Player player, long articleId, int index) {
		if (player.getId() != getOwnerId()) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(1);
		}
		ArticleEntity ae = player.getArticleEntity(articleId);
		PetPropsEntity petEntity = null;
		if (ae instanceof PetPropsEntity) {
			petEntity = (PetPropsEntity) ae;
		}
		if (petEntity == null) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(5);
		}
		Pet pet = PetManager.getInstance().getPet(petEntity.getPetId());
		if (player.getActivePetId() == pet.getId()) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(2);
		}
		if (pet.getHookInfo() != null) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(3);
		}
		int maxNum = FaeryManager.getInstance().getPethouseCfg()[getPethouse().getGrade() - 1].getStoreNum();
		if (index > maxNum - 1) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(7);
		}
		if (getPethouse().storeFull()) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(4);
		}
		if (getPethouse().getStorePets()[index] > 0) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(6);
		}
		getPethouse().getStorePets()[index] = articleId;
		player.getPetKnapsack().removeByArticleId(articleId, "洞府", false);

		notifyFieldChange(getPethouse().getType());
		if (CaveSubSystem.logger.isWarnEnabled()) {
			CaveSubSystem.logger.warn(player.getLogString() + "[存放宠物] [成功] [储藏位置:" + index + "] [宠物ID:" + pet.getId() + "] [宠物名字:" + pet.getName() + "] [物品ID:" + ae.getId() + "] [物品名字 :" + ae.getArticleName() + "]");
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(true);
	}

	/**
	 * 宠物挂机
	 * 1.宠物不存在<BR/>
	 * 2.出战的宠物不能挂机<BR/>
	 * 3.宠物在其他地方挂机<BR/>
	 * 4.挂机位满了<BR/>
	 * 5.宠物不能在自己家挂机<BR/>
	 * 6.所选的位置上有其他宠物在挂机<BR/>
	 * 7.只能在自己国家挂机<BR/>
	 * 8.该位置还没有开启<BR/>
	 * @param petOwner
	 * @param petId
	 * @return
	 */
	public CompoundReturn hookPet(Player petOwner, long articleId, int index) {

		PlayerSimpleInfo owner;
		try {
			owner = PlayerSimpleInfoManager.getInstance().getInfoById(this.getOwnerId());
		} catch (Exception e) {
			CaveSubSystem.logger.error(petOwner.getLogString() + "[挂机宠物:{}] [失败] [仙府主人:{}不存在]", new Object[] { articleId, this.getOwnerId() }, e);
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(7);
		}
		if (owner == null || owner.getCountry() != petOwner.getCountry()) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(7);
		}

		if (petOwner.getId() == getOwnerId()) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(5);
		}
		ArticleEntity ae = petOwner.getArticleEntity(articleId);
		PetPropsEntity petEntity = null;
		if (ae instanceof PetPropsEntity) {
			petEntity = (PetPropsEntity) ae;
		}
		if (petEntity == null) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(1);
		}
		Pet pet = PetManager.getInstance().getPet(petEntity.getPetId());
		if (pet == null) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(1);
		}
		if (petOwner.getActivePetId() == pet.getId()) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(2);
		}

		if (pet.getHookInfo() != null) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(3);
		}
		int maxNum = FaeryManager.getInstance().getPethouseCfg()[getPethouse().getGrade() - 1].getHookNum();
		if (index > maxNum - 1) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(8);
		}
		if (getPethouse().hookFull()) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(4);
		}

		if (getPethouse().getHookInfos()[index] != null) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(6);
		}
		PetHookInfo hookInfo = new PetHookInfo(petOwner, pet, this.getOwnerId(), this.getOwnerName(), getId(), articleId);
		pet.setHookInfo(hookInfo);
		this.getPethouse().getHookInfos()[index] = (hookInfo);
		notifyFieldChange(this.getPethouse().getType());
		if (CaveSubSystem.logger.isWarnEnabled()) {
			CaveSubSystem.logger.warn(petOwner.getLogString() + "[宠物挂机] [成功] [宠物房主人:" + getOwnerId() + "] [挂机位置:" + index + "] [宠物ID:" + pet.getId() + "] [宠物名字:" + pet.getName() + "] [物品ID:" + ae.getId() + "] [物品名字:" + ae.getArticleName() + "]");
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(true).setObjValue(hookInfo);
	}

	/**
	 * 取出挂机宠物<BR/>
	 * 1.宠物不是你的<BR/>
	 * 2.宠物不再挂机<BR/>
	 * @param petOwner
	 * @param petId
	 * @param force是否是强制取出
	 *            强制取出不计算经验
	 * @return
	 */
	/**
	 * @param petOwner
	 * @param articleId
	 * @param force
	 * @return
	 */
	public CompoundReturn takeOutHookPet(Player petOwner, long articleId, boolean force) {
		long petId = 0;
		ArticleEntity articleEntity = ArticleEntityManager.getInstance().getEntity(articleId);
		if (articleEntity != null && articleEntity instanceof PetPropsEntity) {
			petId = ((PetPropsEntity) articleEntity).getPetId();
		} else {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(1);
		}

		try {
			PetHookInfo[] infos = getPethouse().getHookInfos();
			Pet pet = PetManager.getInstance().getPet(petId);
			PetHookInfo hookInfo = null;
			int index = -1;
			for (int i = 0; i < infos.length; i++) {
				if (infos[i] != null) {
					if (infos[i].getPetId() == petId) {
						hookInfo = infos[i];
						index = i;
						break;
					}
				}
			}
			if (CaveSubSystem.logger.isInfoEnabled()) {
				CaveSubSystem.logger.info(getOwnerName() + "[赶走宠物] [所在位置:{}] [挂机信息:{}]", new Object[] { index, hookInfo });
			}
			if (petOwner == null) {
				try {
					petOwner = PlayerManager.getInstance().getPlayer(hookInfo.getPetOwnerId());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (hookInfo == null) {
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(2);
			}
			if (petOwner == null || petOwner.getId() != hookInfo.getPetOwnerId() && !force) {
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(1);
			}
			int hookExp = getHookExp(hookInfo);
			if (!force) {
				// 计算经验
				pet.addExp(hookExp, PetExperienceManager.ADDEXP_REASON_HOOK);
				if (CaveSubSystem.logger.isWarnEnabled()) {
					CaveSubSystem.logger.warn(petOwner.getLogString() + "[取出挂机宠物] [hookExp:" + hookExp + "] [hookInfo:" + hookInfo.toString() + "]");
				}
			} else {
				// 给主人增加经验丹
				Article article = ArticleManager.getInstance().getArticle(FaeryManager.driveArticleName);
				Player caveOwner = GamePlayerManager.getInstance().getPlayer(this.ownerId);
				if (article != null) {
					ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.CREATE_REASON_DRIVEPET, caveOwner, 0, 1, true);
					if (ae instanceof SingleForPetPropsEntity) {
						SingleForPetPropsEntity sfpe = (SingleForPetPropsEntity) ae;
						sfpe.setExp(hookExp);
						if (CaveSubSystem.logger.isWarnEnabled()) {
							CaveSubSystem.logger.warn(caveOwner.getLogString() + "[驱赶挂机宠物:{}] [给主人经验丹:{}] [经验:{}] [hookInfo:" + hookInfo.toString() + "]", new Object[] { FaeryManager.driveArticleName, sfpe, hookExp });
						}

						boolean addSucc = caveOwner.putToKnapsacks(ae, "洞府");
						if (!addSucc) {
							MailManager.getInstance().sendMail(caveOwner.getId(), new ArticleEntity[] { ae }, Translate.仙府, "", 0, 0, 0, Translate.驱赶宠物);
						}
					} else {
						CaveSubSystem.logger.error(caveOwner.getLogString() + "[驱赶挂机宠物][给主人物品类型异常][{}][{}]", new Object[] { FaeryManager.driveArticleName, ae.getClass() });
					}
				} else {
					if (CaveSubSystem.logger.isInfoEnabled()) CaveSubSystem.logger.info(caveOwner.getLogString() + "[驱赶挂机宠物] [给主人物品不存在][{}]", new Object[] { FaeryManager.driveArticleName });
				}
			}
			pet.setHookInfo(null);
			infos[index] = null;

			notifyFieldChange(getPethouse().getType());
		} catch (Exception e) {
			CaveSubSystem.logger.error("在挂机房取出宠物", e);
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(true);
	}

	/**
	 * 计算宠物挂机经验
	 * @param hookInfo
	 * @return
	 */
	public int getHookExp(PetHookInfo hookInfo) {
		Double petExpPerSecond = FaeryManager.getInstance().getPetHookExp().get(hookInfo.getPetGrade());
		if (petExpPerSecond == null) {
			petExpPerSecond = 1d;
			if (CaveSubSystem.logger.isWarnEnabled()) {
				CaveSubSystem.logger.warn("[宠物经验配置缺失] [等级:{}]", new Object[] { hookInfo.getPetGrade() });
			}
		}
		double expParm = FaeryManager.getInstance().getPethouseCfg()[getPethouse().getGrade() - 1].getExpParm();// 经验参数
		long hookTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - hookInfo.getHookTime();// 挂机时间
		hookTime = hookTime > MAX_HOOK_TIME ? MAX_HOOK_TIME : hookTime;
		return (int) (petExpPerSecond * expParm * hookTime / 1000);
		//(基础经验  * 经验系数  * 挂架时间  * 祝福系数)/ 1000
	}

	/**
	 * 建筑升级<br/>
	 * 0.成功<br/>
	 * 1.建筑不存在<br/>
	 * 2.已经到最大等级了<br/>
	 * 3.建筑不能升级<br/>
	 * 4.资源不足<br/>
	 * 5.等级过高,不能超过主建筑<br/>
	 * 6.建筑正在升级状态,不能再升级<br/>
	 * 7.没可用队列<br/>
	 * 8.土地需要开垦才能升级<br/>
	 * 9.已经到最大等级,不能升级了<br/>
	 * @return
	 */
	public synchronized CompoundReturn lvUp(CaveBuilding building, Player player) {
		// 建筑不存在
		if (building == null) {
			return CompoundReturn.createCompoundReturn().setIntValue(1).setBooleanValue(false);
		}
		if (building.getGrade() >= FaeryConfig.BUILDING_MAX_LEVEL) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(9);
		}
		if (building.getStatus() == CAVE_BUILDING_STATUS_LVUPING) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(6);
		}
		int type = building.getType();
		// 等级不低于最高建筑
		if (type != CAVE_BUILDING_TYPE_MAIN && building.getGrade() >= getMainBuilding().getGrade()) {
			return CompoundReturn.createCompoundReturn().setIntValue(5).setBooleanValue(false);
		}
		if (!hasScheduleLeft()) {
			return CompoundReturn.createCompoundReturn().setIntValue(7).setBooleanValue(false);
		}
		if (building.getType() > CAVE_BUILDING_TYPE_FENCE) {// 是土地 判断是否是土地。土地需要开垦才能升级
			CaveField caveField = (CaveField) building;
			if (caveField.getAssartStatus() == FIELD_STATUS_DESOLATION) {
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(8);
			}
		}
		try {
			CompoundReturn info = FaeryManager.getInstance().getLvUpInfo(type, building.getGrade());
			ResourceCollection cost = (ResourceCollection) info.getObjValue();
			if (!getCurrRes().isEnough(cost)) {
				if (CaveSubSystem.logger.isWarnEnabled()) {
					CaveSubSystem.logger.warn(player.getLogString() + "[升级仙府] [资源不足] [" + building.getNpc().getName() + "] [现有资源:" + getCurrRes().toString() + "] [需要资源:" + cost.toString() + "]");
				}
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(4);
			}
			getCurrRes().deduct(cost);
			notifyFieldChange("currRes");
			building.setLvUpStartTime(SystemTime.currentTimeMillis());
			building.setStatus(CAVE_BUILDING_STATUS_LVUPING);

			((CaveNPC) building.getNpc()).setInBuilding(true);
			ResourceManager.getInstance().setAvata(((CaveNPC) building.getNpc()));

			CompoundReturn cr = FaeryManager.getInstance().getLvUpInfo(building.getType(), building.getGrade());

			CaveSchedule caveSchedule = new CaveSchedule(FaeryManager.getBasename(building.getNpc().getName()) + building.getGrade() + Translate.级升级, building.getType(), OPTION_LEVEL_UP, cr.getIntValue() * 1000);
			building.getSchedules().add(caveSchedule);
			building.getCave().getSchedules().add(caveSchedule);
			building.modifyName();
			notifyFieldChange(building.getType());

			player.addMessageToRightBag(CaveSubSystem.getInstance().getCave_DETAILED_INFO_RES(player, null));

			if (CaveSubSystem.logger.isWarnEnabled()) {
				CaveSubSystem.logger.warn(player.getLogString() + "[升级仙府] [成功] [" + building.getNpc().getName() + "] [现有资源:" + getCurrRes().toString() + "] [消耗了资源:" + cost.toString() + "]");
			}

		} catch (OutOfMaxLevelException e) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(2);
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(true).setIntValue(0);
	}

	public void notifyFieldChange(String fieldName) {
		FaeryManager.caveEm.notifyFieldChange(this, fieldName);
	}

	public void notifyFieldChange(int buildingType) {
		switch (buildingType) {
		case CAVE_BUILDING_TYPE_MAIN:
			notifyFieldChange("mainBuilding");
			break;
		case CAVE_BUILDING_TYPE_STORE:
			notifyFieldChange("storehouse");
			break;
		case CAVE_BUILDING_TYPE_PETHOUSE:
			notifyFieldChange("pethouse");
			break;
		case CAVE_BUILDING_TYPE_FENCE:
			notifyFieldChange("fence");
			break;
		case CAVE_BUILDING_TYPE_DOORPLATE:
			notifyFieldChange("doorplate");
			break;
		case CAVE_BUILDING_TYPE_FIELD1:
		case CAVE_BUILDING_TYPE_FIELD2:
		case CAVE_BUILDING_TYPE_FIELD3:
		case CAVE_BUILDING_TYPE_FIELD4:
		case CAVE_BUILDING_TYPE_FIELD5:
		case CAVE_BUILDING_TYPE_FIELD6:
			notifyFieldChange("fields");
			break;
		default:
			break;
		}
	}

	/**
	 * 兑换资源<BR/>
	 * 1.物品不存在<BR/>
	 * 2.不是果实,不能兑换<BR/>
	 * 3.服务器异常<BR/>
	 * 4.数量不足<BR/>
	 * 5.超过上限<BR/>
	 * @param articleEntity
	 * @param num
	 * @return
	 */
	public CompoundReturn exchangeRes(long articleEntityId, int num) {
		try {
			Player owner = PlayerManager.getInstance().getPlayer(getOwnerId());
			ArticleEntity articleEntity = owner.getArticleEntity(articleEntityId);
			if (articleEntity == null) {
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(1);
			}
			Article article = ArticleManager.getInstance().getArticle(articleEntity.getArticleName());
			if (article == null) {
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(1);
			}
			PlantCfg cfg = FaeryManager.getInstance().getPlantCfg(articleEntity.getArticleName());
			if (cfg == null || !cfg.outputIsConvertFruit()) {// 不是庄园果实类型
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(2);
			}
			int hasNum = owner.getArticleEntityNum(articleEntityId);
			if (hasNum < num) {
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(4);
			}

			try {
				ResourceCollection add = new ResourceCollection(cfg.getType(), getResNum(articleEntity.getColorType(), cfg, num));
				ResourceCollection curr = (ResourceCollection) getCurrRes().clone();
				ResourceCollection currMax = getCurrMaxResource();

				if (curr.unite(add).moreThan(currMax)) {
					return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(5);
				}

				getCurrRes().unite(add);
				notifyFieldChange("currRes");

				if (!"".equals(add.getNoticleString())) {
					owner.sendNotice(Translate.获得资源 + add.getNoticleString());
				}

				for (int i = 0; i < num; i++) {
					ArticleEntity ae = null;
					try {
						ae = owner.removeFromKnapsacks(articleEntityId, "洞府", true);
						ArticleStatManager.addToArticleStat(owner, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0L, (byte) 0, 1L, "庄园果实兑换", "");
					} catch (Exception e) {
						if (logger.isInfoEnabled()) logger.info("[扣除兑换果实异常]", e);
					}
				}
				if (logger.isInfoEnabled()) {
					logger.info(owner.getLogString() + " [兑换资源] [成功] [物品:{}] [颜色:{}] [{}个] [兑换的资源:{}]", new Object[] { articleEntity.getArticleName(), articleEntity.getColorType(), num, add.toString() });
				}
			} catch (Exception e) {
				CaveSubSystem.logger.error("[合并/创建资源异常] [caveId:{}]", new Object[] { getId() }, e);
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(3);
			}
			return CompoundReturn.createCompoundReturn().setBooleanValue(true);
		} catch (Exception e) {
			logger.error("[兑换果实异常][caveId:{}]", new Object[] { getId() }, e);
			return null;
		}
	}

	/**
	 * 开垦土地<BR/>
	 * 要按顺序开启<BR/>
	 * 1.选的不是土地<BR/>
	 * 2.选的土地已经开垦<BR/>
	 * 3.已经达到上限<BR/>
	 * 4.田地令不足<BR/>
	 * 5.物品不存在<BR/>
	 * @param type
	 * @param player
	 * @return
	 */
	public CompoundReturn assartField(int type, Player player) {
		if (type < CAVE_BUILDING_TYPE_FIELD1) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(1);
		}
		CaveBuilding building = getCaveBuilding(type);
		if (building.getGrade() >= 1) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(2);
		}
		int hasFiledNum = 0;
		for (CaveField field : getFields()) {
			if (field.getGrade() > 0) {
				hasFiledNum++;
			}
		}
		int fieldLimit = FaeryManager.getInstance().getMainCfgs()[getMainBuilding().getGrade() - 1].getFieldNumLimit();
		if (fieldLimit <= hasFiledNum) {
			if (logger.isInfoEnabled()) logger.info("[开垦田地][失败][庄园等级:{}][已有田地:{}][田地上限:{}]", new Object[] { getMainBuilding().getGrade(), hasFiledNum, fieldLimit });
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(3);
		}
		// 田地开垦消耗
		FieldAssartCfg assartCfg = FaeryManager.getInstance().getFieldAssartCfgs()[hasFiledNum];
		String articleName = assartCfg.getArticleName();
		int costNum = assartCfg.getCostNum();
		Article article = ArticleManager.getInstance().getArticle(articleName);
		if (article == null) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(5);
		}

		int hasNum = player.getArticleEntityNum(articleName);

		if (logger.isInfoEnabled()) {
			logger.info(player.getLogString() + "[需要:{}][需要:{}个][已有:{}]", new Object[] { articleName, costNum, hasNum });
		}
		if (hasNum < costNum) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(4).setStringValues(new String[] { articleName, String.valueOf(costNum), String.valueOf(hasNum) });
		}
		for (int i = 0; i < costNum; i++) {
			ArticleEntity ae = player.getArticleEntity(articleName);
			player.removeFromKnapsacks(ae, "洞府", true);
			ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0L, (byte) 0, 1L, "庄园开垦田地扣除", "");
		}
		building.setGrade(1);
		faery.getGame().removeSprite(building.getNpc());
		((CaveField) building).setAssartStatus(FIELD_STATUS_UNPLANTING);
		building.initNpc(this);
		building.modifyName();

		notifyFieldChange(building.getType());

		player.sendError(Translate.translateString(Translate.text_cave_087, new String[][] { { Translate.COUNT_1, String.valueOf(costNum) }, { Translate.STRING_1, articleName }, { Translate.STRING_2, building.getNpc().getName() } }));
		{
			AchievementManager.getInstance().record(player, RecordAction.仙府开垦土地次数);
		}

		return CompoundReturn.createCompoundReturn().setBooleanValue(true);
	}

	/**
	 * 是否还有剩余的可用的进度
	 * @return
	 */
	public boolean hasScheduleLeft() {
		return getCurrMaxScheduleNum() > getSchedules().size();
	}

	/**
	 * 获得当前的最大任务数
	 * @return
	 */
	public int getCurrMaxScheduleNum() {
		// TODO 这里可能要有时间操作
		int addNum = 0;
		if (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() <= (getIncreaseScheduleStart() + getIncreaseScheduleLast())) {
			addNum = getIncreaseScheduleNum();
		}
		return DEFAULT_MAX_ACTIVE_SCHEDUL + addNum;
	}

	// TODO
	/**
	 * 计算兑换的公式
	 * @param color
	 * @param grade
	 * @return
	 */
	public static int getResNum(int color, PlantCfg cfg, int mum) {
		return (int) (FaeryManager.getInstance().getExchangeRate()[color] * cfg.getBaseExchangeNum() * mum);
	}

	/**
	 * 通过类型获得建筑
	 * @param type
	 * @return
	 */
	public CaveBuilding getCaveBuilding(int type) {
		switch (type) {
		case CAVE_BUILDING_TYPE_MAIN:
			return getMainBuilding();
		case CAVE_BUILDING_TYPE_STORE:
			return getStorehouse();
		case CAVE_BUILDING_TYPE_PETHOUSE:
			return getPethouse();
		case CAVE_BUILDING_TYPE_FENCE:
			return getFence();
		case CAVE_BUILDING_TYPE_DOORPLATE:
			return getDoorplate();
		case CAVE_BUILDING_TYPE_FIELD1:
		case CAVE_BUILDING_TYPE_FIELD2:
		case CAVE_BUILDING_TYPE_FIELD3:
		case CAVE_BUILDING_TYPE_FIELD4:
		case CAVE_BUILDING_TYPE_FIELD5:
		case CAVE_BUILDING_TYPE_FIELD6:
			for (int i = 0; i < getFields().size(); i++) {
				if (getFields().get(i).getType() == type) {
					return getFields().get(i);
				}
			}
			return null;
		default:
			break;
		}
		return null;
	}

	/**
	 * 加载的时候初始化NPC
	 */
	public void onLoadInitNpc() {
		if (getStatus() != CAVE_STATUS_DELETE) {
			this.setOwnerLastSeeDynamic(SystemTime.currentTimeMillis());
			getMainBuilding().initNpc(this);
			getStorehouse().initNpc(this);
			getPethouse().initNpc(this);
			getDoorplate().initNpc(this);
			getFence().initNpc(this);
			for (CaveField field : getFields()) {
				if (field.getPlantStatus() != null) {
					field.getPlantStatus().initCfg();
				}
				field.initNpc(this);
			}
		}
	}

	/**
	 * 得到当前等级最大资源上限
	 * @return
	 */
	public ResourceCollection getCurrMaxResource() {
		int maxWood = getStorehouse().getWoodLevel();
		int maxFood = getStorehouse().getFoodLevel();
		int maxStone = getStorehouse().getStoneLevel();
		return new ResourceCollection(FaeryManager.getInstance().getResource(maxFood), FaeryManager.getInstance().getResource(maxWood), FaeryManager.getInstance().getResource(maxStone));
	}

	public CaveBuilding getCaveBuildingByNPCId(long NPCId) {
		return getBuildings().get(NPCId);
	}

	/**
	 * 加载所有调度任务
	 */
	public void initSchedule() {
		getSchedules().clear();
		for (Iterator<Long> it = getBuildings().keySet().iterator(); it.hasNext();) {
			long id = it.next();
			CaveBuilding cBuilding = getBuildings().get(id);
			if (cBuilding != null) {
				getSchedules().addAll(cBuilding.getSchedules());
			}

		}
	}

	/********************************************* getters setters *********************************************/

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
		FaeryManager.caveEm.notifyFieldChange(this, "ownerId");
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
		FaeryManager.caveEm.notifyFieldChange(this, "ownerName");
	}

	public long getOwnerGrade() {
		return ownerGrade;
	}

	public void setOwnerGrade(long ownerGrade) {
		this.ownerGrade = ownerGrade;
		FaeryManager.caveEm.notifyFieldChange(this, "ownerGrade");
	}

	public CaveMainBuilding getMainBuilding() {
		return mainBuilding;
	}

	public void setMainBuilding(CaveMainBuilding mainBuilding) {
		this.mainBuilding = mainBuilding;
		FaeryManager.caveEm.notifyFieldChange(this, "mainBuilding");
	}

	public CaveStorehouse getStorehouse() {
		return storehouse;
	}

	public void setStorehouse(CaveStorehouse storehouse) {
		this.storehouse = storehouse;
		FaeryManager.caveEm.notifyFieldChange(this, "storehouse");
	}

	public CavePethouse getPethouse() {
		return pethouse;
	}

	public void setPethouse(CavePethouse pethouse) {
		this.pethouse = pethouse;
		FaeryManager.caveEm.notifyFieldChange(this, "pethouse");
	}

	public CaveFence getFence() {
		return fence;
	}

	public void setFence(CaveFence fence) {
		this.fence = fence;
		FaeryManager.caveEm.notifyFieldChange(this, "fence");
	}

	public CaveDoorplate getDoorplate() {
		return doorplate;
	}

	public void setDoorplate(CaveDoorplate doorplate) {
		this.doorplate = doorplate;
		FaeryManager.caveEm.notifyFieldChange(this, "doorplate");
	}

	public ArrayList<CaveField> getFields() {
		return fields;
	}

	public void setFields(ArrayList<CaveField> fields) {
		this.fields = fields;
		FaeryManager.caveEm.notifyFieldChange(this, "fields");
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
		FaeryManager.caveEm.notifyFieldChange(this, "status");
	}

	public long getOwnerLastVisitTime() {
		return ownerLastVisitTime;
	}

	public void setOwnerLastVisitTime(long ownerLastVisitTime) {
		this.ownerLastVisitTime = ownerLastVisitTime;
		FaeryManager.caveEm.notifyFieldChange(this, "ownerLastVisitTime");
	}

	public ResourceCollection getCurrRes() {
		return currRes;
	}

	public void setCurrRes(ResourceCollection currRes) {
		this.currRes = currRes;
		FaeryManager.caveEm.notifyFieldChange(this, "currRes");
	}

	public long getKhatamTime() {
		return khatamTime;
	}

	public void setKhatamTime(long khatamTime) {
		this.khatamTime = khatamTime;
		FaeryManager.caveEm.notifyFieldChange(this, "khatamTime");
	}

	public Map<String, PlantRecord> getPlantRecord() {
		return plantRecord;
	}

	public void setPlantRecord(Map<String, PlantRecord> plantRecord) {
		this.plantRecord = plantRecord;
		FaeryManager.caveEm.notifyFieldChange(this, "plantRecord");
	}

	public Hashtable<Long, CaveBuilding> getBuildings() {
		return buildings;
	}

	public void setBuildings(Hashtable<Long, CaveBuilding> buildings) {
		this.buildings = buildings;
	}

	public long getLastMaintenanceTime() {
		return lastMaintenanceTime;
	}

	public void setLastMaintenanceTime(long lastMaintenanceTime) {
		this.lastMaintenanceTime = lastMaintenanceTime;
		FaeryManager.caveEm.notifyFieldChange(this, "lastMaintenanceTime");
	}

	public Faery getFaery() {
		return faery;
	}

	public void setFaery(Faery faery) {
		this.faery = faery;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public List<CaveSchedule> getSchedules() {
		return schedules;
	}

	public void setSchedules(ArrayList<CaveSchedule> schedules) {
		this.schedules = schedules;
	}

	public long getIncreaseScheduleStart() {
		return increaseScheduleStart;
	}

	public void setIncreaseScheduleStart(long increaseScheduleStart) {
		this.increaseScheduleStart = increaseScheduleStart;
		FaeryManager.caveEm.notifyFieldChange(this, "increaseScheduleStart");
	}

	public long getIncreaseScheduleLast() {
		return increaseScheduleLast;
	}

	public void setIncreaseScheduleLast(long increaseScheduleLast) {
		this.increaseScheduleLast = increaseScheduleLast;
		FaeryManager.caveEm.notifyFieldChange(this, "increaseScheduleLast");
	}

	public int getIncreaseScheduleNum() {
		return increaseScheduleNum;
	}

	public void setIncreaseScheduleNum(int increaseScheduleNum) {
		this.increaseScheduleNum = increaseScheduleNum;
		FaeryManager.caveEm.notifyFieldChange(this, "increaseScheduleNum");
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public List<CaveDynamic> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<CaveDynamic> recordList) {
		this.recordList = recordList;
	}

	/**
	 * 添加一条动态信息
	 * 同时通知玩家,
	 * @param caveDynamic
	 */
	public synchronized void addDynamic(CaveDynamic caveDynamic) {
		if (recordList == null) {
			recordList = new ArrayList<CaveDynamic>();
		}
		if (recordList.size() >= maxRecordNum) {
			for (int i = 0; i < recordList.size() - maxRecordNum + 1; i++) {
				recordList.remove(0);
			}
		}
		recordList.add(caveDynamic);

		noticeOwnerDynamic();

	}

	/**
	 * 通知主人动态
	 */
	public void noticeOwnerDynamic() {
		if (!GamePlayerManager.getInstance().isOnline(getOwnerId())) {
			return;
		}
		long ownerLastSeeDynamic = getOwnerLastSeeDynamic();
		int num = 0;
		for (int i = 0; i < recordList.size(); i++) {
			CaveDynamic cd = recordList.get(i);
			if (cd.getTime() > ownerLastSeeDynamic) {
				num = recordList.size() - i;
				break;
			}
		}

		if (num > 0) {
			CAVE_DYNAMIC_NOTICE_REQ req = new CAVE_DYNAMIC_NOTICE_REQ(GameMessageFactory.nextSequnceNum(), num);
			try {
				Player owner = GamePlayerManager.getInstance().getPlayer(getOwnerId());
				owner.addMessageToRightBag(req);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 更改种植
	 * 1)当前组今天已经达到了种植上限
	 * 2)异常
	 * @param player
	 * @param cfg
	 * @return
	 */
	public synchronized CompoundReturn modifyTodayPlantTimes(Player player, PlantCfg cfg) {
		String groupName = cfg.getPlantGroup();
		if (!getPlantRecord().containsKey(groupName)) {
			if (CaveSubSystem.logger.isWarnEnabled()) {
				CaveSubSystem.logger.warn(player.getLogString() + "[种植] [第一次种] []", new Object[] { cfg.toString() });
			}
			PlantRecord record = new PlantRecord(cfg.getPlantGroup(), 0, 0L);
			getPlantRecord().put(record.getGroupName(), record);
			FaeryManager.caveEm.notifyFieldChange(this, "plantRecord");
		}

		PlantRecord record = getPlantRecord().get(groupName);
		if (record == null) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(2);
		}
		long now = SystemTime.currentTimeMillis();
		if (TimeTool.instance.isSame(now, record.getLastPlantTime(), TimeDistance.DAY)) {
			if (CaveSubSystem.logger.isInfoEnabled()) {
				CaveSubSystem.logger.info(player.getLogString() + "[种植] [判断通过] [{}] [{}]", new Object[] { cfg.toString(), record.toString() });
			}
			boolean canPlant = record.getPlantCount() < (cfg.getDailyMaxTimes() + cfg.getCVipAddTimes(player.getVipLevel()));
			if (CaveSubSystem.logger.isInfoEnabled()) {
				CaveSubSystem.logger.info(player.getLogString() + "[种植:" + cfg.getPlantGroup() + "] [VIP增加次数:" + cfg.getCVipAddTimes(player.getVipLevel()) + "]");
			}
			if (canPlant) {
				record.setPlantCount(record.getPlantCount() + 1);
				record.setLastPlantTime(now);
				FaeryManager.caveEm.notifyFieldChange(this, "plantRecord");
			}
			return CompoundReturn.createCompoundReturn().setBooleanValue(canPlant).setIntValue(canPlant ? 0 : 10);
		} else {
			if (CaveSubSystem.logger.isInfoEnabled()) {
				CaveSubSystem.logger.info(player.getLogString() + "[种植] [当天第一次种,直接返回] []", new Object[] { cfg.toString(), record.toString() });
			}
			FaeryManager.caveEm.notifyFieldChange(this, "plantRecord");
			record.setLastPlantTime(now);
			record.setPlantCount(1);
			return CompoundReturn.createCompoundReturn().setBooleanValue(true);
		}
	}

	private CompoundReturn isCaveNPC(String name) {
		for (Iterator<Long> itor = buildings.keySet().iterator(); itor.hasNext();) {
			CaveBuilding building = buildings.get(itor.next());
			if (building.getNpc() != null) {
				if (building.getNpc().getName().endsWith(name)) {
					return CompoundReturn.createCompoundReturn().setBooleanValue(true).setObjValue(building.getNpc());
				}
			}
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(false);
	}

	public void modifyTask(Task task) {
		CompoundReturn cr = isCaveNPC(task.getStartNpc());
		if (cr.getBooleanValue()) {
			NPC npc = (NPC) cr.getObjValue();
			task.setStartNpc(npc.getName());
			task.setStartX(npc.getX());
			task.setStartY(npc.getY());
		}
		cr = isCaveNPC(task.getEndNpc());
		if (cr.getBooleanValue()) {
			NPC npc = (NPC) cr.getObjValue();
			task.setEndNpc(npc.getName());
			task.setEndX(npc.getX());
			task.setEndY(npc.getY());
		}
	}

	/**
	 * 得到维护费用
	 * @return
	 */
	public ResourceCollection getMaintenanceCost() {
		ResourceCollection cost = new ResourceCollection(0, 0, 0);
		if (getMainBuilding().getGrade() >= FaeryManager.maintenanceGrade) {
			for (Iterator<Long> itor = buildings.keySet().iterator(); itor.hasNext();) {
				long id = itor.next();
				CaveBuilding cb = buildings.get(id);
				cost.unite(FaeryManager.getMaintenanceCost(cb.getType(), cb.getGrade()));
			}
			MaintanceActivity currMA=null;
			ExchangeActivityManager eam=ExchangeActivityManager.getInstance();
			for(MaintanceActivity ma:eam.maintanceActivities){
				if(ma.isThisServerFit()==null){
					currMA=ma;
				}
			}
			if(null!=currMA){
				Double rate=currMA.getRate();
				cost.setFood((int)Math.floor(cost.getFood()*(1-rate)));
				cost.setStone((int)Math.floor(cost.getStone()*(1-rate)));
				cost.setWood((int)Math.floor(cost.getWood()*(1-rate)));
				ActivitySubSystem.logger.warn("[固定时间内庄园维护费用按比例减少] [减少比例:"+rate+"]");
			}
		}
		return cost;
	}

	public static TimeDistance maintenanceTD = TimeDistance.DAY;

	/**
	 * 仙府是否在锁定状态
	 * @return
	 */
	public boolean isLocked() {
		return this.getStatus() == CAVE_STATUS_LOCKED;
	}

	/**
	 * 维护
	 * 1)如果上次维护时间距离现在>=1天,则需要维护
	 * 2)如果维护费用够,则扣除维护费用并且通知用户
	 * 3)如果维护费用不足,仙府进入锁定状态,除收获作物外的操作都被禁止
	 * 4)如果锁定状态达1天,A)如果维护费用依旧不足,则降级并返还一定资源,B)如果维护费用足够,则扣除费用,并将仙府状态设置为正常
	 */
	public synchronized void maintenance() {
		long now = SystemTime.currentTimeMillis();
		boolean sameDay = TimeTool.instance.isSame(now, getLastMaintenanceTime(), maintenanceTD);
		if (!sameDay) {
			boolean isLocked = this.getStatus() == CAVE_STATUS_LOCKED; // 仙府是否是锁定状态

			if (getMainBuilding().getGrade() >= FaeryManager.maintenanceGrade) {
				setLastMaintenanceTime(now);
				ResourceCollection maintenanceCost = getMaintenanceCost();
				if (getCurrRes().isEnough(maintenanceCost)) {// 足够扣除
					getCurrRes().deduct(maintenanceCost);
					if (isLocked) {
						setStatus(CAVE_STATUS_OPEN);
						if (GamePlayerManager.getInstance().isOnline(getOwnerId())) {
							Player player;
							try {
								player = GamePlayerManager.getInstance().getPlayer(getOwnerId());
								player.addMessageToRightBag(FaeryManager.getInstance().caveSimple(player, null));
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					notifyFieldChange("currRes");
					addDynamic(new CaveDynamic(Translate.系统, Dynamic.扣除维护费用, Translate.仙府维护 + "<f color='#ff0000' style='blod'>" + maintenanceCost.toString() + "</f>"));
					if (CaveSubSystem.logger.isWarnEnabled()) {
						CaveSubSystem.logger.warn("[每天维护仙府] [资源足够] [{}] [扣除主人的资源] [成功] [主人:{}] [剩余资源:{}]", new Object[] { maintenanceCost.toString(), getOwnerName(), getCurrRes().toString() });
					}
				} else {// 资源不足
					if (isLocked) { // 降级
						int maxLevel = getMainBuilding().getGrade() - 1;
						StringBuffer sbf = new StringBuffer();
						ResourceCollection totalRebackResourse = new ResourceCollection();
						CaveBuilding[] cbs = getBuildings().values().toArray(new CaveBuilding[0]);
						for (CaveBuilding cb : cbs) {
							if (cb == null) {
								continue;
							}
							if (cb.getGrade() > maxLevel) {
								ResourceCollection rebackResourse = FaeryManager.getRebackResourse(cb.getType(), cb.getGrade());
								totalRebackResourse.unite(rebackResourse);
								cb.setGrade(maxLevel);
								cb.initNpc(this);
								sbf.append(cb.getNpc().getName()).append(",");
								notifyFieldChange(cb.getType());
								if (logger.isWarnEnabled()) {
									logger.warn("[每天维护仙府] [仙府降级] [" + this.getId() + "] [主人:" + this.getOwnerId() + "] [要降级的建筑:" + cb.getClass().getSimpleName() + "] [建筑名称:" + cb.getNpc().getName() + "] [降级后建筑等级:" + cb.getGrade() + "] [返还资源:" + rebackResourse.toString() + "]");
								}
							}
						}
						this.getCurrRes().unite(totalRebackResourse);
						// 设置为正常状态
						setStatus(CAVE_STATUS_OPEN);
						if (GamePlayerManager.getInstance().isOnline(getOwnerId())) {
							Player player;
							try {
								player = GamePlayerManager.getInstance().getPlayer(getOwnerId());
								player.addMessageToRightBag(FaeryManager.getInstance().caveSimple(player, null));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						addDynamic(new CaveDynamic(Translate.系统, Dynamic.仙府降级, "<f color='#ff0000' style='blod'>" + Translate.仙府被降级 + "</f>," + Translate.返还资源 + ":" + totalRebackResourse.toString()));
						setLastMaintenanceTime(now);
						if (CaveSubSystem.logger.isWarnEnabled()) {
							CaveSubSystem.logger.warn("[每天维护仙府] [" + this.getId() + "] [主人:" + this.getOwnerId() + "] [仙府降级] [解除锁定] [需要资源:" + maintenanceCost.toString() + "] [当前资源:" + getCurrRes().toString() + "] [降级后主建筑级别:" + maxLevel + "] [降级信息:" + sbf.toString() + "] [返还资源:" + totalRebackResourse.toString() + "]");
						}
					} else {// 锁定
						setLastMaintenanceTime(now);
						setStatus(CAVE_STATUS_LOCKED);
						addDynamic(new CaveDynamic(Translate.系统, Dynamic.仙府锁定, "<f color='#ff0000' style='blod'>" + Translate.仙府被锁定 + "</f>"));
						if (CaveSubSystem.logger.isWarnEnabled()) {
							CaveSubSystem.logger.warn("[每天维护仙府] [" + this.getId() + "] [主人:" + this.getOwnerId() + "] [仙府锁定] [需要资源:" + maintenanceCost.toString() + "] [当前资源:" + getCurrRes().toString() + "]");
						}
						if (GamePlayerManager.getInstance().isOnline(getOwnerId())) {
							Player player;
							try {
								player = GamePlayerManager.getInstance().getPlayer(getOwnerId());
								player.addMessageToRightBag(FaeryManager.getInstance().caveSimple(player, null));
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}

	// 用于排行
	private long caveScore;

	public long getCaveScore() {
		int score = 0;
		score += mainBuilding.getGrade() * 10;
		score += storehouse.getGrade() * 8;
		score += pethouse.getGrade() * 6;
		score += fence.getGrade() * 5;
		score += doorplate.getGrade() * 5;
		for (CaveField cf : fields) {
			if (cf != null) {
				score += cf.getGrade() * 3;
			}
		}
		return score;
	}
	
	public void noticeResource() {
		
		Player owner =  null;
		try {
			owner = GamePlayerManager.getInstance().getPlayer(ownerId);
			CAVE_QUERY_RESOURCECOLLECTION_RES res = new CAVE_QUERY_RESOURCECOLLECTION_RES(GameMessageFactory.nextSequnceNum(), this.getCurrRes(), this.getCurrMaxResource());
			owner.addMessageToRightBag(res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Point getDoorpoint() {
		NPC npc = this.fence.getNpc();
		if (npc != null) {
			return new Point(npc.getX(), npc.getY());
		}
		throw new IllegalStateException();
	}

	public void setCaveScore(long caveScore) {
		this.caveScore = caveScore;
	}

	public void setOwnerLastSeeDynamic(long ownerLastSeeDynamic) {
		this.ownerLastSeeDynamic = ownerLastSeeDynamic;
	}

	public long getOwnerLastSeeDynamic() {
		return ownerLastSeeDynamic;
	}

	public int getRewardTimes() {
		return rewardTimes;
	}

	public void setRewardTimes(int rewardTimes) {
		this.rewardTimes = rewardTimes;
	}

}