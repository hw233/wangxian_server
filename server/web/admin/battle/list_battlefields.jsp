<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="
java.util.*,
com.fy.engineserver.battlefield.*,
com.fy.engineserver.battlefield.concrete.* " %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.battlefield.concrete.BattleFieldLineupService.WaittingItem"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@include file="../IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 
<title>战场</title>
<% 
	BattleFieldManager bfm = BattleFieldManager.getInstance();
	 
	BattleFieldInfo infos[] = bfm.getBattleFieldInfos();
	
	LinkedHashMap<String,ArrayList<BattleFieldInfo>> map = new LinkedHashMap<String,ArrayList<BattleFieldInfo>>();
	
	for(int i = 0 ; i < infos.length ; i++){
		BattleFieldInfo bi = infos[i];
		ArrayList<BattleFieldInfo> al = map.get(bi.getMapName());
		if(al == null){
			al = new ArrayList<BattleFieldInfo>();
			map.put(bi.getMapName(),al);
		}
		al.add(bi);
	}
	
	
%>
</head>
<body>
<h2>战场情况，：</h2>
<a href="./list_battlefields.jsp">刷新此页面</a>
<br>
<br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#C2CAF5" align="center">
<td>地图名</td>
<td>战场名</td>
<td>战场类型</td>
<td>战斗模式</td>
<td>玩家等级</td>
<td>有效时间</td>
<td>开门时间</td>
<td>人数范围</td>
<td>战场系数</td>
<td>节日系数</td>
<td>人数不足持续时长</td>
</tr>

<%
	String keys[] = map.keySet().toArray(new String[0]);
	for(int i = 0 ; i < keys.length ; i++){
		ArrayList<BattleFieldInfo> al = map.get(keys[i]);
		for(int j = 0 ; j < al.size() ; j++){
			BattleFieldInfo bi = al.get(j);
			
			out.println("<tr bgcolor='#FFFFFF' align='center'>");
			if(j == 0){
				out.println("<td rowspan='"+al.size()+"'>"+keys[i]+"</td>");
			}
			
			out.println("<td>"+bi.getName()+"</td>");
			out.println("<td>"+bi.getBattleCategory()+"</td>");
			
			out.println("<td>"+(bi.getBattleFightingType() == BattleField.BATTLE_FIGHTING_TYPE_对战?"对战":"混战")+"</td>");
			out.println("<td>"+bi.getMinPlayerLevel()+"~"+bi.getMaxPlayerLevel()+"</td>");
			out.println("<td>"+bi.getMaxLifeTime()/1000+"秒</td>");
			out.println("<td>"+bi.getStartFightingTime()/1000+"秒</td>");
			out.println("<td>"+bi.getMinPlayerNumForStartOnOneSide()+" - "+bi.getMaxPlayerNumOnOneSide()+"</td>");
			
			
			out.println("<td>"+bi.getBattleRewardParam()+"</td>");
			out.println("<td>"+bi.getHolidyRewardParam()+"</td>");
			out.println("<td>"+bi.getLastingTimeForNotEnoughPlayers()/1000+"秒</td>");
			
			out.println("</tr>");
			
		}
	}
%>
</table>
<br/>
<h3>各个战场信息</h3>
<br/>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#C2CAF5" align="center">
<td>战场名</td>
<td>A方排队</td>
<td>B方排队</td>
<td>C方排队</td>

<td>战场</td>
<td>战场状态</td>
<td>A方等待</td>
<td>B方等待</td>
<td>C方等待</td>
<td>A方人数</td>
<td>B方人数</td>
<td>C方人数</td>
<td>开始时间</td>
<td>结束时间</td>
</tr>
<%

