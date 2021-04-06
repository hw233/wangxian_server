<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,com.sqage.stat.commonstat.manager.Impl.*,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.UserManagerImpl"
	%>

  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String level = request.getParameter("level");
	String fenqu=request.getParameter("fenqu");
	String money = request.getParameter("money");
	String jixing = request.getParameter("jixing");
	ArrayList<String []> fenQuList  =  (ArrayList<String []>) session.getAttribute("FENQU_LIST");
	
	if("0".equals(fenqu)){fenqu=null;}
	if(money == null) money = "全部";
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	if(level == null) level = "全部";
	if("0".equals(jixing)){jixing=null;}
	
              UserManagerImpl userManager=UserManagerImpl.getInstance();
              ChongZhiManagerImpl chongZhiManager=ChongZhiManagerImpl.getInstance();
	         if(fenQuList==null)
	           {
		         fenQuList= userManager.getFenQu(null);//获得现有的分区信息
		         session.removeAttribute("FENQU_LIST");
		         session.setAttribute("FENQU_LIST", fenQuList);
	           }
	        
             
              String money_search=null;
              String level_search=null;
              if(!"全部".equals(money)){money_search=money;}
              if(!"全部".equals(level)){level_search=level;}
		     List<String[]> chongZhiList=chongZhiManager.getChongZhi_jineQuJian_fenbu(startDay,endDay,level_search,fenqu,money_search,jixing);
		     
		     	    Long money_50=0L;
					Long money_100=0L;
					Long money_150=0L;
					Long money_200=0L;
					Long money_300=0L;
					Long money_500=0L;
					Long money_1000=0L;
					Long money_2000=0L;
					Long money_5000=0L;
					Long money_10000=0L;
					Long money_more=0L;
					
					float money_sum_50=0f;
					float money_sum_100=0f;
					float money_sum_150=0f;
					float money_sum_200=0f;
					float money_sum_300=0f;
					float money_sum_500=0f;
					float money_sum_1000=0f;
					float money_sum_2000=0f;
					float money_sum_5000=0f;
					float money_sum_10000=0f;
					float money_sum_more=0f;
					
					for(String[] chongZhi:chongZhiList){
					//String userCount=chongZhi[1];
					//String sumMoney=chongZhi[2];
					
					Long moneyL=0L;
					Long usercount=0L;
					float moneySum=0f;
					if(chongZhi[0]!=null&&!"".equals(chongZhi[0])){
					
					moneyL=Long.parseLong(chongZhi[0]);
					usercount=chongZhi[1]==null ? 0L : Long.parseLong(chongZhi[1]);
					moneySum =chongZhi[2]==null ? 0f : Float.parseFloat(chongZhi[2]);
					
					if(moneyL==0)                {money_50    +=usercount;     money_sum_50   += moneySum;   }
					if(moneyL==1)                {money_100   +=usercount;     money_sum_100  += moneySum;   }
					if(moneyL==2)                {money_150   +=usercount;     money_sum_150  += moneySum;   }
					if(moneyL>=3&&moneyL<=4)     {money_200   +=usercount;     money_sum_200  += moneySum;   }
					if(moneyL>=5&&moneyL<=6)     {money_300   +=usercount;     money_sum_300  += moneySum;   }
					if(moneyL>=7&&moneyL<=10)    {money_500   +=usercount;     money_sum_500  += moneySum;   }
					if(moneyL>=11&&moneyL<=20)   {money_1000  +=usercount;     money_sum_1000 += moneySum;   }
					if(moneyL>=21&&moneyL<=40)   {money_2000  +=usercount;     money_sum_2000 += moneySum;   }
					if(moneyL>=41&&moneyL<=100)  {money_5000  +=usercount;     money_sum_5000 += moneySum;   }
					if(moneyL>=101&&moneyL<=200) {money_10000 +=usercount;     money_sum_10000+= moneySum;   }
					if(moneyL>=201)              {money_more  +=usercount;     money_sum_more += moneySum;   }
					
					}
					}
					
					ArrayList realChannelList=new ArrayList();
					realChannelList.add("充值用户数");
					//realChannelList.add("充值金额");
					ArrayList dayList=new ArrayList();
					dayList.add("1-50");
					dayList.add("50-100");
					dayList.add("100-150");
					dayList.add("150-200");
					dayList.add("200-300");
					dayList.add("300-500");
					dayList.add("500-1000");
					dayList.add("1000-2000");
					dayList.add("2000-5000");
					dayList.add("5000-10000");
					dayList.add("10000以上");
					
					long channel_regday_nums[][] = new long[realChannelList.size()][dayList.size()];
					channel_regday_nums[0][0]=money_50;
					channel_regday_nums[0][1]=money_100;
					channel_regday_nums[0][2]=money_150;
					channel_regday_nums[0][3]=money_200;
					channel_regday_nums[0][4]=money_300;
					channel_regday_nums[0][5]=money_500;
					channel_regday_nums[0][6]=money_1000;
					channel_regday_nums[0][7]=money_2000;
					channel_regday_nums[0][8]=money_5000;
					channel_regday_nums[0][9]=money_10000;
					channel_regday_nums[0][10]=money_more;
					/*
					channel_regday_nums[1][0]=(long)money_sum_50;
					channel_regday_nums[1][1]=(long)money_sum_100;
					channel_regday_nums[1][2]=(long)money_sum_150;
					channel_regday_nums[1][3]=(long)money_sum_200;
					channel_regday_nums[1][4]=(long)money_sum_300;
					channel_regday_nums[1][5]=(long)money_sum_500;
					channel_regday_nums[1][6]=(long)money_sum_1000;
					channel_regday_nums[1][7]=(long)money_sum_2000;
					channel_regday_nums[1][8]=(long)money_sum_5000;
					channel_regday_nums[1][9]=(long)money_sum_10000;
					channel_regday_nums[1][10]=(long)money_sum_more;
					*/
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

