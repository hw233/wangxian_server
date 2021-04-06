
<%@page import="com.fy.gamegateway.mieshi.server.DenyUserEntity"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.ClientEntity"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.ClientAuthorization"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.AuthorizeManager"%>
<%@page import="com.fy.boss.client.BossClientService"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.fy.boss.authorize.model.Passport"%><%@page import="java.net.URL"%>
<%@page import="com.xuanzhi.tools.servlet.HttpUtils"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfo"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiPlayerInfo"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiPlayerInfoManager"%>
<%@page import="com.xuanzhi.tools.text.JsonUtil"%>
<%@page import="com.fy.gamegateway.gmaction.GmActionManager"%>
<%@page import="com.fy.gamegateway.gmaction.GmAction"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfoManager"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%!
String[] careerNames = { "通用", "斗罗", "鬼煞", "灵尊", "巫皇" ,"兽魁"};
public String getCareerName(int career)
{
  return this.careerNames[career];
}

public String getServerPlatformUrl(String realName) {
  MieshiServerInfo server = MieshiServerInfoManager.getInstance().getServerInfoByName(realName);
  if (server != null) {
    return server.getServerUrl();
  }
  return "unknown";
}
%>
<%
String username = request.getParameter("userName");
MieshiServerInfoManager msm = MieshiServerInfoManager.getInstance();
Map result = new HashMap();
Passport passport = null;
if (username == null || username.isEmpty()) {
	out.print(JSONObject.fromObject(result).toString());
}
SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
try
{
	try{
	  passport = BossClientService.getInstance().getPassportByUserName(username);
	}catch(Exception e){
		out.print("pasport is null");		
	}
  if (passport != null) {
    Map passportMap = new HashMap();
    passportMap.put("userName", passport.getUserName());
    passportMap.put("registerDate", format.format(passport.getRegisterDate()));
    passportMap.put("lastLoginDate", format.format(passport.getLastLoginDate()));
    passportMap.put("secretQuestion", passport.getSecretQuestion());
    passportMap.put("secretAnswer", passport.getSecretAnswer());
    passportMap.put("registerMobileType", passport.getRegisterMobileType());
    passportMap.put("channel", passport.getRegisterChannel());
    result.put("passport", passportMap);
    DenyUserEntity du = msm.getDenyUser(username);
    Map duMap = new HashMap();
    if (du != null) {
      duMap.put("username", du.getUsername());
      if (du.isForoverDeny())
        duMap.put("foroverDeny", "永久封号");
      else {
        duMap.put("foroverDeny", "临时封号");
      }
      if (du.isDenyDeviceId())
        duMap.put("denyDeviceId", du.getDeviceId());
      else {
        duMap.put("denyDeviceId", "不限制设备");
      }
      if (du.isDenyClientId())
        duMap.put("denyClientId", du.getClientId());
      else {
        duMap.put("denyClientId", "不限制客户端ID");
      }
      duMap.put("denyStartTime", format.format(new Date(du.getDenyStartTime())));
      duMap.put("denyEndTime", format.format(new Date(du.getDenyEndTime())));
      duMap.put("gm", du.getGm());
      duMap.put("reason", du.getReason());
      duMap.put("innerReason", du.getInnerReason());
    }
    result.put("du", duMap);
  }

  List<ClientAuthorization> cas = AuthorizeManager.getInstance().getUsernameClientAuthorization(username);
  List casList = new ArrayList();
  if (cas != null) {
    String[] aa = { "等待", "正常", "被替代", "异常", "拒绝", "等待过期" };
    String[] bb = { "自动授权", "手动授权或拒绝" };
    for (ClientAuthorization ca : cas) {
    	 Map caMap = new HashMap();
      caMap.put("clientID", ca.getClientID());
      caMap.put("username", ca.getUsername());
      caMap.put("authorizeState", aa[ca.getAuthorizeState()]);
      caMap.put("authorizeType", bb[ca.getAuthorizeType()]);
      caMap.put("authorizeTime", format.format(new Date(ca.getAuthorizeTime())));
      caMap.put("lastLoginTime", format.format(new Date(ca.getLastLoginTime())));
      caMap.put("loginNum", Integer.valueOf(ca.getLoginNum()));
      caMap.put("queryNum", Integer.valueOf(ca.getQueryNum()));
      caMap.put("ip", ca.getIp());
      caMap.put("authorizeClientId", ca.getAuthorizeClientId());
      ClientEntity  ce = AuthorizeManager.getInstance().getClientEntity(ca.getClientID());

      caMap.put("mac", ce.getMac());
      caMap.put("platform", ce.getPlatform());
      caMap.put("phoneType", ce.getPhoneType());
      caMap.put("gpuType", ce.getGpuType());
      caMap.put("createTime", Long.valueOf(ce.getCreateTime()));
      casList.add(caMap);
    }
  }
  result.put("cas", casList);
  MieshiPlayerInfoManager mpm = MieshiPlayerInfoManager.getInstance();
  MieshiPlayerInfo[] mps = mpm.getMieshiPlayerInfoByUsername(username);

  HashMap<String,List<MieshiPlayerInfo>> serverPlayerMap = new HashMap<String,List<MieshiPlayerInfo>>();
	for(MieshiPlayerInfo mp : mps) {
		List<MieshiPlayerInfo> list = serverPlayerMap.get(mp.getServerRealName());
		if(list == null) {
			list = new ArrayList<MieshiPlayerInfo>();
			serverPlayerMap.put(mp.getServerRealName(), list);
		}
		list.add(mp);
	}
	String servers[] = serverPlayerMap.keySet().toArray(new String[0]);
	List<Map<String, Object>> serversList = new ArrayList<Map<String, Object>>();
	for(String server : servers) {
		List<MieshiPlayerInfo> list = serverPlayerMap.get(server);
		Map<String, Object> subMap = new HashMap<String, Object>();
		subMap.put("serverName", server);
		List<Map<String, Object>> list_temp = new ArrayList<Map<String, Object>>();
		for(MieshiPlayerInfo info : list){
			Map<String, Object> infoMap = new HashMap<String, Object>();
			infoMap.put("serverPlatformUrl", getServerPlatformUrl(info.getServerRealName()));
			infoMap.put("careerName", getCareerName(info.getCareer()));
			infoMap.put("playerId", info.getPlayerId());
			infoMap.put("playerName", info.getPlayerName());
			infoMap.put("level", info.getLevel());
			infoMap.put("playerRMB", info.getPlayerRMB());
			infoMap.put("playerVIP", info.getPlayerVIP());
			infoMap.put("lastAccessTime", format.format(new Date(info.lastAccessTime)));
			list_temp.add(infoMap);
		}
		subMap.put("list", list_temp);
		serversList.add(subMap);
	}
	
	result.put("servers", serversList);
} catch (Exception e) {
	//out.print("获取账号信息失败"+ e);
}
out.print(JSONObject.fromObject(result).toString());
%>

