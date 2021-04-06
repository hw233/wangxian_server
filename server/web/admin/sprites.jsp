<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.core.*,
com.fy.engineserver.datasource.skill.*,com.fy.engineserver.sprite.monster.*"%>
<%MonsterManager sm = MemoryMonsterManager.getMonsterManager();%>
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
		window.location.href="sprites.jsp?gameName="+obj.value;
	}
}

</script>
</HEAD>
<BODY>
<%String gameName = request.getParameter("gameName");
gameName = "piaomiaowangcheng";
GameManager gm = GameManager.getInstance();
Monster[] ms = sm.getMonstersByPage(0,1000000);
if(ms != null){}

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
	out.println("<h2>"+gameName+"中所有怪物的情况</h2><br><a href='sprites.jsp?gameName="+gameName+"'>刷新此页面</a>");
}else{
%>
<h2>Game Server，各个怪物的情况</h2><br><a href="./sprites.jsp">刷新此页面</a>
<%
}
int PAGE_MAX_NO = 60;
String pageindex = request.getParameter("pageindex");
 int m = 0;
 int index = 0;
if(pageindex != null){
	m = Integer.parseInt(pageindex)*PAGE_MAX_NO;
	index = Integer.parseInt(pageindex);
}
int n = sm.getAmountOfMonsters();
if(n > 0){
	//每页60条数据
	int pageCount = 0;
	pageCount = n/PAGE_MAX_NO;
	if(n%PAGE_MAX_NO != 0){
		pageCount+=1;
	}
	if(gameName == null){
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
	}
	%>
<table>
<tr class="titlecolor">
<td>种类ID</td>
<td>所在地图</td>
<td>ID</td>
<td>名字</td>
<td>归属</td>
<td>出生点</td>
<td>坐标</td>
<td>血</td>
<td>火攻</td>
<td>火防</td>
<td>火减防</td>
<td>冰攻</td>
<td>冰防</td>
<td>冰减防</td>
<td>风攻</td>
<td>风防</td>
<td>风减防</td>
<td>雷攻</td>
<td>雷防</td>
<td>雷减防</td>
<td>内部状态</td>
<td>激活类型</td>
<td>技能</td>
<td>路径</td>
</tr>	
<%

Monster sprites[] = null;
	if((m+PAGE_MAX_NO)>n){
		sprites = sm.getMonstersByPage(m,(n-m));
	}else{
		sprites = sm.getMonstersByPage(m,PAGE_MAX_NO);
	}
	//按地图取npc
	if(gameName != null){
		Monster[] allNpcs = sm.getMonstersByPage(0,n);
		ArrayList<Monster> list = new ArrayList<Monster>();
		if(allNpcs != null){
			for(int i = 0; i < allNpcs.length; i++){
				Monster npc = allNpcs[i];
				if(npc != null && gameName.equals(npc.getGameName())){
					list.add(npc);
				}
			}
		}
		sprites = list.toArray(new Monster[0]);
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
			out.println("<tr>");
			if(j == 0){
				out.println("<td rowspan='"+al.size()+"'>"+cids[i]+"</td>");
			}
			out.println("<td><a href='sprites.jsp?gameName="+s.getGameName()+"'>"+s.getGameName()+"</a></td>");
			out.println("<td><a href='spriteauto.jsp?sid="+s.getId()+"'>"+s.getId()+"</a></td>");
			out.println("<td>"+s.getName()+"</td>");
			out.println("<td>"+(s.getOwner()==null?"--":s.getOwner().getName())+"</td>");
			out.println("<td>"+s.getBornPoint()+"</td>");
			out.println("<td>"+s.getX()+","+s.getY()+"</td>");
			out.println("<td>"+s.getHp()+"/"+s.getMaxHP()+"</td>");
			out.println("<td>"+s.getFireAttackB()+"</td>");
			out.println("<td>"+s.getFireDefenceB()+"</td>");
			out.println("<td>"+s.getFireIgnoreDefenceB()+"</td>");
			out.println("<td>"+s.getBlizzardAttackB()+"</td>");
			out.println("<td>"+s.getBlizzardDefenceB()+"</td>");
			out.println("<td>"+s.getBlizzardIgnoreDefenceB()+"</td>");
			out.println("<td>"+s.getWindAttackB()+"</td>");
			out.println("<td>"+s.getWindDefenceB()+"</td>");
			out.println("<td>"+s.getWindIgnoreDefenceB()+"</td>");
			out.println("<td>"+s.getThunderAttackB()+"</td>");
			out.println("<td>"+s.getThunderDefenceB()+"</td>");
			out.println("<td>"+s.getThunderIgnoreDefenceB()+"</td>");
			out.println("<td>"+innerStateStr[s.getInnerState()]+"</td>");
			out.println("<td>"+s.getActivationType()+"</td>");
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
	out.println("<a href='sprites.jsp?pageindex="+(pageCount-1)+"'>末页  </a>");}
}
%>	
</BODY>
</html>
