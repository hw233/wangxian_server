package com.fy.boss.gm.gmuser;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.fy.boss.gm.gmuser.ChargeStatRecord;
import com.fy.boss.gm.gmuser.GameChargeRecord;
import com.fy.boss.gm.gmuser.Stat_TableConfig;
import com.xuanzhi.tools.configuration.Configuration;
import com.xuanzhi.tools.configuration.ConfigurationException;
import com.xuanzhi.tools.configuration.DefaultConfiguration;
import com.xuanzhi.tools.configuration.DefaultConfigurationBuilder;
import com.xuanzhi.tools.configuration.DefaultConfigurationSerializer;
import com.xuanzhi.tools.text.FileUtils;


public class ChargeStatRecord {

	
	public static Logger logger = Logger.getLogger(ChargeStatRecord.class);
	
	//区别于QQ服统计
	public  String url = "jdbc:oracle:thin:@116.213.192.219:1521:ora11g";
	public String user = "stat_mieshi";
	public String password = "stat_mieshi";
	public Connection conn = null;
//	public PreparedStatement pstat = null;
//	public ResultSet result = null;
	
	//------------------QQ------------------//
	public String url_qq = "jdbc:oracle:thin:@116.213.192.219:1521:ora11g";
	public String user_qq = "STAT_MIESHI_QQ";
	public String password_qq = "STAT_MIESHI_QQ";
	public Connection conn_qq = null;
//	public PreparedStatement pstat_qq = null;
//	public ResultSet result_qq = null;
	
	//统计数据库的配置
	public String filename;
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public List<Stat_TableConfig> configs = new ArrayList<Stat_TableConfig>();
	
	
	private static ChargeStatRecord self = null;
	
	public static ChargeStatRecord getInstance() {
		return self;
	}
	
	public void init(){
		long now = System.currentTimeMillis();
		try {
			self = this;
			configs = loadRecords(filename);
			System.out.println("[初始化充值消耗流水记录] [成功] [configssize:"+configs.size()+"] [耗时："+(System.currentTimeMillis()-now)+"]");
		} catch (Exception e) {
			System.out.println("[初始化充值消耗流水记录] [异常] [configssize:"+configs.size()+"] [耗时："+(System.currentTimeMillis()-now)+"] [异常] ["+e+"]");
		}
		
	}
	
