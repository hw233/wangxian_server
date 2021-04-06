package com.fy.engineserver.newBillboard.date.country;

import java.util.Arrays;
import java.util.Comparator;

import com.fy.engineserver.country.data.Country;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.guozhan.GuozhanOrganizer;
import com.fy.engineserver.guozhan.GuozhanStat;
import com.fy.engineserver.newBillboard.Billboard;
import com.fy.engineserver.newBillboard.BillboardDate;
import com.fy.engineserver.newBillboard.BillboardsManager;
import com.fy.engineserver.sprite.PlayerSimpleInfo;
import com.fy.engineserver.sprite.PlayerSimpleInfoManager;

//国家实力榜
public class CountryPowerBillboard extends Billboard {


//	国家排行 ： 名次 国家名称 混元至圣名称 积分
	
	public void update() throws Exception{
		super.update();
		
		Country[] countrys = CountryManager.getInstance().getSortedCountrys();
		if(countrys != null){
			if(countrys.length > 0){
				BillboardDate[] date1 = new BillboardDate[3];
				
				for(int i= 0;i<countrys.length;i++){
					Country c = countrys[i];
					BillboardDate  bbd = new BillboardDate();
					bbd.setType(BillboardDate.国家);
					bbd.setDateId(c.getCountryId());
					
					String[] values = new String[3];
					values[0] = CountryManager.得到国家名(c.getCountryId());
					
					long kingId = c.getKingId();
					String kingName = Translate.无;
					if(kingId > 0){
						PlayerSimpleInfo info = PlayerSimpleInfoManager.getInstance().getInfoById(kingId);
						if(info != null){
							kingName = info.getName();
						}else{
							BillboardsManager.logger.error("[更新榜单数据错误] ["+this.getLogString()+"] [混元至圣为null] [混元至圣Id:"+kingId+"]");
						}
					}
					values[1] = kingName;
					
					//得分
					long point = 0;
					GuozhanOrganizer org = GuozhanOrganizer.getInstance();
					int totalNum = org.getGuozhanTotalNum();
					GuozhanStat stat = org.getGuozhanStat(c.getCountryId());
					if(stat != null) {
						if(totalNum > 0) {
							point += (stat.getWinNum()-stat.getLoseNum())*1000/totalNum;
						}
					}
					values[2] = point+"";
					bbd.setDateValues(values);
					date1[i] = bbd;
				}
				Arrays.sort(date1, new Comparator<BillboardDate>() {

					@Override
					public int compare(BillboardDate o1, BillboardDate o2) {
						// TODO Auto-generated method stub
						if (Integer.parseInt(o1.getDateValues()[2]) > Integer.parseInt(o2.getDateValues()[2])) {
							return -1;
						} else if (o1.getDateValues()[2].equals(o2.getDateValues()[2])) {
							return 0;
						} else {
							return 1;
						}
					}
					
				});
				setDates(date1);
				BillboardsManager.logger.warn("[更新榜单数据成功] ["+this.getLogString()+"] [length:"+countrys.length+"]");
			}else{
				BillboardDate[] date1 = new BillboardDate[0];
				setDates(date1);
				BillboardsManager.logger.warn("[更新榜单数据失败] [没有数据] ["+this.getLogString()+"]");
			}
		}else{
			BillboardDate[] date1 = new BillboardDate[0];
			setDates(date1);
			BillboardsManager.logger.error("[更新榜单数据错误] [countrys null] ["+this.getLogString()+"]");
		}
	}
	
	
	public void updateScore(int country,int score){
		
		if(country >= 1 && country <=3 && score > 0){
			
			BillboardDate[] datas = getDates();
			for(BillboardDate bbd:datas){
				if(bbd.getDateId() == country){
					String[] st = datas[country-1].getDateValues();
					st[1] = score+"";
				}
			}

			for(int i= 0;i<datas.length;i++){
				for(int j= i+1;j<datas.length;j++){
					String[] st = datas[i].getDateValues();
					int score1 = Integer.parseInt(st[1]);
					
					String[] st2 = datas[j].getDateValues();
					int score2 = Integer.parseInt(st2[1]);
					
					if(score1 < score2){
						BillboardDate temp = new BillboardDate();
						temp.setType(BillboardDate.国家);
						temp.setDateId(datas[j].getDateId());
						temp.setDateValues(datas[j].getDateValues());
						datas[j] = datas[i];
						datas[i] = temp;
					}
				}
			}
			
			this.setDates(datas);
			BillboardsManager.getInstance().getDisk().put("countryPower", getDates());
			
			BillboardsManager.logger.warn("[更新国家实力榜成功] [country:"+country+"] [score:"+score+"]");
			
		}else{
			BillboardsManager.logger.error("[更新国家实力榜错误] [country:"+country+"] [score:"+score+"]");
		}
	}
	
}
