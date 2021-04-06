package com.fy.boss.gm.newfeedback.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.fy.boss.gm.XmlServer;
import com.fy.boss.gm.XmlServerManager;
import com.fy.boss.gm.newfeedback.FeedbackPlayerState;
import com.fy.boss.gm.newfeedback.FeedbackState;
import com.fy.boss.gm.newfeedback.GmTalk;
import com.fy.boss.gm.newfeedback.NewFeedback;
import com.fy.boss.gm.newfeedback.service.NewFeedbackQueueManager;
import com.fy.boss.gm.newfeedback.service.NewFeedbackStateManager;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

/**
 * 消息队列管理器
 * @author wtx
 *
 */
public class NewFeedbackQueueManager implements Runnable{

	public static Logger logger = Logger.getLogger(NewFeedbackQueueManager.class);
	
	private static NewFeedbackQueueManager self;
	
	public List<NewFeedback> queue;
	
	public List<NewFeedback> vipqueue;
	
	//最大排队数
	public int maxQueueNum = 50000;//150
	
	public int maxVipQueueNum = 10000;//50
	
	//处理能力
	public int gmNum;
	
	//客服电话
	public String gmTelephone = "400-086-0066";
	
	//客户端最大显示反馈条数
	public int maxLimmitShow = 4; 
	
	public SimpleEntityManager<NewFeedback> sem;
	
	public SimpleEntityManager<GmTalk> semtalk;
	
	/**
	 * 需要恢复包含挂起的问题，正在处理的问题
	 */
	public List<NewFeedback> fbCache = new LinkedList<NewFeedback>();
	public List<GmTalk> talkCache = new LinkedList<GmTalk>();
	
	
	/**
	 * 如果一个反馈被GM领取到，记录该反馈id，后面的对话中，是该id下的对话都会加入到GM临时处理缓存中
	 * 如果GM处理完毕，或者玩家点删除反馈，清空id，清空缓存。
	 * @return
	 */
	public List<Long> recordIds = new ArrayList<Long>();
	
	public List<GmTalk> talks = new ArrayList<GmTalk>();
	
	//玩家删除的列表
	public List<Long> dels = new ArrayList<Long>();
	
	/**
	 * 用于设置每天的GM班次
	 * @return
	 */
	private DefaultDiskCache ddc;
	
	/**
	 * 用于记录每天的GM编号
	 */
	private DefaultDiskCache gmddc;
	
	//是否强制评分
	public boolean isDeleteScore;
	
	public boolean isDeleteScore() {
		return isDeleteScore;
	}

	public void setDeleteScore(boolean isDeleteScore) {
		this.isDeleteScore = isDeleteScore;
	}

	public List<Long> getDels() {
		return dels;
	}

	public void setDels(List<Long> dels) {
		this.dels = dels;
	}

	public static NewFeedbackQueueManager getInstance(){
		return self;
	}
	
	public File dataFile;
	
	public File gmdataFile;
	
	/**
	 * 记录GM的领取过的问题，防止刷记录
	 */
	public List<Long> lingquCache = new ArrayList<Long>();
	
	public String autoClosemess = "亲爱的玩家您好，因您长时间没有回复此邮件，我将结束此次对话，若您还有问题需要咨询，请您再次与我们取得联系。感谢您对《飘渺寻仙曲》的支持与关注，祝您游戏愉快。";
	
	public String autoColsemess_tw = "若您的問題以解決，系統將結束此次對話，若您遇到其他遊戲問題，請您再與我方聯繫。感謝您對《飘渺寻仙曲》的支持，祝您遊戲愉快";
	
	public String getAutoClosemess() {
		return autoClosemess;
	}

	public void setAutoClosemess(String autoClosemess) {
		this.autoClosemess = autoClosemess;
	}

	public List<Long> getLingquCache() {
		return lingquCache;
	}

	public void setLingquCache(List<Long> lingquCache) {
		this.lingquCache = lingquCache;
	}

	public File getGmdataFile() {
		return gmdataFile;
	}

	public void setGmdataFile(File gmdataFile) {
		this.gmdataFile = gmdataFile;
	}

	public File getDataFile() {
		return dataFile;
	}

	public void setDataFile(File dataFile) {
		this.dataFile = dataFile;
	}
	
	public DefaultDiskCache getDdc() {
		return ddc;
	}

	public DefaultDiskCache getGmddc() {
		return gmddc;
	}

	public void setGmddc(DefaultDiskCache gmddc) {
		this.gmddc = gmddc;
	}

	public void setDdc(DefaultDiskCache ddc) {
		this.ddc = ddc;
	}

	public void init(){
		long now = System.currentTimeMillis();
		self = this;
		sem = SimpleEntityManagerFactory.getSimpleEntityManager(NewFeedback.class);
		semtalk = SimpleEntityManagerFactory.getSimpleEntityManager(GmTalk.class);
		queue = Collections.synchronizedList(new LinkedList<NewFeedback>());
		queue.addAll(recoverQueue());
		vipqueue = Collections.synchronizedList(new LinkedList<NewFeedback>());
		vipqueue.addAll(recoverVipQueue());
		ddc = new DefaultDiskCache(dataFile, null,"GM班次编号", 100L * 365 * 24 * 3600 * 1000L, false, false);
		gmddc = new DefaultDiskCache(gmdataFile, null,"记录每天的GM编号", 100L * 365 * 24 * 3600 * 1000L, false, false);
		start = true;
		new Thread(this,"NewFeedbackQueueManager").start();
		System.out.println("[NewFeedbackQueueManager] [初始化] [成功] [普通队列消息长度："+queue.size()+"] [VIP队列消息长度："+vipqueue.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms] ");
	}
	 
