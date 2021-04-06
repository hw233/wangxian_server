package com.fy.engineserver.newBillboard.date.pet;

import java.util.List;

import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.newBillboard.Billboard;
import com.fy.engineserver.newBillboard.BillboardDate;
import com.fy.engineserver.newBillboard.BillboardsManager;
import com.fy.engineserver.newBillboard.IBillboardPlayerInfo;
import com.fy.engineserver.newBillboard.date.pet.PetALLBillboard.PetInfo;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;

//昆仑
public class PetCountry1Billboard extends Billboard {

	//	//于title对应
	//	private String[] dateValues;  显示内容： 玩家姓名 宠物种族、宠物名字 宠物等级 综合评分
	
	public void update()throws Exception {
		super.update();
		SimpleEntityManager<Pet> em = PetManager.em;
		long[] ids = em.queryIds(Pet.class, " delete = ? AND country = ? ",new Object[]{"F",1},"qualityScore desc ",1,BillboardsManager.实际条数+1);
//		long[] ids = em.queryIds(Pet.class, "country = ? ",new Object[]{1},"qualityScore desc ",1,BillboardsManager.实际条数+100);
		if(ids != null && ids.length > 0){
//			List<PetInfo> petList = em.queryFields(PetInfo.class, ids);
			List<PetInfo> petList = PetALLBillboard.getPetInfo(ids);
			long[] playerIds = new long[petList.size()];
			for(int i=0;i<petList.size();i++){
				playerIds[i] = petList.get(i).getOwnerId();
			}
			List<IBillboardPlayerInfo> playerList = this.getBillboardPlayerInfo(playerIds);
			
			if(petList != null && petList.size() > 0 ){
				BillboardDate[] bbds = new BillboardDate[petList.size()];
				for(int i=0;i<petList.size();i++){
					PetInfo petInfo = petList.get(i);
					IBillboardPlayerInfo info = null;
					for(IBillboardPlayerInfo playerInfo :playerList){
						if(playerInfo.getId() == petInfo.getOwnerId()){
							info = playerInfo;
							break;
						}
					}
					BillboardDate date = new BillboardDate();
//					date.setDateId(petInfo.getId());
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
					bbds[i] = date;
					
				}
				setDates(bbds);
				BillboardsManager.logger.warn("[更新榜单数据成功] ["+this.getLogString()+"]");
			}else{
				BillboardsManager.logger.error("[查询榜单数据没有记录] ["+this.getLogString()+"] [petList:"+(petList != null?petList.size():null)+"] [playerList:"+(playerList != null?playerList.size():null)+"]");
			}
			
		}else{
			BillboardsManager.logger.error("[查询榜单数据错误] [没有记录] ["+this.getLogString()+"]");
		}
		
	}
	
}
