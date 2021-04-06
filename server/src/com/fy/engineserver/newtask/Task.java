package com.fy.engineserver.newtask;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.client.AavilableTaskInfo;
import com.fy.engineserver.core.client.FunctionNPC;
import com.fy.engineserver.newtask.prizes.TaskPrize;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.newtask.targets.TaskTarget;
import com.fy.engineserver.newtask.timelimit.DeliverTimeLimit;
import com.fy.engineserver.newtask.timelimit.TimeLimit;
import com.fy.engineserver.newtask.timelimit.TimeLimit4Accept;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;

/**
 * 任务实体
 * 日常任务记录最后完成时间
 * 跑环任务记录最后一次接取时间（记录第一环的时间）
 * 跑环任务放弃后处理:a前一天接取的,从今天第一环开始.b今天接取的从当前环接取
 * 
 */
public class Task/* extends TimeLimit4Accept*/ implements TaskConfig, Cloneable {
	// 基本信息
	/** ID */

	private long id;
	/** 名字 */
	private String name;
	private String name_stat;
	/** 组名字 */
	private String groupName;
	private String groupName_stat;
	/** 任务集合名 技术录入用 */
	private String collections;
	private String collections_stat;
	/** 任务集合名【不同的任务组可属于同一个任务集合中，方便做活动任务，如消耗15体力的任务与消耗40体力的任务同时只能接一个（无此需求的可以空着） 】 */
	private String bigCollection;
	private String bigCollection_stat;
	/** 前置任务组 */
	private String frontGroupName;
	/** 对前置依赖类型 0-一组完成 一个 1-一组全部完成 */
	private int dependType;
	/** 任务等级 */
	private int grade;
	/** 最高等级限制 */
	private int maxGradeLimit;
	/** 最低等级限制 */
	private int minGradeLimit;
	/** 性别限制 0-无,1-男,2-女 */
	private int sexLimit;
	/**可接任务职业*/
	private int workLimits[] = new int[0];
	/** 国家限制 -1-无 ,>-1各个国家 */
	private int countryLimit;
	/** 职业限制 */
	private int workLimit;
	/** 国家职务限制 */
	private int countryOfficialLimit;
	/** 家族职务限制 */
	private int septOfficialLimit;
	/** 境界值限制 */
	private int bourn;
	/** 任务显示类型 */
	private byte showType;
	/** 家族限制 */
	private int septLimit;
	/** 任务描述 */
	private String des;
	/** 体力值限制 */
	private int thewLimit = 0;
	/** 社会关系限制 */
	private int socialRelationsLimit;
	/** 封印状态限制 (-1不限制 0 封印) */
	private int sealLimit;
	/** 任务未完成的对话 */
	private String unDeliverTalk;
	/** 任务计算积分 0不计算，1计算 */
	private int countScore;
	/** 接取任务给予的物品 */
	private TaskGivenArticle givenArticle;
	/** 接取任务给予的BUFF */
	private TaskGivenBuff givenBuff;
	/** 任务目标 */
	private TaskTarget[] targets = new TaskTarget[0];
	/** 任务奖励 */
	private TaskPrize[] prizes = new TaskPrize[0];
	/** 是否有随机奖励 */
	private transient boolean hasRandomPrize = false;
	/** 额外奖励的物品名称 */
	private String excessTarget;
	/** 额外奖励的物品出现几率 */
	private double excessTargetRate;
	/** 是否有额外的条件 */
	private boolean hasExcess;
	/** 是否是自动交付任务 */
	private boolean isAutoDeliver;
	/** 元神等级限制,只要≥这个级别就可接 */
	private int soulLevelLimit;
	/** 是否是服务器自动接取任务.如果是,无法通过协议接取 */
	private boolean serverAutoAccept;

	/** 任务开始/结束地点信息 */
	private String startNpc;
	private String startMap;
	private String startTalk;
	private int startX;
	private int startY;
	//startMap,endMap,targets

	private String endNpc;
	private String endMap;
	private String endTalk;
	private int endX;
	private int endY;

