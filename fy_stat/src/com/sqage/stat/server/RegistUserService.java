package com.sqage.stat.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.entity.User;
import com.sqage.stat.commonstat.manager.Impl.UserManagerImpl;
import com.sqage.stat.dao.ChannelDAO;
import com.sqage.stat.message.USEREGIST_LOG_REQ;
import com.sqage.stat.model.Channel;
import com.sqage.stat.model.UserRegistFlow;
import com.xuanzhi.tools.queue.AdvancedFilePersistentQueue;
import com.xuanzhi.tools.transport.RequestMessage;

public class RegistUserService implements Runnable {

	 AdvancedFilePersistentQueue queue=null;
	 static Logger logger = Logger.getLogger(RegistUserService.class);
	 public static int startnum=50;//处理队列中对象的最小数量
	 String shetname=StatServerService.shetname;
	 UserManagerImpl userManager;
	 ChannelDAO channelDAO;
	 static RegistUserService self;
	 //SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
	 public static RegistUserService getInstance() {
		return self;
	  }

	 public void init() throws Exception {
		self = this;
	  }

	 private void handle(List<RequestMessage> ls) {
		for(int i=0;i<ls.size();i++)
			doUserRegist(ls.get(i));
	 }


	@Override
	public void run() {
	    if(queue==null){queue=StatServerService.registUserqueue;}
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
	
	
	private boolean doUserRegist(RequestMessage message) {
		boolean result=false;
		USEREGIST_LOG_REQ req = (USEREGIST_LOG_REQ) message;
		long now = System.currentTimeMillis();
		UserRegistFlow flow = req.getUserRegistFlow();
		if(logger.isDebugEnabled()){
		logger.debug("[用户注册] " + flow.toString() + " [队列:" + queue.elementNum() + "] [" + (System.currentTimeMillis() - now) + "ms]");
		}
		User user = new User();
		user.setDiDian(flow.getDidian());
		user.setImei(flow.getEmei());
		user.setGame(flow.getGame());
		user.setHaoMa(flow.getHaoma());
		user.setUuid(flow.getHaoma());
		user.setJiXing(flow.getJixing());
		user.setName(flow.getUserName());
		String quDao=flow.getQudao();
		
		if(quDao==null||"".equals(quDao)){quDao="无渠道";}
		List channelList=channelDAO.findByKey(quDao);
		Channel channel=null;
		Long quDaoId=null;
//		if(channelList!=null&&channelList.size()>0)
//		{
//			channel=(Channel)channelList.get(0);
//			quDaoId=channel.getId();
//			
//		}else{
//			Channel channel_temp=new Channel();
//			channel_temp.setName(quDao);
//			channel_temp.setKey(quDao);
//			channelDAO.save(channel_temp);
//			channelList=channelDAO.findByKey(quDao);
//			channel=(Channel)channelList.get(0);
//			quDaoId=channel.getId();
//		}
//		
//		//添加子渠道
//		if(channelItemManager.getChannelItem(quDao)==null)
//		{
//			ChannelItem channelItem=new ChannelItem();
//			channelItem.setChannelid(quDaoId);
//			channelItem.setKey(quDao);
//			channelItem.setName(quDao);
//			channelItem.setCmode(0L);
//			channelItem.setPrate(1F);
//			channelItemManager.createChannelItem(channelItem);
//		}
//		user.setQuDaoId(quDaoId.toString());
		user.setQuDao(quDao);
		 long registtime= flow.getRegisttime();
         if(shetname!=null&&shetname.indexOf("韩国")!=-1){registtime+=60*60*1000;}//如果是韩国，时间加一小时，以平衡时区
		java.util.Date date = new java.util.Date(registtime);
		user.setRegistTime(date);
		

		user.setPlayerName(flow.getPlayerName());
		
		//String sql=" select * from stat_user u where u.name=?  value ("+flow.getUserName()+")";

		User returnUser=userManager.add(user);
//		List<User> userList=userManager.getBySql(sql);
//		if(userList==null||userList.size()<=0){
//			
//			User returnUser=userManager.add(user);
//			if(returnUser!=null){
//				result=true;
//			}
//		}else{
//			result=true;
//		}
		return result;
	}

	public AdvancedFilePersistentQueue getQueue() {
		return queue;
	}

	public void setQueue(AdvancedFilePersistentQueue queue) {
		this.queue = queue;
	}

	public static int getStartnum() {
		return startnum;
	}

	public static void setStartnum(int startnum) {
		RegistUserService.startnum = startnum;
	}

	public UserManagerImpl getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManagerImpl userManager) {
		this.userManager = userManager;
	}

	public ChannelDAO getChannelDAO() {
		return channelDAO;
	}

	public void setChannelDAO(ChannelDAO channelDAO) {
		this.channelDAO = channelDAO;
	}



}