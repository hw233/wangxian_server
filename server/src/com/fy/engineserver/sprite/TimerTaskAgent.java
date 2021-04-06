package com.fy.engineserver.sprite;

//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.homestead.cave.AbsCallbackable;

/**
 * 
 * 
 * @version 创建时间：Sep 19, 2011 11:42:12 AM
 * 
 */
public class TimerTaskAgent {

	public static Logger logger = LoggerFactory.getLogger(TimerTaskAgent.class);

	private TimerTask task;

	private Player owner;

	private long lastCheckTime;

	public TimerTaskAgent(Player player) {
		this.owner = player;
	}

	public long getLastCheckTime() {
		return lastCheckTime;
	}

	public void setLastCheckTime(long lastCheckTime) {
		this.lastCheckTime = lastCheckTime;
	}

	public TimerTask getTask() {
		return task;
	}

	public void setTask(TimerTask task) {
		this.task = task;
	}

	public synchronized boolean createSpecialTimerTaks(Callbackable callbacker, long delay, int type, Player active, Player passive) {
		int breakFlags[] = TimerTask.getBreakFlags(type);

		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		SpecialTimerTask spt = new SpecialTimerTask();
		spt.setBreakFlags(breakFlags);
		spt.setEndTime(now + delay);
		spt.setCallbacker(callbacker);
		spt.setStartTime(now);
		spt.setType(type);
		boolean result = true;
		if (task == null) {
			task = spt;
				if (logger.isDebugEnabled()) logger.debug("[创建一个特殊进度任务] [直接赋值] [{}] [type:{}] [{}ms]", new Object[] { owner.getLogString(), TimerTask.getTypeDesp(task.getType()), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now });
		} else {
			if (type == task.getType()) {
				// 同类型，直接替换
				task = spt;
					if (logger.isDebugEnabled()) logger.debug("[创建一个特殊进度任务] [同类型替换] [{}] [old:{}] [type:{}] [{}ms]", new Object[] { owner.getLogString(), TimerTask.getTypeDesp(task.getType()), TimerTask.getTypeDesp(spt.getType()), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now });
			} else {
				// 不同类型
				if (task.isBreak(type)) {
					String oldType = TimerTask.getTypeDesp(task.getType());
					task = spt;
					if(logger.isDebugEnabled())
						logger.debug("[创建一个特殊进度任务] [不同类型，打断原来的任务，重新创建新任务] [{}] [old:{}] [type:{}] [{}ms]", new Object[] { owner.getLogString(), oldType, TimerTask.getTypeDesp(spt.getType()), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now });
				} else {
					result = false;
					// 不能打断，不创建
						if (logger.isDebugEnabled()) logger.debug("[创建一个特殊进度任务] [不同类型，无法打断原来的任务，放弃创建] [{}] [old:{}] [type:{}] [{}ms]", new Object[] { owner.getLogString(), TimerTask.getTypeDesp(task.getType()), TimerTask.getTypeDesp(spt.getType()), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now });
				}
			}
		}
		return result;

	}

