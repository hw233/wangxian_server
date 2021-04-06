package com.fy.engineserver.shop;



import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.InlayArticle;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.props.BrightInlayProps;
import com.fy.engineserver.datasource.article.data.props.Props;
import com.fy.engineserver.datasource.article.data.props.WingProps;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.config.ConfigServiceManager;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

public class ShopManager{
	
	//商品的各种出售限制，包括时间限制和数量限制
	//这些限制可以随意组合
	
	/**
	 * 没有时间限制
	 */
	public static final int TIME_LIMIT_NONE = 0;
	
	
	/**
	 * 没有数量限制
	 */
	public static final int NUM_LIMIT_NONE = 0;
	
	/**
	 * 每天定点时段限制
	 * 输入格式：   HH:mm ~ HH:mm
	 */
	public static final int LIMIT_EVERYDAY_HOURS =  1;
	
	/**
	 * 每周定点时段限制，其中天的取值为   1， 2，3，4，5，6 ,7表示星期一到星期日
	 * 输入格式：   d:HH ~ d:HH
	 */
	public static final int LIMIT_EVERYWEEK_DAYS =  2;
	
	public static final String WEEK_DAY_NAMES[] = new String[]{Translate.text_5669,Translate.text_5670,Translate.text_5671,Translate.text_5672,Translate.text_5673,Translate.text_5674,Translate.text_5675};
	
 	/**
	 * 每月定点时段限制
	 * 输入格式：   dd ~ dd
	 */
	public static final int LIMIT_EVERYMONTH_DAYS =  3;
	
	/**
	 *固定时段限制
	 * 输入格式：   dd:HH ~ dd:HH
	 */
	public static final int LIMIT_FIX_TIME =  4;
	
	
	/**
	 * 总售出数量限制
	 * 输入格式：   nnn
	 */
	public static final int LIMIT_TOTAL_SELL_NUM =  5;
	
	/**
	 * 每人可买数量限制
	 * 输入格式：   nnn
	 */
	public static final int LIMIT_SELL_NUM_BY_PLAYER =  6;

	public static final int 商店所在sheet = 0;
	
	public static final int 商店_id_所在列 = 0;
	public static final int 商店_name_所在列 = 1;
	public static final int 商店_name_stat_所在列 = 2;
	public static final int 商店_version_所在列 = 3;
	public static final int 商店_shopType_所在列 = 4;
	public static final int 商店_isBuyBinded_所在列 = 5;
	public static final int 商店_是否开启 = 6;
	public static final int 商店_开启时间段 = 7;
	public static final int 商店_articleName_所在列 = 8;
	public static final int 商店_articleName_stat_所在列 = 9;
	public static final int 商店_article_id_所在列 = 10;
	public static final int 商店_color_所在列 = 11;
	public static final int 商店_bundleNum_所在列 = 12;
	public static final int 商店_payType_所在列 = 13;
	public static final int 商店_oldPrice_所在列 = 14;
	public static final int 商店_price_所在列 = 15;
	public static final int 商店_exchangeArticleNames_所在列 = 16;
	public static final int 商店_exchangeArticleNums_所在列 = 17;
	public static final int 商店_littleSellIcon_所在列 = 18;
	public static final int 商店_zhiwuLimit_所在列 = 19;
	public static final int 商店_gongxunLimit_所在列 = 20;
	public static final int 商店_jiazuGongxianLimit_所在列 = 21;
	public static final int 商店_totalSellNumLimit_所在列 = 22;
	public static final int 商店_personSellNumLimit_所在列 = 23;
	public static final int 商店_在时间限制下的数量限制_所在列 = 24;
	public static final int 商店_timeLimitType_所在列 = 25;
	public static final int 商店_everyDayBeginLimit_所在列 = 26;
	public static final int 商店_everyDayEndLimit_所在列 = 27;
	public static final int 商店_everyWeekBeginLimit_所在列 = 28;
	public static final int 商店_everyWeekEndLimit_所在列 = 29;
	public static final int 商店_everyMonthBeginLimit_所在列 = 30;
	public static final int 商店_everyMonthEndLimit_所在列 = 31;
	public static final int 商店_fixTimeBeginLimit_所在列 = 32;
	public static final int 商店_fixTimeEndLimit_所在列 = 33;
	public static final int 商店_reputeLimit_所在列 = 34;
	public static final int 商店_商品是否绑定_所在列 = 35;
	public static final int 商店_商品购买上限_所在列 = 36;
	public static final int 商店_商品出现概率_所在列 = 37;
	public static final int 商店_每天定时刷新时间_所在列 = 38;
	public static final int 商店_个人战勋限制_所在列 = 39;
	
	public static byte 元宝商城类型 = 1;
	
	public static byte 商店_绑银商店 = 0;
	public static byte 商店_银子商店 = 1;
	public static byte 商店_工资商店 = 2;
	public static byte 商店_资源商店 = 3;
	public static byte 商店_挂机商店 = 4;
	public static byte 商店_历练商店 = 5;
	public static byte 商店_功勋商店 = 6;
	public static byte 商店_文采商店 = 7;
	public static byte 商店_兑换 = 8;
	public static byte 商店_战勋 = 13;
	public static byte 跨服商店 = 16;
	
