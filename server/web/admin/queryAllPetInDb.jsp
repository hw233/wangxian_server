<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
	
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="java.util.List"%>

<%! 
	
	public static interface SimplePet{
	
		public long getId();
		public long getOwnerId();
		public String getName();
		public int getLevel();		
	}
%>

<%
	int start = 1;
	int nPage = 50;
	int orderType = 0;
	
	String orderBy = "";
	
	String selectIndex = request.getParameter("selectIndex");
	if(selectIndex == null) selectIndex = "1";
	if(selectIndex != null){
		if(selectIndex.equals("1")){//按照银子排序
			orderBy = "level desc";
		}
		else{
			out.println("请输入的要查找的东西");
		}
	}
	
	
	if(request.getParameter("start") != null){
		start = Integer.parseInt(request.getParameter("start"));
	}
	
	SimpleEntityManager<Pet> em = SimpleEntityManagerFactory.getSimpleEntityManager(Pet.class);
	long totalCount = em.count();
	long ids[] = em.queryIds(Pet.class,"",orderBy,(start-1)*nPage+1,start*nPage+1);
	List<SimplePet> pList = em.queryFields(SimplePet.class,ids);

%>


<%@page import="com.xuanzhi.tools.page.PageUtil"%><html>
<head>
</head>
<body>
<h2>检查所有宠物属性，从数据库中直接查询，速度很慢，请耐心等待。。。</h2>

角色总数：<%=totalCount %><br/>
<%
	out.println(PageUtil.makePageHTML("./queryAllPetInDb.jsp?selectIndex="+selectIndex+"",15,"start",start,nPage,(int)totalCount));
%>

<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
<td>编号</td>
<td>ID</td>
<td>主人id</td>
<td>Name</td>
<td>级别</td>
</tr>

<%
        //int n = sm.getAmountOfPlayers();

        for(int i = 0 ; i < pList.size(); i++){
        	SimplePet p = pList.get(i);
                %>
                <tr bgcolor="#FFFFFF" align="center">
                <td><%=i+1 %></td>
                <td><%= p.getId() %></td>
                <td><%= p.getOwnerId() %></td>
           		 <td><%= p.getName() %></td>
                <td><%= p.getLevel() %></td>
                </tr><%
        }
%>
</table>
<%
	out.println(PageUtil.makePageHTML("./queryAllPetInDb.jsp?selectIndex="+selectIndex+"",15,"start",start,nPage,(int)totalCount));
%>
</BODY></html>
