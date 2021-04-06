package com.fy.boss.authorize.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fy.boss.authorize.dao.PassportDAO;
import com.fy.boss.authorize.exception.PassportPausedException;
import com.fy.boss.authorize.exception.PasswordWrongException;
import com.fy.boss.authorize.exception.UserOnlineException;
import com.fy.boss.authorize.exception.UsernameAlreadyExistsException;
import com.fy.boss.authorize.exception.UsernameNotExistsException;
import com.fy.boss.authorize.model.Passport;
import com.fy.boss.authorize.service.PassportManager;
import com.xuanzhi.tools.text.StringUtil;

public class PassportManager  {

	public static final Log log = LogFactory.getLog(PassportManager.class);
    
    protected PassportDAO passportDAO;
    
    protected static PassportManager m_self;
    
    public static PassportManager getInstance() {
    	return m_self;
    }
    
	public void initialize() throws Exception{
		m_self = this;
		System.out.println("["+PassportManager.class.getName()+"] [initialized]");
	}
    
    /**
     * 更新passport单个属性
     * @param passport
     * @param fieldName
     */
    public void updatePassportField(Passport passport, String fieldName)
    {
    	try {
			passportDAO.updateField(passport, fieldName);
		} catch (Exception e) {
			log.error("更新passport出错！",e);
		}
    }
    
    /**
     * 批量更新passport多个属性
     * @param passport
     * @param fieldNames
     */
    public void batch_updatePassportFields(Passport passport,List<String> fieldNames)
    {
    	try {
			passportDAO.batch_updateField(passport, fieldNames);
		} catch (Exception e) {
			log.error("批量更新passport出错！",e);
		}
    }
    
	
	/**
	 * 创建通行证 （登录验证）
	 * @param passport
	 * @return
	 * @throws UsernameAlreadyExistsException
	 */
	public Passport createPassport(Passport passport) throws UsernameAlreadyExistsException,Exception {
		if(passport == null)
		{
			throw new NullArgumentException("传入的passPort不能为null！");
		}
		//验证clientid
		
		if(passportDAO.getPassportByUserName(passport.getUserName()) != null) {
			throw new UsernameAlreadyExistsException("用户名已存在");
		}
		if(passport.getStatus() == null) {
			passport.setStatus(Passport.STATUS_NORMAL);
		}
		java.util.Date now = new java.util.Date();
		passport.setRegisterDate(now);
		passport.setLastLoginDate(now);
		
		//密码以md5加密后保存
		String password = passport.getPassWd();
		passport.setPassWd(StringUtil.hash(password));
		
		passport.setLastLoginChannel(passport.getRegisterChannel());
		passport.setLastLoginClientId(passport.getRegisterClientId());
		passport.setLastLoginMobileOs(passport.getRegisterMobileOs());
		passport.setLastLoginMobileType(passport.getRegisterMobileType());
		passport.setLastLoginNetworkMode(passport.getRegisterNetworkMode());
		passport.setLastUpdateStatusDate(now);
		
		passportDAO.save(passport);
		if(log.isInfoEnabled())
			log.info("[create] ["+passport.getUserName()+"] ["+passport.getId()+"]");
		
		return passport;
	}

	public Passport getPassport(long id) {
		Passport passport = null;
		long start = System.currentTimeMillis();
		try {
			passport = passportDAO.findById(id);
		} catch (Exception e) {
			log.error("根据id["+id+"]查passport信息失败！",e);
		}
		if(passport != null) {
			log.debug("[从数据库加载Passport] [id:"+passport.getId()+"] ["+passport.getUserName()+"]["+(System.currentTimeMillis()-start)+"ms]");
		}
		return passport;
	}
	
	public Passport getPassport(String username) {
		if(log.isInfoEnabled()){
			log.info("[username:"+username+"] [test]");
		}
		
		if(StringUtils.isEmpty(username))
		{
			log.warn("传入的账号名为空，值是["+username+"]");
		}
		
		Passport passport = null;
		try {
			passport = passportDAO.getPassportByUserName(username);
		} catch (Exception e) {
			log.error("通过账户名["+username+"]加载passport信息失败！",e);
		}
		if(passport == null)
		{
			log.warn("通过账户名["+username+"]没有从数据库中查询到到passport信息！");
		}
		return passport;
	}
	
