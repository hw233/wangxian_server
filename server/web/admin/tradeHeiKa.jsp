<%@page import="com.fy.engineserver.sprite.PlayerSimpleInfoManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerSimpleInfo"%>
<%@page import="com.fy.engineserver.economic.BillingCenter"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.LineNumberReader"%>
<%@page import="java.io.InputStreamReader"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%!
		boolean isrun = true;
	
		InputStream in = null;
		InputStream err = null;
		
		Hashtable<Long, Long> auctions = new Hashtable<Long, Long>();			//拍卖日志查出来的结果集
		ArrayList<Long> sortIds;					//用来显示的ID
		ArrayList<Long> moneys;						//金额
		ArrayList<String> inLine = new ArrayList<String>();
		ArrayList<String> errLine = new ArrayList<String>();
		
		Thread inThread = null;
		Thread errThread = null;
		
	%>
	
	<%
	if (inThread == null) {
		inThread = new Thread(new Runnable(){
			public void run(){
				while(isrun) {
					try{
						Thread.sleep(1000);
						if (in != null) {
							BufferedReader br = new BufferedReader(new InputStreamReader(in));
							String line = null;
							inLine.clear();
							auctions.clear();
							try{
								while((line = br.readLine()) != null) {
									String[] sp = line.split(" ");
									if (sp.length == 3) {
										Long id = Long.valueOf(sp[1]);
										Long money = auctions.get(id);
										if (money == null) {
											auctions.put(id, Long.valueOf(sp[2]));
										}else {
											auctions.put(id, money + Long.valueOf(sp[2]));
										}
									}else {
										inLine.add(line);
									}
								}
								br.close();
								in = null;
							}catch (Exception e) {
								e.printStackTrace();
							}finally {
								br.close();
								in = null;
							}
						}
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}, "交易shell查询线程1");

		inThread.start();
		
	}
	if (errThread == null) {
		errThread = new Thread(new Runnable(){
			public void run(){
				while(isrun) {
					try{
						Thread.sleep(1000);
						if (err != null) {
							BufferedReader br = new BufferedReader(new InputStreamReader(err));
							String line = null;
							errLine.clear();
							try{
								while((line = br.readLine()) != null) {
									errLine.add(line);
								}
							}catch (Exception e) {
								e.printStackTrace();
							}finally {
								br.close();
								err = null;
							}
						}
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}, "交易shell查询线程2");
		
		errThread.start();
	}
	%>
	
	
	
	<%
		String selectIndex = request.getParameter("selectIndex");
		if(selectIndex != null){
			sortIds = null;
			moneys = null;
			auctions = new Hashtable<Long,Long>();
			
			String HOME_PATH = System.getProperty("user.dir");
			
			if(selectIndex.equals("0")){
				//查询拍卖
				String timeInput = request.getParameter("timeInput");
				if (in != null || err != null) {
					out.print("不要太快刷新界面，上次的还未执行完");
					out.print("<br/>");
					return;
				}
				try {
					String path = "/admin/tradeAuctionSH.sh";
					path = request.getRealPath(path);
					String logPath;
					if (timeInput == null || timeInput.equals("")) {
						logPath = HOME_PATH + "/log/game_server/AuctionSubSystem.log";
					}else {
						logPath = HOME_PATH + "/log/game_server/AuctionSubSystem.log." + timeInput + ".log";
					}
					System.out.println(logPath);
					String exec = "sh " + path + " " + logPath;
					Process p = Runtime.getRuntime().exec(exec);
					in = p.getInputStream();
					err = p.getErrorStream();
					
					p.waitFor();
					
					Thread.sleep(3000);
					
					//对结果进行排序
					sortIds = new ArrayList<Long>();
					moneys = new ArrayList<Long>();
					Long[] auctionBuyids = auctions.keySet().toArray(new Long[0]);
					for (Long id : auctionBuyids){
						long money = auctions.get(id);
						int i = 0;
						for (; i < moneys.size(); i++) {
							Long money1 = moneys.get(i);
							if (money > money1) {
								sortIds.add(i, id);
								moneys.add(i, money);
								break;
							}
						}
						if (i >= moneys.size() - 1) {
							sortIds.add(id);
							moneys.add(money);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					out.print(e.toString());
				}
			}else if (selectIndex.equals("3")) {
				String timeInput = request.getParameter("timeInput");
				if (in != null || err != null) {
					out.print("不要太快刷新界面，上次的还未执行完");
					out.print("<br/>");
					return;
				}
				try {
					String path = "/admin/tradeBoothsaleSH.sh";
					path = request.getRealPath(path);
					String logPath;
					if (timeInput == null || timeInput.equals("")) {
						logPath = HOME_PATH + "/log/game_server/BoothsaleManager.log";
					}else {
						logPath = HOME_PATH + "/log/game_server/BoothsaleManager.log." + timeInput + ".log";
					}
					System.out.println(logPath);
					String exec = "sh " + path + " " + logPath;
					Process p = Runtime.getRuntime().exec(exec);
					in = p.getInputStream();
					err = p.getErrorStream();
					
					p.waitFor();
					
					Thread.sleep(3000);
					
					//对结果进行排序
					sortIds = new ArrayList<Long>();
					moneys = new ArrayList<Long>();
					Long[] auctionBuyids = auctions.keySet().toArray(new Long[0]);
					for (Long id : auctionBuyids){
						long money = auctions.get(id);
						int i = 0;
						for (; i < moneys.size(); i++) {
							Long money1 = moneys.get(i);
							if (money > money1) {
								sortIds.add(i, id);
								moneys.add(i, money);
								break;
							}
						}
						if (i >= moneys.size() - 1) {
							sortIds.add(id);
							moneys.add(money);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					out.print(e.toString());
				}
			}
			
			
			response.sendRedirect("./tradeHeiKa.jsp");
		}
	%>
	
<form id="f2" name="f2" method="get">
	<select name="selectIndex">
	<option value="0">查询拍卖
	<option value="1">查询交易
	<option value="2">查询求购
	<option value="3">查询摆摊
	</select>
	输入要查询的日期<input name="timeInput">&nbsp;例如:2012-08-08
	<input type="submit" value="查询">
</form>
	<%
		if (sortIds != null && moneys != null) {
			out.print("总共在拍卖购买物品的玩家个数" + sortIds.size());
			out.print("</br>");
	%>
		<table border="3">
			<tr>
				<td><%="购买拍卖者ID" %></td>
				<td><%="购买拍卖者名字" %></td>
				<td><%="等级" %></td>
				<td><%="花费的金额" %></td>
			</tr>
			<%
				
					int n  = sortIds.size();
					if (n > 15){
						n = 15;
					}
					for (int i = 0; i < n; i++) {
						PlayerSimpleInfo info = PlayerSimpleInfoManager.getInstance().getInfoById(sortIds.get(i));
						String name = "?未知?";
						int level = -1;
						if (info != null) {
							name = info.getName();
							level = info.getLevel();
						}
			%>
						<tr>
							<td><%=sortIds.get(i) %></td>
							<td><%=name %></td>
							<td><%=level %></td>
							<td><%=BillingCenter.得到带单位的银两(moneys.get(i)) %></td>
						</tr>
			<%
					}
			%>
		</table>
	<%
		}
	%>
</body>
</html>

