package com.fy.boss.authorize.dao;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fy.boss.authorize.model.Passport;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * Passport entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.fy.boss.authorize.model.Passport
 * @author MyEclipse Persistence Tools
 */

public class PassportDAO{
	private static final Log log = LogFactory.getLog(PassportDAO.class);
	// property constants
	public static final Class pojoClass = Passport.class;
	
	private SimpleEntityManager<Passport> em = SimpleEntityManagerFactory.getSimpleEntityManager(pojoClass);

	protected void initDao() {
		// do nothing
	}

	//存储
	public void save(Passport transientInstance) throws Exception {
		if(transientInstance == null)
		{
			log.error("要存储的用户信息Passport为null");
			return;
		}
		log.debug("开始准备存储账户信息，存储的账户的账号为：["+transientInstance.getUserName()+"]");
		try {
			//这里是否用em.save(transientInstance)？因为save 的成本很高 所以考虑采用notifynewobject
			//em.notifyNewObject(transientInstance);
			long id = em.nextId();
			transientInstance.setId(id);
			em.save(transientInstance);
			if(log.isDebugEnabled())
				log.debug("存储账号为["+transientInstance.getUserName()+"]的Passport成功！");
		} catch (RuntimeException re) {
			log.error("存储账号为["+transientInstance.getUserName()+"]Passport失败！", re);
			throw re;
		} catch (Exception e) {
			log.error("存储账号为["+transientInstance.getUserName()+"]Passport失败！", e);
			throw e;
		}
	}

	//根据账号名查询单个对象
	public Passport getPassportByUserName(String userName) throws Exception
	{
		Passport p = null;
		try {
			if(log.isDebugEnabled())
				log.debug("开始根据账号查询用户信息(Passport)的主键(id),传入的账号为：[" +userName+"]");
			long[] ids = em.queryIds(pojoClass, " userName = '" + userName + "' or nickName = '" + userName + "'");
			if(ids == null || ids.length <= 0)
			{	
				log.warn("账户信息表中无关于账号["+userName+"]的用户信息！");
			}
			else
			{
				if(log.isDebugEnabled())
				{
					log.debug("查询账号名为["+userName+"]的账户信息主键成功，信息对应的id有["+ids.length+"]条！");
					log.debug("开始根据查询的账户信息的id["+ids[0]+"]获取用户信息查询用户信息(Passport),Passport的账号为：[" +userName+"]");
				}
				p = em.find(ids[0]);
				
				if(p != null)
				{
					if(p.getFromWhere() == null)
					{
						p.setFromWhere("");
					}
					if(p.getLastChargeChannel() == null)
					{
						p.setLastChargeChannel("");
					}
					if(p.getLastLoginChannel() == null)
					{
						p.setLastLoginChannel("");
					}
					if(p.getLastLoginClientId() == null)
					{
						p.setLastLoginClientId("");
					}
					if(p.getLastLoginIp() == null)
					{
						p.setLastLoginIp("");
					}
					
					if(p.getLastLoginMobileOs() == null)
					{
						p.setLastLoginMobileOs("");
					}
					if(p.getLastLoginMobileType() == null)
					{
						p.setLastLoginMobileType("");
					}
					if(p.getLastLoginNetworkMode() == null)
					{
						p.setLastLoginNetworkMode("");
					}
					if(p.getRegisterChannel() == null)
					{
						p.setRegisterChannel("");
					}
					if(p.getRegisterClientId() == null)
					{
						p.setRegisterClientId("");
					}
					if(p.getRegisterMobile() == null)
					{
						p.setRegisterMobile("");
					}
					if(p.getRegisterMobileOs() == null)
					{
						p.setRegisterMobileOs("");
					}
					if(p.getRegisterMobileType() == null)
					{
						p.setRegisterMobileType("");
					}
					if(p.getRegisterNetworkMode() == null)
					{
						p.setRegisterNetworkMode("");
					}
					if(p.getSecretQuestion() == null)
					{
						p.setSecretQuestion("");
					}
					if(p.getSecretAnswer() == null)
					{
						p.setSecretAnswer("");
					}
					if(p.getNickName() == null)
					{
						p.setNickName("");
					}
					
					if(log.isDebugEnabled())
						log.debug("查询账号名为["+p.getUserName()+"]的账户信息成功，信息对应的id是["+p.getId()+"]！");
				}
				else
				{
					log.warn("账户信息表中无关于账号ID["+ids[0]+"]的用户信息！");

				}
			}
			
			return p;
		} catch (Exception e) {
			log.error("查询用户名为["+userName+"]的账户信息失败！",e);
			throw e;
		}
	}
	
