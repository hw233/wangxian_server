<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.fy.engineserver.datasource.props.*,com.google.gson.*,java.util.*,com.fy.engineserver.sprite.*,
com.fy.engineserver.sprite.concrete.*,java.io.*,java.lang.reflect.*,
com.xuanzhi.tools.text.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="../css/common.css" />
<style type="text/css">
.tablestyle1{
width:96%;
border:0px solid #69c;
border-top:1px solid #69c;
border-right:1px solid #69c;
border-bottom:1px solid #69c;
border-left:1px solid #69c;
border-collapse: collapse;
}
.tablestyle2{
width:100%;
border:0px solid #69c;
border-top:0px solid #69c;
border-right:0px solid #69c;
border-bottom:0px solid #69c;
border-left:0px solid #69c;
border-collapse: collapse;
}
td{
border:1px solid #69c;
}
.tdtable{
padding: 0px;
margin:0px;
border-top:0px solid #69c;
border-right:0px solid #69c;
border-bottom:0px solid #69c;
border-left:0px solid #69c;
}
.lefttd{
border-left:0px solid #69c;
}
.toptd{
border-top:0px solid #69c;
}
.righttd{
border-right:0px solid #69c;
}
.bottomtd{
border-bottom:0px solid #69c;
}
</style>
<title>法宝</title>
<%

%>
</head>
<body>
<br>
	<%
String fid = request.getParameter("fid");
MagicWeapon mw = null;
if(fid != null) {
	MagicWeaponManager mwm = MagicWeaponManager.getInstance();
	mw = mwm.getMagicWeapon(Long.parseLong(fid));
}
	%>
<form action="fabao.jsp" method=get>
	法宝id:<input type=text size=12 name=fid>
	<input type=submit value="确定" name=sub1>
</form>
<br>
<%if(mw != null) {
	LinkedHashMap<String,String> lmap = new LinkedHashMap<String,String>();
	lmap.put("ID", "" + mw.getId());
	lmap.put("名称", mw.getName());
	lmap.put("拥有人", ""+mw.getOwnerId());
	lmap.put("颜色", Gem.COLOR_TYPE_NAMES[mw.getColor()]);
	lmap.put("等级", "" + mw.getLevel());
	lmap.put("星级", "" + mw.getStar());
	lmap.put("品阶", "" + mw.getQuality());
	lmap.put("基础力量","" + mw.getBasicStrength());
	lmap.put("基础敏捷", "" + mw.getBasicDexterity());
	lmap.put("基础智力", "" + mw.getBasicSpellpower());
	lmap.put("基础耐力","" + mw.getBasicConstitution());
	lmap.put("当前力量","" + mw.getStrength());
	lmap.put("当前敏捷", "" + mw.getDexterity());
	lmap.put("当前智力", "" + mw.getSpellpower());
	lmap.put("当前耐力","" + mw.getConstitution());
	lmap.put("当前等级经验","" + mw.getExp());
	lmap.put("下一级剩余经验", "" + mw.getNextLevelExp());
	lmap.put("物品id", "" + mw.getArticleEntity().getId());
	lmap.put("掌握技能",StringUtil.arrayToString(mw.getSkillIds(),","));
	lmap.put("诞生时间", DateUtil.formatDate(mw.getBornTime(),"yyyy-MM-dd HH:mm:ss"));
	if(mw.getLastUpdatetime() != null) {
		lmap.put("上次更新时间", DateUtil.formatDate(mw.getLastUpdatetime(),"yyyy-MM-dd HH:mm:ss"));
	}
	String keys[] = lmap.keySet().toArray(new String[0]);
%>
		<table class="tablestyle1">
		
		<tr bgcolor="#C2CAF5" align="center">
		<td>属性</td><td>值</td>
		</tr>
		<%for(String key : keys) {%>
  			<tr bgcolor="#FFFFFF">
  				<td align="center" style="word-wrap: break-word;">
  				<%=key %>:
  				</td>
  				<td align="center">
  				<%=lmap.get(key) %>
  				</td>
			</tr>
		<%} %>
		</table>
<%} %>
<br/>

</body>
</html>
