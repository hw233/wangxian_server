package com.sqage.stat.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.entity.TaskAnalysis;
import com.sqage.stat.model.AcceptTaskFlow;
import com.xuanzhi.tools.queue.AdvancedFilePersistentQueue;

public class Task2AnalysisService implements Runnable {

	AdvancedFilePersistentQueue taskAcceptqueue=null;
	AdvancedFilePersistentQueue taskAnalysisAcceptqueue=null;
	static Logger logger = Logger.getLogger(Task2AnalysisService.class);
	static Task2AnalysisService self;
	SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");

	public static Task2AnalysisService getInstance() {
		return self;
	}

	public void init() throws Exception {
		self = this;
	}

	private void handle(ArrayList<AcceptTaskFlow> messageList) {
		if(taskAnalysisAcceptqueue==null){ taskAnalysisAcceptqueue = StatServerService.taskAnalysisAcceptqueue; }
		
		HashMap map=new HashMap();
		for (int i =0; i< messageList.size();i++) {
			AcceptTaskFlow flow=messageList.get(i);
			if(flow!=null){
			if(logger.isDebugEnabled()){logger.debug("[接受任务信息上报] " + flow.toString() );}
			
			Long createDate=flow.getCreateDate();
			String fenQu=flow.getFenQu();
			String gameLevel=flow.getGameLevel();
			String taskName=flow.getTaskName();
			String taskType=flow.getTaskType();
			String userName=flow.getUserName();
			
			String career=flow.getCareer();
			String taskGroup=flow.getTaskGroup();
			String preTaskGroup=flow.getPreTaskGroup();
			String jixing=flow.getJixing();
			String date=sf.format(new Date(createDate));
          
			String key=taskGroup+fenQu+date;
//			String prekey=date+preTaskGroup+fenQu;
	
			//前置任务处理
//			if((taskType.indexOf("主线")!=-1||taskType.indexOf("境界")!=-1)&&preTaskGroup!=null&&!"".equals(preTaskGroup)){
//			
//			if(map.get(prekey)!=null){
//				  TaskAnalysis pretaskAnalysisTemp=(TaskAnalysis)map.get(prekey);
//				  pretaskAnalysisTemp.setCareer(career);
//				  pretaskAnalysisTemp.setCreateDate(new Date(createDate));
//				  pretaskAnalysisTemp.setFenQu(fenQu);
//				  pretaskAnalysisTemp.setTaskGroup(preTaskGroup);
//				  pretaskAnalysisTemp.setJixing(jixing);
//				  //pretaskAnalysisTemp.setTaskType(taskType);
//				  pretaskAnalysisTemp.setNextAcceptCount(pretaskAnalysisTemp.getNextAcceptCount()+1);
//				  map.put(prekey,pretaskAnalysisTemp);
//			}else{
//				TaskAnalysis pretaskAnalysis=new TaskAnalysis();
//				pretaskAnalysis.setAcceptCount(0);
//				pretaskAnalysis.setCreateDate(new Date(createDate));
//				pretaskAnalysis.setFenQu(fenQu);
//				pretaskAnalysis.setFinishCount(0);
//				pretaskAnalysis.setNextAcceptCount(1);
//				pretaskAnalysis.setTaskGroup(preTaskGroup);
//				pretaskAnalysis.setJixing(jixing);
//				map.put(prekey, pretaskAnalysis);
//			}
//			}
			//当前任务处理
			if(map.get(key)!=null){
				  TaskAnalysis taskAnalysisTemp=(TaskAnalysis)map.get(key);
				  taskAnalysisTemp.setCreateDate(new Date(createDate));
				  taskAnalysisTemp.setCareer(career);
				  taskAnalysisTemp.setFenQu(fenQu);
				  taskAnalysisTemp.setPreTaskGroup(preTaskGroup);
				  taskAnalysisTemp.setTaskGroup(taskGroup);
				  taskAnalysisTemp.setTaskName(taskName);
				  taskAnalysisTemp.setTaskType(taskType);
				  taskAnalysisTemp.setJixing(jixing);
				  taskAnalysisTemp.setAcceptCount(taskAnalysisTemp.getAcceptCount()+1);
				  map.put(key,taskAnalysisTemp);
			}else{
				TaskAnalysis taskAnalysis=new TaskAnalysis();
				taskAnalysis.setAcceptCount(1);
				taskAnalysis.setCareer(career);
				taskAnalysis.setCreateDate(new Date(createDate));
				taskAnalysis.setFenQu(fenQu);
				taskAnalysis.setFinishCount(0);
				taskAnalysis.setNextAcceptCount(0);
			    taskAnalysis.setPreTaskGroup(preTaskGroup);
				taskAnalysis.setTaskGroup(taskGroup);
			    taskAnalysis.setTaskName(taskName);
				taskAnalysis.setTaskType(taskType);
				taskAnalysis.setJixing(jixing);
				map.put(key, taskAnalysis);
			}
		}else{
			logger.info("AcceptTaskFlow队列出现null值");
			
		}
	}
			Iterator<Entry<String,TaskAnalysis>> entrySetIterator=map.entrySet().iterator();

			while(entrySetIterator.hasNext()){
			      Entry<String,TaskAnalysis> entry=entrySetIterator.next();
			      TaskAnalysis taskAnalysis_t= entry.getValue();
			      taskAnalysisAcceptqueue.push(taskAnalysis_t); 
			    	}
			if(logger.isDebugEnabled()){
			 logger.debug("[完成任务日志排重] [处理前数量:"+new Integer(messageList.size()).toString()+"] " +
				   		"[处理后数量:"+new Integer(map.size()).toString()+"] [排重数量:"+new Integer(messageList.size()-map.size()).toString()+"]" );
			}
	}
	@Override
	public void run() {
		if(taskAcceptqueue==null){taskAcceptqueue=StatServerService.taskAcceptqueue;}
		
		while (Thread.currentThread().isInterrupted() == false) {
			ArrayList<AcceptTaskFlow> al=new ArrayList(); 
			try {
				while(!taskAcceptqueue.isEmpty()) {
					//RequestMessage req = (RequestMessage) daoJuqueue.pop();
					AcceptTaskFlow flow =(AcceptTaskFlow)taskAcceptqueue.pop();
						if(flow!=null){al.add(flow);}
						if (al.size()>= 1000) {
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
				logger.error("[reqest处理错误] " +e.getMessage() + " [队列:" + taskAcceptqueue.elementNum() + "] ",e);
			}
		}
		if(logger.isDebugEnabled()){
		logger.debug("[reqest处理完毕] [处理线程退出] [队列:" + taskAcceptqueue.elementNum() + "]");
	     }
	}

	public AdvancedFilePersistentQueue getTaskAcceptqueue() {
		return taskAcceptqueue;
	}
	public void setTaskAcceptqueue(AdvancedFilePersistentQueue taskAcceptqueue) {
		this.taskAcceptqueue = taskAcceptqueue;
	}

	public AdvancedFilePersistentQueue getTaskAnalysisAcceptqueue() {
		return taskAnalysisAcceptqueue;
	}

	public void setTaskAnalysisAcceptqueue(AdvancedFilePersistentQueue taskAnalysisAcceptqueue) {
		this.taskAnalysisAcceptqueue = taskAnalysisAcceptqueue;
	}
	
}