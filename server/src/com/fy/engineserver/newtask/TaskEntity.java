package com.fy.engineserver.newtask;

import java.util.Arrays;

import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_DELIVER_TASK_REQ;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.message.TASK_SEND_ENTITY_REQ;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.newtask.service.TaskEventTransactCenter;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.newtask.service.Taskoperation;
import com.fy.engineserver.newtask.targets.RandomNum;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.TimeTool.TimeDistance;
import com.xuanzhi.tools.cache.CacheListener;
import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 玩家的任务信息
 * 
 * 
 */
@SimpleEntity
@SimpleIndices({ @SimpleIndex(members = { "playerId" }), @SimpleIndex(members = { "taskId" }), @SimpleIndex(members = { "status" }),@SimpleIndex(members = {"showType"}) })
public class TaskEntity implements TaskConfig, Cacheable, CacheListener {
	// 基本信息
	@SimpleId
	private long id;
	@SimpleVersion
	private int version;
	/** 角色ID */
	private long playerId;
	/** 任务ID */
	private long taskId;

	private transient long taskGrade;
	/** 任务名称 (冗余数据) */
	private transient String taskName;
	/** 任务状态 */
	private int status;

	/** 第一次接取时间 */
	private long firstGetTime;
	/** 第一次完成时间 */
	private long firstDeliverTime;
	/** 最后一次接取时间 */
	private long lastGetTime;
	/** 最后一次完成时间 */
	private long lastDeliverTime;

	/** 总完成次数 */
	private int totalDeliverTimes;
	/** 任务完成度 ,与任务targets对应 */
	@SimpleColumn(saveInterval = 600)
	private int[] completed;
	/** 任务目标需求个数 */
	private int[] taskDemand;

	/** 随机目标数量任务计数 */
	private int randomTargetNum = 0;

	/** 任务得分 */
	private int score;
	/** 最后一次放弃时间 */
	private long lastGiveUpTime;

	private transient Task task;
	private transient Player owner;
	// 日常 //循环
	/** 周期内完成次数 */
	private int cycleDeliverTimes;
	/** 额外的目标 得到后会得到更多经验奖励 */
	private boolean excess = false;
	/** 是否是时间 限制任务 */
	private boolean isDeliverTimeLimit;
	/** 时间 限制任务距离完成时间 */
	private long leftDeliverTime;
	/** 时间限制 */
	private long failTime;
	/** 任务类型 */
	private byte showType;

	public TaskEntity() {

	}

	@Override
	protected void finalize() throws Throwable {

	}

	public TaskEntity(Task task, Player player) throws Exception {
		this();
		this.id = TaskEntityManager.em.nextId();
		this.setPlayerId(player.getId());
		this.setOwner(player);
		this.setTaskId(task.getId());
		this.setTask(task);
		this.setTaskName(task.getName());
		this.setFirstGetTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		this.setLastGetTime(this.getFirstGetTime());
		this.setStatus(TASK_STATUS_GET);
		this.setCompleted(new int[task.getTargets().length]);
		this.setShowType(task.getShowType());
		int[] demands = new int[task.getTargets().length];
		this.setTaskGrade(task.getGrade());

		int randomNum = 0;
		for (int i = 0; i < task.getTargets().length; i++) {
			if (task.getTargets()[i].isRandomNum()) {
				if (task.getTargets()[i] instanceof RandomNum) {
					setRandomTargetNum(TaskManager.getRandom(task.getTargets()[i]));
					randomNum++;
					demands[i] = getRandomTargetNum();
					if (TaskSubSystem.logger.isDebugEnabled()) {
						TaskSubSystem.logger.debug("[玩家:{}] [接取任务:{}] [随机数量:{}]", new Object[] { player.getName(), task.getName(), getRandomTargetNum() });
					}
				}
			} else {
				demands[i] = task.getTargets()[i].getTargetNum();
			}
		}
		setTaskDemand(demands);
	}

