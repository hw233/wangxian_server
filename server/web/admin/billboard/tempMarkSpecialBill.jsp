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
<title>临时设置上次查询当日连斩时间</title>
</head>
<body>
<%! 
	int count = 0;

	List<Long> list = new ArrayList<Long>();
	
	int countNum = 24*60*2;
	
	Thread thread = null;
	
	String[] dateString = {"2012-09-03 16:30:00","2012-09-05 23:55:00","2012-09-05 23:59:00","2012-09-06 23:55:00","2012-09-06 23:59:00","2012-09-07 23:55:00","2012-09-07 23:59:00","2012-09-05 14:20:00","2012-09-05 14:30:00"};
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
				while(hasRunning){
					
					try{
						Thread.sleep(30000);
					}catch(Exception e){
						e.printStackTrace();
					}
					if(hasRunning == false)
						break;
					
					try{
						
						long now = SystemTime.currentTimeMillis();
						for(int j = 0;j<times.length;j++){
							if(now > times[j] && effects[j]){
								
								effects[j] = false;
								Billboard killJoin = BillboardsManager.getInstance().getBillboard("连斩","当日连斩");
								killJoin.setLastUpdateTime(now - 1l*24*60*60*1000);
								killJoin.update();
								BillboardsManager.logger.error("[后台设置上次当日连斩时间] [当日连斩]");
								BillboardDate[] datas = killJoin.getDates();
								if(datas != null && datas.length > 0){
									for(BillboardDate data:datas){
										BillboardsManager.logger.error("[后台打印当日连斩] ["+dateString[j]+"] ["+data.getDateValues()[0]+"] ["+data.getDateValues()[3]+"]");
									}
								}else{
									BillboardsManager.logger.error("[后台设置上次当日连斩时间查询错误] [当日连斩] ["+datas+"]");
								}
								
							}
						}
					}catch(Exception e){
						BillboardsManager.logger.error("[后台定时查询当日连斩]",e);
					}
				}
				hasRunning = false;
				thread = null;
			}
		});
	
		thread.setName("后台定时设置查询当日连斩时间");
		thread.start();
	}else if(action != null && action.equals("stop")){
		hasRunning = false;
		thread.interrupt();
	}	
	%>
<%
	if(thread == null){
		out.println("线程还没有启动，<a href='./tempMarkSpecialBill.jsp?action=start'>点击这里</a>启动线程! <a href='./tempMarkSpecialBill.jsp'>点击这里</a>刷新");
	}else{
		out.println("线程真正运行，已执行"+count+"次循环，<a href='./tempMarkSpecialBill.jsp?action=stop'>点击这里</a>停止线程! <a href='./tempMarkSpecialBill.jsp'>点击这里</a>刷新");
	}
%>	
</body>

</html>
