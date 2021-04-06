<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.core.*,com.fy.engineserver.datasource.buff.*,
com.fy.engineserver.datasource.skill.*,com.google.gson.*,java.lang.reflect.*,com.fy.engineserver.sprite.monster.*"%><% 
	
MonsterManager sm = MemoryMonsterManager.getMonsterManager();
	int sid = Integer.parseInt(request.getParameter("sid"));
	Monster sprite = sm.getMonster(sid);
	String innerStateStr[] = new String[]{"巡逻","战斗","回出生点"};
	ArrayList<ActiveSkill> skills = sprite.getSkillList();
	StringBuffer skillSB = new StringBuffer();
	if(skills.size() > 0){
		for(int k = 0 ; k < skills.size() ; k++){
			skillSB.append("<a href='skillbyid.jsp?id="+skills.get(k).getId() +"&className="+skills.get(k).getClass().getName()+"'>"+skills.get(k).getName() +"<a/>,");
		}
	}
	StringBuffer pathSB = new StringBuffer();
	if(sprite.getMoveTrace() != null){
		MoveTrace mt = sprite.getMoveTrace();
		for(int k = 0 ; k < mt.getRoadLen().length ; k++){
			if(mt.getCurrentRoad() == k){
				pathSB.append("<b>("+mt.getRoadPoints()[k].x+","+mt.getRoadPoints()[k].y+")--></b>");
			}else if(mt.getCurrentRoad() +1 == k){
				pathSB.append("<b>("+mt.getRoadPoints()[k].x+","+mt.getRoadPoints()[k].y+")</b>->");
			}else{
				pathSB.append("("+mt.getRoadPoints()[k].x+","+mt.getRoadPoints()[k].y+")->");
			}
			
			if(k == mt.getRoadLen().length-1){
				pathSB.append("("+mt.getRoadPoints()[mt.getRoadLen().length].x+","+mt.getRoadPoints()[mt.getRoadLen().length].y+")");
			}
		}
	}
	MonsterFightingAgent agent = sprite.getFightingAgent();
	StringBuffer agentSB = new StringBuffer();
	if(agent != null){
		if(agent.isFighting()){
			agentSB.append("状态：战斗中；");
			agentSB.append("技能："+agent.getSkill().getName()+"；");
		}else{
			agentSB.append("状态：未战斗；");
		}
	}
	ActiveSkillAgent sa = sprite.getSkillAgent();
	StringBuffer saSB = new StringBuffer();
	if(sa != null){
		if(sa.getExecuting() != null){
			ActiveSkillEntity en = sa.getExecuting();
			saSB.append("执行中，技能实体状态：" + en.getStateStr());
		}else{
			saSB.append("无执行技能");
		}
	}
	ArrayList<Monster.DamageRecord>  hatrid = sprite.getHatridList();
	Gson gson = new Gson();
	%>
<%@include file="IPManager.jsp" %><html><head>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
<style type="text/css">
.tablestyle1{
width:100%;
border:0px solid #69c;
border-top:1px solid #69c;
border-right:1px solid #69c;
border-bottom:1px solid #69c;
border-left:1px solid #69c;
border-collapse: collapse;
}
td{
border:1px solid #69c;
text-align:left;
}
.tdtable{
 padding: 0px;
 border-top:0px solid #69c;
border-right:0px solid #69c;
border-bottom:0px solid #69c;
border-left:0px solid #69c;
}
.width{
width:200px;
}
.align{
text-align: left;
}
.borderbottom{
border-bottom:1px solid red;
}
.titlecolor{
background-color:#69c;
}
.lefttd{
border-left:0px solid #69c;
}
.toptd{
border-top:0px solid #69c;
}
.righttd{
border-right:0px solid #69c;
}
.bottomtd{
border-bottom:0px solid #69c;
}
</style>
</HEAD>
<BODY>
<h2>精灵的情况</h2><br><a href="./sprite.jsp?sid=<%=sid %>">刷新此页面</a><br><br>
<input type="button" value="返回" onclick="javascript:history.back();"><br>
<table class="tablestyle1">
<tr><td class="width">种类</td><td><%= sprite.getSpriteCategoryId() %></td></tr>
<tr><td class="width">所在地图</td><td><%=sprite.getGameName() %></td></tr>
<tr><td class="width">ID</td><td><%=sprite.getId() %></td></tr>
<tr><td class="width">名字</td><td><%=sprite.getName() %></td></tr>
<tr><td class="width">等级</td><td><%=sprite.getLevel() %></td></tr>
<tr><td class="width">称号</td><td><%=sprite.getTitle() %></td></tr>
<tr><td class="width">阵营</td><td><%=sprite.getCountry() %></td></tr>
<tr><td class="width">归属</td><td><%=(sprite.getOwner()==null?"--":sprite.getOwner().getName()) %></td></tr>
<tr><td class="width">出生点</td><td><%=sprite.getBornPoint() %></td></tr>
<tr><td class="width">坐标</td><td><%= sprite.getX()+","+sprite.getY() %></td></tr>
<tr><td class="width">血</td><td><%=sprite.getHp()+"/"+sprite.getMaxHP() %></td></tr>
<tr><td class="width">avatar属性</td><td></td></tr>
<tr><td class="width">普通攻击速度</td><td><%=(sprite.getCommonAttackSpeed()/1000)+"秒" %></td></tr>
<tr><td class="width">普通攻击距离</td><td><%=sprite.getCommonAttackRange()+"像素" %></td></tr>
<tr><td class="width">物理伤害</td><td></td></tr>
<tr><td class="width">魔法伤害</td><td></td></tr>
<% 
List<Buff> buffList = new ArrayList<Buff>();
for(byte i = 0;i<68 ; i++){
	if(sprite.getBuff(i) != null){
		buffList.add(sprite.getBuff(i));
	}
}
	%>

