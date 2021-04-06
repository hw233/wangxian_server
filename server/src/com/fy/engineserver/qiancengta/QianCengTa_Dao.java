package com.fy.engineserver.qiancengta;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.qiancengta.info.RewardMsg;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 此对象需要存储
 * 
 * 
 */

@SimpleEmbeddable
public class QianCengTa_Dao {
	public static Logger logger = LoggerFactory.getLogger(QianCengTa_Ta.class);
	
	//道的索引
	private int daoIndex;
	
	//当前层  挑战的那层
	private int currentCengIndex;
	
	//最高打到多少层，从第0层开始，一层没有打过为-1
	private int maxReachCengIndex;
	
	private boolean isOpenHidden = false;
	
	private QianCengTa_Ceng hiddenCeng;
	
	public transient QianCengTa_Ta ta;
	
	private ArrayList<QianCengTa_Ceng> cengList = new ArrayList<QianCengTa_Ceng>();
	
	public QianCengTa_Ceng getCeng(int cengIndex){
		if(cengIndex<0||cengIndex>=cengList.size()){
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [获取状态信息] [道数组下标越界]  [daoIndex:"+daoIndex+"]");
			return null;
		}
		return cengList.get(cengIndex);
	}
	
	public int getCengCount(){
		return cengList.size();
	}
	
	public int getMaxReachCengIndex() {
		return maxReachCengIndex;
	}
	public void setMaxReachCengIndex(int maxReachCengIndex) {
		this.maxReachCengIndex = maxReachCengIndex;
	}

	public ArrayList<QianCengTa_Ceng> getCengList() {
		return cengList;
	}

	public void setCengList(ArrayList<QianCengTa_Ceng> cengList) {
		this.cengList = cengList;
	}
	
	public int getDaoIndex() {
		return daoIndex;
	}

	public void setDaoIndex(int daoIndex) {
		this.daoIndex = daoIndex;
	}
	
	//取这道的所有奖励
	public ArrayList<RewardMsg> getRewardIds(){
		ArrayList<RewardMsg> rewardMsg = new ArrayList<RewardMsg>();
		if (hiddenCeng != null) {
			if (hiddenCeng.rewardStatus == QianCengTa_Ceng.STATUS_奖励未领取) {
				rewardMsg.add(hiddenCeng.getRewardMsg());
			}
		}
		for(QianCengTa_Ceng ceng : cengList){
			if(ceng.rewardStatus!=QianCengTa_Ceng.STATUS_奖励未领取){
				continue;
			}
			if(ceng.getRewards().size()<=0){
				continue;
			}
			RewardMsg rm = ceng.getRewardMsg();
			rewardMsg.add(rm);
		}
		return rewardMsg;
	}
	
	public void flush(){
		setCurrentCengIndex(0);
		for (int i = 0; i < cengList.size(); i++) {
			QianCengTa_Ceng ceng = cengList.get(i);
			ceng.clear();
		}
		cengList.clear();
		QianCengTa_Ceng ceng = new QianCengTa_Ceng();
		ceng.setCengIndex(0);
		ceng.dao = this;
		isOpenHidden = false;
		hiddenCeng = null;
		cengList.add(ceng);
	}

	public void setCurrentCengIndex(int currentCengIndex) {
		this.currentCengIndex = currentCengIndex;
	}

	public int getCurrentCengIndex() {
		return currentCengIndex;
	}

	public void setOpenHidden(boolean isOpenHidden) {
		this.isOpenHidden = isOpenHidden;
	}

	public boolean isOpenHidden() {
		return isOpenHidden;
	}
	
	public void openCeng(){
		QianCengTa_Ceng ceng = new QianCengTa_Ceng();
		ceng.setCengIndex(getCurrentCengIndex());
		ceng.dao = this;
		logger.warn("[开启新层] [{}] [D={}] [CI={}] [MI={}]", new Object[]{ta.getPlayer().getLogString(), daoIndex, getCurrentCengIndex(), getMaxReachCengIndex()});
		cengList.add(ceng);
	}

	public void setHiddenCeng(QianCengTa_Ceng hiddenCeng) {
		this.hiddenCeng = hiddenCeng;
	}

	public QianCengTa_Ceng getHiddenCeng() {
		return hiddenCeng;
	}
}
