package com.fy.engineserver.uniteserver;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import com.fy.engineserver.newtask.DeliverTask;
import com.fy.engineserver.newtask.TaskEntity;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 负责数据的合并
 * 
 * 
 * @param <T>
 */
public  class DataUnit<T> implements Runnable {
	Queue<T> input;
	UnitedServer mainServer;// mainServer
	Class<T> clazz;
	DataCollect<T> dataCollect;
	Field versionField;// 要清除version

	int receiveTimes = 0;// 接收到数据次数
	int stopTimes = 0;// 暂停次数
	long receiveTotalNum = 0L;// 接收到总记录数

	Field idField = null;

	public long savedNum = 0;

	public DataUnit(Queue<T> input, UnitedServer mainServer, Class<T> clazz, DataCollect<T> dataCollect) {
		super();
		this.input = input;
		this.mainServer = mainServer;
		this.clazz = clazz;
		this.dataCollect = dataCollect;

		versionField = UnitedServerManager.getFieldByAnnotation(clazz, SimpleVersion.class);
		versionField.setAccessible(true);
		idField = UnitedServerManager.getFieldByAnnotation(clazz, SimpleId.class);
		if (idField == null) {
			throw new IllegalStateException("[未找到ID字段] [" + clazz.toString() + "]");
		}
		idField.setAccessible(true);
	}

	public boolean running = true;

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public Queue<T> getInput() {
		return input;
	}

	public void setInput(Queue<T> input) {
		this.input = input;
	}

	public UnitedServer getMainServer() {
		return mainServer;
	}

	public void setMainServer(UnitedServer mainServer) {
		this.mainServer = mainServer;
	}

	public Class<T> getClazz() {
		return clazz;
	}

	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

	public DataCollect<T> getDataCollect() {
		return dataCollect;
	}

	public void setDataCollect(DataCollect<T> dataCollect) {
		this.dataCollect = dataCollect;
	}

	public Field getVersionField() {
		return versionField;
	}

	public void setVersionField(Field versionField) {
		this.versionField = versionField;
	}

	public int getReceiveTimes() {
		return receiveTimes;
	}

	public void setReceiveTimes(int receiveTimes) {
		this.receiveTimes = receiveTimes;
	}

	public long getReceiveTotalNum() {
		return receiveTotalNum;
	}

	public void setReceiveTotalNum(long receiveTotalNum) {
		this.receiveTotalNum = receiveTotalNum;
	}

	public Field getIdField() {
		return idField;
	}

	public void setIdField(Field idField) {
		this.idField = idField;
	}

	// List<T> list = new ArrayList<T>();// 还未保存的数据,每次取最前面的XXX个通知底层,

