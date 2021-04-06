<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.fy.boss.gm.korea.KoreaTranslateManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String 账号信息查询 = "账号信息查询";
	String 充值信息查询 = "充值信息查询";
	String 帮助信息 = "帮助信息";
	String GM邮件 = "GM邮件";
	String 服务器监控 = "服务器监控";
	String 发送公告 = "发送公告";
	String 后台提交 = "后台提交";
	String 账号查询 = "账号查询";
	String 订单查询 = "订单查询";
	String FAQ添加 = "FAQ添加";
	String FAQ查询 = "FAQ查询";
	String 活动添加 = "活动添加";
	String 活动查询 = "活动查询";
	String GM邮件排队 = "GM邮件排队";
	String 邮件统计 = "邮件统计";
	String 邮件查询 = "邮件查询";
	String 设置编号 = "设置编号";
	String 暂离时间查询 = "暂离时间查询";
	String 邮件设置 = "邮件设置";
	String 服务器聊天监控 = "服务器聊天监控";
	String 服务器在线监控 = "服务器在线监控";
	String 活动公告 = "活动公告";
	String 系统公告 = "系统公告";
	String 登陆公告 = "登陆公告";
	String 定时公告 = "定时公告";
	String 提交新问题 = "提交新问题";
	String 综合查询 = "综合查询";
	String 处理问题 = "处理问题";
	String 已处理查询 = "已处理查询";
	
	String istran = request.getParameter("istran");
	if(istran==null){
		istran = "nocommit";
	}  
	if(istran!=null && istran.equals("korea")){
		KoreaTranslateManager ktm = KoreaTranslateManager.getInstance();
		Map<String, String> translateMap = ktm.getTranslateMap();
	  	if(translateMap!=null && translateMap.size()>0){
	  			账号信息查询 = translateMap.get("账号信息查询");
	  			充值信息查询 = translateMap.get("充值信息查询");
	  			帮助信息 = translateMap.get("帮助信息");
	  			GM邮件 = translateMap.get("GM邮件");
	  			服务器监控 = translateMap.get("服务器监控");
	  			发送公告 = translateMap.get("发送公告");
	  			后台提交 = translateMap.get("后台提交");
	  			账号查询 = translateMap.get("账号查询");
	  			订单查询 = translateMap.get("订单查询");
	  			FAQ添加 = translateMap.get("FAQ添加");
	  			FAQ查询 = translateMap.get("FAQ查询");
	  			活动添加 = translateMap.get("活动添加");
	  			活动查询 = translateMap.get("活动查询");
	  			GM邮件排队 = translateMap.get("GM邮件排队");
	  			邮件统计 = translateMap.get("邮件统计");
	  			邮件查询 = translateMap.get("邮件查询");
	  			设置编号 = translateMap.get("设置编号");
	  			邮件设置 = translateMap.get("邮件设置");
	  			暂离时间查询 = translateMap.get("暂离时间查询");
	  			服务器聊天监控 = translateMap.get("服务器聊天监控");
	  			服务器在线监控 = translateMap.get("服务器在线监控");
	  			活动公告 = translateMap.get("活动公告");
	  			系统公告 = translateMap.get("系统公告");
	  			登陆公告 = translateMap.get("登陆公告");
	  			定时公告 = translateMap.get("定时公告");
	  			提交新问题 = translateMap.get("提交新问题");
	  			综合查询 = translateMap.get("综合查询");
	  			处理问题 = translateMap.get("处理问题");
	  			已处理查询 = translateMap.get("已处理查询");
	  	}
	}
%>