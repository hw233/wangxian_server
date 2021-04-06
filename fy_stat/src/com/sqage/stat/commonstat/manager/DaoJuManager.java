package com.sqage.stat.commonstat.manager;

import java.util.ArrayList;
import java.util.List;

import com.sqage.stat.commonstat.entity.DaoJu;

public interface DaoJuManager {

    public DaoJu getById(Long id);
	
	public List<DaoJu> getBySql(String sql);
	
	public boolean deleteById(Long id);
	
	public void addList(ArrayList<DaoJu> daoJuList);
	
	public void addDaoJu_MoHuList(ArrayList<DaoJu> daoJu_MoHuList);
	
	public DaoJu add(DaoJu daoJu);
	
	public boolean update(DaoJu daoJu);
	
	public boolean updateList(ArrayList<DaoJu> daoJuList);
	
	
	/**
	 * 获取指定道具的运营信息
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @param daojuname
	 * @param gettype
	 * @return
	 */
	public List<String[]> getDaoJuImportant(String startDateStr, String endDateStr, String fenQu,String daojuname,String daojuColor);
	
	/**
	 * 使用道具的玩家的vip
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @param daojuname
	 * @param daojuColor
	 * @return
	 */
	
	public List<String[]> getDaoJuByVip(String startDateStr, String endDateStr, String fenQu, String daojuname, String daojuColor);
	/**
	 * 获取道具的商店购买信息，供运营下载。
	 * @param startDateStr
	 * @return
	 */
	public ArrayList<String[]> getDaoJushop(String startDateStr);
	
	/**
	 * 获取道具的汇总信息，供运营下载。
	 * @param startDateStr
	 * @return
	 */
	public ArrayList<String[]> getDaoJuHuiZong(String startDateStr);
	
	
	public List<DaoJu> getByDaoJu(DaoJu daoJu);

	/**
	 * 获取道具列表
	 */
	public ArrayList<String> getDaoju(java.util.Date date);
	/**
	 * 获取货币种类
	 * @param date
	 * @return
	 */
	public ArrayList<String> getHuoBiType(java.util.Date date);
	
	
	
	public List<String[]> getColorDaoJuXiaoHao(String startDateStr, String endDateStr, String fenQu, String huobitype,String gamelevel,String daojuname,String gettype,String username);
	/**
	 * 获取道具消耗
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @param huobitype
	 * @param gamelevel
	 * @param daojuname
	 *  @param gettype//道具获取类型，购买，使用，赠送等
	 * @return
	 */
	public List<String[]> getDaoJuXiaoHao(String startDateStr, String endDateStr, String fenQu, String huobitype,String gamelevel,String daojuname,String gettype,String username);
	
	/**
	 * 获取购买道具信息
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @param huobitype
	 * @param gamelevel
	 * @param daojuname
	 *  @param gettype//道具获取类型，购买，使用，赠送等
	 * @return
	 */
	public List<String[]> getDaoJuGouMai(String startDateStr, String endDateStr, String fenQu, String huobitype,String gamelevel,String daojuname,String gettype,String username);

	/**
	 * 获取消耗分布信息
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @param huobitype
	 * @param gamelevel
	 * @param daojuname
	 * @param gettype
	 * @return
	 */
	public ArrayList<String[]> getDaoJuXiaoHaoFenBu(String startDateStr, String endDateStr, String fenQu, String huobitype,String gamelevel,String daojuname,String gettype);
	
	/**
	 * 获取敏感道具。
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @param jixing
	 * @param gettype
	 * @param daoJuType
	 * @return
	 */
	 
	public ArrayList<String[]> getSensitiveDaoJu(String startDateStr, String endDateStr, String fenQu,String jixing,String daoJuType);
	/**
	 * 获取敏感道具获取方式
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @param jixing
	 * @param daoJuType
	 * @return
	 */
	public ArrayList<String[]> getSensitiveDaoJuReasonType(String startDateStr, String endDateStr, String fenQu,String jixing,String daoJuType);
	/**
	 * 某个类型获得的道具
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @param jixing
	 * @param getType
	 * @return
	 */
	public ArrayList<String[]> getSensitiveKaiPingHuoDe(String startDateStr, String endDateStr, String fenQu, String jixing, String getType);
	
	public ArrayList<String[]> getGoDong_YinKuai(String startDateStr, String endDateStr, String fenQu);

	public ArrayList<String[]> getGoDong_YinKuai_fenqu(String startDateStr, String endDateStr, String fenQu);
	
	public ArrayList<String[]> getGoDong_HuoDe(String startDateStr, String endDateStr, String fenQu);
	public ArrayList<String[]> getGoDong_DiuShi(String startDateStr, String endDateStr, String fenQu);
	public ArrayList<String[]> getYinKuai_HuoDe(String startDateStr, String endDateStr, String fenQu);
	public ArrayList<String[]> getYinKuai_DiuShi(String startDateStr, String endDateStr, String fenQu);

	/**
	 * 根据sql获取道具信息
	 * @param sql
	 * @return
	 */
    public ArrayList<String[]> getDaoJu_MoHu(String sql);
    
    /**
	 * 根据sql获取积分信息
	 * @param sql
	 * @return
	 */
public ArrayList<String[]> getJiFeninfo(String sql);
}
