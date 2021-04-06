<%@ page contentType="text/html;charset=utf-8" import="java.util.*,java.util.concurrent.*,java.io.*,
com.xuanzhi.tools.text.*,com.fy.engineserver.core.*,com.xuanzhi.tools.servlet.HttpUtils,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.fy.engineserver.datasource.article.concrete.*,
com.fy.engineserver.datasource.article.manager.*,com.fy.engineserver.mail.service.*,
com.fy.engineserver.datasource.article.data.articles.*,
com.fy.engineserver.datasource.article.data.entity.*,com.fy.engineserver.datasource.article.data.equipments.*,
com.fy.engineserver.mail.* "%>
<%
 ip = request.getRemoteAddr();
Object o = session.getAttribute("authorize.username");
if(o!=null){
	recorder = o.toString();
}
%>
<%! 
if(true){return;}
String ip ="";
String recorder = "";
	public String bucha(Player player,ArticleEntity ae){
	try{
		player.getKnapsack_common().autoArrange123();
		if(player.getKnapsack_fangbao() != null){
			player.getKnapsack_fangbao().autoArrange123();
		}
		if(player.getKnapsacks_QiLing() != null){
			player.getKnapsacks_QiLing().autoArrange123();
		}
		if(player.getKnapsacks_cangku() != null){
			player.getKnapsacks_cangku().autoArrange123();
		}
		String message;
		if(!player.putToKnapsacks(ae,"数据修复")){
			MailManager mm = MailManager.getInstance();
			String title = "["+ae.getArticleName()+"]遗失返还";
			String content= "很诚挚的跟您说一声抱歉，由于游戏功能的问题您的装备出现了遗失，我们第一时间为您进行了查询，同时为您追回了遗失的装备，请您在附件中查收。您的心情我们感同身受，深深地对您说一声抱歉，望您谅解。谢谢您的支持和理解！";

			mm.sendMail(player.getId(),new ArticleEntity[]{ae},title,content,0,0,0,"数据修复",MailSendType.后台发送,player.getName(),ip,recorder);

			message = "[数据修复] [成功] ["+player.getId()+"] ["+player.getName()+"] ["+ae.getId()+"] ["+ae.getArticleName()+"] ["+ae.getColorType()+"] ["+ae.isBinded()+"]";
			System.out.println(message);
		}else{
			message = "放入玩家背包"+player.getLogString()+" "+ae.getArticleName();
			System.out.println(message);
		}

		return message;
	}catch(Exception e){
		String message = "[数据修复] [失败] ["+player.getId()+"] ["+player.getName()+"] ["+ae.getId()+"] ["+ae.getArticleName()+"] [-] ["+e+"]";
		System.out.println(message);
		e.printStackTrace(System.out);
		return message;
	}
	}

public String buchangQiling1(Player player,ArticleEntity ae){
	try{
		player.getKnapsack_common().autoArrange123();
		if(player.getKnapsack_fangbao() != null){
			player.getKnapsack_fangbao().autoArrange123();
		}
		if(player.getKnapsacks_QiLing() != null){
			player.getKnapsacks_QiLing().autoArrange123();
		}
		if(player.getKnapsacks_cangku() != null){
			player.getKnapsacks_cangku().autoArrange123();
		}
		String message;
		if(!player.putToKnapsacks(ae,"数据修复")){
			MailManager mm = MailManager.getInstance();
			String title = "["+ae.getArticleName()+"]，回归的器灵";
			String content= " 您好，由于服务器数据问题，您的器灵出现了情况，对于此问题对您造成的困扰，我们深表歉意！根据您获得器灵的记录，我们为您补回了原始的器灵，恳请您在附件中查收，期望您的谅解！感谢您对飘渺寻仙曲的支持，让您久等了，真心向您道歉！";

			mm.sendMail(player.getId(),new ArticleEntity[]{ae},title,content,0,0,0,"数据修复",MailSendType.后台发送,player.getName(),ip,recorder);

			message = "[数据修复] [成功] ["+player.getId()+"] ["+player.getName()+"] ["+ae.getId()+"] ["+ae.getArticleName()+"] ["+ae.getColorType()+"] ["+ae.isBinded()+"]";
			System.out.println(message);
		}else{
			message = "放入玩家背包"+player.getLogString()+" "+ae.getArticleName();
			System.out.println(message);
		}

		return message;
	}catch(Exception e){
		String message = "[数据修复] [失败] ["+player.getId()+"] ["+player.getName()+"] ["+ae.getId()+"] ["+ae.getArticleName()+"] [-] ["+e+"]";
		System.out.println(message);
		e.printStackTrace(System.out);
		return message;
	}
	}
