package com.sqage.stat.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.entity.GameChongZhi;
import com.sqage.stat.commonstat.manager.Impl.GameChongZhiManagerImpl;
import com.sqage.stat.commonstat.manager.Impl.UserManagerImpl;
import com.sqage.stat.model.GameChongZhiFlow;
import com.xuanzhi.tools.queue.AdvancedFilePersistentQueue;

public class GameChongZhi_JingXiService implements Runnable {

	AdvancedFilePersistentQueue gameChongZhiJingXiqueue=StatServerService.gameChongZhiJingXiqueue;
	static Logger logger = Logger.getLogger(GameChongZhi_JingXiService.class);
	Thread thread1;

	public static int startnum=300;//处理队列中对象的最小数量
	public static long targetTime=1364227200000L;//时间
	
	
	GameChongZhiManagerImpl gameChongZhiManager;
	UserManagerImpl userManager;
	static GameChongZhi_JingXiService self;
	
	 SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");

	public static GameChongZhi_JingXiService getInstance() {
		return self;
	}

	public void init() throws Exception {
		if(gameChongZhiJingXiqueue==null){gameChongZhiJingXiqueue=StatServerService.gameChongZhiJingXiqueue;}
		if(StatServerService.CurrencyTypeMap.size()==0){
			reSetCurrencyType();
			}
		self = this;
	}

	
	public void reSetCurrencyType()
	{
		ArrayList<String[]> ls=userManager.getCurrencyType();
		StatServerService.CurrencyTypeMap.clear();
		for(String[] currencyType:ls){
			StatServerService.CurrencyTypeMap.put(currencyType[1], currencyType);
		}
	}
	public void reSetReasonTypeMap()
	{
		ArrayList<String[]> ls=userManager.getReasonType();
		StatServerService.ReasonTypeMap.clear();
		for(String[] reasonType:ls){
			StatServerService.ReasonTypeMap.put(reasonType[1], reasonType);
		}
	}
	
	
	private void handle(ArrayList<GameChongZhiFlow> messageList) {
		HashMap<String, GameChongZhiFlow> map=new HashMap<String, GameChongZhiFlow>();
		try {
			ArrayList<GameChongZhi> ls=new ArrayList<GameChongZhi>();
			for (int i=0;i< messageList.size();i++) {
				GameChongZhiFlow flow =messageList.get(i);
				if(flow!=null){
					//logger.info("[游戏中的货币充值，消耗信息上报] " + flow.toString() + " [队列:" + gameChongZhiJingXiqueue.elementNum() + "] ");
					int action=flow.getAction();
					String currencyType=flow.getCurrencyType();
					String fenQu=flow.getFenQu();
					String game=flow.getGame();
					String gameLevel=flow.getGameLevel();
					Long money=flow.getMoney();
					String quDao=flow.getQuDao();
					String reasonType=flow.getReasonType();
					Long time=flow.getTime();
					String date=sf.format(new Date(time));
					String userName=flow.getUserName();
					String jixing=flow.getJixing();
					
					GameChongZhi gameChongZhi=new GameChongZhi();
					gameChongZhi.setAction(action);

					if(currencyType==null||"".equals(currencyType)){
						gameChongZhi.setCurrencyType(currencyType);
					}else{
					String[] currencyTypeS=StatServerService.CurrencyTypeMap.get(currencyType);
					if(currencyTypeS==null){
						userManager.addCurrencyType(currencyType);
						reSetCurrencyType();
						currencyTypeS=StatServerService.CurrencyTypeMap.get(currencyType);
						  if(currencyTypeS!=null){
							  gameChongZhi.setCurrencyType(currencyTypeS[0]);
							}else{
								gameChongZhi.setCurrencyType(currencyType);
							}
					}else{
						gameChongZhi.setCurrencyType(currencyTypeS[0]);
					}
					}
					gameChongZhi.setFenQu(fenQu);
					gameChongZhi.setGame(game);
					gameChongZhi.setGameLevel(gameLevel);
					gameChongZhi.setMoney(money);
					gameChongZhi.setQuDao(quDao);
					
					String[] reasonTypeS=StatServerService.ReasonTypeMap.get(reasonType);
					if(reasonTypeS==null){
						userManager.addReasonType(reasonType);
						reSetReasonTypeMap();
						reasonTypeS=StatServerService.ReasonTypeMap.get(reasonType);
						  if(reasonTypeS!=null){
							  gameChongZhi.setReasonType(reasonTypeS[0]);
							}else{
								gameChongZhi.setReasonType(reasonType);
								logger.info("dt_RessonType表中"+reasonType+"不存在");
							}
					}else{
						gameChongZhi.setReasonType(reasonTypeS[0]);
					}
					
					if(time!=null){gameChongZhi.setTime(new Date(time));}else{gameChongZhi.setTime(new Date());}
					gameChongZhi.setUserName(userName);
					gameChongZhi.setJixing(jixing);
					ls.add(gameChongZhi);
				}	else{
					logger.info("插入GameChongZhiFlow对象异常");
				}	
			}

			gameChongZhiManager.addList_jifen(ls);
		} catch (Exception e) {
			logger.error("[添加精细GameChongZhi_JingXieService异常！]",e);
		} 
	}


	@Override
	public void run() {
		if(gameChongZhiJingXiqueue==null){gameChongZhiJingXiqueue=StatServerService.gameChongZhiJingXiqueue;}
		while (Thread.currentThread().isInterrupted() == false) {
			ArrayList<GameChongZhiFlow> al=new ArrayList(); 
			long num=gameChongZhiJingXiqueue.pushNum()-gameChongZhiJingXiqueue.popNum();
			if(num>startnum){
			try {
				while(!gameChongZhiJingXiqueue.isEmpty()) {
					GameChongZhiFlow flow = (GameChongZhiFlow) gameChongZhiJingXiqueue.pop();
					//logger.info("GameChongZhiFlow收到"+flow.toString());
					if(flow!=null){	al.add(flow);}
					if (al.size()> 10) {
						handle(al);
						al.clear();
					}
				}
				
				if(al.size()>0){
					handle(al);
					al.clear();
				}
				synchronized(this){
					wait(5000);
				}
			} catch (Exception e) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				logger.error("[reqest处理错误] " +e.getMessage() + " [队列:" + gameChongZhiJingXiqueue.elementNum() + "] ",e);
			}
			}else{
				synchronized(this){
					 try {
						wait(1000);
					} catch (InterruptedException e) {
						logger.info("[reqest处理错误] " +e.getMessage() + " [队列:" + gameChongZhiJingXiqueue.elementNum() + "] ",e);
					}
				 }
				}
		}
		if(logger.isDebugEnabled()){
			logger.debug("[reqest处理完毕] [处理线程退出] [队列:" + gameChongZhiJingXiqueue.elementNum() + "]");
		}
		}



	public GameChongZhiManagerImpl getGameChongZhiManager() {
		return gameChongZhiManager;
	}

	public void setGameChongZhiManager(GameChongZhiManagerImpl gameChongZhiManager) {
		this.gameChongZhiManager = gameChongZhiManager;
	}


	public AdvancedFilePersistentQueue getGameChongZhiJingXiqueue() {
		return gameChongZhiJingXiqueue;
	}

	public void setGameChongZhiJingXiqueue(AdvancedFilePersistentQueue gameChongZhiJingXiqueue) {
		this.gameChongZhiJingXiqueue = gameChongZhiJingXiqueue;
	}

	public UserManagerImpl getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManagerImpl userManager) {
		this.userManager = userManager;
	}


}