<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.core.*,
com.fy.engineserver.datasource.skill.*,com.fy.engineserver.sprite.monster.*"%>
<%MonsterManager sm = MemoryMonsterManager.getMonsterManager();%>
<%@include file="IPManager.jsp" %><html>
<head>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
</HEAD>
<BODY>
<h2>Game Server，各个精灵的情况</h2><br><a href="./sprites.jsp">刷新此页面</a>
<%
int PAGE_MAX_NO = 60;
String pageindex = request.getParameter("pageindex");
 int m = 0;
 int index = 0;
if(pageindex != null){
	m = Integer.parseInt(pageindex)*PAGE_MAX_NO;
	index = Integer.parseInt(pageindex);
}
int n = sm.getAmountOfMonsters();
PAGE_MAX_NO = n;
if(n > 0){
	//每页60条数据
	int pageCount = 0;
	pageCount = n/PAGE_MAX_NO;
	if(n%PAGE_MAX_NO != 0){
		pageCount+=1;
	}
	out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>当前页为第<font color='red'>"+(index+1)+"</font>页&nbsp;&nbsp;总共<font color='#2D20CA'>"+pageCount+"</font>页&nbsp;&nbsp;每页"+PAGE_MAX_NO+"条数据</b><br><br>");
	out.println("<a href='sprites.jsp?pageindex="+0+"'>首页  </a>");
	if(index > 0){
		out.println("<a href='sprites.jsp?pageindex="+(index-1)+"'><<上一页<<  </a>");	
	}
	for(int i = 0; i < 10; i++){
		if(index+i < pageCount){
		out.println("<a href='sprites.jsp?pageindex="+(index+i)+"'>第"+(index+i+1)+"页  </a>");
		}
	}
	if(index <(pageCount-1)){
		out.println("<a href='sprites.jsp?pageindex="+(index+1)+"'>>>下一页>>  </a>");	
	}
	out.println("<a href='sprites.jsp?pageindex="+(pageCount-1)+"'>末页  </a>");
	%>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
<td>所在地图</td>
<td>ID</td>
<td>名字</td>
<td>坐标</td>
<td>技能</td>
</tr>	
<%

Monster sprites[] = null;
	if((m+PAGE_MAX_NO)>n){
		sprites = sm.getMonstersByPage(m,(n-m));
	}else{
		sprites = sm.getMonstersByPage(m,PAGE_MAX_NO);
	}
	
	
	HashMap<Integer,ArrayList<Monster>> map = new HashMap<Integer,ArrayList<Monster>>();
	for(int i = 0 ; i < sprites.length ; i++){
		int cid = sprites[i].getSpriteCategoryId();
		ArrayList<Monster> al = map.get(cid);
		if(al == null){
			al = new ArrayList<Monster>();
			map.put(cid,al);
		}
		al.add(sprites[i]);
	}
	Integer cids[] = map.keySet().toArray(new Integer[0]);
	String innerStateStr[] = new String[]{"巡逻","战斗","回出生点"};
	
	for(int i = 0 ; i < cids.length ; i++){
		ArrayList<Monster> al = map.get(cids[i]);
		
		for(int j = 0 ; j < al.size() ; j++){
			Monster s = al.get(j);
			out.println("<tr bgcolor='#FFFFFF' align='center'>");
			out.println("<td>"+s.getGameName()+"</td>");
			out.println("<td><a href='sprite.jsp?sid="+s.getId()+"'>"+s.getId()+"</a></td>");
			out.println("<td>"+s.getName()+"</td>");
			out.println("<td>"+s.getX()+","+s.getY()+"</td>");
			ArrayList<ActiveSkill> skills = s.getSkillList();
			if(skills.size() > 0){
				StringBuffer sb = new StringBuffer();
				for(int k = 0 ; k < skills.size() ; k++){
					sb.append("<a href='skillbyid.jsp?id="+skills.get(k).getId() +"&className="+skills.get(k).getClass().getName()+"'>"+skills.get(k).getName() +"<a/>,");
				}
				out.println("<td>"+sb.toString().substring(0,(sb.length()-1))+"</td>");
			}else{
				out.println("<td>--</td>");
			}
		}
	}
%>
</table>
<%out.println("<a href='sprites.jsp?pageindex="+0+"'>首页  </a>");
if(index > 0){
	out.println("<a href='sprites.jsp?pageindex="+(index-1)+"'><<上一页<<  </a>");	
}
for(int i = 0; i < 10; i++){
	if(index+i < pageCount){
	out.println("<a href='sprites.jsp?pageindex="+(index+i)+"'>第"+(index+i+1)+"页  </a>");
	}
}
if(index <(pageCount-1)){
	out.println("<a href='sprites.jsp?pageindex="+(index+1)+"'>>>下一页>>  </a>");	
}
out.println("<a href='sprites.jsp?pageindex="+(pageCount-1)+"'>末页  </a>");}%>	
</BODY></html>
