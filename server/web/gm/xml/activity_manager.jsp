<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="com.fy.engineserver.operating.activities.ActivityItemManager"%>
<%@page import="com.fy.engineserver.operating.activities.ActivityItem"%>
<%@include file="../header.jsp"%>
<%@include file="../authority.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<title>任务管理</title>
		<link rel="stylesheet" href="../style.css" />
		<style type="text/css">
		#a{
	 	 	  font-size:0px;
	 	 	}
	 	#a li{
	   	font-size:16px;
	   	text-align:center;
        width:110px;
	   	list-style-type:none;
	   	display: inline;
		padding:5px 10px;
	   	}	 	
		
		</style>
		<script type="text/javascript">
	function overTag(tag) {
		tag.style.color = "red";
		tag.style.backgroundColor = "lightcyan";
	}

	function outTag(tag) {
		tag.style.color = "black";
		tag.style.backgroundColor = "white";
	}
	function findpos(tag) {
		var index = 0;
		if (tag != null) {
			var tab = document.getElementById("tt");
			var trs = tab.rows;
			var trNode = tag.parentNode.parentNode;
			for ( var i = 0; i < tab.rows.length; i++) {
				if (trs[i] == trNode) {
					index = i;
					break;
				}
			}
		}
		if (index != 0)
			index = index - 1;
		return index;
	}
	function genetcolor(col ){  
	  var str = "<select name='fcolor' id='fcolor' >"
	  str =str+"<option value='ffffff' "+(col=="16777215"?"selected":"")+">白色</option>";
	  str =str+"<option value='602020' "+(col=="6299680"?"selected":"")+">红色</option>";
	  str =str+"<option value='6020a0' "+(col=="6299808"?"selected":"")+">紫色</option>";
	  str =str+"<option value='60ffff' "+(col=="6356991"?"selected":"")+">蓝色</option>";
	  str =str+"<option value='40ff80' "+(col=="4259712"?"selected":"")+">绿色</option>";
	  str =str+"<option value='ffff00' "+(col=="16776960"?"selected":"")+">黄色</option>";
	  str =str+"<option value='60ff00' "+(col=="6356736"?"selected":"")+">亮绿色</option>";
	  str =str+"</select>";
	  return str;
	}
	function genetwet(ins ){
	  var str = "<select name='weight' id='weight'  >"
	  for(var i =0;i<50;i++){
	   str =str+"<option value='"+i+"'"+(ins==i?"selected":"")+">权重"+i+"</option>";
	  }
	  str =str+"</select>";
	  return str;
	}
	function insert(tag,week) {
		var tab = document.getElementById("tt");
		var index =-2 ;
		if (tag != null&&tag!="null")
			index =findpos(tag);
		var nextRow = tab.insertRow(index + 1);
		nextRow.onmouseover = function() {
			overTag(this);
		};
		nextRow.onmouseout = function() {
			outTag(this);
		};
		var cel1 = nextRow.insertCell(-1);
		var cel2 = nextRow.insertCell(-1);
		var cel3 = nextRow.insertCell(-1);
		var cel4 = nextRow.insertCell(-1);
		var cel5 = nextRow.insertCell(-1);
		var cel6 = nextRow.insertCell(-1);
		var cel7 = nextRow.insertCell(-1);
		var cel8 = nextRow.insertCell(-1);
		var cel9 = nextRow.insertCell(-1);
		cel1.innerHTML = "ID自动生成";
		cel2.innerHTML="任务名：<input size='5' type='text' name='aname' id='aname' value='' />时间：<input size='7' type='text' name='atime' id='atime' value='' />";
		cel3.innerHTML="<input type='button' value='添加自动寻路' onclick ='auto(this);' />";
		cel4.innerHTML="字体颜色："+genetcolor();
		cel5.innerHTML="任务详情：<input size='8' type='text' name='memo' id='memo' value='' /> ";
		cel6.innerHTML="任务权重："+genetwet();
		cel7.innerHTML="最小级别:<input size='4' id='minlevel' name='minlevel' value='' />最大级别：<input size='4' id='maxlevel' name='maxlevel' value='' />"
		cel8.innerHTML="阵营:<select id='camp' name='camp' ><option value='0' >中立</option><option value='1' >紫微宫</option><option value='2' >日月盟</option></select>";
		cel9.innerHTML = "<a href='#' onclick='add(this,"+week+");'>确认</a>|<a href='#' onclick='cancer();'>取消</a>";
	}
	function auto(tag){
	tag.parentNode.innerHTML="地图名称：<input size='5' type='text' value='' name='mapname'/>X:<input size='3' type='text' value='' name='xsit' id='xsit' />Y:<input size='3' type='text' value='' name='ysit' id='ysit' />";	
	}
	function add(tag,week) {
		var index = findpos(tag);
		var aname = document.getElementById("aname").value;
		var atime = document.getElementById("atime").value;
		var memo = document.getElementById("memo").value;
		var weight = document.getElementById("weight").value;
		var minlevel =document.getElementById("minlevel").value;
		var maxlevel =document.getElementById("maxlevel").value;
		var camp =document.getElementById("camp").value;
		if (aname&&atime&&memo&&weight&&maxlevel&&minlevel) {
			var ff = document.getElementById("f1");
			ff.action = "?action=add&index=" + index+"&week="+week;
			ff.submit();
		} else {
			alert("输入的内容不能为空");
		}
	}
	function update(tag,week) {
		var trRow = tag.parentNode.parentNode;
		var cells = trRow.cells;
		var id = cells[0].innerHTML;
		var value = cells[1].innerHTML.split("%");
		var path1 = cells[2].innerHTML;
		var fcolor = cells[3].innerHTML;
		var memo = cells[4].innerHTML;
		var weight = cells[5].innerHTML;
		var minvalue = cells[6].innerHTML.split("/")[0];
		var maxvalue = cells[6].innerHTML.split("/")[1];
		var camp = cells[7].innerHTML;
		cells[0].innerHTML = "ID："+id+"<input type='hidden' name='id' id='id' value='"+id+"' />";
		cells[1].innerHTML = "任务名称<input size='5' type='text' name='aname' id='aname' value='"+value[0]+"' />时间<input size='7' type='text' name='atime' id='atime' value='"+value[1]+"' />";
		if(path1.indexOf("无")<0){
		path = path1.split("-");
		cells[2].innerHTML="地图名<input size='5' type='text' value='"+path[0]+"' id='mapname' name='mapname'/><input size='3' type='text'  value='"+path[1].split(":")[1]+"' name='xsit' id='xsit' /><input size='3' type='text' value='"+path[2].split(":")[1]+"' name='ysit' id='ysit' />";
		}
		else
		cells[2].innerHTML="<input type='button' value='添加自动寻路' onclick ='auto(this);' />";
		cells[3].innerHTML="字体颜色："+genetcolor(fcolor);
		cells[4].innerHTML="任务描述：<input size='3' type='text' name='memo' id='memo' value='"+memo+"' /> ";
		cells[5].innerHTML="任务权重："+genetwet(weight);
		cells[6].innerHTML="最小级别:<input size='4' id='minlevel' name='minlevel' value='"+minvalue+"' />最大级别：<input size='4' id='maxlevel' name='maxlevel' value='"+maxvalue+"' />"
		cells[7].innerHTML="阵营:<select id='camp' name='camp' ><option value='0' "+(camp==0?"selected":"")+" >中立</option><option value='1'"+(camp==1?"selected":"")+" >紫微宫</option><option value='2'"+(camp==2?"selected":"")+" >日月盟</option></select>";
		cells[8].innerHTML = "<a href='#' onclick='udcommit(this,"+week+");'>确认</a>|<a href='#' onclick='cancer();'>取消</a>";
	}
	function udcommit(tag,week) {
		var index = findpos(tag);
		var aname = document.getElementById("aname").value;
		var atime = document.getElementById("atime").value;
		var memo = document.getElementById("memo").value;
		var weight = document.getElementById("weight").value;
		var minlevel =document.getElementById("minlevel").value;
		var maxlevel =document.getElementById("maxlevel").value;
		var camp =document.getElementById("camp").value;
		if (aname&&atime&&memo&&weight&&maxlevel&&minlevel) {
			var ff = document.getElementById("f1");
			ff.action = "?action=update&index=" + index+"&week="+week;
			ff.submit();
		} else {
			alert("输入框中的内容不能为空");
		}
	}
	function delete1(id,week) {
		if (window.confirm("你是否确定要删除此模块")) {
			window.location.replace("activity_manager.jsp?delid=" + id+"&week="+week);
		}
	}
	function cancer() {
		//取消操作 
		location.reload();
	}
