package com.sqage.stat.commonstat.manager;

import java.util.ArrayList;
import java.util.List;

import com.sqage.stat.commonstat.entity.TaskAnalysis;

public interface TaskAnalysisManager {


	public List<TaskAnalysis> getBySql(String sql);
	
	public TaskAnalysis add(TaskAnalysis taskAnalysis);
	
	public boolean addList(ArrayList<TaskAnalysis> taskAnalysisList);
	
	public boolean update(TaskAnalysis taskAnalysis);
	
	public boolean updateList(ArrayList<TaskAnalysis> taskAnalysisList);

	public List<String[]> getTaskStat(String startDateStr, String endDateStr, String fenQu,String tasktype);
	
	public boolean updateAcceptCount(TaskAnalysis taskAnalysis);
	
	public boolean updateFinishCount(TaskAnalysis taskAnalysis);
	
    public  boolean  updateAcceptCountList(List<TaskAnalysis> taskAnalysisList);
	
	public boolean  updateFinishCountList(List<TaskAnalysis> taskAnalysisList);

}
