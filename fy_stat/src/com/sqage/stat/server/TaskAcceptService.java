package com.sqage.stat.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.entity.TaskAnalysis;
import com.sqage.stat.commonstat.manager.Impl.TaskAnalysisManagerImpl;
import com.xuanzhi.tools.queue.AdvancedFilePersistentQueue;

public class TaskAcceptService implements Runnable {

	AdvancedFilePersistentQueue taskAnalysisAcceptqueue=null;
	static Logger logger = Logger.getLogger(TaskAcceptService.class);
	TaskAnalysisManagerImpl taskAnalysisManager;
	static TaskAcceptService self;
	SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");

	public static TaskAcceptService getInstance() {
		return self;
	}

	public void init() throws Exception {
		self = this;
	}

	private void handle(ArrayList<TaskAnalysis> messageList) {
		     
		     List<TaskAnalysis>  taskAnalysisList=new ArrayList();
			for(TaskAnalysis taskAnalysis_t:messageList){
			     long acceptCount_t = taskAnalysis_t.getAcceptCount();
			     String career_t    = taskAnalysis_t.getCareer();
			     Date createDate_t  = taskAnalysis_t.getCreateDate();
			     String fenQu_t     = taskAnalysis_t.getFenQu();
			     long finishCount_t = taskAnalysis_t.getFinishCount();
			     long NextacceptCount_t= taskAnalysis_t.getNextAcceptCount();
			     String preTaskGroup_t = taskAnalysis_t.getPreTaskGroup();
			     String taskGroup_t= taskAnalysis_t.getTaskGroup();
			     String taskName_t = taskAnalysis_t.getTaskName();
			     String taskType_t = taskAnalysis_t.getTaskType();
			     String jixing_t=taskAnalysis_t.getJixing();
			      
			     
            /////任务处理逻辑start/////////////////////////////////
			  TaskAnalysis taskAnalysis=new TaskAnalysis();
			String taskAnalysisSql=" select t.* from stat_task_analysis t where" +
					" to_char(t.createdate,'YYYY-MM-DD')='"+sf.format(createDate_t!=null ? createDate_t : new Date())+"' and t.taskgroup ='"+taskGroup_t
					+"' and t.fenqu='"+fenQu_t+"'";
					//and t.jixing='"+jixing_t+"'";
			
			//logger.info("[接受任务查询sql:] "+taskAnalysisSql);
			List ls=taskAnalysisManager.getBySql(taskAnalysisSql);
			if(ls!=null&&ls.size()>0){
				taskAnalysis = (TaskAnalysis)ls.get(0);
				
				 long acceptCount= taskAnalysis.getAcceptCount();
			     String career= taskAnalysis.getCareer();
			     Date createDate= taskAnalysis.getCreateDate();
			     String fenQu= taskAnalysis.getFenQu();
			     long finishCount= taskAnalysis.getFinishCount();
			     long NextacceptCount= taskAnalysis.getNextAcceptCount();
			     String preTaskGroup= taskAnalysis.getPreTaskGroup();
			     String taskGroup= taskAnalysis.getTaskGroup();
			     String taskName= taskAnalysis.getTaskName();
			     String taskType= taskAnalysis.getTaskType();
				
			     String career_="";
			     if(career!=null&&!"".equals(career)){career_=career;}else if(career_t!=null&&!"".equals(career_t)){career_=career_t;}
			     String preTaskGroup_="";
			     if(preTaskGroup!=null&&!"".equals(preTaskGroup)){preTaskGroup_=preTaskGroup;}else if(preTaskGroup_t!=null&&!"".equals(preTaskGroup_t)){preTaskGroup_=preTaskGroup_t;}
				 String taskName_="";
				 if(taskName!=null&&!"".equals(taskName)){taskName_=taskName;}else if(taskName_t!=null&&!"".equals(taskName_t)){taskName_=taskName_t;}
				 String taskType_=""; 
				 if(taskType!=null&&!"".equals(taskType)){taskType_=taskType;}else if(taskType_t!=null&&!"".equals(taskType_t)){taskType_=taskType_t;}
				 
				taskAnalysis.setAcceptCount(acceptCount_t+acceptCount);
				
				taskAnalysis.setCareer(career_);
				taskAnalysis.setFenQu(fenQu);
				taskAnalysis.setPreTaskGroup(preTaskGroup_);
				taskAnalysis.setTaskGroup(taskGroup);
				taskAnalysis.setTaskName(taskName_);
				taskAnalysis.setTaskType(taskType_);
				taskAnalysis.setNextAcceptCount( NextacceptCount_t + NextacceptCount);
				
				//taskAnalysisManager.updateAcceptCount(taskAnalysis);
				taskAnalysisList.add(taskAnalysis);
			}else{
				taskAnalysis.setCreateDate(createDate_t!=null ? createDate_t : new Date());
			    taskAnalysisManager.add(taskAnalysis_t);
			}
			/////////当前任务处理逻辑end/////////////////////////////////
		} 
			taskAnalysisManager.updateAcceptCountList(taskAnalysisList);
    	}
	@Override
	public void run() {
		if(taskAnalysisAcceptqueue==null){taskAnalysisAcceptqueue=StatServerService.taskAnalysisAcceptqueue;}
		while (Thread.currentThread().isInterrupted() == false) {
			ArrayList<TaskAnalysis> al=new ArrayList(); 
			try {
				while(!taskAnalysisAcceptqueue.isEmpty()) {
					TaskAnalysis flow =(TaskAnalysis)taskAnalysisAcceptqueue.pop();
						if(flow!=null){al.add(flow);}
						if (al.size()> 1500) {
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
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				logger.error("[reqest处理错误] [队列:" + taskAnalysisAcceptqueue.elementNum() + "] ",e);
			}
		}
		if(logger.isDebugEnabled()){
		logger.debug("[reqest处理完毕] [处理线程退出] [队列:" + taskAnalysisAcceptqueue.elementNum() + "]");
	      }
		}

	public AdvancedFilePersistentQueue getTaskAnalysisAcceptqueue() {
		return taskAnalysisAcceptqueue;
	}

	public void setTaskAnalysisAcceptqueue(AdvancedFilePersistentQueue taskAnalysisAcceptqueue) {
		this.taskAnalysisAcceptqueue = taskAnalysisAcceptqueue;
	}

	
	public TaskAnalysisManagerImpl getTaskAnalysisManager() {
		return taskAnalysisManager;
	}

	public void setTaskAnalysisManager(TaskAnalysisManagerImpl taskAnalysisManager) {
		this.taskAnalysisManager = taskAnalysisManager;
	}

	
}