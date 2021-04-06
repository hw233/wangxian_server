/**
 * 
 */
package com.fy.engineserver.billboard.concrete;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.engineserver.billboard.Billboard;
import com.fy.engineserver.billboard.BillboardData;
import com.fy.engineserver.billboard.BillboardMaker;
import com.fy.engineserver.billboard.BillboardManager;
import com.fy.engineserver.billboard.Billboards;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.text.XmlUtil;

/**
 * @author Administrator
 * 
 */
public class EquipmentBillboards extends Billboards implements Runnable {

	final String[][] TITLES = { { Translate.text_2320, Translate.text_4, Translate.text_395 }, { Translate.text_2320, Translate.text_4, Translate.text_395 }, { Translate.text_2320, Translate.text_4, Translate.text_395 }, { Translate.text_2320, Translate.text_4, Translate.text_395 },
			{ Translate.text_2320, Translate.text_4, Translate.text_395 }, { Translate.text_2320, Translate.text_4, Translate.text_395 }, { Translate.text_2320, Translate.text_4, Translate.text_395 }, { Translate.text_2320, Translate.text_4, Translate.text_395 },
			{ Translate.text_2320, Translate.text_4, Translate.text_395 } };

	public static final byte EQUIPMENT_WEAPON = 0;

	public static final byte EQUIPMENT_HELMET = 1;

	public static final byte EQUIPMENT_SHOULDER = 2;

	public static final byte EQUIPMENT_ARMOR = 3;

	public static final byte EQUIPMENT_WRISTER = 4;

	public static final byte EQUIPMENT_SHOES = 5;

	public static final byte EQUIPMENT_JEWELRY = 6;

	public static final byte EQUIPMENT_NECKLACE = 7;

	public static final byte EQUIPMENT_RING = 8;

	public ArrayList<BillboardData>[] al;

	boolean isNeedSave;

	String fileName = "equipment.xml";
	
	long startTime;