	/**
	 * 通过账号别名查询用户
	 * @param nickName
	 * @return
	 */
	public Passport getPassportByNickName(String nickName) {
		if(StringUtils.isEmpty(nickName))
		{
			log.warn("传入的账号别名为空，值是["+nickName+"]");
		}
		
		Passport passport = null;
		try {
			passport = passportDAO.getPassportByNickName(nickName);
		} catch (Exception e) {
			log.error("通过账户别名["+nickName+"]加载passport信息失败！",e);
		}
		if(passport == null)
		{
			log.warn("通过账户别名["+nickName+"]没有从数据库中查询到到passport信息！");
		}
		return passport;
	}
	

	/**
	 * 修改用户密码
	 * @param passportId
	 * @param password
	 */
	public void changeUserPassword(long passportId, String password) {
		Passport pass = getPassport(passportId);
		if(pass != null) {
			pass.setPassWd(StringUtil.hash(password));
			try {
				updatePassportField(pass, "passWd");
			} catch ( Exception e) {
				log.error("修改id是["+pass.getId()+"],账号名是["+pass.getUserName()+"]的passport密码错误！",e);
			}
		}
		else
		{
			log.warn("通过id值["+passportId+"],无法查出passport信息！");
		}
	}
	
	public void changeUserPasswordByUserName(String username, String password) {
		Passport pass = getPassport(username);
		if(pass != null) {
			pass.setPassWd(StringUtil.hash(password));
			try {
				updatePassportField(pass, "passWd");
			} catch ( Exception e) {
				log.error("修改id是["+pass.getId()+"],账号名是["+pass.getUserName()+"]的passport密码错误！",e);
			}
		}
		else
		{
			log.warn("通过账号名称["+username+"],无法查出passport信息！");
		}
	}
	
	public void changeUserPasswordByNickName(String nickName, String password) {
		Passport pass = getPassportByNickName(nickName);
		if(pass != null) {
			pass.setPassWd(StringUtil.hash(password));
			try {
				updatePassportField(pass, "passWd");
			} catch ( Exception e) {
				log.error("修改id是["+pass.getId()+"],账号名是["+pass.getUserName()+"]，账号别名是["+pass.getNickName()+"]的passport密码错误！",e);
			}
		}
		else
		{
			log.warn("通过账号别名["+nickName+"],无法查出passport信息！");
		}
	}
	
	public void deletePassport(long id) {
		long l = System.currentTimeMillis();
		Passport d = getPassport(id);
		if(d != null) {
			passportDAO.delete(getPassport(id));
			if(log.isInfoEnabled())
				log.info("[delete] ["+d.getId()+"] ["+d.getUserName()+"] ["+(System.currentTimeMillis()-l)+"ms]");
		}
	}

	public PassportDAO getPassportDAO() {
		return passportDAO;
	}

	public void setPassportDAO(PassportDAO passportDAO) {
		this.passportDAO = passportDAO;
	}
	
