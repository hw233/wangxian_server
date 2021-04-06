<%@ page contentType="text/html;charset=utf-8"
	import="java.util.*,com.xuanzhi.tools.text.*,com.fy.engineserver.datasource.skill.*,com.xuanzhi.tools.transport.*,com.google.gson.Gson,com.fy.engineserver.core.*,java.io.*,java.lang.reflect.*,com.fy.engineserver.datasource.career.*"%><%! 
	
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
	Map<String,String> map = new HashMap<String,String>();
	map.put("skillType","技能类型，分主动，被动，和光环");
	map.put("icon","技能的图标");
	map.put("duration1","僵直时间");
	map.put("duration2","攻击时间");
	map.put("duration3","冷却时间");
	map.put("style1","僵直动画风格");
	map.put("style2","攻击动画风格");
	map.put("effectiveTimes","出招时间");
	map.put("enableWeaponType","是否有武器限制");
	map.put("weaponTypeLimit","武器具体的限制是什么");
	map.put("attackType","攻击类型:0 表示物理攻击1 表示法术攻击");
	map.put("buffTargetType","buff作用的对象");
	map.put("buffName","Buff的名称");
	map.put("buffLevel","buff的级别");
	map.put("buffLastingTime","buff持续的时间");
	map.put("buffProbability","buff产生的概率");
	map.put("attackPercent","发挥基础攻击力的百分比，跟等级相关");
	map.put("range","普通攻击的有效距离");
	map.put("traceType","路径类型，0表示直线，1表示寻路出来的路径");
	map.put("distance","距离目标点多远停下来");
	map.put("mp","消耗的法力值，跟等级相关");
	map.put("rangeType","范围的类型");
	map.put("rangeWidth","以玩家自身为中心的，一个矩形，此参数为矩形的宽度");
	map.put("rangeHeight","以玩家自身为中心的，一个矩形，此参数为矩形的宽度");
	map.put("effectType","后效的类型");
	map.put("effectLastTime","后效持续的时间，单位毫秒");
	map.put("effectExplosionLastTime","后效持续的时间过后，爆炸持续的时间");
	map.put("matrixType","后效分布的类型:0 正方形排列,1 菱形排列");
	map.put("gapWidth","后效排列之间");
	map.put("gapHeight","范围的高度");
	map.put("effectNum","后效的个数");
	map.put("maxAttackNum","最大攻击的人数");
	map.put("trackType","轨迹的类型，0表示直线，1表示追踪");
	map.put("speed","飞行物体的飞行速度");
	map.put("attackWidth","攻击的宽度");
	map.put("effectInitPositionX","每个后效的初始位置");
	map.put("effectInitPositionY","每个后效的初始位置");
	map.put("effectInitDirection","后效的方向");
	map.put("penetrateNum","后效能穿透的次数，0表示不能穿透");
	map.put("targetType","目标类型:0 表示对某个目标或者位置点进行攻击，无需配置后效方向。1 表示对周围环境进行攻击，需配置后效方向");
	map.put("explosionLastingTime","后效爆炸持续的时间");
	map.put("name","技能名称");
	map.put("id","技能编号");
	map.put("type","技能类型：主动技能、被动技能、光环辅助技能");
	map.put("description","描述");
	map.put("maxLevel","最大等级");
	map.put("changes","被动技能改变的属性");
	map.put("interval","技能释放周期");
	map.put("followByCommonAttack","继续用普通攻击");
	map.put("attackDamages","技能的伤害");
	map.put("needCareerThreadPoints","需要职业线路点数");
	map.put("class","类");
	map.put("maps","要修改的主动技能的属性");
	map.put("desps","每个级别的描述");
	map.put("auraType","光环类型");
	map.put("","");
	
	CareerManager cm = CareerManager.getInstance();
	Skill[] skills = cm.getSkills();
	Gson gson = new Gson();
	Skill skill = null;
	String skillName = "";
	String data = request.getParameter("id");
	if (data != null) {
		if (skills == null) {
			out.println("无法获得技能对象<br/>");
		} else {
			for(Skill s : skills){
				String id = "" + s.getId();
				if(id.equals(data)){
					skill = s;
					skillName = skill.getName();
				}
			}
		}

	}
%>
<%@include file="IPManager.jsp" %>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Weapon"%><html>
<head>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
</HEAD>
<BODY>
<h2>技能《<font color="red"><%= skillName %></font>》的详细数据</h2>
<input width="68" type="button" value="返回" onclick="javascript:history.back()">
<br>
<br>
	<%
		if(skill != null){
			Method ms[] = skill.getClass().getMethods();
	  		ArrayList<Method> al = new ArrayList<Method>();
	  		for(int j = 0 ; j < ms.length ; j++){
	  			if(ms[j].getName().length() > 3 && (ms[j].getName().startsWith("get") || ms[j].getName().startsWith("is")) 
	  					&& (ms[j].getModifiers() & Modifier.PUBLIC) != 0
	  					&& ms[j].getParameterTypes().length == 0){
	  				al.add(ms[j]);
	  			}
	  		}
	%>
		<table cellspacing="1" cellpadding="0" border="0" bgcolor="black">

		<tr bgcolor="#C2CAF5" align="center">
		<td>属性</td><td nowrap>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;描述&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td>值</td>
		</tr>
  		<%
  		int alSize = al.size();
  		for(int j = 0; j < alSize; j++){
  			%>
  			<tr bgcolor="#FFFFFF">
  			<%
  				Method method = al.get(j);
  		  		String name ="";
  		  		if(method.getName().indexOf("is") == 0){
  		  			name = method.getName().substring(2);
  		  		}else{
  		  			name = method.getName().substring(3);
  		  		}
  		  		name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
  		  		%>
  				<td align="center" style="word-wrap: break-word;">
  				<%= name%>
  				</td>
  				<td align="left" style="word-wrap: break-word;">
  				<%= map.get(name)%>
  				</td>
  				<%
  				if(name.equals("buffName")){
  					String s = gson.toJson(method.invoke(skill));
  					String strs[] = null;
  					if(s.indexOf(",") >= 0){
  						 strs = s.split(",");
  					}
  					%>
					<td align="center">
					<%
					if(strs != null){
	  					for(int i = 0; i < strs.length; i++){
	  						String strTemp = strs[i];
	  						String str = strTemp.substring(strTemp.indexOf('"')+1,strTemp.lastIndexOf('"'));
	  						out.println("&nbsp;<a href='buffbyname.jsp?buffName="+str+"'>"+str+"["+(i+1)+"级]</a>&nbsp;");
	  						
	  					}
  					}else{
  						out.println(s);
  					}
  					%>
  					</td>
  					<%
  					
  				}else if(name.equals("weaponTypeLimit")){
  					%>
					<td align="center" style="word-wrap: break-word;"></td>
				<%
  					
  				}else{
  					if(method.getReturnType().getName().indexOf("[") == 0){
					%>
					<td align="center" style="word-wrap: break-word;"><%= gson.toJson(method.invoke(skill))%></td>
					
					<%
					}else{
					%>
						<td align="center" style="word-wrap: break-word;"><%= method.invoke(skill)%></td>
					<% 
					}
  				}
  				%>
			</tr>
		<%} %>
		</table>
		<%}else{
			out.println("<h2>技能id不存在</h2>");
		}
	
	%>
<br/>
<input width="68" type="button" value="返回" onclick="javascript:history.back()">
<br>
</BODY>
</html>
