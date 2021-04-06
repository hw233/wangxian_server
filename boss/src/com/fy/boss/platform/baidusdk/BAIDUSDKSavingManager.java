package com.fy.boss.platform.baidusdk;

import org.apache.commons.lang.StringUtils;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.platform.baidusdk.BAIDUSDKSavingManager;
import com.xuanzhi.tools.text.DateUtil;

public class BAIDUSDKSavingManager {

	protected static BAIDUSDKSavingManager m_self = null;
	
	public void initialize() throws Exception {
		m_self = this;
		System.out.println("[" + this.getClass().getName() + "] [initialized]");
	}

	/**
	 * 支付宝充值
	 * 
	 * @param passport
	 * @param channel
	 * @param servername
	 * @param addAmount 分
	 * @param addType
	 *            充值方式： 1-支付宝， 5-页面支付， 2-wap支付
	 * @param callbackUrl
	 *            页面支付或wap支付的回调页面
	 * @return
	 */
	public String cardSaving(Passport passport,  int addAmount, String servername,String os, String channel ) {
		long t = System.currentTimeMillis();

		String medinfo = "充值额:" + addAmount;
		String mediumName = "百度SDK充值";

		try {
			// 先生成一个订单
			OrderForm order = new OrderForm();

			java.util.Date cdate = new java.util.Date();

			order.setCreateTime(t);
			order.setPassportId(passport.getId());
			order.setServerName(servername);

			if(channel != null && channel.equalsIgnoreCase("newbaidusdk_mieshi")){
				order.setSavingPlatform("NEWBAIDUSDK");
			}else{
				order.setSavingPlatform("BAIDUSDK");
			}

			order.setSavingMedium(mediumName);
			order.setMediumInfo(medinfo);
			order.setPayMoney(new Long(addAmount));
			order.setHandleResult(OrderForm.HANDLE_RESULT_NOBACK);
			order.setUserChannel(channel);
			order.setMemo1(os);
			order = OrderFormManager.getInstance().createOrderForm(order);

			long id = order.getId();
			String orderID = "baidusdk-"  + DateUtil.formatDate(cdate, "yyyyMMddHH") + "-" + id;
			order.setOrderId(orderID);
			OrderFormManager.getInstance().update(order);

			if (order.getId() > 0 && order.getOrderId() != null) {
				PlatformSavingCenter.logger.info("[充值调用] [产生订单成功] [支付平台:" + order.getSavingPlatform() + "] [方式:" + order.getSavingMedium() + "] [用户:"
						+ passport.getUserName() + "] [amount:" + order.getPayMoney() + "分] [订单号:" + orderID + "] [os:"+os+"]");
				return orderID;
			} else {
				PlatformSavingCenter.logger.info("[充值调用] [产生订单失败] [支付平台:" + order.getSavingPlatform() + "] [方式:" + order.getSavingMedium() + "] [用户:"
						+ passport.getUserName() + "] [amount:" + order.getPayMoney() + "分] [订单号:" + orderID + "] [os:"+os+"]");
				return "";
			}
		} catch (Exception e) {
			PlatformSavingCenter.logger.info("[充值调用] [产生订单失败] [支付平台:BAIDUSDK] [方式:" + mediumName + "] [用户:" + passport.getUserName() + "] [amount:" + addAmount
					+ "分] [订单号:-----]", e);
		}
		return "";
	}
	
	
	public String cardSaving(Passport passport,  int addAmount, String servername,String os, String channel, String[] others) {
		String orderId = cardSaving(passport, addAmount, servername, os, channel);
		
		if(!StringUtils.isEmpty(orderId))
		{
			OrderForm orderForm = OrderFormManager.getInstance().getOrderForm(orderId);
			
			if(orderForm != null)
			{
				try
				{
					if(others.length > 1){
						orderForm.setChargeValue(others[1]);
					}
					if(others.length > 2){
						orderForm.setChargeType(others[2]);
					}
					orderForm.setMemo1(others[0]);
					OrderFormManager.getInstance().update(orderForm);
				}
				catch(Exception e)
				{
					PlatformSavingCenter.logger.info("[充值调用] [更新订单失败] [支付平台:BAIDUSDK] [用户:" + passport.getUserName() + "] [amount:" + addAmount
							+ "分] [订单号:"+orderId+"] [服务器名称:"+servername+"] [playerID:"+others[0]+"]", e);
				}
			}
		}
		
		return orderId;
		
	}

	public static BAIDUSDKSavingManager getInstance() {
		if(m_self == null)
		{
			m_self = new BAIDUSDKSavingManager();
		}
		return m_self;
	}
}
