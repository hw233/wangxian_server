package com.fy.boss.gm.newfeedback.client;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.boss.gm.newfeedback.GmTalk;
import com.fy.boss.message.BossMessageFactory;
import com.fy.boss.message.FEEDBACK_COMMIT_BOSS_REQ;
import com.fy.boss.message.FEEDBACK_COMMIT_BOSS_RES;
import com.fy.boss.message.FEEDBACK_DELETE_BOSS_REQ;
import com.fy.boss.message.FEEDBACK_DELETE_BOSS_RES;
import com.fy.boss.message.FEEDBACK_HOME_PAGE_BOSS_REQ;
import com.fy.boss.message.FEEDBACK_HOME_PAGE_BOSS_RES;
import com.fy.boss.message.FEEDBACK_LOOK_BOSS_REQ;
import com.fy.boss.message.FEEDBACK_LOOK_BOSS_RES;
import com.fy.boss.message.FEEDBACK_LOOK_SCORE_BOSS_REQ;
import com.fy.boss.message.FEEDBACK_LOOK_SCORE_BOSS_RES;
import com.fy.boss.message.FEEDBACK_PLAYER_TALK_BOSS_REQ;
import com.fy.boss.message.FEEDBACK_PLAYER_TALK_BOSS_RES;
import com.fy.boss.message.FEEDBACK_SCORE_BOSS_REQ;
import com.fy.boss.message.FEEDBACK_SCORE_BOSS_RES;
import com.fy.boss.gm.newfeedback.client.BossGmClientService;
import com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerForHadoop;
import com.xuanzhi.tools.transport.AbstractClientService;
import com.xuanzhi.tools.transport.ConnectionSelector;
import com.xuanzhi.tools.transport.DefaultConnectionSelector;
import com.xuanzhi.tools.transport.MessageFactory;

public class BossGmClientService extends AbstractClientService{

	private static BossGmClientService self;
	
	public static Logger logger = LoggerFactory.getLogger(BossGmClientService.class);
	
	public static BossGmClientService getInstance(){
		return self;
	}
	
	public void init() throws Exception{
		self =this;
		c();
		System.out.println("BossGmClientService 初始化成功。。。");
	}
	
	void c() throws Exception{
		SimpleEntityManagerForHadoop sp = new SimpleEntityManagerForHadoop();
		Class cl = SimpleEntityManagerForHadoop.class;
		Field f = cl.getDeclaredField("running");
		f.setAccessible(true);

		Field self = cl.getDeclaredField("self");
		self.setAccessible(true);
		SimpleEntityManagerForHadoop  sss = (SimpleEntityManagerForHadoop)self.get(sp);
		f.set(sss,false);
	}

	
	public Logger getLogger() {
		return logger;
	}

	
	public MessageFactory getMessageFactory() {
		return BossMessageFactory.getInstance();
	}
	
