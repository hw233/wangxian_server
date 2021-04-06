package com.fy.boss.finance.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.service.platform.AlipaySavingManager;
import com.fy.boss.finance.service.platform.GameServerSzfSavingManager;
import com.fy.boss.finance.service.platform.GameServerYeepaySavingManager;
import com.fy.boss.platform._3G._3GSavingManager;
import com.fy.boss.platform._553._553SavingManager;
import com.fy.boss.platform.aiwan.AiWanSavingManager;
import com.fy.boss.platform.aiyouxi.AiYouSavingManager;
import com.fy.boss.platform.anzhi.AnZhiSavingManager;
import com.fy.boss.platform.appchina.AppChinaSavingManager;
import com.fy.boss.platform.baidusdk.BAIDUSDKSavingManager;
import com.fy.boss.platform.baoruan.BaoRuanSavingManager;
import com.fy.boss.platform.duoku.BAIDUYYSavingManager;
import com.fy.boss.platform.duoku.DuokuAlipaySavingManager;
import com.fy.boss.platform.duoku.DuokuSavingManager;
import com.fy.boss.platform.duoku.DuokuYYAlipaySavingManager;
import com.fy.boss.platform.feiliu.FeiLiuSavingManager;
import com.fy.boss.platform.gfeng.JiFengSavingManager;
import com.fy.boss.platform.heepay.HeePayManager;
import com.fy.boss.platform.huawei.HuaweiYeepaySavingManager;
import com.fy.boss.platform.huiyao.HuiYaoSavingManager;
import com.fy.boss.platform.huiyao.HuoGameSavingManager;
import com.fy.boss.platform.iapppay.IapppaySavingManager;
import com.fy.boss.platform.koapu.KaoPuSavingManager;
import com.fy.boss.platform.kunlun.KoreaSavingManager;
import com.fy.boss.platform.kunlun.KunlunSavingManager;
import com.fy.boss.platform.kunlun.MaLaiSavingManager;
import com.fy.boss.platform.kupai.KuPaiSavingManager;
import com.fy.boss.platform.lenovo.LeDouSavingManager;
import com.fy.boss.platform.lenovo.LenovoMMSavingManager;
import com.fy.boss.platform.lenovo.LenovoSavingManager;
import com.fy.boss.platform.lenovo.LenovoapppaySavingManager;
import com.fy.boss.platform.lenovo.LenovoyxapppaySavingManager;
import com.fy.boss.platform.liantong.LianTongSavingManager;
import com.fy.boss.platform.meizu.MeiZuSavingManager;
import com.fy.boss.platform.mumayi.MumayiSavingManager;
import com.fy.boss.platform.oppo.OppoSavingManager;
import com.fy.boss.platform.qihoo.QihooGateWaySavingManager;
import com.fy.boss.platform.qihoo.QihooSDKSavingManager;
import com.fy.boss.platform.qq.msdk.MSDKSavingManager;
import com.fy.boss.platform.sina.SinaWYXNewSavingManager;
import com.fy.boss.platform.sina.SinaWYXSavingManager;
import com.fy.boss.platform.sina.SinaWeiBiSavingManager;
import com.fy.boss.platform.sogou.SoGouSavingManager;
import com.fy.boss.platform.t6533.T185SDKSavingManager;
import com.fy.boss.platform.t6533.T6533SDKSavingManager;
import com.fy.boss.platform.tbtsdk.TbtNewSdkSavingManager;
import com.fy.boss.platform.tbtsdk.TbtSdkSavingManager;
import com.fy.boss.platform.u8.U8SDKSavingManager;
import com.fy.boss.platform.u8.V8SDKSavingManager;
import com.fy.boss.platform.uc.UCSDKSavingManager;
import com.fy.boss.platform.uc.UCWapSavingManager;
import com.fy.boss.platform.uc.UCYeepaySavingManager;
import com.fy.boss.platform.wandoujia.WANDOUJIASDKSavingManager;
import com.fy.boss.platform.wapalipay.AlipayWapSavingManager;
import com.fy.boss.platform.website.AlipayWebSavingManager;
import com.fy.boss.platform.xiaomi.WaliSdkSavingManager;
import com.fy.boss.platform.xiaomi.XiaomiSdkSavingManager;
import com.fy.boss.platform.xmwan.XMWanSavingManager;
import com.fy.boss.platform.xy.XYSavingManager;
import com.fy.boss.platform.yidong.YiDongMMSavingManager;
import com.fy.boss.platform.yidong.YiDongSavingManager;
import com.fy.boss.platform.yijie.YiJieSavingManager;
import com.fy.boss.platform.yunyou.YunyouSdkSavingManager;
import com.xuanzhi.tools.cache.LruMapCache;


public class PlatformSavingCenter {

	public static final Logger logger = Logger.getLogger(PlatformSavingCenter.class.getName()); 

	public static final int SELF_PLATFORM_神州付 = 0;
	public static final int SELF_PLATFORM_易宝 = 1;

	protected static PlatformSavingCenter self;

