<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	com.xuanzhi.tools.text.*,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.commonstat.entity.*"
	%>
	<%!
	Object lock = new Object(){};
      %>
  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String displayType=request.getParameter("displaytype");
	
	String type=request.getParameter("type");
	   if(displayType==null){ displayType="1"; }
	   if(type==null){type="1";}
	   
	
	ArrayList<String []> fenQuList  =  (ArrayList<String []>) session.getAttribute("FENQU_LIST_0");
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay =today;
	if(endDay == null) endDay =today;
	Date t = DateUtil.parseDate(endDay,"yyyy-MM-dd");
	Date s = DateUtil.parseDate(startDay,"yyyy-MM-dd");
	ArrayList<String> dayList = new ArrayList<String>();
	
            UserManagerImpl userManager=UserManagerImpl.getInstance();
            PlayGameManagerImpl playGameManager=PlayGameManagerImpl.getInstance();
            YinZiKuCunManagerImpl yinZiKuCunManager=YinZiKuCunManagerImpl.getInstance();
           
           if(fenQuList==null)
	           {
		         //fenQuList= userManager.getFenQu(null);//获得现有的分区信息
		         fenQuList= userManager.getFenQuByStatus("0");//获得现有的分区信息
		         
		         session.removeAttribute("FENQU_LIST_0");
		         session.setAttribute("FENQU_LIST_0", fenQuList);
	           }
           
synchronized(lock){
	while(s.getTime() <= t.getTime() + 3600000){
		String day = DateUtil.formatDate(s,"yyyy-MM-dd");
		dayList.add(day);
		s.setTime(s.getTime() + 24*3600*1000);
	}
}	
	
	//对比各个渠道数据

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
		<h2 style="font-weight:bold;">
			银子库存
		</h2>
		<form  method="post">
		  
		 开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		  结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
	
	
	  库存种类：<select name="type">
                       <option value="1" 
                        <%
                        if("1".equals(type)){ out.print(" selected=\"selected\"");  }
                         %>
                          >银子</option>
                          
                       <option value="2" 
                        <%
                        if("2".equals(type)){ out.print(" selected=\"selected\"");  }
                         %>
                          >积分</option>
                       
                      <option value="3" 
                        <%
                        if("3".equals(type)){ out.print(" selected=\"selected\"");  }
                         %>
                          >活跃值</option>
                   
                </select> &nbsp;&nbsp;
	<br>
	
	
	   展示方式：
                   <input type="radio" <% if(displayType==null||"1".equals(displayType)){out.print("checked");} %>  name="displaytype" value="1" />按天&nbsp;&nbsp;
                   <input type="radio" <% if("3".equals(displayType)){out.print("checked");} %>  name="displaytype" value="3" />按周活跃（向前推一周，登录过的用户）&nbsp;&nbsp;
                   
                    <input type="radio" <% if("2".equals(displayType)){out.print("checked");} %>  name="displaytype" value="2" />按月活跃（向前推30天，登录过的用户）
          
	
	
	
		<input type='submit' value='提交'> </form>
		
		<br/>
		</center>
		<br/>
		<center>
		    		
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>分区</td><td>总计</td>");
		    		for(int c = 0 ; c < fenQuList.size() ; c++){
		    			out.print("<td>"+fenQuList.get(c)[1]+"</td>");
		    		}
					out.println("</tr>");
					for(int i = 0 ; i < dayList.size() ; i++){
						out.print("<tr bgcolor='#FFFFFF'> ");
						out.print("<td>"+dayList.get(i)+"</td>");
						
	   String sql="select t.fenqu, t.count,t.createdate,t.id,t.column1,t.column2,t.column3,t.column4,t.column5 from stat_yinzikucun t "+
	            " where  t.fenqu !='ST'  and to_char(t.createdate,'YYYY-MM-DD') between "+
	        " '"+dayList.get(i)+"' AND '"+dayList.get(i)+"' and t.column1='"+displayType+"' and t.column2='"+type+"' ";
	       List<YinZiKuCun> ls=yinZiKuCunManager.getBySql(sql);
	      
	      
						Long total=0L;
						//for(int ds=0;ds<ls.size();ds++){
						//YinZiKuCun yinZiKuCun=ls.get(ds);
						//total+=yinZiKuCun.getCount();
			    		//}
			    		
			    		for(int c = 0 ; c < fenQuList.size() ; c++){
						String fenqu= fenQuList.get(c)[1];
						for(int ds=0;ds<ls.size();ds++){
						YinZiKuCun yinZiKuCun=ls.get(ds);
						if(fenqu.trim().equals(yinZiKuCun.getFenQu()))
						{
						total+=yinZiKuCun.getCount();
						}
			    		}
			    		}
                      out.print("<td>"+StringUtil.addcommas(total)+"</td>");
						
					
					
					
						
						for(int c = 0 ; c < fenQuList.size() ; c++){
						String fenqu= fenQuList.get(c)[1];
						boolean flag=true;
						for(int ds=0;ds<ls.size();ds++){
						YinZiKuCun yinZiKuCun=ls.get(ds);
						if(fenqu.trim().equals(yinZiKuCun.getFenQu()))
						{
						out.print("<td>"+StringUtil.addcommas(yinZiKuCun.getCount())+"</td>");
						flag=false;
						continue;
						}
			    		}
			    		if(flag){out.print("<td>0</td>");}
			    		}
						out.println("</tr>");
					}
					out.println("</table><br>");
		    		%>
		    		
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		   <script>
		  drawRegUserFlotr();
		   </script>
		</center>
		<br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
