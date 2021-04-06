<%@page import="java.util.Hashtable"%>
<%@page import="com.fy.engineserver.homestead.cave.CaveField"%>
<%@page
	import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.homestead.faery.Faery"%>
<%@page import="java.util.List"%>
<%@page
	import="com.fy.engineserver.homestead.faery.service.QuerySelect"%>
<%@page import="com.fy.engineserver.homestead.cave.Cave"%>
<%@page
	import="com.fy.engineserver.homestead.faery.service.FaeryConfig"%>
<%@page
	import="com.fy.engineserver.homestead.faery.service.FaeryManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String query = request.getParameter("query");
	String faeryIdStr = request.getParameter("faeryId");

	QuerySelect<Cave> queryCave = new QuerySelect<Cave>();
	if ("all".equals(query)) {
		List<Cave> allCaves = queryCave.selectAll(Cave.class, null, null, null, FaeryManager.caveEm);
		out.print("查询结果:" + allCaves.size() + "个<BR/>");
		out.print("内存中有效数量:" + FaeryManager.getInstance().getCaveIdmap().size() + "个<BR>");
		out.print("内存中封印数量:" + FaeryManager.getInstance().getKhatamMap().size() + "个<BR>");

		for (Cave cave : allCaves) {
			out.print("仙府ID:" + cave.getId() + ",主人ID:" + cave.getOwnerId() + ",主人名字:" + cave.getOwnerName() + ",状态:" + cave.getStatus() + "<BR/>");
		}
	} else if ("one".equals(query)) {
		if (faeryIdStr != null && !"".equals(faeryIdStr.trim()) && !"null".equalsIgnoreCase(faeryIdStr.trim())) {
			long fid = Long.valueOf(faeryIdStr);
			Hashtable<Long, Faery> allFaery = FaeryManager.getInstance().getFaeryMap();
			for (Faery f : allFaery.values()) {
				//Faery f = FaeryManager.getInstance().getFaery(fid);
				if (f == null) {
					out.print("仙府信息不存在");
					return;
				}
				for (Long id : f.getCaveIds()) {
					if (id == null || id <= 0) {
						continue;
					}
					List<Cave> allCaves = queryCave.selectAll(Cave.class, "id = ? ", new Object[] { id }, null, FaeryManager.caveEm);
					//out.print("id:" + id + ",status=" + FaeryConfig.CAVE_STATUS_OPEN + ",查询结果:" + allCaves + "/" + allCaves.size() + "<BR/>");
					if (allCaves != null && allCaves.size() > 0) {
						Cave cave = allCaves.get(0);
						long ownerId = cave.getOwnerId();
						//out.print(cave.getOwnerName() + "的仙府,状态:" + cave.getStatus());
						if (cave.getStatus() == FaeryConfig.CAVE_STATUS_OPEN && !FaeryManager.getInstance().getCaveIdmap().keySet().contains(cave.getId())) {
							out.print("在数据库中但是不在内存中"+cave.getId() + "," + cave.getStatus()+"<BR/>");
							//Player player = GamePlayerManager.getInstance().getPlayer(cave.getOwnerId());
							//if (player != null) {
							//	long faeryId = player.getFaeryId();
								Faery faery = f;//FaeryManager.getInstance().getFaery(f.getId());
								if (faery != null) {
									for (int i = 0; i < faery.getCaveIds().length; i++) {
										Long cidInfaery = faery.getCaveIds()[i];
										if (cidInfaery != null && cidInfaery == cave.getId()) {
											if (faery.getCaves()[i] != null) {
												//out.print("异常,原来位置上有仙府,<BR/>");
											} else {
												out.print("<font color='green'>找到对应的位置:" + i + "</font><BR/>");
												FaeryManager.getInstance().getPlayerCave().put(ownerId, cave);
												FaeryManager.getInstance().getCaveIdmap().put(cave.getId(), cave);

												faery.getCaves()[i] = cave;
												cave.setFaery(faery);
												cave.setIndex(i);
												cave.onLoadInitNpc();

												if (cave.getMainBuilding().getNpc() != null) {
													faery.getGame().addSprite(cave.getMainBuilding().getNpc());
												} else {
													FaeryManager.logger.error("[init仙境异常] [增加了一个空的NPC] [所在仙境:{}] [仙府主人:{}] [建筑类型:{}]", new Object[] { faery.getName(), cave.getOwnerName(), cave.getMainBuilding().getType() });
												}
												if (cave.getStorehouse().getNpc() != null) {
													faery.getGame().addSprite(cave.getStorehouse().getNpc());
												} else {
													FaeryManager.logger.error("[init仙境异常] [增加了一个空的NPC] [所在仙境:{}] [仙府主人:{}] [建筑类型:{}]", new Object[] { faery.getName(), cave.getOwnerName(), cave.getStorehouse().getType() });
												}
												if (cave.getPethouse().getNpc() != null) {
													faery.getGame().addSprite(cave.getPethouse().getNpc());
												} else {
													FaeryManager.logger.error("[init仙境异常] [增加了一个空的NPC] [所在仙境:{}] [仙府主人:{}] [建筑类型:{}]", new Object[] { faery.getName(), cave.getOwnerName(), cave.getPethouse().getType() });
												}
												if (cave.getDoorplate().getNpc() != null) {
													faery.getGame().addSprite(cave.getDoorplate().getNpc());
												} else {
													FaeryManager.logger.error("[init仙境异常] [增加了一个空的NPC] [所在仙境:{}] [仙府主人:{}] [建筑类型:{}]", new Object[] { faery.getName(), cave.getOwnerName(), cave.getDoorplate().getType() });
												}
												if (cave.getFence().getNpc() != null) {
													faery.getGame().addSprite(cave.getFence().getNpc());
												} else {
													FaeryManager.logger.error("[init仙境异常] [增加了一个空的NPC] [所在仙境:{}] [仙府主人:{}] [建筑类型:{}]", new Object[] { faery.getName(), cave.getOwnerName(), cave.getFence().getType() });
												}
												for (CaveField field : cave.getFields()) {
													if (field.getPlantStatus() != null) {
														field.getPlantStatus().initCfg();
													}
													if (field.getNpc() != null) {
														faery.getGame().addSprite(field.getNpc());
													} else {
														FaeryManager.logger.error("[init仙境异常] [增加了一个空的NPC] [所在仙境:{}] [仙府主人:{}] [建筑类型:{}]", new Object[] { faery.getName(), cave.getOwnerName(), field.getType() });
													}
												}
												cave.initSchedule();

											}
											break;
										}
									}
								} else {
									//out.print("faery不存在,fid:" + faeryId + "<BR/>");
								}
							//} else {
							//	out.print("player不存在,pid:" + cave.getOwnerId() + "<BR/>");
						//	}
						}
					} else {
						out.print("仙府不存在,caveId:" + faeryIdStr + "<BR/>");
					}
					out.print("<HR>");
					out.print("<HR>");
				}
			}
		} else {
			out.print("未输入Id:" + faeryIdStr);
		}
	} else {
		out.print("<h1>输入有误,query:" + query + "</h1>");
	}

	out.print("===============================================================<BR/>");
	out.print("=======================直接在数据库中查询=======================<BR/>");
	out.print("===============================================================<BR/>");
	out.print("=======================query=all 查询所有=======================<BR/>");
	out.print("================query=one&faery=?修改某个仙境的仙府==============<BR/>");
	out.print("================================================================<BR/>");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body style="font-size: 14px;">
	<form action="./queryAllcave.jsp">
		query&nbsp;:<input type="text" name="query" value="<%=query%>"><BR />
		caveId:<input type="text" name="faeryId" value="<%=faeryIdStr%>"><BR />
		<input style="background-color: red; color: white;" type="submit"
			value="修复"><BR />
	</form>
</body>
</html>