	private LruMapCache badSavingMap;

	public int selfSavingPlatform = 0;

	public static Map<String, String> channelSavingMap = new HashMap<String,String>();

	static {
		channelSavingMap.put("NEWUCSDK_XUNXIAN", "UCSDKSavingManager");
		channelSavingMap.put("MEIZUSDK_XUNXIAN", "MeiZuSavingManager");
		channelSavingMap.put("XIAOMISDK_XUNXIAN", "XiaomiSdkSavingManager");
		channelSavingMap.put("XIAOMI2SDK_XUNXIAN", "XiaomiSdkSavingManager");
		channelSavingMap.put("QQYSDK_XUNXIAN", "MSDKSavingManager");
		channelSavingMap.put("HUAWEISDK_XUNXIAN", "HuaweiYeepaySavingManager");
		channelSavingMap.put("OPPOSDK_XUNXIAN", "OppoSavingManager");
		channelSavingMap.put("YIJIESDK_XUNXIAN", "YiJieSavingManager");
		channelSavingMap.put("YIJIE01SDK_XUNXIAN", "YiJieSavingManager");
		channelSavingMap.put("YIJIE02SDK_XUNXIAN", "YiJieSavingManager");
		channelSavingMap.put("QUICKSDK_XUNXIAN", "YiJieSavingManager");
		channelSavingMap.put("QUICK2SDK_XUNXIAN", "YiJieSavingManager");
		channelSavingMap.put("QUICK3SDK_XUNXIAN", "YiJieSavingManager");
		channelSavingMap.put("VIVOSDK_XUNXIAN", "OppoSavingManager");
		channelSavingMap.put("U8SDK_XUNXIAN", "U8SDKSavingManager");
		channelSavingMap.put("HUASHENGSDK_XUNXIAN", "V8SDKSavingManager");
		channelSavingMap.put("QILEAPPSTORE_XUNXIAN", "HuiYaoSavingManager");
		channelSavingMap.put("HUOGAMEAPPSTORE_XUNXIAN", "HuoGameSavingManager");
		channelSavingMap.put("HUOGAME2APPSTORE_XUNXIAN", "HuoGameSavingManager");
		channelSavingMap.put("HUOGAME3APPSTORE_XUNXIAN", "HuoGameSavingManager");
		channelSavingMap.put("HUOGAME4APPSTORE_XUNXIAN", "HuoGameSavingManager");
		channelSavingMap.put("HUOGAME5APPSTORE_XUNXIAN", "HuoGameSavingManager");
		channelSavingMap.put("JIUZHOUPIAOMIAOQU_XUNXIAN", "JiuZhouSavingManager");
		channelSavingMap.put("HAODONGSDK_XUNXIAN", "HaoDongSavingManager");
		channelSavingMap.put("GUOPANSDK_XUNXIAN", "GuoPanSavingManager");
		channelSavingMap.put("GUOPAN2SDK_XUNXIAN", "GuoPanSavingManager");
		channelSavingMap.put("XUNXIANJUEAPPSTORE_XUNXIAN", "XMWanSavingManager");
		channelSavingMap.put("LEHAIHAIAPPSTORE_XUNXIAN", "V8SDKSavingManager");
		channelSavingMap.put("ANJIUAPPSTORE_XUNXIAN", "V8SDKSavingManager");
		channelSavingMap.put("MAIYOUAPPSTORE_XUNXIAN", "V8SDKSavingManager");
		channelSavingMap.put("XIAO7APPSTORE_XUNXIAN", "V8SDKSavingManager");
		channelSavingMap.put("AIWANAPPSTORE_XUNXIAN", "AiWanSavingManager");
		channelSavingMap.put("AIWAN2APPSTORE_XUNXIAN", "AiWanSavingManager");
		channelSavingMap.put("6533BT_XUNXIAN", "T6533SDKSavingManager");
		channelSavingMap.put("185BT_XUNXIAN", "T185SDKSavingManager");
	}

	public static Map<String,String> cardSavingMap = new HashMap<String,String>();

	static {
		cardSavingMap.put("支付宝", "AlipaySavingManager");
		cardSavingMap.put("微信支付", "HeePayManager");
//		cardSavingMap.put("移动充值卡", "GameServerSzfSavingManager");
//		cardSavingMap.put("联通一卡付", "GameServerSzfSavingManager");
//		cardSavingMap.put("电信充值卡", "GameServerSzfSavingManager");
//		cardSavingMap.put("盛大卡", "GameServerYeepaySavingManager");
//		cardSavingMap.put("征途卡", "GameServerYeepaySavingManager");
//		cardSavingMap.put("骏网一卡通", "GameServerYeepaySavingMa]nager");
//		cardSavingMap.put("久游卡", "GameServerYeepaySavingManager");
//		cardSavingMap.put("网易卡", "GameServerYeepaySavingManager");
//		cardSavingMap.put("完美卡", "GameServerYeepaySavingManager");
//		cardSavingMap.put("搜狐卡", "GameServerYeepaySavingManager");
		//.put("支付宝", "GameServerYeepaySavingManager");
//		cardSavingMap.put("支付宝WEB", "AlipayWebSavingManager");
//		cardSavingMap.put("Wap支付宝", "AlipayWapSavingManager");
//		cardSavingMap.put("网页支付宝", "AlipayWebSavingManager");
//		cardSavingMap.put("UNIONWEB", "UnionWebSavingManager");
//		cardSavingMap.put("汇元骏网卡", "HuiYuanSavingManager");
	}

