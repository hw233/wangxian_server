<%@page import="org.jfree.chart.axis.CategoryLabelPositions"%>
<%@page import="org.jfree.chart.axis.CategoryAxis"%>
<%@page import="org.jfree.chart.axis.NumberAxis"%>
<%@page import="org.jfree.chart.plot.CategoryPlot"%>
<%@page import="com.fy.engineserver.gm.feedback.Reply"%>
<%@page import="java.util.Date"%>
<%@page import="java.awt.Color"%>
<%@page import="org.jfree.chart.axis.ValueAxis"%>
<%@page import="org.jfree.chart.axis.DateAxis"%>
<%@page import="org.jfree.chart.plot.XYPlot"%>
<%@page import="com.fy.engineserver.gametime.SystemTime"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.engineserver.gm.feedback.Feedback"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.gm.feedback.service.FeedbackManager"%>
<%@page import="org.jfree.data.time.Hour"%>
<%@page import="java.util.Calendar"%>
<%@page import="org.jfree.data.time.Day"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import = "org.jfree.chart.ChartFactory,org.jfree.chart.JFreeChart,
					org.jfree.chart.servlet.ServletUtilities,org.jfree.chart.title.TextTitle,
					org.jfree.data.time.TimeSeries,
					org.jfree.data.time.TimeSeriesCollection,java.awt.Font"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>邮件数量状态 </title>
<link rel="stylesheet" href="../style.css"/>
 	<script>
	 	function $(tag) {
			return document.getElementById(tag);
		}
 		function query(){
 			var date = $("date").value;
			if(date){
				var str = "?action=query&date="+date;
				window.location.replace(str);
			}else{
				alert("信息不完整");
			}
 		}
 	</script>
</head>
<body>
  <%   
		TimeSeries timeSeries = new TimeSeries("AllMailNums", Hour.class);
		TimeSeries timeSeries2 = new TimeSeries("RepliedNums", Hour.class);
		TimeSeries timeSeries3 = new TimeSeries("TimeSize", Hour.class);
		//时间曲线数据集合
		TimeSeriesCollection lineDataset = new TimeSeriesCollection();
		TimeSeriesCollection lineDataset3 = new TimeSeriesCollection();
		String[] states = {"0","1","2","3"};
		String[] types = {"0","1","2","3","4"};
		List<Feedback> list = null;
		FeedbackManager fbm = FeedbackManager.getInstance();
		
		String query = request.getParameter("action");
		String date = request.getParameter("date");
		
		SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
		String nowTime = sdf.format(SystemTime.currentTimeMillis());
		StringBuffer nowt = new StringBuffer();
		nowt.append(nowTime);
		String endTime = sdf.format(SystemTime.currentTimeMillis());
		String startTime = sdf.format(SystemTime.currentTimeMillis());
		if(query!=null&&query.trim().length()>0&&date!=null&&date.trim().length()>0){
			String [] dates = date.split(",");
			nowt.setLength(0);
			if(dates.length==1){
				startTime = dates[0];
				Date da = sdf.parse(startTime);
				endTime = sdf.format(da.getTime()+24*60*60*1000);
				nowt.append(startTime);
			}else{
				startTime = dates[0];
				Date da = sdf.parse(dates[1]);
				endTime = sdf.format(da.getTime()+24*60*60*1000);
				nowt.append(startTime+","+sdf.format(da));
			}
		}
		
		list = fbm.queryFeedbacks(startTime, endTime, types, states, "全部", 0);
		Calendar cl = Calendar.getInstance();
		cl.setTimeInMillis(SystemTime.currentTimeMillis());
		int year = cl.get(Calendar.YEAR);
		int month = cl.get(Calendar.MONTH);
		int day = cl.get(Calendar.DAY_OF_MONTH);
		int hour = cl.get(Calendar.HOUR_OF_DAY);
		int minute = cl.get(Calendar.MINUTE);
		List<Integer> nums = new ArrayList<Integer>(); 
		List<Integer> nums2 = new ArrayList<Integer>(); 
		List<Integer> numst = new ArrayList<Integer>(); 
		List<Reply> rlist = null;
		long size = 0;
		for(int i=0;i<24;i++){
			nums.clear();
			nums2.clear();
			for(int j=0;j<list.size();j++){
				Feedback fb = list.get(j);
				Calendar cal = Calendar.getInstance();
				cal.clear();
				cal.setTimeInMillis(fb.getBeginDate());
				int ho = cal.get(Calendar.HOUR_OF_DAY);
				rlist = fb.getList();
				if(ho==i){
					nums.add(ho);
					if(fb.getGmState()>0){
						nums2.add(ho);
					}
					//处理回复完成时间间隔
					if(rlist.get(rlist.size()-1)!=null){
						Reply str = rlist.get(0);
						Reply etr = rlist.get(rlist.size()-1);
						size = size + etr.getSendDate() - str.getSendDate();
					}
				}
				
			}
			
			timeSeries.add(new Hour(i,day,month,year), nums.size());
			timeSeries2.add(new Hour(i,day,month,year), nums2.size());
			if(nums2.size()!=0){
				timeSeries3.add(new Hour(i,day,month,year), size/nums2.size()/1000);
			}else{
				timeSeries3.add(new Hour(i,day,month,year), -1);
			}
			
		}
		lineDataset.addSeries(timeSeries);
		lineDataset.addSeries(timeSeries2);
		lineDataset3.addSeries(timeSeries3);
		list.clear();
		JFreeChart chart = ChartFactory.createTimeSeriesChart("GM-MAIL", "Time", "Nums", lineDataset, true, true, true);
		JFreeChart chart3 = ChartFactory.createTimeSeriesChart("Time-Size", "Time", "Size", lineDataset3, true, true, true);
		TextTitle textTitle = chart.getTitle();
	    textTitle.setFont(new Font("黑体", Font.PLAIN, 20)); 
		TextTitle title = new TextTitle("GM-MAIL");
		chart.setTitle(title);
		chart.setAntiAlias(true);
		
		
		String filename = ServletUtilities.saveChartAsPNG(chart, 1100, 400, null, session);
		String filename3 = ServletUtilities.saveChartAsPNG(chart3, 1100, 400, null, session);
		String graphURL = request.getContextPath() + "/DisplayChart?filename=" + filename;
		String graphURL3 = request.getContextPath() + "/DisplayChart?filename=" + filename3;

		%> 
	<table>
		<h2>查询一天总邮件数和总回复数</h2>
		 <tr><th>时间：</th><td class='top'><input type='text' id='date' name='date' value='<%=nowt.toString()%>'/></td></tr>	
		 <tr><td colspan=2 ><input type='button' value='查询' onclick='query()' />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=list.size() %>条</td></tr>
	</table>		
 	 <img src="<%= graphURL %>"width=1100 height=500 border=0 usemap="#<%= filename %>">
 	 <img src="<%= graphURL3 %>"width=1100 height=500 border=0 usemap="#<%= filename3 %>">
 	 
 	 
 	 
</body>
</html>
