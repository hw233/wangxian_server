package com.fy.engineserver.newBillboard.date.charm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.newBillboard.Billboard;
import com.fy.engineserver.newBillboard.BillboardDate;
import com.fy.engineserver.newBillboard.BillboardsManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
/**
 * 远征
 */
public class YuanZhenBillboard extends Billboard{
	
	public static String format2(double value) {
		return String.format("%.2f", value).toString();
	}
	
	public void update()throws Exception {
		super.update();
		SimpleEntityManager<Jiazu> em = JiazuManager.jiazuEm;
		long[] ids = em.queryIds(Jiazu.class, " bossLevel > ? AND weekOfy > ? ",new Object[]{0,0},"bossLevel desc",1,111);
		//排行榜统计数据
		BillboardsManager.logger.warn("[更新远征排行] [ids:"+(ids!=null?ids.length:"null")+"]");
		if(ids != null && ids.length > 0){
			List<Jiazu> sList = new ArrayList<Jiazu>();
			for(long id : ids){
				Jiazu jiazu = JiazuManager.getInstance().getJiazu(id);
				sList.add(jiazu);
			}
			
			Jiazu [] bbds = sList.toArray(new Jiazu[]{});
			//boss级别后，再按照伤害百分比，用时排序
			Arrays.sort(bbds, new Comparator<Jiazu>() {
				@Override
				public int compare(Jiazu o1, Jiazu o2) {
					int bosslevel1 = o1.getBossLevel();
					int bosslevel2 = o2.getBossLevel();
					double damage1 = o1.getDamageProp();
					double damage2 = o2.getDamageProp();
					long costTime1 = o1.getCostTime();
					long costTime2 = o2.getCostTime();
					if(bosslevel1 != bosslevel2){
						return bosslevel2 - bosslevel1;
					}
					if(damage1 != damage2){
						return damage1 > damage2 ? 0 : 1;
					}
					if(costTime1 != costTime2){
						return costTime1 > costTime2 ? 0 : 1;
					}
					return 0;
				}
			});
//			BOSS:伤害百分比:所用时间:家族
			BillboardDate ds [] = new BillboardDate[bbds.length];
			for(int i=0;i<bbds.length;i++){
				Jiazu d = bbds[i];
				BillboardDate date = new BillboardDate();
				date.setDateId(d.getJiazuID());
				date.setType(BillboardDate.家族);
				String[] values = new String[4];
				values[0] = d.getBossName();
				values[1] = format2(d.getDamageProp() * 100)+"%";
				values[2] = d.getCostTime()/1000+"秒";
				values[3] = d.getName();
				date.setDateValues(values);
				ds[i] = date;
			}
			
			setDates(ds);
			BillboardsManager.logger.warn("[更新榜单数据成功] [ds:"+ds.length+"] ["+this.getLogString()+"]");
		}else{
			BillboardDate[] bbds = new BillboardDate[0];
			setDates(bbds);
			BillboardsManager.logger.error("[查询榜单数据错误] [没有记录] ["+this.getLogString()+"]");
		}
		
	}
	

}
