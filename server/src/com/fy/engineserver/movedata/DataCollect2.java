package com.fy.engineserver.movedata;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

import com.fy.engineserver.uniteserver.UnitedServerManager;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;

public class DataCollect2<T> implements Runnable{
	
	Queue<T> out;
	
	Class<T> clazz;
	
	Field idField;
	
	public long totalCount;

	public long usedTime;

	public long collectedNum = 0;

	public boolean running = true;
	
	public DataCollect2 (Queue<T> out, Class<T> clazz) {
		this.out = out;
		this.clazz = clazz;
		idField = DataMoveManager.getFieldByAnnotation(clazz, SimpleId.class);
		if (idField == null) {
			throw new IllegalStateException("[未找到ID字段] [" + clazz.toString() + "]");
		}
	}
	public boolean isRunning () {
		return running;
	}
	
	public void start(){
		new Thread(this,clazz.getSimpleName()).start();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		SimpleEntityManager<T> entityManager = SimpleEntityManagerFactory.getSimpleEntityManager(clazz);
		entityManager.setReadOnly(true);
		if (idField == null) {
			throw new IllegalStateException(clazz.toString() + "[没有ID字段]");
		}
		long now = System.currentTimeMillis();
		String log = "[收集数据] [" + clazz.toString() + "]";
		try {
			totalCount = entityManager.count();
			List<Long> idList = new ArrayList<Long>();
			if (totalCount > 0) {
				long leftNum = totalCount + 1;// 剩余条数
				int page = 0;// 分页查询的页码
				int pageSize = DataMoveManager.ONCE_QUERY * 6;
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
					DataMoveManager.logger.warn(log + " [第" + page + "页] [共:" + ((totalCount / pageSize) + (totalCount % pageSize == 0 ? 0 : 1)) + "页] [每页" + pageSize + "个] [从" + start + "到" + end + "] [结果" + tempId.length + "个] [总数:" + totalCount + "] [cost:" + (System.currentTimeMillis() - loopStart) + "ms] [总耗时:" +(System.currentTimeMillis() - now)+ "]");
					leftNum -= pageSize;
					page++;
				}
			}
			Long[] allIds = idList.toArray(new Long[0]);
			idList = null;
			log += " [totalCount:" + totalCount + "]";
			DataMoveManager.logger.warn(log + " [查询数据真实开始]");
			int pageSize = DataMoveManager.ONCE_QUERY;
			for (int i = 0; i < allIds.length; i += pageSize) {
				while (out.size() >= DataMoveManager.ONCE_QUERY) {
					DataMoveManager.logger.warn(log + " [存量太多,暂停3秒] [存量:" + out.size() + "] [总量:" + totalCount + "条] [总耗时:" +(System.currentTimeMillis() - now) + "]");
					Thread.sleep(3000L);
					continue;
				}
				long startId = allIds[i];
				long endId = (i + pageSize) >= allIds.length ? allIds[allIds.length - 1] : allIds[i + pageSize];
				int tempNum = (int) (endId - startId);
				if (tempNum > 10000) {
					tempNum = 9999;
				} else if (tempNum <= 6000){
					tempNum = 6000;
				}
				long loopStartTime = System.currentTimeMillis();
				DataMoveManager.logger.warn(log + " [查询从" + startId + "到" + endId + "] [当前存量:" + out.size() + "]");
				List<T> list = entityManager.query(clazz, idField.getName() + " >= ? and " + idField.getName() + " <= ?", new Object[] { startId, endId }, "", 1, tempNum + 1);

				collectedNum += list.size();
				DataMoveManager.logger.warn(log + " [查询从" + startId + "到" + endId + "] [数量:" + list.size() + "] [耗时:" + (System.currentTimeMillis() - loopStartTime) + "] [总耗时:" +(System.currentTimeMillis() - now) + "]");
				synchronized (out) {
					out.addAll(list);
				}
				list = null;
			}
		} catch (Exception e) {
			DataMoveManager.logger.warn("[异常]", e);
			DataMoveManager.errorLog.append("[服务器:"+GameConstants.getInstance().getServerName()+"] [合服异常] [" + clazz.toString() + "] [" + e.toString() + "]");
		}
		DataMoveManager.logger.warn(log + " [结束] [耗时:" + (System.currentTimeMillis() - now) + "ms]");
		long startTime = System.currentTimeMillis();
		running = false;
		usedTime = System.currentTimeMillis() - startTime;
	}

}