	/**
	 * 通过账号和密码登陆
	 * @param username
	 * @param passwd
	 * @return
	 */
	public Passport login(String username, String passwd, 
			String lastLoginClientId, String lastLoginChannel, 
			String lastLoginIp,
			String lastLoginMobileOs,String lastLoginMobileType,
			String lastLoginNetworkMode
			) 
				throws UsernameNotExistsException,PasswordWrongException, UserOnlineException, PassportPausedException {
		long l = System.currentTimeMillis();
		Passport pp = getPassport(username);
		if(pp != null && pp.getPassWd().equals(StringUtil.hash(passwd))) {
			if(pp.getStatus() == Passport.STATUS_PAUSED) {
				log.info("[用户登录] [失败] [通行证处于暂停状态] [用户名:"+username+"] ["+pp.getStatus()+"]!" );
				throw new PassportPausedException("通行证目前状态为暂停.");
			}
			if(log.isInfoEnabled())
				log.info("[用户登录] [SUCC] ["+pp.getId()+"] ["+username+"] ["+(System.currentTimeMillis()-l)+"]");
			
			List<String> fieldNames = new ArrayList<String>();
			pp.setLastLoginDate(new java.util.Date());
			fieldNames.add("lastLoginDate");
			pp.setLastLoginClientId(lastLoginClientId);
			fieldNames.add("lastLoginClientId");
			/**
			 * 登陆时候渠道很重要 这时是需要渠道的 如果没有渠道则不去更新渠道为空值 继续沿用上次登陆的渠道
			 */
			if(!StringUtils.isEmpty(lastLoginChannel))
			{
				pp.setLastLoginChannel(lastLoginChannel);
				fieldNames.add("lastLoginChannel");
			}
			//不更新lastLoginIp 因为lastLoginIp已经作为appstore的机型唯一标识了
			/*pp.setLastLoginIp(lastLoginIp);
			fieldNames.add("lastLoginIp");*/
			pp.setLastLoginMobileOs(lastLoginMobileOs);
			fieldNames.add("lastLoginMobileOs");
			pp.setLastLoginMobileType(lastLoginMobileType);
			fieldNames.add("lastLoginMobileType");
			pp.setLastLoginNetworkMode(lastLoginNetworkMode);
			fieldNames.add("lastLoginNetworkMode");
			batch_updatePassportFields(pp, fieldNames);
			
			return pp;
		} 
		else if(pp != null && (pp.getRegisterChannel().toLowerCase().contains("uc_mieshi") 
				|| pp.getRegisterChannel().toLowerCase().matches("uc\\d+_mieshi") ||
				pp.getRegisterChannel().toLowerCase().contains("ucsdk") || pp.getRegisterChannel().toLowerCase().contains("ucnewsdk") || pp.getRegisterChannel().toLowerCase().contains("newucsdk") || pp.getRegisterChannel().toLowerCase().contains("newwdjsdk")) && passwd.equals(pp.getUcPassword()))
		{
			if(log.isInfoEnabled())
				log.info("[用户登录] [匹配ucpassword] [SUCC] ["+pp.getId()+"] ["+username+"] ["+(System.currentTimeMillis()-l)+"]");
			
			List<String> fieldNames = new ArrayList<String>();
			pp.setLastLoginDate(new java.util.Date());
			fieldNames.add("lastLoginDate");
			pp.setLastLoginClientId(lastLoginClientId);
			fieldNames.add("lastLoginClientId");
			/**
			 * 登陆时候渠道很重要 这时是需要渠道的 如果没有渠道则不去更新渠道为空值 继续沿用上次登陆的渠道
			 */
			if(!StringUtils.isEmpty(lastLoginChannel))
			{
				pp.setLastLoginChannel(lastLoginChannel);
				fieldNames.add("lastLoginChannel");
			}
			//不更新lastLoginIp 因为lastLoginIp已经作为appstore的机型唯一标识了
			/*pp.setLastLoginIp(lastLoginIp);
			fieldNames.add("lastLoginIp");*/
			pp.setLastLoginMobileOs(lastLoginMobileOs);
			fieldNames.add("lastLoginMobileOs");
			pp.setLastLoginMobileType(lastLoginMobileType);
			fieldNames.add("lastLoginMobileType");
			pp.setLastLoginNetworkMode(lastLoginNetworkMode);
			fieldNames.add("lastLoginNetworkMode");
			batch_updatePassportFields(pp, fieldNames);
			
			return pp;
		}
		else if(pp != null && !pp.getPassWd().equals(StringUtil.hash(passwd))) {
			if(log.isInfoEnabled())
				log.info("[用户登录] [失败] [密码不匹配] [账户密码:"+pp.getPassWd()+"] [输入的密码:"+passwd+"]");
			throw new PasswordWrongException("密码有误.");
		} else {
			if(log.isInfoEnabled())
				log.info("[用户登录] [失败] [用户名不存在] [输入的用户名:"+username+"]");
			throw new UsernameNotExistsException("用户不存在");
		}
	}

	
	/**
	 * 通过账号和密码登陆
	 * 同时更新一些必要的更新
	 * @return
	 */
	public Passport login(Passport passport) 
				throws UsernameNotExistsException,PasswordWrongException, UserOnlineException, PassportPausedException {
		long l = System.currentTimeMillis();
		if( passport == null )
		{
			throw new NullArgumentException("传入的passport信息为空！");
		}
		
		Passport pp = getPassport(passport.getUserName());
		if(pp != null && pp.getPassWd().equals(StringUtil.hash(passport.getPassWd()))) {
			if(pp.getStatus() == Passport.STATUS_PAUSED) {
				log.warn("[用户登录] [失败] [通行证处于暂停状态] [用户名:"+passport.getUserName()+"] [状态："+pp.getStatus()+"]!" );
				throw new PassportPausedException("通行证目前状态为暂停.");
			}
			
			List<String> willUpdateFields = new ArrayList<String>();
			//更新最后一次登陆时间
			pp.setLastLoginDate(new Date());
			willUpdateFields.add("lastLoginDate");
			//更新最后一次登陆IP
			pp.setLastLoginIp(passport.getLastLoginIp());
			willUpdateFields.add("lastLoginIp");
			//更新最后一次登陆渠道
			pp.setLastLoginChannel(passport.getLastLoginChannel());
			willUpdateFields.add("lastLoginChannel");
			//更新最后一次ClientID
			pp.setLastLoginClientId(passport.getLastLoginClientId());
			willUpdateFields.add("lastLoginClientId");
			//更新最后一次登陆手机平台
			pp.setLastLoginMobileOs(passport.getLastLoginMobileOs());
			willUpdateFields.add("lastLoginMobileOs");
			//更新最后一次登陆机型
			pp.setLastLoginMobileType(passport.getLastLoginMobileType());
			willUpdateFields.add("lastLoginMobileType");
			//更新最后一次登陆联网方式
			pp.setLastLoginNetworkMode(passport.getLastLoginNetworkMode());
			willUpdateFields.add("lastLoginNetworkMode");
			
			batch_updatePassportFields(pp, willUpdateFields);
			if(log.isInfoEnabled())
				log.info("[用户登录] [SUCC] ["+pp.getId()+"] ["+passport.getUserName()+"] ["+(System.currentTimeMillis()-l)+"]");
			
			return pp;
		} else if(pp != null && !pp.getPassWd().equals(StringUtil.hash(passport.getPassWd()))) {
			log.warn("[用户登录] [失败] [密码不匹配] [账户密码:"+pp.getPassWd()+"] [输入的密码:"+passport.getPassWd()+"]");
			throw new PasswordWrongException("密码有误.");
		} else {
			log.warn("[用户登录] [失败] [用户名不存在] [输入的用户名:"+passport.getUserName()+"]");
			throw new UsernameNotExistsException("用户不存在");
		}
	}
	


 


	
	//设置密保问题和答案
	//修改密保答案
	//修改密保问题

