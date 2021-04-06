package com.fy.engineserver.gm.feedback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gm.feedback.service.FeedbackManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.REPLY_SPECIAL_FEEDBACK_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.xuanzhi.tools.cache.CacheListener;
import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
@SimpleIndices({
	@SimpleIndex(members={"beginDate"}),
	@SimpleIndex(members={"playerId"}),
	@SimpleIndex(members={"lastGmId"}),
	@SimpleIndex(members={"GmState"})
})
public class Feedback implements Cacheable, CacheListener  {

	@SimpleId
	private long id;

	@SimpleVersion
	private int version;
	
	public Feedback(){
		
	}
	
	//未处理,等待，新，关闭
	private int GmState;
	private int playerState;
	
	private String fid;
	
	private String countryname;
	
	private String career;
	
	private int playerlevel;
	
	private String servername;
	
	//标志在浏览器显示，当shownum等于0，是没有显示，大于0，是在浏览器的标示
	private int showNum;
	
	private String channel;
	
//	private boolean isShow;
	
	//玩家的创建新消息的时间，玩家回复新消息的时间
	private long sendTimes;

	private String userName;
	
	private String playerName;
	
	@SimpleColumn(length=512)
	private String content;
	
	//玩家的创建新消息的时间，玩家回复新消息的时间
	private String sendTimestr;
	
	@SimpleColumn(length=256)
	private String dialogs;
	
	//@SimpleColumn(length=1024)
	private transient String dialogCopy;
	
	//BUG  建议    投诉    充值   其他
	private int feedbackType;
	
	//是否发送了评价
	private boolean sendJudge = false;
	
	//玩家已经评价
	private boolean alreadyJudge = false;
	//评价等级
	private int judge;
	
	//发送反馈的玩家Id
	private long playerId;
	
	private long beginDate;
	
	//VIP等级
	private int vipLevel;
	 
	//跟踪状态
	private boolean isFollow;
	
	//上一个回复的gm 的id
	private String lastGmId = "";
	
	//主题
	@SimpleColumn(length=100)
	private String subject;
	
	
	//回复
	@SimpleColumn(length=8000)
	List<Reply> list = new ArrayList<Reply>();
	
	public boolean noticePlayer = false;
	
	public boolean noticeGm = false;
	// 玩家回复反馈
	public void playerReply(Reply r){
		
		this.getList().add(r);
		this.setPlayerState(FeedBackState.等待反馈.state);
		this.setGmState(FeedBackState.新反馈.state);
		FeedbackManager fm = FeedbackManager.getInstance();
		this.setSendTimes(System.currentTimeMillis());
		List <Feedback> fbs = fm.getFeedbacks();
		Feedback oldfb = fm.getFeedBack(this.getId());
		if(fbs.contains(oldfb)){
			fbs.set(fbs.indexOf(oldfb), this);
		}else{
			fbs.add(this);
		}
		
		noticeGm = true;  //客户端变化
		FeedbackManager.logger.warn("[玩家回复反馈] [消息总数："+fbs.size()+"] [反馈标题："+this.getSubject()+"] [反馈回复时间："+this.getSendTimes()+"] []");
	}
	
