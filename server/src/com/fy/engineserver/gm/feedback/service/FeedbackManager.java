package com.fy.engineserver.gm.feedback.service;

import static com.fy.engineserver.datasource.language.Translate.反馈状态关闭;
import static com.fy.engineserver.datasource.language.Translate.反馈状态新;
import static com.fy.engineserver.datasource.language.Translate.反馈状态未处理;
import static com.fy.engineserver.datasource.language.Translate.反馈状态等待;
import static com.fy.engineserver.datasource.language.Translate.反馈类型BUG;
import static com.fy.engineserver.datasource.language.Translate.反馈类型充值;
import static com.fy.engineserver.datasource.language.Translate.反馈类型其他;
import static com.fy.engineserver.datasource.language.Translate.反馈类型建议;
import static com.fy.engineserver.datasource.language.Translate.反馈类型投诉;
import static com.fy.engineserver.datasource.language.Translate.角色已删除;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.gm.feedback.FeedBackState;
import com.fy.engineserver.gm.feedback.Feedback;
import com.fy.engineserver.gm.feedback.GMRecord;
import com.fy.engineserver.gm.feedback.Reply;
import com.fy.engineserver.message.FEEDBACK_NOTICE_CLIENT_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.JUDGE_SPECIAL_FEEDBACK_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.cache.CacheObject;
import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;


public class FeedbackManager implements Runnable{

	public static Logger logger = LoggerFactory.getLogger(FeedbackManager.class.getName());
	
	public static SimpleEntityManager<Feedback> em;
	public static SimpleEntityManager<GMRecord> gmem;
	public static int replyLENGTH = 200;
	public static int subjectLENGTH = 20;
	
	private static FeedbackManager self;
	
	//所有状态是未处理，新，的反馈集合
	public List<Feedback> feedbacks = new Vector<Feedback>();
	
	//所有未显示的状态是未处理，新，的反馈集合
//	public List<Feedback> isshows = new ArrayList<Feedback>();
	
	public void init()throws Exception{
		
		self = this;
		em = SimpleEntityManagerFactory.getSimpleEntityManager(Feedback.class);
		gmem = SimpleEntityManagerFactory.getSimpleEntityManager(GMRecord.class);
		feedbacks = getAllNewFeedback();
		Thread thread = new Thread(this, "反馈心跳");
		thread.start();
		ServiceStartRecord.startLog(this);
	}
	
	public static long 关闭间隔 = 30*60*1000;
	
	public static String 反馈关闭提示 = "如您不需要其他帮助，我将结束此次对话，若您遇到其他游戏问题，请您再与我们取得联系；感谢您对《飘渺寻仙曲》的支持，祝您游戏愉快。";
	
	public static boolean running = true;
	
	public void run() {
		while(running){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			try{
				Object os[] = mCache.values().toArray(new Object[0]);
				long now = SystemTime.currentTimeMillis();
				for(int i=0; i<os.length; i++) {
					try {
						Object o = os[i];
						Feedback feedback = (Feedback)((CacheObject)o).object;
						if(feedback.getGmState() == FeedBackState.等待反馈.state){
							if(now -feedback.getSendTimes() >=  关闭间隔){
								
								feedback.overTimeGmCloseFeedback();
								
								feedback.setPlayerState(FeedBackState.已关闭.state);
								feedback.setGmState(FeedBackState.已关闭.state);
							
								feedbacks.remove(feedback);
							}
						}
					} catch (Exception e) {
						logger.error("[反馈心跳异常]",e);
					}
				}
			}catch (Throwable t) {
				logger.error("[反馈心跳异常]",t);
			}
		}
	}
	
	public static FeedbackManager getInstance(){
		return self;
	}
	public LruMapCache mCache = new LruMapCache(32*1024*1024, 60*60*1000);
	public LruMapCache gmRecordCache = new LruMapCache(32*1024*1024, 60*60*1000);
	
	/**
	 * 虚拟机关闭时调用
	 */
	public void destroy() {
		em.destroy();
		gmem.destroy();
	}
	
