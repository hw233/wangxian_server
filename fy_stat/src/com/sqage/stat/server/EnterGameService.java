package com.sqage.stat.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.entity.PlayGame;
import com.sqage.stat.commonstat.manager.Impl.PlayGameManagerImpl;
import com.sqage.stat.message.ENTERGAME_LOG_REQ;
import com.sqage.stat.message.LOGOUTGAME_LOG_REQ;
import com.sqage.stat.model.EnterGameFlow;
import com.sqage.stat.model.LogOutGameFlow;
import com.xuanzhi.tools.queue.AdvancedFilePersistentQueue;
import com.xuanzhi.tools.transport.RequestMessage;

public class EnterGameService implements Runnable {

	 AdvancedFilePersistentQueue queue=null;
	 static Logger logger = Logger.getLogger(StatServerService.class);
	 public static int startnum=1000;//处理队列中对象的最小数量
	 PlayGameManagerImpl playGameManager;
	 String shetname=StatServerService.shetname;
	 static EnterGameService self;
	 public static EnterGameService getInstance() {
		return self;
	  }

	 public void init() throws Exception {
		self = this;
	  }

	 private void handle(List<RequestMessage> ls) {
		 ArrayList<PlayGame> enterGameList   = new ArrayList<PlayGame>();
		 
		 for(int i=0;i<ls.size();i++)
		 {
			 RequestMessage message=ls.get(i);
			 if (message instanceof ENTERGAME_LOG_REQ) {  //玩家进入游戏
				long now = System.currentTimeMillis();
				ENTERGAME_LOG_REQ req = (ENTERGAME_LOG_REQ) message;
				EnterGameFlow flow = req.getEnterGameFlow();
				if(logger.isDebugEnabled()){logger.debug("[玩家进入游戏上报] " + flow.toString() + " [队列:" + queue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]"); }
				PlayGame playGame = null;
				
				 long date = flow.getDate();
		         if(shetname!=null&&shetname.indexOf("韩国")!=-1){date+=60*60*1000;}//如果是韩国，时间加一小时，以平衡时区
				
				String fenQu  = flow.getFenQu();
				String game   = flow.getGame();
				String level  = flow.getLevel();
				String name   = flow.getUserName();
				String quDao  = flow.getQuDao();
				String jixing = flow.getJixing();
				String zhiye   = flow.getZhiye();
				String column1 = flow.getColumn1();
				String column2 = flow.getColumn2();
			 
				if (playGame == null) {
					playGame = new PlayGame();
					playGame.setFenQu(fenQu);
					playGame.setEnterDate(new Date(date));
					playGame.setUserName(name);
					playGame.setGame(game);
					playGame.setEnterTimes(1L);
					if (level != null) {
						playGame.setMaxLevel(Long.parseLong(level));
						playGame.setMinLevel(Long.parseLong(level));
					} else {
						playGame.setMaxLevel(1L);
						playGame.setMinLevel(1L);
					}
					playGame.setQuDao(quDao);
					playGame.setJixing(jixing);
					playGame.setZhiye(zhiye);
					playGame.setColumn1(column1);
					playGame.setColumn2(column2);
					
					enterGameList.add(playGame);
				} 
		 }
			 else if (message instanceof LOGOUTGAME_LOG_REQ) {  //玩家退出游戏
		 { 
			    boolean result=false;
				long now = System.currentTimeMillis();
				LOGOUTGAME_LOG_REQ req = (LOGOUTGAME_LOG_REQ) message;
				LogOutGameFlow flow = req.getLogOutGameFlow();
				if(logger.isDebugEnabled()){logger.debug("[玩家退出游戏上报] " + flow.toString() + " [队列:" + queue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]"); }
				
				PlayGame playGame = new PlayGame();;
				 long date = flow.getDate();
		         if(shetname!=null&&shetname.indexOf("韩国")!=-1){date+=60*60*1000;}//如果是韩国，时间加一小时，以平衡时区
				
				String fenQu = flow.getFenQu();
				String game = flow.getGame();
				String level = flow.getLevel();
				String name = flow.getUserName();
				Long youXiBi=flow.getYouXiBi();
				Long yaunBaoCount=flow.getYuanBaoCount();
				long onLineTime = flow.getOnLineTime();
				String quDao   = flow.getQuDao();
				String jixing  = flow.getJixing();
			
					playGame.setFenQu(fenQu);
					playGame.setGame(game);
					playGame.setUserName(name);
					
					playGame.setEnterDate(new Date(date));
					playGame.setOnLineTime(onLineTime);
					playGame.setMaxLevel(Long.parseLong(level));
					//playGame.setMinLevel(Long.parseLong(level));
					if(youXiBi!=null){playGame.setYouXiBi(youXiBi);}
					if(yaunBaoCount!=null){playGame.setYuanBaoCount(yaunBaoCount);}
					playGame.setQuDao(quDao);
					playGame.setJixing(jixing);
			   enterGameList.add(playGame);	
		 }
//			 if (message instanceof DAYCHENG_LOG_REQ) {//  玩家连登处理
//			 
//			 boolean result=false;
//				long now = System.currentTimeMillis();
//				DAYCHENG_LOG_REQ req = (DAYCHENG_LOG_REQ) message;
//				DayChangFlow flow = req.getDayChangFlow();
//				if(logger.isDebugEnabled()){logger.debug("[日期变化] " + flow.toString() + " [队列:" + queue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]");	}
//				
//				 long date = flow.getDate();
//		         if(shetname!=null&&shetname.indexOf("韩国")!=-1){date+=60*60*1000;}//如果是韩国，时间加一小时，以平衡时区
//				
//				String fenqu = flow.getFenQu();
//				String game = flow.getGame();
//				String level = flow.getLevel();
//				String name = flow.getUserName();
//				Long youXiBi=flow.getYouXiBi();
//				Long yuanBaoCount=flow.getYuanBaoCount();
//				String quDao=flow.getQuDao();
//				String jixing=flow.getJixing();
//				String zhiye   =flow.getZhiye();
//				String column1 =flow.getColumn1();
//				String column2 =flow.getColumn2();
//				long onlineTime=flow.getOnLineTime();
//				
//				long onLineTime_1 = flow.getOnLineTime();
//				PlayGame playGame = new PlayGame();
//								
//					playGame.setFenQu(fenqu);
//					playGame.setEnterDate(new Date(date));
//					playGame.setUserName(name);
//					playGame.setGame(game);
//					playGame.setEnterTimes(1L);
//					playGame.setOnLineTime(onlineTime);
//					if (level != null) {
//						playGame.setMaxLevel(Long.parseLong(level));
//						playGame.setMinLevel(Long.parseLong(level));
//					}
//					playGame.setQuDao(quDao);
//					playGame.setJixing(jixing);
//					
//					playGame.setZhiye(zhiye);
//					playGame.setColumn1(column1);
//					playGame.setColumn2(column2);
//					if(youXiBi!=null){playGame.setYouXiBi(youXiBi);}
//					if(yuanBaoCount!=null){playGame.setYuanBaoCount(yuanBaoCount);}
//				
//				enterGameList.add(playGame);	
//				
//				PlayGame playGame_nextDay = new PlayGame();
//				playGame_nextDay.setFenQu(fenqu);
//				playGame_nextDay.setEnterDate(new Date(date + 24 * 3600 * 1000));
//				playGame_nextDay.setUserName(name);
//				playGame_nextDay.setGame(game);
//				playGame_nextDay.setEnterTimes(1L);
//				playGame_nextDay.setOnLineTime(0L);
//				if (level != null) {
//					playGame_nextDay.setMaxLevel(Long.parseLong(level));
//					playGame_nextDay.setMinLevel(Long.parseLong(level));
//				}
//				playGame_nextDay.setQuDao(quDao);
//				playGame_nextDay.setJixing(jixing);
//				
//				playGame_nextDay.setZhiye(zhiye);
//				playGame_nextDay.setColumn1(column1);
//				playGame_nextDay.setColumn2(column2);
//				
//				enterGameList.add(playGame_nextDay);
//		 }
		 }
		 }
			 
			 
		 if(enterGameList.size()>0)  { 
			 playGameManager.merge(enterGameList);
			 }
	  }


	@Override
	public void run() {
	    if(queue==null){queue=StatServerService.enterGamequeue;}
		while (Thread.currentThread().isInterrupted() == false) {
			try {
				List<RequestMessage> ls=new ArrayList();
				while(!queue.isEmpty()) {
					RequestMessage req = (RequestMessage) queue.pop();
					if(req != null){
						ls.add(req);
						if(ls.size()>=startnum)
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
				try {
					Thread.sleep(11100);
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
				
				logger.error("[reqest处理错误] [队列queue:" + queue.elementNum() + "] ",e);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		if(logger.isDebugEnabled()){
		logger.debug("[reqest处理完毕] [处理线程退出] [队列:" + queue.elementNum() + "]");
	}
	}

	public AdvancedFilePersistentQueue getQueue() {
		return queue;
	}

	public void setQueue(AdvancedFilePersistentQueue queue) {
		this.queue = queue;
	}

	public PlayGameManagerImpl getPlayGameManager() {
		return playGameManager;
	}

	public void setPlayGameManager(PlayGameManagerImpl playGameManager) {
		this.playGameManager = playGameManager;
	}



}