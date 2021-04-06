package com.fy.engineserver.septbuilding.entity;

import java.util.Arrays;
import java.util.Set;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.message.NOTICE_CLIENT_UPDATE_MAPRES_REQ;
import com.fy.engineserver.septbuilding.service.BuildingState;
import com.fy.engineserver.septbuilding.service.SeptBuildingManager;
import com.fy.engineserver.septbuilding.templet.SeptBuildingTemplet.BuildingType;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.septstation.service.SeptStationManager;
import com.fy.engineserver.septstation.service.SeptStationManager.ActionType;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.sprite.npc.SeptStationNPC;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.TimeTool.TimeDistance;
import com.fy.gamegateway.message.GameMessageFactory;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 家族建筑实体（NPC)
 */
@SimpleEmbeddable
public class SeptBuildingEntity {

	private static final long serialVersionUID = 1L;

	/** 帮派驻地ID */
	protected long ssId;
	/** 当前建筑状态 */
	protected BuildingState buildingState;
	/** 模板的索引 */
	protected int templetIndex;
	/** 持有NPCID 模板ID· */
	protected int npcTempletId;
	/** 持有NPC 内存数据 每次LOAD时候生成 */
	protected transient NPC npc;
	/** 在地图中的位置 */
	protected int index;
	/** 是否在建造中 */
	protected boolean inBuild = false;
	/** 升级建筑所需任务数 */
	protected int lvUpTaskNum = 0;
	/** 当前完成任务数 */
	protected int currTaskNum = 0;

	/**
	 * 开始升级时间，升级持续时间
	 */
	private long startUpTime;
	private long upLastTime;
	
	private transient SeptStation station;

	protected transient GameManager gm = GameManager.getInstance();

	public SeptBuildingEntity() {

	}

	public SeptBuildingEntity(int npcTempletId, long ssId, int index, int templetIndex) {
		this.npcTempletId = npcTempletId;
		this.ssId = ssId;
		this.index = index;
		this.templetIndex = templetIndex;
	}

	/**
	 * 当前建筑的维修费
	 * 2011-4-25
	 * 
	 * @return int
	 * 
	 */
	public long getDailyMaintaiCost() {
		return SeptBuildingManager.getTemplet(templetIndex).getDailyMaintainCost()[buildingState.getLevel()] /SeptStationManager.每次维护费用比率;
	}

	/**
	 * 更改NPC形象
	 * 2011-4-28 void
	 * 
	 * 
	 */
	public void modifyNPCOutShow() {
		((SeptStationNPC) getNpc()).setGrade(getBuildingState() == null ? 0 : getBuildingState().getLevel());
		ResourceManager.getInstance().setAvata((SeptStationNPC) getNpc());
	}

	/**
	 * 建筑级别降低到level
	 * 2011-5-3
	 * 
	 * @param level
	 *            void
	 * 
	 */
	public void lvDownTo(int level) {
		if (getBuildingState().getLevel() > level) {
			getBuildingState().setLevel(level);
			setInBuild(false);
			modifyNPCOutShow();
		}
	}
	
