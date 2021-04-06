<%@page import="com.fy.engineserver.activity.ActivitySubSystem"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.ArticleProperty"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.CareerPackageProps"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@page import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@page import="com.fy.engineserver.activity.activeness.ActivenessManager"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.menu.Option_ExchangeSliver_Salary"%>
<%@page import="com.fy.engineserver.menu.Option"%>
<%@page import="com.fy.engineserver.menu.MenuWindow"%>
<%@page import="com.fy.engineserver.menu.WindowManager"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.shop.Goods"%>
<%@page import="com.fy.engineserver.shop.Shop"%>
<%@page import="com.fy.engineserver.shop.ShopManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%
	String [] names = {"VIP1级宝箱","VIP2级宝箱","VIP3级宝箱"};
	int career[] = { 0, 1, 2, 3 };

	if(PlatformManager.getInstance().isPlatformOf(Platform.官方)==false){
		out.print("<font color='red'>此服务器不在修改范围!!</font>");
		return;
	}	

	String pwd = ParamUtils.getParameter(request, "pwd");
	if(pwd!=null && pwd.trim().length()>0){
		if(pwd.trim().equals("范冰冰")){
			CareerPackageProps[] allCareerPackageProps = ArticleManager.getInstance().getAllCareerPackageProps();
			for(CareerPackageProps cp:allCareerPackageProps){
				for(String name : names){
					if(cp!=null && name!=null){
						if(cp.getName().equals(name)){
							ArticleProperty[][] articleNames = cp.getArticleNames();
							if(articleNames==null){
								out.print("===============[后台设置 职业礼包 开出物品绑定方式] [失败:articleNames==null] [礼包："+name+"] ============<br>");
								continue;
							}
							cp.setOpenBindType((byte)0);
							out.print("[后台设置 职业礼包 开出物品绑定方式] [成功] [礼包："+name+"] <br>");
							ActivitySubSystem.logger.warn("[后台设置 职业礼包 开出物品绑定方式] [成功] [礼包："+name+"] ");
						}						
					}					
				}				
			}
			
			out.print("<hr>");
			String shopName = "全部道具";
			Shop shop = ShopManager.getInstance().getShops().get(shopName);
			if (shop == null) {
				out.print(shopName + ",不存在");
				return;
			}
			
			List<Goods> oldgs = shop.getGoods(); 
			for(int i=0;i<oldgs.size();i++){
				if("灵兽内丹".equals(oldgs.get(i).getArticleName())){
					if(oldgs.get(i).getFixTimeEndLimit()>0){
						out.print("灵兽内丹<font color='yellow'>修改之前</font>的结束时间是:"+TimeTool.formatter.varChar23.format(oldgs.get(i).getFixTimeEndLimit())+"<br>");
						oldgs.get(i).setFixTimeEndLimit(TimeTool.formatter.varChar19.parse("2014-03-31 23:59:59"));
						out.print("灵兽内丹 修改之后的结束时间是:"+TimeTool.formatter.varChar23.format(oldgs.get(i).getFixTimeEndLimit())+"<br><hr>");
						ActivitySubSystem.logger.warn("[后台修改全部道具灵兽内丹时间] [ok] [结束时间："+(TimeTool.formatter.varChar23.format(oldgs.get(i).getFixTimeEndLimit()))+"] ");
					}
				}
			}
		}else{
			out.print("<font color = 'red'>请出示正确的验证码！</font>");
		}
	}
	

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改职业宝箱开出物品的绑定方式</title>
</head>
<body>
<form action="">
	<table>
		<tr><th>验证码：</th><td><input type='text' name="pwd" value=""/></td></tr>
		<tr><td><input type="submit" value='确认'></td></tr>
	</table>
</form>
</body>
</html>