package com.sqage.stat.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.entity.DaoJu;
import com.sqage.stat.commonstat.manager.Impl.DaoJuManagerImpl;
import com.sqage.stat.commonstat.manager.Impl.UserManagerImpl;
import com.sqage.stat.model.DaoJuFlow;
import com.xuanzhi.tools.queue.AdvancedFilePersistentQueue;

public class DaoJuService implements Runnable {

	AdvancedFilePersistentQueue daoJuqueue=StatServerService.daoJuqueue;
	static Logger logger = Logger.getLogger(DaoJuService.class);
	//Thread thread1;

	public static int startnum=1000;//处理队列中对象的最小数量
	DaoJuManagerImpl daoJuManager;
	UserManagerImpl userManager;
	static DaoJuService self;
	
	 SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");

	public static DaoJuService getInstance() {
		return self;
	}

	public void init() throws Exception {
		self = this;
		reSetCurrencyType();
	}
	public void reSetCurrencyType()
	{
		ArrayList<String[]> ls=userManager.getCurrencyType();
		StatServerService.CurrencyTypeMap.clear();
		for(String[] currencyType:ls){
			StatServerService.CurrencyTypeMap.put(currencyType[1], currencyType);
		}
	}
	
	public void reSetGetType()
	{
		ArrayList<String[]> ls=userManager.getGetType();
		StatServerService.GetTypeMap.clear();
		for(String[] getType:ls){
			StatServerService.GetTypeMap.put(getType[1], getType);
		}
	}
	
	public void reSetDaoJuName()
	{
		ArrayList<String[]> ls=userManager.getDaoJuName();
		StatServerService.DaoJuNameMap.clear();
		for(String[] daoJuName:ls){
			StatServerService.DaoJuNameMap.put(daoJuName[1], daoJuName);
		}
	}
	