	public static String[] 可以刷新物品的商店 = new String[]{};
	public static String[] 刷新商店物品消耗物品统计名 = new String[]{};
	public static int[] 刷新商店物品消耗物品数量 = new int[]{};
	/**
	 * 策划配置
	 */
	public static String[] 元宝商城左边标签显示的商店的名字 = new String[]{Translate.全部道具};
	public static String[] 元宝商城左边标签显示的商店的图标 = new String[]{"chengbaoshi"};
	public static String[] 元宝商城右边隐藏的商店的名字 = new String[]{Translate.我想更快升级,Translate.我想增强实力,Translate.我想传情达意};
	public static String[] 元宝商城右边隐藏的商店的名字包含vip = new String[]{Translate.我想更快升级,Translate.我想增强实力,Translate.我想传情达意,Translate.VIP商店};
	
	public static String[] 随身商店名字 = new String[]{Translate._1级随身商店,Translate._20级随身商店,Translate._40级随身商店,Translate._60级随身商店,Translate._80级随身商店,Translate._100级随身商店,Translate._120级随身商店,Translate._140级随身商店,Translate._160级随身商店,Translate._180级随身商店,Translate._200级随身商店, Translate._220级随身商店, Translate._240级随身商店, Translate._260级随身商店, Translate._280级随身商店, Translate._300级随身商店};
	public static int[][] 随身商店级别 = new int[][]{{0,19},{20,39},{40,59},{60,79},{80,99},{100,119},{120,139},{140,159},{160,179},{180,199},{200,220},{220,240},{240,260},{260,280},{280,300}};
	public static String[] VIP商店名字 = new String[]{Translate.VIP1商店,Translate.VIP2商店,Translate.VIP3商店,Translate.VIP4商店,Translate.VIP5商店,Translate.VIP6商店,Translate.VIP7商店,Translate.VIP8商店,Translate.VIP9商店,Translate.VIP10商店,Translate.VIP11商店,Translate.VIP12商店,Translate.VIP13商店,Translate.VIP14商店,Translate.VIP15商店
		,Translate.VIP15商店,Translate.VIP15商店,Translate.VIP15商店,Translate.VIP15商店,Translate.VIP15商店,Translate.VIP15商店,Translate.VIP15商店,Translate.VIP15商店,Translate.VIP15商店,Translate.VIP15商店,Translate.VIP15商店};
	
	public static int 新商城默认显示的商店 = 2;
	public static String[] 新商城拥有的商店名字 = {Translate.限时抢购, Translate.积分抢购,Translate.全部道具,Translate.积分商城,Translate.活跃商城};
	public static String[] 新商城拥有的商店名字_VIP = {Translate.限时抢购, Translate.积分抢购,Translate.全部道具,Translate.积分商城,Translate.活跃商城 ,Translate.VIP商店};
	
	public static String 得到随身商店名字(int level){
		for(int i = 0; i < 随身商店级别.length; i++){
			if(随身商店级别[i][0] <= level && 随身商店级别[i][1] >= level){
				return 随身商店名字[i];
			}
		}
		return null;
	}
	
	public static String 得到VIP商店名字(int vipLevel){
		if(vipLevel >= 1 && vipLevel <= VIP商店名字.length){
			return VIP商店名字[vipLevel - 1];
		}
		return null;
	}

	public static String 畅销icon = "changxiao";
	
	//	protected static Logger logger = Logger.getLogger(ShopManager.class.getName());
	public	static Logger logger = LoggerFactory.getLogger(ShopManager.class.getName());
	
	protected static ShopManager self = null;

	public static ShopManager getInstance() {
		return self;
	}
	
	protected File configFile;
	
	protected String cacheFile;
	
	public String getCacheFile() {
		return cacheFile;
	}
	public void setCacheFile(String cacheFile) {
		this.cacheFile = cacheFile;
	}

	protected static LinkedHashMap<String,Shop> shops = new LinkedHashMap<String,Shop>();
	
	protected HashMap<Integer,Shop> idShopMaps = new HashMap<Integer,Shop>();
	
	//
	//HashMap<String, SelledGoodsData> map = new HashMap<String, SelledGoodsData>();
	DefaultDiskCache cache = null;
	
	public boolean containsSelledGoodsData(int shopId,int goodsId,String articleName){
		return false;
	}
	public synchronized SelledGoodsData getSelledGoodsDataAndCreateIfNotExists(int shopId,int goodsId,String articleName){
		String key = ""+shopId+"-"+goodsId+"-"+articleName;
		SelledGoodsData sgd = (SelledGoodsData)cache.get(key);
		if(sgd == null){
			sgd = new SelledGoodsData();
			sgd.setSelledGoodsDataKey(key);
			cache.put(key, sgd);
		}
		
		return sgd;
	}
	
	
	public synchronized void saveSelledGoodsData(int shopId,int goodsId,String articleName,SelledGoodsData data){
		String key = ""+shopId+"-"+goodsId+"-"+articleName;
		cache.put(key, data);
	}
	
	protected ArticleEntityManager articleEntityManager;

	
	public ArticleEntityManager getArticleEntityManager() {
		return articleEntityManager;
	}

	public void setArticleEntityManager(ArticleEntityManager articleEntityManager) {
		this.articleEntityManager = articleEntityManager;
	}

	public LinkedHashMap<String, Shop> getShops() {
		return shops;
	}
	
	public File getConfigFile() {
		return configFile;
	}

	public void setConfigFile(File configFile) {
		this.configFile = configFile;
	}

