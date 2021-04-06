<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	com.xuanzhi.tools.text.*,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.*"
	%>
  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String fenqu=request.getParameter("fenqu");
	if("0".equals(fenqu)){fenqu=null;}
	String datetypes=request.getParameter("datetypes");
	if(datetypes==null){datetypes="1";}
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay =today;
	if(endDay == null) endDay = today;
	Date t = DateUtil.parseDate(endDay,"yyyy-MM-dd");
	ArrayList<String> dayList = new ArrayList<String>();

	 String[] daoJuNamelList = {"杏花村","屠苏酒","妙沁药酒","屠魔帖●降魔","屠魔帖●逍遥","屠魔帖●霸者","屠魔帖●朱雀","屠魔帖●水晶",
	              "屠魔帖●倚天","屠魔帖●青虹","屠魔帖●赤霄","屠魔帖●震天","屠魔帖●天罡","炼星符","灵兽内丹","鉴定符","麒麟精魂","青龙精魂",
	              "朱雀精魂","白虎精魂","玄武精魂","才子印记","绝品圣宠精魄","飞行坐骑碎片","宝石竹清(1级)","宝石幽橘(1级)","宝石枳香(1级)",
	              "宝石湛天(1级)","宝石墨轮(1级)","宝石竹清(2级)","宝石幽橘(2级)","宝石枳香(2级)","宝石湛天(2级)","宝石墨轮(2级)","角色装备(0~20)",
	              "角色装备(20~40)","角色装备(40~60)","角色装备(60~80)","角色装备(80~100)","角色装备(100~120)","角色装备(120~140)","角色装备(140~160)",
	              "角色装备(160~180)","角色装备(180~220)","角色装备(200~220)","角色装备(220~240)"};
	              
	    String [] colorList = {"白色","绿色","蓝色","紫色","完美紫色","橙色","完美橙色"};
	    String [] tranTypeList={"快速出售","摆摊出售"};  
          
             UserManagerImpl userManager=UserManagerImpl.getInstance();
             PlayGameManagerImpl playGameManager=PlayGameManagerImpl.getInstance();
             ArrayList<String []> fenQuList= userManager.getFenQu(null);//获得现有的分区信息
             TransactionManagerImpl transactionManager =TransactionManagerImpl.getInstance();
	    
	    ArrayList<String> realChannelList = new ArrayList<String>();
		if(request.getParameter("filterChannel") != null){
			String ss[] = request.getParameterValues("filterChannel");
			for(int i = 0 ; i < ss.length  ;i++){
				if(ss[i].trim().length() > 0){
					realChannelList.add(ss[i].trim());
				}
			}
		}
		String subSql=" ";
		
		 ArrayList<String> realColorList = new ArrayList<String>();
		if(request.getParameter("filterColor") != null){
			String ss[] = request.getParameterValues("filterColor");
			String subsql_col="";
			for(int i = 0 ; i < ss.length  ;i++){
				if(ss[i].trim().length() > 0){
					realColorList.add(ss[i].trim());
					if(i==ss.length-1){ subsql_col+="'"+ss[i].trim()+"'";}else{{subsql_col+="'"+ss[i].trim()+"',";} }
				}
			}
			subSql+=" and t.daojucolor in ("+subsql_col+") ";
		  }
		
		 ArrayList<String> realTransList = new ArrayList<String>();
		if(request.getParameter("tranType") != null){
			String ss[] = request.getParameterValues("tranType");
			String subsql_trans="";
			for(int i = 0 ; i < ss.length  ;i++){
				if(ss[i].trim().length() > 0){
					realTransList.add(ss[i].trim());
					if(i==ss.length-1){ subsql_trans+="'"+ss[i].trim()+"'";}else{{subsql_trans+="'"+ss[i].trim()+"',";} }
				}
			}
			subSql+=" and t.transactiontype in ("+subsql_trans+") ";
		  }
          if(fenqu!=null){ subSql+=" and t.fenqu='"+fenqu+"' ";}
		
		ArrayList<String []> ls=new ArrayList<String []>();
		if("1".equals(datetypes)){
		String sql="select  to_char(t.createdate,'YYYY-MM-DD') day,t.daojuname,t.daojucolor,sum(t.daojunum) count,round(sum(t.daojunum*t.danjia)/sum(t.daojunum))  menoy "
		      +" from stat_transaction  t where  t.createdate  between "+
		      "  to_date('"+startDay+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and to_date('"+endDay+" 23:59:59','YYYY-MM-DD hh24:mi:ss') "+subSql
		      +" group by  to_char(t.createdate,'YYYY-MM-DD'),t.daojuname,t.daojucolor "
		      +" order by to_char(t.createdate,'YYYY-MM-DD'),t.daojuname";
		String[] columusEnums={"day","daojuname","daojucolor","count","menoy"};
		 ls=transactionManager.getDaoJuinfo(sql,columusEnums);
		}
		else
		{
		String sql="select t.daojuname,t.daojucolor,sum(t.daojunum) count,round(sum(t.daojunum*t.danjia)/sum(t.daojunum))  menoy "
		      +" from stat_transaction  t where  t.createdate  between "+
		      "  to_date('"+startDay+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and to_date('"+endDay+" 23:59:59','YYYY-MM-DD hh24:mi:ss') "+subSql
		      +" group by  t.daojuname,t.daojucolor "
		      +" order by t.daojuname";
		String[] columusEnums={"daojuname","daojucolor","count","menoy"};
		 ls=transactionManager.getDaoJuinfo(sql,columusEnums);
		}
		
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
		<script type="text/javascript" src="<%=request.getContextPath()%>/comfile/js/calendar.js"></script>
