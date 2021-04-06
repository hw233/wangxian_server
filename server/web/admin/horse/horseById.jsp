<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@page import="com.fy.engineserver.datasource.article.data.props.*"%>
<%@page import="com.fy.engineserver.datasource.article.manager.*"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.*"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.*"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.*"%>
<%@page import="com.fy.engineserver.sprite.horse.*"%>
<%@page import="com.fy.engineserver.sprite.*"%>
<%@page import="com.fy.engineserver.sprite.concrete.*"%>
<%@page import="java.util.*"%>


<%@page import="com.fy.engineserver.sprite.horse.HorseManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>人物所有坐骑</title>
<link rel="stylesheet" type="text/css" href="../css/common.css" />


<script type="text/javascript">

	function horseEquipment(obj,obj1){
		document.getElementById("horseEquipmentId").value =obj;
		document.getElementById("horseId").value =obj1;
		document.useHorseEquipment.submit();
	}
	function horseUseFashion(obj,obj1){
		document.getElementById("horseFashionId").value =obj;
		document.getElementById("horseIdd").value =obj1;
		document.useHorseFashion.submit();
	}
	
	function takeOffHorseEquipment(obj,obj1){
		document.getElementById("takeOffEquipmentId").value =obj;
		document.getElementById("takeOffEquipmentHorseId").value =obj1;
		document.takeOffEquipmentForm.submit();
	}
	function takeOffHorseFashion(obj){
		document.getElementById("takeOffFashionHorseId").value =obj;
		document.takeOffHorseFashion.submit();
	}
	
function divHidden(){
	  var obj = document.getElementById("showdiv");
	  obj.style.display="none";
}


//给url地址增加时间戳，不读取缓存    
function convertURL(url) {    
        //获取时间戳
        var timstamp = (new Date()).valueOf();    
        //将时间戳信息拼接到url上    
        //url = "AJAXServer"
        if (url.indexOf("?") >= 0) {
            url = url + "&t=" + timstamp;    
        } else {
            url = url + "?t=" + timstamp;    
        }
        return url;
}

function ajaxFunction(path,xy,id){
		var xmlHttp;
		try
		   {
		  // Firefox, Opera 8.0+, Safari
		   xmlHttp=new XMLHttpRequest();
		   }
		catch (e)
		   {
		 // Internet Explorer
		  try
		     {
		     xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
		     }
		  catch (e)
		     {
		     try
		        {
		        xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
		        }
		     catch (e)
		        {
		        alert("您的浏览器不支持AJAX！");
		        return false;
		        }
		     }
		   }
		
		xmlHttp.onreadystatechange=function()
		{
		if(xmlHttp.readyState==4)
		  {
		   //document.myForm.name.value=xmlHttp.ResponseText;
		   document.getElementById("showdiv").innerHTML = xmlHttp.responseText;
		   setposition(xy);
		  }
		}
		var playerIdStr = document.getElementById("playerIdStr").value;
		var url = path+"/admin/ArticlesSelectforajax.jsp?playerid="+playerIdStr+"&articleid="+id;
		xmlHttp.open("GET",convertURL(url),true);
		xmlHttp.send(null);

}


