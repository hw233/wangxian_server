<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfoManager"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfoManager.TestUser"%>
<%@page import="java.util.Date"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem"%>
<%@page import="java.io.File"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.DiskCache"%>
<%@page import="com.fy.gamegateway.thirdpartner.lenovo.LenovoClientService"%>
<%@page import="com.fy.gamegateway.thirdpartner.baoruan.BaoRuanBossClientService"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="java.io.Serializable"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@page import="com.xuanzhi.tools.authorize.Role"%>
<%@page import="com.xuanzhi.tools.authorize.User"%>
<%@page import="com.xuanzhi.tools.authorize.UserManager"%>
<%@page import="com.xuanzhi.tools.authorize.RoleManager"%>
<%@page import="com.xuanzhi.tools.authorize.AuthorizeManager"%>
<%!
	public static class OpRecord implements Serializable
	{
		public long optime;
		public String mesc;
		public String username;
		public String action;
	}

DiskCache opCache = new DefaultDiskCache(new File("/home/game/resin_gateway/webapps/game_gateway/WEB-INF/diskCacheFileRoot/op.ddc"), "操作日志", 1000L*60*60*24*MieshiGatewaySubSystem.CACHE_DISTIME);

%>

<%
/**
一条操作记录 应该包含 时间 谁操作的 操作了什么
*/

Logger logger = LenovoClientService.logger;
String act = request.getParameter("action");

String username = request.getParameter("username");

StringBuffer oprecord = new StringBuffer();

StringBuffer oprecStr = new StringBuffer();


//如果是高级经理则vip目录下所有页面可见，否则有选择的可见
	String curUser = (String)session.getAttribute("authorize.username");

	AuthorizeManager authorizeManager = AuthorizeManager.getInstance();
	RoleManager roleManager = authorizeManager.getRoleManager();
	UserManager userManager = authorizeManager.getUserManager();
	User user = userManager.getUser(curUser);
	Role roles[] = user.getRoles();

String strroles = "";
for(Role r : roles)
{
	strroles+=r.getName()+",";
}
	
if(act == null || act.trim().length() == 0)
{
	act = "";
	//把页面的所有参数记录下来
	Map map = request.getParameterMap();
	Iterator it = map.keySet().iterator();
	
	while(it.hasNext())
	{
		String key = (String)it.next();
		act += " "+key+"="+((String[])map.get(key))[0];
	}
}

oprecord.append("user:"+user.getName());
oprecord.append("	");
oprecord.append("action:"+act);
oprecord.append(" url:"+request.getRequestURI()+" context:");

logger.warn("[权限] ["+oprecord.toString()+"]");
/* OpRecord opRecord = new OpRecord();
opRecord.optime = System.currentTimeMillis();
opRecord.action = act;
opRecord.username = user.getName();
opRecord.mesc = strroles+"  "+"url:"+request.getRequestURI();
ArrayList arrayList =  null;
if(opCache.get("oplog") == null)
{
	arrayList =  new ArrayList();
}
else
{
	arrayList = (ArrayList)opCache.get("oplog");
	
}
arrayList.add(opRecord);
opCache.put("oplog", arrayList); */


oprecStr.append(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
oprecStr.append("@@");
oprecStr.append(user.getName());
oprecStr.append("@@");
oprecStr.append((act == null ? "暂无操作" : act ));
oprecStr.append("@@");
oprecStr.append((request.getRequestURI() == null ? "无" : request.getRequestURI() ));
oprecStr.append("@@");
if(request.getQueryString() != null &&  request.getQueryString().indexOf("testuser.jsp") > -1)
{
	if(username != null)
	{
		MieshiServerInfoManager mi = MieshiServerInfoManager.getInstance();
		TestUser tt = mi.getTestUser(username);
		if(tt != null)
		{
			oprecStr.append("url:"+request.getQueryString()+" reason:"+tt.reason+" bumen:"+tt.bumen);
		}
		else
		{
			oprecStr.append(request.getQueryString());
		}
		//oprecStr.append(( == null ? "无" : request.getQueryString()));
	}
	else
	{
		oprecStr.append(request.getQueryString());
	}
}
else
{
	oprecStr.append("无");
}
	

ArrayList strList =  null;
if(opCache.get("oplogstr") == null)
{
	strList =  new ArrayList();
}
else
{
	strList = (ArrayList)opCache.get("oplogstr");
	
}
strList.add(oprecStr);
opCache.put("oplogstr", strList);
%>