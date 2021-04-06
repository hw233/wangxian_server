<%@page import="com.fy.boss.vip.platform.CustomServicerManager"%>
<%@page import="com.fy.boss.vip.platform.model.CustomServicer"%>
<%@page import="com.fy.boss.vip.platform.model.VipPlayerInfoRecord"%>
<%@page import="com.xuanzhi.tools.authorize.UserManager"%>
<%@page import="com.xuanzhi.tools.authorize.RoleManager"%>
<%@page import="com.xuanzhi.tools.authorize.Role"%>
<%@page import="com.xuanzhi.tools.authorize.User"%>
<%@page import="com.xuanzhi.tools.authorize.AuthorizeManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerOracleImpl"%>
<%@page import="com.fy.boss.vip.platform.VipPlayerInfoManager"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="org.apache.commons.codec.language.bm.BeiderMorseEncoder"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>

<%@page import="java.util.Date"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>

<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ include file="priv.jsp"%>  
 <%
 
	String beginDate = ParamUtils.getParameter(request, "begindate", "");
	String endDate = ParamUtils.getParameter(request, "enddate", "");
	String username = ParamUtils.getParameter(request, "username", "");
	String serverName = ParamUtils.getParameter(request, "server", "");
	String manager4vip1 = ParamUtils.getParameter(request, "manager4vip", "");
	int viplevel = ParamUtils.getIntParameter(request, "viplevel", 0);
	int priv = ParamUtils.getIntParameter(request,"priv", -1);
	String birthbegintime = ParamUtils.getParameter(request, "birthbegintime", "");
	String birthendtime = ParamUtils.getParameter(request, "birthendtime", "");
	String qq = ParamUtils.getParameter(request, "qq", "");
	String phone = ParamUtils.getParameter(request, "phone", "");
	
	
	
	String beginDateParam = beginDate;
	String endDateParam = endDate;
	String usernameParam = username;
	String serverNameParam = serverName;
	String manager4vip1Param = manager4vip1;
	int viplevelParam = viplevel;
	

	
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	 <link rel='stylesheet' type='text/css' href='../css/jquery-ui.css' />
	<script type='text/javascript' src='../js/jquery-1.9.1.min.js'></script>
	<script type='text/javascript' src='../js/jquery-ui-1.10.3.min.js'></script>
	<script>
	
	  jQuery(function(){  
		    $.datepicker.regional['zh-CN'] = {  
		      clearText: '清除',  
		      clearStatus: '清除已选日期',  
		      closeText: '关闭',  
		      closeStatus: '不改变当前选择',  
		      prevText: '<上月',  
		      prevStatus: '显示上月',  
		      prevBigText: '<<',  
		      prevBigStatus: '显示上一年',  
		      nextText: '下月>',  
		      nextStatus: '显示下月',  
		      nextBigText: '>>',  
		      nextBigStatus: '显示下一年',  
		      currentText: '今天',  
		      currentStatus: '显示本月',  
		      monthNames: ['一月','二月','三月','四月','五月','六月', '七月','八月','九月','十月','十一月','十二月'],  
		      monthNamesShort: ['一','二','三','四','五','六', '七','八','九','十','十一','十二'],  
		      monthStatus: '选择月份',  
		      yearStatus: '选择年份',  
		      weekHeader: '周',  
		      weekStatus: '年内周次',  
		      dayNames: ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'],  
		      dayNamesShort: ['周日','周一','周二','周三','周四','周五','周六'],  
		      dayNamesMin: ['日','一','二','三','四','五','六'],  
		      dayStatus: '设置 DD 为一周起始',  
		      dateStatus: '选择 m月 d日, DD',  
		      dateFormat: 'yy-mm-dd',  
		      firstDay: 1,  
		      initStatus: '请选择日期',  
		      isRTL: false  
		    };  
		    $.datepicker.setDefaults($.datepicker.regional['zh-CN']);  
		    $('#datepicker').datepicker({changeMonth:true,changeYear:true}); 

		    
		    
		    
		  });
	
	$(document).ready(function() 
		{
	 	 $("#begindate").datepicker(
	 		      { 
	 		      	dateFormat: "yy-mm-dd"
	 						
	 			  } );
	 	 $("#enddate").datepicker(
	 		      { 
	 		      	dateFormat: "yy-mm-dd"
	 						
	 			  } );		
		}
	
		
	);
	

	
	</script>