	public String getGmWorkNumByDay(String key){
		String gms = "";
		long now = System.currentTimeMillis();
		try{
			gms =  (String) gmddc.get(key);
			logger.warn("[获得每天的GM编号] [成功] [key："+key+"] [value："+gms+"] [耗时："+(System.currentTimeMillis()-now)+"]");
		}catch(Exception e){
			e.printStackTrace();
			logger.warn("[获得每天的GM编号] [异常] [key："+key+"] [耗时："+(System.currentTimeMillis()-now)+"]",e);
		}
		return gms;
	}
	
	public boolean recordGmWorkNum(String gmnum){
		long now = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String recordtime = sdf.format(now);
		try{
			String lastgmid = (String) gmddc.get(recordtime);
			boolean isadd = false;
			if(lastgmid!=null){
				String [] ids = lastgmid.split(",");
				for(String id:ids){
					if(gmnum.equals(id)){
						isadd = true;
					}
				}
			}
			if(!isadd){
				String newgmid = "";
				if(lastgmid!=null){
					newgmid = lastgmid+","+gmnum;
				}else{
					newgmid = gmnum;
				}
				
				gmddc.put(recordtime, newgmid);
			}
			logger.warn("[记录每天的GM编号] [成功] [key:"+recordtime+"] [gmnum:"+gmnum+"] [耗时："+(System.currentTimeMillis()-now)+"]");
			return true;
		}catch(Exception e){
			e.printStackTrace();
			logger.warn("[记录每天的GM编号] [异常] [key:"+recordtime+"] [gmnum:"+gmnum+"] [耗时："+(System.currentTimeMillis()-now)+"]",e);
			return false;
		}
	}
	
