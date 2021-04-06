<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.File"%>
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
<title>交易信息查询</title>
</head>
<body>
<%! 
	class TradeMessage {
		String userName;
		String playerName;
		int level;
		long auctionMoney;
		long requestBuyMoney;
		long dealMoney;
		long boothSaleMoney;
		long shopMoney;
	}

	void jiexiMessage(String line, int type){
		Hashtable<String, TradeMessage> table = tradeTable;
		
		String name = line.substring(line.indexOf(" [角色:") + " [角色:".length(), line.indexOf("] [服务器:"));
		String level = line.substring(line.indexOf(" [角色等级:") + " [角色等级:".length(), line.indexOf("] [账户变化:"));
		String money = line.substring(line.indexOf("[消费金额:") + "[消费金额:".length(), line.indexOf("] [消费渠道"));
		TradeMessage oldMoney = table.get(name);
		if (oldMoney != null) {
			if (type == 0) {
				oldMoney.auctionMoney =  oldMoney.auctionMoney + Long.valueOf(money);
			}else if (type == 1) {
				oldMoney.requestBuyMoney =  oldMoney.requestBuyMoney + Long.valueOf(money);
			}else if (type == 2) {
				oldMoney.dealMoney =  oldMoney.dealMoney + Long.valueOf(money);
			}else if (type == 3) {
				oldMoney.boothSaleMoney =  oldMoney.boothSaleMoney + Long.valueOf(money);
			}else if (type == 4) {
				oldMoney.shopMoney =  oldMoney.shopMoney + Long.valueOf(money);
			}
			oldMoney.level = Integer.valueOf(level);
			table.put(name, oldMoney);
		}else {
			String userName = line.substring(line.indexOf(" [用户:") + " [用户:".length(), line.indexOf("] [角色:"));
			oldMoney = new TradeMessage();
			oldMoney.playerName = name;
			oldMoney.userName = userName;
			if (type == 0) {
				oldMoney.auctionMoney =  Long.valueOf(money);
			}else if (type == 1) {
				oldMoney.requestBuyMoney =  Long.valueOf(money);
			}else if (type == 2) {
				oldMoney.dealMoney =  Long.valueOf(money);
			}else if (type == 3) {
				oldMoney.boothSaleMoney =  Long.valueOf(money);
			}else if (type == 4) {
				oldMoney.shopMoney =  Long.valueOf(money);
			}
			oldMoney.level = Integer.valueOf(level);
			table.put(name, oldMoney);
		}
	}	

	int showNum = 15;

	boolean isHavaFile = true;
	String rizhiPath = "";
	
	//String为角色名字
	Hashtable<String,TradeMessage> tradeTable = new Hashtable<String,TradeMessage>();
	
	ArrayList<TradeMessage> AuctionTMs = new ArrayList<TradeMessage>();
	ArrayList<TradeMessage> RequestBuyTMs = new ArrayList<TradeMessage>();
	ArrayList<TradeMessage> DealTMs = new ArrayList<TradeMessage>();
	ArrayList<TradeMessage> BoothSaleTMs = new ArrayList<TradeMessage>();
	ArrayList<TradeMessage> ShopTMs = new ArrayList<TradeMessage>();
	