</head>
<body>
	<h1>VIP玩家资料库</h1>
<form method="post" action="playerinfoList.jsp">	
<input type="hidden" name="isdone" value="true"/>
<div align="left">
	查询:
	时间:<input type="text" name="begindate" id="begindate" value="<%=(beginDate == null ? "" : beginDate) %>" /> - <input type="text" name="enddate" id="enddate" value="<%=(endDate == null ? "" : endDate) %>"  /> &nbsp;&nbsp;&nbsp;
	账号:<input type="text" name="username" value="<%=(username == null ? "" : username) %>"  />&nbsp;&nbsp;&nbsp;服务器:<input type="text" name="server"  value="<%=(serverName == null ? "" : serverName) %>"/>&nbsp;&nbsp;&nbsp;
客服昵称:<input type="text" name="manager4vip"  value="<%=(manager4vip1 == null ? "" : manager4vip1) %>" />&nbsp;&nbsp;&nbsp;
	VIP等级:<input type="text" name="viplevel" value="<%=(viplevel == 0 ? "" : viplevel) %>" />&nbsp;&nbsp;&nbsp;
	分配状态:<select name="priv">
			<option value="-1" <%if(priv == -1) {%> selected="selected"  <%}else{ %><%} %> >请选择</option>
			<option value="0"  <%if(priv == 0) {%> selected="selected"  <%}else{ %><%} %>  >未分配</option>
			<option value="1"  <%if(priv == 1) {%> selected="selected"  <%}else{ %><%} %>   >已分配</option>
			</select>&nbsp;&nbsp;&nbsp;
	QQ:<input type="text" name="qq" value="<%=(qq == null ? "" : qq) %>"  />&nbsp;&nbsp;&nbsp;
	电话:<input type="text" name="phone" value="<%=(phone == null ? "" : phone) %>"  />	
			
	<!--  玩家生日:<input type="text" name="birthbegintime" id="birthbegintime" value="<%=(birthbegintime == null ? "" : birthbegintime) %>" />	-	<input type="text" name="birthendtime" id="birthendtime" value="<%=(birthendtime == null ? "" : birthendtime) %>" />-->	
	<input type="submit" value="查询">
</div>
</form>	
	<table border="1px">
		<tr>
			<td>
				序列号
			</td>
			<td>
				服务器名
			</td>
			<td>
				账号
			</td>
			<td>
				角色名
			</td>
			<td>
				VIP等级
			</td>
			<td>
				玩家生日
			</td>
			<Td>
				手机
			</Td>
			<td>
				QQ
			</td>
			<td>
				记录资料时间
			</td>
			<td>
				资料修改时间
			</td>
			<td>
				客户经理
			</td>
			<td>
				分配状态
			</td>
			<td>
				操作
			</td>
		</tr>