	/**
	 * 时间到，升级
	 */
	private transient long lastUpdateDate;
	public void realLevelUp(){
		if(inBuild){
			if(getUpLastTime() <= 0 || getStartUpTime() <= 0){
				return;
			}
			if(System.currentTimeMillis() >= lastUpdateDate){
				long overtime = getStartUpTime() + getUpLastTime() - System.currentTimeMillis();
				getNpc().setTitle("<f color='0xff00ff00'>建设中……剩余建设时间："+(overtime < 60000 ? "1" : overtime/60000)+"分</f>");
				lastUpdateDate = System.currentTimeMillis() + 10000;
			}
			if(System.currentTimeMillis() >= (getStartUpTime() + getUpLastTime())){
				getNpc().setTitle("");
				setInBuild(false);
				getBuildingState().setLevel(getBuildingState().getLevel() + 1);
				modifyNPCOutShow();
				getBuildingState().getTempletType().onLevelUp(station);
				ChatMessageService.getInstance().sendMessageToJiazu(station.getJiazu(), getBuildingState().getTempletType().getName() + " " + (getBuildingState().getLevel()) + "级, 升级完毕", "");
				if (getBuildingState().getTempletType().getBuildingType().getIndex() == BuildingType.聚仙殿.getIndex()) {
					getStation().getJiazu().setLevel(getStation().getMainBuildingLevel());
				}
				if (station.getJiazu() != null) {
					station.getJiazu().setUpdateProsperityTime(SystemTime.currentTimeMillis());
				}
				station.initInfo();
				// 通知所有在这幅地图的人.本图NPC的遮挡
				int level = this.getBuildingState().getLevel();
				this.setLvUpTaskNum(getBuildingState().getTempletType().getReleaseTaskNum()[level == 0 ? level : level - 1]);
				setStartUpTime(0);
				setUpLastTime(0);
				getNpc().setTitle("");
				
				try{
					if(!getStation().getMapName().equals(getStation().getMapName2UpLv())){
						getStation().setMapName(getStation().getMapName2UpLv());
						for (LivingObject lo : getStation().getGame().getLivingObjects()) {
							if (lo instanceof Player) {
								try {
									Player p = (Player)lo;
									if(p.isOnline()){
										NOTICE_CLIENT_UPDATE_MAPRES_REQ res = new NOTICE_CLIENT_UPDATE_MAPRES_REQ(GameMessageFactory.nextSequnceNum(), getStation().getMapName2UpLv(), 0, getStation().getMapName(), true);
										p.addMessageToRightBag(res);
									}
								} catch (Exception e) {
									JiazuSubSystem.logger.error("[通知在家族地图的角色地图遮挡变化]", e);
								}
							}
						}
					}
					JiazuSubSystem.logger.warn("[驻地建筑升级] [id:{}] [jiaZuName:{}] [{}] [等级:{}] [mapname:{}] [map2:{}] [持续时间:{}分]",
							new Object[]{(getStation().getJiazu().getJiazuID()),(getStation().getJiazu().getName()),getNpc().getName(), level,getStation().getMapName(),getStation().getMapName2UpLv(),this.getLvUpTaskNum()});
				}catch(Exception e){
					e.printStackTrace();
					JiazuSubSystem.logger.warn("[驻地建筑升级] [异常]",e);
				}
			}
		}
	}