	private void handle(ArrayList<DaoJuFlow> messageList) {
		
		HashMap map=new HashMap();
		for (DaoJuFlow flow : messageList) {
			if(flow!=null){
				 //DAOJU_LOG_REQ req = (DAOJU_LOG_REQ) message;
					//DaoJuFlow flow = req.getDaoJuFlow();
					//
			    if(logger.isDebugEnabled()){
			        logger.debug("[道具购买消耗信息上报] " + flow.toString() + " [队列:" + daoJuqueue.elementNum() + "] ");
			      }
					Long createDate=flow.getCreateDate();
					String date=sf.format(new Date(createDate));
					Long danJia=flow.getDanJia();
					String daoJuName=flow.getDaoJuName();
					Long daoJuNum=flow.getDaoJuNum();
					
					String fenQu=flow.getFenQu();
					String gameLevel=flow.getGameLevel();
					String getType=flow.getGetType();
					
					
					
					String huoBiType=flow.getHuoBiType();
					String userName=flow.getUserName();
					String position=flow.getPosition();
					
				   String daoJuColor=flow.getDaoJuColor();
				   String daoJuLevel=flow.getDaoJuLevel();
				   String bindType=flow.getBindType();
				   String jixing=flow.getJixing();
				   Long vip=flow.getVip();
				   String guojia=flow.getGuojia();
					
				   String key=date+daoJuName+fenQu+getType+huoBiType+position+daoJuColor;
					
					DaoJuFlow flowtemp=null;
					if(map.get(key)!=null){
						flowtemp=(DaoJuFlow)map.get(key); 
						flowtemp.setCreateDate(createDate);
						flowtemp.setDaoJuNum(flowtemp.getDaoJuNum()+daoJuNum);
						flowtemp.setDanJia(flowtemp.getDanJia()+danJia*daoJuNum);
						map.put(key,flowtemp);
					}
					else{
						flow.setDanJia(flow.getDanJia()*flow.getDaoJuNum());
						map.put(key, flow);
					}
			} else	{
				logger.info("发现有DaoJuFlow对象为空");
			}
		}		
					//HashMap<String,String> entrySetMap=new HashMap<String,String>();
		
		              ArrayList<DaoJu> ls=new ArrayList<DaoJu>();
					   Iterator<Entry<String,DaoJuFlow>> entrySetIterator=map.entrySet().iterator();
					   while(entrySetIterator.hasNext()){
					      Entry<String,DaoJuFlow> entry=entrySetIterator.next();
					      DaoJuFlow daoJuFlow= entry.getValue();
					      
					        Long tcreateDate=daoJuFlow.getCreateDate();
							Long tdanJia=daoJuFlow.getDanJia();
							String tdaoJuName=daoJuFlow.getDaoJuName();
							Long tdaoJuNum=daoJuFlow.getDaoJuNum();
							String tfenQu=daoJuFlow.getFenQu();
							String tgameLevel=daoJuFlow.getGameLevel();
							String tgetType=daoJuFlow.getGetType();
							String thuoBiType=daoJuFlow.getHuoBiType();
							String tuserName=daoJuFlow.getUserName();
							String tposition=daoJuFlow.getPosition();
							
							  String daoJuColor=daoJuFlow.getDaoJuColor();
							  String daoJuLevel=daoJuFlow.getDaoJuLevel();
							  String bindType=daoJuFlow.getBindType();
							  String jixing =daoJuFlow.getJixing();
							  Long vip=daoJuFlow.getVip();
							  String guojia=daoJuFlow.getGuojia();
							
							DaoJu daoJu=new DaoJu();
							daoJu.setCreateDate(new Date(tcreateDate));
							daoJu.setDanJia(tdanJia);
							
							
							
							String[] daoJuNameS=StatServerService.DaoJuNameMap.get(tdaoJuName);
							if(daoJuNameS==null){
								userManager.addDaoJuName(tdaoJuName);
								reSetDaoJuName();
								daoJuNameS=StatServerService.DaoJuNameMap.get(tdaoJuName);
								  if(daoJuNameS!=null){
									  daoJu.setDaoJuName(daoJuNameS[0]);
									}else{
										logger.info("daojuname 为空:"+tdaoJuName);
										daoJu.setDaoJuName(tdaoJuName);
									}
							}else{
								daoJu.setDaoJuName(daoJuNameS[0]);
							}
							

							//daoJu.setDaoJuName(tdaoJuName);
							daoJu.setDaoJuNum(tdaoJuNum);
							daoJu.setFenQu(tfenQu);
							daoJu.setGameLevel(tgameLevel);
							
							
							String[] getTypeS=StatServerService.GetTypeMap.get(tgetType);
							if(getTypeS==null){
								userManager.addGetType(tgetType);
								reSetGetType();
								getTypeS=StatServerService.GetTypeMap.get(tgetType);
								  if(getTypeS!=null){
									  daoJu.setGetType(getTypeS[0]);
									}else{
										daoJu.setGetType(tgetType);
									}
							}else{
								daoJu.setGetType(getTypeS[0]);
							}
							
							if(thuoBiType==null||"".equals(thuoBiType)){
								daoJu.setHuoBiType(thuoBiType);
							}else{
							String[] currencyTypeS=StatServerService.CurrencyTypeMap.get(thuoBiType);
							if(currencyTypeS==null){
								userManager.addCurrencyType(thuoBiType);
								reSetCurrencyType();
								currencyTypeS=StatServerService.CurrencyTypeMap.get(thuoBiType);
								  if(currencyTypeS!=null){
									  daoJu.setHuoBiType(currencyTypeS[0]);
									}else{
										daoJu.setHuoBiType(thuoBiType);
									}
							}else{
								daoJu.setHuoBiType(currencyTypeS[0]);
							}
							}
							//daoJu.setHuoBiType(thuoBiType);
							
							
							
							daoJu.setUserName(tuserName);
							daoJu.setPosition(tposition);
							
							daoJu.setDaoJuColor(daoJuColor);
							daoJu.setBindType(bindType);
							daoJu.setDaoJuLevel(daoJuLevel);
							daoJu.setJixing(jixing);
							daoJu.setVip(vip);
							daoJu.setGuojia(guojia);
					      
							ls.add(daoJu);
							 if(logger.isDebugEnabled()){
							        logger.debug("[道具购买消耗信息上报] 获得数据条数ls："+ls.size());
							      }
					      //result= doDaoJu(daoJu);
					      //if(!result){daoJuqueue.push(daoJuFlow);}
		               }
					   doDaoJuList(ls);
	   }
	
