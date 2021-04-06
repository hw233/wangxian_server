package com.fy.engineserver.uniteserver;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;

/**
 * 负责数据的收集
 * 
 * 
 * @param <T>
 */
public class DataCollect<T> implements Runnable {
	Queue<T> out;
	Map<String, UnitedServer> unitedServerMap;
	Class<T> clazz;
	public boolean running = true;
	Field idField;
	public long totalCount;

	public long usedTime;

	public long collectedNum = 0;

	public DataCollect(Queue<T> out, Map<String, UnitedServer> unitedServerMap, Class<T> clazz) {
		super();
		this.out = out;
		this.unitedServerMap = unitedServerMap;
		this.clazz = clazz;
		idField = UnitedServerManager.getFieldByAnnotation(clazz, SimpleId.class);

		if (idField == null) {
			throw new IllegalStateException("[未找到ID字段] [" + clazz.toString() + "]");
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public Map<String, UnitedServer> getUnitedServerMap() {
		return unitedServerMap;
	}

	public void setUnitedServerMap(Map<String, UnitedServer> unitedServerMap) {
		this.unitedServerMap = unitedServerMap;
	}

	@Override
	public void run() {

		{
			// 首先统计总数量
			for (UnitedServer unitedServer : unitedServerMap.values()) {
				SimpleEntityManager<T> entityManager = unitedServer.getFactory().getSimpleEntityManager(clazz);
				entityManager.setReadOnly(true);

				if (idField == null) {
					throw new IllegalStateException(clazz.toString() + "[没有ID字段]");
				}
				try {
					long count = entityManager.count();
					totalCount += count;
					UnitedServerManager.logger.warn("[统计要合并的数据] [" + unitedServer.getServerName() + "] [" + clazz.getName() + "] [要合并数据数量:" + count + "/" + totalCount + "]");
				} catch (Exception e) {
					UnitedServerManager.logger.warn("[异常] [" + unitedServer.toString() + "]", e);
				}
			}

		}
		for (UnitedServer unitedServer : unitedServerMap.values()) {
			SimpleEntityManager<T> entityManager = unitedServer.getFactory().getSimpleEntityManager(clazz);
			entityManager.setReadOnly(true);
			String log = "[" + unitedServer.getServerName() + "] [收集数据] [" + clazz.toString() + "]";
			long now = System.currentTimeMillis();

			if (idField == null) {
				throw new IllegalStateException(clazz.toString() + "[没有ID字段]");
			}
			try {
				List<Long> idList = new ArrayList<Long>();
				long count = entityManager.count();
				// 查询所有ID
				if (count > 0) {
					long leftNum = count + 1;// 剩余条数
					int page = 0;// 分页查询的页码
					int pageSize = UnitedServerManager.ONCE_QUERY * 6;
					long start = 1;// 开始位置
					long end = 2;// 结束位置
					while (leftNum > 0) {
						long loopStart = System.currentTimeMillis();
						start = page * pageSize + 1;
						end = start + pageSize;
						long[] tempId = entityManager.queryIds(clazz, "", idField.getName(), start, end);// 这里改成prepared
						for (long id : tempId) {
							idList.add(id);
						}
						UnitedServerManager.logger.warn(log + " [第" + page + "页] [共:" + ((totalCount / pageSize) + (totalCount % pageSize == 0 ? 0 : 1)) + "页] [每页" + pageSize + "个] [从" + start + "到" + end + "] [结果" + tempId.length + "个] [总数:" + count + "] [cost:" + (System.currentTimeMillis() - loopStart) + "ms] [总耗时:" + UnitedServerManager.getTotolCost() + "]");
						leftNum -= pageSize;
						page++;
					}
				}

				// 获取对象
				Long[] allIds = idList.toArray(new Long[0]);
				idList = null;

				log += " [totalCount:" + totalCount + "]";
				UnitedServerManager.logger.warn(log + " [合并数据真实开始]");
				int pageSize = UnitedServerManager.ONCE_QUERY;
				for (int i = 0; i < allIds.length; i += pageSize) {
					while (out.size() >= UnitedServerManager.ONCE_QUERY) {
						UnitedServerManager.logger.warn(log + " [存量太多,暂停3秒] [存量:" + out.size() + "] [总量:" + totalCount + "条] [总耗时:" + UnitedServerManager.getTotolCost() + "]");
						Thread.sleep(3000L);
						continue;
					}
					long startId = allIds[i];
					long endId = (i + pageSize) >= allIds.length ? allIds[allIds.length - 1] : allIds[i + pageSize];
					long loopStartTime = System.currentTimeMillis();

					UnitedServerManager.logger.warn(log + " [查询从" + startId + "到" + endId + "] [当前存量:" + out.size() + "]");
					List<T> list = entityManager.query(clazz, idField.getName() + " >= ? and " + idField.getName() + " <= ?", new Object[] { startId, endId }, "", 1, pageSize + 1);

					collectedNum += list.size();

					UnitedServerManager.logger.warn(log + " [查询从" + startId + "到" + endId + "] [数量:" + list.size() + "] [耗时:" + (System.currentTimeMillis() - loopStartTime) + "] [总耗时:" + UnitedServerManager.getTotolCost() + "]");
					synchronized (out) {
						out.addAll(list);
					}
					list = null;
				}
			} catch (Exception e) {
				UnitedServerManager.logger.warn(log + " [异常]", e);
			}
			UnitedServerManager.logger.warn(log + " [结束] [耗时:" + (System.currentTimeMillis() - now) + "ms]");
		}
		long startTime = System.currentTimeMillis();
		running = false;
		usedTime = System.currentTimeMillis() - startTime;
	}
}