	public void overTimeGmCloseFeedback(){
		
		String gmName = this.getLastGmId();
		Reply reply = new Reply();
		reply.setGmName(gmName);
		reply.setFcontent(FeedbackManager.反馈关闭提示);
		
		this.getList().add(reply);
		this.checkLength();
		this.setList(this.getList());
		this.setLastGmId(gmName);
		
		FeedbackManager.logger.warn("[时间超过半小时关闭反馈] ["+this.getLogString()+"]");
	
		
	}
	
	
	
	
	// Gm回复反馈
	public void gmReply(Reply r,String gmName){
		
		this.getList().add(r);
		checkLength();
		this.setList(this.getList());
		this.setLastGmId(gmName);
		this.setPlayerState(FeedBackState.新反馈.state);
		this.setGmState(FeedBackState.等待反馈.state);
		
		REPLY_SPECIAL_FEEDBACK_RES res = new REPLY_SPECIAL_FEEDBACK_RES(GameMessageFactory.nextSequnceNum(), this.getId());
		
		PlayerManager pm = PlayerManager.getInstance();
		if(pm.isOnline(playerId)){
			Player player;
			try {
				noticePlayer = true;
				player = pm.getPlayer(playerId);
				player.addMessageToRightBag(res);
				
				player.send_HINT_REQ(Translate.translateString(Translate.gm回复了你的反馈XX, new String[][]{{Translate.STRING_1,this.getSubject()}}));
			} catch (Exception e) {
				FeedbackManager.logger.error("[gm回复反馈] ["+gmName+"]",e);
			}
		}
		if(FeedbackManager.logger.isWarnEnabled())
			FeedbackManager.logger.warn("[gm回复反馈成功] [gm:"+gmName+"] [反馈id:"+this.getId()+"]");
		
	}
	
	// GM 关闭反馈
	public void feedBackFinish(Reply r,String gmName){
		this.getList().add(r);
		checkLength();
		setList(this.getList());
		this.setLastGmId(gmName);
		this.setPlayerState(FeedBackState.已关闭.state);
		this.setGmState(FeedBackState.已关闭.state);
	}
	
	public String getLogString(){
		return "玩家id:"+playerId+"主题:"+this.getSubject()+"gmName:"+this.getLastGmId();
	}
	
	
	//gm回复时间，大于半小时关闭反馈
	public long lastUpdateTime;
	
	
	//GM 能不能发送评价
	public String canReply(){
		
		if(this.getGmState() == FeedBackState.已关闭.state){
			return Translate.反馈已关闭;
		}
		if(this.lastGmId != null && !this.lastGmId.equals("")){
			
			if(this.isSendJudge()){
				return Translate.Gm已经发送了评价;
			}
			return "";
		}else{
			return Translate.Gm还没有回复过反馈不能发送评价;
		}
		
	}
	// 判断长度
	public synchronized void checkLength(){
		try{
			if(this.getList().size() >= 30){
				for(int i = 1 ; i <= 10 ; i++){
					getList().remove(i);
				}
				FeedbackManager.logger.warn("[回复太长删除成功] ["+this.getSubject()+"] ["+this.playerId+"]");
			}
		}catch(Exception e){
			FeedbackManager.logger.error("[回复太长删除成功异常] ["+this.getSubject()+"] ["+this.playerId+"]",e);
		}
		
	}
	
	
	
	/*******************************  set get**********************************/

	
	
	
	public long getId() {
		return id;
	}