BattleFieldLineupService service = BattleFieldLineupService.getInstance();
for(int i = 0 ; i < keys.length ; i++){
	ArrayList<BattleFieldInfo> al = map.get(keys[i]);
	for(int j = 0 ; j < al.size() ; j++){
		BattleFieldInfo bi = al.get(j);
		
		BattleField bfs[] = bfm.getBattleFieldsByName(bi.getName());
		for(int k = 0 ; k < bfs.length ; k++){
			BattleField bf = bfs[k];
			out.println("<tr bgcolor='#FFFFFF' align='center'>");
			if(k == 0){
				out.println("<td rowspan='"+bfs.length+"'>"+bi.getName()+"</td>");
				
				out.println("<td rowspan='"+bfs.length+"'>"+ service.getPlayersByBattleFieldInfo(bi,BattleField.BATTLE_SIDE_A).length+"</td>");
				out.println("<td rowspan='"+bfs.length+"'>"+service.getPlayersByBattleFieldInfo(bi,BattleField.BATTLE_SIDE_B).length+"</td>");
				out.println("<td rowspan='"+bfs.length+"'>"+service.getPlayersByBattleFieldInfo(bi,BattleField.BATTLE_SIDE_C).length+"</td>");
			}
			String ssss[] = new String[]{"开启","战斗中","停止战斗","结束","","","","","","","","","",""};
			out.println("<td>"+bf.getId()+"</td>");
			out.println("<td>"+ ssss[bf.getState()]+"</td>");
			BattleFieldLineupService.WaittingItem wis[] = service.getWaittingItemByBattleField(bf,BattleField.BATTLE_SIDE_A);
			
			out.println("<td>"+wis.length+"</td>");
			wis = service.getWaittingItemByBattleField(bf,BattleField.BATTLE_SIDE_B);
			out.println("<td>"+wis.length+"</td>");
			
			wis = service.getWaittingItemByBattleField(bf,BattleField.BATTLE_SIDE_C);
			out.println("<td>"+wis.length+"</td>");
			
			Player ps[] = bf.getPlayersBySide(BattleField.BATTLE_SIDE_A);
			
			out.println("<td>"+ps.length+"</td>");
			ps = bf.getPlayersBySide(BattleField.BATTLE_SIDE_B);
			out.println("<td>"+ps.length+"</td>");
			
			ps = bf.getPlayersBySide(BattleField.BATTLE_SIDE_C);
			out.println("<td>"+ps.length+"</td>");
			
			out.println("<td>"+ DateUtil.formatDate(new Date(bf.getStartTime()),"HH:mm:ss") +"</td>");
			out.println("<td>"+ DateUtil.formatDate(new Date(bf.getEndTime()),"HH:mm:ss") +"</td>");
			out.println("</tr>");
		}
		
		if(bfs.length == 0){
			out.println("<tr bgcolor='#FFFFFF' align='center'>");
		
			out.println("<td>"+bi.getName()+"</td>");
		
			String ssss[] = new String[]{"开启","战斗中","停止战斗","结束"};
			out.println("<td>"+ service.getPlayersByBattleFieldInfo(bi,BattleField.BATTLE_SIDE_A).length+"</td>");
			out.println("<td>"+service.getPlayersByBattleFieldInfo(bi,BattleField.BATTLE_SIDE_B).length+"</td>");
			out.println("<td>"+service.getPlayersByBattleFieldInfo(bi,BattleField.BATTLE_SIDE_C).length+"</td>");
			
			out.println("<td>--</td>");
			out.println("<td>--</td>");
			out.println("<td>--</td>");
			
			out.println("<td>--</td>");
			out.println("<td>--</td>");
			out.println("<td>--</td>");
			out.println("<td>--</td>");
			out.println("<td>--</td>");
			out.println("<td>--</td>");
			out.println("<td>--</td>");
			out.println("</tr>");
		}
		
	}
}
%>

<h3>各个战场信息</h3>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#C2CAF5" align="center">

<td>战场Id</td>
<td>战场名</td>
<td>战场</td>
<td>战场状态</td>

<td>名字</td>
<td>职业</td>
<td>等级</td>
<td>SIDE</td>
<td>荣誉值</td>
<td>杀人数</td>
<td>被杀数</td>
<td>荣誉击杀</td>
<td>伤害输出</td>
<td>治疗输出</td>
<td>描述</td>
</tr>
<%

