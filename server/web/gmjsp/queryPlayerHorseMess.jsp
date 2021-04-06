<%@page import="com.xuanzhi.tools.text.JsonUtil"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="com.fy.engineserver.sprite.horse2.manager.Horse2Manager"%>
<%@page import="com.fy.engineserver.sprite.horse2.model.HorseSkillModel"%>
<%@page import="com.fy.engineserver.sprite.horse.HorseOtherData"%>
<%@page import="com.fy.engineserver.sprite.horse.HorseMessForGM"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.EquipmentEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.HorseEquipmentColumn"%>
<%@page import="com.fy.engineserver.sprite.horse.Horse"%>
<%@page import="com.fy.engineserver.sprite.horse.HorseManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="java.net.URLDecoder"%>
<%@ page
	language="java"
	contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
%>
<%
	String pname = request.getParameter("playerName");
	if (pname == null || pname.isEmpty()) {
		out.print("请输入正确的角色名");
		return;
	}
	pname = URLDecoder.decode(pname, "UTF-8");

	Player p = PlayerManager.getInstance().getPlayer(pname);
	if (p == null) {
		out.print("玩家:" + pname + "不存在");
		return;
	}

	if (p.getRideHorseId() <= 0) {
		out.print("没有默认坐骑");
		return;
	}
	
	Horse horse = HorseManager.getInstance().getHorseById(p.getRideHorseId(), p);
	if (horse == null) {
		out.print("获取坐骑出错,id:" + p.getRideHorseId());
		return;
	}

	HorseEquipmentColumn hec = horse.getEquipmentColumn();
	EquipmentEntity[] hEquEntitys = hec.getEquipmentArrayByCopy();
	if (hEquEntitys != null && hEquEntitys.length > 0) {
		HorseMessForGM mess = new HorseMessForGM();
		mess.setHorseName(horse.getHorseName());
		String eqNames[] = new String[hEquEntitys.length];
		for (int i = 0; i < hEquEntitys.length; i++) {
			if(hEquEntitys[i] != null){
				eqNames[i] = hEquEntitys[i].getArticleName();
			}
		}
		mess.setEquipmentNames(eqNames);
		HorseOtherData otherData = horse.getOtherData();
		int[] skills = otherData.getSkillList();
		int[] skillLevels = otherData.getSkillLevel();
		if (skills != null) {
			String skillNames [] = new String[skills.length];
			for (int k = 0; k < skills.length; k++) {
				HorseSkillModel hsm = Horse2Manager.instance.horseSkillMap.get(skills[k]);
				if (hsm != null) {
					skillNames[k] = hsm.getName() + "("+skillLevels[k]+"等级)";
				}
			}
			mess.setSkillNames(skillNames);
		}

		mess.setPinzhi(String.valueOf(horse.getColorType()));
		String result = JsonUtil.jsonFromObject(mess);
		out.print(result); 
// 		HorseMessForGM horsemess = JsonUtil.objectFromJson(result,HorseMessForGM.class);
// 		out.print("<hr>");
// 		out.print("horseName:"+horsemess.getHorseName()+"<br>");
// 		out.print("color:"+horsemess.getPinzhi()+"<br>");
	} else {
		out.print("玩家坐骑没有装备");
	}
%>
