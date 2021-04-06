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
<%@page import="com.fy.engineserver.gametime.SystemTime"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTa_Thread"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTaManager"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTa_Ta"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTa_Dao"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTa_Ceng"%><html>
<head>
<title>修改千层塔</title>
</head>
<body>
<%! 
	int count = 0;

	List<Long> list = new ArrayList<Long>();
	
	int countNum = 24*60*2;
	
	Thread thread = null;
	boolean hasRunning = false;
	
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
					QianCengTa_Thread[] threads = QianCengTaManager.getInstance().getThreads();
					for (int i = 0; i < threads.length; i++) {
						QianCengTa_Ta[] tas = threads[i].getTaLists().toArray(new QianCengTa_Ta[0]);
						
						for (int j = 0; j < tas.length; j++) {
							QianCengTa_Ta ta = tas[j];
							QianCengTa_Dao[] daos = ta.getDaoList().toArray(new QianCengTa_Dao[0]);
							for (int k = 0; k < daos.length; k++) {
								QianCengTa_Dao dao = daos[k];
								
								int cengIndex = -1;
								QianCengTa_Ceng[] cengs = dao.getCengList().toArray(new QianCengTa_Ceng[0]);
								if (cengs.length > 1) {
									cengIndex = cengs[0].getCengIndex();
									for (int m = 1; m < cengs.length; m++) {
										QianCengTa_Ceng ceng = cengs[m];
										if (cengIndex == ceng.getCengIndex()) {
											if (cengs.length == dao.getCengList().size()){
												dao.getCengList().remove(m);
												ta.notifyChanager("daoList");
												QianCengTaManager.getInstance().logger.error("[千层塔数据出错修复] [{}] [{}] [{}] [{}]", new Object[]{ta.getPlayer().getLogString(), ta.getLogString(), dao.getDaoIndex()+"/"+dao.getCurrentCengIndex()+"/"+dao.getCengList().size(), ceng.getCengIndex()+"-"+cengIndex});
											}
											break;
										}
										cengIndex = cengs[m].getCengIndex();
									}
								}
							}
							
						}
						
					}
					}catch(Exception e){
						QianCengTaManager.getInstance().logger.error("千层塔修复线程出错",e);
					}
					
					
					
					
				}
				hasRunning = false;
				thread = null;
			}
		});
	
		thread.setName("后台修改千层塔");
		thread.start();
	}else if(action != null && action.equals("stop")){
		hasRunning = false;
		thread.interrupt();
	}	
	%>
<%
	if(thread == null){
		out.println("线程还没有启动，<a href='./tempUpdateQianCengTa.jsp?action=start'>点击这里</a>启动线程! <a href='./tempUpdateQianCengTa.jsp'>点击这里</a>刷新");
	}else{
		out.println("线程真正运行，已执行"+count+"次循环，<a href='./tempUpdateQianCengTa.jsp?action=stop'>点击这里</a>停止线程! <a href='./tempUpdateQianCengTa.jsp'>点击这里</a>刷新");
	}
%>	
</body>

</html>
