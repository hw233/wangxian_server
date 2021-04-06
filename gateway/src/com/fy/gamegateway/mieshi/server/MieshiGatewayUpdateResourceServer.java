package com.fy.gamegateway.mieshi.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.fy.boss.authorize.model.Passport;
import com.fy.gamegateway.gmaction.GmActionManager;
import com.fy.gamegateway.message.DENY_USER_REQ;
import com.fy.gamegateway.message.DENY_USER_RES;
import com.fy.gamegateway.message.GET_WAIGUA_PROCESS_NAMES_REQ;
import com.fy.gamegateway.message.GET_WAIGUA_PROCESS_NAMES_RES;
import com.fy.gamegateway.message.GM_ACTION_REQ;
import com.fy.gamegateway.message.GameMessageFactory;
import com.fy.gamegateway.message.MIESHI_SKILL_INFO_REQ;
import com.fy.gamegateway.message.MIESHI_UPDATE_PLAYER_INFO;
import com.fy.gamegateway.message.MIESHI_UPDATE_SERVER_INFO;
import com.fy.gamegateway.message.MODIFY_VIP_INFO_REQ;
import com.fy.gamegateway.message.MODIFY_VIP_INFO_RES;
import com.fy.gamegateway.message.NEW_MIESHI_UPDATE_PLAYER_INFO;
import com.fy.gamegateway.message.NOTIFY_USER_ENTERSERVER_REQ;
import com.fy.gamegateway.message.NOTIFY_USER_LEAVESERVER_REQ;
import com.fy.gamegateway.message.QUERY_CLIENT_INFO_REQ;
import com.fy.gamegateway.message.QUERY_CLIENT_INFO_RES;
import com.fy.gamegateway.message.QUERY_WHITE_LIST_REQ;
import com.fy.gamegateway.message.QUERY_WHITE_LIST_RES;
import com.fy.gamegateway.message.REPORT_LONG_HEARTBEAT_REQ;
import com.fy.gamegateway.message.REPORT_LONG_PROTOCAL_REQ;
import com.fy.gamegateway.message.REPORT_ONLINENUM_REQ;
import com.fy.gamegateway.message.REPORT_ONLINENUM_RES;
import com.fy.gamegateway.message.SESSION_VALIDATE_REQ;
import com.fy.gamegateway.message.SESSION_VALIDATE_RES;
import com.fy.gamegateway.message.VALIDATE_DEVICE_INFO_REQ;
import com.fy.gamegateway.message.VALIDATE_DEVICE_INFO_RES;
import com.fy.gamegateway.mieshi.resource.manager.ResourceManager;
import com.fy.gamegateway.mieshi.server.MieshiServerInfoManager.TestUser;
import com.fy.gamegateway.mieshi.waigua.ClientWaiGuaProcessManager;
import com.fy.gamegateway.mieshi.waigua.SessionManager;
import com.fy.gamegateway.mieshi.waigua.UserGameServerManager;
import com.fy.gamegateway.stat.Client;
import com.fy.gamegateway.stat.StatManager;
import com.fy.gamegateway.thirdpartner.migu.MiguWorker;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionConnectedHandler;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.ConnectionSelector;
import com.xuanzhi.tools.transport.DefaultConnectionSelector;
import com.xuanzhi.tools.transport.MessageHandler;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

/**
 * 此类处理 服务器向网关发送的协议
 * 
 * 
 *
 */
