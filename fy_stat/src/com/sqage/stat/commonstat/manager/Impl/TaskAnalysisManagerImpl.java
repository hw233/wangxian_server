package com.sqage.stat.commonstat.manager.Impl;

import java.util.ArrayList;
import java.util.List;

import com.sqage.stat.commonstat.dao.Impl.TaskAnalysisDaoImpl;
import com.sqage.stat.commonstat.entity.TaskAnalysis;
import com.sqage.stat.commonstat.manager.TaskAnalysisManager;

public class TaskAnalysisManagerImpl implements TaskAnalysisManager {
	
	TaskAnalysisDaoImpl  taskAnalysisDao;
	
	static TaskAnalysisManagerImpl self;
	public void init(){
		self = this;
	}
	public static TaskAnalysisManagerImpl getInstance() {
		return self;
	}
	@Override
	public TaskAnalysis add(TaskAnalysis taskAnalysis) {
		return taskAnalysisDao.add(taskAnalysis);
	}

	@Override
	public boolean addList(ArrayList<TaskAnalysis> taskAnalysisList) {
		return taskAnalysisDao.addList(taskAnalysisList);
	}

	@Override
	public List<TaskAnalysis> getBySql(String sql) {
		return taskAnalysisDao.getBySql(sql);
	}

	@Override
	public boolean update(TaskAnalysis taskAnalysis) {
		return taskAnalysisDao.update(taskAnalysis);
	}

	
	@Override
	public List<String[]> getTaskStat(String startDateStr, String endDateStr, String fenQu, String tasktype) {
		return taskAnalysisDao.getTaskStat(startDateStr, endDateStr, fenQu, tasktype);
	}
	
	@Override
	public boolean updateList(ArrayList<TaskAnalysis> taskAnalysisList) {
		return taskAnalysisDao.updateList(taskAnalysisList);
	}
	public TaskAnalysisDaoImpl getTaskAnalysisDao() {
		return taskAnalysisDao;
	}
	public void setTaskAnalysisDao(TaskAnalysisDaoImpl taskAnalysisDao) {
		this.taskAnalysisDao = taskAnalysisDao;
	}
	@Override
	public boolean updateAcceptCount(TaskAnalysis taskAnalysis) {
		return taskAnalysisDao.updateAcceptCount(taskAnalysis);
	}
	@Override
	public boolean updateFinishCount(TaskAnalysis taskAnalysis) {
		return taskAnalysisDao.updateFinishCount(taskAnalysis);
	}
	@Override
	public boolean updateAcceptCountList(List<TaskAnalysis> taskAnalysisList) {
		return taskAnalysisDao.updateAcceptCountList(taskAnalysisList);
	}
	@Override
	public boolean updateFinishCountList(List<TaskAnalysis> taskAnalysisList) {
		return taskAnalysisDao.updateFinishCountList(taskAnalysisList);
	}

}
