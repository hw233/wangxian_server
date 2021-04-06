package com.sqage.stat.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.entity.TaskAnalysis;
import com.sqage.stat.commonstat.manager.Impl.TaskAnalysisManagerImpl;
import com.xuanzhi.tools.queue.AdvancedFilePersistentQueue;

public class TaskFinishService implements Runnable {

	AdvancedFilePersistentQueue taskAnalysisFinishqueue=null;
	static Logger logger = Logger.getLogger(TaskFinishService.class);
	TaskAnalysisManagerImpl taskAnalysisManager;
	static TaskFinishService self;
	
	 SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");

	public static TaskFinishService getInstance() {
		return self;
	}

	public void init() throws Exception {
		self = this;
	}

	private void handle(ArrayList<TaskAnalysis> messageList) {
		
		    List<TaskAnalysis> taskAnalysisList= new ArrayList();
			for(TaskAnalysis taskAnalysis_t: messageList){
				
			     long acceptCount= taskAnalysis_t.getAcceptCount();
			     String career= taskAnalysis_t.getCareer();
			     Date createDate= taskAnalysis_t.getCreateDate();
			     String fenQu= taskAnalysis_t.getFenQu();
			     long finishCount= taskAnalysis_t.getFinishCount();
			     long NextacceptCount= taskAnalysis_t.getNextAcceptCount();
			     String preTaskGroup= taskAnalysis_t.getPreTaskGroup();
			     String taskGroup= taskAnalysis_t.getTaskGroup();
			     String taskName= taskAnalysis_t.getTaskName();
			     String taskType= taskAnalysis_t.getTaskType();
                 String jixing=taskAnalysis_t.getJixing();
			     
			TaskAnalysis taskAnalysis=new TaskAnalysis();
			String taskAnalysisSql=" select t.* from stat_task_analysis t where" +
					" to_char(t.createdate,'YYYY-MM-DD')='"+sf.format(createDate!=null?createDate:new Date())+"' and t.taskgroup ='"+taskGroup
					+"' and t.fenqu='"+fenQu+"'" ;
							//+" and t.jixing='"+jixing+"'";
			//logger.info("[完成任务查询sql:] "+taskAnalysisSql);
			
			List ls=taskAnalysisManager.getBySql(taskAnalysisSql);
			if(ls!=null&&ls.size()>0){
				taskAnalysis = (TaskAnalysis)ls.get(0);
				
				taskAnalysis.setFinishCount(taskAnalysis.getFinishCount()+finishCount);
				taskAnalysis.setCareer(career);
				taskAnalysis.setFenQu(fenQu);
				taskAnalysis.setPreTaskGroup(preTaskGroup);
				taskAnalysis.setTaskGroup(taskGroup);
				taskAnalysis.setTaskName(taskName);
				taskAnalysis.setJixing(jixing);
				taskAnalysisList.add(taskAnalysis);
				//taskAnalysisManager.updateFinishCount(taskAnalysis);
			}else{
				taskAnalysis.setCreateDate(createDate!=null ? createDate : new Date());
			    taskAnalysisManager.add(taskAnalysis_t);
			}
			/////////当前任务处理逻辑end/////////////////////////////////
		}	
			taskAnalysisManager.updateFinishCountList(taskAnalysisList);
			
	   }

	@Override
	public void run() {
		if(taskAnalysisFinishqueue==null){taskAnalysisFinishqueue=StatServerService.taskAnalysisFinishqueue;}
		while (Thread.currentThread().isInterrupted() == false) {
			ArrayList<TaskAnalysis> al=new ArrayList(); 
			try {
				while(!taskAnalysisFinishqueue.isEmpty()) {
					TaskAnalysis flow =(TaskAnalysis)taskAnalysisFinishqueue.pop();
						if(flow!=null){	al.add(flow);}
						if (al.size()>= 1500) {
							handle(al);
							al.clear();
					}
				    }
				if(al.size()>0){
					   handle(al);
						al.clear();
					 }
				synchronized(this){
					 wait(100);
					 }
			} catch (Exception e) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				logger.error("[reqest处理错误] " +e.getMessage() + " [队列:" + taskAnalysisFinishqueue.elementNum() + "] ",e);
			}
		}
		if(logger.isDebugEnabled()){
		logger.debug("[reqest处理完毕] [处理线程退出] [队列:" + taskAnalysisFinishqueue.elementNum() + "]");
	    }
	}

	public AdvancedFilePersistentQueue getTaskAnalysisFinishqueue() {
		return taskAnalysisFinishqueue;
	}

	public void setTaskAnalysisFinishqueue(AdvancedFilePersistentQueue taskAnalysisFinishqueue) {
		this.taskAnalysisFinishqueue = taskAnalysisFinishqueue;
	}

	public TaskAnalysisManagerImpl getTaskAnalysisManager() {
		return taskAnalysisManager;
	}

	public void setTaskAnalysisManager(TaskAnalysisManagerImpl taskAnalysisManager) {
		this.taskAnalysisManager = taskAnalysisManager;
	}
	
}