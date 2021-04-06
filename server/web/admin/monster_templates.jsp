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
	
MonsterManager npcm = MemoryMonsterManager.getMonsterManager();
MemoryMonsterManager.MonsterTempalte sts[] = ((MemoryMonsterManager)npcm).getMonsterTemaplates();
String categoryId = request.getParameter("categoryId");
if(categoryId != null && !categoryId.trim().equals("")){
	String mapName = request.getParameter("mapName");
	String x = request.getParameter("x");
	String y = request.getParameter("y");
	String country = request.getParameter("country");
	String count = request.getParameter("count");
	if(mapName != null && x != null && y != null){
		try{
			Random random = new Random();
			for(int i = 0; i < Integer.parseInt(count); i++){
				Monster newNpc = npcm.createMonster(Integer.parseInt(categoryId));
				newNpc.setX(Integer.parseInt(x+random.nextInt(100)));
				newNpc.setY(Integer.parseInt(y+random.nextInt(100)));
				
				newNpc.setBornPoint(new Point(newNpc.getX(),newNpc.getY()));
				GameManager gm = GameManager.getInstance();
				for(int j = 1; j <= 3; j++){
					Game game = gm.getGameByName(mapName.trim(),j);
					if(game != null){
						game.addSprite(newNpc);
						out.println("在"+mapName+"的("+x+","+y+")位置上产生一个id为"+newNpc.getId()+"的monster"+"血量:"+newNpc.getHp()+"/"+newNpc.getMaxHP()+"avata"+newNpc.getAvata().length+" avataRace"+newNpc.getAvataRace()+" avataSex"+newNpc.getAvataSex());
					}else{
						out.println("game wei kong "+ mapName+Integer.parseInt(country));
					}
				}

			}
		}catch(Exception ex){
			out.println("产生monster出现异常<br/>");
			ex.printStackTrace();
		}
	}
}
	
	%>
