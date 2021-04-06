<%@ page contentType="text/html;charset=utf-8" import="java.util.*,java.util.concurrent.*,java.io.*,
com.xuanzhi.tools.text.*,com.fy.engineserver.core.*,com.xuanzhi.tools.servlet.HttpUtils,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.fy.engineserver.datasource.article.concrete.*,
com.fy.engineserver.datasource.article.manager.*,com.fy.engineserver.mail.service.*,
com.fy.engineserver.datasource.article.data.articles.*,
com.fy.engineserver.datasource.article.data.entity.*,com.fy.engineserver.datasource.article.data.equipments.*,
com.fy.engineserver.mail.* "%>
<%
String ip = request.getRemoteAddr();
String recorder = "";
Object o = session.getAttribute("authorize.username");
if(o!=null){
	recorder = o.toString();
}
%>
<%! 

public static class Item{
	public long playerId;
	public long articleId;
	public int count;
	public long money;
}

public static class Item2{
	public long playerId;
	public long articleId;
	public boolean buchangFlag;
	public String aName;
}
String ip ="";
String recorder = "";

	public String buchang(Player player,long eId,int count,long money){
		try{
			String message;

			MailManager mm = MailManager.getInstance();
			ArticleEntityManager aem = ArticleEntityManager.getInstance();
			ArticleManager am = ArticleManager.getInstance();
			Article a = am.getArticle("炼器符");
			ArticleEntity ae = aem.createEntity(a,false,ArticleEntityManager.CREATE_REASON_BUCHANG,player,a.getColorType(),count,true);
			
			String title = "炼器";
			String content= " 您好，由于服务器问题，您之前的炼器操作出现了丢失情况，对于这一情况我们向您致以最真挚的歉意。目前功能已经修复，我们将返还您炼器花费的炼器符（非绑定）！";

			mm.sendMail(player.getId(),new ArticleEntity[]{ae},new int[]{count},title,content,0,0,0,"数据修复",MailSendType.后台发送,player.getName(),ip,recorder);

			if(money > 0){
				title = "炼器银子返还";
				content= " 您好，由于服务器问题，您之前的炼器操作出现了丢失情况，对于这一情况我们向您致以最真挚的歉意。目前功能已经修复，我们将返还您炼器绑定器灵孔花费的资金，感谢您的理解和支持！";
				mm.sendMail(-1,player.getId(),new ArticleEntity[0],title,content,money,0,0,"数据修复",MailSendType.后台发送,player.getName(),ip,recorder);
			}
			message = "[数据修复] [成功] ["+player.getId()+"] ["+player.getName()+"] ["+ae.getId()+"] ["+ae.getArticleName()+"] ["+ae.getColorType()+"] ["+ae.isBinded()+"]";
			System.out.println(message);

			return message;
		}catch(Exception e){
			String message = "[数据修复] [失败] ["+player.getId()+"] ["+player.getName()+"] ["+e+"]";
			System.out.println(message);
			e.printStackTrace(System.out);
			return message;
		}
	}

	public String buchangMoney(Player player,long money){
		try{
			String message = "";
			MailManager mm = MailManager.getInstance();
			if(money > 0){
				String title = "炼器补偿";
				String content= " 您好，由于服务器问题，您之前的炼器操作出现了丢失情况，对于这一情况我们向您致以最真挚的歉意。奉上500两银子作为补偿请您笑纳，感谢您的理解和支持！";
				mm.sendMail(-1,player.getId(),new ArticleEntity[0],title,content,money,0,0,"数据修复",MailSendType.后台发送,player.getName(),ip,recorder);
				message = "[数据修复] [成功] ["+player.getId()+"] ["+player.getName()+"]";
				System.out.println(message);
			}
			return message;
		}catch(Exception e){
			String message = "[数据修复] [失败] ["+player.getId()+"] ["+player.getName()+"] ["+e+"]";
			System.out.println(message);
			e.printStackTrace(System.out);
			return message;
		}
	}

