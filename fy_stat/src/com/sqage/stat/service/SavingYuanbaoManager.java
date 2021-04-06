package com.sqage.stat.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sqage.stat.dao.SavingYuanbaoDAO;
import com.sqage.stat.model.SavingYuanbao;
import com.xuanzhi.tools.cache.LruMapCache;

public class SavingYuanbaoManager {

	protected static SavingYuanbaoManager m_self = null;
    
	protected static final Log logger = LogFactory.getLog(SavingYuanbaoManager.class);
	
	protected static LruMapCache mCache;
    
    protected SavingYuanbaoDAO savingYuanbaoDAO;
        
    public static SavingYuanbaoManager getInstance() {
		return m_self;
	}
    
	public void initialize() throws Exception {
		mCache = new LruMapCache();
		m_self = this;
		System.out.println("[" + SavingYuanbaoManager.class.getName() + "] [initialized]");
		logger.info("[" + SavingYuanbaoManager.class.getName() + "] [initialized]");
	}
	
	public SavingYuanbao createSavingYuanbao(SavingYuanbao savingYuanbao) {
		boolean result = savingYuanbaoDAO.save(savingYuanbao);
		if (result) {
			mCache.__put(savingYuanbao.getId(), savingYuanbao);
			logger.info("[创建SavingYuanbao]");
		} else {
			savingYuanbao = null;
		}
		return savingYuanbao;
	}

	public SavingYuanbao getSavingYuanbao(long id) {
		if(mCache.__get(id) != null) {
			return (SavingYuanbao)mCache.__get(id);
		}
		SavingYuanbao savingYuanbao = savingYuanbaoDAO.findById(id);
		if(savingYuanbao != null) {
			mCache.__put(savingYuanbao.getId(), savingYuanbao);
			mCache.__put(savingYuanbao.getOrderno(), savingYuanbao);
		}
		return savingYuanbao;
	}

	public SavingYuanbao getSavingYuanbao(String orderno) {
		if(mCache.__get(orderno) != null) {
			return (SavingYuanbao)mCache.__get(orderno);
		}
		List<SavingYuanbao> list = savingYuanbaoDAO.findByOrderno(orderno);
		if(list != null && list.size() > 0) {
			SavingYuanbao sy = list.get(0);
			if(sy != null) {
				mCache.__put(sy.getId(), sy);
				mCache.__put(sy.getOrderno(), sy);
				return sy;
			}
		}
		return null;
	}
	
	public int getCount() {
		return savingYuanbaoDAO.getRowNum();
	}
	
	public List getSavingYuanbaos() {
		return savingYuanbaoDAO.findAll();
	}
	
	public List getSavingYuanbaos(int start, int length) {
		return savingYuanbaoDAO.findPageRows(start, length);
	}
	
	public long getSavingRmbTotal() {
		return savingYuanbaoDAO.getSavingRMBTotal();
	}
	
	public long getSavingRmbTotal(long serverid) {
		if(serverid == -1) {
			return getSavingRmbTotal();
		}
		return savingYuanbaoDAO.getSavingRMBTotal(serverid);
	}
	
	public long getSavingRmbPureAmount(Date starttime, Date endtime, long channelid, long channelitemid) {
		return savingYuanbaoDAO.getSavingRMBPureAmount(starttime, endtime, channelid, channelitemid);
	}
	
	public long getSavingRmbAmount(Date starttime, Date endtime) {
		return savingYuanbaoDAO.getSavingRMBAmount(starttime, endtime);
	}
	
	public long getSavingRmbAmount(Date starttime, Date endtime, long channelid, long channelitemid) {
		return savingYuanbaoDAO.getSavingRMBAmount(starttime, endtime, channelid, channelitemid);
	}
	
	public long getSavingRmbAmount(Date starttime, Date endtime, long channelid) {
		return savingYuanbaoDAO.getSavingRMBAmount(starttime, endtime, channelid);
	}
	
	public long getSavingRmbAmount(Date regstart, Date regend, Date starttime, Date endtime) {
		return savingYuanbaoDAO.getSavingRMBAmount(regstart, regend , starttime, endtime);
	}
	
	public long getSavingRmbAmountPure(Date regstart, Date regend, Date starttime, Date endtime) {
		return savingYuanbaoDAO.getSavingRMBAmountPure(regstart, regend , starttime, endtime);
	}
	