%>
	<%
	
		String timeInput = request.getParameter("timeInput");
		if (timeInput != null){
			AuctionTMs.clear();
			RequestBuyTMs.clear();
			DealTMs.clear();
			BoothSaleTMs.clear();
			ShopTMs.clear();
			String HOME_PATH = System.getProperty("user.dir");
			//查询拍卖
			try {
				String logPath;
				if (timeInput == null || timeInput.equals("")) {
					logPath = HOME_PATH + "/log/game_server/billingCenter.log";
				}else {
					logPath = HOME_PATH + "/log/game_server/billingCenter.log." + timeInput + ".log";
				}
				System.out.println(logPath);
				rizhiPath = logPath;
				File f = new File(logPath);
				if (!f.isFile()) {
					isHavaFile = false;
				}else {
					isHavaFile = true;
					BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
					String line  = null;
					while((line = br.readLine()) != null) {
						if (line.indexOf("消费") != -1 && line.indexOf(" [失败:") == -1) {
							if (line.indexOf("消费渠道:拍卖") != -1) {
								jiexiMessage(line, 0);
							}else if (line.indexOf("消费渠道:求购") != -1) {
								jiexiMessage(line, 1);
							}else if (line.indexOf("消费渠道:交易") != -1) {
								jiexiMessage(line, 2);
							}else if (line.indexOf("消费渠道:摆摊") != -1) {
								jiexiMessage(line, 3);
							}else if (line.indexOf("消费渠道:商店购买") != -1) {
								jiexiMessage(line, 4);
							}
						}
					}
					br.close();
					{
						String[] tradeNames = tradeTable.keySet().toArray(new String[0]);
						for (String name : tradeNames) {
							TradeMessage tm =  tradeTable.get(name);
							long money = tm.auctionMoney;
							int i = 0;
							for (; i < AuctionTMs.size(); i++) {
								long money1 = AuctionTMs.get(i).auctionMoney;
								if (money > money1) {
									AuctionTMs.add(i, tm);
									break;
								}
							}
							if (i >= AuctionTMs.size() - 1) {
								AuctionTMs.add(tm);
							}
							money = tm.requestBuyMoney;
							i = 0;
							for (; i < RequestBuyTMs.size(); i++) {
								long money1 = RequestBuyTMs.get(i).requestBuyMoney;
								if (money > money1) {
									RequestBuyTMs.add(i, tm);
									break;
								}
							}
							if (i >= RequestBuyTMs.size() - 1) {
								RequestBuyTMs.add(tm);
							}
							money = tm.dealMoney;
							i = 0;
							for (; i < DealTMs.size(); i++) {
								long money1 = DealTMs.get(i).dealMoney;
								if (money > money1) {
									DealTMs.add(i, tm);
									break;
								}
							}
							if (i >= DealTMs.size() - 1) {
								DealTMs.add(tm);
							}
							money = tm.boothSaleMoney;
							i = 0;
							for (; i < BoothSaleTMs.size(); i++) {
								long money1 = BoothSaleTMs.get(i).boothSaleMoney;
								if (money > money1) {
									BoothSaleTMs.add(i, tm);
									break;
								}
							}
							if (i >= BoothSaleTMs.size() - 1) {
								BoothSaleTMs.add(tm);
							}
							money = tm.shopMoney;
							i = 0;
							for (; i < ShopTMs.size(); i++) {
								long money1 = ShopTMs.get(i).shopMoney;
								if (money > money1) {
									ShopTMs.add(i, tm);
									break;
								}
							}
							if (i >= ShopTMs.size() - 1) {
								ShopTMs.add(tm);
							}
						}
						System.out.println(tradeTable.size() + "-----" + AuctionTMs.size());
					}
					
					tradeTable.clear();
					
					if (AuctionTMs.size() > showNum) {
						AuctionTMs = (ArrayList<TradeMessage>)(AuctionTMs.subList(0, showNum));
					}
					if (RequestBuyTMs.size() > showNum) {
						RequestBuyTMs = (ArrayList<TradeMessage>)RequestBuyTMs.subList(0, showNum);
					}
					if (DealTMs.size() > showNum) {
						DealTMs = (ArrayList<TradeMessage>)DealTMs.subList(0, showNum);
					}
					if (BoothSaleTMs.size() > showNum) {
						BoothSaleTMs = (ArrayList<TradeMessage>)BoothSaleTMs.subList(0, showNum);
					}
					if (ShopTMs.size() > showNum) {
						ShopTMs = (ArrayList<TradeMessage>)ShopTMs.subList(0, showNum);
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				out.print(e.toString());
			}
			response.sendRedirect("./censusTradeMoney4Java.jsp");
		}
	%>
	
<form id="f2" name="f2" method="get">
	输入要查询的日期<input name="timeInput">&nbsp;例如:2012-08-09
	<input type="submit" value="查询">
	</br>
	<% 
		if(!isHavaFile) {
			out.print("日志文件不存在" + rizhiPath);
			out.print("</br>");
		} else {
	%>
		</br>
		拍卖信息
		<table border="3">
			<tr>
				<td><%="用户名" %></td>
				<td><%="名字" %></td>
				<td><%="等级" %></td>
				<td><%="拍卖花费" %></td>
				<td><%="交易花费" %></td>
				<td><%="摆摊花费" %></td>
				<td><%="商店花费" %></td>
				<td><%="求购花费" %></td>
			</tr>
	<%
			for (int i = 0; i < showNum; i++) {
				if (i < AuctionTMs.size()) {
					
	%>
			<tr>
				<td><%=AuctionTMs.get(i).userName %></td>
				<td><%=AuctionTMs.get(i).playerName %></td>
				<td><%=AuctionTMs.get(i).level %></td>
				<td><%=BillingCenter.得到带单位的银两(AuctionTMs.get(i).auctionMoney) %></td>
				<td><%=BillingCenter.得到带单位的银两(AuctionTMs.get(i).dealMoney) %></td>
				<td><%=BillingCenter.得到带单位的银两(AuctionTMs.get(i).boothSaleMoney) %></td>
				<td><%=BillingCenter.得到带单位的银两(AuctionTMs.get(i).shopMoney) %></td>
				<td><%=BillingCenter.得到带单位的银两(AuctionTMs.get(i).requestBuyMoney) %></td>
			</tr>
	<%
				}
			}
	%>
		</table>
		</br>
		交易信息
		<table border="3">
			<tr>
				<td><%="用户名" %></td>
				<td><%="名字" %></td>
				<td><%="等级" %></td>
				<td><%="交易花费" %></td>
				<td><%="拍卖花费" %></td>
				<td><%="摆摊花费" %></td>
				<td><%="商店花费" %></td>
				<td><%="求购花费" %></td>
			</tr>
			
			<%
			for (int i = 0; i < showNum; i++) {
				if (i < DealTMs.size()) {
					
	%>
				<tr>
					<td><%=DealTMs.get(i).userName %></td>
					<td><%=DealTMs.get(i).playerName %></td>
					<td><%=DealTMs.get(i).level %></td>
					<td><%=BillingCenter.得到带单位的银两(DealTMs.get(i).dealMoney) %></td>
					<td><%=BillingCenter.得到带单位的银两(DealTMs.get(i).auctionMoney) %></td>
					<td><%=BillingCenter.得到带单位的银两(DealTMs.get(i).boothSaleMoney) %></td>
					<td><%=BillingCenter.得到带单位的银两(DealTMs.get(i).shopMoney) %></td>
					<td><%=BillingCenter.得到带单位的银两(DealTMs.get(i).requestBuyMoney) %></td>
				</tr>
	<%
				}
			}
	%>
		</table>
		</br>
		摆摊信息
		<table border="3">
			<tr>
				<td><%="用户名" %></td>
				<td><%="名字" %></td>
				<td><%="等级" %></td>
				<td><%="摆摊花费" %></td>
				<td><%="拍卖花费" %></td>
				<td><%="交易花费" %></td>
				<td><%="商店花费" %></td>
				<td><%="求购花费" %></td>
			</tr>
			
			<%
			for (int i = 0; i < showNum; i++) {
				if (i < BoothSaleTMs.size()) {
					
	%>
				<tr>
					<td><%=BoothSaleTMs.get(i).userName %></td>
					<td><%=BoothSaleTMs.get(i).playerName %></td>
					<td><%=BoothSaleTMs.get(i).level %></td>
					<td><%=BillingCenter.得到带单位的银两(BoothSaleTMs.get(i).boothSaleMoney) %></td>
					<td><%=BillingCenter.得到带单位的银两(BoothSaleTMs.get(i).auctionMoney) %></td>
					<td><%=BillingCenter.得到带单位的银两(BoothSaleTMs.get(i).dealMoney) %></td>
					<td><%=BillingCenter.得到带单位的银两(BoothSaleTMs.get(i).shopMoney) %></td>
					<td><%=BillingCenter.得到带单位的银两(BoothSaleTMs.get(i).requestBuyMoney) %></td>
				</tr>
	<%
				}
			}
	%>
		</table>
		</br>
		商店信息
		<table border="3">
			<tr>
				<td><%="用户名" %></td>
				<td><%="名字" %></td>
				<td><%="等级" %></td>
				<td><%="商店花费" %></td>
				<td><%="拍卖花费" %></td>
				<td><%="交易花费" %></td>
				<td><%="摆摊花费" %></td>
				<td><%="求购花费" %></td>
			</tr>
			
			<%
			for (int i = 0; i < showNum; i++) {
				if (i < ShopTMs.size()) {
					
	%>
				<tr>
					<td><%=ShopTMs.get(i).userName %></td>
					<td><%=ShopTMs.get(i).playerName %></td>
					<td><%=ShopTMs.get(i).level %></td>
					<td><%=BillingCenter.得到带单位的银两(ShopTMs.get(i).shopMoney) %></td>
					<td><%=BillingCenter.得到带单位的银两(ShopTMs.get(i).auctionMoney) %></td>
					<td><%=BillingCenter.得到带单位的银两(ShopTMs.get(i).dealMoney) %></td>
					<td><%=BillingCenter.得到带单位的银两(ShopTMs.get(i).boothSaleMoney) %></td>
					<td><%=BillingCenter.得到带单位的银两(ShopTMs.get(i).requestBuyMoney) %></td>
				</tr>
	<%
				}
			}
	%>
		</table>
		</br>
		求购信息
		<table border="3">
			<tr>
				<td><%="用户名" %></td>
				<td><%="名字" %></td>
				<td><%="等级" %></td>
				<td><%="求购花费" %></td>
				<td><%="拍卖花费" %></td>
				<td><%="交易花费" %></td>
				<td><%="摆摊花费" %></td>
				<td><%="商店花费" %></td>
			</tr>
			
			<%
			for (int i = 0; i < showNum; i++) {
				if (i < RequestBuyTMs.size()) {
					
	%>
				<tr>
					<td><%=RequestBuyTMs.get(i).userName %></td>
					<td><%=RequestBuyTMs.get(i).playerName %></td>
					<td><%=RequestBuyTMs.get(i).level %></td>
					<td><%=BillingCenter.得到带单位的银两(RequestBuyTMs.get(i).requestBuyMoney) %></td>
					<td><%=BillingCenter.得到带单位的银两(RequestBuyTMs.get(i).auctionMoney) %></td>
					<td><%=BillingCenter.得到带单位的银两(RequestBuyTMs.get(i).dealMoney) %></td>
					<td><%=BillingCenter.得到带单位的银两(RequestBuyTMs.get(i).boothSaleMoney) %></td>
					<td><%=BillingCenter.得到带单位的银两(RequestBuyTMs.get(i).shopMoney) %></td>
				</tr>
	<%
				}
			}
	%>
		</table>
	<%
		}
	%>
	
</form>
</body>
</html>

