package com.fy.engineserver.gm.custom;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuanzhi.tools.configuration.Configuration;
import com.xuanzhi.tools.configuration.DefaultConfiguration;
import com.xuanzhi.tools.configuration.DefaultConfigurationBuilder;
import com.xuanzhi.tools.configuration.DefaultConfigurationSerializer;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.FileUtils;

public class GmItemManager {
//	protected static Logger logger = Logger.getLogger(GmItemManager.class
public	static Logger logger = LoggerFactory.getLogger(GmItemManager.class
			.getName());
	protected String itemConFile;

	private List<PresentItem> items = new ArrayList<PresentItem>();
	private static GmItemManager self;

	public static GmItemManager getInstance() {
		return self;
	}

	public void initialize() {
		// ......
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		try {
			items = loadPage(itemConFile);
			self = this;
			System.out.println(this.getClass().getName()
					+ " initialize successfully ["
					+ (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) + "]");
			if(logger.isInfoEnabled())
				logger.info("{} initialize successfully [{}][saveFile:{}]", new Object[]{this.getClass().getName(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now),itemConFile});
		} catch (Exception e) {
if(logger.isInfoEnabled())
	logger.info("{} initialize fail [saveFile:{}]", new Object[]{this.getClass().getName(),itemConFile});
		}
	}

	public List<String> getItemNames() {
		List<String> itemnames = new ArrayList<String>();
		for (PresentItem pi : items) {
			itemnames.add(pi.getItemname());
		}
		return itemnames;
	}

	public void insertPresentItem(PresentItem pitem, int index) {
		try {// 测试是否是值改变了
			pitem.setItemname(pitem.getItemname().trim());
			items.add(index, pitem);
			savePage(items, itemConFile);
//logger.info("item: [" + pitem.getItemname() + "to " + itemConFile
//+ "] insert success at");
if(logger.isInfoEnabled())
	logger.info("item: [{}to {}] insert success at", new Object[]{pitem.getItemname(),itemConFile});
		} catch (Exception e) {
//logger.info("item: [" + pitem.getItemname() + "to " + itemConFile
//+ "] insert fail at" );
if(logger.isInfoEnabled())
	logger.info("item: [{}to {}] insert fail at" , new Object[]{pitem.getItemname(),itemConFile});
		}
	}

	public boolean sendPermit(String userid, String pname) {

		for (PresentItem it : items) {
			// 首先遍历各个礼品
			if (pname.equals(it.getItemname())) {
				long date = new Date().getTime();
				if (date > change(it.getEndDate())
						|| date < change(it.getStartDate())) {
					//如果有日期限制，则返回false；
					
if(logger.isInfoEnabled())
	logger.info("it contain someitem is before the begindate or after the end date");
					return false;
				}
				int count1 = Integer.parseInt(it.getCount());
				if(!it.getPlayids().contains(userid)){
					//如果不包含该用户则可以发送；直接添加
					PlayerItem p1 = new PlayerItem();
					p1.setPlayerid(userid);
					p1.setCount(1);
					it.getPis().add(p1);
					it.getPlayids().add(userid);
					savePage(items, itemConFile);
//			    	logger.info("the item gave to " + userid + "success");
			    	if(logger.isInfoEnabled())
				    	logger.info("the item gave to {}success", new Object[]{userid});
					return true;
				}
					
				if (it.getPlayids().contains(userid)) {
					//如果包含这个用户
					List<PlayerItem> pis = it.getPis();
					for (PlayerItem pi : pis) {
						if (userid.equals(pi.getPlayerid())) {
							int count2 = pi.getCount();
							if (count2 < count1) {
								pi.setCount(pi.getCount() + 1);
								savePage(items, itemConFile);
//						    	logger.info("the item gave to " + userid + "success");
						    	if(logger.isInfoEnabled())
							    	logger.info("the item gave to {}success", new Object[]{userid});
						    	return true;
							} else {
//logger.info("the item" + pname
//+ "gave to " + userid
//+ "fail result is larger to standard");
if(logger.isInfoEnabled())
	logger.info("the item{}gave to {}fail result is larger to standard", new Object[]{pname,userid});
								return false;

							}
						}
					}
				}
			}
		}
//		logger.info("the items gave to " + userid + "fail because is not contain "+pname);
		if(logger.isInfoEnabled())
			logger.info("the items gave to {}fail because is not contain {}", new Object[]{userid,pname});
		return false;
	}

	public long change(String date) {
		long l = DateUtil
				.parseDate((DateUtil.formatDate(new Date(), "yyyy-") + date),
						"yyyy-MM-dd").getTime();
		return l;
	}

