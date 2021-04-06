package com.sqage.stat.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.entity.GameChongZhi;
import com.sqage.stat.commonstat.manager.Impl.GameChongZhiManagerImpl;
import com.sqage.stat.commonstat.manager.Impl.UserManagerImpl;
import com.sqage.stat.message.GAMECHONGZHI_LOG_REQ;
import com.sqage.stat.model.GameChongZhiFlow;
import com.xuanzhi.tools.queue.AdvancedFilePersistentQueue;
import com.xuanzhi.tools.transport.RequestMessage;

public class GameChongZhiService implements Runnable {

	AdvancedFilePersistentQueue gameChongZhiqueue=StatServerService.gameChongZhiqueue;
	static Logger logger = Logger.getLogger(GameChongZhiService.class);
	Thread thread1;

	public static int startnum=2000;//处理队列中对象的最小数量
	
	public static long targetTime=1364227200000L;//时间
	
	
	GameChongZhiManagerImpl gameChongZhiManager;
	UserManagerImpl userManager;
	static GameChongZhiService self;
	
	 SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");

	public static GameChongZhiService getInstance() {
		return self;
	}

	public void init() throws Exception {
		if(gameChongZhiqueue==null){gameChongZhiqueue=StatServerService.gameChongZhiqueue;}
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
	
	
	
	
	private boolean doGameChongZhiFlow(RequestMessage message) {
		boolean result=false;
		long now = System.currentTimeMillis();
		GAMECHONGZHI_LOG_REQ req = (GAMECHONGZHI_LOG_REQ) message;
		GameChongZhiFlow flow = req.getGameChongZhiFlow();
		if(logger.isDebugEnabled()){
		logger.debug("[游戏中的货币充值，消耗信息上报] " + flow.toString() + " [队列:" + gameChongZhiqueue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]");
		}
		int action=flow.getAction();
		String currencyType=flow.getCurrencyType();
		String fenQu=flow.getFenQu();
		String game=flow.getGame();
		
		String gameLevel=flow.getGameLevel();
		Long money=flow.getMoney();
		String quDao=flow.getQuDao();
		String reasonType=flow.getReasonType();
		
		Long time=flow.getTime();
		String userName=flow.getUserName();
		String jixing=flow.getJixing();
		
		GameChongZhi gameChongZhi=new GameChongZhi();
		gameChongZhi.setAction(action);
		
		gameChongZhi.setCurrencyType(currencyType);
		 
		
		gameChongZhi.setFenQu(fenQu);
		gameChongZhi.setGame(game);
		
		gameChongZhi.setGameLevel(gameLevel);
		gameChongZhi.setMoney(money);
		gameChongZhi.setQuDao(quDao);
		
		
		String[] reasonTypeS=StatServerService.ReasonTypeMap.get(reasonType);
		if(reasonTypeS==null){
			userManager.addReasonType(reasonType);
			reSetReasonTypeMap();
		}
		reasonTypeS=StatServerService.ReasonTypeMap.get(reasonType);
		  if(reasonTypeS!=null){
			  gameChongZhi.setReasonType(reasonTypeS[0]);
			}else{
				gameChongZhi.setReasonType(reasonType);
				logger.info("dt_RessonType表中"+reasonType+"不存在");
			}
		  
		  
		gameChongZhi.setReasonType(reasonType);
		
		
		if(time!=null){gameChongZhi.setTime(new Date(time));}else{gameChongZhi.setTime(new Date());}
		gameChongZhi.setUserName(userName);
		gameChongZhi.setJixing(jixing);
		
		
		GameChongZhi returngameChongZhi=gameChongZhiManager.add(gameChongZhi);
//		if(returngameChongZhi!=null){
//			result=true;
//		}
		return true;
	}
	
	private void handle(ArrayList<GameChongZhiFlow> messageList) {
		HashMap<String, GameChongZhiFlow> map=new HashMap<String, GameChongZhiFlow>();
		try {
			for (int i=0;i< messageList.size();i++) {
				GameChongZhiFlow flow =messageList.get(i);
				if(flow!=null){
					//GAMECHONGZHI_LOG_REQ req = (GAMECHONGZHI_LOG_REQ) message;
					//GameChongZhiFlow flow = req.getGameChongZhiFlow();
					if(logger.isDebugEnabled()){
					logger.debug("[游戏中的货币充值，消耗信息上报] " + flow.toString() + " [队列:" + gameChongZhiqueue.elementNum() + "] ");
					}
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
					
					//String key=currencyType+fenQu+gameLevel+quDao+reasonType+date+userName;
					
					String key=currencyType+fenQu+quDao+reasonType+date;
					
					GameChongZhiFlow flowtemp=null;
					if(map.get(key)!=null){
						flowtemp=(GameChongZhiFlow)map.get(key); 
						flowtemp.setTime(time);
						flowtemp.setMoney(flowtemp.getMoney()+money);
						map.put(key,flowtemp);
						
					} else {
						map.put(key, flow);
						
					}
				}	else{
					logger.info("发现有GameChongZhiFlow对象为空");
				}	
				
			}
				//HashMap<String,String> entrySetMap=new HashMap<String,String>();
				Iterator<Entry<String,GameChongZhiFlow>> entrySetIterator=map.entrySet().iterator();
				ArrayList<GameChongZhi> ls=new ArrayList<GameChongZhi>();
				while(entrySetIterator.hasNext()){
					Entry<String,GameChongZhiFlow> entry=entrySetIterator.next();
					GameChongZhiFlow gameChongZhiFlow= entry.getValue();
					
					int action=gameChongZhiFlow.getAction();
					String currencyType=gameChongZhiFlow.getCurrencyType();
					String fenQu=gameChongZhiFlow.getFenQu();
					String game=gameChongZhiFlow.getGame();
					
					String gameLevel=gameChongZhiFlow.getGameLevel();
					Long money=gameChongZhiFlow.getMoney();
					String quDao=gameChongZhiFlow.getQuDao();
					String reasonType=gameChongZhiFlow.getReasonType();
					Long time=gameChongZhiFlow.getTime();
					String userName=gameChongZhiFlow.getUserName();
					String jixing=gameChongZhiFlow.getJixing();
					
					
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
					
					
					//gameChongZhi.setCurrencyType(currencyType);
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
					
					
					
					//gameChongZhi.setReasonType(reasonType);
					if(time!=null){gameChongZhi.setTime(new Date(time));}else{gameChongZhi.setTime(new Date());}
					gameChongZhi.setUserName(userName);
					gameChongZhi.setJixing(jixing);
					//GameChongZhi returngameChongZhi=gameChongZhiManager.add(gameChongZhi);
					
					//result= doGameChongZhi(gameChongZhi);
					//if(!result){gameChongZhiqueue.push(gameChongZhiFlow);}
					
					ls.add(gameChongZhi);
					//gameChongZhiManager.add(gameChongZhi);
					
				}
//			if(logger.isDebugEnabled()){
//			logger.debug("[游戏中的货币充值排重] [排重前数量:"+new Integer(messageList.size()).toString()+"] " +
//					"[排重后数量:"+new Integer(map.size()).toString()+"] [排重数量:"+new Integer(messageList.size()-map.size()).toString()+"] " +
//					" [真正排重num2:"+num2+"] [map未排重num3:"+num3+"] [num4:" +num4+ "]" );
//			}
			gameChongZhiManager.addList(ls);
		} catch (Exception e) {
			logger.error("[去重复时异常]",e);
		} 
	}

	private boolean doGameChongZhi(GameChongZhi gameChongZhi) {
		boolean result=false;
		GameChongZhi tempgameChongZhi=null;
		GameChongZhi gameChongZ=null;
		List<GameChongZhi> gameChongZhiList=gameChongZhiManager.getByGameChongZhi(gameChongZhi);
		if(gameChongZhiList.size()>0){
			tempgameChongZhi=(GameChongZhi)gameChongZhiList.get(0);
		}
		if(tempgameChongZhi==null){
			gameChongZ=gameChongZhiManager.add(gameChongZhi);
			if(gameChongZ!=null){ result=true;  }
		}else{
			tempgameChongZhi.setMoney(tempgameChongZhi.getMoney()+gameChongZhi.getMoney());
			tempgameChongZhi.setTime(gameChongZhi.getTime());
			result=gameChongZhiManager.update(tempgameChongZhi);
		}
		return result;
	}

	@Override
	public void run() {
		if(gameChongZhiqueue==null){gameChongZhiqueue=StatServerService.gameChongZhiqueue;}
		while (Thread.currentThread().isInterrupted() == false) {
			ArrayList<GameChongZhiFlow> al=new ArrayList(); 
			long num=gameChongZhiqueue.pushNum()-gameChongZhiqueue.popNum();
			if(num>startnum){
			try {
				while(!gameChongZhiqueue.isEmpty()) {
					GameChongZhiFlow flow = (GameChongZhiFlow) gameChongZhiqueue.pop();
					//logger.info("GameChongZhiFlow收到"+flow.toString());
					if(flow!=null){	al.add(flow);}
					if (al.size()> startnum/10) {
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
				logger.error("[reqest处理错误] " +e.getMessage() + " [队列:" + gameChongZhiqueue.elementNum() + "] ",e);
			}
			}else{
				synchronized(this){
					 try {
						wait(1000);
					} catch (InterruptedException e) {
						logger.info("[reqest处理错误] " +e.getMessage() + " [队列:" + gameChongZhiqueue.elementNum() + "] ",e);
					}
				 }
				}
		}
		if(logger.isDebugEnabled()){
			logger.debug("[reqest处理完毕] [处理线程退出] [队列:" + gameChongZhiqueue.elementNum() + "]");
		}
		}



	public GameChongZhiManagerImpl getGameChongZhiManager() {
		return gameChongZhiManager;
	}

	public void setGameChongZhiManager(GameChongZhiManagerImpl gameChongZhiManager) {
		this.gameChongZhiManager = gameChongZhiManager;
	}

	public AdvancedFilePersistentQueue getGameChongZhiqueue() {
		return gameChongZhiqueue;
	}

	public void setGameChongZhiqueue(AdvancedFilePersistentQueue gameChongZhiqueue) {
		this.gameChongZhiqueue = gameChongZhiqueue;
	}

	public UserManagerImpl getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManagerImpl userManager) {
		this.userManager = userManager;
	}


}