	public synchronized void createAbTimerTask(TimerTask tt,Callbackable callbacker, long delay, int type) {
		
		int breakFlags[] = TimerTask.getBreakFlags(type);
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		TimerTask spt = tt;
		spt.setBreakFlags(breakFlags);
		spt.setEndTime(now + delay);
		spt.setCallbacker(callbacker);
		spt.setStartTime(now);
		spt.setType(type);
		if (task == null) {
			task = spt;
				if (logger.isDebugEnabled()) logger.debug("[创建一个指定进度任务] [直接赋值] [{}] [type:{}] [{}ms]", new Object[] { owner.getLogString(), TimerTask.getTypeDesp(task.getType()), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now });
		} else {
			if (type == task.getType()) {
				// 同类型，直接替换
				task = spt;
					if (logger.isDebugEnabled()) logger.debug("[创建一个指定进度任务] [同类型替换] [{}] [old:{}] [type:{}] [{}ms]", new Object[] { owner.getLogString(), TimerTask.getTypeDesp(task.getType()), TimerTask.getTypeDesp(spt.getType()), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now });
			} else {
				// 不同类型
				if (task.isBreak(type)) {
					String oldType = TimerTask.getTypeDesp(task.getType());
					task = spt;
					if(logger.isDebugEnabled())
						logger.debug("[创建一个指定进度任务] [不同类型，打断原来的任务，重新创建新任务] [{}] [old:{}] [type:{}] [{}ms]", new Object[] { owner.getLogString(), oldType, TimerTask.getTypeDesp(spt.getType()), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now });
				} else {
					// 不能打断，不创建
						if (logger.isDebugEnabled()) logger.debug("[创建一个指定进度任务] [不同类型，无法打断原来的任务，放弃创建] [{}] [old:{}] [type:{}] [{}ms]", new Object[] { owner.getLogString(), TimerTask.getTypeDesp(task.getType()), TimerTask.getTypeDesp(spt.getType()), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now });
				}
			}
		}

	}
	
	
	/**
	 * 创建一个进度任务，已存在同类型的将会被替换，已存在不同类型的看当前任务是否会被此类型打断，如果可以被打断，
	 * 那么打断原来的，置入新建的；如果不能打断，那么此创建无效。
	 * @param callbacker
	 *            读条完成后执行回调
	 * @param delay
	 *            读条时间
	 * @param type
	 *            见TimerTask.type_*
	 */
	public synchronized void createTimerTask(Callbackable callbacker, long delay, int type) {
		int breakFlags[] = TimerTask.getBreakFlags(type);
		createTimerTask(callbacker, delay, type, breakFlags);
	}

	/**
	 * 创建一个进度任务，已存在同类型的将会被替换，已存在不同类型的看当前任务是否会被此类型打断，如果可以被打断，
	 * 那么打断原来的，置入新建的；如果不能打断，那么此创建无效。
	 * @param executor
	 * @param delay
	 * @param type
	 * @param breakFlags
	 *            打断的条件状态
	 */
	private synchronized void createTimerTask(Callbackable callbacker, long delay, int type, int[] breakFlags) {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		TimerTask t = new TimerTask();
		t.setBreakFlags(breakFlags);
		t.setEndTime(now + delay);
		t.setCallbacker(callbacker);
		t.setStartTime(now);
		t.setType(type);
		if (task == null) {
			task = t;
				if (logger.isDebugEnabled()) logger.debug("[创建一个进度任务] [直接赋值] [{}] [type:{}] [{}ms]", new Object[] { owner.getLogString(), TimerTask.getTypeDesp(task.getType()), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now });
		} else {
			if (type == task.getType()) {
				// 同类型，直接替换
				task = t;
					if (logger.isDebugEnabled()) logger.debug("[创建一个进度任务] [同类型替换] [{}] [old:{}] [type:{}] [{}ms]", new Object[] { owner.getLogString(), TimerTask.getTypeDesp(task.getType()), TimerTask.getTypeDesp(t.getType()), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now });
			} else {
				// 不同类型
				if (task.isBreak(type)) {
					String oldType = TimerTask.getTypeDesp(task.getType());
					task = t;
						if (logger.isDebugEnabled()) logger.debug("[创建一个进度任务] [不同类型，打断原来的任务，重新创建新任务] [{}] [old:{}] [type:{}] [{}ms]", new Object[] { owner.getLogString(), oldType, TimerTask.getTypeDesp(t.getType()), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now });
				} else {
					// 不能打断，不创建
						if (logger.isDebugEnabled()) logger.debug("[创建一个进度任务] [不同类型，无法打断原来的任务，放弃创建] [{}] [old:{}] [type:{}] [{}ms]", new Object[] { owner.getLogString(), TimerTask.getTypeDesp(task.getType()), TimerTask.getTypeDesp(t.getType()), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now });
				}
			}
		}
	}

