<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.fy.boss.gm.korea.KoreaTranslateManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String 账号信息查询 = "账号信息查询";
	String 充值信息查询 = "充值信息查询";
	String 发送公告 = "发送公告";
	String 语言切换 = "语言切换";
	String 账号查询 = "账号查询";
	String 订单查询 = "订单查询";
	String 活动公告 = "活动公告";
	String 系统公告 = "系统公告";
	String 登陆公告 = "登陆公告";
	String 定时公告 = "定时公告";
	String  账号 = "账号";
	String  查询 = "查询";
	String  通过角色名反查帐号名 = "通过角色名反查帐号名";
	String 查看GM操作记录 = "查看GM操作记录";
	String 基本信息 = "基本信息";
	String 用户名 = "用户名";
	String 注册时间 = "注册时间";
	String 最后一次登录时间 = "最后一次登录时间";
	String 密保问题 = "密保问题";
	String 密保答案 = "密保答案";
	String 最后一次充值时间 = "最后一次充值时间";
	String 注册机型 = "注册机型";
	String 账号状态 = "账号状态";
	String 是否为封停状态 = "是否为封停状态";
	String 高级操作 = "高级操作";
	String 修改密码 = "修改密码";String 封停 = "封停";
	String 清空密保信息 = "清空密保信息";
	String 授权信息 = "授权信息";
	String 授权状态 = "授权状态";
	String 授权方式 = "授权方式";
	String 授权时间 = "授权时间";
	String 最后登录时间 = "最后登录时间";
	String 登录次数 = "登录次数";
	String 请求授权次数 = "请求授权次数";
	String 客户端ID = "客户端ID";
	String IP地址 = "IP地址";
	String MAC地址 = "MAC地址";
	String 平台 = "平台";
	String 授权客户端ID = "授权客户端ID";
	String GPU型号 = "机型";
	String 客户端创建时间 = "客户端创建时间";
	String 机型 = "机型";
	String 删除 = "删除";
	String 角色列表 = "角色列表";
	String 韩国测试服 = "韩国测试服";
	String 角色ID = "角色ID";
	String 角色名 = "角色名";
	String 职业 = "职业";
	String 等级 = "等级";
	String 充值额度 = "充值额度";
	String VIP等级 = "VIP等级";
	String 充值订单查询 = "充值订单查询";
	String 按订单号查询 = "按订单号查询";
	String 按玩家帐号查询 = "按玩家帐号查询";
	String 订单号 = "订单号";
	String 用户 = "用户";
	String 服务器 = "服务器";
	String 额度 = "额度(元)";
	String 充值渠道 = "充值渠道";
	String 充值方式 = "充值方式";
	String 充值卡号 = "充值卡号";
	String 当前状态 = "当前状态";
	String 订单时间 = "订单时间";
	String 共 = "共";
	String 条 = "条 ";
	String 页 = "页";
	String 测试服 = "测试服";
	String 公告填写人 = "公告填写人";
	String 公告名称 = "公告名称";
	String 公告内容 = "公告内容";
	String 公告开启时间 = "公告开启时间";
	String 公告失效时间 = "公告失效时间";
	String 全选 = "全选";
	String 发送 = "发送";
	String 类型 = "类型";
	String 系统滚动消息 = "系统滚动消息";
	String 系统提示消息 = "系统提示消息";
	String 系统电视消息 = "系统电视消息";
	String 世界频道 = "世界频道";
	String 彩色世界频道 = "彩色世界频道";
	String 消息 = "消息";
	String 是否有绑银 = "是否有绑银";
	String 绑银数量 = "绑银数量";
	String 请选择服务器 = "请选择服务器";
	String 如果是多天用英文逗号隔开 = "如果是多天，用英文逗号隔开";
	String 设置时间和间隔 = "设置时间和间隔";
	String 发公告人 = "发公告人";
	String 滚动次数 = "滚动次数";
	String 轮播间隔 = "轮播间隔";
	String 消息间隔 = "消息间隔";
	String 时间哪一天 = "时间哪一天";
	String 起始时间 = "起始时间";
	String 结束时间 = "结束时间";
	String 分钟 = "分钟";
	String 小时 = "小时";
	String 设定 = "设定";
	String 清空 = "清空";String 设置滚屏公告内容 = "设置滚屏公告内容";
	String 历史记录 = "历史记录";
	String 取消全选 = "取消全选";
	String 添加成功 = "添加成功";
	String 必填项不能为空 = "必填项不能为空";
	String 设置失败 = "设置失败，请找相关人员查看错误原因！";
	String 公告 = "公告";
	String 活动 = "活动";
	
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
	  			发送公告 = translateMap.get("发送公告");
	  			账号查询 = translateMap.get("账号查询");
	  			订单查询 = translateMap.get("订单查询");
	  			活动公告 = translateMap.get("活动公告");
	  			系统公告 = translateMap.get("系统公告");
	  			登陆公告 = translateMap.get("登陆公告");
	  			定时公告 = translateMap.get("定时公告");
	  			账号 = translateMap.get("账号");
	  			通过角色名反查帐号名 = translateMap.get("通过角色名反查帐号名");
	  			查看GM操作记录 = translateMap.get("查看GM操作记录");
	  			基本信息 = translateMap.get("基本信息");
	  			用户名 = translateMap.get("用户名");
	  			注册时间  = translateMap.get("注册时间");
	  			最后一次登录时间  = translateMap.get("最后一次登录时间");
	  			密保问题  = translateMap.get("密保问题");
	  			密保答案  = translateMap.get("密保答案");
	  			最后一次充值时间  = translateMap.get("最后一次充值时间");
	  			注册机型  = translateMap.get("注册机型");
	  			账号状态  = translateMap.get("账号状态");
	  			高级操作  = translateMap.get("高级操作");
	  			是否为封停状态  = translateMap.get("是否为封停状态");
	  			修改密码  = translateMap.get("修改密码");封停  = translateMap.get("封停");
	  			清空密保信息  = translateMap.get("清空密保信息");
	  			授权信息  = translateMap.get("授权信息");
	  			授权状态  = translateMap.get("授权状态");
	  			授权方式  = translateMap.get("授权方式");
	  			授权时间  = translateMap.get("授权时间");
	  			最后登录时间  = translateMap.get("最后登录时间");
	  			登录次数  = translateMap.get("登录次数");
	  			请求授权次数  = translateMap.get("请求授权次数");
	  			客户端ID  = translateMap.get("客户端ID");
	  			IP地址  = translateMap.get("IP地址");
	  			MAC地址  = translateMap.get("MAC地址");
	  			平台  = translateMap.get("平台");
	  			机型  = translateMap.get("机型");
	  			GPU型号  = translateMap.get("GPU型号");
	  			客户端创建时间  = translateMap.get("客户端创建时间");
	  			授权客户端ID  = translateMap.get("授权客户端ID");
	  			删除  = translateMap.get("删除");
	  			角色列表  = translateMap.get("角色列表");韩国测试服  = translateMap.get("韩国测试服");
	  			角色ID  = translateMap.get("角色ID");
	  			角色名  = translateMap.get("角色名");
	  			职业  = translateMap.get("职业");
	  			充值渠道  = translateMap.get("充值渠道");
	  			等级  = translateMap.get("等级");
	  			充值额度  = translateMap.get("充值额度");
	  			充值方式  = translateMap.get("充值方式");
	  			VIP等级  = translateMap.get("VIP等级");
	  			充值订单查询  = translateMap.get("充值订单查询");
	  			按订单号查询  = translateMap.get("按订单号查询");
	  			按玩家帐号查询  = translateMap.get("按玩家帐号查询");
	  			订单号  = translateMap.get("订单号");
	  			用户  = translateMap.get("用户");
	  			服务器  = translateMap.get("服务器");
	  			额度  = translateMap.get("额度(元)");
	  			当前状态  = translateMap.get("当前状态");
	  			充值卡号  = translateMap.get("充值卡号");
	  			订单时间  = translateMap.get("订单时间");
	  			共  = translateMap.get("共");
	  			条  = translateMap.get("条");
	  			页  = translateMap.get("页");
	  			测试服  = translateMap.get("测试服");
	  			公告填写人  = translateMap.get("公告填写人");
	  			公告名称  = translateMap.get("公告名称");
	  			公告内容  = translateMap.get("公告内容");
	  			公告开启时间  = translateMap.get("公告开启时间");
	  			公告失效时间  = translateMap.get("公告失效时间");
	  			全选  = translateMap.get("全选");
	  			发送  = translateMap.get("发送");
	  			类型  = translateMap.get("类型");
	  			系统滚动消息  = translateMap.get("系统滚动消息");
	  			系统提示消息  = translateMap.get("系统提示消息");
	  			系统电视消息  = translateMap.get("系统电视消息");
	  			世界频道  = translateMap.get("世界频道");
	  			彩色世界频道  = translateMap.get("彩色世界频道");
	  			消息  = translateMap.get("消息");
	  			是否有绑银  = translateMap.get("是否有绑银");
	  			绑银数量  = translateMap.get("绑银数量");
	  			请选择服务器  = translateMap.get("请选择服务器");
	  			如果是多天用英文逗号隔开  = translateMap.get("如果是多天，用英文逗号隔开");
	  			设置时间和间隔  = translateMap.get("设置时间和间隔");
	  			发公告人  = translateMap.get("发公告人");
	  			滚动次数  = translateMap.get("滚动次数");
	  			轮播间隔  = translateMap.get("轮播间隔");
	  			消息间隔  = translateMap.get("消息间隔");
	  			时间哪一天  = translateMap.get("时间哪一天");
	  			起始时间  = translateMap.get("起始时间");
	  			结束时间  = translateMap.get("结束时间");
	  			分钟  = translateMap.get("分钟");
	  			小时  = translateMap.get("小时");
	  			设定  = translateMap.get("设定");
	  			清空  = translateMap.get("清空");
	  			设置滚屏公告内容  = translateMap.get("设置滚屏公告内容");
	  			历史记录  = translateMap.get("历史记录");
	  			取消全选  = translateMap.get("取消全选");
	  			添加成功  = translateMap.get("添加成功");
	  			设置失败  = translateMap.get("设置失败，请找相关人员查看错误原因！");
	  			必填项不能为空  = translateMap.get("必填项不能为空");
	  			公告 = "공지";
	  			活动 = "이벤트";
	  	}
	}
%>