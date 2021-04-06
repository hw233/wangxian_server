<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.sqage.stat.server.*,java.text.SimpleDateFormat,java.util.Date"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%
	String numS = request.getParameter("num");
	if (numS != null && !"null".equals(numS)&& !"".equals(numS)) {
		DaoJuService.startnum = Integer.valueOf(numS);
	} else {
	}
		
	 String nummohu = request.getParameter("nummohu");
	if (nummohu != null && !"null".equals(nummohu)&& !"".equals(nummohu)) {
		DaoJu_MoHuService.startnum = Integer.valueOf(nummohu);
	} else {
	}
		
		
		
	String numgamechongzhi = request.getParameter("numgamechongzhi");
	if (numgamechongzhi != null && !"null".equals(numgamechongzhi)&& !"".equals(numgamechongzhi)) {
		GameChongZhiService.startnum = Integer.valueOf(numgamechongzhi);
	} else {
	}
	String numgamechongzhijx = request.getParameter("numgamechongzhijx");
	if (numgamechongzhijx != null && !"null".equals(numgamechongzhijx)&& !"".equals(numgamechongzhijx)) {
		GameChongZhi_JingXiService.startnum = Integer.valueOf(numgamechongzhijx);
	} else {
	}
	
	
	
	
	String onlinusercount = request.getParameter("onlinusercount");
	if (onlinusercount != null && !"null".equals(onlinusercount)&& !"".equals(onlinusercount)) {
		OnLineUsersService.startnum = Integer.valueOf(onlinusercount);
	} else {
	}
	String transcationcount = request.getParameter("transcationcount");
	if (transcationcount != null && !"null".equals(transcationcount)&& !"".equals(transcationcount)) {
		TransactionService.startnum = Integer.valueOf(transcationcount);
	} else {
	}
	
	String yinzikucun = request.getParameter("yinzikucun");
	if (yinzikucun != null && !"null".equals(yinzikucun)&& !"".equals(yinzikucun)) {
		YinZiKuCunService.startnum = Integer.valueOf(yinzikucun);
	} else {
	}
	
	
	 String targetTime = request.getParameter("targetTime");
	 if (targetTime != null && !"null".equals(targetTime)&& !"".equals(targetTime)) {
	 	GameChongZhiService.targetTime = Long.valueOf(targetTime);
	 } else {
	 }
	
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'countnum.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  <body>
  <h2>本功能是开发人员专用功能，非技术人员不准使用。</h2>
	<form action="" method="post">
	        <table align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>
	     <tr bgcolor='#EEEEBB'>
	        <td> 道具队列最小处理数据量，默认是4000 </td>
	        <td>  <input type="text" name="num" value="<%=numS %>"> 	</td>
		    <td>当前值: <%=DaoJuService.startnum %>	</td>
	     </tr>
		
		
		    <tr bgcolor='#EEEEBB'>
	        <td> 模糊道具队列最小处理数据量，默认是2000 </td>
	        <td>  <input type="text" name="nummohu" value="<%=nummohu %>"> 	</td>
		    <td>当前值: <%=DaoJu_MoHuService.startnum %>	</td>
	     </tr>
		
		 <tr bgcolor='#EEEEBB'>
	        <td> 游戏币队列最小处理数据量，默认是4000 </td>
	        <td>  <input type="text" name="numgamechongzhi" value="<%=numgamechongzhi %>"> 	</td>
		    <td>当前值：<%=GameChongZhiService.startnum %> </td>
	    </tr>
	    
	     <tr bgcolor='#EEEEBB'>
	        <td> 游戏币队列精细（积分）最小处理数据量，默认是300 </td>
	        <td>  <input type="text" name="numgamechongzhijx" value="<%=numgamechongzhijx %>"> 	</td>
		    <td>当前值：<%=GameChongZhi_JingXiService.startnum %> </td>
	    </tr>
	    
	    
	     <tr bgcolor='#EEEEBB'>
	        <td> 在线用户队列最小处理数据量，默认是4000 </td>
	        <td>  <input type="text" name="onlinusercount" value="<%=onlinusercount %>"> 	</td>
		    <td>当前值：<%=OnLineUsersService.startnum %> </td>
	    </tr>
	    
	     <tr bgcolor='#EEEEBB'>
	        <td> 交易队列最小处理数据量，默认是4000 </td>
	        <td>  <input type="text" name="transcationcount" value="<%=transcationcount %>"> 	</td>
		    <td>当前值：<%=TransactionService.startnum %> </td>
	    </tr>
	    
	    <tr bgcolor='#EEEEBB'>
	        <td> 银子库存队列最小处理数据量，默认是10 </td>
	        <td>  <input type="text" name="yinzikucun" value="<%=yinzikucun %>"> 	</td>
		    <td>当前值：<%=YinZiKuCunService.startnum %> </td>
	    </tr>
	    
	    
	    <% SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// System.out.println(sf.format(new Date(1333967912166L))); %>
	      <tr bgcolor='#EEEEBB'>
	        <td> 开始使用字典表的时间，默认是1332691200000L; <% out.println(sf.format(new Date(1332691200000L))); %> </td>
	        <td>  <input type="text" name="targetTime" value="<%=targetTime %>"> 	</td>
		   
		    <td>当前值：<%=GameChongZhiService.targetTime %>  <% out.println(sf.format(new Date(GameChongZhiService.targetTime))); %></td>
	         
	    </tr>
	    
		</table>
		<input type="submit" value="OK" align='center' > 
	</form>    
    
  </body>
</html>
