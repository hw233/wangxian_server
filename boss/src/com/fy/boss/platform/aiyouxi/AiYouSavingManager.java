package com.fy.boss.platform.aiyouxi;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.platform.aiyouxi.AiYouSavingManager;
import com.xuanzhi.tools.text.DateUtil;

public class AiYouSavingManager {

	protected static AiYouSavingManager m_self = null;
	
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
	public String aiyouSaving(Passport passport,  int addAmount, String servername,String os, String channel, String playerId ) {
		long t = System.currentTimeMillis();

		String medinfo = "充值额:" + addAmount;
		String mediumName = "爱游充值";

		try {
			// 先生成一个订单
			OrderForm order = new OrderForm();

			java.util.Date cdate = new java.util.Date();

			order.setCreateTime(t);
			order.setPassportId(passport.getId());
			order.setServerName(servername);

			order.setSavingPlatform("爱游戏");

			order.setSavingMedium(mediumName);
			order.setMediumInfo(medinfo);
			order.setPayMoney(new Long(addAmount));
			order.setHandleResult(OrderForm.HANDLE_RESULT_NOBACK);
			order.setUserChannel(channel);
			order.setMemo1(os);
			order = OrderFormManager.getInstance().createOrderForm(order);

			long id = order.getId();
			String orderID = "ay-"  + DateUtil.formatDate(cdate, "yyyyMMdd") + "-" + id;
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
			PlatformSavingCenter.logger.info("[充值调用] [产生订单失败] [支付平台:爱游戏] [方式:" + mediumName + "] [用户:" + passport.getUserName() + "] [amount:" + addAmount
					+ "分] [订单号:-----]", e);
		}
		return "";
	}
	
	
	public String aiyouSaving(Passport passport,  int addAmount, String servername,String os, String channel, String playerId ,String[] others) {
		long t = System.currentTimeMillis();

		String medinfo = "充值额:" + addAmount;
		String mediumName = "爱游充值";

		try {
			// 先生成一个订单
			OrderForm order = new OrderForm();

			java.util.Date cdate = new java.util.Date();

			order.setCreateTime(t);
			order.setPassportId(passport.getId());
			order.setServerName(servername);

			order.setSavingPlatform("爱游戏");

			order.setSavingMedium(mediumName);
			order.setMediumInfo(medinfo);
			order.setPayMoney(new Long(addAmount));
			order.setHandleResult(OrderForm.HANDLE_RESULT_NOBACK);
			order.setUserChannel(channel);
			order.setMemo1(others[0]);
			if(others.length > 1){
				order.setChargeValue(others[1]);
			}
			if(others.length > 2){
				order.setChargeType(others[2]);
			}
			order = OrderFormManager.getInstance().createOrderForm(order);

			long id = order.getId();
			String orderID = "ay-"  + DateUtil.formatDate(cdate, "yyyyMMdd") + "-" + id;
			order.setOrderId(orderID);
			OrderFormManager.getInstance().update(order);

			if (order.getId() > 0 && order.getOrderId() != null) {
				PlatformSavingCenter.logger.info("[充值调用] [产生订单成功] [支付平台:" + order.getSavingPlatform() + "] [方式:" + order.getSavingMedium() + "] [用户:"
						+ passport.getUserName() + "] [amount:" + order.getPayMoney() + "分] [订单号:" + orderID + "] [os:"+os+"] [角色id:"+others[0]+"]");
				return orderID;
			} else {
				PlatformSavingCenter.logger.info("[充值调用] [产生订单失败] [支付平台:" + order.getSavingPlatform() + "] [方式:" + order.getSavingMedium() + "] [用户:"
						+ passport.getUserName() + "] [amount:" + order.getPayMoney() + "分] [订单号:" + orderID + "] [os:"+os+"] [角色id:"+others[0]+"]");
				return "";
			}
		} catch (Exception e) {
			PlatformSavingCenter.logger.info("[充值调用] [产生订单失败] [支付平台:爱游戏] [方式:" + mediumName + "] [用户:" + passport.getUserName() + "] [amount:" + addAmount
					+ "分] [订单号:-----] [角色id:"+others[0]+"]", e);
		}
		return "";
	}

	public static AiYouSavingManager getInstance() {
		if(m_self == null)
		{
			m_self = new AiYouSavingManager();
		}
		return m_self;
	}
}
