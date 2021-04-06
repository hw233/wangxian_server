<%@page import="com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.fy.gamegateway.thirdpartner.ThirdPartDataEntityManager"%>
<%@page import="com.fy.gamegateway.thirdpartner.ThirdPartDataEntity"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>金山点击数据统计</title>
</head>
<body>
<%!
	


	/*
		//判断是否是同一台机器
		规则1：同一个idfa代表同一台机器 如果相同则不记录
		规则2:若没有idfa 则以mac为准排除02:00:00:00:00:00
		这两个规则以外的皆认为无效点击
	*/
	public boolean isInsertDb(ThirdPartDataEntity dataEntity) throws Exception
	{
		ThirdPartDataEntityManager thirdPartDataEntityManager = null;
		try
		{
			thirdPartDataEntityManager = ThirdPartDataEntityManager.getInstance();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		long now = System.currentTimeMillis();
		//查询是否存在这个用户
		String whereSql = null;
		String value = null;
		if(!StringUtils.isEmpty(dataEntity.getIdfa()))
		{
			whereSql = " idfa = ? ";
			value = dataEntity.getIdfa();
		}
		else if(!StringUtils.isEmpty(dataEntity.getMac()) && !"020000000000".equals(dataEntity.getMac()))
		{
			whereSql = " mac = ? ";
			value = dataEntity.getMac();
		}
		
		if(whereSql != null && value != null)
		{
			List<ThirdPartDataEntity> lst = thirdPartDataEntityManager.queryForWhere(whereSql, new Object[]{value});
			if(lst == null || lst.size() == 0)
			{
				return true;
			}
		}
		if(MieshiGatewaySubSystem.logger.isInfoEnabled())
			MieshiGatewaySubSystem.logger.info("[金山统计] [判断是否插入数据库] [不插入数据库] "+dataEntity.getLogString() +" [cost:"+(System.currentTimeMillis()-now)+"ms]");
		return false;
	}

%>
<%
	//加一个key 做一个基本验证
/* 	String key = request.getParameter("sign");
	if(!"".equals(key))
	{
		return;
	} */
	
	long now = System.currentTimeMillis();
	/*
		金山点击数据上传 规则是一台物理机如果重复 则不算
	*/
	String ckey = request.getParameter("ckey");
	String clickid = request.getParameter("clickid");
	String udid = request.getParameter("udid");
	String mac = request.getParameter("mac");
	
	String idfa = request.getParameter("idfa");
	
	String ip = request.getParameter("ip");
	String v = request.getParameter("v");
	String dm = request.getParameter("dm");
	String dv = request.getParameter("dv");
	
	ThirdPartDataEntity thirdPartDataEntity = new ThirdPartDataEntity();
	thirdPartDataEntity.setQudaohao(ckey);
	thirdPartDataEntity.setClickid(clickid);
	thirdPartDataEntity.setOpenudid(udid);
	thirdPartDataEntity.setMac(mac);
	thirdPartDataEntity.setIdfa(idfa);
	thirdPartDataEntity.setIp(ip);
	thirdPartDataEntity.setV(v);
	thirdPartDataEntity.setDm(dm);
	thirdPartDataEntity.setDv(dv);
	
	if(StringUtils.isEmpty(ckey) || StringUtils.isEmpty(clickid) || (StringUtils.isEmpty(idfa) && StringUtils.isEmpty(mac)))
	{
		if(MieshiGatewaySubSystem.logger.isInfoEnabled())
			MieshiGatewaySubSystem.logger.info("[金山统计] [失败] [发现不合法的数据] "+thirdPartDataEntity.getLogString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
		return;
	}
	
	ThirdPartDataEntityManager thirdPartDataEntityManager = ThirdPartDataEntityManager.getInstance();
	if(isInsertDb(thirdPartDataEntity))
	{
		long time = System.currentTimeMillis();
		thirdPartDataEntity.setCreateTime(time);
		thirdPartDataEntity.setUpdateTime(time);
		thirdPartDataEntity.setStatus(ThirdPartDataEntity.DATA_WAIT_VALID);
		thirdPartDataEntityManager.saveOrUpdate(thirdPartDataEntity,false,new String[0]);
		if(MieshiGatewaySubSystem.logger.isInfoEnabled())
			MieshiGatewaySubSystem.logger.info("[金山统计] [插入数据库] [成功] "+thirdPartDataEntity.getLogString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
		
	}
	


%>
</body>
</html>