	/**
	 * 修改任务的完成度
	 * @param index
	 * @param changeNum
	 */
	public synchronized void modifyPercentageCompleted(int index, int changeNum) {
		if (index < 0 || index > completed.length) {
			return;
		}
		if (completed[index] >= taskDemand[index]) {
			return;
		}
		if (completed[index] + changeNum >= taskDemand[index]) {
			completed[index] = taskDemand[index];
			TaskEntityManager.em.notifyFieldChange(this, "completed");
		} else {
			completed[index] += changeNum;
			TaskEntityManager.em.notifyFieldChange(this, "completed");
		}
		task.getTargets()[index].sendModifyMessage(index, this);
		if (taskComplete()) {
			TaskEventTransactCenter.getInstance().dealWithTask(Taskoperation.done, task, owner, owner.getCurrentGame());
			if (task.isAutoDeliver()) {
				if (TaskSubSystem.logger.isDebugEnabled()) {
					TaskSubSystem.logger.debug(owner.getLogString() + " [完成了自动完成任务:{}]", new Object[] { task.getName() });
				}
				NOTICE_CLIENT_DELIVER_TASK_REQ client_DELIVER_TASK_REQ = new NOTICE_CLIENT_DELIVER_TASK_REQ(GameMessageFactory.nextSequnceNum(), task.getId());
				owner.addMessageToRightBag(client_DELIVER_TASK_REQ);
				// 境界的探索任务弹出任务结束对话
				if (task.getShowType() == TASK_SHOW_TYPE_BOURN && task.getTargetByType(TargetType.DISCOVERY) != null && task.getTargetByType(TargetType.DISCOVERY).size() > 0) {
					MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
					mw.setDescriptionInUUB(task.getEndTalk());
					mw.setNpcId(-1);
					QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, new Option[0]);
					owner.addMessageToRightBag(res);
					if (TaskSubSystem.logger.isDebugEnabled()) {
						TaskSubSystem.logger.debug("[任务:{}实体处理] [是境界探索任务] [任务完成] [弹出提示:{}]", new Object[] { task.getName(), task.getEndTalk() });
					}
				}
			}
		}
		sendEntityChange((byte) 0);
	}

	/**
	 * 通知客户端任务实体发生了变化
	 * @param actionType
	 * 
	 */
	public void sendEntityChange(byte actionType) {
		TASK_SEND_ENTITY_REQ req = new TASK_SEND_ENTITY_REQ(GameMessageFactory.nextSequnceNum(), actionType, this);
		owner.addMessageToRightBag(req);
	}

	/**
	 * 第index个目标是否达成
	 * @param index
	 * @return
	 */
	public boolean isComplete(int index) {
		return completed[index] >= taskDemand[index];
	}

	/**
	 * 任务是否完成
	 * @return
	 */
	public boolean taskComplete() {
		if (getStatus() == TASK_STATUS_DEILVER) {
			return true;
		}
		if (getStatus() == TASK_STATUS_COMPLETE) {
			return true;
		}
		if (getStatus() == TASK_STATUS_FAILED) {
			return false;
		}
		if (getStatus() == TASK_STATUS_GIVEUP) {
			return false;
		}
		for (int i = 0, j = completed.length; i < j; i++) {
			if (!isComplete(i)) {
				return false;
			}
		}
		setStatus(TASK_STATUS_COMPLETE);
		return true;
	}

	public boolean isDeliverTimeLimit() {
		return isDeliverTimeLimit;
	}

	public void setDeliverTimeLimit(boolean isDeliverTimeLimit) {
		this.isDeliverTimeLimit = isDeliverTimeLimit;
		if (version > 0) {
			TaskEntityManager.em.notifyFieldChange(this, "isDeliverTimeLimit");
		}
	}

	public long getLeftDeliverTime() {
		return leftDeliverTime;
	}

	public void setLeftDeliverTime(long leftDeliverTime) {
		this.leftDeliverTime = leftDeliverTime;
		if (version > 0) {
			TaskEntityManager.em.notifyFieldChange(this, "leftDeliverTime");
		}
	}

	/**
	 * 周期任务当前时间是否允许接任务
	 * 周期内接的任务是否达到上限
	 * @return
	 */
	public boolean timeAllow() {
		if (task.getType() == TASK_TYPE_ONCE) {// 单次任务不受时间限制
			return true;
		}
		if (task.getType() == TASK_TYPE_DAILY) {// 日常任务受时间限制
			// 当前时间和最后一次完成时间在同一个计算周期内
			if (TimeTool.instance.isSame(com.fy.engineserver.gametime.SystemTime.currentTimeMillis(), getLastDeliverTime(), TimeDistance.DAY, task.getDailyTaskCycle())) {
				if (getCycleDeliverTimes() >= task.getDailyTaskMaxNum()) {
					return false;
				}
			} else {
				setCycleDeliverTimes(0);
				return true;
			}
		}
		return true;
	}

	/**
	 * 得到非单次任务的完成信息<br/>
	 * booleanValue 本周期是否还能再做<br/>
	 * intValue 本周期剩余可完成次数
	 * @return
	 */
	public CompoundReturn getCycleDeilverInfo() {
		// 最后一次完成和现在非同一个周期
		if (!TimeTool.instance.isSame(com.fy.engineserver.gametime.SystemTime.currentTimeMillis(), getLastDeliverTime(), TimeDistance.DAY, task.getDailyTaskCycle())) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(true).setIntValue(task.getDailyTaskMaxNum());
		} else {
			return CompoundReturn.createCompoundReturn().setBooleanValue((task.getDailyTaskMaxNum() - getCycleDeliverTimes()) > 0).setIntValue(task.getDailyTaskMaxNum() - getCycleDeliverTimes());
		}
	}

	/**
	 * 任务重置(日常,跑环)<BR/>
	 * 设置初始状态<BR/>
	 * 单次任务无效
	 */
	public void reSet() {
		this.setLastGetTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		this.setScore(0);
		this.setLeftDeliverTime(0);
		this.setStatus(TASK_STATUS_GET);

		completed = new int[task.getTargets().length];
		taskDemand = new int[task.getTargets().length];

		for (int i = 0; i < task.getTargets().length; i++) {
			completed[i] = 0;
			if (task.getTargets()[i].isRandomNum()) {
				taskDemand[i] = TaskManager.getRandom(task.getTargets()[i]);
				setRandomTargetNum(completed[i]);
			} else {
				taskDemand[i] = task.getTargets()[i].getTargetNum();
			}
		}
		TaskEntityManager.em.notifyFieldChange(this, "completed");
		TaskEntityManager.em.notifyFieldChange(this, "taskDemand");
	}

	/**
	 * 当接取任务时候对任务目标做处理<br/>
	 * 是否已经满足
	 */
	public void dealOnGet() {
		for (int i = 0; i < getTask().getTargets().length; i++) {
			CompoundReturn compoundReturn = getTask().getTargets()[i].dealOnGet(getOwner(), getTask());
			if (compoundReturn.getBooleanValue()) {
				if (compoundReturn.getIntValue() > 0) {
					modifyPercentageCompleted(i, compoundReturn.getIntValue());
				}
			}
		}
	}

	public boolean isChanged(Task task) {
		if (task.getTargets().length != taskDemand.length) {
			return true;
		}
		return false;
	}

	/*************************************************************************************************************************************/
	/******************************************************** getters and setters ********************************************************/
	/*************************************************************************************************************************************/

	public long getPlayerId() {
		return playerId;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
		if (version > 0) {
			TaskEntityManager.em.notifyFieldChange(this, "score");
		}
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
		if (version > 0) {
			TaskEntityManager.em.notifyFieldChange(this, "playerId");
		}
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
		if (version > 0) {
			TaskEntityManager.em.notifyFieldChange(this, "taskId");
		}
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
		if (version > 0) {
			TaskEntityManager.em.notifyFieldChange(this, "status");
		}
	}

	public long getFirstGetTime() {
		return firstGetTime;
	}

	public void setFirstGetTime(long firstGetTime) {
		this.firstGetTime = firstGetTime;
		if (version > 0) {
			TaskEntityManager.em.notifyFieldChange(this, "firstGetTime");
		}
	}

	public long getLastGetTime() {
		return lastGetTime;
	}

	public void setLastGetTime(long lastGetTime) {
		this.lastGetTime = lastGetTime;
		if (version > 0) {
			TaskEntityManager.em.notifyFieldChange(this, "lastGetTime");
		}
	}

	public long getLastDeliverTime() {
		return lastDeliverTime;
	}

	public void setLastDeliverTime(long lastDeliverTime) {
		if (this.firstDeliverTime == 0) {
			setFirstDeliverTime(lastDeliverTime);
		}
		this.lastDeliverTime = lastDeliverTime;
		if (version > 0) {
			TaskEntityManager.em.notifyFieldChange(this, "lastDeliverTime");
		}
	}

	public int getTotalDeliverTimes() {
		return totalDeliverTimes;
	}

	public void setTotalDeliverTimes(int totalDeliverTimes) {
		this.totalDeliverTimes = totalDeliverTimes;
		if (version > 0) {
			TaskEntityManager.em.notifyFieldChange(this, "totalDeliverTimes");
		}
	}

	public int getCycleDeliverTimes() {
		return cycleDeliverTimes;
	}

	public void setCycleDeliverTimes(int cycleDeliverTimes) {
		this.cycleDeliverTimes = cycleDeliverTimes;
		if (version > 0) {
			TaskEntityManager.em.notifyFieldChange(this, "cycleDeliverTimes");
		}
	}

	public long getFirstDeliverTime() {
		return firstDeliverTime;
	}

	public void setFirstDeliverTime(long firstDeliverTime) {
		this.firstDeliverTime = firstDeliverTime;
		if (version > 0) {
			TaskEntityManager.em.notifyFieldChange(this, "firstDeliverTime");
		}
	}

	public long getLastGiveUpTime() {
		return lastGiveUpTime;
	}

	public void setLastGiveUpTime(long lastGiveUpTime) {
		this.lastGiveUpTime = lastGiveUpTime;
		if (version > 0) {
			TaskEntityManager.em.notifyFieldChange(this, "lastGiveUpTime");
		}
	}

	public boolean isExcess() {
		return excess;
	}

	public void setExcess(boolean excess) {
		this.excess = excess;
		if (version > 0) {
			TaskEntityManager.em.notifyFieldChange(this, "excess");
		}
	}

	public long getFailTime() {
		return failTime;
	}

	public void setFailTime(long failTime) {
		this.failTime = failTime;
		if (version > 0) {
			TaskEntityManager.em.notifyFieldChange(this, "failTime");
		}
	}

	public int[] getCompleted() {
		return completed;
	}

	public void setCompleted(int[] completed) {
		this.completed = completed;
		if (version > 0) {
			TaskEntityManager.em.notifyFieldChange(this, "completed");
		}
	}

	public byte getShowType() {
		return showType;
	}

	public void setShowType(byte showType) {
		this.showType = showType;
		if (version > 0) {
			TaskEntityManager.em.notifyFieldChange(this, "showType");
		}
	}

	public int getRandomTargetNum() {
		return randomTargetNum;
	}

	public void setRandomTargetNum(int randomTargetNum) {
		this.randomTargetNum = randomTargetNum;
		if (version > 0) {
			TaskEntityManager.em.notifyFieldChange(this, "randomTargetNum");
		}
	}

	public int[] getTaskDemand() {
		return taskDemand;
	}

	public void setTaskDemand(int[] taskDemand) {
		this.taskDemand = taskDemand;
		if (version > 0) {
			TaskEntityManager.em.notifyFieldChange(this, "taskDemand");
		}
	}

	public long getTaskGrade() {
		return taskGrade;
	}

	public void setTaskGrade(long taskGrade) {
		this.taskGrade = taskGrade;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "TaskEntity [playerId=" + playerId + ", taskId=" + taskId + ", taskName=" + taskName + ", status=" + status + ", firstGetTime=" + firstGetTime + ", firstDeliverTime=" + firstDeliverTime + ", lastGetTime=" + lastGetTime + ", lastDeliverTime=" + lastDeliverTime + ", totalDeliverTimes=" + totalDeliverTimes + ", completed=" + Arrays.toString(completed) + ", taskDemand=" + Arrays.toString(taskDemand) + ", randomTargetNum=" + randomTargetNum + ", score=" + score + ", lastGiveUpTime=" + lastGiveUpTime + ", cycleDeliverTimes=" + cycleDeliverTimes + ", excess=" + excess + ", isDeliverTimeLimit=" + isDeliverTimeLimit + ", leftDeliverTime=" + leftDeliverTime + ", failTime=" + failTime + ", showType=" + showType + "]";
	}

	@Override
	public void remove(int type) {
		// TODO Auto-generated method stub
		if (type == CacheListener.LIFT_TIMEOUT) {
			TaskEntityManager.getInstance().notifyRemoveFromCache(this);
		}
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 1;
	}
}