	/**
	 * 做完建设任务时调用,用倒计时来代替任务
	 */
	public synchronized void onDeliverTask(Player player) {
		if (inBuild) {
			currTaskNum++;
			if (JiazuSubSystem.logger.isWarnEnabled()) {
				JiazuSubSystem.logger.warn(player.getJiazuLogString() + "[家族:{}] [完成建设任务] [currTaskNum:{}] [lvUpTaskNum:{}]", new Object[] { (getStation().getJiazu().getName()), currTaskNum, lvUpTaskNum });
			}
			ChatMessageService.getInstance().sendMessageToJiazu(station.getJiazu(), player.getName() +Translate.完成了建设任务当前进度 +  "[" + getBuildingState().getTempletType().getName() + " " + (getBuildingState().getLevel() + 1) + Translate.级 +"](" + currTaskNum + "/" + lvUpTaskNum + ")", "");
			JiazuMember jiazuMember = JiazuManager.getInstance().getJiazuMember(player.getId(), player.getJiazuId());
			long now = SystemTime.currentTimeMillis();
			if (jiazuMember != null) {
				if (!TimeTool.instance.isSame(now, jiazuMember.getLastDeliverBuildingTaskTime(), TimeDistance.DAY)) {
					jiazuMember.setDeliverBuildingTaskNum(0);
				}
				jiazuMember.setDeliverBuildingTaskNum(jiazuMember.getDeliverBuildingTaskNum() + 1);
				jiazuMember.setLastDeliverBuildingTaskTime(now);
				if (jiazuMember.getDeliverBuildingTaskNum() <= 10) {
					jiazuMember.setCurrentWeekContribution(jiazuMember.getCurrentWeekContribution() + JiazuManager.CONTRIBUTION_ADD_BY_BUILDINGTASK);
					getStation().getJiazu().initMember4Client();
					if (JiazuSubSystem.logger.isWarnEnabled()) {
						JiazuSubSystem.logger.warn(player.getJiazuLogString() + "[获得贡献度] [完成建设任务] [currTaskNum:{}] [lvUpTaskNum:{}] [增加贡献度:{}] [增加后贡献度:{}] [当天完成任务数:{}]", new Object[] { currTaskNum, lvUpTaskNum, JiazuManager.CONTRIBUTION_ADD_BY_BUILDINGTASK, jiazuMember.getCurrentWeekContribution(), jiazuMember.getDeliverBuildingTaskNum() });
					}
				} else {
					if (JiazuSubSystem.logger.isWarnEnabled()) {
						JiazuSubSystem.logger.warn(player.getJiazuLogString() + "[完成建设任务] [currTaskNum:{}] [lvUpTaskNum:{}] [增加贡献度:{}] [增加后贡献度:{}] [超过了最大上限,不给于贡献,当天完成任务数:{}]", new Object[] { currTaskNum, lvUpTaskNum, 0, jiazuMember.getCurrentWeekContribution(), jiazuMember.getDeliverBuildingTaskNum() });
					}
				}

			}
			if (currTaskNum >= lvUpTaskNum) {
				cancelTask();
				getBuildingState().setLevel(getBuildingState().getLevel() + 1);
				setInBuild(false);
				modifyNPCOutShow();
				getBuildingState().getTempletType().onLevelUp(station);
				ChatMessageService.getInstance().sendMessageToJiazu(station.getJiazu(), getBuildingState().getTempletType().getName() + " " + (getBuildingState().getLevel()) + "级, 升级完毕", "");
				if (getBuildingState().getTempletType().getBuildingType().getIndex() == BuildingType.聚仙殿.getIndex()) {
					getStation().getJiazu().setLevel(getStation().getMainBuildingLevel());
					{
						// 统计
						Set<JiazuMember> memberSet = JiazuManager.getInstance().getJiazuMember(getStation().getJiazu().getJiazuID());
						for (JiazuMember member : memberSet) {
							if (member != null) {
								try {
									if (GamePlayerManager.getInstance().isOnline(member.getPlayerID())) {//只给在线的人加,其他人等上线的时候加
										Player p = GamePlayerManager.getInstance().getPlayer(member.getPlayerID());
										AchievementManager.getInstance().record(p, RecordAction.家族到达等级, getStation().getMainBuildingLevel());
									}
								} catch (Exception e) {
									JiazuSubSystem.logger.error("[家族升级,统计家族级别] [异常]", e);
								}
							}
						}
					}
				}
				if (station.getJiazu() != null) {
					station.getJiazu().setUpdateProsperityTime(SystemTime.currentTimeMillis());
				}
				station.initInfo();
				// 通知所有在这幅地图的人.本图NPC的遮挡
			}
			station.notifyFieldChange("buildingEntities");
		}
	}

	public static boolean testBuildTime = false;
	
	/**
	 * 发布任务
	 */
	public void releaseTask(ActionType type, Game game) {
		//ChatMessageService.getInstance().sendMessageToJiazu(station.getJiazu(), Translate.text_jiazu_024, "");
		int level = this.getBuildingState().getLevel();
		this.setLvUpTaskNum(getBuildingState().getTempletType().getReleaseTaskNum()[level == 0 ? level : level - 1]);
//		this.setCurrTaskNum(0);
		this.setStartUpTime(System.currentTimeMillis());
		if(testBuildTime){
			this.setUpLastTime(60 * 1000L);
		}else{
			this.setUpLastTime(this.getLvUpTaskNum()* 60 * 1000L);
		}
		
		if (JiazuSubSystem.logger.isWarnEnabled()) {
			JiazuSubSystem.logger.warn("[家族:" + station.getJiazu().getName() + "] [建造:{}] [调用了发布任务：{}] [任务数量:{}] [npcavata:{}] [npcTitle:{}]", 
					new Object[] { getNpc().getName(), getBuildingState().getTempletType().getName(), this.getLvUpTaskNum(),Arrays.toString(getNpc().getAvata()),getNpc().getTitle() });
		}
		// 测试代码
		{
			// try {
			// Jiazu jiazu = station.getJiazu();
			// Player player = GamePlayerManager.getInstance().getPlayer(JiazuManager.getInstance().getJiazuMember(jiazu.getJiazuID(), JiazuTitle.master).get(0).getPlayerID());
			// for (int i = 0; i < this.getLvUpTaskNum(); i++) {
			// onDeliverTask(player);
			// }
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
		}
	}

