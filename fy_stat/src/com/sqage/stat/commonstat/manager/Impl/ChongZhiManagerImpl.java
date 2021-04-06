package com.sqage.stat.commonstat.manager.Impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sqage.stat.commonstat.dao.Impl.ChongZhiDaoImpl;
import com.sqage.stat.commonstat.entity.ChongZhi;
import com.sqage.stat.commonstat.manager.ChongZhiManager;

public class ChongZhiManagerImpl implements ChongZhiManager {

	ChongZhiDaoImpl chongZhiDao;
	
	static ChongZhiManagerImpl self;
		
	public void init(){ self = this; }
	
	public static ChongZhiManagerImpl getInstance() {
			return self;
		}
	

	public ChongZhiDaoImpl getChongZhiDao() {
		return chongZhiDao;
	}

	public void setChongZhiDao(ChongZhiDaoImpl chongZhiDao) {
		this.chongZhiDao = chongZhiDao;
	}

	@Override
	public ChongZhi addUnRecorde(ChongZhi chongZhi) {
		return chongZhiDao.addUnRecorde(chongZhi);
	}
	
	@Override
	public ChongZhi add(ChongZhi chongZhi) {
		return chongZhiDao.add(chongZhi);
	}

	public boolean addList(ArrayList<ChongZhi> chongZhiList) {
		return chongZhiDao.addList(chongZhiList);
	}

	@Override
	public boolean deleteById(Long id) {
		return chongZhiDao.deleteById(id);
	}

	@Override
	public ChongZhi getById(Long id) {
		return chongZhiDao.getById(id);
	}

	@Override
	public ArrayList<ChongZhi> getBySql(String sql) {
		return chongZhiDao.getBySql(sql);
	}

	
	
	@Override
	public List<String[]> getChongZhiUser(String startDateStr,String lastday, String qudao, String fenQu, String userName, String money) {
		return chongZhiDao.getChongZhiUser(startDateStr,lastday, qudao, fenQu, userName, money);
	}


	@Override
	public Long getChongZhiCount_includeCost(String startDateStr, String endDateStr, String qudao, String fenQu, String jixing) {
		return chongZhiDao.getChongZhiCount_includeCost(startDateStr, endDateStr, qudao, fenQu, jixing);
	}
	
	@Override
	public Long getChongZhiCount(String startDateStr, String endDateStr, String qudao, String fenQu, String jixing) {
		return chongZhiDao.getChongZhiCount(startDateStr, endDateStr, qudao, fenQu, jixing);
	}

	@Override
	public Long getChongZhiUserCount(String startDateStr, String endDateStr, String qudao, String fenQu, String jixing) {
		return chongZhiDao.getChongZhiUserCount(startDateStr, endDateStr, qudao, fenQu, jixing);
	}

	@Override
	public Long getChongZhi_LoginUserCount(String startDateStr, String endDateStr, String qudao, String fenQu, String game) {
		return chongZhiDao.getChongZhi_LoginUserCount(startDateStr, endDateStr, qudao, fenQu, game);
	}

	@Override
	public Long getUnAheadChongZhiUserCount(String startDateStr, String endDateStr, String qudao, String fenQu, String jixing) {
		return chongZhiDao.getUnAheadChongZhiUserCount(startDateStr, endDateStr, qudao, fenQu, jixing);
	}

//	@Override
//	public boolean update(ChongZhi chongZhi) {
//		return chongZhiDao.update(chongZhi);
//	}

	@Override
	public ArrayList<String[]> getChongZhiJinEFenbu(String startDateStr, String endDateStr, String qudaoid, String fenQu, String type,
			String cardType, String username, String money, String game) {
		return chongZhiDao.getChongZhiJinEFenbu(startDateStr, endDateStr, qudaoid, fenQu, type, cardType, username, money, game);
	}
	
	
	@Override
	public List<ChongZhi> getChongZhi_unrecord(String startDateStr, String endDateStr, String qudaoid, String fenQu, String type,String cardtType, String username, String money,
			String jixing,String modeType) {
		return chongZhiDao.getChongZhi_unrecord(startDateStr, endDateStr, qudaoid, fenQu, type,cardtType, username, money, jixing,modeType);
	}

