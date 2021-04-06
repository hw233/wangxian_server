package com.fy.engineserver.newBillboard.date.charm;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.newBillboard.Billboard;
import com.fy.engineserver.newBillboard.BillboardDate;
import com.fy.engineserver.newBillboard.BillboardMenu;
import com.fy.engineserver.newBillboard.BillboardsManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.console.ChangeAble;
import com.fy.engineserver.util.console.MConsole;
import com.fy.engineserver.util.console.MConsoleManager;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.JsonUtil;
/**
 * 充值排行榜
 * 
 *
 */
public class ChargeMonthBillboard extends Billboard implements MConsole{
	
	@ChangeAble("begintime")
	private long begintime = TimeTool.formatter.varChar19.parse("2013-08-10 00:00:00");//在国内本地测试测试锭
	
	@ChangeAble("endtime")
	private long endtime = TimeTool.formatter.varChar19.parse("2013-08-13 23:59:59");
	
	@ChangeAble("count")
	private int count = 10;
	
	@ChangeAble("servertype")
	private String servertype = "appstore";
	
	public void update()throws Exception {
		
		
		
		long now = System.currentTimeMillis();
		if(now > endtime){
			List<BillboardMenu> list = BillboardsManager.getInstance().menuList;
			List<BillboardMenu> removeList = new ArrayList<BillboardMenu>();
			
			for(BillboardMenu bm:list){
				if(bm.getMenuName().equals(Translate.充值)){
					//list.remove(bm);
					removeList.add(bm);	
					BillboardsManager.logger.warn("[充值排行榜] [已过期删除充值菜单]");
				}
			}
			list.removeAll(removeList);
			return;
		}
		
		GameConstants gc = GameConstants.getInstance();
		String servername = URLEncoder.encode(gc.getServerName(), "utf-8");
		MConsoleManager.register(this);
		String adminurl = "http://"+Game.网关地址+":8882/game_gateway/queryAppCharge.jsp";
		HashMap headers = new HashMap();
		
		String contentP ="servertype="+servertype+"&servername="+servername+"&begintime="+begintime+"&endtime="+endtime+"&curtime="+now+"&num="+count+"&authorize.username=zhangjianqin&authorize.password=Qinshou7hao";
		String result = "";
		try {
			byte[] b = HttpUtils.webPost(new URL(adminurl), contentP.getBytes(), headers, 20000, 20000);
			result = new String(b).trim();
			String results[] = result.split("\r\n");
			if(results!=null && results.length>0){
				ChargeBillboardMess []cbm = new ChargeBillboardMess[results.length];
				for(int i=0;i<results.length;i++){
					String []messes = results[i].split("@@@@");
					if(messes.length==4){
						ChargeBillboardMess cb = new ChargeBillboardMess();
						//mixiangcun@@@@觅香岑@@@@73373400@@@@西方灵山
						//角色名称:服务器:级别:金额
						cb.setPaymonery(Long.parseLong(messes[2]));
						cb.setPlayername(messes[1]);
						cb.setServername(messes[3]);
						cbm[i] = cb;
					}else{
						BillboardDate[] bbds = new BillboardDate[0];
						setDates(bbds);
						BillboardsManager.logger.warn("[充值排行榜] [BOSS传来的数据异常] ["+Arrays.toString(messes)+"]");
					}
				}
				if(cbm!=null && cbm.length>0){
					BillboardDate[] bbds = new BillboardDate[results.length];
					for(int i=0;i<cbm.length;i++){
						ChargeBillboardMess mess = cbm[i];
						BillboardDate date = new BillboardDate();
						date.setDateId(mess.getPid());
						date.setType(BillboardDate.玩家);

						String[] values = new String[3];
						values[0] = mess.getPlayername();
						values[1] = mess.getServername();
						values[2] = mess.getPaymonery()/2000+"锭";
						date.setDateValues(values);
						bbds[i] = date;
					}
					setDates(bbds);
					BillboardsManager.logger.warn("[充值排行榜] [更新成功]");
				}else{
					BillboardDate[] bbds = new BillboardDate[0];
					setDates(bbds);
					BillboardsManager.logger.warn("[充值排行榜] [没有数据]");
				}
			}else{
				BillboardDate[] bbds = new BillboardDate[0];
				setDates(bbds);
			}
		} catch (Exception e) {
			BillboardDate[] bbds = new BillboardDate[0];
			setDates(bbds);
			e.printStackTrace();
			BillboardsManager.logger.warn("[充值排行榜] [异常] [耗时："+(System.currentTimeMillis()-now)+"]"+e);
		}
		
	}

	@Override
	public String getMConsoleName() {
		return "充值排行榜";
	}

	@Override
	public String getMConsoleDescription() {
		return "充值排行榜一些配置控制";
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

	public String getServertype() {
		return servertype;
	}

	public void setServertype(String servertype) {
		this.servertype = servertype;
	}
	

}