<%@include file="IPManager.jsp" %>
<%@page import="com.fy.engineserver.sprite.npc.npcaction.NpcExecuteItem"%>
<%@page import="com.fy.engineserver.sprite.npc.npcaction.NpcAction"%>
<%@page import="com.fy.engineserver.sprite.npc.npcaction.SaySomething"%>
<%@page import="com.fy.engineserver.sprite.monster.MonsterManager"%>
<%@page import="com.fy.engineserver.sprite.monster.MemoryMonsterManager"%>
<%@page import="com.fy.engineserver.sprite.monster.Monster"%>
<%@page import="com.fy.engineserver.core.g2d.Point"%>
<%@page import="com.fy.engineserver.sprite.monster.BossMonster"%>
<%@page import="com.fy.engineserver.sprite.monster.BossExecuteItem"%>
<%@page import="com.fy.engineserver.sprite.monster.BossAction"%>
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
<h2>Monster模板数据</h2><br><a href="./sprites.jsp">查看所有的Monster</a><br><br>
<table>
	<tr class="titlecolor">
	<td>Java类</td>
	<td>类型编号</td>
	<td>Monster名</td>
	<td>称号</td>
	<td>阵营</td>
	<td>等级</td>
	<td>血量</td>
	<td>windowId</td>
	<td>AI</td>
	</tr>
	<%
	//为了整合统一类型的技能设置的List
	List<Monster> tempSkills = new ArrayList<Monster>();
	
	for(MemoryMonsterManager.MonsterTempalte s : sts){
		Monster npc = s.newMonster();
		npc.setSpriteCategoryId(s.MonsterCategoryId);
		tempSkills.add(npc);
	}
	List<List<Monster>> lists = new ArrayList<List<Monster>>();
	List<Monster> list = new ArrayList<Monster>();
	List<Integer> indexList = new ArrayList<Integer>();
	while(true){
		for(int i = 0; i < tempSkills.size(); i++){
			Monster sprite = tempSkills.get(i);
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
		list = new ArrayList<Monster>();
		indexList = new ArrayList<Integer>();
		if(tempSkills.isEmpty()){
			break;
		}
	}
	
	Map<String,String> map = new HashMap<String,String>();
	map.put("Monster","一般Monster");
	map.put("ConvoyMonster","护送Monster");
	map.put("DeliverNpc","车夫Monster");
	map.put("FightableMonster","战斗Monster");
	map.put("GuardMonster","守卫Monster");
	map.put("ShopNpc","商店Npc");
	map.put("CaijiNpc","采集Npc");
	
	
	
	if(!lists.isEmpty()){
		int listsSize = lists.size();
		for (int i = 0; i < listsSize; i++) {
			List<Monster> l = lists.get(i);
			if(!l.isEmpty()){
				String CountryStr = CountryManager.得到国家名(l.get(0).getCountry());;
				String ais = "没有设置AI";
				Monster monster = l.get(0);
				if(monster instanceof BossMonster){
					if(((BossMonster)monster).getItems() != null && ((BossMonster)monster).getItems().length > 0){
						ais = "<table><tr><td>开启后多长时间执行</td><td>距离monster最小距离</td><td>距离monster最大距离</td><td>血量小于多少百分比</td><td>最多执行几次</td><td>两次执行之间的最小间隔</td><td>是否无视BOSS移动</td><td>执行的动作</td></tr>";
						for(BossExecuteItem nei : ((BossMonster)monster).getItems()){
							if(nei != null){
								ais = ais + "<tr><td>" + nei.getExeTimeLimit()+"</td><td>"+nei.getMinDistance()+"</td><td>"+nei.getMaxDistance()+"</td><td>"+nei.getHpPercent()+"</td><td>"+nei.getMaxExeTimes()+"</td><td>"+nei.getExeTimeGap()+"</td><td>"+nei.isIgnoreMoving()+"</td><td>"+nei.getActionId()+"</td></tr>";
							}
						}
						ais = ais + "</table>";
					}
				}
				
				%>
				<tr>
					<td rowspan="<%=l.size() %>"><%= map.get(l.get(0).getClass().getSimpleName()) == null ? l.get(0).getClass().getSimpleName() : map.get(l.get(0).getClass().getSimpleName()) %></td>
					<td><a href="spritetemplatebyid.jsp?spriteCategoryId=<%=l.get(0).getSpriteCategoryId() %>"><%= l.get(0).getSpriteCategoryId() %></a></td>
					<td><%= l.get(0).getName() %></td>
					<td><%= l.get(0).getTitle() %></td>
					<td><%= CountryStr %></td>
					<td><%= l.get(0).getLevel()%></td>
					<td><%= l.get(0).getMaxHP() %></td>
					<td></td>
					<td><%= ais %></td>
				</tr>
				<%
				for(int j = 1; j < l.size(); j++){
					Monster sprite = null;
					sprite = l.get(j);
					CountryStr = CountryManager.得到国家名(l.get(0).getCountry());
					String aiss = "没有设置AI";
					monster = l.get(j);
					if(monster instanceof BossMonster){
						if(((BossMonster)monster).getItems() != null && ((BossMonster)monster).getItems().length > 0){
							aiss = "<table><tr><td>开启后多长时间执行</td><td>距离monster最小距离</td><td>距离monster最大距离</td><td>血量小于多少百分比</td><td>最多执行几次</td><td>两次执行之间的最小间隔</td><td>是否无视BOSS移动</td><td>执行的动作</td></tr>";
							for(BossExecuteItem nei : ((BossMonster)monster).getItems()){
								if(nei != null){
									aiss = aiss + "<tr><td>" + nei.getExeTimeLimit()+"</td><td>"+nei.getMinDistance()+"</td><td>"+nei.getMaxDistance()+"</td><td>"+nei.getHpPercent()+"</td><td>"+nei.getMaxExeTimes()+"</td><td>"+nei.getExeTimeGap()+"</td><td>"+nei.isIgnoreMoving()+"</td><td>"+nei.getActionId()+"</td></tr>";
								}
							}
							aiss = aiss + "</table>";
						}
					}
				%>
					<tr>
						<td><a href="spritetemplatebyid.jsp?spriteCategoryId=<%= sprite.getSpriteCategoryId() %>"><%= sprite.getSpriteCategoryId() %></a></td>
						<td><%= sprite.getName() %></td>
						<td><%= sprite.getTitle() %></td>
						<td><%= CountryStr%></td>
						<td><%= sprite.getLevel()%></td>
						<td><%= sprite.getMaxHP() %></td>
						<td></td>
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
monster编号:<input name="categoryId">&nbsp;产生地图:<input name="mapName">&nbsp;产生x坐标:<input name="x">&nbsp;产生y坐标:<input name="y">&nbsp;产生国家id:<input name="country">&nbsp;刷新只数:<input name="count">&nbsp;<input type="submit" value="在该位置产生monster">
</form>
<br/>
<table>
<tr><td>类</td><td>id</td><td>描述</td></tr>
<%if(((MemoryMonsterManager)npcm).bossActionMap != null){
	for(BossAction na : ((MemoryMonsterManager)npcm).bossActionMap.values()){
		%><tr><td><%=na.getClass().getSimpleName() %></td><td><%=na.getActionId() %></td><td><%=na.getDescription()%></td></tr><%
	}
} %>

</table>
</BODY></html>
