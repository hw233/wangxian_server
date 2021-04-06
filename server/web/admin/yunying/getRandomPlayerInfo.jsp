<%@page import="com.fy.engineserver.util.StringTool"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.PetPropsEntity"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.skill.Skill"%>
<%@page import="com.fy.engineserver.datasource.career.CareerManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Cell"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Knapsack"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.EquipmentEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Equipment"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.engineserver.economic.BillingCenter"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Random"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!
String [] equipment_color = {"#ffffff","#00ff00","#0000ff","#E706EA","#C905CC","#FDA700","#FD6B00"}; 
String [] article_color = {"#ffffff","#00ff00","#0000ff","#E706EA","#FDA700"}; 
%>
<%
	String minLevelS = request.getParameter("minLevel");
	String maxLevelS = request.getParameter("maxLevel");
	String playerNumS = request.getParameter("playerNum");
	String quitGameTime = request.getParameter("quitGameTime");
	String action = request.getParameter("action");
	String pwd = request.getParameter("pwd");
	
	String[] sysStarName = ArticleManager.starNames;
	String [] starCNname = new String [sysStarName.length];
	for (int i = 0; i < sysStarName.length;i++) {
		String star = sysStarName[i];
		Article article = ArticleManager.getInstance().getArticle(star);
		if (article != null) {
			starCNname[i] = article.getName_stat();
		}
	}
	
	long [] ids = null;
	List<Player> list = new ArrayList<Player>();
	int maxQuery = 10;
	long startTime = System.currentTimeMillis();
	if ("query".equals(action)) {
		if ("chayixia".equals(pwd)) {
			int minLevel = Integer.valueOf(minLevelS);
			int maxLevel = Integer.valueOf(maxLevelS);
			int playerNum = Integer.valueOf(playerNumS);
			if (minLevel <= maxLevel && minLevel > 0 && maxLevel > 0) {
				if (playerNum > 0 && playerNum <= maxQuery) {
					try {
						//player.getQuitGameTime()
						long quitGameTimeLong = TimeTool.formatter.varChar10.parse(quitGameTime);
						ids = ((GamePlayerManager)GamePlayerManager.getInstance()).em.queryIds(Player.class ," level >=" + minLevel + " and level <= " + maxLevel + " and quitGameTime > " + quitGameTimeLong );
						//out.print("查询ID耗时:"+(System.currentTimeMillis() - startTime)+"<BR/>");
						Set<Long> set = getRandomIds(ids,playerNum);
						for (Long playerId : set) {
							Player player = GamePlayerManager.getInstance().getPlayer(playerId);
							if (player != null) {
								list.add(player);
							}
						}
					//	out.print("查询角色耗时:"+(System.currentTimeMillis() - startTime)+"<BR/>");
					} catch (Exception e) {
						out.print(e.getMessage());
					}
					
				} else {
					out.print("<h1>角色数量输入错误或者数量太多[单次最多:"+maxQuery+"]</h1>");
				}
			} else {
				out.print("<h1>等级输入错误</h1>");
			}
		} else {
			out.print("<h1>密码输入错误</h1>");
		}
	} else {
		minLevelS = "";
		maxLevelS = "";
		playerNumS = "5";
	}
	String 级 = "";
	
	Platform platform = PlatformManager.getInstance().getPlatform();
	switch (platform) {
	case 台湾:
		级 = "級";
		break;
	case 韩国:
		级 = "레벨";
		break;
	case 官方:
	case 腾讯:
	case 马来:
		级 = "级";
		break;
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
#rl{writing-mode:tb-rl;}
</style>
<title>随机获取玩家信息</title>
</head>
<body>
<%
	Calendar calendar = Calendar.getInstance();
	calendar.add(Calendar.DAY_OF_MONTH, -7);
	String defaultDate =  TimeTool.formatter.varChar10.format(calendar.getTimeInMillis());%>
<h1><%="当前伺服器:" + GameConstants.getInstance().getServerName() + "["+PlatformManager.getInstance().getPlatform().toString()+"]" %></h1>
<form action="./getRandomPlayerInfo.jsp" method="post">
	角色级别段:<input name="minLevel" type="text" value="<%=minLevelS %>" size="8">~<input name="maxLevel" type="text" value="<%=maxLevelS %>" size="8">
	查询此日期之后有登录(默认7天前):<input name="quitGameTime" type="text" value="<%=(quitGameTime == null  ? defaultDate: quitGameTime) %>"  size="12">
	采样用户数(最多:<%=maxQuery %>):<input name="playerNum" type="text" value="<%=playerNumS %>"  size="8">
	查询&nbsp;密码:<input type="password" name="pwd">
	<input type="hidden" name="action" value="query">
	<input type="submit" value="查询">
</form>
<% if (list.size() >0) { 
	StringBuffer totalShow = new StringBuffer("");
	StringBuffer totalShowTitle = new StringBuffer("");
	%>
	<hr>
	<h2>当前服务器<%=minLevelS %>到<%=maxLevelS %>级用户共<%=ids.length %>个,采样数量:<%=playerNumS %>个,以下是角色信息:</h2>
	<hr>
	<table style="font-size: 12px;width: 100%;" border="1">
	<%
		String [] qilingNames = {"长生之灵","擎天之灵","琼浆之灵","金汤之灵","罡岚之灵","离火之灵","玄冰之灵","飓风之灵","惊雷之灵","御火之灵","御冰之灵","御风之灵","御雷之灵","强化长生之灵","强化擎天之灵","强化琼浆之灵","强化金汤之灵","强化罡岚之灵","强化离火之灵","强化玄冰之灵","强化飓风之灵","强化惊雷之灵","强化御火之灵","强化御冰之灵","强化御风之灵","强化御雷之灵"}; 
		PetManager pm = PetManager.getInstance();
		CareerManager cm = CareerManager.getInstance();
		for (Player player : list) {
			Pet bestPet = null;
			int bestPetLevel = -1;
			int bestPetCurrLevel = -1;
			int bestPetStar = -1;
			
			List<EquipmentEntity> equipments = new ArrayList<EquipmentEntity>();
			List<Pet> pets = new ArrayList<Pet>();
			EquipmentColumn ecs = player.getCurrSoul().getEc();
			for (int i = 0 ; i < ecs.getEquipmentIds().length; i++) {
				long equipmentId = ecs.getEquipmentIds()[i];
				if (equipmentId > 0) {
					EquipmentEntity ae = (EquipmentEntity)ArticleEntityManager.getInstance().getEntity(equipmentId);
					equipments.add(ae);
				}
			}
			Knapsack petKnapsack = player.getPetKnapsack();
			for (Cell cell : petKnapsack.getCells()) {
				if (cell != null && cell.getEntityId() > 0) {
					ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(cell.getEntityId());
					if(ae instanceof PetPropsEntity){
						PetPropsEntity pp = (PetPropsEntity)ae;
						long petId = pp.getPetId();
						Pet pet = pm.getPet(petId);
						pets.add(pet);
					}
				}
			}
		%>
			<tr>
				<td colspan="10"><font  style="color: red;text-align: center;"><b><center><%=player.getName() %> 的属性</center></b></font></td>
			</tr>
			<tr style="background-color: black;font-weight: bolder;font-size: 14px;color: white;">
				<td>角色名</td>
				<td>角色ID</td>
				<td>等级</td>
				<td>充值金额(分)</td>
				<td>vip等级</td>
				<td>银子数</td>
				<td>绑银数</td>
				<td>境界</td>
				<td>元神等级</td>
				<td>最后一次在线时间</td>
			</tr>
			
			<tr>
				<td><%=player.getName() %></td>
				<td><%=player.getId() %></td>
				<td><%=player.getLevel() %></td>
				<td><%=player.getRMB() %></td>
				<td><%=player.getVipLevel() %></td>
				<td><%=BillingCenter.得到带单位的银两(player.getSilver()) %></td>
				<td><%=BillingCenter.得到带单位的银两(player.getBindSilver()) %></td>
				<td><%=PlayerManager.classlevel[player.getClassLevel()] %></td>
				<td><%=player.getSoul(1) == null ? "未修炼" : player.getSoul(1).getGrade() %></td>
				<td><%=TimeTool.formatter.varChar19.format(player.getQuitGameTime()) %></td>
			</tr>
			<tr>
				<td colspan="10"><font  style="color: red;;"><b><center><%=player.getName() %> 的宠物栏</center></b></font></td>
			</tr>
			<tr>
				<td colspan="10">
					<table style="font-size: 10px;width: 100%;" border="1" >
						<tr>
							<td>名字</td>
							<td>携带等级</td>
							<td>等级</td>
							<td>稀有度</td>
							<td>品质</td>
							<td>是否出战</td>
							<td>星级</td>
							<td>技能列表</td>
						</tr>
						<%for (Pet p : pets){
							if (bestPet == null ){
								bestPet = p;
							}
							if (aBetterThanB(p,bestPet)) {
								bestPet = p;
							}
							long petPropsId = p.getPetPropsId();
							ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(petPropsId);
							int [] skillIds = p.getSkillIds();
							String petName = ArticleManager.getInstance().getArticle(ae.getArticleName()).getName_stat();
							
							StringBuffer skillInfo = new StringBuffer("技能数量:" + skillIds.length + ":");
							for (int i = 0; i < skillIds.length; i++) {
								Skill skill = cm.getSkillById(skillIds[i]);
								if (skill != null) {
									skillInfo.append(skill.getName()).append(",");
								}
							}
						%>
							<tr style="background-color: <%=PetManager.得到宠物成长品质颜色值(p.isIdentity(),p.getGrowthClass()).replace("0x","#")%>">
								<td title="<%=PetManager.得到宠物成长品质颜色值(p.isIdentity(),p.getGrowthClass())%>"><%=petName %></td>
								<td><%=p.getTrainLevel() %></td>
								<td><%=p.getLevel() %></td>
								<td><%=PetManager.宠物稀有度[p.getRarity()]  + "[" + p.getRarity() +"]" %></td>
								<td><%=PetManager.得到宠物成长品质名(p.isIdentity(),p.getGrowthClass())%></td>
								<td><%=player.getActivePetId() == p.getId() %></td>
								<td><%=p.getStarClass() %></td>
								<td><%=skillInfo.toString() %></td>
							</tr>
						<%} %>
					</table>
				</td>
			</tr>
			<tr>
				<td colspan="10"><font  style="color: red;text-align: center;"><b><center><%=player.getName() %> 的装备栏</center></b></font></td>
			</tr>
			<tr>
				<td colspan="10">
					<table style="font-size: 10px;width: 100%;" border="1">
						<tr>
							<td>装备名称</td>
							<td>装备颜色</td>
							<td>佩戴等级</td>
							<td>星级</td>
							<td>铭刻</td>
							<td>资质</td>
							<td>镶嵌宝石</td>
							<td>镶嵌器灵</td>
						</tr>
						<% 
						HashMap<String,Integer> star = new HashMap<String,Integer>();// 星级，数量
						HashMap<String,Integer> oldbaoshiMap = new HashMap<String,Integer>();// 宝石等级，数量
						HashMap<String,Integer> newbaoshiMap = new HashMap<String,Integer>();// 宝石等级，数量
						HashMap<String,Integer> colorMap = new HashMap<String,Integer>();// 宝石等级，数量
						int [] qilingNums = new int[qilingNames.length];
						for (int i = 0; i < qilingNums.length; i ++) {
							qilingNums[i] = 0;
						}
						int totalBaoshiCell = 0;//
						int usedBaoshiCell = 0;
						
						
						for (EquipmentEntity equipmentEntity : equipments) {
							Equipment equipment = (Equipment)ArticleManager.getInstance().getArticle(equipmentEntity.getArticleName());
							StringBuffer baoshiInfo = new StringBuffer();
							StringBuffer qilingInfo = new StringBuffer();
							for (int i = 0 ; i < equipmentEntity.getInlayArticleIds().length;i++) {
								totalBaoshiCell++;
								long baoshiId = equipmentEntity.getInlayArticleIds()[i];
								int baoshiColor = equipmentEntity.getInlayArticleColors()[i];
								ArticleEntity baoshi = ArticleEntityManager.getInstance().getEntity(baoshiId);
								
							
								if (baoshi != null) {
									usedBaoshiCell++;
									String baoshiName = baoshi.getArticleName();
									Article article = ArticleManager.getInstance().getArticle(baoshiName);
									boolean isNewBaoshi = isNewbaoshi(article.getName_stat());
									baoshiName = baoshiName.substring(baoshiName.indexOf("("));
									baoshiInfo.append("第").append(i+1).append("孔:颜色").append(baoshiColor).append(",").append(baoshi == null ?  "--" : article.getName_stat()).append("<BR/>");
									if (isNewBaoshi) {
										if (!newbaoshiMap.containsKey(baoshiName)) {
											newbaoshiMap.put(baoshiName,0);
										}
										newbaoshiMap.put(baoshiName,newbaoshiMap.get(baoshiName) + 1);
									} else {
										if (!oldbaoshiMap.containsKey(baoshiName)) {
											oldbaoshiMap.put(baoshiName,0);
										}
										oldbaoshiMap.put(baoshiName,oldbaoshiMap.get(baoshiName) + 1);
									}
								}
							}
							for (int i = 0; i < equipmentEntity.getInlayQiLingArticleIds().length;i++) {
								long qilingId = equipmentEntity.getInlayQiLingArticleIds()[i];
								int qilingColor = equipmentEntity.getInlayQiLingArticleTypes()[i];
								ArticleEntity qiling = ArticleEntityManager.getInstance().getEntity(qilingId);
								if(qiling != null) {
									String qilingNCname = ArticleManager.getInstance().getArticle(qiling.getArticleName()).getName_stat();
									for (int m = 0; m < qilingNames.length; m++) {
										if (qilingNCname.contains(qilingNames[m])) {
											qilingNums[m] += 1;
										}
									}
								}
								qilingInfo.append("第").append(i+1).append("孔:颜色").append(qilingColor).append(",").append(qiling == null ?  "--" : qiling.getArticleName()).append("<BR/>");
							}
							String stringColor = ArticleManager.getColorString(equipment,equipmentEntity.getColorType());
							if (!colorMap.containsKey(stringColor)) {
								colorMap.put(stringColor,0);
							}
							colorMap.put(stringColor,colorMap.get(stringColor) + 1);
							
							String stringStar = ArticleManager.getInstance().得到装备能装的星(equipmentEntity);
							if (!star.containsKey(stringStar)) {
								star.put(stringStar,0);
							}
							star.put(stringStar,star.get(stringStar) + 1);
							String equipmentName = ArticleManager.getInstance().getArticle(equipmentEntity.getArticleName()).getName_stat();
							String starName = "";
							try {
								starName = ArticleManager.getInstance().getArticle(ArticleManager.getInstance().得到装备能装的星(equipmentEntity)).getName_stat();
							} catch(Exception e) {}
						%>
						<tr>
							<td style="color: <%=equipment_color[equipmentEntity.getColorType()]%>" title="<%=equipmentEntity.getCommonInfoShow(player)%>"><b><%=equipmentName %></b></td>
							<td><%=ArticleManager.getColorString(equipment,equipmentEntity.getColorType()) %></td>
							<td><%=equipment.getPlayerLevelLimit() %></td>
							<td><%=starName %></td>
							<td><%=equipmentEntity.isInscriptionFlag() ? "已铭刻" : "未铭刻" %></td>
							<td><%=ArticleManager.getEndowmentsString(equipmentEntity.getEndowments()) %></td>
							<td><%=baoshiInfo.toString() %></td>
							<td><%=qilingInfo.toString() %></td>
						</tr>
						<%
						}
						%>
					</table>
				</td>
			</tr>
			
			<tr>
				<td colspan="8">
					<table border="1">
						<tr>
							<td class="rl">服务器</td>
							<td>角色名</td>
							<td class="rl">本尊等级</td>
							<td class="rl">元神等级</td>
							<td class="rl">VIP等级</td>
							<td class="rl">充值金额[分]</td>
							<% for (String s : starCNname) {
								out.print("<td  class='rl'>"+s+"</td>");
							}
								for (int i = 0 ; i < ArticleManager.color_equipment_Strings.length; i++) {
									String color = ArticleManager.color_equipment_Strings[i];
									out.print("<td  class='rl'>装备颜色" + "[" + i + "]" + color + "</td>");
								}
							%>
							<td class="rl">已镶嵌宝石</td>
							<td class="rl">总孔镶嵌宝石</td>
							<%
								for (int i = 1; i <= 9; i++) {
									out.print("<td bgcolor='#80FF00' class='rl'>"+i+"级宝石[老]</td>");
								}
							%>
							<%
								for (int i = 4; i <= 9; i++) {
									out.print("<td bgcolor='#FF0000' class='rl'>"+i+"级宝石[新]</td>");
								}
							%>
							<td class="rl">宠物名称 </td>
							<td class="rl">技能数量</td>
							<td class="rl">携带等级</td>
							<td class="rl">等级</td>
							<td class="rl">稀有度</td>
							<td class="rl">品质</td>
							<td class="rl">星级</td>
							<%
								for (String qilingName : qilingNames) {
									out.print("<td bgcolor='#8080FF' class='rl'>");
									out.print(qilingName);
									out.print("</td>");
								}
							%>
						</tr>
						
						<tr>
							<%
								totalShow.append("<tr>");
								totalShow.append("<td>");
								totalShow.append(GameConstants.getInstance().getServerName());
								totalShow.append("</td>");
								totalShow.append("<td>");
								totalShow.append(player.getName());
								totalShow.append("</td>");
								totalShow.append("<td>");
								totalShow.append(player.getSoul(0).getGrade());
								totalShow.append("</td>");
								totalShow.append("<td>");
								totalShow.append(player.getSoul(1) == null ? "--" :player.getSoul(1).getGrade());
								totalShow.append("</td>");
								totalShow.append("<td>");
								totalShow.append(player.getVipLevel());
								totalShow.append("</td>");
								totalShow.append("<td>");
								totalShow.append(player.getRMB());
								totalShow.append("</td>");
							%>
							<td><%=GameConstants.getInstance().getServerName() %></td>
							<td><%=player.getName() %></td>
							<td><%=player.getSoul(0).getGrade() %></td>
							<td><%=player.getSoul(1) == null ? "--" :player.getSoul(1).getGrade() %></td>
							<td><%=player.getVipLevel() %></td>
							<td><%=player.getRMB() %></td>
							<%
								for (String s : ArticleManager.starNames) {
									out.print("<td>" + (star.get(s) == null ? " " : star.get(s)) +"</td>");
									totalShow.append("<td>" + (star.get(s) == null ? " " : star.get(s)) +"</td>");
								}
								for (int i = 0; i < ArticleManager.color_equipment_Strings.length; i++) {
									String color = ArticleManager.color_equipment_Strings[i];
									out.print("<td>" + (colorMap.get(color) == null ? " " : colorMap.get(color)) + "</td>");
									totalShow.append("<td>" + (colorMap.get(color) == null ? " " : colorMap.get(color)) + "</td>");
								}
							%>
							<td><%=usedBaoshiCell %></td>
							<td><%=totalBaoshiCell %></td>
							<%
							totalShow.append("<td>");
							totalShow.append(usedBaoshiCell);
							totalShow.append("</td>");
							totalShow.append("<td>");
							totalShow.append(totalBaoshiCell);
							totalShow.append("</td>");
							String gread= "";
							if (PlatformManager.getInstance().isPlatformOf(Platform.台湾)) {
								gread = "級";
							} else if (PlatformManager.getInstance().isPlatformOf(Platform.官方,Platform.腾讯,Platform.马来)) {
								gread = "级";
							} else if (PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
								gread = "레벨";
							}
							for (int i = 1; i <= 9; i++) {
								String starName = "("+i+gread+")";
								out.print("<td bgcolor='#80FF00'>" + (oldbaoshiMap.get(starName) == null ? " " : oldbaoshiMap.get(starName)) + "</td>");
								totalShow.append("<td bgcolor='#80FF00'>" + (oldbaoshiMap.get(starName) == null ? " " : oldbaoshiMap.get(starName)) + "</td>");
							}
							for (int i = 4; i <= 9; i++) {
								String starName = "("+i+gread+")";
								out.print("<td bgcolor='#FF0000'>" + (newbaoshiMap.get(starName) == null ? " " : newbaoshiMap.get(starName)) + "</td>");
								totalShow.append("<td bgcolor='#FF0000'>" + (newbaoshiMap.get(starName) == null ? " " : newbaoshiMap.get(starName)) + "</td>");
							}
							%>
							<%if (bestPet != null) { 
								int [] skillIds = bestPet.getSkillIds();
								int skillNum = 0;
								for (int i = 0; i < skillIds.length ;i++) {
									if (skillIds[i] > 0) {
										skillNum ++;
									}
								}
								Article article = ArticleManager.getInstance().getArticle(bestPet.getCategory());
								
							%>
								<td><%=article.getName_stat() %></td>
								<td><%=skillNum %></td>
								<td><%=bestPet.getTrainLevel() %></td>
								<td><%=bestPet.getLevel() %></td>
								<td><%=PetManager.宠物稀有度[bestPet.getRarity()] + "["+bestPet.getRarity()+"]" %></td>
								<td><%=PetManager.得到宠物成长品质名(bestPet.isIdentity(),bestPet.getGrowthClass()) %></td>
								<td><%=bestPet.getStarClass() %></td>
							
							<%
								for (int qilingNum: qilingNums) {
									out.print("<td  bgcolor='#8080FF'>");
									out.print(qilingNum);
									out.print("</td>");
								}
							%>	
							<%
							
								totalShow.append("<td>");
								totalShow.append(article.getName_stat());
								totalShow.append("</td>");
								totalShow.append("<td>");
								totalShow.append(skillNum);
								totalShow.append("</td>");
								totalShow.append("<td>");
								totalShow.append(bestPet.getTrainLevel());
								totalShow.append("</td>");
								totalShow.append("<td>");
								totalShow.append(bestPet.getLevel());
								totalShow.append("</td>");
								totalShow.append("<td>");
								totalShow.append(PetManager.宠物稀有度[bestPet.getRarity()] +"[" + bestPet.getRarity() + "]");
								totalShow.append("</td>");
								totalShow.append("<td>");
								totalShow.append(PetManager.得到宠物成长品质名(bestPet.isIdentity(),bestPet.getGrowthClass()) );
								totalShow.append("</td>");
								totalShow.append("<td>");
								totalShow.append(bestPet.getStarClass());
								totalShow.append("</td>");
							} else {
								totalShow.append("<td>");
								totalShow.append("</td>");
								totalShow.append("<td>");
								totalShow.append("</td>");
								totalShow.append("<td>");
								totalShow.append("</td>");
								totalShow.append("<td>");
								totalShow.append("</td>");
								totalShow.append("<td>");
								totalShow.append("</td>");
								totalShow.append("<td>");
								totalShow.append("</td>");
								totalShow.append("<td>");
								totalShow.append("</td>");								
							} 
							for (int qilingNum: qilingNums) {
								totalShow.append("<td  bgcolor='#8080FF'>");
								totalShow.append(qilingNum);
								totalShow.append("</td>");
							}
							totalShow.append("</tr>");
							
							%>
						</tr>			
					</table>
				</td>
			</tr>
			<tr>
				<td><%=GameConstants.getInstance().getServerName() %></td>
			</tr>
			<tr style="background-color: black;color: white;font-weight: bold;">
				<td>角色名</td>
				<td><%=player.getName() %></td>
				<td>本尊等级/元神等级</td>
				<td><%=player.getLevel() + "/" + (player.getSoul(1) == null ? "未修炼" : player.getSoul(1).getGrade()) %></td>
				<td>VIP</td>
				<td><%=player.getVipLevel() %></td>
			</tr>
			<tr>
				<td>装备星级信息</td>
				<td colspan="7">
					<%
						for (Iterator<String> itor = star.keySet().iterator();itor.hasNext();) {
							String starName = itor.next();
							out.print(starName + ":" + star.get(starName) + ",");
						}
					%>
				</td>
			</tr>
			<tr>
				<td>装备颜色信息</td>
				<td colspan="7">
					<%
						for (Iterator<String> itor = colorMap.keySet().iterator();itor.hasNext();) {
							String starName = itor.next();
							out.print(starName + ":" + colorMap.get(starName) + ",");
						}
					%>
				</td>
			</tr>
			<tr>
				<td>装备宝石信息</td>
				<td colspan="7"><%="已镶嵌/总孔数:" + usedBaoshiCell + "/" + totalBaoshiCell %><br/>
					<%
						int oldNum = 0;
						for (Iterator<String> itor = oldbaoshiMap.keySet().iterator();itor.hasNext();) {
							String starName = itor.next();
							out.print(starName + ":" + oldbaoshiMap.get(starName) + ",");
							oldNum += oldbaoshiMap.get(starName);
						}
						out.print("<font color='red'>老宝石总计:" + oldNum + "</font>");
						out.print("<HR>");
						int newNum = 0;
						for (Iterator<String> itor = newbaoshiMap.keySet().iterator();itor.hasNext();) {
							String starName = itor.next();
							out.print(starName + ":" + newbaoshiMap.get(starName) + ",");
							newNum += newbaoshiMap.get(starName);
						}
						out.print("<font color='red'>新宝石总计:" + newNum + "</font>");
					%>
				</td>
			</tr>
			<tr>
				<td>最优宠物信息</td>
				<% if (bestPet != null) {%>
					<td>携带等级:<%=bestPet.getTrainLevel() %></td>
					<td>等级:<%=bestPet.getLevel() %></td>
					<td>稀有度:<%=PetManager.宠物稀有度[bestPet.getRarity()] + "[" + bestPet.getRarity() + "]" %></td>
					<td>品质:<%=PetManager.得到宠物成长品质名(bestPet.isIdentity(),bestPet.getGrowthClass()) %></td>
					<td>星级:<%=bestPet.getStarClass() %></td>
				<%} %>
			</tr>
			<tr>
				<td colspan="10"><font  style="color: red;text-align: center;"><b><center><%=player.getName() %> 的背包,[物品](数量)[颜色][绑:■ 不绑:<font color=black>■</font>]</center></b></font></td>
			</tr>
			<tr>
				<td colspan="10">
					<table style="font-size: 10px;width: 100%;" border="1">
						<%
							int oneRowShowNum = 7;//一行显示几个
							int index = 0;
							for (Cell cell : player.getKnapsack_common().getCells()) {
								%>
								<% if (index % oneRowShowNum == 0) {
									out.print("<tr>");
								}
									
									String color = "";
									ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(cell.getEntityId());
									Article article = null;
									if (ae != null) {
										article = ArticleManager.getInstance().getArticle(ae.getArticleName());
										if (article != null) {
											if (article instanceof Equipment) {
												color = equipment_color[ae.getColorType()];
											} else {
												color = article_color[ae.getColorType()];
											}
										}
									}
									if (ae != null && ae.getColorType() == 0) {
										color = "#000000";
									}
									out.print("<td style='color:" + ((cell.getEntityId()>0 && ae == null) ? "red" : color) +"'>");
									if (cell.getEntityId() <= 0) {
										out.print("--");
									} else {
										if (ae == null) {
											out.print(cell.getEntityId() + "不存在");
										} else {
											out.print("<B>"+ article.getName_stat() + "</B>" + "["+cell.getCount()+"]["+ArticleManager.getColorString(article,ae.getColorType())+"]"+ (ae.isBinded() ? "<font color=red>■</font>" : "<font color=black>■</font>"));
										}
									}
									out.print("</td>");
								if (index % oneRowShowNum == 6) {
									out.print("</tr>");
								}
								index++;
							}%>
					</table>
				</td>
			</tr>
			<tr>
				<td colspan="10"><font  style="color: red;text-align: center;"><b><center><%=player.getName() %> 的防爆包,[物品](数量)[颜色][绑:■ 不绑:<font color=black>■</font>]</center></b></font></td>
			</tr>
			<tr>
				<td colspan="10">
					<table style="font-size: 10px;width: 100%;" border="1">
						<%
							index = 0;
						if (player.getKnapsack_fangbao() != null) {
							for (Cell cell : player.getKnapsack_fangbao().getCells()) {
								%>
								<% if (index % oneRowShowNum == 0) {
									out.print("<tr>");
								} 
									
									String color = "";
									ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(cell.getEntityId());
									Article article = null;
									if (ae != null) {
										article = ArticleManager.getInstance().getArticle(ae.getArticleName());
										if (article != null) {
											if (article instanceof Equipment) {
												color = equipment_color[ae.getColorType()];
											} else {
												color = article_color[ae.getColorType()];
											}
										}
									}
									if (ae != null && ae.getColorType() == 0) {
										color = "#000000";
									}
									out.print("<td style='color:" + ((cell.getEntityId()>0 && ae == null) ? "red" : color) +"'>");
									if (cell.getEntityId() <= 0) {
										out.print("--");
									} else {
										if (ae == null) {
											out.print(cell.getEntityId() + "不存在");
										} else {
											out.print("<B>"+ article.getName_stat() + "</B>" + "["+cell.getCount()+"]["+ArticleManager.getColorString(article,ae.getColorType())+"]"+ (ae.isBinded() ? "<font color=red>■</font>" : "<font color=black>■</font>"));
										}
									}
									out.print("</td>");
							if (index % oneRowShowNum == 6) {
								out.print("</tr>");
							}
								index++;
							}
						}
							%>
					</table>
				</td>
			</tr>
			<tr>
				<td colspan="10"><font  style="color: red;text-align: center;"><b><center><%=player.getName() %> 的仓库,[物品](数量)[颜色][绑:■ 不绑:<font color=black>■</font>]</center></b></font></td>
			</tr>
			<tr>
				<td colspan="10">
					<table style="font-size: 10px;width: 100%;" border="1">
						<%
							index = 0;
						if ( player.getKnapsacks_cangku() != null) {
							for (Cell cell : player.getKnapsacks_cangku().getCells()) {
								if (index % oneRowShowNum == 0) {
									out.print("<tr>");
								}
								String color = "";
								ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(cell.getEntityId());
								Article article = null;
								if (ae != null) {
									article = ArticleManager.getInstance().getArticle(ae.getArticleName());
									if (article != null) {
										if (article instanceof Equipment) {
											color = equipment_color[ae.getColorType()];
										} else {
											color = article_color[ae.getColorType()];
										}
									}
								}
								if (ae != null && ae.getColorType() == 0) {
									color = "#000000";
								}
								out.print("<td style='color:" + ((cell.getEntityId()>0 && ae == null) ? "red" : color) +"'>");
								if (cell.getEntityId() <= 0) {
									out.print("--");
								} else {
									if (ae == null) {
										out.print(cell.getEntityId() + "不存在");
									} else {
										out.print("<B>"+ article.getName_stat()  + "</B>" + "["+cell.getCount()+"]["+ArticleManager.getColorString(article,ae.getColorType())+"]"+ (ae.isBinded() ? "<font color=red>■</font>" : "<font color=black>■</font>"));
									}
								}
								out.print("</td>");
								if (index % oneRowShowNum == 6) {
									out.print("</tr>");
								} 
								index++;
							}
						}
							%>
					</table>
				</td>
			</tr>
	
	<%
	}
		%>
	<tr>
		<td colspan="8">
			<table border="1">
				<tr>
					<td class="rl">服务器</td>
					<td>角色名</td>
					<td class="rl">本尊等级</td>
					<td class="rl">元神等级</td>
					<td class="rl">VIP等级</td>
					<td class="rl">充值金额[分]</td>
					<% for (String s : starCNname) {
						out.print("<td  class='rl'>"+s+"</td>");
					}
						for (int i = 0; i < ArticleManager.color_equipment_Strings.length;i++) {
							String color = ArticleManager.color_equipment_Strings[i];
							out.print("<td  class='rl'>装备颜色:[" + i + "]" + color + "</td>");
						}
					%>
					<td class="rl">已镶嵌宝石</td>
					<td class="rl">总孔镶嵌宝石</td>
					<%
						for (int i = 1; i <= 9; i++) {
							out.print("<td bgcolor='#80FF00' class='rl'>"+i+"级宝石[老]</td>");
						}
					%>
					<%
						for (int i = 4; i <= 9; i++) {
							out.print("<td bgcolor='#FF0000' class='rl'>"+i+"级宝石[新]</td>");
						}
					%>
					<td class="rl">宠物名称 </td>
					<td class="rl">技能数量</td>
					<td class="rl">携带等级</td>
					<td class="rl">等级</td>
					<td class="rl">稀有度</td>
					<td class="rl">品质</td>
					<td class="rl">星级</td>
					<%
						for (String qilingName : qilingNames) {
							out.print("<td  bgcolor='#8080FF' class='rl'>");
							out.print(qilingName);
							out.print("</td>");
						}
					%>
					</tr>
				<%=totalShow.toString() %>
			</table>
		</td>
	</tr>
		
		
		<%
	} %>
	</table>
	
</body>
</html>
<%!
	Set<Long> getRandomIds(long [] ids ,int num) {
		Set<Long> set = new HashSet<Long>();
		if (num >= ids.length) {
			for (int i = 0; i < ids.length;i++) {
				set.add(ids[i]);
			}
		} else {
			Random random = new Random();
			while (set.size() < num) {
				set.add(ids[random.nextInt(ids.length - 1)]);
			}
		}
		return set;
}
 boolean aBetterThanB (Pet a ,Pet b){
	 if (a.getTrainLevel() > b.getTrainLevel()) {
		 return true;
	 } else if (a.getTrainLevel() == b.getTrainLevel()) {
		 if (a.getStarClass() > b.getStarClass()) {
			 return true;
		 } else if (a.getStarClass() == b.getStarClass()) {
			 if (a.getLevel() >= b.getLevel()) {
				 return true;
			 } else {
				 return false;
			 }
		 } else {
			 return false;
		 }
	 } else {
		 return false;
	 }
 }
 String [] newBaoshiName = {"宝石焚焱","宝石焚天","宝石焚荒","宝石焚融","宝石寒冰","宝石寒霜","宝石寒雨","宝石寒颤","宝石雷霆","宝石雷击","宝石雷鸣","宝石雷诸","宝石疾风","宝石朔风","宝石狂风","宝石暴风"};
 boolean isNewbaoshi(String baoshiCNname){
	 for (String sysName : newBaoshiName) {
		 if (baoshiCNname.contains(sysName)) {
			 return true;
		 }
	 }
	return false; 
 } 
%>