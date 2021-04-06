<%@page import="java.io.File"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfoManager.DenyUser"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.concrete.DiskCacheHelper"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.concrete.DataBlock"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache"%>
<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,com.fy.gamegateway.mieshi.server.*"%>
<%@page import="com.xuanzhi.tools.text.*,com.xuanzhi.tools.page.* "%><%


//String beanName = "outer_gateway_connection_selector";
	MieshiServerInfoManager mm = MieshiServerInfoManager.getInstance();
	
	
	
	int totalDenyUserNum = 0;
	int totalForeverDenyUserNum = 0;
	long now = System.currentTimeMillis();
	ArrayList<MieshiServerInfoManager.DenyUser> duList = mm.getDenyUserList();
	for(int i = 0 ; i < duList.size() ; i++){
		out.print(  "username:"+duList.get(i).username+ ":denyuser.enableDeny:"+ duList.get(i).enableDeny+":foreverDeny:"+duList.get(i).foroverDeny +"<br/>");
	}
	
	//读取所有块
	
	DefaultDiskCache cache =  new DefaultDiskCache(new File(MieshiServerInfoManager.getInstance().getCacheFile()),"mieshi-server-info-cache",100L*365*24*3600*1000L);
	
	DataBlock[] blocks = cache.getDataBlocks();
	
	ArrayList al  = new ArrayList();
	boolean canAdd = false;
	for(DataBlock block : blocks)
	{
		if(block == null)
		{
			continue;
		}
		Object[] os = null;
		try
		{
			os = DiskCacheHelper.readData(cache, block);
		}
		catch(Exception e){
			
		}
		if(os != null)
		{
		
			for(Object o : os)
			{
				if(o instanceof ArrayList)
				{
					ArrayList lst = (ArrayList) o;
					for(int i=0; i < lst.size(); i++)
					{
						if(lst.get(i) instanceof DenyUser)
						{
							DenyUser du = (DenyUser) lst.get(i);
							if(du != null)
							{
								String username = du.username;
								out.print( "username:"+username+":i:"+i+"<br/>");
							}
						}
					}
					
				}
				else{
					out.println("ooooo:"+o+":classname:"+o.getClass().getCanonicalName()+"<br/>");
				}
			}
		}
	}
	//
/* 	MieshiServerInfoManager.getInstance().getDenyUserList().clear();
	MieshiServerInfoManager.getInstance().getDenyUserList().addAll(al);
	cache.put("testDenyUserList", al); */
	
	
%><html>
<head>
</head>
<body>
</body>
</html> 
