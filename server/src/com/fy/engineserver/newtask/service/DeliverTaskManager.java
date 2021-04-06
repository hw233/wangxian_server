package com.fy.engineserver.newtask.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.newtask.DeliverTask;
import com.fy.engineserver.newtask.NewDeliverTaskManager;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class DeliverTaskManager {
	public static Logger logger = LoggerFactory.getLogger(DeliverTaskManager.class);
	static DeliverTaskManager self;

	private LruMapCache dtcache = new LruMapCache(1024 * 128, 1800000L, false, "DeliverTaskCache");
	
	public Map<Long, DeliverTask> tempMap = new Hashtable<Long, DeliverTask>(); 
	
	public static int deleteFlag = -300;

	public static DeliverTaskManager getInstance() {
		return self;
	}

	public SimpleEntityManager<DeliverTask> em;

	//HashMap<Long, HashMap<Long, DeliverTask>> map = new HashMap<Long, HashMap<Long, DeliverTask>>();

	public void init() throws Exception {
		
		em = SimpleEntityManagerFactory.getSimpleEntityManager(DeliverTask.class);
		self = this;
		ServiceStartRecord.startLog(this);

	}

	public void destroy() {
		if (em != null) {
			em.destroy();
		}
	}

	public void save(DeliverTask dt) {
		try {
			em.save(dt);
		} catch (Exception e) {
			if (logger.isWarnEnabled()) {
				logger.warn("[在cache移除时保存DT] [异常]  [id:" + dt.getId() + "] [task:" + dt.getTaskId() + "] [player:--]", e);
			}
		}
	}

	public void notifyNewDeliverTask(Player p, DeliverTask dt) {
		try {
			em.notifyNewObject(dt);
			dtcache.put(dt.getId(), dt);
			tempMap.put(dt.getId(), dt);

		} catch (Exception e) {
			if (logger.isWarnEnabled()) {
				logger.warn("[通知新的完成实体] [异常]  [id:" + dt.getId() + "] [task:" + dt.getTaskId() + "] [player:" + p.getLogString() + "]", e);
			}
		}
		synchronized (p) {
			HashMap<Long, DeliverTask> al = p.deliverTaskMap;
			if (al == null) {
				al = new HashMap<Long, DeliverTask>();
				p.deliverTaskMap = al;
			}
			dt.setDeliver(true);
			al.put(dt.getTaskId(), dt);
		}
	}
	
	public void notifyDeleteFromCache(DeliverTask dt) {
		try {
			NewDeliverTaskManager ndtm = NewDeliverTaskManager.getInstance();
			if(ndtm != null && ndtm.isNewDeliverTaskAct) {
				dt.setDeliverTimes(deleteFlag);
				em.notifyFieldChange(dt, "deliverTimes");
			} else {
				em.remove(dt);
			}
			if (dtcache.get(dt.getId()) != null) {
				dtcache.remove(dt.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[删除delivertask数据异常]",e);
		}
	}

	public void notifyRemoveFromServerMemory(Player p) {
		long startTime = System.currentTimeMillis();
		synchronized (p) {
			HashMap<Long, DeliverTask> al = p.deliverTaskMap;
			p.deliverTaskMap = null;
			if (al == null) {
				return;
			}
			Iterator<Long> it = al.keySet().iterator();
			while (it.hasNext()) {
				Long id = it.next();
				DeliverTask dt = al.get(id);
				if (dt.getVersion() == 0 && dt.isDeliver()) {
					try {
						em.flush(dt);
					} catch (Exception e) {
						logger.error("[角色从内存中清空移除任务] [保存任务失败,数据丢失] [id:" + dt.getId() + "] [task:" + dt.getTaskId() + "] [player:" + p.getLogString() + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
					}
				}
			}

		}
		if (logger.isInfoEnabled()) {
			logger.info("[角色从内存中清空移除任务] [成功] [ret:-] [player:" + p.getLogString() + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
		}
	}

	public void notifyEnterGame(Game game, Player p) {
		long startTime = System.currentTimeMillis();
		try {
			Long allTaskId[] = game.getAllTasks();
			int loadNum = loadDeliverTask4Player(p, allTaskId);
			if (logger.isInfoEnabled()) {
				logger.info("[进入地图加载完成任务实体] [成功] [ret:" + loadNum + "] [player:" + p.getLogString() + "] [game:" + game.getGameInfo().getName() + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
			}

		} catch (Exception e) {
			logger.error("[进入地图加载完成任务实体] [异常] [ret:-] [player:" + p.getLogString() + "] [game:" + game.getGameInfo().getName() + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]", e);
		}
	}

	public void batchLoadDeliverTask(Player player, List<Task> tasks) {
		long startTime = System.currentTimeMillis();
		try {
			if (tasks == null || tasks.size() == 0) {
				return;
			}
			Long[] allTaskId = new Long[tasks.size()];
			for (int i = 0; i < tasks.size(); i++) {
				allTaskId[i] = tasks.get(i).getId();
			}
			int loadNum = loadDeliverTask4Player(player, allTaskId);
			if (logger.isInfoEnabled()) {
				logger.info("[批量加载完成任务实体] [成功] [ret:" + loadNum + "] [player:" + player.getLogString() + "] [game:-] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
			}
		} catch (Exception e) {
			logger.error("[进入地图加载完成任务实体] [异常] [ret:-] [player:" + player.getLogString() + "] [game:-] [cost:" + (System.currentTimeMillis() - startTime) + "ms]", e);
		}
	}

	private int loadDeliverTask4Player(Player p, Long[] allTaskId) throws Exception {

		// 查询内存有的任务就不重复查了
		Long taskIds[] = null;// game.getAllTasks();
		if (p.deliverTaskMap != null) {
			HashMap<Long, DeliverTask> dts = p.deliverTaskMap;
			synchronized (p) {
				List<Long> ids = new ArrayList<Long>();
				for (int i = 0; i < allTaskId.length; i++) {
					if (!dts.containsKey(allTaskId[i])) {
						ids.add(allTaskId[i]);
					}
				}
				taskIds = ids.toArray(new Long[0]);
			}
		} else {
			taskIds = allTaskId;
		}

		if (taskIds.length == 0) {
			return 0;
		}
		StringBuffer sb = new StringBuffer();
		Object[] paramValues = new Object[taskIds.length+1];
		sb.append("playerId= ? and (");
		paramValues[0] = p.getId();
		for (int i = 0; i < taskIds.length; i++) {
			sb.append("taskId=?");
			paramValues[i+1] = taskIds[i];
			if (i < taskIds.length - 1) {
				sb.append(" OR ");
			}
		}
		sb.append(")");
		List<DeliverTask> list = em.query(DeliverTask.class, sb.toString(), paramValues,"", 1, taskIds.length + 1000);

		synchronized (p) {
			HashMap<Long, DeliverTask> al = p.deliverTaskMap;
			if (al == null) {
				al = new HashMap<Long, DeliverTask>();
				p.deliverTaskMap = al;
			}

			for (DeliverTask dt : list) {
				dt.setDeliver(true);
				al.put(dt.getTaskId(), dt);
			}

			for (int i = 0; i < taskIds.length; i++) {
				if (!al.containsKey(taskIds[i])) {
					DeliverTask dt = new DeliverTask();
					dt.setTaskId(taskIds[i]);
					dt.setPlayerId(p.getId());
					dt.setDeliver(false);
					dt.setId(-1);
					dt.setVersion(-1);
					al.put(dt.getTaskId(), dt);
				}
			}
		}
		return list.size();
	}

	/**
	 * 出现任何错误,都认为不能接任务
	 * @param p
	 * @param task
	 * @return
	 */
	public boolean isDelived(Player p, Task task) {
		long startTime = System.currentTimeMillis();
		HashMap<Long, DeliverTask> al = p.deliverTaskMap;
		if (al != null) {
			DeliverTask dt = al.get(task.getId());
			if (dt != null) {
				return dt.isDeliver();
			}
		}

		DeliverTask d;
		try {
			d = load(p.getId(), task.getId());
		} catch (Exception e) {
			logger.error("[从DB中load完成任务] [异常] [player:" + p.getId() + "] [task:" + task.getId() + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]", e);
			return true;
		}
		synchronized (p) {
			al = p.deliverTaskMap;
			if (al == null) {
				al = new HashMap<Long, DeliverTask>();
				p.deliverTaskMap = al;
			}
			if (d != null) {
				d.setDeliver(true);

				al.put(d.getTaskId(), d);
				return true;
			} else {
				DeliverTask dt = new DeliverTask();
				dt.setTaskId(task.getId());
				dt.setPlayerId(p.getId());
				dt.setDeliver(false);
				dt.setId(-1);
				dt.setVersion(-1);
				al.put(dt.getTaskId(), dt);
				return false;
			}
		}
	}
	/**
	 * 查出玩家所有的已完成 的任务
	 * @param playerId
	 * @return
	 */
	public List<DeliverTask> loadAllDeliverTask(long playerId) {
		long startTime = System.currentTimeMillis();
		List<DeliverTask> list = null;
		try{
			list = em.query(DeliverTask.class, "playerId = ? and deliverTimes > ?", new Object[]{playerId, deleteFlag},"", 1, 2000);
		}catch(Exception e) {
			logger.error("[从DB中load完成任务] [异常] [player:" + playerId + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]", e);
			e.printStackTrace();
		}
		return list;
	}

	public DeliverTask load(long playerId, long taskId) throws Exception {
		long startTime = System.currentTimeMillis();
		try {
			//List<DeliverTask> list = em.query(DeliverTask.class, "playerId=" + playerId + " and taskId = " + taskId + "", "", 1, 10);
			List<DeliverTask> list = em.query(DeliverTask.class, "playerId=? and taskId = ?", new Object[]{playerId,taskId},"", 1, 10);
			if (list.size() > 0) {
				return list.get(0);
			}
			return null;
		} catch (Exception e) {
			logger.error("[从DB中load完成任务] [异常] [player:" + playerId + "] [task:" + taskId + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]", e);
			throw e;
		}
	}

}
