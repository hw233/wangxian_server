<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.io.DataOutputStream"%>
<%@page import="com.xuanzhi.tools.text.JsonUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.enterlimit.EnterLimitManager"%>
<%@page import="com.fy.engineserver.enterlimit.EnterLimitManager.PlayerRecordData"%>
<%@page import="com.fy.engineserver.sprite.SpriteSubSystem"%>
<%@page import="com.fy.boss.authorize.model.Passport"%>
<%@page import="java.io.Serializable"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.enterlimit.AndroidMsgData"%>
<%@page import="com.fy.engineserver.enterlimit.AndroidMsgManager"%>
<%@ page contentType="text/html;charset=utf-8" import="java.util.*"%>
<%!
	public static class PlayerData implements Serializable{
		private static final long serialVersionUID = 311750396093343420L;
		
		public long pId;
		public String userName;
		public String pName = "";
		public String pChannel = "";
		public String loginChannel = "";
		public String ipAdd = "";
		public int level;
		public long rmb;
		public long silver;
		public String macAdd = "";
		public String ua = "";
		public String login_os = "";
		public int noReqNum = 0;
		public String wifiName = "";
		public boolean isOnline;
		
		public PlayerData(Player p, AndroidMsgData data) {
			pId = p.getId();
			pName = p.getName();
			userName = p.getPassport().getUserName();
			pChannel = p.getPassport().getRegisterChannel();
			loginChannel = p.getPassport().getLastLoginChannel();
			login_os = p.getPassport().getLastLoginMobileOs();
			ua = p.getPassport().getLastLoginMobileType();
			
			Integer num = EnterLimitManager.getInstance().NO_PROCESS_REQNUM.get(p.getId());
			if (num != null) {
				noReqNum = num.intValue();
			}
			
			ipAdd = p.getIPAddress();
			if (ipAdd == null) {
				ipAdd = "";
			}else {
				ipAdd = ipAdd.substring(0, ipAdd.indexOf(":"));
			}
			level = p.getSoulLevel();
			if (data.getMac() != null) {
				macAdd = data.getMac();
			}else {
				Long l = data.getSendTimes().get(AndroidMsgManager.GET_CLIENT_MAC);
				if (l != null && l > 0) {
					long now = System.currentTimeMillis();
					if (now - l > 30*1000) {
						macAdd = "未收到协议";
					}
				}
			}
//			PlayerRecordData recordData = EnterLimitManager.getPlayerRecordData(p.getId());
//			if (recordData != null && recordData.ua != null) {
//				ua = recordData.ua;
//			}else {
//				
//			}
			if (data.getWifiName() != null) {
				wifiName = data.getWifiName();
				if (data.getWifiName().equals("没")) {
					Long l = data.getSendTimes().get(AndroidMsgManager.GET_CLIENT_MAC);
					if (l != null && l > 0) {
						long now = System.currentTimeMillis();
						if (now - l > 30*1000) {
							wifiName = "未收到协议";
						}
					}
				}
			}else {
				Long l = data.getSendTimes().get(AndroidMsgManager.GET_CLIENT_MAC);
				if (l != null && l > 0) {
					long now = System.currentTimeMillis();
					if (now - l > 30*1000) {
						wifiName = "未收到协议";
					}
				}
			}
			isOnline = false;
			rmb = p.getRMB();
			silver = p.getSilver();
		}
	}

	public static class LimitedMsg implements Serializable {
		private static final long serialVersionUID = 211750106096743425L;
		
		public String userName;
		public String serverName;
		public long pId;
	}
%>

	<%
		String action = request.getParameter("action");
		if (action != null) {
			if (action.equals("AndroidMsg")) {
				int level_min = Integer.parseInt(request.getParameter("levelMin"));
				int level_max = Integer.parseInt(request.getParameter("levelMax"));
				boolean isOnLine = Boolean.parseBoolean(request.getParameter("online"));
				long rmb = Long.parseLong(request.getParameter("rmb"));
				ArrayList<PlayerData> pdatas = new ArrayList<PlayerData>();
				for (Long id : AndroidMsgManager.msgDatas.keySet()) {
					AndroidMsgData data = AndroidMsgManager.msgDatas.get(id);
					Player p = null;
					if (isOnLine) {
						if (PlayerManager.getInstance().isOnline(data.getpID())) {
							p = PlayerManager.getInstance().getPlayer(data.getpID());
						}
					}else {
						p = PlayerManager.getInstance().getPlayer(data.getpID());
					}
					if (p != null) {
						if (p.getSoulLevel() < level_min || p.getSoulLevel() > level_max) {
							continue;
						}
						if (p.getRMB() > rmb) {
							continue;
						}
						if (p.getPassport() == null) {
							Passport passport = SpriteSubSystem.getInstance().bossService.getPassportByUserName(p.getUsername());
							p.setPassport(passport);
						}
						if (EnterLimitManager.LIMIT && (EnterLimitManager.getInstance().inEnterLimit(p.getUsername(), null) || EnterLimitManager.getInstance().inEnterLimit(p.getPassport().getNickName(), null))) {
							continue;
						}
						PlayerData pd = new PlayerData(p, data);
						pd.isOnline = p.isOnline();
						pdatas.add(pd);
					}
				}
				PlayerData[] aa = pdatas.toArray(new PlayerData[0]);
				out.println(JsonUtil.jsonFromObject(aa));
//				System.out.println(JsonUtil.jsonFromObject(aa));
//				ByteArrayOutputStream bos = new ByteArrayOutputStream();
//				DataOutputStream dos = new DataOutputStream(bos);
//				dos.writeInt(aa.length);
//				for (int i = 0 ; i < aa.length; i++) {
//					dos.writeLong(aa[i].pId);
//					dos.writeUTF(aa[i].pName);
//					dos.writeUTF(aa[i].pChannel);
//					dos.writeUTF(aa[i].ipAdd);
//					dos.writeInt(aa[i].level);
//					dos.writeUTF(aa[i].macAdd);
//					dos.writeUTF(aa[i].ua);
//					dos.writeUTF(aa[i].wifiName);
//					dos.writeBoolean(aa[i].isOnline);
//				}
//				dos.close();
//				out.println(new String(bos.toByteArray()));
			}else if (action.equals("limited")) {
				String str = request.getParameter("uInfo");
				LimitedMsg[] msgs = JsonUtil.objectFromJson(str, LimitedMsg[].class);
				StringBuffer allUser = new StringBuffer("");
				StringBuffer fengUser = new StringBuffer("");
				String sName = GameConstants.getInstance().getServerName();
				for (LimitedMsg m : msgs) {
					if (m.serverName.equals(sName)) {
						fengUser.append("服务器:"+sName+"用户:["+m.userName+"]<br>");
						EnterLimitManager.getInstance().limited.add(m.userName);
						if (PlayerManager.getInstance().isOnline(m.pId)) {
							Player p = PlayerManager.getInstance().getPlayer(m.pId);
							p.getConn().close();
						}
						AndroidMsgManager.msgDatas.remove(m.pId);
					}
					allUser.append("用户:["+m.userName+"]");
				}
				out.println(fengUser.toString());
				EnterLimitManager.logger.warn("[被网关页面封号 相同IP下账号过多] 封号["+fengUser.toString()+"] 所有用户:["+allUser+"]");
			}else if(action.equals("removeLimited")) {
				String uName = request.getParameter("uName");
				if (EnterLimitManager.getInstance().inEnterLimit(uName, null)) {
					EnterLimitManager.getInstance().removeFromlimit(new String[]{uName});
					out.println("已经解除封号限制");
				}else {
					out.println("此用户没有被封");
				}
			}
		}
	%>