	/**
	 * 设置一个验证问题
	 * @param passport
	 * @param question
	 * @param answer
	 * @return-
	 */
	public void setAuthQuestion(Passport passport, String question, String answer) {
		
		if(passport == null)
		{
			throw new NullArgumentException("设置验证问题时，传入的passport信息为空值！");
		}
		
		if(passport.getId() == null)
		{
			throw new IllegalArgumentException("设置验证问题时，传入的用户名为["+passport.getUserName()+"]的passport的id值为空值！");
		}
		
		if(StringUtils.isEmpty(passport.getUserName()))
		{
			log.warn("设置验证问题时，传入的passpotID为["+passport.getId()+"]的passport的账号名值为空值！");
		}
		
		if(StringUtils.isEmpty(question)) 
		{
			throw new NullArgumentException("设置验证问题时，id为["+passport.getId()+"],用户名为["+passport.getUserName()+"]的验证问题信息为空值！");
		}
		
		if(StringUtils.isEmpty(answer))
		{
			log.warn("设置验证问题时，id为["+passport.getId()+"],用户名为["+passport.getUserName()+"]的验证问题答案为空值！");
		}
		
		List<String> willUpdateFields = new ArrayList<String>();
		
		passport.setSecretQuestion(question);
		passport.setSecretAnswer(answer);
		passport.setIsSetSecretQuestion(true);
		passport.setLastQuestionSetDate(new Date());
		
		willUpdateFields.add("secretQuestion");
		willUpdateFields.add("secretAnswer");
		willUpdateFields.add("isSetSecretQuestion");
		willUpdateFields.add("lastQuestionSetDate");
		
		batch_updatePassportFields(passport, willUpdateFields);
	} 
	
