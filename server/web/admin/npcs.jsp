<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.core.*,
com.fy.engineserver.datasource.skill.*,com.fy.engineserver.sprite.npc.*"%>
<% 
NPCManager npcm = MemoryNPCManager.getNPCManager();
%>
<%@include file="IPManager.jsp" %><html>
<head>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
<link rel="stylesheet" type="text/css" href="../css/table.css" />
<style type="text/css">
.titlecolor{
background-color:#C2CAF5;
}
td {
white-space:nowrap;
}
</style>
<script type="text/javascript">
function selectionNameForMap(obj){
	if(obj.value != ""){
		window.location.href="npcs.jsp?gameName="+obj.value;
	}
}

</script>
</HEAD>
<BODY>
<%String gameName = request.getParameter("gameName");
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
%>
<select id="selectName" name="selectName" onchange="javascript:selectionNameForMap(this)">
<option value="">
<%for(int i = 0; i < mapNamesList.size(); i++){ %>
<option value="<%=mapNamesList.get(i) %>"><%=mapNamesList.get(i) %>
<%} %>
</select>
<%
if(gameName != null){
	out.println("<h2>"+gameName+"中所有NPC的情况</h2><br><a href='npcs.jsp?gameName="+gameName+"'>刷新此页面</a>");
}else{
%>
<h2>Game Server，各个NPC的情况</h2><br><a href="npcs.jsp">刷新此页面</a>
<%
}
//每页60条数据
int PAGE_MAX_NO = 60;
String pageindex = request.getParameter("pageindex");
 int m = 0;
 int index = 0;
if(pageindex != null){
	m = Integer.parseInt(pageindex)*PAGE_MAX_NO;
	index = Integer.parseInt(pageindex);
}
int n = npcm.getAmountOfNPCs();
if(n > 0){
	int pageCount = 0;
	pageCount = n/PAGE_MAX_NO;
	if(n%PAGE_MAX_NO != 0){
		pageCount+=1;
	}
	if(gameName == null){
	out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>当前页为第<font color='red'>"+(index+1)+"</font>页&nbsp;&nbsp;总共<font color='#2D20CA'>"+pageCount+"</font>页&nbsp;&nbsp;每页"+PAGE_MAX_NO+"条数据</b><br><br>");
	out.println("<a href='npcs.jsp?pageindex="+0+"'>首页  </a>");
	if(index > 0){
		out.println("<a href='npcs.jsp?pageindex="+(index-1)+"'><<上一页<<  </a>");	
	}
	for(int i = 0; i < 10; i++){
		if(index+i < pageCount){
		out.println("<a href='npcs.jsp?pageindex="+(index+i)+"'>第"+(index+i+1)+"页  </a>");
		}
	}
	if(index <(pageCount-1)){
		out.println("<a href='npcs.jsp?pageindex="+(index+1)+"'>>>下一页>>  </a>");	
	}
	out.println("<a href='npcs.jsp?pageindex="+(pageCount-1)+"'>末页  </a>");
	}
	%>
<table>
<tr class="titlecolor">
<td>种类ID</td>
<td>所在地图</td>
<td>ID</td>
<td>名字</td>
<td>阵营</td>
<td>出生点</td>
<td>坐标</td>
<td>血</td>
<td>路径</td>
</tr>	
<%

NPC sprites[] = null;
	if((m+PAGE_MAX_NO)>n){
		sprites = npcm.getNPCsByPage(m,(n-m));
	}else{
		sprites = npcm.getNPCsByPage(m,PAGE_MAX_NO);
	}
	//按地图取npc
	if(gameName != null){
		NPC[] allNpcs = npcm.getNPCsByPage(0,n);
		ArrayList<NPC> list = new ArrayList<NPC>();
		if(allNpcs != null){
			for(int i = 0; i < allNpcs.length; i++){
				NPC npc = allNpcs[i];
				if(npc != null && gameName.equals(npc.getGameName())){
					list.add(npc);
				}
			}
		}
		sprites = list.toArray(new NPC[0]);
	}
	
	
	HashMap<Integer,ArrayList<NPC>> map = new HashMap<Integer,ArrayList<NPC>>();
	for(int i = 0 ; i < sprites.length ; i++){
		int cid = sprites[i].getNPCCategoryId();
		ArrayList<NPC> al = map.get(cid);
		if(al == null){
			al = new ArrayList<NPC>();
			map.put(cid,al);
		}
		al.add(sprites[i]);
	}
	Integer cids[] = map.keySet().toArray(new Integer[0]);
	String innerStateStr[] = new String[]{"巡逻","战斗","回出生点"};
	
	for(int i = 0 ; i < cids.length ; i++){
		ArrayList<NPC> al = map.get(cids[i]);
		
		for(int j = 0 ; j < al.size() ; j++){
			NPC s = al.get(j);
			out.println("<tr>");
			if(j == 0){
				out.println("<td rowspan='"+al.size()+"'>"+cids[i]+"</td>");
			}
			out.println("<td><a href='npcs.jsp?gameName="+s.getGameName()+"'>"+s.getGameName()+"</a></td>");
			out.println("<td><a href='npcauto.jsp?sid="+s.getId()+"'>"+s.getId()+"</a></td>");
			out.println("<td>"+s.getName()+"</td>");
			String politicalCampStr = "";
			
			out.println("<td>"+politicalCampStr+"</td>");
			out.println("<td>"+s.getBornPoint()+"</td>");
			out.println("<td>"+s.getX()+","+s.getY()+"</td>");
			out.println("<td>"+s.getHp()+"/"+s.getMaxHP()+"</td>");
			if(s.getMoveTrace() != null){
				MoveTrace mt = s.getMoveTrace();
				StringBuffer sb = new StringBuffer();
				for(int k = 0 ; k < mt.getRoadLen().length ; k++){
					if(mt.getCurrentRoad() == k){
						sb.append("("+mt.getRoadPoints()[k].x+","+mt.getRoadPoints()[k].y+")-->");
					}else if(mt.getCurrentRoad() +1 == k){
						sb.append("("+mt.getRoadPoints()[k].x+","+mt.getRoadPoints()[k].y+")->");
					}else{
						sb.append("("+mt.getRoadPoints()[k].x+","+mt.getRoadPoints()[k].y+")->");
					}
					
					if(k == mt.getRoadLen().length-1){
						sb.append("("+mt.getRoadPoints()[mt.getRoadLen().length].x+","+mt.getRoadPoints()[mt.getRoadLen().length].y+")");
					}
				}
				out.println("<td>"+sb.toString()+"</td>");
			}else{
				out.println("<td>--</td>");
			}
			
		}
	}
%>
</table>
<%
if(gameName == null){
out.println("<a href='npcs.jsp?pageindex="+0+"'>首页  </a>");
if(index > 0){
	out.println("<a href='npcs.jsp?pageindex="+(index-1)+"'><<上一页<<  </a>");	
}
for(int i = 0; i < 10; i++){
	if(index+i < pageCount){
	out.println("<a href='npcs.jsp?pageindex="+(index+i)+"'>第"+(index+i+1)+"页  </a>");
	}
}
if(index <(pageCount-1)){
	out.println("<a href='npcs.jsp?pageindex="+(index+1)+"'>>>下一页>>  </a>");	
}
out.println("<a href='npcs.jsp?pageindex="+(pageCount-1)+"'>末页  </a>");}
}
%>	
</BODY>
</html>
