<%@page import="com.fy.engineserver.mail.MailSendType"%>
<%@ page contentType="text/html;charset=utf-8" import="java.util.*,java.util.concurrent.*,java.io.*,
com.xuanzhi.tools.text.*,com.fy.engineserver.core.*,com.xuanzhi.tools.servlet.HttpUtils,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.fy.engineserver.datasource.article.concrete.*,
com.fy.engineserver.datasource.article.manager.*,com.fy.engineserver.mail.service.*,
com.fy.engineserver.datasource.article.data.articles.*,
com.fy.engineserver.datasource.article.data.entity.*,com.fy.engineserver.datasource.article.data.equipments.*"%><%! 
String ip = "";
String recorder = "";
	public static class Item{
		public String articleName;
		public long playerId;
		public long articleId;
		public boolean zaixingFlag;
		public int level = -1;
	}

	public static int[][] DATA=new int[][]{
		{1,0,0,0,0}, //1
		{2,0,0,0,0}, //2
		{3,0,0,0,0}, //3
		{4,0,0,0,0}, //4
		{2,1,0,0,0}, //5
		{1,2,0,0,0}, //6
		{0,3,0,0,0}, //7
		{0,4,0,0,0}, //8
		{0,3,1,0,0}, //9 
		{0,2,2,0,0}, //10
		{0,1,3,0,0}, //11
		{0,0,4,0,0}, //12
		{0,0,3,1,0}, //13
		{0,0,2,2,0}, //14 
		{0,0,1,3,0}, //15
		{0,0,0,4,0}, //16
		{0,0,0,3,1}, //17
		{0,0,0,2,2}, //18
		{0,0,0,1,3}, //19
		{0,0,0,0,4}, //20
	};
	
	public static String COLOR[] = new String[]{"白","绿","蓝","紫","橙"};

	public String buchang(Player player,EquipmentEntity ee,ArrayList<Item> al){
		boolean b = false;
		for(int i = 0 ; i < al.size() ; i++){
			Item item = al.get(i);
			if(item.zaixingFlag == false && item.level > 0){
				b = true;
			}
		}
		if(b == false){
			String message = "[摘星失败补偿] [无需补偿] ["+player.getId()+"] ["+player.getName()+"] ["+ee.getId()+"] ["+ee.getArticleName()+"] [-]";
			System.out.println(message);
			return message;
		}
		
		try{
			MailManager mm = MailManager.getInstance();
			ArticleEntity entitys[] = null;
			int counts[] = null;
			String title = "["+ee.getArticleName()+"]摘星失败补偿";
			String content= "由于我们的失误导致您为了升级装备而摘星，并且部分摘星失败了。我们已经修改了装备升级系统，恢复为以前的功能。经查询，您的装备["+ee.getArticleName()+"]在摘取第";
			int nums[] = new int[5];
			for(int i = 0 ; i < al.size() ; i++){
				Item item = al.get(i);
				if(item.zaixingFlag == false && item.level > 0){
					content += item.level+"星,";
					int dd[] = DATA[item.level-1];
					for(int j = 0 ; j < dd.length ; j++){
						nums[j] += dd[j];
					}
					
				}
			}
			Article article = ArticleManager.getInstance().getArticle("炼星符");
			ArrayList<ArticleEntity> list = new ArrayList<ArticleEntity>();
			ArrayList<Integer> list2 = new ArrayList<Integer>();
			for(int i = 0; i < nums.length ; i++){
				if(nums[i] > 0){
					ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article,true,ArticleEntityManager.CREATE_REASON_后台,player,i,nums[i],true);
					list.add(ae);
					list2.add(nums[i]);
				}
			}
			counts = new int[list2.size()];
			for(int i = 0; i < counts.length ; i++){
				counts[i] = list2.get(i);
			}
			entitys = new ArticleEntity[list.size()];
			for(int i = 0; i < entitys.length ; i++){
				entitys[i] = list.get(i);
			}
			
			content = content.substring(0,content.length()-1);
			content += "时失败了，附件中的炼星符是我们按照成功率100%需要的颜色和个数给您补偿！给您带来的不便深表歉意！";
		
			
			mm.sendMail(player.getId(),entitys,counts,title,content,0,0,0,"摘星失败补偿",MailSendType.后台发送,player.getName(),ip,recorder);
			
			StringBuffer sb = new StringBuffer();
			for(int i = 0; i < nums.length ; i++){
				sb.append(""+COLOR[i]+":"+nums[i]+",");
			}
			String message = "[摘星失败补偿] [成功] ["+player.getId()+"] ["+player.getName()+"] ["+ee.getId()+"] ["+ee.getArticleName()+"] ["+sb+"]";
			System.out.println(message);
			return message;
		}catch(Exception e){
			String message = "[摘星失败补偿] [失败] ["+player.getId()+"] ["+player.getName()+"] ["+ee.getId()+"] ["+ee.getArticleName()+"] [-] ["+e+"]";
			System.out.println(message);
			e.printStackTrace(System.out);
			return message;
		}
	}
	%><%
	ip = request.getRemoteAddr();
	Object o = session.getAttribute("authorize.username");
	if(o!=null){
		recorder = o.toString();
	}
	String action = request.getParameter("action");
	if(action == null ) action = "";
	String password = request.getParameter("password");
	if(password == null ) password = "";
	
	ArrayList<String> duokaiMessage = new ArrayList<String>();
	
	
	String filename = System.getProperty("resin.home")+"/zaixing_data.txt";
	
	
	LinkedHashMap<Long,HashMap<Long,ArrayList<Item>>> zaixingData = new LinkedHashMap<Long,HashMap<Long,ArrayList<Item>>>();
	
	BufferedReader reader = new BufferedReader(new FileReader(filename));
	String line = null;
	int count = 0;
	while((line = reader.readLine()) != null){
		//1101000000002031329 摇光羽纶垫肩 1101000000149930493 很遗憾，摘星失败。
		//1101000000002031329 摇光羽纶垫肩 1101000000149930493 恭喜您摘星成功，您获得了破碎星辰(1级)。
		String ss[] = line.split(" ");
		count++;
		if(ss.length != 4){
			throw new Exception("数据文件有错误，在"+count+"行！");
		}
		long playerId = Long.parseLong(ss[0]);
		String articleName = ss[1];
		long articleId = Long.parseLong(ss[2]);
		String desp = ss[3];
		HashMap<Long,ArrayList<Item>> map = zaixingData.get(playerId);
		if(map == null){
			map = new HashMap<Long,ArrayList<Item>> ();
			zaixingData.put(playerId,map);
		}
		ArrayList<Item> al = map.get(articleId);
		if(al == null){
			al = new ArrayList<Item>();
			map.put(articleId,al);
		}
		Item item = new Item();
		item.articleName = articleName;
		item.articleId = articleId;
		item.playerId = playerId;
		if(desp.contains("恭喜您摘星成功，您获得了破碎星辰")){
			item.zaixingFlag = true;
			int a = desp.indexOf("(");
			int b = desp.indexOf("级)");
			item.level = Integer.parseInt(desp.substring(a+1,b));
		}else{
			item.zaixingFlag = false;
		}
		al.add(item);
	}
	reader.close();
	
	PlayerManager sm = PlayerManager.getInstance();
	ArticleEntityManager aem = ArticleEntityManager.getInstance();
	ArticleManager am = ArticleManager.getInstance();
	
	int buChangStat[] = new int[20];
	Iterator<Long> it = zaixingData.keySet().iterator();
	while(it.hasNext()){

		Long id = it.next();
		Player player = sm.getPlayer(id);
		HashMap<Long,ArrayList<Item>> map = zaixingData.get(id);
		Iterator<Long> it2 = map.keySet().iterator();
		while(it2.hasNext()){
			Long aId = it2.next();
			EquipmentEntity ee = (EquipmentEntity)aem.getEntity(aId);
			Equipment e = (Equipment)am.getArticle(ee.getArticleName());
			ArrayList<Item> al = map.get(aId);
			
			if(e.getPlayerLevelLimit() >= 80 && ee.getColorType() >= 3){
				
				Item firstTrue = null;
				int firstIndex= -1;
				for(int i = al.size() -1 ; i>= 0 ; i--){
					Item item = al.get(i);
					if(firstTrue == null && item.zaixingFlag){
						firstTrue = item;
						firstIndex = i;
					}else if(firstTrue == null && item.zaixingFlag == false){
						//
					}else if(firstTrue != null && item.zaixingFlag){
						firstTrue = item;
						firstIndex = i;
					}else{
						item.level = firstTrue.level + (firstIndex - i);
					}
				}
				//
				firstTrue = null;
				firstIndex= -1;
				for(int i = 0; i< al.size() ; i++){
					Item item = al.get(i);
					if(firstTrue == null && item.zaixingFlag){
						firstTrue = item;
						firstIndex = i;
					}else if(firstTrue == null && item.zaixingFlag == false){
						//
					}else if(firstTrue != null && item.zaixingFlag){
						firstTrue = item;
						firstIndex = i;
					}else if(item.level == -1){
						item.level = firstTrue.level + (firstIndex - i);
					}
				}
				
				boolean allFalse = false;
				//
				for(int i = 0; i< al.size() ; i++){
					Item item = al.get(i);
					if(item.level == -1){
						allFalse = true;
					}
				}
				if(allFalse){
					int ll = ee.getLevel();
					ll = Math.min(ll,20-al.size());
					for(int i = al.size() -1 ; i>= 0 ; i--){
						Item item = al.get(i);
						if(item.level == -1){
							item.level = ll+i+1;
						}
					}
				}
				
				for(int i = 0 ; i < al.size() ; i++){
					Item item = al.get(i);
					if(item.level == 0) item.level = 1;
					if(item.zaixingFlag == false){
						buChangStat[item.level-1]++;
					}
				}

				
				//bufa
				if(action.equals("buchang") && password.equals("mijia1234567890")){	
					String s = buchang(player,ee,al);
					duokaiMessage.add(s);
				}
			}
		}
	}

