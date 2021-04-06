<%@page import="com.fy.gamegateway.thirdpartner.migu.MiguWorker"%>
<%@page import="com.fy.boss.tools.JacksonManager"%>
<%@page import="com.fy.gamegateway.thirdpartner.migu.SimplePlayer4Migu"%>
<%@page import="com.xuanzhi.tools.servlet.HttpUtils"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.net.URL"%>
<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,com.fy.gamegateway.mieshi.server.*"%>
<%

		
//String beanName = "outer_gateway_connection_selector";
MieshiPlayerInfoManager mm = MieshiPlayerInfoManager.getInstance();
	
String username = request.getParameter("username");


	
%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache"%>
<%
MieshiPlayerInfo pis[] = mm.getMieshiPlayerInfoByUsername(username);
MieshiServerInfoManager msm = MieshiServerInfoManager.getInstance();
//List<MieshiPlayerInfo> lst = new ArrayList<MieshiPlayerInfo>();
List<SimplePlayer4Migu> lst = new ArrayList<SimplePlayer4Migu>();
List<String> serverNames = new ArrayList<String>();
JacksonManager jacksonManager = JacksonManager.getInstance();
for(MieshiPlayerInfo mpi : pis)
{
	if (!serverNames.contains(mpi.getServerRealName())) {
		serverNames.add(mpi.getServerRealName());
        MieshiServerInfo ms = msm.getServerInfoByName(mpi.getServerRealName());
        if(ms != null);
        {
			Map paramMap = new LinkedHashMap();
			paramMap.put("username", username);
			paramMap.put("op", "queryAllRole");
			paramMap.put("authorize.username", "caolei");
			paramMap.put("authorize.password", "212006");
			HashMap headers = new HashMap();
			String jsons = "";
        	paramMap.put("servername", mpi.getServerRealName());
        	String params = getSignParam(paramMap);
        	if (ms == null || ms.getServerUrl() == null || ms.getServerUrl().isEmpty()) {
        		MiguWorker.logger.warn("[米谷通讯] [查询角色信息] [异常] [" + username + "] [" + mpi.getServerRealName() + "]");
        		continue;
        	} 
        	try {
        		URL url = new URL(ms.getServerUrl()+"/migu/miguQuery");
				byte bytes[] = HttpUtils.webPost(url,params.getBytes("utf-8"), headers, 10000, 10000);
				String encoding = (String)headers.get(HttpUtils.ENCODING);
				Integer code = (Integer)headers.get(HttpUtils.Response_Code);
				String message = (String)headers.get(HttpUtils.Response_Message);
				jsons = new String(bytes,encoding);
				ArrayList<Object> lst2 = (ArrayList<Object>)jacksonManager.jsonDecodeObject(jsons);
				for (int i=0; i<lst2.size(); i++) {
					SimplePlayer4Migu ss = coverJson((Map)lst2.get(i),mpi.getServerRealName());
					lst.add(ss);
				} 
			} catch (Exception e) {
			}
                //{
                        //lst.add(mpi);
                //}
        }
	}
}
%>
<%
String jsons = "";
jsons = JsonUtil.jsonFromObject(lst.toArray());
out.println(jsons);
%>
<%!

public SimplePlayer4Migu coverJson(Map map,String servername) {
	SimplePlayer4Migu info = new SimplePlayer4Migu();
	info.setServername(servername);
	info.setPlayerId((Long)map.get("playerId"));
	info.setName((String)map.get("name"));
	info.setCareer((Integer)map.get("career"));
	info.setLevel((Integer)map.get("playerLevel"));
	info.setVipLevel((Integer)map.get("vipLevel"));
	info.setCountry((Integer)map.get("country"));
	return info;
}

public static String getSignParam(Map<String,String> params)
{
	String keys[] = params.keySet().toArray(new String[0]);
	java.util.Arrays.sort(keys);
	StringBuffer sb = new StringBuffer();
	for(int i = 0 ; i < keys.length ; i++){
		String v = params.get(keys[i]);
		if( StringUtils.isEmpty(v))continue;
		sb.append(keys[i]+"="+v+"&");
	}
	String md5Str = sb.substring(0,sb.length()-1);
	
	return md5Str;
}


%>

