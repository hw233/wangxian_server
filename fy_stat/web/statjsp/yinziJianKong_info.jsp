<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.*,com.sqage.stat.commonstat.manager.*,
	com.sqage.stat.commonstat.manager.Impl.UserManagerImpl,com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.service.*,com.sqage.stat.model.Channel"
	%>

     <%
              String flag = null;
              flag = request.getParameter("flag");
    
	          String startDay = request.getParameter("day");
	          String endDay = request.getParameter("endDay");
	          String fenqu=request.getParameter("fenqu");
	          //String displayType=request.getParameter("displaytype");
	          //if(displayType==null){ displayType="1"; }
	
	          if("0".equals(fenqu)){fenqu=null;}
	
	          String today = DateUtil.formatDate(new Date(new Date().getTime()-24*60*60*1000),"yyyy-MM-dd");
	          if(startDay == null) startDay = today;
	          if(endDay == null) endDay = today;
	
	         Date t = DateUtil.parseDate(endDay,"yyyy-MM-dd");
	         Date s = DateUtil.parseDate(startDay,"yyyy-MM-dd");
	         ArrayList<String> dayList = new ArrayList<String>();

              UserManagerImpl userManager=UserManagerImpl.getInstance();
              ArrayList<String []> fenQuList= userManager.getFenQu(null);//获得现有的渠道信息
	          Transaction_SpecialManagerImpl transaction_SpecialManager = Transaction_SpecialManagerImpl.getInstance();
	       
	           String subSql="";
	           if(fenqu!=null){  subSql+=" and  t.fenqu ='"+fenqu+"'";  }
	           else
	           {
	           subSql+=" and  t.fenqu not in ('春暖花开','桃花仙岛','天下无双','海纳百川','琼楼金阙','飘渺仙道','万里苍穹','盛世欢歌','修罗转生')"; 
	           }
	            List<Transaction_yinzi> ls_yinDingChanChu=null;
	             List<Transaction_yinzi> ls_ChongZhi=null;
	            
	            
	           
	                  String sql_yinDingChanChu="select '11' createdate,'dd' fenqu,r.name REASONTYPE,'0' ACTION,sum(t.money) money "+
	                   " from stat_gamechongzhi_yinzi t,dt_reasontype r  where  r.name in "+
                            " ('邮件获取系统补单','邮件获取充值返利','邮件获取充值活动','充值活动奖励累计充值奖励返利','邮件获取GM工具补发',"
                            +"'邮件获取后台发送-系统奖励！','邮件获取后台发送-系统邮件','金钱道具使用碎银子','金钱道具使用银两','金钱道具使用银块',"
                            +"'金钱道具使用银条','金钱道具使用银锭','金钱道具使用银砖','竞技奖励银子竞技奖励银子','占领城市给的钱占领城市给的钱',"
                            +"'占领矿给的钱占领矿给的钱','10级奖励银子10级奖励银子','10级奖励银子20级奖励银子','10级奖励银子30级奖励银子',"
                            +"'10级奖励银子40级奖励银子','10级奖励银子50级奖励银子','10级奖励银子60级奖励银子','10级奖励银子70级奖励银子',"
                            +"'10级奖励银子80级奖励银子','10级奖励银子90级奖励银子')"+subSql+
	                           " and  t.createdate between '"+startDay+"' and '"+endDay+"'  and  to_char(t.reasontype)=to_char(r.id) group by r.name ";
	                         
	                         ls_yinDingChanChu=transaction_SpecialManager.getTransaction_yinziBySql(sql_yinDingChanChu);
	                            HashMap<String,String> map=new HashMap<String,String>();
	                       for(int num=0;num<ls_yinDingChanChu.size();num++){
	                            Transaction_yinzi transaction_yinzi= ls_yinDingChanChu.get(num);
	                             map.put(transaction_yinzi.getReasonType(),String.valueOf(transaction_yinzi.getMoney()));
	                             }
	                 
	                  String sql_ChongZhi="select '1' createdate ,'dd' fenqu,'银锭充值' REASONTYPE,'0' ACTION,round(sum(t.money)) money from stat_chongzhi t "+
	                              " where to_char(t.time,'YYYY-MM-DD') between '"+startDay+"' and  '"+endDay+"' " +subSql;
	                            
	                          ls_ChongZhi=transaction_SpecialManager.getTransaction_yinziBySql(sql_ChongZhi);
	                 
	    
	     //对比各个渠道数据
	       ArrayList<String> realChannelList = new ArrayList<String>();
	          realChannelList.add("类别名称");
	          realChannelList.add("2级分类");
	          realChannelList.add("获取原因");
	          realChannelList.add("银锭(文)");
	          
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
	</head>
	 <body>
		<center>
		<h2 style="font-weight:bold;">银子产出</h2>
		<form   method="post" id="form1">
		  <input type='hidden' name='flag' value='true'/>
		
		 开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		  结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
		    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
                </select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                
                                                 
		    	<input type='submit' value='提交'></form>
		    	<br/>
		    		
		    		<h3>银子充值</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'>");
					
		    		for(int c = 0 ; c < realChannelList.size() ; c++){
		    			out.print("<td>"+realChannelList.get(c)+"</td>");
		    		}
					out.println("</tr>");
					
					if ("true".equals(flag)) {  //如果是刚打开页面，不查询){
					
					%>
                     <tr bgcolor='#FFFFFF'><td  rowspan='2'>充值</td><td rowspan='2'>&nbsp;</td><td>充值</td><td><% out.print(StringUtil.addcommas(ls_ChongZhi.get(0).getMoney()*500));%></td></tr> 
                                                      <tr bgcolor='#FFFFFF'><td>系统补单</td><td><% out.print( StringUtil.addcommas(Long.parseLong(map.get("邮件获取系统补单")==null?"0":map.get("邮件获取系统补单"))));%></td></tr> 
						
						<tr bgcolor='#FFFFFF'><td  rowspan='3'>充返</td><td rowspan='3'>&nbsp;</td><td>充值返利</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("邮件获取充值返利")==null?"0":map.get("邮件获取充值返利"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>充值活动</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("邮件获取充值活动")==null?"0":map.get("邮件获取充值活动"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>累计充值奖励返利</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("充值活动奖励累计充值奖励返利")==null?"0":map.get("充值活动奖励累计充值奖励返利"))));%></td></tr>   
						
						
						<tr bgcolor='#FFFFFF'><td  rowspan='3'>后台手工</td><td rowspan='3'>&nbsp;</td><td>GM工具补发</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("邮件获取GM工具补发")==null?"0":map.get("邮件获取GM工具补发"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>后台发送-系统奖励！</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("邮件获取后台发送-系统奖励！")==null?"0":map.get("邮件获取后台发送-系统奖励！"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>后台发送-系统邮件</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("邮件获取后台发送-系统邮件")==null?"0":map.get("邮件获取后台发送-系统邮件"))));%></td></tr>       
						
						
						
						  <tr bgcolor='#FFFFFF'><td  rowspan='18'>系统产出</td><td rowspan='6'>使用金钱道具</td>
                 	                                                          <td>使用碎银子</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("金钱道具使用碎银子")==null?"0":map.get("金钱道具使用碎银子"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>使用银两</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("金钱道具使用银两")==null?"0":map.get("金钱道具使用银两"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>使用银块</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("金钱道具使用银块")==null?"0":map.get("金钱道具使用银块"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>使用银条</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("金钱道具使用银条")==null?"0":map.get("金钱道具使用银条"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>使用银锭</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("金钱道具使用银锭")==null?"0":map.get("金钱道具使用银锭"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>使用银砖</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("金钱道具使用银砖")==null?"0":map.get("金钱道具使用银砖"))));%></td></tr>  
                                            
                                                       <tr bgcolor='#FFFFFF'><td rowspan='1'>武圣竞技场</td>
                 	                                                          <td>武圣竞技场奖励</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("竞技奖励银子竞技奖励银子")==null?"0":map.get("竞技奖励银子竞技奖励银子"))));%></td></tr>  
                 	                                                          	
                 	                                      <tr bgcolor='#FFFFFF'>  <td rowspan='2'>城战矿战</td>
                 	                                                          <td>占领城市给的钱</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("占领城市给的钱占领城市给的钱")==null?"0":map.get("占领城市给的钱占领城市给的钱"))));%></td></tr>  
                 	                                                          	<tr bgcolor='#FFFFFF'><td>占领矿给的钱</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("占领矿给的钱占领矿给的钱")==null?"":map.get("占领矿给的钱占领矿给的钱"))));%></td></tr>  
                   
                                          <tr bgcolor='#FFFFFF'><td rowspan='9'>系统奖励</td>
                 	                                                          <td>10级奖励银子</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("10级奖励银子10级奖励银子")==null?"0":map.get("10级奖励银子10级奖励银子"))));%></td></tr>  
                  
                                                      <tr bgcolor='#FFFFFF'><td>20级奖励银子</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("10级奖励银子20级奖励银子")==null?"0":map.get("10级奖励银子20级奖励银子"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>30级奖励银子</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("10级奖励银子30级奖励银子")==null?"0":map.get("10级奖励银子30级奖励银子"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>40级奖励银子</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("10级奖励银子40级奖励银子")==null?"0":map.get("10级奖励银子40级奖励银子"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>50级奖励银子</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("10级奖励银子50级奖励银子")==null?"0":map.get("10级奖励银子50级奖励银子"))));%></td></tr>  
                                                     
                                                      <tr bgcolor='#FFFFFF'><td>60级奖励银子</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("10级奖励银子60级奖励银子")==null?"0":map.get("10级奖励银子60级奖励银子"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>70级奖励银子</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("10级奖励银子70级奖励银子")==null?"0":map.get("10级奖励银子70级奖励银子"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>80级奖励银子</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("10级奖励银子80级奖励银子")==null?"0":map.get("10级奖励银子80级奖励银子"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>90级奖励银子</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("10级奖励银子90级奖励银子")==null?"0":map.get("10级奖励银子90级奖励银子"))));%></td></tr>  
						
		               <%  
						}
					out.println("</table><br>");
		    		%>
		    		
		</center>
		<br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
