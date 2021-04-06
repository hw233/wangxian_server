<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,com.xuanzhi.tools.text.*,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
    com.sqage.stat.commonstat.manager.Impl.*"
	%>
	<%!
	Object lock = new Object(){};
      %>
  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String huoBiType=request.getParameter("huoBiType");
	String fenqu=request.getParameter("fenqu");
	String wupintype=request.getParameter("wupintype");
	String gamelevel = request.getParameter("gamelevel");
	
	if("0".equals(huoBiType)){huoBiType=null;}
	if("0".equals(fenqu)){fenqu=null;}
	if("0".equals(wupintype)){wupintype=null;}
	if(gamelevel == null) gamelevel = "全部";
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	/**
	*获得渠道信息
	**/
	ArrayList<String> huoBiTypeList=null;
	ArrayList<String> daojuList=null;
	
              UserManagerImpl userManager=UserManagerImpl.getInstance();
              DaoJuManagerImpl daoJuManager=DaoJuManagerImpl.getInstance();
              huoBiTypeList=daoJuManager.getHuoBiType(null);
              daojuList=daoJuManager.getDaoju(null);
              ArrayList<String []> fenQuList= userManager.getFenQu(null);//获得现有的分区信息
             
             String lost="LOST','发送邮件删除','合成删除','鉴定删除','铭刻删除','强化删除','商店出售删除','死亡掉落";
              String gamelevel_search=null;
              if(!"全部".equals(gamelevel)){gamelevel_search=gamelevel;}
		      List goumaiList=daoJuManager.getDaoJuXiaoHaoFenBu(startDay,endDay,fenqu,huoBiType,gamelevel_search,wupintype,lost);
		      
%>
		
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet" href="../css/style.css" />
		<link rel="stylesheet" href="../css/atalk.css" />
		<script language="javascript" type="text/javascript" src="../js/flotr/lib/prototype-1.6.0.2.js"></script>
		<script language="javascript" type="text/javascript" src="../js/flotr/flotr-0.2.0-alpha.js"></script>

	</head>
	<body>
		<center>
		<h2 style="font-weight:bold;">
			道具消耗等级分布
		</h2>
		<form  method="post">开始日期：<input type='text' name='day' value='<%=startDay %>'>
		-- 结束日期：<input type='text' name='endDay' value='<%=endDay %>'>(格式：2012-02-09)
		<br/><br/>
		       货币类型：<select name="huoBiType">
                       <option  value="0">全部</option>
                       <% 
                       for(int i = 0 ; i < huoBiTypeList.size() ; i++){
                       String _huoBiType = huoBiTypeList.get(i);
                       %>
                        <option value="<%=_huoBiType %>" 
                        <%
                        if(_huoBiType.equals(huoBiType)){
                        out.print(" selected=\"selected\"");
                        }
                         %>
                          ><%=_huoBiType %></option>
                       <%
                       }
                       %>
                </select> &nbsp;&nbsp;
                                             分区：<select name="fenqu">
                       <option  value="0">全部</option>
                       <% 
                       for(int i = 0 ; i < fenQuList.size() ; i++){
                       String[] _fenqu = fenQuList.get(i);
                       %>
                        <option value="<%=_fenqu[1] %>" 
                        <%
                        if(_fenqu[1].equals(fenqu)){ out.print(" selected=\"selected\""); }
                         %>
                          ><%=_fenqu[1] %></option>
                       <%
                       }
                       %>
                </select>&nbsp;&nbsp;
                
                                            物品类型：<select name="wupintype">
                       <option  value="0">全部</option>
                       <% 
                       for(int i = 0 ; i < daojuList.size() ; i++){
                       String _wupintype = daojuList.get(i);
                       %>
                        <option value="<%=_wupintype %>" 
                        <%
                        if(_wupintype.equals(wupintype)){
                        out.print(" selected=\"selected\"");
                        }
                         %>
                          ><%=_wupintype %></option>
                       <%
                       }
                       %>
                </select>&nbsp;&nbsp;
                
                                            等级：<input type='text' name='gamelevel' value='<%=gamelevel %>'>&nbsp;&nbsp;
		    		<input type='submit' value='提交'></form>
		    		
		    		<h3>道具消耗等级分布</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>等级</td><td>消费账号数</td><td>消费金额</td><td>人均消费</td><td>消费占比</td>"+
					                                             "<td>新增消费账号数</td><td>新增消费金额</td><td>新增人均消费</td><td>新增消费占比</td></tr>");
					Long oyanbao=0L;
					Long ocount=0L;
					Long yanbao=0L;
					Long count=0L;
					for(int i = 0 ; i < goumaiList.size() ; i++){
					String [] goumai=(String[])goumaiList.get(i);
					if(goumai[2]!=null){oyanbao+=(long)Float.parseFloat(goumai[2]);}
					if(goumai[4]!=null){yanbao+=(long)Float.parseFloat(goumai[4]);}
					if(goumai[1]!=null){ocount+=(long)Float.parseFloat(goumai[1]);}
					if(goumai[3]!=null){count+=(long)Float.parseFloat(goumai[3]);}
					}
					for(int i = 0 ; i < goumaiList.size() ; i++){
					String [] goumai=(String[])goumaiList.get(i);
					Long ozhanbi=0L;
					Long zhanbi=0L;
					Long orenjun=0L;
					Long renjun=0L;
					
					if(oyanbao!=0&&goumai[2]!=null){ozhanbi=((long)Float.parseFloat(goumai[2])*100)/oyanbao;}
					if(yanbao!=0&&goumai[4]!=null){zhanbi=((long)Float.parseFloat(goumai[4])*100)/yanbao;}
					
					if(goumai[1]!=null&&!"0".equals(goumai[1])&&goumai[2]!=null){orenjun=(long)Float.parseFloat(goumai[2])/(long)Float.parseFloat(goumai[1]);}
					if(goumai[3]!=null&&!"0".equals(goumai[3])&&goumai[4]!=null){renjun=(long)Float.parseFloat(goumai[4])/(long)Float.parseFloat(goumai[3]);}
					
						out.print("<tr bgcolor='#FFFFFF'><td>"+(long)Float.parseFloat(goumai[0])+"</td><td>"+(long)Float.parseFloat(goumai[1])+"</td><td>"+(long)Float.parseFloat(goumai[2])+"</td><td>"+orenjun+"&nbsp;</td><td>"+ozhanbi+"% &nbsp;</td>"+
						                                 "<td>"+(long)Float.parseFloat(goumai[3])+"</td><td>"+(long)Float.parseFloat(goumai[4])+"</td><td>"+renjun+"&nbsp;</td><td>"+zhanbi+"% &nbsp;</td>");
						out.println("</tr>");
					}
					
					out.print("<tr bgcolor='#EEEEBB'><td>总计</td><td>"+ocount+"</td><td>"+oyanbao+"</td><td>&nbsp;</td><td>&nbsp;</td>"+
					                                             "<td>"+count+"</td><td>"+yanbao+"</td><td>&nbsp;</td><td>&nbsp;</td></tr>");
					out.println("</table><br>");
		    		%>
		    		
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		 
		</center>
		<br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
