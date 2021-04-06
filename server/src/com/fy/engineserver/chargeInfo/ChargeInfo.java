/**
 * 
 */
package com.fy.engineserver.chargeInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.boss.client.BossClientService;
import com.xuanzhi.tools.text.XmlUtil;
import com.xuanzhi.tools.watchdog.ConfigFileChangedAdapter;
import com.xuanzhi.tools.watchdog.ConfigFileChangedListener;

/**
 * @author Administrator
 * 
 */
public class ChargeInfo implements ConfigFileChangedListener {

	String fileName;

	String fileDir;

	// public static Logger log=Logger.getLogger("ChargeInfo");
	public static Logger log = LoggerFactory.getLogger("ChargeInfo");

	final String CHARGE_WAY_CARD = "card";

	final String CHARGE_WAY_SMS = "sms";

	/**
	 * 充值卡种类名称
	 */
	String[] cardTypeNames;

	/**
	 * 充值卡种类ID
	 */
	byte[] cardTypeIds;

	/**
	 * 每种充值卡的说明名字
	 */
	String[] cardTypeInfos;

	/**
	 * 每种充值卡的面值，所有面额用一个字符串，用分隔号隔开，例如10：20：30
	 */
	String[] cardFacevalues;

	/**
	 * 兑换元宝的数量，所有兑换的数量用一个字符串，用分隔号隔开，例如120：240：360
	 */
	String[] exchange;

	/**
	 * 短信充值途径
	 */
	String[] smsGateWay;

	/**
	 * 
	 */
	String[] smsInfos;// 信息提示
	/**
	 * 兑换率，一元短信兑换多少元宝
	 */
	int[] exchangeRate;

	/**
	 * 发送短信的金额，2元6元10元等
	 */
	String[] smsFacevalues;

	/**
	 * 发送的信息
	 */
	String[] msg;

	/**
	 * 短信号码
	 */
	String[] smsNum;

	/**
	 * 每条短信的收费金额
	 */
	int[] unitPrice;

	/**
	 * 用户可发送的金额
	 */
	int[] chargeLimit;

	/**
	 * 用户本月已经充值的金额
	 */
	int[] userChargeAmount;

	BossClientService bossClientService;

	/**
	 * 短信每日充值限额
	 */
	int smsDailyQuota;

	/**
	 * 短信每月充值限额
	 */
	int smsMonthlyQuota;

	/**
	 * 绑定短信发送的信息
	 */
	String[] bindingMsg;

	/**
	 * 绑定短信号码
	 */
	String[] bindingSmsNum;

	public final String[] GATE_WAY_NAME = { Translate.text_2570, Translate.text_238, Translate.text_2571 };

	public static ChargeInfo self;

	/**
	 * 徒弟充值返还的最低限额
	 */
	int returnLimit;

	/**
	 * 返还比例
	 */
	int returnPercent;

	/**
	 * 
	 */
	public ChargeInfo() {
		// TODO Auto-generated constructor stub
		this.bossClientService = BossClientService.getInstance();
		ChargeInfo.self = this;
	}

