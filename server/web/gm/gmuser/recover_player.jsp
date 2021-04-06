<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,java.lang.reflect.*,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,
com.google.gson.*,com.fy.engineserver.datasource.career.*,
com.fy.engineserver.datasource.skill.*,
com.fy.engineserver.datasource.props.*,
com.fy.engineserver.task.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.downcity.*,
com.fy.engineserver.util.*"%>
<%@page import="com.fy.engineserver.gm.record.Action"%>
<%@page import="com.fy.engineserver.gm.record.ActionManager"%>
<%@include file="../authority.jsp" %>
<%

	Gson gson = new Gson();
	PlayerManager sm = PlayerManager.getInstance();
	TaskManager tm = TaskManager.getInstance();
	ActionManager amanager = ActionManager.getInstance();
	int playerId = -1;
	String playerName = null;
	Player player = null;
	String errorMessage = null;
    String gmusername = session.getAttribute("username").toString();
	int rid = ParamUtils.getIntParameter(request,"rid",-1);
	if(rid != -1) {
		boolean succ = PlayerRecover.getInstance().recoverPlayer(rid);
		playerId = rid;
		errorMessage = succ?"恢复成功":"恢复失败";
		amanager.save(gmusername,"恢复了玩家信息id【"+rid+"】"+(succ?"恢复成功":"恢复失败"));
	}
	
	try {
		if(rid == -1) {
			playerId = Integer.parseInt(request
					.getParameter("playerid"));
		}
	} catch (Exception e) {
		playerId = -1;
	}
	if (errorMessage == null && playerId != -1) {
		player = sm.getPlayer(playerId);
		if (player == null) {
			errorMessage = "ID为" + playerId + "的玩家不存在！";
		}else{
			session.setAttribute("playerid",request.getParameter("playerid"));
			playerName = player.getName();
			session.setAttribute("playerName",playerName);
		}
	}
%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.PrintStream"%>
<html><head>
<link rel="stylesheet" type="text/css" href="../../css/common.css" />
<style type="text/css">
<!--
*{margin: 0px;padding: 0px;list-style: none;}
body{
 margin:0px;
 padding: 0px;
 text-align: center;
 table-layout: fixed;
 word-wrap: break-word;
 list-style: none;
}
#main{
width:1004px;
margin:10px auto;
border:0px solid #69c;
background-color:#DCE0EB;
}
.titlecolor{
background-color:#C2CAF5;
text-align: center;
}
.tableclass1{
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
word-wrap: break-word;
}
.tdwidth{width:100px;word-wrap: break-word;}
-->

</style>
<script type="text/javascript">
<!--
function recover(playerid) {
	if(playerid == -1) {
		window.alert("请你先输入角色id查询角色");
	} else if(window.confirm("是否确认要恢复玩家数据？玩家现有数据将被替换！")){
		window.location.replace('recover_player.jsp?rid=' + playerid);
	}
}
-->
</script>
</HEAD>
<BODY>
<h2>角色数据恢复</h2><br><br>
<%
	if (errorMessage != null) {
		out.println("<center><table border='0' cellpadding='0' cellspacing='2' width='100%' bgcolor='#FF0000' align='center'>");
		out.println("<tr bgcolor='#FFFFFF' align='center'><td>");
		out.println("<font color='red'><pre>" + errorMessage + "</pre></font>");
		out.println("</td></tr>");
		out.println("</table></center>");
	}