for(int i = 0 ; i < keys.length ; i++){
	ArrayList<BattleFieldInfo> al = map.get(keys[i]);
	for(int j = 0 ; j < al.size() ; j++){
		BattleFieldInfo bi = al.get(j);
		BattleField bfs[] = bfm.getBattleFieldsByName(bi.getName());
		
		for(int k = 0 ; k < bfs.length ; k++){
			BattleField bf = bfs[k];
			
			Hashtable<Long,BattleFieldStatData> map2 = bf.getStatDataMap();
			BattleFieldStatData datas[] = map2.values().toArray(new BattleFieldStatData[0]);
			Arrays.sort(datas,new Comparator<BattleFieldStatData>(){

				public int compare(BattleFieldStatData o1,
						BattleFieldStatData o2) {
					if(o1.getHonorPoints() > o2.getHonorPoints()){
						return -1;
					}
					if(o1.getHonorPoints() < o2.getHonorPoints()){
						return 1;
					}
					return 0;
				}
				
			});
			
			if(datas.length == 0){
				out.println("<tr bgcolor='#FFFFFF' align='center'>");
				
				if(k == 0){
					int count = 0;
					for(int kk = 0 ; kk < bfs.length ; kk++){
						int ss = bfs[kk].getStatDataMap().size();
						if(ss > 0) count += ss;
						else count += 1;
					}
					out.println("<td rowspan='"+count+"'>"+bi.getName()+"</td>");
				}
				
				out.println("<td>"+bf.getId()+"</td>");
				out.println("<td>"+bf.getName()+"</td>");
				out.println("<td>"+ bf.getBattleFieldSituation() +"</td>");
				
				
				out.println("<td>--</td>");
				out.println("<td>--</td>");
				out.println("<td>--</td>");
				out.println("<td>--</td>");
				
				out.println("<td>--</td>");
				out.println("<td>--</td>");
				
				out.println("<td>--</td>");
				
				
				out.println("<td>--</td>");
				out.println("<td>--</td>");
				out.println("<td>--</td>");
				out.println("<td>--</td>");
				
				
				out.println("</tr>");
			}
			
			for(int x = 0 ; x < datas.length; x++){
				BattleFieldStatData sd = datas[x];
				
				out.println("<tr bgcolor='#FFFFFF' align='center'>");
				
				if(k == 0 && x == 0){
					int count = 0;
					for(int kk = 0 ; kk < bfs.length ; kk++){
						int ss = bfs[kk].getStatDataMap().size();
						if(ss > 0) count += ss;
						else count += 1;
					}
					out.println("<td rowspan='"+count+"'>"+bi.getName()+"</td>");
				}
				
				if(x == 0){
					out.println("<td rowspan='"+datas.length+"'>"+bf.getId()+"</td>");
					out.println("<td rowspan='"+datas.length+"'>"+ bf.getBattleFieldSituation() +"</td>");
				}
				
				
				out.println("<td>"+sd.getPlayerName()+"</td>");
				out.println("<td>"+sd.getCareer()+"</td>");
				out.println("<td>"+sd.getPlayerLevel()+"</td>");
				
				out.println("<td>"+bf.getBattleFieldSideNames()[sd.getBattleSide()]+"</td>");
				
				out.println("<td>"+sd.getHonorPoints()+"</td>");
				out.println("<td>"+sd.getKillingNum()+"</td>");
				
				out.println("<td>"+sd.getKilledNum()+"</td>");
				
				
				out.println("<td>"+sd.getHonorKillingNum()+"</td>");
				out.println("<td>"+sd.getTotalDamage()+"</td>");
				out.println("<td>"+sd.getTotalEnhenceHp()+"</td>");
				out.println("<td>"+sd.getDescription()+"</td>");
				
				
				out.println("</tr>");
			}
			
		}
		
	}
}
%>
</body>
</html>