	/** 任务类型 0,单次，1日常，2跑环，3循环 */
	private int type;
	/** 体力值消耗 */
	private int thewCost;
	/** 任务的时间限制 (接取) */
	private TimeLimit timeLimit;
	/** 任务的时间限制 (完成) */
	private DeliverTimeLimit deliverTimeLimit;
	/** 是否显示任务目标,0显示1不显示 */
	private byte showTarget;

	// 日常任务
	/** 周期限定 天 */
	private int dailyTaskCycle;
	/** 同周期可完成次数上限 */
	private int dailyTaskMaxNum;

	// // 循环任务
	// /** 循环任务正常奖励最高次数 */
	// private int cycleTaskFullPrizeTimes;
	// /** 循环任务超出最大全额奖励后的衰减比例 */
	// double cycleTaskReduce;

	// 循环//跑环
	/** 放弃,失败后的CD */
	private long reGetCodeDown;

	/** 温馨提示 */
	private String notice = "";

	/** 开始NPC形象 */
	private String startNPCAvataRace = "";
	private String startNPCAvataSex = "";
	/** 交付NPC形象 */
	private String endNPCAvataRace = "";
	private String endNPCAvataSex = "";
	/** 开始地图的资源名 寻路用 */
	private String startMapResName = "";
	/** 结束地图的资源名 寻路用 */
	private String endMapResName = "";

	private transient boolean hasRandomTarget = false;
	private transient boolean hasCollectionTarget = false;

	public boolean gradeFit(Player player) {
		int grade = player.getLevel();
		int min = getMinGradeLimit() == -1 ? 0 : getMinGradeLimit();
		int max = getMaxGradeLimit() == -1 ? grade : getMaxGradeLimit();
		return min <= grade && max >= grade;// getMinGradeLimit() <= grade && getMaxGradeLimit() >= grade;
	}

	public boolean isTiliTask() {
		return thewCost > 0;
	}

	/**
	 * 获得指定类型的任务目标
	 * @param targetTypes
	 * @return
	 */
	public List<TaskTarget> getTargetByType(TargetType... targetTypes) {
		List<TaskTarget> list = null;
		for (TargetType targetType : targetTypes) {
			for (TaskTarget target : getTargets()) {
				if (target.getTargetType() == targetType) {
					if (list == null) {
						list = new ArrayList<TaskTarget>();
					}
					list.add(target);
				}
			}
		}
		return list;
	}

	/**
	 * 获得指定类型的任务目标
	 * @param prizeTypes
	 * @return
	 */
	public List<TaskPrize> getPrizeByType(PrizeType... prizeTypes) {
		List<TaskPrize> list = null;
		for (PrizeType prizeType : prizeTypes) {
			for (TaskPrize prize : getPrizes()) {
				if (prize.getPrizeType().getIndex() == prizeType.getIndex()) {
					if (list == null) {
						list = new ArrayList<TaskPrize>();
					}
					list.add(prize);
				}
			}
		}
		return list;
	}

	/**
	 * 是否计算积分
	 * @return
	 */
	public boolean needCountScore() {
		return this.getCountScore() == TASK_COUNT_SCORE_YES;
	}

	/**************************************** getters and setters ****************************************/

	public long getId() {
		return id;
	}

	public int[] getWorkLimits() {
		return this.workLimits;
	}

