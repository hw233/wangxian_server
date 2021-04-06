package com.sqage.stat.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.entity.TaskAnalysis;
import com.sqage.stat.model.FinishTaskFlow;
import com.xuanzhi.tools.queue.AdvancedFilePersistentQueue;

public class TaskFinish2AnalysisService implements Runnable {

	AdvancedFilePersistentQueue taskFinishqueue=null;
	AdvancedFilePersistentQueue taskAnalysisFinishqueue=null;
	
	static Logger logger = Logger.getLogger(TaskFinish2AnalysisService.class);
	static TaskFinish2AnalysisService self;
	SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
	public static TaskFinish2AnalysisService getInstance() {
		return self;
	}
	public void init() throws Exception {
		self = this;
	}

	private void handle(ArrayList<FinishTaskFlow> messageList) {
		if(taskAnalysisFinishqueue==null){taskAnalysisFinishqueue=StatServerService.taskAnalysisFinishqueue;}
		HashMap map=new HashMap();
		for (int i=0;i<messageList.size();i++) {
			FinishTaskFlow flow=messageList.get(i);
			//logger.info("[完成任务信息上报] " + flow.toString() );
			String fenQu=flow.getFenQu();
			Integer getDaoJu=flow.getGetDaoJu();
			Integer getWuPin=flow.getGetWuPin();
			Integer getYouXiBi=flow.getGetYOuXiBi();
			String status=flow.getStatus();
			String taskName=flow.getTaskName();
			String userName=flow.getUserName();
			String award=flow.getAward();
			String career=flow.getCareer();
			String preTaskGroup=flow.getPreTaskGroup();
			String taskGroup=flow.getTaskGroup();
			String jixing=flow.getJixing();
			long date=flow.getDate();
			String dateStr=sf.format(new Date(date));
			
			String key=dateStr+taskGroup+fenQu;
			//当前任务处理
			if(map.get(key)!=null){
				  TaskAnalysis pretaskAnalysisTemp=(TaskAnalysis)map.get(key);
				  pretaskAnalysisTemp.setCareer(career);
				  pretaskAnalysisTemp.setFenQu(fenQu);
				  pretaskAnalysisTemp.setPreTaskGroup(preTaskGroup);
				  pretaskAnalysisTemp.setTaskGroup(taskGroup);
				  pretaskAnalysisTemp.setTaskName(taskName);
				  pretaskAnalysisTemp.setFinishCount(pretaskAnalysisTemp.getFinishCount()+1);
				  map.put(key,pretaskAnalysisTemp);
			}else{
				TaskAnalysis pretaskAnalysis=new TaskAnalysis();
				pretaskAnalysis.setAcceptCount(0);
				pretaskAnalysis.setCareer(career);
				pretaskAnalysis.setFenQu(fenQu);
				pretaskAnalysis.setFinishCount(1);
				pretaskAnalysis.setNextAcceptCount(0);
			    pretaskAnalysis.setPreTaskGroup(preTaskGroup);
				pretaskAnalysis.setTaskGroup(taskGroup);
			    pretaskAnalysis.setTaskName(taskName);
			    pretaskAnalysis.setJixing(jixing);
			    pretaskAnalysis.setCreateDate(new Date(date));
				map.put(key, pretaskAnalysis);
			}
		}
		
		Iterator<Entry<String,TaskAnalysis>> entrySetIterator=map.entrySet().iterator();
			while(entrySetIterator.hasNext()){
			      Entry<String,TaskAnalysis> entry=entrySetIterator.next();
			      TaskAnalysis taskAnalysis_t= entry.getValue();
			      taskAnalysisFinishqueue.push(taskAnalysis_t);
		      }	
			if(logger.isDebugEnabled()){
			  logger.debug("[接受任务日志排重] [排重前数量:"+new Integer(messageList.size()).toString()+"] " +
				   		"[排重后数量:"+new Integer(map.size()).toString()+"] [排重数量:"+new Integer(messageList.size()-map.size()).toString()+"]" );
	     }

	}
	@Override
	public void run() {
		if(taskFinishqueue==null){ taskFinishqueue=StatServerService.taskFinishqueue; }
		while (Thread.currentThread().isInterrupted() == false) {
			ArrayList<FinishTaskFlow> al=new ArrayList(); 
			try {
				while(!taskFinishqueue.isEmpty()) {
					FinishTaskFlow flow =(FinishTaskFlow)taskFinishqueue.pop();
						if(flow!=null){	al.add(flow);}
						if (al.size()>= 1500) {
							handle(al);
							al.clear();
					   }
				    }
				   if(al.size()>0){
					   handle(al);
						al.clear();
						Thread.sleep(10);
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
				logger.error("[reqest处理错误] " +e.getMessage() + " [队列:" + taskFinishqueue.elementNum() + "] ",e);
			}
		}
		//logger.info("[reqest处理完毕] [处理线程退出] [队列:" + taskFinishqueue.elementNum() + "]");
	}

	public AdvancedFilePersistentQueue getTaskFinishqueue() {
		return taskFinishqueue;
	}

	public void setTaskFinishqueue(AdvancedFilePersistentQueue taskFinishqueue) {
		this.taskFinishqueue = taskFinishqueue;
	}
	public AdvancedFilePersistentQueue getTaskAnalysisFinishqueue() {
		return taskAnalysisFinishqueue;
	}
	public void setTaskAnalysisFinishqueue(AdvancedFilePersistentQueue taskAnalysisFinishqueue) {
		this.taskAnalysisFinishqueue = taskAnalysisFinishqueue;
	}

}