package com.fy.boss.gm.gmuser;

import com.fy.boss.gm.gmuser.server.TransferQuestionManager;
import com.fy.boss.gm.gmuser.TransferQuestionForMax;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 浠ｈ〃涓�釜浼犻�鐨勯棶棰�
 * 
 * @author wtx
 * 
 */
@SimpleEntity
@SimpleIndices({ @SimpleIndex(members = { "eventid" }, compress = 1) })
public class TransferQuestion {

	@SimpleId
	protected long id;

	@SimpleVersion
	protected int version;

	public String eventid;

	public String realName;

	public String telephone;

	public String recordTime;

	public String mail;

	public String telephone2;

	public String gameName;

	public String gameQuDao;

	public String username;

	public String playerName;

	public String varsionNum;

	public String resVersionNum;

	public String questionType1;

	public String questionType2;

	public String serverName;

	// 鎻愪氦闂鐨勭殑浜�
	public String handler;

	public String handlBuMeng;

	public String handlOtherBuMeng;
	@SimpleColumn(length = 1024)
	public String questionMess;

	/**
	 * 璇ラ棶棰樻槸鐢盙m鍜岀數璇濆鏈嶆彁浜わ紝鐒跺悗閫掍氦缁欒繍缁存垨鑰呰繍钀ワ紝 杩愮淮锛岃繍钀ユ槸閮芥湁涓�釜璐熻矗浜哄幓缁熶竴绠＄悊鍒嗛厤锛屼笉闇�鑰冭檻鍚屾闂
	 * 0:娌¤澶勭悊锛�:宸茬粡澶勭悊锛�:XXX姝ｅ湪澶勭悊銆傘�
	 */
	public int handleState = 0;
	@SimpleColumn(length = 1024)
	public String reason;

	public String replymess;

	public String viplevel;

	public int transferNum;

	// 褰撳墠澶勭悊浜�
	public String currHadler;

	// 鏂版秷鎭姸鎬�
	public String isNewQuestion;

	// 鐢ㄦ潵鎺у埗椤甸潰娑堟伅鐨勫垹闄�
	public String isDelete;

	// 闂鐨勯�浜ゆ椂闂�
	public long commitTime;

	// 鐜╁鍙嶉娑堟伅id
	public long feedbackid;

	public String getIsGive() {
		return isGive;
	}

	public void setIsGive(String isGive) {
		this.isGive = isGive;
	}

	public String isGive;
	
	public int backNum;

	public long getFeedbackid() {
		return feedbackid;
	}

	public void setFeedbackid(long feedbackid) {
		this.feedbackid = feedbackid;
		TransferQuestionManager.getInstance().sem.notifyFieldChange(this,
				"feedbackid");

	}

	public long getCommitTime() {
		return commitTime;
	}

	public void setCommitTime(long commitTime) {
		this.commitTime = commitTime;
		TransferQuestionManager.getInstance().sem.notifyFieldChange(this,
				"commitTime");
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
		TransferQuestionManager.getInstance().sem.notifyFieldChange(this,
				"isDelete");
	}

	public String getIsNewQuestion() {
		return isNewQuestion;
	}

	public void setIsNewQuestion(String isNewQuestion) {
		this.isNewQuestion = isNewQuestion;
		TransferQuestionManager.getInstance().sem.notifyFieldChange(this,
				"isNewQuestion");
	}

	public int getTransferNum() {
		return transferNum;
	}

	public void setTransferNum(int transferNum) {
		this.transferNum = transferNum;
	}

	public String getViplevel() {
		return viplevel;
	}

	public void setViplevel(String viplevel) {
		this.viplevel = viplevel;
	}

	public String getReplymess() {
		return replymess;
	}

	public void setReplymess(String replymess) {
		this.replymess = replymess;
		TransferQuestionManager.getInstance().sem.notifyFieldChange(this,
				"replymess");
	}

	public String getReason() {
		return reason;
	}

	public TransferQuestionForMax tffm = new TransferQuestionForMax();

	public TransferQuestionForMax getTffm() {
		return tffm;
	}

