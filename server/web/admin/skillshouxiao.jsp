<%@ page contentType="text/html;charset=utf-8"
	import="java.util.*,com.xuanzhi.tools.text.*,com.fy.engineserver.datasource.skill.*,com.xuanzhi.tools.transport.*,com.google.gson.Gson,com.fy.engineserver.core.*,java.io.*,java.lang.reflect.*,com.fy.engineserver.datasource.career.*,com.fy.engineserver.datasource.skill.activeskills.*"%><%!
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
	map.put("IncreaseMoveSpeedPassiveSkill","增加移动速度百分比");
	map.put("IncreaseLiMinZhiNaiPassiveSkill","增加力敏智耐百分比(同比例)");
	map.put("IncreaseFaFangWuFangCConstitutionC","增加法防(数值)物防(百分比)耐力(百分比)");
	map.put("IncreaseFanShangPassiveSkill","增加反伤百分比");
	map.put("IncreaseWaiGongFangYuZhiPercentagePassiveSkill","增加外功防御值百分比");
	map.put("","");
	//为了整合统一类型的技能设置的List
	List<ResurrectionSkill> tempResurrectionSkills = new ArrayList<ResurrectionSkill>();
	List<SkillWithoutTraceAndOnTeamMember> tempSkillWithoutTraceAndOnTeamMembers = new ArrayList<SkillWithoutTraceAndOnTeamMember>();
	List<SkillWithoutTraceAndWithMatrix> tempSkillWithoutTraceAndWithMatrixs = new ArrayList<SkillWithoutTraceAndWithMatrix>();
	List<SkillWithoutTraceAndWithRange> tempSkillWithoutTraceAndWithRanges = new ArrayList<SkillWithoutTraceAndWithRange>();
	List<SkillWithoutTraceAndWithTargetOrPosition> tempSkillWithoutTraceAndWithTargetOrPositions = new ArrayList<SkillWithoutTraceAndWithTargetOrPosition>();
	List<SkillWithTraceAndDirectionOrTarget> tempSkillWithTraceAndDirectionOrTargets = new ArrayList<SkillWithTraceAndDirectionOrTarget>();
	int skillIndex = 0;
	for(Skill skill : skills){
		if(skill == null){
			out.println("技能数据出错，第"+skillIndex+"个技能为空");
			return;
		}
		if(skill instanceof ResurrectionSkill){
			skillIndex++;
			tempResurrectionSkills.add((ResurrectionSkill)skill);
		}
		if(skill instanceof SkillWithoutTraceAndOnTeamMember){
			skillIndex++;
			tempSkillWithoutTraceAndOnTeamMembers.add((SkillWithoutTraceAndOnTeamMember)skill);
		}
		if(skill instanceof SkillWithoutTraceAndWithMatrix){
			skillIndex++;
			tempSkillWithoutTraceAndWithMatrixs.add((SkillWithoutTraceAndWithMatrix)skill);
		}
		if(skill instanceof SkillWithoutTraceAndWithRange){
			skillIndex++;
			tempSkillWithoutTraceAndWithRanges.add((SkillWithoutTraceAndWithRange)skill);
		}
		if(skill instanceof SkillWithoutTraceAndWithTargetOrPosition){
			skillIndex++;
			tempSkillWithoutTraceAndWithTargetOrPositions.add((SkillWithoutTraceAndWithTargetOrPosition)skill);
		}
		if(skill instanceof SkillWithTraceAndDirectionOrTarget){
			skillIndex++;
			tempSkillWithTraceAndDirectionOrTargets.add((SkillWithTraceAndDirectionOrTarget)skill);
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
<h2>有后效的技能数据</h2>
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
		<td>后效的类型</td>
		<td>技能描述</td>
	</tr>
	<%

	if(!tempResurrectionSkills.isEmpty()){
		%>
		<tr>
			<td align="center" rowspan="<%=tempResurrectionSkills.size() %>"><%=map.get(tempResurrectionSkills.get(0).getClass().getSimpleName()) == null ? tempResurrectionSkills.get(0).getClass().getSimpleName():map.get(tempResurrectionSkills.get(0).getClass().getSimpleName())%></td>
			<td><%=tempResurrectionSkills.get(0).getId()%></td>
			<td nowrap><a href="skillbyid.jsp?id=<%=tempResurrectionSkills.get(0).getId()%>&className=<%=tempResurrectionSkills.get(0).getClass().getName() %>"><%=tempResurrectionSkills.get(0).getName()%></a></td>
			<td><img src="/game_server/imageServlet?id=<%=tempResurrectionSkills.get(0).getIconId() %>"></img></td>
			<td><%=tempResurrectionSkills.get(0).getEffectType() %></td>
			<td><%=tempResurrectionSkills.get(0).getDescription()%></td>
		</tr>
		
		<%
	
		for(int j = 1; j < tempResurrectionSkills.size(); j++){
			ResurrectionSkill skill = tempResurrectionSkills.get(j);

		%><tr>
			<td><%=skill.getId()%></td>
			<td nowrap><a href="skillbyid.jsp?id=<%=skill.getId()%>&className=<%=tempResurrectionSkills.get(j).getClass().getName() %>"><%=skill.getName()%></a></td>
			<td><img src="/game_server/imageServlet?id=<%=skill.getIconId() %>"></img></td>
			<td><%=skill.getEffectType() %></td>
			<td><%=skill.getDescription()%></td>
		</tr>
		<%
		}
	}
	if(!tempSkillWithoutTraceAndOnTeamMembers.isEmpty()){
		%>
		<tr>
			<td align="center" rowspan="<%=tempSkillWithoutTraceAndOnTeamMembers.size() %>"><%=map.get(tempSkillWithoutTraceAndOnTeamMembers.get(0).getClass().getSimpleName()) == null ? tempSkillWithoutTraceAndOnTeamMembers.get(0).getClass().getSimpleName():map.get(tempSkillWithoutTraceAndOnTeamMembers.get(0).getClass().getSimpleName())%></td>
			<td><%=tempSkillWithoutTraceAndOnTeamMembers.get(0).getId()%></td>
			<td nowrap><a href="skillbyid.jsp?id=<%=tempSkillWithoutTraceAndOnTeamMembers.get(0).getId()%>&className=<%=tempSkillWithoutTraceAndOnTeamMembers.get(0).getClass().getName() %>"><%=tempSkillWithoutTraceAndOnTeamMembers.get(0).getName()%></a></td>
			<td><img src="/game_server/imageServlet?id=<%=tempSkillWithoutTraceAndOnTeamMembers.get(0).getIconId() %>"></img></td>
			<td><%=tempSkillWithoutTraceAndOnTeamMembers.get(0).getEffectType() %></td>
			<td><%=tempSkillWithoutTraceAndOnTeamMembers.get(0).getDescription()%></td>
		</tr>
		
		<%
	
		for(int j = 1; j < tempSkillWithoutTraceAndOnTeamMembers.size(); j++){
			SkillWithoutTraceAndOnTeamMember skill = tempSkillWithoutTraceAndOnTeamMembers.get(j);

		%><tr>
			<td><%=skill.getId()%></td>
			<td nowrap><a href="skillbyid.jsp?id=<%=skill.getId()%>&className=<%=tempSkillWithoutTraceAndOnTeamMembers.get(j).getClass().getName() %>"><%=skill.getName()%></a></td>
			<td><img src="/game_server/imageServlet?id=<%=skill.getIconId() %>"></img></td>
			<td><%=skill.getEffectType() %></td>
			<td><%=skill.getDescription()%></td>
		</tr>
		<%
		}
	}
	if(!tempSkillWithoutTraceAndWithMatrixs.isEmpty()){
		%>
		<tr>
			<td align="center" rowspan="<%=tempSkillWithoutTraceAndWithMatrixs.size() %>"><%=map.get(tempSkillWithoutTraceAndWithMatrixs.get(0).getClass().getSimpleName()) == null ? tempSkillWithoutTraceAndWithMatrixs.get(0).getClass().getSimpleName():map.get(tempSkillWithoutTraceAndWithMatrixs.get(0).getClass().getSimpleName())%></td>
			<td><%=tempSkillWithoutTraceAndWithMatrixs.get(0).getId()%></td>
			<td nowrap><a href="skillbyid.jsp?id=<%=tempSkillWithoutTraceAndWithMatrixs.get(0).getId()%>&className=<%=tempSkillWithoutTraceAndWithMatrixs.get(0).getClass().getName() %>"><%=tempSkillWithoutTraceAndWithMatrixs.get(0).getName()%></a></td>
			<td><img src="/game_server/imageServlet?id=<%=tempSkillWithoutTraceAndWithMatrixs.get(0).getIconId() %>"></img></td>
			<td><%=tempSkillWithoutTraceAndWithMatrixs.get(0).getEffectType() %></td>
			<td><%=tempSkillWithoutTraceAndWithMatrixs.get(0).getDescription()%></td>
		</tr>
		
		<%
	
		for(int j = 1; j < tempSkillWithoutTraceAndWithMatrixs.size(); j++){
			SkillWithoutTraceAndWithMatrix skill = tempSkillWithoutTraceAndWithMatrixs.get(j);

		%><tr>
			<td><%=skill.getId()%></td>
			<td nowrap><a href="skillbyid.jsp?id=<%=skill.getId()%>&className=<%=tempSkillWithoutTraceAndWithMatrixs.get(j).getClass().getName() %>"><%=skill.getName()%></a></td>
			<td><img src="/game_server/imageServlet?id=<%=skill.getIconId() %>"></img></td>
			<td><%=skill.getEffectType() %></td>
			<td><%=skill.getDescription()%></td>
		</tr>
		<%
		}
	}
	if(!tempSkillWithoutTraceAndWithRanges.isEmpty()){
		%>
		<tr>
			<td align="center" rowspan="<%=tempSkillWithoutTraceAndWithRanges.size() %>"><%=map.get(tempSkillWithoutTraceAndWithRanges.get(0).getClass().getSimpleName()) == null ? tempSkillWithoutTraceAndWithRanges.get(0).getClass().getSimpleName():map.get(tempSkillWithoutTraceAndWithRanges.get(0).getClass().getSimpleName())%></td>
			<td><%=tempSkillWithoutTraceAndWithRanges.get(0).getId()%></td>
			<td nowrap><a href="skillbyid.jsp?id=<%=tempSkillWithoutTraceAndWithRanges.get(0).getId()%>&className=<%=tempSkillWithoutTraceAndWithRanges.get(0).getClass().getName() %>"><%=tempSkillWithoutTraceAndWithRanges.get(0).getName()%></a></td>
			<td><img src="/game_server/imageServlet?id=<%=tempSkillWithoutTraceAndWithRanges.get(0).getIconId() %>"></img></td>
			<td><%=tempSkillWithoutTraceAndWithRanges.get(0).getEffectType() %></td>
			<td><%=tempSkillWithoutTraceAndWithRanges.get(0).getDescription()%></td>
		</tr>
		
		<%
	
		for(int j = 1; j < tempSkillWithoutTraceAndWithRanges.size(); j++){
			SkillWithoutTraceAndWithRange skill = tempSkillWithoutTraceAndWithRanges.get(j);

		%><tr>
			<td><%=skill.getId()%></td>
			<td nowrap><a href="skillbyid.jsp?id=<%=skill.getId()%>&className=<%=tempSkillWithoutTraceAndWithRanges.get(j).getClass().getName() %>"><%=skill.getName()%></a></td>
			<td><img src="/game_server/imageServlet?id=<%=skill.getIconId() %>"></img></td>
			<td><%=skill.getEffectType() %></td>
			<td><%=skill.getDescription()%></td>
		</tr>
		<%
		}
	}
	if(!tempSkillWithoutTraceAndWithTargetOrPositions.isEmpty()){
		%>
		<tr>
			<td align="center" rowspan="<%=tempSkillWithoutTraceAndWithTargetOrPositions.size() %>"><%=map.get(tempSkillWithoutTraceAndWithTargetOrPositions.get(0).getClass().getSimpleName()) == null ? tempSkillWithoutTraceAndWithTargetOrPositions.get(0).getClass().getSimpleName():map.get(tempSkillWithoutTraceAndWithTargetOrPositions.get(0).getClass().getSimpleName())%></td>
			<td><%=tempSkillWithoutTraceAndWithTargetOrPositions.get(0).getId()%></td>
			<td nowrap><a href="skillbyid.jsp?id=<%=tempSkillWithoutTraceAndWithTargetOrPositions.get(0).getId()%>&className=<%=tempSkillWithoutTraceAndWithTargetOrPositions.get(0).getClass().getName() %>"><%=tempSkillWithoutTraceAndWithTargetOrPositions.get(0).getName()%></a></td>
			<td><img src="/game_server/imageServlet?id=<%=tempSkillWithoutTraceAndWithTargetOrPositions.get(0).getIconId() %>"></img></td>
			<td><%=tempSkillWithoutTraceAndWithTargetOrPositions.get(0).getEffectType() %></td>
			<td><%=tempSkillWithoutTraceAndWithTargetOrPositions.get(0).getDescription()%></td>
		</tr>
		
		<%
	
		for(int j = 1; j < tempSkillWithoutTraceAndWithTargetOrPositions.size(); j++){
			SkillWithoutTraceAndWithTargetOrPosition skill = tempSkillWithoutTraceAndWithTargetOrPositions.get(j);

		%><tr>
			<td><%=skill.getId()%></td>
			<td nowrap><a href="skillbyid.jsp?id=<%=skill.getId()%>&className=<%=tempSkillWithoutTraceAndWithTargetOrPositions.get(j).getClass().getName() %>"><%=skill.getName()%></a></td>
			<td><img src="/game_server/imageServlet?id=<%=skill.getIconId() %>"></img></td>
			<td><%=skill.getEffectType() %></td>
			<td><%=skill.getDescription()%></td>
		</tr>
		<%
		}
	}
	if(!tempSkillWithTraceAndDirectionOrTargets.isEmpty()){
		%>
		<tr>
			<td align="center" rowspan="<%=tempSkillWithTraceAndDirectionOrTargets.size() %>"><%=map.get(tempSkillWithTraceAndDirectionOrTargets.get(0).getClass().getSimpleName()) == null ? tempSkillWithTraceAndDirectionOrTargets.get(0).getClass().getSimpleName():map.get(tempSkillWithTraceAndDirectionOrTargets.get(0).getClass().getSimpleName())%></td>
			<td><%=tempSkillWithTraceAndDirectionOrTargets.get(0).getId()%></td>
			<td nowrap><a href="skillbyid.jsp?id=<%=tempSkillWithTraceAndDirectionOrTargets.get(0).getId()%>&className=<%=tempSkillWithTraceAndDirectionOrTargets.get(0).getClass().getName() %>"><%=tempSkillWithTraceAndDirectionOrTargets.get(0).getName()%></a></td>
			<td><img src="/game_server/imageServlet?id=<%=tempSkillWithTraceAndDirectionOrTargets.get(0).getIconId() %>"></img></td>
			<td><%=tempSkillWithTraceAndDirectionOrTargets.get(0).getEffectType() %></td>
			<td><%=tempSkillWithTraceAndDirectionOrTargets.get(0).getDescription()%></td>
		</tr>
		
		<%
	
		for(int j = 1; j < tempSkillWithTraceAndDirectionOrTargets.size(); j++){
			SkillWithTraceAndDirectionOrTarget skill = tempSkillWithTraceAndDirectionOrTargets.get(j);

		%><tr>
			<td><%=skill.getId()%></td>
			<td nowrap><a href="skillbyid.jsp?id=<%=skill.getId()%>&className=<%=tempSkillWithTraceAndDirectionOrTargets.get(j).getClass().getName() %>"><%=skill.getName()%></a></td>
			<td><img src="/game_server/imageServlet?id=<%=skill.getIconId() %>"></img></td>
			<td><%=skill.getEffectType() %></td>
			<td><%=skill.getDescription()%></td>
		</tr>
		<%
		}
	}
	%>
</table>
</BODY>
</html>
