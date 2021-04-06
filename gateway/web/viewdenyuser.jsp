<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfoManager.DenyUser"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.concrete.DiskCacheHelper"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.DiskCache"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.concrete.DataBlock"%>
<%@page import="java.io.File"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfoManager"%>
<%
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
								
								if(al.size() == 0)
								{
									al.add(du);
								}
								else
								{
									for(int j = 0; j < al.size(); j++)
									{
										DenyUser duu = (DenyUser) al.get(j);
										if(username.equals(duu.username))
										{
											al.set(j, du);
											out.println("有重名:"+username+"<br/>");
											break;
										}
										else
										{
											al.add(du);
											break;
										}
										
									}
								}
							}
						}
					}
					
					out.print("读到arraylist"+((ArrayList)o).size()+"<br/>");
				}
				else{
					out.println("ooooo:"+o+":classname:"+o.getClass().getCanonicalName()+"<br/>");
				}
			}
		}
	}
	//
	MieshiServerInfoManager.getInstance().getDenyUserList().clear();
	MieshiServerInfoManager.getInstance().getDenyUserList().addAll(al);
	cache.put("testDenyUserList", al);

%>