</script>
	</head>
	<body>
		<%
			try {
			    String[] ws ={"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
				out.print("<input type='button' value='刷新' onclick='window.location.replace(\"activity_manager.jsp\")' />");
				String username = session.getAttribute("username").toString();
				int week =0; 
				ActivityItemManager aimanager = ActivityItemManager.getInstance();
				ActionManager amanager = ActionManager.getInstance();
				ArrayList<ActivityItem>[] ais = aimanager.getActivityItem();  
				if(request.getParameter("week")!=null)
				 week =Integer.parseInt(request.getParameter("week").trim());
				 String delid = request.getParameter("delid");
				if (delid != null) {
				    aimanager.removeActivityItem(Long.parseLong(delid.trim()));
				    amanager.save(username,"删除了一个"+ws[week]+"日常任务：id["+delid+"]");
					response.sendRedirect("activity_manager.jsp?week="+week);
				}
				String action = request.getParameter("action");
				if (action != null) {
					if ("add".equals(action.trim())) {
						int index = Integer.parseInt(request.getParameter(
								"index").trim());
						//ActivityItem at = new ActivityItem();
						try{
						
						String aname=request.getParameter("aname");
						String atime=request.getParameter("atime");
						String mapname=request.getParameter("mapname");
						int xsit=request.getParameter("xsit")==null?0:Integer.parseInt(request.getParameter("xsit"));
						int ysit=request.getParameter("ysit")==null?0:Integer.parseInt(request.getParameter("ysit"));
						int weight=Integer.parseInt(request.getParameter("weight"));
					    String memo=request.getParameter("memo");
					    int fcolor=Integer.valueOf(request.getParameter("fcolor"),16);
					    int maxlevel = Integer.parseInt(request.getParameter("maxlevel").trim());
					    int minlevel = Integer.parseInt(request.getParameter("minlevel").trim());
					    int camp = Integer.parseInt(request.getParameter("camp").trim());
						aimanager.creatActivityItem(week,aname,atime,mapname==null?false:true,mapname==null?"":mapname,xsit,ysit,memo,weight,fcolor,minlevel,maxlevel,camp);
						amanager.save(username,"新增了"+ws[week]+"任务，名为："+aname);
						}catch(Exception e){  
						  out.print("读取错误");
						}
					//	amanager.save(username, "添加了一个任务名为："+at.getTitle() );
					}
					if ("update".equals(action.trim())) {
						int index = Integer.parseInt(request.getParameter(
								"index").trim());
						long id = Long.parseLong(request.getParameter("id"));
						String aname=request.getParameter("aname");
						String atime=request.getParameter("atime");
						String mapname=request.getParameter("mapname");
						int xsit=request.getParameter("xsit")==null?0:Integer.parseInt(request.getParameter("xsit"));
						int ysit=request.getParameter("ysit")==null?0:Integer.parseInt(request.getParameter("ysit"));
						int weight=Integer.parseInt(request.getParameter("weight"));
					    String memo=request.getParameter("memo");
					    int fcolor=Integer.valueOf(request.getParameter("fcolor"),16);
					    int maxlevel = Integer.parseInt(request.getParameter("maxlevel").trim());
					    int minlevel = Integer.parseInt(request.getParameter("minlevel").trim());
					    int camp = Integer.parseInt(request.getParameter("camp").trim());
					    aimanager.editeActivityItem(week,id,aname,atime,mapname==null?false:true,mapname==null?"":mapname,xsit,ysit,memo,weight,fcolor,minlevel,maxlevel,camp);
						amanager.save(username, "更新了一"+ws[week]+"任务：id[" +id+"]" );
					}
				}  
				out.print("<p></p><p></p><hr size=10 color='green' style=\"width:45%\" /><ul id='a'>");
				for(int i=0;i<7;i++){
				if(week==i)
				out.print("<li style='background-color:gray'><a href='javascript:window.location.replace(\"activity_manager.jsp?week="+i+"\")' >"+ws[i]+"</a></li>");
				else
				out.print("<li style='background-color:#2080ff'><a href='javascript:window.location.replace(\"activity_manager.jsp?week="+i+"\")'> "+ws[i]+"</a></li>");
				}
				out.print("</ul><hr size='10' color='green' style=\"width:45%\" />");
				out.print("<form action='' method='post' id='f1'><table width=80% align='center'><tbody id='tt'>");
				out.print("<tr><th>任务ID</th><th>活动地点</th><th>任务地图及坐标</th><th>字体颜色</th><th>活动详情</th><th>权限</th><th>等级</th><th>阵营</th><th>操作</th></tr>");
				if(ais[week]!=null&&ais[week].size()>0){
				   for(ActivityItem a:ais[week]){
				   out.print("<tr onmouseover=\"overTag(this);\" onmouseout='outTag(this);'><td>"+a.getActivityId()+"</td><td>"+a.getTitle()+"%"+a.getTime()+"</td><td>"+(a.getStartMapName()==null||"".equals(a.getStartMapName())?"无":a.getStartMapName()+"-横:"+a.getStartX()+"- 纵:"+a.getStartY())+"</td>");
                   out.print("<td>"+a.getTextColor()+"</td><td>"+a.getDetail()+"</td><td>"+a.getWeight()+"</td>");
                   out.print("<td>"+a.getMinimalLevelLimit()+"/"+a.getMaximalLevelLimit()+"</td>");
                   out.print("<td>"+a.getCamp()+"</td>");
                   out.print("<td><input type=\"button\" value='添加' onclick='insert(this,"+week+");' />"); 
                   out.print("<input type=\"button\" value='删除' onclick='delete1("+a.getActivityId()+","+week+");' />");
                   out.print("<input type=\"button\" value='更新' onclick='update(this,"+week+");' />");          				   
				   out.print("</td></tr>");
				   }
				}
				out.print("</tbody></table><input type=\"button\" value='添加' onclick='insert(\"null\","+week+");' /></form>");
			} catch (Exception e) {
			//e.printStackTrace();
			//    out.print(e.getMessage());
				//RequestDispatcher rdp = request
				//		.getRequestDispatcher("../gmuser/visitfobiden.jsp");
				//rdp.forward(request, response);
               out.print(StringUtil.getStackTrace(e));
			}
		%>
	</body>
</html>