	private boolean  doDaoJuList(ArrayList<DaoJu> daoJulist) {
		
		
		 if(logger.isDebugEnabled()){
		        logger.debug("[道具购买消耗信息上报] 获得道具条数ls："+daoJulist.size());
		      }
		boolean result=true;
//		DaoJu tempdaoJu=null;
//		
//		 ArrayList<DaoJu> addDaoJuList= new ArrayList<DaoJu>();
//		 ArrayList<DaoJu> updateDaoJuList= new ArrayList<DaoJu>();
//		
//		for(DaoJu daoJu:daoJulist){
//		List daJuList_=daoJuManager.getByDaoJu(daoJu);
//		if(daJuList_==null||daJuList_.size()==0){
//			addDaoJuList.add(daoJu);
//			//daoj=daoJuManager.add(daoJu);
//		}else{
//			tempdaoJu=(DaoJu)daJuList_.get(0);
//			tempdaoJu.setDaoJuNum(tempdaoJu.getDaoJuNum()+daoJu.getDaoJuNum());
//			tempdaoJu.setCreateDate(daoJu.getCreateDate());
//			updateDaoJuList.add(tempdaoJu);
//			//result=daoJuManager.update(tempdaoJu);
//		}
//		}
//		daoJuManager.updateList(updateDaoJuList);
//		daoJuManager.addList(addDaoJuList);
		
		daoJuManager.addList(daoJulist);
		if(logger.isDebugEnabled())
		{
			logger.debug("批量插入数据库道具信息"+daoJulist.size()+"条");
		}
		return result;
	}

	private boolean  doDaoJu(DaoJu daoJu) {
		boolean result=false;
		DaoJu tempdaoJu=null;
		DaoJu daoj=null;
		List daJuList=daoJuManager.getByDaoJu(daoJu);
		if(daJuList.size()>0){
			tempdaoJu=(DaoJu)daJuList.get(0);
		}
		if(tempdaoJu==null){
			daoj=daoJuManager.add(daoJu);
			if(daoj!=null){ result=true;  }
		}else{
			tempdaoJu.setDaoJuNum(tempdaoJu.getDaoJuNum()+daoJu.getDaoJuNum());
			tempdaoJu.setCreateDate(daoJu.getCreateDate());
			result=daoJuManager.update(tempdaoJu);
		}
		return result;
	}

	@Override
	public void run() {
		if(daoJuqueue==null){daoJuqueue=StatServerService.daoJuqueue;}
		while (Thread.currentThread().isInterrupted() == false) {
			ArrayList<DaoJuFlow> al=new ArrayList(); 
			long num=daoJuqueue.pushNum()-daoJuqueue.popNum();
			if(num>startnum){
			try {
				while(!daoJuqueue.isEmpty()) {
					//RequestMessage req = (RequestMessage) daoJuqueue.pop();
					DaoJuFlow flow =(DaoJuFlow)daoJuqueue.pop();
						if(flow!=null){al.add(flow);}
						if (al.size()>startnum) {
							handle(al);
							al.clear();
							Thread.sleep(10);
					}
				    }
				if(al.size()>0){
					   handle(al);
						al.clear();
					 }
				synchronized(this){
					 wait(120000);
					 }
			} catch (Exception e) {
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				logger.error("[reqest处理错误] " +e.getMessage() + " [队列:" + daoJuqueue.elementNum() + "] ",e);
			}
			}else{
				 synchronized(this){
					 try {
						wait(60000);
					} catch (InterruptedException e) {
						logger.error("[reqest处理错误] " +e.getMessage() + " [队列:" + daoJuqueue.elementNum() + "] ",e);
					}
					 }
			
				
			}
		}
		if(logger.isDebugEnabled()){
		logger.debug("[reqest处理完毕] [处理线程退出] [队列:" + daoJuqueue.elementNum() + "]");
	     }
	}

	public DaoJuManagerImpl getDaoJuManager() {
		return daoJuManager;
	}

	public void setDaoJuManager(DaoJuManagerImpl daoJuManager) {
		this.daoJuManager = daoJuManager;
	}

	public AdvancedFilePersistentQueue getDaoJuqueue() {
		return daoJuqueue;
	}

	public void setDaoJuqueue(AdvancedFilePersistentQueue daoJuqueue) {
		this.daoJuqueue = daoJuqueue;
	}

	public UserManagerImpl getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManagerImpl userManager) {
		this.userManager = userManager;
	}
	
	
}