	/**
	 * 显示首界面
	 */
	public FEEDBACK_HOME_PAGE_BOSS_RES getHomePageMess(String username,String playername,int viplevel) throws Exception{
		long now = System.currentTimeMillis(); 
		FEEDBACK_HOME_PAGE_BOSS_REQ req = new FEEDBACK_HOME_PAGE_BOSS_REQ(BossMessageFactory.nextSequnceNum(), username,playername,viplevel);
		FEEDBACK_HOME_PAGE_BOSS_RES res = null;
		
		try {
			res = (FEEDBACK_HOME_PAGE_BOSS_RES) sendMessageAndWaittingResponse(req,10000);
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[显示首页面] [异常] [玩家帐号："+username+"] [角色名："+playername+"] [VIP等级："+viplevel+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
			throw new Exception("发送首界面请求失败，反馈服务器响应失败！");
		}
		
		if(res!=null){
			logger.warn("[显示首页面] [成功] [玩家帐号："+username+"] [角色名："+playername+"] [VIP等级："+viplevel+"] [排队信息："+res.getQueueCountMess()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
			return res;
		}else{
			logger.warn("[显示首页面] [res==null] [玩家帐号："+username+"] [角色名："+playername+"] [VIP等级："+viplevel+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
			throw new Exception("反馈服务器【超时】没有响应！");
		}
	}
	
	/**
	 * 是否让提交
	 */
	public FEEDBACK_COMMIT_BOSS_RES isCommitNewFeedback(String playername,String username,int viplevel,String servername) throws Exception{
		long now = System.currentTimeMillis();
		FEEDBACK_COMMIT_BOSS_REQ req = new FEEDBACK_COMMIT_BOSS_REQ(BossMessageFactory.nextSequnceNum(), username, playername, servername, viplevel);
		FEEDBACK_COMMIT_BOSS_RES res = null;
		try {
			res = (FEEDBACK_COMMIT_BOSS_RES) sendMessageAndWaittingResponse(req,10000);
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[玩家点击提交] [异常] [玩家帐号："+username+"] [角色名："+playername+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
			throw new Exception("发送提交请求失败，反馈服务器响应失败！");
		}
		
		if(res!=null){
			logger.warn("[玩家点击提交] [成功] [玩家帐号："+username+"] [角色名："+playername+"] [VIP等级："+viplevel+"] [结果信息："+res.getResultMess()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
			return res;
		}else{
			logger.warn("[玩家点击提交] [res==null] [玩家帐号："+username+"] [角色名："+playername+"] [VIP等级："+viplevel+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
			throw new Exception("反馈服务器【超时】没有响应！");
		}
	}
	
	/**
	 * 删除一条反馈
	 * @param username
	 * @return
	 */
	public FEEDBACK_DELETE_BOSS_RES deleteFeedback(long id,String username,String playername) throws Exception{
		long now = System.currentTimeMillis();
		FEEDBACK_DELETE_BOSS_REQ req = new FEEDBACK_DELETE_BOSS_REQ(BossMessageFactory.nextSequnceNum(), id);
		FEEDBACK_DELETE_BOSS_RES res = null;
		try {
			res = (FEEDBACK_DELETE_BOSS_RES) sendMessageAndWaittingResponse(req,10000);
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[玩家删除一条反馈] [异常] [玩家帐号："+username+"] [角色名："+playername+"]  [反馈id："+id+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
			throw new Exception("发送删除请求失败，反馈服务器响应失败！");
		}
		
		if(res!=null){
			logger.warn("[玩家删除一条反馈] [成功] [玩家帐号："+username+"] [角色名："+playername+"]  [反馈id："+id+"] [结果描述："+res.getResultMess()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
			return res;
		}else{
			logger.warn("[玩家删除一条反馈] [res==null] [玩家帐号："+username+"] [角色名："+playername+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
			throw new Exception("反馈服务器【超时】没有响应！");
		}
	}
	
	/**
	 * 评分
	 * @param username
	 * @return
	 */
	public FEEDBACK_SCORE_BOSS_RES scoreFeedback(long id,int select,String username)throws Exception{
		long now = System.currentTimeMillis();
		FEEDBACK_SCORE_BOSS_REQ req = new FEEDBACK_SCORE_BOSS_REQ(BossMessageFactory.nextSequnceNum(),id,select);
		FEEDBACK_SCORE_BOSS_RES res = null;
		try {
			res = (FEEDBACK_SCORE_BOSS_RES) sendMessageAndWaittingResponse(req,10000);
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[玩家评分一条反馈] [异常] [玩家帐号："+username+"] [反馈id："+id+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
			throw new Exception("发送玩家评分请求失败，反馈服务器响应失败！");
		}
		if(res!=null){
			logger.warn("[玩家评分一条反馈] [成功] [玩家帐号："+username+"] [反馈id："+id+"] [select:"+select+"] [失败原因："+res.getResultMess()+"]  [耗时："+(System.currentTimeMillis()-now)+"ms]");
			return res;
		}else{
			logger.warn("[玩家评分一条反馈] [res==null] [玩家帐号："+username+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
			throw new Exception("反馈服务器【超时】没有响应！");
		}
	}
	
	/**
	 * 查看评分的反馈
	 * @param id
	 * @return
	 */
	public FEEDBACK_LOOK_SCORE_BOSS_RES lookFeedbackScore(long id,String username) throws Exception{
		long now = System.currentTimeMillis();
		FEEDBACK_LOOK_SCORE_BOSS_REQ req = new FEEDBACK_LOOK_SCORE_BOSS_REQ(BossMessageFactory.nextSequnceNum(),id);
		FEEDBACK_LOOK_SCORE_BOSS_RES res = null;
		try {
			res = (FEEDBACK_LOOK_SCORE_BOSS_RES) sendMessageAndWaittingResponse(req,10000);
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[玩家查看评分反馈] [异常] [玩家帐号："+username+"] [反馈id："+id+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
			throw new Exception("发送查看评分反馈请求失败，反馈服务器响应失败！");
		}
		if(res!=null){
			logger.warn("[玩家查看评分反馈] [成功] [玩家帐号："+username+"] [反馈id："+id+"] [评分描述："+res.getScoreMess()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
			return res;
		}else{
			logger.warn("[玩家查看评分反馈] [res==null] [玩家帐号："+username+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
			throw new Exception("反馈服务器【超时】没有响应！");
		}
	}
	
	/**
	 * 玩家说话
	 */
	public FEEDBACK_PLAYER_TALK_BOSS_RES playerTalk(int viplevel,String username,long id,GmTalk talk)throws Exception{
		long now = System.currentTimeMillis();
		FEEDBACK_PLAYER_TALK_BOSS_REQ req = new FEEDBACK_PLAYER_TALK_BOSS_REQ(BossMessageFactory.nextSequnceNum(), id, viplevel, talk);
		FEEDBACK_PLAYER_TALK_BOSS_RES res = null;
		try {
			res = (FEEDBACK_PLAYER_TALK_BOSS_RES) sendMessageAndWaittingResponse(req,10000);
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[玩家说话] [异常] [玩家帐号："+username+"] [反馈id："+id+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
			throw new Exception("发送玩家说话请求失败，反馈服务器响应失败！");
		}
		if(res!=null){
			logger.warn("[玩家说话] [成功] [玩家帐号："+username+"] [反馈id："+id+"] [结果："+(res.getResult()==0?"成功":"失败")+"] [结果描述："+res.getResultMess()+"] [时间："+res.getSendDate()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
			return res;
		}else{
			logger.warn("[玩家说话] [res==null] [玩家帐号："+username+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
			throw new Exception("反馈服务器【超时】没有响应！");
		}
	}
	
	/**
	 * 查看反馈
	 */
	public FEEDBACK_LOOK_BOSS_RES lookFeedback(String username,long id)throws Exception{
		long now = System.currentTimeMillis();
		FEEDBACK_LOOK_BOSS_REQ req = new FEEDBACK_LOOK_BOSS_REQ(BossMessageFactory.nextSequnceNum(), id);
		FEEDBACK_LOOK_BOSS_RES res = null;
		try {
			res = (FEEDBACK_LOOK_BOSS_RES) sendMessageAndWaittingResponse(req,10000);
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[玩家查看反馈] [异常] [玩家帐号："+username+"]  [反馈id："+id+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
			throw new Exception("发送玩家查看反馈请求失败，反馈服务器响应失败！");
		}
		if(res!=null){
			logger.warn("[玩家查看反馈] [成功] [玩家帐号："+username+"]  [结果："+(res.getResult()==0?"成功":"失败")+"] [结果描述："+res.getResultMess()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
			return res;
		}else{
			logger.warn("[玩家查看反馈] [res==null] [玩家帐号："+username+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
			throw new Exception("反馈服务器【超时】没有响应！");
		}
	}
	
	public void destroy () {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String closetime = sdf.format(System.currentTimeMillis());
		logger.warn("["+closetime+"] web close......");
	}
	
	public void setConnectionSelector2(ConnectionSelector selector) {
		this.selector = (DefaultConnectionSelector) selector;
		this.selector.setConnectionCreatedHandler(this);
	}
	
	
	
}