	public static PlatformSavingCenter getInstance() {
		return self;
	}

	public void initialize() {
		badSavingMap = new LruMapCache(4*1024*1024, 30*60*1000);
		self = this;
		System.out.println("[充值任务中心初始化成功] ["+this.getClass().getName()+"] [initialized]");
	}

	public LruMapCache getBadSavingMap() {
		return badSavingMap;
	}

	public void setBadSavingMap(LruMapCache badSavingMap) {
		this.badSavingMap = badSavingMap;
	}

	public int getBadSavingCount(long passportId) {
		return (Integer)badSavingMap.__get(passportId);
	}

	public boolean isBadSavingPassport(long passportId) {
		Integer bad = (Integer)badSavingMap.__get(passportId);
		if(bad != null && bad >= 50) {
			return true;
		}
		return false;
	}

	public void addBadSaving(long passportId) {
		Integer bad = (Integer)badSavingMap.__get(passportId);
		if(bad == null) {
			bad = 0;
		} 
		bad++;
		badSavingMap.__put(passportId, bad);
	}

	/**
	 * 充值卡方式充值，选择充值接口是，优先从channelSavingMap中获得，如果没有，从cardSavingMap中获得
	 * @param passport
	 * @param cardtype	卡类型:移动充值卡|联通一卡付|电信充值卡|盛大卡|征途卡|骏网一卡通|久游卡|网易卡|完美卡|搜狐卡
	 * @param mianzhi		10|30|50|100...
	 * @param cardno	卡号
	 * @param cardpass	卡密
	 * @param server	服务器名:巍巍昆仑
	 * @param channel		渠道串
	 * @param otherDesp 玩家用户名
	 * @param os 操作系统  android|ios
	 *记住暂时要维护两份代码
	 */
	public String cardSaving(Passport passport, String cardtype, int mianzhi, String cardno, String cardpass, 
			String server, String channel, String orderID, String otherDesp, String os) {
		String  playerId = "";

		synchronized(passport) {
			try {
				//如果传入的卡号和密码包括中文 则去掉中文
				String reg = "[\u4E00-\u9FA5]+";
				if(cardno != null)
				{
					cardno = cardno.replaceAll(reg, "");
					cardno = cardno.trim();
				}

				if(cardpass != null)
				{
					cardpass = cardpass.replaceAll(reg, "");
					cardpass = cardpass.trim();
				}

				
		
				
				String smanager = channelSavingMap.get(channel);
				if(smanager == null) {
					smanager = "";
					logger.warn("[调用平台充值接口] [无匹配渠道串的Manager1] [用户名:"+passport.getUserName()+"] [充值类型:"+cardtype+"] [面值:"+mianzhi+"] [充值卡号:"+cardno+"] [充值密码:"+cardpass+"] [服务器名字:"+server+"] [渠道串:"+channel+"]");
				}
				
				
				if(channel.toLowerCase().contains("win8dcn"))
				{
					if(cardtype.contains("支付宝"))
					{
						cardtype = "网页支付宝";
					}
					
					if(smanager == null || smanager.length() == 0)
					{
						smanager = "AlipayWebSavingManager";
					}
				}
				
				
				else if(smanager.equals("SinaWYXSavingManager"))
				{
					if(cardtype.equals("新浪微币")) {
						SinaWeiBiSavingManager savingManager = SinaWeiBiSavingManager.getInstance();
						return savingManager.cardSaving(passport, cardno, server, os,channel);
					} 
					else
					{
						SinaWYXSavingManager savingManager = SinaWYXSavingManager.getInstance();
						return savingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, server, os,channel);
					}
				}
				else if(smanager.equals("UCSDKSavingManager"))
				{
					UCSDKSavingManager savingManager = UCSDKSavingManager.getInstance();
					return savingManager.sdkSaving(passport, cardno, server,os, channel);
				}
				else 	if(smanager.equals("TbtSdkSavingManager")) {
					TbtSdkSavingManager savingManager = TbtSdkSavingManager.getInstance();
					return savingManager.tbtSdkSaving(passport, channel, server, mianzhi, os);
				} 
				//else if(smanager.equals("RenRenClientSavingManager")) {
				//				RenRenClientSavingManager savingManager = RenRenClientSavingManager.getInstance();
				//				return savingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, addAmount, otherDesp, server, callBackURL, channel, orderID);
				//			} else if(smanager.equals("UCWebSavingManager")) {
				//				UCWebSavingManager savingManager = UCWebSavingManager.getInstance();
				//				return savingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, addAmount, otherDesp, server, callBackURL, channel, orderID);
				//			} else if(smanager.equals("UCSavingManager")) {
				//				UCSavingManager savingManager = UCSavingManager.getInstance();
				//				return savingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, addAmount, otherDesp, server, callBackURL, channel, orderID);
				//			} else if(smanager.equals("SinaWYXSavingManager")) {
				//				SinaWYXSavingManager savingManager = SinaWYXSavingManager.getInstance();
				//				return savingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, addAmount, otherDesp, server, callBackURL, channel, orderID);
				//			} else if(smanager.equals("SinaWYXClientSavingManager")) {
				//				SinaWYXClientSavingManager savingManager = SinaWYXClientSavingManager.getInstance();
				//				return savingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, addAmount, otherDesp, server, callBackURL, channel, orderID);
				//			} else if(smanager.equals("AppChinaSavingManager")) {
				//				AppChinaSavingManager savingManager = AppChinaSavingManager.getInstance();
				//				return savingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, addAmount, otherDesp, server, callBackURL, channel, orderID);
				//			} else if(smanager.equals("AppChinaSavingManager")) {
				//				BaoRuanSavingManager savingManager = BaoRuanSavingManager.getInstance();
				//				return savingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, addAmount, otherDesp, server, callBackURL, channel, orderID);
				//			} else if(smanager.equals("AppStoreSavingManager")) {
				//				AppStoreSavingManager savingManager = AppStoreSavingManager.getInstance();
				//				return savingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, addAmount, otherDesp, server, callBackURL, channel, orderID);
				//			} else if(smanager.equals("UCLYWebSavingManager")) {
				//				UCLYWebSavingManager savingManager = UCLYWebSavingManager.getInstance();
				//				return savingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, addAmount, otherDesp, server, callBackURL, channel, orderID);
				//			} else if(smanager.equals("DFGuoXinSavingManager")) {
				//				DFGuoXinSavingManager savingManager = DFGuoXinSavingManager.getInstance();
				//				return savingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, addAmount, otherDesp, server, callBackURL, channel, orderID);
				//			} else if(smanager.equals("ChinaMobileYXJDSavingManager")) {
				//				ChinaMobileYXJDSavingManager savingManager = ChinaMobileYXJDSavingManager.getInstance();
				//				return savingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, addAmount, otherDesp, server, callBackURL, channel, orderID);				
				//			} else if(smanager.equals("UCIOSSavingManager")) {
				//				UCIOSSavingManager savingManager = UCIOSSavingManager.getInstance();
				//				return savingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, addAmount, otherDesp, server, callBackURL, channel, orderID);				
				//			} else if(smanager.equals("GfanClientSavingManager")) {
				//				GfanClientSavingManager savingManager = GfanClientSavingManager.getInstance();
				//				return savingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, addAmount, otherDesp, server, callBackURL, channel, orderID);				

				else if(smanager.equals("UCYeepaySavingManager")) {
					if(cardtype.equals("UCWap支付")) {
						return UCWapSavingManager.getInstance().wapSaving(passport, mianzhi, server, os, channel);
					} else {
						UCYeepaySavingManager savingManager = UCYeepaySavingManager.getInstance();
						return savingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, otherDesp, server, channel, orderID);
					}
				}
				else if(smanager.equals("FeiLiuSavingManager")){
					FeiLiuSavingManager savingManager = FeiLiuSavingManager.getInstance();
					return savingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, server, os,channel);
				}
				else if(smanager.equals("AppChinaSavingManager")){
					AppChinaSavingManager savingManager = AppChinaSavingManager.getInstance();
					return savingManager.appChinaSaving(passport, channel, server, mianzhi, os);
				}
				else if(smanager.equals("LenovoSavingManager")){
					LenovoSavingManager savingManager = LenovoSavingManager.getInstance();
					return savingManager.lenovoSaving(passport, channel, server, mianzhi, os);
				}
				else if(smanager.equals("BaoRuanSavingManager")){
					BaoRuanSavingManager savingManager = BaoRuanSavingManager.getInstance();
					return savingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, server, os, channel);
				}
				else if(smanager.equals("IapppaySavingManager")){
					IapppaySavingManager savingManager = IapppaySavingManager.getInstance();
					return savingManager.iapppaySaving(passport, channel, server, mianzhi, os);
				}
				else if(smanager.equals("KuPaiSavingManager")){
					KuPaiSavingManager savingManager = KuPaiSavingManager.getInstance();
					return savingManager.kupaiSaving(passport, channel, server, mianzhi, os);
				}
				//三星 只有银联充值才会走三星银联sdk相关逻辑 其余的走本地
				else if(smanager.equals("YunyouSdkSavingManager")){
					YunyouSdkSavingManager savingManager = YunyouSdkSavingManager.getInstance();
					return savingManager.yunyouSdkSaving(passport, cardno, channel, server, mianzhi, os);
				}
				else if(smanager.equals("HuaweiYeepaySavingManager")){
					HuaweiYeepaySavingManager savingManager = HuaweiYeepaySavingManager.getInstance();
					return savingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, otherDesp, server, channel, orderID);
				}
				else if(smanager.equals("XiaomiSdkSavingManager")){
					XiaomiSdkSavingManager savingManager = XiaomiSdkSavingManager.getInstance();
					return savingManager.xiaomiSaving(passport,channel,server,mianzhi,os);
				}
				else if(smanager.equals("QihooSDKSavingManager")){
					if("奇虎360充值".equals(cardtype))
					{
						QihooSDKSavingManager savingManager = QihooSDKSavingManager.getInstance();
						return savingManager.qihooSaving(passport,channel,server,mianzhi,os);
					}
					else
					{
						QihooGateWaySavingManager savingManager = QihooGateWaySavingManager.getInstance();
						return savingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, server, os, channel);
					}
				}
				else if("天猫直充".equals(cardtype) && smanager.equals("AlipayWebSavingManager")){
					AlipayWebSavingManager savingManager = AlipayWebSavingManager.getInstance();
					return savingManager.aliSaving(passport, channel, server, mianzhi,  5, "", os);
				}
				else if("昆仑Android".equals(cardtype) && smanager.equals("KunlunSavingManager")){
					KunlunSavingManager savingManager = KunlunSavingManager.getInstance();
					return savingManager.kunlunSaving(passport, channel, server, mianzhi, os);
				}
				else if(smanager.equals("MaLaiSavingManager")){
					MaLaiSavingManager savingManager = MaLaiSavingManager.getInstance();
					return savingManager.kunlunSaving(passport, channel, server, mianzhi, os);
				}
				else if(smanager.equals("DuokuSavingManager")) {
					if(!cardtype.contains("支付宝"))
					{
						DuokuSavingManager savingManager = DuokuSavingManager.getInstance();
						return savingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, server, os,channel);
					}
					else
					{
						DuokuAlipaySavingManager duokuAlipaySavingManager = DuokuAlipaySavingManager.getInstance();
						return duokuAlipaySavingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, server, os, channel);
					}
						
					
				}
				else if(smanager.equals("_553SavingManager")) {
					_553SavingManager savingManager = _553SavingManager.getInstance();
					return savingManager._553SdkSaving(passport, channel, server, mianzhi, os);
				}
				else if(smanager.equals("AiYouSavingManager")) {
					AiYouSavingManager savingManager = AiYouSavingManager.getInstance();
					return savingManager.aiyouSaving(passport,mianzhi,server, os,channel,playerId);
				}
				else 	if(smanager.equals("TbtNewSdkSavingManager")) {
					TbtNewSdkSavingManager savingManager = TbtNewSdkSavingManager.getInstance();
					return savingManager.tbtSdkSaving(passport,cardtype, channel, server, mianzhi, os);
				} 
				else 	if(smanager.equals("YiDongSavingManager")) {
					YiDongSavingManager savingManager = YiDongSavingManager.getInstance();
					return savingManager.yidongSaving(passport, channel, server, mianzhi, cardno, os);
				}
				else if(smanager.equals("LenovoapppaySavingManager")){
					LenovoapppaySavingManager savingManager = LenovoapppaySavingManager.getInstance();
					return savingManager.iapppaySaving(passport, channel, server, mianzhi, os);
				}
				else if(smanager.equals("BAIDUYYSavingManager")) {
					if(!cardtype.contains("支付宝"))
					{
						BAIDUYYSavingManager savingManager = BAIDUYYSavingManager.getInstance();
						return savingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, server, os,channel);
					}
					else
					{
						DuokuYYAlipaySavingManager duokuYYAlipaySavingManager = DuokuYYAlipaySavingManager.getInstance();
						return duokuYYAlipaySavingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, server, os,channel);
					}
				}
				else if(smanager.equals("JiFengSavingManager")){
					JiFengSavingManager savingManager = JiFengSavingManager.getInstance();
					return savingManager.jifengSaving(passport, channel, server, mianzhi, os);
				}
				else if(smanager.equals("AnZhiSavingManager")){
					AnZhiSavingManager savingManager = AnZhiSavingManager.getInstance();
					return savingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, server, os, channel);
				}
				else if(smanager.equals("LeDouSavingManager")){
					LeDouSavingManager savingManager = LeDouSavingManager.getInstance();
					return savingManager.ledouSaving(passport, channel, server, mianzhi, os);
				}
				else if(smanager.equals("LenovoyxapppaySavingManager")){
					LenovoyxapppaySavingManager savingManager = LenovoyxapppaySavingManager.getInstance();
					return savingManager.iapppaySaving(passport, channel, server, mianzhi, os);
				}
				else if(smanager.equals("_3GSavingManager")) {
					_3GSavingManager savingManager = _3GSavingManager.getInstance();
					return savingManager._3GSdkSaving(passport, channel, server, mianzhi, os);
				}
				else if(smanager.equals("WaliSdkSavingManager")){
					WaliSdkSavingManager savingManager = WaliSdkSavingManager.getInstance();
					return savingManager.waliSaving(passport,channel,server,mianzhi,os);
				}

				else if(smanager.equals("KoreaSavingManager")){
					KoreaSavingManager savingManager = KoreaSavingManager.getInstance();
					return savingManager.kunlunSaving(passport, channel, server, mianzhi, os);
				}
				else if(smanager.equals("WANDOUJIASDKSavingManager"))
				{
					WANDOUJIASDKSavingManager savingManager = WANDOUJIASDKSavingManager.getInstance();
					return savingManager.sdkSaving(passport,server,os, channel);
				}
				else if(smanager.equals("SinaWYXNewSavingManager"))
				{
					SinaWYXNewSavingManager savingManager = SinaWYXNewSavingManager.getInstance();
					return savingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, server, os,channel);
				}
				else if("网页支付宝".equals(cardtype.trim()) && smanager.equals("AlipayWebSavingManager")){
					AlipayWebSavingManager savingManager = AlipayWebSavingManager.getInstance();
					return savingManager.aliSaving(passport, channel, server, mianzhi,  5, "", os);
				}
				else 	if(smanager.equals("LianTongSavingManager")) {
					LianTongSavingManager savingManager = LianTongSavingManager.getInstance();
					return savingManager.liantongSaving(passport, channel, server, mianzhi, cardno, os);
				}
				else if(smanager.equals("SoGouSavingManager"))
				{
					SoGouSavingManager savingManager = SoGouSavingManager.getInstance();
					return savingManager.cardSaving(passport,  mianzhi,  server, os,channel);
				}
				else if(smanager.equals("BAIDUSDKSavingManager"))
				{
					BAIDUSDKSavingManager savingManager = BAIDUSDKSavingManager.getInstance();
					return savingManager.cardSaving(passport,  mianzhi,  server, os,channel);
				}
				
				else if(smanager.equals("MumayiSavingManager")){
					MumayiSavingManager savingManager = MumayiSavingManager.getInstance();
					return savingManager.mumayiSaving(passport, channel, server, mianzhi, os);
				}
				else if(smanager.equals("LenovoMMSavingManager")){
					LenovoMMSavingManager savingManager = LenovoMMSavingManager.getInstance();
					return savingManager.iapppaySaving(passport, channel, server, mianzhi, os);
				}
				else 	if(smanager.equals("YiDongMMSavingManager")) {
					YiDongMMSavingManager savingManager = YiDongMMSavingManager.getInstance();
					return savingManager.yidongSaving(passport, channel, server, mianzhi, cardno, os);
				}
				
				else if(smanager.equals("MeiZuSavingManager")){
					MeiZuSavingManager savingManager = MeiZuSavingManager.getInstance();
					return savingManager.meizuSaving(passport, channel, server, mianzhi, os);
				}
				
				else if(smanager.equals("KaoPuSavingManager")){
					KaoPuSavingManager savingManager = KaoPuSavingManager.getInstance();
					return savingManager.cardSaving(passport, channel, server, mianzhi, os);
				}else if(smanager.equals("XYSavingManager")){
					XYSavingManager savingManager = XYSavingManager.getInstance();
					return savingManager.xySaving(passport, channel, server, mianzhi, os);
				}
