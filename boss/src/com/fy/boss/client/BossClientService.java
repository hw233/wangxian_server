package com.fy.boss.client;

import java.util.Date;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.boss.authorize.exception.OrderGenerateException;
import com.fy.boss.authorize.exception.PasswordWrongException;
import com.fy.boss.authorize.exception.SavingFailedException;
import com.fy.boss.authorize.exception.ServerNameNotExistsException;
import com.fy.boss.authorize.exception.UserNameInValidException;
import com.fy.boss.authorize.exception.UsernameAlreadyExistsException;
import com.fy.boss.authorize.exception.UsernameNotExistsException;
import com.fy.boss.authorize.model.Passport;
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
import com.fy.boss.message.GET_USER_LAST_SAVING_TIME_RES;
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
import com.fy.boss.platform.qq.QQUserInfo;
import com.fy.boss.transport.BossServerService;
import com.fy.boss.client.AliOrderInfo;
import com.fy.boss.client.AlipayArgs;
import com.fy.boss.client.BossClientService;
import com.fy.boss.client.SavingPageRecord;
import com.fy.boss.client.SavingRecord;
import com.xuanzhi.tools.transport.AbstractClientService;
import com.xuanzhi.tools.transport.MessageFactory;

public class BossClientService extends AbstractClientService {

	public static Logger logger = LoggerFactory.getLogger(BossClientService.class);

	protected static BossClientService self;

	public static BossClientService getInstance() {
		if (self == null) {
			synchronized (BossClientService.class) {
				if (self == null)
					self = new BossClientService();
			}
		}
		return self;
	}

	public void initialize() throws Exception {
		long now = System.currentTimeMillis();
		self = this;
		System.out.println(this.getClass().getName() + " initialize successfully [cost:" + (System.currentTimeMillis() - now) + "ms]");
		logger.info(this.getClass().getName() + " initialize successfully [" + (System.currentTimeMillis() - now) + "]");
	}


	public String tagforbid[] = new String[]{"'","\""," or ","μ","\n","\t","="};

