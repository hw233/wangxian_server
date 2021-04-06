package com.xuanzhi.tools.async;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

public abstract class AsyncStoreManager implements Runnable  {
	
	/**
	 * 只有在本系统注册的客户用户，才能获取对象的引用
	 */
	private List<ClientUser> users = new ArrayList<ClientUser>();
	
	/**
	 * 内存中的存储对象，只对此集合内的对象进行存储操作
	 */
	private List<Storable> masterList =new LinkedList<Storable>();
	
	private List<SoftReference<Storable>> cc;
	
	/**
	 * 被标示为可删除的存储对象
	 */
	private List<Storable> deletableList = new LinkedList<Storable>();
	
	/**
	 * 更新的频率,ms
	 */
	protected long updatePeriod = 20000;
	
	/**
	 * 在此系统注册
	 * @param user
	 */
	public void register(ClientUser user) {
		if(!users.contains(user)) {
			users.add(user);
		}
	}
	
	/**
	 * 批量保存接口
	 * @param list
	 */
	protected abstract void batchSave(List<Storable> list);
	
	/**
	 * 批量更新接口
	 * @param list
	 */
	protected abstract void batchUpdate(List<Storable> list);
	
	/**
	 * 批量删除接口
	 * @param list
	 */
	protected abstract void batchDelete(List<Storable> list);
	
	/**
	 * 得到日志
	 * @return
	 */
	protected abstract Logger getLogger();
	
	/**
	 * 增加一个存储对象到主集合中
	 * @param object
	 */
	protected void addMaster(Storable object) {
		masterList.add(object);
	}
	
	/**
	 * 删除某个对象
	 * @param object
	 */
	protected synchronized void delete(Storable object) {
		deletableList.add(object);
	}
	
	/**
	 * 对象是否已被删除
	 * @param object
	 * @return
	 */
	protected boolean isDeleted(Storable object) {
		if(deletableList.contains(object)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 把对象从内存中清除
	 * @param object
	 */
	protected synchronized void clearFromMem(Storable object) {
		masterList.remove(object);		
		for(int i=0, len=users.size(); i<len; i++) {
			users.get(i).notifyMasterCleared(object);
		}
	}
	
	/**
	 * 组件注销时保存
	 */
	public void finalizeStore() {
		long start = System.currentTimeMillis();
		List<Storable> saveList = new ArrayList<Storable>();
		List<Storable> updateList = new ArrayList<Storable>();
		List<Storable> deleteList = new ArrayList<Storable>();
		for(int i=0, len=masterList.size(); i<len; i++) {
			Storable object = masterList.get(i);
			if(object.isNew()) {
				saveList.add(object);
			} else if(object.isDirty()) {
				updateList.add(object);
			}
		}
		if(saveList.size() > 0) {
			batchSave(saveList);
		}
		if(updateList.size() > 0) {
			batchUpdate(updateList);
		}
		for(int i=0,len=deletableList.size(); i<len; i++) {
			deleteList.add(deletableList.remove(0));
		}
		if(deleteList.size() > 0) {
			batchDelete(deleteList);
		}
		getLogger().info("[注销组件时保存数据] [新增:"+saveList.size()+"] [更新:"+updateList.size()+"] [删除:"+deleteList.size()+"]", start);
	}
	
	public void run() {
		long start = 0;
		while(true) {
			try {
				Thread.sleep(5000);
				start = System.currentTimeMillis();
				List<Storable> saveList = new ArrayList<Storable>();
				List<Storable> updateList = new ArrayList<Storable>();
				List<Storable> deleteList = new ArrayList<Storable>();
				for(int i=0, len=masterList.size(); i<len; i++) {
					Storable object = masterList.get(i);
					if(object.isNew()) {
						saveList.add(object);
					} else if(object.isDirty() && start - object.getLastUpdateTime() > updatePeriod) {
						updateList.add(object);
					}
				}
				if(saveList.size() > 0) {
					batchSave(saveList);
				}
				if(updateList.size() > 0) {
					batchUpdate(updateList);
				}
				for(int i=0,len=deletableList.size(); i<len; i++) {
					deleteList.add(deletableList.remove(0));
				}
				if(deleteList.size() > 0) {
					batchDelete(deleteList);
				}
			} catch(Throwable e) {
				getLogger().error("[异步存储处理出错] ["+(System.currentTimeMillis()-start)+"]", e);
			}
		}
	}
}