public String buchangQil(Player player){
	try{
			String message;
			MailManager mm = MailManager.getInstance();
			String title = "器灵补偿";
			String content= " 您好，由于服务器数据问题，您的器灵出现了情况，对于此问题对您造成的困扰，我们深表歉意！我们特意为您奉上500两银子作为额外的补偿，恳请您在附件中查收，期望您的谅解！感谢您对飘渺寻仙曲的支持，让您久等了，真心向您道歉！";

			mm.sendMail(player.getId(),new ArticleEntity[0],title,content,500000,0,0,"数据修复",MailSendType.后台发送,player.getName(),ip,recorder);

			message = "[数据修复] [补偿银两成功] ["+player.getId()+"] ["+player.getName()+"]";
			System.out.println(message);

		return message;
	}catch(Exception e){
		String message = "[数据修复] [补偿银两失败] ["+player.getId()+"] ["+player.getName()+"] ["+e+"]";
		System.out.println(message);
		e.printStackTrace(System.out);
		return message;
	}
	}
public String buchangXil(Player player,int count){
	try{
String message;
			MailManager mm = MailManager.getInstance();
			String title = "洗练补偿";
			String content= "您好，因游戏功能问题，您之前的洗练出现了丢失，我们郑重的向您表示歉意，对于此问题，我们将以一次洗练10两银子为您进行补偿（非绑银）（您共进行"+count+"次洗练），请您务必收下。对于给您带来的困扰，请您务必原谅，感谢那您的飘渺寻仙曲的支持！";

			mm.sendMail(player.getId(),new ArticleEntity[0],title,content,1l*count*10000,0,0,"数据修复",MailSendType.后台发送,player.getName(),ip,recorder);

			message = "[数据修复] [洗练补偿成功] ["+player.getId()+"] ["+player.getName()+"]";
			System.out.println(message);
		return message;
	}catch(Exception e){
		String message = "[数据修复] [洗练补偿失败] ["+player.getId()+"] ["+player.getName()+"] ["+e+"]";
		System.out.println(message);
		e.printStackTrace(System.out);
		return message;
	}
	}
	%><%
	String action = request.getParameter("action");
	if(action == null ) action = "";
	String password = request.getParameter("password");
	if(password == null ) password = "";
	
	ArrayList<String> duokaiMessage = new ArrayList<String>();
	
	
	//String filename = System.getProperty("resin.home")+"/整理丢失.txt";
	
	String filename1 = System.getProperty("resin.home")+"/qiling.txt";
	
	//String filename2 = System.getProperty("resin.home")+"/xilian.txt";
	
	
	PlayerManager sm = PlayerManager.getInstance();
	ArticleEntityManager aem = ArticleEntityManager.getInstance();

	//物品id,玩家id
	HashMap<Long,Long> zaixingData = new HashMap<Long,Long>();
	//玩家id,器灵名字集合
	HashMap<Long,ArrayList<String>> qilingData = new HashMap<Long,ArrayList<String>>();
	//玩家id,洗练次数
	HashMap<Long,Integer> xilianData = new HashMap<Long,Integer>();
	{}
	{
		BufferedReader reader = new BufferedReader(new FileReader(filename1));
		String line = null;
		int count = 0;
		while((line = reader.readLine()) != null){
			//1103000000000085120 1103000000065753260(playerId,articleEntityId)
			String ss[] = line.split(" ");
			count++;
			if(ss.length != 2){
				throw new Exception("数据文件有错误，在"+count+"行！");
			}
			long playerId = Long.parseLong(ss[0]);
			String articleName = ss[1];
			ArrayList<String> sss = qilingData.get(playerId);
			if(sss == null){
				sss = new ArrayList<String>();
				qilingData.put(playerId,sss);
			}
			sss.add(articleName);
		}
		reader.close();
	}
	{}
	//bufa
	if(action.equals("buchang") && password.equals("mijia1234567890")){	
		for(Long id : zaixingData.keySet()){
			try{
				ArticleEntity ae = aem.getEntity(id);
				if(ae != null){
					{}
					Player p = sm.getPlayer(zaixingData.get(id));
					if(p != null){
						//String s = buchang(p,ae);
						//duokaiMessage.add(s);
					}
				}
			}catch(Exception ex){
				
			}
		}
	}
	MailManager mm = MailManager.getInstance();
	if(action.equals("buchang") && password.equals("mijia1234567890qiling")){
		ArticleManager am = ArticleManager.getInstance();
		for(Long playerId : qilingData.keySet()){
			try{
				{}
				Player p = sm.getPlayer(playerId);
				List<Mail> mails = mm.getBetweenMails(p,new Date(System.currentTimeMillis() - 7200000),new Date());
				if(mails != null){
					for(Mail m : mails){
						if(m.getTitle().indexOf("回归的器灵") > 0){
							mm.deleteMail(m.getId());
							System.out.println("删除了一封邮件id"+m.getId()+" "+p.getLogString());
						}
					}
				}
				ArrayList<String> as = qilingData.get(playerId);
				for(String str : as){
					Article a = am.getArticle(str);
					ArticleEntity ae = aem.createEntity(a,false,ArticleEntityManager.CREATE_REASON_BUCHANG,p,0,1,true);
					String s = buchangQiling1(p,ae);
					duokaiMessage.add(s);
				}
				
				//String s = buchangQiling2(p);
				//duokaiMessage.add(s);
			}catch(Exception ex){
				
			}
		}
	}
	if(action.equals("buchang") && password.equals("mijia1234567890xilian")){	
		
		for(Long playerId : xilianData.keySet()){
			try{
				{}
				Player p = sm.getPlayer(playerId);
				int count = xilianData.get(playerId);
				if(count > 0){
					//String s = buchangXilian(p,count);
					//duokaiMessage.add(s);
				}
			}catch(Exception ex){
				
			}
		}
	}
