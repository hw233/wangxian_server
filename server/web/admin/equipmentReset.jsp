<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.fy.engineserver.datasource.props.*,com.google.gson.*,java.util.*,com.fy.engineserver.sprite.*,
com.fy.engineserver.sprite.concrete.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>物品</title>
<%!
public static   int 智力 = 0;
public static   int 耐力 = 1;
public static   int 力量 = 2;
public static   int 敏捷 = 3;
public static   int 生命值 = 4;
public static   int 内力值 = 5;
public static   int 内功强度 = 6;
public static   int 外功强度 = 7;
public static   int 法力回复 = 8;
public static   int 生命回复 = 9;
public static   int 反伤几率 = 10;
public static   int 吸血几率 = 11;
public static   int 命中等级 = 12;
public static   int 闪避等级 = 13;
public static   int 暴击等级 = 14;
public static   int 移动速度 = 15;
public static   int 外功防御 = 16;
public static   int 内功防御 = 17;
public static   int 外功减伤 = 18;
public static   int 内功减伤 = 19;

public static final int BASE_NUM = 20;
public static  int 强化4 = 0;
public static  int 强化8 = 1;
public static  int 强化12 = 2;
public static  int 强化16 = 3;

public static IntProperty[] ips = new IntProperty[]{
	new IntProperty("spellpowerB","智力"),
	new IntProperty("constitutionB","耐力"),
	new IntProperty("strengthB","力量"),
	new IntProperty("dexterityB","敏捷"),
	new IntProperty("totalHpB","生命值"),
	new IntProperty("totalMpB","内力值"),
	new IntProperty("spellAttackIntensityB","内功强度"),
	new IntProperty("meleeAttackIntensityB","外功强度"),
	new IntProperty("mpRecoverBaseB","每5秒内力回复"),
	new IntProperty("hpRecoverBaseB","每5秒生命回复"),
	new IntProperty("ironMaidenPercent","反伤"),
	new IntProperty("hpStealPercent","吸血"),
	new IntProperty("attackRatingB","命中等级"),
	new IntProperty("dodgeB","闪避等级"),
	new IntProperty("criticalHitB","暴击等级"),
	new IntProperty("speedC","移动速度"),
	new IntProperty("defenceB","外功防御"),
	new IntProperty("resistanceB","内功防御"),
	new IntProperty("defenceD","外功减伤"),
	new IntProperty("resistanceD","内功减伤")};
%>
<%
PlayerManager sm = PlayerManager.getInstance();
String action = request.getParameter("action");
long playerId = -1;
String playerName = null;
Object obj = session.getAttribute("playerid");
Object obj2 = session.getAttribute("playerName");
if(obj != null){
	playerId = Long.parseLong(obj.toString());
}
if(obj2 != null){
	playerName = obj2.toString();
}
Player player = null;
String errorMessage = null;

