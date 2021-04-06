<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.wing.WingMessForGM"%>
<%@page import="com.fy.engineserver.wing.WingManager"%>
<%@page import="com.fy.engineserver.wing.Wing"%>
<%@page import="com.fy.engineserver.wing.WingPanel"%>
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

	WingPanel wp = p.getWingPanel();
	if (wp != null) {
		if (wp.getWingId() > 0) {
			Wing wing = WingManager.getInstance().getWingbyId(wp.getWingId(), p);
			if (wing == null) {
				out.print("获取翅膀出错:" + wp.getWingId());
				return;
			}
			WingMessForGM mess = new WingMessForGM();
			if (wp.getBrightInlayId() > 0) {
				ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(wp.getBrightInlayId());
				if (ae != null) {
					mess.setGuangXiaoStoneName(ae.getArticleName());
				}
			}
			/* Map<String, Wing> wingNameMap = WingManager.getInstance().getWingNameMap();
			if (wingNameMap.size() > 0) {
				String[] collectStoneNames = wingNameMap.keySet().toArray(new String[] {});
				mess.setCollectStoneNames(collectStoneNames);
			} */
			Map<Integer, Wing> wingNameMap = WingManager.getInstance().getWingIdMap();
			int nums[] = new int[wingNameMap.size()];
			String[] collectStoneNames = new String[wingNameMap.size()];
			int index = 0;
			for (int i = 1; i <= wingNameMap.size(); i++) {
				nums[index]= wp.getWingNumsByType(i);
				collectStoneNames[index] = WingManager.getInstance().getWingIdMap().get(i).getName();
				index++;
			}
			mess.setCollectStoneNames(collectStoneNames);
			mess.setCollectStoneNums(nums);
			mess.setXiangQianStoneNames_Levels(wp.getInlayNames_Levels());
			mess.setStarLevel(wp.getStar());
			boolean[] basicPropertyActive = wp.getBasicPropertyActive();
			List<String> list = new ArrayList<String>();
			if(basicPropertyActive != null){
				for(int i=0;i<basicPropertyActive.length;i++){
					if(basicPropertyActive[i]){
						list.add(wp.getOpenBasicPropInfo(i));
					}
				}
			}
			mess.setBasicPropNames(list.toArray(new String[]{}));
			
			String result = JsonUtil.jsonFromObject(mess);
			out.print(result);
		} else {
			out.print("当前没使用翅膀");
			return;
		}
	} else {
		out.print("获取玩家：" + pname + " 翅膀信息面板出错");
	}
%>