public String buchangQiling1(Player player,ArticleEntity ae,String equipmentName){
	try{
		String message;

		MailManager mm = MailManager.getInstance();
		String title = "["+ae.getArticleName()+"]，回归的器灵";
		String content= " 您好，您的器灵出现了丢失情况，对于此问题对您造成的困扰，我们深表歉意！根据您获得器灵的记录，我们为您补回了器灵，恳请您在附件中查收，期望您的谅解！感谢您对飘渺寻仙曲的支持，让您久等了，真心向您道歉！";

		mm.sendMail(player.getId(),new ArticleEntity[]{ae},title,content,0,0,0,"数据修复");

		message = "[数据修复] [成功] ["+player.getId()+"] ["+player.getName()+"] ["+ae.getId()+"] ["+ae.getArticleName()+"] ["+ae.getColorType()+"] ["+ae.isBinded()+"]";
		System.out.println(message);

		return message;
	}catch(Exception e){
		String message = "[数据修复] [失败] ["+player.getId()+"] ["+player.getName()+"] ["+ae.getId()+"] ["+ae.getArticleName()+"] [-] ["+e+"]";
		System.out.println(message);
		e.printStackTrace(System.out);
		return message;
	}
	}
	%><%
	if(true){return;}
	String action = request.getParameter("action");
	if(action == null ) action = "";
	String password = request.getParameter("password");
	if(password == null ) password = "";
	
	ArrayList<String> duokaiMessage = new ArrayList<String>();
	
	
	String filename = System.getProperty("resin.home")+"/炼器消耗.txt";
	
	String filename1 = System.getProperty("resin.home")+"/镶嵌挖取.txt";

	
	PlayerManager sm = PlayerManager.getInstance();
	ArticleEntityManager aem = ArticleEntityManager.getInstance();

	ArrayList<Item> 补偿炼器 = new ArrayList<Item>();
	
	
	{
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String line = null;
		int count = 0;
		while((line = reader.readLine()) != null){
			//1103000000000085120 1103000000065753260(playerId,articleEntityId)
			String ss[] = line.split(" ");
			count++;
			if(ss.length != 4){
				throw new Exception("数据文件有错误，在"+count+"行！");
			}
			String userName = ss[0];
			Player[] ps = sm.getPlayerByUser(userName);
			long eId = Long.parseLong(ss[1]);
			int c = Integer.parseInt(ss[2]);
			long money = Long.parseLong(ss[3]);
			
			Item item = new Item();
			item.playerId = ps[0].getId();
			item.articleId = eId;
			item.count = c;
			item.money = money;
			补偿炼器.add(item);
			
		}
		reader.close();
	}
	HashMap<Long,Item2> 补发器灵 = new HashMap<Long,Item2>();
	{
		BufferedReader reader = new BufferedReader(new FileReader(filename1));
		String line = null;
		int count = 0;
		while((line = reader.readLine()) != null){
			//1103000000000085120 1103000000065753260(playerId,articleEntityId)
			String ss[] = line.split(" ");
			count++;
			if(ss.length != 5){
				throw new Exception("数据文件有错误，在"+count+"行！");
			}
			long playerId = Long.parseLong(ss[0]);
			long aId = Long.parseLong(ss[1]);
			boolean buchangFlag = false;
			if("器灵镶嵌".equals(ss[2])){
				buchangFlag = true;
			}
			String aName = ss[3];
			
			Item2 item = new Item2();
			item.playerId = playerId;
			item.articleId = aId;
			item.buchangFlag = buchangFlag;
			item.aName = aName;
			补发器灵.put(aId,item);
			
		}
		reader.close();
	}
	HashMap<Long,Integer> 补偿人名单 = new HashMap<Long,Integer>();
	for(Item item : 补偿炼器){
		补偿人名单.put(item.playerId,500000);
	}
	//bufa
	if(action.equals("buchang") && password.equals("mijia1234567890")){	
		for(Item item : 补偿炼器){
			try{
				Player p = sm.getPlayer(item.playerId);
				if(p != null){
					{}
					if(p != null){
						String s = buchang(p,item.articleId,item.count,item.money);
						duokaiMessage.add(s);
					}
				}
			}catch(Exception ex){
				
			}
		}
		for(Long playerId : 补偿人名单.keySet()){
			try{
				Player player = sm.getPlayer(playerId);
				String s = buchangMoney(player,补偿人名单.get(playerId));
				duokaiMessage.add(s);
			}catch(Exception ex){
				
			}
		}
	}
	MailManager mm = MailManager.getInstance();
	if(action.equals("buchang") && password.equals("mijia1234567890qiling")){
		for(Long articleId : 补发器灵.keySet()){
			try{
				{}
				ArticleEntity ae = aem.getEntity(articleId);
				if(ae != null){
					Item2 item = 补发器灵.get(articleId);
					Player p = sm.getPlayer(item.playerId);
					if(item.buchangFlag){
						String s = buchangQiling1(p,ae,item.aName);
						duokaiMessage.add(s);
					}
				}
			}catch(Exception ex){
				
			}
		}
	}