	//修改账户状态
	public void updatePassportStatus(Passport passport, Integer status)
	{
		if(passport == null)
		{
			throw new NullArgumentException("修改用户状态时，传入的passport信息为空值！");
		}
		
		if(passport.getId() == null)
		{
			throw new IllegalArgumentException("修改用户状态时，传入的用户名为["+passport.getUserName()+"]的passport的id值为空值！");
		}
		
		if(StringUtils.isEmpty(passport.getUserName()))
		{
			log.warn("修改用户状态时，传入的passpotID为["+passport.getId()+"]的passport的账号名值为空值！");
		}
		
		if(status == null)
		{
			log.warn("修改用户状态时，id值为["+passport.getId()+"],用户名为["+passport.getUserName()+"]的passport的当前状态为["+passport.getStatus()+"]，传入的要修改的状态为空值，故立即返回不做修改");
			return;
		}
		
		if(status.compareTo(Passport.STATUS_NORMAL) != 0 && status.compareTo(Passport.STATUS_ONLINE) != 0 &&
				status.compareTo(Passport.STATUS_PAUSED) != 0 && status.compareTo(Passport.STATUS_TRY) != 0)
		{
			log.warn("修改用户状态时，id值为["+passport.getId()+"],用户名为["+passport.getUserName()+"]的passport的当前状态为["+passport.getStatus()+"]，传入的要修改的状态值为["+status+"]," +
					"设置的状态值不为预先定义的任何状态值之一");
		}
		
		passport.setStatus(status);
		updatePassportField(passport, "status");
	}
	
	//更新充值金额 （及其他一些字段状态）
	public void updateChargeMount(Passport passport,Long chargeMount)
	{
		if(passport == null)
		{
			throw new NullArgumentException("更新充值金额时，传入的passport信息为空值！");
		}
		
		if(passport.getId() == null)
		{
			throw new IllegalArgumentException("更新充值金额时，传入的用户名为["+passport.getUserName()+"]的passport的id值为空值！");
		}
		
		if(StringUtils.isEmpty(passport.getUserName()))
		{
			log.warn("更新充值金额时，传入的passpotID为["+passport.getId()+"]的passport的账号名值为空值！");
		}
		
		if(chargeMount == null)
		{
			log.warn("更新充值金额时，id值为["+passport.getId()+"],用户名为["+passport.getUserName()+"]的passport传入的要修改的充值金额为空值，故立即返回不做修改");
			return;
		}
		
		List<String> willUpdateFields = new ArrayList<String>();
		
		passport.setTotalChargeAmount(passport.getTotalChargeAmount() + chargeMount);
		willUpdateFields.add("totalChargeAmount");
		passport.setLastChargeAmount(chargeMount);
		willUpdateFields.add("lastChargeAmount");
		passport.setLastChargeDate(new Date());
		willUpdateFields.add("lastChargeDate");
		//这里只是明确一下需要更新最后一次的充值渠道 ，语句本身只是为了让代码更容易读懂，无实质意义
		passport.setLastChargeChannel(passport.getLastChargeChannel());
		willUpdateFields.add("lastChargeChannel");
		
		batch_updatePassportFields(passport, willUpdateFields);
		
	}
	
	public void update(Passport passport)
	{
		long startTime = System.currentTimeMillis();
		this.passportDAO.update(passport);
		if(log.isInfoEnabled())
			log.info("[更新Passport] [成功] [PassPort id:"+passport.getId()+"] [Passport username:"+passport.getUserName()+"] [costs:"+(System.currentTimeMillis() - startTime)+"ms]");
	}
	

}
