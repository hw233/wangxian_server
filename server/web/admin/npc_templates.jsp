<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.core.*,java.lang.reflect.*,com.google.gson.*,com.fy.engineserver.sprite.npc.*"%><%! 
	
	String aaa(String s,int len){
		StringBuffer sb = new StringBuffer();
		char chars[] = s.toCharArray();
		int c = 0;
		for(int i = 0 ; i < chars.length ; i++){
			sb.append(chars[i]);
			c++;
			if( c >= len && (chars[i] == ',' || chars[i] == '{' || chars[i] == '}' || chars[i] == ':')){
				sb.append("<br/>");
				c = 0;
			}
		}
		return sb.toString();
	}
%><% 
	
NPCManager npcm = MemoryNPCManager.getNPCManager();
MemoryNPCManager.NPCTempalte sts[] = ((MemoryNPCManager)npcm).getNPCTemaplates();
String categoryId = request.getParameter("categoryId");
String[] 颜色 = new String[]{"白色","绿色","蓝色","紫色","完美紫色","橙色","完美橙色"};
if(categoryId != null && !categoryId.trim().equals("")){
	String mapName = request.getParameter("mapName");
	String x = request.getParameter("x");
	String y = request.getParameter("y");
	String counts = request.getParameter("count");
	if(mapName != null && x != null && y != null){
		try{
			int count = 1;
			if(counts != null){
				count = Integer.parseInt(counts);
			}
			for(int i = 0; i < count; i++){
				NPC newNpc = npcm.createNPC(Integer.parseInt(categoryId));
				newNpc.setX(Integer.parseInt(x));
				newNpc.setY(Integer.parseInt(y));
				if(newNpc instanceof FlopCaijiNpc){
					ArticleManager am = ArticleManager.getInstance();
					Article a = am.getArticle("武幻百龙刃");
					if(a != null){
						
						ArticleEntityManager aem = ArticleEntityManager.getInstance();
						Random random = new Random();
						ArticleEntity ae = aem.createEntity(a,false,0,null,random.nextInt(5),true);
						((FlopCaijiNpc)newNpc).setAe(ae);
						((FlopCaijiNpc)newNpc).setStartTime(System.currentTimeMillis());
						((FlopCaijiNpc)newNpc).setEndTime(System.currentTimeMillis() + 60000);
						((FlopCaijiNpc)newNpc).setAllCanPickAfterTime(30000);
						newNpc.setName(颜色[ae.getColorType()]+a.getName());
						newNpc.setTitle("");
					}else{
						out.println("没有此物品<br/>");
					}
				}
				if(newNpc instanceof SealTaskFlushNPC){
					((SealTaskFlushNPC)newNpc).flushPlayerId = 1001000000000228308l;
					((SealTaskFlushNPC)newNpc).sealLevel = SealManager.getInstance().getSealLevel();
					((SealTaskFlushNPC)newNpc).setCountry((byte)1);
				}
				GameManager gm = GameManager.getInstance();
				Game game = gm.getGameByName(mapName,1);
				game.addSprite(newNpc);
				out.println("在"+mapName+"的("+x+","+y+")位置上产生一个id为"+newNpc.getId()+"的npc"+"血量:"+newNpc.getHp()+"/"+newNpc.getMaxHP()+"avata"+newNpc.getAvata().length+" avataRace"+newNpc.getAvataRace()+" avataSex"+newNpc.getAvataSex());
			}
			out.println("<br/>在"+mapName+"的("+x+","+y+")位置上产生"+count+"个npc");
		}catch(Exception ex){
			out.println("产生npc出现异常<br/>");
		}
	}
}
	
	%>
