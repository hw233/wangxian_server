package com.fy.engineserver.newtask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.newtask.service.DeliverTaskManager;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class NewDeliverTaskManager {
	public static Logger logger = LoggerFactory.getLogger(DeliverTaskManager.class);
	
	static NewDeliverTaskManager self;
	private LruMapCache dtcache = new LruMapCache(1024 * 128, 1800000L, false, "NewDeliverTaskCache");
	
	public static boolean isNewDeliverTaskAct = true;
	
	public SimpleEntityManager<NewDeliverTask> em;
	
	public DeliverTaskRemoveThread rmThread = null;
	
	public static boolean removeRubbishData = true;
	
	public static int loadNewTaskNum = 8000;
	
	public static NewDeliverTaskManager getInstance() {
		return self;
	}
	
	public void init() throws Exception {
		
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		em = SimpleEntityManagerFactory.getSimpleEntityManager(NewDeliverTask.class);
		self = this;
		rmThread = new DeliverTaskRemoveThread();
		rmThread.setName("[旧的任务删除线程]");
		rmThread.start();
		ServiceStartRecord.startLog(this);
		System.out.println("[系统初始化] [新任务系统] [初始化完成] [" + getClass().getName() + "] [耗时：" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) + "毫秒]");
	}
	
	public void destroy() {
		if (em != null) {
			em.destroy();
			rmThread.notifyServerClosed();
		}
	}
	
	public void save(NewDeliverTask dt) {
		try {
//			em.save(dt);
		} catch (Exception e) {
			if (logger.isWarnEnabled()) {
				logger.warn("[在cache移除时保存NDT] [异常]  [id:" + dt.getId() + "] [task:" + dt.getTaskId() + "] [player:--][" + dt.getPlayerId() + "]", e);
			}
		}
	}
	/**
	 * 登陆服务器时去倒老玩家的已交付任务列表
	 * @param p
	 */
	public void transforOldDeliverData(Player p) {
		long startTime = System.currentTimeMillis();
		synchronized (p) {
//			if(p.newdeliverTaskMap != null) {
//				logger.warn("[玩家数据已导入新的已完成任务表][" + p.getLogString() + "]");
//				return;
//			}
			DeliverTaskManager dtm = DeliverTaskManager.getInstance();
			NewDeliverTaskManager ndtm = NewDeliverTaskManager.getInstance();
			if(dtm != null && ndtm != null) {
				List<DeliverTask> oldList = dtm.loadAllDeliverTask(p.getId());
				if(oldList == null || oldList.size() <= 0) {
					return ;
				}
				TaskManager tm = TaskManager.getInstance();
				List<DeliverTask> nuFrontG = new ArrayList<DeliverTask>();			//没有前置任务的任务组
				Set<Long> deliveredList = new HashSet<Long>();						//已经完成的任务id
				for(DeliverTask dd : oldList) {
					DeliverTaskManager.getInstance().notifyDeleteFromCache(dd);
					DeliverTaskManager.getInstance().tempMap.put(dd.getId(), dd);
					Task task = tm.getTask(dd.getTaskId());
					deliveredList.add(dd.getTaskId());
					if(task != null) {
						if(task.getFrontGroupName() == null || task.getFrontGroupName().isEmpty()) {		//挑出已完成任务重没有前置任务的任务列表
							nuFrontG.add(dd);
						}
					}
				}
				logger.error("[需要入库的数据有1][" + p.getLogString() + "][" + nuFrontG.size() + "]");
				Set<Long> wrongData = new TreeSet<Long>(new Comparator<Long>() {
					@Override
					public int compare(Long arg0, Long arg1) {
						// TODO Auto-generated method stub
						if(arg0 > arg1) {
							return 1;
						} else {
							return -1;
						}
					}
				});
				HashMap<Long, NewDeliverTask> al = p.newdeliverTaskMap;
				for(DeliverTask dt : nuFrontG) {									
					try {
						ndtm.notifyNewDeliverTask(p, new NewDeliverTask(p.getId(), dt.getTaskId()));	//首先将没有前置任务的任务存入新库
						Set<Long> dd = transAllDeliverdTask(p, dt.getTaskName(), deliveredList, al);
						if(dd != null && dd.size() > 0) {
							for(Long ddd : dd) {
								wrongData.add(ddd);
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error("[将数据导入新库出错]["+p.getLogString()+"]", e);
					}
				}
				if(wrongData != null && wrongData.size() > 0) {
					if(logger.isWarnEnabled()) {
						logger.warn("[delivertask库中有数据丢失,丢失数据也导入到新库] [" + p.getLogString() + "]");
					}
					for(Long id : wrongData) {
						try {
							ndtm.notifyNewDeliverTask(p, new NewDeliverTask(p.getId(), id));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							logger.error("[任务数据导入新库容错处理出异常]", e);
						}	
					}
				}
			} else {
				logger.error("[delivermanager等于null！！][旧的manager=" + dtm + "][新的=" + ndtm + "]");
			}
		}
		long lastTime = System.currentTimeMillis() - startTime;
		logger.error("[" + p.getLogString() + "][任务数据导入新库消耗时间：" + lastTime + "]");
	}
	
	private Set<Long> transAllDeliverdTask(Player p ,String taskName, Set<Long> deliveredList, HashMap<Long, NewDeliverTask> al) {
		TaskManager tm = TaskManager.getInstance();
		NewDeliverTaskManager ndtm = NewDeliverTaskManager.getInstance();
		Task t = TaskManager.getInstance().getTask(taskName,p.getCountry());
		if(t == null) {
			logger.error("[任务数据为空][" + p.getLogString() + "][" + taskName + "]");
			return new TreeSet<Long>();
		}
		List<Task> nextList = tm.getnextTask(t.getGroupName());
		Set<Long> rightData = new HashSet<Long>();
		if(nextList != null && nextList.size() > 0) {
			for(Task nt : nextList) {
				if(deliveredList.contains(nt.getId())) {
					try {
						if(al == null || !(al.containsKey(nt.getId()) && al.get(nt.getId()) != null && al.get(nt.getId()).isDeliver())) {
							NewDeliverTask temp = new NewDeliverTask(p.getId(), nt.getId());
							ndtm.notifyNewDeliverTask(p, temp);
							rightData.add(nt.getId());
						}
						this.transAllDeliverdTask(p, nt.getName(), deliveredList, al);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error("导入新库出错：", e);
					}
				}
			}
		}
		Set<Long> wrongData = new HashSet<Long>();
		if(deliveredList.size() < rightData.size()) {
			for(Long ll : deliveredList) {
				if(!rightData.contains(ll)) {
					wrongData.add(ll);
				}
			}
		}
		return wrongData;
	}
	
	public List<NewDeliverTask> getPreDeliverTask(long playerId, String taskName, byte showType,String reason,int countryLimit) throws Exception {
		List<NewDeliverTask> list = new ArrayList<NewDeliverTask>();
		List<Task> tlist = TaskManager.getInstance().getTaskGroupByGroupName(taskName);
		for(Task task : tlist) {
			if(task != null && task.getCountryLimit() > 0 && (task.getCountryLimit() != countryLimit)){
				continue;
			}
//			System.out.println("[reason:"+reason+"] [taskGroupName:"+taskName+"] [playerId:"+playerId+"] [showType:"+showType+"] [countryLimit:"+countryLimit+"] [tlist:"+(tlist!=null?tlist.size():"nul")+"]");
			NewDeliverTask ttt = new NewDeliverTask(playerId, task.getId(), -1, -1);
			ttt.setDeliver(true);
			list.add(ttt);
			//只去遍历有前置任务切前置任务与当前存储任务showtype相同的任务
			if(task != null && task.getFrontGroupName() != null && !task.getFrontGroupName().isEmpty() && task.getShowType() == showType) {
				NewDeliverTask ndt = new NewDeliverTask(playerId, task.getId(), -1, -1);
				ndt.setDeliver(true);
				list.add(ndt);
				List<NewDeliverTask> nl = getPreDeliverTask(playerId, task.getFrontGroupName(), task.getShowType(),"getPreDeliverTask",countryLimit);
				for(NewDeliverTask ndt2 : nl) {
					list.add(ndt2);
				}
			}
			if(task != null && task.getName()!= null && !task.getName().equals(taskName)) {									//需要加载第一个任务
				NewDeliverTask ndt = new NewDeliverTask(playerId, task.getId(), -1, -1);
				ndt.setDeliver(true);
				list.add(ndt);
			}
		}
		return list;
	}
	
	public NewDeliverTask getNewDeliverTask(long  taskId, Player p) {
		HashMap<Long, NewDeliverTask> al = p.newdeliverTaskMap;
		if(al != null && al.containsKey(taskId) && dtcache.get(al.get(taskId).getId()) != null) {
			return al.get(taskId);
		}
		NewDeliverTask entity = null;
		try {
			List<NewDeliverTask> list = em.query(NewDeliverTask.class, "playerId=? and taskId = ?", new Object[]{p.getId(),taskId},"", 1, 10);
			if(list != null && list.size() > 0) {
				entity = list.get(0);
				if(removeRubbishData) {
					if(list.size() > 1) {
						for(int i=1; i<list.size(); i++) {
							em.remove(list.get(i));
						}
					}
				}
			}
			if(entity != null){
				dtcache.put(entity.getId(), entity);
				synchronized (p) {
					HashMap<Long, NewDeliverTask> al2 = p.newdeliverTaskMap;
					if (al2 == null) {
						al2 = new HashMap<Long, NewDeliverTask>();
						p.newdeliverTaskMap = al2;
					}
					entity.setDeliver(true);
					al2.put(entity.getTaskId(), entity);				//数据库中查找出来的数据存入缓存
					Task nT = TaskManager.getInstance().getTask(entity.getTaskId());
					if(nT != null && nT.getFrontGroupName() != null && !nT.getFrontGroupName().isEmpty()) {
						List<NewDeliverTask> tList = getPreDeliverTask(p.getId(), nT.getFrontGroupName(), nT.getShowType(),"getNewDeliverTask",p.getCountry());
						for(NewDeliverTask nn : tList) {
							if(al.containsKey(nn.getTaskId()) && al.get(nn.getTaskId()).isDeliver()) {
								continue;
							}
							al.put(nn.getTaskId(), nn);
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("[获取新完成任务列表异常][playerId=" + p.getLogString() + "]",e);
		}
		return entity;
	}
	/**
	 * 判断是否有前置任务，有前置任务则去已完成数据中查找并将id更新为此序列任务的最新完成任务
	 * @param p
	 * @param dt
	 */
	public void notifyNewDeliverTask(Player p, NewDeliverTask dt) {
		NewDeliverTask dtn = dt;
		Task tk = TaskManager.getInstance().getTask(dtn.getTaskId());
		if(tk == null) {
			logger.error("[得到任务配置为空][taskId=" + dtn.getTaskId() + "][" + p.getLogString() + "]");
			return;
		}
		boolean flag = false;
		boolean nosave = false;
		try {
			NewDeliverTask ntemp = this.getNewDeliverTask(dtn.getTaskId(), p);
			if(ntemp != null && ntemp.isDeliver()) {
				logger.error("[完成已完成的任务！][" + p.getLogString() + "][" + dtn.getTaskId() + "]");
				List<NewDeliverTask> list = em.query(NewDeliverTask.class, "playerId=? and taskId = ?", new Object[]{p.getId(),dtn.getTaskId()},"", 1, 10);
				if(list != null && list.size() > 0) {
					nosave = true;
				}
			}
			if(!nosave) {
				if(tk.getFrontGroupName() == null || tk.getFrontGroupName().isEmpty()) {		//没有前置任务则插入
					long totalNum111 = em.count(NewDeliverTask.class, "playerId=" + dtn.getPlayerId() + " and taskId=" + dtn.getTaskId());
					if(totalNum111 <= 0) {
						em.notifyNewObject(dtn);
						dtcache.put(dtn.getId(), dtn);				//用newdelivertask的主键做主键
						logger.warn("[更新数据入库][" + dtn + "]");
					}
				} else {													//有前置任务则做更新操作
					List<Task> list = TaskManager.getInstance().getTaskGroupByGroupName(tk.getFrontGroupName());
	//				Task forntTk = TaskManager.getInstance().getTask();
					NewDeliverTask oldData = null;
					for(Task forntTk : list) {
						if(forntTk != null) {
							oldData = getNewDeliverTask(forntTk.getId(), p);
							if(oldData != null && oldData.isDeliver()){
								if(forntTk.getShowType() != tk.getShowType()) {	//有前置任务，判断showtype是否相同，不相同则执行插入操作(主线支线需要区分开,境界与主线相同，单独一条)
									em.notifyNewObject(dtn);
									dtcache.put(dtn.getId(), dtn);				//用newdelivertask的主键做主键
									flag = true;
									logger.error("[更新数据入库][" + dtn + "]");
								} else {														//有前置任务，showtype相同执行更新
									NewDeliverTask cacheData = (NewDeliverTask) dtcache.get(oldData.getId());
									if(cacheData == null) {
										List<NewDeliverTask> list1 = em.query(NewDeliverTask.class, "playerId=? and taskId = ?", new Object[]{p.getId(),oldData.getTaskId()},"", 1, 10);
										if(list != null && list.size() > 0) {
											cacheData = list1.get(0);
										}
										if(cacheData != null) {
											dtcache.put(cacheData.getId(), cacheData);
										} else {
											logger.error("[数据库中也没有查出来人物信息][" + p.getLogString() + "][" + oldData.getTaskId() + "]");
											continue;
										}
									}
									if (cacheData.getTaskId() != dtn.getTaskId()) {
										cacheData.updateTaskId(dtn.getTaskId());
									}
									dtn = cacheData;
									dtcache.put(cacheData.getId(), cacheData);
									flag = true;
									break;
								}
							} 
						} else {
							logger.error("[得到任务配置为空][前置taskName=" + tk.getFrontGroupName() + "][" + p.getLogString() + "]");
						}
					}
					if(!flag) {
						long totalNum111 = em.count(NewDeliverTask.class, "playerId=" + dtn.getPlayerId() + " and taskId=" + dtn.getTaskId());
						if(totalNum111 <= 0) {
							em.notifyNewObject(dtn);
							dtcache.put(dtn.getId(), dtn);				//用newdelivertask的主键做主键
							logger.warn("[没有取到前置任务更新][" + p.getLogString() + "][" + dtn + "]");
						}
					}
				}
			}

		} catch (Exception e) {
			if (logger.isWarnEnabled()) {
				logger.error("[通知新的完成实体new] [异常]  [id:" + dtn.getId() + "] [task:" + dtn.getTaskId() + "] [player:" + p.getLogString() + "]", e);
			}
		}
		synchronized (p) {
			HashMap<Long, NewDeliverTask> al = p.newdeliverTaskMap;
			if (al == null) {
				al = new HashMap<Long, NewDeliverTask>();
				p.newdeliverTaskMap = al;
			}
			dtn.setDeliver(true);
			al.put(dtn.getTaskId(), dtn);				//新交付的任务维护到列表中
			if(!flag) {
				List<NewDeliverTask> tl = loadAllPreTask(p, dtn);
				if(tl != null) {
					for(NewDeliverTask dd : tl) {
						dd.setDeliver(true);
						al.put(dd.getTaskId(), dd);	
					}
				}
			}
		}
	}
	
	private List<NewDeliverTask> loadAllPreTask(Player p, NewDeliverTask ndt) {
		ndt.setDeliver(true);
		Task temp = TaskManager.getInstance().getTask(ndt.getTaskId());
		try{
			if(temp.getFrontGroupName() != null && !temp.getFrontGroupName().isEmpty()) {
				//遍历将此玩家完成的前置任务加载进来
				List<NewDeliverTask> tList = getPreDeliverTask(p.getId(), temp.getFrontGroupName(), temp.getShowType(),"loadAllPreTask",p.getCountry());
				return tList;
			} else {
			}
		}catch(Exception e) {
		}
		return null;
	}
	
	public void notifyRemoveFromServerMemory(Player p) {
		long startTime = System.currentTimeMillis();
		synchronized (p) {
			HashMap<Long, NewDeliverTask> al = p.newdeliverTaskMap;
			p.newdeliverTaskMap = null;
			if (al == null) {
				return;
			}
			Iterator<Long> it = al.keySet().iterator();
			while (it.hasNext()) {
				Long id = it.next();
				NewDeliverTask dt = al.get(id);
				if (dt.getVersion() == 0) {
					try {
						em.flush(dt);
					} catch (Exception e) {
						logger.error("[角色从内存中清空移除任务11] [保存任务失败,数据丢失] [id:" + dt.getId() + "] [task:" + dt.getTaskId() + "] [player:" + p.getLogString() + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
					}
				}
			}

		}
		if (logger.isInfoEnabled()) {
			logger.info("[角色从内存中清空移除任务11] [成功] [ret:-] [player:" + p.getLogString() + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
		}
	}
	/**
	 * 进入地图加载地图中的所有任务
	 * @param game
	 * @param p
	 */
	public void notifyEnterGame(Game game, Player p) {
		long startTime = System.currentTimeMillis();
		try {
			Long allTaskId[] = game.getAllTasks();
			int loadNum = loadGameTask4Player(p, allTaskId);
//			int loadNum = loadNewDeliverTask4Player(p, allTaskId);
			if (logger.isInfoEnabled()) {
				logger.info("[进入地图加载完成任务实体] [成功] [ret:" + loadNum + "] [player:" + p.getLogString() + "] [game:" + game.getGameInfo().getName() + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
			}

		} catch (Exception e) {
			logger.error("[进入地图加载完成任务实体] [异常] [ret:-] [player:" + p.getLogString() + "] [game:" + game.getGameInfo().getName() + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]", e);
		}
	}
	/**
	 * 批量加载任务实体
	 * @param player
	 * @param tasks
	 */
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
			int loadNum = loadGameTask4Player(player, allTaskId);
//			int loadNum = loadNewDeliverTask4Player(player, allTaskId);
			if (logger.isInfoEnabled()) {
				logger.info("[批量加载完成任务实体] [成功] [ret:" + loadNum + "] [player:" + player.getLogString() + "] [game:-] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
			}
		} catch (Exception e) {
			logger.error("[进入地图加载完成任务实体] [异常] [ret:-] [player:" + player.getLogString() + "] [game:-] [cost:" + (System.currentTimeMillis() - startTime) + "ms]", e);
		}
	}
	
	private int loadGameTask4Player(Player p, Long[] alltasks) throws Exception {
		Long taskIds[] = alltasks;
		int num = 0;
		synchronized(p){
			HashMap<Long, NewDeliverTask> al = p.newdeliverTaskMap;
			if(al != null) {
				for (int i = 0; i < taskIds.length; i++) {
					if (!al.containsKey(taskIds[i])) {
						NewDeliverTask dt1 = new NewDeliverTask(p.getId(), taskIds[i], -1, -1);
						dt1.setDeliver(false);
						al.put(dt1.getTaskId(), dt1);
						num++;
					}
				}
			}
		}
		return num;
	}
	
	/**
	 * 加载任务实体
	 * @param p
	 * @param alltasks
	 * @return
	 * @throws Exception
	 */
	public HashMap<Long, NewDeliverTask> loadNewDeliverTask4Player(Player p) throws Exception {
		if(p.hasLoadAllDeliverTask) {
			return null;
		}
		long startTime = System.currentTimeMillis();
//		Long taskIds[] = alltasks;
		List<NewDeliverTask> list = em.query(NewDeliverTask.class, "playerId=?", new Object[]{p.getId()},"", 1, loadNewTaskNum);
		HashMap<Long, NewDeliverTask> al = put2PlayerMap(p, list);
		p.hasLoadAllDeliverTask = true;
		long lastTime = System.currentTimeMillis() - startTime;
		if(lastTime > 1000) {
			logger.error("[" + p.getLogString() + "][新库中拿出玩家所有数据消耗时间：" + lastTime + "]");
		}
		return al;
	}
	
	Comparator <NewDeliverTask> desc = new Comparator<NewDeliverTask>(){
		public int compare(NewDeliverTask o1, NewDeliverTask o2) {
			return (int)-(o1.getTaskId() - o2.getTaskId());
		}
	};
	
	public HashMap<Long, NewDeliverTask> put2PlayerMap(Player p, List<NewDeliverTask> list) {
		HashMap<Long, NewDeliverTask> al = null;
		Set<Long> tempSet = new HashSet<Long>();
		try {
			Collections.sort(list,desc);
		} catch(Exception e) {
			logger.error("[任务] [排序出错]", e);
		}
		synchronized (p) {
			al = p.newdeliverTaskMap;
			if(al == null) {
				al = new HashMap<Long, NewDeliverTask>();
			}
			p.newdeliverTaskMap = al;
			for(NewDeliverTask ndt : list) {
				if(tempSet.contains(ndt.getTaskId())) {
					continue;
				}
				if(ndt.getId() < 0) {
					continue;
				}
				ndt.setDeliver(true);
				dtcache.put(ndt.getId(), ndt);
				al.put(ndt.getTaskId(), ndt);
				Task temp = TaskManager.getInstance().getTask(ndt.getTaskId());
				try{
					if(temp.getFrontGroupName() != null && !temp.getFrontGroupName().isEmpty()) {
						//遍历将此玩家完成的前置任务加载进来
						List<NewDeliverTask> tList = getPreDeliverTask(p.getId(), temp.getFrontGroupName(), temp.getShowType(),"put2PlayerMap",p.getCountry());
						for(NewDeliverTask nn : tList) {
							tempSet.add(ndt.getTaskId());
							if(al.containsKey(nn.getTaskId()) && al.get(nn.getTaskId()).isDeliver()) {
								continue;
							}
							al.put(nn.getTaskId(), nn);
						}
					} else {
						logger.debug("[任务没有被加载进来,没有前置任务][" + ndt + "]");
					}
				}catch(Exception e) {
					logger.error("加载任务出错：" + ndt.getTaskId() + e);
				}
			}
		}
		return al;
	}
	
	/**
	 * 任务是否已经交付
	 * @param p
	 * @param task
	 * @return
	 * @throws Exception 
	 */
	public boolean isDelived(Player p, Task task) throws Exception {
		long startTime = System.currentTimeMillis();
		HashMap<Long, NewDeliverTask> al = p.newdeliverTaskMap;
		if(al != null) {
			NewDeliverTask ndt = al.get(task.getId());
			if(ndt != null) {
				return ndt.isDeliver();
			}
		}
		synchronized (p) {
			al = p.newdeliverTaskMap;
			if(al == null) {			//没有初始化都返回已完成。
				al = loadNewDeliverTask4Player(p);
//				logger.error("[重新加载的al等于][" + al + "]");
				if(al != null) {
					if(al.containsKey(task.getId())) {
//						logger.error("[数据对了]");
						return al.get(task.getId()).isDeliver();
					} else {
						DeliverTaskManager dtm = DeliverTaskManager.getInstance();
						DeliverTask d = null;
						if(dtm != null) {									//会再倒库之前调用此方法，因此需要对老库执行一次查询操作
							try {
								d = dtm.load(p.getId(), task.getId());				
							} catch (Exception e) {
								logger.error("[从DB中load完成任务] [异常] [player:" + p.getId() + "] [task:" + task.getId() + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]", e);
								return true;
							}
						}
						if(d == null) {
							NewDeliverTask ndt1 = new NewDeliverTask(p.getId(), task.getId(), -1, -1);
							ndt1.setDeliver(false);
							al.put(task.getId(), ndt1);
							logger.error("[判断任务是否完成111]["+ndt1+"]["+ndt1.isDeliver()+"]");
							return false;
						} else {									//如果在老库中查出来有此数据则返回ture，不需要存储，之后会存储
							return true;
						}
					}
				} else{
					p.hasLoadAllDeliverTask = false;
					logger.error("[没加载好] [" + p.getLogString() + "]");
					return true;
				}
			}
			return false;
		}
	}

}
