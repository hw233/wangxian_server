<%@ page contentType="text/html;charset=utf-8"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Equipment"%><html xmlns="http://www.w3.org/1999/xhtml">
<META http-equiv=Content-Type content="text/html; charset=utf-8">
<%
String name4 [] = {"无双●皓月阴阳鼎","无双●神宝幽焚蛇兜","无双●神宝幽焚蟒衣","无双●神宝幽焚蛛巾","无双●神宝幽焚蝎腕","无双●神宝幽焚蜂靴","无双●神宝幽焚蚣玉","无双●神宝幽焚魂戒","无双●神宝幽焚天蚕","无双●神宝幽焚螟灵","无双●百鬼噬魂鼎","无双●天宝幽焚蛇兜","无双●天宝幽焚蟒衣","无双●天宝幽焚蛛巾","无双●天宝幽焚蝎腕","无双●天宝幽焚蜂靴","无双●天宝幽焚蚣玉","无双●天宝幽焚魂戒","无双●天宝幽焚天蚕","无双●天宝幽焚螟灵","无双●万妖招魂鼎","无双●至宝幽焚蛇兜","无双●至宝幽焚蟒衣","无双●至宝幽焚蛛巾","无双●至宝幽焚蝎腕","无双●至宝幽焚蜂靴","无双●至宝幽焚蚣玉","无双●至宝幽焚魂戒","无双●至宝幽焚天蚕","无双●至宝幽焚螟灵","无双●地狱邪魂鼎","无双●无极幽焚蛇兜","无双●无极幽焚蟒衣","无双●无极幽焚蛛巾","无双●无极幽焚蝎腕","无双●无极幽焚蜂靴","无双●无极幽焚蚣玉","无双●无极幽焚魂戒","无双●无极幽焚天蚕","无双●无极幽焚螟灵","无双●九幽泣血鼎","无双●鸿蒙幽焚蛇兜","无双●鸿蒙幽焚蟒衣","无双●鸿蒙幽焚蛛巾","无双●鸿蒙幽焚蝎腕","无双●鸿蒙幽焚蜂靴","无双●鸿蒙幽焚蚣玉","无双●鸿蒙幽焚魂戒","无双●鸿蒙幽焚天蚕","无双●鸿蒙幽焚螟灵","无双●九黎镇天鼎","无双●混沌幽焚蛇兜","无双●混沌幽焚蟒衣","无双●混沌幽焚蛛巾","无双●混沌幽焚蝎腕","无双●混沌幽焚蜂靴","无双●混沌幽焚蚣玉","无双●混沌幽焚魂戒","无双●混沌幽焚天蚕","无双●混沌幽焚螟灵","无双●乾坤灭魔鼎","无双●乾坤幽焚蛇兜","无双●乾坤幽焚蟒衣","无双●乾坤幽焚蛛巾","无双●乾坤幽焚蝎腕","无双●乾坤幽焚蜂靴","无双●乾坤幽焚蚣玉","无双●乾坤幽焚魂戒","无双●乾坤幽焚天蚕","无双●乾坤幽焚螟灵",
		"无双●万魂邪皇匕","无双●神宝影刹面甲","无双●神宝影刹轻甲","无双●神宝影刹肩甲","无双●神宝影刹手甲","无双●神宝影刹长靴","无双●神宝影刹符咒","无双●神宝影刹刃环","无双●神宝影刹天邪","无双●神宝影刹血眼","无双●饕餮噬魂刃","无双●天宝影刹面甲","无双●天宝影刹轻甲","无双●天宝影刹肩甲","无双●天宝影刹手甲","无双●天宝影刹长靴","无双●天宝影刹符咒","无双●天宝影刹刃环","无双●天宝影刹天邪","无双●天宝影刹血眼","无双●封魔化灵刺","无双●至宝影刹面甲","无双●至宝影刹轻甲","无双●至宝影刹肩甲","无双●至宝影刹手甲","无双●至宝影刹长靴","无双●至宝影刹符咒","无双●至宝影刹刃环","无双●至宝影刹天邪","无双●至宝影刹血眼","无双●无极天道匕","无双●无极影刹面甲","无双●无极影刹轻甲","无双●无极影刹肩甲","无双●无极影刹手甲","无双●无极影刹长靴","无双●无极影刹符咒","无双●无极影刹刃环","无双●无极影刹天邪","无双●无极影刹血眼","无双●梵音释天刺","无双●鸿蒙影刹面甲","无双●鸿蒙影刹轻甲","无双●鸿蒙影刹肩甲","无双●鸿蒙影刹手甲","无双●鸿蒙影刹长靴","无双●鸿蒙影刹符咒","无双●鸿蒙影刹刃环","无双●鸿蒙影刹天邪","无双●鸿蒙影刹血眼","无双●鸿蒙傲天刃","无双●混沌影刹面甲","无双●混沌影刹轻甲","无双●混沌影刹肩甲","无双●混沌影刹手甲","无双●混沌影刹长靴","无双●混沌影刹符咒","无双●混沌影刹刃环","无双●混沌影刹天邪","无双●混沌影刹血眼","无双●乾坤禁魔刺","无双●乾坤影刹面甲","无双●乾坤影刹轻甲","无双●乾坤影刹肩甲","无双●乾坤影刹手甲","无双●乾坤影刹长靴","无双●乾坤影刹符咒","无双●乾坤影刹刃环","无双●乾坤影刹天邪","无双●乾坤影刹血眼"};
int num4 [] = {125,122,130,129,128,126,124,127,121,123,145,142,150,149,148,146,144,147,141,143,165,162,170,169,168,166,164,167,161,163,185,182,190,189,188,186,184,187,181,183,205,202,210,209,208,206,204,207,201,203,225,222,230,229,228,226,224,227,221,223,245,242,250,249,248,246,244,247,241,243,
		125,122,130,129,128,126,124,127,121,123,145,142,150,149,148,146,144,147,141,143,165,162,170,169,168,166,164,167,161,163,185,182,190,189,188,186,184,187,181,183,205,202,210,209,208,206,204,207,201,203,225,222,230,229,228,226,224,227,221,223,245,242,250,249,248,246,244,247,241,243};
if(name4.length != num4.length){
	out.print("长度不符<br>");
	return;
}
for(int i=0;i<name4.length;i++){
	Article a = ArticleManager.getInstance().getArticle(name4[i]);
	if(a == null){
		out.print("物品不存在："+name4[i]+"<br>");
		return;
	}
	int oldL = a.getArticleLevel();
	a.setArticleLevel(num4[i]);
	out.print("[装备名："+a.getName()+"] [老的装备数值等级："+oldL+"] [修改后的数值等级："+a.getArticleLevel()+"]<br>");
}
%>