%>
<form id='f1' name='f1' action="recover_player.jsp"><input type='hidden' name='action' value='select_playerId'>
请输入要用户的ID：<input type='text' name='playerid' id='playerid' value='<%=playerId %>' size='20'><input type='submit' value='提  交'>
</form>
<!--  <form id='f01' name='f01' action="recover_player.jsp"><input type='hidden' name='action' value='select_playerName'>
请输入要角色名：<input type='text' name='playerName' id='playerName' value='<%=playerName %>' size='20'><input type='submit' value='提  交'>
</form> -->
<br>
<%  if(player != null){ 
		CareerManager cm = CareerManager.getInstance();	
		Player p = player;
		byte teamMark = p.getTeamMark();
		String teamMarkStr = "无组队";
		if(teamMark == 1){
			teamMarkStr = "团队成员" ;
		}else if(teamMark == 2){
			teamMarkStr = "团队队长";
		}
		Team team = p.getTeam();
		Player captain = null;
		if(team != null){
			captain = team.getCaptain();
		}
		String captainName = "";
		if(captain != null){
			captainName = "'"+captain.getName()+"'";
		}
		Player ps[] = p.getTeamMembers();
		StringBuffer sb = new StringBuffer();
		if(ps != null){
			for(Player pl : ps){
				if(pl != null){
					sb.append("'"+pl.getName()+"'");
				}
			}
		}
	String careerStr = "";
	String weaponStr = "";
	String nameStr = "";
	String sexStr = "";

	int skillCount = 0;
	String nextLevelExpStr = "";
	String currentLevel = "";
	//光环技能名称
	String[] auroStr = null;
	//光环技能下标，开启光环技能需要知道下标
	List auraIndexList = new ArrayList();
	int aura = -1;
	int[] auraIndex = null;
	List<AuraSkill> auraSkillList = new ArrayList<AuraSkill>();

	String playModeStr = "";

	if (player != null) {
		nameStr = player.getName();
		nextLevelExpStr = ""
				+ (player.getNextLevelExp() - player.getExp());
		currentLevel = "" + player.getLevel();
		int careerId = player.getCareer();
		Career career = cm.getCareer(careerId);

		if (player.getSex() == 0) {
			sexStr = "男";
		} else {
			sexStr = "女";
		}
	}
%>
<br>
当前角色<font color="red"><%=nameStr%></font><%="("+sexStr+")" %>的基本信息:
<br>
<form id='f51' name='f51' action="recover_player.jsp">
<input type='hidden' name='action' value='skilllearn'>
<input type='hidden' name='playerid' value='<%=playerId %>'>
<table class="tableclass1">
	<tr class="titlecolor">
		<td colspan="8">人物属性</td>
	</tr>
	<tr>
		<td class="tdsmallwidth">上次离线时间</td><td colspan="7"><%=DateUtil.formatDate(new java.util.Date(p.getQuitGameTime()),"yyyy-MM-dd HH:mm:ss") %></td>
	</tr>
	<tr>
		<td class="tdsmallwidth">力量</td><td colspan="7"><%=p.getStrength() %></td>
	</tr>
	<tr>
		<td>智力</td><td colspan="7"><%=p.getSpellpower() %></td>
	</tr>
	<tr>
		<td>耐力</td><td colspan="7"><%=p.getConstitution() %></td>
	</tr>
	<tr>
		<td>敏捷</td><td colspan="7"><%=p.getDexterity() %></td>
	</tr>
	<tr>
		<td>HP</td><td colspan="7"><%=p.getHp()%>/<%=p.getTotalHP() %></td>
	</tr>
	<tr>
		<td>MP</td><td colspan="7"><%=p.getMp()%>/<%=p.getTotalMP() %></td>
	</tr>
	<tr>
		<td>近战攻击强度</td><td colspan="7"><%=p.getMeleeAttackIntensity() %></td>
	</tr>
	<tr>
		<td>法术攻击强度</td><td colspan="7"><%=p.getSpellAttackIntensity() %></td>
	</tr>
	<tr>
		<td>物理伤害</td><td colspan="7"><%=p.getPhysicalDamageLowerLimit() %>/<%=p.getPhysicalDamageUpperLimit()%></td>
	</tr>
	<tr>
		<td>武器伤害</td><td colspan="7"><%=p.getWeaponDamageLowerLimit() %>/<%=p.getWeaponDamageUpperLimit()%></td>
	</tr>
	<tr>
		<td>攻击速度</td><td colspan="7"><%=p.getCommonAttackSpeed()/1000f %>秒</td>
	</tr>
	<tr>
		<td>暴击</td><td colspan="7"><%=p.getCriticalHit() %></td>
	</tr>
	<tr>
		<td>韧性</td><td colspan="7"><%=p.getToughness() %></td>
	</tr>
	<tr>
		<td>命中</td><td colspan="7"><%=p.getAttackRating() %></td>
	</tr>
	<tr>
		<td>闪避</td><td colspan="7"><%=p.getDodge() %></td>
	</tr>
	<tr>
		<td>移动速度</td><td colspan="7"><%=p.getSpeed() %>像素/秒</td>
	</tr>
	<tr>
		<td>怒气值</td><td colspan="7"><%=p.getXp() %>/<%=p.getTotalXp() %></td>
	</tr>
	<tr>
		<td>幸运值</td><td colspan="7"><%=p.getLuck() %></td>
	</tr>
	<tr>
		<td>物理防御</td><td colspan="7"><%=p.getDefence() %></td>
	</tr>
	<tr>
		<td>道术防御</td><td colspan="7"><%=p.getResistance() %></td>
	</tr>
	<tr>
		<td>等级</td><td colspan="7"><%=p.getLevel()+"级"%></td>
	</tr>
	<tr>
		<td>角色自身的元宝</td><td colspan="7"><%=p.getRmbyuanbao()%></td>
	</tr>
	<tr>
		<td>绑定元宝</td><td colspan="7"><%=p.getBindyuanbao()%></td>
	</tr>
	<tr>
		<td>游戏币</td><td colspan="7"><%=p.getMoney()%></td>
	</tr>
	<tr>
		<td>经验</td><td colspan="7"><%=p.getExp()%>/<%=p.getNextLevelExp() %></td>
	</tr>
	<tr>
		<td>门派</td><td colspan="7"><%=cm!= null&&cm.getCareer(p.getCareer())!= null? cm.getCareer(p.getCareer()).getName():"错误" %></td>
	</tr>
	<tr>
		<td>阵营</td><td colspan="7"><%=p.getPoliticalCampDesp(p.getPoliticalCamp()) %></td>
	</tr>
	<tr>
		<td>技能冷却时间的百分比</td><td colspan="7"><%=p.getCoolDownTimePercent()+"%" %></td>
	</tr>
	<tr>
		<td>技能僵直时间的百分比</td><td colspan="7"><%=p.getSetupTimePercent()+"%" %></td>
	</tr>
	<tr>
		<td>反噬伤害的百分比</td><td colspan="7"><%=p.getIronMaidenPercent()+"%" %></td>
	</tr>
	<tr>
		<td>攻击时吸取生命值的百分比</td><td colspan="7"><%=p.getHpStealPercent()+"%" %></td>
	</tr>
	<tr>
		<td>攻击时吸取魔法值的百分比</td><td colspan="7"><%=p.getMpStealPercent()+"%" %></td>
	</tr>
	<tr>
		<td>天赋增加的暴击百分比</td><td colspan="7"><%=p.getCriticalHitD()+"%" %></td>
	</tr>
	<tr>
		<td>天赋增加的命中百分比</td><td colspan="7"><%=p.getAttackRatingD()+"%" %></td>
	</tr>
	<tr>
		<td>天赋增加的闪避百分比</td><td colspan="7"><%=p.getDodgeD()+"%" %></td>
	</tr>
	<tr>
		<td>天赋增加的外功防御百分比</td><td colspan="7"><%=p.getDefenceD()+"%" %></td>
	</tr>
	<tr>
		<td>天赋增加的内功防御百分比</td><td colspan="7"><%=p.getResistanceD()+"%" %></td>
	</tr>
	<tr>
		<td>经验值获得的百分比</td><td colspan="7"><%=p.getExpPercent()+"%" %></td>
	</tr>
	<tr>
		<td>提高玩家整体输出伤害比例</td><td colspan="7"><%=p.getSkillDamageIncreaseRate()+"%" %></td>
	</tr>
	<tr>
		<td>降低玩家整体输出伤害比例</td><td colspan="7"><%=p.getSkillDamageDecreaseRate()+"%" %></td>
	</tr>
	<tr>
		<td>未分配的属性点</td><td colspan="7"><%=p.getUnallocatedPropertyPoint() %></td>
	</tr>
	<tr>
		<td>玩家模式</td><td colspan="7"><%=p.getPlayingMode() == 0 ? "练功" : "屠杀" %></td>
	</tr>
	<tr>
		<td>回城点</td><td colspan="7"><%=p.getHomeMapName()+"("+p.getHomeX()+","+ p.getHomeY()+")" %></td>
	</tr>
	<tr>
		<td>复活点</td><td colspan="7"><%=p.getResurrectionMapName()+"("+p.getResurrectionX()+","+ p.getResurrectionY()+")" %></td>
	</tr>
	<tr>
		<td>离线时间</td><td colspan="7"><%=p.getTotalOfflineTime()+"小时" %></td>
	</tr>
	<tr>
		<td>公会名称</td><td colspan="7"><%=p.getGangName() %></td>
	</tr>
	<tr>
		<td>公会中的职位</td><td colspan="7"><% String[] gangTitleStr = new String[]{"帮主","副帮主","精英","帮众"};%><%=(p.getGangTitle()>=0 && p.getGangTitle() < 4)?gangTitleStr[p.getGangTitle()]:p.getGangTitle() %></td>
	</tr>
	<tr>
		<td>保存的副本</td><td colspan="7">
		<%
		HashMap<String,DownCity> downCityProgress = p.getDownCityProgress();
		StringBuffer sbb = new StringBuffer();
		if(downCityProgress != null && downCityProgress.keySet() != null){
			for(String str : downCityProgress.keySet()){
				if(str != null){
					sbb.append("<a href=../downcitybyid.jsp?downcityid="+downCityProgress.get(str) != null ? downCityProgress.get(str).getId() : ""+">"+str+"</a> ");
				}
			}
		}
		%>
		<%=sbb.toString() %></td>
	</tr>
	<tr>
	<td>可装备武器</td>
	<td colspan="7">
	<%
	StringBuffer weaponsb = new StringBuffer();
	if(cm!= null&&cm.getCareer(p.getCareer())!= null){
		Career career = cm.getCareer(p.getCareer());
		int[] ints = career.getWeaponTypeLimit();
		if(ints != null){
			for(int index : ints){
				weaponsb.append(" "+Weapon.getWeaponTypeName((byte)index));
			}
		}
	}
	int usedSkillPoints = 0;
		%>
		<%=weaponsb.toString() %>
	</td>
	</tr>
<tr class="titlecolor">
<td class="tdwidth" nowrap>技能名称</td>
<td class="tdwidth" nowrap>技能类型</td>
<td>技能描述</td>
<td>需要点数</td>
<td>技能等级</td>
<td>伤害</td>
<td>公共CD</td>
<td>CD</td>
</tr>
<%
	if (player != null) {
		Career career = cm.getCareer(player.getCareer());
		//short skillIds[] = career.getSkillIds();
		CareerThread careerThreads[] = null;
		if(career != null){
			careerThreads = career.getCareerThreads();
			careerStr = career.getName();
		}
		if(careerThreads != null){
			for(int i = 0; i < careerThreads.length; i++){
				CareerThread careerThread = careerThreads[i];
				if(careerThread != null){
					Skill skills[] = careerThread.getSkills();
					if(skills != null){
						for(int j = 0; j < skills.length; j++){
							Skill skill = skills[j];
							if(skill != null){
								String skillType = "---";
								if(skill instanceof ActiveSkill){
									skillType = "主动技能";
								}else if(skill instanceof PassiveSkill){
									skillType = "被动技能";
								}else if(skill instanceof AuraSkill){
									skillType = "辅助光环技能";
								}
								boolean canLearn = career.isUpgradable(player,i,j);
					%>
						<tr bgcolor="#FFFFFF" align="center">
							<td nowrap><b><a href="../skillbyid.jsp?id=<%=skill != null ?skill.getId():"" %>&className=<%=skill != null ?skill.getClass().getName():"" %>"><%=skill != null ?skill.getName():"错误:技能为空"%></a></b></td>
							<td nowrap><b><%= skillType %></b></td>
							<td align="left"><%
								if(skill != null){
								String s = SkillInfoHelper.generate(player,skill);
								if(s != null){
									s = s.replaceAll("\\[/color\\]","</font>");
									s = s.replaceAll("\\[color=(.*)\\]","<font color='$1'>");
									s = s.replaceAll("\\[icon\\](.*)\\[/icon\\]","<img src='/icons/$1'>");
								}else{
									s = "无描述";
								}
								out.print("<pre>"+s+"</pre>"); }%></td>
								<td><b><%= skill != null ? skill.getNeedCareerThreadPoints():"" %></b></td>
							<td><b><%= skill != null ? player.getSkillLevel(skill.getId())+"/"+skill.getMaxLevel():"技能为空" %><%
									if(skill != null){
										usedSkillPoints = usedSkillPoints + player.getSkillLevel(skill.getId());
									}
								%></b></td>
							<td>
							<%if(skill instanceof ActiveSkill){
								%>
								<%=((ActiveSkill)skill).calDamageOrHpForFighter(player,player.getSkillLevel(skill.getId())) %>
								<%
							}else{
								%>
								0
								<%
							}
								%>
							</td>
							<td>
							<%if(skill instanceof ActiveSkill){
								%>
								<%=(((ActiveSkill)skill).getDuration1()+((ActiveSkill)skill).getDuration2())/1000f %>秒
								<%
							}else{
								%>
								--
								<%
							}
							%>
							</td>
							<td>
							<%if(skill instanceof ActiveSkill){
								%>
								<%=((ActiveSkill)skill).getDuration3()/1000f %>秒
								<%
							}else{
								%>
								--
								<%
							}
							%>
							</td>
						</tr>
					<%
							
							}
						}
					}
				}
			}
		}
	}
%>
	<tr>
		<td>已分配的技能点</td><td colspan="7"><%=usedSkillPoints %></td>
	</tr>
	<tr>
		<td>未分配的技能点</td><td colspan="7"><%=p.getUnallocatedSkillPoint() %></td>
	</tr>
</table>
<%} 
if(playerId > 0) {
	player = PlayerRecover.getInstance().getRecoverPlayer(playerId);
	if(player != null) {
		CareerManager cm = CareerManager.getInstance();	
		Player p = player;
		byte teamMark = p.getTeamMark();
		String teamMarkStr = "无组队";
		if(teamMark == 1){
			teamMarkStr = "团队成员";
		}else if(teamMark == 2){
			teamMarkStr = "团队队长";
		}
		Team team = p.getTeam();
		Player captain = null;
		if(team != null){
			captain = team.getCaptain();
		}
		String captainName = "";
		if(captain != null){
			captainName = "'"+captain.getName()+"'";
		}
		Player ps[] = p.getTeamMembers();
		StringBuffer sb = new StringBuffer();
		if(ps != null){
			for(Player pl : ps){
				if(pl != null){
					sb.append("'"+pl.getName()+"'");
				}
			}
		}
	String careerStr = "";
	String weaponStr = "";
	String nameStr = "";
	String sexStr = "";

	int skillCount = 0;
	String nextLevelExpStr = "";
	String currentLevel = "";
	//光环技能名称
	String[] auroStr = null;
	//光环技能下标，开启光环技能需要知道下标
	List auraIndexList = new ArrayList();
	int aura = -1;
	int[] auraIndex = null;
	List<AuraSkill> auraSkillList = new ArrayList<AuraSkill>();

	String playModeStr = "";

	if (player != null) {
		nameStr = player.getName();
		nextLevelExpStr = ""
				+ (player.getNextLevelExp() - player.getExp());
		currentLevel = "" + player.getLevel();
		int careerId = player.getCareer();
		Career career = cm.getCareer(careerId);

		if (player.getSex() == 0) {
			sexStr = "男";
		} else {
			sexStr = "女";
		}
	}
%>
<br>
备份数据库中角色<font color="red"><%=nameStr%></font><%="("+sexStr+")" %>的基本信息:
<br>
<form id='f51' name='f51' action="recover_player.jsp">
<input type='hidden' name='playerid' value='<%=playerId %>'>
<table class="tableclass1">

	<tr class="titlecolor">
		<td colspan="8">玩家的组队状态</td>
	</tr>
	<tr bgcolor="#FFFFFF" align="center">
		<td colspan="8">玩家(<%=p.getName() %>)<%=team == null?"为"+teamMarkStr+"状态":"为"+teamMarkStr %><%=(teamMark != 0 && sb.length() != 0) ? "<br>队长:"+captainName+"<br>全部团队成员:"+sb.toString() : "" %></td>
	</tr>
	<tr class="titlecolor">
		<td colspan="8">人物属性</td>
	</tr>
	<tr>
		<td class="tdsmallwidth">上次离线时间</td><td colspan="7"><%=DateUtil.formatDate(new java.util.Date(p.getQuitGameTime()),"yyyy-MM-dd HH:mm:ss") %></td>
	</tr>
	<tr>
		<td class="tdsmallwidth">力量</td><td colspan="7"><%=p.getStrength() %></td>
	</tr>
	<tr>
		<td>智力</td><td colspan="7"><%=p.getSpellpower() %></td>
	</tr>
	<tr>
		<td>耐力</td><td colspan="7"><%=p.getConstitution() %></td>
	</tr>
	<tr>
		<td>敏捷</td><td colspan="7"><%=p.getDexterity() %></td>
	</tr>
	<tr>
		<td>HP</td><td colspan="7"><%=p.getHp()%>/<%=p.getTotalHP() %></td>
	</tr>
	<tr>
		<td>MP</td><td colspan="7"><%=p.getMp()%>/<%=p.getTotalMP() %></td>
	</tr>
	<tr>
		<td>近战攻击强度</td><td colspan="7"><%=p.getMeleeAttackIntensity() %></td>
	</tr>
	<tr>
		<td>法术攻击强度</td><td colspan="7"><%=p.getSpellAttackIntensity() %></td>
	</tr>
	<tr>
		<td>物理伤害</td><td colspan="7"><%=p.getPhysicalDamageLowerLimit() %>/<%=p.getPhysicalDamageUpperLimit()%></td>
	</tr>
	<tr>
		<td>武器伤害</td><td colspan="7"><%=p.getWeaponDamageLowerLimit() %>/<%=p.getWeaponDamageUpperLimit()%></td>
	</tr>
	<tr>
		<td>攻击速度</td><td colspan="7"><%=p.getCommonAttackSpeed()/1000f %>秒</td>
	</tr>
	<tr>
		<td>暴击</td><td colspan="7"><%=p.getCriticalHit() %></td>
	</tr>
	<tr>
		<td>韧性</td><td colspan="7"><%=p.getToughness() %></td>
	</tr>
	<tr>
		<td>命中</td><td colspan="7"><%=p.getAttackRating() %></td>
	</tr>
	<tr>
		<td>闪避</td><td colspan="7"><%=p.getDodge() %></td>
	</tr>
	<tr>
		<td>移动速度</td><td colspan="7"><%=p.getSpeed() %>像素/秒</td>
	</tr>
	<tr>
		<td>怒气值</td><td colspan="7"><%=p.getXp() %>/<%=p.getTotalXp() %></td>
	</tr>
	<tr>
		<td>幸运值</td><td colspan="7"><%=p.getLuck() %></td>
	</tr>
	<tr>
		<td>物理防御</td><td colspan="7"><%=p.getDefence() %></td>
	</tr>
	<tr>
		<td>道术防御</td><td colspan="7"><%=p.getResistance() %></td>
	</tr>
	<tr>
		<td>等级</td><td colspan="7"><%=p.getLevel()+"级"%></td>
	</tr>
	<tr>
		<td>角色自身的元宝</td><td colspan="7"><%=p.getRmbyuanbao()%></td>
	</tr>
	<tr>
		<td>绑定元宝</td><td colspan="7"><%=p.getBindyuanbao()%></td>
	</tr>
	<tr>
		<td>游戏币</td><td colspan="7"><%=p.getMoney()%></td>
	</tr>
	<tr>
		<td>经验</td><td colspan="7"><%=p.getExp()%>/<%=p.getNextLevelExp() %></td>
	</tr>
	<tr>
		<td>门派</td><td colspan="7"><%=cm!= null&&cm.getCareer(p.getCareer())!= null? cm.getCareer(p.getCareer()).getName():"错误" %></td>
	</tr>
	<tr>
		<td>阵营</td><td colspan="7"><%=p.getPoliticalCampDesp(p.getPoliticalCamp()) %></td>
	</tr>
	<tr>
		<td>技能冷却时间的百分比</td><td colspan="7"><%=p.getCoolDownTimePercent()+"%" %></td>
	</tr>
	<tr>
		<td>技能僵直时间的百分比</td><td colspan="7"><%=p.getSetupTimePercent()+"%" %></td>
	</tr>
	<tr>
		<td>反噬伤害的百分比</td><td colspan="7"><%=p.getIronMaidenPercent()+"%" %></td>
	</tr>
	<tr>
		<td>攻击时吸取生命值的百分比</td><td colspan="7"><%=p.getHpStealPercent()+"%" %></td>
	</tr>
	<tr>
		<td>攻击时吸取魔法值的百分比</td><td colspan="7"><%=p.getMpStealPercent()+"%" %></td>
	</tr>
	<tr>
		<td>天赋增加的暴击百分比</td><td colspan="7"><%=p.getCriticalHitD()+"%" %></td>
	</tr>
	<tr>
		<td>天赋增加的命中百分比</td><td colspan="7"><%=p.getAttackRatingD()+"%" %></td>
	</tr>
	<tr>
		<td>天赋增加的闪避百分比</td><td colspan="7"><%=p.getDodgeD()+"%" %></td>
	</tr>
	<tr>
		<td>天赋增加的外功防御百分比</td><td colspan="7"><%=p.getDefenceD()+"%" %></td>
	</tr>
	<tr>
		<td>天赋增加的内功防御百分比</td><td colspan="7"><%=p.getResistanceD()+"%" %></td>
	</tr>
	<tr>
		<td>经验值获得的百分比</td><td colspan="7"><%=p.getExpPercent()+"%" %></td>
	</tr>
	<tr>
		<td>提高玩家整体输出伤害比例</td><td colspan="7"><%=p.getSkillDamageIncreaseRate()+"%" %></td>
	</tr>
	<tr>
		<td>降低玩家整体输出伤害比例</td><td colspan="7"><%=p.getSkillDamageDecreaseRate()+"%" %></td>
	</tr>
	<tr>
		<td>未分配的属性点</td><td colspan="7"><%=p.getUnallocatedPropertyPoint() %></td>
	</tr>
	<tr>
		<td>玩家模式</td><td colspan="7"><%=p.getPlayingMode() == 0 ? "练功" : "屠杀" %></td>
	</tr>
	<tr>
		<td>回城点</td><td colspan="7"><%=p.getHomeMapName()+"("+p.getHomeX()+","+ p.getHomeY()+")" %></td>
	</tr>
	<tr>
		<td>复活点</td><td colspan="7"><%=p.getResurrectionMapName()+"("+p.getResurrectionX()+","+ p.getResurrectionY()+")" %></td>
	</tr>
	<tr>
		<td>离线时间</td><td colspan="7"><%=p.getTotalOfflineTime()+"小时" %></td>
	</tr>
	<tr>
		<td>公会名称</td><td colspan="7"><%=p.getGangName() %></td>
	</tr>
	<tr>
		<td>公会中的职位</td><td colspan="7"><% String[] gangTitleStr = new String[]{"帮主","副帮主","精英","帮众"};%><%=(p.getGangTitle()>=0 && p.getGangTitle() < 4)?gangTitleStr[p.getGangTitle()]:p.getGangTitle() %></td>
	</tr>
	<tr>
		<td>保存的副本</td><td colspan="7">
		<%
		HashMap<String,DownCity> downCityProgress = p.getDownCityProgress();
		StringBuffer sbb = new StringBuffer();
		if(downCityProgress != null && downCityProgress.keySet() != null){
			for(String str : downCityProgress.keySet()){
				if(str != null){
					sbb.append("<a href=../downcitybyid.jsp?downcityid="+downCityProgress.get(str) != null ? downCityProgress.get(str).getId() : ""+">"+str+"</a> ");
				}
			}
		}
		%>
		<%=sbb.toString() %></td>
	</tr>
	<tr>
	<td>可装备武器</td>
	<td colspan="7">
	<%
	StringBuffer weaponsb = new StringBuffer();
	if(cm!= null&&cm.getCareer(p.getCareer())!= null){
		Career career = cm.getCareer(p.getCareer());
		int[] ints = career.getWeaponTypeLimit();
		if(ints != null){
			for(int index : ints){
				weaponsb.append(" "+Weapon.getWeaponTypeName((byte)index));
			}
		}
	}
	int usedSkillPoints = 0;
		%>
		<%=weaponsb.toString() %>
	</td>
	</tr>
<tr class="titlecolor">
<td class="tdwidth" nowrap>技能名称</td>
<td class="tdwidth" nowrap>技能类型</td>
<td>技能描述</td>
<td>需要点数</td>
<td>技能等级</td>
<td>伤害</td>
<td>公共CD</td>
<td>CD</td>
</tr>
<%
	if (player != null) {
		Career career = cm.getCareer(player.getCareer());
		//short skillIds[] = career.getSkillIds();
		CareerThread careerThreads[] = null;
		if(career != null){
			careerThreads = career.getCareerThreads();
			careerStr = career.getName();
		}
		if(careerThreads != null){
			for(int i = 0; i < careerThreads.length; i++){
				CareerThread careerThread = careerThreads[i];
				if(careerThread != null){
					Skill skills[] = careerThread.getSkills();
					if(skills != null){
						for(int j = 0; j < skills.length; j++){
							Skill skill = skills[j];
							if(skill != null){
								String skillType = "---";
								if(skill instanceof ActiveSkill){
									skillType = "主动技能";
								}else if(skill instanceof PassiveSkill){
									skillType = "被动技能";
								}else if(skill instanceof AuraSkill){
									skillType = "辅助光环技能";
								}
								boolean canLearn = career.isUpgradable(player,i,j);
					%>
						<tr bgcolor="#FFFFFF" align="center">
							<td nowrap><b><a href="../skillbyid.jsp?id=<%=skill != null ?skill.getId():"" %>&className=<%=skill != null ?skill.getClass().getName():"" %>"><%=skill != null ?skill.getName():"错误:技能为空"%></a></b></td>
							<td nowrap><b><%= skillType %></b></td>
							<td align="left"><%
								if(skill != null){
								String s = SkillInfoHelper.generate(player,skill);
								if(s != null){
									s = s.replaceAll("\\[/color\\]","</font>");
									s = s.replaceAll("\\[color=(.*)\\]","<font color='$1'>");
									s = s.replaceAll("\\[icon\\](.*)\\[/icon\\]","<img src='/icons/$1'>");
								}else{
									s = "无描述";
								}
								out.print("<pre>"+s+"</pre>"); }%></td>
								<td><b><%= skill != null ? skill.getNeedCareerThreadPoints():"" %></b></td>
							<td><b><%= skill != null ? player.getSkillLevel(skill.getId())+"/"+skill.getMaxLevel():"技能为空" %><%
									if(skill != null){
										usedSkillPoints = usedSkillPoints + player.getSkillLevel(skill.getId());
									}
								%></b></td>
							<td>
							<%if(skill instanceof ActiveSkill){
								%>
								<%=((ActiveSkill)skill).calDamageOrHpForFighter(player,player.getSkillLevel(skill.getId())) %>
								<%
							}else{
								%>
								0
								<%
							}
								%>
							</td>
							<td>
							<%if(skill instanceof ActiveSkill){
								%>
								<%=(((ActiveSkill)skill).getDuration1()+((ActiveSkill)skill).getDuration2())/1000f %>秒
								<%
							}else{
								%>
								--
								<%
							}
							%>
							</td>
							<td>
							<%if(skill instanceof ActiveSkill){
								%>
								<%=((ActiveSkill)skill).getDuration3()/1000f %>秒
								<%
							}else{
								%>
								--
								<%
							}
							%>
							</td>
						</tr>
					<%
							
							}
						}
					}
				}
			}
		}
	}
%>
	<tr>
		<td>已分配的技能点</td><td colspan="7"><%=usedSkillPoints %></td>
	</tr>
	<tr>
		<td>未分配的技能点</td><td colspan="7"><%=p.getUnallocatedSkillPoint() %></td>
	</tr>
</table>

<%}} %>
<br>
<input type="button" name=bt1 value=" 从备份数据库恢复 " onclick="recover(<%=playerId %>)">
</form>
</BODY></html>
