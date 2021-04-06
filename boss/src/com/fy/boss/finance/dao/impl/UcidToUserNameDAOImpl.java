package com.fy.boss.finance.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fy.boss.finance.dao.ExceptionOrderFormDAO;
import com.fy.boss.finance.dao.UcidToUserNameDAO;
import com.fy.boss.finance.model.UcidToUserName;
import com.fy.boss.finance.dao.impl.UcidToUserNameDAOImpl;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;



public class UcidToUserNameDAOImpl implements UcidToUserNameDAO {

	private static final Log logger = LogFactory.getLog(UcidToUserNameDAOImpl.class);
	
	// property constants
	public static final Class pojoClass = UcidToUserName.class;
	
	private SimpleEntityManager<UcidToUserName> em = SimpleEntityManagerFactory.getSimpleEntityManager(pojoClass);
	
	protected static UcidToUserNameDAOImpl self;
	
	public static UcidToUserNameDAOImpl getInstance() {
		if (self == null) {
			synchronized (UcidToUserNameDAOImpl.class) {
				if (self == null)
					self = new UcidToUserNameDAOImpl();
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
	
	@Override
	public void saveNew(UcidToUserName ucidToUserName) {
		long startTime = System.currentTimeMillis();
		try {
			long id = em.nextId();
			ucidToUserName.setId(id);
			em.save(ucidToUserName);
			if(logger.isInfoEnabled())
				logger.info("[存储订单实体] [成功] [id:"+ucidToUserName.getId()+"] [ucid:"+ucidToUserName.getUcid()+"] [username:"+ucidToUserName.getUsername()+"] [userchannel:"+ucidToUserName.getUserChannel()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.error("[存储订单实体] [失败] [出现异常] [id:"+ucidToUserName.getId()+"] [ucid:"+ucidToUserName.getUcid()+"] [username:"+ucidToUserName.getUsername()+"] [userchannel:"+ucidToUserName.getUserChannel()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
	}

	@Override
	public UcidToUserName getById(long id) {
		long startTime = System.currentTimeMillis();
		try {
			UcidToUserName ucidToUserName = em.find(id);
			if(ucidToUserName != null)
			{
				if(logger.isInfoEnabled())
					logger.info("[根据id查询订单实体] [成功] [id:"+ucidToUserName.getId()+"] [ucid:"+ucidToUserName.getUcid()+"] [username:"+ucidToUserName.getUsername()+"] [userchannel:"+ucidToUserName.getUserChannel()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			else
			{
				logger.warn("[根据id查询订单实体] [失败] [无对应此id对象] [id:"+ucidToUserName.getId()+"] [ucid:"+ucidToUserName.getUcid()+"] [username:"+ucidToUserName.getUsername()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			return ucidToUserName;
		} catch (Exception e) {
			logger.error("[根据id查询订单实体] [失败] [出现异常] [id:"+id+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		return null;
	}

	@Override
	public UcidToUserName getByUcid(String ucid) {
		long startTime = System.currentTimeMillis();
		try {
			long ids[] = em.queryIds(UcidToUserName.class, " ucid = '" +ucid+"'");
			UcidToUserName ucidToUserName = em.find(ids[0]);
			if(ucidToUserName != null)
			{
				if(logger.isInfoEnabled())
					logger.info("[根据ucid查询订单实体] [成功] [id:"+ucidToUserName.getId()+"] [ucid:"+ucidToUserName.getUcid()+"] [username:"+ucidToUserName.getUsername()+"] [userchannel:"+ucidToUserName.getUserChannel()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			else
			{
				logger.warn("[根据ucid查询订单实体] [失败] [无对应此ucid对象] [id:--] [ucid:"+ucid+"] [username:--] [userchannel:--] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			
			return ucidToUserName;
		} catch (Exception e) {
			logger.error("[根据ucid查询订单实体] [失败] [出现异常] [id:--] [ucid:"+ucid+"] [username:--] [userchannel:--] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return null;
	}

	@Override
	public UcidToUserName getByUserName(String username) {
		long startTime = System.currentTimeMillis();
		try {
			long ids[] = em.queryIds(UcidToUserName.class, " username = '" +username+"'");
			UcidToUserName ucidToUserName = em.find(ids[0]);
			if(ucidToUserName != null)
			{
				if(logger.isInfoEnabled())
					logger.info("[根据username查询订单实体] [成功] [id:"+ucidToUserName.getId()+"] [ucid:"+ucidToUserName.getUcid()+"] [username:"+ucidToUserName.getUsername()+"] [userchannel:"+ucidToUserName.getUserChannel()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			else
			{
				logger.warn("[根据username查询订单实体] [失败] [无对应此username对象] [id:--] [ucid:--] [username:"+username+"] [userchannel:--] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			
			return ucidToUserName;
		} catch (Exception e) {
			logger.error("[根据username查询订单实体] [失败] [出现异常] [id:--] [ucid:--] [username:"+username+"] [userchannel:--] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return null;
	}

	
	@Override
	public long getCount() {
		long startTime = System.currentTimeMillis();
		try {
			long count = 0l;
			count = em.count();
			if(logger.isInfoEnabled())
				logger.info("[订单count统计] [成功] [count:"+count+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			return count;
		} catch (Exception e) {
			logger.error("[订单count统计] [失败] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return 0l;
	}




	public SimpleEntityManager<UcidToUserName> getEm() {
		return em;
	}

	public void setEm(SimpleEntityManager<UcidToUserName> em) {
		this.em = em;
	}
	
	public void update(UcidToUserName ucidToUserName) throws Exception
	{
		long startTime = System.currentTimeMillis();
		try {
			em.flush(ucidToUserName);
			if(logger.isInfoEnabled())
				logger.info("[flushUcidToUserName] [成功] [id:"+ucidToUserName.getId()+"] [ucid:"+ucidToUserName.getUcid()+"] [username:"+ucidToUserName.getUsername()+"] [userchannel:"+ucidToUserName.getUserChannel()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.error("[flushUcidToUserName] [失败] [id:"+ucidToUserName.getId()+"] [ucid:"+ucidToUserName.getUcid()+"] [username:"+ucidToUserName.getUsername()+"] [userchannel:"+ucidToUserName.getUserChannel()+"]  [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			throw new Exception("更新UcidToUserName失败！");
		}
	}


	
	
	
}