	public void init()throws Exception{
		
		
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		cache = new DefaultDiskCache(new File(cacheFile), "shopManagerCache", 365L*24*3600000);
		configFile = new File(ConfigServiceManager.getInstance().getFilePath(configFile));
		TransitRobberyManager.logger.warn("[商店表路径] [ " + configFile.getAbsolutePath() + "]");
		loadFrom(configFile);
		if(cacheFile == null){
			throw new NullPointerException();
		}

		self = this;
		
		ServiceStartRecord.startLog(this);
	}
	
	public Shop getShop(String name, Player player){
		if(Translate.VIP商店.equals(name) && player != null){
			name = 得到VIP商店名字(player.getVipLevel());
		}
		return shops.get(name);
	}

	public void destroy(){
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		cache.destory();
		System.out.println("[Desctroy] 调用destroy方法保存所有商店中商品购买刷新信息， cost "+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now)+" ms");
		
	}

	
	/**
	 * 把goods中的数据赋值到g里
	 */
	void transferGoodsToSelledGoodsData(SelledGoodsData g, Goods goods){
		g.setSelledGoodsCount(goods.totalSellNum);
		g.setSelledLastFlushTime(goods.totalSellNumLastFlushTime);
	}
	
	/**
	 * 把g中的数据赋值到goods里
	 */
	void transferSelledGoodsDataToGoods(Goods goods, SelledGoodsData g){
		goods.totalSellNum = g.getSelledGoodsCount();
		goods.totalSellNumLastFlushTime = g.getSelledLastFlushTime();
	}

	public void loadFrom(File file) throws Exception{
		InputStream is = new FileInputStream(file);
		loadFromInputStream(is);
		is.close();
	}
	public static void main(String[] args) throws Exception {
		ShopManager sm = new ShopManager();
		sm.loadFrom(new File("E://javacode2//hg1-clone//game_mieshi_server//conf//game_init_config//shops.xls"));
	}
	
	public void loadFromInputStream(InputStream is) throws Exception{
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = workbook.getSheetAt(商店所在sheet);
		int rowNum = sheet.getPhysicalNumberOfRows();
		LinkedHashMap<String, Shop> map = new LinkedHashMap<String, Shop>();
		HashMap<Integer, Shop> idMap = new HashMap<Integer, Shop>();
		Shop shop = null;
		for(int i = 1; i < rowNum; i++){
			HSSFRow row = sheet.getRow(i); 
			HSSFCell hc = row.getCell(商店_id_所在列);
			try{
				int id = (int)hc.getNumericCellValue();
				logger.warn("[服务器启动] [总行："+rowNum+"] [行："+i+"] [id:"+id+"]");
				shop = idMap.get(id);
				if(shop == null){
					shop = new Shop();
					
					shop.setId(id);
					idMap.put(id, shop);
					HSSFCell nameHc = row.getCell(商店_name_所在列);
					try{
						String name = (nameHc.getStringCellValue().trim());
						shop.setName(name);
						map.put(name, shop);
					}catch(Exception e){
						throw e;
					}
					HSSFCell nameHc_stat = row.getCell(商店_name_stat_所在列);
					try{
						String name_stat = (nameHc_stat.getStringCellValue().trim());
						shop.name_stat = name_stat;
					}catch(Exception e){
						throw e;
					}
					HSSFCell versionHc = row.getCell(商店_version_所在列);
					try{
						int version = (int)versionHc.getNumericCellValue();
						shop.setVersion(version);
					}catch(Exception e){
						try{
							int version = Integer.parseInt(versionHc.getStringCellValue().trim());
							shop.setVersion(version);
						}catch(Exception ee){
							throw e;
						}
					}
					HSSFCell shopTypeHc = row.getCell(商店_shopType_所在列);
					try{
						byte shopType = (byte)shopTypeHc.getNumericCellValue();
						shop.shopType = shopType;
					}catch(Exception e){
						try{
							byte shopType = Byte.parseByte(shopTypeHc.getStringCellValue().trim());
							shop.shopType = shopType;
						}catch(Exception ee){
							throw e;
						}
					}
					HSSFCell isBuyBindedHc = row.getCell(商店_isBuyBinded_所在列);
					try{
						boolean isBuyBinded = isBuyBindedHc.getBooleanCellValue();
						shop.setBuyBinded(isBuyBinded);
					}catch(Exception e){
						throw e;
					}
					HSSFCell isOpenShopHc = row.getCell(商店_是否开启);
					try{
						boolean isOpenShop = isOpenShopHc.getBooleanCellValue();
						shop.setOpenShop(isOpenShop);
					}catch(Exception e){
						throw e;
					}
					HSSFCell opentimelength = row.getCell(商店_开启时间段);
					try{
						String opentime = (opentimelength.getStringCellValue().trim());
						shop.setTimelimits(opentime);
					}catch(Exception e){
						throw e;
					}
//					HSSFCell hiddenHc = row.getCell(商店_hidden_所在列);
//					try{
//						boolean hidden = hiddenHc.getBooleanCellValue();
//						shop.setHidden(hidden);
//					}catch(Exception e){
//
//					}
					
				}
			}catch(Exception ex){
				try{
					
					int id = Integer.parseInt(hc.getStringCellValue().trim());
					shop = idMap.get(id);
					if(shop == null){
						shop = new Shop();
						idMap.put(id, shop);
						HSSFCell nameHc = row.getCell(商店_name_所在列);
						try{
							String name = nameHc.getStringCellValue().trim();
							shop.setName(name);
							map.put(name, shop);
						}catch(Exception e){
							throw e;
						}
						HSSFCell nameHc_stat = row.getCell(商店_name_stat_所在列);
						try{
							String name_stat = (nameHc_stat.getStringCellValue().trim());
							shop.name_stat = name_stat;
						}catch(Exception e){
							throw e;
						}
						HSSFCell versionHc = row.getCell(商店_version_所在列);
						try{
							int version = (int)versionHc.getNumericCellValue();
							shop.setVersion(version);
						}catch(Exception e){
							try{
								int version = Integer.parseInt(versionHc.getStringCellValue().trim());
								shop.setVersion(version);
							}catch(Exception ee){
								
							}
						}
						HSSFCell shopTypeHc = row.getCell(商店_shopType_所在列);
						try{
							byte shopType = (byte)shopTypeHc.getNumericCellValue();
							shop.shopType = shopType;
						}catch(Exception e){
							try{
								byte shopType = Byte.parseByte(shopTypeHc.getStringCellValue().trim());
								shop.shopType = shopType;
							}catch(Exception ee){
								
							}
						}
						HSSFCell isBuyBindedHc = row.getCell(商店_isBuyBinded_所在列);
						try{
							boolean isBuyBinded = isBuyBindedHc.getBooleanCellValue();
							shop.setBuyBinded(isBuyBinded);
						}catch(Exception e){

						}
						HSSFCell isOpenShopHc = row.getCell(商店_是否开启);
						try{
							boolean isOpenShop = isOpenShopHc.getBooleanCellValue();
							shop.setOpenShop(isOpenShop);
						}catch(Exception e){
							throw e;
						}
						HSSFCell opentimelength = row.getCell(商店_开启时间段);
						try{
							String opentime = (opentimelength.getStringCellValue().trim());
							shop.setTimelimits(opentime);
						}catch(Exception e){
							throw e;
						}
//						HSSFCell hiddenHc = row.getCell(商店_hidden_所在列);
//						try{
//							boolean hidden = hiddenHc.getBooleanCellValue();
//							shop.setHidden(hidden);
//						}catch(Exception e){
//
//						}
					}
				}catch(Exception exx){
					exx.printStackTrace();
				}
			}
			Goods goods = new Goods(shop);
			shop.goods.add(goods);
			
			HSSFCell nameHc2 = null;
			try{
				nameHc2 = row.getCell(商店_articleName_所在列);
				String articleName = (nameHc2.getStringCellValue().trim());
				goods.articleName = articleName;
			}catch(Exception e){
				System.out.println("row:"+(row==null)+"--rowNum:"+i+"--nameHc2"+nameHc2);
				e.printStackTrace();
				throw e;
			}
			
			HSSFCell nameHc_stat = row.getCell(商店_articleName_stat_所在列);
			try{
				String articleName_stat = (nameHc_stat.getStringCellValue().trim());
				goods.articleName_stat = articleName_stat;
			}catch(Exception e){
				throw e;
			}
			ShopManager.logger.warn("[服务器启动] [" + shop.name_stat + "] [" + goods.articleName_stat + "]");
			
			hc = row.getCell(商店_article_id_所在列);
			try{
				int article_id = (int)hc.getNumericCellValue();
				goods.id = article_id;
			}catch(Exception ex){
				int article_id = Integer.parseInt(hc.getStringCellValue().trim());
				goods.id = article_id;
			}
			
			hc = row.getCell(商店_color_所在列);
			try{
				int color = (int)hc.getNumericCellValue();
				goods.color = color;
			}catch(Exception ex){
				int color = Integer.parseInt(hc.getStringCellValue().trim());
				goods.color = color;
			}
			
			hc = row.getCell(商店_bundleNum_所在列);
			try{
				int bundleNum = (int)hc.getNumericCellValue();
				goods.bundleNum = bundleNum;
			}catch(Exception ex){
				int bundleNum = Integer.parseInt(hc.getStringCellValue().trim());
				goods.bundleNum = bundleNum;
			}
			
			hc = row.getCell(商店_payType_所在列);
			try{
				int payType = (int)hc.getNumericCellValue();
				goods.payType = payType;
			}catch(Exception ex){
				try{
					int payType = Integer.parseInt(hc.getStringCellValue().trim());
					goods.payType = payType;
				}catch(Exception e){
					throw e;
				}
			}
			
			hc = row.getCell(商店_oldPrice_所在列);
			try{
				int oldPrice = (int)hc.getNumericCellValue();
				goods.oldPrice = oldPrice;
			}catch(Exception ex){
				try{
					int oldPrice = Integer.parseInt(hc.getStringCellValue().trim());
					goods.oldPrice = oldPrice;
				}catch(Exception e){
					throw e;
				}
			}
			
			hc = row.getCell(商店_price_所在列);
			try{
				int price = (int)hc.getNumericCellValue();
				goods.price = price;
			}catch(Exception ex){
				try{
					int price = Integer.parseInt(hc.getStringCellValue().trim());
					goods.price = price;
				}catch(Exception e){
					throw e;
				}
			}
			
			hc = row.getCell(商店_exchangeArticleNames_所在列);
			try{
				if(!hc.getStringCellValue().trim().equals("")){
					String exchangeArticleNames = hc.getStringCellValue().trim();
					String[] names = exchangeArticleNames.split(";");
					if(names.length == 1){
						names = exchangeArticleNames.split("；");
					}
					goods.exchangeArticleNames = names;
				}
			}catch(Exception ex){

			}
			
			hc = row.getCell(商店_exchangeArticleNums_所在列);
			try{
				String exchangeArticleNums = hc.getStringCellValue().trim();
				String[] names = exchangeArticleNums.split(";");
				if(names.length == 1){
					names = exchangeArticleNums.split("；");
				}
				int[] articleNums = new int[names.length];
				for(int ii = 0; ii < names.length; ii++){
					articleNums[ii] = Integer.parseInt(names[ii]);
				}
				goods.exchangeArticleNums = articleNums;
			}catch(Exception ex){
				if(goods.exchangeArticleNames.length > 0){
					if(goods.exchangeArticleNames.length != 1){
						throw new Exception("交换物品的名字和个数不匹配");
					}
					int num = (int)hc.getNumericCellValue();
					int[] articleNums = new int[1];
					articleNums[0] = num;
					goods.exchangeArticleNums = articleNums;
				}
			}
			
			hc = row.getCell(商店_littleSellIcon_所在列);
			try{
				String littleSellIcon = hc.getStringCellValue().trim();
				goods.littleSellIcon = littleSellIcon;
			}catch(Exception ex){

			}
			
			hc = row.getCell(商店_zhiwuLimit_所在列);
			try{
				int zhiwuLimit = (int)hc.getNumericCellValue();
				goods.zhiwuLimit = zhiwuLimit;
			}catch(Exception ex){
				try{
					int zhiwuLimit = Integer.parseInt(hc.getStringCellValue().trim());
					goods.zhiwuLimit = zhiwuLimit;
				}catch(Exception e){

				}
			}
			
			hc = row.getCell(商店_gongxunLimit_所在列);
			try{
				int gongxunLimit = (int)hc.getNumericCellValue();
				goods.gongxunLimit = gongxunLimit;
			}catch(Exception ex){
				try{
					int gongxunLimit = Integer.parseInt(hc.getStringCellValue().trim());
					goods.gongxunLimit = gongxunLimit;
				}catch(Exception e){

				}
			}
			
			hc = row.getCell(商店_jiazuGongxianLimit_所在列);
			try{
				int jiazuGongxianLimit = (int)hc.getNumericCellValue();
				goods.jiazuGongxianLimit = jiazuGongxianLimit;
			}catch(Exception ex){
				try{
					int jiazuGongxianLimit = Integer.parseInt(hc.getStringCellValue().trim());
					goods.jiazuGongxianLimit = jiazuGongxianLimit;
				}catch(Exception e){

				}
			}
			
			hc = row.getCell(商店_jiazuGongxianLimit_所在列);
			try{
				int jiazuGongxianLimit = (int)hc.getNumericCellValue();
				goods.jiazuGongxianLimit = jiazuGongxianLimit;
			}catch(Exception ex){
				try{
					int jiazuGongxianLimit = Integer.parseInt(hc.getStringCellValue().trim());
					goods.jiazuGongxianLimit = jiazuGongxianLimit;
				}catch(Exception e){

				}
			}
			
			hc = row.getCell(商店_totalSellNumLimit_所在列);
			try{
				int totalSellNumLimit = (int)hc.getNumericCellValue();
				goods.totalSellNumLimit = totalSellNumLimit;
				if(totalSellNumLimit==0){
					goods.setOverNum(-1);
				}else{
					goods.setOverNum(totalSellNumLimit);
				}
			}catch(Exception ex){
				try{
					int totalSellNumLimit = Integer.parseInt(hc.getStringCellValue().trim());
					goods.totalSellNumLimit = totalSellNumLimit;
					if(totalSellNumLimit==0){
						goods.setOverNum(-1);
					}else{
						goods.setOverNum(totalSellNumLimit);
					}
				}catch(Exception e){
					goods.setOverNum(-1);
				}
			}
			hc = row.getCell(商店_personSellNumLimit_所在列);
			try{
				int personSellNumLimit = (int)hc.getNumericCellValue();
				goods.personSellNumLimit = personSellNumLimit;
			}catch(Exception ex){
				try{
					int personSellNumLimit = Integer.parseInt(hc.getStringCellValue().trim());
					goods.personSellNumLimit = personSellNumLimit;
				}catch(Exception e){

				}
			}
			
			hc = row.getCell(商店_在时间限制下的数量限制_所在列);
			try{
				boolean 在时间限制下的数量限制 = hc.getBooleanCellValue();
				goods.在时间限制下的数量限制 = 在时间限制下的数量限制;
			}catch(Exception ex){

			}
			
			hc = row.getCell(商店_timeLimitType_所在列);
			try{
				int timeLimitType = (int)hc.getNumericCellValue();
				goods.timeLimitType = timeLimitType;
				try{
					if(goods.timeLimitType == ShopManager.LIMIT_FIX_TIME && goods.totalSellNumLimit > 0){
						SelledGoodsData sgd = this.getSelledGoodsDataAndCreateIfNotExists(shop.getId(), goods.id, goods.getArticleName());
						long starttime = sgd.getConfigStartTime();
						long endtime = sgd.getConfigEndTime();
						long fixTimeEndLimit = goods.getFixTimeEndLimit();
						long fixTimeBeginLimit = goods.getFixTimeBeginLimit();
						long allsellnums = sgd.getSelledGoodsCount();
						boolean isChange = false;
						if((starttime/1000) != (fixTimeBeginLimit/1000) || (endtime/1000) != (fixTimeEndLimit/1000)){
							goods.setOverNum((int)(goods.totalSellNumLimit-allsellnums));
							isChange = true;
						}
						logger.warn("[设置商品的剩余数量] [成功] [isChange:"+isChange+"] [商店:"+(shop==null?"null":shop.getName_stat())+"] [商品:"+goods.getArticleName_stat()+"] [卖出："+allsellnums+"] [剩余数量:"+goods.getOverNum()+"]");
					}
				}catch(Exception ee){
					ee.printStackTrace();
					logger.warn("[设置商品的剩余数量] [异常] [商店:"+(shop==null?"null":shop.getName_stat())+"] [商品:"+(goods==null?"null":goods.getArticleName_stat())+"] ["+ee+"]");
				}
			}catch(Exception ex){
				try{
					int timeLimitType = Integer.parseInt(hc.getStringCellValue().trim());
					goods.timeLimitType = timeLimitType;
					try{
						if(goods.timeLimitType == ShopManager.LIMIT_FIX_TIME && goods.totalSellNumLimit > 0){
							SelledGoodsData sgd = this.getSelledGoodsDataAndCreateIfNotExists(shop.getId(), goods.id, goods.getArticleName());
							long starttime = sgd.getConfigStartTime();
							long endtime = sgd.getConfigEndTime();
							long fixTimeEndLimit = goods.getFixTimeEndLimit();
							long fixTimeBeginLimit = goods.getFixTimeBeginLimit();
							long allsellnums = sgd.getSelledGoodsCount();
							boolean isChange = false;
							if((starttime/1000) != (fixTimeBeginLimit/1000) || (endtime/1000) != (fixTimeEndLimit/1000)){
								goods.setOverNum((int)(goods.totalSellNumLimit-allsellnums));
								isChange = true;
							}
							logger.warn("[设置商品的剩余数量] [成功2] [isChange:"+isChange+"] [商店:"+(shop==null?"null":shop.getName_stat())+"] [商品:"+goods.getArticleName_stat()+"] [卖出："+allsellnums+"] [剩余数量:"+goods.getOverNum()+"]");
						}
					}catch(Exception ee){
						ee.printStackTrace();
						logger.warn("[设置商品的剩余数量] [异常2] [商店:"+(shop==null?"null":shop.getName_stat())+"] [商品:"+(goods==null?"null":goods.getArticleName_stat())+"] ["+ee+"]");
					}
				}catch(Exception e){

				}
			}
			
			hc = row.getCell(商店_everyDayBeginLimit_所在列);
			try{
				int everyDayBeginLimit = (int)hc.getNumericCellValue();
				goods.everyDayBeginLimit = everyDayBeginLimit;
			}catch(Exception ex){
				try{
					int everyDayBeginLimit = Integer.parseInt(hc.getStringCellValue().trim());
					goods.everyDayBeginLimit = everyDayBeginLimit;
				}catch(Exception e){

				}
			}
			
			hc = row.getCell(商店_everyDayEndLimit_所在列);
			try{
				int everyDayEndLimit = (int)hc.getNumericCellValue();
				goods.everyDayEndLimit = everyDayEndLimit;
			}catch(Exception ex){
				try{
					int everyDayEndLimit = Integer.parseInt(hc.getStringCellValue().trim());
					goods.everyDayEndLimit = everyDayEndLimit;
				}catch(Exception e){

				}
			}
			
			hc = row.getCell(商店_everyWeekBeginLimit_所在列);
			try{
				int everyWeekBeginLimit = (int)hc.getNumericCellValue();
				goods.everyWeekBeginLimit = everyWeekBeginLimit;
			}catch(Exception ex){
				try{
					int everyWeekBeginLimit = Integer.parseInt(hc.getStringCellValue().trim());
					goods.everyWeekBeginLimit = everyWeekBeginLimit;
				}catch(Exception e){

				}
			}
			
			hc = row.getCell(商店_everyWeekEndLimit_所在列);
			try{
				int everyWeekEndLimit = (int)hc.getNumericCellValue();
				goods.everyWeekEndLimit = everyWeekEndLimit;
			}catch(Exception ex){
				try{
					int everyWeekEndLimit = Integer.parseInt(hc.getStringCellValue().trim());
					goods.everyWeekEndLimit = everyWeekEndLimit;
				}catch(Exception e){

				}
			}
			
			hc = row.getCell(商店_everyMonthBeginLimit_所在列);
			try{
				int everyMonthBeginLimit = (int)hc.getNumericCellValue();
				goods.everyMonthBeginLimit = everyMonthBeginLimit;
			}catch(Exception ex){
				try{
					int everyMonthBeginLimit = Integer.parseInt(hc.getStringCellValue().trim());
					goods.everyMonthBeginLimit = everyMonthBeginLimit;
				}catch(Exception e){

				}
			}
			
			hc = row.getCell(商店_everyMonthEndLimit_所在列);
			try{
				int everyMonthEndLimit = (int)hc.getNumericCellValue();
				goods.everyMonthEndLimit = everyMonthEndLimit;
			}catch(Exception ex){
				try{
					int everyMonthEndLimit = Integer.parseInt(hc.getStringCellValue().trim());
					goods.everyMonthEndLimit = everyMonthEndLimit;
				}catch(Exception e){

				}
			}
			
			hc = row.getCell(商店_fixTimeBeginLimit_所在列);
			try{
				String startStr = hc.getStringCellValue().trim();
				String[] times = startStr.split("-");
				Calendar cal = Calendar.getInstance();
				cal.set(Integer.parseInt(times[0]), Integer.parseInt(times[1])-1, Integer.parseInt(times[2]), Integer.parseInt(times[3]), Integer.parseInt(times[4]),0);
				goods.fixTimeBeginLimit = cal.getTimeInMillis();
			}catch(Exception ex){

			}
			
			hc = row.getCell(商店_fixTimeEndLimit_所在列);
			try{
				String endStr = hc.getStringCellValue().trim();
				String[] times = endStr.split("-");
				Calendar cal = Calendar.getInstance();
				cal.set(Integer.parseInt(times[0]), Integer.parseInt(times[1])-1, Integer.parseInt(times[2]), Integer.parseInt(times[3]), Integer.parseInt(times[4]),0);
				goods.fixTimeEndLimit = cal.getTimeInMillis();
			}catch(Exception ex){

			}
			
			hc = row.getCell(商店_reputeLimit_所在列);
			try{
				boolean reputeLimit = hc.getBooleanCellValue();
				goods.reputeLimit = reputeLimit;
			}catch(Exception ex){

			}
			
			hc = row.getCell(商店_商品是否绑定_所在列);
			try{
				/**
				 * 商店绑定，里面的物品全是帮
				 * 商店不帮，帮不帮看里面的商品
				 */
				boolean isbind = hc.getBooleanCellValue();
				if(shop.isBuyBinded){
					goods.setBuyBind(true);
				}else{
					goods.setBuyBind(isbind);
				}
			}catch(Exception ex){
				if(shop.isBuyBinded){
					goods.setBuyBind(shop.isBuyBinded);
				}else{
					goods.setBuyBind(shop.isBuyBinded);
				}
			}
			
			hc = row.getCell(商店_商品购买上限_所在列);
			try{
				int goodMaxNumLimit = (int)hc.getNumericCellValue();
				goods.goodMaxNumLimit = goodMaxNumLimit;
			}catch(Exception ex){
				throw new Exception("商品购买上限没有配置，请配置，商店："+shop.getName()+" ("+shop.getName_stat()+")");
			}
			HSSFCell showNum = row.getCell(商店_商品出现概率_所在列);
			try {
				if(showNum != null) {
					goods.showProbabbly = (int) showNum.getNumericCellValue();
				}
			} catch(Exception e) {
				throw e;
			}
			HSSFCell refreashtimes = row.getCell(商店_每天定时刷新时间_所在列);
			try {
				if(refreashtimes != null ) {
					String times = refreashtimes.getStringCellValue().trim();
					if (times != null && !times.isEmpty()) {
						List<Integer[]> refreashTimesList = new ArrayList<Integer[]>();
						String[] tempT = times.split("\\|");
						for(int kk=0; kk<tempT.length; kk++) {
							String[] tempStr = tempT[kk].split(":");
							Integer[] tempTT = new Integer[2];
							tempTT[0] = Integer.parseInt(tempStr[0]);
							tempTT[1] = Integer.parseInt(tempStr[1]);
							refreashTimesList.add(tempTT);
						}
						shop.refreashTimesList = refreashTimesList;
					}
				}
			} catch(Exception e) {
				throw e;
			}
			try {
				HSSFCell jjcLevelLimit = row.getCell(商店_个人战勋限制_所在列);
				if(jjcLevelLimit != null) {
					goods.setJjcLevelLimit((int) jjcLevelLimit.getNumericCellValue());
				}
			} catch(Exception e) {
				throw e;
			}
			
			SelledGoodsData g = this.getSelledGoodsDataAndCreateIfNotExists(shop.id, goods.id, goods.getArticleName());
			transferSelledGoodsDataToGoods(goods, g);
			
		}
		shops = map;
		idShopMaps = idMap;
	}
	
	public static long hotSailFlushTime;
	/**
	 * 第一维为畅销物品的key(商店id物品id物品名字)，第二维为畅销物品的数量
	 */
	public static String[][] hotSailKeys = new String[][]{{"a","0"},{"a","0"},{"a","0"},{"a","0"},{"a","0"},{"a","0"},{"a","0"},{"a","0"},{"a","0"},{"a","0"}};
	
	public static void 畅销物品刷新(){
		String[] keys = shops.keySet().toArray(new String[0]);
		for(int i = 0 ; i < keys.length ; i++){
			Shop shop = shops.get(keys[i]);
			if(shop != null && shop.shopType == Shop.SHOP_TYPE_YUANBAO){
				Goods gs[] = shop.goods.toArray(new Goods[0]);
				for(int j = 0 ; j < gs.length ; j++){
					if(gs[j] != null){
						sort(shop, gs[j]);
					}
				}
			}
		}
		for(int i = 0; i < hotSailKeys.length; i++){
			String[] hotSailKey = hotSailKeys[i];
//			System.out.println("热卖商品："+hotSailKey[0]+" "+hotSailKey[1]);
			
		}
	}
	
	public static void sort(Shop shop,Goods goods){
		for(int i = 0; i < hotSailKeys.length; i++){
			String[] hotSailKey = hotSailKeys[i];
			if(hotSailKey == null){
				hotSailKey = new String[]{"a","0"};
				hotSailKeys[i] = hotSailKey;
			}
		}
		for(int i = 0; i < hotSailKeys.length; i++){
			String[] hotSailKey = hotSailKeys[i];
			if(hotSailKey[0].equals(shop.getId() + "-" + goods.getId() + "-" + goods.getArticleName())){
				hotSailKey[1] = goods.totalSellNum+"";
				return;
			}
		}
		Arrays.sort(hotSailKeys, new Comparator<String[]>() {
			@Override
			public int compare(String[] o1, String[] o2) {
				// TODO Auto-generated method stub
				return Integer.parseInt(o1[1]) - Integer.parseInt(o2[1]);
			}
		});
		for(int i = 0; i < hotSailKeys.length; i++){
			String[] hotSailKey = hotSailKeys[i];
			if(Integer.parseInt(hotSailKey[1]) < goods.totalSellNum){
				hotSailKey[0] = shop.getId() + "-" + goods.getId() + "-" + goods.getArticleName();
				hotSailKey[1] = goods.totalSellNum+"";
				break;
			}
		}
	}
	static Hashtable<String,ArticleEntity> 商店临时物品idMap = new Hashtable<String,ArticleEntity>();
	public static com.fy.engineserver.shop.client.Goods[] translate(Player player,Shop shop,Goods g[]){
		ArrayList<com.fy.engineserver.shop.client.Goods> al = new ArrayList<com.fy.engineserver.shop.client.Goods>();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		
		
		//热卖特殊处理，最快10秒刷新一次
		if(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - hotSailFlushTime > 10000){
			hotSailFlushTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			畅销物品刷新();
		}
		
		for(int i = 0 ; i < g.length ; i++){
			Article article = ArticleManager.getInstance().getArticle(g[i].getArticleName());
			if(article != null){
				String name = g[i].getArticleName()+"-"+g[i].getColor();
				ArticleEntity entity = 商店临时物品idMap.get(name);
				if(entity == null){
					try{
						entity = aem.createTempEntity(article, false,ArticleEntityManager.CREATE_REASON_SHOP_SELL,player,g[i].getColor());
						//商店创建的物品是废弃的，在维护的时候可以删除，添加
						entity.setAbandoned(true);
						商店临时物品idMap.put(name, entity);
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}

				if(entity != null){
					com.fy.engineserver.shop.client.Goods gg = new com.fy.engineserver.shop.client.Goods();
					gg.setArticleId(entity.getId());
					gg.setArticleName(g[i].getArticleName());
					gg.setId(g[i].getId());
					gg.setColor(g[i].getColorForClient());
					if(article instanceof WingProps || article instanceof BrightInlayProps){
						gg.setColor(ArticleManager.COLOR_PINK);
					}
					gg.setLevel(-1);
					gg.setBuyBind(shop.isBuyBinded());
					if(article instanceof Equipment){
						gg.setLevel(((Equipment)article).getPlayerLevelLimit());
						if(g[i].getColor() == ArticleManager.equipment_color_完美紫 || g[i].getColor() == ArticleManager.equipment_color_完美橙){
							gg.setArticleName(g[i].getArticleName()+"•"+Translate.完美);
						}
					}else if(article instanceof Props){
						gg.setLevel(((Props)article).getLevelLimit());
					}else if(article instanceof InlayArticle){
						gg.setArticleName(((InlayArticle)article).getShowName());
					}
					gg.setLittleSellIcon(g[i].getLittleSellIcon());
					gg.setBundleNum(g[i].bundleNum);
					gg.setExchangeArticleDescription(g[i].getExchangeArticleDescription());
					gg.setReputeLimit(g[i].isReputeLimit());
					gg.setPType(g[i].getPayType());
					gg.setPrice(g[i].getPrice());
					gg.setOldPrice(g[i].getOldPrice());
					gg.setDescription(g[i].getDescription(player));
					gg.setGoodEndTime(g[i].getFixTimeEndLimit());
					gg.setServerNumlimit(g[i].getTotalSellNumLimit());
					gg.setPlayerNumlimit(g[i].getPersonSellNumLimit());
					gg.setOverNum(g[i].getOverNum());
					gg.setJjcLevelLimit(g[i].getJjcLevelLimit());
					gg.setGoodMaxNumLimit(g[i].getGoodMaxNumLimit());
					if(g[i].getPrice() == g[i].getOldPrice() && hotSailKeys != null){
						for(String[] hotSailKey : hotSailKeys){
							if(hotSailKey != null && hotSailKey[0].equals(shop.getId()+"-"+g[i].getId()+"-"+g[i].getArticleName())){
								gg.setLittleSellIcon(畅销icon);
							}
						}
					}

					al.add(gg);
				}else{
//					logger.warn("[商店] [错误] ["+shop.getName()+"] ["+player.getName()+"] [不能创建临时的实例] ["+g[i].getArticleName()+"]");
					if(logger.isWarnEnabled())
						logger.warn("[商店] [错误] [{}] [{}] [不能创建临时的实例] [{}]", new Object[]{shop.getLogString(),player.getName(),g[i].getArticleName()});
				}
			}
			
		}
		return al.toArray(new com.fy.engineserver.shop.client.Goods[0]);
	}
	
	public final static Map<Integer,Integer> costJiFen = new HashMap<Integer,Integer>();
	{
		costJiFen.put(0, 200);
		costJiFen.put(1, 200);
		costJiFen.put(2, 400);
		costJiFen.put(3, 400);
		costJiFen.put(4, 800);
		costJiFen.put(5, 800);
		costJiFen.put(6, 1600);
		costJiFen.put(7, 1600);
		costJiFen.put(8, 3200);
		costJiFen.put(9, 3200);
	}
	

}