	/**
	 * 判断是否含有禁用的标签字符
	 * @param name
	 * @return
	 */
	public boolean validStr(String name) {
		String aname = name.toLowerCase();
		for(String s : tagforbid) {
			if(aname.indexOf(s) != -1) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 用户登录
	 * 
	 * @param username
	 * @param passwd
	 * @return
	 * @throws Exception
	 *             //登录的结果，0表示成功登陆，1表示密码不匹配，2表示用户不存在，3此帐号正在被其他人使用，6表示包格式错误
	 * 
	 * 抛出用户名不存在 ，密码错误，用户名非法
	 */
	public Passport login(String username, String passwd, String lastLoginClientId, String lastLoginChannel, String lastLoginMobileOs,
			String lastLoginMobileType, String lastLoginNetworkMode) throws UsernameNotExistsException, PasswordWrongException, Exception {
		long startTime = System.currentTimeMillis();
		PASSPORT_LOGIN_REQ req = null;
		PASSPORT_LOGIN_RES res = null;

		if (StringUtils.isEmpty(username)) {
			throw new NullArgumentException("传入的username为空值！");
		}
		if (StringUtils.isEmpty(passwd)) {
			throw new NullArgumentException("传入的password为空值！");
		}
		// 开始验证用户名
		// 验证用户名是否还有非法字符
		if (validStr(username)) {
			// 发请求验证用户名是否已存在
			req = new PASSPORT_LOGIN_REQ(BossMessageFactory.nextSequnceNum(), username, passwd, lastLoginClientId, lastLoginChannel, lastLoginMobileOs,
					lastLoginMobileType, lastLoginNetworkMode);
			// 发送请求
			try {
				res = (PASSPORT_LOGIN_RES) sendMessageAndWaittingResponse(req, 100000);
			} catch (Exception e) {
				logger.error("[登陆][失败][发送请求连接错误！][cost:" + (System.currentTimeMillis() - startTime) + "ms]",e);
				throw new Exception("发送请求连接错误！"+e.getMessage());
			}
			// 如果请求返回
			if (res != null) {
				byte result = res.getResult();
				// 登录的结果，0表示成功登陆，1表示密码不匹配，2表示用户不存在，3此帐号正在被其他人使用，6表示包格式错误"
				// 判断是否执行成功
				if (result != 0) {
					if (result == 1) {
						logger.warn("[登陆][失败][密码错误][用户名：" + username + "][cost:" + (System.currentTimeMillis() - startTime) + "ms]");
						throw new PasswordWrongException("密码错误！" + res.getDescription());
					} else if (result == 2) {
						logger.warn("[登陆][失败][用户不存在][用户名：" + username + "][cost:" + (System.currentTimeMillis() - startTime) + "ms]");
						throw new UsernameNotExistsException("用户不存在!" + res.getDescription());
					} else if (result == 6) {
						logger.error("[登陆][失败][Boss内部错误][用户名：" + username + "][cost:" + (System.currentTimeMillis() - startTime) + "ms]");
						throw new Exception(res.getDescription());
					} else {
						logger.error("[登陆][失败][Boss内部错误][用户名：" + username + "][cost:" + (System.currentTimeMillis() - startTime) + "ms]");
						throw new Exception(res.getDescription());
					}
				} else {
					// 如果用户存在
					long id = res.getPassportid();
					// 发送请求获取passport
					Passport p = getPassport(id);
					logger.info("[登陆][成功][用户名：" + username + "][cost:" + (System.currentTimeMillis() - startTime) + "ms]");
					return p;
				}
			}
			// 如果请求超时
			else {
				logger.warn("[登陆][失败][请求超时][username:" + username + "][cost:" + (System.currentTimeMillis() - startTime) + "ms]");
				throw new Exception("Boss响应超时！");
			}
		} else // 如果用户名不合法
		{
			logger.warn("[登陆][失败][用户名含有非法字符][username:" + username + "][cost:" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new UserNameInValidException("用户名含有非法字符！");
		}
	}

	/**
	 * 注册
	 * 
	 * @param username
	 * @param password
	 * @param email
	 *            optional
	 * @param mobile
	 *            optional
	 * @return object[]{passport}
	 * @throws Exception
	 */
	public synchronized Passport register(String username, String passwd, String registerClientId, String nickName, String fromWhere, String registerChannel,
			String registerMobile, String registerMobileOs, String registerMobileType, String registerNetworkMode) throws UsernameAlreadyExistsException,
			UserNameInValidException, Exception {
		long startTime = System.currentTimeMillis();

		if (StringUtils.isEmpty(username)) {
			throw new NullArgumentException("传入的username为空值！");
		}
		if (StringUtils.isEmpty(passwd)) {
			throw new NullArgumentException("传入的password为空值！");
		}
		if (StringUtils.isEmpty(registerClientId)) {
			throw new NullArgumentException("传入的clientId为空值！");
		}

		// 注册验证非法字符
		PASSPORT_REGISTER_REQ req = null;

		PASSPORT_REGISTER_RES res = null;

		// 验证非法字符
		if (validStr(username)) {
			// 如果账户名合法 发送注册请求 定好注册错误信息
			req = new PASSPORT_REGISTER_REQ(BossMessageFactory.nextSequnceNum(), registerClientId, username, passwd, nickName, fromWhere, registerChannel,
					registerMobile, registerMobileOs, registerMobileType, registerNetworkMode);
			try {
				res = (PASSPORT_REGISTER_RES) sendMessageAndWaittingResponse(req, 100000);
			} catch (Exception e) {
				logger.error("[注册][失败][发送请求连接错误！][cost:" + (System.currentTimeMillis() - startTime) + "ms]",e);
				throw new Exception("发送请求连接错误！"+e.getMessage());
			}

			// 如果没有超时
			if (res != null) {
				byte result = res.getResult();
				// 判断注册结果
				if (result != 0) {
					// "注册的结果，0表示成功，1表示用户已存在，2表示服务器内部错误"
					if (result == 1) {
						logger.warn("[注册][失败][用户已存在][username:" + username + "][cost:" + (System.currentTimeMillis() - startTime) + "ms]");
						throw new UsernameAlreadyExistsException("用户已存在！" + res.getDescription());
					} else if (result == 2) {
						logger.warn("[注册][失败][Boss服务器内部错误][username:" + username + "][cost:" + (System.currentTimeMillis() - startTime) + "ms]");
						throw new Exception("Boss服务器内部错误！" + res.getDescription());
					} else {
						logger.warn("[注册][失败][Boss服务器内部错误!][username:" + username + "][cost:" + (System.currentTimeMillis() - startTime) + "ms]");
						throw new Exception("Boss服务器内部错误！" + res.getDescription());
					}
				} else {
					Passport p = getPassport(res.getPassportid());
					logger.info("[注册][成功][用户名：" + username + "][cost:" + (System.currentTimeMillis() - startTime) + "ms]");
					return p;
				}
			}
			// 如果请求超时
			else {
				logger.warn("[注册][失败][Boss响应超时][username:" + username + "][cost:" + (System.currentTimeMillis() - startTime) + "ms]");
				throw new Exception("Boss响应超时！");
			}
		} else {
			logger.warn("[注册][失败][用户名含有非法字符][username:" + username + "][cost:" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new UserNameInValidException("用户名含有非法字符！");
		}
	}

	/**
	 * 通过id获取一个存在的通行证
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Passport getPassport(long id) throws Exception {
		long startTime = System.currentTimeMillis();
		PASSPORT_GET_REQ req = new PASSPORT_GET_REQ(BossMessageFactory.nextSequnceNum(), id);
		PASSPORT_GET_RES res = null;
		try {
			res = (PASSPORT_GET_RES) sendMessageAndWaittingResponse(req, 100000);
		} catch (Exception e) {
			logger.error("[通过id获取通行证][失败][请求连接错误][id:" + id + "][cost:" + (System.currentTimeMillis() - startTime) + "ms]", e);
			throw new Exception("发送请求连接异常！");
		}
		if (res != null) {
			byte result = res.getResult();
			if (result != 0) {
				// 结果，0表示成功，1表示id不存在，2表示Boss内部错误，
				if (result == 1) {
					logger.error("[通过id获取通行证][失败][id不存在][id:" + id + "][cost:" + (System.currentTimeMillis() - startTime) + "ms]");
					throw new Exception(res.getDescription());
				} else if (result == 2) {
					logger.error("[通过id获取通行证][失败][Boss内部错误][id:" + id + "][cost:" + (System.currentTimeMillis() - startTime) + "ms]");
					throw new Exception(res.getDescription());
				} else {
					logger.error("[通过id获取通行证][失败][Boss内部错误][id:" + id + "][cost:" + (System.currentTimeMillis() - startTime) + "ms]");
					throw new Exception(res.getDescription());
				}
			} else {
				Passport passport = new Passport();
				passport.setFromWhere(res.getFromWhere());
				passport.setId(res.getPassportid());
				passport.setIsSetSecretQuestion(res.getIsSetSecretQuestion());
				passport.setLastChargeAmount(res.getLastChargeAmount());
				passport.setLastChargeChannel(res.getLastChargeChannel());

				passport.setLastChargeDate(new Date(res.getLastChargeDate()));

				passport.setLastLoginChannel(res.getLastLoginChannel());
				passport.setLastLoginClientId(res.getLastLoginClientId());

				passport.setLastLoginDate(new Date(res.getLastLoginDate()));

				passport.setLastLoginIp(res.getLastLoginIp());
				passport.setLastLoginMobileOs(res.getLastLoginMobileOs());
				passport.setLastLoginMobileType(res.getLastLoginMobileType());
				passport.setLastLoginNetworkMode(res.getLastLoginNetworkMode());

				passport.setLastQuestionSetDate(new Date(res.getLastQuestionSetDate()));
				passport.setLastUpdateStatusDate(new Date(res.getLastUpdateStatusDate()));

				passport.setNickName(res.getNickName());
				// passport.setPassWd(passWord);
				passport.setRegisterChannel(res.getRegisterChannel());
				passport.setRegisterClientId(res.getRegisterClientId());

				passport.setRegisterDate(new Date(res.getRegisterDate()));

				passport.setRegisterMobile(res.getRegisterMobile());
				passport.setRegisterMobileOs(res.getRegisterMobileOs());
				passport.setRegisterMobileType(res.getRegisterMobileType());
				passport.setRegisterNetworkMode(res.getRegisterNetworkMode());
				passport.setSecretAnswer(res.getSecretAnswer());
				passport.setSecretQuestion(res.getSecretQuestion());
				passport.setStatus(res.getStatus());
				passport.setTotalChargeAmount(res.getTotalChargeAmount());
				passport.setUserName(res.getUsername());
				passport.setUcPassword(res.getUcPassword());
				if (logger.isInfoEnabled())
					logger.info("[通过id获取通行证][成功] [id:" + id + "] [username:" + passport.getUserName() + "]  [cost:" + (System.currentTimeMillis() - startTime)
							+ "ms]");
				return passport;
			}

		} else {
			throw new Exception("Boss响应超时！");
		}

	}
	
	public void updatePassportMt(String username, String mt, String ip) {
		long startTime = System.currentTimeMillis();
		PASSPORT_UPDATE_REQ2 req = new PASSPORT_UPDATE_REQ2(BossMessageFactory.nextSequnceNum(), username, mt, ip);
		PASSPORT_UPDATE_RES2 res = null;
		try {
			res = (PASSPORT_UPDATE_RES2) sendMessageAndWaittingResponse(req, 100000);
		} catch (Exception e) {
			logger.error("[更新][失败][请求连接错误][cost:" + (System.currentTimeMillis() - startTime) + "ms]", e);
		}
	}

	/**
	 * 通过username获取一个存在的通行证
	 * 
	 * @param username
	 * @return
	 */
	public Passport getPassportByUserName(String userName) throws Exception {
		long startTime = System.currentTimeMillis();

		PASSPORT_GET_REQ2 req = new PASSPORT_GET_REQ2(BossMessageFactory.nextSequnceNum(), userName);
		PASSPORT_GET_RES2 res = null;

		// 验证用户名合法性
		if (validStr(userName)) {
			try {
				res = (PASSPORT_GET_RES2) sendMessageAndWaittingResponse(req, 100000);
			} catch (Exception e) {
				logger.error("[通过username获取通行证][失败][请求连接错误][username:" + userName + "][cost:" + (System.currentTimeMillis() - startTime) + "ms]", e);
				throw new Exception("发送请求连接异常！");
			}
			if (res != null) {
				byte result = res.getResult();
				logger.warn("[通过username获取通行证] [返回数据] [username:" + userName + "] [result:"+result+"] [channel:"+res.getRegisterChannel()+"] [passportId:"+res.getPassportid()+"] [rusername:"+res.getUsername()+"]");
				if (result != 0) { // 结果不对
					// 结果，0表示成功，1表示用户不存在，2Boss内部错误
					if (result == 1) {
						logger.error("[通过username获取通行证][失败][用户不存在][username:" + userName + "][cost:" + (System.currentTimeMillis() - startTime) + "ms]");
						throw new UsernameNotExistsException("无此用户！");
					} else {
						logger.error("[通过username获取通行证][失败][Boss内部错误][username:" + userName + "][cost:" + (System.currentTimeMillis() - startTime) + "ms]");
						throw new Exception(res.getDescription());
					}
				} else {
					Passport passport = new Passport();
					passport.setFromWhere(res.getFromWhere());
					passport.setId(res.getPassportid());
					passport.setIsSetSecretQuestion(res.getIsSetSecretQuestion());
					passport.setLastChargeAmount(res.getLastChargeAmount());
					passport.setLastChargeChannel(res.getLastChargeChannel());

					passport.setLastChargeDate(new Date(res.getLastChargeDate()));

					passport.setLastLoginChannel(res.getLastLoginChannel());
					passport.setLastLoginClientId(res.getLastLoginClientId());

					passport.setLastLoginDate(new Date(res.getLastLoginDate()));

					passport.setLastLoginIp(res.getLastLoginIp());
					passport.setLastLoginMobileOs(res.getLastLoginMobileOs());
					passport.setLastLoginMobileType(res.getLastLoginMobileType());
					passport.setLastLoginNetworkMode(res.getLastLoginNetworkMode());

					passport.setLastQuestionSetDate(new Date(res.getLastQuestionSetDate()));
					passport.setLastUpdateStatusDate(new Date(res.getLastUpdateStatusDate()));

					passport.setNickName(res.getNickName());
					passport.setPassWd(res.getPasswd());
					passport.setRegisterChannel(res.getRegisterChannel());
					passport.setRegisterClientId(res.getRegisterClientId());

					passport.setRegisterDate(new Date(res.getRegisterDate()));

					passport.setRegisterMobile(res.getRegisterMobile());
					passport.setRegisterMobileOs(res.getRegisterMobileOs());
					passport.setRegisterMobileType(res.getRegisterMobileType());
					passport.setRegisterNetworkMode(res.getRegisterNetworkMode());
					passport.setSecretAnswer(res.getSecretAnswer());
					passport.setSecretQuestion(res.getSecretQuestion());
					passport.setStatus(res.getStatus());
					passport.setTotalChargeAmount(res.getTotalChargeAmount());
					passport.setUserName(res.getUsername());
					passport.setUcPassword(res.getUcPassword());

					if (logger.isInfoEnabled())
						logger.info("[通过username获取通行证][username:" + passport.getUserName() + "]  [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
					return passport;
				}
			} else {
				logger.error("[通过username获取通行证][失败][Boss响应超时][username:" + userName + "][cost:" + (System.currentTimeMillis() - startTime) + "ms]");
				throw new Exception("Boss响应超时！");
			}
		} else {
			logger.warn("[通过username获取通行证][失败][用户名含有非法字符][username:" + userName + "][cost:" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new UserNameInValidException("用户名含有非法字符！");
		}

	}

	public boolean update(Passport p) {
		long startTime = System.currentTimeMillis();

		long id = p.getId();
		String userName = p.getUserName();
		if (userName == null) {
			logger.warn("[更新通行证] [失败] [账号名为空值] [id:" + id + "] [username:" + userName + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
			return false;
		} else if (!validStr(userName)) {
			logger.warn("[更新通行证][失败][账号名含有非法字符][id:" + id + "][username:" + userName + "][cost:" + (System.currentTimeMillis() - startTime) + "ms]");
			return false;
		}

		String passwd = p.getPassWd();
		String nickName = p.getNickName();
		String secretQuestion = p.getSecretQuestion();
		String secretAnswer = p.getSecretAnswer();
		Date lastQuestionSetDate = p.getLastQuestionSetDate();
		boolean isSetSecretQuestion = p.getIsSetSecretQuestion();
		Integer status = p.getStatus();
		Date lastUpdateStatusDate = p.getLastUpdateStatusDate();
		String uc_password = p.getUcPassword();
		if(uc_password == null) uc_password = "";

		PASSPORT_UPDATE_REQ req = new PASSPORT_UPDATE_REQ(BossMessageFactory.nextSequnceNum(), id, userName, passwd, nickName, secretQuestion, secretAnswer,
				lastQuestionSetDate == null ? -1l : lastQuestionSetDate.getTime(), isSetSecretQuestion, status, lastUpdateStatusDate == null ? -1l
						: lastQuestionSetDate.getTime(),uc_password);

		PASSPORT_UPDATE_RES res = null;
		try {
			res = (PASSPORT_UPDATE_RES) sendMessageAndWaittingResponse(req, 100000);
		} catch (Exception e) {
			logger.error("[更新通行证] [失败] [请求连接错误] [username:" + userName + "] [nickName:"+nickName+"] [secretQuestion:"+secretQuestion+"] [secretAnswer:"+secretAnswer+"] [uc_password:"+uc_password+"] [cost:" + (System.currentTimeMillis() - startTime) + "ms]", e);
			return false;
		}

		if (res != null) {
			if (res.getIsOk()) {
				if (logger.isInfoEnabled())
					logger.info("[更新通行证][成功][username:" + userName + "][cost:" + (System.currentTimeMillis() - startTime) + "ms]");
				return res.getIsOk();
			} else {
				logger.error("[更新通行证][失败][Boss执行更新失败][username:" + userName + "][cost:" + (System.currentTimeMillis() - startTime) + "ms]");
				return res.getIsOk();
			}
		} else {
			logger.error("[更新通行证][失败][Boss响应超时][username:" + userName + "][cost:" + (System.currentTimeMillis() - startTime) + "ms]");
			return false;
		}

	}

	public boolean updateAppstoreIdentify(Passport p,String _identify) {
		long startTime = System.currentTimeMillis();

		long id = p.getId();
		String userName = p.getUserName();
		if (userName == null) {
			logger.warn("[添加APPSTORE唯一验证串] [失败] [账号名为空值] [id:" + id + "] [username:" + userName + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
			return false;
		}


		APPSTORE_UPDATE_IDENTY_REQ req = new APPSTORE_UPDATE_IDENTY_REQ(BossMessageFactory.nextSequnceNum(), id, _identify);

		APPSTORE_UPDATE_IDENTY_RES res = null;
		try {
			res = (APPSTORE_UPDATE_IDENTY_RES) sendMessageAndWaittingResponse(req, 100000);
		} catch (Exception e) {
			logger.error("[添加APPSTORE唯一验证串] [失败] [请求连接错误] [passportid:"+id+"] [username:" + userName + "] [验证串:"+_identify+"] [cost:" + (System.currentTimeMillis() - startTime) + "ms]", e);
			return false;
		}

		if (res != null) {
			if (res.getIsOk()) {
				if (logger.isInfoEnabled())
					logger.info("[添加APPSTORE唯一验证串] [成功] [passportid:"+id+"] [username:" + userName + "] [验证串:"+_identify+"] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
				return res.getIsOk();
			} else {
				logger.error("[添加APPSTORE唯一验证串] [失败] [Boss执行更新失败] [passportid:"+id+"] [username:" + userName + "] [验证串:"+_identify+"] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
				return res.getIsOk();
			}
		} else {
			logger.error("[添加APPSTORE唯一验证串] [失败] [Boss响应超时] [passportid:"+id+"] [username:" + userName + "] [验证串:"+_identify+"] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
			return false;
		}

	}


	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public byte validateQQLogin(String uid, String sessionId) throws Exception {
		long startTime = System.currentTimeMillis();
		QQ_LOGIN_VALIDATE_REQ req = new QQ_LOGIN_VALIDATE_REQ(BossMessageFactory.nextSequnceNum(), uid, sessionId);
		QQ_LOGIN_VALIDATE_RES res = null;
		try {
			res = (QQ_LOGIN_VALIDATE_RES) sendMessageAndWaittingResponse(req, 100000);
		} catch (Exception e) {
			logger.error("[腾讯登录验证] [失败:发生异常] [uid:" + uid + "] [sessionId:" + sessionId + "] [" + (System.currentTimeMillis() - startTime) + "ms]", e);
		}
		if (res != null) {
			logger.error("[腾讯登录验证] [结果:" + res.getResult() + "] [uid:" + uid + "] [sessionId:" + sessionId + "] [" + (System.currentTimeMillis() - startTime)
					+ "ms]");
			return res.getResult();
		}
		return 1;
	}
	/**
	 * 
	 * @param uid
	 * @param sid
	 * @param goodsCount
	 * @param savingType
	 * @return
	 * @throws NullArgumentException
	 * @throws IllegalArgumentException
	 * @throws UIDNotExsitsException
	 * @throws InValidSessionIdException
	 * @throws OrderGenerateException
	 * @throws Exception
	 */

	public String savingForQQUser(String userName,int goodsCount,String savingType,String serverName) 
			throws NullArgumentException,
			IllegalArgumentException,
			UsernameNotExistsException,
			OrderGenerateException,Exception
			{
		long startTime = System.currentTimeMillis();

		if(StringUtils.isEmpty(userName))
		{
			logger.error("[腾讯充值订单生成] [失败:传入的uid为空值] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的uid为空值！");
		}

		if(StringUtils.isEmpty(savingType))
		{
			logger.error("[腾讯充值订单生成] [失败:传入的充值类别为空值] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称："+serverName+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的充值类别为空值！");
		}

		if(StringUtils.isEmpty(serverName))
		{
			logger.error("[腾讯充值订单生成] [失败:传入的ServerName为空值] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称："+serverName+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的ServerName为空值！");
		}

		/*	if(goodsCount < 1)
		{
			logger.error("[腾讯充值订单生成] [失败:传入商品数量小于1] " +
							"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
									"[充值类型:"+savingType+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new IllegalArgumentException("传入商品数量小于1！");
		}*/

		QQ_SAVING_REQ req = new QQ_SAVING_REQ(BossMessageFactory.nextSequnceNum(), userName, goodsCount, savingType, serverName);
		QQ_SAVING_RES res = null;

		try {
			res = (QQ_SAVING_RES) sendMessageAndWaittingResponse(req, 100000);
		} catch (Exception e) {
			logger.error("[腾讯充值订单生成] [失败:发送请求时发生异常] [req sequnceNum:"+req.getSequnceNum()+"] " +
					"[req Type:"+req.getType()+"] [req typeDesc:"+req.getTypeDescription()+"] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [" + (System.currentTimeMillis() - startTime) + "ms]", e);
			throw new Exception("发送请求时出现异常！");
		}

		if (res != null) 
		{
			//结果,0-成功, 1-uid不存在  2-订单生成失败  3-server不存在 
			//如果没有操作成功  
			byte result = res.getResult();

			if(result != 0)
			{
				if(result == 1)
				{
					logger.error("[腾讯充值订单生成] [失败:用户不存在] [req sequnceNum:"+req.getSequnceNum()+"] " +
							"[req Type:"+req.getType()+"] [req typeDesc:"+req.getTypeDescription()+"] " +
							"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [serverName:"+serverName+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
					throw new UsernameNotExistsException("[userName:"+userName+"]不存在！");
				}
				else if(result == 2)
				{
					logger.error("[腾讯充值订单生成] [失败:订单生成失败] [req sequnceNum:"+req.getSequnceNum()+"] " +
							"[req Type:"+req.getType()+"] [req typeDesc:"+req.getTypeDescription()+"] " +
							"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [serverName:"+serverName+"]  [" + (System.currentTimeMillis() - startTime) + "ms]");
					throw new OrderGenerateException("订单生成失败！");
				}
				else if(result == 3)
				{
					logger.error("[腾讯充值订单生成] [失败:服务器不存在] [req sequnceNum:"+req.getSequnceNum()+"] " +
							"[req Type:"+req.getType()+"] [req typeDesc:"+req.getTypeDescription()+"] " +
							"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [serverName:"+serverName+"]  [" + (System.currentTimeMillis() - startTime) + "ms]");
					throw new ServerNameNotExistsException("Server：["+serverName+"]不存在！");
				}
				else
				{
					logger.error("[腾讯充值订单生成] [失败:未定义错误] [resultType:"+res.getResult()+"] [req sequnceNum:"+req.getSequnceNum()+"] " +
							"[req Type:"+req.getType()+"] [req typeDesc:"+req.getTypeDescription()+"] " +
							"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [serverName:"+serverName+"]  [" + (System.currentTimeMillis() - startTime) + "ms]");
					throw new Exception("未定义错误！");
				}
			}
			else
			{
				//如果操作成功
				if(logger.isInfoEnabled())
				{
					logger.info("[腾讯充值订单生成] [成功] " +
							"[req sequnceNum:"+req.getSequnceNum()+"] " +
							"[req Type:"+req.getType()+"] " +
							"[req typeDesc:"+req.getTypeDescription()+"] " +
							"[userName:" + userName + "] " +
							"[商品数量:"+goodsCount+"] [充值类型:"+savingType+"] [serverName:"+serverName+"] [订单id:"+res.getOrderId()+"]" +
							"[" + (System.currentTimeMillis() - startTime) + "ms]");
				}

				return res.getOrderId();
			}

		}
		else
		{
			logger.error("[腾讯充值订单生成] [失败:Boss响应超时] [req sequnceNum:"+req.getSequnceNum()+"] " +
					"[req Type:"+req.getType()+"] [req typeDesc:"+req.getTypeDescription()+"] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [serverName:"+serverName+"] [" + (System.currentTimeMillis() - startTime) + "ms]");

			throw new Exception("Boss响应超时！");
		}
			}
	public String savingForQQUser(String userName,int goodsCount,String savingType,String serverName,String userChannel) 
			throws NullArgumentException,
			IllegalArgumentException,
			UsernameNotExistsException,
			OrderGenerateException,Exception
			{
		long startTime = System.currentTimeMillis();

		if(StringUtils.isEmpty(userName))
		{
			logger.error("[腾讯充值订单生成] [失败:传入的uid为空值] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的uid为空值！");
		}

		if(StringUtils.isEmpty(savingType))
		{
			logger.error("[腾讯充值订单生成] [失败:传入的充值类别为空值] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称："+serverName+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的充值类别为空值！");
		}

		if(StringUtils.isEmpty(serverName))
		{
			logger.error("[腾讯充值订单生成] [失败:传入的ServerName为空值] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称："+serverName+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的ServerName为空值！");
		}

		/*	if(goodsCount < 1)
		{
			logger.error("[腾讯充值订单生成] [失败:传入商品数量小于1] " +
							"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
									"[充值类型:"+savingType+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new IllegalArgumentException("传入商品数量小于1！");
		}*/

		QQ_SAVING_NEW_REQ req = new QQ_SAVING_NEW_REQ(BossMessageFactory.nextSequnceNum(), userName, goodsCount, savingType, serverName,userChannel);
		QQ_SAVING_NEW_RES res = null;

		try {
			res = (QQ_SAVING_NEW_RES) sendMessageAndWaittingResponse(req, 100000);
		} catch (Exception e) {
			logger.error("[腾讯充值订单生成] [失败:发送请求时发生异常] [req sequnceNum:"+req.getSequnceNum()+"] " +
					"[req Type:"+req.getType()+"] [req typeDesc:"+req.getTypeDescription()+"] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [" + (System.currentTimeMillis() - startTime) + "ms]", e);
			throw new Exception("发送请求时出现异常！");
		}

		if (res != null) 
		{
			//结果,0-成功, 1-uid不存在  2-订单生成失败  3-server不存在 
			//如果没有操作成功  
			byte result = res.getResult();

			if(result != 0)
			{
				if(result == 1)
				{
					logger.error("[腾讯充值订单生成] [失败:用户不存在] [req sequnceNum:"+req.getSequnceNum()+"] " +
							"[req Type:"+req.getType()+"] [req typeDesc:"+req.getTypeDescription()+"] " +
							"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [serverName:"+serverName+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
					throw new UsernameNotExistsException("[userName:"+userName+"]不存在！");
				}
				else if(result == 2)
				{
					logger.error("[腾讯充值订单生成] [失败:订单生成失败] [req sequnceNum:"+req.getSequnceNum()+"] " +
							"[req Type:"+req.getType()+"] [req typeDesc:"+req.getTypeDescription()+"] " +
							"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [serverName:"+serverName+"]  [" + (System.currentTimeMillis() - startTime) + "ms]");
					throw new OrderGenerateException("订单生成失败！");
				}
				else if(result == 3)
				{
					logger.error("[腾讯充值订单生成] [失败:服务器不存在] [req sequnceNum:"+req.getSequnceNum()+"] " +
							"[req Type:"+req.getType()+"] [req typeDesc:"+req.getTypeDescription()+"] " +
							"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [serverName:"+serverName+"]  [" + (System.currentTimeMillis() - startTime) + "ms]");
					throw new ServerNameNotExistsException("Server：["+serverName+"]不存在！");
				}
				else
				{
					logger.error("[腾讯充值订单生成] [失败:未定义错误] [resultType:"+res.getResult()+"] [req sequnceNum:"+req.getSequnceNum()+"] " +
							"[req Type:"+req.getType()+"] [req typeDesc:"+req.getTypeDescription()+"] " +
							"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [serverName:"+serverName+"]  [" + (System.currentTimeMillis() - startTime) + "ms]");
					throw new Exception("未定义错误！");
				}
			}
			else
			{
				//如果操作成功
				if(logger.isInfoEnabled())
				{
					logger.info("[腾讯充值订单生成] [成功] " +
							"[req sequnceNum:"+req.getSequnceNum()+"] " +
							"[req Type:"+req.getType()+"] " +
							"[req typeDesc:"+req.getTypeDescription()+"] " +
							"[userName:" + userName + "] " +
							"[商品数量:"+goodsCount+"] [充值类型:"+savingType+"] [serverName:"+serverName+"] [订单id:"+res.getOrderId()+"]" +
							"[" + (System.currentTimeMillis() - startTime) + "ms]");
				}

				return res.getOrderId();
			}

		}
		else
		{
			logger.error("[腾讯充值订单生成] [失败:Boss响应超时] [req sequnceNum:"+req.getSequnceNum()+"] " +
					"[req Type:"+req.getType()+"] [req typeDesc:"+req.getTypeDescription()+"] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [serverName:"+serverName+"] [" + (System.currentTimeMillis() - startTime) + "ms]");

			throw new Exception("Boss响应超时！");
		}
			}



	public String savingForChannelUser(String uid, int goodsCount,String payType,String cardNo,String cardPassword,int payAMount,String serverName,String channel, String mobileOs) throws NullArgumentException,IllegalArgumentException,Exception
	{
		long startTime = System.currentTimeMillis();
		logger.warn("["+Thread.currentThread().getName()+"] [渠道用户充值订单生成] [调用了老方法充值] ["+channel+"] ["+uid+"] ["+payType+"] ["+payAMount+"] ["+serverName+"] ["+mobileOs+"]");
		if(StringUtils.isEmpty(uid))
		{
			logger.error("[渠道用户充值订单生成] [失败:传入的uid为空值] " +
					"[userName:" + uid + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+payType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的uid为空值！");
		}
		/*	
		if(goodsCount < 1)
		{
			logger.error("[渠道充值订单生成] [失败:商品数量小于1] " +
					"[userName:" + uid + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+payType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new IllegalArgumentException("传入的[商品数量:"+goodsCount+"]小于1！");
		} */

		/*		if(StringUtils.isEmpty(payType))
		{
			logger.error("[渠道充值订单生成] [失败:支付方式为空值] " +
					"[userName:" + uid + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+payType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的[充值类型:"+payType+"]为空值！");
		}*/

		/*		if(StringUtils.isEmpty(cardNo))
		{
			logger.error("[渠道充值订单生成] [失败:充值卡号为空值] " +
					"[userName:" + uid + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+payType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的[充值卡号:"+cardNo+"]为空值！");
		}*/

		/*		if(StringUtils.isEmpty(cardPassword))
		{
			logger.error("[渠道充值订单生成] [失败:充值卡密码为空值] " +
					"[userName:" + uid + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+payType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的[充值卡密码:"+cardPassword+"]为空值！");
		}*/

		/*		if(payAMount <= 0)
		{
			logger.error("[渠道充值订单生成] [失败:充值金额小于或等于0] " +
					"[userName:" + uid + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+payType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new IllegalArgumentException("传入的[充值金额:"+payAMount+"]小于等于0！");
		}*/

		if(StringUtils.isEmpty(serverName))
		{
			logger.error("[渠道充值订单生成] [失败:传入的服务器名称为空值] " +
					"[userName:" + uid + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+payType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的[服务器名称:"+serverName+"]为空值！");
		}

		if(StringUtils.isEmpty(channel))
		{
			logger.error("[渠道充值订单生成] [失败:传入的渠道名称为空值] " +
					"[userName:" + uid + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+payType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的[渠道名称:"+channel+"]为空值！");
		}

		if(StringUtils.isEmpty(mobileOs))
		{
			logger.error("[渠道充值订单生成] [失败:传入的手机平台为空值] " +
					"[userName:" + uid + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+payType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的[手机平台名称:"+mobileOs+"]为空值！");
		}

		//构造充值请求
		CHANNEL_UER_SAVING_REQ req = new CHANNEL_UER_SAVING_REQ(BossMessageFactory.nextSequnceNum(), uid, goodsCount,payType,cardNo,cardPassword,payAMount,serverName,channel,mobileOs);
		//发送请求给bosserverservice
		CHANNEL_UER_SAVING_RES res = null;
		try {
			res = (CHANNEL_UER_SAVING_RES)sendMessageAndWaittingResponse(req, 100000);
		} catch (Exception e) {
			logger.error("[渠道充值订单生成] [失败:发送请求时发生异常] [req sequnceNum:"+req.getSequnceNum()+"] " +
					"[req Type:"+req.getType()+"] [req typeDesc:"+req.getTypeDescription()+"] " +
					"[userName:" + uid + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+payType+"] [服务器名称："+serverName+"] [充值卡号："+cardNo+"] [充值卡密码:"+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]", e);
			throw new Exception("发送请求时出现异常！");
		}
		//0-成功, 非0错误
		//如果响应
		if(res != null)
		{
			//如果result == 0
			if(res.getResult() == 0)
			{
				//返回告知订单已经生成
				if(logger.isInfoEnabled())
				{
					logger.info("[渠道充值订单生成] [成功] " +
							"[req sequnceNum:"+req.getSequnceNum()+"] " +
							"[req Type:"+req.getType()+"] " +
							"[req typeDesc:"+req.getTypeDescription()+"] " +
							"[userName:" + uid + "] " +
							"[商品数量:"+goodsCount+"] [充值类型:"+payType+"] [充值卡号："+cardNo+"] [充值卡密码:"+cardPassword+"] [充值金额："+payAMount+"] [渠道:"+channel+"] [服务器名称:"+serverName+"] " +
							"[" + (System.currentTimeMillis() - startTime) + "ms]");
				}

				return res.getDesc();
			}
			else
			{
				//如果result != 0
				logger.error("[渠道充值订单生成] [失败："+res.getDesc()+"] " +
						"[req sequnceNum:"+req.getSequnceNum()+"] " +
						"[req Type:"+req.getType()+"] " +
						"[req typeDesc:"+req.getTypeDescription()+"] " +
						"[userName:" + uid + "] " +
						"[商品数量:"+goodsCount+"] [充值类型:"+payType+"]  [充值卡号："+cardNo+"] [充值卡密码:"+cardPassword+"] [充值金额："+payAMount+"] [渠道:"+channel+"] [服务器名称:"+serverName+"]" +
						"[" + (System.currentTimeMillis() - startTime) + "ms]");
				return res.getDesc();
			}

		}
		//如果没有响应
		else
		{
			logger.error("[渠道充值订单生成] [失败：Boss响应超时] " +
					"[req sequnceNum:"+req.getSequnceNum()+"] " +
					"[req Type:"+req.getType()+"] " +
					"[req typeDesc:"+req.getTypeDescription()+"] " +
					"[userName:" + uid + "] " +
					"[商品数量:"+goodsCount+"] [充值类型:"+payType+"]  [充值卡号："+cardNo+"] [充值卡密码:"+cardPassword+"] [充值金额："+payAMount+"] [渠道:"+channel+"] [服务器名称:"+serverName+"]" +
					"[" + (System.currentTimeMillis() - startTime) + "ms]");
			return new String("服务器响应超时！");

		}
	}


	public String savingForChannelUser(String uid, int goodsCount,String payType,String cardNo,String cardPassword,int payAMount,String serverName,String channel, String mobileOs,String[] others) throws NullArgumentException,IllegalArgumentException,Exception
	{
		long startTime = System.currentTimeMillis();
		if(StringUtils.isEmpty(uid))
		{
			logger.error("[渠道用户充值订单生成] [失败:传入的uid为空值] " +
					"[userName:" + uid + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+payType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的uid为空值！");
		}
		/*	
		if(goodsCount < 1)
		{
			logger.error("[渠道充值订单生成] [失败:商品数量小于1] " +
					"[userName:" + uid + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+payType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new IllegalArgumentException("传入的[商品数量:"+goodsCount+"]小于1！");
		} */

		/*		if(StringUtils.isEmpty(payType))
		{
			logger.error("[渠道充值订单生成] [失败:支付方式为空值] " +
					"[userName:" + uid + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+payType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的[充值类型:"+payType+"]为空值！");
		}*/

		/*		if(StringUtils.isEmpty(cardNo))
		{
			logger.error("[渠道充值订单生成] [失败:充值卡号为空值] " +
					"[userName:" + uid + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+payType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的[充值卡号:"+cardNo+"]为空值！");
		}*/

		/*		if(StringUtils.isEmpty(cardPassword))
		{
			logger.error("[渠道充值订单生成] [失败:充值卡密码为空值] " +
					"[userName:" + uid + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+payType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的[充值卡密码:"+cardPassword+"]为空值！");
		}*/

		/*		if(payAMount <= 0)
		{
			logger.error("[渠道充值订单生成] [失败:充值金额小于或等于0] " +
					"[userName:" + uid + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+payType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new IllegalArgumentException("传入的[充值金额:"+payAMount+"]小于等于0！");
		}*/

		if(StringUtils.isEmpty(serverName))
		{
			logger.error("[渠道充值订单生成] [失败:传入的服务器名称为空值] " +
					"[userName:" + uid + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+payType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的[服务器名称:"+serverName+"]为空值！");
		}

		if(StringUtils.isEmpty(channel))
		{
			logger.error("[渠道充值订单生成] [失败:传入的渠道名称为空值] " +
					"[userName:" + uid + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+payType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的[渠道名称:"+channel+"]为空值！");
		}

		if(StringUtils.isEmpty(mobileOs))
		{
			logger.error("[渠道充值订单生成] [失败:传入的手机平台为空值] " +
					"[userName:" + uid + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+payType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的[手机平台名称:"+mobileOs+"]为空值！");
		}

		//构造充值请求
		CHANNEL_UER_SAVING_NEW_REQ req = new CHANNEL_UER_SAVING_NEW_REQ(BossMessageFactory.nextSequnceNum(), uid, goodsCount,payType,cardNo,cardPassword,payAMount,serverName,channel,mobileOs,others);
		//发送请求给bosserverservice
		CHANNEL_UER_SAVING_NEW_RES res = null;
		try {
			res = (CHANNEL_UER_SAVING_NEW_RES)sendMessageAndWaittingResponse(req, 100000);
		} catch (Exception e) {
			logger.error("[渠道充值订单生成] [失败:发送请求时发生异常] [req sequnceNum:"+req.getSequnceNum()+"] " +
					"[req Type:"+req.getType()+"] [req typeDesc:"+req.getTypeDescription()+"] " +
					"[userName:" + uid + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+payType+"] [服务器名称："+serverName+"] [充值卡号："+cardNo+"] [充值卡密码:"+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [角色:"+others[0]+"] [cardId:"+(others.length > 1?others[1]:"")+"] [" + (System.currentTimeMillis() - startTime) + "ms]", e);
			throw new Exception("发送请求时出现异常！");
		}
		//0-成功, 非0错误
		//如果响应
		if(res != null)
		{
			//如果result == 0
			if(res.getResult() == 0)
			{
				//返回告知订单已经生成
				if(logger.isInfoEnabled())
				{
					logger.info("[渠道充值订单生成] [成功] " +
							"[req sequnceNum:"+req.getSequnceNum()+"] " +
							"[req Type:"+req.getType()+"] " +
							"[req typeDesc:"+req.getTypeDescription()+"] " +
							"[userName:" + uid + "] " +
							"[商品数量:"+goodsCount+"] [充值类型:"+payType+"] [充值卡号："+cardNo+"] [充值卡密码:"+cardPassword+"] [充值金额："+payAMount+"] [渠道:"+channel+"] [角色:"+others[0]+"] [cardId:"+(others.length > 1?others[1]:"")+"] [服务器名称:"+serverName+"] " +
							"[" + (System.currentTimeMillis() - startTime) + "ms]");
				}

				return res.getDesc();
			}
			else
			{
				//如果result != 0
				logger.error("[渠道充值订单生成] [失败："+res.getDesc()+"] " +
						"[req sequnceNum:"+req.getSequnceNum()+"] " +
						"[req Type:"+req.getType()+"] " +
						"[req typeDesc:"+req.getTypeDescription()+"] " +
						"[userName:" + uid + "] " +
						"[商品数量:"+goodsCount+"] [充值类型:"+payType+"]  [充值卡号："+cardNo+"] [充值卡密码:"+cardPassword+"] [充值金额："+payAMount+"] [渠道:"+channel+"] [角色:"+others[0]+"] [cardId:"+(others.length > 1?others[1]:"")+"] [服务器名称:"+serverName+"]" +
						"[" + (System.currentTimeMillis() - startTime) + "ms]");
				return res.getDesc();
			}

		}
		//如果没有响应
		else
		{
			logger.error("[渠道充值订单生成] [失败：Boss响应超时] " +
					"[req sequnceNum:"+req.getSequnceNum()+"] " +
					"[req Type:"+req.getType()+"] " +
					"[req typeDesc:"+req.getTypeDescription()+"] " +
					"[userName:" + uid + "] " +
					"[商品数量:"+goodsCount+"] [充值类型:"+payType+"]  [充值卡号："+cardNo+"] [充值卡密码:"+cardPassword+"] [充值金额："+payAMount+"] [渠道:"+channel+"] [角色:"+others[0]+"] [cardId:"+(others.length > 1?others[1]:"")+"] [服务器名称:"+serverName+"]" +
					"[" + (System.currentTimeMillis() - startTime) + "ms]");
			return new String("服务器响应超时！");

		}
	}



	/**
	 * 玩家提交充值
	 * @param playername	角色名
	 * @param username	用户名
	 * @param servername	服务器名
	 * @param channel	渠道	YOUAI_XUNXIAN, UC_MIESHI....
	 * @param cardtype	卡类型，目前取值： 移动充值卡|联通一卡付|电信充值卡|盛大卡|征途卡|骏网一卡通|久游卡|网易卡|完美卡|搜狐卡
	 * @param cardno	卡号
	 * @param cardpass	卡密
	 * @param mianzhi	卡面值
	 * 
	 * @param goodsType	第三方goods类型，备用
	 * @param amount	数量， 备用
	 * @param orderID	第三方订单，备用
	 */
	public void playerSaving(String os, String playername, String username, String servername, String channel, String cardtype, String cardno, 
			String cardpass, int mianzhi, int goodsType, int amount, String orderID) throws SavingFailedException {
		long startTime = System.currentTimeMillis();
		USER_SAVING_REQ req = new USER_SAVING_REQ(BossMessageFactory.nextSequnceNum(), username, playername, servername, channel, os, cardtype, cardno, cardpass, mianzhi, goodsType, amount, orderID);
		USER_SAVING_RES res = null;
		try {
			res = (USER_SAVING_RES) sendMessageAndWaittingResponse(req, 100000);
			if(logger.isInfoEnabled()) {
				logger.info("[玩家充值提交] [结果:"+(res.getResult()==0?"成功":"失败")+"] [结果描述:"+res.getFailreason()+"] [username:"+username+"] [player:"+req.getPlayername()+"] [当前客户端渠道:"+req.getChannel()+"] [类型:"+req.getCardtype()+"] [卡号:"+req.getCardno()+"] [卡密:"+req.getCardpass()+"] [面值:"+req.getMianzhi()+"] [amount:"+req.getAmount()+"] [goodsType:"+req.getGoodsType()+"] [第三方订单号:"+req.getOrderID()+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			}
		} catch (Exception e) {
			logger.error("[玩家充值提交] [失败] [请求连接错误][username:" + username + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]", e);
			throw new SavingFailedException("请求超时，请稍候再试");
		}
		if(res!=null && res.getResult()==1) {
			throw new SavingFailedException(res.getFailreason());
		}
	}

	public String getServerPlatformUrl(String realName) {
		long startTime = System.currentTimeMillis();
		GET_SERVER_URL_REQ req = new GET_SERVER_URL_REQ(BossMessageFactory.nextSequnceNum(), realName);
		GET_SERVER_URL_RES res = null;
		try {
			res = (GET_SERVER_URL_RES) sendMessageAndWaittingResponse(req, 100000);
			if(logger.isInfoEnabled()) {
				logger.info("[获得服务器后台地址] [结果:"+res.getUrl()+"] ["+realName+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			}
			return res.getUrl();
		} catch (Exception e) {
			logger.error("[获得服务器后台地址:失败] [--] ["+realName+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		}
		return "unknown";
	}

	public long getPlayerLastSavingTime(String username) {
		long startTime = System.currentTimeMillis();
		GET_USER_LAST_SAVING_TIME_REQ req = new GET_USER_LAST_SAVING_TIME_REQ(BossMessageFactory.nextSequnceNum(), username);
		GET_USER_LAST_SAVING_TIME_RES res = null;
		try {
			res = (GET_USER_LAST_SAVING_TIME_RES) sendMessageAndWaittingResponse(req, 100000);
			if(logger.isInfoEnabled()) {
				logger.info("[获得用户最后一次充值时间] [结果:"+res.getSavingTime()+"] ["+username+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			}
			return res.getSavingTime();
		} catch (Exception e) {
			logger.error("[获得用户最后一次充值时间:失败] [--] ["+username+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
		}
		return 0;
	}


	public void playerSaving(String os, String playername, String username, String servername, String channel, String cardtype, String cardno, 
			String cardpass, int mianzhi, int goodsType, int amount, String orderID,String others[]) throws SavingFailedException {
		long startTime = System.currentTimeMillis();
		USER_SAVING_NEW_REQ req = new USER_SAVING_NEW_REQ(BossMessageFactory.nextSequnceNum(), username, playername, servername, channel, os, cardtype, cardno, cardpass, mianzhi, goodsType, amount, orderID,others);
		USER_SAVING_NEW_RES res = null;
		try {
			res = (USER_SAVING_NEW_RES) sendMessageAndWaittingResponse(req, 100000);
			if(logger.isInfoEnabled()) {
				logger.info("[玩家充值提交] [结果:"+(res.getResult()==0?"成功":"失败")+"] [结果描述:"+res.getFailreason()+"] [username:"+username+"] [player:"+req.getPlayername()+"] [当前客户端渠道:"+req.getChannel()+"] [类型:"+req.getCardtype()+"] [卡号:"+req.getCardno()+"] [卡密:"+req.getCardpass()+"] [面值:"+req.getMianzhi()+"] [amount:"+req.getAmount()+"] [goodsType:"+req.getGoodsType()+"] [第三方订单号:"+req.getOrderID()+"] [角色:"+others[0]+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			}
		} catch (Exception e) {
			logger.error("[玩家充值提交] [失败] [请求连接错误][username:" + username + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]", e);
			throw new SavingFailedException("请求超时，请稍候再试");
		}
		if(res!=null && res.getResult()==1) {
			throw new SavingFailedException(res.getFailreason());
		}
	}

	/**
	 * appstore充值校验
	 * @param username
	 * @param channel
	 * @param verify
	 * @return
	 */
	public String appStoreSavingVerify(String username, String channel, String verify) {
		long startTime = System.currentTimeMillis();
		APPSTORE_SAVING_VERIFY_REQ req = new APPSTORE_SAVING_VERIFY_REQ(BossMessageFactory.nextSequnceNum(), username, channel, verify);
		APPSTORE_SAVING_VERIFY_RES res = null;
		try {
			res = (APPSTORE_SAVING_VERIFY_RES) sendMessageAndWaittingResponse(req, 100000);
			if(logger.isInfoEnabled()) {
				logger.info("[appstore充值校验] [结果:"+(res.getResult()==0?"成功":"失败")+"] [error:"+res.getDescription()+"] [username:"+username+"] [当前客户端渠道:"+req.getChannel()+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			}
		} catch (Exception e) {
			logger.error("[appstore充值校验] [失败] [请求连接错误][username:" + username + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]", e);
		}
		if(res!=null) {
			return res.getDescription();
		}
		return "尊敬的用户，您目前无法充值，请联系GM";
	}

	/**
	 * AppStore应用商店充值
	 * @param username
	 * @param servername
	 * @param channel
	 * @param receipt
	 * @throws SavingFailedException
	 */
	public void appStoreSaving(String username, String servername, String channel, String receipt, String deviceCode) throws SavingFailedException {
		long startTime = System.currentTimeMillis();
		APPSTORE_RECEIPT_REQ req = new APPSTORE_RECEIPT_REQ(BossMessageFactory.nextSequnceNum(), username, channel, servername, receipt, deviceCode);
		APPSTORE_RECEIPT_RES res = null;
		try {
			res = (APPSTORE_RECEIPT_RES) sendMessageAndWaittingResponse(req, 100000);
			if(logger.isInfoEnabled()) {
				logger.info("[玩家充值提交] [结果:"+(res.getResult()==0?"成功":"失败")+"] [username:"+username+"] [当前客户端渠道:"+req.getChannel()+"] [receipt:"+receipt+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			}
		} catch (Exception e) {
			logger.error("[充值调用] [失败] [请求连接错误] [username:" + username + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]", e);
			throw new SavingFailedException("请求超时，请稍候再试");
		}
		if(res!=null && res.getResult()==1) {
			throw new SavingFailedException("AppStore充值返回生成订单失败");
		}
	}


	public void appStoreSaving(String username, String servername, String channel, String receipt, String deviceCode,String[]others) throws SavingFailedException {
		long startTime = System.currentTimeMillis();
		APPSTORE_RECEIPT_NEW_REQ req = new APPSTORE_RECEIPT_NEW_REQ(BossMessageFactory.nextSequnceNum(), username, channel, servername, receipt, deviceCode,others);
		APPSTORE_RECEIPT_NEW_RES res = null;
		try {
			res = (APPSTORE_RECEIPT_NEW_RES) sendMessageAndWaittingResponse(req, 100000);
			if(logger.isInfoEnabled()) {
				logger.info("[玩家充值提交] [结果:"+(res.getResult()==0?"成功":"失败")+"] [username:"+username+"] [当前客户端渠道:"+req.getChannel()+"] [receipt:"+receipt+"] [角色:"+others[0]+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			}
		} catch (Exception e) {
			logger.error("[充值调用] [失败] [请求连接错误] [username:" + username + "] [角色:"+others[0]+"] [cost:" + (System.currentTimeMillis() - startTime) + "ms]", e);
			throw new SavingFailedException("请求超时，请稍候再试");
		}
		if(res!=null && res.getResult()==1) {
			throw new SavingFailedException("AppStore充值返回生成订单失败");
		}
	}

	/**
	 * 获得充值记录
	 * @param username
	 * @param pageNum
	 * @param pageIndex
	 * @return
	 */
	public SavingPageRecord getSavingRecord(String username, int pageNum, int pageIndex) {
		long startTime = System.currentTimeMillis();
		SAVING_RECORD_REQ req = new SAVING_RECORD_REQ(BossMessageFactory.nextSequnceNum(), username, pageNum, pageIndex);
		try {
			SAVING_RECORD_RES res = (SAVING_RECORD_RES) sendMessageAndWaittingResponse(req, 100000);
			SavingPageRecord record = new SavingPageRecord();
			SavingRecord sr[] = new SavingRecord[res.getCreateTime().length];
			for(int i=0; i<sr.length; i++) {
				SavingRecord r = new SavingRecord();
				r.setCreateTime(res.getCreateTime()[i]);
				r.setOrderId(res.getOrderId()[i]);
				r.setSavingAmount(res.getSavingAmount()[i]);
				r.setSavingMedium(res.getSavingMedium()[i]);
				r.setStatusDesp(res.getStatusDesp()[i]);
				sr[i] = r;
			}
			record.setRecords(sr);
			record.setTotalPageCount(res.getSavingPageNum());
			record.setTotalRecordNum(res.getSavingTotalNum());
			if(logger.isInfoEnabled()) {
				logger.info("[充值记录查询] ["+username+"] [pageNum:"+pageNum+"] [pageIndex:"+pageIndex+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			}
			return record;
		} catch (Exception e) {
			logger.error("[充值记录查询] [失败] ["+username+"] [pageNum:"+pageNum+"] [pageIndex:"+pageIndex+"] ["+(System.currentTimeMillis()-startTime)+"ms]", e);
		}
		return null;
	}	  

	/* 获得支付宝参数
	 * @return
	 */
	@Deprecated
	public AlipayArgs getAlipayArgs() {
		long startTime = System.currentTimeMillis();
		ALIPAY_ARGS_REQ req = new ALIPAY_ARGS_REQ(BossMessageFactory.nextSequnceNum());
		try {
			ALIPAY_ARGS_RES res = (ALIPAY_ARGS_RES) sendMessageAndWaittingResponse(req, 100000);
			AlipayArgs args = new AlipayArgs();
			args.setAliPublicKey(res.getAliPublicKey());
			args.setNotifyUrl(res.getNotifyUrl());
			args.setOwnPrivateKey(res.getOwnPrivateKey());
			args.setOwnPublicKey(res.getOwnPublicKey());
			args.setPartnerID(res.getPartnerID());
			args.setSellerID(res.getSellerID());
			if(logger.isDebugEnabled()) {
				logger.debug("[获得支付宝参数] [partnerID:"+args.getPartnerID()+"] [sellerID:"+args.getSellerID()+"] [aliPublicKey:"+args.getAliPublicKey()+"] [ownPublicKey:"+args.getOwnPublicKey()+"] [ownPrivateKey:"+args.getOwnPrivateKey()+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			}
			return args;
		} catch (Exception e) {
			logger.error("[获得支付宝参数] [失败] ["+(System.currentTimeMillis()-startTime)+"ms]", e);
		}
		return null;
	}

	/**
	 * 生成支付宝订单
	 * @param username
	 * @param channel
	 * @param servername
	 * @param payAmount
	 * @return
	 */
	public AliOrderInfo getAlipayOrderID(String username, String channel, String servername, long payAmount) {
		long startTime = System.currentTimeMillis();
		ALIPAY_GET_ORDERID_REQ req = new ALIPAY_GET_ORDERID_REQ(BossMessageFactory.nextSequnceNum(), username, channel, servername, payAmount);
		try {
			ALIPAY_GET_ORDERID_RES res = (ALIPAY_GET_ORDERID_RES) sendMessageAndWaittingResponse(req, 100000);
			if(logger.isInfoEnabled()) {
				logger.info("[生成支付宝订单] [username:"+username+"] ["+payAmount+"] [channel:"+channel+"] [servername:"+servername+"] [orderID:"+res.getOrderId()+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			}
			String orderId = res.getOrderId();
			String orderInfo = res.getOrderInfo();
			String publicKey = res.getPublicKey();
			AliOrderInfo info = new AliOrderInfo();
			info.setOrderId(orderId);
			info.setOrderInfo(orderInfo);
			info.setPublicKey(publicKey);
			return info;
		} catch (Exception e) {
			logger.error("[生成支付宝订单] [username:"+username+"] ["+payAmount+"] [channel:"+channel+"] [servername:"+servername+"] ["+(System.currentTimeMillis()-startTime)+"ms]", e);
		}
		return null;
	}

	public AliOrderInfo getAlipayOrderID(String username, String channel, String servername, long payAmount,String[]others) {
		long startTime = System.currentTimeMillis();
		ALIPAY_GET_ORDERID_NEW_REQ req = new ALIPAY_GET_ORDERID_NEW_REQ(BossMessageFactory.nextSequnceNum(), username, channel, servername, payAmount,others);
		try {
			ALIPAY_GET_ORDERID_NEW_RES res = (ALIPAY_GET_ORDERID_NEW_RES) sendMessageAndWaittingResponse(req, 100000);
			if(logger.isInfoEnabled()) {
				logger.info("[生成支付宝订单] [username:"+username+"] ["+payAmount+"] [channel:"+channel+"] [servername:"+servername+"] [orderID:"+res.getOrderId()+"] [角色:"+others[0]+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			}
			String orderId = res.getOrderId();
			String orderInfo = res.getOrderInfo();
			String publicKey = res.getPublicKey();
			AliOrderInfo info = new AliOrderInfo();
			info.setOrderId(orderId);
			info.setOrderInfo(orderInfo);
			info.setPublicKey(publicKey);
			return info;
		} catch (Exception e) {
			logger.error("[生成支付宝订单] [username:"+username+"] ["+payAmount+"] [channel:"+channel+"] [servername:"+servername+"] [角色:"+others[0]+"] ["+(System.currentTimeMillis()-startTime)+"ms]", e);
		}
		return null;
	}

	/**
	 * 通知订单状态发生了变化
	 * @param orderId
	 * @param channel
	 * @param status
	 * @param statusDesp
	 * @return
	 */
	public void notifyOrderStatusChanged(String username, String orderId, String channel, String status, String statusDesp) {
		long startTime = System.currentTimeMillis();
		ORDER_STATUS_CHANGE_REQ req = new ORDER_STATUS_CHANGE_REQ(BossMessageFactory.nextSequnceNum(), orderId, channel, status, statusDesp);
		try {
			ORDER_STATUS_CHANGE_RES res = (ORDER_STATUS_CHANGE_RES) sendMessageAndWaittingResponse(req, 100000);
			if(logger.isInfoEnabled()) {
				logger.info("[通知订单发生了变化] [username:"+username+"] ["+orderId+"] [channel:"+channel+"] [status:"+status+"] [statusDesp:"+statusDesp+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			}
		} catch (Exception e) {
			logger.error("[通知订单发生了变化] [username:"+username+"] ["+orderId+"] [channel:"+channel+"] [status:"+status+"] [statusDesp:"+statusDesp+"] ["+(System.currentTimeMillis()-startTime)+"ms]", e);
		}
	}

	/**
	 * 通知订单状态发生了变化
	 * @param username
	 * @param orderId
	 * @param channel
	 * @param handleStatus
	 * @param handleStatusDesp
	 * @param responseStatus
	 * @param responseStatusDesp
	 */
	public void notifyOrderStatusChanged(String username, String orderId, String channel, String handleStatus, String handleStatusDesp,String responseStatus, String responseStatusDesp) {
		long startTime = System.currentTimeMillis();
		ORDER_STATUS_NOTIFY_REQ req = new ORDER_STATUS_NOTIFY_REQ(BossMessageFactory.nextSequnceNum(), orderId, channel, handleStatus, handleStatusDesp,responseStatus,responseStatusDesp);
		try {
			ORDER_STATUS_NOTIFY_RES res = (ORDER_STATUS_NOTIFY_RES) sendMessageAndWaittingResponse(req, 100000);
			if(logger.isInfoEnabled()) {
				logger.info("[通知订单发生了变化] [username:"+username+"] ["+orderId+"] [channel:"+channel+"] [handleStatus:"+handleStatus+"] [handleStatusDesp:"+handleStatusDesp+"] [responseStatus:"+responseStatus+"] [responseStatusDesp:"+responseStatusDesp+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			}
		} catch (Exception e) {
			logger.error("[通知订单发生了变化] [username:"+username+"] ["+orderId+"] [channel:"+channel+"] [handleStatus:"+handleStatus+"] [handleStatusDesp:"+handleStatusDesp+"] [responseStatus:"+responseStatus+"] [responseStatusDesp:"+responseStatusDesp+"] ["+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
	}

	/**
	 * 通知订单状态发生了变化
	 */
	public String notifyOrderStatusChanged(String username, String orderId, String channel, String status, String statusDesp,String zoneid,String pfkey,String pay_token,String [] args) {
		long startTime = System.currentTimeMillis();
		MORE_ARGS_ORDER_STATUS_CHANGE_REQ req = new MORE_ARGS_ORDER_STATUS_CHANGE_REQ(BossMessageFactory.nextSequnceNum(), orderId, channel, status, statusDesp, zoneid, pfkey, pay_token, args);
		try {
			MORE_ARGS_ORDER_STATUS_CHANGE_RES res = (MORE_ARGS_ORDER_STATUS_CHANGE_RES) sendMessageAndWaittingResponse(req, 100000);
			if(logger.isInfoEnabled()) {
				logger.info("[多参数,通知订单发生了变化] [成功] [result:"+(res==null?"null":res.getResult())+"] [username:"+username+"] ["+orderId+"] [channel:"+channel+"] [status:"+status+"] [statusDesp:"+statusDesp+"] [pfkey:"+pfkey+"] [pay_token:"+pay_token+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			}
			if(res !=  null){
				return res.getResult();
			}
		} catch (Exception e) {
			logger.error("[多参数,通知订单发生了变化] [异常] [username:"+username+"] ["+orderId+"] [channel:"+channel+"] [status:"+status+"] [statusDesp:"+statusDesp+"] [pfkey:"+pfkey+"] [pay_token:"+pay_token+"] ["+(System.currentTimeMillis()-startTime)+"ms]", e);
		}
		return "";
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public MessageFactory getMessageFactory() {
		return BossMessageFactory.getInstance();
	}

	//91充值方法

	public String savingFor91User(String userName, int goodsCount,String savingType,String cardNo,String cardPassword,int payAMount,String serverName,String channel, String mobileOs) 
			throws NullArgumentException,
			IllegalArgumentException,
			UsernameNotExistsException,
			OrderGenerateException,Exception
			{
		long startTime = System.currentTimeMillis();

		if(StringUtils.isEmpty(userName))
		{
			logger.error("[91用户充值订单生成] [失败:传入的userName为空值] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的userName为空值！");
		}

		if(goodsCount < 1)
		{
			logger.error("[91用户充值订单生成] [失败:商品数量小于1] " +
					"[userName:" + userName+ "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new IllegalArgumentException("传入的[商品数量:"+goodsCount+"]小于1！");
		}

		if(StringUtils.isEmpty(savingType))
		{
			logger.error("[91用户充值订单生成] [失败:支付方式为空值] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的[充值类型:"+savingType+"]为空值！");
		}

		/*		if(StringUtils.isEmpty(cardNo))
		{
			logger.error("[91用户充值订单生成] [失败:充值卡号为空值] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的[充值卡号:"+cardNo+"]为空值！");
		}

		if(StringUtils.isEmpty(cardPassword))
		{
			logger.error("[91用户充值订单生成] [失败:充值卡密码为空值] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的[充值卡密码:"+cardPassword+"]为空值！");
		}*/

		if(payAMount <= 0)
		{
			logger.error("[91用户充值订单生成] [失败:充值金额小于或等于0] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new IllegalArgumentException("传入的[充值金额:"+payAMount+"]小于等于0！");
		}

		/*		if(StringUtils.isEmpty(serverName))
		{
			logger.error("[91用户充值订单生成] [失败:传入的服务器名称为空值] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的[服务器名称:"+serverName+"]为空值！");
		}*/

		if(StringUtils.isEmpty(channel))
		{
			logger.error("[91用户充值订单生成] [失败:传入的渠道名称为空值] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的[渠道名称:"+channel+"]为空值！");
		}

		if(StringUtils.isEmpty(mobileOs))
		{
			logger.error("[91用户充值订单生成] [失败:传入的手机平台为空值] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的[手机平台名称:"+mobileOs+"]为空值！");
		}

		/*	if(goodsCount < 1)
		{
			logger.error("[腾讯充值订单生成] [失败:传入商品数量小于1] " +
							"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
									"[充值类型:"+savingType+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new IllegalArgumentException("传入商品数量小于1！");
		}*/

		NINEONE_SAVING_REQ req = new NINEONE_SAVING_REQ(BossMessageFactory.nextSequnceNum(), userName, goodsCount,savingType,cardNo,cardPassword,payAMount,serverName,channel,mobileOs);
		NINEONE_SAVING_RES res = null;

		try {
			res = (NINEONE_SAVING_RES) sendMessageAndWaittingResponse(req, 100000);
		} catch (Exception e) {
			logger.error("[91用户充值订单生成] [失败:请求连接错误] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]",e);
			throw new Exception("发送请求时出现异常！");
		}

		if (res != null) 
		{
			//结果,0-成功, 1-uid不存在  2-订单生成失败  3-server不存在 
			//如果没有操作成功  
			byte result = res.getResult();

			if(result != 0)
			{
				if(result == 1)
				{
					logger.error("[91用户充值订单生成] [失败:用户不存在] " +
							"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
					throw new UsernameNotExistsException("[userName:"+userName+"]不存在！");
				}
				else if(result == 2)
				{
					logger.error("[91用户充值订单生成] [失败:订单生成失败] " +
							"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
					throw new OrderGenerateException("订单生成失败！");
				}
				else
				{
					logger.error("[91用户充值订单生成] [失败:未知错误] " +
							"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
					throw new Exception("未定义错误！");
				}
			}
			else
			{
				//如果操作成功
				if(logger.isInfoEnabled())
				{
					logger.info("[91用户充值订单生成] [失败:传入的手机平台为空值] " +
							"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
				}

				return res.getOrderId();
			}

		}
		else
		{
			logger.error("[91用户充值订单生成] [失败:Boss未响应] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");

			throw new Exception("Boss响应超时！");
		}
			}


	public String savingFor91User(String userName, int goodsCount,String savingType,String cardNo,String cardPassword,int payAMount,String serverName,String channel, String mobileOs,String[] others) 
			throws NullArgumentException,
			IllegalArgumentException,
			UsernameNotExistsException,
			OrderGenerateException,Exception
			{
		long startTime = System.currentTimeMillis();

		if(StringUtils.isEmpty(userName))
		{
			logger.error("[91用户充值订单生成] [失败:传入的userName为空值] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的userName为空值！");
		}

		if(goodsCount < 1)
		{
			logger.error("[91用户充值订单生成] [失败:商品数量小于1] " +
					"[userName:" + userName+ "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new IllegalArgumentException("传入的[商品数量:"+goodsCount+"]小于1！");
		}

		if(StringUtils.isEmpty(savingType))
		{
			logger.error("[91用户充值订单生成] [失败:支付方式为空值] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的[充值类型:"+savingType+"]为空值！");
		}

		/*		if(StringUtils.isEmpty(cardNo))
		{
			logger.error("[91用户充值订单生成] [失败:充值卡号为空值] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的[充值卡号:"+cardNo+"]为空值！");
		}

		if(StringUtils.isEmpty(cardPassword))
		{
			logger.error("[91用户充值订单生成] [失败:充值卡密码为空值] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的[充值卡密码:"+cardPassword+"]为空值！");
		}*/

		if(payAMount <= 0)
		{
			logger.error("[91用户充值订单生成] [失败:充值金额小于或等于0] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new IllegalArgumentException("传入的[充值金额:"+payAMount+"]小于等于0！");
		}

		/*		if(StringUtils.isEmpty(serverName))
		{
			logger.error("[91用户充值订单生成] [失败:传入的服务器名称为空值] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的[服务器名称:"+serverName+"]为空值！");
		}*/

		if(StringUtils.isEmpty(channel))
		{
			logger.error("[91用户充值订单生成] [失败:传入的渠道名称为空值] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的[渠道名称:"+channel+"]为空值！");
		}

		if(StringUtils.isEmpty(mobileOs))
		{
			logger.error("[91用户充值订单生成] [失败:传入的手机平台为空值] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的[手机平台名称:"+mobileOs+"]为空值！");
		}

		/*	if(goodsCount < 1)
		{
			logger.error("[腾讯充值订单生成] [失败:传入商品数量小于1] " +
							"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
									"[充值类型:"+savingType+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new IllegalArgumentException("传入商品数量小于1！");
		}*/

		NINEONE_SAVING_NEW_REQ req = new NINEONE_SAVING_NEW_REQ(BossMessageFactory.nextSequnceNum(), userName, goodsCount,savingType,cardNo,cardPassword,payAMount,serverName,channel,mobileOs,others);
		NINEONE_SAVING_NEW_RES res = null;

		try {
			res = (NINEONE_SAVING_NEW_RES) sendMessageAndWaittingResponse(req, 100000);
		} catch (Exception e) {
			logger.error("[91用户充值订单生成] [失败:请求连接错误] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [角色:"+others[0]+"] [" + (System.currentTimeMillis() - startTime) + "ms]",e);
			throw new Exception("发送请求时出现异常！");
		}

		if (res != null) 
		{
			//结果,0-成功, 1-uid不存在  2-订单生成失败  3-server不存在 
			//如果没有操作成功  
			byte result = res.getResult();

			if(result != 0)
			{
				if(result == 1)
				{
					logger.error("[91用户充值订单生成] [失败:用户不存在] " +
							"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [角色:"+others[0]+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
					throw new UsernameNotExistsException("[userName:"+userName+"]不存在！");
				}
				else if(result == 2)
				{
					logger.error("[91用户充值订单生成] [失败:订单生成失败] " +
							"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [角色:"+others[0]+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
					throw new OrderGenerateException("订单生成失败！");
				}
				else
				{
					logger.error("[91用户充值订单生成] [失败:未知错误] " +
							"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [角色:"+others[0]+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
					throw new Exception("未定义错误！");
				}
			}
			else
			{
				//如果操作成功
				if(logger.isInfoEnabled())
				{
					logger.info("[91用户充值订单生成] [失败:传入的手机平台为空值] " +
							"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [角色:"+others[0]+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
				}

				return res.getOrderId();
			}

		}
		else
		{
			logger.error("[91用户充值订单生成] [失败:Boss未响应] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [角色:"+others[0]+"] [" + (System.currentTimeMillis() - startTime) + "ms]");

			throw new Exception("Boss响应超时！");
		}
			}





	//应用汇充值方法

	public String savingForAppChinaUser(String userName, int goodsCount,String savingType,String cardNo,String cardPassword,int payAMount,String serverName,String channel, String mobileOs) 
			throws NullArgumentException,
			IllegalArgumentException,
			UsernameNotExistsException,
			OrderGenerateException,Exception
			{
		long startTime = System.currentTimeMillis();

		if(StringUtils.isEmpty(userName))
		{
			logger.error("[应用汇用户充值订单生成] [失败:传入的userName为空值] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的userName为空值！");
		}

		if(goodsCount < 1)
		{
			logger.error("[应用汇用户充值订单生成] [失败:商品数量小于1] " +
					"[userName:" + userName+ "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new IllegalArgumentException("传入的[商品数量:"+goodsCount+"]小于1！");
		}

		if(StringUtils.isEmpty(savingType))
		{
			logger.error("[应用汇用户充值订单生成] [失败:支付方式为空值] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的[充值类型:"+savingType+"]为空值！");
		}
		/*	
			if(StringUtils.isEmpty(cardNo))
			{
				logger.error("[应用汇用户充值订单生成] [失败:充值卡号为空值] " +
						"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
								"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
				throw new NullArgumentException("传入的[充值卡号:"+cardNo+"]为空值！");
			}

			if(StringUtils.isEmpty(cardPassword))
			{
				logger.error("[应用汇用户充值订单生成] [失败:充值卡密码为空值] " +
						"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
								"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
				throw new NullArgumentException("传入的[充值卡密码:"+cardPassword+"]为空值！");
			}*/

		if(payAMount <= 0)
		{
			logger.error("[应用汇用户充值订单生成] [失败:充值金额小于或等于0] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new IllegalArgumentException("传入的[充值金额:"+payAMount+"]小于等于0！");
		}

		if(StringUtils.isEmpty(serverName))
		{
			logger.error("[应用汇用户充值订单生成] [失败:传入的服务器名称为空值] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的[服务器名称:"+serverName+"]为空值！");
		}

		if(StringUtils.isEmpty(channel))
		{
			logger.error("[应用汇用户充值订单生成] [失败:传入的渠道名称为空值] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的[渠道名称:"+channel+"]为空值！");
		}

		if(StringUtils.isEmpty(mobileOs))
		{
			logger.error("[应用汇用户充值订单生成] [失败:传入的手机平台为空值] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的[手机平台名称:"+mobileOs+"]为空值！");
		}

		/*	if(goodsCount < 1)
			{
				logger.error("[腾讯充值订单生成] [失败:传入商品数量小于1] " +
								"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
										"[充值类型:"+savingType+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
				throw new IllegalArgumentException("传入商品数量小于1！");
			}*/

		APPCHINA_SAVING_REQ req = new APPCHINA_SAVING_REQ(BossMessageFactory.nextSequnceNum(), userName, goodsCount,savingType,cardNo,cardPassword,payAMount,serverName,channel,mobileOs);
		APPCHINA_SAVING_RES res = null;

		try {
			res = (APPCHINA_SAVING_RES) sendMessageAndWaittingResponse(req, 100000);
		} catch (Exception e) {
			logger.error("[应用汇用户充值订单生成] [失败:请求连接错误] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]",e);
			throw new Exception("发送请求时出现异常！");
		}

		if (res != null) 
		{
			//结果,0-成功, 1-uid不存在  2-订单生成失败  3-server不存在 
			//如果没有操作成功  
			byte result = res.getResult();

			if(result != 0)
			{
				if(result == 1)
				{
					logger.error("[应用汇用户充值订单生成] [失败:用户不存在] " +
							"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
					throw new UsernameNotExistsException("[userName:"+userName+"]不存在！");
				}
				else if(result == 2)
				{
					logger.error("[应用汇用户充值订单生成] [失败:订单生成失败] " +
							"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
					throw new OrderGenerateException("订单生成失败！");
				}
				else
				{
					logger.error("[应用汇用户充值订单生成] [失败:未知错误] " +
							"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
					throw new Exception("未定义错误！");
				}
			}
			else
			{
				//如果操作成功
				if(logger.isInfoEnabled())
				{
					logger.info("[应用汇用户充值订单生成] [失败:传入的手机平台为空值] " +
							"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
				}

				return res.getOrderId();
			}

		}
		else
		{
			logger.error("[应用汇用户充值订单生成] [失败:Boss未响应] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");

			throw new Exception("Boss响应超时！");
		}
			}

	public String savingForAppChinaUser(String userName, int goodsCount,String savingType,String cardNo,String cardPassword,int payAMount,String serverName,String channel, String mobileOs,String[]others) 
			throws NullArgumentException,
			IllegalArgumentException,
			UsernameNotExistsException,
			OrderGenerateException,Exception
			{
		long startTime = System.currentTimeMillis();

		if(StringUtils.isEmpty(userName))
		{
			logger.error("[应用汇用户充值订单生成] [失败:传入的userName为空值] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的userName为空值！");
		}

		if(goodsCount < 1)
		{
			logger.error("[应用汇用户充值订单生成] [失败:商品数量小于1] " +
					"[userName:" + userName+ "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new IllegalArgumentException("传入的[商品数量:"+goodsCount+"]小于1！");
		}

		if(StringUtils.isEmpty(savingType))
		{
			logger.error("[应用汇用户充值订单生成] [失败:支付方式为空值] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的[充值类型:"+savingType+"]为空值！");
		}
		/*	
			if(StringUtils.isEmpty(cardNo))
			{
				logger.error("[应用汇用户充值订单生成] [失败:充值卡号为空值] " +
						"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
								"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
				throw new NullArgumentException("传入的[充值卡号:"+cardNo+"]为空值！");
			}

			if(StringUtils.isEmpty(cardPassword))
			{
				logger.error("[应用汇用户充值订单生成] [失败:充值卡密码为空值] " +
						"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
								"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
				throw new NullArgumentException("传入的[充值卡密码:"+cardPassword+"]为空值！");
			}*/

		if(payAMount <= 0)
		{
			logger.error("[应用汇用户充值订单生成] [失败:充值金额小于或等于0] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new IllegalArgumentException("传入的[充值金额:"+payAMount+"]小于等于0！");
		}

		if(StringUtils.isEmpty(serverName))
		{
			logger.error("[应用汇用户充值订单生成] [失败:传入的服务器名称为空值] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的[服务器名称:"+serverName+"]为空值！");
		}

		if(StringUtils.isEmpty(channel))
		{
			logger.error("[应用汇用户充值订单生成] [失败:传入的渠道名称为空值] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的[渠道名称:"+channel+"]为空值！");
		}

		if(StringUtils.isEmpty(mobileOs))
		{
			logger.error("[应用汇用户充值订单生成] [失败:传入的手机平台为空值] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的[手机平台名称:"+mobileOs+"]为空值！");
		}

		/*	if(goodsCount < 1)
			{
				logger.error("[腾讯充值订单生成] [失败:传入商品数量小于1] " +
								"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
										"[充值类型:"+savingType+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
				throw new IllegalArgumentException("传入商品数量小于1！");
			}*/

		APPCHINA_SAVING_NEW_REQ req = new APPCHINA_SAVING_NEW_REQ(BossMessageFactory.nextSequnceNum(), userName, goodsCount,savingType,cardNo,cardPassword,payAMount,serverName,channel,mobileOs,others);
		APPCHINA_SAVING_NEW_RES res = null;

		try {
			res = (APPCHINA_SAVING_NEW_RES) sendMessageAndWaittingResponse(req, 100000);
		} catch (Exception e) {
			logger.error("[应用汇用户充值订单生成] [失败:请求连接错误] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [角色:"+others[0]+"] [" + (System.currentTimeMillis() - startTime) + "ms]",e);
			throw new Exception("发送请求时出现异常！");
		}

		if (res != null) 
		{
			//结果,0-成功, 1-uid不存在  2-订单生成失败  3-server不存在 
			//如果没有操作成功  
			byte result = res.getResult();

			if(result != 0)
			{
				if(result == 1)
				{
					logger.error("[应用汇用户充值订单生成] [失败:用户不存在] " +
							"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [角色:"+others[0]+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
					throw new UsernameNotExistsException("[userName:"+userName+"]不存在！");
				}
				else if(result == 2)
				{
					logger.error("[应用汇用户充值订单生成] [失败:订单生成失败] " +
							"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [角色:"+others[0]+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
					throw new OrderGenerateException("订单生成失败！");
				}
				else
				{
					logger.error("[应用汇用户充值订单生成] [失败:未知错误] " +
							"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [角色:"+others[0]+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
					throw new Exception("未定义错误！");
				}
			}
			else
			{
				//如果操作成功
				if(logger.isInfoEnabled())
				{
					logger.info("[应用汇用户充值订单生成] [失败:传入的手机平台为空值] " +
							"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [角色:"+others[0]+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
				}

				return res.getOrderId();
			}

		}
		else
		{
			logger.error("[应用汇用户充值订单生成] [失败:Boss未响应] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [充值卡号："+cardNo+"] [充值卡密码："+cardPassword+"] [充值金额:"+payAMount+"] [渠道:"+channel+"] [角色:"+others[0]+"] [" + (System.currentTimeMillis() - startTime) + "ms]");

			throw new Exception("Boss响应超时！");
		}
			}



	public int createUcidToUserName(String ucid,String username,String userchannel)
	{
		long startTime = System.currentTimeMillis();
		SAVING_UCIDTOUSERNAME_REQ req = new SAVING_UCIDTOUSERNAME_REQ(BossMessageFactory.nextSequnceNum(), ucid,username,userchannel);
		SAVING_UCIDTOUSERNAME_RES res = null;
		int result = 0;
		try {
			res = (SAVING_UCIDTOUSERNAME_RES) sendMessageAndWaittingResponse(req, 100000);
		} catch (Exception e) {
			result = 2;
			logger.error("[创建ucidtousername] [失败] [出现异常] [ucid:"+ucid+"] [username:"+username+"] [userchannel:"+userchannel+"] [cost:"+(System.currentTimeMillis()-startTime)+"]ms" , e);
			return result;
		}

		if(res != null)
			result = res.getResult();
		else
			result = 2;

		if(result == 0)
		{
			if(logger.isInfoEnabled())
			{
				logger.info("[创建ucidtousername] [成功] [ok] [ucid:"+ucid+"] [username:"+username+"] [userchannel:"+userchannel+"] [cost:"+(System.currentTimeMillis()-startTime)+"]ms");
			}
		}
		else if(result == 1)
		{
			if(logger.isInfoEnabled())
			{
				logger.info("[创建ucidtousername] [失败] [要存入的ucid已经在数据库中存在] [ucid:"+ucid+"] [username:"+username+"] [userchannel:"+userchannel+"] [cost:"+(System.currentTimeMillis()-startTime)+"]ms");
			}
		}
		else
		{
			if(logger.isInfoEnabled())
			{
				logger.info("[创建ucidtousername] [失败] [出现内部错误] [ucid:"+ucid+"] [username:"+username+"] [userchannel:"+userchannel+"] [cost:"+(System.currentTimeMillis()-startTime)+"]ms");
			}
		}

		return result;
	}

	public String getUserNameByUcid(String ucid)
	{
		long startTime = System.currentTimeMillis();
		GET_USERNAMEBYUCID_REQ req = new GET_USERNAMEBYUCID_REQ(BossMessageFactory.nextSequnceNum(), ucid);
		GET_USERNAMEBYUCID_RES res = null;

		try {
			res = (GET_USERNAMEBYUCID_RES)sendMessageAndWaittingResponse(req, 100000);
		} catch (Exception e) {
			logger.error("[根据ucid获取游戏账号] [失败] [出现异常] [ucid:"+ucid+"] [username:--] [cost:"+(System.currentTimeMillis()-startTime)+"]ms",e);
			return "";
		}

		if(res != null)
		{
			if(StringUtils.isEmpty(res.getUsername()))
			{
				if(logger.isInfoEnabled())
					logger.info("[根据ucid获取游戏账号] [失败] [没有获取到对应的游戏账号] [ucid:"+ucid+"] [username:"+res.getUsername()+"] [cost:"+(System.currentTimeMillis()-startTime)+"]ms");
			}
			else
			{
				if(logger.isInfoEnabled())
					logger.info("[根据ucid获取游戏账号] [成功] [ok] [ucid:"+ucid+"] [username:"+res.getUsername()+"] [cost:"+(System.currentTimeMillis()-startTime)+"]ms");
			}
			return res.getUsername();
		}
		else
		{
			if(logger.isInfoEnabled())
				logger.info("[根据ucid获取游戏账号] [失败] [服务器无响应或出现错误] [ucid:"+ucid+"] [username:--] [cost:"+(System.currentTimeMillis()-startTime)+"]ms");

			return "";
		}


	}

	public String getUcidByUserName(String username)
	{

		long startTime = System.currentTimeMillis();
		GET_UCIDBYUSERNAME_REQ req = new GET_UCIDBYUSERNAME_REQ(BossMessageFactory.nextSequnceNum(), username);
		GET_UCIDBYUSERNAME_RES res = null;

		try {
			res = (GET_UCIDBYUSERNAME_RES)sendMessageAndWaittingResponse(req, 100000);
		} catch (Exception e) {
			logger.error("[根据游戏账号获取ucid] [失败] [出现异常] [username:"+username+"] [ucid:--] [cost:"+(System.currentTimeMillis()-startTime)+"]ms",e);
			return "";
		}

		if(res != null)
		{
			if(StringUtils.isEmpty(res.getUcid()))
			{
				if(logger.isInfoEnabled())
					logger.info("[根据游戏账号获取ucid] [失败] [没有获取到对应的ucid] [username:"+username+"] [ucid:--] [cost:"+(System.currentTimeMillis()-startTime)+"]ms");
			}
			else
			{
				if(logger.isInfoEnabled())
					logger.info("[根据游戏账号获取ucid] [成功] [ok] [ucid:"+res.getUcid()+"] [username:"+username+"] [cost:"+(System.currentTimeMillis()-startTime)+"]ms");
			}

			return res.getUcid();
		}
		else
		{
			if(logger.isInfoEnabled())
				logger.info("[根据游戏账号获取ucid] [失败] [服务器无响应或出现错误] [ucid:--] [username:"+username+"] [cost:"+(System.currentTimeMillis()-startTime)+"]ms");
			return "";
		}

	}

	public QQUserInfo getQQUserInfo4MoZuan(String username, String clientString,String channel)  {
		long startTime = System.currentTimeMillis();
		GET_MOZUANINFO_REQ req = new GET_MOZUANINFO_REQ(BossMessageFactory.nextSequnceNum(), username, clientString,channel);
		GET_MOZUANINFO_RES res = null;
		try {
			res = (GET_MOZUANINFO_RES) sendMessageAndWaittingResponse(req, 5000);
		} catch (Exception e) {
			logger.error("[获取魔钻信息] [失败] [发生异常] [username:" + username + "] [clientstring:" + clientString + "] [channel:"+channel+"] [" + (System.currentTimeMillis() - startTime) + "ms]", e);
			return null;
		}
		if (res != null) {
			if(logger.isInfoEnabled())
				logger.info("[获取魔钻信息] ["+(res.getResult()==1?"失败":"成功")+"] ["+res.getDescription()+"] [username:" + username + "] [clientstring:" + clientString + "] [channel:"+channel+"] [" + (System.currentTimeMillis() - startTime)
						+ "ms]");
			return res.getQqUserInfo();
		}
		else
		{
			logger.error("[获取魔钻信息] [失败] [连接服务端boss 超时] [username:"+username+"] [clientstring:"+clientString+"] [channel:"+channel+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
		}
		return null;
	}

	public int sendFund4NineOne(String username,String channel,String type) throws Exception
	{
		//协议访问server端 
		long startTime = System.currentTimeMillis();
		SEND_FUND_FOR_NINEONE_REQ req = new SEND_FUND_FOR_NINEONE_REQ(BossMessageFactory.nextSequnceNum(), username, channel, type);
		SEND_FUND_FOR_NINEONE_RES res = null;
		try {
			res = (SEND_FUND_FOR_NINEONE_RES) sendMessageAndWaittingResponse(req, 5000);
		} catch (Exception e) {
			logger.error("[为91特定渠道用户返积金] [失败] [连接服务端boss出现异常] [username:"+username+"] [channel:"+channel+"] [type:"+type+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			return -1;
		}
		if (res != null) {

			if(res.getResult() < 0)
			{
				logger.error("[为91特定渠道用户返积金] [失败] [未知错误] [username:"+username+"] [channel:"+channel+"] [type:"+type+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
				return -1;
			}

			if(logger.isInfoEnabled())
				logger.info("[为91特定渠道用户返积金] [成功] [ok] [username:"+username+"] [channel:"+channel+"] [type:"+type+"] [返积金数:"+res.getSum()+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			return res.getSum();
		}
		else
		{
			logger.error("[为91特定渠道用户返积金] [失败] [连接服务器端boss超时] [username:"+username+"] [channel:"+channel+"] [type:"+type+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
		}
		return -1;
	}



	public int isPopWindow(String[] infos)
	{
		long startTime = System.currentTimeMillis();
		int result = BossServerService.NOT_POP_WINDOW_FOR_VIP;
		JUDGE_POP_WINDOW_FOR_VIP_REQ req = new JUDGE_POP_WINDOW_FOR_VIP_REQ(BossMessageFactory.nextSequnceNum(), infos);
		JUDGE_POP_WINDOW_FOR_VIP_RES res = null;
		try
		{
			res = (JUDGE_POP_WINDOW_FOR_VIP_RES) sendMessageAndWaittingResponse(req, 5000);
		}
		catch(Exception e)
		{
			logger.error("[收集vip玩家信息] [失败] [出现异常] [infos长度:"+infos.length+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			return result;
		}

		if(res != null)
		{
			result = res.getWindowCanPop();
		}
		else
		{
			logger.error("[收集vip玩家信息] [失败] [发送请求超时] [infos长度:"+infos.length+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
		}

		return result;
	}

	public int createNewVipRecord(String[] infos)
	{
		long startTime = System.currentTimeMillis();
		int result = 1;
		CREATE_NEW_VIPINFORECORD_REQ req = new CREATE_NEW_VIPINFORECORD_REQ(BossMessageFactory.nextSequnceNum(), infos);
		CREATE_NEW_VIPINFORECORD_RES res = null;
		try
		{
			res = (CREATE_NEW_VIPINFORECORD_RES) sendMessageAndWaittingResponse(req, 5000);
		}
		catch(Exception e)
		{
			logger.error("[收集vip玩家信息] [失败] [出现异常] [infos长度:"+infos.length+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			return result;
		}

		if(res != null)
		{
			result = res.getResult();
		}
		else
		{
			logger.error("[收集vip玩家信息] [失败] [发送请求超时] [infos长度:"+infos.length+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
		}

		return result;
	}




	public String savingForQQUser(String userName,int goodsCount,String savingType,String serverName,String userChannel,String[]others) 
			throws NullArgumentException,
			IllegalArgumentException,
			UsernameNotExistsException,
			OrderGenerateException,Exception
			{
		long startTime = System.currentTimeMillis();

		if(StringUtils.isEmpty(userName))
		{
			logger.error("[腾讯充值订单生成] [失败:传入的uid为空值] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称:"+serverName+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的uid为空值！");
		}

		if(StringUtils.isEmpty(savingType))
		{
			logger.error("[腾讯充值订单生成] [失败:传入的充值类别为空值] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称："+serverName+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的充值类别为空值！");
		}

		if(StringUtils.isEmpty(serverName))
		{
			logger.error("[腾讯充值订单生成] [失败:传入的ServerName为空值] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [服务器名称："+serverName+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
			throw new NullArgumentException("传入的ServerName为空值！");
		}

		/*	if(goodsCount < 1)
			{
				logger.error("[腾讯充值订单生成] [失败:传入商品数量小于1] " +
								"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
										"[充值类型:"+savingType+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
				throw new IllegalArgumentException("传入商品数量小于1！");
			}*/

		QQ_SAVING_PRO_REQ req = new QQ_SAVING_PRO_REQ(BossMessageFactory.nextSequnceNum(), userName, goodsCount, savingType, serverName,userChannel,others);
		QQ_SAVING_PRO_RES res = null;

		try {
			res = (QQ_SAVING_PRO_RES) sendMessageAndWaittingResponse(req, 100000);
		} catch (Exception e) {
			logger.error("[腾讯充值订单生成] [失败:发送请求时发生异常] [req sequnceNum:"+req.getSequnceNum()+"] " +
					"[req Type:"+req.getType()+"] [req typeDesc:"+req.getTypeDescription()+"] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [角色id:"+req.getOthers()[0]+"] [" + (System.currentTimeMillis() - startTime) + "ms]", e);
			throw new Exception("发送请求时出现异常！");
		}

		if (res != null) 
		{
			//结果,0-成功, 1-uid不存在  2-订单生成失败  3-server不存在 
			//如果没有操作成功  
			byte result = res.getResult();

			if(result != 0)
			{
				if(result == 1)
				{
					logger.error("[腾讯充值订单生成] [失败:用户不存在] [req sequnceNum:"+req.getSequnceNum()+"] " +
							"[req Type:"+req.getType()+"] [req typeDesc:"+req.getTypeDescription()+"] " +
							"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [serverName:"+serverName+"] [" + (System.currentTimeMillis() - startTime) + "ms]");
					throw new UsernameNotExistsException("[userName:"+userName+"]不存在！");
				}
				else if(result == 2)
				{
					logger.error("[腾讯充值订单生成] [失败:订单生成失败] [req sequnceNum:"+req.getSequnceNum()+"] " +
							"[req Type:"+req.getType()+"] [req typeDesc:"+req.getTypeDescription()+"] " +
							"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [serverName:"+serverName+"]  [" + (System.currentTimeMillis() - startTime) + "ms]");
					throw new OrderGenerateException("订单生成失败！");
				}
				else if(result == 3)
				{
					logger.error("[腾讯充值订单生成] [失败:服务器不存在] [req sequnceNum:"+req.getSequnceNum()+"] " +
							"[req Type:"+req.getType()+"] [req typeDesc:"+req.getTypeDescription()+"] " +
							"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [serverName:"+serverName+"]  [" + (System.currentTimeMillis() - startTime) + "ms]");
					throw new ServerNameNotExistsException("Server：["+serverName+"]不存在！");
				}
				else
				{
					logger.error("[腾讯充值订单生成] [失败:未定义错误] [resultType:"+res.getResult()+"] [req sequnceNum:"+req.getSequnceNum()+"] " +
							"[req Type:"+req.getType()+"] [req typeDesc:"+req.getTypeDescription()+"] " +
							"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
							"[充值类型:"+savingType+"] [serverName:"+serverName+"]  [" + (System.currentTimeMillis() - startTime) + "ms]");
					throw new Exception("未定义错误！");
				}
			}
			else
			{
				//如果操作成功
				if(logger.isInfoEnabled())
				{
					logger.info("[腾讯充值订单生成] [成功] " +
							"[req sequnceNum:"+req.getSequnceNum()+"] " +
							"[req Type:"+req.getType()+"] " +
							"[req typeDesc:"+req.getTypeDescription()+"] " +
							"[userName:" + userName + "] " +
							"[商品数量:"+goodsCount+"] [充值类型:"+savingType+"] [serverName:"+serverName+"] [订单id:"+res.getOrderId()+"]" +
							"[" + (System.currentTimeMillis() - startTime) + "ms]");
				}

				return res.getOrderId();
			}

		}
		else
		{
			logger.error("[腾讯充值订单生成] [失败:Boss响应超时] [req sequnceNum:"+req.getSequnceNum()+"] " +
					"[req Type:"+req.getType()+"] [req typeDesc:"+req.getTypeDescription()+"] " +
					"[userName:" + userName + "] [商品数量:"+goodsCount+"] " +
					"[充值类型:"+savingType+"] [serverName:"+serverName+"] [" + (System.currentTimeMillis() - startTime) + "ms]");

			throw new Exception("Boss响应超时！");
		}
			}


	public String[] getUcVipInfos(String[] infos)
	{
		long startTime = System.currentTimeMillis();
		GET_UCVIP_INFO_REQ req = new GET_UCVIP_INFO_REQ(BossMessageFactory.nextSequnceNum(), infos);
		GET_UCVIP_INFO_RES res = null;
		try
		{
			res = (GET_UCVIP_INFO_RES) sendMessageAndWaittingResponse(req, 5000);
		}
		catch(Exception e)
		{
			logger.error("[收集ucvip玩家信息] [失败] [出现异常] [infos长度:"+infos.length+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			return new String[0];
		}
		String[] results = null;
		if(res != null)
		{
			results = res.getResult();
		}
		else
		{
			logger.error("[收集ucvip玩家信息] [失败] [发送请求超时] [infos长度:"+infos.length+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
		}

		return results;
	}
	
	public boolean canSendMail4UCVip(String[] infos)
	{
		long startTime = System.currentTimeMillis();
		String username = infos[0];
		String playerid = infos[1];
		
		CAN_SEND_UC_VID_MAIL_REQ req = new CAN_SEND_UC_VID_MAIL_REQ(BossMessageFactory.nextSequnceNum(), infos);
		CAN_SEND_UC_VID_MAIL_RES res = null;
		
		try
		{
			res = (CAN_SEND_UC_VID_MAIL_RES) sendMessageAndWaittingResponse(req, 5000);
		}
		catch(Exception e)
		{
			logger.error("[判断是否给ucvip玩家发送礼包] [失败] [出现异常] [infos长度:"+infos.length+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			return false;
		}
		String[] results = null;
		if(res != null)
		{
			results = res.getResult();
		}
		else
		{
			logger.error("[判断是否给ucvip玩家发送礼包] [失败] [发送请求超时] [infos长度:"+infos.length+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			return false;
		}

		if(results.length < 1)
		{
			logger.error("[判断是否给ucvip玩家发送礼包] [失败] [返回结果失败] [infos长度:"+infos.length+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			return false;
		}
		else
		{
			logger.info("[判断是否给ucvip玩家发送礼包] [成功] [ok] [infos长度:"+infos.length+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			return true;
		}
		
	}
	
	public int createUcVipRecord(String[] infos)
	{
		long startTime = System.currentTimeMillis();
		
		CREATE_UC_VID_MAIL_SEND_RECORD_REQ req = new CREATE_UC_VID_MAIL_SEND_RECORD_REQ(BossMessageFactory.nextSequnceNum(), infos);
		CREATE_UC_VID_MAIL_SEND_RECORD_RES res = null;
		
		try
		{
			res = (CREATE_UC_VID_MAIL_SEND_RECORD_RES) sendMessageAndWaittingResponse(req, 5000);
		}
		catch(Exception e)
		{
			logger.error("[记录给ucvip玩家发送礼包] [失败] [出现异常] [infos长度:"+infos.length+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			return -1;
		}
		String[] results = null;
		if(res != null)
		{
			results = res.getResult();
		}
		else
		{
			logger.error("[记录给ucvip玩家发送礼包] [失败] [发送请求超时] [infos长度:"+infos.length+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			return -1;
		}

		if(results.length < 1)
		{
			logger.error("[记录给ucvip玩家发送礼包] [失败] [服务器返回失败] [infos长度:"+infos.length+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			return -1;
		}
		else
		{
			logger.info("[记录给ucvip玩家发送礼包] [成功] [ok] [infos长度:"+infos.length+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			return 0;
		}
	}
	
	public String[] miguCommunicate(String[] infos)
	{
		//TODO 添加日志
		long startTime = System.currentTimeMillis();
		
		MIGU_COMMUNICATE_REQ req = new MIGU_COMMUNICATE_REQ(BossMessageFactory.nextSequnceNum(), infos);
		MIGU_COMMUNICATE_RES res = null;
		
		try
		{
			res = (MIGU_COMMUNICATE_RES) sendMessageAndWaittingResponse(req, 5000);
			if(logger.isInfoEnabled())
				logger.info("[米谷通讯] [成功] [ok] [infos长度:"+infos.length+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			return res.getResult();
		}
		catch(Exception e)
		{
			logger.error("[米谷通讯] [失败] [fail] [infos长度:"+infos.length+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		return new String[0];
	}

	public long getSavingAmount4Player(long playerId,long beginTime,long endTime)
	{
		//TODO 添加日志
		long startTime = System.currentTimeMillis();
		
		QUERY_SAVINGAMOUNT_REQ req = new QUERY_SAVINGAMOUNT_REQ(BossMessageFactory.nextSequnceNum(), playerId,beginTime,endTime,new String[0]);
		QUERY_SAVINGAMOUNT_RES res = null;
		
		try
		{
			res = (QUERY_SAVINGAMOUNT_RES) sendMessageAndWaittingResponse(req, 5000);
			if(logger.isInfoEnabled())
				logger.info("[查询玩家金额] [成功] [ok] [playerId:"+req.getPlayerId()+"] [beginTime:"+req.getStartTime()+"] [endTime:"+req.getEndTime()+"] [金额:"+res.getAmount()+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			return res.getAmount();
		}
		catch(Exception e)
		{
			logger.error("[查询玩家金额] [失败] [fail] playerId:"+req.getPlayerId()+"] [beginTime:"+req.getStartTime()+"] [endTime:"+req.getEndTime()+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		return 0l;
	}

}