package com.fy.boss.authorize.model;

import java.util.Date;

import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * Passport entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SimpleEntity(name="Passport")
@SimpleIndices({
	@SimpleIndex(members={"userName"}),
	@SimpleIndex(members={"nickName"}),
	@SimpleIndex(members={"lastLoginIp"})
})
public class Passport implements java.io.Serializable  
{
	// Fields
	@SimpleId
	private long id;
	
	@SimpleVersion
	private int version;
	
	//注册clientId
	private String registerClientId;
	//账号名
	private String userName;
	//password (利用MD5加密)
	private String passWd;
	//账号别名 nickname 通过账号别名也能登录
	private String nickName;
	//注册时间
	private Date registerDate;

	//账号来源 
	private String fromWhere;
	//注册渠道
	private String registerChannel;
	//最后一次登录渠道
	private String lastLoginChannel;
	//最后一次登陆时间
	private Date lastLoginDate;
	//最后一次登录IP 对于appstore来说放入的是一个唯一验证串
	@SimpleColumn(length=512)
	private String lastLoginIp;
	//注册手机号码
	@SimpleColumn(length=100)
	private String registerMobile;
	//最后一次登录ClientID
	private String lastLoginClientId;
	//注册手机平台
	@SimpleColumn(length=100)
	private String registerMobileOs;
	//最后一次登陆手机平台
	@SimpleColumn(length=100)
	private String lastLoginMobileOs;
	//注册机型
	private String registerMobileType;
	//最后一次登陆机型
	private String lastLoginMobileType;
	//注册联网方式
	@SimpleColumn(length=100)
	private String registerNetworkMode;
	//最后一次登陆联网方式
	@SimpleColumn(length=100)
	private String lastLoginNetworkMode;
	//充值金额（精确到分）
	private long totalChargeAmount;
	//最后一次充值时间
	private Date lastChargeDate;
	//最后一次充值金额
	private long lastChargeAmount;
	//最后一次充值渠道
	private String lastChargeChannel;
	//密保问题
	private String secretQuestion;
	//密保答案
	private String secretAnswer;
	//最后一次密保设置时间
	private Date lastQuestionSetDate;
	//是否设置密保
	private boolean isSetSecretQuestion;
	//账户状态 0 正常
	@SimpleColumn(length=20)
	private int status;
	//最后一次修改账户状态的时间
	private Date lastUpdateStatusDate;
	//最后一次修改密码的时间
	private Date lastUpdatePasswdDate;
	//ucpassword
	@SimpleColumn(length=100)
	private String ucPassword;
	
	
	
	
	public String getUcPassword() {
		return ucPassword;
	}


	public void setUcPassword(String ucPassword) {
		this.ucPassword = ucPassword;
	}


	public Date getLastUpdatePasswdDate() {
		return lastUpdatePasswdDate;
	}


	public void setLastUpdatePasswdDate(Date lastUpdatePasswdDate) {
		this.lastUpdatePasswdDate = lastUpdatePasswdDate;
	}


	public Date getLastUpdateStatusDate() {
		return lastUpdateStatusDate;
	}


	public void setLastUpdateStatusDate(Date lastUpdateStatusDate) {
		this.lastUpdateStatusDate = lastUpdateStatusDate;
	}




	public static final int STATUS_NORMAL = 0;
	public static final int STATUS_PAUSED = 1;
	public static final int STATUS_TRY = 2;
	public static final int STATUS_ONLINE = 3;


	// Constructors

