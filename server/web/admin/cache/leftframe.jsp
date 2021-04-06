<%@ page contentType="text/html;charset=utf-8" import="java.util.*"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="css/dtree.css" type="text/css" rel="stylesheet" />
<script src="js/dtree.js" type=text/javascript></script>

<style type="text/css">
<!--
body,td,th {
	font-size: 14px;
}
body {
	margin-left: 5px;
	margin-top: 5px;
	margin-right: 0px;
	margin-bottom: 0px;
	background-color: #DDEDFF;
}
-->
</style></head>
<body>
<div align="center" class="dtree">
<a href="javascript:%20d.openAll();" class="node">全部展开</a> |
<a href="javascript:%20d.closeAll();" class="node">全部折叠</a><br/><br/>
</div>
<SCRIPT type=text/javascript>
		<!--

		d = new dTree('d');

		d.add(0,-1,'Cache System');
		d.add(1,0,'缓存状态','');
		//d.add(11,1,'玩家一级','playercache1.jsp');
		d.add(12,1,'角色二级缓存','status/playercache2.jsp');
		//d.add(13,1,'装备一级','admin/cache/articlecache1.jsp');
		d.add(14,1,'物品二级缓存','status/articlecache2.jsp');

		d.add(2,0,'系统状态','');
		d.add(21,2,'线程情况','thread/threadDump_1_5.jsp?s=&n=');
		d.add(22,2,'服务器LAMP连接','transport/conns.jsp?bean=cache_system_server');
		d.add(23,2,'DB连接池','../dbpool/poolinfo.jsp');
		
		document.write(d);

		//-->
	</SCRIPT>
<div></div></body></html>