	public void heartbeatCheck(long startTime) {
		this.lastCheckTime = startTime;
		if (task != null) {
			if (startTime > task.getEndTime()) {
				TimerTask temp = task;
				synchronized (this) {
					if (task == temp && task != null) {
						try {
							task.getCallbacker().callback();
								if (logger.isDebugEnabled()) logger.debug("[进度计时完成] [执行任务] [{}] [type:{}] [start:{}] [end:{}] [{}ms]", new Object[] { owner.getLogString(), TimerTask.getTypeDesp(task.getType()), task.getStartTime(), task.getEndTime(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime });
						} catch (Exception e) {
							logger.error("[进度计时完成] [执行时发生异常] [" + owner.getLogString() + "] [type:" + TimerTask.getTypeDesp(task.getType()) + "] [start:" + task.getStartTime() + "] [end:" + task.getEndTime() + "]", e);
						}
						AbsCallbackable absCallbackable = null;
						if (task.getCallbacker() instanceof AbsCallbackable) {
							absCallbackable = ((AbsCallbackable) task.getCallbacker());
						}

						task = null;
						try {
							if (absCallbackable != null) {
								if (logger.isDebugEnabled()) {
									logger.debug("[执行下一个{}]", new Object[] { absCallbackable.getClass() });
								}
								absCallbackable.doNext();
							}
						} catch (Exception e) {
							logger.error("[执行next异常]", e);
						}
					}
				}
			}
		}
	}

	/**
	 * 通知发生了移动
	 */
	public void notifyMoved() {
		checkTask(TimerTask.event_移动);
	}

	/**
	 * 通知发动攻击
	 */
	public void notifyAttacking() {
		checkTask(TimerTask.event_施放技能);
	}

	/**
	 * 通知被攻击
	 */
	public void notifyBeAttacked() {
		checkTask(TimerTask.event_被攻击);
	}

	/**
	 * 通知采集
	 */
	public void notifyCollection() {
		checkTask(TimerTask.event_采集);
	}

	/**
	 * 通知使用了道具
	 */
	public void notifyUseProps() {
		checkTask(TimerTask.event_使用道具);
	}

	/**
	 * 通知打坐
	 */
	public void notifySit() {
		checkTask(TimerTask.event_打坐);
	}

	/**
	 * 通知上马
	 */
	public void notifyRide() {
		checkTask(TimerTask.event_上马);
	}

	/**
	 * 通知死亡
	 */
	public void notifyDead() {
		checkTask(TimerTask.event_死亡);
	}

	/**
	 * 通知交易
	 */
	public void notifyDeal() {
		checkTask(TimerTask.event_交易);
	}

	/**
	 * 通知元神切换
	 */
	public void notifyYuanshengqiehuan() {
		checkTask(TimerTask.event_元神切换);
	}

	/**
	 * 通知被动传送
	 */
	public void notifyPassiveTransfer() {
		checkTask(TimerTask.event_传送);
	}

	/**
	 * 通知上下线
	 */
	public void notifyUpOrDown() {
		checkTask(TimerTask.event_上下线);
	}
	/**
	 * 通知捕捉失败
	 */
	public void notifyCatchFailed() {
		checkTask(TimerTask.event_捕捉失败);
	}

	private void checkTask(int actionType) {
		if (task != null) {
			synchronized (this) {
				if (task != null) {
					boolean breakable = task.isBreak(actionType);
					if (breakable) {
						if (owner != null) {
							try {
								task.breakNoticePlayer(owner);
							} catch (Exception e) {
								logger.error(owner.getLogString() + "[打断异常] [打断类型:{}] [打断对象{}]", new Object[] { actionType, task.getCallbacker().getClass() }, e);
							}
						}
						task = null;
					}
				}
			}
		}
	}
}
