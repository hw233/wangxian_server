package com.sqage.stat.server;

public class  PayMoneyCost {

	//static Logger //logger = Logger.getLogger(PayMoneyCost.class);
	
	public static String getCost(String cardType,String type,long payMoney){
		  Float cost=0.0F;
		if("支付宝".equals(cardType)&&"支付宝".equals(type)){ 
			    cost=payMoney*0.03f;
			    //logger.info("cardType:"+cardType+",type:"+type+",支付金额:"+payMoney+",手续费率:0.03"+",手续费金额:"+cost);
			} else if("Wap支付宝".equals(cardType)&&"Wap支付宝".equals(type)){ 
		       cost=payMoney*0.03f;
		    } else if ("电信充值卡".equals(cardType)&&"神州付".equals(type)){
				cost=payMoney*0.03f;
				//logger.info("cardType:"+cardType+",type:"+type+",支付金额:"+payMoney+",手续费率:0.03"+",手续费金额:"+cost);
			} else if("网站充值".equals(type)){
				cost=payMoney*0.005f;
			} else if ("久游卡".equals(cardType)&&"易宝".equals(type)){
				cost=payMoney*0.25f;
				//logger.info("cardType:"+cardType+",type:"+type+",支付金额:"+payMoney+",手续费率:0.25"+",手续费金额:"+cost);
			} else if ("骏网一卡通".equals(cardType)&&"易宝".equals(type)){
				cost=payMoney*0.18f;
				//logger.info("cardType:"+cardType+",type:"+type+",支付金额:"+payMoney+",手续费率:0.18"+",手续费金额:"+cost);
			} else if ("联通一卡付".equals(cardType)&&"神州付".equals(type)){
				cost=payMoney*0.03f;
				//logger.info("cardType:"+cardType+",type:"+type+",支付金额:"+payMoney+",手续费率:0.03"+",手续费金额:"+cost);
			} else if ("盛大卡".equals(cardType)&&"易宝".equals(type)){
				cost=payMoney*0.2f;
				//logger.info("cardType:"+cardType+",type:"+type+",支付金额:"+payMoney+",手续费率:0.2"+",手续费金额:"+cost);
			} else if ("搜狐卡".equals(cardType)&&"易宝".equals(type)){
				cost=payMoney*0.25f;
				//logger.info("cardType:"+cardType+",type:"+type+",支付金额:"+payMoney+",手续费率:0.25"+",手续费金额:"+cost);
			} else if ("完美卡".equals(cardType)&&"易宝".equals(type)){
				cost=payMoney*0.25f;
				//logger.info("cardType:"+cardType+",type:"+type+",支付金额:"+payMoney+",手续费率:0.25"+",手续费金额:"+cost);
			} else if ("网易卡".equals(cardType)&&"易宝".equals(type)){
				cost=payMoney*0.25f;
				//logger.info("cardType:"+cardType+",type:"+type+",支付金额:"+payMoney+",手续费率:0.25"+",手续费金额:"+cost);
			} else if ("移动充值卡".equals(cardType)&&"神州付".equals(type)){
				cost=payMoney*0.03f;
				//logger.info("cardType:"+cardType+",type:"+type+",支付金额:"+payMoney+",手续费率:0.03"+",手续费金额:"+cost);
			} else if ("征途卡".equals(cardType)&&"易宝".equals(type)){
				cost=payMoney*0.2f;
				//logger.info("cardType:"+cardType+",type:"+type+",支付金额:"+payMoney+",手续费率:0.2"+",手续费金额:"+cost);
			}
		return cost.toString();
	}
}
