package com.fy.engineserver.newBillboard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.message.GET_ONE_BILLBOARD_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.newBillboard.BillboardsManager.JiazuSimpleInfo;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;


public class Billboard implements Serializable{

	private static final long serialVersionUID = -4918931147308307764L;

	private String menu;
	private String subMenu;
	
	//标题
	private String[] titles;
	//标题的最大字数
	private int[] titlesMaxNum;
	//每条数据   这个会变化
	private BillboardDate[] dates = new BillboardDate[]{};
	
	private long lastUpdateTime;
	private String description;
	
	private boolean dateOrMonth;
	
	public Billboard(){
		
	}
	
	public void getBillboard(Player player){
		
		try{
			long now = SystemTime.currentTimeMillis();
			if(now - getLastUpdateTime() > BillboardsManager.flushTime || dates == null){
				setLastUpdateTime(SystemTime.currentTimeMillis());
				try {
					this.update();
					BillboardsManager.logger.warn("[更新榜单成功] ["+player.getLogString()+"] ["+this.getLogString()+"]");
				} catch (Exception e) {
					BillboardsManager.logger.error("[更新榜单异常] ["+player.getLogString()+"] ["+this.getLogString()+"]",e);
					return ;
				}
			}
			BillboardDate[] ds = getDates();
			if(ds != null){
				BillboardDate[] nds = null;
				if(ds.length >= BillboardsManager.显示条数){
					nds = new BillboardDate[BillboardsManager.显示条数];
					for(int i=0;i<BillboardsManager.显示条数;i++){
						nds[i] = ds[i];
					}
				}else{
					nds = ds;
				}
				int i = 0;
				//200里有没有自己
				boolean hava = false;
//				if(nds!=null && ds!=null){
//					BillboardsManager.logger.error("[更新榜单异常] [ds:"+ds.length+"] [nds:"+nds.length+"]");
//				}
				
				for(BillboardDate date : ds){
					i++;
					if(date==null){
						continue;
					}
					if(date.getType() == BillboardDate.玩家){
						if(date.getDateId() == player.getId()){
							hava = true;
							break;
						}
					}else if(date.getType() == BillboardDate.宠物){
						
						Knapsack k = player.getPetKnapsack();
						Cell[] cells = k.getCells();
						boolean breakFlag = false;
						for(Cell c : cells){
							if(c.getEntityId() == date.getDateId()){
								breakFlag = true;
								hava = true;
								break;
							}
						}
						if(breakFlag){
							break;
						}
						
					}else if(date.getType() == BillboardDate.国家){
						int country = player.getCountry();
						if(date.getDateId() == country){
							hava = true;
							break;
						}
					}else if(date.getType() == BillboardDate.家族){
						if(date.getDateId() == player.getJiazuId()){
							hava = true;
							break;
						}
					}else{
						BillboardsManager.logger.warn("[查看榜单查看第几名错误] ["+player.getLogString()+"] ["+this.getLogString()+"]");
					}
				}
				String selfRank;
				if(i<= 200 && i<= ds.length && hava){
					selfRank = Translate.translateString(Translate.第X名, new String[][]{{Translate.COUNT_1,String.valueOf(i)}});//"第"+i+"名";
				}else{
					selfRank = Translate.没有上榜;
				}
				
				//排行榜NullPointerException处理
				if(nds != null){
					for(BillboardDate d : nds){
						if(d != null){
							if(d.getDateValues() != null){
								String [] dates = new String[d.getDateValues().length];
								for(int k = 0;k < dates.length;k++){
									if(d.getDateValues()[k] != null){
										dates[k] = d.getDateValues()[k];
									}else{
										dates[k] = "";
									}
								}
								d.setDateValues(dates);
							}
						}
					}
				}
				
				GET_ONE_BILLBOARD_RES res = new GET_ONE_BILLBOARD_RES(GameMessageFactory.nextSequnceNum(), getTitles(), getTitlesMaxNum(),nds, getDescription(), selfRank);
				player.addMessageToRightBag(res);
				BillboardsManager.logger.warn("[查看榜单成功] ["+player.getLogString()+"] [ds:"+ds.length+"] [nds:"+nds.length+"] [hava:"+hava+"] ["+this.getLogString()+"] ["+this.getDescription()+"] ["+(SystemTime.currentTimeMillis() - now)+"ms]");
			}else{
				BillboardDate[] nds = new BillboardDate[0];
				GET_ONE_BILLBOARD_RES res = new GET_ONE_BILLBOARD_RES(GameMessageFactory.nextSequnceNum(), getTitles(), getTitlesMaxNum(),nds, getDescription(), Translate.没有上榜);
				player.addMessageToRightBag(res);
				BillboardsManager.logger.warn("[查看榜单成功] [没有数据] ["+player.getLogString()+"] ["+this.getLogString()+"] [nds:"+nds.length+"] ["+this.getDescription()+"] ["+(SystemTime.currentTimeMillis() - now)+"ms]");
			}
		
		}catch(Exception e){
			BillboardsManager.logger.error("[查看榜单更新榜单异常] ["+player.getLogString()+"] ["+this.getLogString()+"]",e);
		}
				
	}
	
