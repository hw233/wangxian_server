package com.fy.boss.gm.gmuser;

import com.fy.boss.gm.gmuser.server.TransferQuestionManager;
import com.fy.boss.gm.gmuser.QuestionQueryForMax;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 一条问题转移的记录
 * @author wtx
 *
 */
@SimpleEntity
@SimpleIndices({
	@SimpleIndex(members={"eventid"},compress=1)
})
public class QuestionQuery {

	@SimpleId
	protected long id;
	
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
		TransferQuestionManager.getInstance().sem2.notifyFieldChange(this, "eventid");
	}

	@SimpleVersion
	protected int version;
	
	private String eventid;
	
	private String handleer;
	
	private String handledoor;
	
	private int state;
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getHandletodoor() {
		return handletodoor;
	}

	public void setHandletodoor(String handletodoor) {
		this.handletodoor = handletodoor;
		TransferQuestionManager.getInstance().sem2.notifyFieldChange(this, "handletodoor");
	}

	private String handletime;
	@SimpleColumn(length = 1024)
	private String questionmess;
	
	private String handletodoor;


	public String getHandleer() {
		return handleer;
	}

	public void setHandleer(String handleer) {
		this.handleer = handleer;
		TransferQuestionManager.getInstance().sem2.notifyFieldChange(this, "handleer");
	}

	public String getHandledoor() {
		return handledoor;
	}

	public void setHandledoor(String handledoor) {
		this.handledoor = handledoor;
		TransferQuestionManager.getInstance().sem2.notifyFieldChange(this, "handledoor");
	}

	public String getHandletime() {
		return handletime;
	}

	public void setHandletime(String handletime) {
		this.handletime = handletime;
		TransferQuestionManager.getInstance().sem2.notifyFieldChange(this, "handletime");
	}

	public String getQuestionmess() {
		handleOldData();
		if(qqfm!=null){
			return qqfm.getQuestionMess();
		}
		return questionmess==null ? "":"questionmess";
	}

	public QuestionQueryForMax qqfm = new QuestionQueryForMax();
	
	public QuestionQueryForMax getQqfm() {
		return qqfm;
	}

	public void setQqfm(QuestionQueryForMax qqfm) {
		this.qqfm = qqfm;
	}

	public void setQuestionmess(String questionmess) {
//		this.questionmess = questionmess;
		handleOldData();
		qqfm.setQuestionMess(questionmess);
		TransferQuestionManager.getInstance().sem2.notifyFieldChange(this, "qqfm");
//		TransferQuestionManager.getInstance().sem2.notifyFieldChange(this, "questionmess");
	}
	
	public void handleOldData(){
		if(qqfm==null){
			qqfm = new QuestionQueryForMax();
			TransferQuestionManager.getInstance().sem2.notifyFieldChange(this, "qqfm");
		}
		if(qqfm.getQuestionMess()==null){
			if(questionmess!=null){
				qqfm.setQuestionMess(questionmess);
				TransferQuestionManager.getInstance().sem2.notifyFieldChange(this, "qqfm");
			}
		}
	}

}
