<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.message.GameMessageFactory"%>
<%@page import="com.fy.engineserver.gateway.MieshiGatewayClientService"%>
<%@page import="com.fy.engineserver.message.DENY_USER_REQ"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.enterlimit.EnterLimitManager.PlayerRecordData"%>
<%@ page contentType="text/html;charset=utf-8" import="java.util.*"%>
<%@ page import="com.fy.engineserver.enterlimit.*,com.fy.engineserver.dajing.*" %>
<%@page import="com.xuanzhi.tools.text.JsonUtil"%>
<%!
public static class IPUsernames{
	public String ip;
	public ArrayList<String> usernames = new ArrayList<String>();
}


public DajingStudio getDajingStudio(DajingStudioManager dm,String username) throws Exception{
	if(username == null || username.length() == 0) return null;
	
	java.lang.reflect.Field f = dm.getClass().getDeclaredField("player2DajingMap");
	f.setAccessible(true);
	java.util.concurrent.ConcurrentHashMap<String,DajingStudio> player2DajingMap = (java.util.concurrent.ConcurrentHashMap<String,DajingStudio>)f.get(dm);
	return player2DajingMap.get(username);
}

%>
<%
EnterLimitManager elm = EnterLimitManager.getInstance();
DajingStudioManager dsManager = DajingStudioManager.getInstance();
if(dsManager.initialized == false){
	dsManager.init();
}
boolean online = "true".equals(request.getParameter("online"));
String getEnterLimitMap = request.getParameter("enterLimit");
if(getEnterLimitMap != null && getEnterLimitMap.equals("true")){
	int maxSize = 0;
	int level = 0;
	String levelStr = request.getParameter("level");
	String maxsize = request.getParameter("maxSize");
	String saleEmptyNumStr = request.getParameter("saleEmptyNum");
	
	if(maxsize != null || levelStr != null){
		try{
			maxSize = Integer.parseInt(maxsize);
			level = Integer.valueOf(levelStr);
		}catch(Exception ex){
		}
	}

	//
	EnterLimitManager.EnterLimitData[] dataArray = elm.getLikeNeedLimitArray(1,level);

	for(EnterLimitManager.EnterLimitData d : dataArray){
		String ip = d.ip;
		Iterator<Long> itor = d.playerIds.iterator();
		while(itor.hasNext()){
			long playerId = itor.next();
			boolean removeFlag = false;
			PlayerRecordData prd = EnterLimitManager.getPlayerRecordData(playerId);
			if (prd == null) {
				continue;
			}
			prd.online = Boolean.FALSE;
			prd.online = GamePlayerManager.getInstance().isOnline(playerId);
			prd.obj9=ip;

			DajingStudio ds = getDajingStudio(dsManager,prd.username);
			if(ds != null){
				removeFlag = true;
			}
			if(!removeFlag && online){
				Boolean b = prd.online;
				if(b == false){
					removeFlag = true;
				}
			}
			
			if(!removeFlag){
				if(elm.inEnterLimit(prd.username,null)){
					removeFlag = true;
				}
			}
			if (!removeFlag && saleEmptyNumStr != null && !"".equals(saleEmptyNumStr.trim())) {
				int saleEmptyNum = Integer.valueOf(saleEmptyNumStr);
				if (prd.sealEmptyCellTimes < saleEmptyNum) {
					removeFlag = true;
				}
			}
			if(removeFlag){
				itor.remove();
			}
		}
	}
	//

	HashMap<String,EnterLimitManager.EnterLimitData> map = new HashMap<String,EnterLimitManager.EnterLimitData>();

	for(EnterLimitManager.EnterLimitData d : dataArray){
		String ip = d.ip;
		int ii = ip.lastIndexOf(".");
		ip = ip.substring(0,ii) + ".*";
		EnterLimitManager.EnterLimitData data = map.get(ip);
		if(data == null) {
			data = new EnterLimitManager.EnterLimitData();
			data.ip = ip;
			map.put(ip,data);
		}
		data.playerIds.addAll(d.playerIds);
	}
	
	ArrayList<EnterLimitManager.EnterLimitData> al = new ArrayList<EnterLimitManager.EnterLimitData>();
	Iterator<String> it = map.keySet().iterator();
	while(it.hasNext()){
		String ip = it.next();
		EnterLimitManager.EnterLimitData d = map.get(ip);
		for (Long playerId : d.playerIds) {
				PlayerRecordData prd = EnterLimitManager.getPlayerRecordData(playerId);
				if(d.recordDatas.contains(prd) == false)
					d.recordDatas.add(prd);	
		}
		if(d.recordDatas.size() >= maxSize)
			al.add(d);
	}
	dataArray = al.toArray(new EnterLimitManager.EnterLimitData[0]);
	out.println(JsonUtil.jsonFromObject(dataArray));
}

String limit = request.getParameter("limit");
if(limit != null && limit.equals("true")){
	String usernames = request.getParameter("usernames");
	
	IPUsernames[] checkUsername = JsonUtil.objectFromJson(usernames,IPUsernames[].class);
	StringBuffer sb = new StringBuffer();
	if(checkUsername != null){
		for(IPUsernames str : checkUsername){
			sb.append("[封号] [IP:"+str.ip+"] ["+str.usernames.toString()+"]</br>");
			elm.putTolimit(str.ip,str.usernames.toArray(new String[0]));
		}
		
	}
	//加入限制列表
	out.println(sb.toString());
	
	
}else if(limit != null && limit.equals("false")){
	String usernames = request.getParameter("usernames");
	IPUsernames[] checkUsername = JsonUtil.objectFromJson(usernames,IPUsernames[].class);
	StringBuffer sb = new StringBuffer();
	if(checkUsername != null){
		for(IPUsernames str : checkUsername){
			sb.append("[踢掉] [IP:"+str.ip+"] ["+str.usernames.toString()+"]</br>");
			//elm.putTolimit(str.ip,str.usernames.toArray(new String[0]));
		}
		
	}
	//加入限制列表
	out.println(sb.toString()+"踢掉");
}
%>