%><html><head>
</HEAD>
<BODY>
<h2>补偿，一共有<%= zaixingData.size() %>物品要补偿</h2>
<br><a href="./shujuxiufu_buchang.jsp">刷新此页面</a><br>
<% 
	
	for(int i = 0 ; i < duokaiMessage.size() ;i++){
		out.println(duokaiMessage.get(i)+"<br>");
	}

%>

<h4>补偿列表</h4>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
<td>编号</td><td>帐号</td><td>等级</td><td>名字</td><td>充值</td>
<td>物品</td><td>颜色</td><td>绑定</td><td>数量</td>
</tr>
<%
	Long articleIds[] = zaixingData.keySet().toArray(new Long[0]);
	for(int i = 0 ; i < articleIds.length ; i++){
		long articleId = articleIds[i];
		Long pid = zaixingData.get(articleId);
		Player player = sm.getPlayer(pid);

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
		out.println("<td>"+(ae != null ? ae.getReferenceCount():"-")+"</td>");
		out.println("</tr>");
		
	}
%>
</table>

<h4>补偿列表</h4>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
<td>编号</td><td>帐号</td><td>等级</td><td>名字</td><td>充值</td>
<td>物品</td>
</tr>
<%
	Long players[] = qilingData.keySet().toArray(new Long[0]);
	for(int i = 0 ; i < players.length ; i++){
		ArrayList<String> al = qilingData.get(players[i]);
		Player player = sm.getPlayer(players[i]);

		out.println("<tr bgcolor='#FFFFFF'>");
		out.println("<td >"+(i+1)+"</td>");
		out.println("<td >"+player.getUsername()+"</td>");
		out.println("<td >"+player.getLevel()+"</td>");
		out.println("<td >"+player.getName()+"</td>");
		out.println("<td >"+(player.getRMB()/100)+"</td>");
				

			
		StringBuffer sb = new StringBuffer();
		for(String a : al){
			sb.append(a+",");
		}
		out.println("<td>"+(sb.toString())+"</td>");
		out.println("</tr>");
		
	}
%>
</table>

<h4>补偿列表</h4>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
<td>编号</td><td>帐号</td><td>等级</td><td>名字</td><td>充值</td>
<td>补偿钱</td>
</tr>
<%
	Long playerss[] = xilianData.keySet().toArray(new Long[0]);
	for(int i = 0 ; i < playerss.length ; i++){
		Integer count = xilianData.get(playerss[i]);
		Player player = sm.getPlayer(playerss[i]);

		out.println("<tr bgcolor='#FFFFFF'>");
		out.println("<td >"+(i+1)+"</td>");
		out.println("<td >"+player.getUsername()+"</td>");
		out.println("<td >"+player.getLevel()+"</td>");
		out.println("<td >"+player.getName()+"</td>");
		out.println("<td >"+(player.getRMB()/100)+"</td>");
				

			
		
		out.println("<td>"+count*10+"两</td>");
		out.println("</tr>");
		
	}
%>
</table>

<form>
<input type='hidden' name='action' value='buchang'>
请输入密码：<input type='text' name='password' value=''>
<input type='submit' value='提 交'>
</form>

</BODY></html>
