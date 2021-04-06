<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>



<%@page import="com.fy.engineserver.newBillboard.BillboardsManager"%>
<%@page import="com.fy.engineserver.newBillboard.Billboard"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardStatDate"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardStatDateManager"%>
<%@page import="com.fy.engineserver.newBillboard.IBillboardPlayerInfo"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardDate"%>
<%@page import="com.fy.engineserver.country.manager.CountryManager"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardsManager.JiazuSimpleInfo"%>
<%@page import="com.fy.engineserver.newBillboard.monitorLog.LogRecordManager"%>
<%@page import="com.fy.engineserver.gametime.SystemTime"%><html>
<head>
<title>后台页面刷新当日排行榜</title>
</head>
<body>
<%! 
	int count = 0;

	List<Long> list = new ArrayList<Long>();
	
	int countNum = 7*24*60*2;
	
	Thread thread = null;
	
	public static boolean hasRunning = false;
	public boolean flag = true;
%>

<%
	String action = request.getParameter("action");

	if(action != null && action.equals("start")){


		
	thread = new Thread(new Runnable(){
			
			public void run(){
				hasRunning =  true;
				long 间隔 = 25*60*1000;
				while(hasRunning && count < countNum){
					
					try{
						Thread.sleep(30000);
					}catch(Exception e){
						e.printStackTrace();
					}
					if(hasRunning == false)
						break;
					
					try{
						count++;
						
						Billboard killJoin = BillboardsManager.getInstance().getBillboard("连斩","当日连斩");
						
						if(!flag && SystemTime.currentTimeMillis() - killJoin.getLastUpdateTime() <=  间隔){
							continue;	
						}
						flag = false;
						int day = BillboardsManager.getInstance().得到当日时间天();

						SimpleEntityManager<BillboardStatDate> em = BillboardStatDateManager.em;
						long[] ids = em.queryIds(BillboardStatDate.class, "dayKillJoinNum > ? AND dayKillJoin = ?",new Object[]{0,day},"dayKillJoinNum desc,dayKillJoinScore desc ",1,BillboardsManager.实际条数+1);
						//排行榜统计数据
						if(ids != null && ids.length > 0){
							
							List<IBillboardPlayerInfo> playerList = killJoin.getBillboardPlayerInfo(ids);
							
							if(playerList != null && playerList.size() > 0){
								BillboardDate[] bbds = new BillboardDate[playerList.size()];
								for(int i=0;i<playerList.size();i++){
									IBillboardPlayerInfo info = playerList.get(i);
									BillboardDate date = new BillboardDate();
									date.setDateId(info.getId());
									date.setType(BillboardDate.玩家);

									String[] values = new String[4];
									values[0] = info.getName();
									values[1] = CountryManager.得到国家名(info.getCountry());
									BillboardsManager.JiazuSimpleInfo jiazu = null;
									if(info.getJiazuId() > 0){
										jiazu = BillboardsManager.getInstance().getJiazuSimpleInfo(info.getJiazuId());
									}
									if(jiazu == null){
										values[2] = "无";
									}else{
										values[2] = jiazu.getName();
									}
									BillboardStatDate bbs = BillboardStatDateManager.getInstance().getBillboardStatDate(info.getId());
									if(bbs != null){
										values[3] = bbs.getDayKillJoinNum()+"";
									}else{
										values[3] = 0+"";
									}
									date.setDateValues(values);
									bbds[i] = date;
								}
								killJoin.setDates(bbds);
								
								if(LogRecordManager.getInstance() != null){
									LogRecordManager.getInstance().活动记录日志(LogRecordManager.当日连斩, killJoin);
								}
							}else{
								BillboardsManager.logger.error("[查询榜单数据没有记录] ["+killJoin.getLogString()+"]");
							}
							
						}else{
							BillboardDate[] bbds = new BillboardDate[0];
							killJoin.setDates(bbds);
							BillboardsManager.logger.error("[查询榜单数据错误] [没有记录] ["+killJoin.getLogString()+"]");
						}
						killJoin.setLastUpdateTime(SystemTime.currentTimeMillis());
						BillboardsManager.logger.error("[后台刷新当日连斩]");
						
						Billboard dayFlower = BillboardsManager.getInstance().getBillboard("魅力","当日鲜花");
						long[] idsFlower = em.queryIds(BillboardStatDate.class, " dayFlowersNum > ? AND dayFlower = ? ",new Object[]{0,day},"dayFlowersNum desc ",1,BillboardsManager.实际条数+1);
						//排行榜统计数据
						if(idsFlower != null && idsFlower.length > 0){
							
							List<IBillboardPlayerInfo> playerList = dayFlower.getBillboardPlayerInfo(idsFlower);
							
							if(playerList != null && playerList.size() > 0){
								BillboardDate[] bbds = new BillboardDate[playerList.size()];
								for(int i=0;i<playerList.size();i++){
									IBillboardPlayerInfo info = playerList.get(i);
									BillboardDate date = new BillboardDate();
									date.setDateId(info.getId());
									date.setType(BillboardDate.玩家);

									String[] values = new String[4];
									values[0] = info.getName();
									values[1] = CountryManager.得到国家名(info.getCountry());
									JiazuSimpleInfo jiazu = null;
									if(info.getJiazuId() > 0){
										jiazu = BillboardsManager.getInstance().getJiazuSimpleInfo(info.getJiazuId());
									}
									if(jiazu == null){
										values[2] = "无";
									}else{
										values[2] = jiazu.getName();
									}
									BillboardStatDate bbs = BillboardStatDateManager.getInstance().getBillboardStatDate(info.getId());
									if(bbs != null){
										values[3] = bbs.getDayFlowersNum()+"";
									}else{
										values[3] = 0+"";
									}
									date.setDateValues(values);
									bbds[i] = date;
								}
								dayFlower.setDates(bbds);
							}
						}else{
							BillboardDate[] bbds = new BillboardDate[0];
							dayFlower.setDates(bbds);
						}
						dayFlower.setLastUpdateTime(SystemTime.currentTimeMillis());
						BillboardsManager.logger.error("[后台刷新当日鲜花]");
						
						
						
						Billboard daySweet = BillboardsManager.getInstance().getBillboard("魅力","当日糖果");
						long[] idsSweet = em.queryIds(BillboardStatDate.class, " daySweetsNum > ? AND daySweet = ? ",new Object[]{0,day},"daySweetsNum desc ",1,BillboardsManager.实际条数+1);
						//排行榜统计数据
						if(idsSweet != null && idsSweet.length > 0){
							
							List<IBillboardPlayerInfo> playerList = daySweet.getBillboardPlayerInfo(idsSweet);
							
							if(playerList != null && playerList.size() > 0){
								BillboardDate[] bbds = new BillboardDate[playerList.size()];
								for(int i=0;i<playerList.size();i++){
									IBillboardPlayerInfo info = playerList.get(i);
									BillboardDate date = new BillboardDate();
									date.setDateId(info.getId());
									date.setType(BillboardDate.玩家);

									String[] values = new String[4];
									values[0] = info.getName();
									values[1] = CountryManager.得到国家名(info.getCountry());
									JiazuSimpleInfo jiazu = null;
									if(info.getJiazuId() > 0){
										jiazu = BillboardsManager.getInstance().getJiazuSimpleInfo(info.getJiazuId());
									}
									if(jiazu == null){
										values[2] = "无";
									}else{
										values[2] = jiazu.getName();
									}
									BillboardStatDate bbs = BillboardStatDateManager.getInstance().getBillboardStatDate(info.getId());
									if(bbs != null){
										values[3] = bbs.getDaySweetsNum()+"";
									}else{
										values[3] = 0+"";
									}
									date.setDateValues(values);
									bbds[i] = date;
								}
								daySweet.setDates(bbds);
							}else{
								
							}
						}else{
							BillboardDate[] bbds = new BillboardDate[0];
							daySweet.setDates(bbds);
							BillboardsManager.logger.error("[查询榜单数据错误] [没有记录]");
						}
						daySweet.setLastUpdateTime(System.currentTimeMillis());
						BillboardsManager.logger.error("[后台刷新当日糖果]");
					}catch(Exception e){
						BillboardsManager.logger.error("[后台刷新排行榜异常]",e);
					}
				}
				hasRunning = false;
				thread = null;
			}
		});
	
		thread.setName("后台修改当日排行榜");
		thread.start();
	}else if(action != null && action.equals("stop")){
		hasRunning = false;
		thread.interrupt();
	}	
	%>
<%
	if(thread == null){
		out.println("线程还没有启动，<a href='./tempUpdateBill.jsp?action=start'>点击这里</a>启动线程! <a href='./tempUpdateBill.jsp'>点击这里</a>刷新");
	}else{
		out.println("线程真正运行，已执行"+count+"次循环，<a href='./tempUpdateBill.jsp?action=stop'>点击这里</a>停止线程! <a href='./tempUpdateBill.jsp'>点击这里</a>刷新");
	}
%>	
</body>

</html>
