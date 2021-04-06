package com.fy.engineserver.newBillboard.date.charm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.newBillboard.Billboard;
import com.fy.engineserver.newBillboard.BillboardDate;
import com.fy.engineserver.newBillboard.BillboardInfo;
import com.fy.engineserver.newBillboard.BillboardMenu;
import com.fy.engineserver.newBillboard.BillboardStatDateManager;
import com.fy.engineserver.newBillboard.BillboardsManager;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.console.ChangeAble;
import com.fy.engineserver.util.console.MConsole;
/**
 * 排行榜活动
 * 
 *
 */
public class FlowerBillboardActivity extends Billboard implements MConsole{
	
	@ChangeAble("begintime")
	private long begintime = TimeTool.formatter.varChar19.parse("2013-08-13 00:00:00");//在国内本地测试测试锭
	
	@ChangeAble("endtime")
	private long endtime = TimeTool.formatter.varChar19.parse("2013-08-19 23:59:59");
	
	@ChangeAble("排行榜消失时间")
	private long 排行榜消失时间 = TimeTool.formatter.varChar19.parse("2013-08-20 20:00:00");
	
	@ChangeAble("count")
	private int count = 20;
	
	public void update()throws Exception {
		super.update();
		long now = System.currentTimeMillis();
		BillboardStatDateManager bdm = BillboardStatDateManager.getInstance();
		List<BillboardInfo> infos = bdm.getInfoByKey(begintime, endtime);
		if(infos!=null && infos.size()>0){
			List<BillboardDate> list = new ArrayList<BillboardDate>();
			for(int i=0;i<infos.size();i++){
				BillboardInfo info = infos.get(i);
				if(info.getMenuname().equals(this.getMenu()) && info.getSubmenuname().equals(this.getSubMenu())){
					try{
						BillboardDate date = new BillboardDate();
						date.setDateId(info.getPid());
						date.setType(BillboardDate.玩家);
						String[] values = new String[3];
						values[0] = info.getPlayername();
						values[1] = CountryManager.得到国家名(info.getCountry());
						values[2] = info.getValue()+"";
						date.setDateValues(values);
						list.add(date);
					} catch (Exception e) {
						e.printStackTrace();
						BillboardsManager.logger.warn("[韩国排行榜活动] [异常] [耗时："+(System.currentTimeMillis()-now)+"]"+e);
						continue;
					}
				}
			}
			List<BillboardDate> newlist = new ArrayList<BillboardDate>();
			for(BillboardDate data:list){
				boolean isadd = false;
				for(BillboardDate dt:newlist){
					if(dt!=null){
						if(dt.getDateId()==data.getDateId()){
							String s [] = dt.getDateValues();
							s[2] = Long.parseLong(s[2])+Long.parseLong(data.getDateValues()[2])+"";
							dt.setDateValues(s);
							isadd = true;
						}
					}
				}
				if(!isadd){
					newlist.add(data);
					Collections.sort(newlist, new Comparator<BillboardDate>() {
						@Override
						public int compare(BillboardDate o1, BillboardDate o2) {
							return Integer.valueOf(o2.getDateValues()[2]) - Integer.valueOf(o1.getDateValues()[2]);
						}
					});
				}
				
			}
			
			setDates(newlist.toArray(new BillboardDate[]{}));
			BillboardsManager.logger.warn("[韩国排行榜活动] [update排行榜活动] [bbds:"+list.size()+"] [6] ");
		}else{
			BillboardDate[] bbds = new BillboardDate[0];
			setDates(bbds);
		}
		
		BillboardsManager.logger.warn("[排行榜活动] [update排行榜活动] [infos:"+infos+"] [now > endtime:"+(now > endtime)+"]");
		if(now > 排行榜消失时间){
			List<BillboardMenu> list = BillboardsManager.getInstance().menuList;
			for(BillboardMenu bm:list){
				if(bm.getMenuName().equals(Translate.魅力)){
					String[] subMenus = bm.getSubMenus();
					for(String subname:subMenus){
						if(subname.equals(Translate.鲜花活动)||subname.equals(Translate.糖果活动)){
							list.remove(bm);
							BillboardsManager.logger.warn("[活动排行榜] [已过期删除活动菜单] [subname:"+subname+"]");
						}
					}
				}
			}
			return;
		}
	}

	@Override
	public String getMConsoleName() {
		return "韩国送花送糖排行榜";
	}

	@Override
	public String getMConsoleDescription() {
		return "韩国送花送糖排行榜一些配置控制";
	}

	public long getBegintime() {
		return begintime;
	}

	public void setBegintime(long begintime) {
		this.begintime = begintime;
	}

	public long getEndtime() {
		return endtime;
	}

	public void setEndtime(long endtime) {
		this.endtime = endtime;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
