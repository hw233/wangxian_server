package com.fy.engineserver.newBillboard;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.message.GET_BILLBOARD_MENUS_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.newBillboard.date.country.CountryPowerBillboard;
import com.fy.engineserver.newBillboard.monitorLog.NewServerBillboard;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.config.ConfigServiceManager;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

public class BillboardsManager {

	// 九游 6月28日12点 官方6月29日12点 当乐 7月3日10点 7月4日10点
	public static Logger logger = LoggerFactory.getLogger(BillboardsManager.class.getName());

	public static String[] 千层塔道 = new String[] { Translate.黄泉, Translate.天玄, Translate.阴浊, Translate.阳清, Translate.洪荒, Translate.混沌 };

	private static String[] appallservers = { "国内本地测试", "梦倾天下", "玉幡宝刹", "西方灵山", "飞瀑龙池", "问天灵台", "傲啸封仙", "再续前缘", "裂风峡谷", "左岸花海", "白露横江", "雪域冰城", "兰若古刹", "万象更新", "霹雳霞光", "永安仙城", "右道长亭", "权倾皇朝", "幻灵仙境", "独步天下", "独霸一方", "对酒当歌", "诸神梦境", "仙子乱尘", "春风得意", "九霄龙吟", "飞龙在天", "天下无双", "一统江湖" };

	public static boolean 是真当月 = true;

	public static interface JiazuSimpleInfo {
		public long getJiazuID();

		public String getName();

		public byte getCountry();

		public int getProsperity();

		public long getUpdateProsperityTime();
	}

	public static String baseFile = "com.fy.engineserver.newBillboard.date.";
	public static long flushTime = 30 * 60 * 1000;
	public static int 显示条数 = 50;
	public static int 实际条数 = 200;

	public static String ALLDATEKEY = "allDate";
	public static String THKEY = "tableHead";

	private String fileName;

	private String diskFile;
	DiskCache disk = null;

	private String diskFile1;
	public DiskCache billboardDisk = null; // <日期时间,List<NewServerBillboard>>,<"allDate",List<日期时间集合>>,<"tableHead",List<表头描述集合>>

	public List<Billboard> list = new ArrayList<Billboard>();
	List<Billboard> updatePerMonthlist = new ArrayList<Billboard>();
	public List<BillboardMenu> menuList = new ArrayList<BillboardMenu>();
	public List<BillboardMenu> noappmenuList = new ArrayList<BillboardMenu>();
	private static BillboardsManager self;

	public static BillboardsManager getInstance() {
		return self;
	}

	public Timer timer = new Timer();

