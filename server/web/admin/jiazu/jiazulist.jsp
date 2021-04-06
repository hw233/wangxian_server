<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.*"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.jiazu.service.*"%>
<%@page import="com.fy.engineserver.jiazu.dao.*"%>
<%@page import="com.fy.engineserver.septstation.service.*"%>
<%@page import="com.fy.engineserver.septstation.*"%>
<%@page
	import="com.fy.engineserver.septbuilding.templet.SeptBuildingTemplet.BuildingType"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="../task/css/main.css">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript">
 function savejiazu(i)
 {
	 var processflag=document.getElementById('processflag'+i)
	 processflag.value=2;
	 var formi=document.getElementById('form'+i);
	 formi.submit();
 }
 function deletejiazu(i)
 {
	 var processflag=document.getElementById('processflag'+i)
	 processflag.value=1;
	 var formi=document.getElementById('form'+i);
	 formi.submit();
 }
 function searchname()
 {
	 var searchform=document.getElementById('searchform');
	 searchform.submit();
 }
</script>
</head>
<%
	String keyword = null;
	int pageNum = 1;
	int pageSize = 10;
	int jiazucount=0;
	long count=0;
	JiazuManager jiazuManager = JiazuManager.getInstance();
	if(jiazuManager==null)
	{
		jiazuManager=new JiazuManager();
		jiazuManager.init();
		if(jiazuManager.getDao()==null)
		System.out.println("dao is null");
	}
	
	String processflag=null;
	processflag=request.getParameter("processflag");
	//删除jiazu
	if(processflag!=null&&processflag.equals("1"))
	{
		String jiazuid=request.getParameter("jiazuid");
		if(jiazuid!=null&&jiazuid.trim().length()!=0&&!jiazuid.equals("null"))
		{
			Jiazu jiazu=jiazuManager.getJiazu(Long.parseLong(jiazuid));
			jiazuManager.removeJiazu(jiazu);
		}
	}
	else if(processflag!=null&&processflag.equals("2"))
	{
		String jiazuid=request.getParameter("jiazuid");
		if(jiazuid!=null&&jiazuid.trim().length()!=0&&!jiazuid.equals("null"))
		{
			Jiazu jiazu=jiazuManager.getJiazu(Long.parseLong(jiazuid));
		
			String iconid=request.getParameter("iconID");
			if(iconid!=null&&!iconid.equals("null")&&iconid.trim().length()>0)
			{
			 	jiazu.setIconID(Long.parseLong(iconid));
			}
			String baseID=request.getParameter("baseID");
			if(baseID!=null&&!baseID.equals("null")&&baseID.trim().length()>0)
			{
			 	jiazu.setBaseID(Long.parseLong(baseID));
			}
			String level=request.getParameter("level");
			if(level!=null&&!level.equals("null")&&level.trim().length()>0)
			{
			 	jiazu.setLevel(Integer.parseInt(level));
			}
			String slogan=request.getParameter("slogan");
			if(slogan!=null&&!slogan.equals("null")&&slogan.trim().length()>0)
			{
			 	jiazu.setSlogan(slogan);
			}
			String jiazuPassword=request.getParameter("jiazuPassword");
			if(jiazuPassword!=null&&!jiazuPassword.equals("null")&&jiazuPassword.trim().length()>0)
			{
			 	jiazu.setJiazuPassword(jiazuPassword);
			}
			String jiazuMoney=request.getParameter("jiazuMoney");
			if(jiazuMoney!=null&&!jiazuMoney.equals("null")&&jiazuMoney.trim().length()>0)
			{
			 	jiazu.addJiazuMoney(Long.parseLong(jiazuMoney)-jiazu.getJiazuMoney());
			}
			String landNum=request.getParameter("landNum");
			if(landNum!=null&&!landNum.equals("null")&&landNum.trim().length()>0)
			{
			 	jiazu.setLandNum(Integer.parseInt(landNum));
			}
			String constructionConsume=request.getParameter("constructionConsume");
			if(constructionConsume!=null&&!constructionConsume.equals("null")&&constructionConsume.trim().length()>0)
			{
			 	jiazu.setContributionSum(Integer.parseInt(constructionConsume));
			}
			
			String prosperity=request.getParameter("prosperity");
			if(prosperity!=null&&!prosperity.equals("null")&&prosperity.trim().length()>0)
			{
			 	jiazu.setProsperity(Integer.parseInt(prosperity));
			}
			
			String salarySum=request.getParameter("salarySum");
			if(salarySum!=null&&!salarySum.equals("null")&&salarySum.trim().length()>0)
			{
			 	jiazu.setSalarySum(Integer.parseInt(salarySum));
			}
			
			String salaryLeft=request.getParameter("salaryLeft");
			if(salaryLeft!=null&&!salaryLeft.equals("null")&&salaryLeft.trim().length()>0)
			{
			 	jiazu.setSalaryLeft(Integer.parseInt(salaryLeft));
			}
			String singleMemberSalaryMax=request.getParameter("singleMemberSalaryMax");
			if(singleMemberSalaryMax!=null&&!singleMemberSalaryMax.equals("null")&&singleMemberSalaryMax.trim().length()>0)
			{
			 	jiazu.setSingleMemberSalaryMax(Integer.parseInt(singleMemberSalaryMax));
			}
			String contributionSum=request.getParameter("contributionSum");
			if(contributionSum!=null&&!contributionSum.equals("null")&&contributionSum.trim().length()>0)
			{
			 	jiazu.setContributionSum(Integer.parseInt(contributionSum));
			}
			
			jiazuManager.updateJiazu(jiazu);
		}
		
	}
	
	keyword = request.getParameter("keyword");
	String a = request.getParameter("pageNum");
	String b = request.getParameter("pageSize");
	if (a != null&&a.length()>0) {
		pageNum = Integer.parseInt(a);
	}
	if (b != null&&b.length()>0) {
		pageSize = Integer.parseInt(b);
	}
	java.text.SimpleDateFormat format=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Map<Long, Jiazu> jiazuMap =JiazuManager.getInstance().getJiazuLruCacheByID();
	/**
	if(pageNum>0&&pageSize>0)
	{
		jiazuList=jiazuManager.getJiazuByOrder(keyword,pageNum-1,pageSize,1,true);
	}
	if(keyword!=null&&keyword.trim().length()>0)
	{
		count=jiazuManager.getJiazuCount(keyword);
	}
	else
		count=jiazuManager.getJiazuCount();
	*/
