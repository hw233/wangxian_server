package com.fy.engineserver.sprite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.fy.engineserver.activity.fateActivity.FateActivityType;
import com.fy.engineserver.activity.fateActivity.FateManager;
import com.fy.engineserver.activity.fateActivity.FateTemplate;
import com.fy.engineserver.activity.fateActivity.FinishRecord;
import com.fy.engineserver.activity.fateActivity.base.FateActivity;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.sprite.horse.dateUtil.DateFormat;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class ActivityRecordOnPlayer {

	
	//主动参加活动id  活动实体id
	private long initiativeActivityId = -1;
	//被参加活动id
	private long passivityActivityId = -1;
	//主动完成次数
	private byte initiativeNum;
	//被动完成次数
	private byte passivityNum;
	
	
	private long lastUpdateNumTime;
	/**
	 * 上次完成活动的玩家id
	 */
	private long lastFinishActivityPlayerId;
	
	
	public ActivityRecordOnPlayer() {
		
	}
	@Override
	protected void finalize() throws Throwable {
		
	}
	
	/**
	 * 已经完成的活动的记录
	 * 最大50条
	 */
	private ArrayList<FinishRecord> finishRecordList = new ArrayList<FinishRecord>();
	
	
	public long getLastFinishActivityPlayerId() {
		return lastFinishActivityPlayerId;
	}
	public void setLastFinishActivityPlayerId(long lastFinishActivityPlayerId) {
		this.lastFinishActivityPlayerId = lastFinishActivityPlayerId;
	}
	public List<FinishRecord> getFinishRecordList() {
		return finishRecordList;
	}
	public void setFinishRecordList(ArrayList<FinishRecord> finishRecordList) {
		this.finishRecordList = finishRecordList;
	}
	
	/**
	 * 更新完成次数
	 */
	public void enterUpdateNum(byte type,Player player){
		
		String oldst = DateFormat.getSimpleDay(new Date(lastUpdateNumTime));
		String nowst = DateFormat.getSimpleDay(new Date(SystemTime.currentTimeMillis()));
		if(!oldst.equals(nowst)){
			lastUpdateNumTime = SystemTime.currentTimeMillis();
			initiativeNum = 0;
			passivityNum = 0;
			
			if (type == FateActivityType.国内仙缘.getType()) {
				player.setDirty(true, "fate");
			} else if (type == FateActivityType.国外仙缘.getType()) {
				player.setDirty(true, "abroadFate");
			} else if (type == FateActivityType.国内论道.getType()) {
				player.setDirty(true, "beer");
			} else if (type == FateActivityType.国外论道.getType()) {
				player.setDirty(true, "abroadBeer");
			}
		}
		
		
		
	}
	public long getInitiativeActivityId() {
		return initiativeActivityId;
	}
	public void setInitiativeActivityId(long initiativeActivityId) {
		this.initiativeActivityId = initiativeActivityId;
	}
	public void setPassivityActivityId(long passivityActivityId) {
		this.passivityActivityId = passivityActivityId;
	}
	public void setInitiativeActivityId(long initiativeActivityId,Player player,byte type) {
		this.initiativeActivityId = initiativeActivityId;
		if (type == FateActivityType.国内仙缘.getType()) {
			player.setDirty(true, "fate");
		} else if (type == FateActivityType.国外仙缘.getType()) {
			player.setDirty(true, "abroadFate");
		} else if (type == FateActivityType.国内论道.getType()) {
			player.setDirty(true, "beer");
		} else if (type == FateActivityType.国外论道.getType()) {
			player.setDirty(true, "abroadBeer");
		}
	}
	
	public long getPassivityActivityId() {
		return passivityActivityId;
	}
	public void setPassivityActivityId(long passivityActivityId,Player player,byte type) {
		this.passivityActivityId = passivityActivityId;
		if (type == FateActivityType.国内仙缘.getType()) {
			player.setDirty(true, "fate");
		} else if (type == FateActivityType.国外仙缘.getType()) {
			player.setDirty(true, "abroadFate");
		} else if (type == FateActivityType.国内论道.getType()) {
			player.setDirty(true, "beer");
		} else if (type == FateActivityType.国外论道.getType()) {
			player.setDirty(true, "abroadBeer");
		}
	}
	public byte getInitiativeNum() {
		return initiativeNum;
	}
	public void setInitiativeNum(byte initiativeNum) {
		this.initiativeNum = initiativeNum;
	}
	public byte getPassivityNum() {
		return passivityNum;
	}
	public void setPassivityNum(byte passivityNum) {
		this.passivityNum = passivityNum;
	}
	
	//得到活动的id
	public long[] getActivityId(){
		return new long[]{initiativeActivityId,passivityActivityId};
	}
	
	/**
	 * 检查数量，防止过大
	 */
	protected synchronized void checkFinishRecordList(){
		if(finishRecordList.size() > 50){
			FinishRecord frs[] = finishRecordList.toArray(new FinishRecord[0]);
			Arrays.sort(frs, new FinishRecord.FinishRecordComparator());
			
			for(int i = frs.length -1 ; i >= 30 ; i--){
				finishRecordList.remove(frs[i]);
			}
		}
		
	}
	
	// 添加完成记录
	public void addFinishRecord(Player player,boolean passive,byte type){
		

		if(passive){
			//被动
			++passivityNum;
			passivityActivityId = -1;
		}else{
			++initiativeNum;
			initiativeActivityId = -1;

		}
		
		checkFinishRecordList();
		
		if(player != null){
			setLastFinishActivityPlayerId(player.getId());
			boolean temp = true;
			for(FinishRecord fr :finishRecordList){
				if(fr.getPlayerId() == player.getId()){
					temp = false;
					fr.add();
					break;
				}
			}
			if(temp){
				finishRecordList.add(new FinishRecord(player.getId(),1));
			}
			if (FateManager.logger.isWarnEnabled()){
				FateManager.logger.warn("[完成添加完成记录] ["+finishRecordList.size()+"] ["+player.getLogString()+"] ["+type+"] ["+passive+"]");
			}
			
			if (type == FateActivityType.国内仙缘.getType()) {
				player.setDirty(true, "fate");
			} else if (type == FateActivityType.国外仙缘.getType()) {
				player.setDirty(true, "abroadFate");
			} else if (type == FateActivityType.国内论道.getType()) {
				player.setDirty(true, "beer");
			} else if (type == FateActivityType.国外论道.getType()) {
				player.setDirty(true, "abroadBeer");
			}
		}

		
	}
	
	//某人可以被邀请
	public boolean isCanInvited(FateTemplate ft){
		
		//没有主动  主动没选人
		if(initiativeActivityId < 0){
			
		}else{
			FateActivity fa = FateManager.getInstance().getFateActivity(initiativeActivityId);
			if( fa != null && fa.getState() >= FateActivity.选人成功){
				return false;
			}
		}
		
		if(passivityNum < ft.getPerNum()){
			if(passivityActivityId < 0){
				return true;
			}else{
				FateActivity fa = FateManager.getInstance().getFateActivity(passivityActivityId);
				if(fa != null && fa.getState() >= FateActivity.选人成功){
					return false;
				}
				return true;
			}
		}else{
			return false;
		}
	}
	
	// 某人可以邀请人
	public boolean isCanInvite(FateTemplate ft){
		
		if(initiativeActivityId < 0){
			return false;
		}else{
			FateActivity fa = FateManager.getInstance().getFateActivity(initiativeActivityId);
			if(fa != null && fa.getState() >= FateActivity.进行状态){
				return false;
			}
		}
		
		if(passivityActivityId < 0){
			return true;
		}else{
			FateActivity fa = FateManager.getInstance().getFateActivity(passivityActivityId);
			if(fa != null && fa.getState() >= FateActivity.进行状态){
				return false;
			}
		}
		return true;
		
	}
	
}
