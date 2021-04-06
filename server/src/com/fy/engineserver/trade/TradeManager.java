package com.fy.engineserver.trade;

import com.fy.engineserver.datasource.language.Translate;


public class TradeManager {

	/** 交易状态-无 */
	public static final byte TRADE_STATE_NO = 0;
	/** 交易状态-摆摊 */
	public static final byte TRADE_STATE_BOOOTH = 1;
	/** 交易状态-摆摊追售 */
	public static final byte TRADE_STATE_RESALE = 2;
	/** 交易状态-交易 */
	public static final byte TRADE_STATE_TRADE = 3;

	
	public static String putMoneyToMyText(long money){
		StringBuffer re = new StringBuffer();
		int ding = (int)(money/1000/1000);
		int lian = (int)(money/1000%1000);
		int wen = (int)(money%1000);
		
		if (ding > 0) {
			re.append(ding).append("<f color='0x00ff00'>"+Translate.锭+"</f>");
		}
		
		if (ding > 0 || lian > 0) {
			re.append(lian).append("<f color='0x40ff40'>"+Translate.两+"</f>");
		}
		re.append(wen).append("<f color='0x80ff80'>"+Translate.文+"</f>");
		
		return re.toString();
	}
}
