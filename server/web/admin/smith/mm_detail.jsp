<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.util.*,com.fy.engineserver.smith.*,com.fy.engineserver.enterlimit.*"%><%!
public String formatLine(String name, String ip, long outMoney, int level, long rmb, long currentMoney, long mmId, boolean red) {
	StringBuffer sb = new StringBuffer();
	sb.append("<table><tr style='border:1px #ccc solid;"+(red?"color:red;":"")+"'>");
	sb.append("<td style='width:10px;'><input type='checkbox' name=except value='"+mmId+"'></td>");
	sb.append("<td style='width:180px;'>" + name + "</td>");
	sb.append("<td style='width:50px;color:orange'>" + String.valueOf(level) + "</td>");
	sb.append("<td style='width:160px;'>" + ip + "</td>");
	sb.append("<td style='width:60px;color:#35c851'>" + String.valueOf(outMoney) + "</td>");
	sb.append("<td style='width:60px;color:red'>" + rmb + "</td>");
	sb.append("<td style='width:80px;color:blue'>" + String.valueOf(currentMoney) + "</td>");
	sb.append("</tr></table>");
	return sb.toString();
}

public String getMoneyMakerTable(MoneyMaker mm, StringBuffer sb) {
	if(mm == null) {
		return "";
	}
	HashSet<Long> set = new HashSet<Long>();
	for(int i=0; i<mm.getDownList().size(); i++) {
		set.add(mm.getDownList().get(i));
	}
	int downNum = set.size();
	boolean f1 = MoneyRelationShipManager.getInstance().isForbid(mm.getId());
	sb.append("<table><tr height='18'><td rowspan='"+downNum+"' style='border:1px #ccc solid;"+(f1?"color:red;":"")+"'>" + formatLine(mm.getUsername(), (mm.getIp()==null?"":(mm.getIp().indexOf(":")!=-1?mm.getIp().substring(1, mm.getIp().indexOf(":")):mm.getIp())), mm.getTotalUp()/1000, mm.getLevel(), mm.getRmb()/100, mm.getCurrentSilver()/1000, mm.getId(), f1) + "</td>");
	int i = 0;
	for(Long mmId : set) {
		if(i > 0) {
			sb.append("<tr height='18'>");
		}
		MoneyMaker maker = MoneyRelationShipManager.getInstance().getMoneyMaker(mmId);
		if(maker == null) {
			sb.append("<td style='border:1px #ccc solid'>此下线丢失</td>");
		} else {
			boolean f2 = MoneyRelationShipManager.getInstance().isForbid(maker.getId());
			if(maker.getDownList().size() > 0) {
				sb.append("<td style='border:1px #ccc solid;"+(f2?"color:red;":"")+"'>");
				getMoneyMakerTable(maker, sb);
				sb.append("</td>");
			} else {
				sb.append("<td style='border:1px #ccc solid;"+(f2?"color:red;":"")+"'>"+formatLine(maker.getUsername(), (maker.getIp()==null?"null":(maker.getIp().indexOf(":")!=-1?maker.getIp().substring(1, maker.getIp().indexOf(":")):maker.getIp())), maker.getTotalUp()/1000, maker.getLevel(), maker.getRmb()/100, maker.getCurrentSilver()/1000, maker.getId(), f2)+"</td>");
			}
		}
		sb.append("</tr>");
		i++;
	}
	sb.append("</table>");
	return sb.toString();
}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	<title></title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<script type="text/javascript">
	function doForbid() {
		document.getElementById("action").value="forbid";
		f1.submit();
	}
	function viewProcess() {
		document.getElementById("action").value="process";
		f1.submit();
	}
	function doRelease() {
		document.getElementById("action").value="release";
		f1.submit();
	}
	function doReleaseMaker() {
		document.getElementById("action").value="releaseMaker";
		f1.submit();
	}
	function doRemove() {
		document.getElementById("action").value="remove";
		f1.submit();
	}
	function doSymbolForbid() {
		document.getElementById("action").value="symbolforbid";
		f1.submit();
	}
	function doForbidSelect() {
		document.getElementById("action").value="forbidselect";
		f1.submit();
	}
	</script>
	<style>
body {
	padding: 0;
	border: 0;
	margin: 0;
	font-size: 9px;
	font-family: verdana, arial, sans-serif;
	color: #545454;
}