function setposition(id){
	  var obj = document.getElementById("showdiv");
		obj.style.display="";
		obj.style.border="1px solid red";
		var objheight = obj.clientHeight;
		var objwidth = obj.clientWidth;
		var o = document.getElementById(id);
		var lefttopx = o.offsetLeft;
		var lefttopy = o.offsetTop;
		var oparent = o.offsetParent;
		while(oparent.tagName != "BODY"){
			lefttopx = lefttopx + oparent.offsetLeft;
			lefttopy = lefttopy + oparent.offsetTop;
			oparent = oparent.offsetParent;
		}
		
		
		var righttopx = lefttopx+o.offsetWidth;
		//var righttopy = o.offsetTop;
		//var leftbottomx = o.offsetLeft;
		var leftbottomy = lefttopy+o.offsetHeight;
		//var rightbottomx = o.offsetLeft+o.offsetWidth;
		//var rightbottomy = o.offsetTop+o.offsetHeight;
		var scrollTopvalue = document.documentElement.scrollTop;
		var scrollLeftvalue = document.documentElement.scrollLeft;
		var clientHeightvalue = document.documentElement.clientHeight;
		var clientWidthvalue = document.documentElement.clientWidth;
		//如果相对左边横坐标大于浏览器宽度的1/2，那么就把位置设置在这个对象的左边，以此类推就可以比较好的显示
		if((lefttopx-scrollLeftvalue)>(clientWidthvalue/2)){
			if((lefttopx-2)>objwidth){
				obj.style.left = (lefttopx-2-objwidth)+"px";
			}else{
				obj.style.left = (scrollLeftvalue+2)+"px";
			}
		}else{
			obj.style.left=(righttopx+2)+"px";
		}
		if((lefttopy-scrollTopvalue)> (clientHeightvalue/2)){
			if(objheight>clientHeightvalue){
				obj.style.top = (scrollTopvalue+2)+"px";
			}else{
				if((lefttopy-2)>objheight){
					obj.style.top = (lefttopy-2-objheight)+"px";
				}else{
					obj.style.top = (scrollTopvalue+2)+"px";
				}
			}
			
		}else{
			obj.style.top = (leftbottomy+2)+"px";
		}
}

</script>



</head>

