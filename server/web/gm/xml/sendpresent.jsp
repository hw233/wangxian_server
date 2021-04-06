	<%@ page language="java" contentType="text/html; charset=utf-8"
		pageEncoding="utf-8"%>
	<%@page import="com.fy.engineserver.datasource.article.data.props.Props"%>
	<%@page import="com.fy.engineserver.datasource.article.data.equipments.Equipment"%>
	<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
	<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
	<%@page import="java.net.URLEncoder"%>
	<%@page import="java.net.URLDecoder"%>
	<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager" %>
	<%@page import="com.fy.engineserver.datasource.article.data.articles.Article" %>
	<%@include file="../header.jsp"%>
	<%
	if(true){return;}
	%>
	<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<html>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
			<title>礼品发送</title>
			<link rel="stylesheet" href="../style.css" />
			<script type="text/javascript">
		function $(tag) {
			return document.getElementById(tag);
		}
	    	function change(mid, tag) { 
			//将子定义的内容填入回复框
			var instr = document.getElementById("repcontent" + mid);
			if (tag.value != "") {
				instr.value = tag.value;
			}
		}
		
		function send() {
			var str = "?action=add&mid=1";
			var mailname = $("playname").value;
			str = str + "&playname=" + mailname;
			var title = $("restitle" + 1).value;
			str = str + "&title=" + title;
			var content = $("repcontent" + 1).value;
			str = str + "&content=" + content;
			var item = $("item").value;
			str = str +"&item="+item;
			var mailid = $("mailid").value;
			str = str + "&mailid=" + mailid;
			var count = $("count").value;
			str = str + "&count="+count;
			var color = $("color").value;
			str = str + "&color="+color;
			window.location.replace(str);
		
		}
		
		function onchange1(value){
			var str = "?changename="+value;
			window.location.replace(str);
		}
		
		
	</script>
		</head>
		<body>
			<%
				try {
					String ip = request.getRemoteAddr();
					String recorder = "";
					Object o = session.getAttribute("authorize.username");
					if(o!=null){
						recorder = o.toString();
					}
				    out.print("<input type='button' value='刷新' onclick='window.location.replace(\"sendpresent.jsp\")' />");
					GmMailReplay gmreplay = GmMailReplay.getInstance();
					PlayerManager pmanager = PlayerManager.getInstance();
					String articles[] = {"传功石","静心咒","千金方","心如止水丸","粮饷","绝密文件","假人头","密信","丝巾","全本道德经","铁如意","香囊","忘魂花","红拂女的信","腰牌","搜神记","天刑剑","指环","符文","易经心得","八卦图","行军符","玉钗","李玄妻的信","泥人","李玄的信","受伤士兵的信","神秘信件","蓝采和的信","张果老的信","一半玉佩","师说",
							"玉萧","曹大人手令","曹国舅笏板","天道令牌","天道教拂尘","冷不防的玉璧",
							"阐教法器","金刚镇邪石","灵蛇内丹","白素贞的雨伞",
							"小青的宝剑","小青的玉镯","白鹿仙毫","白鹿鹿茸","泾河龙王鳞片","魏征的信","龙王的手令","玄龟之甲","法海禅杖","锦囊妙计 ","金剪刀"};
					//如果物品有多个颜色要求，以"-"分割。
					//eg:绿-红-完美澄
					String colors[] = {"白","绿","绿","绿","绿","绿","绿","绿","绿","绿","绿","绿","绿","绿","绿","绿","绿","绿","绿","绿","绿","绿"
							+"绿","绿","绿","绿","绿","绿","绿","绿","绿","绿","绿","绿","绿","绿","绿","绿","绿","绿","绿","绿"
							+"绿","绿","绿","绿","绿","绿","绿","绿","绿","绿","绿","绿","绿","绿"};
					
					Map<String,String[]> articleandcolor = new 	HashMap<String,String[]>();
					String al [] = new String [5];
					for(int j=0;j<articles.length;j++){
						if(articles[j]!=null){
							if(colors[j].split("-").length>0){
								al = colors[j].split("-");
							}
							articleandcolor.put(articles[j], al);
						}
					}
					
					
					ArticleManager acmanager = ArticleManager.getInstance();
					ActionManager amanager = ActionManager.getInstance();
					ArticleEntityManager aem = ArticleEntityManager.getInstance();
					String action = request.getParameter("action");
					MailManager mm = MailManager.getInstance();
					String gmname = "";
					Article a = null;
					long pid = pmanager.getPlayer("gm01").getId();
					if(pid!=0){
						gmname = pmanager.getPlayer(pid).getName();
					}
					String changename = request.getParameter("changename");
					if(action!=null&&"add".equals(action.trim())){
					  String playername = request.getParameter("playname");
					  String title = request.getParameter("title");
					  String content = request.getParameter("content"); 
					  String item = request.getParameter("item");
					  String mailid = request.getParameter("mailid");
					  int count = Integer.parseInt(request.getParameter("count").trim());
					  int color = Integer.parseInt(request.getParameter("color").trim());
					  Player p =null;
					  ArticleEntity ae = null;
					  if(playername!=null&&!playername.trim().equals(""))
					    p = pmanager.getPlayer(playername.trim());
					  if(mailid!=null&&!mailid.trim().equals(""))
					    p = pmanager.getPlayer(Long.parseLong(mailid));
					  	
					    if(!request.getParameter("item").equals("")){
					    	if(acmanager.getArticle(request.getParameter("item"))!=null){
					    		a = acmanager.getArticle(request.getParameter("item"));
					    	}else{
					    		out.print("物品不存在！");
					    	}
					    }
					    if(p!=null){
					    	 if(a!=null&&count<=10){
							     out.print(p.getId()+":玩家id|"+item+":物品|"+color+":颜色"+title+":邮件标题|"+content+":邮件内容"+"<br/>");
							 	 try{
							 		 if(a.isOverlap()){
							 			ae = aem.createEntity(a, true, ArticleEntityManager.CREATE_REASON_后台, p, color,count, true);
						 				mm.sendMail(p.getId(), new ArticleEntity[] { ae }, new int []{count}, title, content, 0, 0, 0,"后台发送",MailSendType.后台发送,p.getName(),ip,recorder);
							 		 }else{
							 			 ArrayList<ArticleEntity> alist = new ArrayList<ArticleEntity>();
							 			 if(count<=5){
							 				for(int i=0;i<count;i++){
								 				ae = aem.createEntity(a, true, ArticleEntityManager.CREATE_REASON_后台, p, color,1, true);
								 				if(ae != null){
								 					alist.add(ae);
								 				}
								 			 }
								 			mm.sendMail(p.getId(), alist.toArray(new ArticleEntity[0]), title, content, 0, 0, 0,"后台发送",MailSendType.后台发送,p.getName(),ip,recorder);
							 			 }else{
							 				out.print("发送失败！！！！不堆叠的物品一次最多可发5个！"); 
							 			 }
							 			 
							 		 }
									  amanager.save(gmname,"给玩家:"+p.getName()+"发送["+item+"]数量["+count+"]");
									  out.print("发送成功！！！！"); 
							 	 }catch(Exception e){
							 		 e.printStackTrace();
							 		 out.print("发送失败！！！请检查是否错误信息！");
							 	 }
							     
								 }else{
									 out.print("发送失败！！！！没有该物品，或者物品数量不能超过10");
								 } 
					    }else{
					    	 out.print("发送失败！！！没有该玩家！");
					    }
					   
						}else{
						    out.print("请输入正确的角色名或者角色Id");
						}
					    
					
					
					out.print("<br/><br/><br/><table width='60%' ><tr><th>角色名</th><td class='top'><input type='text' id='playname' name='playname' value=''/>或ID:<input type ='text' id='mailid' name='mailid' value='' /> </td></tr>");
					out.print("<tr><th>标题</th><td><input type='text' id='restitle1' value='补偿' /></td></tr>");
					out.print("<tr><th>礼品：</th><td><select id='item' name='item' onchange='onchange1(this.value)'>");
				     if(changename!=null) {
				   		out.print("<option value='" + changename + "' >"+changename+"</option>");
				   		for(String articlename : articles) {
						     out.print("<option value='" + articlename + "'>" +articlename+ "</option>");
				     	}
				     } else {
				     	for(String articlename : articles) {
						     out.print("<option value='" + articlename + "'>" +articlename+ "</option>");
				     	}
				     }
					 out.print("</select></td></tr>");
					
					out.print("<tr><th>颜色</th><td><select name='color' id='color' ><option>--</option>");
					if(changename!=null){
						for (int i=0;i<articleandcolor.get(changename).length;i++){
							out.print("<option value='" + i + "' >" + articleandcolor.get(changename)[i]  + "</option>");
						}
					}else{
						out.print("<option value='" + 0 + "' >" + colors[0]  + "</option>");
					}
					
					out.print("</select></td></tr>");
					out.print("<tr><th>数量</th><td><input type='count' id='count'  value='1'/></td></tr>");
					out.print("<tr><th>内容</th><td><textarea style='width:50%' rows='5' name ='repcontent' id='repcontent1' value=''></textarea>");
					out.print("</td></tr>");
					out.print("<tr><td colspan=2 ><input type='button' value='发送' onclick='send()' /></td></tr></table>");
				} catch (Exception e) {
					 out.print("<h1>请输入正确的角色名或者角色Id");
					out.println(StringUtil.getStackTrace(e));
				}
			%>
		</body>
	</html>
	
	
	
	
	<servlet>
          <servlet-name>DisplayChart</servlet-name>
          <servlet-class>org.jfree.chart.servlet.DisplayChart</servlet-class>
	</servlet>
	<servlet-mapping>
          <servlet-name>DisplayChart</servlet-name>
          <url-pattern>/DisplayChart</url-pattern>
	</servlet-mapping>