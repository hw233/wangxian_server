package com.fy.engineserver.economic;

import com.fy.engineserver.datasource.language.Translate;

public class CurrencyType {
	public static final int RMB_YUANBAO = 100;

	public static final int BIND_YINZI = 0;

	public static final int YINZI = 1;

	public static final int GONGZI = 2;

	public static final int ZIYUAN = 3;

	public static final int BANGPAIZIJIN = 4;

	public static final int LILIAN = 5;

	public static final int GONGXUN = 6;

	public static final int WENCAI = 7;
	public static final int ARTICLES = 8;

	public static final int SHOPYINZI = 9;

	public static final int ACTIVENESS = 10;
	
	public static final int JIFEN = 11;
	
	public static final int YUANQI = 12;
	
	public static final int XUESHI = 13;
	public static final int CHONGZHI = 14;
	public static final int CROSS_POINT = 16;

	public static void main(String[] args) {
		System.out.println(CURRENCY_DESP[14]);
	}
	public static final String CURRENCY_DESP[] = new String[] { Translate.绑定银子, Translate.非绑定银子, Translate.工资, Translate.资源, Translate.帮派资金, Translate.历练, Translate.功勋, Translate.文采, Translate.物品兑换, Translate.商店银子, Translate.活跃度 , Translate.text_2351, Translate.元气,Translate.血石,Translate.充值,"","跨服积分"};

	public static boolean isValidCurrencyType(int currencyType) {
		if (currencyType != YUANQI && currencyType != YINZI && currencyType != SHOPYINZI && currencyType != BIND_YINZI && currencyType != GONGZI && currencyType != ZIYUAN && currencyType != BANGPAIZIJIN && currencyType != ARTICLES && currencyType != LILIAN && currencyType != GONGXUN && currencyType != WENCAI && currencyType != ACTIVENESS&& currencyType != JIFEN&& currencyType != XUESHI&& currencyType != CHONGZHI&& currencyType != CROSS_POINT) {
			return false;
		}
		return true;
	}

	public static String getCurrencyDesp(int currencyType) {
		try {
			return CURRENCY_DESP[currencyType];
		} catch (Exception e) {
			return Translate.未知;
		}
	}
}
