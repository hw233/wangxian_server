<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="inc.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%
	String sId = request.getParameter("id");
	String msg = request.getParameter("msg");
	msg = msg == null ? "": msg;
	long id = -1;
	if (sId != null) {
		id = Long.valueOf(sId);
	}
	SeptStation station = SeptStationManager.getInstance().getSeptStationById(id);
	if (station == null) {
		out.print("无效的ID：" + sId);
		return;
	}
	Map<Integer,NpcStation> canBuildIndex = new HashMap<Integer,NpcStation>();//可建造的位置
	List<Integer> canBuildList =  new ArrayList<Integer>();//可建造的列表
	
	SeptStationMapTemplet mapTemplet = SeptStationMapTemplet.getInstance();
	for (Iterator<Integer> it = mapTemplet.getSeptNpcStations().keySet().iterator();it.hasNext();) {
		int index = Integer.valueOf(it.next());
		canBuildIndex.put(index,mapTemplet.getSeptNpcStations().get(index));
	}
	
	for (SeptBuildingTemplet t : SeptBuildingManager.type_templet_map.values()) {
		canBuildList.add(t.getTypeIndex());
	}
	
	List<SeptBuildingEntity> entitys = new ArrayList<SeptBuildingEntity>(); 
	for (SeptBuildingEntity entity : station.getBuildings().values()) {
		entitys.add(entity);
	}
	//Collections.sort(entitys,order);
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" type="text/css"
	href="../SeptBuilding/css/septStation.css">

</head>
<body>
	<div>
		<span style="font-size: 18px; color: red"><%=msg %></span>
	</div>
	<hr />
	<form action="./septCallBoss.jsp?id=<%=id %>" name="f1" method="post">
		<table>
			<tr class="head">
				<td>形象</td>
				<td>建筑名称</td>
				<td>建筑级别</td>
				<td>增加繁荣度</td>
				<td colspan="3">操作</td>
			</tr>
			<%
					int ko = 0;
					for (SeptBuildingEntity entity : entitys) {
						System.out.println("-----" + (ko ++) + ">>>>" + entity.getBuildingState());
						canBuildIndex.remove(entity.getIndex());
						if (entity.getBuildingState() == null) {
							continue;
						}
						if (!entity.getBuildingState().getTempletType().getBuildingType().isJianTa()) {
							canBuildList.remove(Integer.valueOf(entity.getBuildingState().getTempletType().getTypeIndex()));
						}
						if (entity.getBuildingState().getLevel()== 0) {
							continue;
						}
				 %>
			<tr
				style="background-color:<%=entity.getBuildingState().getTempletType().getBuildingType().isBase() ? baseColor : otherColor%>">
				<td><img
					src="../../SeptBuilding/img/<%=entity.getBuildingState().getTempletType().getOutShows()[entity.getBuildingState().getLevel() - 1] %>.jpg">
				</td>
				<td ><%=entity.getBuildingState().getTempletType().getName() %>&<%="NPC:" +entity.getNpc().getId()+",baseId=" + entity.getNpc().getNPCCategoryId() + ">>>>" + entity.getNpc().getX() + "," + entity.getNpc().getY() %></td>
				<td><%=entity.getBuildingState().getLevel() %>[<%=station.getBuildingLevel(entity.getBuildingState().getTempletType().getBuildingType()) %>][<%=station.getMainBuildingLevel() %>]</td>
				<td><%=entity.getBuildingState().getTempletType().getLvUpAddProsper()[entity.getBuildingState().getLevel() - 1]%></td>
				<td><input class="button" type="button" value="升级"
					onclick="lvUp('<%=entity.getIndex()%>')"></td>
				<td><input class="button" type="button" value="降级"
					onclick="javascript:if(confirm('确实要降级[<%=entity.getBuildingState().getTempletType().getBuildingType().getName()%>]么?'))lvDown('<%=entity.getIndex()%>')">
				</td>
				<td><input class="button" type="button" value="拆除"
					onclick="javascript:if(confirm('确实要摧毁[<%=entity.getBuildingState().getTempletType().getBuildingType().getName()%>]么?'))destory('<%=entity.getIndex()%>')">
				</td>
			</tr>
			<%
				}
				 %>
		</table>
		<input type="hidden" value="<%=id %>" name="id">
		<hr>
		可建造建筑<BR />
		<hr>
		<div style="font-size: 14px;">
			<%
					int ii = 0;
					for (Integer k : canBuildList) {
						ii++;
						SeptBuildingTemplet t = SeptBuildingManager.getTemplet(k);
				 %>
			<input type="radio" value="<%= t.getTypeIndex() %>" name="buildType"><%=t.getBuildingType().getName() %>
			<%
					 	if (ii % 7 == 0) {
					 		out.print("<BR/>");
					 	}
				 	}
				  %>
			<BR />
			<hr>
			可选位置<BR />
			<hr>
			<%
				  	ii = 0;
				  	for (Iterator<Integer> it = canBuildIndex.keySet().iterator();it.hasNext();) {
				  		ii++;
				  		int index = it.next();
				  		NpcStation ns = canBuildIndex.get(index);
				   %>
			<input type="radio" value="<%=ns.getIndex() %>" name="npcstation"
				title="<%=ns.toString() %>"><%=ns.getIndex() %>
			<%
					   if (ii % 7 == 0) {
					 		out.print("<BR/>");
					 	}
				   	}
				    %>
		</div>
		<input class="button" type="button" value="建造" onclick="build()">
	</form>
	<div>
		图例:<BR /> <span style="background-color:<%=baseColor%>;">基础建筑</span>
		<span style="background-color:<%=otherColor%>;">扩展建筑</span>
	</div>
</body>
</html>
<script>
	function lvUp(type) {
		document.f1.action = "../SeptBuilding/septStationManager.jsp?type="
				+ type + "&option=1";
		document.f1.submit();
	}
	function lvDown(type) {
		document.f1.action = "../SeptBuilding/septStationManager.jsp?type="
				+ type + "&option=0";
		document.f1.submit();
	}

	function destory(type) {
		document.f1.action = "../SeptBuilding/septStationManager.jsp?type="
				+ type + "&option=3";
		document.f1.submit();
	}
	function build() {
		var types = document.f1.buildType;
		var indexs = document.f1.npcstation;
		var checkType = "null";
		var checkIndex = "null";
		for (i = 0; i < types.length; i++) {
			if (types[i].checked) {
				checkType = types[i].value;
			}
		}
		for (i = 0; i < indexs.length; i++) {
			if (indexs[i].checked) {
				checkIndex = indexs[i].value;
			}
		}

		if (checkType == "null") {
			alert("请选择建造类型");
			return;
		}
		if (checkIndex == "null") {
			alert("请选择建造的位置");
			return;
		}
		document.f1.action = "../SeptBuilding/septStationManager.jsp?type="
				+ checkType + "&option=2&index=" + checkIndex;
		document.f1.submit();
	}
</script>