	public void init() throws Exception {
		
		long begin = SystemTime.currentTimeMillis();
		self = this;
		File file = new File(diskFile);
		disk = new DefaultDiskCache(file, null, "billboards", 100L * 365 * 24 * 3600 * 1000L);
		File file1 = new File(diskFile1);
		billboardDisk = new DefaultDiskCache(file1, null, "新服排行榜", 100L * 365 * 24 * 3600 * 1000L);
		if (billboardDisk.get(ALLDATEKEY) == null) {
			billboardDisk.put(ALLDATEKEY, new ArrayList<String>());
		}
		if (billboardDisk.get(THKEY) == null) {
			List<String> tableHeads = new ArrayList<String>();
			tableHeads.add("日期");
			tableHeads.add("类型");
			tableHeads.add("详细类型");
			tableHeads.add("角色名/宗派名");
			tableHeads.add("账号");
			tableHeads.add("id");
			billboardDisk.put(THKEY, (Serializable) tableHeads);
		}
		this.loadExcel();
		this.createBillboard();

		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				flushAllBillBoard();
			}
		};
		// 寻找下一个整点/半点 时间
		long delay = 0L;

		Calendar calendar = Calendar.getInstance();
		long start = calendar.getTimeInMillis();
		int minute = calendar.get(Calendar.MINUTE);// 当前分钟
		if (minute < 30) {
			calendar.set(Calendar.MINUTE, 30);
			calendar.set(Calendar.SECOND, 00);
			calendar.set(Calendar.MILLISECOND, 00);
		} else {
			calendar.add(Calendar.HOUR, 1);
			calendar.set(Calendar.MINUTE, 00);
			calendar.set(Calendar.SECOND, 00);
			calendar.set(Calendar.MILLISECOND, 00);
		}
		delay = calendar.getTimeInMillis() - start;
		timer.schedule(timerTask, delay, 刷新间隔);

		logger.error("[排行榜初始化完成] [" + (SystemTime.currentTimeMillis() - begin) + "ms] [下次更新排行榜时间:" + TimeTool.formatter.varChar19.format(calendar.getTimeInMillis()) + "]");
		ServiceStartRecord.startLog(this);
	}

	public void getBillboradMenus(Player player) {

		if (isAppServer()) {
			GET_BILLBOARD_MENUS_RES res = new GET_BILLBOARD_MENUS_RES(GameMessageFactory.nextSequnceNum(), menuList.toArray(new BillboardMenu[0]));
			player.addMessageToRightBag(res);
		} else {
			GET_BILLBOARD_MENUS_RES res = new GET_BILLBOARD_MENUS_RES(GameMessageFactory.nextSequnceNum(), noappmenuList.toArray(new BillboardMenu[0]));
			player.addMessageToRightBag(res);
		}

		if (logger.isWarnEnabled()) {
			logger.warn("[查看排行榜菜单成功] [" + player.getLogString() + "]");
		}
		// Billboard bb = list.get(0);
		// bb.getBillboard(player);
	}

	public JiazuSimpleInfo getJiazuSimpleInfo(long jiazuId) {

		try {
			List<JiazuSimpleInfo> list = JiazuManager.jiazuEm.queryFields(JiazuSimpleInfo.class, new long[] { jiazuId });
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		} catch (Exception e) {
			logger.warn("[查看排行榜菜单异常] [取家族信息错误]", e);
		}
		return null;
	}

	public Billboard getBillboard(String menu, String subMenu) {

		for (Billboard b : list) {
			if (b.getMenu().equals(menu) && b.getSubMenu().equals(subMenu)) {
				return b;
			}
		}
		return null;
	}

	/**
	 * 改名涉及的排行榜中角色名更新
	 * @param menu
	 * @param subMenu
	 * @param playerName
	 * @param index
	 *            BillboardDate.dateValuesStrings[]这个数组的下标,因为不同的排行榜中玩家角色名可能存到这个数组的不同位置
	 */
	public void notifyBillboardChangeName(String menu, String subMenu, String oldName, Player player, int index) {
		Billboard b = getBillboard(menu, subMenu);
		if (b != null) {
			BillboardDate[] data = b.getDates();
			if (data != null) {
				for (BillboardDate bd : data) {
					String[] dateValuesStrings = bd.getDateValues();
					if (dateValuesStrings != null && dateValuesStrings[index] != null && dateValuesStrings[index].equals(oldName)) {
						dateValuesStrings[index] = player.getName();
						GamePlayerManager.logger.warn(player.getLogString()+"[玩家改名更新排行榜] ["+menu+"] ["+subMenu+"] [oldName:"+oldName+"]");
					}
				}
			}
		}
	}

	public static String menu = Translate.等级;
	public static String subMenu = Translate.全部;

	public static int getPlayerLevel(String levelDesc){
		if(levelDesc != null && levelDesc.contains(Translate.仙)){
			String levelArr [] = levelDesc.split(Translate.仙);
			return 220+ Integer.parseInt(levelArr[1]);
		}
		return Integer.parseInt(levelDesc);
	}
	
	// 等级 全部
	public int getMaxLevelByBillBoard() {
		try {
			Billboard bb = getBillboard(menu, subMenu);
			if (bb != null) {
				BillboardDate[] datas = bb.getDates();

				if (datas == null || datas.length == 0) {
					bb.update();
				}
				datas = bb.getDates();
				if (datas != null) {
					String[] str = datas[0].getDateValues();
					if (str != null) {
						int level = getPlayerLevel(str[4]);
						if (level < 70) {
							level = 70;
						}

						if (level > ExperienceManager.getInstance().maxLevel) {
							level = ExperienceManager.getInstance().maxLevel;
						}
						return level;
					}
				}
			}
		} catch (Exception e) {
			BillboardsManager.logger.error("[取排行榜的最高级别异常] [" + menu + "] [" + subMenu + "]", e);
		}
		return 200;
	}

	public void createBillboard() throws Exception {

		if (menuList != null && menuList.size() > 0) {
			for (int i = 0; i < menuList.size(); i++) {
				BillboardMenu bbm = menuList.get(i);
				List<Billboard> tempList = bbm.createBillboard();
				if (tempList != null && tempList.size() > 0) {
					list.addAll(tempList);
				} else {
					logger.error("[生成排行榜错误] [" + (bbm != null ? bbm.getMenuName() : "") + "] [" + i + "]");
					throw new RuntimeException("[生成排行榜错误] [" + (bbm != null ? bbm.getMenuName() : "") + "] [" + i + "]");
				}
			}
			logger.error("[生成排行榜的成功]");
		} else {
			logger.error("[没有生成排行榜的数据]");
		}
	}

	public void loadExcel() throws Exception {

		InputStream is = new FileInputStream(ConfigServiceManager.getInstance().getFilePath(new File(fileName)));
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);

		HSSFSheet sheet = workbook.getSheetAt(0);

		// 一级菜单 一级菜单最大 二级菜单 二级菜单最大 表头 表头最大字数 描述
		int 一级菜单 = 0;
		int 一级菜单最大 = 1;

		int 二级菜单 = 2;
		int 二级菜单最大 = 3;
		int 表头 = 4;
		int 表头最大字数 = 5;
		int 描述 = 6;
		int className = 7;
		int dayOrMonth = 8;

		if (sheet != null) {

			int rows = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					BillboardMenu bbmenu = null;
					HSSFCell cell = row.getCell(一级菜单);
					String menuName = "";
					try {
						menuName = (cell.getStringCellValue().trim());
					} catch (Exception ex) {
						throw ex;
					}

					if (menuList != null && menuList.size() != 0) {
						for (BillboardMenu bb : menuList) {
							if (bb.getMenuName().equals(menuName)) {
								bbmenu = bb;
								break;
							}
						}
					}
					if (bbmenu == null) {
						bbmenu = new BillboardMenu();
						menuList.add(bbmenu);
						bbmenu.setMenuName(menuName);
						cell = row.getCell(一级菜单最大);
						int menuMaxNum = 0;
						try {
							menuMaxNum = (int) cell.getNumericCellValue();
						} catch (Exception ex) {
							menuMaxNum = Integer.parseInt(cell.getStringCellValue());
							// throw ex;
						}
						bbmenu.setMenuMaxNum(menuMaxNum);
					}

					cell = row.getCell(二级菜单);
					String subMenuName = "";
					try {
						subMenuName = (cell.getStringCellValue().trim());
					} catch (Exception ex) {
						throw ex;
					}
					if (bbmenu.getSubMenusList() == null) {
						bbmenu.setSubMenusList(new ArrayList<String>());
					}
					bbmenu.getSubMenusList().add(subMenuName);

					cell = row.getCell(二级菜单最大);
					int submenuMaxNum = 0;
					try {
						submenuMaxNum = (int) cell.getNumericCellValue();
					} catch (Exception ex) {
						submenuMaxNum = Integer.parseInt(cell.getStringCellValue());
					}
					if (bbmenu.getSubMenuMaxNum() < submenuMaxNum) {
						bbmenu.setSubMenuMaxNum(submenuMaxNum);
					}
					cell = row.getCell(表头);
					String title = "";
					try {
						title = (cell.getStringCellValue().trim());
					} catch (Exception ex) {
						throw ex;
					}
					if (bbmenu.getTitles() == null) {
						bbmenu.setTitles(new ArrayList<String>());
					}
					bbmenu.getTitles().add(title);

					cell = row.getCell(表头最大字数);
					String titleNums = "";
					try {
						titleNums = cell.getStringCellValue().trim();
					} catch (Exception ex) {
						throw ex;
					}
					if (bbmenu.getTitleNums() == null) {
						bbmenu.setTitleNums(new ArrayList<String>());
					}
					bbmenu.getTitleNums().add(titleNums);

					cell = row.getCell(描述);
					String description = "";
					try {
						description = (cell.getStringCellValue().trim());
					} catch (Exception ex) {
						throw ex;
					}
					if (bbmenu.getDescriptions() == null) {
						bbmenu.setDescriptions(new ArrayList<String>());
					}
					bbmenu.getDescriptions().add(description);

					cell = row.getCell(className);
					String classNames = "";
					try {
						classNames = cell.getStringCellValue().trim();
					} catch (Exception ex) {
						throw ex;
					}
					if (bbmenu.getClassNames() == null) {
						bbmenu.setClassNames(new ArrayList<String>());
					}

					bbmenu.getClassNames().add(classNames);

					cell = row.getCell(dayOrMonth);
					boolean dayOrMonth_ = false;
					try {
						dayOrMonth_ = cell.getBooleanCellValue();
					} catch (Exception ex) {
						throw ex;
					}
					if (bbmenu.getDayOrMonths() == null) {
						bbmenu.setDayOrMonths(new ArrayList<Boolean>());
					}

					bbmenu.getDayOrMonths().add(dayOrMonth_);

				}
			}
			/** APP充值排行榜 **/
			if (!isAppServer()) {
				for (BillboardMenu menu : menuList) {
					if (!menu.getMenuName().equals(Translate.充值)) {
						noappmenuList.add(menu);
					}
				}
			}
			logger.error("[初始化排行榜数据完成]");
		} else {
			throw new IllegalArgumentException("排行榜fileName错误" + fileName);
		}
	}

	public boolean isAppServer() {
		boolean isopen = false;
		GameConstants gc = GameConstants.getInstance();
		String servername = gc.getServerName();
		for (String name : appallservers) {
			if (name.equals(servername)) {
				isopen = true;
				break;
			}
		}
		return isopen;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<Billboard> getList() {
		return list;
	}

	public void setList(List<Billboard> list) {
		this.list = list;
	}

	public List<BillboardMenu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<BillboardMenu> menuList) {
		this.menuList = menuList;
	}

	public void setDiskFile(String diskFile) {
		this.diskFile = diskFile;
	}

	public DiskCache getDisk() {
		return disk;
	}

	public void setDisk(DiskCache disk) {
		this.disk = disk;
	}

	public String getDiskFile1() {
		return diskFile1;
	}

	public void setDiskFile1(String diskFile1) {
		this.diskFile1 = diskFile1;
	}

	public DiskCache getBillboardDisk() {
		return billboardDisk;
	}

	public void setBillboardDisk(DiskCache billboardDisk) {
		this.billboardDisk = billboardDisk;
	}

	public List<Billboard> getUpdatePerMonthlist() {
		return updatePerMonthlist;
	}

	public void setUpdatePerMonthlist(List<Billboard> updatePerMonthlist) {
		this.updatePerMonthlist = updatePerMonthlist;
	}

	class PerMonthUpdateThread implements Runnable {

		boolean canUpdate = true;

		@Override
		public void run() {

			while (true) {

				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				try {
					Calendar calendar = Calendar.getInstance();

					if (calendar.get(calendar.DAY_OF_MONTH) == 1) {
						if (calendar.get(Calendar.HOUR_OF_DAY) == 0 && canUpdate) {
							canUpdate = false;

							for (Billboard bb : updatePerMonthlist) {

								bb.updatePerMonth();

							}
						} else {
							canUpdate = true;
						}
					}
				} catch (Throwable t) {
					BillboardsManager.logger.error("[每月更新榜单错误] []", t);
				}

			}
		}
	}

	// 国家实力榜 (没用)
	public void updateCountryWar(int country, int score) {
		try {
			Billboard bb = this.getBillboard("国战", "国家实力榜");
			if (bb != null) {
				if (bb instanceof CountryPowerBillboard) {
					CountryPowerBillboard cbb = (CountryPowerBillboard) bb;
					cbb.updateScore(country, score);
				} else {
					BillboardsManager.logger.error("[国家实力排行榜错误] [" + bb.getClass() + "]");
				}
			} else {
				BillboardsManager.logger.error("[国家实力排行榜错误] [没有找到指定的榜单] [国战] [国家实力榜]");
			}
		} catch (Exception e) {
			BillboardsManager.logger.error("[国家实力排行榜异常] [没有找到指定的榜单] [国战] [国家实力榜]", e);
		}
	}

	public int 得到当日时间天() {

		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_YEAR);
		return day;
	}
	
	public int 得到当日时间周() {

		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.WEEK_OF_YEAR);
		return day;
	}

	public int 得到当月时间月() {
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		return month;

	}

	public static long 刷新间隔 = 30 * 60 * 1000;
	// 上次刷新排行榜信息时间
	public long lastFlushBillboardTime;

	public static boolean running = true;

	// @Override
	// public void run() {
	//
	// while(running){
	// try {
	// Thread.sleep(1000);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// long now = SystemTime.currentTimeMillis();
	// if(now - lastFlushBillboardTime >= 刷新间隔){
	// lastFlushBillboardTime = now;
	// flushAllBillBoard();
	// }
	// }
	//
	// }

	// 定时刷新所有排行榜
	public void flushAllBillBoard() {
		try {
			long allBegin = SystemTime.currentTimeMillis();
			for (Billboard board : list) {
				try {
					board.setLastUpdateTime(SystemTime.currentTimeMillis());
					long begin = SystemTime.currentTimeMillis();
					board.update();
					long end = SystemTime.currentTimeMillis();
					logger.error("[到期刷新指定排行榜信息] [" + board.getMenu() + "] [" + board.getSubMenu() + "] [cost:" + (end - begin) + "]");
				} catch (Exception e) {
					logger.error("[到期刷新指定排行榜信息异常] [" + board.getMenu() + "] [" + board.getSubMenu() + "]", e);
				}
			}
			long allEnd = SystemTime.currentTimeMillis();
			logger.error("[到期刷新排行榜信息完成] [cost:" + (allEnd - allBegin) + "]");
		} catch (Exception e) {
			logger.error("[到期刷新排行榜信息异常]", e);
		}
	}

	public String parseTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(System.currentTimeMillis());
	}

	public void addToDisCatch(List<NewServerBillboard> nsBillboards) {
		String date = parseTime();
		if (billboardDisk.get(date) != null) {
			List<NewServerBillboard> oldBillboards = (List<NewServerBillboard>) billboardDisk.get(date);
			oldBillboards.addAll(nsBillboards);
			billboardDisk.put(date, (Serializable) oldBillboards);
		} else {
			billboardDisk.put(date, (Serializable) nsBillboards);
			List<String> allDateKey = (List<String>) billboardDisk.get(ALLDATEKEY);
			allDateKey.add(date);
			billboardDisk.put(ALLDATEKEY, (Serializable) allDateKey);
		}
	}
}
