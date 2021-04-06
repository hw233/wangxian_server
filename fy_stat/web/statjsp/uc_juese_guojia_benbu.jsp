<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,com.xuanzhi.tools.text.*,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.UserManagerImpl,com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.service.*,com.sqage.stat.model.Channel"
	%>

  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String qudao=request.getParameter("qudao");
	String fenqu=request.getParameter("fenqu");
	
	if("0".equals(qudao)){qudao=null;}
	if("0".equals(fenqu)){fenqu=null;}
	
	if(qudao==null||"-1".equals(qudao)){qudao="ucly_b','ucly_c','uc_l'"+
	                           "'ucly_c1','ucly_c2','ucly_c3','ucly_c4','ucly_c5','ucly_c6','ucly_c7','ucly_c8','ucly_c9','ucly_c10'"+
	                           "'ucly_c11','ucly_c12','ucly_c13','ucly_c14','ucly_c15','ucly_c16','ucly_c17','ucly_c18','ucly_c19','ucly_c20'"+
	                           "'ucly_c21','ucly_c22','ucly_c23','ucly_c24','ucly_c25','ucly_c26','ucly_c27','ucly_c28','ucly_c29','ucly_c30";}
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	
	/**
	*获得渠道信息
	**/
	          ChannelManager cmanager = ChannelManager.getInstance();
              UserManagerImpl userManager=UserManagerImpl.getInstance();
              ChongZhiManagerImpl chongZhiManager=ChongZhiManagerImpl.getInstance();
              
              ArrayList<String []> fenQuList= userManager.getFenQu(null);//获得现有的分区信息
              List<Channel> channelList = cmanager.getChannels();//渠道信息
     
		    // List chongZhiList=chongZhiManager.getChongZhi(startDay,endDay,qudao,fenqu);
		    List<String[]>  jsfbList=userManager.getJueSeGuoJiaFenBu(null,null,qudao,fenqu);
		    List<String[]>  jsfbList_new=userManager.getJueSeGuoJiaFenBu(startDay,endDay,qudao,fenqu);
		    Long registUserCount=userManager.getRegistUerCount(startDay,endDay,qudao,null,null);//注册用户数
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
			角色国家分布
		</h2>
		<form  method="post">开始日期：<input type='text' name='day' value='<%=startDay %>'>
		-- 结束日期：<input type='text' name='endDay' value='<%=endDay %>'>(格式：2012-02-09)
		<br/><br/>
                
                       渠道：<select name="qudao">
                       <option  value="-1">全部</option>
                       
                        <option value="ucly_b" 
                        <%  if("ucly_b".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >ucly_b</option>
                       
                       <option value="ucly_c" 
                        <%  if("ucly_c".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >ucly_c</option>
                        
                         <option value="ucly_c1" 
                        <%  if("ucly_c1".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >ucly_c1</option>
                        <option value="ucly_c2" 
                        <%  if("ucly_c2".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >ucly_c2</option>
                        <option value="ucly_c3" 
                        <%  if("ucly_c3".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >ucly_c3</option>
                        
                        <option value="ucly_c4" 
                        <%  if("ucly_c4".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >ucly_c4</option>
                        <option value="ucly_c5" 
                        <%  if("ucly_c5".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >ucly_c5</option>
                        <option value="ucly_c6" 
                        <%  if("ucly_c6".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >ucly_c6</option>
                        <option value="ucly_c7" 
                        <%  if("ucly_c7".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >ucly_c7</option>
                        <option value="ucly_c8" 
                        <%  if("ucly_c8".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >ucly_c8</option>
                        <option value="ucly_c9" 
                        <%  if("ucly_c9".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >ucly_c9</option>
                        
                        <option value="ucly_c10" 
                        <%  if("ucly_c10".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >ucly_c10</option>
                        <option value="ucly_c11" 
                        <%  if("ucly_c11".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >ucly_c11</option>
                        <option value="ucly_c12" 
                        <%  if("ucly_c12".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >ucly_c12</option>
                        <option value="ucly_c13" 
                        <%  if("ucly_c13".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >ucly_c13</option>
                        <option value="ucly_c14" 
                        <%  if("ucly_c14".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >ucly_c14</option>
                        <option value="ucly_c15" 
                        <%  if("ucly_c15".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >ucly_c15</option>
                        <option value="ucly_c16" 
                        <%  if("ucly_c16".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >ucly_c16</option>
                        <option value="ucly_c17" 
                        <%  if("ucly_c17".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >ucly_c17</option>
                        <option value="ucly_c18" 
                        <%  if("ucly_c18".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >ucly_c18</option>
                        <option value="ucly_c19" 
                        <%  if("ucly_c19".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >ucly_c19</option>
                        
                        <option value="ucly_c20" 
                        <%  if("ucly_c20".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >ucly_c20</option>
                        <option value="ucly_c21" 
                        <%  if("ucly_c21".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >ucly_c21</option>
                        <option value="ucly_c22" 
                        <%  if("ucly_c22".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >ucly_c22</option>
                        <option value="ucly_c23" 
                        <%  if("ucly_c23".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >ucly_c23</option>
                        <option value="ucly_c24" 
                        <%  if("ucly_c24".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >ucly_c24</option>
                        <option value="ucly_c25" 
                        <%  if("ucly_c25".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >ucly_c25</option>
                        <option value="ucly_c26" 
                        <%  if("ucly_c26".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >ucly_c26</option>
                        <option value="ucly_c27" 
                        <%  if("ucly_c27".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >ucly_c27</option>
                        <option value="ucly_c28" 
                        <%  if("ucly_c28".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >ucly_c28</option>
                        
                         <option value="ucly_c29" 
                        <%  if("ucly_c29".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >ucly_c29</option>
                         <option value="ucly_c30" 
                        <%  if("ucly_c30".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >ucly_c30</option>
                        
                        <option value="uc_l" 
                        <%  if("uc_l".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >uc_l</option>
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
    
		    		<input type='submit' value='提交'></form><br/>
		    		     
		    		<br/>
		    		<h3>角色国家分布</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>注册数据</td><td>角色数量</td><td>角色分布(吴/蜀/魏)</td><td>新增角色数量</td><td>新增角色分布(吴/蜀/魏)</td></tr>");
					
					Long sumCount=0L;
					String wu="";
					String shu="";
					String wei="";
					
					Long sumCount_new=0L;
					String wu_new="";
					String shu_new="";
					String wei_new="";
					
					for(int j=0;j<jsfbList.size();j++){
					String [] jsfb=jsfbList.get(j);
					if(jsfb[1]==null){jsfb[1]="0";}
					if("吴".equals(jsfb[0])){wu=jsfb[1];	    }
					if("魏".equals(jsfb[0])){wei=jsfb[1];	}
					if("蜀".equals(jsfb[0])){shu=jsfb[1];	}
					sumCount+=Long.parseLong(jsfb[1]);
					}
					
					for(int j=0;j<jsfbList_new.size();j++){
					String [] jsfb_new=jsfbList_new.get(j);
					if(jsfb_new[1]==null){jsfb_new[1]="0";}
					if("吴".equals(jsfb_new[0])){wu_new=jsfb_new[1];	    }
					if("魏".equals(jsfb_new[0])){wei_new=jsfb_new[1];	}
					if("蜀".equals(jsfb_new[0])){shu_new=jsfb_new[1];	}
					sumCount_new+=Long.parseLong(jsfb_new[1]);
					}
					
					
					out.print("<tr bgcolor='#FFFFFF'>"+
					                             "<td>"+registUserCount+"</td>"+
					                             "<td>"+sumCount+"</td><td>"+wu+"/"+shu+"/"+wei+"</td>"+
					                             "<td>"+sumCount_new+"</td><td>"+wu_new+"/"+shu_new+"/"+wei_new+"</td>");
					out.println("</tr>");
					out.println("</table><br>");
		    		%>
		</center>
		<br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