<script language="JavaScript">
      function quanxuanabd(){
       var m = document.getElementsByName('filterChannel');
        var l = m.length;
        for ( var i=0; i< l; i++)
        {
            m[i].checked=true;
         }
      }
      function buxuanabd(){
        var m = document.getElementsByName('filterChannel');
        var l = m.length;
        for ( var i=0; i< l; i++)
        {
            m[i].checked=false;
         }
         }
         </script>
	</head>
	<body>
		<center>
		<h2 style="font-weight:bold;">
			 交易道具监控
		</h2>
		<form method="post">
		     开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		     结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
		 &nbsp;&nbsp;
		
		    分区：<select name="fenqu">
                       <option  value="0">全部</option>
                       <% 
                       for(int i = 0 ; i < fenQuList.size() ; i++){
                       String[] _fenqu = fenQuList.get(i);
                       %>
                        <option value="<%=_fenqu[1] %>" 
                        <%
                        if(_fenqu[1].equals(fenqu)){  out.print(" selected=\"selected\"");  }
                         %>
                          ><%=_fenqu[1] %></option>
                       <%
                       }
                       %>
                </select>&nbsp;&nbsp;
                
                            日期展示方式：<select name="datetypes">
                        <option value="1" 
                        <%
                        if("1".equals(datetypes)){ out.print(" selected=\"selected\"");  }
                         %>>日期展开</option>
                       <option value="2" 
                        <%
                        if("2".equals(datetypes)){ out.print(" selected=\"selected\"");    }
                         %>>日期合并</option>
                  </select>&nbsp;&nbsp;
		<br/>
		
		汇总道具选择：   <input type="button" name="quanxuanab" onclick="quanxuanabd();" value="全选" >
		    		   <input type="button" name="buxuanab" onClick="buxuanabd();" value="全不选" >
		<table width="800"><tr>
		    		<%
		    		for(int i = 0 ; i < daoJuNamelList.length ; i++){
		    		if(i>0&&i%9==0){out.print("</tr><tr>");}
		    			String _channel = daoJuNamelList[i];
		    			
		    			if(realChannelList.contains(_channel)){
		    				out.print("<td align='left'><input type='checkbox' id='filterChannel' name='filterChannel' value='"+_channel+"' checked />"+_channel+"&nbsp;</td>");
		    			}else{
		    				out.print("<td align='left'><input type='checkbox' id='filterChannel' name='filterChannel' value='"+_channel+"'/>"+_channel+"&nbsp;</td>");
		    			}
		    		}
		    		%>
		    		</tr>
		    		</table>
		    		  
		      颜色选择：
		    <table width="800"><tr>
		    		<%
		    		for(int i = 0 ; i < colorList.length ; i++){
		    		if(i>0&&i%9==0){out.print("</tr><tr>");}
		    			String _color = colorList[i];
		    			
		    			if(realColorList.contains(_color)){
		    				out.print("<td align='left'><input type='checkbox' id='filterColor' name='filterColor' value='"+_color+"' checked />"+_color+"&nbsp;</td>");
		    			}else{
		    				out.print("<td align='left'><input type='checkbox' id='filterColor' name='filterColor' value='"+_color+"'/>"+_color+"&nbsp;</td>");
		    			}
		    		}
		    		%>
		    		</tr>
		    		</table>
		    		  
		     交易方式选择：
		    <table width="800"><tr>
		    		<%
		    		for(int i = 0 ; i < tranTypeList.length ; i++){
		    		if(i>0&&i%9==0){out.print("</tr><tr>");}
		    			String _trans = tranTypeList[i];
		    			
		    			if(realTransList.contains(_trans)){
		    				out.print("<td align='left'><input type='checkbox' id='tranType' name='tranType' value='"+_trans+"' checked />"+_trans+"&nbsp;</td>");
		    			}else{
		    				out.print("<td align='left'><input type='checkbox' id='tranType' name='tranType' value='"+_trans+"'/>"+_trans+"&nbsp;</td>");
		    			}
		    		}
		    		%>
		    		</tr>
		    		</table>
		    		  
		    		<br/><input type='submit' value='提交'></form>
		    		<h3>道具交易查询</h3>
		    		<%
		    		if("1".equals(datetypes)){}
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					if("1".equals(datetypes)){
					out.print("<tr bgcolor='#EEEEBB'><td>日期</td><td>道具名称</td><td>品质</td><td>交易量</td><td>平均价格（两）</td></tr>");
					}else{
		    		out.print("<tr bgcolor='#EEEEBB'><td>道具名称</td><td>品质</td><td>交易量</td><td>平均价格（两）</td></tr>");
		    		}
		    		
		    		
					for(int i = 0 ; i < ls.size() ; i++){
						String[] datas= ls.get(i);
						String strs=datas[0];
						if("1".equals(datetypes))
						{
						strs=datas[1];
						}
						
						
						if(realChannelList.indexOf(strs)!=-1){
						out.print("<tr bgcolor='#FFFFFF'>");
						
						if("1".equals(datetypes)){
						out.print("<td bgcolor='#EEEEBB'>"+datas[0]+"</td><td>"+datas[1]+"</td><td>"+datas[2]+"</td><td>"+ datas[3]+"</td><td>"+Float.parseFloat(datas[4])/1000+"</td>");
						}else
						{
						out.print("<td bgcolor='#EEEEBB'>"+datas[0]+"</td><td>"+datas[1]+"</td><td>"+ datas[2]+"</td><td>"+Float.parseFloat(datas[3])/1000+"</td>");
						}
						out.println("</tr>");
						}
					}
					out.println("</table>");
		    		%>
		    		
		    		
		</center>
	</body>
</html>