	/** default constructor */
	public Passport() {
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public int getVersion() {
		return version;
	}


	public void setVersion(int version) {
		this.version = version;
	}


	public String getRegisterClientId() {
		return registerClientId;
	}


	public void setRegisterClientId(String registerClientId) {
		this.registerClientId = registerClientId;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getPassWd() {
		return passWd;
	}


	public void setPassWd(String passWord) {
		this.passWd = passWord;
	}


	public String getNickName() {
		return nickName;
	}


	public void setNickName(String nickName) {
		this.nickName = nickName;
	}


	public String getFromWhere() {
		return fromWhere;
	}


	public void setFromWhere(String fromWhere) {
		this.fromWhere = fromWhere;
	}


	public String getRegisterChannel() {
		return registerChannel;
	}


	public void setRegisterChannel(String registerChannel) {
		this.registerChannel = registerChannel;
	}


	public String getLastLoginChannel() {
		return lastLoginChannel;
	}


	public void setLastLoginChannel(String lastLoginChannel) {
		this.lastLoginChannel = lastLoginChannel;
	}


	public Date getLastLoginDate() {
		return lastLoginDate;
	}


	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}


	public String getLastLoginIp() {
		return lastLoginIp;
	}


	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}


	public String getRegisterMobile() {
		return registerMobile;
	}


	public void setRegisterMobile(String registerMobile) {
		this.registerMobile = registerMobile;
	}


	public String getLastLoginClientId() {
		return lastLoginClientId;
	}


	public void setLastLoginClientId(String lastLoginClientId) {
		this.lastLoginClientId = lastLoginClientId;
	}


	public String getRegisterMobileOs() {
		return registerMobileOs;
	}


	public void setRegisterMobileOs(String registerMobileOs) {
		this.registerMobileOs = registerMobileOs;
	}


	public String getLastLoginMobileOs() {
		return lastLoginMobileOs;
	}


	public void setLastLoginMobileOs(String lastLoginMobileOs) {
		this.lastLoginMobileOs = lastLoginMobileOs;
	}


	public String getRegisterMobileType() {
		return registerMobileType;
	}


	public void setRegisterMobileType(String registerMobileType) {
		this.registerMobileType = registerMobileType;
	}


	public String getLastLoginMobileType() {
		return lastLoginMobileType;
	}


	public void setLastLoginMobileType(String lastLoginMobileType) {
		this.lastLoginMobileType = lastLoginMobileType;
	}


	public String getRegisterNetworkMode() {
		return registerNetworkMode;
	}


	public void setRegisterNetworkMode(String registerNetworkMode) {
		this.registerNetworkMode = registerNetworkMode;
	}


	public String getLastLoginNetworkMode() {
		return lastLoginNetworkMode;
	}


	public void setLastLoginNetworkMode(String lastLoginNetworkMode) {
		this.lastLoginNetworkMode = lastLoginNetworkMode;
	}


	public Long getTotalChargeAmount() {
		return totalChargeAmount;
	}


	public void setTotalChargeAmount(Long totalChargeAmount) {
		this.totalChargeAmount = totalChargeAmount;
	}


	public Date getLastChargeDate() {
		return lastChargeDate;
	}


	public void setLastChargeDate(Date lastChargeDate) {
		this.lastChargeDate = lastChargeDate;
	}


	public Long getLastChargeAmount() {
		return lastChargeAmount;
	}


	public void setLastChargeAmount(Long lastChargeAmount) {
		this.lastChargeAmount = lastChargeAmount;
	}


	public String getLastChargeChannel() {
		return lastChargeChannel;
	}


	public void setLastChargeChannel(String lastChargeChannel) {
		this.lastChargeChannel = lastChargeChannel;
	}


	public String getSecretQuestion() {
		return secretQuestion;
	}


	public void setSecretQuestion(String secretQuestion) {
		this.secretQuestion = secretQuestion;
	}


	public String getSecretAnswer() {
		return secretAnswer;
	}


	public void setSecretAnswer(String secretAnswer) {
		this.secretAnswer = secretAnswer;
	}


	public Date getLastQuestionSetDate() {
		return lastQuestionSetDate;
	}


	public void setLastQuestionSetDate(Date lastQuestionSetDate) {
		this.lastQuestionSetDate = lastQuestionSetDate;
	}


	public boolean getIsSetSecretQuestion() {
		return isSetSecretQuestion;
	}


	public void setIsSetSecretQuestion(boolean isSetSecretQuestion) {
		this.isSetSecretQuestion = isSetSecretQuestion;
	}
	
	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Date getRegisterDate() {
		return registerDate;
	}


	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	

}
