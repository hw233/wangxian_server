<%@page import="java.net.URLDecoder"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="com.fy.gamegateway.gmaction.GmActionManager"%>
<%@page import="com.fy.gamegateway.gmaction.GmAction"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.util.concurrent.ConcurrentHashMap"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,com.fy.gamegateway.mieshi.server.*"%>
<%!
	static Map<Long,String> resultMap = new ConcurrentHashMap<Long,String>();
	static Logger logger =  GmActionManager.logger;

	public MieshiPlayerInfo getPlayerNameByUsernameAndServerName(String username,String servername)
	{
		MieshiPlayerInfoManager mm = MieshiPlayerInfoManager.getInstance();
		MieshiPlayerInfo pis[] = mm.getMieshiPlayerInfoByUsername(username);
		MieshiServerInfoManager msm = MieshiServerInfoManager.getInstance();
		List<MieshiPlayerInfo> lst = new ArrayList<MieshiPlayerInfo>();
		for(MieshiPlayerInfo mpi : pis)
		{
			MieshiServerInfo ms = msm.getServerInfoByName(mpi.getServerRealName());
			if(ms.getRealname().equals(servername))
			{
				return mpi;
			}
		}
		
		return null;
	}
	
%>
<%

	
	/**
	游戏服传入的当前时间 和 缓存中时间作比较 若传入时间大于三十 则重新查一个结果并以当前时间作为key 然后进入如下产生结果的步骤
	若不大于三十 直接取缓存结果
	清除缓存 （和当前时间比大于三十分钟的）
	
	1.得到所有的appstore服务器或者其他专服
	2.拼装合适的sql，传入所有的appstore服务器列表，查找合适的结果
	3.过滤判断是否是appstore的服 1 appstore 2 dcn
	4.根据查到的用户账号去找到对应的角色名称和服务器
	5.将结果拼成特定字符串
	6.缓存结果
	select   from (select p.username as username,sum(paymoney) as summoney from orderform o,passport p where o.passportid = p.id
	and o.servername in ('王者之域',?) and o.notifysucc = 'T' and o.createtime>= 开始 and o.createtime <= 结束 group by p.username order by summoney desc) t where rownum <= ?;
	username@@@@playername@@@@summoney@@@@servername
	
	*/
			
	//String beanName = "outer_gateway_connection_selector";
	
	
	
	long  begintime = ParamUtils.getLongParameter(request, "begintime", -1l);
	long  endtime = ParamUtils.getLongParameter(request, "endtime", -1l);
	long  curtime = ParamUtils.getLongParameter(request, "curtime", -1l);
	
	if(begintime <= 0 || endtime <= 0 || curtime <= 0)
	{
		out.print("fail");
		logger.warn("[查询appstore全服角色充值排行] [失败] [未传入时间] [开始时间:"+begintime+"] [结束时间:"+endtime+"] [游戏服当前时间:"+curtime+"] [服务器类别:--] [服务器名称:--] [num:--] [请求ip:"+request.getRemoteAddr()+"] [查询结果:--]");
		return;
	}
	
	String servertype = ParamUtils.getParameter(request, "servertype");
	if( StringUtils.isEmpty(servertype) )
	{
		out.print("fail");
		logger.warn("[查询appstore全服角色充值排行] [失败] [未传入服务器类型] [开始时间:"+begintime+"] [结束时间:"+endtime+"] [游戏服当前时间:"+curtime+"] [服务器类别:"+servertype+"] [服务器名称:--] [num:--] [请求ip:"+request.getRemoteAddr()+"] [查询结果:--]");
		return;
	}
	
	
	
	
	String servername = URLDecoder.decode(ParamUtils.getParameter(request, "servername", ""),"utf-8");
	
	if( StringUtils.isEmpty(servername) )
	{
		out.print("fail");
		logger.warn("[查询appstore全服角色充值排行] [失败] [未传入服务器名称] [开始时间:"+begintime+"] [结束时间:"+endtime+"] [游戏服当前时间:"+curtime+"] [服务器类别:"+servertype+"] [服务器名称:"+servername+"] [num:--] [请求ip:"+request.getRemoteAddr()+"] [查询结果:--]");
		return;
	}
	
	int num = ParamUtils.getIntParameter(request, "num", 0);
	
	String splitStr = "@@@@";
	String splitUser = "\r\n";
	
	long gapTime = 30l * 60l * 1000l;
	
	//清理缓存
	long mycurTime = System.currentTimeMillis();
	try
	{
		Iterator<Long> it = resultMap.keySet().iterator();
		
		while(it.hasNext())
		{
			long ctime = it.next();
			
			if(mycurTime - ctime > gapTime)
			{
				it.remove();
				logger.info("[查询appstore全服角色充值排行] [清理缓存] [开始时间:"+begintime+"] [结束时间:"+endtime+"] [游戏服当前时间:"+curtime+"] [服务器类别:"+servertype+"] [服务器名称:--] [num:--] [请求ip:"+request.getRemoteAddr()+"] [查询结果:--] [缓存中时间:"+ctime+"] [缓存数量:"+resultMap.size()+"] [缓存内容:"+resultMap+"]");
			}
		}
		
		
	
		
		
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection conn = DriverManager.getConnection(
			"jdbc:oracle:thin:@116.213.192.216:1521:ora183", "mieshiboss", "4Fxtkq5J9ydy3sK1");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	/**	
		ArrayList<String> appServer = new ArrayList<String>();
		appServer.add("巍巍昆仑");
		appServer.add("梦倾天下");
		appServer.add("玉幡宝刹");
		appServer.add("西方灵山");
		appServer.add("飞瀑龙池");
		appServer.add("问天灵台");
		appServer.add("傲啸封仙");
		appServer.add("再续前缘");
		appServer.add("裂风峡谷");
		appServer.add("左岸花海");
		appServer.add("白露横江");
		appServer.add("雪域冰城");
		appServer.add("兰若古刹");
		appServer.add("万象更新");
		appServer.add("霹雳霞光");
		appServer.add("永安仙城");
		appServer.add("右道长亭");
		appServer.add("权倾皇朝");
		appServer.add("幻灵仙境");
		appServer.add("独步天下");
		appServer.add("独霸一方");
		appServer.add("对酒当歌");
		appServer.add("诸神梦境");
		appServer.add("仙子乱尘");
		appServer.add("春风得意");
		appServer.add("九霄龙吟");
		appServer.add("飞龙在天");
		appServer.add("天下无双");
		
		if (!appServer.contains(servername)) {
			out.print("fail");
			return ;
		}
	**/	
		if(servertype.toLowerCase().contains("appstore"))
		{
			//得到所有的appstore服务器
			MieshiServerInfoManager msm = MieshiServerInfoManager.getInstance();
			MieshiServerInfo[] mieshiServerInfos = msm.getServerInfoList();
			List<MieshiServerInfo> appstoreMieshiServerInfos = new ArrayList<MieshiServerInfo>();
			
			for(MieshiServerInfo mieshiServerInfo : mieshiServerInfos)
			{
				if(mieshiServerInfo.getServerType() == MieshiServerInfo.SERVERTYPE_苹果正式服务器)
				{
					appstoreMieshiServerInfos.add(mieshiServerInfo);
				}
			}
		
			//判断是否是appstore的服
			boolean isAppstore = false;
			for(MieshiServerInfo mieshiServerInfo : appstoreMieshiServerInfos)
			{
				if(servername.equals(mieshiServerInfo.getRealname()))
				{
					isAppstore = true;
					break;
				}
			}
			
		
			
			if(!isAppstore)
			{
				out.print("fail");
				logger.info("[查询appstore全服角色充值排行] [失败] [传入的服务器不是app服] [开始时间:"+begintime+"] [结束时间:"+endtime+"] [游戏服当前时间:"+curtime+"] [服务器类别:"+servertype+"] [服务器名称:"+servername+"] [num:"+num+"] [请求ip:"+request.getRemoteAddr()+"] [查询结果:--] [缓存中时间:--] [缓存数量:"+resultMap.size()+"] [缓存内容:"+resultMap+"]");
				return;
			}
			
			it = resultMap.keySet().iterator();
			while(it.hasNext())
			{
				long ctime = it.next();
				
				if(curtime - ctime <= gapTime)
				{
					out.print(resultMap.get(ctime));
					logger.info("[查询appstore全服角色充值排行] [成功] [ok] [开始时间:"+begintime+"] [结束时间:"+endtime+"] [游戏服当前时间:"+curtime+"] [服务器类别:"+servertype+"] [服务器名称:"+servername+"] [num:"+num+"] [请求ip:"+request.getRemoteAddr()+"] [查询结果:"+resultMap.get(ctime)+"] [缓存中时间:"+ctime+"] [缓存数量:"+resultMap.size()+"] [缓存内容:"+resultMap+"]");
					return;
				}
			}
			
			
			String servers = "";
			
			for(MieshiServerInfo mieshiServerInfo : appstoreMieshiServerInfos)
			{
				servers += "'"+mieshiServerInfo.getRealname()+"'" + ",";
			}
			
			servers = servers.substring(0, servers.length()-1);
			
			String sql = "select *  from (select p.username as username,sum(paymoney) as summoney,o.servername as servername from orderform o,passport p where o.passportid = p.id "+
					"and o.servername in ("+servers+") and o.notifysucc = 'T' and o.createtime >= ? and o.createtime <= ? group by p.username,o.servername order by summoney desc) t where rownum <= ?";
			
			logger.info(sql);
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, begintime);
			pstmt.setLong(2, endtime);
			pstmt.setLong(3, num);
			
			rs = pstmt.executeQuery();
			String returnString = "";
			
			while(rs.next())
			{
				//username@@@@playername@@@@summoney@@@@servername
				String username = rs.getString(1);
				long summoney = rs.getLong(2);
				String chargeservername = rs.getString(3);
				
				//根据账号,服务器名称查找角色名称
				MieshiPlayerInfo mpi = getPlayerNameByUsernameAndServerName(username, chargeservername);
				if(mpi == null)
				{
					out.print("fail");
					logger.error("[查询appstore全服角色充值排行] [失败] [账号在服务器上不存在角色] [开始时间:"+begintime+"] [结束时间:"+endtime+"] [游戏服当前时间:"+curtime+"] [服务器类别:"+servertype+"] [服务器名称:"+servername+"] [num:"+num+"] [请求ip:"+request.getRemoteAddr()+"] [查询结果:--] [缓存中时间:--] [缓存数量:"+resultMap.size()+"] [缓存内容:"+resultMap+"]");
					return;
				}
				String playername = mpi.getPlayerName();
				
				returnString += username+splitStr+playername+splitStr+summoney+splitStr+chargeservername+splitUser;
			}
			if (returnString.length() > 0) {
				returnString = returnString.substring(0, returnString.lastIndexOf(splitUser));
			}
			out.print(returnString);
			logger.info("[查询appstore全服角色充值排行] [成功] [ok] [开始时间:"+begintime+"] [结束时间:"+endtime+"] [游戏服当前时间:"+curtime+"] [服务器类别:"+servertype+"] [服务器名称:"+servername+"] [num:"+num+"] [请求ip:"+request.getRemoteAddr()+"] [查询结果:"+returnString+"] [缓存中时间:--] [缓存数量:"+resultMap.size()+"] [缓存内容:"+resultMap+"]");
			resultMap.put(mycurTime, returnString);
			rs.close();
			pstmt.close();
			conn.close();
			
		}
		else
		{
			out.print("fail");
		
		}
	}
	catch(Exception e)
	{
		out.print("fail");
		logger.error("[查询appstore全服角色充值排行] [失败] [出现未知错误] [开始时间:"+begintime+"] [结束时间:"+endtime+"] [游戏服当前时间:"+curtime+"] [服务器类别:"+servertype+"] [服务器名称:"+servername+"] [num:"+num+"] [请求ip:"+request.getRemoteAddr()+"]",e);
		
	}
%>