	@Override
	public void run() {
		String log = "[" + mainServer.getServerName() + "] [接收数据] [" + clazz.getSimpleName() + "]";
		// 通知过底层的列表.因为只通知了底层可能在没来得及存储的时候就被GC了
		// 所以创建此列表.每次循环的时候剔除version是1的对象(已经存储完毕的)保证不会因为GC丢失数据
		List<T> notifiedList = new ArrayList<T>();
		// 收集数据线程现在/即将有新数据/底层还有未存储完的数据
		while (dataCollect.running || input.size() > 0) {
			{
				// 剔除version不是0的对象
				Iterator<T> itor = notifiedList.iterator();
				while (itor.hasNext()) {
					T t = itor.next();
					try {
						int version = versionField.getInt(t);
						if (version != 0) {
							long id = idField.getLong(t);
							itor.remove();
							savedNum++;
							if (UnitedServerManager.logger.isInfoEnabled()) {
								UnitedServerManager.logger.info(log + " [移除已经存储的对象] [id:" + id + "]");
							}
						}
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			try {
				// 防止过多的调用底层的countUnSavedNewObjects。暂停100毫秒
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			long countUnSavedNewObjects = mainServer.getFactory().getSimpleEntityManager(clazz).countUnSavedNewObjects();
			// 当前未存储的数据量过大.不做任何事情,等待底层存储,
			if (countUnSavedNewObjects >= UnitedServerManager.singleClassUnsaveMax) {
				UnitedServerManager.logger.warn(log + " [当前底层未存储量太大,线程暂停2秒] [存量情况:" + countUnSavedNewObjects + "/" + UnitedServerManager.singleClassUnsaveMax + "] [存储数量:" + savedNum + "/" + dataCollect.totalCount + "] [完成比例:" + new DecimalFormat("0.0000%").format(((double) savedNum) / dataCollect.totalCount) + "]");
				try {
					Thread.sleep(2000L);
					stopTimes++;
				} catch (InterruptedException e) {
					UnitedServerManager.logger.error(log + " [线程sleep异常]", e);
				}
			} else {
				try {
					// 当前未保存的数据量
					List<T> onceNoticeSimpleJPA = new ArrayList<T>();
					synchronized (input) {
						int leftNum = UnitedServerManager.singleClassUnsaveMax;
						while (leftNum > 0 && input.size() > 0) {
							onceNoticeSimpleJPA.add(input.poll());
							leftNum--;
						}
					}

					if (onceNoticeSimpleJPA.size() == 0) {
						continue;
					}
					SimpleEntityManager<T> mm = mainServer.getFactory().getSimpleEntityManager(clazz);
					mm.releaseReference();

					for (T t : onceNoticeSimpleJPA) {
						try
						{
							if(t.getClass().isAssignableFrom(TaskEntity.class))
							{
								TaskEntity task = (TaskEntity) t;
								
								if(task.getShowType() == (byte)0 && task.getStatus() >= 3)
								{
									if (UnitedServerManager.logger.isInfoEnabled()) {
										UnitedServerManager.logger.info("[notifyNewObject] [跳过] [" + clazz.getSimpleName() + "] [id:" + idField.getLong(t) + "]");
									}
									continue;
								}
							}
							else if(t.getClass().isAssignableFrom(DeliverTask.class))
							{
								if(PlatformManager.getInstance().isPlatformOf(Platform.官方))
								{
									DeliverTask deliverTask = (DeliverTask) t;
									
									if(deliverTask.getDeliverTimes() <= -300)
									{
										if (UnitedServerManager.logger.isInfoEnabled()) {
											UnitedServerManager.logger.info("[notifyNewObject] [跳过] [" + clazz.getSimpleName() + "] [id:" + idField.getLong(t) + "]");
										}
										continue;
									}
								}
							}
						}
						catch(Exception e)
						{
							UnitedServerManager.logger.error("[notifyNewObject] [跳过] [出现未知异常] [" + clazz.getSimpleName() + "] [id:" + idField.getLong(t) + "]",e);
						}
							
						versionField.setInt(t, 0);
						mainServer.getFactory().getSimpleEntityManager(clazz).notifyNewObject(t);
						notifiedList.add(t);
						if (UnitedServerManager.logger.isInfoEnabled()) {
							UnitedServerManager.logger.info("[notifyNewObject] [" + clazz.getSimpleName() + "] [id:" + idField.getLong(t) + "]");
						}
					}
					receiveTotalNum += onceNoticeSimpleJPA.size();
					receiveTimes++;
					UnitedServerManager.logger.warn(log + " [存储数据] [" + onceNoticeSimpleJPA.size() + "个] [接收数据:" + receiveTimes + "次] [累计:" + receiveTotalNum + "条]  [总记录数:" + dataCollect.totalCount + "] [接收比例:" + new DecimalFormat("0.0000%").format(((double) receiveTotalNum) / dataCollect.totalCount) + "] [总耗时:" + UnitedServerManager.getTotolCost() + "]");
				} catch (NegativeArraySizeException e) {
					UnitedServerManager.logger.error(log + " [异常]", e);
				} catch (Exception e) {
					UnitedServerManager.logger.error(log + " [异常]", e);
				}
			}
		}
		// 处理那些其他都执行完毕，但是还有未被存储的对象，防止GC导致对象丢失
		while (notifiedList.size() > 0) {
			try {
				Thread.sleep(5000L);
				{
					// 剔除version不是0的对象
					Iterator<T> itor = notifiedList.iterator();
					while (itor.hasNext()) {
						T t = itor.next();
						try {
							int version = versionField.getInt(t);
							if (version != 0) {
								long id = idField.getLong(t);
								itor.remove();
								savedNum++;
								if (UnitedServerManager.logger.isInfoEnabled()) {
									UnitedServerManager.logger.info(log + " [只剩下通知了底层，但未存储的] [移除已经存储的对象] [id:" + id + "]");
								}
							}
						} catch (Exception e) {
							UnitedServerManager.logger.error("DataUnit出错:", e);
						}
					}
				}
			} catch (Exception e) {
				UnitedServerManager.logger.error("DataUnit出错:", e);
			}
		}
		running = false;
		UnitedServerManager.logger.warn(log + " [存储数据] [完毕] [暂停" + (++stopTimes) + "次] [接收数据:" + receiveTimes + "次] [接收总录数:" + receiveTotalNum + "条]  [总记录数:" + dataCollect.totalCount + "]");
	}
}