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

public class TaskFinishService_merge implements Runnable {

	AdvancedFilePersistentQueue taskAnalysisFinishqueue=null;
	static Logger logger = Logger.getLogger(TaskFinishService_merge.class);
	static TaskFinishService_merge self;
	SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");

	public static TaskFinishService_merge getInstance() {
		return self;
	}

	public void init() throws Exception {
		self = this;
	}

	private void handle(ArrayList<TaskAnalysis> messageList) {

		HashMap map=new HashMap();
		for (int i=0;i<messageList.size();i++) {
			TaskAnalysis flow=messageList.get(i);
			long acceptCount=flow.getAcceptCount();
			Date creatDate=flow.getCreateDate();
			long finishCount=flow.getFinishCount();
			long nextAcceptCount=flow.getNextAcceptCount();
			String taskType=flow.getTaskType();
			String fenQu=flow.getFenQu();
			String taskName=flow.getTaskName();
			String career=flow.getCareer();
			String preTaskGroup=flow.getPreTaskGroup();
			String taskGroup=flow.getTaskGroup();
			String jixing=flow.getJixing();
			String dateStr=sf.format(creatDate);
			
			//String key=dateStr+taskGroup+fenQu+jixing;
			
			String key=dateStr+taskGroup+fenQu;
			
			//当前任务处理
			if(map.get(key)!=null){
				  TaskAnalysis pretaskAnalysisTemp=(TaskAnalysis)map.get(key);
				  
				  long acceptCount_t=pretaskAnalysisTemp.getAcceptCount();
					Date creatDate_t=pretaskAnalysisTemp.getCreateDate();
					long finishCount_t=pretaskAnalysisTemp.getFinishCount();
					long nextAcceptCount_t=pretaskAnalysisTemp.getNextAcceptCount();
					String taskType_t=pretaskAnalysisTemp.getTaskType();
					String fenQu_t=pretaskAnalysisTemp.getFenQu();
					String taskName_t=pretaskAnalysisTemp.getTaskName();
					String career_t=pretaskAnalysisTemp.getCareer();
					String preTaskGroup_t=pretaskAnalysisTemp.getPreTaskGroup();
					String taskGroup_t=pretaskAnalysisTemp.getTaskGroup();
					
					pretaskAnalysisTemp.setCareer(career_t);
					
					Date dt=null;
					  if(creatDate!=null){
						   dt=creatDate;
					     }else if(creatDate_t!=null){
					    	 dt=creatDate_t;
					     }else{
					    	 dt=new Date();
					    	 }
					String preTaskGrouptemp=null;
					   if(preTaskGroup!=null){
						  preTaskGrouptemp=preTaskGroup;
					     }else if(preTaskGroup_t!=null){
					    	 preTaskGrouptemp=preTaskGroup_t;
					    	 }
					
				  pretaskAnalysisTemp.setCreateDate(dt);
				  pretaskAnalysisTemp.setCareer(career);
				  pretaskAnalysisTemp.setFenQu(fenQu);
				  pretaskAnalysisTemp.setPreTaskGroup(preTaskGrouptemp);
				  pretaskAnalysisTemp.setTaskGroup(taskGroup);
				  pretaskAnalysisTemp.setTaskName(taskName);
				  pretaskAnalysisTemp.setFinishCount(finishCount_t+finishCount);
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
		      taskAnalysisFinishqueue.push(taskAnalysis_t); 
	   }
//		  logger.info("[完成任务日志排重merge] [排重前数量:"+new Integer(messageList.size()).toString()+"] " +
//			   		"[排重后数量:"+new Integer(map.size()).toString()+"] [排重数量:"+new Integer(messageList.size()-map.size()).toString()+"]" );

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
				logger.info("[reqest处理错误] " +e.getMessage() + " [队列:" + taskAnalysisFinishqueue.elementNum() + "] ",e);
			}
		}
		logger.info("[reqest处理完毕] [处理线程退出] [队列:" + taskAnalysisFinishqueue.elementNum() + "]");
	}

	public AdvancedFilePersistentQueue getTaskAnalysisFinishqueue() {
		return taskAnalysisFinishqueue;
	}

	public void setTaskAnalysisFinishqueue(AdvancedFilePersistentQueue taskAnalysisFinishqueue) {
		this.taskAnalysisFinishqueue = taskAnalysisFinishqueue;
	}
}