	/**
	 * @param name
	 * @param type
	 * @param submenu
	 */
	@SuppressWarnings("unchecked")
	public EquipmentBillboards(String name, byte type) {
		super(name, type, new String[] { Translate.text_2330, Translate.text_2331, Translate.text_2332, Translate.text_2333, Translate.text_2334, Translate.text_2335, Translate.text_2336, Translate.text_2337, Translate.text_2338 });
		this.al = new ArrayList[this.submenu.length];
		for (int i = 0; i < this.al.length; i++) {
			this.al[i] = new ArrayList<BillboardData>();
		}
		Thread th = new Thread(this);
		th.setName(Translate.text_2339);
		th.start();
		this.startTime=com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.fy.engineserver.billboard.Billboards#update(java.sql.Connection)
	 */
	@Override
	protected void update(Connection con) {
		try {
			this.billboard = new Billboard[this.submenu.length];
			for (int i = 0; i < this.billboard.length; i++) {
				this.billboard[i] = new Billboard(this.submenu[i], this.TITLES[i]);
				this.billboard[i].data = new BillboardData[BillboardManager.MAX_LINES];
			}
			if (log.isInfoEnabled()) {
//				log.info("[更新排行榜] [成功] [" + this.getName() + "]");
				if(log.isInfoEnabled())
					log.info("[更新排行榜] [成功] [{}]", new Object[]{this.getName()});
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(log.isWarnEnabled())
				log.warn("[更新排行榜] [失败] [发生错误] [" + this.getName() + "] [错误：" + e + "]", e);
		}
	}

	public synchronized boolean update(Player player, ArticleEntity ae) {
		
		return false;
	}

	public static int getArticlePoints(ArticleEntity ae) {
		
		return 0;
	}

	private boolean isExist(long id, int type) {
		for (BillboardData data : this.al[type]) {
			if (data.getId() == id) {
				this.al[type].remove(data);
				if (log.isInfoEnabled()) {
//					log.info("[更新装备排行榜] [装备已经存在，删除原来排行榜中的数据] [装备id：" + id + "]");
					if(log.isInfoEnabled())
						log.info("[更新装备排行榜] [装备已经存在，删除原来排行榜中的数据] [装备id：{}]", new Object[]{id});
				}
				return true;
			}
		}
		return false;
	}

	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				this.autoSort();
				if (this.isNeedSave&&(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-this.startTime)>200000) {
					this.outputXml();
					this.isNeedSave = false;
				}

				Thread.sleep(120000);
			} catch (Exception e) {
				e.printStackTrace();
				if(log.isWarnEnabled())
					log.warn("[生成装备排行榜文件] [失败] [发生错误] [错误：" + e + "]", e);
			}
		}
	}

	public void outputXml() throws Exception {
		long t = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		Calendar ca = Calendar.getInstance();
		ca.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		int year = ca.get(Calendar.YEAR);
		int month = ca.get(Calendar.MONTH) + 1;
		int day = ca.get(Calendar.DAY_OF_MONTH);
		int hour = ca.get(Calendar.HOUR_OF_DAY);
		int minute = ca.get(Calendar.MINUTE);

		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version='1.0' encoding='utf-8'?>\n");
		sb.append("<equipment time='" + year + "." + month + "." + day + "." + hour + "." + minute + "'>\n");
		for (int i = 0; i < this.al.length; i++) {
			sb.append("<equipmentType name='" + this.submenu[i] + "'>\n");
			for (int j = 0; j < this.al[i].size(); j++) {
				BillboardData data = this.al[i].get(j);
				sb.append("<data rankObject='" + data.getRankingObject() + "' value='" + data.getValue()
						+ "' id='" + data.getId() + "' colorType='"
						+ data.getAdditionalInfo() + "' playerId='"+data.getPlayerId()+"'>\n");
				sb.append("<![CDATA["+ data.getDescription() +"]]>");
				sb.append("</data>\n");
			}
			sb.append("</equipmentType>\n");
		}
		sb.append("</equipment>\n");

		File file = new File(BillboardMaker.getInstance().fileDir, this.fileName);
		FileOutputStream output = new FileOutputStream(file);
		output.write(sb.toString().getBytes("utf-8"));
		output.flush();
		output.close();
		if (log.isInfoEnabled()) {
//log.info("[生成装备排行榜文件] [成功] [" + BillboardMaker.getInstance().fileDir + "/" + this.fileName + "] [耗时："
//+ (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - t) + "]");
if(log.isInfoEnabled())
	log.info("[生成装备排行榜文件] [成功] [{}/{}] [耗时：{}]", new Object[]{BillboardMaker.getInstance().fileDir,this.fileName,(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - t)});
		}

	}

	public void loadFromXml() {
		try {
			long t = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			Document doc = null;
			doc = XmlUtil.load(new FileInputStream(BillboardMaker.getInstance().fileDir.getPath() + "/" + this.fileName),"utf-8");
			if (doc != null) {
				Element root = doc.getDocumentElement();
				Element[] equipmentTypes = XmlUtil.getChildrenByName(root, "equipmentType");
				if (equipmentTypes != null) {
					for (int i = 0; i < equipmentTypes.length; i++) {
						String name = XmlUtil.getAttributeAsString(equipmentTypes[i], "name", null);
						Element[] datas = XmlUtil.getChildrenByName(equipmentTypes[i], "data");
						if (datas != null) {
							for (int j = 0; j < datas.length; j++) {
								String rankObject = XmlUtil.getAttributeAsString(datas[j], "rankObject", null);
								long value = XmlUtil.getAttributeAsLong(datas[j], "value");
								String description = XmlUtil.getValueAsString(datas[j],"", null).trim();
								long id = XmlUtil.getAttributeAsLong(datas[j], "id");
								byte colorType = (byte) XmlUtil.getAttributeAsInteger(datas[j], "colorType");
								int playerId=XmlUtil.getAttributeAsInteger(datas[j], "playerId");
								BillboardData data = new BillboardData(j + 1);
								data.setRankingObject(rankObject);
								data.setValue(value);
								data.setDescription(description);
								data.setId(id);
								data.setAdditionalInfo(colorType);
								data.setPlayerId(playerId);
								this.al[i].add(data);
							}

						} else {
//							log.info("[读取装备排行榜文件] [失败] [数据为空] [类型：" + name + "]");
							if(log.isInfoEnabled())
								log.info("[读取装备排行榜文件] [失败] [数据为空] [类型：{}]", new Object[]{name});
						}
					}
//					log.info("[读取装备排行榜文件] [成功] [耗时：" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - t) + "]");
					if(log.isInfoEnabled())
						log.info("[读取装备排行榜文件] [成功] [耗时：{}]", new Object[]{(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - t)});
				} else {
					if(log.isInfoEnabled())
						log.info("[读取装备排行榜文件] [失败] [没有数据]");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(log.isWarnEnabled())
				log.warn("[读取装备排行榜文件] [失败] [发生错误] [错误：" + e + "]", e);
		}
	}

	private void autoSort() {}
	
//	@Override
//	protected void resetRankObject() {
//		// TODO Auto-generated method stub
//		
//	}

}
