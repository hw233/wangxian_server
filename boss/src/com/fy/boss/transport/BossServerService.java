package com.fy.boss.transport;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fy.boss.authorize.exception.PasswordWrongException;
import com.fy.boss.authorize.exception.UsernameAlreadyExistsException;
import com.fy.boss.authorize.exception.UsernameNotExistsException;
import com.fy.boss.authorize.model.Passport;
import com.fy.boss.authorize.service.PassportManager;
import com.fy.boss.finance.dao.UcidToUserNameDAO;
import com.fy.boss.finance.dao.impl.UcidToUserNameDAOImpl;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.model.UcidToUserName;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.finance.service.platform.AlipaySavingManager;
import com.fy.boss.game.model.Server;
import com.fy.boss.game.service.ServerManager;
import com.fy.boss.gm.XmlServer;
import com.fy.boss.gm.XmlServerManager;
import com.fy.boss.message.ALIPAY_ARGS_REQ;
import com.fy.boss.message.ALIPAY_ARGS_RES;
import com.fy.boss.message.ALIPAY_GET_ORDERID_NEW_REQ;
import com.fy.boss.message.ALIPAY_GET_ORDERID_NEW_RES;
import com.fy.boss.message.ALIPAY_GET_ORDERID_REQ;
import com.fy.boss.message.ALIPAY_GET_ORDERID_RES;
import com.fy.boss.message.APPCHINA_SAVING_NEW_REQ;
import com.fy.boss.message.APPCHINA_SAVING_NEW_RES;
import com.fy.boss.message.APPCHINA_SAVING_REQ;
import com.fy.boss.message.APPCHINA_SAVING_RES;
import com.fy.boss.message.APPSTORE_RECEIPT_NEW_REQ;
import com.fy.boss.message.APPSTORE_RECEIPT_NEW_RES;
import com.fy.boss.message.APPSTORE_RECEIPT_REQ;
import com.fy.boss.message.APPSTORE_RECEIPT_RES;
import com.fy.boss.message.APPSTORE_SAVING_VERIFY_REQ;
import com.fy.boss.message.APPSTORE_SAVING_VERIFY_RES;
import com.fy.boss.message.APPSTORE_UPDATE_IDENTY_REQ;
import com.fy.boss.message.APPSTORE_UPDATE_IDENTY_RES;
import com.fy.boss.message.BossMessageFactory;
import com.fy.boss.message.CAN_SEND_UC_VID_MAIL_REQ;
import com.fy.boss.message.CAN_SEND_UC_VID_MAIL_RES;
import com.fy.boss.message.CHANNEL_UER_SAVING_NEW_REQ;
import com.fy.boss.message.CHANNEL_UER_SAVING_NEW_RES;
import com.fy.boss.message.CHANNEL_UER_SAVING_REQ;
import com.fy.boss.message.CHANNEL_UER_SAVING_RES;
import com.fy.boss.message.CREATE_NEW_VIPINFORECORD_REQ;
import com.fy.boss.message.CREATE_NEW_VIPINFORECORD_RES;
import com.fy.boss.message.CREATE_UC_VID_MAIL_SEND_RECORD_REQ;
import com.fy.boss.message.CREATE_UC_VID_MAIL_SEND_RECORD_RES;
import com.fy.boss.message.GET_MOZUANINFO_REQ;
import com.fy.boss.message.GET_MOZUANINFO_RES;
import com.fy.boss.message.GET_SERVER_URL_REQ;
import com.fy.boss.message.GET_SERVER_URL_RES;
import com.fy.boss.message.GET_UCIDBYUSERNAME_REQ;
import com.fy.boss.message.GET_UCIDBYUSERNAME_RES;
import com.fy.boss.message.GET_UCVIP_INFO_REQ;
import com.fy.boss.message.GET_UCVIP_INFO_RES;
import com.fy.boss.message.GET_USERNAMEBYUCID_REQ;
import com.fy.boss.message.GET_USERNAMEBYUCID_RES;
import com.fy.boss.message.GET_USER_LAST_SAVING_TIME_REQ;
import com.fy.boss.message.JUDGE_POP_WINDOW_FOR_VIP_REQ;
import com.fy.boss.message.JUDGE_POP_WINDOW_FOR_VIP_RES;
import com.fy.boss.message.MIGU_COMMUNICATE_REQ;
import com.fy.boss.message.MIGU_COMMUNICATE_RES;
import com.fy.boss.message.MORE_ARGS_ORDER_STATUS_CHANGE_REQ;
import com.fy.boss.message.MORE_ARGS_ORDER_STATUS_CHANGE_RES;
import com.fy.boss.message.NINEONE_SAVING_NEW_REQ;
import com.fy.boss.message.NINEONE_SAVING_NEW_RES;
import com.fy.boss.message.NINEONE_SAVING_REQ;
import com.fy.boss.message.NINEONE_SAVING_RES;
import com.fy.boss.message.ORDER_STATUS_CHANGE_REQ;
import com.fy.boss.message.ORDER_STATUS_CHANGE_RES;
import com.fy.boss.message.ORDER_STATUS_NOTIFY_REQ;
import com.fy.boss.message.ORDER_STATUS_NOTIFY_RES;
import com.fy.boss.message.PASSPORT_GET_REQ;
import com.fy.boss.message.PASSPORT_GET_REQ2;
import com.fy.boss.message.PASSPORT_GET_RES;
import com.fy.boss.message.PASSPORT_GET_RES2;
import com.fy.boss.message.PASSPORT_LOGIN_REQ;
import com.fy.boss.message.PASSPORT_LOGIN_RES;
import com.fy.boss.message.PASSPORT_REGISTER_REQ;
import com.fy.boss.message.PASSPORT_REGISTER_RES;
import com.fy.boss.message.PASSPORT_UPDATE_REQ;
import com.fy.boss.message.PASSPORT_UPDATE_REQ2;
import com.fy.boss.message.PASSPORT_UPDATE_RES;
import com.fy.boss.message.PASSPORT_UPDATE_RES2;
import com.fy.boss.message.QQ_LOGIN_VALIDATE_REQ;
import com.fy.boss.message.QQ_LOGIN_VALIDATE_RES;
import com.fy.boss.message.QQ_SAVING_NEW_REQ;
import com.fy.boss.message.QQ_SAVING_NEW_RES;
import com.fy.boss.message.QQ_SAVING_PRO_REQ;
import com.fy.boss.message.QQ_SAVING_PRO_RES;
import com.fy.boss.message.QQ_SAVING_REQ;
import com.fy.boss.message.QQ_SAVING_RES;
import com.fy.boss.message.QUERY_SAVINGAMOUNT_REQ;
import com.fy.boss.message.QUERY_SAVINGAMOUNT_RES;
import com.fy.boss.message.SAVING_RECORD_REQ;
import com.fy.boss.message.SAVING_RECORD_RES;
import com.fy.boss.message.SAVING_UCIDTOUSERNAME_REQ;
import com.fy.boss.message.SAVING_UCIDTOUSERNAME_RES;
import com.fy.boss.message.SEND_FUND_FOR_NINEONE_REQ;
import com.fy.boss.message.SEND_FUND_FOR_NINEONE_RES;
import com.fy.boss.message.USER_SAVING_NEW_REQ;
import com.fy.boss.message.USER_SAVING_NEW_RES;
import com.fy.boss.message.USER_SAVING_REQ;
import com.fy.boss.message.USER_SAVING_RES;
import com.fy.boss.message.appstore.AppStoreGuojiSavingManager;
import com.fy.boss.message.appstore.AppStoreKunLunSavingManager;
import com.fy.boss.message.appstore.AppStoreMaLaiSavingManager;
import com.fy.boss.message.appstore.AppStoreSavingManager;
import com.fy.boss.platform.amazon.AmazonSavingManager;
import com.fy.boss.platform.duoku.DuokuAlipaySavingManager;
import com.fy.boss.platform.duoku.DuokuYYAlipaySavingManager;
import com.fy.boss.platform.feiliu.FeiLiuAliPaySavingManager;
import com.fy.boss.platform.googleplay.GooglePlaySavingManager;
import com.fy.boss.platform.kunlun.MaLaiSavingCallBackServlet;
import com.fy.boss.platform.migu.MiguWorker;
import com.fy.boss.platform.qihoo.QihooAliPaySavingManager;
import com.fy.boss.platform.qq.QQConstants;
import com.fy.boss.platform.qq.QQInfoService;
import com.fy.boss.platform.qq.QQUser;
import com.fy.boss.platform.qq.QQUserCenter;
import com.fy.boss.platform.qq.QQUserInfo;
import com.fy.boss.platform.qq.msdk.MSDKSavingManager;
import com.fy.boss.tools.JacksonManager;
import com.fy.boss.vip.platform.UCVipManager;
import com.fy.boss.vip.platform.VipPlayerInfoManager;
import com.fy.boss.vip.platform.model.UcVipRecord;
import com.fy.boss.vip.platform.model.VipNewPlayerInfoRecord;
import com.fy.boss.vip.platform.model.VipPlayerInfoRecord;
import com.xuanzhi.promotion.client.PromotionClient;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.JsonUtil;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionConnectedHandler;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.ConnectionSelector;
import com.xuanzhi.tools.transport.DefaultConnectionSelector;
import com.xuanzhi.tools.transport.MessageHandler;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class BossServerService implements ConnectionConnectedHandler, MessageHandler {

	public PassportManager getPassportManager() {
		return passportManager;
	}

	public static Logger logger = Logger.getLogger(BossServerService.class);

	DefaultConnectionSelector selector;

	private PassportManager passportManager;

	private ServerManager serverManager;

	private OrderFormManager orderFormManager;

	public OrderFormManager getOrderFormManager() {
		return orderFormManager;
	}

	public void setOrderFormManager(OrderFormManager orderFormManager) {
		this.orderFormManager = orderFormManager;
	}

	public static boolean startTestLog = false;
	
	public ServerManager getServerManager() {
		return serverManager;
	}

	public void setServerManager(ServerManager serverManager) {
		this.serverManager = serverManager;
	}

	public void setPassportManager(PassportManager passportManager) {
		this.passportManager = passportManager;
	}

	public ConnectionSelector getConnectionSelector() {
		return selector;
	}

	public void setConnectionSelector(ConnectionSelector selector) {
		this.selector = (DefaultConnectionSelector) selector;
		this.selector.setConnectionConnectedHandler(this);
	}

	@Override
	public void discardRequestMessage(Connection arg0, RequestMessage arg1) throws ConnectionException {
		// TODO Auto-generated method stub

	}

	public static int[] fundNumToVipLevel = { 0, 60, 420, 960, 1800, 3600, 7200, 15000, 42000, 84000, 180000 };

	@Override
	public ResponseMessage receiveRequestMessage(Connection conn, RequestMessage message) throws ConnectionException {
		long startTime = System.currentTimeMillis();
		String remoteAddress = conn.getRemoteAddress();
		String ip = remoteAddress.substring(1, remoteAddress.indexOf(":"));
		if(startTestLog){
			logger.warn("[BossReceiverMessage] [conn:"+conn.getName()+"] [remoteAddress:"+remoteAddress+"] [ip:"+ip+"] [message:"+message.getTypeDescription()+"] [length:"+message.getLength()+"] [type:"+message.getType()+"] [class:"+message.getClass()+"]");
		}
		if (message instanceof PASSPORT_REGISTER_REQ) {

			// 注册的结果，0表示成功，1表示用户已存在，2表示服务器内部错误"

			PASSPORT_REGISTER_REQ req = (PASSPORT_REGISTER_REQ) message;
			String username = req.getUsername();
			String password = req.getPasswd();
			// 注册结果 0成功,1用户已存在
			byte result = 0;
			try {
				Passport passport = new Passport();
				passport.setRegisterClientId(req.getRegisterClientId());
				passport.setUserName(username);
				passport.setPassWd(password);
				passport.setNickName(req.getNickName());
				passport.setFromWhere(req.getFromWhere());
				passport.setRegisterChannel(req.getRegisterChannel());
				passport.setRegisterClientId(req.getRegisterClientId());
				passport.setRegisterMobile(req.getRegisterMobile());
				passport.setRegisterMobileOs(req.getRegisterMobileOs());
				passport.setRegisterMobileType(req.getRegisterMobileType());
				passport.setRegisterNetworkMode(req.getRegisterNetworkMode());
				// passport.setLastLoginIp(ip);

				passport = passportManager.createPassport(passport);
				PASSPORT_REGISTER_RES res = new PASSPORT_REGISTER_RES(req.getSequnceNum(), passport.getId(), (byte) 0, "注册成功！");
				logger.info("[注册通行证] [成功] [username:" + username + "] [id:" + passport.getId() + "]  " + " [cost:" + (System.currentTimeMillis() - startTime)
						+ "ms]");
				return res;
			} catch (UsernameAlreadyExistsException e) {
				e.printStackTrace();
				result = 1;
				logger.warn("[注册通行证] [失败] [用户已存在] [username：" + username + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]", e);
				PASSPORT_REGISTER_RES res = new PASSPORT_REGISTER_RES(req.getSequnceNum(), -1L, (byte) 1, "注册失败，用户已存在！");
				return res;
			} catch (Exception e) {
				result = 2;
				logger.warn("[注册通行证] [失败] [出现未知错误] [" + username + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]", e);
				PASSPORT_REGISTER_RES res = new PASSPORT_REGISTER_RES(req.getSequnceNum(), -1L, (byte) 2, "注册失败，出现错误！");
				return res;
			}
		} else if (message instanceof PASSPORT_LOGIN_REQ) {
			PASSPORT_LOGIN_REQ req = (PASSPORT_LOGIN_REQ) message;
			String username = req.getUsername();
			String passwd = req.getPasswd();
			String lastLoginClientId = req.getLastLoginClientId();
			String lastLoginChannel = req.getLastLoginChannel();
			String lastLoginIp = ip;
			String lastLoginMobileOs = req.getLastLoginMobileOs();
			String lastLoginMobileType = req.getLastLoginMobileType();
			String lastLoginNetworkMode = req.getLastLoginNetworkMode();
			// 登录的结果，0表示成功登陆，1表示密码不匹配，2表示用户不存在，3此帐号正在被其他人使用，6表示包格式错误, 7 其他
			byte result = 0;
			try {
				Passport passport = passportManager.login(username, passwd, lastLoginClientId, lastLoginChannel, lastLoginIp, lastLoginMobileOs,
						lastLoginMobileType, lastLoginNetworkMode);
				PASSPORT_LOGIN_RES res = new PASSPORT_LOGIN_RES(req.getSequnceNum(), passport.getId(), result, "");
				if (logger.isInfoEnabled())
					logger.info("[用户登陆] [SUCC] [" + req.getUsername() + "]  [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
				return res;
			} catch (UsernameNotExistsException e) {
				// TODO Auto-generated catch block
				result = 2;
				PASSPORT_LOGIN_RES res = new PASSPORT_LOGIN_RES(req.getSequnceNum(), -1, result, StringUtil.getStackTrace(e));
				logger.error("[用户登陆] [FAILED] [用户名不存在] [" + req.getUsername() + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]", e);
				return res;
			} catch (PasswordWrongException e) {
				// TODO Auto-generated catch block
				result = 1;
				PASSPORT_LOGIN_RES res = new PASSPORT_LOGIN_RES(req.getSequnceNum(), -1, result, StringUtil.getStackTrace(e));
				logger.error("[用户登陆] [FAILED] [密码错误] [" + req.getUsername() + "]  [" + passwd + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]",
						e);
				return res;
			} catch (Throwable e) {
				result = 6;
				PASSPORT_LOGIN_RES res = new PASSPORT_LOGIN_RES(req.getSequnceNum(), -1, result, com.xuanzhi.language.translate.text_1676);
				logger.error("[用户登陆] [FAILED] [未知错误(30)] [" + req.getUsername() + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]", e);
				return res;
			}
		} else if (message instanceof PASSPORT_UPDATE_REQ) {
			PASSPORT_UPDATE_REQ req = (PASSPORT_UPDATE_REQ) message;
			Passport p = passportManager.getPassport(req.getPassportid());
			boolean isOk = false;
			PASSPORT_UPDATE_RES res = null;
			if (p != null) {
				List<String> fieldNames = new ArrayList<String>();
				p.setNickName(req.getNickName());
				fieldNames.add("nickName");
				p.setPassWd(req.getPasswd());
				fieldNames.add("passWd");
				p.setSecretQuestion(req.getSecretQuestion());
				fieldNames.add("secretQuestion");
				p.setSecretAnswer(req.getSecretAnswer());
				fieldNames.add("secretAnswer");
				p.setLastQuestionSetDate(new Date(req.getLastQuestionSetDate()));
				fieldNames.add("lastQuestionSetDate");
				p.setIsSetSecretQuestion(req.getIsSetSecretQuestion());
				fieldNames.add("isSetSecretQuestion");
				p.setStatus(req.getStatus());
				fieldNames.add("status");
				p.setLastUpdateStatusDate(new Date(req.getLastQuestionSetDate()));
				fieldNames.add("lastUpdateStatusDate");
				p.setUcPassword(req.getUcPassword());
				fieldNames.add("ucPassword");
				passportManager.batch_updatePassportFields(p, fieldNames);
				isOk = true;
				res = new PASSPORT_UPDATE_RES(req.getSequnceNum(), isOk, (byte) 0, "更新成功");
				if (logger.isInfoEnabled())
					logger.info("[更新Passport][成功][id:" + req.getPassportid() + "][username:" + req.getUsername() + "][passwd:" + p.getPassWd() + "][cost:"
							+ (System.currentTimeMillis() - startTime) + "ms]");
			} else {
				isOk = false;
				res = new PASSPORT_UPDATE_RES(req.getSequnceNum(), isOk, (byte) 1, "更新失败");
				logger.error("[更新Passport][失败][id:" + req.getPassportid() + "][username:" + req.getUsername() + "][cost:"
						+ (System.currentTimeMillis() - startTime) + "ms]");
			}

			return res;
		} else if (message instanceof APPSTORE_UPDATE_IDENTY_REQ) {
			APPSTORE_UPDATE_IDENTY_REQ req = (APPSTORE_UPDATE_IDENTY_REQ) message;
			Passport p = passportManager.getPassport(req.getPassportid());
			boolean isOk = false;
			APPSTORE_UPDATE_IDENTY_RES res = null;
			if (p != null) {
				List<String> fieldNames = new ArrayList<String>();
				p.setLastLoginIp(req.getAppidentify());
				fieldNames.add("lastLoginIp");
				passportManager.batch_updatePassportFields(p, fieldNames);
				isOk = true;
				res = new APPSTORE_UPDATE_IDENTY_RES(req.getSequnceNum(), isOk, (byte) 0, "更新成功");
				if (logger.isInfoEnabled())
					logger.info("[connName:" + conn.getName() + "] [更新PASSPORT的手机验证串] [成功] [id:" + req.getPassportid() + "] [标识串:" + req.getAppidentify()
							+ "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
			} else {
				isOk = false;
				res = new APPSTORE_UPDATE_IDENTY_RES(req.getSequnceNum(), isOk, (byte) 1, "更新失败");
				logger.error("[connName:" + conn.getName() + "] [更新PASSPORT的手机验证串] [失败] [id:" + req.getPassportid() + "] [标识串:" + req.getAppidentify()
						+ "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
			}

			return res;
		} else if (message instanceof PASSPORT_GET_REQ) {
			// 结果，0表示成功，1表示id不存在，2表示Boss内部错误
			PASSPORT_GET_REQ req = (PASSPORT_GET_REQ) message;
			Passport p = passportManager.getPassport(req.getId());
			PASSPORT_GET_RES res = null;
			if (p != null) {
				res = new PASSPORT_GET_RES(req.getSequnceNum(), p.getId(), p.getRegisterClientId(), p.getUserName(), p.getPassWd(), p.getNickName(), p
						.getFromWhere(), p.getRegisterDate() == null ? -1 : p.getRegisterDate().getTime(), p.getLastLoginDate() == null ? -1 : p
								.getLastLoginDate().getTime(), p.getRegisterChannel(), p.getLastLoginChannel(), p.getLastLoginIp(), p.getLastLoginClientId(), p
								.getRegisterMobile(), p.getRegisterMobileOs(), p.getLastLoginMobileOs(), p.getRegisterMobileType(), p.getLastLoginMobileType(), p
								.getRegisterNetworkMode(), p.getLastLoginNetworkMode(), p.getTotalChargeAmount(), p.getLastChargeDate() == null ? -1 : p
										.getLastChargeDate().getTime(), p.getLastChargeAmount(), p.getLastChargeChannel(), p.getSecretQuestion(), p.getSecretAnswer(), p
										.getLastQuestionSetDate() == null ? -1 : p.getLastQuestionSetDate().getTime(), p.getIsSetSecretQuestion(), p.getStatus(), p
												.getLastUpdateStatusDate() == null ? -1 : p.getLastUpdateStatusDate().getTime(), p.getUcPassword() == null ? "" : p.getUcPassword(),
														(byte) 0, "");

				logger.info("[通过ID获得通行证] [成功] [id:" + req.getId() + "] [userName:" + p.getUserName() + "] [" + conn.getName() + "]");
			} else {
				logger.error("[通过ID获得通行证][失败][id:" + req.getId() + "][cost:" + (System.currentTimeMillis() - startTime) + "ms]");
				res = new PASSPORT_GET_RES(req.getSequnceNum(), -1L, "", "", "", "", "", -1L, -1L, "", "", "", "", "", "", "", "", "", "", "", -1L, -1L, -1L,
						"", "", "", -1L, false, -1, -1L, "", (byte) 1, "出现异常，无Passport");
			}
			return res;
		} else if (message instanceof PASSPORT_GET_REQ2) {
			// 结果，0表示成功，1表示用户不存在
			PASSPORT_GET_REQ2 req = (PASSPORT_GET_REQ2) message;
			Passport p = passportManager.getPassport(req.getUsername());
			if(p == null){
				try{
					//针对快用绑定老账号
					p = passportManager.getPassportByNickName(req.getUsername());
					if(p != null && p.getLastLoginChannel().contains("KUAIYONGSDK")){
						
					}else{
						p = null;
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			PASSPORT_GET_RES2 res = null;
			boolean isOk = false;
			if (p != null) {
				res = new PASSPORT_GET_RES2(req.getSequnceNum(), p.getId(), p.getRegisterClientId(), p.getUserName(), p.getPassWd(), p.getNickName(), p
						.getFromWhere(), p.getRegisterDate() == null ? -1 : p.getRegisterDate().getTime(), p.getLastLoginDate() == null ? -1 : p
								.getLastLoginDate().getTime(), p.getRegisterChannel(), p.getLastLoginChannel(), p.getLastLoginIp(), p.getLastLoginClientId(), p
								.getRegisterMobile(), p.getRegisterMobileOs(), p.getLastLoginMobileOs(), p.getRegisterMobileType(), p.getLastLoginMobileType(), p
								.getRegisterNetworkMode(), p.getLastLoginNetworkMode(), p.getTotalChargeAmount(), p.getLastChargeDate() == null ? -1 : p
										.getLastChargeDate().getTime(), p.getLastChargeAmount(), p.getLastChargeChannel(), p.getSecretQuestion(), p.getSecretAnswer(), p
										.getLastQuestionSetDate() == null ? -1 : p.getLastQuestionSetDate().getTime(), p.getIsSetSecretQuestion(), p.getStatus(), p
												.getLastUpdateStatusDate() == null ? -1 : p.getLastUpdateStatusDate().getTime(), p.getUcPassword() == null ? "" : p.getUcPassword(),
														(byte) 0, "");
				if (logger.isInfoEnabled())
					logger.info("[通过username获得通行证] [成功] [id:" + p.getId() + "] [username:" + req.getUsername() + "] [cost:"
							+ (System.currentTimeMillis() - startTime) + "ms]");
				return res;
			} else {
				logger.error("[通过username获得通行证][失败][username:" + req.getUsername() + "][cost:" + (System.currentTimeMillis() - startTime) + "ms]");
				res = new PASSPORT_GET_RES2(req.getSequnceNum(), -1L, "", "", "", "", "", -1L, -1L, "", "", "", "", "", "", "", "", "", "", "", -1L, -1L, -1L,
						"", "", "", -1L, false, -1, -1L, "", (byte) 1, "出现异常，无Passport");
				return res;
			}

		} else if (message instanceof QQ_LOGIN_VALIDATE_REQ) {
			QQ_LOGIN_VALIDATE_REQ req = (QQ_LOGIN_VALIDATE_REQ) message;
			String uid = req.getUid();
			String sessionId = req.getSessionId();
			QQUserCenter center = QQUserCenter.getInstance();
			QQUser user = center.getUser(uid);
			if (user != null && user.getSid().equals(sessionId)) {
				QQ_LOGIN_VALIDATE_RES res = new QQ_LOGIN_VALIDATE_RES(req.getSequnceNum(), (byte) 0);
				logger.info("[腾讯登录验证] [成功] [UID:" + uid + "] [sessionID:" + sessionId + "]");
				return res;
			} else {
				QQ_LOGIN_VALIDATE_RES res = new QQ_LOGIN_VALIDATE_RES(req.getSequnceNum(), (byte) 1);
				logger.info("[腾讯登录验证] [失败] [UID:" + uid + "] [sessionID:" + sessionId + "] [" + (user == null ? "没有用户信息" : "sessionID不一致") + "]");
				return res;
			}
		} else if (message instanceof QQ_SAVING_REQ) {
			// //结果,0-成功, 1-uid不存在 2-订单生成失败 3-服务器不存在

			QQ_SAVING_REQ req = (QQ_SAVING_REQ) message;
			QQ_SAVING_RES res = null;
			// 接受请求 抽出参数
			String userName = req.getUid();
			String serverName = req.getServerName();

			// 验证用户合法性
			Passport user = passportManager.getPassport(userName);

			if (user != null) {
				int goodsCount = req.getGoodcount();
				String savingType = req.getSavingType();
				// 生成订单
				OrderForm order = new OrderForm();
				order.setCreateTime(new Date().getTime());
				// 设置充值平台
				order.setSavingPlatform("腾讯");
				// 设置充值介质
				order.setSavingMedium(savingType);
				// 设置充值人
				order.setPassportId(user.getId());
				// 设置商品数量
				order.setGoodsCount(goodsCount);
				// 设置消费金额
			
				order.setPayMoney(QQConstants.GOODS_PRICE * goodsCount);
				// 设置serverid
				order.setServerName(serverName);
				order.setHandleResult(-1);
				order.setHandleResultDesp("新生成订单");
				// 设置订单responseResult
				order.setResponseResult(-1);
				order.setResponseDesp("新生成订单");
				// 设置是否通知游戏服状态 设为false
				order.setNotified(false);
				// 设置通知游戏服是否成功 设为false
				order.setNotifySucc(false);
				order.setUserChannel(user.getLastLoginChannel() == null ? "QQ_MIESHI" : user.getLastLoginChannel());
				// 先存入到数据库中
				order = orderFormManager.createOrderForm(order);
				if (logger.isInfoEnabled())
					logger.info("[" + conn.getName() + "] [生成订单] [插入新订单成功] [用户名:" + userName + "] [serverName:" + serverName + "] [支付方式:" + savingType
							+ "] [充值平台：腾讯] [商品数量：" + goodsCount + "] [orderForm ID:" + order.getId() + "] [cost:" + (System.currentTimeMillis() - startTime)
							+ "ms]");
				// 设置订单号
				order.setOrderId(DateUtil.formatDate(new Date(), "yyyy-MM-dd") + "-" + QQConstants.GOODS_ID + "-" + order.getId());
				// orderFormManager.getEm().notifyFieldChange(order, "orderId");
				// orderFormManager.updateOrderForm(order);
				orderFormManager.updateOrderForm(order, "orderId");
				if (logger.isInfoEnabled())
					logger.info("[" + conn.getName() + "] [生成订单-] [更新订单信息成功] [用户名:" + userName + "] [serverName:" + serverName + "] [支付方式:" + savingType
							+ "] [充值平台：腾讯] [商品数量：" + goodsCount + "] [orderForm ID:" + order.getId() + "] [orderId:" + order.getOrderId() + "] [cost:"
							+ (System.currentTimeMillis() - startTime) + "ms]");

				// 将订单id放入Reseponse中返回
				res = new QQ_SAVING_RES(req.getSequnceNum(), order.getOrderId(), (byte) 0, "生成订单成功！");
				return res;
			} else {
				logger.error("[" + conn.getName() + "] [生成订单] [失败：用户不存在] [用户名:" + userName + "] [serverName:" + serverName + "] [支付方式:" + req.getSavingType()
						+ "] [充值平台：腾讯] [商品数量：" + req.getGoodcount() + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
				res = new QQ_SAVING_RES(req.getSequnceNum(), "", (byte) 1, "生成订单失败，用户[" + req.getUid() + "]不存在！");
				return res;
			}
		} else if (message instanceof QQ_SAVING_NEW_REQ) {
			// //结果,0-成功, 1-uid不存在 2-订单生成失败 3-服务器不存在

			QQ_SAVING_NEW_REQ req = (QQ_SAVING_NEW_REQ) message;
			QQ_SAVING_NEW_RES res = null;
			// 接受请求 抽出参数
			String userName = req.getUid();
			String serverName = req.getServerName();

			// 验证用户合法性
			Passport user = passportManager.getPassport(userName);

			if (user != null) {
				int goodsCount = req.getGoodcount();
				String savingType = req.getSavingType();
				// 生成订单
				OrderForm order = new OrderForm();
				order.setCreateTime(new Date().getTime());
				// 设置充值平台
				order.setSavingPlatform("腾讯");
				// 设置充值介质
				order.setSavingMedium(savingType);
				// 设置充值人
				order.setPassportId(user.getId());
				// 设置商品数量
				order.setGoodsCount(goodsCount);
				// 设置消费金额
				order.setPayMoney(QQConstants.GOODS_PRICE * goodsCount);
				// 设置serverid
				order.setServerName(serverName);
				order.setHandleResult(-1);
				order.setHandleResultDesp("新生成订单");
				// 设置订单responseResult
				order.setResponseResult(-1);
				order.setResponseDesp("新生成订单");
				// 设置是否通知游戏服状态 设为false
				order.setNotified(false);
				// 设置通知游戏服是否成功 设为false
				order.setNotifySucc(false);
				order.setUserChannel(req.getUserChannel());
				// 先存入到数据库中
				order = orderFormManager.createOrderForm(order);
				if (logger.isInfoEnabled())
					logger.info("[" + conn.getName() + "] [生成订单] [插入新订单成功] [用户名:" + userName + "] [serverName:" + serverName + "] [支付方式:" + savingType
							+ "] [充值平台：腾讯] [商品数量：" + goodsCount + "] [orderForm ID:" + order.getId() + "] [cost:" + (System.currentTimeMillis() - startTime)
							+ "ms]");
				// 设置订单号
				order.setOrderId(DateUtil.formatDate(new Date(), "yyyy-MM-dd") + "-" + QQConstants.GOODS_ID + "-" + order.getId());
				// orderFormManager.getEm().notifyFieldChange(order, "orderId");
				// orderFormManager.updateOrderForm(order);
				orderFormManager.updateOrderForm(order, "orderId");
				if (logger.isInfoEnabled())
					logger.info("[" + conn.getName() + "] [生成订单] [更新订单信息成功] [用户名:" + userName + "] [serverName:" + serverName + "] [支付方式:" + savingType
							+ "] [充值平台：腾讯] [商品数量：" + goodsCount + "] [orderForm ID:" + order.getId() + "] [orderId:" + order.getOrderId() + "] [cost:"
							+ (System.currentTimeMillis() - startTime) + "ms]");

				// 将订单id放入Reseponse中返回
				res = new QQ_SAVING_NEW_RES(req.getSequnceNum(), order.getOrderId(), (byte) 0, "生成订单成功！");
				return res;
			} else {
				logger.error("[" + conn.getName() + "] [生成订单] [失败：用户不存在] [用户名:" + userName + "] [serverName:" + serverName + "] [支付方式:" + req.getSavingType()
						+ "] [充值平台：腾讯] [商品数量：" + req.getGoodcount() + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
				res = new QQ_SAVING_NEW_RES(req.getSequnceNum(), "", (byte) 1, "生成订单失败，用户[" + req.getUid() + "]不存在！");
				return res;
			}
		} 
		else if (message instanceof QQ_SAVING_PRO_REQ) {
			// //结果,0-成功, 1-uid不存在 2-订单生成失败 3-服务器不存在

			QQ_SAVING_PRO_REQ req = (QQ_SAVING_PRO_REQ) message;
			QQ_SAVING_PRO_RES res = null;
			// 接受请求 抽出参数
			String userName = req.getUid();
			String serverName = req.getServerName();

			// 验证用户合法性
			Passport user = passportManager.getPassport(userName);

			if (user != null) {
				int goodsCount = req.getGoodcount();
				String savingType = req.getSavingType();
				// 生成订单
				OrderForm order = new OrderForm();
				order.setCreateTime(new Date().getTime());
				// 设置充值平台
				order.setSavingPlatform("腾讯");
				// 设置充值介质
				order.setSavingMedium(savingType);
				// 设置充值人
				order.setPassportId(user.getId());
				// 设置商品数量
				order.setGoodsCount(goodsCount);
				// 设置消费金额
				order.setPayMoney(QQConstants.GOODS_PRICE * goodsCount);
				// 设置serverid
				order.setServerName(serverName);
				order.setHandleResult(-1);
				order.setHandleResultDesp("新生成订单");
				// 设置订单responseResult
				order.setResponseResult(-1);
				order.setResponseDesp("新生成订单");
				// 设置是否通知游戏服状态 设为false
				order.setNotified(false);
				// 设置通知游戏服是否成功 设为false
				order.setNotifySucc(false);
				order.setUserChannel(user.getLastLoginChannel() == null ? "QQ_MIESHI" : user.getLastLoginChannel());
				order.setMemo1(req.getOthers()[0]);
				if(req.getOthers() != null && req.getOthers().length > 0){
					if(req.getOthers().length > 1){
						order.setChargeValue(req.getOthers()[1]);
					}
					if(req.getOthers().length > 2){
						order.setChargeType(req.getOthers()[2]);
					}
				}
				
				// 先存入到数据库中
				order = orderFormManager.createOrderForm(order);
				if (logger.isInfoEnabled())
					logger.info("[" + conn.getName() + "] [生成订单] [插入新订单成功] [用户名:" + userName + "] [serverName:" + serverName + "] [支付方式:" + savingType
							+ "] [充值平台：腾讯] [商品数量：" + goodsCount + "] [orderForm ID:" + order.getId() + "] [角色id:"+order.getMemo1()+"] [cost:" + (System.currentTimeMillis() - startTime)
							+ "ms]");
				// 设置订单号
				order.setOrderId(DateUtil.formatDate(new Date(), "yyyy-MM-dd") + "-" + QQConstants.GOODS_ID + "-" + order.getId());
				// orderFormManager.getEm().notifyFieldChange(order, "orderId");
				// orderFormManager.updateOrderForm(order);
				orderFormManager.updateOrderForm(order, "orderId");
				if (logger.isInfoEnabled())
					logger.info("[" + conn.getName() + "] [生成订单] [更新订单信息成功] [用户名:" + userName + "] [serverName:" + serverName + "] [支付方式:" + savingType
							+ "] [充值平台：腾讯] [商品数量：" + goodsCount + "] [orderForm ID:" + order.getId() + "] [orderId:" + order.getOrderId() + "] [角色id:"+order.getMemo1()+"] [cost:"
							+ (System.currentTimeMillis() - startTime) + "ms]");

				// 将订单id放入Reseponse中返回
				res = new QQ_SAVING_PRO_RES(req.getSequnceNum(), order.getOrderId(), (byte) 0, "生成订单成功！");
				return res;
			} else {
				logger.error("[" + conn.getName() + "] [生成订单] [失败：用户不存在] [用户名:" + userName + "] [serverName:" + serverName + "] [支付方式:" + req.getSavingType()
						+ "] [充值平台：腾讯] [商品数量：" + req.getGoodcount() + "] [角色id:"+req.getOthers()[0]+"] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
				res = new QQ_SAVING_PRO_RES(req.getSequnceNum(), "", (byte) 1, "生成订单失败，用户[" + req.getUid() + "]不存在！");
				return res;
			}
		}


		else if (message instanceof USER_SAVING_REQ) {
			USER_SAVING_REQ req = (USER_SAVING_REQ) message;
			String username = req.getUsername();
			int result = 0;
			String error = "";
			Passport passport = PassportManager.getInstance().getPassport(username);
			if (passport == null) {
				result = 1;
				error = "无此用户";
			} else {
				String orderFormId = PlatformSavingCenter.getInstance().cardSaving(passport, req.getCardtype(), req.getMianzhi(), req.getCardno(),
						req.getCardpass(), req.getServername(), req.getChannel(), req.getOrderID(), req.getPlayername(), req.getOs());
				if (orderFormId == null) {
					result = 1;
					error = "生成订单失败";
				}
			}
			if (logger.isInfoEnabled()) {
				logger.info("[玩家充值提交] [结果:" + (result == 0 ? "成功" : "失败") + "] [error:" + error + "] [username:" + username + "] [player:"
						+ req.getPlayername() + "] [当前客户端渠道:" + req.getChannel() + "] [类型:" + req.getCardtype() + "] [卡号:" + req.getCardno() + "] [卡密:"
						+ req.getCardpass() + "] [面值:" + req.getMianzhi() + "] [amount:" + req.getAmount() + "] [goodsType:" + req.getGoodsType()
						+ "] [第三方订单号:" + req.getOrderID() + "]");
			}
			USER_SAVING_RES res = new USER_SAVING_RES(req.getSequnceNum(), (byte) result, error);
			return res;
		}

		else if (message instanceof USER_SAVING_NEW_REQ) {
			USER_SAVING_NEW_REQ req = (USER_SAVING_NEW_REQ) message;
			String username = req.getUsername();
			int result = 0;
			String error = "";
			Passport passport = PassportManager.getInstance().getPassport(username);
			if (passport == null) {
				result = 1;
				error = "无此用户";
			} else {
				String orderFormId = PlatformSavingCenter.getInstance().cardSaving(passport, req.getCardtype(), req.getMianzhi(), req.getCardno(),
						req.getCardpass(), req.getServername(), req.getChannel(), req.getOrderID(), req.getPlayername(), req.getOs(), req.getOthers());
				if (orderFormId == null) {
					result = 1;
					error = "生成订单失败";
				}
			}
			if (logger.isInfoEnabled()) {
				logger.info("[玩家充值提交] [结果:" + (result == 0 ? "成功" : "失败") + "] [error:" + error + "] [username:" + username + "] [player:"
						+ req.getPlayername() + "] [当前客户端渠道:" + req.getChannel() + "] [类型:" + req.getCardtype() + "] [卡号:" + req.getCardno() + "] [卡密:"
						+ req.getCardpass() + "] [面值:" + req.getMianzhi() + "] [amount:" + req.getAmount() + "] [goodsType:" + req.getGoodsType()
						+ "] [第三方订单号:" + req.getOrderID() + "]");
			}
			USER_SAVING_NEW_RES res = new USER_SAVING_NEW_RES(req.getSequnceNum(), (byte) result, error);
			return res;
		}

		else if (message instanceof CHANNEL_UER_SAVING_REQ) {
			CHANNEL_UER_SAVING_REQ req = (CHANNEL_UER_SAVING_REQ) message;
			CHANNEL_UER_SAVING_RES res = null;
			// 接受请求 抽出参数
			String userName = req.getUid();
			String serverName = req.getServerName();

			// 根据username获取passportid
			Passport user = passportManager.getPassport(userName);

			// 验证游戏服名字
			Server server = serverManager.getServer(serverName.trim());

			if (user != null) {

				// 验证成功后 accessToken肯定不会为空 故不作判断
				// 去调uc的充值接口
				int goodsCount = req.getGoodsCount();
				String savingType = req.getPayType();
				// 走充值中心 渠道是UC_MIESHI
				PlatformSavingCenter psc = PlatformSavingCenter.getInstance();
				String returnMessage = psc.cardSaving(user, req.getPayType(), req.getPayAmount(), req.getCardNo(), req.getCardPassword(), serverName, req
						.getChannel(), "", "", req.getMobileOs());

				if (!StringUtils.isEmpty(returnMessage)) {
					res = new CHANNEL_UER_SAVING_RES(req.getSequnceNum(), (byte) 0, returnMessage);

					if (logger.isInfoEnabled())
						logger.info("[" + conn.getName() + "] [渠道充值订单生成] [成功] [用户id:" + user.getId() + "] [用户名:" + user.getUserName() + "] [充值类型:"
								+ req.getPayType() + "] [充值金额:" + req.getPayAmount() + "] [充值卡号:" + req.getCardNo() + "] [充值卡密码:" + req.getCardPassword()
								+ "] [游戏服务器名称:" + serverName + "] [手机平台:" + req.getMobileOs() + "] [充值平台:" + req.getChannel() + "] [costs:"
								+ (System.currentTimeMillis() - startTime) + "ms]");
					return res;
				} else {
					res = new CHANNEL_UER_SAVING_RES(req.getSequnceNum(), (byte) 1, returnMessage == null ? "" : returnMessage);

					logger.error("[" + conn.getName() + "] [渠道充值订单生成] [失败:系统错误] [用户id:" + user.getId() + "] [用户名:" + user.getUserName() + "] [充值类型:"
							+ req.getPayType() + "] [充值金额:" + req.getPayAmount() + "] [充值卡号:" + req.getCardNo() + "] [充值卡密码:" + req.getCardPassword()
							+ "] [游戏服务器名称:" + serverName + "] [手机平台:" + req.getMobileOs() + "] [充值平台:" + req.getChannel() + "] [返回信息:" + returnMessage
							+ "] [costs:" + (System.currentTimeMillis() - startTime) + "ms]");
					return res;
				}
				// 调用uc的充值接口获得相关的响应

				// 将订单id放入Reseponse中返回
				// res = new
				// QQ_SAVING_RES(req.getSequnceNum(),order.getOrderId(),(byte)0,"生成订单成功！");

				/*
				 * else //如果server不存在 { logger.error("["+conn.getName()+"]
				 * [渠道充值订单生成] [失败：游戏服务器不存在] [用户id:"+user.getId()+"]
				 * [用户名:"+user.getUserName()+"] [充值类型:"+req.getPayType()+"]
				 * [充值金额:"+req.getPayAmount()+"] [充值卡号:"+req.getCardNo()+"]
				 * [充值卡密码:"+req.getCardPassword()+"] [游戏服务器名称:"+server+"]
				 * [手机平台:"+req.getMobileOs()+"] [充值平台:"+req.getChannel()+"]
				 * [costs:"+(System.currentTimeMillis()-startTime)+"ms]"); res =
				 * new
				 * CHANNEL_UER_SAVING_RES(req.getSequnceNum(),(byte)1,"生成订单失败，服务器不存在！");
				 * return res; }
				 */
			} else {
				logger.error("[" + conn.getName() + "] [渠道充值订单生成] [失败：用户不存在] [充值类型:" + req.getPayType() + "] [充值金额:" + req.getPayAmount() + "] [充值卡号:"
						+ req.getCardNo() + "] [充值卡密码:" + req.getCardPassword() + "] [游戏服务器名称:" + server + "] [手机平台:" + req.getMobileOs() + "] [充值平台:"
						+ req.getChannel() + "] [costs:" + (System.currentTimeMillis() - startTime) + "ms]");
				res = new CHANNEL_UER_SAVING_RES(req.getSequnceNum(), (byte) 1, "生成订单失败，用户不存在！");
				return res;
			}
		} else if (message instanceof CHANNEL_UER_SAVING_NEW_REQ) {
			CHANNEL_UER_SAVING_NEW_REQ req = (CHANNEL_UER_SAVING_NEW_REQ) message;
			CHANNEL_UER_SAVING_NEW_RES res = null;
			// 接受请求 抽出参数
			String userName = req.getUid();
			String serverName = req.getServerName();

			// 根据username获取passportid
			Passport user = passportManager.getPassport(userName);

			// 验证游戏服名字
			Server server = serverManager.getServer(serverName.trim());

			if (user != null) {

				// 验证成功后 accessToken肯定不会为空 故不作判断
				// 去调uc的充值接口
				int goodsCount = req.getGoodsCount();
				String savingType = req.getPayType();
				// 走充值中心 渠道是UC_MIESHI
				PlatformSavingCenter psc = PlatformSavingCenter.getInstance();
				String returnMessage = psc.cardSaving(user, req.getPayType(), req.getPayAmount(), req.getCardNo(), req.getCardPassword(), serverName, req
						.getChannel(), "", "", req.getMobileOs(), req.getOthers());

				if (!StringUtils.isEmpty(returnMessage)) {
					res = new CHANNEL_UER_SAVING_NEW_RES(req.getSequnceNum(), (byte) 0, returnMessage);

					if (logger.isInfoEnabled())
						logger.info("[" + conn.getName() + "] [渠道充值订单生成] [成功] [用户id:" + user.getId() + "] [用户名:" + user.getUserName() + "] [充值类型:"
								+ req.getPayType() + "] [充值金额:" + req.getPayAmount() + "] [充值卡号:" + req.getCardNo() + "] [充值卡密码:" + req.getCardPassword()
								+ "] [游戏服务器名称:" + serverName + "] [手机平台:" + req.getMobileOs() + "] [充值平台:" + req.getChannel() + "] [costs:"
								+ (System.currentTimeMillis() - startTime) + "ms]");
					return res;
				} else {
					res = new CHANNEL_UER_SAVING_NEW_RES(req.getSequnceNum(), (byte) 1, returnMessage == null ? "" : returnMessage);

					logger.error("[" + conn.getName() + "] [渠道充值订单生成] [失败:系统错误] [用户id:" + user.getId() + "] [用户名:" + user.getUserName() + "] [充值类型:"
							+ req.getPayType() + "] [充值金额:" + req.getPayAmount() + "] [充值卡号:" + req.getCardNo() + "] [充值卡密码:" + req.getCardPassword()
							+ "] [游戏服务器名称:" + serverName + "] [手机平台:" + req.getMobileOs() + "] [充值平台:" + req.getChannel() + "] [返回信息:" + returnMessage
							+ "] [costs:" + (System.currentTimeMillis() - startTime) + "ms]");
					return res;
				}
			} else {
				logger.error("[" + conn.getName() + "] [渠道充值订单生成] [失败：用户不存在] [充值类型:" + req.getPayType() + "] [充值金额:" + req.getPayAmount() + "] [充值卡号:"
						+ req.getCardNo() + "] [充值卡密码:" + req.getCardPassword() + "] [游戏服务器名称:" + server + "] [手机平台:" + req.getMobileOs() + "] [充值平台:"
						+ req.getChannel() + "] [costs:" + (System.currentTimeMillis() - startTime) + "ms]");
				res = new CHANNEL_UER_SAVING_NEW_RES(req.getSequnceNum(), (byte) 1, "生成订单失败，用户不存在！");
				return res;
			}
		}

		else if (message instanceof APPSTORE_SAVING_VERIFY_REQ) {
			APPSTORE_SAVING_VERIFY_REQ req = (APPSTORE_SAVING_VERIFY_REQ) message;
			Passport passport = PassportManager.getInstance().getPassport(req.getUsername());
			int result = 0;
			String error = "";
			if (req.getChannel().indexOf("APPSTOREGUOJI") != -1) {
				// 国际版先去掉限制
			} else if (req.getChannel().indexOf("KUNLUNAPPSTORE") != -1) {
				// 昆仑appstore先去掉限制
			} else if (req.getChannel().indexOf("MALAIIOS") != -1) {
				// 昆仑appstore先去掉限制
			} else if (req.getChannel().indexOf("AMAZON") != -1) {

			} else {
				String dcode = null;
				if (req.getVerifyStr().indexOf("MAC") != -1) {
					dcode = req.getVerifyStr().split("MAC")[0];
				}
				if (passport == null) {
					result = 1;
					error = "获取用户信息失败，请联系GM";
				} else {
					if (dcode == null) {
						// result = 1;
						// error = "无法获取设备号，请联系GM";
						logger.warn("[校验是否可以充值:充值请求上未携带合法设备标识串，暂不做限制] [username:" + req.getUsername() + "] [channel:" + req.getChannel() + "] [verify:"
								+ req.getVerifyStr() + "] [passport上的设备校验串:" + passport.getLastLoginIp() + "] [passport的注册渠道:" + passport.getRegisterChannel()
								+ "] [" + (System.currentTimeMillis() - startTime) + "ms]");

					} else {
						String regCode = passport.getLastLoginIp();
						if (regCode == null || regCode.equals("116.213.192.216")) {
							// result = 1;
							// error = "请您使用苹果官方下载的飘渺寻仙曲HD应用注册帐号，再充值！";
							logger.warn("[校验是否可以充值:Passport上无合法注册设备号，暂不做限制] [username:" + req.getUsername() + "] [channel:" + req.getChannel() + "] [verify:"
									+ req.getVerifyStr() + "] [passport上的设备校验串:" + passport.getLastLoginIp() + "] [passport的注册渠道:"
									+ passport.getRegisterChannel() + "] [" + (System.currentTimeMillis() - startTime) + "ms]");
						} else if (regCode.indexOf(dcode) == -1) {
							// // result = 1;
							// // error =
							// "尊敬的玩家，此设备不是此帐号的注册时使用的设备，请您使用注册时的设备进行充值！";
							logger.warn("[校验是否可以充值:充值设备标识不等于注册设备标识，暂不做限制] [username:" + req.getUsername() + "] [channel:" + req.getChannel() + "] [verify:"
									+ req.getVerifyStr() + "] [passport上的设备校验串:" + passport.getLastLoginIp() + "] [passport的注册渠道:"
									+ passport.getRegisterChannel() + "] [" + (System.currentTimeMillis() - startTime) + "ms]");
						} else {
							logger.warn("[校验是否可以充值:登陆设备就是注册设备] [username:" + req.getUsername() + "] [channel:" + req.getChannel() + "] [verify:"
									+ req.getVerifyStr() + "] [passport上的设备校验串:" + passport.getLastLoginIp() + "] [passport的注册渠道:"
									+ passport.getRegisterChannel() + "] [" + (System.currentTimeMillis() - startTime) + "ms]");
						}
						// else
						// if(SavingLimitManager.getInstance().isLimit(passport,
						// dcode, req.getChannel())) {
						// // result = 1;
						// // error = "您已达今日充值上限，请改日再充值。";
						// }
					}
				}
			}
			// else if(SavingForbidManager.getInstance().isForbid(passport,
			// code)) {
			// result = 1;
			// error = "您已被禁止充值，如有异议请与GM联系。";
			// } else if(SavingLimitManager.getInstance().isLimit(passport,
			// code, req.getChannel())) {
			// result = 1;
			// error = "您已达今日充值上限，请改日再充值。";
			// } else
			// if(!SavingPeriodManager.getInstance().isPeriodPermit(passport,
			// code)) {
			// result = 1;
			// error = "您的充值频率过快，请稍候再试。";
			// }
			// }
			if (logger.isInfoEnabled()) {
				logger.info("[校验是否可以充值]  [result:" + (result == 0 ? "成功" : "失败") + "] [error:" + error + "] [username:" + req.getUsername() + "] [channel:"
						+ req.getChannel() + "] [verify:" + req.getVerifyStr() + "] [" + (System.currentTimeMillis() - startTime) + "ms]");
			}
			return new APPSTORE_SAVING_VERIFY_RES(req.getSequnceNum(), result, error);
		} else if (message instanceof APPSTORE_RECEIPT_REQ) {
			APPSTORE_RECEIPT_REQ req = (APPSTORE_RECEIPT_REQ) message;
			String username = req.getUsername();
			int result = 0;
			String error = "";
			Passport passport = PassportManager.getInstance().getPassport(username);
			if (passport == null) {
				result = 1;
				error = "无此用户";
			} else {

				String orderFormId = null;
				if (req.getChannel().equals("APPSTORE_XUNXIAN") || req.getChannel().equals("APPSTORE2014_MIESHI")) {
					orderFormId = AppStoreSavingManager.getInstance().appStoreSaving(passport, req.getChannel(), req.getServername(), req.getReceipt(),
							req.getDeviceCode());
				} else if (req.getChannel().equals("APPSTOREGUOJI_MIESHI")) {
					orderFormId = AppStoreGuojiSavingManager.getInstance().appStoreSaving(passport, req.getChannel(), req.getServername(), req.getReceipt(),
							req.getDeviceCode());
				} else if (req.getChannel().contains("KUNLUNAPPSTORE")) {
					orderFormId = AppStoreKunLunSavingManager.getInstance().appStoreSaving(passport, req.getChannel(), req.getServername(), req.getReceipt(),
							req.getDeviceCode());
				} 
				else if (req.getChannel().contains("KUNLUNSDKAPPSTORE")) {
					orderFormId = AppStoreKunLunSavingManager.getInstance().appStoreSaving(passport, req.getChannel(), req.getServername(), req.getReceipt(),
							req.getDeviceCode());
				}
				else if (req.getChannel().contains("MALAIIOS")) {
					orderFormId = AppStoreMaLaiSavingManager.getInstance().appStoreSaving(passport, req.getChannel(), req.getServername(), req.getReceipt(),
							req.getDeviceCode());
				} else if (req.getChannel().contains("AMAZON")) {
					orderFormId = AmazonSavingManager.getInstance().appStoreSaving(passport, req.getChannel(), req.getServername(), req.getReceipt(),
							req.getDeviceCode());
				}
				else if (req.getChannel().contains("GOOGLEINAPPBILLING")) {
					orderFormId = GooglePlaySavingManager.getInstance().appStoreSaving(passport, req.getChannel(), req.getServername(), req.getReceipt(),
							req.getDeviceCode());
				}

				if (orderFormId == null) {
					result = 1;
					error = "生成订单失败";
				}
			}
			if (logger.isInfoEnabled()) {
				logger.info("[玩家充值提交] [结果:" + (result == 0 ? "成功" : "失败") + "] [error:" + error + "] [username:" + username + "] [当前客户端渠道:" + req.getChannel()
						+ "] [server:" + req.getServername() + "] [平台:AppStore] [Receipt:" + req.getReceipt() + "]");
			}
			APPSTORE_RECEIPT_RES res = new APPSTORE_RECEIPT_RES(req.getSequnceNum(), (byte) result);
			return res;
		} else if (message instanceof APPSTORE_RECEIPT_NEW_REQ) {
			APPSTORE_RECEIPT_NEW_REQ req = (APPSTORE_RECEIPT_NEW_REQ) message;
			String username = req.getUsername();
			int result = 0;
			String error = "";
			Passport passport = PassportManager.getInstance().getPassport(username);
			if (passport == null) {
				result = 1;
				error = "无此用户";
			} else {

				String orderFormId = null;
				if (req.getChannel().equals("APPSTORE_XUNXIAN") || req.getChannel().equals("JIUZHOUAPPSTORE_XUNXIAN")
						|| req.getChannel().equals("QILEAPPSTORE_XUNXIAN")|| req.getChannel().equals("HUOGAMEAPPSTORE_XUNXIAN")|| 
						req.getChannel().equals("HUOGAME2APPSTORE_XUNXIAN") || req.getChannel().equals("JIUZHOUPIAOMIAOQU_XUNXIAN") || req.getChannel().equals("XUNXIANJUEAPPSTORE_XUNXIAN")) {
					orderFormId = AppStoreSavingManager.getInstance().appStoreSaving(passport, req.getChannel(), req.getServername(), req.getReceipt(),
							req.getDeviceCode(), req.getOthers());
				}

				if (orderFormId == null) {
					result = 1;
					error = "生成订单失败";
				}
			}
			if (logger.isInfoEnabled()) {
				logger.info("[玩家充值提交] [结果:" + (result == 0 ? "成功" : "失败") + "] [error:" + error + "] [username:" + username + "] [当前客户端渠道:" + req.getChannel()
						+ "] [server:" + req.getServername() + "] [平台:AppStore] [Receipt:" + req.getReceipt() + "]");
			}
			APPSTORE_RECEIPT_NEW_RES res = new APPSTORE_RECEIPT_NEW_RES(req.getSequnceNum(), (byte) result);
			return res;
		}

		else if (message instanceof SAVING_RECORD_REQ) {
			SAVING_RECORD_REQ req = (SAVING_RECORD_REQ) message;
			String userName = req.getUserName();
			int pageNum = req.getPageNum();
			int pageIndex = req.getPageIndex();
			Passport passport = PassportManager.getInstance().getPassport(userName);
			if (passport != null) {
				OrderFormManager om = OrderFormManager.getInstance();
				long count = om.getUserOrderFormCount(passport.getId());
				long pnum = count / pageNum;
				if (count % pageNum != 0) {
					pnum++;
				}
				List<OrderForm> list = om.getUserOrderForms(passport.getId(), pageIndex * pageNum, pageNum);
				String statusDesp[] = new String[list.size()];
				String orderIds[] = new String[list.size()];
				long createTime[] = new long[list.size()];
				long savingAmount[] = new long[list.size()];
				String savingMedium[] = new String[list.size()];
				for (int i = 0; i < list.size(); i++) {
					OrderForm order = list.get(i);
					if (order.isNotifySucc()) {
						statusDesp[i] = "充值成功";
						if(order.getUserChannel() != null)
						{
							String channel = order.getUserChannel().toLowerCase();
							if(channel.contains("korea"))
							{
								statusDesp[i] = "충전성공";
							}
						}
					} else if (order.isNotified()) {
						statusDesp[i] = "充值异常，联系客服";
						if(order.getUserChannel() != null)
						{
							String channel = order.getUserChannel().toLowerCase();
							if(channel.contains("korea"))
							{
								statusDesp[i] = "충전이상,고객센터에 문의하세요";
							}
						}

					} else if (order.getResponseResult() == OrderForm.RESPONSE_SUCC) {
						statusDesp[i] = "充值已提交，请稍候";
						if(order.getUserChannel() != null)
						{
							String channel = order.getUserChannel().toLowerCase();
							if(channel.contains("korea"))
							{
								statusDesp[i] = "충전중,잠시 기다려주세요";
							}
						}

					} else if (order.getResponseResult() == OrderForm.RESPONSE_FAILED) {
						statusDesp[i] = "充值失败";
						if(order.getUserChannel() != null)
						{
							String channel = order.getUserChannel().toLowerCase();
							if(channel.contains("korea"))
							{
								statusDesp[i] = "충전실패";
							}
						}
					} else if (order.getHandleResult() == OrderForm.HANDLE_RESULT_SUCC) {
						statusDesp[i] = "您的充值已经提交，请您耐心等待验证结果，如果充值失败，不会扣除您的任何费用。";
						if(order.getUserChannel() != null)
						{
							String channel = order.getUserChannel().toLowerCase();
							if(channel.contains("korea"))
							{
								statusDesp[i] = "충전중,잠시 기다려주세요";
							}
						}
					} else if (order.getHandleResult() == OrderForm.HANDLE_RESULT_NOBACK) {
						statusDesp[i] = "订单已生成，等待支付";
						if(order.getUserChannel() != null)
						{
							String channel = order.getUserChannel().toLowerCase();
							if(channel.contains("korea"))
							{
								statusDesp[i] = " 주문생성, 결제해주세요";
							}
						}
					} 
					else if (order.getResponseResult() == OrderForm.BACK_MONEY_SUCC) {
						statusDesp[i] = "扣除已处理";
						if(order.getUserChannel() != null)
						{
							String channel = order.getUserChannel().toLowerCase();
							if(channel.contains("korea"))
							{
								statusDesp[i] = "회수 완료";
							}
						}
					}


					else {
						statusDesp[i] = "充值失败";
						if(order.getUserChannel() != null)
						{
							String channel = order.getUserChannel().toLowerCase();
							if(channel.contains("korea"))
							{
								statusDesp[i] = "충전실패";
							}
						}
					}
					orderIds[i] = order.getOrderId();
					createTime[i] = order.getCreateTime();
					savingAmount[i] = order.getPayMoney();

					if (order.getUserChannel() != null && order.getUserChannel().toLowerCase().contains("malai")) {
						savingAmount[i] = order.getPayMoney() / getFactor(order.getUserChannel());
					}

					savingMedium[i] = order.getSavingMedium();
					if (order.getSavingPlatform().equals("支付宝")) {
						savingMedium[i] = "支付宝";
					}
				}
				if (logger.isInfoEnabled()) {
					logger.info("[充值记录查询] [成功] [username:" + userName + "] [pernum:" + pageNum + "] [pageIndex:" + pageIndex + "] [count:" + count + "] [pnum:"
							+ pnum + "] [" + (System.currentTimeMillis() - startTime) + "ms]");
				}
				SAVING_RECORD_RES res = new SAVING_RECORD_RES(req.getSequnceNum(), statusDesp, orderIds, createTime, savingAmount, savingMedium, count, pnum);
				return res;
			}
			if (logger.isInfoEnabled()) {
				logger.info("[充值记录查询] [失败：用户不存在] [username:" + userName + "]");
			}
			SAVING_RECORD_RES res = new SAVING_RECORD_RES(req.getSequnceNum(), new String[0], new String[0], new long[0], new long[0], new String[0], 0L, 0L);
			return res;
		} else if (message instanceof ALIPAY_ARGS_REQ) {
			ALIPAY_ARGS_REQ req = (ALIPAY_ARGS_REQ) message;
			if (logger.isDebugEnabled()) {
				logger.debug("[获得支付宝参数]");
			}
			return new ALIPAY_ARGS_RES(req.getSequnceNum(), AlipaySavingManager.partnerID, AlipaySavingManager.sellerID, AlipaySavingManager.aliPublicKey,
					AlipaySavingManager.ownPublicKey, AlipaySavingManager.ownPrivateKey, AlipaySavingManager.notifyUrl);
		} else if (message instanceof ALIPAY_GET_ORDERID_REQ) {
			ALIPAY_GET_ORDERID_REQ req = (ALIPAY_GET_ORDERID_REQ) message;
			String username = req.getUsername();
			String channel = req.getChannel();
			long addAmount = req.getPayAmount();
			Passport passport = PassportManager.getInstance().getPassport(username);
			if (passport == null) {
				logger.warn("[生成支付宝订单] [失败:用户不存在] [username:" + username + "] [channel:" + channel + "] [addAmount:" + addAmount + "] ["
						+ (System.currentTimeMillis() - startTime) + "ms]");
				return new ALIPAY_GET_ORDERID_RES(req.getSequnceNum(), "", "", "");
			}
			String ucWapPayChannels[] = new String[] { "UC_MIESHI", "UC01_MIESHI", "UC02_MIESHI", "UC03_MIESHI", "UC04_MIESHI", "UC05_MIESHI", "UC06_MIESHI",
					"UC07_MIESHI", "UC08_MIESHI", "UC09_MIESHI", "UC10_MIESHI", "UC11_MIESHI", "UC12_MIESHI", "UC13_MIESHI", "UC14_MIESHI", "UC15_MIESHI",
					"UC16_MIESHI", "UC17_MIESHI", "UC18_MIESHI", "UC19_MIESHI", "UC20_MIESHI" };
			String feiliuWapPayChannels[] = new String[] { "feiliu_MIESHI", "feiliu1_MIESHI", "feiliu2_MIESHI", "feiliu3_MIESHI", "feiliu4_MIESHI",
					"feiliu5_MIESHI", "feiliu6_MIESHI", "feiliu7_MIESHI", "feiliu8_MIESHI", "feiliu9_MIESHI", "feiliu10_MIESHI", "feiliu11_MIESHI",
					"feiliu12_MIESHI", "feiliu13_MIESHI", "feiliu14_MIESHI", "feiliu15_MIESHI", "feiliu16_MIESHI", "feiliu17_MIESHI", "feiliu18_MIESHI",
					"feiliu19_MIESHI", "feiliu20_MIESHI" };
			String qihooWapPayChannels[] = new String[] { "360JIEKOU_MIESHI", "360JIEKOU01_MIESHI", "360JIEKOU02_MIESHI", "360JIEKOU03_MIESHI",
					"360JIEKOU04_MIESHI", "360JIEKOU05_MIESHI", "360JIEKOU06_MIESHI", "360JIEKOU07_MIESHI", "360JIEKOU08_MIESHI", "360JIEKOU09_MIESHI",
					"360JIEKOU10_MIESHI", "360JIEKOU11_MIESHI", "360JIEKOU12_MIESHI", "360JIEKOU13_MIESHI", "360JIEKOU14_MIESHI", "360JIEKOU15_MIESHI",
					"360JIEKOU16_MIESHI", "360JIEKOU17_MIESHI", "360JIEKOU18_MIESHI", "360JIEKOU19_MIESHI", "360JIEKOU20_MIESHI" };
			String duokuAliPayChannels[] = new String[] { "duokuApi_MIESHI", "duokuApi01_MIESHI", "duokuApi02_MIESHI", "duokuApi03_MIESHI",
					"duokuApi04_MIESHI", "duokuApi05_MIESHI", "duokuApi06_MIESHI", "duokuApi07_MIESHI", "duokuApi08_MIESHI", "duokuApi09_MIESHI",
					"duokuApi10_MIESHI", "duokuApi11_MIESHI", "duokuApi12_MIESHI", "duokuApi13_MIESHI", "duokuApi14_MIESHI", "duokuApi15_MIESHI",
					"duokuApi16_MIESHI", "duokuApi17_MIESHI", "duokuApi18_MIESHI", "duokuApi19_MIESHI", "duokuApi20_MIESHI" };
			String duokuYYAliPayChannels[] = new String[] { "BAIDUYY_MIESHI", "BAIDUYY01_MIESHI", "BAIDUYY02_MIESHI", "BAIDUYY03_MIESHI", "BAIDUYY04_MIESHI",
					"BAIDUYY05_MIESHI", "BAIDUYY06_MIESHI", "BAIDUYY07_MIESHI", "BAIDUYY08_MIESHI", "BAIDUYY09_MIESHI", "BAIDUYY10_MIESHI", "BAIDUYY11_MIESHI",
					"BAIDUYY12_MIESHI", "BAIDUYY13_MIESHI", "BAIDUYY14_MIESHI", "BAIDUYY15_MIESHI", "BAIDUYY16_MIESHI", "BAIDUYY17_MIESHI", "BAIDUYY18_MIESHI",
					"BAIDUYY19_MIESHI", "BAIDUYY20_MIESHI" };

			boolean uc = false;
			for (String s : ucWapPayChannels) {
				if (s.equals(channel)) {
					uc = true;
					break;
				}
			}
			boolean feiliu = false;
			for (String s : feiliuWapPayChannels) {
				if (s.equals(channel)) {
					feiliu = true;
					break;
				}
			}
			boolean qihoo = false;
			for (String s : qihooWapPayChannels) {
				if (s.equals(channel)) {
					qihoo = true;
					break;
				}
			}
			boolean duoku = false;
//			for (String s : duokuAliPayChannels) {
//				if (s.equals(channel)) {
//					duoku = true;
//					break;
//				}
//			}

			boolean duokuyy = false;
//			for (String s : duokuYYAliPayChannels) {
//				if (s.equals(channel)) {
//					duokuyy = true;
//					break;
//				}
//			}

			// 支付宝暂时没有传os，先取passport的lastLoginMobileOs
			String mobileOs = passport.getLastLoginMobileOs();
			if (mobileOs == null || mobileOs.length() == 0) {
				mobileOs = "Android";
			}
			if (uc) {
				String infos = PlatformSavingCenter.getInstance().cardSaving(passport, "UCWap支付", (int) addAmount, "", "", req.getServername(), channel, "",
						"", mobileOs);
				if (logger.isInfoEnabled()) {
					logger.info("[生成UCWap支付订单] [成功] [username:" + username + "] [channel:" + channel + "] [addAmount:" + addAmount + "] [orderInfo:" + infos
							+ "] [" + (System.currentTimeMillis() - startTime) + "ms]");
				}
				String str[] = infos.split("@@");
				return new ALIPAY_GET_ORDERID_RES(req.getSequnceNum(), str[0], str.length >= 2 ? str[1] : "", str.length >= 3 ? str[2] : "");
			} else if (feiliu) {
				String infos = FeiLiuAliPaySavingManager.getInstance().aliSaving(passport, (int) addAmount, req.getServername(), mobileOs, channel);
				if (logger.isInfoEnabled()) {
					logger.info("[生成飞流支付宝支付订单] [成功] [username:" + username + "] [channel:" + channel + "] [addAmount:" + addAmount + "] [orderInfo:" + infos
							+ "] [" + (System.currentTimeMillis() - startTime) + "ms]");
				}
				String str[] = infos.split("@@");
				return new ALIPAY_GET_ORDERID_RES(req.getSequnceNum(), str[0], str.length >= 2 ? str[1] : "", str.length >= 3 ? str[2] : "");
			} else if (qihoo) {
				String infos = QihooAliPaySavingManager.getInstance().aliSaving(passport, (int) addAmount, req.getServername(), mobileOs, channel);
				if (logger.isInfoEnabled()) {
					logger.info("[生成奇虎支付宝支付订单] [成功] [username:" + username + "] [channel:" + channel + "] [addAmount:" + addAmount + "] [orderInfo:" + infos
							+ "] [" + (System.currentTimeMillis() - startTime) + "ms]");
				}
				String str[] = infos.split("@@");
				return new ALIPAY_GET_ORDERID_RES(req.getSequnceNum(), str[0], str.length >= 2 ? str[1] : "", str.length >= 3 ? str[2] : "");
			} else if (duoku) {
				String infos = DuokuAlipaySavingManager.getInstance().cardSaving(passport, "支付宝", (int) addAmount, "", "", req.getServername(), mobileOs,
						channel);
				if (logger.isInfoEnabled()) {
					logger.info("[生成多酷支付宝支付订单] [成功] [username:" + username + "] [channel:" + channel + "] [addAmount:" + addAmount + "] [orderInfo:" + infos
							+ "] [" + (System.currentTimeMillis() - startTime) + "ms]");
				}
				String str[] = infos.split("@@");
				return new ALIPAY_GET_ORDERID_RES(req.getSequnceNum(), str[0], str.length >= 2 ? str[1] : "", str.length >= 3 ? str[2] : "");
			} else if (duokuyy) {
				String infos = DuokuYYAlipaySavingManager.getInstance().cardSaving(passport, "支付宝", (int) addAmount, "", "", req.getServername(), mobileOs,
						channel);
				if (logger.isInfoEnabled()) {
					logger.info("[生成多酷影音支付宝支付订单] [成功] [username:" + username + "] [channel:" + channel + "] [addAmount:" + addAmount + "] [orderInfo:" + infos
							+ "] [" + (System.currentTimeMillis() - startTime) + "ms]");
				}
				String str[] = infos.split("@@");
				return new ALIPAY_GET_ORDERID_RES(req.getSequnceNum(), str[0], str.length >= 2 ? str[1] : "", str.length >= 3 ? str[2] : "");
			} else {
				String infos = PlatformSavingCenter.getInstance().cardSaving(passport, "支付宝", (int) addAmount, "", "", req.getServername(), channel, "", "",
						mobileOs);
				if (logger.isInfoEnabled()) {
					logger.info("[生成支付宝订单] [成功] [username:" + username + "] [channel:" + channel + "] [addAmount:" + addAmount + "] [orderInfo:" + infos
							+ "] [" + (System.currentTimeMillis() - startTime) + "ms]");
				}
				String str[] = null;
				if (!StringUtils.isEmpty(infos)) {
					str = infos.split("@@");
				} else {
					str = new String[3];
					str[0] = "";
					str[1] = "";
					str[2] = "";
				}
				return new ALIPAY_GET_ORDERID_RES(req.getSequnceNum(), str[0], str[1], str.length >= 3 ? str[2] : "");
			}
		} else if (message instanceof ALIPAY_GET_ORDERID_NEW_REQ) {
			ALIPAY_GET_ORDERID_NEW_REQ req = (ALIPAY_GET_ORDERID_NEW_REQ) message;
			String username = req.getUsername();
			String channel = req.getChannel();
			long addAmount = req.getPayAmount();
			Passport passport = PassportManager.getInstance().getPassport(username);
			if (passport == null) {
				logger.warn("[生成支付宝订单] [失败:用户不存在] [username:" + username + "] [channel:" + channel + "] [addAmount:" + addAmount + "] ["
						+ (System.currentTimeMillis() - startTime) + "ms]");
				return new ALIPAY_GET_ORDERID_RES(req.getSequnceNum(), "", "", "");
			}
			String ucWapPayChannels[] = new String[] { "UC_MIESHI", "UC01_MIESHI", "UC02_MIESHI", "UC03_MIESHI", "UC04_MIESHI", "UC05_MIESHI", "UC06_MIESHI",
					"UC07_MIESHI", "UC08_MIESHI", "UC09_MIESHI", "UC10_MIESHI", "UC11_MIESHI", "UC12_MIESHI", "UC13_MIESHI", "UC14_MIESHI", "UC15_MIESHI",
					"UC16_MIESHI", "UC17_MIESHI", "UC18_MIESHI", "UC19_MIESHI", "UC20_MIESHI" };
			String feiliuWapPayChannels[] = new String[] { "feiliu_MIESHI", "feiliu1_MIESHI", "feiliu2_MIESHI", "feiliu3_MIESHI", "feiliu4_MIESHI",
					"feiliu5_MIESHI", "feiliu6_MIESHI", "feiliu7_MIESHI", "feiliu8_MIESHI", "feiliu9_MIESHI", "feiliu10_MIESHI", "feiliu11_MIESHI",
					"feiliu12_MIESHI", "feiliu13_MIESHI", "feiliu14_MIESHI", "feiliu15_MIESHI", "feiliu16_MIESHI", "feiliu17_MIESHI", "feiliu18_MIESHI",
					"feiliu19_MIESHI", "feiliu20_MIESHI" };
			String qihooWapPayChannels[] = new String[] { "360JIEKOU_MIESHI", "360JIEKOU01_MIESHI", "360JIEKOU02_MIESHI", "360JIEKOU03_MIESHI",
					"360JIEKOU04_MIESHI", "360JIEKOU05_MIESHI", "360JIEKOU06_MIESHI", "360JIEKOU07_MIESHI", "360JIEKOU08_MIESHI", "360JIEKOU09_MIESHI",
					"360JIEKOU10_MIESHI", "360JIEKOU11_MIESHI", "360JIEKOU12_MIESHI", "360JIEKOU13_MIESHI", "360JIEKOU14_MIESHI", "360JIEKOU15_MIESHI",
					"360JIEKOU16_MIESHI", "360JIEKOU17_MIESHI", "360JIEKOU18_MIESHI", "360JIEKOU19_MIESHI", "360JIEKOU20_MIESHI" };
			String duokuAliPayChannels[] = new String[] { "duokuApi_MIESHI", "duokuApi01_MIESHI", "duokuApi02_MIESHI", "duokuApi03_MIESHI",
					"duokuApi04_MIESHI", "duokuApi05_MIESHI", "duokuApi06_MIESHI", "duokuApi07_MIESHI", "duokuApi08_MIESHI", "duokuApi09_MIESHI",
					"duokuApi10_MIESHI", "duokuApi11_MIESHI", "duokuApi12_MIESHI", "duokuApi13_MIESHI", "duokuApi14_MIESHI", "duokuApi15_MIESHI",
					"duokuApi16_MIESHI", "duokuApi17_MIESHI", "duokuApi18_MIESHI", "duokuApi19_MIESHI", "duokuApi20_MIESHI" };
			String duokuYYAliPayChannels[] = new String[] { "BAIDUYY_MIESHI", "BAIDUYY01_MIESHI", "BAIDUYY02_MIESHI", "BAIDUYY03_MIESHI", "BAIDUYY04_MIESHI",
					"BAIDUYY05_MIESHI", "BAIDUYY06_MIESHI", "BAIDUYY07_MIESHI", "BAIDUYY08_MIESHI", "BAIDUYY09_MIESHI", "BAIDUYY10_MIESHI", "BAIDUYY11_MIESHI",
					"BAIDUYY12_MIESHI", "BAIDUYY13_MIESHI", "BAIDUYY14_MIESHI", "BAIDUYY15_MIESHI", "BAIDUYY16_MIESHI", "BAIDUYY17_MIESHI", "BAIDUYY18_MIESHI",
					"BAIDUYY19_MIESHI", "BAIDUYY20_MIESHI" };

			boolean uc = false;
			for (String s : ucWapPayChannels) {
				if (s.equals(channel)) {
					uc = true;
					break;
				}
			}
			boolean feiliu = false;
			for (String s : feiliuWapPayChannels) {
				if (s.equals(channel)) {
					feiliu = true;
					break;
				}
			}
			boolean qihoo = false;
			for (String s : qihooWapPayChannels) {
				if (s.equals(channel)) {
					qihoo = true;
					break;
				}
			}
			boolean duoku = false;
//			for (String s : duokuAliPayChannels) {
//				if (s.equals(channel)) {
//					duoku = true;
//					break;
//				}
//			}

			boolean duokuyy = false;
//			for (String s : duokuYYAliPayChannels) {
//				if (s.equals(channel)) {
//					duokuyy = true;
//					break;
//				}
//			}

			// 支付宝暂时没有传os，先取passport的lastLoginMobileOs
			String mobileOs = passport.getLastLoginMobileOs();
			if (mobileOs == null || mobileOs.length() == 0) {
				mobileOs = "Android";
			}
			if (uc) {
				String infos = PlatformSavingCenter.getInstance().cardSaving(passport, "UCWap支付", (int) addAmount, "", "", req.getServername(), channel, "",
						"", mobileOs, req.getOthers());
				if (logger.isInfoEnabled()) {
					logger.info("[生成UCWap支付订单] [成功] [username:" + username + "] [channel:" + channel + "] [addAmount:" + addAmount + "] [orderInfo:" + infos
							+ "] [" + (System.currentTimeMillis() - startTime) + "ms]");
				}
				String str[] = infos.split("@@");
				return new ALIPAY_GET_ORDERID_NEW_RES(req.getSequnceNum(), str[0], str.length >= 2 ? str[1] : "", str.length >= 3 ? str[2] : "");
			} else if (feiliu) {
				String infos = FeiLiuAliPaySavingManager.getInstance().aliSaving(passport, (int) addAmount, req.getServername(), mobileOs, channel,
						req.getOthers());
				if (logger.isInfoEnabled()) {
					logger.info("[生成飞流支付宝支付订单] [成功] [username:" + username + "] [channel:" + channel + "] [addAmount:" + addAmount + "] [orderInfo:" + infos
							+ "] [" + (System.currentTimeMillis() - startTime) + "ms]");
				}
				String str[] = infos.split("@@");
				return new ALIPAY_GET_ORDERID_NEW_RES(req.getSequnceNum(), str[0], str.length >= 2 ? str[1] : "", str.length >= 3 ? str[2] : "");
			} else if (qihoo) {
				String infos = QihooAliPaySavingManager.getInstance().aliSaving(passport, (int) addAmount, req.getServername(), mobileOs, channel,
						req.getOthers());
				if (logger.isInfoEnabled()) {
					logger.info("[生成奇虎支付宝支付订单] [成功] [username:" + username + "] [channel:" + channel + "] [addAmount:" + addAmount + "] [orderInfo:" + infos
							+ "] [" + (System.currentTimeMillis() - startTime) + "ms]");
				}
				String str[] = infos.split("@@");
				return new ALIPAY_GET_ORDERID_NEW_RES(req.getSequnceNum(), str[0], str.length >= 2 ? str[1] : "", str.length >= 3 ? str[2] : "");
			} else if (duoku) {
				String infos = DuokuAlipaySavingManager.getInstance().cardSaving(passport, "支付宝", (int) addAmount, "", "", req.getServername(), mobileOs,
						channel, req.getOthers());
				if (logger.isInfoEnabled()) {
					logger.info("[生成多酷支付宝支付订单] [成功] [username:" + username + "] [channel:" + channel + "] [addAmount:" + addAmount + "] [orderInfo:" + infos
							+ "] [" + (System.currentTimeMillis() - startTime) + "ms]");
				}
				String str[] = infos.split("@@");
				return new ALIPAY_GET_ORDERID_NEW_RES(req.getSequnceNum(), str[0], str.length >= 2 ? str[1] : "", str.length >= 3 ? str[2] : "");
			} else if (duokuyy) {
				String infos = DuokuYYAlipaySavingManager.getInstance().cardSaving(passport, "支付宝", (int) addAmount, "", "", req.getServername(), mobileOs,
						channel, req.getOthers());
				if (logger.isInfoEnabled()) {
					logger.info("[生成多酷影音支付宝支付订单] [成功] [username:" + username + "] [channel:" + channel + "] [addAmount:" + addAmount + "] [orderInfo:" + infos
							+ "] [" + (System.currentTimeMillis() - startTime) + "ms]");
				}
				String str[] = infos.split("@@");
				return new ALIPAY_GET_ORDERID_NEW_RES(req.getSequnceNum(), str[0], str.length >= 2 ? str[1] : "", str.length >= 3 ? str[2] : "");
			} else {
				String infos = PlatformSavingCenter.getInstance().cardSaving(passport, "支付宝", (int) addAmount, "", "", req.getServername(), channel, "", "",
						mobileOs, req.getOthers());
				if (logger.isInfoEnabled()) {
					logger.info("[生成支付宝订单] [成功] [username:" + username + "] [channel:" + channel + "] [addAmount:" + addAmount + "] [orderInfo:" + infos
							+ "] [" + (System.currentTimeMillis() - startTime) + "ms]");
				}
				String str[] = null;
				if (!StringUtils.isEmpty(infos)) {
					str = infos.split("@@");
				} else {
					str = new String[3];
					str[0] = "";
					str[1] = "";
					str[2] = "";
				}
				return new ALIPAY_GET_ORDERID_NEW_RES(req.getSequnceNum(), str[0], str[1], str.length >= 3 ? str[2] : "");
			}
		}

		else if (message instanceof NINEONE_SAVING_REQ) {
			// //结果,0-成功, 1-uid不存在 2-订单生成失败 3-服务器不存在
			NINEONE_SAVING_REQ req = (NINEONE_SAVING_REQ) message;
			String username = req.getUserName();
			String channel = req.getChannel();
			long addAmount = req.getPayAmount();
			Passport passport = PassportManager.getInstance().getPassport(username);
			if (passport == null) {
				logger.warn("[生成91订单] [失败:用户不存在] [username:" + username + "] [channel:" + channel + "] [addAmount:" + addAmount + "] ["
						+ (System.currentTimeMillis() - startTime) + "ms]");
				return new NINEONE_SAVING_RES(req.getSequnceNum(), "", (byte) 1, "用户不存在");
			}
			String orderID = PlatformSavingCenter.getInstance().cardSaving(passport, "", (int) addAmount, "", "", req.getServerName(), channel, "", "",
					req.getMobileOs());
			if (logger.isInfoEnabled()) {
				logger.info("[生成91订单] [成功] [username:" + username + "] [channel:" + channel + "] [addAmount:" + addAmount + "] [orderID:" + orderID + "] ["
						+ (System.currentTimeMillis() - startTime) + "ms]");
			}
			return new NINEONE_SAVING_RES(req.getSequnceNum(), orderID == null ? "" : orderID, (byte) 0, "");
		} else if (message instanceof NINEONE_SAVING_NEW_REQ) {
			// //结果,0-成功, 1-uid不存在 2-订单生成失败 3-服务器不存在
			NINEONE_SAVING_NEW_REQ req = (NINEONE_SAVING_NEW_REQ) message;
			String username = req.getUserName();
			String channel = req.getChannel();
			long addAmount = req.getPayAmount();
			Passport passport = PassportManager.getInstance().getPassport(username);
			if (passport == null) {
				logger.warn("[生成91订单] [失败:用户不存在] [username:" + username + "] [channel:" + channel + "] [addAmount:" + addAmount + "] ["
						+ (System.currentTimeMillis() - startTime) + "ms]");
				return new NINEONE_SAVING_NEW_RES(req.getSequnceNum(), "", (byte) 1, "用户不存在");
			}
			String orderID = PlatformSavingCenter.getInstance().cardSaving(passport, "", (int) addAmount, "", "", req.getServerName(), channel, "", "",
					req.getMobileOs(), req.getOthers());
			if (logger.isInfoEnabled()) {
				logger.info("[生成91订单] [成功] [username:" + username + "] [channel:" + channel + "] [addAmount:" + addAmount + "] [orderID:" + orderID + "] ["
						+ (System.currentTimeMillis() - startTime) + "ms]");
			}
			return new NINEONE_SAVING_NEW_RES(req.getSequnceNum(), orderID == null ? "" : orderID, (byte) 0, "");
		}

		else if (message instanceof APPCHINA_SAVING_REQ) {
			APPCHINA_SAVING_REQ req = (APPCHINA_SAVING_REQ) message;
			APPCHINA_SAVING_RES res = null;
			// 接受请求 抽出参数
			String userName = req.getUserName();
			String serverName = req.getServerName();

			// 验证用户合法性
			Passport user = passportManager.getPassport(userName);

			if (user != null) {
				String orderID = PlatformSavingCenter.getInstance().cardSaving(user, "", req.getPayAmount(), "", "", req.getServerName(), req.getChannel(), "",
						"", req.getMobileOs());
				// 将订单id放入Reseponse中返回
				res = new APPCHINA_SAVING_RES(req.getSequnceNum(), orderID == null ? "" : orderID, (byte) 0, "生成订单成功！");
				if (logger.isInfoEnabled()) {
					logger.info("[APPCHINA生成订单] [成功] [username:" + userName + "] [channel:" + req.getChannel() + "] [addAmount:" + req.getPayAmount()
							+ "] [orderID:" + orderID + "] [" + (System.currentTimeMillis() - startTime) + "ms]");
				}
				return res;
			} else {
				logger.error("[" + conn.getName() + "] [应用汇充值订单生成] [失败：用户不存在] [用户名:" + req.getUserName() + "] [充值类型:" + req.getPayType() + "] [充值金额:"
						+ req.getPayAmount() + "] [充值卡号:" + req.getCardNo() + "] [充值卡密码:" + req.getCardPassword() + "] [游戏服务器名称:" + serverName + "] [手机平台:"
						+ req.getMobileOs() + "] [充值平台:" + req.getChannel() + "] [costs:" + (System.currentTimeMillis() - startTime) + "ms]");
				res = new APPCHINA_SAVING_RES(req.getSequnceNum(), "", (byte) 1, "生成订单失败，用户不存在！");
				return res;
			}

		} else if (message instanceof APPCHINA_SAVING_NEW_REQ) {
			APPCHINA_SAVING_NEW_REQ req = (APPCHINA_SAVING_NEW_REQ) message;
			APPCHINA_SAVING_NEW_RES res = null;
			// 接受请求 抽出参数
			String userName = req.getUserName();
			String serverName = req.getServerName();

			// 验证用户合法性
			Passport user = passportManager.getPassport(userName);

			if (user != null) {
				String orderID = PlatformSavingCenter.getInstance().cardSaving(user, "", req.getPayAmount(), "", "", req.getServerName(), req.getChannel(), "",
						"", req.getMobileOs(), req.getOthers());
				// 将订单id放入Reseponse中返回
				res = new APPCHINA_SAVING_NEW_RES(req.getSequnceNum(), orderID == null ? "" : orderID, (byte) 0, "生成订单成功！");
				if (logger.isInfoEnabled()) {
					logger.info("[APPCHINA生成订单] [成功] [username:" + userName + "] [channel:" + req.getChannel() + "] [addAmount:" + req.getPayAmount()
							+ "] [orderID:" + orderID + "] [" + (System.currentTimeMillis() - startTime) + "ms]");
				}
				return res;
			} else {
				logger.error("[" + conn.getName() + "] [应用汇充值订单生成] [失败：用户不存在] [用户名:" + req.getUserName() + "] [充值类型:" + req.getPayType() + "] [充值金额:"
						+ req.getPayAmount() + "] [充值卡号:" + req.getCardNo() + "] [充值卡密码:" + req.getCardPassword() + "] [游戏服务器名称:" + serverName + "] [手机平台:"
						+ req.getMobileOs() + "] [充值平台:" + req.getChannel() + "] [costs:" + (System.currentTimeMillis() - startTime) + "ms]");
				res = new APPCHINA_SAVING_NEW_RES(req.getSequnceNum(), "", (byte) 1, "生成订单失败，用户不存在！");
				return res;
			}

		} else if (message instanceof ORDER_STATUS_CHANGE_REQ) {
			ORDER_STATUS_CHANGE_REQ req = (ORDER_STATUS_CHANGE_REQ) message;
			OrderForm order = OrderFormManager.getInstance().getOrderForm(req.getOrderId());
			if (order != null && order.getResponseResult() == OrderForm.RESPONSE_NOBACK && req.getStatusDesp().length() > 0) {
				
				//豌豆荚做了特殊处理 因为他们的sdk会在回调之前就发消息
				if(!order.getUserChannel().toLowerCase().contains("wandoujia"))
				{
				
					order.setHandleResult(Integer.valueOf(req.getStatus()));
					order.setHandleResultDesp(req.getStatusDesp());
				}
				try {
					OrderFormManager.getInstance().update(order);
					if (logger.isInfoEnabled()) {
						logger.info("[更新订单状态成功] [订单号:" + req.getOrderId() + "] [渠道:" + req.getChannel() + "] [状态码:" + req.getStatus() + "] [状态描述:"
								+ req.getStatusDesp() + "]");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("[更新订单状态失败] [订单号:" + req.getOrderId() + "] [渠道:" + req.getChannel() + "] [状态码:" + req.getStatus() + "] [状态描述:"
							+ req.getStatusDesp() + "]");
				}
			}
			return new ORDER_STATUS_CHANGE_RES(req.getSequnceNum(), 0);
		}  else if (message instanceof MORE_ARGS_ORDER_STATUS_CHANGE_REQ) {
			MORE_ARGS_ORDER_STATUS_CHANGE_REQ req = (MORE_ARGS_ORDER_STATUS_CHANGE_REQ) message;
			OrderForm order = OrderFormManager.getInstance().getOrderForm(req.getOrderId());
			if (order != null && order.getResponseResult() == OrderForm.RESPONSE_NOBACK && req.getStatusDesp().length() > 0) {
				try {
					if(order.getUserChannel().toLowerCase().contains("qqysdk_xunxian")){
						MSDKSavingManager msdk = MSDKSavingManager.getInstance();
						if(req.getArgs() != null && req.getArgs().length == 5){
							String type = req.getArgs()[4];
							int golds = -1;
							if(order.getUserChannel().toLowerCase().contains("qqysdk_xunxian")){
								golds = msdk.queryYSDKGameGold(ip,req.getChannel(), req.getArgs()[0], req.getArgs()[3],req.getArgs()[2], req.getPay_token(), req.getZoneid(), req.getPfkey());
							}else{
								golds = msdk.queryGameGold(req.getChannel(), req.getArgs()[0], req.getArgs()[3],req.getArgs()[2], req.getPay_token(), req.getZoneid(), req.getPfkey());
							}
							long firstGolds = order.getPayMoney();
							logger.info("[多参数,更新订单状态] [MORE_ARGS_ORDER_STATUS_CHANGE_REQ] [查询余额成功] [type:"+type+"] [firstGolds:"+firstGolds+"] [golds:"+golds+"] ["+(req.getArgs()==null?"null":Arrays.toString(req.getArgs()))+"] [channel:"+(order!=null?order.getUserChannel():"nul")+"] [条件一:"+(order != null)+"] [条件二:"+(order.getResponseResult())+"] [条件三:"+(req.getStatusDesp().length())+"] ");
							if(golds > 0){
								if(type.equals("MSDK兑换")){
									if(firstGolds/100 > golds){
										return new MORE_ARGS_ORDER_STATUS_CHANGE_RES(req.getSequnceNum(), "MSDK兑换error####余额不足,当前余额:"+golds);
									}
									if(req.getArgs()[1].equals("true")){
										return new MORE_ARGS_ORDER_STATUS_CHANGE_RES(req.getSequnceNum(), "MSDK兑换ok####"+(firstGolds/100)+"####"+req.getArgs()[3]+"####"+req.getArgs()[2]+"####"+req.getPay_token()+"####"+req.getZoneid()+"####"+req.getPfkey()+"####"+order.getUserChannel());
									}
									String resultstr = "";
									resultstr = msdk.costTencentGlods(req.getChannel(), req.getArgs()[0], req.getArgs()[3],req.getArgs()[2],req.getPay_token(), req.getZoneid(), req.getPfkey(), firstGolds/100+"");
									if(resultstr != null && resultstr.equals("ok")){
										order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
										order.setResponseTime(System.currentTimeMillis());
										order.setResponseResult(OrderForm.RESPONSE_SUCC);
										OrderFormManager.getInstance().update(order);
									}else{
										return new MORE_ARGS_ORDER_STATUS_CHANGE_RES(req.getSequnceNum(), "MSDK兑换error####"+resultstr);
									}
								}else if(type.equals("MSDK充值")){
									if(req.getArgs()[1].equals("true")){
										return new MORE_ARGS_ORDER_STATUS_CHANGE_RES(req.getSequnceNum(), "MSDK充值ok####"+golds+"####"+req.getArgs()[3]+"####"+req.getArgs()[2]+"####"+req.getPay_token()+"####"+req.getZoneid()+"####"+req.getPfkey()+"####"+order.getUserChannel());
									}
									//全部兑换,本次充值的还是剩下的余额？
									String resultstr= "";
									resultstr = msdk.costTencentGlods(req.getChannel(), req.getArgs()[0], req.getArgs()[3],req.getArgs()[2],req.getPay_token(), req.getZoneid(), req.getPfkey(), golds+"");
									if(resultstr != null && resultstr.equals("ok")){
										order.setPayMoney(golds*100);
										order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
										order.setResponseTime(System.currentTimeMillis());
										order.setResponseResult(OrderForm.RESPONSE_SUCC);
										OrderFormManager.getInstance().update(order);
									}else{
										if (logger.isInfoEnabled()) {
											logger.info("[多参数,更新订单状态] [失败] [MSDK更新充值金额] ["+type+"] [resultstr:"+resultstr+"] [原始金额:"+firstGolds+"(两)] [msdk查询游戏币:"+golds+"] [订单号:" + req.getOrderId() + "] [渠道:" + req.getChannel() + "] [状态码:" + req.getStatus() + "] [状态描述:"+ req.getStatusDesp() + "] ["+(req.getArgs()==null?"nul":Arrays.toString(req.getArgs()))+"]");
										}
									}
								}else if(type.equals("米大师兑换")){
									if(firstGolds/100 > golds){
										return new MORE_ARGS_ORDER_STATUS_CHANGE_RES(req.getSequnceNum(), "米大师兑换error####余额不足,当前余额:"+golds);
									}
									if(req.getArgs()[1].equals("true")){
										return new MORE_ARGS_ORDER_STATUS_CHANGE_RES(req.getSequnceNum(), "米大师兑换ok####"+(firstGolds/100)+"####"+req.getArgs()[3]+"####"+req.getArgs()[2]+"####"+req.getPay_token()+"####"+req.getZoneid()+"####"+req.getPfkey()+"####"+order.getUserChannel());
									}
									String resultstr = "";
									resultstr = msdk.costYSDKTencentGlods(req.getOrderId(), req.getChannel(), req.getArgs()[0], req.getArgs()[3],req.getArgs()[2],req.getPay_token(), req.getZoneid(), req.getPfkey(), firstGolds/100+"");
									if(resultstr != null && resultstr.equals("ok")){
										order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
										order.setResponseTime(System.currentTimeMillis());
										order.setResponseResult(OrderForm.RESPONSE_SUCC);
										OrderFormManager.getInstance().update(order);
									}else{
										return new MORE_ARGS_ORDER_STATUS_CHANGE_RES(req.getSequnceNum(), "米大师充值error####"+resultstr);
									}
								}else if(type.equals("米大师充值")){
									if(req.getArgs()[1].equals("true")){
										return new MORE_ARGS_ORDER_STATUS_CHANGE_RES(req.getSequnceNum(), "米大师充值ok####"+golds+"####"+req.getArgs()[3]+"####"+req.getArgs()[2]+"####"+req.getPay_token()+"####"+req.getZoneid()+"####"+req.getPfkey()+"####"+order.getUserChannel());
									}
									//全部兑换,本次充值的还是剩下的余额？
									String resultstr= "";
									resultstr = msdk.costYSDKTencentGlods(req.getOrderId(), req.getChannel(), req.getArgs()[0], req.getArgs()[3],req.getArgs()[2],req.getPay_token(), req.getZoneid(), req.getPfkey(), golds+"");
									if(resultstr != null && resultstr.equals("ok")){
										order.setPayMoney(golds*100);
										order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
										order.setResponseTime(System.currentTimeMillis());
										order.setResponseResult(OrderForm.RESPONSE_SUCC);
										OrderFormManager.getInstance().update(order);
									}else{
										if (logger.isInfoEnabled()) {
											logger.info("[多参数,更新订单状态] [失败] [YSDK更新充值金额] ["+type+"] [resultstr:"+resultstr+"] [原始金额:"+firstGolds+"(两)] [msdk查询游戏币:"+golds+"] [订单号:" + req.getOrderId() + "] [渠道:" + req.getChannel() + "] [状态码:" + req.getStatus() + "] [状态描述:"+ req.getStatusDesp() + "] ["+(req.getArgs()==null?"nul":Arrays.toString(req.getArgs()))+"]");
										}
									}
								}
							}else{
								return new MORE_ARGS_ORDER_STATUS_CHANGE_RES(req.getSequnceNum(), "MSDK兑换error####余额不足,当前余额:"+golds);
							}
							if (logger.isInfoEnabled()) {
								logger.info("[多参数,更新订单状态] [成功] [MSDK更新充值金额] ["+type+"] [原始金额:"+firstGolds+"(两)] [msdk查询游戏币:"+golds+"] [订单号:" + req.getOrderId() + "] [渠道:" + req.getChannel() + "] [状态码:" + req.getStatus() + "] [状态描述:"+ req.getStatusDesp() + "] ["+(req.getArgs()==null?"nul":Arrays.toString(req.getArgs()))+"]");
							}
						}else{
							return new MORE_ARGS_ORDER_STATUS_CHANGE_RES(req.getSequnceNum(), "多参数不正确");
						}
					}
				} catch (Exception e) {
					logger.error("[多参数,更新订单状态失败] [订单号:" + req.getOrderId() + "] [渠道:" + req.getChannel() + "] [状态码:" + req.getStatus() + "] [状态描述:"
							+ req.getStatusDesp() + "]");
				}
			}
			return new MORE_ARGS_ORDER_STATUS_CHANGE_RES(req.getSequnceNum(), "msdk更新订单异常");
		} else if (message instanceof ORDER_STATUS_NOTIFY_REQ) {
			ORDER_STATUS_NOTIFY_REQ req = (ORDER_STATUS_NOTIFY_REQ) message;
			OrderForm order = OrderFormManager.getInstance().getOrderForm(req.getOrderId());
			if (order != null && order.getResponseResult() == OrderForm.RESPONSE_NOBACK) {
				if (!StringUtils.isEmpty(req.getHandlestatus())) {
					order.setHandleResult(Integer.valueOf(req.getHandlestatus()));
				}
				if (!StringUtils.isEmpty(req.getHandlestatusDesp())) {
					order.setHandleResultDesp(req.getHandlestatusDesp());
				}

				if (!StringUtils.isEmpty(req.getResponsestatus())) {
					order.setResponseResult(Integer.valueOf(req.getResponsestatus()));
				}
				if (!StringUtils.isEmpty(req.getResponsestatusDesp())) {
					order.setResponseDesp(req.getResponsestatusDesp());
				}

				try {
					OrderFormManager.getInstance().update(order);
					if (logger.isInfoEnabled()) {
						logger.info("[更新订单状态成功] [订单号:" + req.getOrderId() + "] [渠道:" + req.getChannel() + "] [handleStatus:" + req.getHandlestatus()
								+ "] [handleStatusDesp:" + req.getHandlestatusDesp() + "] [reponseStatus:" + req.getResponsestatus() + "] [responseStatusResp:"
								+ req.getResponsestatusDesp() + "]");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("[更新订单状态失败] [订单号:" + req.getOrderId() + "] [渠道:" + req.getChannel() + "] [handleStatus:" + req.getHandlestatus()
							+ "] [handleStatusDesp:" + req.getHandlestatusDesp() + "] [reponseStatus:" + req.getResponsestatus() + "] [responseStatusResp:"
							+ req.getResponsestatusDesp() + "]");
				}
			}
			return new ORDER_STATUS_NOTIFY_RES(req.getSequnceNum(), 0);
		} else if (message instanceof SAVING_UCIDTOUSERNAME_REQ) {
			SAVING_UCIDTOUSERNAME_REQ req = (SAVING_UCIDTOUSERNAME_REQ) message;
			// 判断是否已经存在ucid对应关系
			UcidToUserNameDAO ucidToUserNameDAO = UcidToUserNameDAOImpl.getInstance();
			UcidToUserName uun = ucidToUserNameDAO.getByUcid(req.getUcid());

			if (uun != null) {
				if (logger.isInfoEnabled()) {
					logger.info("[" + conn.getName() + "] [创建ucid和游戏账号对应关系] [失败] [ucid和游戏账号对应关系已存在] [ucid:" + req.getUcid() + "] [游戏账号:" + req.getUsername()
							+ "] [渠道:" + req.getChannel() + "] [数据库中存在的对应关系(ucid<->游戏账号):" + uun.getUcid() + "<->" + uun.getUsername() + "] [数据库中存在的渠道:"
							+ uun.getUserChannel() + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
				}
				return new SAVING_UCIDTOUSERNAME_RES(req.getSequnceNum(), 1);
			}
			// 不存在则创建对应记录
			UcidToUserName ucidToUserName = new UcidToUserName();
			ucidToUserName.setCreateTime(System.currentTimeMillis());
			ucidToUserName.setStatus(UcidToUserName.NORMAL);
			ucidToUserName.setUcid(req.getUcid());
			ucidToUserName.setUsername(req.getUsername());
			ucidToUserName.setUserChannel(req.getChannel());
			ucidToUserNameDAO.saveNew(ucidToUserName);
			if (logger.isInfoEnabled()) {
				logger.info("[" + conn.getName() + "] [创建ucid和游戏账号对应关系] [失败] [ucid和游戏账号对应关系已存在] [ucid:" + req.getUcid() + "] [游戏账号:" + req.getUsername()
						+ "] [渠道:" + req.getChannel() + "] [数据库中存在的对应关系(ucid<->游戏账号):--] [数据库中存在的渠道:--] [cost:" + (System.currentTimeMillis() - startTime)
						+ "ms]");
			}
			return new SAVING_UCIDTOUSERNAME_RES(req.getSequnceNum(), 0);
		} else if (message instanceof GET_USERNAMEBYUCID_REQ) {
			GET_USERNAMEBYUCID_REQ req = (GET_USERNAMEBYUCID_REQ) message;
			// 判断是否已经存在ucid对应关系
			UcidToUserNameDAO ucidToUserNameDAO = UcidToUserNameDAOImpl.getInstance();
			UcidToUserName uun = ucidToUserNameDAO.getByUcid(req.getUcid());

			if (uun != null) {
				if (logger.isInfoEnabled()) {
					logger.info("[" + conn.getName() + "] [通过UCID查找游戏账号] [成功] [OK] [ucid:" + req.getUcid() + "] [游戏账号:" + uun.getUsername() + "] [渠道:"
							+ uun.getUserChannel() + "] [id:" + uun.getId() + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
				}
				return new GET_USERNAMEBYUCID_RES(req.getSequnceNum(), uun.getUsername());
			}
			// 不存在返回空
			if (logger.isInfoEnabled()) {
				logger.info("[" + conn.getName() + "] [通过UCID查找游戏账号] [失败] [对应ucid的记录不存在] [ucid:" + req.getUcid() + "] [游戏账号:--] [渠道:--] [id:--] [cost:"
						+ (System.currentTimeMillis() - startTime) + "ms]");
			}
			return new GET_USERNAMEBYUCID_RES(req.getSequnceNum(), "");
		} else if (message instanceof GET_UCIDBYUSERNAME_REQ) {
			GET_UCIDBYUSERNAME_REQ req = (GET_UCIDBYUSERNAME_REQ) message;
			// 判断是否已经存在ucid对应关系
			UcidToUserNameDAO ucidToUserNameDAO = UcidToUserNameDAOImpl.getInstance();
			UcidToUserName uun = ucidToUserNameDAO.getByUserName(req.getUsername());

			if (uun != null) {
				if (logger.isInfoEnabled()) {
					logger.info("[" + conn.getName() + "] [通过游戏账号查找UCID] [成功] [OK] [ucid:" + uun.getUcid() + "] [游戏账号:" + uun.getUsername() + "] [渠道:"
							+ uun.getUserChannel() + "] [id:" + uun.getId() + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
				}
				return new GET_UCIDBYUSERNAME_RES(req.getSequnceNum(), uun.getUcid());
			}
			// 不存在返回空
			if (logger.isInfoEnabled()) {
				logger.info("[" + conn.getName() + "] [通过游戏账号查找UCID] [失败] [对应游戏账号的记录不存在] [ucid:--] [游戏账号:--] [渠道:" + req.getUsername() + "] [id:--] [cost:"
						+ (System.currentTimeMillis() - startTime) + "ms]");
			}
			return new GET_UCIDBYUSERNAME_RES(req.getSequnceNum(), "");
		} else if (message instanceof GET_MOZUANINFO_REQ) {
			// //结果,0-成功, 1-uid不存在 2-订单生成失败 3-服务器不存在
			GET_MOZUANINFO_REQ req = (GET_MOZUANINFO_REQ) message;
			String username = req.getUsername();
			String clientString = req.getClientstring();
			String channel = req.getChannel();
			logger.info("[" + channel + "] [调用魔钻] [" + username + "] [" + channel + "] [" + clientString + "] [" + username + "]");

			QQUserInfo qqUserInfo = QQInfoService.getInstance().getQQUserInfoByUserName(username, clientString, channel);
			if (qqUserInfo == null) {
				logger.warn("[获取魔钻信息] [失败] [没有获取到魔钻信息] [username:" + username + "] [channel:" + channel + "] [clientString:" + clientString + "] ["
						+ (System.currentTimeMillis() - startTime) + "ms]");
				return new GET_MOZUANINFO_RES(req.getSequnceNum(), new QQUserInfo(), (byte) 1, "获取魔钻信息失败");
			}
			if (logger.isInfoEnabled()) {
				logger.info("[获取魔钻信息] [成功] [ok] [username:" + username + "] [channel:" + channel + "] [clientString:" + clientString + "] ["
						+ (System.currentTimeMillis() - startTime) + "ms]");
			}
			return new GET_MOZUANINFO_RES(req.getSequnceNum(), qqUserInfo, (byte) 0, "");
		} else if (message instanceof SEND_FUND_FOR_NINEONE_REQ) {
			SEND_FUND_FOR_NINEONE_REQ req = (SEND_FUND_FOR_NINEONE_REQ) message;

			int sum = -1;
			boolean isCanGive = checkCanGiveFund(req.getChannel());

			if (!isCanGive) {
				return new SEND_FUND_FOR_NINEONE_RES(req.getSequnceNum(), sum, -1, "不合法的积金渠道");
			}

			try {
				sum = sendFund4NineOne(req.getUsername(), req.getChannel(), req.getFundtype());

				return new SEND_FUND_FOR_NINEONE_RES(req.getSequnceNum(), sum, 0, "");

				// if(logger.isWarnEnabled())
				// logger.warn("["+Thread.currentThread().getName()+"] [接收充值通知]
				// [给91特定渠道用户返积金] ["+(ret == 0 ? "成功" :"失败")+"]
				// [充值前等级:"+beforeVip+"] [充值后等级:"+afterVip+"]
				// [角色名称:"+player.getName()+"]
				// [账户名称:"+player.getPassport().getUserName()+"]
				// [账户昵称:"+player.getPassport().getNickName()+"] [充值金额:"+rmb+"]
				// [所发银两:"+yinzi+"] [渠道:"+channel+"] [操作系统:"+os+"]
				// [最小vip等级:"+(MIN_VIP_LEVEL)+"] [最大vip等级:"+(MAX_VIP_LEVEL)+"]
				// [cost:"+(System.currentTimeMillis()-start)+"]");
			} catch (Exception e) {
				logger.error("[给91特定渠道用户返积金] [失败] [出现未知异常] [username:" + req.getUsername() + "] [channel:" + req.getChannel() + "] [fundtype:"
						+ req.getFundtype() + "]", e);
			}
			return new SEND_FUND_FOR_NINEONE_RES(req.getSequnceNum(), sum, -1, "出现未知异常");

		} else if(message instanceof GET_SERVER_URL_REQ) {
			GET_SERVER_URL_REQ req = (GET_SERVER_URL_REQ)message;
			XmlServerManager sm = XmlServerManager.getInstance();
			XmlServer server = sm.getXmlServerByName(req.getServername());
			String url = "";
			if(server != null) {
				url = server.getUri();
			}
			GET_SERVER_URL_RES res = new GET_SERVER_URL_RES(req.getSequnceNum(), url);
			if(logger.isInfoEnabled()) {
				logger.info("[获得服务器的url] ["+req.getServername()+"] [return:"+url+"]");
			}
			return res;
		} else if(message instanceof GET_USER_LAST_SAVING_TIME_REQ) {
			GET_USER_LAST_SAVING_TIME_REQ req = (GET_USER_LAST_SAVING_TIME_REQ)message;
			//			Passport passport = PassportManager.getInstance().getPassport(req.getUsername());
			//			if(passport == null) {
			//				logger.warn("[获得]");
			//			}
			//			List<OrderForm> list = OrderFormManager.getInstance().getOrderFormsByCondition(" ")
			//			GET_SERVER_URL_RES res = new GET_SERVER_URL_RES(req.getSequnceNum(), url);
			if(logger.isInfoEnabled()) {
				logger.info("[获得服务器的url:错误不做处理] ["+req.getUsername()+"]");
			}
			return null;
		}
		/**
		 * 此方法用来供游戏服使用 判断用户是否应该弹窗 调用此方法的条件应该在满足业务条件时调用
		 * 此方法是用来供游戏服判断是否弹窗的，同时收集vip相关等级用户的信息
		 * 逻辑如下:
		 * 	1.判断是否弹窗
		 * 	
		 * 	若一个用户从未在任何一个游戏服弹过窗口让其填写信息，则应该弹出窗口让玩家填写信息 并应该创建一条记录
		 * 	若一个用户在他玩的多个游戏服中曾经有一个游戏服，但不是当前服务器， 弹出过信息，则不用弹出信息，但要创建一条记录
		 * 	若一个用户在他玩的当前服中弹出过窗口，并收集过信息 则不用弹窗也不用创建记录
		 * 
		 * 	0 代表不用弹出窗口 1 代表要弹出窗口
		 * str[0] viplevel
		 * str[1] username
		 * str[2] platform
		 * str[3] playername
		 * str[4] playerlevel
		 * str[5] gamecountry
		 * str[6] server
		 */
		else if(message instanceof JUDGE_POP_WINDOW_FOR_VIP_REQ)
		{
			JUDGE_POP_WINDOW_FOR_VIP_REQ req = (JUDGE_POP_WINDOW_FOR_VIP_REQ) message;
			JUDGE_POP_WINDOW_FOR_VIP_RES res = null;
			int popWindow = NOT_POP_WINDOW_FOR_VIP;
			boolean isCreateVipRecord = true;
			String[] infos =  req.getInfos();
			String username = "";
			if(infos.length >= 5)
			{
				username = infos[1];
			}
			else
			{	
				res = new JUDGE_POP_WINDOW_FOR_VIP_RES(req.getSequnceNum(), popWindow);
				logger.warn("["+conn.getName()+"] ["+Thread.currentThread().getName()+"] ["+req.getType()+"] ["+req.getSequnceNum()+"] [判断是否弹出收集信息窗口] [失败] [请求中传入的参数不合要求] [传入参数的长度:"+infos.length+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
				return res;
			}

			//查询库中是否存在有此用户的记录 并查询出来形成一个列表
			String preparedWhereSql = " username = ? ";
			Object[] params = new Object[]{username};
			VipPlayerInfoManager vipPlayerInfoManager = VipPlayerInfoManager.getInstance();
			try {
				List<VipPlayerInfoRecord> lst = vipPlayerInfoManager.queryForWhere(preparedWhereSql,params);
				if(lst != null && lst.size() > 0)
				{
					//如果搜集过记录 则不用弹窗 但是要判断是否搜集信息
					String curServer = "";
					if(infos.length >= 7)
					{
						curServer = infos[6];
					}


					for(VipPlayerInfoRecord vipPlayerInfoRecord : lst)
					{
						if(curServer.equals(vipPlayerInfoRecord.getServerName()))
						{
							isCreateVipRecord = false;
							break;
						}
					}
				}
				else
				{
					popWindow = IS_POP_WINDOW_FOR_VIP;
					isCreateVipRecord = false;
				}

				if(isCreateVipRecord)
				{
					if(infos.length >=  7)
					{
						if(Integer.parseInt(infos[0]) > 7){
							VipPlayerInfoRecord newVipPlayerInfoRecord = new VipPlayerInfoRecord();
							if(popWindow == NOT_POP_WINDOW_FOR_VIP)
							{
								//则复制一份联系人信息
								VipPlayerInfoRecord vipPlayerInfoRecord = lst.get(0);
								
								newVipPlayerInfoRecord.setRealname(vipPlayerInfoRecord.getRealname());
								newVipPlayerInfoRecord.setPhone(vipPlayerInfoRecord.getPhone());
								newVipPlayerInfoRecord.setQq(vipPlayerInfoRecord.getQq());
								newVipPlayerInfoRecord.setBirthday(vipPlayerInfoRecord.getBirthday());
								
								newVipPlayerInfoRecord.setVipLevel(Integer.parseInt(infos[0]));
								newVipPlayerInfoRecord.setUsername(infos[1]);
								newVipPlayerInfoRecord.setPlatform(infos[2]);
								newVipPlayerInfoRecord.setPlayerName(infos[3]);
								newVipPlayerInfoRecord.setPlayerLevel(Integer.parseInt(infos[4]));
								newVipPlayerInfoRecord.setGameCountry(infos[5]);
								newVipPlayerInfoRecord.setServerName(infos[6]);
								
								newVipPlayerInfoRecord.setCreateTime(System.currentTimeMillis());
							}
							vipPlayerInfoManager.saveOrUpdate(newVipPlayerInfoRecord);
							if(logger.isInfoEnabled())
							{
								logger.info("["+conn.getName()+"] ["+Thread.currentThread().getName()+"] ["+req.getType()+"] ["+req.getSequnceNum()+"] [判断是否弹出收集信息窗口] [成功] [ok] [username:"+username+"] [server:"+infos[6]+"] [playername:"+infos[3]+"] [viplevel:"+infos[0]+"] [是否弹出窗口:"+(popWindow == IS_POP_WINDOW_FOR_VIP ? "是":"否")+"] [是否创建新记录:"+isCreateVipRecord+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
							}
						}else{
							VipNewPlayerInfoRecord newVipPlayerInfoRecord = new VipNewPlayerInfoRecord();
							if(popWindow == NOT_POP_WINDOW_FOR_VIP)
							{
								//则复制一份联系人信息
								VipPlayerInfoRecord vipPlayerInfoRecord = lst.get(0);
								
								newVipPlayerInfoRecord.setRealname(vipPlayerInfoRecord.getRealname());
								newVipPlayerInfoRecord.setPhone(vipPlayerInfoRecord.getPhone());
								newVipPlayerInfoRecord.setQq(vipPlayerInfoRecord.getQq());
								newVipPlayerInfoRecord.setBirthday(vipPlayerInfoRecord.getBirthday());
								
								newVipPlayerInfoRecord.setVipLevel(Integer.parseInt(infos[0]));
								newVipPlayerInfoRecord.setUsername(infos[1]);
								newVipPlayerInfoRecord.setPlatform(infos[2]);
								newVipPlayerInfoRecord.setPlayerName(infos[3]);
								newVipPlayerInfoRecord.setPlayerLevel(Integer.parseInt(infos[4]));
								newVipPlayerInfoRecord.setGameCountry(infos[5]);
								newVipPlayerInfoRecord.setServerName(infos[6]);
								
								newVipPlayerInfoRecord.setCreateTime(System.currentTimeMillis());
							}
							vipPlayerInfoManager.saveOrUpdate(newVipPlayerInfoRecord);
							if(logger.isInfoEnabled())
							{
								logger.info("[newVipInfo] ["+conn.getName()+"] ["+Thread.currentThread().getName()+"] ["+req.getType()+"] ["+req.getSequnceNum()+"] [判断是否弹出收集信息窗口] [成功] [ok] [username:"+username+"] [server:"+infos[6]+"] [playername:"+infos[3]+"] [viplevel:"+infos[0]+"] [是否弹出窗口:"+(popWindow == IS_POP_WINDOW_FOR_VIP ? "是":"否")+"] [是否创建新记录:"+isCreateVipRecord+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
							}
						}
					}
					else
					{
						logger.warn("["+conn.getName()+"] ["+Thread.currentThread().getName()+"] ["+req.getType()+"] ["+req.getSequnceNum()+"] [判断是否弹出收集信息窗口] [失败] [传入参数不符合规范] [username:"+username+"] [server:--] [playername:--] [viplevel:--] [是否弹出窗口:"+(popWindow == IS_POP_WINDOW_FOR_VIP ? "是":"否")+"] [是否创建新记录:"+isCreateVipRecord+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
					}

				}
				else
				{
					if(logger.isInfoEnabled())
					{
						logger.info("["+conn.getName()+"] ["+Thread.currentThread().getName()+"] ["+req.getType()+"] ["+req.getSequnceNum()+"] [判断是否弹出收集信息窗口] [成功] [ok] [username:"+username+"] [server:"+infos[6]+"] [playername:"+infos[3]+"] [viplevel:"+infos[0]+"] [是否弹出窗口:"+(popWindow == IS_POP_WINDOW_FOR_VIP ? "是":"否")+"] [是否创建新记录:"+isCreateVipRecord+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
					}
				}
			}
			catch (Exception e) {
				popWindow = NOT_POP_WINDOW_FOR_VIP;
				logger.warn("["+conn.getName()+"] ["+Thread.currentThread().getName()+"] ["+req.getType()+"] ["+req.getSequnceNum()+"] [判断是否弹出收集信息窗口] [失败] [出现异常] [username:"+username+"] [server:--] [playername:--] [viplevel:--] [是否弹出窗口:"+(popWindow == IS_POP_WINDOW_FOR_VIP ? "是":"否")+"] [是否创建新记录:"+isCreateVipRecord+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			}
			res = new JUDGE_POP_WINDOW_FOR_VIP_RES(req.getSequnceNum(), popWindow);
			return res;


		}
		else if(message instanceof CREATE_NEW_VIPINFORECORD_REQ)
		{

			CREATE_NEW_VIPINFORECORD_REQ req = (CREATE_NEW_VIPINFORECORD_REQ) message;
			CREATE_NEW_VIPINFORECORD_RES res = null;
			String[] infos =  req.getInfos();
			int result = 0;
			VipPlayerInfoManager vipPlayerInfoManager = VipPlayerInfoManager.getInstance();
			try {


				if(infos.length >=  10)
				{
					VipPlayerInfoRecord newVipPlayerInfoRecord = new VipPlayerInfoRecord();
					//从请求信息中拿
					newVipPlayerInfoRecord.setRealname(infos[0]); //姓名
					newVipPlayerInfoRecord.setQq(infos[1]); //qq
					newVipPlayerInfoRecord.setPhone(infos[2]); //联系方式
					newVipPlayerInfoRecord.setVipLevel(Integer.parseInt(infos[3]));
					newVipPlayerInfoRecord.setUsername(infos[4]);
					newVipPlayerInfoRecord.setPlatform(infos[5]);
					newVipPlayerInfoRecord.setPlayerName(infos[6]);
					newVipPlayerInfoRecord.setPlayerLevel(Integer.parseInt(infos[7]));
					newVipPlayerInfoRecord.setGameCountry(infos[8]);
					newVipPlayerInfoRecord.setServerName(infos[9]);
					newVipPlayerInfoRecord.setCreateTime(System.currentTimeMillis());
					if(infos.length >= 11)
					{
						newVipPlayerInfoRecord.setRecordType(VipPlayerInfoRecord.ORDER_VIP_RECORD);
					}
					else
					{
						newVipPlayerInfoRecord.setRecordType(VipPlayerInfoRecord.NORMAL_VIP_RECORD);
					}
					vipPlayerInfoManager.saveOrUpdate(newVipPlayerInfoRecord);
					if(logger.isInfoEnabled())
					{
						logger.info("["+conn.getName()+"] ["+Thread.currentThread().getName()+"] ["+req.getType()+"] ["+req.getSequnceNum()+"] [收集vip用户信息] [成功] [ok] [username:"+infos[0]+"] [server:"+infos[9]+"] [playername:"+infos[6]+"] [viplevel:"+infos[3]+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
					}
				}
				else
				{
					result = 1;
					logger.warn("["+conn.getName()+"] ["+Thread.currentThread().getName()+"] ["+req.getType()+"] ["+req.getSequnceNum()+"] [收集vip用户信息] [失败] [传入参数不符合规范] [username:"+(infos==null?"null":infos[0])+"] [server:--] [playername:--] [viplevel:--] [length:"+infos.length+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
				}
			} 
			catch (Exception e) {
				result = 1;
				logger.error("["+conn.getName()+"] ["+Thread.currentThread().getName()+"] ["+req.getType()+"] ["+req.getSequnceNum()+"] [收集vip用户信息] [失败] [出现未知异常] [username:"+(infos==null?"null":infos[0])+"] [server:"+(infos==null?"null":infos[9])+"] [playername:"+(infos==null?"null":infos[0])+"] [viplevel:"+infos[6]+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			}
			res = new CREATE_NEW_VIPINFORECORD_RES(req.getSequnceNum(), result);
			return res;
		}
		else if(message instanceof GET_UCVIP_INFO_REQ)
		{
			GET_UCVIP_INFO_REQ req = (GET_UCVIP_INFO_REQ) message;
			GET_UCVIP_INFO_RES res = null;
			String[] infos =  req.getInfos();

			String username = infos[0];	



			String[] results = null;

			Passport p = PassportManager.getInstance().getPassport(username);
			if(p != null)
			{
				//访问uc接口
				String ucurl = "http://sdk.g.uc.cn/ss/";
				String os = "android";
				int CPID = 149;
				int GAMEID = 63371;
				int ServerID = 907;
				int CHANNELID = 2;
				int APPID = 10024;
				String Secretkey = "1b31d9c43c8d8d7ebf655b6ab96f5c09";

				if(p.getLastLoginMobileOs() != null)
				{
					if(p.getLastLoginMobileOs().toLowerCase().contains("ios"))
					{
						os = "ios";
						GAMEID = 63586;
						ServerID = 908 ;
						CHANNELID = 2;
						APPID = 10025;
						Secretkey = "1b31d9c43c8d8d7ebf655b6ab96f5c09";
					}
				}
				String sid = p.getUcPassword();
				StringBuffer jsonStr = new StringBuffer();
				if(sid != null)
				{
					String signStr = CPID+"sid="+sid+Secretkey;

					jsonStr.append("{\n");
					jsonStr.append("\"id\":"+System.currentTimeMillis()+",\n");
					jsonStr.append("\"service\":\"ucid.vip.isVip\",\n");
					jsonStr.append("\"data\":{\"sid\":\""+sid+"\"},\n");
					jsonStr.append("\"game\":{\"cpId\":"+CPID+",\"gameId\":"+GAMEID+",\"channelId\":"+CHANNELID+",\"serverId\":"+ServerID+"},\n");
					jsonStr.append("\"sign\":\""+StringUtil.hash(signStr)+"\"\n");
					jsonStr.append("}");
				}
				else
				{
					logger.info("[判断是否是ucvip会员] [失败] [无法获取sid] ["+username+"]");
				}

				if(jsonStr.length() > 0)
				{
					//开始访问uc接口 
					/**
					 *    "id":1330395827,
							"state":{"code":1, "msg":"操作已完成"}, "data":{"status":1}

					 */
					String logStr = jsonStr.toString().replaceAll("\n", "");

					HashMap headers = new HashMap();

					String content =  "";
					try {
						byte bytes[] = HttpUtils.webPost(new java.net.URL(ucurl), jsonStr.toString().getBytes("UTF-8"), headers, 5000, 10000);

						String encoding = (String)headers.get(HttpUtils.ENCODING);
						Integer code = (Integer)headers.get(HttpUtils.Response_Code);
						String httpmessage = (String)headers.get(HttpUtils.Response_Message);

						content = new String(bytes,encoding);

						JacksonManager jm = JacksonManager.getInstance();
						try {
							Map map =(Map)jm.jsonDecodeObject(content);

							int rcode = ((Number)((Map)map.get("state")).get("code")).intValue();
							String msg = ((String)((Map)map.get("state")).get("msg")).toString();
							msg = URLDecoder.decode(msg, "utf-8");

							if(rcode == 1){
								int status = ((Number)((Map)map.get("data")).get("status")).intValue();
								results = new String[1];
								results[0] = status+"";

								logger.info("[解析返回数据] [成功] ["+ucurl+"] [code:"+rcode+"] [message:"+msg+"] [username:"+username+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
							}
							else
							{
								logger.info("[解析返回数据] [失败] ["+ucurl+"] [code:"+rcode+"] [message:"+msg+"] [username:"+username+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
							}

						}catch (Exception e) {
							logger.error("[解析返回数据] [失败] ["+ucurl+"] [code:--] [message:--] [username:"+username+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);

						}

						logger.info("[调用ucvip接口] ["+os+"] [成功] ["+ucurl+"] [code:"+code+"] ["+httpmessage+"] ["+logStr+"] ["+content+"] [username:"+username+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
					} catch (Exception e) {
						logger.info("[调用ucvip接口] ["+os+"] [失败] ["+ucurl+"] [code:--] [--] ["+logStr+"] ["+content+"] [username:"+username+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
					}

				}
			}
			else
			{
				logger.info("[判断是否是ucvip会员] [失败] [无法获取passport] ["+username+"]");
			}



			if(results == null)
			{
				results = new String[0];
			}
			res = new GET_UCVIP_INFO_RES(req.getSequnceNum(), results);
			return res;


		}
		else if(message instanceof CAN_SEND_UC_VID_MAIL_REQ)
		{
			CAN_SEND_UC_VID_MAIL_REQ req = (CAN_SEND_UC_VID_MAIL_REQ) message;
			try
			{
			
				CAN_SEND_UC_VID_MAIL_RES res = null;
				String[] infos =  req.getInfos();

				String username = infos[0];	
				String playerId = infos[1];



				String[] results = null;

				Passport p = PassportManager.getInstance().getPassport(username);
				if(p != null)
				{
					//访问uc接口
					String ucurl = "http://sdk.g.uc.cn/ss/";
					String os = "android";
					int CPID = 149;
					int GAMEID = 63371;
					int ServerID = 907;
					int CHANNELID = 2;
					int APPID = 10024;
					String Secretkey = "1b31d9c43c8d8d7ebf655b6ab96f5c09";

					if(p.getLastLoginMobileOs() != null)
					{
						if(p.getLastLoginMobileOs().toLowerCase().contains("ios"))
						{
							os = "ios";
							GAMEID = 63586;
							ServerID = 908 ;
							CHANNELID = 2;
							APPID = 10025;
							Secretkey = "1b31d9c43c8d8d7ebf655b6ab96f5c09";
						}
					}
					String sid = p.getUcPassword();
					StringBuffer jsonStr = new StringBuffer();
					if(sid != null)
					{
						String signStr = CPID+"sid="+sid+Secretkey;

						jsonStr.append("{\n");
						jsonStr.append("\"id\":"+System.currentTimeMillis()+",\n");
						jsonStr.append("\"service\":\"ucid.vip.isVip\",\n");
						jsonStr.append("\"data\":{\"sid\":\""+sid+"\"},\n");
						jsonStr.append("\"game\":{\"cpId\":"+CPID+",\"gameId\":"+GAMEID+",\"channelId\":"+CHANNELID+",\"serverId\":"+ServerID+"},\n");
						jsonStr.append("\"sign\":\""+StringUtil.hash(signStr)+"\"\n");
						jsonStr.append("}");
					}
					else
					{
						logger.info("[判断是否是ucvip会员] [失败] [无法获取sid] ["+username+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
					}

					if(jsonStr.length() > 0)
					{
						//开始访问uc接口 
						/**
						 *    "id":1330395827,
							"state":{"code":1, "msg":"操作已完成"}, "data":{"status":1}

						 */
						String logStr = jsonStr.toString().replaceAll("\n", "");

						HashMap headers = new HashMap();

						String content =  "";
						try {
							byte bytes[] = HttpUtils.webPost(new java.net.URL(ucurl), jsonStr.toString().getBytes("UTF-8"), headers, 5000, 10000);

							String encoding = (String)headers.get(HttpUtils.ENCODING);
							Integer code = (Integer)headers.get(HttpUtils.Response_Code);
							String httpmessage = (String)headers.get(HttpUtils.Response_Message);

							content = new String(bytes,encoding);

							JacksonManager jm = JacksonManager.getInstance();
							try {
								Map map =(Map)jm.jsonDecodeObject(content);

								int rcode = ((Number)((Map)map.get("state")).get("code")).intValue();
								String msg = ((String)((Map)map.get("state")).get("msg")).toString();
								msg = URLDecoder.decode(msg, "utf-8");

								if(rcode == 1){
									int status = ((Number)((Map)map.get("data")).get("status")).intValue();
									if(status == 1)
									{
										results = new String[1];
										results[0] = status+"";
									}

									logger.info("[解析返回数据] [成功] ["+ucurl+"] [code:"+rcode+"] [message:"+msg+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
								}
								else
								{
									logger.info("[解析返回数据] [失败] ["+ucurl+"] [code:"+rcode+"] [message:"+msg+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
								}

							}catch (Exception e) {
								logger.error("[解析返回数据] [失败] ["+ucurl+"] [code:--] [message:--] ["+(System.currentTimeMillis()-startTime)+"ms]",e);

							}

							logger.info("[调用ucvip接口] ["+os+"] [成功] ["+ucurl+"] [code:"+code+"] ["+httpmessage+"] ["+logStr+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
						} catch (Exception e) {
							logger.info("[调用ucvip接口] ["+os+"] [失败] ["+ucurl+"] [code:--] [--] ["+logStr+"] ["+content+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
						}

					}
				}
				else
				{
					logger.info("[判断是否是ucvip会员] [失败] [无法获取passport] ["+username+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
				}


				UCVipManager ucVipManager = UCVipManager.getInstance();

				if(results == null)
				{
					//不是vip 不能发礼包
					results = new String[0];
					logger.info("[判断是否是ucvip会员] [成功] [不是ucvip] ["+username+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
				}
				else 
				{
					//发送过不能给礼包
					List<UcVipRecord>  lst =  ucVipManager.queryForWhere(" playerId = ?", new Object[]{Long.parseLong(playerId)});
					if(lst.size() > 0)
					{
						results = new String[0];
						logger.info("[判断是否是ucvip会员] [成功] [发送过礼包] ["+username+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
					}

				}
				//判断是否发送过礼包

				res = new CAN_SEND_UC_VID_MAIL_RES(req.getSequnceNum(), results);
				return res;
			}
			catch(Exception e)
			{
				CAN_SEND_UC_VID_MAIL_RES res = new CAN_SEND_UC_VID_MAIL_RES(req.getSequnceNum(), new String[0]);
				logger.info("[判断是否是ucvip会员] [失败] [出现异常]",e);
				return res;
			}


		}
		
		else if(message instanceof CREATE_UC_VID_MAIL_SEND_RECORD_REQ)
		{
			CREATE_UC_VID_MAIL_SEND_RECORD_REQ req = (CREATE_UC_VID_MAIL_SEND_RECORD_REQ) message;

			CREATE_UC_VID_MAIL_SEND_RECORD_RES res = null;
			
		/*	private String username;
			
			private long playerId;
			
			private String playerName;
			
			private String serverName;
			
//			private long birthtime;
			
			private int playerLevel;
			
			private int  vipLevel;
			*/
			
			try
			{
			
				String[] infos =  req.getInfos();

				String username = infos[0];	
				String playerId = infos[1];
				String playerName = infos[2];
				String serverName = infos[3];
				String playerLevel = infos[4];
				String vipLevel = infos[5];



				String[] results = null;
				
				UcVipRecord ucVipRecord = new UcVipRecord();
				ucVipRecord.setUsername(username);
				ucVipRecord.setPlayerId(Long.parseLong(playerId));
				ucVipRecord.setPlayerName(playerName);
				ucVipRecord.setServerName(serverName);
				ucVipRecord.setPlayerLevel(Integer.parseInt(playerLevel));
				ucVipRecord.setVipLevel(Integer.parseInt(vipLevel));

				UCVipManager ucVipManager = UCVipManager.getInstance();
				ucVipManager.saveOrUpdate(ucVipRecord);
				
				results = new String[]{"ok"};

				res = new CREATE_UC_VID_MAIL_SEND_RECORD_RES(req.getSequnceNum(), results);
				logger.info("[插入ucvip发送礼包记录] [成功] [ok] ["+ucVipRecord.getLogString()+"] [cost:"+(System.currentTimeMillis()-startTime)+"]");
				return res;
			}
			catch(Exception e)
			{
				res = new CREATE_UC_VID_MAIL_SEND_RECORD_RES(req.getSequnceNum(), new String[0]);
				logger.error("[判断是否是ucvip会员] [失败] [出现异常]",e);
				return res;
			}


		}
		else if(message instanceof MIGU_COMMUNICATE_REQ)
		{
			MIGU_COMMUNICATE_REQ req = (MIGU_COMMUNICATE_REQ) message;
			try
			{
				
				String[] infos = req.getInfos();
				String op = infos[0];
				if(infos[0].contains("GET_MIGU_TOKEN"))
				{
					String[] strs = MiguWorker.applyMiguToken(infos);
					if(logger.isInfoEnabled())
						logger.info("[米谷通讯] [成功] [ok] ["+infos.length+"] ["+strs.length+"] [cost:"+(System.currentTimeMillis()-startTime)+"]");
					return  new MIGU_COMMUNICATE_RES(req.getSequnceNum(), strs);
				}
				else if(op.contains("enterserver_query") || op.contains("getmigutoken_query"))
				{
					String username = infos[1];
					String servername = infos[2];
					String rolename = infos[3];
					String roleid = infos[4];
					String enterservertime = infos[5];
					
					MiguWorker.opCache(op, username, roleid, servername, rolename, enterservertime);;
					if(logger.isInfoEnabled())
						logger.info("[米谷通讯] [成功] [ok] ["+infos.length+"] ["+op+"] [cost:"+(System.currentTimeMillis()-startTime)+"]");
					return  new MIGU_COMMUNICATE_RES(req.getSequnceNum(), new String[0]);
				} else if (op.contains("updateusername")) {
					MiguWorker.notifyMiguUpdataUserName(infos);
				}
			
			}
			catch(Exception e)
			{
				logger.error("[米谷通讯] [失败] [出现异常]",e);
				return  new MIGU_COMMUNICATE_RES(req.getSequnceNum(), new String[0]);
			}
			logger.error("[米谷通讯] [失败] [未知操作] ["+req.getInfos()[0]+"]");
			return  new MIGU_COMMUNICATE_RES(req.getSequnceNum(), new String[0]);
			
		}
		else if(message instanceof QUERY_SAVINGAMOUNT_REQ)
		{
			QUERY_SAVINGAMOUNT_REQ req = (QUERY_SAVINGAMOUNT_REQ) message;
			try
			{
				long playerId = req.getPlayerId();
				long beginTime = req.getStartTime();
				long endTime = req.getEndTime();
				
				long savingAmount = orderFormManager.getPeriodSavingAmountByPlayer(playerId, beginTime, endTime);
				if(logger.isInfoEnabled())
					logger.info("[查询玩家金额] [成功] [ok] [playerId:"+req.getPlayerId()+"] [beginTime:"+req.getStartTime()+"] [endTime:"+req.getEndTime()+"] [金额:"+savingAmount+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
				
				return  new QUERY_SAVINGAMOUNT_RES(req.getSequnceNum(),savingAmount ,new String[0]);
			}
			catch(Exception e)
			{
				logger.error("[查询角色充值金额] [失败] [出现异常]",e);
				return  new QUERY_SAVINGAMOUNT_RES(req.getSequnceNum(), 0,new String[0]);
			}
			
		}
		else if(message instanceof PASSPORT_UPDATE_REQ2) {
			PASSPORT_UPDATE_REQ2 req = (PASSPORT_UPDATE_REQ2)message;
			Passport pass = PassportManager.getInstance().getPassport(req.getUsername());
			if(pass != null) {
				pass.setLastLoginDate(new Date());
				PassportManager.getInstance().updatePassportField(pass, "lastLoginDate");
			}
			return new PASSPORT_UPDATE_RES2(message.getSequnceNum(), (byte)0);
		}
		
		else {
			logger.error(message.getTypeDescription() + "未知的协议！");
			return null;
		}

	}

	public static final int NOT_POP_WINDOW_FOR_VIP = 0;
	public static final int IS_POP_WINDOW_FOR_VIP = 1;
	public static final int NOT_POP_WINDOW_AND_CREATENEWVIPRECORD_FOR_VIP = 2;


	public static String[] NINEONE_FUND_CHANNELS = { "91Fund_MIESHI" };

	private boolean checkCanGiveFund(String channel) {
		boolean isCanGiveFund = false;
		for (String s : NINEONE_FUND_CHANNELS) {
			if (channel.equalsIgnoreCase(s)) {
				isCanGiveFund = true;
				return isCanGiveFund;
			}
		}

		return isCanGiveFund;
	}

	public static Map<String, Integer> ruleMap = new HashMap<String, Integer>();
	public static Map<Integer, String> operateMap = new HashMap<Integer, String>();

	static {
		ruleMap.put("levelup3", 1);
	}

	static {
		operateMap.put(1, "");
	}

	public int sendFund4NineOne(String username, String channel, String fundtype) {
		try {
			long startTime = System.currentTimeMillis();

			Passport p = PassportManager.getInstance().getPassport(username);
			if (p == null) {
				logger.error("[给91特定渠道用户返积金] [失败] [没有找对应的用户] [username:" + username + "] [channel:" + channel + "] [fundtype:" + fundtype + "] [cost:"
						+ (System.currentTimeMillis() - startTime) + "ms]");
				return -1;
			}

			String fundUrl = "http://AccPoint.91.com/ForAP/AddV2.ashx";
			String Appkey = "aaa";
			Map<String, Object> jsoncontent = new HashMap<String, Object>();
			String strs[] = username.split("_");
			long uin = -1l;
			if (strs != null && strs.length > 1) {
				uin = Long.parseLong(strs[1]);
			} else {
				logger.error("[给91特定渠道用户返积金] [失败] [无法得到91uin号] [username:" + username + "] [channel:" + channel + "] [fundtype:" + fundtype + "] [strs length:"
						+ strs.length + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
				return -1;
			}

			int ruleid = ruleMap.get(fundtype);
			String desc = operateMap.get(ruleid);

			jsoncontent.put("Uin", uin);
			jsoncontent.put("RuleId", ruleid);
			jsoncontent.put("Operate", desc);

			String bodyContent = "";
			try {
				bodyContent = JsonUtil.jsonFromObject(jsoncontent);
			} catch (Exception e) {

				logger.error("[给91特定渠道用户返积金] [失败] [将对象转成json格式失败] [username:" + username + "] [channel:" + channel + "] [fundtype:" + fundtype
						+ "] [strs length:" + strs.length + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
				logger.error("[给91特定渠道用户返积金] [失败] [无法得到91uin号] [username:" + username + "] [channel:" + channel + "] [fundtype:" + fundtype + "] [要转成json对象内容:"
						+ jsoncontent + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]", e);
				return -1;
			}
			String sign = StringUtil.hash(bodyContent + Appkey);
			String urlParam = "?AppId=1&Act=1&Platform=" + p.getLastLoginMobileOs() + "&Sign=" + sign + "";

			// 开始调用
			URL url = new URL(fundUrl + urlParam);
			Map headers = new HashMap();
			String content = "";
			try {
				byte bytes[] = HttpUtils.webPost(url, bodyContent.getBytes(), headers, 5000, 10000);

				String encoding = (String) headers.get(HttpUtils.ENCODING);
				Integer code = (Integer) headers.get(HttpUtils.Response_Code);
				String message = (String) headers.get(HttpUtils.Response_Message);
				content = new String(bytes, encoding); // json格式

				// 解析json格式
				JacksonManager jm = JacksonManager.getInstance();
				Map returnJsonMap = (Map) jm.jsonDecodeObject(content);
				Object resultCode = returnJsonMap.get("ResultCode");
				if (resultCode instanceof Number) {
					int rcode = ((Integer) resultCode).intValue();
					Object result = returnJsonMap.get("Result");
					String strresult = "";
					if (result instanceof String) {
						strresult = (String) strresult;
					} else {
						if (logger.isInfoEnabled())
							logger.info("[给91特定渠道用户返积金] [获取返回结果提示] [失败] [username:" + username + "] [channel:" + channel + "] [fundtype:" + fundtype
									+ "] [返回值:" + content + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
						logger.error("[给91特定渠道用户返积金] [失败] [无法得到91uin号] [username:" + username + "] [channel:" + channel + "] [fundtype:" + fundtype
								+ "] [要转成json对象内容:" + jsoncontent + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
					}

					if (rcode == 0) {
						try {
							Map bodyMap = (Map) returnJsonMap.get("Body");
							if (bodyMap == null) {
								return -1;
							}
							int isdone = -10000;
							if (bodyMap.get("IsDone") instanceof Number) {
								isdone = (Integer) bodyMap.get("IsDone");
								if (isdone > 0) {
									if (bodyMap.get("Added") instanceof Number) {
										int added = (Integer) bodyMap.get("Added");
										if (logger.isInfoEnabled())
											logger.info("[给91特定渠道用户返积金] [成功] [OK] [username:" + username + "] [channel:" + channel + "] [fundtype:" + fundtype
													+ "] [返回值:" + content + "] [isdone:" + isdone + "] [added:" + added + "] [result:" + result + "] [cost:"
													+ (System.currentTimeMillis() - startTime) + "ms]");

										return added;
									} else {
										logger.warn("[给91特定渠道用户返积金] [成功] [但是无法获取添加的积金数] [username:" + username + "] [channel:" + channel + "] [fundtype:"
												+ fundtype + "] [返回值:" + content + "] [isdone:" + isdone + "] [result:" + result + "] [cost:"
												+ (System.currentTimeMillis() - startTime) + "ms]");
										return 0;
									}
								} else if (isdone == 0) {
									if (logger.isInfoEnabled())
										logger.info("[给91特定渠道用户返积金] [成功] [等待91处理] [username:" + username + "] [channel:" + channel + "] [fundtype:" + fundtype
												+ "] [返回值:" + content + "] [isdone:" + isdone + "] [added:" + 0 + "] [result:" + result + "] [cost:"
												+ (System.currentTimeMillis() - startTime) + "ms]");

									return 0;
								} else {
									logger.warn("[给91特定渠道用户返积金] [失败] [添加积金失败] [username:" + username + "] [channel:" + channel + "] [fundtype:" + fundtype
											+ "] [返回值:" + content + "] [isdone:" + isdone + "] [url:" + url.toString() + "] [result:" + result + "] [cost:"
											+ (System.currentTimeMillis() - startTime) + "ms]");
									return -1;
								}
							} else {
								logger.error("[给91特定渠道用户返积金] [失败] [无法解析返回结果isdone] [username:" + username + "] [channel:" + channel + "] [fundtype:" + fundtype
										+ "] [返回值:" + content + "] [isdone:" + isdone + "] [added:--] [result:" + result + "] [cost:"
										+ (System.currentTimeMillis() - startTime) + "ms]");
								return -1;
							}
						} catch (Exception e) {
							logger.error("[给91特定渠道用户返积金] [失败] [将json格式转成对象失败(body)] [username:" + username + "] [channel:" + channel + "] [fundtype:"
									+ fundtype + "] [strs length:" + strs.length + "] [result:" + result + "] [cost:"
									+ (System.currentTimeMillis() - startTime) + "ms]", e);
							return -1;
						}
					} else {
						logger.error("[给91特定渠道用户返积金] [失败] [返回结果为失败] [username:" + username + "] [channel:" + channel + "] [fundtype:" + fundtype
								+ "] [content:" + content + "] [result:" + result + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
						logger.error("[给91特定渠道用户返积金] [失败] [无法得到91uin号] [username:" + username + "] [channel:" + channel + "] [fundtype:" + fundtype
								+ "] [要转成json对象内容:" + content + "] [resultcode:" + rcode + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
						return -1;
					}

				} else {
					logger.error("[给91特定渠道用户返积金] [失败] [得到的返回字符串不符合文档规则，返回值类型不为数字] [username:" + username + "] [channel:" + channel + "] [fundtype:" + fundtype
							+ "] [返回字符串:" + content + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
					return -1;
				}
			} catch (Exception e) {
				logger.error("[给91特定渠道用户返积金] [失败] [出现未知异常] [username:" + username + "] [channel:" + channel + "] [fundtype:" + fundtype + "] [返回字符串:" + content
						+ "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]", e);
				return -1;
			}

		} catch (Exception e) {
			logger.error("[给91特定渠道用户返积金] [失败] [出现未知异常] [username:" + username + "] [channel:" + channel + "] [fundtype:" + fundtype + "] [返回字符串:--]", e);
		}

		return -1;
	}



	@Override
	public void receiveResponseMessage(Connection arg0, RequestMessage arg1, ResponseMessage arg2) throws ConnectionException {
		// TODO Auto-generated method stub

	}

	@Override
	public RequestMessage waitingTimeout(Connection arg0, long arg1) throws ConnectionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void connected(Connection conn) throws IOException {
		conn.setMessageFactory(BossMessageFactory.getInstance());
		conn.setMessageHandler(this);
	}

	private long getFactor(String channel) {
		if (channel.toLowerCase().contains("malai")) {
			return MaLaiSavingCallBackServlet.factor;
		} else {
			return 1;
		}
	}

	/**
	 * 账号绑定推广码
	 * 
	 * @param username
	 * @param Key
	 * @return true:绑定成功, false:绑定失败
	 */
	public boolean passportBindPromotionKey(String servername, String username, String promotionKey) {
		// 填写实现代码
		//
		PromotionClient pc = PromotionClient.getInstance();
		String res = pc.pkeyIsIn(username, promotionKey);
		if (res.contains("0")) {
			return true;
		}
		return false;
	}

}