var mycars=[<% for(int i = 0 ; i < dayList.size() ; i++){ out.print("\""+dayList.get(i)+"\",");} %>"E"];
      
       function drawRegUserFlotr(){
		    var f = Flotr.draw(
		$('regUserContainer'), [
		<%for(int j = 0 ; j < realChannelList.size() ; j++){
			StringBuffer sb2 = new StringBuffer();
			sb2.append("[");
			for(int kk = 0 ; kk < channel_regday_nums[j].length ; kk++){
				sb2.append("["+kk+","+channel_regday_nums[j][kk]+"]");
				if(kk < channel_regday_nums[j].length -1) sb2.append(",");
			}
			sb2.append("]");
			out.println("{");
			out.println("data:"+sb2.toString()+",");
			out.println("label:'"+realChannelList.get(j)+"'");
			out.println("}");
			if(j < realChannelList.size()-1) out.print(",");
		}
		%>
		],{
			xaxis:{
				noTicks: <%=dayList.size()%>,	// Display 7 ticks.	
				tickFormatter: function(n){ return mycars[Math.floor(n)];}, // =>
				min: 0,
				max: <%= dayList.size()%>
			},
			yaxis:{
				tickFormatter: function(n){ return (n);}, // =>
				min: 0,
			},
			legend:{
				position: 'ne', // => position the legend 'south-east'.
				backgroundColor: '#D2E8FF' // => a light blue background color.
			},
			mouse:{
				track: true,
				color: 'purple',
				sensibility: 1, // => distance to show point get's smaller
				trackDecimals: 2,
				trackFormatter: function(obj){ return 'y = ' + obj.y; }
			}
		}
	);
	}

</script>
	</head>
	<body>
		<center>
		<h2 style="font-weight:bold;">
			充值金额区间分布
		</h2>
		<form  method="post">
		
		<!--开始日期：<input type='text' name='day' value='<%=startDay %>'>
		-- 结束日期：<input type='text' name='endDay' value='<%=endDay %>'>(格式：2012-01-01)  -->
		
		  开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:80px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		  结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:80px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
		
		<br/><br/>
		     &nbsp;&nbsp;&nbsp;&nbsp;
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
                      
                </select>
                
                                              充值金额：<input type='text' name='money' value='<%=money %>'>
                                              统计级别：<input type='text' name='level' value='<%=level %>'>
                                            平台：<select name="jixing">
                       <option  value="0">全部</option>
                        <option value="Android" 
                        <%
                        if("Android".equals(jixing)){ out.print(" selected=\"selected\"");  }
                         %>>Android</option>
                          
                       <option value="IOS" 
                        <%
                        if("IOS".equals(jixing)){ out.print(" selected=\"selected\"");    }
                         %>>IOS</option>
                  </select>
		                                  
		    		<input type='submit' value='提交'></form>
		    		<br/>
		    		     <a href="chongzhi_info_page.jsp">充值查询—分页</a>|
		    		     <a href="chongzhi_info.jsp">充值查询-不分页</a>|
		    		     <a href="chongzhi_fenbu.jsp">  充值等级分布</a>|
		    		     <a href="chongzhi_money_fenbu.jsp">充值金额区间分布</a>|
		    		     <a href="qianrensouyi_info.jsp">千人收益</a>|
		    		     <a href="Kri_qianrensouyi_info.jsp">K 日千人收益</a>|
		    		     <a href="Krisouyi_info.jsp">K 日收益</a>|
		    		     <a href="quDaoChongzhi_info.jsp">按渠道充值查询</a>|
		    		     <a href="fenQuChongzhi_info.jsp">按分区充值查询</a>|
		    		     <a href="chongzhi_jine_fenbu.jsp">充值金额分布</a>|
		    		      <a href="modeTypeChongzhi_info.jsp">安手机类型充值查询</a>
		    		     
		    		<br/>
		    		<h3>充值金额区间分布</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>充值区间(元)</td><td>0-50</td><td>50-100</td><td>100-150</td><td>150-200</td>"+
					                               "<td>200-300</td><td>300-500</td><td>500-1000</td><td>1000-2000</td>"+
					                               "<td>2000-5000</td><td>5000-10000</td><td>10000以上</td></tr>");
					
					
				    out.print("<tr bgcolor='#EEEEBB'><td>充值用户数</td><td>"+money_50+"</td><td>"+money_100+"</td><td>"+money_150+"</td><td>"+money_200+"</td>"+
					                               "<td>"+money_300+"</td><td>"+money_500+"</td><td>"+money_1000+"</td><td>"+money_2000+"</td>"+
					                               "<td>"+money_5000+"</td><td>"+money_10000+"</td><td>"+money_more+"</td></tr>");
					
					
					  out.print("<tr bgcolor='#EEEEBB'><td>充值总额</td><td>"+money_sum_50+"</td><td>"+money_sum_100+"</td><td>"+money_sum_150+"</td><td>"+money_sum_200+"</td>"+
					                               "<td>"+money_sum_300+"</td><td>"+money_sum_500+"</td><td>"+money_sum_1000+"</td><td>"+money_sum_2000+"</td>"+
					                               "<td>"+money_sum_5000+"</td><td>"+money_sum_10000+"</td><td>"+money_sum_more+"</td></tr>");
					
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
