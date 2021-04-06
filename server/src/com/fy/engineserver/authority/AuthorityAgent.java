package com.fy.engineserver.authority;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.datasource.language.MultiLanguageTranslateManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.configuration.Configuration;
import com.xuanzhi.tools.configuration.DefaultConfigurationBuilder;

/**
 * 玩家的权力记录媒介，记录了玩家所有某种行为的权利，规定了默认次数和当前次数，以及最大次数
 * 
 * 
 */
public class AuthorityAgent {

	public static Logger logger = LoggerFactory.getLogger(AuthorityAgent.class);

	private String filename;

	private Map<Integer, AuthorityConfig> configMap;

	protected static AuthorityAgent self;

	public static AuthorityAgent getInstance() {
		return self;
	}

	public void init() {
		
		long now = System.currentTimeMillis();
		try {
			loadFromXml(filename);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("权利配置初始化失败", e);
		}
		self = this;
		ServiceStartRecord.startLog(this);
	}

	private void loadFromXml(String filename) throws Exception {
		configMap = new HashMap<Integer, AuthorityConfig>();
		Configuration rootConfig = new DefaultConfigurationBuilder().buildFromFile(filename);
		Configuration authConfigs[] = rootConfig.getChildren("authority");
		for (Configuration conf : authConfigs) {
			int type = conf.getAttributeAsInteger("type", 0);
			int defaultNum = conf.getAttributeAsInteger("defaultNum", 0);
			String maxNumStr[] = conf.getAttribute("maxNum", "").split(",");
			if(maxNumStr.length < 11) {
				throw new RuntimeException("用户权利组初始化失败，最大次数(maxNum)配置的数量<11，权利组:"+type);
			}
			int maxNum[] = new int[maxNumStr.length];
			for(int i=0; i<maxNum.length; i++) {
				maxNum[i] = Integer.valueOf(maxNumStr[i]);
			}
			int refreshType = conf.getAttributeAsInteger("refreshType", 0);
			String vipAddNumStr[] = conf.getAttribute("vipAddNum", "").split(",");
			if(maxNumStr.length < 11) {
				throw new RuntimeException("用户权利组初始化失败，vip增加次数(vipAddNum)配置的数量<11，权利组:"+type);
			}
			int vipAddNum[] = new int[vipAddNumStr.length];
			for (int i = 0; i < vipAddNumStr.length; i++) {
				vipAddNum[i] = Integer.valueOf(vipAddNumStr[i]);
			}
			boolean accumulate = conf.getAttributeAsBoolean("accumulate", false);
			int accumulateMax = conf.getAttributeAsInteger("accumulateMax", 100000000);
			String desp = MultiLanguageTranslateManager.languageTranslate(conf.getAttribute("description", ""));
			AuthorityConfig config = new AuthorityConfig();
			config.defaultNum = defaultNum;
			config.maxNum = maxNum;
			config.refreshType = refreshType;
			config.type = type;
			config.desp = desp;
			config.vipAddNum = vipAddNum;
			config.accumulate = accumulate;
			config.accumulateMax = accumulateMax;
			configMap.put(type, config);
		}
		if(logger.isInfoEnabled())
			logger.info("[读取权利配置] [len:{}", new Object[] { configMap.size() });
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Map<Integer, AuthorityConfig> getConfigMap() {
		return configMap;
	}

	public void setConfigMap(Map<Integer, AuthorityConfig> configMap) {
		this.configMap = configMap;
	}

	/**
	 * 根据类型获得权利配置
	 * 
	 * @param type
	 * @return
	 */
	public AuthorityConfig getAuthorityConfig(int type) {
		return configMap.get(type);
	}
	
	/**
	 * 得到此权利组本周期内的总次数，这个总次数定义为: 已经行使的+未行使的
	 * @param player
	 * @param type
	 * @return
	 */
	public int getAuthorityPeriodTotal(Player player, int type)  {
		Authority auth = getAuthority(player, type);
		if(auth != null) {
			synchronized(auth) {
				return auth.getTotalNum();
			}
		}
		return 0;
	}
	
	/**
	 * 得到玩家周期内最大使用次数
	 * @param player
	 * @param type
	 * @return
	 */
	public int getAuthorityMaxNum(Player player, int type)  {
		Authority auth = getAuthority(player, type);
		if(auth != null) {
			synchronized(auth) {
				return auth.getConfig().maxNum[player.getVipLevel()];
			}
		}
		return 0;
	}
	
	/**
	 * 得到某权利组本周期内已经行使的次数
	 * @param player
	 * @param type
	 * @return
	 */
	public int getAuthorityPeriodAlreadyUsed(Player player, int type) {
		Authority auth = getAuthority(player, type);
		if(auth != null) {
			synchronized(auth) {
				return auth.getUsedNum();
			}
		}
		return 0;
	}
	
	/**
	 * 玩家是否可以行使某权利
	 * @param player
	 * @param type
	 * @return
	 * @throws TotalNumAccessedException 没有剩余未行使次数
	 * @throws MaxNumAccessedException	已达使用次数上限
	 */
	public boolean playerCanUse(Player player, int type)  throws TotalNumAccessedException,MaxNumAccessedException {
		Authority auth = getAuthority(player, type);
		if(auth != null) {
			synchronized(auth) { 
				return auth.canUse();
			}
		}
		return false;
	}
	
	/**
	 * 通知Agent玩家行使了一次某权利
	 * @param player
	 * @param type
	 */
	public void notifyPlayerUsed(Player player, int type) {
		Authority auth = getAuthority(player, type);
		if(auth != null) {
			synchronized(auth) {
				auth.addUsed(player);
				player.notifyAuthorityChanged();
			}
		}
	}
	
	/**
	 * 给玩家的某个权利增加次数，下次刷新就没有了，适合使用道具或付费直接增加的情况
	 * @param type
	 * @param player
	 * 
	 * @return 是否增加成功，如果已经到最大次数限制，不能再增加次数了，那么返回false
	 */
	public boolean addAuthorityNum(int type, Player player) {
		Authority auth = getAuthority(player, type);
		if(auth != null) {
			synchronized(auth) {
				if(auth != null && auth.getTotalNum() <  auth.config.maxNum[player.getVipLevel()]) {
					auth.otherAdd++;
					player.notifyAuthorityChanged();
					if(logger.isInfoEnabled())
						logger.info("[给玩家某权利增加一次次数] ["+player.getLogString()+"] [auth:"+auth.config.desp+"] [nowNum:"+auth.getTotalNum()+"]");
					return true;
				} else {
					if(logger.isInfoEnabled())
						logger.info("[给玩家某权利增加一次次数失败:已经达上限] ["+player.getLogString()+"] [auth:"+auth.config.desp+"] [nowNum:"+auth.getTotalNum()+"] [max:"+auth.config.maxNum+"]");
				}
				return false;
			}
		}
		if(logger.isInfoEnabled())
			logger.info("[给玩家某权利增加一次次数失败:找不到权利组] ["+player.getLogString()+"] [auth:"+type+"]");
		return false;
	}

	/**
	 * 拿到某玩家某个权利
	 * 
	 * @param player
	 * @param type
	 * @return
	 */
	public Authority getAuthority(Player player, int type) {
		Authority auth = player.getAuthorityMap().get(type);
		if (auth == null) {
			auth = createAuthority(player, type);
		} else {
			if(needRefresh(auth, player)) {
				refreshPlayerAuthority(auth, player);
			}
		}
		return auth;
	}
	
//	/**
//	 * 通知用户vip属性变化
//	 * @param player
//	 */
//	public void notifyPlayerVipChanged(Player player) {
//		Map<Integer, Authority> amap = player.getAuthorityMap();
//		Authority as[] = amap.values().toArray(new Authority[0]);
//		for(Authority a : as) {
//			if(a.config.vipAddNum != null && player.getVipLevel() > 0 && a.config.vipAddNum.length >= player.getVipLevel()) {
//				a.vipAdd = a.config.vipAddNum[player.getVipLevel()-1];
//				if(logger.isInfoEnabled())
//					logger.info("[玩家VIP发生变化] [更新权利] ["+player.getLogString()+"] ["+a.config.desp+"] [vipAdd:"+a.vipAdd+"]");
//			}
//		}
//	}
	
	/**
	 * 给玩家创建一个新的权利
	 * @param player
	 * @param type
	 */
	public Authority createAuthority(Player player, int type) {
		AuthorityConfig config = getAuthorityConfig(type);
		if (config != null) {
			Authority auth = new Authority();
			auth.setType(type);
			auth.setConfig(config);
			auth.setOwner(player);
			Calendar cal = Calendar.getInstance();
			if(config.refreshType == AuthorityConfig.每小时刷新) {
				auth.lastRefreshItem = cal.get(Calendar.HOUR_OF_DAY);
			} else if(config.refreshType == AuthorityConfig.每日刷新) {
				auth.lastRefreshItem = cal.get(Calendar.DAY_OF_YEAR);
			} else if(config.refreshType == AuthorityConfig.每周刷新) {
				auth.lastRefreshItem = cal.get(Calendar.WEEK_OF_YEAR);
			} else if(config.refreshType == AuthorityConfig.每月刷新) {
				auth.lastRefreshItem = cal.get(Calendar.MONTH);
			}
			auth.usedNum = 0;
			player.getAuthorityMap().put(type, auth);
			player.notifyAuthorityChanged();
			return auth;
		} else {
			logger.error("[创建权利时找不到对应类型的配置] [type:" + type + "] ["+player.getLogString()+"]");
		}
		return null;
	}
	
	/**
	 * 某个玩家的权利是否需要刷新
	 * @param player
	 * @param siFangType
	 * @return
	 */
	public boolean needRefresh(Authority authority, Player player) {
		if(authority.config != null) {
			Calendar cal = Calendar.getInstance();
			if(authority.config.refreshType == AuthorityConfig.每小时刷新) {
				int hour = cal.get(Calendar.HOUR_OF_DAY);
				int day = cal.get(Calendar.DAY_OF_YEAR);
				if(day != authority.lastRefreshDay || hour != authority.lastRefreshItem) {
					return true;
				}
			} else if(authority.config.refreshType == AuthorityConfig.每日刷新) {
				int day = cal.get(Calendar.DAY_OF_YEAR);
				if(day != authority.lastRefreshItem) {
					return true;
				}
			} else if(authority.config.refreshType == AuthorityConfig.每周刷新) {
				int week = cal.get(Calendar.WEEK_OF_YEAR);
				if(week != authority.lastRefreshItem) {
					return true;
				}
			} else if(authority.config.refreshType == AuthorityConfig.每月刷新) {
				int month = cal.get(Calendar.MONTH);
				if(month != authority.lastRefreshItem) {
					return true;
				}
			}
		} else {
			if(logger.isWarnEnabled())
				logger.warn("[玩家某个权利判断需要刷新时失败:config不存在] ["+player.getLogString()+"] [type:"+authority.type+"]");
		}
		return false;
	}

	/**
	 * 刷新玩家的某个权利
	 * @param player
	 * @param siFangType
	 */
	public void refreshPlayerAuthority(Authority authority, Player player) {
		if(authority.config != null) {
			int vipAddNum = authority.getVipAddNum();
			Calendar cal = Calendar.getInstance();
			if(authority.config.refreshType == AuthorityConfig.每小时刷新) {
				int hour = cal.get(Calendar.HOUR_OF_DAY);
				int day = cal.get(Calendar.DAY_OF_YEAR);
				if(day != authority.lastRefreshDay || hour != authority.lastRefreshItem) {
					if(authority.getConfig().accumulate) {
						int shengyu = authority.getTotalNum()-authority.getUsedNum();
						int dayNum = day-authority.lastRefreshDay;
						if(dayNum < 0) {
							dayNum = 365-authority.lastRefreshDay+day;
						}
						int period = dayNum*24+(hour-authority.lastRefreshItem);
						if(period > 1) {
							//如果隔开了一个周期，那么累计周期内的数量
							//这个地方有一个不太准确的计算，如果周期最后VIP发生变化，将导致累计值的多计，目前认为是合法的
							shengyu += (authority.getConfig().defaultNum+vipAddNum) * (period-1);
						}
						if(shengyu > authority.getConfig().accumulateMax) {
							shengyu = authority.getConfig().accumulateMax;
						}
						authority.accumulateNum = shengyu;
					} else {
						authority.accumulateNum = 0;
					}
					authority.otherAdd = 0;
					authority.usedNum = 0;
					if(authority.config.vipAddNum.length > 0 && player.getVipLevel() > 0 && authority.config.vipAddNum.length >= player.getVipLevel()) { 
						vipAddNum = authority.config.vipAddNum[player.getVipLevel()-1];
					}
					authority.lastRefreshItem = hour;
					authority.lastRefreshDay = day;
				}
			} else if(authority.config.refreshType == AuthorityConfig.每日刷新) {
				int day = cal.get(Calendar.DAY_OF_YEAR);
				if(day != authority.lastRefreshItem) {
					if(authority.getConfig().accumulate) {
						int shengyu = authority.getTotalNum()-authority.getUsedNum();
						int period = day-authority.lastRefreshItem;
						if(period < 0) {
							period = 365-authority.lastRefreshItem+day;
						}
						if(period > 1) {
							//如果隔开了一个周期，那么累计周期内的数量
							//这个地方有一个不太准确的计算，如果周期最后VIP发生变化，将导致累计值的多计，目前认为是合法的
							shengyu += (authority.getConfig().defaultNum+vipAddNum) * (period-1);
						}
						if(shengyu > authority.getConfig().accumulateMax) {
							shengyu = authority.getConfig().accumulateMax;
						}
						authority.accumulateNum = shengyu;
					} else {
						authority.accumulateNum = 0;
					}
					authority.otherAdd = 0;
					authority.usedNum = 0;
					if(authority.config.vipAddNum.length > 0 && player.getVipLevel() > 0 && authority.config.vipAddNum.length >= player.getVipLevel()) { 
						vipAddNum = authority.config.vipAddNum[player.getVipLevel()-1];
					}
					authority.lastRefreshItem = day;
					authority.lastRefreshDay = day;
				}
			} else if(authority.config.refreshType == AuthorityConfig.每周刷新) {
				int week = cal.get(Calendar.WEEK_OF_YEAR);
				if(week != authority.lastRefreshItem) {
					if(authority.getConfig().accumulate) {
						int shengyu = authority.getTotalNum()-authority.getUsedNum();
						int period = week - authority.lastRefreshItem;
						if(period < 0) {
							period = 52 - authority.lastRefreshItem + week;
						}
						if(period > 1) {
							//如果隔开了一个周期，那么累计周期内的数量
							//这个地方有一个不太准确的计算，如果周期最后VIP发生变化，将导致累计值的多计，目前认为是合法的
							shengyu += (authority.getConfig().defaultNum+vipAddNum) * (period-1);
						}
						if(shengyu > authority.getConfig().accumulateMax) {
							shengyu = authority.getConfig().accumulateMax;
						}
						authority.accumulateNum = shengyu;
					} else {
						authority.accumulateNum = 0;
					}
					authority.otherAdd = 0;
					authority.usedNum = 0;
					if(authority.config.vipAddNum.length > 0 && player.getVipLevel() > 0 && authority.config.vipAddNum.length >= player.getVipLevel()) { 
						vipAddNum = authority.config.vipAddNum[player.getVipLevel()-1];
					}
					authority.lastRefreshItem = week;
					authority.lastRefreshDay = cal.get(Calendar.DAY_OF_YEAR);
				}
			} else if(authority.config.refreshType == AuthorityConfig.每月刷新) {
				int month = cal.get(Calendar.MONTH);
				if(month != authority.lastRefreshItem) {
					if(authority.getConfig().accumulate) {
						int shengyu = authority.getTotalNum()-authority.getUsedNum();
						int period = month - authority.lastRefreshItem;
						if(period < 0) {
							period = 12 - authority.lastRefreshItem + month;
						}
						if(period > 1) {
							//如果隔开了一个周期，那么累计周期内的数量
							//这个地方有一个不太准确的计算，如果周期最后VIP发生变化，将导致累计值的多计，目前认为是合法的
							shengyu += (authority.getConfig().defaultNum+vipAddNum) * (period-1);
						}
						if(shengyu > authority.getConfig().accumulateMax) {
							shengyu = authority.getConfig().accumulateMax;
						}
						authority.accumulateNum = shengyu;
					} else {
						authority.accumulateNum = 0;
					}
					authority.otherAdd = 0;
					authority.usedNum = 0;
					if(authority.config.vipAddNum.length > 0 && player.getVipLevel() > 0 && authority.config.vipAddNum.length >= player.getVipLevel()) { 
						vipAddNum = authority.config.vipAddNum[player.getVipLevel()-1];
					}
					authority.lastRefreshItem = month;
					authority.lastRefreshDay = cal.get(Calendar.DAY_OF_YEAR);
				}
			}
			player.notifyAuthorityChanged();
		} else {
			if(logger.isWarnEnabled())
				logger.warn("[刷新玩家某个权利时失败:config不存在] ["+player.getLogString()+"] [type:"+authority.type+"]");
		}
	}
}