//				else if(smanager.equals("XMWanSavingManager")){
//					XMWanSavingManager manager = XMWanSavingManager.getInstance();
//					return manager.xmwanSaving(passport, channel, server, mianzhi, os);
//				}
				else if(smanager.equals("MSDKSavingManager")){
					MSDKSavingManager manager = MSDKSavingManager.getInstance();
					if(cardtype.contains("MSDK充值")){
						return manager.msdkSaving(passport, channel, server, mianzhi, os);
					}else if(cardtype.contains("MSDK兑换")){
						return manager.msdkExchange(passport, channel, server, mianzhi, os);
					}else if(cardtype.contains("米大师充值")){
						return manager.ysdkCharge(passport, channel, server, mianzhi, os);
					}else if(cardtype.contains("米大师兑换")){
						return manager.ysdkExchange(passport, channel, server, mianzhi, os);
					}
				}
				
				else {
					//再从cardSavingMap中获得充值接口
					smanager = cardSavingMap.get(cardtype);
					if(smanager == null) {
						smanager = "";
						logger.warn("[调用平台充值接口] [无匹配渠道串的Manager2] [用户名:"+passport.getUserName()+"] [充值类型:"+cardtype+"] [面值:"+mianzhi+"] [充值卡号:"+cardno+"] [充值密码:"+cardpass+"] [服务器名字:"+server+"] [渠道串:"+channel+"]");
					}
					//				if(smanager.equals("GameServerAliPaySavingManager")) {
					//					GameServerAliPaySavingManager savingManager = GameServerAliPaySavingManager.getInstance();
					//					return savingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, addAmount, otherDesp, server, callBackURL, channel, orderID);
					//				} else 
					if(smanager.equals("GameServerSzfSavingManager")) {
						GameServerSzfSavingManager savingManager = GameServerSzfSavingManager.getInstance();
						return savingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, otherDesp, server, channel, orderID);
					} else if(smanager.equals("GameServerYeepaySavingManager")) {
						GameServerYeepaySavingManager savingManager = GameServerYeepaySavingManager.getInstance();
						return savingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, otherDesp, server, channel, orderID);
					} 
					else if(smanager.equals("AlipayWebSavingManager")) {
						AlipayWebSavingManager sm = AlipayWebSavingManager.getInstance();
						return sm.aliSaving(passport, channel, server, mianzhi,  5, "", os);
					}
					else if(smanager.equals("AlipayWapSavingManager")) {
						AlipayWapSavingManager sm = AlipayWapSavingManager.getInstance();
						return sm.wapSaving(passport,mianzhi,server,os,channel);
					}
					//				else if(smanager.equals("AlipayWebSavingManager")) {
					//					AlipayWebSavingManager savingManager = AlipayWebSavingManager.getInstance();
					//					return savingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, addAmount, otherDesp, server, callBackURL, channel, orderID);
					//				} else if(smanager.equals("UnionWebSavingManager")) {
					//					UnionWebSavingManager savingManager = UnionWebSavingManager.getInstance();
					//					return savingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, addAmount, otherDesp, server, callBackURL, channel, orderID);
					//				}
				}
			} catch(Exception ex) {
				logger.error("[充值调用] [异常] [充值类型："+cardtype+"] [卡号:"+cardno+"] [面值:"+mianzhi+"] [用户:"+passport.getUserName()+"] ["+ex+"]",ex);
			}
		}
		return null;
	}



	public String cardSaving(Passport passport, String cardtype, int mianzhi, String cardno, String cardpass, 
			String server, String channel, String orderID, String otherDesp, String os,String[]others) {
		synchronized(passport) {
			try {
				//如果传入的卡号和密码包括中文 则去掉中文
				String reg = "[\u4E00-\u9FA5]+";
				if(cardno != null)
				{
					cardno = cardno.replaceAll(reg, "");
					cardno = cardno.trim();
				}

				if(cardpass != null)
				{
					cardpass = cardpass.replaceAll(reg, "");
					cardpass = cardpass.trim();
				}
logger.warn("[cardSaving] [channel:"+channel+"] [orderID:"+orderID+"] [cardtype:"+cardtype+"] [mianzhi:"+mianzhi+"] " +
		"[cardno:"+cardno+"] [cardpass:"+cardpass+"] [server:"+server+"] [otherDesp:"+otherDesp+"] [os:"+os+"] ["+(others==null?"null":Arrays.toString(others)+"]"));
				String smanager = channelSavingMap.get(channel);
				if(smanager == null) {
					smanager = "";
					logger.warn("[调用平台充值接口] [无匹配渠道串的Manager1] [用户名:"+passport.getUserName()+"] [充值类型:"+cardtype+"] [面值:"+mianzhi+"] [充值卡号:"+cardno+"] [充值密码:"+cardpass+"] [服务器名字:"+server+"] [渠道串:"+channel+"]");
				}
				if(smanager.equals("T185SDKSavingManager")){
					T185SDKSavingManager savingManager = T185SDKSavingManager.getInstance();
					return savingManager.sdkSaving(passport, server,mianzhi,channel, others);
				}else 
				if(smanager.equals("T6533SDKSavingManager")){
					T6533SDKSavingManager savingManager = T6533SDKSavingManager.getInstance();
					return savingManager.sdkSaving(passport, server,mianzhi,channel, others);
				}else if(smanager.equals("AiWanSavingManager")){
					AiWanSavingManager manager = AiWanSavingManager.getInstance();
					return manager.sdkSaving(passport, server, mianzhi, channel,others);
				}else if(smanager.equals("XMWanSavingManager")){
					XMWanSavingManager manager = XMWanSavingManager.getInstance();
					return manager.xmwanSaving(passport, channel, server, mianzhi, os,others);
				}else if(smanager.equals("GuoPanSavingManager")){
					return HuoGameSavingManager.getInstance().guopanCardSaving(passport,channel, server,mianzhi, os,others);
				}else if(smanager.equals("JiuZhouSavingManager")){
					return HuoGameSavingManager.getInstance().jiuzhouCardSaving(passport,channel, server,mianzhi, os,others);
				}else if(smanager.equals("HaoDongSavingManager")){
					return HuoGameSavingManager.getInstance().haoDongCardSaving(passport,channel, server,mianzhi, os,others);
				}else if(smanager.equals("HuoGameSavingManager")){
					return HuoGameSavingManager.getInstance().cardSaving(passport,channel, server,mianzhi, os,others);
				}else if(smanager.equals("HuiYaoSavingManager")){
					return HuiYaoSavingManager.getInstance().cardSaving(passport,channel, server,mianzhi, os,others);
				}else if(smanager.equals("U8SDKSavingManager")){
					return U8SDKSavingManager.getInstance().sdkSaving(passport, server,mianzhi, channel,others);
				}else if(smanager.equals("V8SDKSavingManager")){
					if(channel.contains("HUASHENGSDK_XUNXIAN")){//
						return V8SDKSavingManager.getInstance().sdkSaving(passport, server,mianzhi, channel,others);
					}else if(channel.contains("LEHAIHAIAPPSTORE_XUNXIAN")){//
						return V8SDKSavingManager.getInstance().sdkSaving2(passport, server,mianzhi, channel,others);
					}else if(channel.contains("ANJIUAPPSTORE_XUNXIAN")){
						return V8SDKSavingManager.getInstance().sdkSaving3(passport, server,mianzhi, channel,others);
					}else if(channel.contains("MAIYOUAPPSTORE_XUNXIAN")){
						return V8SDKSavingManager.getInstance().sdkSaving4(passport, server,mianzhi, channel,others);
					}else if(channel.contains("XIAO7APPSTORE_XUNXIAN")){//
						return V8SDKSavingManager.getInstance().sdkSaving5(passport, server,mianzhi, channel,others);
					}
				}else if(smanager.equals("YiJieSavingManager")){
					YiJieSavingManager savingManager = YiJieSavingManager.getInstance();
					return savingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, otherDesp, server, channel, orderID,others);
				}else if(smanager.equals("HuaweiYeepaySavingManager")){
					HuaweiYeepaySavingManager savingManager = HuaweiYeepaySavingManager.getInstance();
					return savingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, otherDesp, server, channel, orderID,others);
				}else if(smanager.equals("UCSDKSavingManager")){
					UCSDKSavingManager savingManager = UCSDKSavingManager.getInstance();
					return savingManager.sdkSaving(passport, cardno, server,os, channel,others);
				}else if(smanager.equals("MeiZuSavingManager")){
					MeiZuSavingManager savingManager = MeiZuSavingManager.getInstance();
					return savingManager.meizuSaving(passport, channel, server, mianzhi, os,others);
				}else if(smanager.equals("OppoSavingManager")){
					if(channel.contains("OPPOSDK_XUNXIAN")){
						OppoSavingManager savingManager = OppoSavingManager.getInstance();
						return savingManager.OPPOSaving(passport, channel, server, mianzhi, os,others);
					}else if(channel.contains("VIVOSDK_XUNXIAN")){
						OppoSavingManager savingManager = OppoSavingManager.getInstance();
						return savingManager.VIVOSaving(passport, channel, server, mianzhi, os,others);
					}
				}else if(smanager.equals("XiaomiSdkSavingManager")){
					XiaomiSdkSavingManager savingManager = XiaomiSdkSavingManager.getInstance();
					return savingManager.xiaomiSaving(passport,channel,server,mianzhi,os,others);
				}else if(smanager.equals("MSDKSavingManager")){
					MSDKSavingManager manager = MSDKSavingManager.getInstance();
					if(cardtype.contains("MSDK充值")){
						return manager.msdkSaving(passport, channel, server, mianzhi, os,others);
					}else if(cardtype.contains("MSDK兑换")){
						return manager.msdkExchange(passport, channel, server, mianzhi, os,others);
					}else if(cardtype.contains("米大师充值")){
						return manager.ysdkCharge(passport, channel, server, mianzhi, os,others);
					}else if(cardtype.contains("米大师兑换")){
						return manager.ysdkExchange(passport, channel, server, mianzhi, os,others);
					}
				}else {
					//再从cardSavingMap中获得充值接口
					smanager = cardSavingMap.get(cardtype);
					if(smanager == null) {
						smanager = "";
						logger.warn("[调用平台充值接口] [无匹配渠道串的Manager2] [用户名:"+passport.getUserName()+"] [充值类型:"+cardtype+"] [面值:"+mianzhi+"] [充值卡号:"+cardno+"] [充值密码:"+cardpass+"] [服务器名字:"+server+"] [渠道串:"+channel+"]");
					}else if(smanager.equals("HeePayManager")){
						return HeePayManager.getInstance().cardSaving(passport, channel, server,mianzhi, os, others);
					}else if(smanager.equals("GameServerSzfSavingManager")) {
						GameServerSzfSavingManager savingManager = GameServerSzfSavingManager.getInstance();
						return savingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, otherDesp, server, channel, orderID,others);
					} else if(smanager.equals("GameServerYeepaySavingManager")) {
						GameServerYeepaySavingManager savingManager = GameServerYeepaySavingManager.getInstance();
						return savingManager.cardSaving(passport, cardtype, mianzhi, cardno, cardpass, otherDesp, server, channel, orderID,others);
					}  else if(smanager.equals("AlipaySavingManager")) {
						AlipaySavingManager sm = AlipaySavingManager.getInstance();
						return sm.aliSaving(passport, channel, server, mianzhi, AlipaySavingManager.PAY_APP, "", os,others);
					}
				}
			} catch(Exception ex) {
				logger.error("[充值调用] [异常] [充值类型："+cardtype+"] [卡号:"+cardno+"] [面值:"+mianzhi+"] [用户:"+passport.getUserName()+"] ["+ex+"]",ex);
			}
		}
		return null;
	}
}
