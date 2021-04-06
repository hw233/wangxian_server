package com.sqage.stat.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sqage.stat.dao.ProvinceDAO;
import com.sqage.stat.model.Province;
import com.xuanzhi.tools.cache.LruMapCache;

public class ProvinceManager {

	protected static ProvinceManager m_self = null;
    
	protected static final Log log = LogFactory.getLog(ProvinceManager.class);
	
	protected static LruMapCache mCache;
    
    protected ProvinceDAO provinceDAO;
        
    public static ProvinceManager getInstance() {
		return m_self;
	}
    
	public void initialize() throws Exception{
    	mCache = new LruMapCache();
		m_self = this;
		System.out.println("["+ProvinceManager.class.getName()+"] [initialized]");
		log.info("["+ProvinceManager.class.getName()+"] [initialized]");
	}
	
	public Province createProvince(Province Province) {
		if(getProvince(Province.getName()) != null) {
			return null;
		}
		provinceDAO.save(Province);
		mCache.__put(Province.getId(), Province);
		mCache.__put(Province.getName(), Province);
		return Province;
	}

	public Province getProvince(long id) {
		if(mCache.__get(id) != null) {
			return (Province)mCache.__get(id);
		}
		Province Province = provinceDAO.findById(id);
		if(Province != null) {
			mCache.__put(Province.getId(), Province);
			mCache.__put(Province.getName(), Province);
		} else {
		}
		return Province;
	}

	public Province getProvince(String name) {
		if(mCache.__get(name) != null) {
			return (Province)mCache.__get(name);
		}
		List list = provinceDAO.findByName(name);
		if(list.size() > 0) {
			Province Province = (Province)list.get(0);
			mCache.__put(Province.getId(), Province);
			mCache.__put(Province.getName(), Province);
			return Province;
		}
		return null;
	}
	
	public int getCount() {
		return provinceDAO.getRowNum();
	}
	
	public List getProvinces() {
		return provinceDAO.findAll();
	}
	
	public List getProvinces(int start, int length) {
		return provinceDAO.findPageRows(start, length);
	}
	
	public void updateProvince(Province d) {
		provinceDAO.attachDirty(d);
		mCache.remove(d.getId());
		mCache.remove(d.getName());
	}
	
	public void deleteProvince(long id) {
		Province d = getProvince(id);
		if(d != null) {
			provinceDAO.delete(getProvince(id));
			mCache.remove(d.getId());
			mCache.remove(d.getName());
		}
	}

	public ProvinceDAO getProvinceDAO() {
		return provinceDAO;
	}

	public void setProvinceDAO(ProvinceDAO provinceDAO) {
		this.provinceDAO = provinceDAO;
	}

}