<%

	long pagenum = 0;
	long recordNum = 0;

	int pageIndex = ParamUtils.getIntParameter(request,"pageIndex", 1);
	if(pageIndex < 1)
	{
		pageIndex = 1;
	}

    int pernum = 50;
    recordNum = VipPlayerInfoManager.getInstance().em.count();
    
    pagenum = recordNum/pernum;
    if(recordNum % pernum != 0) {
    	pagenum++;
    }
    
    if(pageIndex > pagenum)
    {
    	pageIndex = (int)pagenum;
    }
	
	
	int sum = 0;

	
	
	


		
		
		
		String where = " id >= 1 ";
		
		CustomServicer customServicer = null;
 		if(level != 1)
 		{
 			//判断客服经理是否和当前登陆账号一致  一致才可以继续操作
 			String wheresql = "loginName = ?";
 			List<CustomServicer>  lst11 = CustomServicerManager.getInstance().queryForWhere(wheresql, new Object[]{curUser});
 			if(lst11 != null && lst11.size() > 0)
 			{
 				
 				customServicer = lst11.get(0);
 			}
 		}
		
		
		int[] paramsPos = new int[]{0,0,0,0,0,0};
		Object[] values = new Object[]{null,null,null,null,null,null};
		

		
		
		int curPos = 0;
		
		if(customServicer != null)
		{
			paramsPos = new int[]{0,0,0,0,0,0,0};
			values = new Object[]{null,null,null,null,null,null,null};
			where += (where.length() > 0 ?  ( " and manager4vip = ?") : (" manager4vip = ?"));
			if(curPos > 0)
			{
				paramsPos[curPos] = paramsPos[curPos-1]+1;
			
			}
			else
			{
				paramsPos[curPos] = 1;
			}
			values[curPos] = customServicer.getNickname();
			curPos += 1;
		}
		else
		{
			if(level != 1)
			{
				return;
			}
		}
		
		if(priv != -1)
		{
			where += (where.length() > 0 ?  ( " and privValue = ?") : ( " privValue  = ?"));
			if(curPos > 0)
			{
				paramsPos[curPos] = paramsPos[curPos-1]+1;
			
			}
			else
			{
				paramsPos[curPos] = 1;
			}
			values[curPos] = priv;
			curPos += 1;
		}
		
		
		
		if(!StringUtils.isEmpty(beginDate))
		{
			long beginTime =  DateUtil.parseDate(beginDate, "yy-MM-dd").getTime();
			where += (where.length() > 0 ?  ( " and createTime >= ?") : ( " createTime >= ?"));
			if(curPos > 0)
			{
				paramsPos[curPos] = paramsPos[curPos-1]+1;
			
			}
			else
			{
				paramsPos[curPos] = 1;
			}
			values[curPos] = beginTime;
			curPos += 1;
		}
		
		if(!StringUtils.isEmpty(endDate))
		{
			long endTime =  DateUtil.parseDate(endDate, "yy-MM-dd").getTime();
			where += (where.length() > 0 ?  (" and createTime <= ?")  :  ( "  createTime <= ?") )  ;
			
			if(curPos > 0)
			{
				paramsPos[curPos] = paramsPos[curPos-1]+1;
			
			}
			else
			{
				paramsPos[curPos] = 1;
			}
			values[curPos] = endTime;
			curPos+=1;
		}
		
