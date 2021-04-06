package com.fy.engineserver.homestead.faery.service;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.gametime.SystemTime;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;

/**
 * 一些通用的查询
 * 
 * 
 * @param <T>
 */
public class QuerySelect<T> {

	public List<T> selectAll(Class<?> clazz, String whereSql, Object[] paramValues,String orderSql, SimpleEntityManager<T> em) throws Exception {
		long startTime = SystemTime.currentTimeMillis();
		List<T> tempList = new ArrayList<T>();
		long totalNum = em.count(clazz, whereSql,paramValues);
		if (FaeryManager.logger.isInfoEnabled()) {
			FaeryManager.logger.info("[{}] [查询数据结果] [{}]", new Object[] { clazz.toString(), totalNum });
		}
		if (totalNum > 0) {
			long tempNum = totalNum + 1;
			int page = 0;// 分页查询的页码
			int pageSize = 5000;
			long start = 1;// 开始位置
			long end = 2;// 结束位置
			while (tempNum > 0) {
				start = page * pageSize + 1;
				end = start + pageSize;
				// end = end > totalNum ? totalNum : end;
				tempList.addAll(em.query(clazz, whereSql, paramValues,orderSql, start, end));
				tempNum -= pageSize;
				page++;
			}
		}
		if (FaeryManager.logger.isWarnEnabled()) {
			FaeryManager.logger.warn("[{}] [查询语句:{},order语句:{}] [查询数据结果] [{}] [返回:{}] [耗时:{}]", new Object[] { clazz.toString(), whereSql, orderSql, totalNum, tempList.size(), (SystemTime.currentTimeMillis() - startTime) });
		}
		return tempList;
	}
}