%>
<body>
	<h1 align="center">家族列表</h1>
	<form method="get" id="searchform">
		<p>
			按名称搜索家族: <input type="text" name="keyword"></input>&nbsp;&nbsp;单页显示个数:<input
				type="text" name="pageSize"></input>&nbsp;&nbsp;页数:<input
				type="text" name="pageNum"></input>&nbsp;&nbsp;
			<button type="button" onclick="searchname()">查询</button>
			<% if(jiazuList==null){ out.println("无数据"); return;}else{ out.println(""+count+"当前页数个数"+jiazuList.size());} %>
		</p>
	</form>
	<table border="1" align="center" width="80%">
		<tr>
			<td width="13%">家族名称</td>
			<td width="13%">家族ID</td>
			<td width="60%">家族信息</td>
			<td width="13%">操作</td>
		</tr>

		<%
			int i=0;
for(Iterator itor = jiazuMap.keySet().iterator();itor.hasNext();){
	Jiazu jiazu = jiazuMap.get(itor.next());
%>

		<tr>
			<td><%=jiazu.getName() %></td>
			<td><%=jiazu.getJiazuID() %></td>
			<td>
				<form method='post' id='form<%=i %>'>
					<input type='hidden' name='jiazuid'
						value='<%=jiazu.getJiazuID() %>'></input> <input type='hidden'
						name='processflag' id='processflag<%=i %>' value='0'></input>
					<table align="center">
						<tr>
							<td>家族名称:</td>
							<td><input type='text' name='jiazuname'
								value="<%=jiazu.getName()%>" readonly></input>
							</td>
						</tr>
						<tr>
							<td>创建时间:</td>
							<td><input type='text' name='createTime'
								value="<%=format.format(new java.util.Date(jiazu.getCreateTime()))%>"
								readonly></input>
							</td>
						</tr>
						<tr>
							<td>家族徽章ID:</td>
							<td><input type='text' name='iconID'
								value="<%=jiazu.getIconID()%>"></input>
							</td>
						</tr>
						<tr>
							<td>家族驻地ID:</td>
							<td><input type='text' name='baseID'
								value="<%=jiazu.getBaseID() %>"></input>
							</td>
						</tr>
						<tr>
							<td>级别:</td>
							<td><input type='text' name='level'
								value="<%=jiazu.getLevel() %>"></input>
							</td>
						</tr>
						<tr>
							<td>家族标语:</td>
							<td><input type='text' name='slogan'
								value="<%=jiazu.getSlogan() %>"></input>
							</td>
						</tr>
						<tr>
							<td>家族密码:</td>
							<td><input type='text' name='jiazuPassword'
								value="<%=jiazu.getJiazuPassword() %>"></input>
							</td>
						</tr>
						<tr>
							<td>家族灵石:</td>
							<td><input type='text' name='jiazuMoney'
								value="<%=jiazu.getJiazuMoney() %>"></input>
							</td>
						</tr>
						<tr>
							<td>家族领地个数:</td>
							<td><input type='text' name='landNum'
								value="<%=jiazu.getLandNum() %>"></input>
							</td>
						</tr>
						<tr>
							<td>家族灵脉值:</td>
							<td><input type='text' name='constructionConsume'
								value="<%=jiazu.getConstructionConsume() %>"></input>
							</td>
						</tr>
						<tr>
							<td>家族驻地的繁荣度:</td>
							<td><input type='text' name='prosperity'
								value="<%=jiazu.getProsperity() %>"></input>
							</td>
						</tr>
						<tr>
							<td>本周的工资的总和:</td>
							<td><input type='text' name='salarySum'
								value="<%=jiazu.getSalarySum() %>"></input>
							</td>
						</tr>
						<tr>
							<td>本周的工资的剩余:</td>
							<td><input type='text' name='salaryLeft'
								value="<%=jiazu.getSalaryLeft() %>"></input>
							</td>
						</tr>
						<tr>
							<td>本周个人工资上限:</td>
							<td><input type='text' name='singleMemberSalaryMax'
								value="<%=jiazu.getSingleMemberSalaryMax() %>"></input>
							</td>
						</tr>
						<tr>
							<td>贡献度之和:</td>
							<td><input type='text' name='contributionSum'
								value="<%=jiazu.getContributionSum() %>"></input>
							</td>
						</tr>
						<tr>
							<td>成员memberID:</td>
							<td>
								<%   Set<JiazuMember> memlist=jiazuManager.getJiazuMember(jiazu.getJiazuID());
		        StringBuffer buf=new StringBuffer("<table border=\"1\"><tr><td>玩家ID</td><td>成员ID</td><td>成员名称</td><td>成员title</td></tr>");
		        PlayerManager playerManager=PlayerManager.getInstance();
		             for(JiazuMember mem:memlist)
		             {
		            	 Player p = null;
		            	 try{
		            	 p=playerManager.getPlayer(mem.getPlayerID());
		            	 } catch (Exception e){
		            		 continue;
		            	 }
		        	buf.append("<tr><td>").append(mem.getPlayerID()).append("</td><td>").append(mem.getJiazuMemID()).append("</td><td>")
		        	.append(p.getName()).append("</td><td>").append(jiazu.getTitleName(mem.getTitle())).append("</td></tr>"); 
		         }
		         buf.append("</table>");
		         out.print(buf.toString());
		         %>
							</td>
						</tr>
						<tr>
							<td>申请者<%=jiazu.getRequestMap().size() %></td>
							<td>
								<%
								StringBuffer sbf = new StringBuffer();
									for (Long id :jiazu.getRequestMap().values()) {
										sbf.append(id).append(",");
									}
									out.print(sbf.toString());
								%>
							</td>
						</tr>
					</table>
				</form></td>
			<td><table>
					<tr>
						<td><button type="button" onclick="savejiazu(<%=i%>)">保存</button>
						</td>
					</tr>
					<tr>
						<td><button onclick="deletejiazu(<%=i%>)">删除</button>
						</td>
					</tr>
					<tr>
						<%
						SeptStation station = SeptStationManager.getInstance().getSeptStationById(Long.valueOf(jiazu.getBaseID()));
						if(station!=null){
						int level = station.getBuildingLevel(BuildingType.龙图阁);
						System.out.println("id"+jiazu.getBaseID()+"\t"+(station==null)+"level="+level);
						}
						if(jiazu.getBaseID()==0){ 
						//	 SeptStation station = SeptStationManager.getInstance().getSeptStationById(Long.valueOf(jiazu.getBaseID()));
							 
						%>
						<td><button
								onclick="location='/SeptBuilding/addSeptStation.jsp?septId=<%=jiazu.getJiazuID() %>&mapName=测试地图&name=<%=(jiazu.getName()+"的驻地") %>&npcId=&country='">创建驻地</button>
						</td>
						<%}
					else {
					
					   %>
						<td><button
								onclick="location='/SeptBuilding/septStation.jsp?id=<%=jiazu.getBaseID() %>'">查看驻地</button>
						</td>
						<%} %>
					</tr>
				</table>
			</td>
		</tr>

		<%
			i++;
		}
%>
	</table>
	<%  
		int pages=(int)(count/pageSize); 
		   if(count%pageSize>0)
		   pages++;
		   if(pages>0)
		   {
	%>
	<p align="center">
		分页:
		<% 
		   for(i=1;i<=pages;i++)
		   {
			if(pageNum==i){out.print("<B>"+i+"</B>");}
				else
					{%>
		<a
			href="<%="?keyword="+(keyword!=null?keyword:"")+"&pageNum="+(i)+"&pageSize="+pageSize %>">
			<% out.print(i); }%> </a>&nbsp;&nbsp;
		<%} %>
	</p>
	<%} %>

	<script type="text/javascript">

	
</script>


</body>
</html>