<tr><td class="width">身上的buff</td>
	<td>
<%if(buffList.isEmpty()){
	%>该怪物没有中Buff<%
}else{
	for(Buff buff : buffList){
		%>
		&nbsp;<a href="buffbyname.jsp?buffName=<%=buff.getTemplateName() %>"><%=buff.getTemplateName()%>(<%=buff.getBufferType() %>)</a>&nbsp;
		<%
	}
}
%>
	</td>
</tr>
<tr><td class="width">定身状态，不能移动</td><td><%=sprite.isHold()%></td></tr>
<tr><td class="width">眩晕状态，不能移动，不能使用技能和道具</td><td><%=sprite.isStun()%></td></tr>
<tr><td class="width">免疫状态</td><td><%=sprite.isImmunity()%></td></tr>
<tr><td class="width">中毒状态</td><td><%=sprite.isPoison()%></td></tr>
<tr><td class="width">虚弱状态</td><td><%=sprite.isWeak()%></td></tr>
<tr><td class="width">无敌状态</td><td><%=sprite.isInvulnerable()%></td></tr>
<tr><td class="width">影响闪避率的变量</td><td><%=sprite.getDodge()%></td></tr>
<tr><td class="width">影响会心一击的概率</td><td><%=sprite.getCriticalHit()%></td></tr>
<tr><td class="width">初始移动速度</td><td><%=sprite.getSpeed()+"像素/秒"%></td></tr>
<tr><td class="width">掉落经验值</td><td><%=sprite.getExp() %></td></tr>
<tr><td class="width">NPC的对话内容</td><td><%=sprite.getDialogContent() %></td></tr>
<tr><td class="width">身高</td><td><%=sprite.getHeight() %></td></tr>
<tr><td class="width">是否有任务可接</td><td><%=sprite.isTaskMark() %></td></tr>
<tr><td class="width">状态</td><td><%= Sprite.getStateStr(sprite.getState()) %></td></tr>
<tr><td class="width">内部状态</td><td><%=innerStateStr[sprite.getInnerState()] %></td></tr>
<tr><td class="width">激活类型</td><td><%=sprite.getActivationType() %></td></tr>
<tr><td class="width">技能</td><td><%=skillSB.length() > 1?  skillSB.substring(0,skillSB.length()-1):skillSB%></td></tr>
<tr><td class="width">路径</td><td><%=pathSB %></td></tr>
<tr><td class="width">自动战斗代理</td><td><%= agentSB %></td></tr>
<tr><td class="width">技能执行代理</td><td><%= saSB %></td></tr>
<%
	if(hatrid.size() == 0){
		%><tr><td class="width">仇恨列表</td><td>---</td></tr><%
	}else{
		for(int i = 0 ; i < hatrid.size() ; i++){
			out.println("<tr>");
			if(i == 0){
				out.println("<td class='width' rowspan='"+hatrid.size()+"'>仇恨列表</td>");
			}
			Monster.DamageRecord d = hatrid.get(i);
			if(d.f instanceof Player){
				out.println("<td>玩家："+((Player)d.f).getName()+", 仇恨："+d.hatred+", 伤害："+d.damage+"</td>");
			}else{
				out.println("<td>其他："+d.f.getId()+", 仇恨："+d.hatred+", 伤害："+d.damage+"</td>");
			}
			out.println("</tr>");
		}
	}
%>
<tr><td class="width">刷新时间</td><td><%= sprite.getFlushFrequency() %></td></tr>
<tr><td class="width">回程补血量</td><td><%= sprite.getBackBornPointHp() %></td></tr>
<tr><td class="width">回程速度比</td><td><%=sprite.getBackBornPointMoveSpeedPercent()*1.0/100 %></td></tr>
<tr><td class="width">掉落经验值</td><td><%=sprite.getExp() %></td></tr>
<tr><td class="width">轨迹类型</td><td><%=sprite.getTraceType() %></td></tr>
<tr><td class="width">心跳时间</td><td><%= (System.currentTimeMillis() - sprite.getHeartBeatStartTime())+"毫秒前" %></td></tr>
<tr><td class="width">实现类</td><td><%=sprite.getClass().getName() %></td></tr>
</table>
<br>
<input type="button" value="返回" onclick="javascript:history.back();">
</BODY></html>
