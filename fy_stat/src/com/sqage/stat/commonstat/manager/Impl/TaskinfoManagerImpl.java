package com.sqage.stat.commonstat.manager.Impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sqage.stat.commonstat.dao.Impl.TaskinfoDaoImpl;
import com.sqage.stat.commonstat.entity.Taskinfo;
import com.sqage.stat.commonstat.manager.TaskinfoManager;

public class TaskinfoManagerImpl implements TaskinfoManager {

	TaskinfoDaoImpl  taskinfoDao;
	static TaskinfoManagerImpl self;
	public void init(){
		self = this;
	}
	public static TaskinfoManagerImpl getInstance() {
		return self;
	}
	
	@Override
	public Taskinfo add(Taskinfo taskinfo) {
		return taskinfoDao.add(taskinfo);
	}

	@Override
	public boolean deleteById(Long id) {
		return taskinfoDao.deleteById(id);
	}

	@Override
	public Taskinfo getById(Long id) {
		return taskinfoDao.getById(id);
	}

	@Override
	public List<Taskinfo> getBySql(String sql) {
		return taskinfoDao.getBySql(sql);
	}

	@Override
	public boolean addList(ArrayList<Taskinfo> taskinfoList) {
		return taskinfoDao.addList(taskinfoList);
	}
	@Override
	public boolean update(Taskinfo taskinfo) {
		return taskinfoDao.update(taskinfo);
	}

	@Override
	public boolean updateList(ArrayList<Taskinfo> taskinfoList) {
		return taskinfoDao.updateList(taskinfoList);
	}
	public TaskinfoDaoImpl getTaskinfoDao() {
		return taskinfoDao;
	}
	public void setTaskinfoDao(TaskinfoDaoImpl taskinfoDao) {
		this.taskinfoDao = taskinfoDao;
	}
	@Override
	public List<String[]> getTaskShouYi(String startDateStr, String endDateStr, String fenQu, String taskName, String gamelevel) {
		return taskinfoDao.getTaskShouYi(startDateStr, endDateStr, fenQu, taskName, gamelevel);
	}
	@Override
	public List<String[]> getTaskStat(String startDateStr, String endDateStr, String fenQu, String taskName, String tasktype,String status) {
		return taskinfoDao.getTaskStat(startDateStr, endDateStr, fenQu, taskName, tasktype,status);
	}
	@Override
	public ArrayList<String> gettaskType(Date date) {
		return taskinfoDao.gettaskType(date);
	}
	@Override
	public ArrayList<String> gettasks(Date date) {
		return taskinfoDao.gettasks(null);
	}
	

}