%><html><head>
</HEAD>
<BODY>
<h2>摘星补偿，一共有<%= zaixingData.size() %>人要补偿</h2>
<br><a href="./zaixing_buchang.jsp">刷新此页面</a><br>
<% 
	
	for(int i = 0 ; i < duokaiMessage.size() ;i++){
		out.println(duokaiMessage.get(i)+"<br>");
	}

%>
<h4>摘星统计</h4>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
<%
	for(int i = 1 ; i <= 20 ; i++){
		out.println("<td>"+i+"星</td>");
	}
%></tr>

<tr bgcolor="#FFFFFF" align="center">
<%
	for(int i = 0 ; i < 20 ; i++){
		out.println("<td>"+buChangStat[i]+"</td>");
	}
%></tr>
</table>

<h4>摘星列表</h4>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
<td>编号</td><td>帐号</td><td>等级</td><td>名字</td><td>充值</td>
<td>装备</td>
<%
	for(int i = 1 ; i <= 20 ; i++){
		out.println("<td>"+i+"星</td>");
	}
%>
</tr>
<%
	Long pids[] = zaixingData.keySet().toArray(new Long[0]);
	for(int i = 0 ; i < pids.length ; i++){
		Player player = sm.getPlayer(pids[i]);
		
		HashMap<Long,ArrayList<Item>> map = zaixingData.get(pids[i]);
		Long aids[] = map.keySet().toArray(new Long[0]);
		
		for(int j = 0 ; j < aids.length ; j++){
			EquipmentEntity ee = (EquipmentEntity)aem.getEntity(aids[j]);
			
			ArrayList<Item> al = map.get(aids[j]);
			out.println("<tr bgcolor='#FFFFFF'>");

			if(j == 0){
			
				out.println("<td rowspan='"+aids.length+"'>"+(i+1)+"</td>");
				out.println("<td rowspan='"+aids.length+"'>"+player.getUsername()+"</td>");
				out.println("<td rowspan='"+aids.length+"'>"+player.getLevel()+"</td>");
				out.println("<td rowspan='"+aids.length+"'>"+player.getName()+"</td>");
				out.println("<td rowspan='"+aids.length+"'>"+(player.getRMB()/100)+"</td>");
				
			}
			out.println("<td>"+ee.getArticleName()+"</td>");
			
			for(int k = 1 ; k <= 20 ; k++){
				String ss = "-";
				String color = "#FFFFFF";
				for(int x = 0 ; x < al.size() ; x++){
					Item iii = al.get(x);
					if(iii.level == k){
						if(iii.zaixingFlag){
							ss = "T";
						}else{
							ss = "F";
							color = "#FF0000";
						}
					}
				}
				
				out.println("<td bgcolor='"+color+"'>"+ ss +"</td>");
			}
			out.println("</tr>");
		}
	}
%>
</table>

<form>
<input type='hidden' name='action' value='buchang'>
请输入密码：<input type='text' name='password' value=''>
<input type='submit' value='提 交'>
</form>

</BODY></html>