	public void updatePresentItem(PresentItem pitem, int index) {
		try {
			items.set(index, pitem);
			savePage(items, itemConFile);
//logger.info("item :[" + pitem.getItemname() + "] at " + index
//+ " update success at " );
if(logger.isInfoEnabled())
	logger.info("item :[{}] at {} update success at " , new Object[]{pitem.getItemname(),index});
		} catch (Exception e) {
//logger.info("item :[" + pitem.getItemname() + "] at " + index
//+ " update fail at " );
if(logger.isInfoEnabled())
	logger.info("item :[{}] at {} update fail at " , new Object[]{pitem.getItemname(),index});
		}

	}

	public void deletePresentItem(int index) {
		try {
			items.remove(index);
			savePage(items, itemConFile);
//logger.info("index :[" + index + "] delete success at "
//);
if(logger.isInfoEnabled())
	logger.info("index :[{}] delete success at ", new Object[]{index});
		} catch (Exception e) {
//logger.info("index :[" + index + "] delete fail at "
//);
if(logger.isInfoEnabled())
	logger.info("index :[{}] delete fail at ", new Object[]{index});
		}
	}

	public List<PresentItem> loadPage(String xmlname) {
		List<PresentItem> items = new ArrayList<PresentItem>();
		try {
			Configuration rootConf = new DefaultConfigurationBuilder()
					.buildFromFile(xmlname);
			Configuration itemsConf[] = rootConf.getChildren();
			for (int i = 0; i < itemsConf.length; i++) {
				Configuration itemConf = itemsConf[i];
				PresentItem item = new PresentItem();
				item.setCount(itemConf.getAttribute("count"));
				item.setStartDate(itemConf.getAttribute("startdate"));
				item.setEndDate(itemConf.getAttribute("enddate"));
				item.setItemname(itemConf.getAttribute("itemname"));
				Configuration piConf[] = itemConf.getChildren();
				for (Configuration pc : piConf) {
					PlayerItem pi = new PlayerItem();
					pi.setPlayerid(pc.getAttribute("playerid"));
					pi.setCount(Integer.parseInt(pc.getAttribute("count")));
					item.getPlayids().add(pi.getPlayerid());
					item.getPis().add(pi);
				}
				items.add(item);
			}
//logger.info("presentItem loading success  file is [" + xmlname
//+ "]");
if(logger.isInfoEnabled())
	logger.info("presentItem loading success  file is [{}]", new Object[]{xmlname});
			return items;
		} catch (Exception e) {
//			logger.info("presentItem loading fail  file is [" + xmlname + "]");
			if(logger.isInfoEnabled())
				logger.info("presentItem loading fail  file is [{}]", new Object[]{xmlname});
			return new ArrayList<PresentItem>();
		}
	}

	public void savePage(List<PresentItem> items, String saveFile) {
		Configuration rootConf = new DefaultConfiguration("items", "-");
		try {
			for (PresentItem item : items) {
				Configuration itemConf = new DefaultConfiguration("item", "-");
				itemConf.setAttribute("count", item.getCount());
				itemConf.setAttribute("startdate", item.getStartDate());
				itemConf.setAttribute("enddate", item.getEndDate());
				itemConf.setAttribute("itemname", item.getItemname());
				for (PlayerItem pi : item.getPis()) {
					Configuration piConf = new DefaultConfiguration(
							"playeritem", "-");
					piConf.setAttribute("playerid", pi.getPlayerid());
					piConf.setAttribute("count", pi.getCount() + "");
					itemConf.addChild(piConf);
				}
				rootConf.addChild(itemConf);
			}
			FileUtils.chkFolder(saveFile);
			new DefaultConfigurationSerializer().serializeToFile(new File(
					saveFile), rootConf);
//			logger.info("presentItem save success  file is [" + saveFile + "]");
			if(logger.isInfoEnabled())
				logger.info("presentItem save success  file is [{}]", new Object[]{saveFile});
		} catch (Exception e) {
//			logger.info("presentItem save fail  file is [" + saveFile + "]");
			if(logger.isInfoEnabled())
				logger.info("presentItem save fail  file is [{}]", new Object[]{saveFile});
		}

	}

	public String getItemConFile() {
		return itemConFile;
	}

	public void setItemConFile(String itemConFile) {
		this.itemConFile = itemConFile;
	}

	public List<PresentItem> getItems() {
		return items;
	}

	public void setItems(List<PresentItem> items) {
		this.items = items;
	}

}