	public long getLastUpdateTime() {
		return lastUpdateTime;
	}


	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
		FeedbackManager.em.notifyFieldChange(this, "lastUpdateTime");
	}

	public void setId(long id) {
		this.id = id;
	}


	public int getGmState() {
		return GmState;
	}


	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
		FeedbackManager.em.notifyFieldChange(this, "fid");
	}
	
	public void setGmState(int gmState) {
		GmState = gmState;
		FeedbackManager.em.notifyFieldChange(this, "GmState");
	}

	public long getSendTimes() {
		return sendTimes;
	}

	public void setSendTimes(long sendTimes) {
		this.sendTimes = sendTimes;
		FeedbackManager.em.notifyFieldChange(this, "sendTimes");
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
		String st = format1.format(new Date(sendTimes));
		this.setSendTimestr(st);
	}
	
	public int getShowNum() {
		return showNum;
	}

	public void setShowNum(int showNum) {
		this.showNum = showNum;
		FeedbackManager.em.notifyFieldChange(this, "showNum");
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
		FeedbackManager.em.notifyFieldChange(this, "channel");
	}
	
	public String getCountryname() {
		return countryname;
	}

	public void setCountryname(String countryname) {
		this.countryname = countryname;
		FeedbackManager.em.notifyFieldChange(this, "countryname");
	}

	public String getCareer() {
		return career;
	}

	public void setCareer(String career) {
		this.career = career;
		FeedbackManager.em.notifyFieldChange(this, "career");
	}

	public int getPlayerState() {
		return playerState;
	}


	public int getPlayerlevel() {
		return playerlevel;
	}

	public void setPlayerlevel(int playerlevel) {
		this.playerlevel = playerlevel;
		FeedbackManager.em.notifyFieldChange(this, "playerlevel");
	}

	public void setPlayerState(int playerState) {
		this.playerState = playerState;
		FeedbackManager.em.notifyFieldChange(this, "playerState");
	}


	public int getFeedbackType() {
		return feedbackType;
	}


	public void setFeedbackType(int feedbackType) {
		this.feedbackType = feedbackType;
		FeedbackManager.em.notifyFieldChange(this, "feedbackType");
	}


	public boolean isSendJudge() {
		return sendJudge;
	}


	public void setSendJudge(boolean sendJudge) {
		this.sendJudge = sendJudge;
		FeedbackManager.em.notifyFieldChange(this, "sendJudge");
	}


	public int getJudge() {
		return judge;
	}


	public void setJudge(int judge) {
		this.judge = judge;
		FeedbackManager.em.notifyFieldChange(this, "judge");
	}


	public long getPlayerId() {
		return playerId;
	}


	public void setPlayerId(long playerId) {
		this.playerId = playerId;
		FeedbackManager.em.notifyFieldChange(this, "playerId");
	}


	public long getBeginDate() {
		return beginDate;
	}


	public void setBeginDate(long beginDate) {
		this.beginDate = beginDate;
		FeedbackManager.em.notifyFieldChange(this, "beginDate");
	}


	public String getLastGmId() {
		return lastGmId;
	}


	public void setLastGmId(String lastGmId) {
		this.lastGmId = lastGmId;
		FeedbackManager.em.notifyFieldChange(this, "lastGmId");
	}


	public String getSubject() {
		return subject;
	}


	public void setSubject(String subject) {
		this.subject = subject;
		FeedbackManager.em.notifyFieldChange(this, "subject");
	}


	public List<Reply> getList() {
		return list;
	}


	public void setList(List<Reply> list) {
		this.list = list;
		FeedbackManager.em.notifyFieldChange(this, "list");
	}


	public boolean isAlreadyJudge() {
		return alreadyJudge;
	}


	public void setAlreadyJudge(boolean alreadyJudge) {
		this.alreadyJudge = alreadyJudge;
		FeedbackManager.em.notifyFieldChange(this, "alreadyJudge");
	}

	public int getVipLevel() {
		return vipLevel;
	}
	
	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
		FeedbackManager.em.notifyFieldChange(this, "vipLevel");
	}
	
	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		this.servername = servername;
		FeedbackManager.em.notifyFieldChange(this, "servername");
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
		FeedbackManager.em.notifyFieldChange(this, "userName");
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
		FeedbackManager.em.notifyFieldChange(this, "playerName");
	}

	
	public String getSendTimestr() {
		return sendTimestr;
	}

	public void setSendTimestr(String sendTimestr) {
		this.sendTimestr = sendTimestr;
		FeedbackManager.em.notifyFieldChange(this, "sendTimestr");
	}

	public String getDialogs() {
		return dialogCopy;
	}

	public void setDialogs(String dialogs) {
		this.dialogCopy = dialogs;
//+		FeedbackManager.em.notifyFieldChange(this, "dialogs");
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
		FeedbackManager.em.notifyFieldChange(this, "content");
	}
	
	public boolean isFollow() {
		return isFollow;
	}

	public void setFollow(boolean isFollow) {
		this.isFollow = isFollow;
		FeedbackManager.em.notifyFieldChange(this, "isFollow");
	}
	
	@Override
	public void remove(int type) {
		try {
			FeedbackManager.em.save(this);
		} catch (Exception e) {
			FeedbackManager.logger.error("[从缓存删除异常]",e);
		}
	}


	@Override
	public int getSize() {
		return 0;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}