<body>
	<%
		PlayerManager pm = PlayerManager.getInstance();
		HorseManager hm = HorseManager.getInstance();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
	
	%>
	<%		
			Horse h = null;
			Player player = null;
			String errorMsg = null;
			long[] ids = null;
			long fashionId = -1;
			long playerId = 0;
			Object obj = session.getAttribute("playerid");
			if(obj != null){
				playerId = Long.parseLong(obj.toString());
				player = pm.getPlayer(playerId);
				long id = Long.parseLong(request.getParameter("horseById").trim());
				h = hm.getHorseById(id,player);
				
				if(h != null){
					int energyIndex = h.getLastEnergyIndex();
					ids = h.getEquipmentColumn().getEquipmentIds();
					fashionId = h.getFashionId();
				}else{
					errorMsg = "出错";
				}
			}else{
				errorMsg = "出错";
			}
	
	 %>
	<a href="<%= path %>/admin/horse/queryHorse.jsp">返回</a>
	<br>
	<br>
	<% if(errorMsg != null){
		%>
		<%= errorMsg %>
		<%
	}else{
	%>
	<input type="hidden" name ="playerIdStr" id="playerIdStr" value="'<%=playerId %>'">
	<div id="showdiv" style="display:none;position:absolute"></div>
	<div id = "main">
		<div id = "left" style="height:405px;width:250px;float:left">
			<table>
				<tr align="center">
					<th colspan="2" height="15px">属性</th>
				</tr>
				<tr>
					<td height="15px" >速度</td>
					<td><%=h.getSpeed() %></td>
				</tr>
				<tr>
					<td height="15px" >最大hp</td>
					<td><%=h.getMaxHP() %></td>
				</tr>
				<tr>
					<td>物理攻击</td>
					<td><%=h.getPhyAttack() %></td>
				</tr>
				<tr>
					<td>魔法攻击</td>
					<td><%=h.getMagicAttack() %></td>
				</tr>
				<tr>
					<td>物理防御</td>
					<td><%=h.getPhyDefence() %></td>
				</tr>
				<tr>
					<td>魔法防御</td>
					<td><%=h.getMagicDefence() %></td>
				</tr>
				<tr>
					<td>最大mp</td>
					<td><%=h.getMaxMP() %></td>
				</tr>
				<tr>
					<td>破防</td>
					<td><%=h.getBreakDefence() %></td>
				</tr>
				<tr>
					<td>命中</td>
					<td><%=h.getHit() %></td>
				</tr>
				<tr>
					<td>躲闪</td>
					<td><%=h.getDodge() %></td>
				</tr>
				<tr>
					<td>精确</td>
					<td><%=h.getAccurate() %></td>
				</tr>
				<tr>
					<td>会心防御</td>
					<td><%=h.getCriticalDefence() %></td>
				</tr>
				<tr>
					<td>会心攻击</td>
					<td><%=h.getCriticalHit()%></td>
				</tr>
				<tr>
					<td>火攻</td>
					<td><%=h.getFireAttack() %></td>
				</tr>
				<tr>
					<td>火防</td>
					<td><%=h.getFireDefence() %></td>
				</tr>
				<tr>
					<td>冰攻</td>
					<td><%=h.getBlizzardAttack() %></td>
				</tr>
				<tr>
					<td>冰防</td>
					<td><%=h.getBlizzardDefence() %></td>
				</tr>
				<tr>
					<td>雷攻</td>
					<td><%=h.getThunderAttack() %></td>
				</tr>
				<tr>
					<td>雷防</td>
					<td><%=h.getThunderDefence() %></td>
				</tr>
				<tr>
					<td>风攻</td>
					<td><%=h.getWindAttack() %></td>
				</tr>
				<tr>
					<td>风防</td>
					<td><%=h.getWindDefence() %></td>
				</tr>
				<tr>
					<td>减少对方火防</td>
					<td><%=h.getFireIgnoreDefence() %></td>
				</tr>
				<tr>
					<td>减少对方冰防</td>
					<td><%=h.getBlizzardIgnoreDefence() %></td>
				</tr>
				<tr>
					<td>减少对方雷防</td>
					<td><%=h.getThunderIgnoreDefence() %></td>
				</tr>
				<tr>
					<td>减少对象风防</td>
					<td><%=h.getWindIgnoreDefence() %></td>
				</tr>
			
			</table>
		
		</div>
		<div id ="rigth" style="height:405px;width:600px;float:left">
		
		
		<h2>坐骑身上装备</h2>
		<form name="takeOffEquipmentForm" action="takeOffEquipment.jsp"  method="get">
					<input type="hidden" id="takeOffEquipmentId" name="takeOffEquipmentId" />
					<input type="hidden" id="takeOffEquipmentHorseId" name="takeOffEquipmentHorseId" />
		</form>
		
		<% 
			boolean bln = false;
			for(long id : ids){
				if(id == -1){
					out.println(-1);
					continue;
				}
				bln = true;
				ArticleEntity ae =  aem.getEntity(id);
				String name= ae.getArticleName();
			%>
				<div style="height:30px;" id='<%=id%>'>
					<%= name%>
					<img width="16" height="16" src="/imageServlet?id=<%= am.getArticle(ae.getArticleName())== null?"":am.getArticle(ae.getArticleName()).getIconId()%>" 
						onmouseover="javascript:ajaxFunction('<%= path %>','<%=ae.getId() %>','<%=ae.getId() %>');" 
						onmouseout="javascript:divHidden();" />
					<%=id %>
					<a href ="javascript:takeOffHorseEquipment('<%= id%>','<%=h.getHorseId() %>')">脱装备</a>
				</div>
				
				<%
			
			}
			
			if(fashionId >0){
				bln = true;
				ArticleEntity ae =  aem.getEntity(fashionId);
				String name= ae.getArticleName();
				%>
				<div style="height:30px;" id='<%=fashionId%>'>
					<%= name%>
					<img width="16" height="16" src="/imageServlet?id=<%= am.getArticle(ae.getArticleName())== null?"":am.getArticle(ae.getArticleName()).getIconId()%>" 
						onmouseover="javascript:ajaxFunction('<%= path %>','<%=ae.getId() %>','<%=ae.getId() %>');" 
						onmouseout="javascript:divHidden();" />

					<a href ="javascript:takeOffHorseFashion('<%=h.getHorseId() %>')">脱时装</a>
					<form name="takeOffHorseFashion" action="takeOffHorseFashion.jsp" method="get">
						<input type="hidden" id="takeOffFashionHorseId" name="takeOffFashionHorseId" />
					</form>
				</div>
				
				
				<%
			}
			if(!bln){
				%>
				<div style="height:30px; ">没有装备</div>
				<%
			}
		%>
		<hr>
		<h2>坐骑可以穿的装备</h2>
		
		<%
		List<Long> list = new ArrayList<Long>();
		Knapsack k = player.getKnapsack_common();
		if(k != null){
			int num = k.getCells().length;
			for(int i = 0;i<num ;i++){
				Cell cell = k.getCell(i);
				if(cell != null){
					long id = cell.getEntityId();
					ArticleEntity ae = aem.getEntity(id);
					if(ae != null){
						Article a = am.getArticle(ae.getArticleName());
						if( a!= null && a instanceof Equipment ){
							Equipment e = (Equipment)a;
							if(e.canUse(h) == null){
								list.add(id);
								out.println("****"+id+"***");
							}else{
								out.print("horse"+e.canUse(h)+"<br/>");
							}
						}
					}
				}
				
				
			}
		}
		k = player.getKnapsack_fangbao();
		if(k != null){
			int num = k.getCells().length;
			out.print(num+"fangbao");
			for(int i = 0;i<num ;i++){
				Cell cell = k.getCell(i);
				if(cell != null){
					long id = cell.getEntityId();
					ArticleEntity ae = aem.getEntity(id);
					if(ae != null){
						Article a = am.getArticle(ae.getArticleName());
						if( a!= null && a instanceof Equipment ){
							Equipment e = (Equipment)a;
							if(e.canUse(h) == null){
								list.add(id);
								out.println("###"+id+"###");
							}
						}
					}
				}
				
				
			}
		}
		List<Long> fashionList = new ArrayList<Long>();
		Knapsack[] ks = player.getKnapsacks_common();
		for(Knapsack kk : ks){
			if(kk != null){
				int num = kk.getCells().length;
				for(int i = 0;i<num ;i++){
					Cell cell = kk.getCell(i);
					if(cell != null){
						long id = cell.getEntityId();
						ArticleEntity ae = aem.getEntity(id);
						if(ae != null){
							Article a = am.getArticle(ae.getArticleName());
							if( a!= null && a instanceof AvataProps ){
								AvataProps e = (AvataProps)a;
								if(e.canUse(h)){
									fashionList.add(id);
								}
							}
						}
					}
				}
			}
			
			
		}
		int i =list.size();
		long[] idss = new long[i];
		for(int j =0 ;j< i ;j++){
			idss[j] = list.get(j);
		}
		
		%>
		
	<form name="useHorseEquipment" action="useHorseEquipment.jsp"  method="get">
			<input type="hidden" id="horseEquipmentId" name="horseEquipmentId" />
			<input type="hidden" id="horseId" name="horseId" />
	</form>
	<form name="useHorseFashion" action="useHorseFashion.jsp"  method="get">
			<input type="hidden" id="horseFashionId" name="horseFashionId" />
			<input type="hidden" id="horseIdd" name="horseIdd" />
	</form>
	
	
		
		<% 
			boolean bln1 = false;
			for(long id : idss){
				if(id == -1){
					continue;
				}
				bln1 = true;
				ArticleEntity ae =  aem.getEntity(id);
				String name= ae.getArticleName();
			%>
				<div style="height:30px;" id='<%=id%>'>
					<%= name%>
					<img width="16" height="16" src="/imageServlet?id=<%= am.getArticle(ae.getArticleName())== null?"":am.getArticle(ae.getArticleName()).getIconId()%>" 
						onmouseover="javascript:ajaxFunction('<%= path %>','<%=ae.getId() %>','<%=ae.getId() %>');" 
						onmouseout="javascript:divHidden();" />
						
					<a href ="javascript:horseEquipment('<%= ae.getId()%>','<%=h.getHorseId() %>')">使用</a>
				</div>
				
				<%
			
			}
			
			for(long id : fashionList){
				bln1 = true;
				ArticleEntity ae =  aem.getEntity(id);
				String name= ae.getArticleName();
				%>
				<div style="height:30px;" id='<%=id%>'>
				<%= name%>
				<img width="16" height="16" src="/imageServlet?id=<%= am.getArticle(ae.getArticleName())== null?"":am.getArticle(ae.getArticleName()).getIconId()%>" 
					onmouseover="javascript:ajaxFunction('<%= path %>','<%=ae.getId() %>','<%=ae.getId() %>');" 
					onmouseout="javascript:divHidden();" />
					
				<a href ="javascript:horseUseFashion('<%= ae.getId()%>','<%=h.getHorseId() %>')">使用时装</a>
			</div>
				<%
			}
			if(!bln1){
				%>
				<div style="height:30px; ">包里没有可用装备</div>
				<%
			}
		%>
		</div>
		</div>
		<%
	}
	%>
	
</body>
</html>
