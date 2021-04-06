package com.fy.engineserver.newBillboard.date.cave;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.cave.CaveMainBuilding;
import com.fy.engineserver.homestead.faery.Faery;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.newBillboard.Billboard;
import com.fy.engineserver.newBillboard.BillboardDate;
import com.fy.engineserver.newBillboard.BillboardsManager;
import com.fy.engineserver.newBillboard.date.cave.CaveScoreALLBillboard.CaveCompare;
import com.fy.engineserver.sprite.PlayerSimpleInfo;
import com.fy.engineserver.sprite.PlayerSimpleInfoManager;

//九州
public class CaveScoreCountry2Billboard extends Billboard {

	
//	显示内容：名次、角色ID、村庄、主建筑等级、分数
	
	
	public void update()throws Exception {
		super.update();
		FaeryManager fm = FaeryManager.getInstance();
		Map<Long, Cave> map = fm.getCaveIdmap();
//		Map<Long, Cave> map = fm.getPlayerCave();
		List<Cave> list = new ArrayList<Cave>();
		if(map != null && map.size() > 0){
			Set<Long> set =  map.keySet();
			Iterator<Long> it = set.iterator();
			while(it.hasNext()){
				long id = it.next();
				Cave cave = map.get(id);
				if(cave != null){
					Faery fa = cave.getFaery();
					if(fa != null && fa.getCountry() == 2){
						list.add(cave);
					}
				}
			}
			Collections.sort(list, new CaveCompare());
			
			if(list.size() >= 200){
				list = list.subList(0, 200);
			}
			
			BillboardDate[] bbds = new BillboardDate[list.size()];
			for(int i =0;i<list.size();i++){
				Cave cave = list.get(i);
				long playerId = cave.getOwnerId();
				Faery fa = cave.getFaery();
				CaveMainBuilding cmb = cave.getMainBuilding();
				BillboardDate date = new BillboardDate();
				date.setDateId(playerId);
				date.setType(BillboardDate.玩家);
				
				String[] values = new String[4];
				PlayerSimpleInfo info = PlayerSimpleInfoManager.getInstance().getInfoById(playerId);
				if(info != null){
					values[0] = info.getName();
				}else{
					values[0] = Translate.无;
				}
				if(fa != null){
					values[1] = fa.getName();
				}else{
					values[1] = Translate.无;
				}
				values[2] = cmb.getGrade()+"";
				values[3] = cave.getCaveScore()+"";
				
				date.setDateValues(values);
				bbds[i] = date;
			}
			setDates(bbds);
			BillboardsManager.logger.warn("[更新榜单数据成功] ["+this.getLogString()+"]");
		}else{
			BillboardsManager.logger.warn("[更新榜单数据失败] [没有数据] ["+this.getLogString()+"]");
		}
		
	}
	
	
}
