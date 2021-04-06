package com.fy.engineserver.newBillboard.date.jiazu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.newBillboard.Billboard;
import com.fy.engineserver.newBillboard.BillboardDate;
import com.fy.engineserver.newBillboard.BillboardsManager;
import com.fy.engineserver.newBillboard.date.jiazu.JiazuProsperityALLBillboard.JiazuCompare;
import com.fy.engineserver.sprite.PlayerSimpleInfo;
import com.fy.engineserver.sprite.PlayerSimpleInfoManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;

//昆仑
public class JiazuProsperity1Billboard extends Billboard {

	
	//	private String[] dateValues;  显示内容：jiazuID、国家、积分；
//	家族名称:族长名称:国家:积分
	public void update()throws Exception {
		super.update();
		SimpleEntityManager<Jiazu> em = JiazuManager.jiazuEm;
		long[] ids = em.queryIds(Jiazu.class, " country = ? ",new Object[]{1},"prosperity desc",1,BillboardsManager.实际条数+1);
		if(ids != null){
			List<Jiazu> list = new ArrayList<Jiazu>();
			for(int i =0;i<ids.length;i++){
				if(i >= 200){
					break;
				}
				Jiazu jiazu = JiazuManager.getInstance().getJiazu(ids[i]);
				if(jiazu != null){
					list.add(jiazu);
				}
			}
			Collections.sort(list, new JiazuCompare());
			
			BillboardDate[] bbds = new BillboardDate[list.size()];
			for(int i=0;i<list.size();i++){
				Jiazu jiazu = list.get(i);
				BillboardDate date = new BillboardDate();
				date.setDateId(jiazu.getJiazuID());
				date.setType(BillboardDate.家族);

				String[] values = new String[4];
				values[0] = jiazu.getName();
				
				PlayerSimpleInfo info = PlayerSimpleInfoManager.getInstance().getInfoById(jiazu.getJiazuMaster());
				if(info != null){
					values[1] = info.getName();
				}else{
					values[1] = Translate.无;
				}
				
				values[2] = CountryManager.得到国家名(jiazu.getCountry());
				values[3] = jiazu.getProsperity()+"";
				date.setDateValues(values);
				bbds[i] = date;
			}
			setDates(bbds);
			BillboardsManager.logger.warn("[更新榜单数据成功] ["+this.getLogString()+"]");
			
		}else{
			BillboardsManager.logger.error("[查询榜单数据错误] [没有记录] ["+this.getLogString()+"]");
		}
	}
	
}
