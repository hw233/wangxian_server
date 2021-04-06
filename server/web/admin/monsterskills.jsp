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
    //SkillFactory sf = SkillFactory.getInstance();
    CareerManager cm = CareerManager.getInstance();
	Skill[] skills = cm.getSkills();
	Gson gson = new Gson();
	Map<String,String> map = new HashMap<String,String>();
	map.put("SkillWithoutEffect","无后效");
	map.put("SkillWithoutEffectAndQuickMove","无后效瞬移");
	map.put("SkillWithoutTraceAndOnTeamMember","无轨迹对队友");
	map.put("SkillWithoutTraceAndWithMatrix","无轨迹矩阵");
	map.put("SkillWithoutTraceAndWithRange","无轨迹有范围");
	map.put("SkillWithoutTraceAndWithTargetOrPosition","无轨迹有目标或位置");
	map.put("SkillWithTraceAndDirectionOrTarget","有轨迹和方向或目标");
	map.put("IncreaseBaoJiPassiveSkill","会心一击百分比");
	map.put("IncreaseDuoBiPassiveSkill","躲避百分比");
	map.put("IncreaseFaFangPassiveSkill","法防百分比");
	map.put("IncreaseFaGongPassiveSkill","道术攻击伤害力的百分比");
	map.put("IncreaseHpAndMpPassiveSkill","角色的最大HP,MP百分比");
	map.put("IncreaseHpPassiveSkill","角色的最大HP百分比");
	map.put("IncreaseHpRecoveryAndMpRecoveryPassiveSkill","体力魔法恢复速度，每五秒恢复一次");
	map.put("IncreaseHpRecoveryPassiveSkill","体力恢复速度，每五秒恢复一次 ");
	map.put("IncreaseMingZhongAndDuoBiPassiveSkill","躲避命中百分比");
	map.put("IncreaseMingZhongPassiveSkill","命中百分比");
	map.put("IncreaseMpPassiveSkill","角色的最大魔法值百分比");
	map.put("IncreaseMpRecoveryPassiveSkill","魔法恢复速度，每五秒恢复一次");
	map.put("IncreaseWuFangAndFaFangPassiveSkill","物防法防百分比");
	map.put("IncreaseWuFangPassiveSkill","物理防御百分比");
	map.put("IncreaseWuGongAndFaGongPassiveSkill","物理攻击道术攻击上限下限百分比");
	map.put("IncreaseWuGongPassiveSkill","物理攻击上限下限百分比");
	map.put("AuraSkill","光环类技能");
	map.put("CommonAttackSkill","普通攻击技能");
	map.put("IncreaseTizhiPassiveSkill","增加耐力被动技能");
	map.put("AssistActiveSkillPassiveSkill","辅助主动技能的被动技能");
	map.put("IncreaseLiliangPassiveSkill","增加力量被动技能");
	map.put("IncreaseZhiliPassiveSkill","增加智力被动技能");
	map.put("IncreaseMinjiePassiveSkill","增加敏捷被动技能");
	map.put("","");
	//为了整合统一类型的技能设置的List
	List<Skill> tempSkills = new ArrayList<Skill>();
	int skillIndex = 0;
	for(Skill skill : skills){
		if(skill == null){
			out.println("技能数据出错，第"+skillIndex+"个技能为空");
			return;
		}
		if(!cm.isSkillForSprite(skill)){
			continue;
		}
		skillIndex++;
		tempSkills.add(skill);
	}
	List<List<Skill>> lists = new ArrayList<List<Skill>>();
	List<Skill> list = new ArrayList<Skill>();
	while(true){
		for(Skill skill : tempSkills){
			if(list.isEmpty()){
				list.add(skill);
				continue;
			}
			if(list.get(0).getClass().getSimpleName().equals(skill.getClass().getSimpleName())){
				list.add(skill);
			}
			
		}
		lists.add(list);
		for(Skill skill : list){
			tempSkills.remove(skill);
		}
		list = new ArrayList<Skill>();
		if(tempSkills.isEmpty()){
			break;
		}
	}
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
<h2>怪物技能数据</h2>
<br>
<a href="./skills.jsp">刷新此页面</a>
<br>
<br>
<table>
	<tr class="titlecolor">
		<td>技能类型</td>
		<td>技能编号</td>
		<td nowrap>技能名称</td>
		<td>技能图标</td>
		<td>技能描述</td>
	</tr>
	<%
		if(!lists.isEmpty()){
			int listsSize = lists.size();

			for (int i = 0; i < listsSize; i++) {
				List<Skill> l = lists.get(i);
				if(!l.isEmpty()){
					%>
					<tr>
						<td align="center" rowspan="<%=l.size() %>"><%=map.get(l.get(0).getClass().getSimpleName())%></td>
						<td><%=l.get(0).getId()%></td>
						<td nowrap><a href="skillbyid.jsp?id=<%=l.get(0).getId()%>&className=<%=l.get(0).getClass().getName() %>"><%=l.get(0).getName()%></a></td>
						<td><img src="/game_server/imageServlet?id=<%=l.get(0).getIconId() %>"></img></td>
						<td><%=l.get(0).getDescription()%></td>
					</tr>
					
					<%
				
					for(int j = 1; j < l.size(); j++){
						Skill skill = l.get(j);

					%><tr>
						<td><%=skill.getId()%></td>
						<td nowrap><a href="skillbyid.jsp?id=<%=skill.getId()%>&className=<%=l.get(j).getClass().getName() %>"><%=skill.getName()%></a></td>
						<td><img src="/game_server/imageServlet?id=<%=skill.getIconId() %>"></img></td>
						<td><%=skill.getDescription()%></td>
					</tr>
					<%
					}
				}
			}
		}
	%>
</table>
</BODY>
</html>