/* 		if(!StringUtils.isEmpty(birthbegintime))
		{
			long beginTime =  DateUtil.parseDate(birthbegintime, "yy-MM-dd").getTime();
			where += (where.length() > 0 ?  ( " and birthtime >= ?") : ( " birthtime >= ?"));
			if(curPos > 0)
			{
				paramsPos[curPos] = paramsPos[curPos-1]+1;
			
			}
			else
			{
				paramsPos[curPos] = 1;
			}
			values[curPos] = beginTime;
			curPos += 1;
		}
		
		if(!StringUtils.isEmpty(birthendtime))
		{
			long endTime =  DateUtil.parseDate(birthendtime, "yy-MM-dd").getTime();
			where += (where.length() > 0 ?  (" and birthtime <= ?")  :  ( "  birthtime <= ?") )  ;
			
			if(curPos > 0)
			{
				paramsPos[curPos] = paramsPos[curPos-1]+1;
			
			}
			else
			{
				paramsPos[curPos] = 1;
			}
			values[curPos] = endTime;
			curPos+=1;
		} */
		
		if(!StringUtils.isEmpty(username))
		{
			where += (where.length() > 0  ? (" and username like ?") : ( " username like ?"));
			if(curPos > 0)
			{
				paramsPos[curPos] = paramsPos[curPos-1]+1;
			
			}
			else
			{
				paramsPos[curPos] = 1;
			}
			values[curPos] = "%"+username+"%";
			curPos+=1;
		}
		
		if(!StringUtils.isEmpty(serverName))
		{
			where += ( where.length() > 0 ? (" and serverName like ?") : ("  serverName like ?"));
			if(curPos > 0)
			{
				paramsPos[curPos] = paramsPos[curPos-1]+1;
			
			}
			else
			{
				paramsPos[curPos] = 1;
			}
			values[curPos] = "%"+serverName+"%";
			curPos+=1;
		}
		
		if(!StringUtils.isEmpty(manager4vip1))
		{
			where += (where.length() > 0  ? (" and manager4vip like ?") : (" manager4vip like ?"));
			if(curPos > 0)
			{
				paramsPos[curPos] = paramsPos[curPos-1]+1;
			
			}
			else
			{
				paramsPos[curPos] = 1;
			}
			values[curPos] = "%"+manager4vip1+"%";
			curPos += 1;
		}
		
		
		if(viplevel != 0)
		{
			where +=  (where.length() > 0 ? (" and vipLevel = ?") : ("   vipLevel = ?"));
			if(curPos > 0)
			{
				paramsPos[curPos] = paramsPos[curPos-1]+1;
			
			}
			else
			{
				paramsPos[curPos] = 1;
			}
			values[curPos] = viplevel;
			curPos+=1;
		}
		
		if(!StringUtils.isEmpty(qq))
		{
			where += (where.length() > 0  ? (" and qq like ?") : (" qq like ?"));
			if(curPos > 0)
			{
				paramsPos[curPos] = paramsPos[curPos-1]+1;
			
			}
			else
			{
				paramsPos[curPos] = 1;
			}
			values[curPos] = "%"+qq+"%";
			curPos += 1;
		}
		
		if(!StringUtils.isEmpty(phone))
		{
			where += (where.length() > 0  ? (" and phone like ?") : (" phone like ?"));
			if(curPos > 0)
			{
				paramsPos[curPos] = paramsPos[curPos-1]+1;
			
			}
			else
			{
				paramsPos[curPos] = 1;
			}
			values[curPos] = "%"+phone+"%";
			curPos += 1;
		}
		
		where += " and  recordType="+ VipPlayerInfoRecord.NORMAL_VIP_RECORD ;
		
		
		
		
		long id = 0l;
		String playerName = null;
		Date createDate = null;	
		Date modDate = null;
		String manager4vip = null;
		
		Object[] os = new Object[curPos];
		
		int osindex = 0;
		
		for(int i=0; i<values.length; i++)
		{
			if(values[i] != null)
			{
				os[osindex] = values[i];
				osindex+=1;
			}
		}
		
	

		Class<VipPlayerInfoRecord> cl = VipPlayerInfoRecord.class;
	    recordNum = VipPlayerInfoManager.getInstance().em.count(cl, where, os);
	    
	    pagenum = recordNum/pernum;
	    if(recordNum % pernum != 0) {
	    	pagenum++;
	    }
	    
	    if(pageIndex > pagenum)
	    {
	    	pageIndex = (int)pagenum;
	    }
		
		
		
		try
		{
				int start =  (pageIndex-1)*pernum+1;
				List<VipPlayerInfoRecord> lst = VipPlayerInfoManager.getInstance().queryForWhere(where, os, "createTime desc,privValue", start, pernum);
				if(lst != null)
				{
						for(VipPlayerInfoRecord v : lst)
						{
							id = v.getId();
							serverName = v.getServerName();
							username = v.getUsername();
							playerName = v.getPlayerName();
							viplevel =  v.getVipLevel();
							createDate =  new Date(v.getCreateTime());
							long modTime = v.getUpdateTime();
							modDate = modTime > 0 ? new Date(modTime):null;
							manager4vip = v.getManager4vip();
							int alloc = v.getPrivValue();
							String birth = v.getBirthday();
							
							
							
	%>
					<tr>
						<td>
							<%=id %>
						</td>	
						<td>
							<%=serverName%>
						</td>	
						<td>
							<%=username%>
						</td>	
						<td>
							<%=playerName %>
						</td>	
						<td>
							<%=viplevel %>
						</td>	
						<td>
							<%=birth %>
						</td>
						<td>
							<%=v.getPhone() %>
						</td>
						<td>
							<%=v.getQq() %>
						</td>
						<td>
							<%=DateUtil.formatDate(createDate,"yyyy-MM-dd HH:mm:ss")%>
						</td>	
						<td>
							<%=(modDate != null ? DateUtil.formatDate(modDate,"yyyy-MM-dd HH:mm:ss"):"--") %>
						</td>	
						<td>
							<%=manager4vip == null ? "" : manager4vip %>
						</td>
						<td>
							<%=alloc == 0 ? "未分配" : "已分配" %>
						</td>
						<td>
							<a href="modifyvipinfo.jsp?id=<%=id%>">查看/修改</a>
						</td>	
					</tr>	
				
	<%
				sum += 1;
						}
				}
	}
	finally
	{
	}
	
	%>
	<script type="text/javascript">