	public void setWorkLimits(int[] workLimits) {
		this.workLimits = workLimits;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getCountScore() {
		return countScore;
	}

	public void setCountScore(int countScore) {
		this.countScore = countScore;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getWorkLimit() {
		return workLimit;
	}

	public void setWorkLimit(int workLimit) {
		this.workLimit = workLimit;
	}

	public int getBourn() {
		return bourn;
	}

	public void setBourn(int bourn) {
		this.bourn = bourn;
	}

	public String getExcessTarget() {
		return excessTarget;
	}

	public void setExcessTarget(String excessTarget) {
		this.excessTarget = excessTarget;
	}

	public TaskGivenArticle getGivenArticle() {
		return givenArticle;
	}

	public void setGivenArticle(TaskGivenArticle givenArticle) {
		this.givenArticle = givenArticle;
	}

	public TaskGivenBuff getGivenBuff() {
		return givenBuff;
	}

	public void setGivenBuff(TaskGivenBuff givenBuff) {
		this.givenBuff = givenBuff;
	}

	public boolean isHasRandomPrize() {
		return hasRandomPrize;
	}

	public void setHasRandomPrize(boolean hasRandomPrize) {
		this.hasRandomPrize = hasRandomPrize;
	}

	public String getUnDeliverTalk() {
		return unDeliverTalk;
	}

	public void setUnDeliverTalk(String unDeliverTalk) {
		this.unDeliverTalk = unDeliverTalk;
	}

	public int getSocialRelationsLimit() {
		return socialRelationsLimit;
	}

	public void setSocialRelationsLimit(int socialRelationsLimit) {
		this.socialRelationsLimit = socialRelationsLimit;
	}

	public int getThewLimit() {
		return thewLimit;
	}

	public void setThewLimit(int thewLimit) {
		this.thewLimit = thewLimit;
	}

	public String getFrontGroupName() {
		return frontGroupName;
	}

	public void setFrontGroupName(String frontGroupName) {
		this.frontGroupName = frontGroupName;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public byte getShowType() {
		return showType;
	}

	public void setShowType(byte showType) {
		this.showType = showType;
	}

	public int getMaxGradeLimit() {
		return maxGradeLimit;
	}

	public void setMaxGradeLimit(int maxGradeLimit) {
		this.maxGradeLimit = maxGradeLimit;
	}

	public int getMinGradeLimit() {
		return minGradeLimit;
	}

	public void setMinGradeLimit(int minGradeLimit) {
		this.minGradeLimit = minGradeLimit;
	}

	public int getSexLimit() {
		return sexLimit;
	}

	public void setSexLimit(int sexLimit) {
		this.sexLimit = sexLimit;
	}

	public int getCountryLimit() {
		return countryLimit;
	}

	public void setCountryLimit(int countryLimit) {
		this.countryLimit = countryLimit;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public TaskTarget[] getTargets() {
		return targets;
	}

	public void setTargets(TaskTarget[] targets) {
		this.targets = targets;
	}

	public TaskPrize[] getPrizes() {
		return prizes;
	}

	public void setPrizes(TaskPrize[] prizes) {
		this.prizes = prizes;
	}

	public String getStartNpc() {
		return startNpc;
	}

	public void setStartNpc(String startNpc) {
		this.startNpc = startNpc;
	}

	public String getStartMap() {
		return startMap;
	}

	public void setStartMap(String startMap) {
		this.startMap = startMap;
	}

	public String getStartTalk() {
		return startTalk;
	}

	public void setStartTalk(String startTalk) {
		this.startTalk = startTalk;
	}

	public int getStartX() {
		return startX;
	}

	public void setStartX(int startX) {
		this.startX = startX;
	}

	public int getStartY() {
		return startY;
	}

	public void setStartY(int startY) {
		this.startY = startY;
	}

	public String getEndNpc() {
		return endNpc;
	}

	public void setEndNpc(String endNpc) {
		this.endNpc = endNpc;
	}

	public String getEndMap() {
		return endMap;
	}

	public void setEndMap(String endMap) {
		this.endMap = endMap;
	}

	public String getEndTalk() {
		return endTalk;
	}

	public void setEndTalk(String endTalk) {
		this.endTalk = endTalk;
	}

	public int getEndX() {
		return endX;
	}

	public void setEndX(int endX) {
		this.endX = endX;
	}

	public int getEndY() {
		return endY;
	}

	public void setEndY(int endY) {
		this.endY = endY;
	}

	public boolean isHasExcess() {
		return hasExcess;
	}

	public void setHasExcess(boolean hasExcess) {
		this.hasExcess = hasExcess;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getThewCost() {
		return thewCost;
	}

	public void setThewCost(int thewCost) {
		this.thewCost = thewCost;
	}

	public TimeLimit getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(TimeLimit timeLimit) {
		this.timeLimit = timeLimit;
	}

	public DeliverTimeLimit getDeliverTimeLimit() {
		return deliverTimeLimit;
	}

	public void setDeliverTimeLimit(DeliverTimeLimit deilverTimeLimit) {
		this.deliverTimeLimit = deilverTimeLimit;
	}

	public int getDailyTaskCycle() {
		return dailyTaskCycle;
	}

	public void setDailyTaskCycle(int dailyTaskCycle) {
		this.dailyTaskCycle = dailyTaskCycle;
	}

	public int getDailyTaskMaxNum() {
		return dailyTaskMaxNum;
	}

	public void setDailyTaskMaxNum(int dailyTaskMaxNum) {
		this.dailyTaskMaxNum = dailyTaskMaxNum;
	}

	public long getReGetCodeDown() {
		return reGetCodeDown;
	}

	public void setReGetCodeDown(long reGetCodeDown) {
		this.reGetCodeDown = reGetCodeDown;
	}

	public int getDependType() {
		return dependType;
	}

	public void setDependType(int dependType) {
		this.dependType = dependType;
	}

	public int getCountryOfficialLimit() {
		return countryOfficialLimit;
	}

	public void setCountryOfficialLimit(int countryOfficialLimit) {
		this.countryOfficialLimit = countryOfficialLimit;
	}

	public int getSeptOfficialLimit() {
		return septOfficialLimit;
	}

	public void setSeptOfficialLimit(int septOfficialLimit) {
		this.septOfficialLimit = septOfficialLimit;
	}

	public int getSeptLimit() {
		return septLimit;
	}

	public void setSeptLimit(int septLimit) {
		this.septLimit = septLimit;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public String getNotice() {
		return notice;
	}

	public String getStartNPCAvataRace() {
		return startNPCAvataRace;
	}

	public void setStartNPCAvataRace(String startNPCAvataRace) {
		this.startNPCAvataRace = startNPCAvataRace;
	}

	public String getStartNPCAvataSex() {
		return startNPCAvataSex;
	}

	public void setStartNPCAvataSex(String startNPCAvataSex) {
		this.startNPCAvataSex = startNPCAvataSex;
	}

	public String getEndNPCAvataRace() {
		return endNPCAvataRace;
	}

	public void setEndNPCAvataRace(String endNPCAvataRace) {
		this.endNPCAvataRace = endNPCAvataRace;
	}

	public String getEndNPCAvataSex() {
		return endNPCAvataSex;
	}

	public void setEndNPCAvataSex(String endNPCAvataSex) {
		this.endNPCAvataSex = endNPCAvataSex;
	}

	public String getStartMapResName() {
		return startMapResName;
	}

	public void setStartMapResName(String startMapResName) {
		this.startMapResName = startMapResName;
	}

	public String getEndMapResName() {
		return endMapResName;
	}

	public void setEndMapResName(String endMapResName) {
		this.endMapResName = endMapResName;
	}

	public boolean isHasRandomTarget() {
		return hasRandomTarget;
	}

	public void setHasRandomTarget(boolean hasRandomTarget) {
		this.hasRandomTarget = hasRandomTarget;
	}

	public int getSealLimit() {
		return sealLimit;
	}

	public void setSealLimit(int sealLimit) {
		this.sealLimit = sealLimit;
	}

	public boolean isHasCollectionTarget() {
		return hasCollectionTarget;
	}

	public void setHasCollectionTarget(boolean hasCollectionTarget) {
		this.hasCollectionTarget = hasCollectionTarget;
	}

	public String getCollections() {
		return collections;
	}

	public void setCollections(String collections) {
		this.collections = collections;
	}

	public double getExcessTargetRate() {
		return excessTargetRate;
	}

	public void setExcessTargetRate(double excessTargetRate) {
		this.excessTargetRate = excessTargetRate;
	}

	public byte getShowTarget() {
		return showTarget;
	}

	public void setShowTarget(byte showTarget) {
		this.showTarget = showTarget;
	}

	public void doOnServerBeforeAdd(Player player) {
		if (this.getThewCost() > 0) {
			player.subVitality(this.getThewCost(), "接取任务:" + getName());// 扣除体力值
		}
	}

	public void doOnServerAfterAdd(Player player) {
		if (this.getGivenArticle() != null) {
			this.getGivenArticle().giveToPlayer(player);
		}
	}

	public boolean isDailyTask() {
		return getType() == TASK_TYPE_DAILY;
	}

	public boolean isBournTask() {
		return getShowType() == TASK_SHOW_TYPE_BOURN;
	}

	public boolean isAutoDeliver() {
		return isAutoDeliver;
	}

	public void setAutoDeliver(boolean isAutoDeliver) {
		this.isAutoDeliver = isAutoDeliver;
	}

	public String getBigCollection() {
		return bigCollection;
	}

	public void setBigCollection(String bigCollection) {
		this.bigCollection = bigCollection;
	}

	public int getSoulLevelLimit() {
		return soulLevelLimit;
	}

	public void setSoulLevelLimit(int soulLevelLimit) {
		this.soulLevelLimit = soulLevelLimit;
	}

	public String getName_stat() {
		return name_stat;
	}

	public void setName_stat(String name_stat) {
		this.name_stat = name_stat;
	}

	public String getGroupName_stat() {
		return groupName_stat;
	}

	public void setGroupName_stat(String groupName_stat) {
		this.groupName_stat = groupName_stat;
	}

	public String getCollections_stat() {
		return collections_stat;
	}

	public void setCollections_stat(String collections_stat) {
		this.collections_stat = collections_stat;
	}

	public String getBigCollection_stat() {
		return bigCollection_stat;
	}

	public void setBigCollection_stat(String bigCollection_stat) {
		this.bigCollection_stat = bigCollection_stat;
	}

	public boolean isServerAutoAccept() {
		return serverAutoAccept;
	}

	public void setServerAutoAccept(boolean serverAutoAccept) {
		this.serverAutoAccept = serverAutoAccept;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", name=" + name + "]";
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public boolean needAnswer() {
		return TaskManager.needAnswerTasks.contains(this.getId());
	}
	
	public TaskSubSystem.ModifyType do4Player(List<Long> playerIds, byte actType) {
		TaskSubSystem.ModifyType mtype = null;
		if(actType == 1) {
			Player player = null;
			mtype = TaskSubSystem.ModifyType.DAY_CHANGE_MODIFY_CURRENT;
			for(int i=0; i<playerIds.size(); i++) {
				try {
					player = GamePlayerManager.getInstance().getPlayer(playerIds.get(i));
					TaskEntity entity = player.getTaskEntity(this.getId());
					if(entity != null) {
						entity.setStatus(TaskSubSystem.TASK_STATUS_GIVEUP);
						entity.sendEntityChange((byte)3);
						player.getAllTask().remove(entity);
						FunctionNPC functionNPC = player.getFunctionNPC(this.getStartMap(), this.getStartNpc());
						if (functionNPC != null) {// 有这个NPC
							functionNPC.addTask2Wait(TaskSubSystem.ModifyType.DAY_CHANGE, this);
						}
						player.checkFunctionNPCModify(TaskSubSystem.ModifyType.DAY_CHANGE_MODIFY_CURRENT);
					}
				} catch(Exception e) {
					TaskManager.logger.error("[时限任务] [检测异常][pId:" + playerIds.get(i) + "][ taskId :" + this.getId() + "]", e);
				}
			}
		} else if (actType == 2) {
			mtype = TaskSubSystem.ModifyType.DAY_CHANGE;
		}
		return mtype;
	}

//	@Override
//	public void doAct(List<Long> playerIds) {
//		// TODO Auto-generated method stub
//		byte actType = getActType();
//		if(playerIds == null || playerIds.size() <= 0 || actType == 0) {
//			return;
//		}
//		List<Player> allGamePlayers = new ArrayList<Player>();			//维护functionNpc窗口
//		for(int i=0; i<=4; i++){
//			Game game = GameManager.getInstance().getGameByDisplayName(this.getStartMap(), i);
//			if(game != null) {
//				allGamePlayers.addAll(game.getPlayers());
//			}
//		}
//		TaskSubSystem.ModifyType mtype = do4Player(playerIds, actType);
//		for(Player pers : allGamePlayers) {
//			pers.checkFunctionNPCModify(mtype);
//		}
//	}
}