	/**
	 * 取消发布任务
	 */
	public void cancelTask() {
		if (SeptStationManager.logger.isInfoEnabled()) {
			SeptStationManager.logger.info("完成了发布任务：{}", new Object[] { getBuildingState().getTempletType().getName() });
		}
	}


	/**************************************** Getters Setters ****************************************/

	public BuildingState getBuildingState() {
		return buildingState;
	}

	public boolean isInBuild() {
		return inBuild;
	}

	public void setInBuild(boolean inBuild) {
		this.inBuild = inBuild;
		if (station != null) {
			station.notifyFieldChange("buildingEntities");
		} else {
			JiazuSubSystem.logger.error("[建筑数值改变] [设置的时候还没设置驻地] [value = inBuild]");
		}
	}

	public int getNpcTempletId() {
		return npcTempletId;
	}

	public void setNpcTempletId(int npcId) {
		this.npcTempletId = npcId;
	}

	public int getIndex() {
		return index;
	}

	public long getStartUpTime() {
		return startUpTime;
	}

	public void setStartUpTime(long startUpTime) {
		this.startUpTime = startUpTime;
		if (station != null) {
			station.notifyFieldChange("buildingEntities");
		} else {
			JiazuSubSystem.logger.error("[建筑数值改变] [设置的时候还没设置驻地] [value = startUpTime]");
		}
	}

	public long getUpLastTime() {
		return upLastTime;
	}

	public void setUpLastTime(long upLastTime) {
		this.upLastTime = upLastTime;
		if (station != null) {
			station.notifyFieldChange("buildingEntities");
		} else {
			JiazuSubSystem.logger.error("[建筑数值改变] [设置的时候还没设置驻地] [value = upLastTime]");
		}
	}

	public void setIndex(int index) {
		this.index = index;
		if (station != null) {
			station.notifyFieldChange("buildingEntities");
		} else {
			JiazuSubSystem.logger.error("[建筑数值改变] [设置的时候还没设置驻地] [value = index]");
		}
	}

	public void setBuildingState(BuildingState buildingState) {
		this.buildingState = buildingState;
	}

	// public boolean isDirty() {
	// return dirty;
	// }
	//
	// public void setDirty(boolean dirty) {
	// this.dirty = dirty;
	// }

	public long getSsId() {
		return ssId;
	}

	public void setSsId(long ssId) {
		this.ssId = ssId;
		if (station != null) {
			station.notifyFieldChange("buildingEntities");
		} else {
			JiazuSubSystem.logger.error("[建筑数值改变] [设置的时候还没设置驻地] [value = ssId]");
		}
	}

	public int getTempletIndex() {
		return templetIndex;
	}

	public void setTempletIndex(int templetIndex) {
		this.templetIndex = templetIndex;
	}

	public NPC getNpc() {
		return npc;
	}

	public void setNpc(NPC npc) {
		this.npc = npc;
	}

	public int getLvUpTaskNum() {
		return lvUpTaskNum;
	}

	public void setLvUpTaskNum(int lvUpTaskNum) {
		this.lvUpTaskNum = lvUpTaskNum;
		if (station != null) {
			station.notifyFieldChange("buildingEntities");
		} else {
			JiazuSubSystem.logger.error("[建筑数值改变] [设置的时候还没设置驻地] [value = lvUpTaskNum]");
		}
	}

	public int getCurrTaskNum() {
		return currTaskNum;
	}

	public void setCurrTaskNum(int currTaskNum) {
		this.currTaskNum = currTaskNum;
		if (station != null) {
			station.notifyFieldChange("buildingEntities");
		} else {
			JiazuSubSystem.logger.error("[建筑数值改变] [设置的时候还没设置驻地] [value = currTaskNum]");
		}
	}

	public SeptStation getStation() {
		return station;
	}

	public void setStation(SeptStation station) {
		this.station = station;
	}

}
