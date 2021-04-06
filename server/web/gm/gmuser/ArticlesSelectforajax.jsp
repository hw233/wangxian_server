<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.fy.engineserver.datasource.article.manager.*,com.google.gson.*,java.util.*,com.fy.engineserver.sprite.*,
com.fy.engineserver.sprite.concrete.*" %>
<%@page import ="com.fy.engineserver.datasource.article.data.props.*" %>
<%@page import="com.fy.engineserver.datasource.article.data.articles.*" %>
<%@page import="com.fy.engineserver.datasource.article.data.entity.*" %>
<%@page import="java.util.regex.Pattern"%>
<%@page import="java.util.regex.Matcher"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>物品 </title>
<%
int playerId = -1;
String errorMessage = null;
PlayerManager pm = PlayerManager.getInstance();
Player player = null;
ArticleManager am = ArticleManager.getInstance();
ArticleEntityManager aem = ArticleEntityManager.getInstance();
PropsCategory pcs[] = am.getAllPropsCategory();
// HashMap<String, Article> hm = am.getArticle(name);

	String cellIndex = request.getParameter("articleid");
	try{
		playerId = Integer.parseInt(request.getParameter("playerid"));
		if(cellIndex == null || cellIndex.trim().length() == 0){
			errorMessage = "要使用的物品不能为空";
		}
	}catch(Exception e){
		errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
	}
	if(errorMessage == null){
		player = pm.getPlayer(playerId);
		if(player == null){
			errorMessage = "ID为"+playerId+"的玩家不存在！";
		}
	}
	if(errorMessage == null){
// 		if(hm == null){
// 			errorMessage = "没有物品";
// 		}
		//Knapsack knapsack = player.getKnapsack();
		ArticleEntity ae = aem.getEntity(new Long(cellIndex).longValue());
		if(ae instanceof EquipmentEntity){
			String s = AritcleInfoHelper.generate(player,(EquipmentEntity)ae);
			s = s.replaceAll("\\[/color\\]","</font>");
			Pattern p = Pattern.compile("\\[color=(.*)\\]",Pattern.CASE_INSENSITIVE
			);
		    Matcher m = p.matcher(s);
		    while(m.find()){
			    String s1 = m.group();
			      String s2 = Integer.toHexString(Integer.parseInt(s1.split("=")[1].substring(0,s1.split("=")[1].length()-1)));
				  s = s.replaceAll("\\"+s1.substring(0,s1.length()-1)+"\\]","<font color='#"+s2+"'>");
			}
			s = s.replaceAll("\\[icon\\](.*)\\[/icon\\]","<img src='/icons/$1'>");
			out.println("<pre>"+s+"</pre><br>");
		}else if(ae instanceof PropsEntity){
			String s =AritcleInfoHelper.generate(player,(PropsEntity)ae);
			s = s.replaceAll("\\[/color\\]","</font>");
		    Pattern p = Pattern.compile("\\[color=(.*)\\]");
		    Matcher m = p.matcher(s);
		     while(m.find()){
			    String s1 = m.group();
			      String s2 = Integer.toHexString(Integer.parseInt(s1.split("=")[1].substring(0,s1.split("=")[1].length()-1)));
				  s = s.replaceAll("\\"+s1.substring(0,s1.length()-1)+"\\]","<font color='#"+s2+"'>");
			}
			s = s.replaceAll("\\[icon\\](.*)\\[/icon\\]","<img src='/icons/$1'>");
			out.println("<pre>"+s+"</pre><br>");
		
		}else if(ae instanceof ArticleEntity){
			String s = AritcleInfoHelper.generate(player,(ArticleEntity)ae);
			s = s.replaceAll("\\[/color\\]","</font>");
			Pattern p = Pattern.compile("\\[color=(.*)\\]");
		    Matcher m = p.matcher(s);
		     while(m.find()){
			    String s1 = m.group();
			      String s2 = Integer.toHexString(Integer.parseInt(s1.split("=")[1].substring(0,s1.split("=")[1].length()-1)));
				  s = s.replaceAll("\\"+s1.substring(0,s1.length()-1)+"\\]","<font color='#"+s2+"'>");
			}
			s = s.replaceAll("\\[icon\\](.*)\\[/icon\\]","<img src='/icons/$1'>");
			out.println("<pre>"+s+"</pre><br>");
		
		}

	}

%>
</head>
<body>
</body>
</html>
