<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="inc.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%
	String sId = request.getParameter("id");
	String sType = request.getParameter("type");
	String option = request.getParameter("option");
	long id = -1;
	long type = -1;
	int opt = -1;
	SeptStation station = null;
	if (sId != null) {
		id = Long.valueOf(sId);
	}
	if (sType != null) {
		type = Long.valueOf(sType);
	}
	if (option != null) {
		opt = Integer.valueOf(option);
	}
	if (!(id > -1 && type > -1)) {
		out.print("输入错误:id =" + sId + ",type = " + type);
		return;
	}
	station = SeptStationManager.getInstance().getSeptStationById(id);
	if (station == null) {
		out.print("无效的ID：" + sId);
		return;
	}
	String info = null;
	SeptBuildingEntity entity = station.getBuildings().get(Long.valueOf(type));//0,1,3为NPCID
	for (Iterator<Long> it = station.getBuildings().keySet().iterator();it.hasNext();) {
		long idd = it.next();
		SeptBuildingEntity e = station.getBuildings().get(idd);
		out.print("驻地的建筑: ID = " + idd + ",E = " + e + "<BR/>");
	}
	//for (SeptBuildingEntity e : station.getBuildings().values()) {
	//	if (e.getNpcId()% 10000000 == type) {
	//		entity = e;
	//		System.out.println(option + "[manager]entity = " + entity.getBuildingState().toString());
	//		break;
	//	}
	//}
	if (opt == 3 || opt == 1 || opt == 0) {
		if (entity == null) {
	out.print("entity == null NPCID=" + type);
	System.out.println("entity == null NPCID=" + type);
	return;
		}
	}

	if (opt == 1) {//升级
		try {
	info = "升级[" +entity.getBuildingState().getTempletType().getBuildingType().getName() + "]成功";
	entity.getBuildingState().getTempletType().levelUp(station, entity.getNpc().getId());
		} catch (MainBuildNotLvUpException e) {
	//info = TextTranslate.translate.text_6203;
		} catch (ResNotEnoughException e) {
	//info = TextTranslate.translate.text_6204;
		} catch (OtherBuildingNotLvUpException e) {
	//info = TextTranslate.translate.text_6205;
		} catch (BuildingNotFoundException e) {
	//info = TextTranslate.translate.text_6202;
		} catch (MaxLvException e) {
	//info = TextTranslate.translate.text_6207;
		}
	} else if (opt == 0) {//降级
		try {
	info = "降级[" +entity.getBuildingState().getTempletType().getBuildingType().getName() + "]成功";
	entity.getBuildingState().getTempletType().levelDown(station, entity.getNpc().getId(), true, null);
		} catch (BuildingNotFoundException e) {
	//info = TextTranslate.translate.text_6202;
		} catch (WrongLvOnLvDownException e) {
	//info = TextTranslate.translate.text_6208;
		} catch (ActionIsCDException e) {
	//info = TextTranslate.translate.text_6339 + e.getMsg();
		}
	} else if (opt == 2) {//建造
		String sIndex = request.getParameter("index");
		int buildIndex = -1;	
		if (sIndex != null) {
	buildIndex = Integer.valueOf(sIndex);
		}
		if (buildIndex == -1) {
	info = "位置错误";
		} else {
	SeptBuildingTemplet templet = SeptBuildingManager.getTemplet((int)type);
	info = "建造[" + templet.getBuildingType().getName() + "]成功";
	try {
		templet.build(station,entity.getNpc().getId(),buildIndex);
	} catch (DependBuildingNotLvUpException e) {
		info = "需要" + e.getMsg();
	} catch (TooManyBiaozhixiangException e) {
		info = "已经建造了其他标志像";
	} catch (BuildingExistException e) {
		info = "已经建造此建筑";
	} catch (ResNotEnoughException e) {
		info = "资源不够";
	} catch (IndexHasBuildingException e) {
		info = "建筑等级大于1不可建造";
	} catch (OtherInBuildException e) {
		info = "";
	} catch (JiaZuFenyingException e) {
		
	}
		}
	} else if (opt == 3) {//销毁
		try {
	info = "摧毁[" +entity.getBuildingState().getTempletType().getBuildingType().getName() + "]成功";
	entity.getBuildingState().getTempletType().destroy(station, entity.getNpc().getId());
		} catch (CannotDestoryException e) {
	info = "无法摧毁此建筑";
		} catch (BuildingNotFoundException e) {
	info = "没有找到对应建筑";
		}
	}else {
		out.print("参数错误: option = " + option);
		return;
	}
	response.sendRedirect("./septStation.jsp?id="+id+"&msg="+java.net.URLEncoder.encode(info,"utf-8"));
	return;
%>