if (action == null) {
	if (errorMessage == null) { 
		if(playerId != -1){
			player = sm.getPlayer(playerId);
			if (player == null) {
				errorMessage = "ID为" + playerId + "的玩家不存在！";
			}
		}else if(playerName != null){
			player = sm.getPlayer(playerName);
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
		player = sm.getPlayer(playerId);
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
		player = sm.getPlayer(playerName);
		if (player == null) {
			errorMessage = "角色名为" + playerName + "的玩家不存在！";
		}else{
			session.setAttribute("playerName",request.getParameter("playerName"));
			playerId = player.getId();
			session.setAttribute("playerid",playerId);
		}
	}
}else if(action != null && action.equals("modify")){
	try {
		playerId = Long.parseLong(request
				.getParameter("playerid"));
	} catch (Exception e) {
		errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
	}
	if (errorMessage == null) {
		player = sm.getPlayer(playerId);
		if (player == null) {
			errorMessage = "ID为" + playerId + "的玩家不存在！";
		}else{
			session.setAttribute("playerid",request.getParameter("playerid"));
			playerName = player.getName();
			session.setAttribute("playerName",playerName);
		}
	}
	if(errorMessage == null){
		long articleId = 0;
		try{
			articleId = Long.parseLong(request.getParameter("articleId"));
		}catch(Exception ex){
			out.println("请输入数字");
		}
		ArticleEntity ae = player.getArticleEntity(articleId);
		if(ae != null){
			int count = player.getKnapsack().getArticleCellCount(ae.getArticleName());
			if(count != 0){
				int aindex = player.getKnapsack().getArticleCellPos(ae.getArticleName());
				ArticleEntity aeTemp = player.getKnapsack().getArticleEntityByCell(aindex);
				try{
					int strong4 = Integer.parseInt(request.getParameter("strong4"));
					int strong8 = Integer.parseInt(request.getParameter("strong8"));
					int strong12 = Integer.parseInt(request.getParameter("strong12"));
					int strong16 = Integer.parseInt(request.getParameter("strong16"));
					int value4 = Integer.parseInt(request.getParameter("value4"));
					int value8 = Integer.parseInt(request.getParameter("value8"));
					int value12 = Integer.parseInt(request.getParameter("value12"));
					int value16 = Integer.parseInt(request.getParameter("value16"));
					IntProperty[] ipss = new IntProperty[4];
					ipss[0] = (IntProperty)ips[strong4].clone();
					ipss[0].setFieldValue(value4);
					ipss[1] = (IntProperty)ips[strong8].clone();
					ipss[1].setFieldValue(value8);
					ipss[2] = (IntProperty)ips[strong12].clone();
					ipss[2].setFieldValue(value12);
					ipss[3] = (IntProperty)ips[strong16].clone();
					ipss[3].setFieldValue(value16);
					if(aeTemp instanceof EquipmentEntity){
						EquipmentEntity ee = (EquipmentEntity)aeTemp;
						((EquipmentEntity)aeTemp).setAddPlanIPS(ipss);
						StringBuffer sb = new StringBuffer();
						sb.append("<color="+0xff8400+">强化: "+ee.getLevel()+"/"+16+"</color><br>");
						int activeLevels[] = EquipmentColumn.EQUIPMENT_STRENGTHEN_ACTIVE_LEVELS;
						IntProperty addPlanIps[] = ee.getAddPlanIPS();
						boolean[] addPlanFlags = ee.getAddPlanFlags();
						for(int i = 0 ; addPlanIps != null && i < addPlanIps.length  ;i++){
							if(addPlanIps[i] != null && i < activeLevels.length && addPlanFlags[i]){
								
								String name = addPlanIps[i].getFieldName();

								int al = activeLevels[i];
								if(al > ee.getLevel() ){
									if("hpStealPercent".equals(addPlanIps[i].getFieldName()) || "skillDamageDecreaseRate".equals(addPlanIps[i].getFieldName()) || "ironMaidenPercent".equals(addPlanIps[i].getFieldName()) || "speedC".equals(addPlanIps[i].getFieldName()) || "physicalDecrease".equals(addPlanIps[i].getFieldName()) || "spellDecrease".equals(addPlanIps[i].getFieldName())){
										sb.append(" <color="+0x8f8f8f+">+"+al+" "+addPlanIps[i].getComment()+"提高"+addPlanIps[i].getFieldValue()+"%</color><br>");
									}else{
										sb.append(" <color="+0x8f8f8f+">+"+al+" "+addPlanIps[i].getComment()+"提高"+addPlanIps[i].getFieldValue()+"点</color><br>");
									}
								}else{
									if("hpStealPercent".equals(addPlanIps[i].getFieldName()) || "skillDamageDecreaseRate".equals(addPlanIps[i].getFieldName()) || "ironMaidenPercent".equals(addPlanIps[i].getFieldName()) || "speedC".equals(addPlanIps[i].getFieldName()) || "physicalDecrease".equals(addPlanIps[i].getFieldName()) || "spellDecrease".equals(addPlanIps[i].getFieldName())){
										sb.append(" +"+al+" "+addPlanIps[i].getComment()+"提高"+addPlanIps[i].getFieldValue()+"%<br>");
									}else{
										sb.append(" +"+al+" "+addPlanIps[i].getComment()+"提高"+addPlanIps[i].getFieldValue()+"点<br>");
									}
								}
							}
						}
						out.println(sb+"<br>");
					}
					
				}catch(Exception ex){
					out.println("请填写正确内容");
				}
			}else{
				out.println("必须放到背包中才能进行此操作");
			}
		}
	}
}
%>
</head>
<body>
<%if(errorMessage != null){
	out.println(errorMessage);
} %>
<form id='f1' name='f1'><input type='hidden' name='action' value='select_playerId'>
请输入要用户的ID：<input type='text' name='playerid' id='playerid' value='<%=playerId %>' size='20'><input type='submit' value='提  交'>
</form>
<form id='f01' name='f01'><input type='hidden' name='action' value='select_playerName'>
请输入要角色名：<input type='text' name='playerName' id='playerName' value='<%=playerName %>' size='20'><input type='submit' value='提  交'>
</form>
<form name="form1">
<input type="hidden" name="action" value="modify">
<input type="hidden" name="playerid" value="<%=playerId %>">
装备id<input type="text" name="articleId"><br>
强化4<select name="strong4">
<option value="<%=智力 %>">智力
<option value="<%=耐力 %>">耐力
<option value="<%=力量 %>">力量
<option value="<%=敏捷 %>">敏捷
<option value="<%=生命值 %>">生命值
<option value="<%=内力值 %>">内力值
<option value="<%=内功强度 %>">内功强度
<option value="<%=外功强度 %>">外功强度
<option value="<%=法力回复 %>">法力回复
<option value="<%=生命回复 %>">生命回复
<option value="<%=反伤几率 %>">反伤几率
<option value="<%=吸血几率 %>">吸血几率
<option value="<%=命中等级 %>">命中等级
<option value="<%=闪避等级 %>">闪避等级
<option value="<%=暴击等级 %>">暴击等级
<option value="<%=移动速度 %>">移动速度
<option value="<%=外功防御 %>">外功防御
<option value="<%=内功防御 %>">内功防御
<option value="<%=外功减伤  %>">外功减伤
<option value="<%=内功减伤  %>">内功减伤
</select><input type="text" name="value4"><br>
强化8<select name="strong8">
<option value="<%=智力 %>">智力
<option value="<%=耐力 %>">耐力
<option value="<%=力量 %>">力量
<option value="<%=敏捷 %>">敏捷
<option value="<%=生命值 %>">生命值
<option value="<%=内力值 %>">内力值
<option value="<%=内功强度 %>">内功强度
<option value="<%=外功强度 %>">外功强度
<option value="<%=法力回复 %>">法力回复
<option value="<%=生命回复 %>">生命回复
<option value="<%=反伤几率 %>">反伤几率
<option value="<%=吸血几率 %>">吸血几率
<option value="<%=命中等级 %>">命中等级
<option value="<%=闪避等级 %>">闪避等级
<option value="<%=暴击等级 %>">暴击等级
<option value="<%=移动速度 %>">移动速度
<option value="<%=外功防御 %>">外功防御
<option value="<%=内功防御 %>">内功防御
<option value="<%=外功减伤  %>">外功减伤
<option value="<%=内功减伤  %>">内功减伤
</select><input type="text" name="value8"><br>
强化12<select name="strong12">
<option value="<%=智力 %>">智力
<option value="<%=耐力 %>">耐力
<option value="<%=力量 %>">力量
<option value="<%=敏捷 %>">敏捷
<option value="<%=生命值 %>">生命值
<option value="<%=内力值 %>">内力值
<option value="<%=内功强度 %>">内功强度
<option value="<%=外功强度 %>">外功强度
<option value="<%=法力回复 %>">法力回复
<option value="<%=生命回复 %>">生命回复
<option value="<%=反伤几率 %>">反伤几率
<option value="<%=吸血几率 %>">吸血几率
<option value="<%=命中等级 %>">命中等级
<option value="<%=闪避等级 %>">闪避等级
<option value="<%=暴击等级 %>">暴击等级
<option value="<%=移动速度 %>">移动速度
<option value="<%=外功防御 %>">外功防御
<option value="<%=内功防御 %>">内功防御
<option value="<%=外功减伤  %>">外功减伤
<option value="<%=内功减伤  %>">内功减伤
</select><input type="text" name="value12"><br>
强化16<select name="strong16">
<option value="<%=智力 %>">智力
<option value="<%=耐力 %>">耐力
<option value="<%=力量 %>">力量
<option value="<%=敏捷 %>">敏捷
<option value="<%=生命值 %>">生命值
<option value="<%=内力值 %>">内力值
<option value="<%=内功强度 %>">内功强度
<option value="<%=外功强度 %>">外功强度
<option value="<%=法力回复 %>">法力回复
<option value="<%=生命回复 %>">生命回复
<option value="<%=反伤几率 %>">反伤几率
<option value="<%=吸血几率 %>">吸血几率
<option value="<%=命中等级 %>">命中等级
<option value="<%=闪避等级 %>">闪避等级
<option value="<%=暴击等级 %>">暴击等级
<option value="<%=移动速度 %>">移动速度
<option value="<%=外功防御 %>">外功防御
<option value="<%=内功防御 %>">内功防御
<option value="<%=外功减伤  %>">外功减伤
<option value="<%=内功减伤  %>">内功减伤
</select><input type="text" name="value16"><br>
<input type="submit" value="提交">
</form>
</body>
</html>
