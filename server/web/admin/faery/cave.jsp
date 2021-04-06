<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="./inc.jsp"%>
<%
	long faeryId = Long.valueOf(ParamUtils.getParameter(request, "faeryId"));
	int index = ParamUtils.getIntParameter(request, "index", -1);
	int country = ParamUtils.getIntParameter(request, "country", -1);
	if (index == -1 || faeryId <= 0) {
		return;
	}
	String msg = "";
	Faery faery = FaeryManager.getInstance().getFaery(faeryId);
	if (faery == null) {
		msg = "仙府不存在";
		response.sendRedirect("admin/faery/faeryList.jsp?msg=" + msg);
		return;
	}
	Cave cave = faery.getCave(index);
	if (cave == null) {
		msg = "仙府不存在";
		response.sendRedirect("admin/faery/faeryList.jsp?msg=" + msg);
		return;
	}
	out.print("剩余资源 " + cave.getCurrRes().toString() + "<BR/>");
	out.print("资源上限 " + cave.getCurrMaxResource().toString() + "<BR/>");
	out.print("食物存储等级 " + cave.getStorehouse().getFoodLevel() + "<BR/>");
	out.print("木材存储等级 " + cave.getStorehouse().getWoodLevel() + "<BR/>");
	out.print("石料存储等级 " + cave.getStorehouse().getStoneLevel() + "<BR/>");
	out.print("主人最后一次访问时间: " + sdf.format(cave.getOwnerLastVisitTime()) + FaeryConfig.CAVE_STATUS_ARR[cave.getStatus()] + "<BR/>");
	if (cave.getStatus() == FaeryConfig.CAVE_STATUS_KHATAM) {//在封印状态
		out.print("封印时间:" + sdf.format(cave.getKhatamTime()));
	}
	String modify = request.getParameter("modify");

	//修改越界的资源等级
	if ("storelevel".equals(modify)) {
		boolean changed = false;
		if (cave.getStorehouse().getFoodLevel() > 20) {
			cave.getStorehouse().setFoodLevel(20);
			changed = true;
		}
		if (cave.getStorehouse().getWoodLevel() > 20) {
			cave.getStorehouse().setWoodLevel(20);
			changed = true;
		}
		if (cave.getStorehouse().getStoneLevel() > 20) {
			cave.getStorehouse().setStoneLevel(20);
			changed = true;
		}
		if (changed) {
			cave.notifyFieldChange(cave.getStorehouse().getType());
		}
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.sprite.npc.CaveNPC"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.newtask.actions.TaskActionOfCaveBuild"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.core.res.ResourceManager"%>
<%@page import="com.fy.engineserver.achievement.RecordAction"%>
<%@page import="com.fy.engineserver.achievement.AchievementManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="./css/main.css">
<title><%=faery.getName()%><%=index%></title>
</head>
<body>
	<form action="" name="f1" method="post">
		<table border="1">
			<tr class="head">
				<td>类型</td>
				<td>等级</td>
				<td>状态</td>
				<td>位置</td>
				<td>升级开始时间</td>
				<td>avata</td>
			</tr>
			<tr>
				<td><%=cave.getMainBuilding().getNpc().getName()%><%=cave.getMainBuilding().getNpc().getId()%></td>
				<td><%=cave.getMainBuilding().getGrade()%></td>
				<td><%=FaeryConfig.CAVE_BUILDING_STATUS_ARR[cave.getMainBuilding().getStatus()]%></td>
				<td>[<%=cave.getMainBuilding().getNpc().getX() + "," + cave.getMainBuilding().getNpc().getY()%>]</td>
				<td><%=sdf.format(new Date(cave.getMainBuilding().getLvUpStartTime()))%></td>
				<td><%=cave.getMainBuilding().getNpc().getAvataRace() + "," + cave.getMainBuilding().getNpc().getAvataSex()%></td>
			</tr>
			<tr>
				<td><%=cave.getStorehouse().getNpc().getName()%><%=cave.getStorehouse().getNpc().getId()%></td>
				<td><%=cave.getStorehouse().getGrade()%></td>
				<td><%=FaeryConfig.CAVE_BUILDING_STATUS_ARR[cave.getStorehouse().getStatus()]%></td>
				<td>[<%=cave.getStorehouse().getNpc().getX() + "," + cave.getStorehouse().getNpc().getY()%>]</td>
				<td><%=sdf.format(new Date(cave.getStorehouse().getLvUpStartTime()))%></td>
				<td><%=cave.getStorehouse().getNpc().getAvataRace() + "," + cave.getStorehouse().getNpc().getAvataSex()%></td>
			</tr>
			<tr>
				<td><%=cave.getPethouse().getNpc().getName()%><%=cave.getPethouse().getNpc().getId()%></td>
				<td><%=cave.getPethouse().getGrade()%></td>
				<td><%=FaeryConfig.CAVE_BUILDING_STATUS_ARR[cave.getPethouse().getStatus()]%></td>
				<td>[<%=cave.getPethouse().getNpc().getX() + "," + cave.getPethouse().getNpc().getY()%>]</td>
				<td><%=sdf.format(new Date(cave.getPethouse().getLvUpStartTime()))%></td>
				<td><%=cave.getPethouse().getNpc().getAvataRace() + "," + cave.getPethouse().getNpc().getAvataSex()%></td>
			</tr>
			<tr>
				<td><%=cave.getFence().getNpc().getName()%><%=cave.getFence().getNpc().getId()%></td>
				<td><%=cave.getFence().getGrade()%></td>
				<td><%=FaeryConfig.CAVE_BUILDING_STATUS_ARR[cave.getFence().getStatus()]%></td>
				<td>[<%=cave.getFence().getNpc().getX() + "," + cave.getFence().getNpc().getY()%>]</td>
				<td><%=sdf.format(new Date(cave.getFence().getLvUpStartTime()))%></td>
				<td><%=cave.getFence().getNpc().getAvataRace() + "," + cave.getFence().getNpc().getAvataSex()%></td>
			</tr>
			<tr>
				<td><%=cave.getDoorplate().getNpc().getName()%><%=cave.getDoorplate().getNpc().getId()%></td>
				<td><%=cave.getDoorplate().getGrade()%></td>
				<td><%=FaeryConfig.CAVE_BUILDING_STATUS_ARR[cave.getDoorplate().getStatus()]%></td>
				<td>[<%=cave.getDoorplate().getNpc().getX() + "," + cave.getDoorplate().getNpc().getY()%>]</td>
				<td><%=sdf.format(new Date(cave.getDoorplate().getLvUpStartTime()))%></td>
				<td><%=cave.getDoorplate().getNpc().getAvataRace() + "," + cave.getDoorplate().getNpc().getAvataSex()%></td>
			</tr>
			<%
				for (CaveField caveField : cave.getFields()) {
					CaveSchedule cs = caveField.getScheduleByOptType(3);
			%>
			<tr>
				<td><%=caveField.getNpc().getName()%><%=caveField.getNpc().getId()%></td>
				<td><%=caveField.getGrade()%></td>
				<td><%=FaeryConfig.CAVE_BUILDING_STATUS_ARR[caveField.getStatus()]%></td>
				<td>[<%=caveField.getNpc().getX() + "," + caveField.getNpc().getY()%>]</td>
				<td><%=sdf.format(new Date(caveField.getLvUpStartTime()))%></td>
				<td><%="assart:" + caveField.getAssartStatus() + "/building:" + caveField.inBuilding() + "/cs:" + (cs == null ? "null" : cs.hasDone())%></td>
			</tr>
			<%
				}
			%>
		</table>
		<span> 挂机拦(别人来挂的):<BR />
			<%
				for (PetHookInfo pi : cave.getPethouse().getHookInfos()) {
					if (pi != null) {
						out.print(pi.toString() + ",," + pi.getPetId() + "<BR/>");
					}
				}
			%> </span><br />
		<hr />
		宠物仓库(自己的宠物):<BR />
		<%
			for (Long pid : cave.getPethouse().getStorePets()) {
				out.print(pid + ",");
			}
		%>
		</span><br />
	</form>
	<%
		for (Iterator<Long> iterator = cave.getBuildings().keySet().iterator(); iterator.hasNext();) {
			long id = iterator.next();
			out.print("ID=" + id + ",BUILDING:" + cave.getBuildings().get(id) + ">>>" + cave.getBuildings().get(id).getNpc().getName() + FaeryConfig.CAVE_BUILDING_NAMES[cave.getBuildings().get(id).getType()] + "<BR/>");
			if ("plants".equals(modify)) {
				CaveBuilding cBuilding = cave.getBuildings().get(id);
				if (cBuilding != null && cBuilding instanceof CaveField) {
					CaveField caveField = (CaveField) cBuilding;
					PlantStatus plantStatus = caveField.getPlantStatus();
					if (plantStatus != null) {
						if (plantStatus.getLeftOutput() <= 0) {
							caveField.setPlantStatus(null);
							cave.getBuildings().remove(caveField.getNpc().getId());
							cave.getFaery().getGame().removeSprite(caveField.getNpc());
							caveField.initNpc(cave);
							out.print("已修复收获完果实土地仍不空闲的问题</br>");
						}
						caveField.modifyName();
						cave.notifyFieldChange(caveField.getType());
					}
				}
			}
			if ("xiaoyaoju".equals(modify)) {
				out.print("逍遥居是否在建设中：" + cave.getMainBuilding().inBuilding());
				CaveMainBuilding cmb = cave.getMainBuilding();
				if (cmb.inBuilding()) {
					CaveSchedule caveSchedule = cmb.getScheduleByOptType(Cave.OPTION_LEVEL_UP);
					out.print("开始时间：" + cmb.getLvUpStartTime() + "caveSchedule:" + caveSchedule);
					if (caveSchedule != null) {
						out.print("逍遥居hasDone:" + caveSchedule.hasDone());
					}
					cmb.setGrade(cmb.getGrade() + 1);
					((CaveNPC) cmb.getNpc()).setGrade(cmb.getGrade());
					((CaveNPC) cmb.getNpc()).setInBuilding(false);
					ResourceManager.getInstance().setAvata(((CaveNPC) cmb.getNpc()));
					cmb.removeScheduleForDone(cave.OPTION_LEVEL_UP);
					cmb.setStatus(cave.CAVE_BUILDING_STATUS_SERVICE);

					Player owner = GamePlayerManager.getInstance().getPlayer(cave.getOwnerId());
					owner.dealWithTaskAction(TaskActionOfCaveBuild.createTaskAction(cmb.getType(), cmb.getGrade()));
					if (GamePlayerManager.getInstance().isOnline(cave.getOwnerId())) {
						owner.sendNotice(Translate.translateString(Translate.text_cave_064, new String[][] { { Translate.STRING_1, cmb.getNpc().getName() }, { Translate.STRING_2, String.valueOf(cmb.getGrade()) } }));
					}
					if (cave.logger.isWarnEnabled()) {
						cave.logger.warn("[建筑升级完成] [{}] [升级后等级:{}] [主人:{}] [建筑ID:{}]", new Object[] { cmb.getNpc().getName(), cmb.getGrade(), cave.getOwnerId(), cmb.getNpc().getId() });
					}

					cave.notifyFieldChange(cmb.getType());
					{
						// 主建筑级别统计
						AchievementManager.getInstance().record(owner, RecordAction.仙府主建筑达到等级, cmb.getGrade());
					}
				}
			}
			if ("wanbaoku".equals(modify)) {
				out.print("万宝库是否在建设中：" + cave.getStorehouse().inBuilding());
				CaveStorehouse csh = cave.getStorehouse();
				if (cave.getStorehouse().inBuilding()) {
					CaveSchedule caveSchedule = cave.getStorehouse().getScheduleByOptType(Cave.OPTION_LEVEL_UP);
					out.print("开始时间：" + cave.getStorehouse().getLvUpStartTime() + "caveSchedule:" + caveSchedule);
					if (caveSchedule != null) {
						out.print("万宝库hasDone:" + caveSchedule.hasDone());
					}
					csh.setGrade(csh.getGrade() + 1);
					((CaveNPC) csh.getNpc()).setGrade(csh.getGrade());
					((CaveNPC) csh.getNpc()).setInBuilding(false);
					ResourceManager.getInstance().setAvata(((CaveNPC) csh.getNpc()));
					csh.removeScheduleForDone(cave.OPTION_LEVEL_UP);
					csh.setStatus(cave.CAVE_BUILDING_STATUS_SERVICE);

					Player owner = GamePlayerManager.getInstance().getPlayer(cave.getOwnerId());
					owner.dealWithTaskAction(TaskActionOfCaveBuild.createTaskAction(csh.getType(), csh.getGrade()));
					if (GamePlayerManager.getInstance().isOnline(cave.getOwnerId())) {
						owner.sendNotice(Translate.translateString(Translate.text_cave_064, new String[][] { { Translate.STRING_1, csh.getNpc().getName() }, { Translate.STRING_2, String.valueOf(csh.getGrade()) } }));
					}
					if (cave.logger.isWarnEnabled()) {
						cave.logger.warn("[建筑升级完成] [{}] [升级后等级:{}] [主人:{}] [建筑ID:{}]", new Object[] { csh.getNpc().getName(), csh.getGrade(), cave.getOwnerId() }, csh.getNpc().getId());
					}
					cave.notifyFieldChange(csh.getType());
				}
				if(csh.getGrade()>20){
					csh.setGrade(20);
					cave.notifyFieldChange(csh.getType());
				}
			}
			if ("yushouzhai".equals(modify)) {
				out.print("驭兽斋是否在建设中：" + cave.getPethouse().inBuilding());
				CavePethouse cph = cave.getPethouse();
				if (cave.getPethouse().inBuilding()) {
					CaveSchedule caveSchedule = cave.getPethouse().getScheduleByOptType(Cave.OPTION_LEVEL_UP);
					out.print("开始时间：" + cave.getPethouse().getLvUpStartTime() + "caveSchedule:" + caveSchedule);
					if (caveSchedule != null) {
						out.print("驭兽斋hasDone:" + caveSchedule.hasDone());
					}
					cph.setGrade(cph.getGrade() + 1);
					((CaveNPC) cph.getNpc()).setGrade(cph.getGrade());
					cph.removeScheduleForDone(cave.OPTION_LEVEL_UP);

					((CaveNPC) cph.getNpc()).setInBuilding(false);
					ResourceManager.getInstance().setAvata(((CaveNPC) cph.getNpc()));
					cph.setStatus(cave.CAVE_BUILDING_STATUS_SERVICE);

					Player owner = GamePlayerManager.getInstance().getPlayer(cave.getOwnerId());
					owner.dealWithTaskAction(TaskActionOfCaveBuild.createTaskAction(cph.getType(), cph.getGrade()));
					if (GamePlayerManager.getInstance().isOnline(cave.getOwnerId())) {
						owner.sendNotice(Translate.translateString(Translate.text_cave_064, new String[][] { { Translate.STRING_1, cph.getNpc().getName() }, { Translate.STRING_2, String.valueOf(cph.getGrade()) } }));
					}
					if (cave.logger.isWarnEnabled()) {
						cave.logger.warn("[建筑升级完成] [{}] [升级后:{}] [主人:{}] [建筑ID:{}]", new Object[] { cph.getNpc().getName(), cph.getGrade(), cave.getOwnerId() }, cph.getNpc().getId());
					}
					cave.notifyFieldChange(cph.getType());
				}
			}
			if ("tiandi".equals(modify)) {
				for (CaveField caveField : cave.getFields()) {
					out.print("田地是否在建设中：" + caveField.inBuilding());
					if (caveField.inBuilding()) {
						CaveSchedule caveSchedule = caveField.getScheduleByOptType(Cave.OPTION_LEVEL_UP);
						out.print("开始时间：" + caveField.getLvUpStartTime() + "caveSchedule:" + caveSchedule);
						if (caveSchedule != null) {
							out.print("田地hasDone:" + caveSchedule.hasDone());
						}
						caveField.setGrade(caveField.getGrade() + 1);
						((CaveNPC) caveField.getNpc()).setGrade(caveField.getGrade());
						((CaveNPC) caveField.getNpc()).setInBuilding(false);
						ResourceManager.getInstance().setAvata(((CaveNPC) caveField.getNpc()));
						caveField.removeScheduleForDone(Cave.OPTION_LEVEL_UP);
						caveField.setStatus(Cave.CAVE_BUILDING_STATUS_SERVICE);

						Player owner = GamePlayerManager.getInstance().getPlayer(cave.getOwnerId());
						owner.dealWithTaskAction(TaskActionOfCaveBuild.createTaskAction(caveField.getType(), caveField.getGrade()));
						if (GamePlayerManager.getInstance().isOnline(cave.getOwnerId())) {
							owner.sendNotice(Translate.translateString(Translate.text_cave_064, new String[][] { { Translate.STRING_1, caveField.getNpc().getName() }, { Translate.STRING_2, String.valueOf(caveField.getGrade()) } }));
						}
						cave.notifyFieldChange(caveField.getType());
						if (cave.logger.isWarnEnabled()) {
							cave.logger.warn(cave.getOwnerId() + "[建筑升级完成] [" + caveField.getNpc().getName() + "] [升级后:" + caveField.getGrade() + "] [主人:" + cave.getOwnerName() + "] [建筑ID:" + caveField.getNpc().getId() + "]");
						}

					}
				}
				CavePethouse cph = cave.getPethouse();
				if (cave.getPethouse().inBuilding()) {
					CaveSchedule caveSchedule = cave.getPethouse().getScheduleByOptType(Cave.OPTION_LEVEL_UP);
					out.print("开始时间：" + cave.getPethouse().getLvUpStartTime() + "caveSchedule:" + caveSchedule);
					if (caveSchedule != null) {
						out.print("驭兽斋hasDone:" + caveSchedule.hasDone());
					}
					cph.setGrade(cph.getGrade() + 1);
					((CaveNPC) cph.getNpc()).setGrade(cph.getGrade());
					cph.removeScheduleForDone(cave.OPTION_LEVEL_UP);

					((CaveNPC) cph.getNpc()).setInBuilding(false);
					ResourceManager.getInstance().setAvata(((CaveNPC) cph.getNpc()));
					cph.setStatus(cave.CAVE_BUILDING_STATUS_SERVICE);

					Player owner = GamePlayerManager.getInstance().getPlayer(cave.getOwnerId());
					owner.dealWithTaskAction(TaskActionOfCaveBuild.createTaskAction(cph.getType(), cph.getGrade()));
					if (GamePlayerManager.getInstance().isOnline(cave.getOwnerId())) {
						owner.sendNotice(Translate.translateString(Translate.text_cave_064, new String[][] { { Translate.STRING_1, cph.getNpc().getName() }, { Translate.STRING_2, String.valueOf(cph.getGrade()) } }));
					}
					if (cave.logger.isWarnEnabled()) {
						cave.logger.warn("[建筑升级完成] [{}] [升级后:{}] [主人:{}] [建筑ID:{}]", new Object[] { cph.getNpc().getName(), cph.getGrade(), cave.getOwnerId() }, cph.getNpc().getId());
					}
					cave.notifyFieldChange(cph.getType());
				}
			}
		}
		out.print("==========cave的进度===========<BR/>");
		for (CaveSchedule cs : cave.getSchedules()) {
			out.print(cs.toString() + "剩余时间:" + cs.getLeftTime() + ",done?" + cs.hasDone() + "<BR/>");

		}
		out.print("==========各个建筑的进度===========<BR/>");
	%>
	<table border="1">
		<tr>
			<td>建筑</td>
			<td>进度列表</td>
		</tr>
	<%
		for (Iterator<Long> itor = cave.getBuildings().keySet().iterator(); itor.hasNext();) {
			long id = itor.next();
			CaveBuilding cb = cave.getBuildings().get(id);
			if (cb != null) {
				String spString = "";
				if (cb instanceof CaveField) {
					CaveField cf = (CaveField) cb;
					if (cf.getCaveFieldBombData() != null) {
						CaveFieldBombData cfb = cf.getCaveFieldBombData();
						spString = cfb.toString();
					}
				}
				out.print("<tr><td>" + cb.getNpc().getName() + (spString == null ? "" : "[" + spString + "]") + "</td>");
				out.print("<td>");
				if (cb.getSchedules() != null && cb.getSchedules().size() > 0) {
					for (CaveSchedule cs : cb.getSchedules()) {
						if (cs != null) {
							out.print(cs.toString());
						} else {
							out.print("无");
						}
					}
				} else {
					out.print("空闲");
				}
				out.print("</td></tr>");
			}
		}
	%>
	</table>
</body>
</html>
<script type="text/javascript">
	function up (type,idd) {
		alert("不能用");
		//alert(type + "----" + document.f1.action);
		//document.f1.submit();
	}
	function add(playerId){
		//alert(type);
		document.f1.action="caveOption.jsp?op=100&playerId="+playerId;
		//alert(type + "----" + document.f1.action);
		document.f1.submit();
	}
</script>