<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.core.*,
com.fy.engineserver.datasource.skill.*,java.lang.reflect.*,com.google.gson.*,java.lang.annotation.*,com.fy.engineserver.sprite.monster.*,com.fy.engineserver.datasource.career.*"%><%! 
	
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
	
MonsterManager sm = MemoryMonsterManager.getMonsterManager();
MemoryMonsterManager.MonsterTempalte sts[] = ((MemoryMonsterManager)sm).getMonsterTemaplates();
CareerManager cm = CareerManager.getInstance();
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
<h2>怪模板数据</h2>
<input type="button" value="返回" onclick="javascript:history.back();">
<br><br>
	<%
	Map<String,String> map = new HashMap<String,String>();
	map.put("spriteCategoryId","怪的种类ID，此ID在编辑的时候必须唯一");
	map.put("flushFrequency","刷新频率，表示此对应的怪消失后，多少毫秒内出现");
	map.put("deadLastingTime","死亡动画播放的时间");
	map.put("traceType","运动轨迹:0 原地不动,1 按一定的轨迹巡逻");
	map.put("patrolingWidth","巡逻的范围宽度");
	map.put("patrolingHeight","巡逻的范围高度");
	map.put("patrolingTimeInterval","巡逻的时间间隔");
	map.put("activationType","激活条件，所谓激活就是攻击玩家:0  表示被攻击，准备攻击攻击他的玩家,1  表示进入怪的视野范围，怪看到玩家后，就开始主动攻击玩家，同时如果怪被攻击，优先攻击攻击他的玩家,2  满足0和1，同时视野范围内的同类怪被攻击，就开始主动攻击玩家");
	map.put("activationWidth","激活的范围宽度，以怪为中心");
	map.put("activationHeight","激活的范围高度，以怪为中心");
	map.put("pursueWidth","追击的范围宽度，以怪为中心");
	map.put("pursueHeight","追击的范围高度，以怪为中心");
	map.put("backBornPointMoveSpeedPercent","回出生点的速度是移动速度的百分之多少。150表示1.5倍");
	map.put("backBornPointHp","回程路上的补血量，每0.5秒钟的补血");
	map.put("activeSkillIds","怪装备的技能ID列表");
	map.put("activeSkillLevels","怪装备的技能ID列表，对应的各个技能的级别");
	map.put("scheme","怪的avatar属性，此属性为静态属性");
	map.put("gameName","游戏中名称");
	map.put("title","怪物的称号");
	map.put("name","怪物名");
	map.put("level","怪物的等级");
	map.put("type","怪物类型，为mapping使用的，比如0代表兔子，1代表老虎");
	map.put("totalHP","生命的最大值，需要策划指定");
	map.put("physicalDamageUpperLimit","物理伤害上限，需要策划指定");
	map.put("physicalDamageLowerLimit","物理伤害下限，需要策划指定");
	map.put("spellDamageUpperLimit","魔法伤害上限，需要策划指定");
	map.put("spellDamageLowerLimit","魔法伤害下限，需要策划指定");
	map.put("moveSpeed","初始移动速度，每秒多少像素，需要策划指定");
	map.put("defence","物理防御力，需要策划指定");
	map.put("resistance","魔法抵抗力，需要策划指定");
	map.put("attackRating","影响攻击命中率的变量，需要策划指定");
	map.put("dodge","影响闪避率的变量，需要策划指定");
	map.put("criticalHit","影响会心一击的概率，需要策划指定");
	map.put("exp","基本经验值，死亡后根据计算公式分配给玩家，需要策划指定");
	map.put("state","当前的状态，如站立、行走等");
	map.put("style","动画风格，为mapping使用的");
	map.put("aura","阴影层的光环，受光环辅助技能影响，默认值-1");
	map.put("direction","面朝的方向，如上下左右等");
	map.put("hp","当前生命值");
	map.put("hold","定身状态，不能移动");
	map.put("stun","眩晕状态，不能移动，不能使用技能和道具");
	map.put("immunity","免疫状态");
	map.put("id","生物编号");
	map.put("alive","是否活着");
	map.put("x","地图横坐标");
	map.put("y","地图纵坐标");
	map.put("viewWidth","可视区域宽");
	map.put("viewHeight","可视区域高");
	map.put("flopScheme","这个怪物的掉落方案");
	map.put("faShuHuDunDamage","法术护盾");
	map.put("wuLiHuDunDamage","物理护盾");
	map.put("spriteType","精灵的类型，用于区别怪物还是普通NPC，或其他种类的精灵");
	map.put("monsterCareer","怪物职业");
	map.put("monsterRace","怪物种族");
	map.put("commonAttackSpeed","普通攻击的攻击速度（毫秒为单位）");
	map.put("commonAttackRange","普通攻击的攻击距离（像素为单位）");
	map.put("poison","中毒状态");
	map.put("weak","虚弱状态");
	map.put("invulnerable","无敌状态");
	map.put("shield","护盾类型，-1代表没有护盾");
	map.put("dialogContent","用于NPC的对话内容");
	map.put("height","身高");
	map.put("monsterType","区别不同种类的怪物，只有在spriteType表明是怪物时有意义");
	map.put("npcType","区别不同种类的NPC，只有在spriteType表明是NPC时有意义");
	map.put("taskMark","标记NPC身上是否有任务可接");
	map.put("deadLastingTimeForFloop","有物品掉落的情况下，死亡持续的时间");
	map.put("politicalCamp","阵营 0中立，1紫薇宫，2日月盟");
	map.put("immuneStunFlag","免疫眩晕");
	map.put("immuneHoldFlag","免疫定省");
	map.put("immuneSilenceFlag","免疫沉默");
	map.put("immuneSpeedDownFlag","免疫减速");
	map.put("immuneSneerFlag","免疫嘲讽");
	Gson gson = new Gson();
	String id = request.getParameter("spriteCategoryId");
	Monster sprite = null;
	for(MemoryMonsterManager.MonsterTempalte spriteTemplate : sts){
		if(id.equals(""+spriteTemplate.MonsterCategoryId)){
			sprite = spriteTemplate.newMonster();
		}
	}		
	if(sprite != null){
		Method ms[] = sprite.getClass().getMethods();
		ArrayList<Method> al = new ArrayList<Method>();
		for(int j = 0 ; j < ms.length ; j++){
			if(ms[j].getName().length() > 3 && (ms[j].getName().startsWith("get") || ms[j].getName().startsWith("is")) 
					&& (ms[j].getModifiers() & Modifier.PUBLIC) != 0
					&& ms[j].getParameterTypes().length == 0){
				String name = ms[j].getName();
				//属性为布尔值时
				if(name.indexOf("is") == 0){
					name = "set" + name.substring(2);
				}else{
					name = "s" + name.substring(1);
				}
				Class c = ms[j].getReturnType();
				if(c.isPrimitive() || (c == String.class) || (c.toString().indexOf("class [") >= 0)){
					try{
						Method m = sprite.getClass().getMethod(name,new Class[]{c});
						if(m != null){
							al.add(ms[j]);
						}
					}catch(Exception e){
						
					}
				}
			}
		}
	%>
		<table>
		<tr class="titlecolor">
		<td>属性</td><td>描述</td><td>值</td>
		</tr>
		<tr>
			<td>skillList</td>
			<td>这个怪物拥有的技能</td>
			<td>
			<%
			ActiveSkill ass[] = sprite.getSkillList().toArray(new ActiveSkill[0]);
			if(ass.length != 0){
				for(ActiveSkill as : ass){
					if(as != null){
						%>
						&nbsp;<a href="skillbyid.jsp?id=<%=as.getId()%>&className=<%=as.getClass().getName() %>"><%=as.getName() %></a>&nbsp;
						<%
					}
				}
			}else{
				out.println("该怪物无技能");
			}
			
			%>
			</td>
		</tr>
  		<%
  		int alSize = al.size();
  		for(int j = 0; j < alSize; j++){
  			%>
  			<tr>
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
  				<td>
  				<%= name%>
  				</td>
  				<td>
  				<%= map.get(name) == null ? name : map.get(name)%>
  				</td>
  				<%
  					if(method.getReturnType().getName().indexOf("[") == 0){
  						if("activeSkillIds".equals(name)){
  							int[] skillIds = (int[])method.invoke(sprite);
  							StringBuffer sb = new StringBuffer();
  							if(skillIds != null){
  								for(int skillId : skillIds){
  									Skill skill = cm.getSkillById(skillId);
  									if(skill != null){
  										sb.append("<a href='skillbyid.jsp?id="+skillId+"&className="+skill.getClass().getName()+"'>"+skill.getName()+"</a> ");
  									}
  								}
  							}
  							%>
  							<td align="center" style="word-wrap: break-word;"><%= sb.toString()%></td>
  							
  							<%
  						}else{
					%>
							<td align="center" style="word-wrap: break-word;"><%= gson.toJson(method.invoke(sprite))%></td>
							
							<%
  						}
				}else{
				%>
					<td align="center" style="word-wrap: break-word;"><%= method.invoke(sprite)%></td>
				<% 
				}
  				%>
			</tr>
		<%} %>
		</table>
		
		<%
		//BOSS
			if(sprite instanceof BossMonster){
				BossMonster boss = (BossMonster)sprite;
				boss.init();
				BossExecuteItem items[] = boss.getBossExecuteItems();
				
				
				out.println("<h3>BOSS执行条目</h3>");
				out.println("<table>");
				out.println("<tr class='titlecolor'><td>开启后多久(秒)</td><td>范围要求（像素）</td><td>血量少于</td><td>最大执行次数</td><td>执行间隔(秒)</td><td>是否无视移动</td><td>动作类</td><td>动作编号</td><td>动作描述</td></tr>");
				for(int i = 0 ; i < items.length ; i++){
					BossExecuteItem bi = items[i];
					BossAction ba = ((MemoryMonsterManager)sm).getBossActionById(bi.getActionId());
					out.println("<tr>");
					out.println("<td>"+bi.getExeTimeLimit()/1000.0+"</td>");
					out.println("<td>"+bi.getMinDistance()+"~"+bi.getMaxDistance()+"</td>");
					out.println("<td>"+bi.getHpPercent()+"%</td>");
					out.println("<td>"+bi.getMaxExeTimes()+"</td>");
					out.println("<td>"+bi.getExeTimeGap()/1000.0+"</td>");
					out.println("<td>"+bi.isIgnoreMoving()+"</td>");
					if(ba == null){
						out.println("<td>没有动作</td>");
						out.println("<td>"+bi.getActionId()+"</td>");
						out.println("<td>没有动作</td>");
					}else{
						out.println("<td>"+ba.getClass().getName().substring(ba.getClass().getName().lastIndexOf(".")+1)+"</td>");
						out.println("<td><a href=''>"+bi.getActionId()+"</a></td>");
						out.println("<td>"+ba.getDescription()+"</td>");
					}
					
					out.println("</tr>");
				}
				
				out.println("</table>");
			}
		%>
		
		<%}else{
			out.println("<h2>怪物id不存在</h2>");
		}
	%>
<br>
<input type="button" value="返回" onclick="javascript:history.back();">
</BODY></html>
