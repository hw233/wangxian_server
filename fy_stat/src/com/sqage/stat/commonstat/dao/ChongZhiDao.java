package com.sqage.stat.commonstat.dao;

import java.util.ArrayList;
import java.util.List;

import com.sqage.stat.commonstat.entity.ChongZhi;

public interface ChongZhiDao {

	public ChongZhi getById(Long id);

	public ArrayList<ChongZhi> getBySql(String sql);

	public boolean deleteById(Long id);

	
	public ChongZhi addUnRecorde(ChongZhi chongZhi);
	
	public ChongZhi add(ChongZhi chongZhi);
	
	public boolean addList(ArrayList<ChongZhi>  chongZhiList);

	
	public List<String[]> getChongZhiUser(String startDateStr,String laseDay,String qudao,String fenQu,String userName,String money);
	
	/**
	 * 充值人数
	 * @param startDateStr
	 * @param endDateStr
	 * @param qudao
	 * @param fenQu
	 * @param game
	 * @return
	 */
	public Long getChongZhiUserCount(String startDateStr, String endDateStr,String qudao,String fenQu,String jixing);

	/**
	 * 以前没有付过费的用户数
	 * @param startDateStr
	 * @param endDateStr
	 * @param qudao
	 * @param fenQu
	 * @param game
	 * @return
	 */
	public Long getUnAheadChongZhiUserCount(String startDateStr, String endDateStr,String qudao,String fenQu,String jixing);
	/**
	 * 付费且当天登陆的用户。
	 * @param startDateStr
	 * @param endDateStr
	 * @param qudao
	 * @param fenQu
	 * @param game
	 * @return
	 */
	public Long getChongZhi_LoginUserCount(String startDateStr, String endDateStr,String qudao,String fenQu,String game);

	
	/**
	 * 充值数,包含手续费
	 * @param startDateStr
	 * @param endDateStr
	 * @param qudao
	 * @param fenQu
	 * @param game
	 * @return
	 */
	public Long getChongZhiCount_includeCost(String startDateStr, String endDateStr,String qudao,String fenQu,String jixing);
	
	/**
	 * 充值数，扣除手续费
	 * @param startDateStr
	 * @param endDateStr
	 * @param qudao
	 * @param fenQu
	 * @param game
	 * @return
	 */
	public Long getChongZhiCount(String startDateStr, String endDateStr,String qudao,String fenQu,String jixing);
	/**
	 * 按充值金额分部
	 * @param startDateStr
	 * @param endDateStr
	 * @param qudaoid
	 * @param fenQu
	 * @param type
	 * @param cardType
	 * @param username
	 * @param money
	 * @param game
	 * @return
	 */
	
	public ArrayList<String []> getChongZhiJinEFenbu(String startDateStr, String endDateStr, String qudaoid, String fenQu, String type,String cardType, String username, String money, String game);
	  
    /**
     * 包括充值金额区间分布
     * @param startDateStr
     * @param endDateStr
     * @param level
     * @param fenQu
     * @param money
     * @param game
     * 50元一个等级
     * @return
     */
    public List<String[]> getChongZhi_jineQuJian_fenbu(String startDateStr, String endDateStr, String level, String fenQu, String money,String jiXing);
    
    
	/**
	 * 
	 * @param startDateStr
	 * @param beginmoney 充值区间
	 * @param endmoney
	 * @param qudao
	 * @param fenQu
	 * @param jixing
	 * @return
	 */
	public ArrayList<String []> getVipChongzhi(String startDateStr,String beginmoney,String endmoney, String qudao, String fenQu,String jixing);
	
	
	  public long getChongZhi_totalMoney_unRecord(String startDateStr, String endDateStr, String qudaoid, String fenQu, 
	    		String type,String cardType, String username, String money, String jixing,String modeType);
	
	/**
	 *  获取充值信息总金额
	 * @param startDateStr
	 * @param endDateStr
	 * @param qudao
	 * @param fenQu
	 * @param type 充值类型
	 * @param username
	 * @param money
	 * @param game
	 * @return
	 */
	
    
    public long getChongZhi_totalMoney(String startDateStr, String endDateStr, String qudaoid, String fenQu, 
    		String type,String cardType, String username, String money, String jixing,String modeType);
    
    
    public List<ChongZhi> getChongZhi_unrecord(String startDateStr, String endDateStr,String qudao,String fenQu,String type,String cardType,String username,String money,String jixing,String modeType);
    
	/**
	 *  获取充值信息
	 * @param startDateStr
	 * @param endDateStr
	 * @param qudao
	 * @param fenQu
	 * @param type 充值类型
	 * @param username
	 * @param money
	 * @param game
	 * @return
	 */
	
	public List<ChongZhi> getChongZhi(String startDateStr, String endDateStr,String qudao,String fenQu,String type,String cardType,String username,String money,String jixing,String modeType);

	
	/**
	 * 除去手续费的充值总额
	 * @param startDateStr
	 * @param endDateStr
	 * @param qudaoid
	 * @param fenQu
	 * @param type
	 * @param cardtType
	 * @param username
	 * @param money
	 * @param jixing
	 * @return
	 */
	public long getChongZhi_totalMoney_cost(String startDateStr, String endDateStr, String qudaoid, String fenQu, String type,String cardtType, String username, String money,
			String jixing);
	

	/**
	 * 除去手续费的充值信息
	 * @param startDateStr
	 * @param laseDay
	 * @param qudao
	 * @param fenQu
	 * @param userName
	 * @param money
	 * @return
	 */
	public List<ChongZhi> getChongZhi_cost(String startDateStr, String endDateStr,String qudao,String fenQu,String type,String cardType,String username,String money,String jixing);


