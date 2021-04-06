package com.fy.boss.finance.dao;

import java.util.List;

import com.fy.boss.finance.model.ExceptionOrderForm;
import com.fy.boss.finance.model.UcidToUserName;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;



public interface UcidToUserNameDAO {
	
	public void saveNew(UcidToUserName ucidToUserName);
	
	public UcidToUserName getById(long id);
	
	public UcidToUserName getByUcid(String ucid);
	
	public UcidToUserName getByUserName(String username);
	
	public long getCount();
	
	/**
	 * 获取SimpleEntity对象
	 */
	public SimpleEntityManager<UcidToUserName> getEm();
	

	public void update(UcidToUserName ucidToUserName) throws Exception;
	
}