<%@include file="IPManager.jsp" %>
<%@page import="com.fy.engineserver.sprite.npc.npcaction.NpcExecuteItem"%>
<%@page import="com.fy.engineserver.sprite.npc.npcaction.NpcAction"%>
<%@page import="com.fy.engineserver.sprite.npc.npcaction.SaySomething"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.seal.SealManager"%>
<%@page import="com.fy.engineserver.country.manager.CountryManager"%><html>
<head>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
<link rel="stylesheet" type="text/css" href="../css/table.css" />
<style type="text/css">
.titlecolor{
background-color:#C2CAF5;
}
</style>
</HEAD>
<BODY>
<h2>NPC模板数据</h2><br><a href="./npcs.jsp">查看所有的NPC</a><br><br>
<table>
	<tr class="titlecolor">
	<td>Java类</td>
	<td>类型编号</td>
	<td>NPC名</td>
	<td>称号</td>
	<td>阵营</td>
	<td>等级</td>
	<td>血量</td>
	<td>windowId</td>
	<td>AI</td>
	</tr>
	<%
	//为了整合统一类型的技能设置的List
	List<NPC> tempSkills = new ArrayList<NPC>();
	
	for(MemoryNPCManager.NPCTempalte s : sts){
		NPC npc = s.newNPC();
		npc.setNPCCategoryId(s.NPCCategoryId);
		tempSkills.add(npc);
	}
	List<List<NPC>> lists = new ArrayList<List<NPC>>();
	List<NPC> list = new ArrayList<NPC>();
	List<Integer> indexList = new ArrayList<Integer>();
	while(true){
		for(int i = 0; i < tempSkills.size(); i++){
			NPC sprite = tempSkills.get(i);
			if(list.isEmpty()){
				list.add(sprite);
				indexList.add(i);
				continue;
			}
			if(list.get(0).getClass().getSimpleName().equals(sprite.getClass().getSimpleName())){
				list.add(sprite);
				indexList.add(i);
			}
			
		}
		lists.add(list);
		Integer[] ints = indexList.toArray(new Integer[0]);
		Arrays.sort(ints,new Comparator<Integer>(){
			public int compare(Integer o1, Integer o2){
				return o2.compareTo(o1);
			}
		});
		for(int index : ints){
			tempSkills.remove(index);
		}
		list = new ArrayList<NPC>();
		indexList = new ArrayList<Integer>();
		if(tempSkills.isEmpty()){
			break;
		}
	}
	
	Map<String,String> map = new HashMap<String,String>();
	map.put("NPC","一般NPC");
	map.put("ConvoyNPC","护送NPC");
	map.put("DeliverNpc","车夫NPC");
	map.put("FightableNPC","战斗NPC");
	map.put("GuardNPC","守卫NPC");
	map.put("ShopNpc","商店Npc");
	map.put("CaijiNpc","采集Npc");

	if(!lists.isEmpty()){
		int listsSize = lists.size();
		for (int i = 0; i < listsSize; i++) {
			List<NPC> l = lists.get(i);
			if(!l.isEmpty()){
				String CountryStr = "";
				if(l.get(0).getCountry() == 0){
					CountryStr = "中立";
				}else if(l.get(0).getCountry() == 1){
					CountryStr = "紫薇宫";
				}else if(l.get(0).getCountry() == 2){
					CountryStr = "日月盟";
				}else{
					CountryStr = "阵营错误("+l.get(0).getCountry()+")";
				}
				String ais = "没有设置AI";
				if(l.get(0).getItems() != null && l.get(0).getItems().length > 0){
					ais = "<table><tr><td>开启后多长时间执行</td><td>距离npc最小距离</td><td>距离npc最大距离</td><td>血量小于多少百分比</td><td>最多执行几次</td><td>两次执行之间的最小间隔</td><td>是否无视BOSS移动</td><td>执行的动作</td></tr>";
					for(NpcExecuteItem nei : l.get(0).getItems()){
						if(nei != null){
							ais = ais + "<tr><td>" + nei.getExeTimeLimit()+"</td><td>"+nei.getMinDistance()+"</td><td>"+nei.getMaxDistance()+"</td><td>"+nei.getHpPercent()+"</td><td>"+nei.getMaxExeTimes()+"</td><td>"+nei.getExeTimeGap()+"</td><td>"+nei.isIgnoreMoving()+"</td><td>"+nei.getActionId()+"</td></tr>";
						}
					}
					ais = ais + "</table>";
				}
				%>
				<tr>
					<td rowspan="<%=l.size() %>"><%= map.get(l.get(0).getClass().getSimpleName()) == null ? l.get(0).getClass().getSimpleName() : map.get(l.get(0).getClass().getSimpleName()) %></td>
					<td><a href="npctemplatebyid.jsp?spriteCategoryId=<%=l.get(0).getNPCCategoryId() %>"><%= l.get(0).getNPCCategoryId() %></a></td>
					<td><%= l.get(0).getName() %></td>
					<td><%= l.get(0).getTitle() %></td>
					<td><%= CountryStr %></td>
					<td><%= l.get(0).getLevel()%></td>
					<td><%= l.get(0).getMaxHP() %></td>
					<td><%= l.get(0).getWindowId() %></td>
					<td><%= ais %></td>
				</tr>
				<%
				for(int j = 1; j < l.size(); j++){
					NPC sprite = null;
					sprite = l.get(j);
					CountryStr = "";
					if(sprite.getCountry() == 0){
						CountryStr = "中立";
					}else if(sprite.getCountry() == 1){
						CountryStr = "紫薇宫";
					}else if(sprite.getCountry() == 2){
						CountryStr = "日月盟";
					}else{
						CountryStr = "阵营错误("+sprite.getCountry()+")";
					}
					String aiss = "没有设置AI";
					if(sprite.getItems() != null && sprite.getItems().length > 0){
						aiss = "<table><tr><td>开启后多长时间执行</td><td>距离npc最小距离</td><td>距离npc最大距离</td><td>血量小于多少百分比</td><td>最多执行几次</td><td>两次执行之间的最小间隔</td><td>是否无视BOSS移动</td><td>执行的动作</td></tr>";
						for(NpcExecuteItem nei : sprite.getItems()){
							if(nei != null){
								aiss = aiss + "<tr><td>" + nei.getExeTimeLimit()+"</td><td>"+nei.getMinDistance()+"</td><td>"+nei.getMaxDistance()+"</td><td>"+nei.getHpPercent()+"</td><td>"+nei.getMaxExeTimes()+"</td><td>"+nei.getExeTimeGap()+"</td><td>"+nei.isIgnoreMoving()+"</td><td>"+nei.getActionId()+"</td></tr>";
							}
						}
						aiss = aiss + "</table>";
					}
				%>
					<tr>
						<td><a href="npctemplatebyid.jsp?spriteCategoryId=<%= sprite.getNPCCategoryId() %>"><%= sprite.getNPCCategoryId() %></a></td>
						<td><%= sprite.getName() %></td>
						<td><%= sprite.getTitle() %></td>
						<td><%= CountryStr%></td>
						<td><%= sprite.getLevel()%></td>
						<td><%= sprite.getMaxHP() %></td>
						<td><%= sprite.getWindowId() %></td>
						<td><%= aiss %></td>
					</tr>
				<%
				}
			}
		}
	}
%>
</table>
<br/>
<form name= "f1" method="post">
npc编号:<input name="categoryId">&nbsp;产生地图:<input name="mapName">&nbsp;产生x坐标:<input name="x">&nbsp;产生y坐标:<input name="y">&nbsp;<input name="count" value="1"><input type="submit" value="在该位置产生npc">
</form>
<br/>
<table>
<tr><td>类</td><td>id</td><td>描述</td></tr>
<%if(((MemoryNPCManager)npcm).npcActionMap != null){
	for(NpcAction na : ((MemoryNPCManager)npcm).npcActionMap.values()){
		if(na instanceof SaySomething){
			%><tr><td><%=na.getClass().getSimpleName() %></td><td><%=na.getActionId() %></td><td><%=na.getDescription()+"   喊话内容:"+((SaySomething)na).getContent() %></td></tr><%	
		}else{
			%><tr><td><%=na.getClass().getSimpleName() %></td><td><%=na.getActionId() %></td><td><%=na.getDescription()%></td></tr><%
		}
		
	}
} %>

</table>
</BODY></html>
