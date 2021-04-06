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
<title>临时打印当日鲜花，当日糖果</title>
</head>
<body>
<%! 
	int count = 0;

	List<Long> list = new ArrayList<Long>();
	
	int countNum = 24*60*2;
	
	Thread thread = null;
	
	String[] dateString = {"2012-09-28 23:59:00","2012-09-29 23:59:00","2012-09-30 23:59:00","2012-10-01 23:59:00","2012-10-02 23:59:00",
			"2012-10-03 23:59:00","2012-10-04 23:59:00","2012-10-05 23:59:00","2012-10-06 23:59:00","2012-10-07 23:59:00"};
	long[] times = new long[dateString.length];
	boolean[] effects = new boolean[dateString.length];
	
	public static boolean hasRunning = false;
%>

<%
	String action = request.getParameter("action");

	if(action != null && action.equals("start")){
		long now = SystemTime.currentTimeMillis();
		for(int i = 0;i<dateString.length;i++){
			Date date = LogRecordManager.sdf.parse(dateString[i]);
			long time = date.getTime();
			times[i] = time;
			if(time > now){
				effects[i] = true;
				BillboardsManager.logger.error("[后台加载定时查询生效] ["+dateString[i]+"]");
			}else{
				effects[i] = false;
				BillboardsManager.logger.error("[后台加载定时查询失效] ["+dateString[i]+"]");
			}
		}
		
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
						
						long now = SystemTime.currentTimeMillis();
						for(int j = 0;j<times.length;j++){
							if(now > times[j] && effects[j]){
								effects[j] = false;
								
								Billboard dayFlower = BillboardsManager.getInstance().getBillboard("魅力","当日鲜花");
								if(dayFlower != null){
									BillboardDate[] data = dayFlower.getDates();
									if(data != null){
										for(BillboardDate bbd : data){
											BillboardsManager.logger.error("["+dateString[j]+"] [当日鲜花打印日志] ["+bbd.getDateId()+"] ["+bbd.getDateValues()[0]+"] ["+bbd.getDateValues()[3]+"]");
										}
									}else{
										BillboardsManager.logger.error("["+dateString[j]+"] [后台打印当日鲜花失败] [数据为null]");
									}
								}else{
									BillboardsManager.logger.error("["+dateString[j]+"] [后台打印当日鲜花失败] [没有排行榜]");
								}
								
								Billboard daySweet = BillboardsManager.getInstance().getBillboard("魅力","当日糖果");
								if(daySweet != null){
									BillboardDate[] data = daySweet.getDates();
									if(data != null){
										for(BillboardDate bbd : data){
											BillboardsManager.logger.error("["+dateString[j]+"] [当日糖果打印日志] ["+bbd.getDateId()+"] ["+bbd.getDateValues()[0]+"] ["+bbd.getDateValues()[3]+"]");
										}
									}else{
										BillboardsManager.logger.error("["+dateString[j]+"] [后台打印当日糖果失败] [数据为null]");
									}
								}else{
									BillboardsManager.logger.error("["+dateString[j]+"] [后台打印当日糖果失败] [没有排行榜]");
								}
							}
						}
					}catch(Exception e){
						BillboardsManager.logger.error("[后台定时查询当日鲜花糖果]",e);
					}
				}
				hasRunning = false;
				thread = null;
			}
		});
	
		thread.setName("后台定时查询当日鲜花糖果");
		thread.start();
	}else if(action != null && action.equals("stop")){
		hasRunning = false;
		thread.interrupt();
	}	
	%>
<%
	if(thread == null){
		out.println("线程还没有启动，<a href='./tempMarkBill.jsp?action=start'>点击这里</a>启动线程! <a href='./tempMarkBill.jsp'>点击这里</a>刷新");
	}else{
		out.println("线程真正运行，已执行"+count+"次循环，<a href='./tempMarkBill.jsp?action=stop'>点击这里</a>停止线程! <a href='./tempMarkBill.jsp'>点击这里</a>刷新");
	}
%>	
</body>

</html>