	public void setTffm(TransferQuestionForMax tffm) {
		this.tffm = tffm;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getEventid() {
		return eventid;
	}

	public void setEventid(String eventid) {
		this.eventid = eventid;
		TransferQuestionManager.getInstance().sem.notifyFieldChange(this,
				"eventid");
	}

	public int getHandleState() {
		return handleState;
	}

	public void setHandleState(int handleState) {
		this.handleState = handleState;
		TransferQuestionManager.getInstance().sem.notifyFieldChange(this,
				"handleState");
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
		TransferQuestionManager.getInstance().sem.notifyFieldChange(this,
				"telephone");
	}

	public String getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
		TransferQuestionManager.getInstance().sem.notifyFieldChange(this,
				"recordTime");
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getTelephone2() {
		return telephone2;
	}

	public void setTelephone2(String telephone2) {
		this.telephone2 = telephone2;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getGameQuDao() {
		return gameQuDao;
	}

	public void setGameQuDao(String gameQuDao) {
		this.gameQuDao = gameQuDao;
	}

	public String getUsername() {
		return username;
	}
	
	

	public int getBackNum() {
		return backNum;
	}

	public void setBackNum(int backNum) {
		this.backNum = backNum;
		TransferQuestionManager.getInstance().sem.notifyFieldChange(this,"backNum");
	}

	public void setUsername(String username) {
		this.username = username;
		TransferQuestionManager.getInstance().sem.notifyFieldChange(this,"username");
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getVarsionNum() {
		return varsionNum;
	}

	public void setVarsionNum(String varsionNum) {
		this.varsionNum = varsionNum;
	}

	public String getResVersionNum() {
		return resVersionNum;
	}

	public void setResVersionNum(String resVersionNum) {
		this.resVersionNum = resVersionNum;
	}

	public String getQuestionType1() {
		return questionType1;
	}

	public void setQuestionType1(String questionType1) {
		this.questionType1 = questionType1;
		TransferQuestionManager.getInstance().sem.notifyFieldChange(this,
				"questionType1");
	}

	public String getQuestionType2() {
		return questionType2;
	}

	public void setQuestionType2(String questionType2) {
		this.questionType2 = questionType2;
		TransferQuestionManager.getInstance().sem.notifyFieldChange(this,
				"questionType2");
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
		TransferQuestionManager.getInstance().sem.notifyFieldChange(this,
				"serverName");
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
		TransferQuestionManager.getInstance().sem.notifyFieldChange(this,
				"handler");
	}

	public String getCurrHadler() {
		return currHadler;
	}

	public void setCurrHadler(String currHadler) {
		this.currHadler = currHadler;
		TransferQuestionManager.getInstance().sem.notifyFieldChange(this,
				"currHadler");
	}

	public String getHandlBuMeng() {
		return handlBuMeng;
	}

	public void setHandlBuMeng(String handlBuMeng) {
		this.handlBuMeng = handlBuMeng;
		TransferQuestionManager.getInstance().sem.notifyFieldChange(this,
				"handlBuMeng");
	}

	public String getHandlOtherBuMeng() {
		return handlOtherBuMeng;
	}

	public void setHandlOtherBuMeng(String handlOtherBuMeng) {
		this.handlOtherBuMeng = handlOtherBuMeng;
		TransferQuestionManager.getInstance().sem.notifyFieldChange(this,
				"handlOtherBuMeng");
	}

	public String getQuestionMess() {
		handleOldData();
		if (tffm != null) {
			return tffm.getQuestionMess();
		}
		return questionMess == null ? "" : questionMess;
	}

	public void setQuestionMess(String questionMess) {
//		this.questionMess = questionMess;
		try{
			handleOldData();
			tffm.setQuestionMess(questionMess);
			// TransferQuestionManager.getInstance().sem.notifyFieldChange(this,
			// "questionMess");
			TransferQuestionManager.getInstance().sem.notifyFieldChange(this,
					"tffm");
			TransferQuestionManager.logger.warn("[setQuestionMess] [鎴愬姛]");
		}catch(Exception  e){
			TransferQuestionManager.logger.warn("[setQuestionMess] [寮傚父]",e);
		}
		
		
	}

	private void handleOldData() {
		if (tffm == null) {
			tffm = new TransferQuestionForMax();
			TransferQuestionManager.getInstance().sem.notifyFieldChange(this,
					"tffm");
		}
		if (tffm.getQuestionMess() == null ) {
			if (questionMess != null) {
				tffm.setQuestionMess(questionMess);
				TransferQuestionManager.getInstance().sem.notifyFieldChange(
						this, "tffm");
				// questionMess = null;
				// TransferQuestionManager.getInstance().sem.notifyFieldChange(
				// this, "questionMess");
				TransferQuestionManager.logger.warn("[handleOldData] [tffm.getQuestionMess() == null]");
			}
		}
	}
}
