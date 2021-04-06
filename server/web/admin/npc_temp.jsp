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
	
	%>
<%@include file="IPManager.jsp" %><html>
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
	<td>所在地图</td>
	<td>类型编号</td>
	<td>NPC名</td>
	<td>称号</td>
	<td>阵营</td>
	<td>等级</td>
	<td>血量</td>
	<td>近战攻击强度</td>
	<td>法术攻击强度</td>
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
		GameManager gm = GameManager.getInstance();
		List<String> mapNamesList = new ArrayList<String>();
		if(gm != null){
			GameInfo[] gis = gm.getGameInfos();
			if(gis != null){
				for(GameInfo gi : gis){
					if(gi != null){
						if(gi.getMapName() != null){
							mapNamesList.add(gi.getMapName());
						}
					}
				}
			}
		}
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
					CountryStr = "阵营错误";
				}
				for(int j = 0; j < l.size(); j++){
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
						CountryStr = "阵营错误";
					}
			
				%>
					<tr>
					<%
					StringBuffer sb = new StringBuffer();
					if(mapNamesList != null){
						for(String str : mapNamesList){
							if(str != null){
								Game gmTemp = gm.getGameByName(str,0);
								if(gmTemp == null){
									gmTemp = gm.getGameByName(str,1);
								}
								if(gmTemp != null){
									LivingObject[] los = gmTemp.getLivingObjects();
									if(los != null){
										for(LivingObject lo : los){
											if(lo instanceof NPC && ((NPC)lo).getName().equals(sprite.getName())){
												sb.append(str+" ");
												break;
											}
										}
									}
								}
							}
						}
					}
					%>
					<td><%=sb.toString() %></td>
						<td><a href="npctemplatebyid.jsp?spriteCategoryId=<%= sprite.getNPCCategoryId() %>"><%= sprite.getNPCCategoryId() %></a></td>
						<td><%= sprite.getName() %></td>
						<td><%= sprite.getTitle() %></td>
						<td><%= CountryStr%></td>
						<td><%= sprite.getLevel()%></td>
						<td><%= sprite.getMaxHP() %></td>
						<td><%= sprite.getPhyAttack() %></td>
						<td><%= sprite.getMagicAttack()%></td>
					</tr>
				<%
				}
			}
		}
	}
%>
</table>	
</BODY></html>
