package com.fy.engineserver.newBillboard.date.pet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.newBillboard.Billboard;
import com.fy.engineserver.newBillboard.BillboardDate;
import com.fy.engineserver.newBillboard.BillboardsManager;
import com.fy.engineserver.newBillboard.IBillboardPlayerInfo;
import com.fy.engineserver.newBillboard.monitorLog.LogRecordManager;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;
import com.fy.engineserver.sprite.pet2.Pet2Manager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;

//全部
public class PetALLBillboard extends Billboard {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2031921686745105950L;
	public transient static PetRankData[] data;
	public transient static PetALLBillboard  inst;
	public transient static Map<Long, Integer> petId2rank = new HashMap<Long, Integer>(200);
	
	public PetALLBillboard(){
		inst = this;
	}
	
	static interface PetInfo{
		
		public long getId();
		public long getPetPropsId();
		public long getOwnerId();
		public String getPetSort();
		public String getName();
		public int getLevel();
		public int getQualityScore();
		public String getCategory();
		public byte getCharacter();
		public boolean isDelete();
	}
	
	
	public static List<PetInfo> getPetInfo(long[] ids){
		List<PetInfo> petList = new ArrayList<PetInfo>();
		if(ids != null && ids.length > 0){
			SimpleEntityManager<Pet> em = PetManager.em;
			try {
				List<PetInfo> temp = new ArrayList<PetInfo>();
				temp = em.queryFields(PetInfo.class, ids);
				for(long id : ids){
					for(PetInfo pi : temp){
						if(pi.getId() == id){
							petList.add(pi);
							break;
						}
					}
				}
			} catch (Exception e) {
				BillboardsManager.logger.error("[排行榜查询宠物信息异常]",e);
			}
		}
		return petList;
	}
	
//	玩家姓名:宠物种族:宠物名字:宠物等级:综合评分
	public void update()throws Exception {
		super.update();
		SimpleEntityManager<Pet> em = PetManager.em;
		long[] ids = em.queryIds(Pet.class, " delete = ? ",new Object[]{"F"},"qualityScore desc ",1,BillboardsManager.实际条数+1);
//		long[] ids = em.queryIds(Pet.class, "",new Object[]{},"qualityScore desc ",1,BillboardsManager.实际条数+100);
		if(ids != null && ids.length > 0){
//			List<PetInfo> petList = em.queryFields(PetInfo.class, ids);
			List<PetInfo> petList = PetALLBillboard.getPetInfo(ids);
			long[] playerIds = new long[petList.size()];
			for(int i=0;i<petList.size();i++){
				playerIds[i] = petList.get(i).getOwnerId();
			}
			List<IBillboardPlayerInfo> playerList = this.getBillboardPlayerInfo(playerIds);
			
			if(petList != null && petList.size() > 0){
				
				PetRankData[] bbds = new PetRankData[petList.size()];
				for(int i=0;i<petList.size();i++){
					PetInfo petInfo = petList.get(i);
					IBillboardPlayerInfo info = null;
					for(IBillboardPlayerInfo playerInfo :playerList){
						if(playerInfo.getId() == petInfo.getOwnerId()){
							info = playerInfo;
							break;
						}
					}
					PetRankData date = new PetRankData();
					date.petId = petInfo.getId();
					date.setDateId(petInfo.getPetPropsId());
					date.setType(BillboardDate.宠物);
					String[] values = new String[5];
					if(info != null){
						values[0] = info.getName();
					}else{
						values[0] = Translate.无;
					}
					values[1] = petInfo.getPetSort();
					values[2] = petInfo.getName();
					values[3] = RobberyConstant.getLevelDes(petInfo.getLevel());
					values[4] = petInfo.getQualityScore()+"";
					
					date.setDateValues(values);
					//add by tigo.k 2013年8月28日18:06:50
					date.category = petInfo.getCategory();
					date.character = petInfo.getCharacter();
					date.score = petInfo.getQualityScore();
					//
					bbds[i] = date;
				}
				setDates(bbds);
				data = bbds; 
				builRankMap();
				BillboardsManager.logger.warn("[更新榜单数据成功] ["+this.getLogString()+"]");
				if(LogRecordManager.getInstance() != null){
					LogRecordManager.getInstance().活动记录日志(LogRecordManager.宠物, this);
				}
			}else{
				BillboardsManager.logger.error("[查询榜单数据没有记录] ["+this.getLogString()+"] [petList:"+(petList != null?petList.size():null)+"] [playerList:"+(playerList != null?playerList.size():null)+"]");
			}
			
		}else{
			BillboardsManager.logger.error("[查询榜单数据错误] [没有记录] ["+this.getLogString()+"]");
		}
		
	}
	
	public static void builRankMap(){
		Logger log = Pet2Manager.log;
		if(data == null){
			log.error("PetALLBillboard.builRankMap: data is null");
			return;
		}
		petId2rank.clear();
		int len = data.length;
		for(int i=0; i<len; i++){
			PetRankData d = data[i];
			petId2rank.put(d.petId, i);
		}
		log.info("PetALLBillboard.builRankMap: make rank map ok.");
	}
}
