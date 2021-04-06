<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page import="com.xuanzhi.tools.transaction.*,com.google.gson.*,java.util.*,com.fy.engineserver.sprite.*,com.xuanzhi.gameresource.*,
com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.datasource.career.*,com.fy.engineserver.mail.*,com.fy.engineserver.mail.service.*,java.sql.Connection,java.sql.*,java.io.*,com.xuanzhi.boss.game.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.datasource.article.data.props.*"%>
<%@page import="com.fy.engineserver.datasource.article.manager.*"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.*"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.*"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.*"%>
<%@include file="../IPManager.jsp" %>
<%@page import="com.fy.engineserver.country.manager.CountryManager"%>
<%@page import="com.fy.engineserver.country.data.Country"%>
<%@page import="com.fy.engineserver.zongzu.manager.ZongPaiManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>国家管理器信息</title>
<link rel="stylesheet" type="text/css" href="../../css/common.css" />
<link rel="stylesheet" type="text/css" href="../../css/table.css" />
</head>
<body>
<%!
CountryManager cm = CountryManager.getInstance();
PlayerManager pm = PlayerManager.getInstance();
%>
<%

long playerId = -1;
String playerName = null;
Player player = null;
String action = request.getParameter("action");
try{
try{
	Object obj = session.getAttribute("playerid");
	Object obj2 = session.getAttribute("playerName");
	if(obj != null){
		playerId = Long.parseLong(obj.toString());
	}
	if(obj2 != null){
		playerName = obj2.toString();
	}
	String errorMessage = null;
	if (action == null) {
		if (errorMessage == null) { 
			if(playerId != -1){
				try{
				player = pm.getPlayer(playerId);
				}catch(Exception ex){
					
				}
				if (player == null) {
					errorMessage = "ID为" + playerId + "的玩家不存在！";
				}
			}else if(playerName != null){
				try{
				player = pm.getPlayer(playerName);
				}catch(Exception ex){
					
				}
				if (player == null) {
					errorMessage = "ID为" + playerId + "的玩家不存在！";
				}
			}
		}
	}else if (action != null && action.equals("select_playerId")) {
		try {
			playerId = Long.parseLong(request
					.getParameter("playerid"));
		} catch (Exception e) {
			errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
		}
		if (errorMessage == null) {
			try{
				player = pm.getPlayer(playerId);
				}catch(Exception ex){
					
				}
			if (player == null) {
				errorMessage = "ID为" + playerId + "的玩家不存在！";
			}else{
				session.setAttribute("playerid",request.getParameter("playerid"));
				playerName = player.getName();
				session.setAttribute("playerName",playerName);
			}
		}
	}else if (action != null && action.equals("select_playerName")) {
		try {
			playerName = request
					.getParameter("playerName");
		} catch (Exception e) {
			errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
		}
		if (errorMessage == null) {
			try{
				player = pm.getPlayer(playerName);
				}catch(Exception ex){
					
				}
			if (player == null) {
				errorMessage = "角色名为" + playerName + "的玩家不存在！";
			}else{
				session.setAttribute("playerName",request.getParameter("playerName"));
				playerId = player.getId();
				session.setAttribute("playerid",playerId);
			}
		}
	}
}catch(Exception ex){
	
}
%>
<form id='f1' name='f1' action="" method="post"><input type='hidden' name='action' value='select_playerId'>
请输入要用户的ID：<input type='text' name='playerid' id='playerid' value='<%=playerId %>' size='20'><input type='submit' value='提  交'>
</form>
<form id='f01' name='f01' action=""><input type='hidden' name='action' value='select_playerName'>
请输入要角色名：<input type='text' name='playerName' id='playerName' value='<%=playerName %>' size='20'><input type='submit' value='提  交'>
</form>
<br/>
<%
String kingId = request.getParameter("kingId");
String zhaojiDescription = request.getParameter("zhaojiDescription");


String zhaoji = request.getParameter("zhaoji");
if(zhaoji != null && !zhaoji.trim().equals("")){
	if(player != null){
		cm.使用召集NPC申请(player);
	}
}
String qiujin = request.getParameter("qiujin");
if(qiujin != null && !qiujin.trim().equals("")){
	if(player != null){
		cm.官员申请囚禁(player);
	}
}
String qiujinId = request.getParameter("qiujinId");
if(qiujinId != null && !qiujinId.trim().equals("")){
	if(player != null){
		try{
			Player playerB = pm.getPlayer(Long.parseLong(qiujinId));
			String result = cm.囚禁合法性判断2(player,playerB);
			if(result == null){
				cm.囚禁(player,playerB);
			}else{
			    out.println(result);
			}
		}catch(Exception ex){
			out.println("囚禁异常");
		}
	}
}
String shifang = request.getParameter("shifang");
if(shifang != null && !shifang.trim().equals("")){
	if(player != null){
		cm.官员申请释放(player);
	}
}
String shifangId = request.getParameter("shifangId");
if(shifangId != null && !shifangId.trim().equals("")){
	if(player != null){
		try{
			Player playerB = pm.getPlayer(Long.parseLong(shifangId));
			String result = cm.释放合法性判断2(player,playerB);
			if(result == null){
				cm.确认释放(player,playerB);
			}else{
				out.println(result);
			}
		}catch(Exception ex){
			out.println("释放异常");
		}
	}
}
String ciguan = request.getParameter("ciguan");
if(ciguan != null && !ciguan.trim().equals("")){
	if(player != null){
		cm.官员申请辞官(player);
	}
}
String gonggao = request.getParameter("gonggao");
if(gonggao != null && !gonggao.trim().equals("")){
	if(player != null){
		cm.发布国家公告申请(player);
	}
}
String renmingguanzhi = request.getParameter("renmingguanzhi");
if(renmingguanzhi != null && !renmingguanzhi.trim().equals("")){
	if(player != null){
		cm.任命申请(player,Integer.parseInt(renmingguanzhi));
	}
}
String bamianId = request.getParameter("bamianId");
if(bamianId != null && !bamianId.trim().equals("")){
	if(player != null){
		try{
			Player playerB = pm.getPlayer(Long.parseLong(bamianId));
			int 官职B = cm.官职(playerB.getCountry(),playerB.getId());
			cm.罢免申请(player,playerB,官职B);
		}catch(Exception ex){
			out.println("囚禁异常");
		}
	}
}

String chexiaoId = request.getParameter("chexiaoId");
if(chexiaoId != null && !chexiaoId.trim().equals("")){
	
	try{
		Player opreatePlayer = PlayerManager.getInstance().getPlayer(Long.parseLong(chexiaoId));
		
		CountryManager cm = CountryManager.getInstance();
		if(cm.官职(opreatePlayer.getCountry(), opreatePlayer.getId()) == CountryManager.国王){
			cm.撤销授勋表彰(opreatePlayer);
			out.print("撤销这个国王授勋，表彰成功");
		}else{
			out.print("此人不是国王，不能使用这个功能");
		}
	}catch(Exception e){
		out.println("撤销表彰异常");
		e.printStackTrace();
	}
	
}


String[] guanyuantoupiaos = request.getParameterValues("guanyuantoupiao");
String voteType = request.getParameter("voteType");
if(guanyuantoupiaos != null){
	if(player != null){
		boolean[] votes = new boolean[9];
		for(int i = 0; i < votes.length; i++){
			for(String s : guanyuantoupiaos){
				if(Integer.parseInt(s) == i){
					votes[i] = true;
				}
			}
		}
		String result = cm.投票合法性判断(player, votes,  Byte.parseByte(voteType));
		if(result == null){
			cm.投票(player,votes,Byte.parseByte(voteType));
		}else{
			out.println(result);
		}
		
	}
}
String modifyValue = request.getParameter("modifyValue");
if(action != null && !action.trim().equals("") && modifyValue != null){
	if(action.equals("guojiameicizhaojirenshu")){
		try{
			int value = Integer.parseInt(modifyValue);
			CountryManager.国家召集每次召集的人数 = value;
			out.println("国家召集每次召集的人数:"+value);
		}catch(Exception ex){
			out.println("国家召集每次召集的人数没修改成功");
		}
	}else if(action.equals("zhiqu")){
		try{
			int value = Integer.parseInt(modifyValue);
			CountryManager.每天支取上限 = value;
			out.println("国家每天支取上限:"+value);
		}catch(Exception ex){
			out.println("国家每天支取上限没修改成功");
		}
	}else if(action.equals("guoyunzuizaokaishi")){
		try{
			int value = Integer.parseInt(modifyValue);
			CountryManager.每天国运最早开始时间 = value;
			out.println("国家每天国运最早开始时间:"+value);
		}catch(Exception ex){
			out.println("国家每天国运最早开始时间没修改成功");
		}
	}else if(action.equals("guoyunzuiwankaishi")){
		try{
			int value = Integer.parseInt(modifyValue);
			CountryManager.每天国运最晚开始时间 = value;
			out.println("国家每天国运最晚开始时间:"+value);
		}catch(Exception ex){
			out.println("国家每天国运最晚开始时间没修改成功");
		}
	}else if(action.equals("guotanzuizaokaishi")){
		try{
			int value = Integer.parseInt(modifyValue);
			CountryManager.每天国探最早开始时间 = value;
			out.println("国家每天国探最早开始时间:"+value);
		}catch(Exception ex){
			out.println("国家每天国探最早开始时间没修改成功");
		}
	}else if(action.equals("guotanzuiwankaishi")){
		try{
			int value = Integer.parseInt(modifyValue);
			CountryManager.每天国探最晚开始时间 = value;
			out.println("国家每天国探最晚开始时间:"+value);
		}catch(Exception ex){
			out.println("国家每天国探最晚开始时间没修改成功");
		}
	}else if(action.equals("guoyunshichang")){
		try{
			int value = Integer.parseInt(modifyValue);
			CountryManager.国运时长 = value;
			out.println("国家国运时长:"+value);
		}catch(Exception ex){
			out.println("国家国运时长没修改成功");
		}
	}else if(action.equals("guotanshichang")){
		try{
			int value = Integer.parseInt(modifyValue);
			CountryManager.国探时长 = value;
			out.println("国家国探时长:"+value);
		}catch(Exception ex){
			out.println("国家国探时长没修改成功");
		}
	}else if(action.equals("jingbiaokaishi")){
		try{
			int value = Integer.parseInt(modifyValue);
			CountryManager.每天竞标开始时间 = value;
			out.println("国家每天竞标开始时间:"+value);
		}catch(Exception ex){
			out.println("国家每天竞标开始时间没修改成功");
		}
	}else if(action.equals("jingbiaojieshu")){
		try{
			int value = Integer.parseInt(modifyValue);
			CountryManager.每天竞标结束时间 = value;
			out.println("国家每天竞标结束时间:"+value);
		}catch(Exception ex){
			out.println("国家每天竞标结束时间没修改成功");
		}
	}else if(action.equals("jingbiaoshengyuzijinlv")){
		try{
			double value = Double.parseDouble(modifyValue);
			CountryManager.竞标剩余资金率 = value;
			out.println("国家竞标剩余资金率:"+value);
		}catch(Exception ex){
			out.println("国家竞标剩余资金率没修改成功");
		}
	}else if(action.equals("biaocheyuwanjiazuidajuli")){
		try{
			int value = Integer.parseInt(modifyValue);
			CountryManager.镖车与玩家的距离最大值 = value;
			out.println("国家镖车与玩家的距离最大值:"+value);
		}catch(Exception ex){
			out.println("国家镖车与玩家的距离最大值没修改成功");
		}
	}else if(action.equals("guowanghuodebiaojuliyun")){
		try{
			double value = Double.parseDouble(modifyValue);
			CountryManager.国王获得镖局利润率 = value;
			out.println("国家国王获得镖局利润率:"+value);
		}catch(Exception ex){
			out.println("国家国王获得镖局利润率没修改成功");
		}
	}else if(action.equals("shuaxin")){
		try{
			CountryManager.getInstance().每天对国家的数据进行维护();
			out.println("国家每天对国家的数据进行维护成功");
		}catch(Exception ex){
			out.println("国家每天对国家的数据进行维护没修改成功");
		}
	}else if(action.equals("guowang")){
		try{
			if(playerId != -1){
				try{
				player = pm.getPlayer(playerId);
				}catch(Exception ex){
					
				}
			}else if(playerName != null){
				try{
				player = pm.getPlayer(playerName);
				}catch(Exception ex){
					
				}
			}
			ZongPaiManager zpm = ZongPaiManager.getInstance();
			out.println(zpm.seizeCity(player,"piaomiaowangcheng")+"国家国王诞生成功");
		}catch(Exception ex){
			out.println("国家国王诞生没修改成功");
		}
	}
}

if(player != null){
	out.println(player.getName()+"在"+cm.得到国家名(player.getCountry())+"的职位为:"+cm.得到官职名(cm.官职(player.getCountry(),playerId))+"<br/>");
}else{
	out.println("现在还没有输入操作人员<br/>");
}
if(zhaojiDescription != null && !zhaojiDescription.trim().equals("")){
    String result = cm.召集令合法性判断(player);
    if(result == null){
    	cm.给地图上所有本国人发召集消息(player,zhaojiDescription);
    }else{
    	out.println(result);
    }
}
if(kingId != null && !kingId.trim().equals("")){
	try{
		long king = Long.parseLong(kingId);
		Player player1 = pm.getPlayer(king);
		Country country = cm.getCountryMap().get(player1.getCountry());
		if(country != null){
			if(country.getKingId() > 0){
				out.println(cm.得到国家名(player1.getCountry())+"现在还有国王，需国王下台后才可设定");
			}else{
				country.setKingId(king);
				out.println(player1.getName()+"已经成为"+cm.得到国家名(player1.getCountry())+"的国王了");
			}
		}else{
			out.println(player1.getName()+"属于"+cm.得到国家名(player1.getCountry()));
		}
	}catch(Exception ex){
		
	}
}
Hashtable<Byte,Country> countryMap = cm.countryMap;
if(countryMap != null){
	%>
	<table>
	<tr><td>国家编号</td><td>国家名</td><td>国家拥有人数(创建时的人数)</td><td>国王id</td><td>大司马</td><td>大将军</td><td>元帅</td><td>宰相</td><td>巡捕国王任命</td><td>御史大夫国王任命</td><td>巡捕元帅任命</td><td>御史大夫宰相任命</td><td>武曲</td><td>文曲</td><td>御林军</td><td>授勋</td><td>表彰</td><td>公告</td><td>镖局所属帮会id</td><td>国王金库</td></tr>
	<%
	if(countryMap != null && countryMap.keySet() != null){
		for(Byte i : countryMap.keySet()){
			Country country = countryMap.get(i);
			if(country != null){
				%>
				<tr><td><%=country.getId() %></td><td><%=country.getName() %></td>
				<td><%=country.getCount() %></td><td><%=country.getKingId() %></td><td><%=country.getDasimaId() %></td><td><%=country.getSeniorGeneralId() %></td><td><%=country.getMarshalId() %></td><td><%=country.getPrimeMinisterId() %></td><td><%=country.getPoliceByKingId() %></td><td><%=country.getYushidafuByKingId() %></td><td><%=country.getPoliceByMarshalId() %></td><td><%=country.getYushidafuByPrimeMinisterId() %></td><td><%=country.getHuguogongId() %></td><td><%=country.getFuguogongId() %></td><td><% if(country.getYulinjunIds() != null){
					for(long id : country.getYulinjunIds()){
						out.println(id+",");
					}
				} %></td>
				<td><% if(country.getShouxunIds() != null){
					for(long id : country.getShouxunIds()){
						out.println(id+",");
					}
				} %></td>
				<td><% if(country.getBiaozhangIds() != null){
					for(long id : country.getBiaozhangIds()){
						out.println(id+",");
					}
				} %></td>
				<td><%=country.getNotice() %></td>
				<td><%=(country.getBiaoju() != null ? country.getBiaoju().getJiazuId():"") %></td>
				<td><%=country.getKingMoney() %></td>
				</tr>
				<%
			}
		}
	}

	%>
		<tr><td colspan="3">输入人物id指派他当国王</td><td colspan="10"><form name="f2" method="post"><input type="text" name="kingId" id="kingId">&nbsp;<input type="submit" value="国王"></form></td></tr>
		<tr><td colspan="3">请输入召集内容</td><td colspan="10"><form name="f3" method="post"><input type="text" name="zhaojiDescription" id="zhaojiDescription">&nbsp;<input type="submit" value="发召集令">	</form><form name="f9" method="post"><input type="hidden" name="zhaoji" id="zhaoji" value="1">&nbsp;<input type="submit" value="使用召集功能">	</form></td></tr>
		<tr><td colspan="3">请输入囚禁人的id</td><td colspan="10"><form name="f4" method="post"><input type="text" name="qiujinId" id="qiujinId">&nbsp;<input type="submit" value="囚禁该人">	</form><form name="f5" method="post"><input type="hidden" name="qiujin" id="qiujin" value="1">&nbsp;<input type="submit" value="使用囚禁功能">	</form></td></tr>
		<tr><td colspan="3">请输入囚禁人的id</td><td colspan="10"><form name="f6" method="post"><input type="text" name="shifangId" id="shifangId">&nbsp;<input type="submit" value="释放该人">	</form><form name="f7" method="post"><input type="hidden" name="shifang" id="shifang" value="1">&nbsp;<input type="submit" value="使用释放功能">	</form></td></tr>
		<tr><td colspan="3">辞官</td><td colspan="10"><form name="f8" method="post"><input type="hidden" name="ciguan" id="ciguan" value="1">&nbsp;<input type="submit" value="辞官">	</form></td></tr>
		<tr><td colspan="3">发布国家公告</td><td colspan="10"><form name="f13" method="post"><input type="hidden" name="gonggao" id="gonggao" value="1">&nbsp;<input type="submit" value="公告">	</form></td></tr>
		<tr><td colspan="3">任命</td><td colspan="10"><form name="f10" method="post">
		<select name="renmingguanzhi">
		<option value="2">大司马
		<option value="3">大将军
		<option value="4">元帅
		<option value="5">宰相
		<option value="6">巡捕_国王
		<option value="7">巡捕_元帅
		<option value="8">御史大夫_国王
		<option value="9">御史大夫_宰相
		<option value="10">护国公
		<option value="11">辅国公
		<option value="12">御林卫队
		</select>
		<input type="submit" value="任命此官职">	</form></td></tr>
		<tr><td colspan="3">请输入罢免人id</td><td colspan="10"><form name="f11" method="post"><input type="text" name="bamianId" id="bamianId">&nbsp;<input type="submit" value="罢免该人"></form></td></tr>
		
		<tr><td colspan="3">撤销授勋表彰Id</td><td colspan="10"><form name="fchexiao" method="post"><input type="text" name="chexiaoId" id="chexiaoId">&nbsp;<input type="submit" value="撤销授勋表彰"></form></td></tr>
		
		<%if(player != null){
			Country country = cm.getCountryMap().get(player.getCountry());
			%>
		<tr><td colspan="3">投票</td><td colspan="10"><form name="f12" method="post">
		<select name="voteType">
		<option value="0">NPC处自发的投票
		<option value="1">官员发起的投票
		</select><br/>
		<%int allVote = country.currentAllVote; 
		if(allVote == 0){
			allVote = 1;
		}
		try{
		%>
		<input type="checkbox" name="guanyuantoupiao" value="0">国王<%=(country.getKingId() > 0 ? pm.getPlayer(country.getKingId()).getName() + " " + (country.currentVote[0]*100/allVote)+"%" + (country.currentVote[0]*100/allVote > country.yesterdayVote[0]*100 ? " ↑ " : (country.currentVote[0]*100/allVote < country.yesterdayVote[0]*100 ? " ↓ " : " ↔ ")) + "昨: " + country.yesterdayVote[0]+"%" : "") %><br/>
		<input type="checkbox" name="guanyuantoupiao" value="1">大司马<%=(country.getDasimaId() > 0 ? pm.getPlayer(country.getDasimaId()).getName() + " " + (country.currentVote[1]*100/allVote)+"%" + (country.currentVote[1]*100/allVote > country.yesterdayVote[1]*100 ? " ↑ " : (country.currentVote[1]*100/allVote < country.yesterdayVote[1]*100 ? " ↓ " : " ↔ ")) + "昨: " + country.yesterdayVote[1]+"%" : "") %><br/>
		<input type="checkbox" name="guanyuantoupiao" value="2">大将军<%=(country.getSeniorGeneralId() > 0 ? pm.getPlayer(country.getSeniorGeneralId()).getName() + " " + (country.currentVote[2]*100/allVote)+"%" + (country.currentVote[2]*100/allVote > country.yesterdayVote[2]*100 ? " ↑ " : (country.currentVote[2]*100/allVote < country.yesterdayVote[2]*100 ? " ↓ " : " ↔ ")) + "昨: " + country.yesterdayVote[2]+"%" : "") %><br/>
		<input type="checkbox" name="guanyuantoupiao" value="3">元帅<%=(country.getMarshalId() > 0 ? pm.getPlayer(country.getMarshalId()).getName() + " " + (country.currentVote[3]*100/allVote)+"%" + (country.currentVote[3]*100/allVote > country.yesterdayVote[3]*100 ? " ↑ " : (country.currentVote[3]*100/allVote < country.yesterdayVote[3]*100 ? " ↓ " : " ↔ ")) + "昨: " + country.yesterdayVote[3]+"%" : "") %><br/>
		<input type="checkbox" name="guanyuantoupiao" value="4">宰相<%=(country.getPrimeMinisterId() > 0 ? pm.getPlayer(country.getPrimeMinisterId()).getName() + " " + (country.currentVote[4]*100/allVote)+"%" + (country.currentVote[4]*100/allVote > country.yesterdayVote[4]*100 ? " ↑ " : (country.currentVote[4]*100/allVote < country.yesterdayVote[4]*100 ? " ↓ " : " ↔ ")) + "昨: " + country.yesterdayVote[4]+"%" : "") %><br/>
		<input type="checkbox" name="guanyuantoupiao" value="5">巡捕(国王)<%=(country.getPoliceByKingId() > 0 ? pm.getPlayer(country.getPoliceByKingId()).getName() + " " + (country.currentVote[5]*100/allVote)+"%" + (country.currentVote[5]*100/allVote > country.yesterdayVote[5]*100 ? " ↑ " : (country.currentVote[5]*100/allVote < country.yesterdayVote[5]*100 ? " ↓ " : " ↔ ")) + "昨: " + country.yesterdayVote[5]+"%" : "") %><br/>
		<input type="checkbox" name="guanyuantoupiao" value="6">御史大夫(国王)<%=(country.getYushidafuByKingId() > 0 ? pm.getPlayer(country.getYushidafuByKingId()).getName() + " " + (country.currentVote[6]*100/allVote)+"%" + (country.currentVote[6]*100/allVote > country.yesterdayVote[6]*100 ? " ↑ " : (country.currentVote[6]*100/allVote < country.yesterdayVote[6]*100 ? " ↓ " : " ↔ ")) + "昨: " + country.yesterdayVote[6]+"%" : "") %><br/>
		<input type="checkbox" name="guanyuantoupiao" value="7">巡捕(元帅)<%=(country.getPoliceByMarshalId() > 0 ? pm.getPlayer(country.getPoliceByMarshalId()).getName() + " " + (country.currentVote[7]*100/allVote)+"%" + (country.currentVote[7]*100/allVote > country.yesterdayVote[7]*100 ? " ↑ " : (country.currentVote[7]*100/allVote < country.yesterdayVote[7]*100 ? " ↓ " : " ↔ ")) + "昨: " + country.yesterdayVote[7]+"%" : "") %><br/>
		<input type="checkbox" name="guanyuantoupiao" value="8">御史大夫(宰相)<%=(country.getYushidafuByPrimeMinisterId() > 0 ? pm.getPlayer(country.getYushidafuByPrimeMinisterId()).getName() + " " + (country.currentVote[8]*100/allVote)+"%" + (country.currentVote[8]*100/allVote > country.yesterdayVote[8]*100 ? " ↑ " : (country.currentVote[8]*100/allVote < country.yesterdayVote[8]*100 ? " ↓ " : " ↔ ")) + "昨: " + country.yesterdayVote[8]+"%" : "") %><br/>
		<input type="submit" value="投票">	</form></td></tr>
			<%
		}catch(Exception e){
				out.println("我日，这家伙又删号了");
		}
		} %>
	
		<tr><td colspan="18">
	<form action="" method="post" name="f1">
	<select id="action" name="action">
	<option value="guojiameicizhaojirenshu">国家召集每次召集的人数
	<option value="zhiqu">国王每天支取上限
	<option value="guoyunzuizaokaishi">每天国运最早开始时间
	<option value="guoyunzuiwankaishi">每天国运最晚开始时间
	<option value="guotanzuizaokaishi">每天国探最早开始时间
	<option value="guotanzuiwankaishi">每天国探最晚开始时间
	<option value="guoyunshichang">国运时长
	<option value="guotanshichang">国探时长
	<option value="jingbiaokaishi">每天竞标开始时间
	<option value="jingbiaojieshu">每天竞标结束时间
	<option value="jingbiaoshengyuzijinlv">竞标剩余资金率
	<option value="biaocheyuwanjiazuidajuli">镖车与玩家的距离最大值
	<option value="guowanghuodebiaojuliyun">国王获得镖局利润率
	<option value="shuaxin">执行系统每日刷新测试每天凌晨刷新功能
	<option value="guowang">国王诞生
	</select>
	<input type="text" name="modifyValue" id="modifyValue"><input type="submit" value="提交">
	</form>
	</td></tr>
	<tr><td colspan="18">
		<%="国家召集每次召集的人数"+CountryManager.国家召集每次召集的人数 %>
	</td></tr>
	<tr><td colspan="18">
		<%="国王每天支取上限"+CountryManager.每天支取上限 %>
	</td></tr>
	<tr><td colspan="18">
		<%="每天国运最早开始时间"+CountryManager.每天国运最早开始时间 %>
	</td></tr>
	<tr><td colspan="18">
		<%="每天国运最晚开始时间"+CountryManager.每天国运最晚开始时间 %>
	</td></tr>
	<tr><td colspan="18">
		<%="每天国探最早开始时间"+CountryManager.每天国探最早开始时间 %>
	</td></tr>
	<tr><td colspan="18">
		<%="每天国探最晚开始时间"+CountryManager.每天国探最晚开始时间 %>
	</td></tr>
	<tr><td colspan="18">
		<%="国运时长"+CountryManager.国运时长 %>
	</td></tr>
	<tr><td colspan="18">
		<%="国探时长"+CountryManager.国探时长 %>
	</td></tr>
	<tr><td colspan="18">
		<%="每天竞标开始时间"+CountryManager.每天竞标开始时间 %>
	</td></tr>
	<tr><td colspan="18">
		<%="每天竞标结束时间"+CountryManager.每天竞标结束时间 %>
	</td></tr>
	<tr><td colspan="18">
		<%="竞标剩余资金率"+CountryManager.竞标剩余资金率 %>
	</td></tr>
	<tr><td colspan="18">
		<%="镖车与玩家的距离最大值"+CountryManager.镖车与玩家的距离最大值 %>
	</td></tr>
	<tr><td colspan="18">
		<%="国王获得镖局利润率"+CountryManager.国王获得镖局利润率 %>
	</td></tr>
	</table>
	<%
}
}catch(Exception ex){
	out.println("我日");
}
%>
</body>
</html>