	public long getSavingRmbAmountNoSms(Date regstart, Date regend, Date starttime, Date endtime) {
		return savingYuanbaoDAO.getSavingRMBAmountNoSms(regstart, regend , starttime, endtime);
	}
	
	public long getSavingRmbAmountNoSqage(Date regstart, Date regend, Date starttime, Date endtime) {
		return savingYuanbaoDAO.getSavingRMBAmountNoSqage(regstart, regend , starttime, endtime);
	}
	
	public long getSavingRmbAmount(Date regstart, Date regend , Date starttime, Date endtime, long channelid, long channelitemid) {
		return savingYuanbaoDAO.getSavingRMBAmount(regstart, regend, starttime, endtime, channelid, channelitemid);
	}
	
	public long getSavingRmbAmountPure(Date regstart, Date regend , Date starttime, Date endtime, long channelid, long channelitemid) {
		return savingYuanbaoDAO.getSavingRMBAmountPure(regstart, regend, starttime, endtime, channelid, channelitemid);
	}
	
	public long getSavingRmbAmountNoSms(Date regstart, Date regend , Date starttime, Date endtime, long channelid, long channelitemid) {
		return savingYuanbaoDAO.getSavingRMBAmountNoSms(regstart, regend, starttime, endtime, channelid, channelitemid);
	}
	
	public long getSavingRmbAmountNoSqage(Date regstart, Date regend , Date starttime, Date endtime, long channelid, long channelitemid) {
		return savingYuanbaoDAO.getSavingRMBAmountNoSqage(regstart, regend, starttime, endtime, channelid, channelitemid);
	}
	
	public long getSavingRmbAmount(Date regstart, Date regend, Date starttime, Date endtime, long channelid) {
		return savingYuanbaoDAO.getSavingRMBAmount(regstart, regend, starttime, endtime, channelid);
	}
	
	public long getSavingRmbAmountPure(Date regstart, Date regend, Date starttime, Date endtime, long channelid) {
		return savingYuanbaoDAO.getSavingRMBAmountPure(regstart, regend, starttime, endtime, channelid);
	}
	
	public long getSavingRmbAmountNoSms(Date regstart, Date regend, Date starttime, Date endtime, long channelid) {
		return savingYuanbaoDAO.getSavingRMBAmountNoSms(regstart, regend, starttime, endtime, channelid);
	}
	
	public long getSavingRmbAmountNoSqage(Date regstart, Date regend, Date starttime, Date endtime, long channelid) {
		return savingYuanbaoDAO.getSavingRMBAmountNoSqage(regstart, regend, starttime, endtime, channelid);
	}
	
	public long getSavingRmbCost(Date starttime, Date endtime) {
		return savingYuanbaoDAO.getSavingRMBCost(starttime, endtime);
	}
	
	public long getSavingRmbCost(Date starttime, Date endtime, long channelid, long channelitemid) {
		return savingYuanbaoDAO.getSavingRMBCost(starttime, endtime, channelid, channelitemid);
	}
	
	public long getSavingRmbCost(Date starttime, Date endtime, long channelid) {
		return savingYuanbaoDAO.getSavingRMBCost(starttime, endtime, channelid);
	}
	
	public long getSavingRmbCost(Date regstart, Date regend, Date starttime, Date endtime) {
		return savingYuanbaoDAO.getSavingRMBCost(regstart, regend,starttime, endtime);
	}
	
	public long getSavingRmbCost(Date regstart, Date regend, Date starttime, Date endtime, long channelid, long channelitemid) {
		return savingYuanbaoDAO.getSavingRMBCost(regstart, regend,starttime, endtime, channelid, channelitemid);
	}
	
	public long getSavingRmbCost(Date regstart, Date regend, Date starttime, Date endtime, long channelid) {
		return savingYuanbaoDAO.getSavingRMBCost(regstart, regend,starttime, endtime, channelid);
	}
	
	public long getSavingRmbAmount(long serverid, Date starttime, Date endtime) {
		return savingYuanbaoDAO.getSavingRMBAmount(serverid, starttime, endtime);
	}
	
	public long getSavingYuanbaoTotal() {
		return savingYuanbaoDAO.getSavingYuanbaoTotal();
	}
	
	public long getSavingYuanbaoTotal(long serverid) {
		if(serverid == -1) {
			return getSavingYuanbaoTotal();
		}
		return savingYuanbaoDAO.getSavingYuanbaoTotal(serverid);
	}
	
	public long getSavingYuanbaoAmount(Date starttime, Date endtime) {
		return savingYuanbaoDAO.getSavingYuanbaoAmount(starttime, endtime);
	}
	