	/**
	 *  获取充值信息分布
	 * @param startDateStr
	 * @param endDateStr
	 * @param qudao
	 * @param fenQu
	 * @param type 充值类型
	 * @param username
	 * @param money
	 * @param game
	 * @return
	 */
	
	public List<String[]> getChongZhifenbu(String startDateStr, String endDateStr,String level,String fenQu,String money,String game);
  
	public List<String[]> getModeTypeDateChongZhi(String startDateStr, String endDateStr,String qudao);
	
	public List<String[]> getModeTypeChongZhi(String startDateStr, String endDateStr,String qudao,String type,String cardType,String fenQu);
	/**
	 * 分区充值
	 * @param startDateStr
	 * @param endDateStr
	 * @param qudaoid
	 * @param type
	 * @param cardType
	 * @param fenQu
	 * @return
	 */
	
	public List<String[]> getFenQuChongZhi(String startDateStr, String endDateStr,String qudaoid,String type,String cardType,String fenQu);
	
	/**
	 * 分渠道充值
	 * @param startDateStr
	 * @param endDateStr
	 * @param qudaoid
	 * @param type
	 * @param cardType
	 * @param fenQu
	 * @param money
	 * @return
	 */
	
	public List<String[]> getQuDaoChongZhi(String startDateStr, String endDateStr,String qudaoid,String type,String cardType,String fenQu);

	
	public List<String[]> queryQuDaoChongZhi(String sql);
	
	/**
	 *  获取新增充值信息分布
	 * @param startDateStr
	 * @param endDateStr
	 * @param qudao
	 * @param fenQu
	 * @param type 充值类型
	 * @param username
	 * @param money
	 * @param game
	 * @return
	 */
	
	
	public List<String[]> getNewChongZhifenbu(String startDateStr, String endDateStr,String level,String fenQu,String money,String game);

	

	/**
	 * K日千人收益
	 * @param registstartDateStr
	 * @param registEndDateStr
	 * @param startDateStr
	 * @param endDateStr
	 * @param qudaoid
	 * @param game
	 * @return
	 */
	public  ArrayList<String[]> getKRi_QianRenShouyi(String registstartDateStr,String registEndDateStr, String dayNum,String qudaoid);
	/**
	 * k日收益
	 * @param registstartDateStr
	 * @param registEndDateStr
	 * @param startDateStr
	 * @param endDateStr
	 * @param qudao
	 * @param game
	 * @return
	 */
	
	public  ArrayList<String[]> getKriShouyi(String registstartDateStr,String registEndDateStr, String startDateStr, String endDateStr, String qudao, String game);

	/**
	 * 千人收益
	 * @param startDateStr
	 * @param endDateStr
	 * @param qudao
	 * @param fenQu
	 * @param type
	 * @param username
	 * @param money
	 * @param game
	 * @return
	 */
	public  ArrayList<String[]> getQianRenShouyi(String registstartDateStr,String registEndDateStr,String startDateStr, String endDateStr,String qudaoid,String game);

	/**
	 * 获取所有充值类型
	 * @param date
	 * @return
	 */
	public ArrayList<String> getTypes(java.util.Date date);
	
	/**
	 * 获取所有充值卡类型
	 * @param date
	 * @return
	 */
	public ArrayList<String> getCardTypes();
	
	
   /**
    * 采用渠道商的充值接口充值的
    * @param registstartDateStr
    * @param registEndDateStr
    * @return
    */
	public  ArrayList<String> getSDKChongZhi(String startDateStr);
	
	/**
	    * 渠道商使用我们的接口充值的(不扣除手续费)
	    * @param registstartDateStr
	    * @param registEndDateStr
	    * @return
	    */
		public  ArrayList<String> getSqageChongZhi_nocost(String startDateStr);

	/**
	    * 渠道商使用我们的接口充值的
	    * @param registstartDateStr
	    * @param registEndDateStr
	    * @return
	    */
		public  ArrayList<String> getSqageChongZhi(String startDateStr);
            /**
             * 应用汇注册用在null渠道充值金额。
             * @param startDateStr
             * @param endDateStr
             * @param qudao
             * @param fenQu
             * @return
             */
		public Long getAppChina_null_pay(String startDateStr, String endDateStr, String qudao, String fenQu);
		/**
		 * 注册并进入游戏当天 充值情况
		 * @param startDateStr
		 * @param beginmoney
		 * @param endmoney
		 * @param qudao
		 * @param fenQu
		 * @param jixing
		 * @return
		 */
		public ArrayList<String []>getRegistEnterChongzhi(String startDateStr, String qudao, String fenQu,String jixing);
		/**
		 * 老用户充值情况     。老用户 就是以前充过值的用户
		 * @param startDateStr
		 * @param qudao
		 * @param fenQu
		 * @param jixing
		 * @return
		 */
		public ArrayList<String []>getOldPlayerChongzhi(String startDateStr, String qudao, String fenQu,String jixing);
		/**
		 * 新用户充值情况     。新用户 就是以前没有充过值的用户
		 * @param startDateStr
		 * @param qudao
		 * @param fenQu
		 * @param jixing
		 * @return
		 */
		public ArrayList<String []>getNewPlayerChongzhi(String startDateStr, String qudao, String fenQu,String jixing);
	   
		/**
		 * 充值分时监控
		 * @param startDateStr
		 * @param qudao
		 * @param fenQu
		 * @param jixing
		 * @return
		 */
		public ArrayList<String []>chongZhiJianKong(String startDateStr, String qudao, String fenQu);
		
		public ArrayList<String[]> getChongZhiInfo(String sql, String[] columusEnums);
}