	public boolean addGmWorkNum(String isvip,String recorder,String[] gmnum){
		long now = System.currentTimeMillis();
		try{
			ddc.remove(isvip);
//			gmss.clear();
			ddc.put(isvip, gmnum);
			logger.warn("[Gm组长添加] [成功] [isvip:"+isvip+"] [组长："+recorder+"] [组员："+Arrays.toString(gmnum)+"] [耗时："+(System.currentTimeMillis()-now)+"]");
			return true;
		}catch(Exception e){
			e.printStackTrace();
			logger.warn("[Gm组长添加] [异常] [isvip:"+isvip+"] [组长："+recorder+"] [组员："+Arrays.toString(gmnum)+"] [耗时："+(System.currentTimeMillis()-now)+"]",e);
			return false;
		}
	}
	
//	List<String> gmss = new ArrayList<String>();
	//判断某个GM编号是否有权去处理问题
	public boolean getGmWorkNum(String isvip,String gmnum){
		long now = System.currentTimeMillis();
		try{
//			if(gmss.contains(gmnum)){
//				return true;
//			}
			String [] gms =  (String[]) ddc.get(isvip);
			if(gms!=null&&gms.length>0){
				for(String gg:gms){
					if(gg.equals(gmnum)){
//						gmss.add(gg);
						logger.warn("[GM获得权限验证] [成功] [vip："+isvip+"] [gmnum:"+gmnum+"] [耗时："+(System.currentTimeMillis()-now)+"]");
						return true;
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.warn("[GM获得权限验证] [异常] [vip："+isvip+"] [耗时："+(System.currentTimeMillis()-now)+"]",e);
		}
		return false;
	}
	
	//获得到当前排队数量
	public synchronized int getCurrentLineNumber(){
		int currQueueNum = queue.size();
		if(currQueueNum>=maxQueueNum){
			return maxQueueNum;
		}
		if(currQueueNum<gmNum){
			return 1;
		}else{
			return currQueueNum - gmNum;
		}
	}
	
	private List<NewFeedback> recoverVipQueue(){
		long now = System.currentTimeMillis();
		List<NewFeedback> list = new LinkedList<NewFeedback>();
		try {
			list = sem.query(NewFeedback.class, " isInQueue = 1 AND viplevel >= 5", "sendtime asc", 1, 3000);
			for(int i=0;i<list.size();i++){
				if(list.get(i).getGmHandler()!=null||list.get(i).getGmHandler().trim().length()>0){
					list.remove(i);
				}
			}
			logger.warn("[获得重启后vip队列情况] [成功] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[获得重启后vip队列情况] [成功] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		return list;
	}
	
	private List<NewFeedback> recoverNowHandles(String gmnum){
		long now = System.currentTimeMillis();
		List<NewFeedback> list = new LinkedList<NewFeedback>();
		try {
			list = sem.query(NewFeedback.class, " isHandleNow = 1 AND gmHandler = '"+gmnum+"'", "sendtime desc", 1, 3000);
			logger.warn("[获得重启后正在处理的消息] [成功] [gmnum:"+gmnum+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[获得重启后正在处理的消息] [异常] [gmnum:"+gmnum+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		return list;
	}
	
	public List<NewFeedback> getFeedbackByUserName(String username){
		long now = System.currentTimeMillis();
		List<NewFeedback> list = new LinkedList<NewFeedback>();
		try {
			list = sem.query(NewFeedback.class, " username = '"+username+"'", "sendtime desc", 1, 3000);
			logger.warn("[通过玩家帐号获得记录] [成功] [username:"+username+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[通过玩家帐号获得记录] [异常] [username:"+username+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		return list;
	}
	
	public List<NewFeedback> getFeedbackByPlayername(String playername){
		long now = System.currentTimeMillis();
		List<NewFeedback> list = new LinkedList<NewFeedback>();
		try {
			list = sem.query(NewFeedback.class, " playername = '"+playername+"'", "sendtime desc", 1, 3000);
			logger.warn("[通过玩家角色名获得记录] [成功] [playername:"+playername+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[通过玩家角色名获得记录] [异常] [playername:"+playername+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		return list;
	}
	
	private List<NewFeedback> recoverQueue(){
		long now = System.currentTimeMillis();
		List<NewFeedback> list = new LinkedList<NewFeedback>();
		try {
			list = sem.query(NewFeedback.class, " isInQueue = 1 AND viplevel < 5", "sendtime asc", 1, 3000);
			for(int i=0;i<list.size();i++){
				if(list.get(i).getGmHandler()!=null||list.get(i).getGmHandler().trim().length()>0){
					list.remove(i);
				}
			}
			logger.warn("[获得重启后队列情况] [成功] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[获得重启后队列情况] [成功] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		return list;
	}
	
	private List<NewFeedback> recoverguaqi(String gmnum){
		long now = System.currentTimeMillis();
		List<NewFeedback> list = new LinkedList<NewFeedback>();
		try {
			list = sem.query(NewFeedback.class, " gmstat = 2 AND gmHandler = '"+gmnum+"'", "sendtime desc", 1, 3000);
			logger.warn("[获得挂起问题] [成功] [gmnum:"+gmnum+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[获得挂起问题] [成功] [gmnum:"+gmnum+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		return list;
	}
	
	public synchronized int getWaitNum(NewFeedback fb){
		if(fb.getViplevel()>5){
			if(vipqueue.contains(fb)){
				return vipqueue.indexOf(fb);
			}
		}else{
			if(queue.contains(fb)){
				return queue.indexOf(fb);
			}
		}
		return -1;
	}
	
	public List<NewFeedback> getFBbyState(String playername,String username,int state,int viplevel){ 
		//如果数据库很忙，请加缓存，每个玩家最多显示4条，70000玩家
		
		long now = System.currentTimeMillis();
		List<NewFeedback> list = new ArrayList<NewFeedback>();
		try {
			list = sem.query(NewFeedback.class, " username = '"+username+"' AND playername = '"+playername+"' AND state = "+state, "sendtime asc", 1, 100);
			
			if(fbCache.size()>0){
				for(NewFeedback fb:fbCache){
					//不是GM删除的，不是玩家删除的
					if(fb.getUsername().equals(username) &&fb.getViplevel()==viplevel && fb.getPlayername().equals(playername) && fb.getGmstat()!=1 &&fb.getState()!=0){
						if(!list.contains(fb)){
							list.add(fb);
						}
					}
				}
			}
			
			if(dels.size()>0){
				List<Long> listdels = new ArrayList<Long>();
				List<NewFeedback> listdel = new ArrayList<NewFeedback>();
					for(NewFeedback fb:list){
						for(long id:dels){
							if(fb.getId()==id){
								listdel.add(fb);
								listdels.add(id);
							}
						}
					}
				list.removeAll(listdel);	
				dels.removeAll(listdels);
			}
			
			for(NewFeedback fb : list){
				if(fb.getTitle()==null){
					fb.setTitle("--");
				}
				if(fb.getIsInQueue()==0){
					fb.setWaitCount(0);
				}else{
					fb.setWaitCount(getWaitNum(fb)+1);
				}
				NewFeedbackQueueManager.getInstance().sem.notifyFieldChange(fb, "waitCount");
				
			}
			logger.warn("[通过用户名和状态获得反馈] [成功] [用户名："+username+"] [talkCache.size():"+talkCache.size()+"] [fbCache.size():"+fbCache.size()+"] [状态："+state+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[通过用户名和状态获得反馈] [异常] [用户名："+username+"] [talkCache.size():"+talkCache.size()+"] [fbCache.size():"+fbCache.size()+"] [状态："+state+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		return list;
	}
	
	public synchronized boolean commitNewFeedback(long viplevel){
		if(viplevel>5){
			if(vipqueue.size()<maxVipQueueNum){
				return true;
			}
		}else{
			if(queue.size()<maxQueueNum){
				return true;
			}
		}
		return false;
	}
	
	public long addNewFeedback(NewFeedback fb){
		long now = System.currentTimeMillis();
		try {
			long id = sem.nextId();
			fb.setId(id);
			fbCache.add(fb);
			sem.save(fb);
			logger.warn("[提交新反馈-保存] [成功] [帐号："+fb.getUsername()+"] [角色名："+fb.getPlayername()+"] [vip："+fb.getViplevel()+"] [提交时间："+fb.getSendtime()+"] [服务器："+fb.getServername()+"] [fbCachesize:"+fbCache.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
			return id;
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[提交新反馈-保存] [异常] [帐号："+fb.getUsername()+"] [角色名："+fb.getPlayername()+"]  [vip："+fb.getViplevel()+"] [提交时间："+fb.getSendtime()+"] [服务器："+fb.getServername()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
			//***********删除数据库中该记录**********
			return 0;
		}
	}
	
	public boolean addGmTalk(GmTalk gt){
		long now = System.currentTimeMillis();
		try {
			if(gt.getName().contains("GM")){
				//
				NewFeedbackStateManager statemanager = NewFeedbackStateManager.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String recordid = sdf.format(new Date());
				if(NewFeedbackStateManager.getInstance().isaddNewState(recordid, gt.getName())){
					FeedbackState stat = new FeedbackState();
					stat.setTalknum(1);
					stat.setFbid(gt.getFeedbackid());
					stat.setStateid(recordid);
					stat.setGmnum(gt.getName());
					statemanager.addNewState(stat);
				}else{
					List<FeedbackState> states = statemanager.getStates();
					for(FeedbackState pp:states){
						if(recordid.equals(pp.getStateid()) && gt.getName().equals(pp.getGmnum())){
							pp.setTalknum(pp.getTalknum()+1);
						}
					}
				}
				//
				NewFeedback fb = getNewFeedbackById(gt.getFeedbackid());
				fb.setPlayerState(1);
				fb.setLastReplyTime(now);
				fb.setGmHandler(gt.getName());
				NewFeedbackQueueManager.getInstance().sem.notifyFieldChange(fb, "lastReplyTime");
				NewFeedbackQueueManager.getInstance().sem.notifyFieldChange(fb, "playerState");
			}else{
				NewFeedback fb = getNewFeedbackById(gt.getFeedbackid());
				fb.setPlayerState(0);
				NewFeedbackQueueManager.getInstance().sem.notifyFieldChange(fb, "playerState");
			}
			long id = semtalk.nextId();
			gt.setId(id);
			semtalk.save(gt);
			logger.warn("[玩家说话-保存] [成功] [角色名："+gt.getName()+"] [发送时间："+gt.getSendDate()+"] [talkCachesize:"+talkCache.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[玩家说话-保存] [异常] [角色名："+gt.getName()+"] [发送时间："+gt.getSendDate()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
			//***********删除数据库中该记录**********
			return false;
		}
	}
	
	public long playerTalk(long id,GmTalk talk,int viplevel){
		long now = System.currentTimeMillis();
		try{
			
			NewFeedback	fb = getNewFeedbackById(id);
			if(fb.getPlayerState()==2||fb.getPlayerState()==3){
				return 1000;
			}
			if(fb!=null){
				talk.setSendDate(now);
				talk.setFeedbackid(id);
				NewFeedbackQueueManager.getInstance().semtalk.notifyFieldChange(talk, "sendDate");
				NewFeedbackQueueManager.getInstance().semtalk.notifyFieldChange(talk, "feedbackid");
				if(addGmTalk(talk)){
					if(fbCache.size()>0){
						if(fbCache.contains(fb)){
							for(int i=0;i<fbCache.size();i++){
								if(fb.getId()==fbCache.get(i).getId()){
									fbCache.get(i).setPlayerState(0);
								}
							}
						}
					}
					
					talkCache.add(talk);
					fb.setState(1);
					NewFeedbackQueueManager.getInstance().sem.notifyFieldChange(fb, "state");
					//是不是在队列中
					if(talk.getTalkcontent()!=null||!"".equals(talk.getTalkcontent().trim())){
						if(talk.getTalkcontent().length()>15){
							fb.setTitle(talk.getTalkcontent().substring(0, 15));
						}else{
							fb.setTitle(talk.getTalkcontent());
						}
					}else{
						talk.setTalkcontent("你好");
						fb.setTitle("您好,请查询");
						NewFeedbackQueueManager.getInstance().semtalk.notifyFieldChange(talk, "talkcontent");
					}
					NewFeedbackQueueManager.getInstance().sem.notifyFieldChange(fb, "title");
					if(fb.getIsInQueue()==0){
						//玩家提交了一条成功的问题
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						String recordid = sdf.format(new Date());
						NewFeedbackStateManager statemanager = NewFeedbackStateManager.getInstance();
						if(statemanager.isAddNewPlayerState(recordid)){
							FeedbackPlayerState pstate = new FeedbackPlayerState();
							pstate.setCommitnums(1);
							pstate.setTalknums(1);
							pstate.setRecordtime(System.currentTimeMillis());
							pstate.setRecordid(recordid);
							statemanager.addPlayerState(pstate);
						}else{
							List<FeedbackPlayerState> pstate = statemanager.getPstate();
							for(FeedbackPlayerState pp:pstate){
								if(recordid.equals(pp.getRecordid())){
									pp.setCommitnums(pp.getCommitnums()+1);
									pp.setTalknums(pp.getTalknums()+1);
								}
							}
						}
						//
						fb.setIsInQueue(1);
						NewFeedbackQueueManager.getInstance().sem.notifyFieldChange(fb, "isInQueue");
						if(viplevel>5){
							vipqueue.add(fb);
						}else{
							queue.add(fb);
						}
						logger.warn("[加入队列] [角色名："+talk.getName()+"] [加入队列的反馈id："+id+"] [vip队列数："+vipqueue.size()+"] [普通玩家队列数："+queue.size()+"]");
					}
					
					if(recordIds.contains(id)){
						talks.add(talk);
					}
//					NewFeedbackQueueManager.getInstance().sem.flush(fb);
					return talk.getId();
				}else{
					logger.warn("[存储对话] [失败] [回归状态] []");
				}
			}
			logger.warn("[玩家说话处理] [失败] [反馈id："+id+"] [标识:IsAddQueue:"+fb.getIsInQueue()+"] [角色名："+talk.getName()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		}catch(Exception e){
			e.printStackTrace();
			logger.warn("[玩家说话处理] [异常] [反馈id："+id+"] [角色名："+talk.getName()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		return 0;
		
	}
	
	public NewFeedback getNewFeedbackById(long id){
		long now = System.currentTimeMillis();
		if(fbCache.size()>0){
			for(NewFeedback fb:fbCache){
				if(fb.getId()==id){
					return fb;
				}
			}	
		}
		
		List<NewFeedback> list = new ArrayList<NewFeedback>();
		try {
			list = sem.query(NewFeedback.class, " id = "+id, "sendtime desc", 1, 10);
			logger.warn("[通过id获得反馈] [成功] [id："+id+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[通过id获得反馈] [异常] [id："+id+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		if(list.size()>0){
			if(!fbCache.contains(list.get(0))){
				fbCache.add(list.get(0));
			}
			return list.get(0);
		}
		return null;
	}
	
	public GmTalk[] getGmTalkById(long id){
		long now = System.currentTimeMillis();
		List<GmTalk> list = new ArrayList<GmTalk>();
		try {
			list = semtalk.query(GmTalk.class, " feedbackid = "+id, "sendDate asc", 1, 500);
			for(GmTalk talk:list){
				if(talk.getTalkcontent()==null){
					talk.setTalkcontent("--");
				}
			}
			logger.warn("[通过id获得一个对话] [成功] [id："+id+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[通过id获得一个对话] [异常] [id："+id+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		return list.toArray(new GmTalk[]{});
	}
	
	public boolean deleteNewFeedback(long id){
		long now = System.currentTimeMillis();
		try{
			NewFeedback fb = getNewFeedbackById(id);
			if(fb!=null){
				fb.setState(0);
				NewFeedbackQueueManager.getInstance().sem.notifyFieldChange(fb, "state");
//				if(fb.getIsInQueue()==1){
					fb.setIsInQueue(0);
					NewFeedbackQueueManager.getInstance().sem.notifyFieldChange(fb, "isInQueue");
//				}
				removeQueueIndex(fb);
				if(fb.getIsHandleNow()==1){
					fb.setIsHandleNow(0);
					NewFeedbackQueueManager.getInstance().sem.notifyFieldChange(fb, "isHandleNow");
				}
				
				fb.setGmstat(1);
				NewFeedbackQueueManager.getInstance().sem.notifyFieldChange(fb, "gmstat");
				removeForJsp(id);
				
				if(fbCache.size()>0){
					if(fbCache.contains(fb)){
						fbCache.remove(fb);
					}
				}
			
				List<GmTalk> deltalkcs = new ArrayList<GmTalk>();
				if(talkCache.size()>0){
					for(GmTalk tt:talkCache){
						if(tt.getFeedbackid()==id){
							deltalkcs.add(tt);
						}
					}
					if(deltalkcs.size()>0){
						talkCache.removeAll(deltalkcs);
					}
				}
				if(!dels.contains(fb)){
					dels.add(id);
				}
				//
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String recordid = sdf.format(new Date());
				NewFeedbackStateManager statemanager = NewFeedbackStateManager.getInstance();
				if(statemanager.isAddNewPlayerState(recordid)){
					FeedbackPlayerState pstate = new FeedbackPlayerState();
					pstate.setDeletenums(1);
					pstate.setRecordtime(System.currentTimeMillis());
					pstate.setRecordid(recordid);
					statemanager.addPlayerState(pstate);
				}else{
					List<FeedbackPlayerState> pstate = statemanager.getPstate();
					for(FeedbackPlayerState pp:pstate){
						if(recordid.equals(pp.getRecordid())){
							pp.setDeletenums(pp.getDeletenums()+1);
						}
					}
				}
				//
				
				//处理强制评分
				if(!isDeleteScore){
					if(fb.getPlayerState()==2){
						long fbid = fb.getId();
						String pname = fb.getPlayername();
						String gmhandler = fb.getGmHandler();
						XmlServerManager xsm = XmlServerManager.getInstance();
						List<XmlServer> xs =  xsm.getServers();
						String mess = "";
						if(gmhandler!=null){
							mess = "感谢您使用GM反馈系统，请对为您服务的【"+fb.getGmHandler()+"】进行评价！";
						}else{
							mess = "感谢您使用GM反馈系统，请对为您服务的GM进行评价！";
						}
						
						String args = "fbid="+fbid+"&pname="+pname+"&mess="+mess+"&authorize.username=lvfei&authorize.password=lvfei321";
						String serverIps = "";	
						if(xs!=null && xs.size()>0){
							for(XmlServer server : xs){
								if(fb.getServername()!=null && server.getDescription().equals(fb.getServername())){
									serverIps = server.getUri();
								}
							}
						}
						
						try {
							byte[] b = HttpUtils.webPost(new URL(serverIps+"/playerScore.jsp"), args.getBytes(), new HashMap(), 60000, 60000);
						} catch (MalformedURLException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}	
				
				logger.warn("[玩家删除一条反馈] [成功] [dels:"+dels.size()+"] [IsInQueue:"+fb.getIsInQueue()+"] [talks:"+talks.size()+"] [recordIds:"+recordIds.size()+"] [talkCachesize:"+talkCache.size()+"] [fbCachesize:"+fbCache.size()+"] [IsHandleNow:"+fb.getIsHandleNow()+"] [vipqueuesize:"+vipqueue.size()+"] [queuesize:"+queue.size()+"] [反馈id："+id+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.warn("[玩家删除一条反馈] [失败] [反馈id："+id+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		return false;
	}
	
	public boolean scoreNewFeedback(long id , int select){
		long now = System.currentTimeMillis();
		NewFeedback fb = getNewFeedbackById(id);
		if(fb!=null){
			fb.setScorestate(select);
			fb.setPlayerState(3);
			NewFeedbackQueueManager.getInstance().sem.notifyFieldChange(fb, "playerState");
			NewFeedbackQueueManager.getInstance().sem.notifyFieldChange(fb, "scorestate");
			
			//
			NewFeedbackStateManager statemanager = NewFeedbackStateManager.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String recordid = sdf.format(new Date());
			if(NewFeedbackStateManager.getInstance().isaddNewState(recordid, fb.getGmHandler())){
				FeedbackState stat = new FeedbackState();
				if(select==1){
					stat.setManyi(1);
				}else if(select==2){
					stat.setYiban(1);
				}else if(select==3){
					stat.setBumanyi(1);
				}
				stat.setStateid(recordid);
				stat.setGmnum(fb.getGmHandler());
				stat.setFbid(id);
				statemanager.addNewState(stat);
			}else{
				List<FeedbackState> states = statemanager.getStates();
				for(FeedbackState pp:states){
					if(recordid.equals(pp.getStateid()) && fb.getGmHandler()!=null && fb.getGmHandler().equals(pp.getGmnum())){
						if(select==1){
							pp.setManyi(pp.getManyi()+1);
						}else if(select==2){
							pp.setYiban(pp.getYiban()+1);
						}else if(select==3){
							pp.setBumanyi(pp.getBumanyi()+1);
						}
					}
				}
			}
			//
			
			if(statemanager.isAddNewPlayerState(recordid)){
				FeedbackPlayerState pstate = new FeedbackPlayerState();
				if(select==1){
					pstate.setManyinums(1);
				}else if(select==2){
					pstate.setYibannums(1);
				}else if(select==3){
					pstate.setBumanyinums(1);
				}
				pstate.setRecordtime(System.currentTimeMillis());
				pstate.setRecordid(recordid);
				statemanager.addPlayerState(pstate);
			}else{
				List<FeedbackPlayerState> pstate = statemanager.getPstate();
				for(FeedbackPlayerState pp:pstate){
					if(recordid.equals(pp.getRecordid())){
						if(select==1){
							pp.setManyinums(pp.getManyinums()+1);
						}else if(select==2){
							pp.setYibannums(pp.getYibannums()+1);
						}else if(select==3){
							pp.setBumanyinums(pp.getBumanyinums()+1);
						}
					}
				}
			}
			//
			logger.warn("[玩家评分] [成功] [反馈id："+id+"] [选项："+select+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
			return true;
		}
		logger.warn("[玩家评分] [失败] [反馈id："+id+"] [选项："+select+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		return false;
	}
	
	public List<GmTalk> jspGetTalks(){
		return talks;
	}
	
	public void removeForJsp(long id){
		if(recordIds.contains(id)){
			recordIds.remove(id);
			List<GmTalk> list = new ArrayList<GmTalk>();
			if(talks.size()>0){
				for(GmTalk talk:talks){
					if(talk.getFeedbackid()==id){
						list.add(talk);
					}
				}
				if(list.size()>0){
					talks.removeAll(list);
				}
			}
		}
	}
	
	public void removeQueueIndex(NewFeedback fb){
		long viplevel = fb.getViplevel();
		if(viplevel>5){
			if(vipqueue.contains(fb)){
				vipqueue.remove(fb);
			}
		}else{
			if(queue.contains(fb)){
				queue.remove(fb);
			}
		}
	}
	
	public void setRecordid(long id){
		if(recordIds!=null&&!recordIds.contains(id)){
			recordIds.add(id);
			GmTalk [] tts = getGmTalkById(id);
			talks.addAll(Arrays.asList(tts));
			logger.warn("[GM获取问题成功] [id:"+id+"] [recordids:"+recordIds.size()+"] [talks:"+talks.size()+"]");
		}
		
	}
	
	public void closeNewFeedback(String gmnum,String id){
		logger.warn("[关闭反馈] [--] [gmnum:"+gmnum+"] [id:"+id+"]");
		try {
			NewFeedback fb = getNewFeedbackById(Long.parseLong(id));
			removeQueueIndex(fb);
			fb.setGmHandler(gmnum);
			fb.setGmstat(1);
			
			fb.setIsHandleNow(0);
			fb.setIsInQueue(0);
			fb.setPlayerState(2);
			NewFeedbackQueueManager.getInstance().sem.notifyFieldChange(fb, "gmstat");
			NewFeedbackQueueManager.getInstance().sem.notifyFieldChange(fb, "isHandleNow");
			NewFeedbackQueueManager.getInstance().sem.notifyFieldChange(fb, "isInQueue");
			NewFeedbackQueueManager.getInstance().sem.notifyFieldChange(fb, "playerState");
			NewFeedbackQueueManager.getInstance().sem.notifyFieldChange(fb, "gmHandler");
			NewFeedbackQueueManager.getInstance().sem.notifyFieldChange(fb, "playerState");
		
		} catch (Exception e) {
			logger.warn("[关闭反馈] [异常] [gmnum:"+gmnum+"] [id:"+id+"]",e);
			e.printStackTrace();
		}
	}
	
	//获得挂起的并且有玩家回复的反馈
	public NewFeedback getGuaQiFeedback(String gmnum){
		List<NewFeedback> list = new ArrayList<NewFeedback>();
		list = recoverNowHandles(gmnum);
		if(list.size()>0){
			for(int i=0;i<list.size();i++){
				if(list.get(i).getGmHandler()!=null && list.get(i).getGmHandler().equals(gmnum)&&list.get(i).getIsHandleNow()==1){
					if(list.get(i).getGmstat()!=1&& !fbCache.contains(list.get(i))){
						fbCache.add(list.get(i));
					}
//					if(list.get(i).getGmstat()!=1){
						return list.get(i);
//					}
				}
			}
		}
		list = recoverguaqi(gmnum);
		if(list.size()>0){
			for(int i=0;i<list.size();i++){
				if(list.get(i).getGmHandler()!=null && list.get(i).getGmHandler().equals(gmnum) && list.get(i).getPlayerState()==0){
					if(!fbCache.contains(list.get(i))){
						fbCache.add(list.get(i));
					}
//					if(list.get(i).getGmstat()!=1){
						return list.get(i);
//					}
				}
			}
		}
		return null;
	}
	
	public List<GmTalk> getGmTalkByFId(long id){
		List<GmTalk> list = new ArrayList<GmTalk>();
		for(GmTalk talk:talks){
			if(talk.getFeedbackid()==id){
				list.add(talk);
			}
		}
		return list;
	}
	
	public List<NewFeedback> getFeedbacksByHandler(String handler,long starttime , long endtime){
		long now = System.currentTimeMillis();
		List<NewFeedback> list = new ArrayList<NewFeedback>();
		try {
			list = sem.query(NewFeedback.class, " gmHandler = '"+handler+"' AND lastReplyTime >= "+starttime+" AND lastReplyTime < "+endtime, "sendtime asc", 1, 5000);
			logger.warn("[通过GM处理人获得反馈] [成功] [处理人："+handler+"] [time1:"+(System.currentTimeMillis()-starttime)+"] [time2:"+(endtime-System.currentTimeMillis())+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[通过GM处理人获得反馈] [异常] [处理人："+handler+"] [time1:"+(System.currentTimeMillis()-starttime)+"] [time2:"+(endtime-System.currentTimeMillis())+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		return list;
	}
	
	public List<NewFeedback> getQueue() {
		return queue;
	}

	public void setQueue(List<NewFeedback> queue) {
		this.queue = queue;
	}

	public List<NewFeedback> getVipqueue() {
		return vipqueue;
	}

	public void setVipqueue(List<NewFeedback> vipqueue) {
		this.vipqueue = vipqueue;
	}

	public int getMaxQueueNum() {
		return maxQueueNum;
	}

	public void setMaxQueueNum(int maxQueueNum) {
		this.maxQueueNum = maxQueueNum;
	}

	public int getMaxVipQueueNum() {
		return maxVipQueueNum;
	}

	public void setMaxVipQueueNum(int maxVipQueueNum) {
		this.maxVipQueueNum = maxVipQueueNum;
	}

	public int getGmNum() {
		return gmNum;
	}

	public void setGmNum(int gmNum) {
		this.gmNum = gmNum;
	}

	public String getGmTelephone() {
		return gmTelephone;
	}

	public void setGmTelephone(String gmTelephone) {
		this.gmTelephone = gmTelephone;
	}

	public int getMaxLimmitShow() {
		return maxLimmitShow;
	}

	public void setMaxLimmitShow(int maxLimmitShow) {
		this.maxLimmitShow = maxLimmitShow;
	}
	
	
	public SimpleEntityManager<NewFeedback> getSem() {
		return sem;
	}

	public void setSem(SimpleEntityManager<NewFeedback> sem) {
		this.sem = sem;
	}

	public SimpleEntityManager<GmTalk> getSemtalk() {
		return semtalk;
	}

	public void setSemtalk(SimpleEntityManager<GmTalk> semtalk) {
		this.semtalk = semtalk;
	}

	public List<NewFeedback> getFbCache() {
		return fbCache;
	}

	public void setFbCache(List<NewFeedback> fbCache) {
		this.fbCache = fbCache;
	}

	public List<GmTalk> getTalkCache() {
		return talkCache;
	}

	public void setTalkCache(List<GmTalk> talkCache) {
		this.talkCache = talkCache;
	}

	public List<Long> getRecordIds() {
		return recordIds;
	}

	public void setRecordIds(List<Long> recordIds) {
		this.recordIds = recordIds;
	}
	
	public List<GmTalk> getTalks() {
		return talks;
	}

	public void setTalks(List<GmTalk> talks) {
		this.talks = talks;
	}

	public void destroy(){
		System.out.println("boss web close ,doing....");
		if (sem != null)  {
			sem.destroy();
		}
		if (semtalk != null)  {
			semtalk.destroy();
		}
	}

	public boolean start;
	
	public long systemColseTime = 30*60*1000;
	
	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		this.start = start;
	}

	public long getSystemColseTime() {
		return systemColseTime;
	}

	public void setSystemColseTime(long systemColseTime) {
		this.systemColseTime = systemColseTime;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(60*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while(start){
			try {
				Thread.sleep(60*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
			long now = System.currentTimeMillis();
			if(fbCache!=null && fbCache.size()>0){
				for(int i=0;i<fbCache.size();i++){
					NewFeedback fb = fbCache.get(i);
					if(now-fb.getLastReplyTime()>=systemColseTime && fb.getPlayerState()==1){
						系统关闭(fb);
						if(fbCache.contains(fb)){
							fbCache.remove(fb);
						}
					}
				}
			}
		}
	}
	
	private void 系统关闭(NewFeedback fb){
		long now = System.currentTimeMillis();
		fb.setState(2);
		fb.setGmstat(1);
		fb.setIsHandleNow(0);
		fb.setIsInQueue(0);
		fb.setPlayerState(3);
		fb.setScorestate(0);
		fb.setWaitCount(0);
		NewFeedbackQueueManager.getInstance().sem.notifyFieldChange(fb, "state");
		NewFeedbackQueueManager.getInstance().sem.notifyFieldChange(fb, "gmstat");
		NewFeedbackQueueManager.getInstance().sem.notifyFieldChange(fb, "isHandleNow");
		NewFeedbackQueueManager.getInstance().sem.notifyFieldChange(fb, "isInQueue");
		NewFeedbackQueueManager.getInstance().sem.notifyFieldChange(fb, "playerState");
		NewFeedbackQueueManager.getInstance().sem.notifyFieldChange(fb, "scorestate");
		NewFeedbackQueueManager.getInstance().sem.notifyFieldChange(fb, "waitCount");
		GmTalk talk = new GmTalk();
		talk.setFeedbackid(fb.getId());
		talk.setName(fb.getGmHandler());
		talk.setTalkcontent(autoClosemess);
		talk.setSendDate(System.currentTimeMillis());
		try {
			long id = semtalk.nextId();
			talk.setId(id);
			semtalk.save(talk);
			logger.warn("[系统删除] [成功] [state:"+fb.getState()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.warn("[系统删除] [异常] [state:"+fb.getState()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		/**
		 * 客服回复了玩家，玩家半个小时没有处理，这种情况没必要及时的通知玩家，等玩家查看的时候能看到就行，
		 * 这样就省去了从BOSS到server通信
		 */
//		XmlServerManager xsm = XmlServerManager.getInstance();
//		List<XmlServer> xs =  xsm.getServers();
//		String mess = "亲爱的玩家您好，因您长时间没有回复此邮件，我将结束此次对话，若您还有问题需要咨询，请您再次与我们取得联系。感谢您对《飘渺寻仙曲》的支持与关注，祝您游戏愉快。";
//		String args = "playername="+fb.getPlayername()+"&gmnum="+fb.getGmHandler()+"&feedbackid="+fb.getId()+"&talkmess="+mess+"&authorize.username=lvfei&authorize.password=lvfei321";
//		String serverIps = "";	
//		if(xs!=null && xs.size()>0){
//			for(XmlServer server : xs){
//				if(fb.getServername()!=null && server.getDescription().equals(fb.getServername())){
//					serverIps = server.getUri();
//				}
//			}
//		}
//		
//		try {
//			byte[] b = HttpUtils.webPost(new URL(serverIps+"/getTalks.jsp"), args.getBytes(), new HashMap(), 60000, 60000);
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		logger.warn("[系统删除] [state:"+fb.getState()+"] [gmstate:"+fb.getGmstat()+"] [ishandlenow:"+fb.getIsHandleNow()+"] [isinqueue:"+fb.getIsInQueue()+"] [playerstate:"+fb.getPlayerState()+"]");
	}
	
	
}
