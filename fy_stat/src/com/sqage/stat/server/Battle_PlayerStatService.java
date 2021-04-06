package com.sqage.stat.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.entity.Battle_PlayerStat;
import com.sqage.stat.commonstat.manager.Impl.BattleMSGManagerImpl;
import com.sqage.stat.message.BATTLE_PLAYERSTAT_LOG_REQ;
import com.sqage.stat.model.Battle_PlayerStatFlow;
import com.xuanzhi.tools.queue.AdvancedFilePersistentQueue;
import com.xuanzhi.tools.transport.RequestMessage;

public class Battle_PlayerStatService implements Runnable {

	 AdvancedFilePersistentQueue queue=null;
	 static Logger logger = Logger.getLogger(Battle_PlayerStatService.class);
	 public static int startnum=5;//处理队列中对象的最小数量
	 BattleMSGManagerImpl battleMSGManager;
	 static Battle_PlayerStatService self;
	 SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
	 public static Battle_PlayerStatService getInstance() {
		return self;
	  }

	 public void init() throws Exception {
		self = this;
	  }

	 private void handle(List<RequestMessage> ls) {
		 ArrayList<Battle_PlayerStat> battle_PlayerStatList   = new ArrayList<Battle_PlayerStat>();//购买成功和提前撤单以外的消息入数据库的处理
		 
		 for(int i=0;i<ls.size();i++)
		 {
			 RequestMessage message=ls.get(i);
				long now = System.currentTimeMillis();
				BATTLE_PLAYERSTAT_LOG_REQ req = (BATTLE_PLAYERSTAT_LOG_REQ) message;
				Battle_PlayerStatFlow flow = req.getBattle_PlayerStatFlow();
				if(logger.isDebugEnabled()){logger.debug("[对战个人功勋信息上报] " + flow.toString() + " [队列:" + queue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]"); }
				
				String column1 =flow.getColumn1();
				String column2 =flow.getColumn2();
				long createDate=flow.getCreateTime();
				
				String fenqu=flow.getFenqu();
				String gongxun=flow.getGongxun();
				long playerCount=flow.getPlayerCount();
				String type=flow.getType();
				
				Battle_PlayerStat battle_PlayerStat=new Battle_PlayerStat();
				
				battle_PlayerStat.setColumn1(column1);
				battle_PlayerStat.setColumn2(column2);
				battle_PlayerStat.setCreateDate(createDate);
				battle_PlayerStat.setFenqu(fenqu);
				battle_PlayerStat.setGongxun(gongxun);
				battle_PlayerStat.setPlayerCount(playerCount);
				battle_PlayerStat.setType(type);
				battle_PlayerStatList.add(battle_PlayerStat);
		 }	
		 if(battle_PlayerStatList.size()>0)  { battleMSGManager.addBattle_PlayerStatLis(battle_PlayerStatList);	}
	  }


	@Override
	public void run() {
	    if(queue==null){queue=StatServerService.battle_PlayerStatqueue;}
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
					Thread.sleep(10000);
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