	public void init() {
		long t = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		try {
			// this.setDaemon(true);
			// this.setName("ChargeInfo-Watchlog");
			// this.addFile(new File(fileDir,fileName));
			// this.start();
			ConfigFileChangedAdapter.getInstance().addListener(new File(this.fileDir, this.fileName), this);
			Document doc = null;
			doc = XmlUtil.load(this.fileDir + "/" + this.fileName);
			this.readFile(doc);
		} catch (Exception e) {
			e.printStackTrace();
			if (log.isWarnEnabled()) log.warn("[读取充值信息] [失败] [发生错误] [错误：" + e + "]", e);
		}
		if (log.isInfoEnabled()) {
			// log.info("[初始化充值信息] [结束] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-t)+"毫秒]");
			if (log.isInfoEnabled()) log.info("[初始化充值信息] [结束] [耗时：{}毫秒]", new Object[] { (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - t) });
		}
		System.out.println("[系统初始化] [充值信息] [初始化完成] [" + this.getClass().getName() + "] [耗时：" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - t) + "毫秒]");
	}

	/*
	 * (non-Javadoc)
	 * @see com.xuanzhi.tools.watchdog.FileWatchdog#doOnChange(java.io.File)
	 */

	private void readFile(Document doc) throws Exception {
		if (doc != null) {
			Element root = doc.getDocumentElement();
			this.setReturnLimit(XmlUtil.getAttributeAsInteger(root, "returnLimit"));
			this.setReturnPercent(XmlUtil.getAttributeAsInteger(root, "returnPercent"));
			Element[] chargeWay = XmlUtil.getChildrenByName(root, "chargeWay");
			if (chargeWay != null) {
				for (int i = 0; i < chargeWay.length; i++) {
					String name = XmlUtil.getAttributeAsString(chargeWay[i], "name", null);
					if (name != null) {
						if (name.equals(this.CHARGE_WAY_CARD)) {
							Element[] cardType = XmlUtil.getChildrenByName(chargeWay[i], "cardType");
							if (cardType != null) {
								this.cardTypeNames = new String[cardType.length];
								this.cardTypeIds = new byte[cardType.length];
								this.cardTypeInfos = new String[cardType.length];
								this.cardFacevalues = new String[cardType.length];
								this.exchange = new String[cardType.length];
								for (int j = 0; j < cardType.length; j++) {
									this.cardTypeNames[j] = XmlUtil.getAttributeAsString(cardType[j], "name", null);
									this.cardTypeIds[j] = (byte) XmlUtil.getAttributeAsInteger(cardType[j], "id");
									this.cardTypeInfos[j] = XmlUtil.getAttributeAsString(cardType[j], "info", null);
									Element[] facevalue = XmlUtil.getChildrenByName(cardType[j], "facevalue");
									if (facevalue != null) {
										String s1 = "";
										String s2 = "";
										for (int k = 0; k < facevalue.length; k++) {
											s1 += XmlUtil.getAttributeAsString(facevalue[k], "value", null);
											s2 += XmlUtil.getAttributeAsString(facevalue[k], "exchange", null);
											if (k < facevalue.length - 1) {
												s1 += ":";
												s2 += ":";
											}
										}
										this.cardFacevalues[j] = s1;
										this.exchange[j] = s2;
									} else {
										// log.warn("[从文件中读取充值信息] [失败] [没有兑换信息] [cardType:"+this.cardTypeNames[j]+"]");
										if (log.isWarnEnabled()) log.warn("[从文件中读取充值信息] [失败] [没有兑换信息] [cardType:{}]", new Object[] { this.cardTypeNames[j] });
									}
								}
							} else {
								if (log.isWarnEnabled()) log.warn("[从文件中读取充值信息] [失败] [没有充值卡充值信息]");
							}
							if (log.isInfoEnabled()) {
								log.info("[从文件中读取充值信息] [读取充值卡充值信息成功]");
							}

						} else if (name.equals(this.CHARGE_WAY_SMS)) {
							this.smsDailyQuota = XmlUtil.getAttributeAsInteger(chargeWay[i], "dailyQuota");
							this.smsMonthlyQuota = XmlUtil.getAttributeAsInteger(chargeWay[i], "monthlyQuota");
							Element[] gateWay = XmlUtil.getChildrenByName(chargeWay[i], "gateWay");
							if (gateWay != null) {
								this.smsGateWay = new String[gateWay.length];
								this.smsInfos = new String[gateWay.length];
								this.exchangeRate = new int[gateWay.length];
								this.smsFacevalues = new String[gateWay.length];
								this.msg = new String[gateWay.length];
								this.smsNum = new String[gateWay.length];
								this.unitPrice = new int[gateWay.length];
								this.userChargeAmount = new int[gateWay.length];
								this.chargeLimit = new int[gateWay.length];
								this.bindingMsg = new String[gateWay.length];
								this.bindingSmsNum = new String[gateWay.length];
								for (int j = 0; j < gateWay.length; j++) {
									this.smsGateWay[j] = XmlUtil.getAttributeAsString(gateWay[j], "name", null);
									this.smsInfos[j] = XmlUtil.getAttributeAsString(gateWay[j], "info", null);
									this.exchangeRate[j] = XmlUtil.getAttributeAsInteger(gateWay[j], "exchangeRate");
									this.smsFacevalues[j] = XmlUtil.getAttributeAsString(gateWay[j], "facevalue", null);
									this.msg[j] = XmlUtil.getAttributeAsString(gateWay[j], "msg", null);
									this.smsNum[j] = XmlUtil.getAttributeAsString(gateWay[j], "smsNum", null);
									this.unitPrice[j] = XmlUtil.getAttributeAsInteger(gateWay[j], "unitPrice");
									this.bindingMsg[j] = XmlUtil.getAttributeAsString(gateWay[j], "bindingMsg", null);
									this.bindingSmsNum[j] = XmlUtil.getAttributeAsString(gateWay[j], "bindingSmsNum", null);
								}

							} else {
								if (log.isWarnEnabled()) log.warn("[从文件中读取充值信息] [失败] [没有短信充值信息]");
							}

						} else {
							// log.warn("[从文件中读取充值信息] [不支持的充值方式] [name:"+name+"]");
							if (log.isWarnEnabled()) log.warn("[从文件中读取充值信息] [不支持的充值方式] [name:{}]", new Object[] { name });
						}
					}
				}
			}
		}
	}

	public static void main(String[] args) {

	}

	// @Override
	// protected void doOnChange(File file) {
	// // TODO Auto-generated method stub
	// if(log.isInfoEnabled()){
	// log.info("[自动读取充值信息] [开始]");
	// }
	// long t=com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
	// FileInputStream fis=null;
	// try{
	// fis= new FileInputStream(file);
	// Document doc = null;
	// doc=XmlUtil.load(fis);
	// this.readFile(doc);
	// }catch(Exception e){
	// e.printStackTrace();
	// log.warn("[从文件中读取充值信息] [失败] [发生错误] [错误："+e+"]",e);
	// }finally{
	// if(fis!=null){
	// try {
	// fis.close();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// }
	// if(log.isInfoEnabled()){
	// log.info("[自动读取充值信息] [结束] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-t)+"毫秒]");
	// }
	// }

	public int[] getUserChargeAmount() {
		return userChargeAmount;
	}

	public void setUserChargeAmount(int[] userChargeAmount) {
		this.userChargeAmount = userChargeAmount;
	}

	public String[] getCardTypeNames() {
		return cardTypeNames;
	}

	public byte[] getCardTypeIds() {
		return cardTypeIds;
	}

	public String[] getCardTypeInfos() {
		return cardTypeInfos;
	}

	public String[] getExchange() {
		return exchange;
	}

	public String[] getSmsGateWay() {
		return smsGateWay;
	}

	public String[] getSmsInfos() {
		return smsInfos;
	}

	public int[] getSmsExchangeRate() {
		return exchangeRate;
	}

	public String[] getSmsFacevalues() {
		return smsFacevalues;
	}

	public String[] getMsg() {
		return msg;
	}

	public String[] getSmsNum() {
		return smsNum;
	}

	public int[] getUnitPrice() {
		return unitPrice;
	}

	public int[] getChargeLimit() {
		return chargeLimit;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String[] getCardFacevalues() {
		return cardFacevalues;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFileDir() {
		return fileDir;
	}

	public void setFileDir(String fileDir) {
		this.fileDir = fileDir;
	}

	/**
	 * 0:player charge limit 1:charge daily amount 2:charge monthly amount
	 * @param player
	 * @return
	 */
	public long[] getPlayerChargeData(Player player) {
		long[] l = new long[3];
		//TODO 
		// long t = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		// try {
		// long[] savingRecord = this.bossClientService.getVasSavingRecord(player.getUsername());
		// if (savingRecord != null) {
		// long dailySavingRecord = savingRecord[0];
		// long monthlySavingRecord = savingRecord[1];
		// if (dailySavingRecord >= this.smsDailyQuota) {
		// l[0] = 0;
		// } else {
		// l[0] = this.smsDailyQuota - dailySavingRecord;
		// if (l[0] > this.smsMonthlyQuota - monthlySavingRecord) {
		// l[0] = this.smsMonthlyQuota - monthlySavingRecord;
		// }
		// if (l[0] < 0) {
		// l[0] = 0;
		// }
		// }
		// l[1] = dailySavingRecord;
		// l[2] = monthlySavingRecord;
		// if (log.isInfoEnabled()) {
		// //
		// log.info("[读取短信充值信息] [读取玩家短信充值信息] [成功] [玩家："+player.getName()+"] [charge limit:"+l[0]+"] [charge daily amount:"+l[1]+"] [charge monthly amount:"+l[2]+"] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-t)+"]");
		// if (log.isInfoEnabled()) log.info("[读取短信充值信息] [读取玩家短信充值信息] [成功] [玩家：{}] [charge limit:{}] [charge daily amount:{}] [charge monthly amount:{}] [耗时：{}]", new Object[] {
		// player.getName(), l[0], l[1], l[2], (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - t) });
		// }
		// } else {
		// // log.warn("[读取短信充值信息] [读取玩家短信充值信息] [失败] [玩家信息为空] [玩家："+player.getName()+"]");
		// if (log.isWarnEnabled()) log.warn("[读取短信充值信息] [读取玩家短信充值信息] [失败] [玩家信息为空] [玩家：{}]", new Object[] { player.getName() });
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// if (log.isWarnEnabled()) log.warn("[读取短信充值信息] [读取玩家短信充值信息] [失败] [发生错误] [错误：" + e + "]", e);
		// }

		return l;
	}

	public String[] getBindingMsg() {
		return bindingMsg;
	}

	public String[] getBindingSmsNum() {
		return bindingSmsNum;
	}

	/**
	 * 短信充值
	 * @param player
	 * @param unitPrice
	 */
	public void smsCharge(Player player, int vastype, int unitPrice, int smsCounts) {
		String camp = "";
		//TODO
		// String map = player.getGame();
		// if (map == null || map.length() == 0) {
		// map = player.getLastGame();
		// }
		// if (map == null) map = "";
		// this.bossClientService.vasSaving(player.getName(), player.getUsername(), vastype, unitPrice, player.getLevel(), CareerManager.careerNames[player.getCareer()], player.getSex(), camp, map, smsCounts);
	}

	public void fileChanged(File file) {
		// TODO Auto-generated method stub
		if (log.isInfoEnabled()) {
			log.info("[自动读取充值信息] [开始]");
		}
		long t = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			Document doc = null;
			doc = XmlUtil.load(fis);
			this.readFile(doc);
		} catch (Exception e) {
			e.printStackTrace();
			if (log.isWarnEnabled()) log.warn("[从文件中读取充值信息] [失败] [发生错误] [错误：" + e + "]", e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if (log.isInfoEnabled()) {
			// log.info("[自动读取充值信息] [结束] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-t)+"毫秒]");
			if (log.isInfoEnabled()) log.info("[自动读取充值信息] [结束] [耗时：{}毫秒]", new Object[] { (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - t) });
		}
	}

	public int getReturnLimit() {
		return returnLimit;
	}

	public void setReturnLimit(int returnLimit) {
		this.returnLimit = returnLimit;
	}

	public int getReturnPercent() {
		return returnPercent;
	}

	public void setReturnPercent(int returnPercent) {
		this.returnPercent = returnPercent;
	}

	public static ChargeInfo getInstance() {
		return ChargeInfo.self;
	}

}