td.total {
	padding: 2px;
}
	</style>
	</head>

	<body>
		<%     
		MoneyRelationShipManager msm = MoneyRelationShipManager.getInstance();
		String id = request.getParameter("id");
		String pid = request.getParameter("pid");
		if(pid!=null){
			Map<Long, Integer> shipmap = msm.getPlayerRelationShipMap();
			if(shipmap!=null && shipmap.size()>0){
				Integer shipid = shipmap.get(Long.parseLong(pid));
				if(shipid!=null){
					if(shipid.intValue()>0){
						id = shipid.intValue()+"";
					}
				}
			}
		}
		
		MoneyRelationShip ship = msm.getAllMoneyRelationShip().get(Integer.valueOf(id.trim()));
		String action = request.getParameter("action");
		if(ship != null && action != null && action.equals("forbid")) {
			String exceptStr[] = request.getParameterValues("except");
			List<Long> ids = new ArrayList<Long>();
			if(exceptStr != null && exceptStr.length > 0) {
				for(String s : exceptStr) {
					ids.add(Long.valueOf(s));
				}
			}
			msm.forbidShip(ship, ids, false);
		} else if(ship != null && action != null && action.equals("process")) {
			HashMap<String, Integer> pmap = new HashMap<String, Integer>();
			StringBuffer sb = new StringBuffer();
			String exceptStr[] = request.getParameterValues("except");
			if(exceptStr != null && exceptStr.length > 0) {
				
				for(String s : exceptStr) {
					Player_Process pp = EnterLimitManager.player_process.get(Long.valueOf(s));
					if(pp != null) {
						String ps[] = pp.getAndroidProcesss();
						for(String p : ps) {
							Integer n = pmap.get(p);
							if(n == null) {
								n = 1;
							} else {
								n += 1;
							}
							pmap.put(p, n);
							if(n == exceptStr.length) {
								sb.append(p + ";");
							}
						}
					}
				}
				
				out.println(exceptStr.length + "个账号中公共的进程:<br>"+sb.toString().replaceAll(";","<br>"));
			}
		} else if(ship != null && action != null && action.equals("forbidselect")) {
			String exceptStr[] = request.getParameterValues("except");
			List<Long> ids = new ArrayList<Long>();
			if(exceptStr != null && exceptStr.length > 0) {
				for(String s : exceptStr) {
					MoneyMaker am = msm.getMoneyMaker(Long.valueOf(s));
					if(am != null) {
						msm.recursiveForbidMoneyMaker(am, new ArrayList<Long>(), false);
					}
				}
			}
			symbolForbidShip(ship);
		} else if(ship != null && action != null && action.equals("symbolforbid")) {
			symbolForbidShip(ship); 
		} else if(ship != null && action != null && action.equals("release")) {
			msm.releaseShip(ship);
		} else if(ship != null && action != null && action.equals("releaseMaker")) {
			String exceptStr[] = request.getParameterValues("except");
			List<Long> ids = new ArrayList<Long>();
			if(exceptStr != null && exceptStr.length > 0) {
				for(String s : exceptStr) {
					releaseMaker(Long.valueOf(s));
				}
			}
		} 
		else if(ship != null && action != null && action.equals("remove")) {
			msm.removeShip(ship);
		}
		boolean forbid = false;
		if(ship != null) {
			List<Long> topLevelList = ship.getTopLevelList();
			forbid = msm.isForbid(ship);
		%>
	    <center>         <br>                                  
	    <h1>银子汇聚组织网络详情<%=forbid?"<font color='red'>(已封禁)</font>":"" %>(<font color="orange">等级</font> IP <font color="#35c851">外转</font> <font color="red">总充值</font> <font color="blue">当前银两</font>)</h1> 
	    </center>
		<center>
		<form action="mm_detail.jsp" method=post name=f1>
		<table style="border:0px;">
			<%for(Long mmId : topLevelList) {
				MoneyMaker mm = msm.getMoneyMaker(mmId);
				StringBuffer sb = new StringBuffer();
				String html = getMoneyMakerTable(mm, sb);
			%>
			<tr>
				<td>
				<%=html %>
				</td>
			</tr>
			<%} %>
		</table><br><br>
		<%} %>
		<center>
		<input type=hidden name=id value="<%=ship!=null?ship.getId():"" %>" id="id">
		<input type=hidden name=action value="" id="action">
		<br>
		<%if(!forbid) {%>
		<input type=button name=bt2 value=" 封禁未选的所有 " onclick="doForbid()"/>
		<input type=button name=bt2 value=" 查看选中的进程共性 " onclick="viewProcess()"/>
		<input type=button name=bt2 value=" 封禁选中的及下线 " onclick="doForbidSelect()"/>
		<input type=button name=bt2 value=" 仅标记封禁 " onclick="doSymbolForbid()"/>
		<%} else {%>
		<input type=button name=bt3 value=" 解禁此组织 " onclick="doRelease()"/>
		<input type=button name=bt3 value=" 解封选中的 " onclick="doReleaseMaker()"/>
		<%} %>
		<input type=button name=bt3 value=" 删除此组织 " onclick="doRemove()"/>
		<input type=button name=bt1 value=" 返 回 "  onclick="window.location.replace('mm.jsp');">
		</form>
		<br>
	</body>
</html><%!
public void symbolForbidShip(MoneyRelationShip ship) {
	MoneyRelationShipManager msm = MoneyRelationShipManager.getInstance();
	if(!msm.getForbidShips().contains(ship.getId())) {
		msm.getForbidShips().add(ship.getId());
		if(msm.logger.isInfoEnabled()) {
			msm.logger.info("[标记封禁组织] [id:"+ship.getId()+"]");
		}
	}
}

public void releaseMaker(long id) {
	MoneyRelationShipManager msm = MoneyRelationShipManager.getInstance();
	msm.getForbidPlayers().remove(new Long(id));
	msm.logger.info("[页面手动解封玩家] [id:"+id+"]");
}
%>