	@Override
	public List<ChongZhi> getChongZhi(String startDateStr, String endDateStr, String qudaoid, String fenQu, String type,String cardtType, String username, String money,
			String jixing,String modeType) {
		return chongZhiDao.getChongZhi(startDateStr, endDateStr, qudaoid, fenQu, type,cardtType, username, money, jixing,modeType);
	}
	
	
	@Override
	public long getChongZhi_totalMoney_unRecord(String startDateStr, String endDateStr, String qudaoid, String fenQu, String type,String cardtType, String username, String money,
			String jixing,String modeType) {
		return chongZhiDao.getChongZhi_totalMoney_unRecord(startDateStr, endDateStr, qudaoid, fenQu, type,cardtType, username, money, jixing,modeType);
	}

	@Override
	public long getChongZhi_totalMoney(String startDateStr, String endDateStr, String qudaoid, String fenQu, String type,String cardtType, String username, String money,
			String jixing,String modeType) {
		return chongZhiDao.getChongZhi_totalMoney(startDateStr, endDateStr, qudaoid, fenQu, type,cardtType, username, money, jixing,modeType);
	}
	
	
	public ArrayList<String []> getVipChongzhi(String startDateStr,String beginmoney,String endmoney, String qudao, String fenQu,String jixing)
	{
		return chongZhiDao.getVipChongzhi(startDateStr, beginmoney, endmoney, qudao, fenQu, jixing);
	}
	
	@Override
	public long getChongZhi_totalMoney_cost(String startDateStr, String endDateStr, String qudaoid, String fenQu, String type, String cardType,
			String username, String money, String jixing) {
		return chongZhiDao.getChongZhi_totalMoney_cost(startDateStr, endDateStr, qudaoid, fenQu, type, cardType, username, money, jixing);
	}
	
	@Override
	public List<ChongZhi> getChongZhi_cost(String startDateStr, String endDateStr, String qudao, String fenQu, String type, String cardType,
			String username, String money, String jixing) {
		return chongZhiDao.getChongZhi_cost(startDateStr, endDateStr, qudao, fenQu, type, cardType, username, money, jixing);
	}

	@Override
	public List<String[]> getChongZhi_jineQuJian_fenbu(String startDateStr, String endDateStr, String level, String fenQu, String money,String jiXing) {
		return chongZhiDao.getChongZhi_jineQuJian_fenbu(startDateStr, endDateStr, level, fenQu, money,jiXing);
	}
	@Override
	public List<String[]> getFenQuChongZhi(String startDateStr, String endDateStr, String qudaoid, String type, String cardType, String fenQu) {
		return chongZhiDao.getFenQuChongZhi(startDateStr, endDateStr, qudaoid, type, cardType, fenQu);
	}

	
	@Override
	public List<String[]> getModeTypeChongZhi(String startDateStr, String endDateStr, String qudaoid, String type, String cardType, String fenQu) {
		return chongZhiDao.getModeTypeChongZhi(startDateStr, endDateStr, qudaoid, type, cardType, fenQu);
	}

	@Override
	public List<String[]> getModeTypeDateChongZhi(String startDateStr, String endDateStr, String qudao) {
		return chongZhiDao.getModeTypeDateChongZhi(startDateStr, endDateStr, qudao);
	}
	
	@Override
	public List<String[]> getQuDaoChongZhi(String startDateStr, String endDateStr, String qudaoid, String type, String cardType, String fenQu) {
		return chongZhiDao.getQuDaoChongZhi(startDateStr, endDateStr, qudaoid, type, cardType, fenQu);
	}