	//根据账号别名查询单个对象
	public Passport getPassportByNickName(String nickName) throws Exception
	{
		Passport p = null;
		try {
			if(log.isDebugEnabled())
				log.debug("开始根据账号查询用户信息(Passport)的主键(id),传入的账号别名为：[" +nickName+"]");
			long[] ids = em.queryIds(pojoClass, " nickName = " + nickName);
			if(ids == null || ids.length <= 0)
			{
				log.warn("账户信息表中无关于账号["+nickName+"]的用户信息！");
			}
			else
			{
				if(log.isDebugEnabled())
				{
					log.debug("查询账号别名为["+nickName+"]的账户信息主键成功，信息对应的id有["+ids.length+"]条！");
					log.debug("开始根据查询的账户信息的id["+ids[0]+"]获取用户信息查询用户信息(Passport),Passport的账号别名为：[" +nickName+"]");
				}
				p = em.find(ids[0]);
				
				if(p != null)
				{
					log.debug("通过账号别名["+p.getNickName()+"]查询账号名为["+p.getUserName()+"]的账户信息成功，信息对应的id是["+p.getId()+"]！");
				}
				else
				{
					log.warn("账户信息表中无关于账号ID["+ids[0]+"]的用户信息！");
					
				}
			}
			
			return p;
		} catch (Exception e) {
			log.error("查询用户名为["+nickName+"]的账户信息失败！",e);
			throw e;
		}
	}
	

	public void delete(Passport persistentInstance) {
		log.debug("deleting Passport instance");
		try {
			em.remove(persistentInstance);
			if(log.isDebugEnabled())
			{
				log.debug("delete successful");
			}
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		} catch (Exception e) {
			log.error("delete failed", e);
		}
	}

	public Passport findById(java.lang.Long id) throws Exception {
		if(id == null)
		{
			log.error("通过id值查询Passport,传入的id值不能为null");
			return null;
		}
		if(log.isDebugEnabled())
			log.debug("开始用id["+id+"]获取Passport信息..." );
		try {
			Passport p = em.find(id);
			if(p == null)
			{
				log.warn("通过id["+id+"]无法查出Passport信息！");
			}
			else
			{
				if(p.getFromWhere() == null)
				{
					p.setFromWhere("");
				}
				if(p.getLastChargeChannel() == null)
				{
					p.setLastChargeChannel("");
				}
				if(p.getLastLoginChannel() == null)
				{
					p.setLastLoginChannel("");
				}
				if(p.getLastLoginClientId() == null)
				{
					p.setLastLoginClientId("");
				}
				if(p.getLastLoginIp() == null)
				{
					p.setLastLoginIp("");
				}
				
				if(p.getLastLoginMobileOs() == null)
				{
					p.setLastLoginMobileOs("");
				}
				if(p.getLastLoginMobileType() == null)
				{
					p.setLastLoginMobileType("");
				}
				if(p.getLastLoginNetworkMode() == null)
				{
					p.setLastLoginNetworkMode("");
				}
				if(p.getRegisterChannel() == null)
				{
					p.setRegisterChannel("");
				}
				if(p.getRegisterClientId() == null)
				{
					p.setRegisterClientId("");
				}
				if(p.getRegisterMobile() == null)
				{
					p.setRegisterMobile("");
				}
				if(p.getRegisterMobileOs() == null)
				{
					p.setRegisterMobileOs("");
				}
				if(p.getRegisterMobileType() == null)
				{
					p.setRegisterMobileType("");
				}
				if(p.getRegisterNetworkMode() == null)
				{
					p.setRegisterNetworkMode("");
				}
				if(p.getSecretQuestion() == null)
				{
					p.setSecretQuestion("");
				}
				if(p.getSecretAnswer() == null)
				{
					p.setSecretAnswer("");
				}
				if(p.getNickName() == null)
				{
					p.setNickName("");
				}
				if(log.isDebugEnabled())
					log.debug("通过id["+id+"]成功查出Passport信息,Passport的账号是["+p.getUserName()+"]!");
			}
			return p;
		} catch (RuntimeException re) {
			log.error("通过id["+id+"]查出Passport信息失败！",re);
			throw re;
		} catch (Exception e) {
			log.error("通过id["+id+"]查出Passport信息失败！",e);
			throw e;
		}
		
	}
	
