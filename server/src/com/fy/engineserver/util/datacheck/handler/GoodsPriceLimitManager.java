package com.fy.engineserver.util.datacheck.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.shop.Goods;
import com.fy.engineserver.shop.Shop;
import com.fy.engineserver.shop.ShopManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.config.ConfigServiceManager;
import com.fy.engineserver.util.datacheck.MailEventManger;
import com.fy.engineserver.util.datacheck.event.MailEvent;

public class GoodsPriceLimitManager {
	private static GoodsPriceLimitManager instance;

	public static GoodsPriceLimitManager getInstance() {
		return instance;
	}

	private String filePath;
	public List<GoodsPriceLimit> goodsList = new ArrayList<GoodsPriceLimit>();

	public static final int 道具名_所在列 = 0;
	public static final int 颜色_所在列 = 1;
	public static final int 价格下限_所在列 = 3;
	public static final int 出售数量预警_所在列 = 4;

	public void loadFile() throws Exception {
		File file = new File(getFilePath());
		file = new File(ConfigServiceManager.getInstance().getFilePath(file));
		if (!file.exists()) {
			throw new Exception("配置文件不存在");
		}
		try {
			HSSFWorkbook hssfWorkbook = null;
			InputStream is = new FileInputStream(file);
			POIFSFileSystem pss = new POIFSFileSystem(is);
			hssfWorkbook = new HSSFWorkbook(pss);
			HSSFSheet sheet = null;
			sheet = hssfWorkbook.getSheetAt(0);
			if (sheet == null) return;
			int rows = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					HSSFCell cell = row.getCell(道具名_所在列);
					String articleCNName = cell.getStringCellValue();
					cell = row.getCell(颜色_所在列);
					int color = (int) cell.getNumericCellValue();
					cell = row.getCell(价格下限_所在列);
					int limitPrice = (int) cell.getNumericCellValue();
					cell = row.getCell(出售数量预警_所在列);
					int limitNum = (int) cell.getNumericCellValue();
					GoodsPriceLimit gpl = new GoodsPriceLimit(articleCNName, color, limitPrice, limitNum);
					goodsList.add(gpl);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public void init() throws Exception {
		
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		instance=this;
		loadFile();
		checkLimitPrice();
		ServiceStartRecord.startLog(this);
	}

	public GoodsPriceLimit getGoodPriceLimit(String articleCNName, int color) {
		for (GoodsPriceLimit gpl : goodsList) {
			if (gpl.getArticleCNName().equals(articleCNName) && gpl.getColor() == color) {
				return gpl;
			}
		}
		return null;
	}

	ShopManager sm = ShopManager.getInstance();
	LinkedHashMap<String, Shop> shops = sm.getShops();

	public void checkLimitPrice() {
		List<String[]> sendMailList=new ArrayList<String[]>();
		for (Iterator<String> itor = shops.keySet().iterator(); itor.hasNext();) {
			String name = itor.next();
			Shop shop = shops.get(name);
			if (shop == null) {
				continue;
			}
			List<Goods> list = shop.getGoods();
			for (Goods g : list) {
				// 判断数量不超过上限
				if ((Math.ceil(Integer.MAX_VALUE / g.getPrice())) < g.getGoodMaxNumLimit()) {
					sendMailList.add(new String[] {shop.getName_stat(), g.getArticleName_stat(), g.getColor() + "", "单次可购买数量超过上限,会导致越界", "单价"+(g.getPrice())+"上限(" + Math.ceil(Integer.MAX_VALUE / g.getPrice()) + ")可购(" + g.getGoodMaxNumLimit() + ")" });
				}
				// 判断价格是否高于最低折扣价
				GoodsPriceLimit gpl = getGoodPriceLimit(g.getArticleName_stat(), g.getColor());
				if (gpl != null) {
					if ((g.getPrice() / g.getBundleNum()) < gpl.getLimitPrice()) {
						sendMailList.add(new String[] { shop.getName_stat(),g.getArticleName_stat(), g.getColor() + "", "价格低于最低折扣价", "最低价(" + gpl.getLimitPrice() + ")现价(" + g.getPrice() / g.getBundleNum() + ")" });
					}
				}
			}
		}
		if(sendMailList.size()>0){
			String[][] sendMailArr=new String[sendMailList.size()][5];
			for(int i=0;i<sendMailList.size();i++){
				sendMailArr[i]=sendMailList.get(i);
			}
			MailEventManger.getInstance().addTask(MailEvent.商店检查.立即发送(sendMailArr));
		}
	}

	public List<GoodsPriceLimit> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<GoodsPriceLimit> goodsList) {
		this.goodsList = goodsList;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
