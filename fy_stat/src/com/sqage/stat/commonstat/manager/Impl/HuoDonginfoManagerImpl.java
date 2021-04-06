package com.sqage.stat.commonstat.manager.Impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.sqage.stat.commonstat.dao.Impl.HuoDonginfoDaoImpl;
import com.sqage.stat.commonstat.entity.HuoDonginfo;
import com.sqage.stat.commonstat.manager.HuoDonginfoManager;

public class HuoDonginfoManagerImpl implements HuoDonginfoManager {

	HuoDonginfoDaoImpl  huoDonginfoDao;
	static HuoDonginfoManagerImpl self;
	public void init(){
		self = this;
	}
	public static HuoDonginfoManagerImpl getInstance() {
		return self;
	}
	@Override
	public HuoDonginfo add(HuoDonginfo huoDonginfo) {
		return huoDonginfoDao.add(huoDonginfo);
	}
	
	public boolean addList(List<HuoDonginfo> ls){
		return huoDonginfoDao.addList(ls);
	}
	@Override
	public boolean deleteById(Long id) {
		return huoDonginfoDao.deleteById(id);
	}
	@Override
	public HuoDonginfo getById(Long id) {
		return huoDonginfoDao.getById(id);
	}
	@Override
	public List<HuoDonginfo> getBySql(String sql) {
		return huoDonginfoDao.getBySql(sql);
	}
	@Override
	public ArrayList<String> gethuoDonginfos(Date date) {
		// TODO Auto-generated method stub
		return huoDonginfoDao.gethuoDonginfos(date);
	}
	@Override
	public List<String[]> gethuoDonginfoShouYi(String startDateStr, String endDateStr, String fenQu, String taskName, String gamelevel) {
		return huoDonginfoDao.gethuoDonginfoShouYi(startDateStr, endDateStr, fenQu, taskName, gamelevel);
	}
	
	@Override
    public List<HuoDonginfo> gethuoDonginfoShouYiDetail(String startDateStr, String endDateStr, String fenQu, String taskName, String gamelevel) {
	   return huoDonginfoDao.gethuoDonginfoShouYiDetail(startDateStr, endDateStr, fenQu, taskName, gamelevel);
	}
	
	public HashMap gethuoDonginfoShouYiDetailHashMap(String startDateStr, String endDateStr, String fenQu, String taskName, String gamelevel) {
		List<HuoDonginfo> huodongList=huoDonginfoDao.gethuoDonginfoShouYiDetail(startDateStr, endDateStr, fenQu, taskName, gamelevel);
		HashMap map=new HashMap();
		for(HuoDonginfo huoDonginfo:huodongList)
       {
			String award = huoDonginfo.getAward();
			if (award != null && !"".equals(award.trim())) {
				String[] daoju = award.split("[+]");
				for (int i = 0; i < daoju.length; i++) {
					if (daoju[i] != null && !"".equals(daoju[i].trim())) {
						if(map.containsKey(daoju[i]))
						{
						Long daujunum=(Long)map.get(daoju[i]);
						daujunum+=1;
						map.put(daoju[i],daujunum);
						}else{
						map.put(daoju[i], 1L);
						}
					}
				}
			}
		}
//		Iterator iter = map.entrySet().iterator();
//		  while (iter.hasNext()) 
//		  {
//		   Map.Entry entry = (Map.Entry) iter.next();
//		   Object key = entry.getKey();
//		   Object val = entry.getValue();
//		   }
		return map;
	}
	@Override
	public List<String[]> gethuoDonginfoStat(String startDateStr, String endDateStr, String fenQu, String taskName, String tasktype, String status) {
		return huoDonginfoDao.gethuoDonginfoStat(startDateStr, endDateStr, fenQu, taskName, tasktype, status);
	}
	@Override
	public ArrayList<String> gethuoDonginfoType(Date date) {
		return huoDonginfoDao.gethuoDonginfoType(date);
	}
	@Override
	public boolean update(HuoDonginfo huoDonginfo) {
		return huoDonginfoDao.update(huoDonginfo);
	}
	@Override
	public boolean updateList(ArrayList<HuoDonginfo> huoDonginfoList) {
		return huoDonginfoDao.updateList(huoDonginfoList);
	}
	public HuoDonginfoDaoImpl getHuoDonginfoDao() {
		return huoDonginfoDao;
	}
	public void setHuoDonginfoDao(HuoDonginfoDaoImpl huoDonginfoDao) {
		this.huoDonginfoDao = huoDonginfoDao;
	}
	
}
