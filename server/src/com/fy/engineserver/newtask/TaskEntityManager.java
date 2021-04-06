package com.fy.engineserver.newtask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class TaskEntityManager {

	static Logger logger = LoggerFactory.getLogger(TaskSubSystem.class);
	public static SimpleEntityManager<TaskEntity> em;
	private static TaskEntityManager self;

	LruMapCache cacheForSave = new LruMapCache(1024 * 1024, 1800000L, false, "TaskEntityManager-Check");

	public TaskEntityManager() {

	}

	/**
	 * 得到角色的任务实体
	 * 包括做过的日常任务,当前在做的单次任务,日常,不包含完成的境界日常
	 * @param p
	 * @return
	 */
	// TASK_SHOW_TYPE_DAILY 3
	public List<TaskEntity> getPlayerTaskEntities(Player p) {
		long startTime = System.currentTimeMillis();

		// String where = "playerId = ? and (showType = ? or showType = ? or showType = ? or status <> ?)";
		// String where = "playerId = ? and (showType = 1 or showType = 2 or showType = 3 or status <> 3)";
		String where = "playerId = ? and (showType >=1 and showType <= 3  or status < 3)";
//		String where = "playerId = ?";			//需要查出所有玩家的数据，否则删不完。
		List<TaskEntity> list = new ArrayList<TaskEntity>();
		List<TaskEntity> rmlist = new ArrayList<TaskEntity>();
		try {
			// List<TaskEntity> dbList = em.query(TaskEntity.class, where, new Object[] { p.getId(), TaskConfig.TASK_SHOW_TYPE_DAILY, TaskConfig.TASK_SHOW_TYPE_SUB,
			// TaskConfig.TASK_SHOW_TYPE_BOURN, TaskConfig.TASK_STATUS_DEILVER }, "id desc", 1, 9999);
			List<TaskEntity> dbList = em.query(TaskEntity.class, where,new Object[] { p.getId()}, "", 1, 9999);

			TaskManager taskManager = TaskManager.getInstance();
			Iterator<TaskEntity> itor = dbList.iterator();
			while (itor.hasNext()) {
				TaskEntity te = itor.next();
				Task task = taskManager.getTask(te.getTaskId());
				if (task == null) {
					itor.remove();
				} else if (task.getType() != TaskConfig.TASK_TYPE_DAILY && te.getStatus() == TaskConfig.TASK_STATUS_DEILVER) {
					rmlist.add(te);
					itor.remove();
				} else {
					boolean found = false;
					for (int i = 0; i < list.size(); i++) {
						TaskEntity e = list.get(i);
						if (e.getTaskId() == te.getTaskId()) {
							found = true;
						}
					}
					if (found == false) {
						list.add(te);
					}
				}
			}

			StringBuffer sb = new StringBuffer();
			for (TaskEntity te : list) {
				sb.append("{" + te.getTaskId() + "}");
			}
			if(rmlist.size() > 0) {
				long startT = System.currentTimeMillis();
				logger.error("需要删除的数据数：" + rmlist.size() + "[" + p.getLogString() + "]");
				NewDeliverTaskManager ndtm = NewDeliverTaskManager.getInstance();
				ndtm.rmThread.add2teRemoveList(rmlist);
//				for(TaskEntity te : rmlist) {
//					try{
//						this.notifyDeleteFromCache(te);
//						logger.error("[taskentity中删除的数据][" + te + "]");
//					}catch(Exception e) {
//						logger.error("[删除数据异常：][" + p.getLogString() + "]", e);
//					}
//				}
				logger.error("删除数据耗时：" + (System.currentTimeMillis() - startT) + "[" + p.getLogString() + "]");
			}
			logger.info("[加载玩家任务实体] [成功] [" + p.getLogString() + "] [size:" + list.size() + "] [task:" + sb + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
		} catch (Exception e) {
			logger.info("[加载玩家任务实体] [失败] [" + p.getLogString() + "] [size:" + list.size() + "] [task:{}] [cost:" + (System.currentTimeMillis() - startTime) + "ms]", e);
		}
		return list;
	}

	public void cacheTaskEntityForSave(TaskEntity te) {
		if (cacheForSave.get(te.getId()) == null) {
			cacheForSave.put(te.getId(), te);
		}
	}

	public static TaskEntityManager getInstance() {
		return self;
	}

	public void notifyDeleteFromCache(TaskEntity te) {
		try {
			em.remove(te);
			if (cacheForSave.get(te.getId()) != null) {
				cacheForSave.remove(te.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[删除taskentity数据异常] [taskId:"+(te!=null?te.getTaskId():"0")+"] [taskName:"+(te!=null?te.getTaskName():"null")+"] [异常:"+e+"]");
		}
	}

	public void notifyRemoveFromCache(TaskEntity te) {
		try {
			em.save(te);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void init() {
		
		em = SimpleEntityManagerFactory.getSimpleEntityManager(TaskEntity.class);
		self = this;
		ServiceStartRecord.startLog(this);
	}

	public void destory() {
		if (em != null) {
			em.destroy();
		}
	}
}
