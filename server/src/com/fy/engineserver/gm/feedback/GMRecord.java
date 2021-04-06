package com.fy.engineserver.gm.feedback;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.gm.feedback.service.FeedbackManager;
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
	@SimpleIndex(members={"gmName"})
})
public class GMRecord implements Cacheable, CacheListener {
	
	@SimpleId
	long id;
	@SimpleVersion
	private int version;

	@SimpleColumn(length=50)
	String gmName;
	
	//Gm 回复的反馈id
	@SimpleColumn(length=40000)
	List<Long> recordList;
	
	//得分的id
	@SimpleColumn(length=40000)
	List<Long> scoreList;
	
	public GMRecord(){
		
	}
	public GMRecord(long id){
		this.id = id;
		this.recordList = new ArrayList<Long>();
		this.scoreList = new ArrayList<Long>();
	}
	
	public void clear(){
		
	}
	
	public String getLogString(){
		return "GMname"+gmName;
	}
	
	
	/**
	 * 检查数量，防止过大
	 */
	protected synchronized void checkList(){
	
		
	}
	
	
	//GM 回复反馈
	public void gmReply(long id){
		if(this.getRecordList() == null){
			this.recordList = new  ArrayList<Long>();
		}
		if(!this.getRecordList().contains(id)){
			
			if(this.getRecordList().size() >= 1000){
				for(int i = 0 ; i <= 100 ; i++){
					getRecordList().remove(i);
				}
			}
			this.getRecordList().add(id);
			setRecordList(this.getRecordList());
		}
		
	}
	
	//GM 得到评价
	public synchronized void gmScore(long id){
		
		if(this.getScoreList() == null){
			this.scoreList = new  ArrayList<Long>();
		}
		if(!this.getScoreList().contains(id)){
			
			if(this.getScoreList().size() >= 1000){
				for(int i = 0 ; i <= 100 ; i++){
					getScoreList().remove(i);
				}
			}
			
			this.getScoreList().add(id);
			setScoreList(this.getScoreList());
		}
		
	}
	
/******************************************************************************************/
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getGmName() {
		if(gmName == null){
			return "";
		}
		return gmName;
	}

	public void setGmName(String gmName) {
		this.gmName = gmName;
		FeedbackManager.gmem.notifyFieldChange(this, "gmName");
	}

	public List<Long> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<Long> recordList) {
		this.recordList = recordList;
		FeedbackManager.gmem.notifyFieldChange(this, "recordList");
	}

	public List<Long> getScoreList() {
		return scoreList;
	}

	public void setScoreList(List<Long> scoreList) {
		this.scoreList = scoreList;
		FeedbackManager.gmem.notifyFieldChange(this, "scoreList");
	}

	@Override
	public int getSize() {
		return 0;
	}

	@Override
	public void remove(int type) {
		try {
			FeedbackManager.gmem.save(this);
		} catch (Exception e) {
			FeedbackManager.logger.error("[从缓存删除异常]",e);
		}
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
}
