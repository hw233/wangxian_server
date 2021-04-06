package com.fy.engineserver.sprite;


/**
 * 玩家任务数据管理器
 * 
 *
 */
public class PlayerTaskDataManager {
	
//	private HashMap<Long, PlayerTaskData> taskDataMap = new HashMap<Long, PlayerTaskData>(); 
//	
//	/**
//	 * 得到玩家人物数据
//	 * @param player
//	 * @return
//	 */
//	public PlayerTaskData getPlayerTaskData(Player player) {
//		return taskDataMap.get(player.getId());
//	}
//	
//	/**
//	 * 创建玩家任务数据
//	 * @param player
//	 * @param data
//	 * @throws Exception
//	 */
//	public PlayerTaskData createPlayerTaskData(Player player) {
//		PlayerTaskData data = getPlayerTaskData(player);
//		if(data == null) {
//			data = new PlayerTaskData();
//			data.setPlayerId(player.getId());
//			data.setDirty(true);
//			taskDataMap.put(player.getId(), data);
//		} 
//		return data;
//	}
//
//	/**
//	 * 得到此玩家今天交付了多少次日常任务
//	 * 
//	 * @return
//	 */
//	public int getDailyTaskDeliverCountOfToday(Player player) {
//		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
//		PlayerTaskData data = this.getPlayerTaskData(player);
//		if(data != null) {
//			Calendar cal = Calendar.getInstance();
//			cal.setTimeInMillis(now);
//			int y1 = cal.get(Calendar.YEAR);
//			int d1 = cal.get(Calendar.DAY_OF_YEAR);
//			cal.setTimeInMillis(data.getLastDailyTaskDeliverTime());
//			int y2 = cal.get(Calendar.YEAR);
//			int d2 = cal.get(Calendar.DAY_OF_YEAR);
//	
//			if (y1 == y2 && d1 == d2) {
//				return data.getDailyTaskDeliverCount();
//			} else {
//				return 0;
//			}
//		}
//		return 0;
//	}
//	
//
//	
//	/**
//	 * 得到当天完成的任务
//	 * @return
//	 */
//	public int getCurrentDayDeliverTaskNum(Player player){
//		int num=0;
//		long currentTime=com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
//		PlayerTaskData data = this.getPlayerTaskData(player);
//		if(data != null) {
//			return 0;
//		}
//		 HashMap<Integer, TaskDeliverEntity> deliverTasks = data.getDeliverTasks();
//		Iterator<Entry<Integer, TaskDeliverEntity>> iter = deliverTasks.entrySet().iterator(); 
//		while (iter.hasNext()) { 
//		    Map.Entry<Integer, TaskDeliverEntity> entry = (Map.Entry<Integer, TaskDeliverEntity>) iter.next(); 
//		    TaskDeliverEntity tde=entry.getValue();
//		    if(StatDataUpdateManager.isSameDay(tde.getLastDeliverTime(), currentTime)){
//		    	num++;
//		    }
//		}
//		return num;
//
//	}
//
//	/**
//	 * 判断玩家是否已经交付了某个任务
//	 * 
//	 * @param task
//	 * @return
//	 */
//	public boolean isTaskDelivered(Player player, Task task) {
//		PlayerTaskData data = this.getPlayerTaskData(player);
//		if(data != null) {
//			if (data.getDeliverTasks().get(task.getId()) != null) {
//				return true;
//			}
//			return false;
//		} 
//		return false;
//	}
//	
//	/**
//	 * 判断今天是否交付了此任务
//	 * @param task
//	 * @return
//	 */
//	public boolean isTaskDeliveredToday(Player player, Task task) {
//		PlayerTaskData data = this.getPlayerTaskData(player);
//		if(data != null) {
//			TaskDeliverEntity de = data.getDeliverTasks().get(task.getId());
//			if(de != null){
//				long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
//				Calendar cal = Calendar.getInstance();
//				cal.setTimeInMillis(now);
//				int y1 = cal.get(Calendar.YEAR);
//				int d1 = cal.get(Calendar.DAY_OF_YEAR);
//				cal.setTimeInMillis(de.getLastDeliverTime());
//				int y2 = cal.get(Calendar.YEAR);
//				int d2 = cal.get(Calendar.DAY_OF_YEAR);
//	
//				if (y1 == y2 && d1 == d2) {
//					return true;
//				}
//			}
//			return false;
//		} 
//		return false;
//	}
//
//	/**
//	 * 玩家接共享的护送任务
//	 * @param task
//	 * @return
//	 */
//	public int takeConvoyTaskByInvite(Player player, Task task){
//		PlayerTaskData data = this.getPlayerTaskData(player);
//		if(data != null) {
//			TaskEntity te = data.getInProcessTasks().get(task.getId());
//			if (te != null)
//				return 1;
//	
//			if (task.getTaskType() == Task.TYPE_ONCE) {
//				if (data.getDeliverTasks().get(task.getId()) != null) {
//					return 2;
//				}
//			}
//			if (task.getTaskType() == Task.TYPE_DAILY) {
//				TaskDeliverEntity td = data.getDeliverTasks().get(task.getId());
//				if (td != null) {
//					long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
//					Calendar cal = Calendar.getInstance();
//					cal.setTimeInMillis(now);
//					int y1 = cal.get(Calendar.YEAR);
//					int d1 = cal.get(Calendar.DAY_OF_YEAR);
//					cal.setTimeInMillis(td.getLastDeliverTime());
//					int y2 = cal.get(Calendar.YEAR);
//					int d2 = cal.get(Calendar.DAY_OF_YEAR);
//	
//					if (y1 == y2 && d1 == d2) {
//						return 3;
//					}
//				}
//			}
//	
//			if (data.getInProcessTasks().size() >= TaskManager.MAX_TASKNUM_IN_TASK_LIST) {
//				return 4;
//			}
//			TaskManager tm = TaskManager.getInstance();
//	
//			if (tm.isTakeOnAvaiable(player, task) != 0) {
//				return 5;
//			}
//			
//			te = new TaskEntity();
//			te.setOwner(player);
//			te.setTask(task);
//			te.setStatus(TaskEntity.STATUS_TAKE);
//			short[] goalProcessAmount = new short[task.getGoals().length];
//			te.setGoalProcessAmount(goalProcessAmount);
//			data.getInProcessTasks().put(task.getId(), te);
//			return 0;
//		} else {
//
//			TaskManager tm = TaskManager.getInstance();
//			
//			if (tm.isTakeOnAvaiable(player, task) != 0) {
//				return 5;
//			}
//			
//			TaskEntity te = new TaskEntity();
//			te.setOwner(player);
//			te.setTask(task);
//			te.setStatus(TaskEntity.STATUS_TAKE);
//			short[] goalProcessAmount = new short[task.getGoals().length];
//			te.setGoalProcessAmount(goalProcessAmount);
//			data = this.createPlayerTaskData(player);
//			data.getInProcessTasks().put(task.getId(), te);
//			return 0;
//		}
//	}
//	
//	/**
//	 * 获得一个最近提交的同组任务
//	 * @param task
//	 * @return
//	 */
//	public TaskDeliverEntity getLastDeliverSameGroupTask(Player player, Task task) {
//		PlayerTaskData data = this.getPlayerTaskData(player);
//		if(data != null) {
//			TaskDeliverEntity ts[] = data.getDeliverTasks().values().toArray(new TaskDeliverEntity[0]);
//			List<TaskDeliverEntity> tlist = new ArrayList<TaskDeliverEntity>();
//			TaskManager tm = TaskManager.getInstance();
//			for(TaskDeliverEntity td : ts)  {
//				Task tt = tm.getTaskById(td.getTaskId());
//				if(tt != null && tt.getId() != task.getId() && tt.getTaskGroup().length() > 0 && tt.getTaskGroup().equals(task.getTaskGroup())) {
//					tlist.add(td);
//				}
//			}
//			if(tlist.size() == 1) {
//				return tlist.get(0);
//			} else if(tlist.size() > 1) {
//				ts = tlist.toArray(new TaskDeliverEntity[0]);
//				Arrays.sort(ts, new Comparator<TaskDeliverEntity>(){
//	
//					public int compare(TaskDeliverEntity o1, TaskDeliverEntity o2) {
//						// TODO Auto-generated method stub
//						if(o1.getLastDeliverTime() < o2.getLastDeliverTime()) {
//							return -1;
//						} else if(o1.getLastDeliverTime() > o2.getLastDeliverTime()) {
//							return 1;
//						} 
//						return 0;
//					}
//					
//				});
//				return ts[0];
//			} 
//			return null;
//		}
//		return null;
//	}
//	
//	/**
//	 * 玩家接某个任务，此方法依次判断如下的条件检查： 
//	 * 1. 如果此任务已经接了，返回 1 
//	 * 2. 如果此任务为一次性任务，并且已经交付了，返回 2 
//	 * 3. 如果此任务为日常任务，并且今天已经交付了，返回 3 
//	 * 4.如果任务列表满了，返回4 
//	 * 5. 如果不满足任务本身的要求，返回 5 
//	 * 6. 您的背包已满，请先清空一下背包；
//	 * 7.当前时间任务不可接
//	 * 8.玩家要接的任务当前环没有此任务
//	 * 
//	 * 否则 返回 0
//	 * 
//	 * @param task
//	 */
//	public int takeOnTask(Player player, Task task, boolean test) {
//		return 0;
//	}
//
//	/**
//	 * 交付某个任务，此方法依次将进行如下的检查： 1. 接任务的列表中包含此任务，如果不包含，返回 1 2. 任务的各个目标是否已经完成，如果没有完成，返回 2 3.
//	 * 如果任务是日常任务，并且玩家当天交付的日常任务已经达到上限，返回3 4. 如果有物品奖励，判断玩家是否有地方放置物品，如果没有，返回4
//	 * 
//	 * 否则，返回0，标识交付任务成功
//	 * 
//	 * @param task
//	 */
//	public int deliverOneTask(Player player, Task task, byte articleSelected) {
//		return 0;
//	}
//
//	//用于记录一个玩家放弃任务的次数
//	//目地是为了防止策划配置错误，导致任务提供的物品对用户有价值
//	//用户通过频繁放弃任务来获取更多的任务
//	private HashMap<Integer,Integer> giveupTaskCountMap = new HashMap<Integer,Integer>();
//	
//	public boolean giveUpOneTask(Task task) {
//		return false;
//	}
//
//	public TaskEntity getTaskEntity(Player player, int taskId) {
//		PlayerTaskData data = this.getPlayerTaskData(player);
//		if(data != null) {
//			TaskEntity te = data.getInProcessTasks().get(taskId);
//			return te;
//		}
//		return null;
//	}
//
//	/**
//	 * 得到所有已接未完成的任务
//	 * @return
//	 */
//	public TaskEntity[] getInProcessTaskEnities(Player player) {
//		PlayerTaskData data = this.getPlayerTaskData(player);
//		if(data != null) {
//			return data.getInProcessTasks().values().toArray(new TaskEntity[0]);
//		}
//		return new TaskEntity[0];
//	}
}