	public  List<Stat_TableConfig> loadRecords(String xmlname) {
		long now = System.currentTimeMillis();
		try {
			Configuration rootConf = new DefaultConfigurationBuilder().buildFromFile(xmlname);
			Configuration typesConf[] = rootConf.getChildren();
			for (int i = 0; i < typesConf.length; i++) {
				Stat_TableConfig config = new Stat_TableConfig();
				String tableName = typesConf[i].getAttribute("tableName","");
				String dateType = typesConf[i].getAttribute("dateType","");
				String addTime = typesConf[i].getAttribute("addTime","");
				String timeLimits = typesConf[i].getAttribute("timeLimits", "");
				config.setTableName(tableName);
				config.setDateType(dateType);
				config.setAddTime(addTime);
				config.setTimeLimits(timeLimits);
				configs.add(config);
			}
			if (logger.isInfoEnabled()) {
				logger.info("[加载统计数据库表更新配置] [成功] [数量："+configs.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
			}
		} catch (Exception e) {
			logger.warn("[加载统计数据库表更新配置] [异常] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		return configs;
	}
	
	public boolean delOneConfig(String time){
		long now = System.currentTimeMillis();
		if(configs.size()>0){
			for(Stat_TableConfig config:configs){
				if(config.getAddTime().equals(time)){
					configs.remove(config);
					Configuration rootConf = new DefaultConfiguration("configs", "-");
					for (Stat_TableConfig conf : configs) {
						Configuration typeConf = new DefaultConfiguration("conf", "-");
						typeConf.setAttribute("tableName", conf.getTableName());
						typeConf.setAttribute("dateType", conf.getDateType());
						typeConf.setAttribute("addTime", conf.getAddTime());
						typeConf.setAttribute("timeLimits", conf.getTimeLimits());
						rootConf.addChild(typeConf);
					}
					FileUtils.chkFolder(filename);
					try {
						new DefaultConfigurationSerializer().serializeToFile(new File(filename), rootConf);
						
						if (logger.isInfoEnabled()) {
							logger.info("[删除保存统计数据库表更新配置] [成功] [size:"+configs.size()+"] [tabelname:"+config.getTableName()+"] [dateType:"+config.getDateType()+"] [addTime:"+config.getAddTime()+"] [成功] [耗时："+(System.currentTimeMillis()-now)+"ms]");
						}
						return true;
					} catch (SAXException e) {
						e.printStackTrace();
						logger.info("[删除保存统计数据库表更新配置] [异常:"+e+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
					} catch (IOException e) {
						e.printStackTrace();
						logger.info("[删除保存统计数据库表更新配置] [异常:"+e+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
					} catch (ConfigurationException e) {
						e.printStackTrace();
						logger.info("[删除保存统计数据库表更新配置] [异常:"+e+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
					}
				}
			}
		}
		
		return false;
	}
	
	
	//添加一条新配置
	public boolean addNewConfig(Stat_TableConfig config){
		long now = System.currentTimeMillis();
		if(config!=null){
			configs.add(config);
			Configuration rootConf = new DefaultConfiguration("configs", "-");
			for (Stat_TableConfig conf : configs) {
				Configuration typeConf = new DefaultConfiguration("conf", "-");
				typeConf.setAttribute("tableName", conf.getTableName());
				typeConf.setAttribute("dateType", conf.getDateType());
				typeConf.setAttribute("addTime", conf.getAddTime());
				typeConf.setAttribute("timeLimits", conf.getTimeLimits());
				rootConf.addChild(typeConf);
			}
			FileUtils.chkFolder(filename);
			try {
				new DefaultConfigurationSerializer().serializeToFile(new File(filename), rootConf);
				
				if (logger.isInfoEnabled()) {
					logger.info("[保存统计数据库表更新配置] [成功] [size:"+configs.size()+"] [tabelname:"+config.getTableName()+"] [dateType:"+config.getDateType()+"] [addTime:"+config.getAddTime()+"] [成功] [耗时："+(System.currentTimeMillis()-now)+"ms]");
				}
				return true;
			} catch (SAXException e) {
				e.printStackTrace();
				logger.info("[保存统计数据库表更新配置] [异常:"+e+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
			} catch (IOException e) {
				e.printStackTrace();
				logger.info("[保存统计数据库表更新配置] [异常:"+e+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
			} catch (ConfigurationException e) {
				e.printStackTrace();
				logger.info("[保存统计数据库表更新配置] [异常:"+e+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
			}
		}
		return false;
	}
	
	public List<Stat_TableConfig> getAllConfigs(){
		return configs;
	}
	
	//获得连接
	public Connection getConnection(String type){
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			logger.warn("[类加载] [异常] ",e);
			e.printStackTrace();
		}
		
		try {
			if("QQ".equals(type)){
				conn_qq = DriverManager.getConnection(url_qq, user_qq, password_qq);
				return conn_qq;
			}else{
				conn = DriverManager.getConnection(url, user, password);
				return conn;
			}
			
		} catch (SQLException e) {
			logger.warn("[创建连接] [异常] ",e);
			e.printStackTrace();
		}
		return null;
	}
	
	
	//通过用户名得到充值流水记录
	public List<GameChargeRecord> getRecordsByUsername(String username,String type,String time){
		long now = System.currentTimeMillis();
		String tablenameS = getTableName(time,type);
		List<GameChargeRecord> records = new ArrayList<GameChargeRecord>();
		String times[] = time.split("-");
		Calendar cl = Calendar.getInstance();
		cl.clear();
		cl.set(Integer.parseInt(times[0]), Integer.parseInt(times[1])-1, Integer.parseInt(times[2]));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String begin = sdf.format(new Date(cl.getTimeInMillis()));
		String end = sdf.format(new Date(cl.getTimeInMillis()+24*60*60*1000));
		if(!"".equals(tablenameS)){
			String sql = "select * from "+tablenameS+" where userName = ? and time >= to_date(?,'yyyy-MM-dd') and time < to_date(?,'yyyy-MM-dd')";
			if("QQ".equals(type)){
				PreparedStatement pstat_qq = null;
				ResultSet result_qq =null;
				if(conn_qq==null){
					conn_qq = getConnection("QQ");
				}
				try {
					pstat_qq = conn_qq.prepareStatement(sql);
					pstat_qq.setString(1, username);
					pstat_qq.setString(2, begin);
					pstat_qq.setString(3, end);
				} catch (SQLException e) {
					logger.warn("[通过用户名得到充值流水记录QQ] [创建statement] [异常] ",e);
					e.printStackTrace();
				}
				
				try {
					result_qq = pstat_qq.executeQuery();
					while (result_qq.next()) {
						GameChargeRecord gamechongzhi = new GameChargeRecord();
						gamechongzhi.setUserName(result_qq.getString("userName"));
						gamechongzhi.setTime(new java.util.Date(result_qq.getTimestamp("time").getTime()));
						gamechongzhi.setReasonType(result_qq.getString("reasonType"));
						gamechongzhi.setQuDao(result_qq.getString("quDao"));
						gamechongzhi.setMoney(result_qq.getLong("money"));
						gamechongzhi.setJiXing(result_qq.getString("jiXing"));
						gamechongzhi.setGameLevel(result_qq.getString("gameLevel"));
						gamechongzhi.setGame(result_qq.getString("game"));
						gamechongzhi.setFenQu(result_qq.getString("fenQu"));
						gamechongzhi.setCurrencyType(result_qq.getString("currencyType"));
						gamechongzhi.setAction(result_qq.getInt("action"));
						records.add(gamechongzhi);
					}
				} catch (SQLException e) {
					logger.warn("[通过用户名得到充值流水记录QQ] [执行SQL，获得结果] [异常] ",e);
					e.printStackTrace();
				}finally{
					try {
						result_qq.close();
						pstat_qq.close();
						
					} catch (SQLException e) {
						logger.warn("[关闭statQQ] [异常] ",e);
						e.printStackTrace();
					}
				}
			}else{
				PreparedStatement pstat = null;
				ResultSet result =null;
				if(conn==null){
					conn = getConnection("");
				}
				try {
					pstat = conn.prepareStatement(sql);
					pstat.setString(1, username);
					pstat.setString(2, begin);
					pstat.setString(3, end);
				} catch (SQLException e) {
					logger.warn("[通过用户名得到充值流水记录] [创建statement] [异常] ",e);
					e.printStackTrace();
				}
				
				try {
					long start = System.currentTimeMillis();
					result = pstat.executeQuery();
//					logger.warn("[executeQuery--------耗时---------"+(System.currentTimeMillis()-start)+"]");
					while (result.next()) {
						GameChargeRecord gamechongzhi = new GameChargeRecord();
						gamechongzhi.setUserName(result.getString("userName"));
						gamechongzhi.setTime(new java.util.Date(result.getTimestamp("time").getTime()));
						gamechongzhi.setReasonType(result.getString("reasonType"));
						gamechongzhi.setQuDao(result.getString("quDao"));
						gamechongzhi.setMoney(result.getLong("money"));
						gamechongzhi.setJiXing(result.getString("jiXing"));
						gamechongzhi.setGameLevel(result.getString("gameLevel"));
						gamechongzhi.setGame(result.getString("game"));
						gamechongzhi.setFenQu(result.getString("fenQu"));
						gamechongzhi.setCurrencyType(result.getString("currencyType"));
						gamechongzhi.setAction(result.getInt("action"));
						records.add(gamechongzhi);
					}
//					logger.warn("[recordssize:"+records.size()+"] [result.next()--------耗时---------"+(System.currentTimeMillis()-start)+"]");
				} catch (SQLException e) {
					logger.warn("[通过用户名得到充值流水记录] [执行SQL，获得结果] [异常] [sql:"+sql+"]",e);
					e.printStackTrace();
				}finally{
					try {
						result.close();
						pstat.close();
						
					} catch (SQLException e) {
						logger.warn("[关闭stat] [异常] ",e);
						e.printStackTrace();
					}
				}
				
			}
		
		}
		
		if(records.size()>0){
			logger.info("[通过用户名获取数据] [成功] [数据长度："+records.size()+"] [耗时："+(System.currentTimeMillis()-now)+"-ms]");
			return records;
		}else {
			return null;
		}
		
	}
 	
	//通过货币类型得到充值流水记录
	public List<GameChargeRecord> getRecordsByMoneyTypeAndUser(String moneyType,String userName,String type,String time){
		long now = System.currentTimeMillis();
		List<GameChargeRecord> records = new ArrayList<GameChargeRecord>();
		String tablename = getTableName(time,type);
		logger.warn("moneyType:"+moneyType+"userName:"+userName+"-type:"+type+"-time:"+time+"-tablename:"+tablename+"--------1");
		String times[] = time.split("-");
		Calendar cl = Calendar.getInstance();
		cl.clear();
		cl.set(Integer.parseInt(times[0]), Integer.parseInt(times[1])-1, Integer.parseInt(times[2]));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String begin = sdf.format(new Date(cl.getTimeInMillis()));
		String end = sdf.format(new Date(cl.getTimeInMillis()+24*60*60*1000));
		
		if(!tablename.trim().equals("")){
			String sql = "select * from "+tablename+" where currencyType = ? and userName = ? and time >= to_date(?,'yyyy-MM-dd') and time < to_date(?,'yyyy-MM-dd')";
			logger.warn("sql:"+sql+"--------2");
			if(type.trim().equals("QQ")){
				 	PreparedStatement pstat_qq = null;
					ResultSet result_qq =null;
					if(conn_qq==null){
						conn_qq = getConnection("QQ");
					}
					
					try {
						pstat_qq = conn_qq.prepareStatement(sql);
						pstat_qq.setString(1, moneyType);
						pstat_qq.setString(2, userName);
						pstat_qq.setString(3, begin);
						pstat_qq.setString(4, end);
					} catch (SQLException e) {
						logger.warn("[通过货币类型得到充值流水记录QQ] [创建statement] [异常] ",e);
						e.printStackTrace();
					}
					
					try {
						result_qq = pstat_qq.executeQuery();
						while (result_qq.next()) {
							GameChargeRecord gamechongzhi = new GameChargeRecord();
							gamechongzhi.setUserName(result_qq.getString("userName"));
							gamechongzhi.setTime(new java.util.Date(result_qq.getTimestamp("time").getTime()));
							gamechongzhi.setReasonType(result_qq.getString("reasonType"));
							gamechongzhi.setQuDao(result_qq.getString("quDao"));
							gamechongzhi.setMoney(result_qq.getLong("money"));
							gamechongzhi.setJiXing(result_qq.getString("jiXing"));
							gamechongzhi.setGameLevel(result_qq.getString("gameLevel"));
							gamechongzhi.setGame(result_qq.getString("game"));
							gamechongzhi.setFenQu(result_qq.getString("fenQu"));
							gamechongzhi.setCurrencyType(result_qq.getString("currencyType"));
							gamechongzhi.setAction(result_qq.getInt("action"));
							records.add(gamechongzhi);
						}
					} catch (SQLException e) {
						logger.warn("[通过货币类型得到充值流水记录QQ] [执行SQL，获得结果] [异常] ",e);
						e.printStackTrace();
					}finally{
						try {
							result_qq.close();
							pstat_qq.close();
							
						} catch (SQLException e) {
							logger.warn("[关闭statQQ] [异常] ",e);
							e.printStackTrace();
						}
						
					}
				}else{
					PreparedStatement pstat = null;
					ResultSet result =null;
					if(conn==null){
						conn = getConnection("");
					}
					
					try {
						logger.warn("--------3");
						logger.warn("conn:"+conn+"-result:"+result+"--------3");
						pstat = conn.prepareStatement(sql);
						pstat.setString(1, moneyType);
						pstat.setString(2, userName);
						pstat.setString(3, begin);
						pstat.setString(4, end);
					} catch (SQLException e) {
						logger.warn("[通过货币类型得到充值流水记录] [创建statement] [异常] ",e);
						e.printStackTrace();
					}
					
					try {
						long now1 = System.currentTimeMillis();
						result = pstat.executeQuery();
						logger.warn("耗时："+(System.currentTimeMillis()-now1)+"--------4");
						while (result.next()) {
							GameChargeRecord gamechongzhi = new GameChargeRecord();
							gamechongzhi.setUserName(result.getString("userName"));
							gamechongzhi.setTime(new Date(result.getTimestamp("time").getTime()));
							gamechongzhi.setReasonType(result.getString("reasonType"));
							gamechongzhi.setQuDao(result.getString("quDao"));
							gamechongzhi.setMoney(result.getLong("money"));
							gamechongzhi.setJiXing(result.getString("jiXing"));
							gamechongzhi.setGameLevel(result.getString("gameLevel"));
							gamechongzhi.setGame(result.getString("game"));
							gamechongzhi.setFenQu(result.getString("fenQu"));
							gamechongzhi.setCurrencyType(result.getString("currencyType"));
							gamechongzhi.setAction(result.getInt("action"));
							records.add(gamechongzhi);
						}
						logger.warn("[recordssize:"+records.size()+"] [耗时："+(System.currentTimeMillis()-now1)+"]--------5");
					} catch (SQLException e) {
						logger.warn("[通过货币类型得到充值流水记录] [执行SQL，获得结果] [异常] ",e);
						e.printStackTrace();
					}finally{
						try {
							result.close();
							pstat.close();
						} catch (SQLException e) {
							logger.warn("[关闭stat] [异常] ",e);
							e.printStackTrace();
						}
					}
				}
		}
		
		if(records.size()>0){
			logger.info("[通过货币类型获取数据] [成功] [数据长度："+records.size()+"] [耗时：]"+(System.currentTimeMillis()-now)+"-ms");
			return records;
		}else {
			return null;
		}
		
	}
	
	//分页查询,数据太大，只支持通过条件查询的分页,默认显示1页200条
	public List<GameChargeRecord> getRecordsByPage(String type){
		long now = System.currentTimeMillis();
		List<GameChargeRecord> records = new ArrayList<GameChargeRecord>();
		String sql = "select * from STAT_GAMECHONGZHI  where rownum <= 200 order by id desc";
		if("QQ".equals(type.trim())){
			PreparedStatement pstat_qq = null;
			ResultSet result_qq =null;
			if(conn_qq==null){
				conn_qq = getConnection("QQ");
			}
			
			try {
				pstat_qq = conn_qq.prepareStatement(sql);
			} catch (SQLException e) {
				logger.warn("[默认获得充值流水记录QQ] [创建statement] [异常] ",e);
				e.printStackTrace();
			}
			try {
				result_qq = pstat_qq.executeQuery();
				while (result_qq.next()) {
					GameChargeRecord gamechongzhi = new GameChargeRecord();
					gamechongzhi.setUserName(result_qq.getString("userName"));
					gamechongzhi.setTime(new java.util.Date(result_qq.getTimestamp("time").getTime()));
					gamechongzhi.setReasonType(result_qq.getString("reasonType"));
					gamechongzhi.setQuDao(result_qq.getString("quDao"));
					gamechongzhi.setMoney(result_qq.getLong("money"));
					gamechongzhi.setJiXing(result_qq.getString("jiXing"));
					gamechongzhi.setGameLevel(result_qq.getString("gameLevel"));
					gamechongzhi.setGame(result_qq.getString("game"));
					gamechongzhi.setFenQu(result_qq.getString("fenQu"));
					gamechongzhi.setCurrencyType(result_qq.getString("currencyType"));
					gamechongzhi.setAction(result_qq.getInt("action"));
					records.add(gamechongzhi);
				}
			} catch (SQLException e) {
				logger.warn("[默认获得充值流水记录QQ] [执行SQL，获得结果] [异常] ",e);
				e.printStackTrace();
			} finally{
				try {
					result_qq.close();
					pstat_qq.close();
					
				} catch (SQLException e) {
					logger.warn("[关闭statQQ] [异常] ",e);
					e.printStackTrace();
				}
				
			}
		}else{
			PreparedStatement pstat = null;
			ResultSet result =null;
			if(conn==null){
				conn = getConnection("");
			}
			try {
				pstat = conn.prepareStatement(sql);
			} catch (SQLException e) {
				logger.warn("[默认获得到充值流水记录] [创建statement] [异常] ",e);
				e.printStackTrace();
			}
			try {
				result = pstat.executeQuery();
				while (result.next()) {
					GameChargeRecord gamechongzhi = new GameChargeRecord();
					gamechongzhi.setUserName(result.getString("userName"));
					gamechongzhi.setTime(new java.util.Date(result.getTimestamp("time").getTime()));
					gamechongzhi.setReasonType(result.getString("reasonType"));
					gamechongzhi.setQuDao(result.getString("quDao"));
					gamechongzhi.setMoney(result.getLong("money"));
					gamechongzhi.setJiXing(result.getString("jiXing"));
					gamechongzhi.setGameLevel(result.getString("gameLevel"));
					gamechongzhi.setGame(result.getString("game"));
					gamechongzhi.setFenQu(result.getString("fenQu"));
					gamechongzhi.setCurrencyType(result.getString("currencyType"));
					gamechongzhi.setAction(result.getInt("action"));
					records.add(gamechongzhi);
				}
			} catch (SQLException e) {
				logger.warn("[默认得到充值流水记录] [执行SQL，获得结果] [异常] ",e);
				e.printStackTrace();
			} finally{
				try {
					result.close();
					pstat.close();
					
				} catch (SQLException e) {
					logger.warn("[关闭stat] [异常] ",e);
					e.printStackTrace();
				}
				
			}
		}
		if(records.size()>0){
			logger.info("[分页获取数据] [成功] [数据长度："+records.size()+"] [耗时："+(System.currentTimeMillis()-now)+"-ms]");
			return records;
		}else {
			return null;
		}
	}

  /**
	*   查询表规则,返回所查表名,返回消息信息
	*   eg:
	*   
	*   STAT_GAMECHONGZHI_0901
	*	STAT_GAMECHONGZHI_0905
	*	
	*	STAT_GAMECHONGZHI_QQ_0901
	*	STAT_GAMECHONGZHI_QQ_0905
	* 
	*/
	
	public String getTableName(String queryTime,String type){
		String nowtimeString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS").format(new Date());
		String tablename = ""; 
		String configtime = "";
		Calendar cl = Calendar.getInstance();
		Calendar cl2 = Calendar.getInstance();
		Calendar cl3 = Calendar.getInstance();
		long starttime = 0;
		long endtime = 0;
		long now = 0;
		try {
			if(configs.size()>0){
				for(Stat_TableConfig config:configs){
					if(config!=null){
						if(type.equals(config.getDateType())){
							//2012-12-12#2013-12-12
							configtime = config.getTimeLimits();
							String [] times = configtime.split("#");
							cl2.clear();
							cl.clear();
							cl3.clear();
							String ymd [] = times[0].split("-");
							String ymd2 [] = times[1].split("-");
							String ymd3 [] = queryTime.split("-");
							cl.set(Integer.parseInt(ymd[0]), Integer.parseInt(ymd[1]), Integer.parseInt(ymd[2]));
							cl2.set(Integer.parseInt(ymd2[0]), Integer.parseInt(ymd2[1]), Integer.parseInt(ymd2[2]));
							cl3.set(Integer.parseInt(ymd3[0]), Integer.parseInt(ymd3[1]), Integer.parseInt(ymd3[2]));
							starttime = cl.getTimeInMillis();
							endtime = cl2.getTimeInMillis();
							now = cl3.getTimeInMillis();
							if(now >= starttime && now <= endtime){
								tablename = config.getTableName();
								logger.info("[充值流水记录] [获取表名] [成功] [表名："+tablename+"]  [请求时间："+queryTime+"] [请求时系统当前时间："+nowtimeString+"]");
							}
						}
						
					}
				}
				
			}
			
		} catch (Exception e) {
			logger.info("[充值流水记录] [获取表名] [异常] [表名："+tablename+"] [请求时间："+queryTime+"] [请求时系统当前时间："+nowtimeString+"]");
			return "";
		}
		return tablename;
	}
	
	
	
	//-----------用于页面修改--------
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl_qq() {
		return url_qq;
	}

	public void setUrl_qq(String url_qq) {
		this.url_qq = url_qq;
	}

	public String getUser_qq() {
		return user_qq;
	}

	public void setUser_qq(String user_qq) {
		this.user_qq = user_qq;
	}

	public String getPassword_qq() {
		return password_qq;
	}

	public void setPassword_qq(String password_qq) {
		this.password_qq = password_qq;
	}
	
}