	/**
	 * 一个公用方法，用以修改传入的字段
	 * 
	 * @param fieldName 传入字段名
	 * @throws Exception 
	 * added by liuyang at 2012-05-19
	 */
	public void updateField(Passport passport,String fieldName) throws Exception
	{
		if(passport == null)
		{
			throw new NullArgumentException("传入的passport为空值！");
		}
		
		if(StringUtils.isEmpty(fieldName))
		{
			log.warn("传入的字段名["+fieldName+"]是空值["+fieldName+"]");
		}
		if(log.isDebugEnabled())
			log.debug("开始用updateField方法更新passport的字段["+fieldName+"]" );
		em.notifyFieldChange(passport, fieldName);
		em.save(passport);
	}
	
	/**
	 * 一个公用方法，用以批量修改传入的字段
	 * 
	 * @param fieldNames 传入字段名列表
	 * @throws Exception 
	 * added by liuyang at 2012-05-19
	 */
	public void batch_updateField(Passport passport,List<String> fieldNames) throws Exception
	{
		if(passport == null)
		{
			throw new NullArgumentException("传入的passport为空值！");
		}
		
		if(fieldNames == null)
		{
			throw new NullArgumentException("传入的字段名列表(fieldNames[List<String>]为空值！)");
		}
		
		if(fieldNames.size() <= 0)
		{
			log.warn("准备批量更新账户名为["+passport.getUserName()+"]的passport的属性为["+fieldNames.size()+"]个，无法进行正常更新，故立即返回！");
		}
		if(log.isDebugEnabled())
			log.debug("开始用batch_updateField方法批量更新["+fieldNames.size()+"]个passport的字段,..." );
		
		int i = 1;
		Iterator<String> it = fieldNames.iterator();
		while(it.hasNext())
		{
			String fieldName = it.next();
			if(StringUtils.isEmpty(fieldName))
			{
				log.warn("要更新的账户名为["+passport.getUserName()+"]的passport要更新的属性无值["+fieldName+"]!");
			}
			if(log.isDebugEnabled())
				log.debug("开始通知数据库更新账户名为["+passport.getUserName()+"]的passport的属性["+fieldName+"]");
			em.notifyFieldChange(passport, fieldName);
			i++;
			if(log.isDebugEnabled())
				log.debug("已经通知数据库更新账户名为["+passport.getUserName()+"]的passport的属性["+fieldName+"]，这次是第["+i+"]次通知更新！");
		}
		
		em.save(passport);
	}
	
	public void update(Passport passport)
	{
		long startTime = System.currentTimeMillis();
		try {
			em.flush(passport);
			if(log.isInfoEnabled())
				log.info("[flush Passport] [成功] [passport id:"+passport.getId()+"] [passport name:"+passport.getUserName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			log.error("[flush Passport] [失败] [passport id:"+passport.getId()+"] [passport name:"+passport.getUserName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		}
	}
	
	
}
