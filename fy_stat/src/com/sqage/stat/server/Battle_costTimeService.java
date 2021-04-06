package com.sqage.stat.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.entity.Battle_costTime;
import com.sqage.stat.commonstat.manager.Impl.BattleMSGManagerImpl;
import com.sqage.stat.message.BATTLE_COSTTIME_LOG_REQ;
import com.sqage.stat.model.Battle_costTimeFlow;
import com.xuanzhi.tools.queue.AdvancedFilePersistentQueue;
import com.xuanzhi.tools.transport.RequestMessage;

public class Battle_costTimeService implements Runnable {
	 AdvancedFilePersistentQueue queue=null;
	 static Logger logger = Logger.getLogger(Battle_costTimeService.class);
	 public static int startnum=100;//处理队列中对象的最小数量
	 BattleMSGManagerImpl battleMSGManager;
	 static Battle_costTimeService self;
	 SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
	 public static Battle_costTimeService getInstance() {
		return self;
	  }
	 public void init() throws Exception {
		self = this;
	  }

	 private void handle(List<RequestMessage> ls) {
		 ArrayList<Battle_costTime> battle_costTimeList   = new ArrayList<Battle_costTime>();//购买成功和提前撤单以外的消息入数据库的处理
		 
		 for(int i=0;i<ls.size();i++)
		 {
			 RequestMessage message=ls.get(i);
				long now = System.currentTimeMillis();
				BATTLE_COSTTIME_LOG_REQ req = (BATTLE_COSTTIME_LOG_REQ) message;
				Battle_costTimeFlow flow = req.getBattle_costTimeFlow();
				if(logger.isDebugEnabled()){logger.debug("[对战消耗时长信息上报] " + flow.toString() + " [队列:" + queue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]"); }
				
				String column1 =flow.getColumn1();
				String column2 =flow.getColumn2();
				long costTime=flow.getCostTime();
				long createDate=flow.getCreateTime();
				String fenqu=flow.getFenqu();
				String guiShaCount=flow.getGuiShaCount();
				String haoTianCount=flow.getHaoTianCount();
				String lingZunCount=flow.getLingZunCount();
				String type=flow.getType();
				String wuHuangCount=flow.getWuHuangCount();
				
				Battle_costTime battle_costTime=new Battle_costTime();
				
				battle_costTime.setColumn1(column1);
				battle_costTime.setColumn2(column2);
				battle_costTime.setCostTime(costTime);
				battle_costTime.setCreateDate(createDate);
				battle_costTime.setFenqu(fenqu);
				battle_costTime.setGuiShaCount(guiShaCount);
				battle_costTime.setHaoTianCount(haoTianCount);
				battle_costTime.setLingZunCount(lingZunCount);
				battle_costTime.setType(type);
				battle_costTime.setWuHuangCount(wuHuangCount);
	  		battle_costTimeList.add(battle_costTime);
		 }	
		 if(battle_costTimeList.size()>0)  { battleMSGManager.addBattle_costTimeList(battle_costTimeList);		 }
	  }


	@Override
	public void run() {
	    if(queue==null){queue=StatServerService.battle_costTimeFlowqueue; }
		while (Thread.currentThread().isInterrupted() == false) {
			try {
				List<RequestMessage> ls=new ArrayList();
				while(!queue.isEmpty()) {
					RequestMessage req = (RequestMessage) queue.pop();
					if(req != null){
						ls.add(req);
						if(ls.size()>startnum)
						{
							handle(ls);
							ls.clear();
						}
					}
				}
				if(ls.size()>0){
					handle(ls);	
					ls.clear();
					Thread.sleep(110000);
				}
				
				if(queue.isEmpty()){
					 synchronized(this){
						 wait(160000);
					 }
				}
			} catch (Exception e) {
				logger.error("[reqest处理错误] [队列queue:" + queue.elementNum() + "] ",e);
				try {
					Thread.sleep(100000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		if(logger.isDebugEnabled()){
		logger.debug("[reqest处理完毕] [处理线程退出] [队列:" + queue.elementNum() + "]");
	}
	}
	public BattleMSGManagerImpl getBattleMSGManager() {
		return battleMSGManager;
	}
	public void setBattleMSGManager(BattleMSGManagerImpl battleMSGManager) {
		this.battleMSGManager = battleMSGManager;
	}
}