%><html><head>
</HEAD>
<BODY>
<h2>补偿补偿炼器，一共有<%= 补偿炼器.size() %>个炼器装备要补偿</h2>
<br><a href="./shujuxiufu_qiling.jsp">刷新此页面</a><br>
<% 
	
	for(int i = 0 ; i < duokaiMessage.size() ;i++){
		out.println(duokaiMessage.get(i)+"<br>");
	}

%>

<h4>补偿列表</h4>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
<td>编号</td><td>帐号</td><td>等级</td><td>名字</td><td>充值</td>
<td>物品</td><td>颜色</td><td>绑定</td><td>炼器次数</td><td>消耗</td>
</tr>
<%
	for(int i = 0 ; i < 补偿炼器.size() ; i++){
		Item item = 补偿炼器.get(i);
		long articleId = item.articleId;
		Player player = sm.getPlayer(item.playerId);

		out.println("<tr bgcolor='#FFFFFF'>");
		out.println("<td >"+(i+1)+"</td>");
		out.println("<td >"+player.getUsername()+"</td>");
		out.println("<td >"+player.getLevel()+"</td>");
		out.println("<td >"+player.getName()+"</td>");
		out.println("<td >"+(player.getRMB()/100)+"</td>");
				

			
		ArticleEntity ae = aem.getEntity(articleId);
		out.println("<td>"+(ae != null ? ae.getArticleName():articleId)+"</td>");
		out.println("<td>"+(ae != null ? ae.getColorType():"-")+"</td>");
		out.println("<td>"+(ae != null ? ae.isBinded():"-")+"</td>");
		out.println("<td>"+item.count+"</td>");
		out.println("<td>"+item.money/1000+"两</td>");
		out.println("</tr>");
		
	}
%>
</table>

<h4>补发器灵列表</h4>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
<td>编号</td><td>帐号</td><td>等级</td><td>名字</td><td>充值</td>
<td>物品</td><td>补不补</td>
</tr>
<%
	Long articleIds[] = 补发器灵.keySet().toArray(new Long[0]);
	for(int i = 0 ; i < articleIds.length ; i++){
		Item2 item = 补发器灵.get(articleIds[i]);
		Player player = sm.getPlayer(item.playerId);

		out.println("<tr bgcolor='#FFFFFF'>");
		out.println("<td >"+(i+1)+"</td>");
		out.println("<td >"+player.getUsername()+"</td>");
		out.println("<td >"+player.getLevel()+"</td>");
		out.println("<td >"+player.getName()+"</td>");
		out.println("<td >"+(player.getRMB()/100)+"</td>");
				

			
		ArticleEntity ae = aem.getEntity(articleIds[i]);
		out.println("<td>"+ae.getArticleName()+"</td>");
		out.println("<td>"+item.buchangFlag+"</td>");
		out.println("</tr>");
		
	}
%>
</table>

<h4>补发炼器银子列表</h4>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
<td>补偿人</td><td>银子</td>
</tr>
<%
	for(Long playerId : 补偿人名单.keySet()){
		Player player = sm.getPlayer(playerId);

		out.println("<tr bgcolor='#FFFFFF'>");
		out.println("<td >"+player.getName()+"</td>");
		out.println("<td >"+补偿人名单.get(playerId)/1000+"两</td>");
	}
%>
</table>

<form>
<input type='hidden' name='action' value='buchang'>
请输入密码：<input type='text' name='password' value=''>
<input type='submit' value='提 交'>
</form>

</BODY></html>
