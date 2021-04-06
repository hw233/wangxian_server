package com.sqage.stat.commonstat.manager.Impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sqage.stat.commonstat.dao.Impl.DaoJuDaoImpl;
import com.sqage.stat.commonstat.entity.DaoJu;
import com.sqage.stat.commonstat.manager.DaoJuManager;

public class DaoJuManagerImpl implements DaoJuManager {

	DaoJuDaoImpl  daoJuDao;
	static DaoJuManagerImpl self;
	public void init(){
		self = this;
	}
	public static DaoJuManagerImpl getInstance() {
		return self;
	}

	public void addList(ArrayList<DaoJu> daoJuList)
	{
		daoJuDao.addList(daoJuList);
	}
	
	public void addDaoJu_MoHuList(ArrayList<DaoJu> daoJu_MoHuList)
	{
		daoJuDao.addDaoJu_MoHuList(daoJu_MoHuList);
	}
	
	@Override
	public DaoJu add(DaoJu daoJu) {
		return daoJuDao.add(daoJu);
	}

	@Override
	public boolean deleteById(Long id) {
		return daoJuDao.deleteById(id);
	}

	@Override
	public DaoJu getById(Long id) {
		return daoJuDao.getById(id);
	}

	@Override
	public List<DaoJu> getBySql(String sql) {
		return daoJuDao.getBySql(sql);
	}

	@Override
	public List<DaoJu> getByDaoJu(DaoJu daoJu) {
		return daoJuDao.getByDaoJu(daoJu);
	}
	@Override
	public boolean update(DaoJu daoJu) {
		return daoJuDao.update(daoJu);
	}

	@Override
	public boolean updateList(ArrayList<DaoJu> daoJuList) {
		return daoJuDao.updateList(daoJuList);
	}
	
	@Override
	public List<String[]> getDaoJuImportant(String startDateStr, String endDateStr, String fenQu, String daojuname, String daojuColor) {
		return daoJuDao.getDaoJuImportant(startDateStr, endDateStr, fenQu, daojuname, daojuColor);
	}
	
	@Override
	public List<String[]> getDaoJuByVip(String startDateStr, String endDateStr, String fenQu, String daojuname, String daojuColor) {
		return daoJuDao.getDaoJuByVip(startDateStr, endDateStr, fenQu, daojuname, daojuColor);
	}
	@Override
	public ArrayList<String[]> getDaoJushop(String startDateStr) {
		return daoJuDao.getDaoJushop(startDateStr);
	}
	@Override
	public ArrayList<String[]> getDaoJuHuiZong(String startDateStr) {
		return daoJuDao.getDaoJuHuiZong(startDateStr);
	}
	public DaoJuDaoImpl getDaoJuDao() {
		return daoJuDao;
	}
	public void setDaoJuDao(DaoJuDaoImpl daoJuDao) {
		this.daoJuDao = daoJuDao;
	}
	@Override
	public ArrayList<String> getDaoju(Date date) {
		return daoJuDao.getDaoju(date);
	}
	
	
	
	@Override
	public List<String[]> getColorDaoJuXiaoHao(String startDateStr, String endDateStr, String fenQu, String huobitype, String gamelevel,
			String daojuname, String gettype, String username) {
		return daoJuDao.getColorDaoJuXiaoHao(startDateStr, endDateStr, fenQu, huobitype, gamelevel, daojuname, gettype, username);
	}
	@Override
	public List<String[]> getDaoJuXiaoHao(String startDateStr, String endDateStr, String fenQu, String huobitype, String gamelevel, String daojuname,
			String gettype, String username) {
		return daoJuDao.getDaoJuXiaoHao(startDateStr, endDateStr, fenQu, huobitype, gamelevel, daojuname, gettype, username);
	}
	@Override
	public List<String[]> getDaoJuGouMai(String startDateStr, String endDateStr, String fenQu, String huobitype, String gamelevel, String daojuname,
			String gettype,String username) {
		return daoJuDao.getDaoJuGouMai(startDateStr, endDateStr, fenQu, huobitype, gamelevel, daojuname, gettype,username);
	}
	@Override
	public ArrayList<String[]> getDaoJuXiaoHaoFenBu(String startDateStr, String endDateStr, String fenQu, String huobitype, String gamelevel,
			String daojuname, String gettype) {
		return daoJuDao.getDaoJuXiaoHaoFenBu(startDateStr, endDateStr, fenQu, huobitype, gamelevel, daojuname, gettype);
	}
	@Override
	public ArrayList<String> getHuoBiType(Date date) {
		return daoJuDao.getHuoBiType(date);
	}
	@Override
	public ArrayList<String[]> getSensitiveDaoJu(String startDateStr, String endDateStr, String fenQu, String jixing, String daoJuType) {
		return daoJuDao.getSensitiveDaoJu(startDateStr, endDateStr, fenQu, jixing, daoJuType);
	}
	@Override
	public ArrayList<String[]> getSensitiveDaoJuReasonType(String startDateStr, String endDateStr, String fenQu, String jixing, String daoJuType) {
		return daoJuDao.getSensitiveDaoJuReasonType(startDateStr, endDateStr, fenQu, jixing, daoJuType);
	}
	@Override
	public ArrayList<String[]> getSensitiveKaiPingHuoDe(String startDateStr, String endDateStr, String fenQu, String jixing, String getType) {
		return daoJuDao.getSensitiveKaiPingHuoDe(startDateStr, endDateStr, fenQu, jixing, getType);
	}
	@Override
	public ArrayList<String[]> getGoDong_YinKuai(String startDateStr, String endDateStr, String fenQu) {
		return daoJuDao.getGoDong_YinKuai(startDateStr, endDateStr, fenQu);
	}
	@Override
	public ArrayList<String[]> getGoDong_YinKuai_fenqu(String startDateStr, String endDateStr, String fenQu) {
		return daoJuDao.getGoDong_YinKuai_fenqu(startDateStr, endDateStr, fenQu);
	}
	
	
	@Override
	public ArrayList<String[]> getGoDong_DiuShi(String startDateStr, String endDateStr, String fenQu) {
		return daoJuDao.getGoDong_DiuShi(startDateStr, endDateStr, fenQu);
	}
	@Override
	public ArrayList<String[]> getGoDong_HuoDe(String startDateStr, String endDateStr, String fenQu) {
		return daoJuDao.getGoDong_HuoDe(startDateStr, endDateStr, fenQu);
	}
	@Override
	public ArrayList<String[]> getYinKuai_DiuShi(String startDateStr, String endDateStr, String fenQu) {
		return daoJuDao.getYinKuai_DiuShi(startDateStr, endDateStr, fenQu);
	}
	@Override
	public ArrayList<String[]> getYinKuai_HuoDe(String startDateStr, String endDateStr, String fenQu) {
		return daoJuDao.getYinKuai_HuoDe(startDateStr, endDateStr, fenQu);
	}
	@Override
	public ArrayList<String[]> getDaoJu_MoHu(String sql) {
		return daoJuDao.getDaoJu_MoHu(sql);
	}
	@Override
	public ArrayList<String[]> getJiFeninfo(String sql) {
		return daoJuDao.getJiFeninfo(sql);
	}

	
}