	public List<Feedback> getAllNewFeedback(){
		List<Feedback> list = new ArrayList<Feedback>();
		long now =  System.currentTimeMillis();
		String[] states = {"0","2"};
		String[] types = {"0","1","2","3","4"};
		SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
		String endTime = sdf.format(SystemTime.currentTimeMillis() + 24*60*60*1000);
		try {
			list = self.queryFeedbacks("2012-06-11", endTime, types, states, "全部", 0);
			logger.warn("[初始化] [获得未处理和新的反馈] [成功] [listsize："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[初始化] [获得未处理和新的反馈] [异常]",e);
		}
		return list;
	}
	
	
	public long[] getPlayerFeedBackIds(Player player){
		
		if(player.getFeedbackIds() == null){
			long[] ids = new long[0];
			try {
				ids = em.queryIds(Feedback.class, "playerId = ?", new Object[]{player.getId()},"beginDate asc ",1,5000);
			} catch (Exception e) {
				FeedbackManager.logger.error("[查看个人的反馈记录异常] ["+player.getLogString()+"]",e);
			}
			player.setFeedbackIds(ids);
		}
		
		return player.getFeedbackIds();
	}
	
	public List<Feedback> getFeedbacksByGameId(String gmid,String begintime,String endtime){
		long now = System.currentTimeMillis();
		List<Feedback> list = new ArrayList<Feedback>();
		try {
			list = em.query(Feedback.class, " sendTimestr >= '"+begintime+"' AND sendTimestr <= '"+endtime+"' AND lastGmId = '"+gmid+"' AND GmState = 3", "sendTimes desc", 1, 5000);
			FeedbackManager.logger.warn("[通过GM编号获得反馈] [成功] [GM编号:"+gmid+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"]ms");
		} catch (Exception e) {
			e.printStackTrace();
			FeedbackManager.logger.warn("[通过GM编号获得反馈] [异常] [GM编号:"+gmid+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"]ms",e);
		}
		return list;
	}
	
	
	public boolean createGM(String name){
		long id = 0l;
		try {
			id = gmem.nextId();
		} catch (Exception e1) {
			FeedbackManager.logger.warn("[创建gm记录异常] [记录name:"+name+"] []",e1);
		}
		GMRecord gr = new GMRecord(id);
		gr.setGmName(name);
		try {
			this.gmem.save(gr);
			FeedbackManager.logger.warn("[创建gm记录成功] [记录name:"+name+"] []");
		} catch (Exception e) {
			FeedbackManager.logger.warn("[创建gm记录异常] [记录name:"+name+"] []",e);
		}
		return true;
	}
	
	// 玩家创建一个反馈
	public String createFeedBack(Player player,int type,String subject,String content){
		
		long date = System.currentTimeMillis();
		Feedback feedback = new Feedback();
		
		try {
			feedback.setId(em.nextId());
		} catch (Exception e) {
			logger.error("[生成反馈异常] ",e);
			return "系统错误";
		}
		feedback.setBeginDate(date);
		feedback.setFeedbackType(type);
		
		//新创建gm为未处理状态
		feedback.setGmState(FeedBackState.未处理.state);
		//player为等待处理状态
		feedback.setPlayerState(FeedBackState.等待反馈.state);
		
		feedback.setPlayerId(player.getId());
		
		feedback.setSubject(subject);
		feedback.setVipLevel(player.getVipLevel());
		feedback.setServername(GameConstants.getInstance().getServerName());
		feedback.setUserName(player.getUsername());
		feedback.setPlayerName(player.getName());
		feedback.setContent(content);
		feedback.setCountryname(CountryManager.得到国家名(player.getCountry()));
		feedback.setPlayerlevel(player.getLevel());
		feedback.setCareer(CareerManager.careerNameByType(player.getCareer()));
//		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
//		String st = format1.format(System.currentTimeMillis());
		feedback.setSendTimes(System.currentTimeMillis());
		feedback.setChannel(player.getPassport().getRegisterChannel());
		
		List<Reply> list = new ArrayList<Reply>();
		Reply r = new Reply();
		r.setSendDate(date);
		r.setFcontent(content);
		
		list.add(r);
		feedback.setList(list);

		mCache.put(feedback.getId(), feedback);
		em.notifyNewObject(feedback);
		
		//发布一个新的反馈，添加player的feedbackIds
		long[] ids = null;
		long[] currentFeedBackIds = player.getFeedbackIds();
		if(currentFeedBackIds != null){
			ids = new long[currentFeedBackIds.length+1];
			ids[0] = feedback.getId();
			System.arraycopy(currentFeedBackIds, 0, ids, 1, currentFeedBackIds.length);
		}else{
			ids = new long[1];
			ids[0] = feedback.getId();
		}
		
		player.setFeedbackIds(ids);
		
		feedbacks.add(feedback);
		if(FeedbackManager.logger.isWarnEnabled()){
			FeedbackManager.logger.warn("[发布新的反馈添加成功] ["+player.getLogString()+"] [feedbackssize:"+feedbacks.size()+"] [feedbackid:"+feedback.getId()+"] [viplevel:"+player.getVipLevel()+"] ["+ids.length+"]");
		}
		return "";
	}
	
	// 玩家回复一个反馈
//	public String playerAnserFeedBack(Player player ,long id,String content){
//		long date = System.currentTimeMillis();
//		Feedback fb = getFeedBack(id);
//		if(fb != null){
//			if(fb.getPlayerId() == player.getId()){
//				
//				if(content.equals("")){
//					return Translate.玩家反馈为空;
//				}
//				if(fb.getPlayerState() == FeedBackState.已关闭.state){
//					return Translate.反馈已关闭;
//				}
//				Reply r = new Reply();
//				r.setSendDate(SystemTime.currentTimeMillis());
//				fb.setSubject(fb.getSubject());
//				fb.setVipLevel(player.getVipLevel());
//				fb.setServername(GameConstants.getInstance().getServerName());
//				fb.setUserName(player.getUsername());
//				fb.setPlayerName(player.getName());
//				fb.setContent(content);
////				SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
////				String st = format1.format(System.currentTimeMillis());
//				fb.setSendTimes(System.currentTimeMillis());
//				
//				r.setFcontent(content);
//				fb.setBeginDate(date);
//				fb.playerReply(r);
//				
//				//
////				feedbacks.add(feedbacks.indexOf(fb),fb);
//				if(logger.isWarnEnabled())
//					logger.warn("[玩家反馈回复完成] ["+player.getLogString()+"] [反馈id:"+id+"]");
//				return "";
//			}else{
//				logger.error("[玩家反馈回复错误] [不是自己提的反馈] ["+player.getLogString()+"] [反馈id:"+id+"]");
//			}
//		}
//		return null;
//		
//	}
	
	
	//玩家评价一个反馈
	public String judgeFeedBack(Player player ,long id,int judgeScore){
		
		Feedback fb = getFeedBack(id);
		if(fb != null){
			if(fb.getPlayerId() == player.getId()){
				
				if(fb.isSendJudge() && !fb.isAlreadyJudge()){
					
					String gmName = fb.getLastGmId();
					if(gmName != null && gmName.equals("")){
						
						GMRecord gmRecord = getGMRecord(gmName);
						gmRecord.gmScore(id);
						
						fb.setJudge(judgeScore);
						fb.setPlayerState(FeedBackState.等待反馈.state);
						fb.setGmState(FeedBackState.评价.state);
						
						
						if(logger.isWarnEnabled())
							logger.warn("[玩家评价反馈完成] [] ["+player.getLogString()+"] [反馈id:"+id+"]");
						return "";
						
					}else{
						logger.error("[玩家评价反馈错误] [Gm还没有回复] ["+player.getLogString()+"] [反馈id:"+id+"]");
					}
				}else{
					logger.error("[玩家评价反馈错误] [gm请求评价:"+fb.isSendJudge()+"] [玩家已经评价:"+fb.isAlreadyJudge()+"] ["+player.getLogString()+"] [反馈id:"+id+"]");
					return Translate.玩家已经评价;
				}
			}else{
				logger.error("[玩家评价反馈错误] [不是自己提的反馈] ["+player.getLogString()+"] [反馈id:"+id+"]");
				return Translate.不是自己提的反馈;
			}
		}
		return null;
	
	}
	
	//GM回复一个反馈
	public String GmAnserFeedBack(String gmName,long id,String content){
		
		GMRecord gmRecord = getGMRecord(gmName);
		if(gmRecord != null){
			
			Feedback feedback = getFeedBack(id);
			if(feedback != null){
				if(feedback.getGmState() == FeedBackState.已关闭.state){
					return Translate.反馈已关闭;
				}
				if(content.equals("")){
					return Translate.玩家反馈为空;
				}
				
				Reply reply = new Reply();
				reply.setGmName(gmName);
				reply.setFcontent(content);
				
				feedback.gmReply(reply, gmName);
				gmRecord.gmReply(id);
				
				PlayerManager pm = PlayerManager.getInstance();
				if(pm.isOnline(feedback.getPlayerId())){
					Player p;
					try {
						p = pm.getPlayer(feedback.getPlayerId());
						FEEDBACK_NOTICE_CLIENT_RES res1 = new FEEDBACK_NOTICE_CLIENT_RES(GameMessageFactory.nextSequnceNum(), true);
						p.addMessageToRightBag(res1);
						if(FeedbackManager.logger.isInfoEnabled()){
							FeedbackManager.logger.info("[gm 发送一个评价] ["+p.getLogString()+"] []");
						}
					} catch (Exception e) {
						FeedbackManager.logger.error("[gm发送评价异常] ["+gmName+"] ["+id+"]",e);
					}
				}
				if(feedbacks.indexOf(feedback)!=-1){
					feedback.setSendTimes(System.currentTimeMillis());
//					feedbacks.set(feedbacks.indexOf(feedback), feedback);
//					feedbacks.remove(feedback);
				}else{
					logger.warn("[Gm回复反馈完成] [替换消息失败]");
				}
				
				logger.warn("[Gm回复反馈完成] ["+gmName+"] ["+feedback.getLogString()+"] [feedbackid:"+feedback.getId()+"] ["+content+"]");
				return "yes";
			}
			
		}else{
			logger.error("[GM回复反馈错误] [没有这个gm] ["+gmName+"]");
		}
		
		return null;
	}
	
	//GM发送一个评价
	public String sendJudge(long id,String gmName){
		
		Feedback feedback = getFeedBack(id);
		if(feedback != null){
			
			String result = feedback.canReply();
			if(result.equals("")){
				
				PlayerManager pm = PlayerManager.getInstance();
				if(pm.isOnline(feedback.getPlayerId())){
					try {
						Player p = pm.getPlayer(feedback.getPlayerId());
						feedback.setSendJudge(true);
						JUDGE_SPECIAL_FEEDBACK_RES res =  new JUDGE_SPECIAL_FEEDBACK_RES(GameMessageFactory.nextSequnceNum(), id);
						p.addMessageToRightBag(res);
						p.send_HINT_REQ(Translate.translateString(Translate.gm请求你对反馈xx作出评价, new String[][]{{Translate.STRING_1,feedback.getSubject()}}));
						
						FEEDBACK_NOTICE_CLIENT_RES res1 = new FEEDBACK_NOTICE_CLIENT_RES(GameMessageFactory.nextSequnceNum(), true);
						p.addMessageToRightBag(res1);
						if(FeedbackManager.logger.isInfoEnabled()){
							FeedbackManager.logger.info("[gm 发送一个评价] ["+p.getLogString()+"] []");
						}
						return "";
					} catch (Exception e) {
						FeedbackManager.logger.error("[gm发送评价异常] ["+gmName+"] ["+id+"]",e);
					}
					
				}else{
					return Translate.玩家不在线不能发送评价;
				}
			}else{
				logger.error("[Gm向玩家发送一个评价请求错误] ["+result+"] ["+gmName+"] [反馈id:"+id+"]");
				return result;
			}
		}else{
			logger.error("[Gm向玩家发送一个评价请求错误] [没有这个反馈] ["+gmName+"] [反馈id:"+id+"]");
			return Translate.没有这个反馈;
		}
		return null;
	}
	
	//GM关闭一个反馈
	public String closeFeedBack(long id,String gmName){
		
		GMRecord gmRecord = getGMRecord(gmName);
		if(gmRecord != null){
			
			Feedback feedback = getFeedBack(id);
			if(feedback != null){
				if(feedback.getGmState() == FeedBackState.已关闭.state){
					return Translate.反馈已关闭;
				}
				
				Reply r = new Reply();
				r.setType(Reply.CLOSE);
				
				feedback.feedBackFinish(r, gmName);
				long playerId = feedback.getPlayerId();
				if(PlayerManager.getInstance().isOnline(playerId)){
					Player player;
					try {
						player = PlayerManager.getInstance().getPlayer(playerId);
						enterGameFeedbackNotice(player);
					} catch (Exception e) {
						logger.error("[GM关闭反馈错误] [没有这个player] [id:"+playerId+"]",e);
					}
					
				}
				feedbacks.remove(feedback);
				if(logger.isWarnEnabled())
					logger.warn("[Gm关闭反馈完成] ["+gmName+"] ["+feedback.getLogString()+"]");
				return "closesucc";
			}
			
		}else{
			logger.error("[GM关闭反馈错误] [没有这个gm] ["+gmName+"]");
		}
		
		return null;
	
	}
	
	//GM查询
	public Feedback getFeedBack(long id){
		
		Feedback feedback = (Feedback)mCache.get(id);
		if(feedback == null) {
			try {
				feedback = em.find(id);
			} catch (Exception e) {
				logger.error("[取得反馈实体]",e);
			}
			if(feedback != null) {
				mCache.put(id, feedback);
			}else{
				logger.error("[通过id获得一个反馈] [不存在] [反馈id:"+id+"]");
			}
		}
		return feedback;
	}
	
	public GMRecord getGMRecord(String gmName){
		
		GMRecord gr = null;
		Collection collection = gmRecordCache.values();
		Iterator it = collection.iterator();
		while(it.hasNext()){
			gr = (GMRecord)(((CacheObject)it.next()).object);
			if(gr.getGmName().equals(gmName)){
				break;
			}else{
				gr = null;
			}
		}
		if(gr != null){
			return gr;
		}else{
			List<GMRecord> list = null;
			try {
				list = gmem.query(GMRecord.class, "gmName = ?", new Object[]{gmName}, null, 1, 10);
			} catch (Exception e) {
				logger.error("[取得gm实体根据name] ["+gmName+"]",e);
			}
			if(list != null && list.size() > 0){
				gr = list.get(0);
			}
		}
		
		if(gr == null){
			long id = 1l;
			try {
				id = gmem.nextId();
			} catch (Exception e1) {
				FeedbackManager.logger.info("[createGm] [生成id]",e1);
			}
			try {
				gr = new GMRecord(id);
				gr.setId(gmem.nextId());
				gr.setGmName(gmName);
				gmem.save(gr);
				//新建一个gm
				logger.error("[新建一个gm反馈记录] ["+gr.getLogString()+"]");
				gmRecordCache.put(gr.getId(), gr);
			} catch (Exception e) {
				logger.error("[新建一个gm反馈记录异常]",e);
			}
		}
		
		return gr;
	}
	
	
	public GMRecord getGMRecordById(long id){
		
		GMRecord gr = null;
		gr = (GMRecord)gmRecordCache.get(id);
		if(gr == null){
			try {
				gr = gmem.find(id);
			} catch (Exception e) {
				logger.error("[查询一个gm反馈记录异常]",e);
			}
		}
		if(gr != null){
			gmRecordCache.put(gr.getId(), gr);
		}else{
			FeedbackManager.logger.error("[根据id查询gm记录错误] [没有这个记录] [id:"+id+"]");
		}
		return gr;
	}
	
	
	SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd"); 

public List<Feedback> oldqueryFeedbacks(String begin,String end,String[] feedBackType,String[] feedBackState,String gm ,int beginNum)throws Exception{
		
		long beginTime = 0;
		if(!begin.equals("")){
			beginTime = sdf.parse(begin).getTime();
		}
		long endTime = 0;
		if(!end.equals("")){
			endTime = sdf.parse(end).getTime();
		}
		List<Feedback> list = new ArrayList<Feedback>();
		
		List<Long> idList = new ArrayList<Long>();
		
		if(!gm.equals("")){
			
			if(gm.equals("全部")){
				
				//long[] ids = em.queryIds(Feedback.class, "beginDate >= " +beginTime +" AND beginDate <= "+endTime ,"beginDate desc",1,5000);
				long[] ids = em.queryIds(Feedback.class, "beginDate >= ? AND beginDate <= ?" ,new Object[]{beginTime,endTime},"beginDate desc",1,5000);
				if(ids != null){
					for(long id :ids){
						idList.add(id);
					}
				}
				
			}else{
				GMRecord gr = this.getGMRecord(gm);
				if(gr != null){
					idList = gr.getRecordList();
				}
			}
			int max = idList.size();
			for(int i= 0;i< max;i++){
				Feedback fb = this.getFeedBack(idList.get(i));
				if(beginTime > 0){
					if(fb.getBeginDate() < beginTime){
						continue;
					}
				}
				if(endTime > 0){
					if(fb.getBeginDate()> endTime){
						continue;
					}
				}
				int feedbackType = fb.getFeedbackType();
				int feedbackState= fb.getGmState();
				boolean bln = false;
				for(String type : feedBackType){
					if(Integer.parseInt(type) == feedbackType){
						bln = true;
						break;
					}
				}
				

				if(!bln){
					continue;
				}
				
				boolean bln1 = false;
				for(String state : feedBackState){
					if(Integer.parseInt(state) == feedbackState){
						bln1 = true;
						break;
					}
				}

				if(!bln1){
					continue;
				}
				list.add(fb);
			}
		}else{
				FeedbackManager.logger.error("[gm查询记录错误] [没有这个gm] [gmName"+gm+"]");
		}
			
//		if(list.size() > beginNum*10){
//			int max = list.size();
//			if(beginNum + 10 > list.size()){
//				
//			}else{
//				max = beginNum+10;
//			}
//			list = list.subList(beginNum, max);
//			FeedbackManager.logger.warn("[gm查询记录完成] [gmName"+gm+"] [beginNum:"+beginNum*10+"] [个数:"+list.size()+"]");
//			return list;
//		}else{
//			FeedbackManager.logger.warn("[gm查询记录完成没有多余的记录] [gmName"+gm+"] [beginNum:"+beginNum*10+"] [个数:"+list.size()+"]");
//		}
		
		return list;
	}

	public List<Feedback> queryFeedbacks(String begin,String end,String[] feedBackType,String[] feedBackState,String gm ,int beginNum)throws Exception{
		
		long beginTime = 0;
		if(!begin.equals("")){
			beginTime = sdf.parse(begin).getTime();
		}
		long endTime = 0;
		if(!end.equals("")){
			endTime = sdf.parse(end).getTime();
		}
		List<Feedback> list = new ArrayList<Feedback>();
		
		List<Long> idList = new ArrayList<Long>();
		
		if(!gm.equals("")){
			
			if(gm.equals("全部")){
				
				//long[] ids = em.queryIds(Feedback.class, "beginDate >= " +beginTime +" AND beginDate <= "+endTime ,"beginDate desc",1,5000);
				long[] ids = em.queryIds(Feedback.class, "beginDate >= ? AND beginDate <= ?" ,new Object[]{beginTime,endTime},"sendTimes asc",1,5000);
				if(ids != null){
					for(long id :ids){
						idList.add(id);
					}
				}
				
			}else{
				GMRecord gr = this.getGMRecord(gm);
				if(gr != null){
					idList = gr.getRecordList();
				}
			}
			int max = idList.size();
			for(int i= 0;i< max;i++){
				Feedback fb = this.getFeedBack(idList.get(i));
				if(beginTime > 0){
					if(fb.getBeginDate() < beginTime){
						continue;
					}
				}
				if(endTime > 0){
					if(fb.getBeginDate()> endTime){
						continue;
					}
				}
				int feedbackType = fb.getFeedbackType();
				int feedbackState= fb.getGmState();
				boolean bln = false;
				for(String type : feedBackType){
					if(Integer.parseInt(type) == feedbackType){
						bln = true;
						break;
					}
				}
				

				if(!bln){
					continue;
				}
				
				boolean bln1 = false;
				for(String state : feedBackState){
					if(Integer.parseInt(state) == feedbackState){
						bln1 = true;
						break;
					}
				}

				if(!bln1){
					continue;
				}
				list.add(fb);
			}
		}else{
				FeedbackManager.logger.error("[gm查询记录错误] [没有这个gm] [gmName"+gm+"]");
		}
			
//		if(list.size() > beginNum*10){
//			int max = list.size();
//			if(beginNum + 10 > list.size()){
//				
//			}else{
//				max = beginNum+10;
//			}
//			list = list.subList(beginNum, max);
//			FeedbackManager.logger.warn("[gm查询记录完成] [gmName"+gm+"] [beginNum:"+beginNum*10+"] [个数:"+list.size()+"]");
//			return list;
//		}else{
//			FeedbackManager.logger.warn("[gm查询记录完成没有多余的记录] [gmName"+gm+"] [beginNum:"+beginNum*10+"] [个数:"+list.size()+"]");
//		}
		
		return list;
	}
	
	public List<GMRecord> getAllGm(){
		
		long[] gmIdList = null;
		try {
			gmIdList = gmem.queryIds(GMRecord.class, null);
		} catch (Exception e) {
			FeedbackManager.logger.error("[查询所有gm记录异常]",e);
		}
		if(gmIdList != null && gmIdList.length > 0){
			List<GMRecord> list = new ArrayList<GMRecord>();
			
			for(long id: gmIdList){
				GMRecord gr = this.getGMRecordById(id);
				if(gr != null){
					list.add(gr);
				}
			}
			Collection collection = gmRecordCache.values();
			Iterator it = collection.iterator();
			while(it.hasNext()){
				GMRecord gr = (GMRecord)(((CacheObject)it.next()).object);
				if(!list.contains(gr)){
					list.add(gr);
				}
			}
			
			return list;
		}else{
			FeedbackManager.logger.error("[查询所有gm记录错误] [没有gm记录] []");
		}
		
		return null;
	}
	
	public void enterGameFeedbackNotice(Player player){
		long[] list = null;
		if(player.getFeedbackIds() == null){
			list = this.getPlayerFeedBackIds(player);
			if(list == null){
				list = new long[0];
			}
			player.setFeedbackIds(list);
		}else{
			list = player.getFeedbackIds();
		}
		boolean hava = false;
		if(list != null && list.length > 0){
			for(long id: list){
				Feedback fb = this.getFeedBack(id);
				if(fb != null&& fb.getPlayerState() == FeedBackState.新反馈.state){
					hava = true;
					break;
				}
				if(fb != null && fb.isSendJudge() && !fb.isAlreadyJudge()){
					hava = true;
					break;
				}
			}
			FeedbackManager.logger.warn("[玩家反馈通知] ["+player.getLogString()+"] []");
		}
		FEEDBACK_NOTICE_CLIENT_RES res = null;
		if(hava){
			// 发送通知  通知gm消息闪
			res = new FEEDBACK_NOTICE_CLIENT_RES(GameMessageFactory.nextSequnceNum(), true);
		}else{
			// 发送通知  通知gm消息不闪
			res = new FEEDBACK_NOTICE_CLIENT_RES(GameMessageFactory.nextSequnceNum(), false);
		}
		player.addMessageToRightBag(res);
	}
	
	// 查询得到某个player的所有需要评价的评价
	public List<Long> queryAllJudgeOne(Player player){

		List<Long> listJudge = new ArrayList<Long>();
		long[] list = this.getPlayerFeedBackIds(player);
		if(list != null && list.length > 0){
			for(long id: list){
				Feedback fb = this.getFeedBack(id);
				if(fb != null && fb.isSendJudge() && !fb.isAlreadyJudge()){
					listJudge.add(id);
				}
			}
			FeedbackManager.logger.warn("[查询所有反馈的评价] ["+player.getLogString()+"] []");
		}
		return listJudge;
	}
	
	
	private String outFileName;
	
	public String getOutFileName() {
		return outFileName;
	}

	public void setOutFileName(String outFileName) {
		this.outFileName = outFileName;
	}

	public String writeToTxt(String beginTime,String endTime)throws Exception{
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		long begin = sdf.parse(beginTime).getTime();
		long end = sdf.parse(endTime).getTime();
		PlayerManager pm = PlayerManager.getInstance();
		
		if(begin >= end){
			return "输入时间错误";
		}
		
		FeedbackManager fbm = FeedbackManager.getInstance();
		List<Long> fitList = new ArrayList<Long>();
		//long[] ids = em.queryIds(Feedback.class, "beginDate >= " +beginTime +"AND beginDate <= "+endTime );
		long[] ids = em.queryIds(Feedback.class, "beginDate >= ? AND beginDate <= ?",new Object[]{beginTime,endTime});
		if(ids != null){
			for(long id :ids){
				fitList.add(id);
			}
		}
		if(fitList != null && fitList.size() > 0){
			String[] states = {反馈状态未处理,反馈状态等待,反馈状态新,反馈状态关闭};
			String[] types = {反馈类型BUG,反馈类型建议,反馈类型投诉,反馈类型充值,反馈类型其他};
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outFileName+"/"+endTime+".xls")),"gbk"));
			Feedback fb = null;
			StringBuffer sb = null;
			sb = new StringBuffer();
			sb.append("日期");
			sb.append("\t");
			sb.append("时间");
			sb.append("\t");
			sb.append("反馈状态");
			sb.append("\t");
			sb.append("角色名");
			sb.append("\t");
			sb.append("反馈类型");
			sb.append("\t");
			sb.append("标题");
			sb.append("\t");
			sb.append("具体内容");
			sb.append("\n");
			bw.write(sb.toString());
			Player player = null;
			int flushNum = 0;
			for(long id:fitList){
				fb = fbm.getFeedBack(id);
				if(fb != null){
					++flushNum;
					 sb = new StringBuffer();
						String date = format1.format(new Date(fb.getBeginDate()));
						String time = format2.format(new Date(fb.getBeginDate()));
						String feedbackState = states[fb.getGmState()];
						String name= 角色已删除;
						try{
							player = pm.getPlayer(fb.getPlayerId());
						}catch(Exception e){
							logger.error("[打印反馈异常]",e);
						}
						if(player != null){
							name = player.getName();
						}
						String feedbackType = types[fb.getFeedbackType()];
						String subject = fb.getSubject();
						List<Reply> replyList = fb.getList();
						sb.append(date);
						sb.append("\t");
						sb.append(time);
						sb.append("\t");
						sb.append(feedbackState);
						sb.append("\t");
						sb.append(name);
						sb.append("\t");
						sb.append(feedbackType);
						sb.append("\t");
						sb.append(subject);
						sb.append("\t");
						for(Reply r :replyList){
							if(r.getGmName().equals("")){
								sb.append(name+":"+r.getFcontent().replaceAll("\n", ""));
							}else{
								sb.append(r.getGmName()+":"+r.getFcontent().replaceAll("\n", ""));
							}
							sb.append("\t");
						}
						sb.append("\n");
						bw.write(sb.toString());
						if(flushNum >= 300){
							flushNum = 0;
							bw.flush();
						}
				}
			}
			
			bw.flush();
			bw.close();
		}else{
			return "没有合适记录";
		}
		return "";
	}
	
//	public List<Feedback> getNewFeedbacks(){
//		List<Feedback> list = new ArrayList<Feedback>();
//		for(Feedback fb:feedbacks){
//			if(!fb.isShow()){
//				list.add(fb);	
//			}
//		}
//		return list;
//	}

	public List<Feedback> getFeedbacks() {
		return feedbacks;
	}

	public void setFeedbacks(List<Feedback> feedbacks) {
		this.feedbacks = feedbacks;
	}
}
