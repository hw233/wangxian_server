package com.fy.engineserver.playerAims.instance;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.playerAims.manager.PlayerAimeEntityManager;
import com.fy.engineserver.playerAims.model.ChapterModel;
import com.fy.engineserver.playerAims.model.PlayerAimModel;
import com.xuanzhi.tools.cache.CacheListener;
import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimplePostPersist;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
public class PlayerAimsEntity  implements Cacheable, CacheListener{
	@SimpleId
	private long id;
	@SimpleVersion
	private int version;
	/** 玩家已完成的目标 */
	@SimpleColumn(length = 50000)
	private List<PlayerAim> aimList = new ArrayList<PlayerAim>();
	@SimpleColumn(length = 10000)
	private List<PlayerChapter> chapterList = new ArrayList<PlayerChapter>();
	/** 玩家当前获得的总积分 */
	private transient int totalScore = -1;
	
	public PlayerAimsEntity(){}

	public PlayerAimsEntity(long playerId) {
		// TODO Auto-generated constructor stub
		this.id = playerId;
		PlayerAimeEntityManager.em.notifyFieldChange(this, "id");
	}
	
	public int checkReceiveStatus() {
		int result = 0;
		for(PlayerAim pa : aimList) {
			if(pa.getReveiveStatus() == PlayerAimeEntityManager.UN_RECEIVE) {
				result++;
			}
		}
		for(PlayerChapter pc : chapterList) {
			if(pc.getReceiveType() == PlayerAimeEntityManager.UN_RECEIVE) {
				result++;
			}
		}
		return result;
	}
	
	@SimplePostPersist
	public void saved() {
		PlayerAimsEntity dt = PlayerAimeEntityManager.instance.tempCache.remove(this.getId());
		if(PlayerAimeEntityManager.logger.isDebugEnabled()) {
			PlayerAimeEntityManager.logger.debug("[移除PlayerAimsEntity] [" + dt + "] [" + this + "]");
		}
	}
	/**
	 * 判断目标是否完成
	 * @param aimId
	 * @return
	 */
	public boolean isAimCompleted(int aimId) {
		for(PlayerAim pa : aimList) {
			if(pa.getAimId() == aimId) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取 目标奖励领取状态
	 * @param aimId
	 * @return
	 */
	public synchronized byte getAimReceiveStatus(int aimId) {
		byte status = -1;
		for(PlayerAim pa : aimList) {
			if(pa.getAimId() == aimId) {
				return pa.getReveiveStatus();
			}
		}
		return status;
	}
	/**
	 * 获取目标完成时间
	 * @param aimId
	 * @return
	 */
	public long getAimCompleteTime(int aimId) {
		for(PlayerAim pa : aimList) {
			if(pa.getAimId() == aimId) {
				return pa.getCompletTime();
			}
		}
		return 0L;
	}
	/**
	 * 获取章节奖励领取状态
	 * @param chapterName
	 * @return
	 */
	public synchronized byte getChapterReceiveStatus(String chapterName) {
		byte status = -1;
		for(PlayerChapter pc : chapterList) {
			if(pc.getChapterName().equals(chapterName)) {
				status = pc.getReceiveType();
				if (status < 0) {
					ChapterModel cm = PlayerAimManager.instance.chapterMaps.get(chapterName);
					if (cm.getScoreLimit() <= pc.getScore()) {
						pc.setReceiveType((byte)0);
						status = 0;
						PlayerAimeEntityManager.em.notifyFieldChange(this, "chapterList");
					}
				}
				return status;
			}
		}
		return status;
	}
	
	public List<PlayerAim> getAimList() {
		return aimList;
	}

	public void setAimList(List<PlayerAim> aimList) {
		this.aimList = aimList;
		PlayerAimeEntityManager.em.notifyFieldChange(this, "aimList");
	}

	@Override
	public void remove(int type) {
		// TODO Auto-generated method stub
		if (type == CacheListener.LIFT_TIMEOUT) {
			PlayerAimeEntityManager.instance.notifyRemoveFromCache(this);
		}
	}
	/**
	 * 更新玩家章节总积分
	 * @param cm
	 * @return
	 */
	public int updateChapterScore(ChapterModel cm, boolean isUpdate) {
		int score = 0;
		for(PlayerAimModel pam : cm.getAimsList()) {
			for(PlayerAim pa : aimList) {
				if(pam.getId() == pa.getAimId()) {
					score += pam.getScore();
				}
			}
		}
		if(isUpdate) {
			boolean exist = false;
			for(PlayerChapter pc : getChapterList()) {
				if(pc.getChapterName().equals(cm.getChapterName())) {
					pc.setScore(score);
					exist = true;
					break;
				}
			}
			if(!exist) {
				List<PlayerChapter> cList = this.getChapterList();
				PlayerChapter pc = new PlayerChapter();
				pc.setChapterName(cm.getChapterName());
				pc.setScore(score);
				if (score < cm.getScoreLimit()) {
					pc.setReceiveType((byte) -1);
				} else if (pc.getReceiveType() < 0) {
					pc.setReceiveType((byte) 0);
				}
				cList.add(pc);
				this.setChapterList(cList);
			}
		}
		return score;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 1;
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

	public List<PlayerChapter> getChapterList() {
		return chapterList;
	}

	public void setChapterList(List<PlayerChapter> chapterList) {
		this.chapterList = chapterList;
		PlayerAimeEntityManager.em.notifyFieldChange(this, "chapterList");
	}

	public int getTotalScore() {
//		if(totalScore < 0) {
		totalScore = 0 ;
		for(PlayerAim pa : aimList) {
			PlayerAimModel pam = PlayerAimManager.instance.aimMaps.get(pa.getAimId());
			if(pam == null) {
				PlayerAimManager.logger.warn("[目标系统] [玩家已完成的目标异常] [目标id:" + pa.getAimId() + "] [角色id:" + this.getId() + "]");
				continue;
			}
			totalScore += pam.getScore();
//			}
		}
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

}