function goPage()
{
	var pageindex=$('#pageindex').val();
	var str ="playerinfoList.jsp?begindate=<%=beginDateParam%>&enddate=<%=endDateParam%>&username=<%=usernameParam%>&server=<%=serverNameParam%>&manager4vip=<%=(manager4vip1Param == null ? "" : manager4vip1Param)%>&viplevel=<%=(viplevelParam==0 ? "" : viplevelParam)%>&priv=<%=priv%>&birthbegintime=<%=birthbegintime%>&birthendtime=<%=birthendtime%>"; 
	str+="&pageIndex="+pageindex;location.replace(str);
}
</script>
	
		<tr >
                        <th align=right colspan="13">
                        	<%if(pageIndex > 0) {%>
                        	<input type=button name=bt3 value="上一页" onclick="location.replace('playerinfoList.jsp?pageIndex=<%=pageIndex-1 %>&begindate=<%=beginDateParam%>&enddate=<%=endDateParam%>&username=<%=usernameParam%>&server=<%=serverNameParam%>&manager4vip=<%=(manager4vip1Param == null ? "" : manager4vip1Param)%>&viplevel=<%=(viplevelParam==0 ? "" : viplevelParam)%>&priv=<%=priv%>&birthbegintime=<%=birthbegintime%>&birthendtime=<%=birthendtime%>&qq=<%=qq%>&phone=<%=phone%>')">&nbsp;&nbsp;&nbsp;
                        	<%} 
                        	if(pageIndex < pagenum) {
                        	%>
                        	<input type=button name=bt4 value="下一页" onclick="location.replace('playerinfoList.jsp?pageIndex=<%=pageIndex+1 %>&begindate=<%=beginDateParam%>&enddate=<%=endDateParam%>&username=<%=usernameParam%>&server=<%=serverNameParam%>&manager4vip=<%=(manager4vip1Param == null ? "" : manager4vip1Param)%>&viplevel=<%=(viplevelParam==0 ? "" : viplevelParam)%>&priv=<%=priv%>&birthbegintime=<%=birthbegintime%>&birthendtime=<%=birthendtime%>&qq=<%=qq%>&phone=<%=phone%>')">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        	<%} %>
                        	&nbsp;&nbsp;&nbsp;&nbsp;共<%=pagenum %></>页，<%=recordNum %>条 本页共<%=sum %>条 第<input type="text" name="pageindex" id="pageindex" value="<%=pageIndex %>">页 <input type=button name=gopage  value="go" onclick="goPage()">
                        </th>
                </tr>
		
		
		
	
		</table>

</body>
</html>