public class MieshiGatewayUpdateResourceServer implements ConnectionConnectedHandler,
	MessageHandler {

	public String getSkillFile() {
		return skillFile;
	}

	public void setSkillFile(String skillFile) {
		this.skillFile = skillFile;
	}

	static Logger logger = Logger.getLogger(MieshiGatewaySubSystem.class);
	
	private static MieshiGatewayUpdateResourceServer mself;
	
	public static boolean closeConnect = false;

	public static MieshiGatewayUpdateResourceServer getInstance() {
		return mself;
	}

	protected String skillFile = "";
	
	protected String skillHttpPath = "";
	
	protected DefaultConnectionSelector selector;

	public void init() throws Exception {
		mself = this;
		long now = System.currentTimeMillis();
		System.out.println(this.getClass().getName() + " initialize successfully ["+(System.currentTimeMillis()-now)+"]");
		logger.info(this.getClass().getName() + " initialize successfully ["+(System.currentTimeMillis()-now)+"]");
	}
	
	public void setConnectionSelector(ConnectionSelector selector) {
		this.selector = (DefaultConnectionSelector) selector;
		this.selector.setConnectionConnectedHandler(this);
		this.selector.setConnectionReceiveBufferSize(1024*512);
		System.out.println("this.selector.getConnectionReceiveBufferSize"+this.selector.getConnectionReceiveBufferSize());
	}

	public ConnectionSelector getConnectionSelector(){
		return selector;
	}
	public void discardRequestMessage(Connection conn, RequestMessage req)
			throws ConnectionException {
	}

	public ResponseMessage receiveRequestMessage(Connection conn,
			RequestMessage request) throws ConnectionException {
		//logger.warn("receiveMess: "+ request.getTypeDescription());
		if(request instanceof MIESHI_SKILL_INFO_REQ){
			MIESHI_SKILL_INFO_REQ req = (MIESHI_SKILL_INFO_REQ)request;
			byte[] bs = req.getData();
			logger.info("[更新技能数据] [] ["+skillFile+"] [size:"+bs.length+"]");
			try {
				FileOutputStream fos = null;
				fos = new FileOutputStream(new File(skillFile));
				fos.write(bs);
				fos.flush();
				fos.close();
				
		         ResourceManager rm = ResourceManager.getInstance();
		         rm.createTestResourceFileList(new ArrayList<String>());
		         
				if(logger.isInfoEnabled()){
					logger.info("[更新技能数据] [成功] ["+skillFile+"] [size:"+bs.length+"]");
				}
				
			} catch (Exception e) {
				if(logger.isInfoEnabled()){
					logger.info("[更新技能数据] [失败] ["+skillFile+"] [size:"+bs.length+"]",e);
				}
			}
		}else if(request instanceof MIESHI_UPDATE_PLAYER_INFO){
			MIESHI_UPDATE_PLAYER_INFO req = (MIESHI_UPDATE_PLAYER_INFO)request;
			
			MieshiPlayerInfoManager pm = MieshiPlayerInfoManager.getInstance();
			pm.updateMieshiPlayerInfo(req.getUserName(), req.getServerName(), req.getPlayerLevel(), req.getPlayerCareer(), req.getPlayerName(), req.getAction());
			
		}else if (request instanceof NEW_MIESHI_UPDATE_PLAYER_INFO) {
			NEW_MIESHI_UPDATE_PLAYER_INFO req = (NEW_MIESHI_UPDATE_PLAYER_INFO)request;
			MieshiPlayerInfoManager pm = MieshiPlayerInfoManager.getInstance();
			pm.updateMieshiPlayerInfo(req.getUserName(), req.getPlayerId(), req.getServerName(), req.getPlayerLevel(), req.getPlayerCareer(), req.getPlayerName(), req.getPlayerRMB(), req.getPlayerVip(),req.getAction());
		}else if(request instanceof MIESHI_UPDATE_SERVER_INFO){
			MIESHI_UPDATE_SERVER_INFO req = (MIESHI_UPDATE_SERVER_INFO)request;
			
			MieshiServerInfoManager sm = MieshiServerInfoManager.getInstance();
			MieshiServerInfo si[] = sm.getServerInfoList();
			for(int i = 0 ; i < si.length ; i++){
				MieshiServerInfo s = si[i];
				if(s.getRealname().equals(req.getServerName())){
					s.setOnlinePlayerNum(req.getPlayerOnlineNum());
					//
				}
			}
		}else if(request instanceof DENY_USER_REQ){
			DENY_USER_REQ req = (DENY_USER_REQ)request;
			MieshiServerInfoManager mim = MieshiServerInfoManager.getInstance();
			mim.addDenyUser("",req.getClientId(), false,req.getDenyClientId(), req.getUsername(), req.getReason(), 
					req.getGm(), req.getForoverDeny(), req.getDays(), req.getHours());
			
			return new DENY_USER_RES(request.getSequnceNum(),"ok");
		}else if(request instanceof REPORT_ONLINENUM_REQ){
			REPORT_ONLINENUM_REQ req = (REPORT_ONLINENUM_REQ)request;
			MieshiServerInfoManager mim = MieshiServerInfoManager.getInstance();
			MieshiServerInfo si = mim.getServerInfoByName(req.getServerName());
			if(si != null){
				si.notifyOnlineNum(req.getOnlineNum());
			}
			conn.setIdentity(req.getServerName());
			return new REPORT_ONLINENUM_RES(request.getSequnceNum());
		}else if(request instanceof REPORT_LONG_HEARTBEAT_REQ){
			REPORT_LONG_HEARTBEAT_REQ req = (REPORT_LONG_HEARTBEAT_REQ)request;
			MieshiServerHeartBeatInfoManager mshbim = MieshiServerHeartBeatInfoManager.getInstance();
			if(mshbim != null){
				mshbim.notifyHeartbeatInfo(req.getServerName(), req.getGame(), req.getHeartbeatTime());
				logger.debug("[REPORT_LONG_HEARTBEAT_REQ] [req.getServerName"+req.getServerName()+"]");
			}
		}else if(request instanceof REPORT_LONG_PROTOCAL_REQ){
			REPORT_LONG_PROTOCAL_REQ req = (REPORT_LONG_PROTOCAL_REQ)request;
			GateWayHandleOverTimeInfoManager mshbim = GateWayHandleOverTimeInfoManager.getInstance();
			if(mshbim != null){
				try{
					mshbim.notifyGateWayHandleOvertInfo(req.getServerName(), req.getPName(), req.getCostTime());
					logger.warn("[REPORT_LONG_PROTOCAL_REQ] [req.getServerName"+req.getServerName()+"] [超时："+req.getCostTime()+"]");
				}catch(Exception e){
					e.printStackTrace();
					logger.warn("[REPORT_LONG_PROTOCAL_REQ] [异常] [req.getServerName"+req.getServerName()+"] [超时："+req.getCostTime()+"]",e);
				}
			}
		}else if(request instanceof QUERY_CLIENT_INFO_REQ){
			QUERY_CLIENT_INFO_REQ req = (QUERY_CLIENT_INFO_REQ)request;
			StatManager sm = StatManager.getInstance();
			String username = req.getUsername();
			Client client = null;
			String pClientId = null;
			try{
//				//List<ClientAccount> list = sm.em4Account.query(ClientAccount.class,"username='"+username+"'","loginTimeForLast desc",1,2);
//				long ids[] = sm.em4Account.queryIds(ClientAccount.class,"username='"+username+"'","loginTimeForLast desc",1,2);
//				
//				if(ids != null && ids.length > 0 ){
//				        ClientAccount ca = sm.em4Account.find(ids[0]);
//				        pClientId = ca.getClientId();
//				        //List<Client> al = sm.em4Client.query(Client.class,"clientId='"+ca.getClientId()+"'","",1,2);
//				        ids = sm.em4Client.queryIds(Client.class,"clientId='"+ca.getClientId()+"'","",1,2);
//				        if(ids != null && ids.length  > 0){
//				                client = sm.em4Client.find(ids[0]);
//				        }
//				}
			}catch(Exception e){
				System.err.println("[查询Client] [出错] [clientId:"+pClientId+"]");
				e.printStackTrace();
			}
			String clientId = "";
			String channel= "";
			String uuid= "";
			String deviceId= "";
			String platform= "";
			String network= "";
			String phoneType= "";
			String programVersion= "";
			if(client != null){
				clientId = client.getClientId();
				channel = client.getChannel();
				platform = client.getPlatform();
				network = client.getNetworkTypeOfLastConnected();
				phoneType =client.getPhoneType();
				programVersion = client.getClientProgramVersionOfLastConnected();
				uuid = client.getUuid()==null?"":client.getUuid();
				deviceId = client.getDeviceId()==null?"":client.getDeviceId();
			}
			QUERY_CLIENT_INFO_RES res = new QUERY_CLIENT_INFO_RES(req.getSequnceNum(),
						req.getServerName(),req.getUsername(),clientId,channel,uuid,deviceId,platform,network,phoneType,programVersion);
					return res;
		}else if (request instanceof SESSION_VALIDATE_REQ) {
			SESSION_VALIDATE_REQ req = (SESSION_VALIDATE_REQ)request;
			String serverName = req.getServername();
			String userName = req.getUsername();
			//快用渠道用nickname作为session的key
			String sessionUserName = userName;
			int resultType = 0;
			if(req.getChannel() != null && req.getChannel().contains("KUAIYONGSDK")){
				try {
					Passport passport = MieshiGatewaySubSystem.getInstance().getBossClientService().getPassportByUserName(userName);
					if(passport != null && passport.getNickName() != null && !passport.getNickName().isEmpty()){
						sessionUserName = passport.getNickName();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			int result = SessionManager.getInstance().validateSession(req.getSessionId(), sessionUserName, req.getClientId());
			if(result != 0){
				if(req.getChannel() != null && req.getChannel().contains("KUAIYONGSDK")){
					try {
						resultType = 1;
						result = SessionManager.getInstance().validateSession(req.getSessionId(), userName, req.getClientId());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			String msg = "";
			String I = "";
			DenyUserEntity du = MieshiServerInfoManager.getInstance().getDenyUser(userName);
			if (du != null) {
				if (du.enableDeny && (du.foroverDeny || (System.currentTimeMillis() > du.denyStartTime && System.currentTimeMillis() < du.denyEndTime))) {
					result = 4;
					msg = "此用户已经被封。";
				}
			}
			if (result == 1) {
				msg = "session不存在";
			}else if (result == 2) {
				msg = "session用户名不匹配";
			}else if (result == 3) {
				msg = "session的ClientId不匹配";
			} else if (result == 4) {
			}else {
				MieshiServerInfoManager sm = MieshiServerInfoManager.getInstance();
				MieshiServerInfo sInfo = sm.getServerInfoByName(serverName);
				boolean canJinRu = false;
				if (SessionManager.isSessionServerWeiHu) {
					if (sInfo != null) {
//						if (sInfo.getServerType() == MieshiServerInfo.SERVERTYPE_苹果正式服务器 
//								|| sInfo.getServerType() == MieshiServerInfo.SERVERTYPE_苹果国际服 
//								|| sInfo.getServerType() == MieshiServerInfo.SERVERTYPE_苹果台湾服) {
//							I = "IIOS,";
//						}
						if (sInfo.getStatus() == MieshiServerInfo.STATUS_OFF || 
							sInfo.getStatus() == MieshiServerInfo.STATUS_WEIHU || 
							sInfo.getStatus() == MieshiServerInfo.INNER_TEST ) {
							if (sm.isTestUser(userName)) {
								canJinRu = true;
								logger.warn("[Session验证] [userName:"+userName+"] [serverName:"+serverName+"] [result:"+result+"] [stat:"+sInfo.getStatus()+"]");
							}
						}else {
							canJinRu = true;
							logger.warn("[Session验证] [userName:"+userName+"] [serverName:"+serverName+"] [result:"+result+"] [stat:"+sInfo.getStatus()+"]");
						}
					}else {
						canJinRu = true;
						logger.warn("[Session验证] [userName:"+userName+"] [serverName:"+serverName+"] [result:"+result+"]");
						if(closeConnect){
							result = 5;
							msg = "session验证失败";
						}
					}
				}else {
					canJinRu = true;
					logger.warn("[Session验证] [userName:"+userName+"] [serverName:"+serverName+"] [result:"+result+"] [stat:"+(sInfo!=null?sInfo.getStatus():"null")+"]");
				}
				if (canJinRu) {
					PassportExtend p = MieshiGatewaySubSystem.getInstance().getShenfenzhengCache(req.getUsername());
					if (p != null) {
						msg = p.shenfenzheng;
					}
				}else {
					result = 1;
					msg = "服务器正在维护";
				}
			}
			
			
			
			StatManager statManager = StatManager.getInstance();
			try
			{
				if(statManager != null)
				{
					statManager.notifyEnterServer(req.getClientId());
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			SESSION_VALIDATE_RES res = new SESSION_VALIDATE_RES(req.getSequnceNum(), req.getUsername(), req.getSessionId(), req.getClientId(), result, I+msg);
			logger.warn("[验证sessionId] ["+serverName+"] [sessionUserName:"+sessionUserName+"] [resultType:"+resultType+"] ["+req.getSessionId()+"] ["+userName+"] ["+req.getClientId()+"] ["+req.getChannel()+"] ["+req.getPlatform()+"] ["+req.getPhoneType()+"] ["+req.getGpuOtherName()+"] ["+req.getNetwork()+"] ["+req.getMACADDRESS()+"] [结果="+result+"] ["+msg+"]");
			return res;
		}else if (request instanceof NOTIFY_USER_ENTERSERVER_REQ) {
			NOTIFY_USER_ENTERSERVER_REQ req = (NOTIFY_USER_ENTERSERVER_REQ)request;
			UserGameServerManager.instance.handle_NOTIFY_USER_ENTERSERVER_REQ(conn, req);
			return null;
		}else if (request instanceof NOTIFY_USER_LEAVESERVER_REQ) {
			NOTIFY_USER_LEAVESERVER_REQ req = (NOTIFY_USER_LEAVESERVER_REQ)request;
			UserGameServerManager.instance.handle_NOTIFY_USER_LEAVESERVER_REQ(conn, req);
			return null;
		}else if (request instanceof GM_ACTION_REQ) {
			GM_ACTION_REQ req = (GM_ACTION_REQ)request;
			GmActionManager.getInstance().handle_GM_ACTION_REQ(req.getGmAction());
		}else if (request instanceof GET_WAIGUA_PROCESS_NAMES_REQ) {
			return new GET_WAIGUA_PROCESS_NAMES_RES(request.getSequnceNum(), ClientWaiGuaProcessManager.waiGuaProcessNames);
		}else if(request instanceof MODIFY_VIP_INFO_REQ){
			MODIFY_VIP_INFO_REQ req = (MODIFY_VIP_INFO_REQ)request;
			MODIFY_VIP_INFO_RES res = new MODIFY_VIP_INFO_RES(request.getSequnceNum(), true);
			return res;
		}
		else if(request instanceof QUERY_WHITE_LIST_REQ)
		{
			if(logger.isDebugEnabled())
				logger.debug("[收到协议] [----]");
			
			QUERY_WHITE_LIST_REQ req = (QUERY_WHITE_LIST_REQ)request;
			String[] infos = new String[0];
			try
			{
				
				MieshiServerInfoManager mm = MieshiServerInfoManager.getInstance();
				List<TestUser> lst =  mm.getTestUsers();
				infos = new String[lst.size()];
				
				
				int i = 0;
				int sumByte = 0;
				for(TestUser testUser : lst)
				{
					StringBuffer sb = new StringBuffer();
					sb.append(testUser.username);
					sb.append(",");
					sb.append((testUser.enable == true ? "有效":"失效"));
					sb.append(",");
					sb.append((testUser.reason == null ? "" : testUser.reason));
					sb.append(",");
					sb.append((testUser.addUser == null ? "" : testUser.addUser));
					sb.append(",");
					sb.append((testUser.bumen == null ? "" : testUser.bumen));
					sb.append(",");
					sb.append((testUser.createTime == null ? "" : DateUtil.formatDate(testUser.createTime, "yyyyMMdd")));
					sb.append(",");
					sb.append((testUser.lastModifiedTime == null ? "" : DateUtil.formatDate(testUser.lastModifiedTime, "yyyyMMdd")));
					infos[i] = sb.toString();
					sumByte += (infos[i].getBytes("utf-8")).length;
					i++;
				
				}
				if(logger.isDebugEnabled())
					logger.debug("[收到协议] [成功] ["+conn.getIdentity()+"] ["+conn.getName()+"] ["+infos.length+"] ["+sumByte+"]");
			}
			catch(Exception e)
			{
				logger.error("[查询白名单] [失败] ["+infos.length+"]",e);	
				return new QUERY_WHITE_LIST_RES(req.getSequnceNum(),new String[0]);
			}
			finally
			{
				return new QUERY_WHITE_LIST_RES(req.getSequnceNum(),infos);
			}
			
		}else if(request instanceof MODIFY_VIP_INFO_REQ){
			MODIFY_VIP_INFO_REQ req = (MODIFY_VIP_INFO_REQ)request;
//			boolean result = AuthorizeManager.getInstance().modifyVipInfo(req.getUserName(), req.getCilentId());
			return new MODIFY_VIP_INFO_RES(req.getSequnceNum(), true);
		}
		else if(request instanceof VALIDATE_DEVICE_INFO_REQ)
		{
			VALIDATE_DEVICE_INFO_REQ req = (VALIDATE_DEVICE_INFO_REQ)request;
			String op = req.getInfos()[0];
			if("QUERY_DENYUSER".equalsIgnoreCase(op))
			{
				//查询是否是被封禁用户
				String username = req.getInfos()[1];
				
				MieshiServerInfoManager mieshiServerInfoManager = MieshiServerInfoManager.getInstance();
				DenyUserEntity denyUser =  mieshiServerInfoManager.getDenyUser(username);
				boolean isDenyUser = false;
				
				if(denyUser != null && denyUser.enableDeny)
				{
					if(denyUser.foroverDeny || denyUser.denyEndTime >= System.currentTimeMillis())
						isDenyUser = true;
				}
				String[] resInfo = new String[]{isDenyUser+""};
				VALIDATE_DEVICE_INFO_RES res = new VALIDATE_DEVICE_INFO_RES(req.getSequnceNum(), resInfo);
				if(MiguWorker.logger.isInfoEnabled())
					MiguWorker.logger.info("[米谷通讯] [验证合法设备] [成功] [是否是封禁用户:"+isDenyUser+"] ["+username+"]");
				
				return res;
			} else if ("VALIDATE_DEVICE_FOR_SALE_ROLE".equals(op)) {		//出售角色验证设备
				String isvalid = null;
				try {
					String username = req.getInfos()[1];
					String[] deviceInfo = new String[]{req.getInfos()[2]};
					MieshiServerInfoManager mieshiServerInfoManager = MieshiServerInfoManager.getInstance();
					DenyUserEntity denyUser =  mieshiServerInfoManager.getDenyUser(username);
					boolean isDenyUser = false;
					
					if(denyUser != null && denyUser.enableDeny)
					{
						if(denyUser.foroverDeny || denyUser.denyEndTime >= System.currentTimeMillis())
							isDenyUser = true;
					}
//					if (!isDenyUser) {
//						isvalid = MiguWorker.checkClient4SaleRole(deviceInfo, username);
//					}
				} catch (Exception e) {
					MiguWorker.logger.warn("[米谷通讯] [验证合法设备] [角色交易] [异常]", e);
				}
				if (isvalid == null) {
					isvalid = "true";
				}
				String[] resInfo = new String[]{isvalid+""};
				
				VALIDATE_DEVICE_INFO_RES res = new VALIDATE_DEVICE_INFO_RES(req.getSequnceNum(), resInfo);
				if (MiguWorker.logger.isInfoEnabled()) {
					MiguWorker.logger.info("[米谷通讯] [验证合法设备] [角色交易] [成功] [" + Arrays.toString(resInfo) + "] [" + req.getInfos()[1] + "]");
				}
				return res;
			} else if ("VALIDATE_DEVICE_FOR_CHANGENAME".equals(op)) {
				try {
					String username = req.getInfos()[1];
					String[] deviceInfo = new String[]{req.getInfos()[2]};
					boolean isValid = MiguWorker.checkClient4Changename(deviceInfo, username);
					String[] resInfo = new String[]{isValid+""};
					VALIDATE_DEVICE_INFO_RES res = new VALIDATE_DEVICE_INFO_RES(req.getSequnceNum(), resInfo);
					return res;
				} catch (Exception e) {
					MiguWorker.logger.warn("[验证合法设备] [异常] [" + Arrays.toString(req.getInfos()) + "]", e);
				}
			}
			else
			{
				String username = req.getInfos()[0];
				
				String[] deviceInfo = new String[req.getInfos().length-1];
				
				System.arraycopy(req.getInfos(), 1, deviceInfo, 0, req.getInfos().length-1);
				
				boolean isvalid = false;
				
				try
				{
					isvalid = MiguWorker.checkClient(deviceInfo, username);
				}
				catch(Exception e)
				{
					MiguWorker.logger.error("[米谷通讯] [验证合法设备] [失败] [出现未知异常] ["+isvalid+"]",e);
				}
				
				String[] resInfo = new String[]{isvalid+""};
				
				VALIDATE_DEVICE_INFO_RES res = new VALIDATE_DEVICE_INFO_RES(req.getSequnceNum(), resInfo);
				return res;
			}
		}
		
		return null;
	}

	public void receiveResponseMessage(Connection conn, RequestMessage req,
			ResponseMessage res) throws ConnectionException {
		
	}
	

	public RequestMessage waitingTimeout(Connection arg0, long arg1)
			throws ConnectionException {
		//return new ACTIVE_TEST_REQ(GameMessageFactory.nextSequnceNum());
		return null;
	}

	public void connected(Connection conn) throws IOException {
		conn.setMessageFactory(GameMessageFactory.getInstance());
		conn.setMessageHandler(this);
	}

	public void ternimate(Connection conn,
			List<RequestMessage> noResponseMessages, ByteBuffer receiveBuffer) {
	}

	public String getSkillHttpPath() {
		return skillHttpPath;
	}

	public void setSkillHttpPath(String skillHttpPath) {
		this.skillHttpPath = skillHttpPath;
	}
	
}
