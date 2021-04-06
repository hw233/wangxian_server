package com.sqage.stat.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.entity.Battle_TeamStat;
import com.sqage.stat.commonstat.manager.Impl.BattleMSGManagerImpl;
import com.sqage.stat.message.BATTLE_TEAMSTAT_LOG_REQ;
import com.sqage.stat.model.Battle_TeamStatFlow;
import com.xuanzhi.tools.queue.AdvancedFilePersistentQueue;
import com.xuanzhi.tools.transport.RequestMessage;

public class Battle_TeamStatService implements Runnable {

	 AdvancedFilePersistentQueue queue=null;
	 static Logger logger = Logger.getLogger(Battle_TeamStatService.class);
	 public static int startnum=5;//处理队列中对象的最小数量
	 BattleMSGManagerImpl battleMSGManager;
	 static Battle_TeamStatService self;
	 SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
	 public static Battle_TeamStatService getInstance() {
		return self;
	  }

	 public void init() throws Exception {
		self = this;
	  }

	 private void handle(List<RequestMessage> ls) {
		 ArrayList<Battle_TeamStat> battle_TeamStatList   = new ArrayList<Battle_TeamStat>();
		 
		 for(int i=0;i<ls.size();i++)
		 {
			 RequestMessage message=ls.get(i);
				long now = System.currentTimeMillis();
				BATTLE_TEAMSTAT_LOG_REQ req = (BATTLE_TEAMSTAT_LOG_REQ) message;
				Battle_TeamStatFlow flow = req.getBattle_TeamStatFlow();
				if(logger.isDebugEnabled()){logger.debug("[对战团队功勋信息上报] " + flow.toString() + " [队列:" + queue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]"); }
				
				Battle_TeamStat battle_TeamStat=new Battle_TeamStat();
				
				battle_TeamStat.setColumn1(flow.getColumn1());
				battle_TeamStat.setColumn2(flow.getColumn2());
				battle_TeamStat.setCreateDate(flow.getCreateTime());
				battle_TeamStat.setFenqu(flow.getFenqu());
				battle_TeamStat.setGongxun(flow.getGongxun());
				battle_TeamStat.setGuiShaCount(flow.getGuiShaCount());
				battle_TeamStat.setGuojia(flow.getGuojia());
				battle_TeamStat.setHaoTianCount(flow.getHaoTianCount());
				battle_TeamStat.setLingZunCount(flow.getLingZunCount());
				battle_TeamStat.setType(flow.getType());
				battle_TeamStat.setWuHuangCount(flow.getWuHuangCount());
				battle_TeamStatList.add(battle_TeamStat);

		 }	
		 if(battle_TeamStatList.size()>0)  { battleMSGManager.addBattle_TeamStatList(battle_TeamStatList);	}
	  }


	@Override
	public void run() {
	    if(queue==null){queue=StatServerService.battle_TeamStatFlowqueue;}
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
					Thread.sleep(160000);
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
