package com.fy.engineserver.masterAndPrentice;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class MasterPrentice{

	/**
	 * 师傅id
	 */
	private long  masterId = -1;
	
	/**
	 * 徒弟id列表
	 */

	private List<Long> prentices = new ArrayList<Long>();
	
	/**
	 * 背叛师傅 false  背叛 三天不上线背叛还为false
	 */
	private boolean rebel = true;
	
	
	/**
	 * 背叛师傅时间
	 */
	private long rebelTime;
	
	/**
	 * 逐出徒弟状态(false)    三天不上线逐出还为false
	 */
	private boolean evict = true;
	
	/**
	 * 逐出徒弟时间
	 */
	private long evictTime;

	public long getMasterId() {
		return masterId;
	}

	public void setMasterId(long masterId) {
		this.masterId = masterId;
	}

	public List<Long> getPrentices() {
		return prentices;
	}

	public void setPrentices(List<Long> prentices) {
		this.prentices = prentices;
	}

	public boolean isRebel() {
		return rebel;
	}

	public void setRebel(boolean rebel) {
		this.rebel = rebel;
	}

	public long getRebelTime() {
		return rebelTime;
	}

	public void setRebelTime(long rebelTime) {
		this.rebelTime = rebelTime;
	}

	public boolean isEvict() {
		return evict;
	}

	public void setEvict(boolean evict) {
		this.evict = evict;
	}

	public long getEvictTime() {
		return evictTime;
	}

	public void setEvictTime(long evictTime) {
		this.evictTime = evictTime;
	}
	
	/**
	 * 背叛师傅
	 */
	public void rebelMaster(){
		Player master = null;
		try {
			master = PlayerManager.getInstance().getPlayer(this.masterId);
		} catch (Exception e) {
			MasterPrenticeManager.logger.error("[背叛师傅异常] [没有找着师傅] ["+masterId+"]");
		}
		
		if(master != null){
			if(SystemTime.currentTimeMillis() - master.getEnterGameTime() > MasterPrenticeManager.师傅不上线判师没惩罚时间){
				
			}else{
				this.setRebel(false);
				this.setRebelTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
			}
		}
		this.setMasterId(-1);
	}
	/**
	 * 背叛师傅后师傅自动删除徒弟
	 * @param id
	 */
	public void autoRemovePrentice(long id){
		this.getPrentices().remove(id);
	}
	
	/**
	 * 逐出徒弟
	 * @param id
	 */
	public void evictPrentice(long id){
		
		Player prentice = null;
		try {
			prentice = PlayerManager.getInstance().getPlayer(id);
		} catch (Exception e) {
			MasterPrenticeManager.logger.error("[逐徒异常] [没有找着徒弟] ["+id+"]");
		}
		
		if(prentice != null){
			if(SystemTime.currentTimeMillis() - prentice.getEnterGameTime() > MasterPrenticeManager.徒弟不上线逐徒没惩罚时间){
				
			}else{
				this.setEvict(false);
				this.setEvictTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
			}
		}
		this.getPrentices().remove(id);
	}
	
	/**
	 * 逐出徒弟后徒弟自动删除师傅
	 */
	public void autoRemoveMaster(){
		this.masterId = -1;
	}
	
	/**
	 * 收徒
	 * @param id
	 */
	public void takePrentice(long id){
		this.getPrentices().add(id);
	}
	
	/**
	 * 拜师
	 * @param id
	 */
	public void takeMaster(long id){
		this.masterId = id;
	}
	
}