	public long getNewUserSavingYuanbaoAmount(Date starttime, Date endtime) {
		return savingYuanbaoDAO.getNewUserSavingYuanbaoAmount(starttime, endtime);
	}
	
	public long getSavingYuanbaoAmount(Date starttime, Date endtime, long channelid, long channelitemid) {
		return savingYuanbaoDAO.getSavingYuanbaoAmount(starttime, endtime, channelid, channelitemid);
	}
	
	public long getSavingYuanbaoAmount(Date starttime, Date endtime, long channelid) {
		return savingYuanbaoDAO.getSavingYuanbaoAmount(starttime, endtime, channelid);
	}
	
	public long getSavingYuanbaoAmount(long serverid, Date starttime, Date endtime) {
		return savingYuanbaoDAO.getSavingYuanbaoAmount(serverid, starttime, endtime);
	}
	
	public long getNewUserSavingYuanbaoAmount(long serverid, Date starttime, Date endtime) {
		return savingYuanbaoDAO.getNewUserSavingYuanbaoAmount(serverid, starttime, endtime);
	}
	
	public int getSavingYuanbaoUsernum(Date starttime, Date endtime) {
		return savingYuanbaoDAO.getSavingYuanbaoUserNum(starttime, endtime);
	}
	
	public int getSavingYuanbaoUsernum(Date starttime, Date endtime, long channelid) {
		return savingYuanbaoDAO.getSavingYuanbaoUserNum(starttime, endtime, channelid);
	}
	
	public int getSavingYuanbaoUsernum(Date starttime, Date endtime, long channelid, long channelitemid) {
		return savingYuanbaoDAO.getSavingYuanbaoUserNum(starttime, endtime, channelid, channelitemid);
	}
	
	public int getSavingYuanbaoNewUsernum(Date regstart, Date regend, Date starttime, Date endtime) {
		return savingYuanbaoDAO.getSavingYuanbaoNewUsernum(regstart, regend, starttime, endtime);
	}
	
	public int getSavingYuanbaoNewUsernum(Date regstart, Date regend, Date starttime, Date endtime, long channelid) {
		return savingYuanbaoDAO.getSavingYuanbaoNewUsernum(regstart, regend, starttime, endtime, channelid);
	}
	
	public int getSavingYuanbaoNewUsernum(Date regstart, Date regend, Date starttime, Date endtime, long channelid, long channelitemid) {
		return savingYuanbaoDAO.getSavingYuanbaoNewUsernum(regstart, regend, starttime, endtime, channelid, channelitemid);
	}
	
	public int getSavingYuanbaoUsernum(long serverid, Date starttime, Date endtime) {
		return savingYuanbaoDAO.getSavingYuanbaoUserNum(serverid, starttime, endtime);
	}
	
	public float getSavingYuanbaoUserAvgLevel(Date starttime, Date endtime) { 
		return savingYuanbaoDAO.getSavingYuanbaoUserAvgLevel(starttime, endtime);
	}
	
	public float getSavingYuanbaoUserAvgLevel(long serverid, Date starttime, Date endtime) {
		return savingYuanbaoDAO.getSavingYuanbaoUserAvgLevel(serverid, starttime, endtime);
	}
	
	public float getFirstSavingYuanbaoUserAvgLevel(Date starttime, Date endtime) { 
		return savingYuanbaoDAO.getFirstSavingYuanbaoUserAvgLevel(starttime, endtime);
	}
	
	public float getFirstSavingYuanbaoUserAvgLevel(long serverid, Date starttime, Date endtime) {
		return savingYuanbaoDAO.getFirstSavingYuanbaoUserAvgLevel(serverid, starttime, endtime);
	}
	
	public void updateSavingYuanbao(SavingYuanbao d) {
		savingYuanbaoDAO.attachDirty(d);
		mCache.remove(d.getId());
	}
	 
	public void deleteSavingYuanbao(long id) {
		SavingYuanbao d = getSavingYuanbao(id);
		if(d != null) {
			savingYuanbaoDAO.delete(getSavingYuanbao(id));
			mCache.remove(d.getId());
		}
	}

	public SavingYuanbaoDAO getSavingYuanbaoDAO() {
		return savingYuanbaoDAO;
	}

	public void setSavingYuanbaoDAO(SavingYuanbaoDAO savingYuanbaoDAO) {
		this.savingYuanbaoDAO = savingYuanbaoDAO;
	}

}
