package com.fy.engineserver.homestead.cave;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.homestead.cave.CaveDynamic.Dynamic;
import com.fy.engineserver.homestead.cave.resource.BuildingOutShowCfg;
import com.fy.engineserver.homestead.cave.resource.HitAreaCfg;
import com.fy.engineserver.homestead.cave.resource.Point;
import com.fy.engineserver.homestead.faery.service.CaveSubSystem;
import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.npc.CaveDoorNPC;
import com.fy.engineserver.sprite.npc.CaveNPC;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public abstract class CaveBuilding implements FaeryConfig, Comparable<CaveBuilding> {

	/** 建筑类型 */
	private int type;
	/** NPC */
	private transient NPC npc;
	/** 当前等级 */
	private int grade;
	/** 当前状态 0-正常 1-升级 */
	private int status;
	/** 开始升级时间 */
	private long lvUpStartTime;

	/** 所有的操作列表 */
	private ArrayList<CaveSchedule> schedules = new ArrayList<CaveSchedule>();

	private transient Point point;

	private transient Cave cave;

	public static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	public boolean inBuilding() {
		return getStatus() == CAVE_BUILDING_STATUS_LVUPING;
	}

	/**
	 * 加载NPC
	 */
	public void initNpc(Cave cave) {
		try {
			setCave(cave);
			NPC oldNpc = getNpc();
			if (oldNpc != null) {
				cave.getFaery().getGame().removeSprite(oldNpc);
				cave.getBuildings().remove(oldNpc.getId());
				if (FaeryManager.logger.isInfoEnabled()) {
					FaeryManager.logger.info("[移除老的NPC:" + oldNpc.getId() + "] [" + oldNpc.getName() + "]");
				}
			}
			BuildingOutShowCfg cfg = FaeryManager.getInstance().getBuildingOutShowCfgs()[getType()];
			int npcId = cfg.getDefaultNpcId();
			CaveNPC npc = (CaveNPC) MemoryNPCManager.getNPCManager().createNPC(npcId);
			if (npc != null) {
				Point point = FaeryManager.getInstance().getPoint(cave.getIndex(), getType());
				if (point != null) {
					npc.setX(point.getX());
					npc.setY(point.getY());
				} else {
					FaeryManager.logger.error("[初始化NPC][位置不存在][CaveIndex:{}][建筑类型:{}]", new Object[] { cave.getIndex(), getType() });
				}
				npc.setGrade(getGrade());
				npc.setCave(getCave());
				((CaveNPC) npc).setGrade(getGrade());

				if (npc instanceof CaveDoorNPC) {
					int index = getCave().getIndex();
					HitAreaCfg hcfg = FaeryManager.getInstance().getHitAreaCfgs()[index];
					((CaveDoorNPC) npc).setPolygonX(hcfg.getX());
					((CaveDoorNPC) npc).setPolygonY(hcfg.getY());
					((CaveDoorNPC) npc).setIsClosed(((CaveFence) this).getOpenStatus() == FENCE_STATUS_CLOSE);
					ResourceManager.getInstance().setAvata((CaveDoorNPC) npc);
				} else {
					if (inBuilding()) {
						npc.setInBuilding(true);
					}
					ResourceManager.getInstance().setAvata(npc);
				}
				getCave().getFaery().getGame().addSprite(npc);
				setNpc(npc);
				modifyName();
				cave.getBuildings().put(npc.getId(), this);
			} else {
				FaeryManager.logger.error("[初始化庄园NPC失败][类型:{}][defaultNpcId:{}]", new Object[] { getType(), npcId });
				return;
			}
		} catch (Exception e) {
			CaveSubSystem.logger.error("初始化NPC异常----", e);
		}
	}

	public NPC getNpc() {
		return npc;
	}

	public void setNpc(NPC npc) {
		this.npc = npc;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	/**
	 * @return
	 */
	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public final int getStatus() {
		return status;
	}

	public final void setStatus(int status) {
		this.status = status;
	}

	public long getLvUpStartTime() {
		return lvUpStartTime;
	}

	public void setLvUpStartTime(long lvUpStartTime) {
		this.lvUpStartTime = lvUpStartTime;
	}

	public Cave getCave() {
		return cave;
	}

	public void setCave(Cave cave) {
		this.cave = cave;
	}

	public List<CaveSchedule> getSchedules() {
		return schedules;
	}

	public void setSchedules(ArrayList<CaveSchedule> schedules) {
		this.schedules = schedules;
	}

	/**
	 * 是否有同类型的任务在调度
	 * @param optionType
	 * @return
	 */
	public boolean hasSameSchedule(int optionType) {
		for (CaveSchedule caveSchedule : getSchedules()) {
			if (caveSchedule.getOptionType() == optionType) {
				return true;
			}
		}
		return false;
	}

	public CaveSchedule getScheduleByOptType(int optionType) {
		synchronized (schedules) {
			CaveSchedule result = null;
			if (getSchedules() == null) {
				return null;
			}
			for (int i = 0; i < getSchedules().size(); i++) {
				CaveSchedule caveSchedule = getSchedules().get(i);
				if (caveSchedule.getOptionType() == optionType) {
					result = caveSchedule;
					break;
				}
			}
			return result;
		}
	}

	public void removeScheduleForDone(int optionType) {
		CaveSchedule caveSchedule = getScheduleByOptType(optionType);
		if (caveSchedule != null) {
			getSchedules().remove(caveSchedule);
			getCave().getSchedules().remove(caveSchedule);
			getCave().notifyFieldChange(this.getType());
			if (optionType == OPTION_LEVEL_UP) {
				{
					Player player = null;
					try {
						player = GamePlayerManager.getInstance().getPlayer(getCave().getOwnerId());
						if (player != null) {
							// 统计&成就 建筑升级次数
							AchievementManager.getInstance().record(player, RecordAction.仙府升级建筑次数);
							// 统计&成就 所有建筑最低等级
							int minLevel = 20;
							for (Iterator<Long> itor = cave.getBuildings().keySet().iterator(); itor.hasNext();) {
								Long id = itor.next();
								CaveBuilding cb = cave.getBuildings().get(id);
								if (cb != null && cb.getType() != FaeryConfig.CAVE_BUILDING_TYPE_DOORPLATE) {
									int buildingLevel = cb.getGrade();
									if (minLevel > buildingLevel) {
										minLevel = buildingLevel;
									}
								}
							}
							AchievementManager.getInstance().record(player, RecordAction.仙府全部建筑达到等级, minLevel);
							if (FaeryManager.logger.isInfoEnabled()) {
								FaeryManager.logger.info(player.getLogString() + "[升级建筑成功] [整个庄园最低级建筑等级:{" + minLevel + "}]");
							}
						} else {
							FaeryManager.logger.error("[建筑升级完毕] [记录升级统计] [异常] [主人不存在] [" + (getCave() == null ? "仙府NULL" : "ownerId:" + getCave().getOwnerId()) + "]");
						}

					} catch (Exception e) {
						FaeryManager.logger.error("[建筑升级完毕] [记录升级统计] [异常] [" + (getCave() == null ? "仙府NULL" : "ownerId:" + getCave().getOwnerId()) + "]", e);
					}
				}
				noticeLvup();
			}
		} else {
			FaeryManager.logger.error(" [仙府主人: " + getCave().getOwnerId() + "] [要移除队列" + optionType + "] [队列不存在]");
		}
	}

	public void removeScheduleForCancel(int optionType) {
		CaveSchedule caveSchedule = getScheduleByOptType(optionType);
		if (caveSchedule != null) {
			getSchedules().remove(caveSchedule);
			getCave().getSchedules().remove(caveSchedule);
		}
		((CaveNPC) getNpc()).setInBuilding(false);
		ResourceManager.getInstance().setAvata((CaveNPC) getNpc());
		setStatus(CAVE_BUILDING_STATUS_SERVICE);
	}

	public static String nameseparator = "☞";

	public void modifyName() {
		if (getNpc().getName().indexOf(nameseparator) == -1) {
			getNpc().setName(getCave().getOwnerName() + nameseparator + getNpc().getName());
		}
	}

	public void noticeLvup() {
		cave.addDynamic(new CaveDynamic(Translate.系统, Dynamic.建筑升级完成, Translate.translateString(Translate.text_cave_064, new String[][] { { Translate.STRING_1, this.getNpc().getName() }, { Translate.STRING_2, String.valueOf(this.getGrade()) } })));
	}

	@Override
	public int compareTo(CaveBuilding o) {
		return getType() - o.getType();
	}
}
