package com.sqage.stat.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.entity.TaskAnalysis;
import com.xuanzhi.tools.queue.AdvancedFilePersistentQueue;

public class TaskAnalysisService_merge implements Runnable {

	AdvancedFilePersistentQueue taskAnalysisAcceptqueue=null;
	static Logger logger = Logger.getLogger(TaskAnalysisService_merge.class);
	static TaskAnalysisService_merge self;
	SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");

	public static TaskAnalysisService_merge getInstance() {
		return self;
	}

	public void init() throws Exception {
		self = this;
	}

	private void handle(ArrayList<TaskAnalysis> messageList) {
		
		HashMap map=new HashMap();
		for (int i =0; i< messageList.size();i++) {
			TaskAnalysis flow=messageList.get(i);
			
			Date creatDate=flow.getCreateDate();
			String fenQu=flow.getFenQu();
			long acceptCount=flow.getAcceptCount();
			long finishCount=flow.getFinishCount();
			long nextAcceptCount=flow.getNextAcceptCount();
			String taskType=flow.getTaskType();
			String career=flow.getCareer();
			String taskGroup=flow.getTaskGroup();
			String preTaskGroup=flow.getPreTaskGroup();
			String date=sf.format(creatDate!=null?creatDate: new Date());
			String taskName=flow.getTaskName();
			String jixing=flow.getJixing();
			
			//String key = taskGroup+fenQu+date+jixing;
			
			String key = taskGroup+fenQu+date;
			
			//当前任务处理
			if(map.get(key)!=null){
				  TaskAnalysis pretaskAnalysisTemp=(TaskAnalysis)map.get(key);
				  
					Date creatDate_t=pretaskAnalysisTemp.getCreateDate();
					String fenQu_t=pretaskAnalysisTemp.getFenQu();
					long acceptCount_t=pretaskAnalysisTemp.getAcceptCount();
					long finishCount_t=pretaskAnalysisTemp.getFinishCount();
					long netAcceptCount_t=pretaskAnalysisTemp.getNextAcceptCount();
					String taskType_t=pretaskAnalysisTemp.getTaskType();
					String career_t=pretaskAnalysisTemp.getCareer();
					String taskGroup_t=pretaskAnalysisTemp.getTaskGroup();
					String preTaskGroup_t=pretaskAnalysisTemp.getPreTaskGroup();
					String taskName_t=pretaskAnalysisTemp.getTaskName();
					
					Date dt=null;
					if( creatDate!=null ){
						   dt=creatDate;
					     }else if( creatDate_t!=null ){
					    	 dt=creatDate_t;
					     }else{ dt=new Date(); }
					
					String preTaskGrouptemp=null;
					if(preTaskGroup!=null){
						preTaskGrouptemp=preTaskGroup;
					     }else if(preTaskGroup_t!=null){
					    	 preTaskGrouptemp=preTaskGroup_t;}
					
				  pretaskAnalysisTemp.setCareer(career);
				  pretaskAnalysisTemp.setPreTaskGroup(preTaskGrouptemp);
				  pretaskAnalysisTemp.setTaskName(taskName);
				  pretaskAnalysisTemp.setTaskType(taskType);
				  pretaskAnalysisTemp.setNextAcceptCount(nextAcceptCount+netAcceptCount_t);
				  pretaskAnalysisTemp.setAcceptCount(acceptCount+acceptCount_t);
				  
				  map.put(key,pretaskAnalysisTemp);
			}
			else{
				map.put(key, flow);
			}
		   }
			Iterator<Entry<String,TaskAnalysis>> entrySetIterator=map.entrySet().iterator();
			while(entrySetIterator.hasNext()){
			      Entry<String,TaskAnalysis> entry=entrySetIterator.next();
			      TaskAnalysis taskAnalysis_t= entry.getValue();
			      taskAnalysisAcceptqueue.push(taskAnalysis_t); 
			    	}
			 logger.info("[接受任务日志排重merge] [处理前数量:"+new Integer(messageList.size()).toString()+"] " +
				   		"[处理后数量:"+new Integer(map.size()).toString()+"] [排重数量:"+new Integer(messageList.size()-map.size()).toString()+"]" );

	}
	@Override
	public void run() {
		if(taskAnalysisAcceptqueue==null){ taskAnalysisAcceptqueue = StatServerService.taskAnalysisAcceptqueue; }
		while (Thread.currentThread().isInterrupted() == false) {
			ArrayList<TaskAnalysis> al=new ArrayList(); 
			try {
				while(!taskAnalysisAcceptqueue.isEmpty()) {
					TaskAnalysis taskAnalysis =(TaskAnalysis)taskAnalysisAcceptqueue.pop();
						if(taskAnalysis!=null){al.add(taskAnalysis);}
						if (al.size()>= 4000) {
							handle(al);
							al.clear();
							Thread.sleep(30);
					}
				    }
				if(al.size()>0){
					   handle(al);
						al.clear();
					 }
				synchronized(this){
					 wait(1000);
					 }
			} catch (Exception e) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				logger.info("[reqest处理错误] " +e.getMessage() + " [队列:" + taskAnalysisAcceptqueue.elementNum() + "] ",e);
			}
		}
		logger.info("[reqest处理完毕] [处理线程退出] [队列:" + taskAnalysisAcceptqueue.elementNum() + "]");
	}

	public AdvancedFilePersistentQueue getTaskAnalysisAcceptqueue() {
		return taskAnalysisAcceptqueue;
	}

	public void setTaskAnalysisAcceptqueue(AdvancedFilePersistentQueue taskAnalysisAcceptqueue) {
		this.taskAnalysisAcceptqueue = taskAnalysisAcceptqueue;
	}
}