package com.fy.boss.gm.newfeedback.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.log4j.Logger;

import com.fy.boss.gm.newfeedback.GmTalk;
import com.fy.boss.gm.newfeedback.NewFeedback;
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
import com.fy.boss.gm.newfeedback.service.BossGmServerService;
import com.fy.boss.gm.newfeedback.service.NewFeedbackQueueManager;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionConnectedHandler;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.ConnectionSelector;
import com.xuanzhi.tools.transport.DefaultConnectionSelector;
import com.xuanzhi.tools.transport.MessageHandler;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class BossGmServerService implements ConnectionConnectedHandler,MessageHandler{

	private static BossGmServerService self;
	
	public static Logger logger = Logger.getLogger(BossGmServerService.class);
	
	public static BossGmServerService getInstance(){
		return self;
	}
	
	public void init() throws Exception{
		self =this;
	}

	DefaultConnectionSelector selector;
	
	public ConnectionSelector getConnectionSelector() {
		return selector;
	}

	public void setConnectionSelector(ConnectionSelector selector) {
		this.selector = (DefaultConnectionSelector) selector;
		this.selector.setConnectionConnectedHandler(this);	
	}
	@Override
	public ResponseMessage receiveRequestMessage(Connection conn,
			RequestMessage message) throws ConnectionException {
		long now = System.currentTimeMillis();
		if(message instanceof FEEDBACK_HOME_PAGE_BOSS_REQ){
			logger.warn("receive FEEDBACK_HOME_PAGE_BOSS_REQ in boss.....");
			String playername = "";
			String username = "";
			String gmTelephone = "";
			int viplevel = 0;
			try{
				FEEDBACK_HOME_PAGE_BOSS_REQ req = (FEEDBACK_HOME_PAGE_BOSS_REQ) message;
				playername = req.getPlayername();
				viplevel = req.getViplevel();
				username = req.getUsername();
				gmTelephone = NewFeedbackQueueManager.getInstance().getGmTelephone();
				StringBuffer mess = new StringBuffer();
				int queueLength = 0;
				if(viplevel>=5){
					mess.append("亲爱的<f color='0xF3F349'>VIP</f>玩家");
					queueLength = NewFeedbackQueueManager.getInstance().getVipqueue().size();
				}else{
					mess.append("亲爱的玩家");
					queueLength = NewFeedbackQueueManager.getInstance().getQueue().size();
				}
				
				if(playername!=null&&req.getPlayername().trim().length()>0){
					mess.append("【<f color='0x66CD00'>"+playername+"</f>】");
				}
				mess.append("您好！欢迎您使用飘渺寻仙曲客服反馈系统");
				if(gmTelephone!=null&&gmTelephone.trim().length()>0){
					mess.append("或拨打24小时客服电话<f color='0xCDCD00'>"+gmTelephone+"</f>,");
				}
				mess.append("您所提交的问题将加入至提交队列中排队，请您耐心等待.");
				String queueMess = "当前排队问题数："+queueLength;
				int maxLimitShow = NewFeedbackQueueManager.getInstance().getMaxLimmitShow();
				
				List<NewFeedback> list = NewFeedbackQueueManager.getInstance().getFBbyState(playername,username, 1,viplevel);
				FEEDBACK_HOME_PAGE_BOSS_RES res = new FEEDBACK_HOME_PAGE_BOSS_RES(req.getSequnceNum(),queueMess,mess.toString(),maxLimitShow,list.toArray(new NewFeedback[]{}));
				logger.warn("[获得gm反馈首界面] [成功] FEEDBACK_COMMIT_BOSS_RES[用户名："+username+"] [角色名："+playername+"] [VIP："+viplevel+"] [客服电话："+gmTelephone+"] [队列长度："+queueLength+"] [list:"+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
				return res;
			}catch(Exception e){
				e.printStackTrace();
				logger.warn("[获得gm反馈首界面] [异常] [用户名："+username+"] [角色名："+playername+"] [VIP："+viplevel+"] [客服电话："+gmTelephone+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
			}
		}else if(message instanceof FEEDBACK_DELETE_BOSS_REQ){
			FEEDBACK_DELETE_BOSS_REQ req = (FEEDBACK_DELETE_BOSS_REQ) message;
			boolean issucc = NewFeedbackQueueManager.getInstance().deleteNewFeedback(req.getId());
			FEEDBACK_DELETE_BOSS_RES res = null;
			if(issucc){
				res = new FEEDBACK_DELETE_BOSS_RES(req.getSequnceNum(), req.getId(), "删除成功", (byte)0);
			}else{
				res = new FEEDBACK_DELETE_BOSS_RES(req.getSequnceNum(), req.getId(), "删除一条反馈失败！", (byte)1);
			}
			return res;
		}else if(message instanceof FEEDBACK_SCORE_BOSS_REQ){
			logger.warn("receive FEEDBACK_SCORE_BOSS_REQ in boss.....");
			FEEDBACK_SCORE_BOSS_REQ req = (FEEDBACK_SCORE_BOSS_REQ) message;
			FEEDBACK_SCORE_BOSS_RES res = null;
			boolean issucc = NewFeedbackQueueManager.getInstance().scoreNewFeedback(req.getId(), req.getSelect());
			if(issucc){
				res = new FEEDBACK_SCORE_BOSS_RES(req.getSequnceNum(), req.getId(), (byte)0, "感谢您的评分，我们会努力做到更好！");
			}else{
				res = new FEEDBACK_SCORE_BOSS_RES(req.getSequnceNum(), req.getId(), (byte)1, "评分失败，请稍后再评，或者拨打客服电话<f color='0xCDCD00'>"+NewFeedbackQueueManager.getInstance().getGmTelephone()+"</f>");
			}
			return res;
		}else if(message instanceof FEEDBACK_LOOK_SCORE_BOSS_REQ){
			logger.warn("receive FEEDBACK_LOOK_SCORE_BOSS_REQ in boss.....");
			FEEDBACK_LOOK_SCORE_BOSS_REQ req = (FEEDBACK_LOOK_SCORE_BOSS_REQ) message;
			NewFeedback fb = NewFeedbackQueueManager.getInstance().getNewFeedbackById(req.getId());
			FEEDBACK_LOOK_SCORE_BOSS_RES res = null;
			String mess = "";
			if(fb!=null){
				if(fb.getGmHandler()!=null){
					mess = "感谢您使用GM反馈系统，请对为您服务的【"+fb.getGmHandler()+"】进行评价！";
				}else{
					mess = "感谢您使用GM反馈系统，请对为您服务的GM进行评价！";
				}
				res = new FEEDBACK_LOOK_SCORE_BOSS_RES(req.getSequnceNum(), req.getId(), mess);
			}else{
				res = new FEEDBACK_LOOK_SCORE_BOSS_RES(req.getSequnceNum(), req.getId(), "查看反馈出错，请您拨打客服电话<f color='0xCDCD00'>"+NewFeedbackQueueManager.getInstance().getGmTelephone()+"</f>");
			}
			return res;
		}else if(message instanceof FEEDBACK_PLAYER_TALK_BOSS_REQ){
			logger.warn("receive FEEDBACK_PLAYER_TALK_BOSS_REQ in boss.....");
			FEEDBACK_PLAYER_TALK_BOSS_REQ req = (FEEDBACK_PLAYER_TALK_BOSS_REQ) message;
			long talkid = NewFeedbackQueueManager.getInstance().playerTalk(req.getId(), req.getTalk(),req.getViplevel());
			FEEDBACK_PLAYER_TALK_BOSS_RES res = null;
			if(talkid>0){
				if(talkid==1000){
					res = new FEEDBACK_PLAYER_TALK_BOSS_RES(req.getSequnceNum(), req.getId(), talkid, 0, (byte)1, "1000");
				}else{
					res = new FEEDBACK_PLAYER_TALK_BOSS_RES(req.getSequnceNum(), req.getId(), talkid, 0, (byte)0, "玩家说话成功");
				}
			}else{
				res = new FEEDBACK_PLAYER_TALK_BOSS_RES(req.getSequnceNum(), req.getId(), talkid, 0, (byte)1, "发送一个对话失败！请您拨打客服电话：<f color='0xCDCD00'>"+NewFeedbackQueueManager.getInstance().getGmTelephone()+"</f>");
			}
			logger.warn("[处理玩家说话] [FEEDBACK_PLAYER_TALK_BOSS_REQ] [反馈id："+req.getId()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
			return res;
		}else if(message instanceof FEEDBACK_COMMIT_BOSS_REQ){
			logger.warn("receive FEEDBACK_COMMIT_BOSS_REQ in boss.....");
			FEEDBACK_COMMIT_BOSS_REQ req = (FEEDBACK_COMMIT_BOSS_REQ) message;
			boolean iscommit = NewFeedbackQueueManager.getInstance().commitNewFeedback(req.getViplevel());
			FEEDBACK_COMMIT_BOSS_RES res = null;
			if(iscommit){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String sendtime = sdf.format(new Date());	
				NewFeedback fb = new NewFeedback();
				fb.setUsername(req.getUsername());
				fb.setPlayername(req.getPlayername());
				fb.setSendtime(sendtime);
				fb.setLastReplyTime(System.currentTimeMillis());
				fb.setViplevel(req.getViplevel());
				fb.setServername(req.getServername());
				long resid = NewFeedbackQueueManager.getInstance().addNewFeedback(fb);
				if(resid>0){
					res = new FEEDBACK_COMMIT_BOSS_RES(req.getSequnceNum(), resid, (byte)0, "亲爱的玩家[<f color='0x66CD00'>"+req.getPlayername()+"</f>]您好，请详细描述您遇到的问题！");
				}else{
					res = new FEEDBACK_COMMIT_BOSS_RES(req.getSequnceNum(), 0, (byte)1, "提交新反馈出错,请拨打客户电话<f color='0xCDCD00'>"+NewFeedbackQueueManager.getInstance().getGmTelephone()+"</f>联系GM！");
				}
			}else{
				res = new FEEDBACK_COMMIT_BOSS_RES(req.getSequnceNum(), 0, (byte)1, "当前排队人数已达到反馈队列最大数，请您稍后再提交新问题！");
			}
			logger.warn("[处理提交新问题] [FEEDBACK_COMMIT_BOSS_REQ] [用户名："+req.getUsername()+"] [角色名："+req.getPlayername()+"] [vip:"+req.getViplevel()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
			return res;
		}else if(message instanceof FEEDBACK_LOOK_BOSS_REQ){
			logger.warn("receive FEEDBACK_LOOK_BOSS_REQ in boss.....");
			FEEDBACK_LOOK_BOSS_REQ req = (FEEDBACK_LOOK_BOSS_REQ) message;
			GmTalk [] talks = NewFeedbackQueueManager.getInstance().getGmTalkById(req.getId());
			FEEDBACK_LOOK_BOSS_RES res = null;
			if(talks!=null){
				NewFeedback  fb = NewFeedbackQueueManager.getInstance().getNewFeedbackById(req.getId());
				if(fb!=null&&fb.getIsHandleNow()==1&&fb.getGmHandler()!=null){
					res = new FEEDBACK_LOOK_BOSS_RES(req.getSequnceNum(), req.getId(), "亲爱的玩家您好,<f color='0x66CD00'>"+fb.getGmHandler()+"</f>正在为您处理问题...", (byte)0, talks);
				}else{
					res = new FEEDBACK_LOOK_BOSS_RES(req.getSequnceNum(), req.getId(), "亲爱的玩家您好", (byte)0, talks);
				}
				
			}else{
				res = new FEEDBACK_LOOK_BOSS_RES(req.getSequnceNum(), req.getId(), "查看反馈失败，对话内容为空！", (byte)1, talks);
			}
			logger.warn("[处理查看反馈] [FEEDBACK_LOOK_BOSS_REQ] [id："+req.getId()+"] [对话长度："+talks.length+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		    return res;
		}else{
			logger.warn(".......ERROR....MESSAGE....:"+message.getTypeDescription());
			
		}
		
		return null;
	}
	
	@Override
	public void connected(Connection conn) throws IOException {
		conn.setMessageFactory(BossMessageFactory.getInstance());
		conn.setMessageHandler(this);
	}

	@Override
	public void discardRequestMessage(Connection conn, RequestMessage req)
			throws ConnectionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receiveResponseMessage(Connection conn, RequestMessage req,
			ResponseMessage res) throws ConnectionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RequestMessage waitingTimeout(Connection conn, long timeout)
			throws ConnectionException {
		// TODO Auto-generated method stub
		return null;
	}

	

}