	public List<String[]> queryQuDaoChongZhi(String sql)
	{
		return chongZhiDao.queryQuDaoChongZhi(sql);
		
		
	}
	@Override
	public List<String[]> getChongZhifenbu(String startDateStr, String endDateStr, String level, String fenQu, String money, String game) {
		return chongZhiDao.getChongZhifenbu(startDateStr, endDateStr, level, fenQu, money, game);
	}

	@Override
	public List<String[]> getNewChongZhifenbu(String startDateStr, String endDateStr, String level, String fenQu, String money, String game) {
		return chongZhiDao.getNewChongZhifenbu(startDateStr, endDateStr, level, fenQu, money, game);
	}

	@Override
	public ArrayList<String[]> getKRi_QianRenShouyi(String registstartDateStr, String registEndDateStr, String dayNum, String qudaoid) {
		return chongZhiDao.getKRi_QianRenShouyi(registstartDateStr, registEndDateStr, dayNum, qudaoid);
	}

	@Override
	public ArrayList<String[]> getKriShouyi(String registstartDateStr, String registEndDateStr, String startDateStr, String endDateStr, String qudao,
			String game) {
		return chongZhiDao.getKriShouyi(registstartDateStr, registEndDateStr, startDateStr, endDateStr, qudao, game);
	}

	@Override
	public ArrayList<String[]> getQianRenShouyi(String registstartDateStr, String registEndDateStr, String startDateStr, String endDateStr, String qudaoid,
			String game) {
		return chongZhiDao.getQianRenShouyi(registstartDateStr, registEndDateStr, startDateStr, endDateStr, qudaoid, game);
	}

	@Override
	public List<String[]> getChongZhifenbu_(String startDateStr, String endDateStr, String level, String fenQu, String money, String game) {
		return chongZhiDao.getChongZhifenbu_(startDateStr, endDateStr, level, fenQu, money, game);
	}

	@Override
	public ArrayList<String> getTypes(Date date) {
		return chongZhiDao.getTypes(date);
	}

	@Override
	public ArrayList<String> getCardTypes() {
		return chongZhiDao.getCardTypes();
	}

	@Override
	public ArrayList<String> getSDKChongZhi(String startDateStr) {
		return chongZhiDao.getSDKChongZhi(startDateStr);
	}

	
	
	@Override
	public ArrayList<String> getSqageChongZhi_nocost(String startDateStr) {
		return chongZhiDao.getSqageChongZhi_nocost(startDateStr);
	}

	@Override
	public ArrayList<String> getSqageChongZhi(String startDateStr) {
		return chongZhiDao.getSqageChongZhi(startDateStr);
	}

	@Override
	public Long getAppChina_null_pay(String startDateStr, String endDateStr, String qudao, String fenQu) {
		return chongZhiDao.getAppChina_null_pay(startDateStr, endDateStr, qudao, fenQu);
		
	}

	@Override
	public ArrayList<String[]> getNewPlayerChongzhi(String startDateStr, String qudao, String fenQu, String jixing) {
		return chongZhiDao.getNewPlayerChongzhi(startDateStr, qudao, fenQu, jixing);
	}

	@Override
	public ArrayList<String[]> getOldPlayerChongzhi(String startDateStr, String qudao, String fenQu, String jixing) {
		return chongZhiDao.getOldPlayerChongzhi(startDateStr, qudao, fenQu, jixing);
	}

	@Override
	public ArrayList<String[]> getRegistEnterChongzhi(String startDateStr, String qudao, String fenQu, String jixing) {
		return chongZhiDao.getRegistEnterChongzhi(startDateStr, qudao, fenQu, jixing);
	}

	@Override
	public ArrayList<String[]> chongZhiJianKong(String startDateStr, String qudao, String fenQu) {
		return chongZhiDao.chongZhiJianKong(startDateStr, qudao, fenQu);
	}
	
	public ArrayList<String[]> getChongZhiInfo(String sql, String[] columusEnums) {
		return chongZhiDao.getChongZhiInfo(sql, columusEnums);
	}
}