	/**
	 * 更新某个玩家的大师技能排行榜
	 */
	public void updateData(Player player,int newValue){
		try{
			if(dates!=null){
				List<BillboardDate> list = new ArrayList<BillboardDate>();
				int index = -1;
				for(int i=0,len=dates.length;i<len;i++){
					if(dates[i].getDateId()==player.getId()){
						index = i;
					}
					list.add(dates[i]);
				}

				if(index<0){
					BillboardDate date = new BillboardDate();
					date.setDateId(player.getId());
					date.setType(BillboardDate.玩家);

					String[] values = new String[5];
					values[0] = player.getName();
					values[1] = CountryManager.得到国家名(player.getCountry());
					JiazuSimpleInfo jiazu = null;
					if(player.getJiazuId() > 0){
						jiazu = BillboardsManager.getInstance().getJiazuSimpleInfo(player.getJiazuId());
					}
					if(jiazu == null){
						values[2] = Translate.无;
					}else{
						values[2] = jiazu.getName();
					}
					values[3] = player.getLevel()+"";
					values[4] = String.valueOf(newValue);
					date.setDateValues(values);
					list.add(date);
					if(BillboardsManager.logger.isInfoEnabled()){
						BillboardsManager.logger.info("[大师技能排行榜数据] [新建数据] [成功] [总数据："+list.size()+"] [数据："+Arrays.toString(date.getDateValues())+"] [玩家信息："+player.getLogString()+"]");
					}
				}else{
					BillboardDate olddate = list.get(index);
					String[] values = olddate.getDateValues();
					String oldvalue = values[4];
					values[4] = String.valueOf(newValue);
					olddate.setDateValues(values);
					if(BillboardsManager.logger.isInfoEnabled()){
						BillboardsManager.logger.info("[大师技能排行榜数据] [更新数据] [成功] [总数据："+list.size()+"] [数值变化："+oldvalue+"-->"+newValue+"] [数据："+Arrays.toString(olddate.getDateValues())+"] [玩家信息："+player.getLogString()+"]");
					}
				}
				
				//排序
				setDates(list.toArray(new BillboardDate[]{}));
				
				Arrays.sort(dates, new Comparator<BillboardDate>() {

					@Override
					public int compare(BillboardDate o1, BillboardDate o2) {
						return Integer.parseInt(o2.getDateValues()[4]) - Integer.parseInt(o1.getDateValues()[4]);
					}
				});
				
				setDates(dates);
				BillboardsManager.logger.info("[大师技能排行榜数据] [刷新数据] [玩家信息："+player.getLogString()+"] [成功]"); 
				
			}
		}catch(Exception e){
			BillboardsManager.logger.info("[大师技能排行榜数据] [更新数据] [异常] [newValue:"+newValue+"] ["+player.getLogString()+"] ["+e+"]"); 
		}
		
	}
	
	public void updatePerMonth() throws Exception{
		
	}
	
	public List<IBillboardPlayerInfo> getBillboardPlayerInfo(long[] ids){
		
		SimpleEntityManager<Player> em = SimpleEntityManagerFactory.getSimpleEntityManager(Player.class);
		if(ids != null && ids.length > 0){
			List<IBillboardPlayerInfo> list = null;
			try {
				list = em.queryFields(IBillboardPlayerInfo.class, ids);
				List<IBillboardPlayerInfo> sortList = new ArrayList<IBillboardPlayerInfo>();
				for(long id : ids){
					for(IBillboardPlayerInfo ib : list){
						if(id == ib.getId()){
							sortList.add(ib);
							break;
						}
					}
				}
				return sortList;
			} catch (Exception e) {
				BillboardsManager.logger.error("[查询榜单玩家数据异常] ["+this.getLogString()+"]",e);
			}
		}else{
			BillboardsManager.logger.error("[查询榜单玩家数据错误] [没有记录] ["+this.getLogString()+"]");
		}
		return null;
	}
	
	public void update()throws Exception{
		
//		setLastUpdateTime(SystemTime.currentTimeMillis());
//		if(dateOrMonth){
//			//月更新 加入月更新list
//			List<Billboard> list = BillboardsManager.getInstance().getUpdatePerMonthlist();
//			if(list != null){
//				boolean have = false;
//				for(Billboard bb : list){
//					if(bb.menu.equals(this.menu) && bb.subMenu.equals(this.subMenu)){
//						have = true;
//					}
//				}
//				if(!have){
//					list.add(this);
//				}
//			}else{
//				list = new ArrayList<Billboard>();
//				list.add(this);
//				BillboardsManager.getInstance().setUpdatePerMonthlist(list);
//			}
//		}
		
	}
	
	public String getLogString(){
		return "menu:"+menu+"subMenu:"+subMenu;
	}
	
	/***************************************/
	
	public String getMenu() {
		return menu;
	}
	public void setMenu(String menu) {
		this.menu = menu;
	}
	public String getSubMenu() {
		return subMenu;
	}
	public void setSubMenu(String subMenu) {
		this.subMenu = subMenu;
	}
	public String[] getTitles() {
		return titles;
	}
	public void setTitles(String[] titles) {
		this.titles = titles;
	}
	public int[] getTitlesMaxNum() {
		return titlesMaxNum;
	}

	public void setTitlesMaxNum(int[] titlesMaxNum) {
		this.titlesMaxNum = titlesMaxNum;
	}

	public BillboardDate[] getDates() {
		return dates;
	}
	public void setDates(BillboardDate[] dates) {
		this.dates = dates;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isDateOrMonth() {
		return dateOrMonth;
	}

	public void setDateOrMonth(boolean dateOrMonth) {
		this.dateOrMonth = dateOrMonth;
	}
	
}
