package com.fy.engineserver.economic.charge;

import com.fy.engineserver.economic.charge.card.*;

public class ChargeModeFactory {

	private static ChargeModeFactory instance;

	private static Object lock = new Object();

	private ChargeModeFactory() {

	}

	public static ChargeModeFactory getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new ChargeModeFactory();
				}
			}
		}
		return instance;
	}

	public ChargeMode createEmptyChargeMode(String modeName) {
		if (modeName.equals("QQ充值")) {
			return new QQChargeMode(modeName);
		} else if (modeName.equals("支付宝")) {
			return new ZhiFuBaoChargeMode(modeName);
		} else if (modeName.equals("移动充值卡")) {
			return new CmccChargeMode(modeName);
		} else if (modeName.equals("联通一卡付")) {
			return new CuccChargeMode(modeName);
		} else if (modeName.equals("电信充值卡")) {
			return new CtcChargeMode(modeName);
		} else if (modeName.equals("盛大卡")) {
			return new ShengdaChargeMode(modeName);
		} else if (modeName.equals("征途卡")) {
			return new ZhengtuChargeMode(modeName);
		} else if (modeName.equals("骏网一卡通")) {
			return new JunwangChargeMode(modeName);
		} else if (modeName.equals("久游卡")) {
			return new JiuyouChargeMode(modeName);
		} else if (modeName.equals("网易卡")) {
			return new WangyiChargeMode(modeName);
		} else if (modeName.equals("完美卡")) {
			return new WanmeiChargeMode(modeName);
		} else if (modeName.equals("搜狐卡")) {
			return new SohuChargeMode(modeName);
		} else if (modeName.equals("Q币")) {
			return new QbiChargeMode(modeName);
		} else if (modeName.equals("腾讯神州付")) {
			return new TencentShenzhouChargeMode(modeName);
		} else if (modeName.equals("91豆")) {
			return new Jiu1DouChargeMode(modeName);
		} else if (modeName.equals("应用豆")) {
			return new AppChinaDouChargeMode(modeName);
		} else if (modeName.equals("UCWap支付")) {
			return new UCWapChargeMode(modeName);
		} else if (modeName.equals("乐逗")) {
			return new LeDouChargeMode(modeName);
		} else if (modeName.equals("APPSTORE")) {
			return new AppstoreChargeMode(modeName);
		} else if (modeName.equals("UCSDK充值")) {
			return new UCSDKChargeMode(modeName);
		} else if (modeName.equals("支付宝WAP充值")) {
			return new ZhiFuBaoWapChargeMode(modeName);
		} else if (modeName.equals("财付通WAP充值")) {
			return new CaiFuTongWapChargeMode(modeName);
		} else if (modeName.equals("PP易宝充值卡")) {
			return new PPYiBaoChargeMode(modeName);
		} else if (modeName.equals("PP移动充值卡")) {
			return new PPCmccChargeMode(modeName);
		} else if (modeName.equals("PP联通充值卡")) {
			return new PPCuccChargeMode(modeName);
		} else if (modeName.equals("PP电信充值卡")) {
			return new PPCtcChargeMode(modeName);
		} else if (modeName.equals("宝币")) {
			return new BaoBiChargeMode(modeName);
		} else if (modeName.equals("爱贝充值")) {
			return new AiBeiChargeMode(modeName);
		} else if (modeName.equals("同步推充值")) {
			return new TBTSDKChargeMode(modeName);
		} else if (modeName.equals("新浪微币")) {
			return new SinaVBChargeMode(modeName);
		} else if (modeName.equals("新浪支付宝")) {
			return new SinaZhiFuBaoChargeMode(modeName);
		} else if (modeName.equals("三星SDK充值")) {
			return new SamsungSDKChargeMode(modeName);
		} else if (modeName.equals("云游币")) {
			return new YunYouSDKChargeMode(modeName);
		} else if (modeName.equals("华为充值")) {
			return new HuaWeiSDKChargeMode(modeName);
		} else if (modeName.equals("天猫直充")) {
			return new TianMaoChargeMode(modeName);
		} else if (modeName.equals("米币")) {
			return new MiBiChargeMode(modeName);
		} else if (modeName.equals("AppStoreGuoji")) {
			return new AppstoreGuojiChargeMode(modeName);
		} else if (modeName.equals("奇虎360充值")) {
			return new QiHu360ChargeMode(modeName);
		} else if (modeName.equals("网页支付宝")) {
			return new WebZhifubaoChargeMode(modeName);
		} else if (modeName.equals("昆仑Android")) {
			return new KunlunAndroidChargeMode(modeName);
		} else if (modeName.equals("爱游充值")) {
			return new AiYouChargeMode(modeName);
		} else if (modeName.equals("KunlunAppStore")) {
			return new KunlunAppStoreChargeMode(modeName);
		} else if (modeName.equals("酷派充值")) {
			return new KupaiChargeMode(modeName);
		} else if (modeName.equals("番茄SDK充值")) {
			return new FanQieChargeMode(modeName);
		} else if (modeName.equals("同步推new支付宝")) {
			return new TBTNewZhifubaoChargeMode(modeName);
		} else if (modeName.equals("同步推new财付通")) {
			return new TBTNewCaifutongChargeMode(modeName);
		} else if (modeName.equals("同步推new神州付")) {
			return new TBTNewShenzhoufuChargeMode(modeName);
		} else if (modeName.equals("移动短信")) {
			return new CmccSMSChargeMode(modeName);
		} else if (modeName.equals("联想商店充值")) {
			return new LenovoSdkChargeMode(modeName);
		} else if (modeName.equals("亚马逊充值")) {
			return new AmazonChargeMode(modeName);
		} else if (modeName.equals("MalaiAppstore")) {
			return new MalaiAppstoreChargeMode(modeName);
		} else if (modeName.equals("MalaiAndroid")) {
			return new MalaiAndroidChargeMode(modeName);
		} else if (modeName.equals("机锋券充值")) {
			return new GfangSDKChargeMode(modeName);
		} else if (modeName.equals("3G充值")) {
			return new CMCC3GChargeMode(modeName);
		} else if (modeName.equals("安智充值")) {
			return new AnZhiChargeMode(modeName);
		} else if (modeName.equals("乐逗充值")) {
			return new LeDouChargeMode(modeName);
		} else if (modeName.equals("联想游戏充值")) {
			return new LianxiangYXChargeMode(modeName);
		} else if (modeName.equals("瓦币")) {
			return new LianxiangYXChargeMode(modeName);
		} else if (modeName.equals("KoreaGoogleplay")) {
			return new KoreaGoogleChargeMode(modeName);
		} else if (modeName.equals("KoreaAppstore")) {
			return new KoreaAppstoreChargeMode(modeName);
		} else if (modeName.equals("KoreaKT")) {
			return new KoreaKTChargeMode(modeName);
		} else if (modeName.equals("KoreaT-STORE")) {
			return new KoreaKStoreChargeMode(modeName);
		}
		else if(modeName.trim().equals("微游戏充值"))
		{
			return new SinaWeiNewChargeMode(modeName);
		}
		else if(modeName.trim().equals("GOOGLEINAPPBILLING"))
		{
			return new GooglePlayChargeMode(modeName);
		}
		else if(modeName.trim().equals("豌豆荚支付"))
		{
			return new WandoujiaSdkChargeMode(modeName);
		}
		else if (modeName.equals("沃商店支付")) {
			return new LiantongWoChargeMode(modeName);
		}
		else if (modeName.equals("汇元骏网卡")) {
			return new HuiYuanJunwangChargeMode(modeName);
		}
		else if (modeName.equals("搜狗充值")) {
			return new SoGouChargeMode(modeName);
		}
		else if (modeName.equals("百度SDK充值")) {
			return new BaiduSdkChargeMode(modeName);
		}
		else if (modeName.equals("Wap支付宝")) {
			return new WapZhiFuBaoChargeMode(modeName);
		}
		else if (modeName.equals("快用充值")) {
			return new KuaiYongChargeMode(modeName);
		}
		else if (modeName.equals("木蚂蚁充值")) {
			return new MuMayiChargeMode(modeName);
		}
		else if (modeName.equals("移动MM充值")) {
			return new CmccMMChargeMode(modeName);
		}
		else if (modeName.equals("魅族充值")) {
			return new MeizuChargeMode(modeName);
		}
		else if (modeName.equals("XYSDK充值")) {
			return new XYChargeMode(modeName);
		}
		
		else if (modeName.equals("KoreaNA")) {
			return new KoreaNAChargeMode(modeName);
		}
		else if (modeName.equals("多酷支付宝")) {
			return new DuokuZhiFuBaoChargeMode(modeName);
		}
		else if (modeName.equals("百度应用支付宝")) {
			return new BaiduYYZhiFuBaoChargeMode(modeName);
		}else if(modeName.equals("熊猫玩充值")){
			return new XMWanChargeMode(modeName);
		}else if(modeName.equals("MSDK充值")){
			return new MSDKChargeMode(modeName);
		}else if(modeName.equals("MSDK兑换")){
			return new MSDKChargeChangeMode(modeName);
 		}else if(modeName.equals("微信支付")){
			return new WeiXinChargeMode(modeName);
 		}else if(modeName.equals("靠谱充值")){
			return new KaoPuChargeMode(modeName);
 		}else if(modeName.equals("米大师充值")){
			return new YSDKChargeMode(modeName);
 		}else if(modeName.equals("米大师兑换")){
			return new YSDKChargeChangeMode(modeName);
 		}else if(modeName.equals("小米充值")){
			return new XiaoMiSDKChargeChangeMode(modeName);
 		}else if(modeName.equals("OPPO充值")){
			return new OppoSDKChargeChangeMode(modeName);
		}else if(modeName.equals("VIVO充值")){
			return new VivoSDKChargeChangeMode(modeName);
		}else if(modeName.equals("易接充值")){
			return new YiJieSDKChargeChangeMode(modeName);
	}else if(modeName.equals("U8充值")){
		return new U8SDKChargeChangeMode(modeName);
	}else if(modeName.equals("坚果充值")){
		return new JianGuoSDKChargeChangeMode(modeName);
	}else if(modeName.equals("其乐充值")){
		return new QiLeSDKChargeChangeMode(modeName);
	}else if(modeName.equals("花生充值")){
		return new HuaShengSDKChargeChangeMode(modeName);
	}else if(modeName.equals("花生充值")){
		return new HuaShengSDKChargeChangeMode(modeName);
	}else if(modeName.equals("浩动充值")){
		return new HaoDongSDKChargeChangeMode(modeName);
	}else if(modeName.equals("九州飘渺充值")){
		return new JiuZhouPiaoMiaoChargeChangeMode(modeName);
	}else if(modeName.equals("果盘充值")){
		return new GuoPanChargeChangeMode(modeName);
	}else if(modeName.equals("寻仙充值")){
		return new XunXianChargeChangeMode(modeName);
	}else if(modeName.equals("乐嗨嗨充值")){
		return new LeHaiHaiChargeChangeMode(modeName);
	}else if(modeName.equals("安久充值")){
		return new AnJiuChargeChangeMode(modeName);
	}else if(modeName.equals("麦游充值")){
		return new MaiYouChargeChangeMode(modeName);
	}else if(modeName.equals("小7充值")){
		return new Xiao7ChargeChangeMode(modeName);
	}else if(modeName.equals("爱玩充值")){
		return new AIWanChargeChangeMode(modeName);
	}else if(modeName.equals("仙剑充值")){
		return new JianXiaChargeChangeMode(modeName);
	}else if(modeName.equals("6533充值")){
		return new T6533SDKChargeMode(modeName);
	}else if(modeName.equals("185充值")){
		return new T185SDKChargeMode(modeName);
	}
		
		ChargeManager.logger.error("[非法的充值类型:" + modeName + "]");
		return null;
		
	}
}
