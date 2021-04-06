package com.fy.boss.platform.yunyou;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bonc.client.game.GamePay;
import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.platform.yunyou.YunyouSdkSavingManager;
import com.xuanzhi.stat.chart.AmLineChartData;
import com.xuanzhi.tools.text.DateUtil;

public class YunyouSdkSavingManager {

	protected static YunyouSdkSavingManager m_self = null;
	
	private static Map<String,String> infoMap = new LinkedHashMap<String,String>();
	
	public void initialize() throws Exception {
		m_self = this;
		System.out.println("[" + this.getClass().getName() + "] [initialized]");
	}
	static
	{
		infoMap.put("2000", "登陆成功");
		infoMap.put("2001", "支付失败，账户余额不足");
		infoMap.put("2002", "支付失败，扣费发生异常");
		infoMap.put("2003", "支付失败，本地验证失败");
		infoMap.put("2004", "用户未登陆或用户信息不存在");
		infoMap.put("2005", "token 无效，订单验证失败");
		infoMap.put("2006", "支付类型无效");
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
	public String yunyouSdkSaving(Passport passport, String sid,String channel, String servername, int addAmount, String os) {
		long t = System.currentTimeMillis();

		String medinfo = "充值额:" + addAmount;
		String mediumName = "云游币";

		try {
			// 先生成一个订单
			OrderForm order = new OrderForm();

			java.util.Date cdate = new java.util.Date();

			order.setCreateTime(t);
			order.setPassportId(passport.getId());
			order.setServerName(servername);

			order.setSavingPlatform("YUNYOUSDK");


			order.setSavingMedium(mediumName);
			order.setMediumInfo(medinfo);
			order.setPayMoney(new Long(addAmount));
			order.setHandleResult(OrderForm.HANDLE_RESULT_NOBACK);
			order.setUserChannel(channel);
			order.setMemo1(os);
			
			order.setResponseResult(OrderForm.RESPONSE_NOBACK);
			//设置是否通知游戏服状态 设为false
			order.setNotified(false);
			//设置通知游戏服是否成功 设为false
			order.setNotifySucc(false);
			
			order = OrderFormManager.getInstance().createOrderForm(order);

			long id = order.getId();
			String orderID = "yy-"  + DateUtil.formatDate(cdate, "yyyyMMddHH") + "-" + id;
			order.setOrderId(orderID);
			OrderFormManager.getInstance().update(order);

			if (order.getId() > 0 && order.getOrderId() != null) {
				
				GamePay gamePay = new GamePay();
				String channelOrderid = gamePay.getToken(sid, 1+"", addAmount+"");
				
				if(!StringUtils.isEmpty(channelOrderid))
				{
					order.setChannelOrderId(channelOrderid);
					boolean isValid = gamePay.order(channelOrderid);
					if(isValid)
					{
						if(gamePay.confimOrder("", channelOrderid, ""))
						{
							//更新订单信息为成功
							order.setResponseTime(System.currentTimeMillis());
							order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
							order.setResponseResult(OrderForm.RESPONSE_SUCC);
							order.setPayMoney(addAmount+0l);
							order.setChannelOrderId(channelOrderid);
							OrderFormManager.getInstance().update(order);
							PlatformSavingCenter.logger.info("[充值调用] [支付成功] [支付平台:" + order.getSavingPlatform() + "] [方式:" + order.getSavingMedium() + "] [用户:"
									+ passport.getUserName() + "] [amount:" + order.getPayMoney() + "分] [订单号:" + orderID + "] [os:"+os+"] [渠道订单号:"+channelOrderid+"] [服务器名称:"+servername+"] [sid:"+sid+"] [cost:"+(System.currentTimeMillis()-t)+"ms]");
							return "支付成功！";
						}
						else
						{
							//更新订单信息为失败 
							String payCode = gamePay.getPayCode();
							order.setResponseTime(System.currentTimeMillis());
							order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
							order.setResponseResult(OrderForm.RESPONSE_FAILED);
							order.setPayMoney(addAmount+0l);
							order.setChannelOrderId(channelOrderid);
							OrderFormManager.getInstance().update(order);
							PlatformSavingCenter.logger.info("[充值调用] [支付失败] [支付平台:" + order.getSavingPlatform() + "] [方式:" + order.getSavingMedium() + "] [用户:"
									+ passport.getUserName() + "] [amount:" + order.getPayMoney() + "分] [订单号:" + orderID + "] [os:"+os+"] [渠道订单号:"+channelOrderid+"] [payCode:"+payCode+"] [错误描述:"+infoMap.get(payCode)+"] [服务器名称:"+servername+"] [sid:"+sid+"] [cost:"+(System.currentTimeMillis()-t)+"ms]");
							return infoMap.get(payCode);
						}
					}
					else
					{
						//更新订单信息为失败 订单号非法
						//更新订单信息为失败 
						order.setResponseTime(System.currentTimeMillis());
						order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
						order.setResponseResult(OrderForm.RESPONSE_FAILED);
						order.setPayMoney(addAmount+0l);
						order.setChannelOrderId(channelOrderid);
						OrderFormManager.getInstance().update(order);
						PlatformSavingCenter.logger.info("[充值调用] [支付失败] [支付平台:" + order.getSavingPlatform() + "] [方式:" + order.getSavingMedium() + "] [用户:"
								+ passport.getUserName() + "] [amount:" + order.getPayMoney() + "分] [订单号:" + orderID + "] [os:"+os+"] [渠道订单号:"+channelOrderid+"] [错误描述:渠道产生的订单号验证非法] [服务器名称:"+servername+"] [sid:"+sid+"] [cost:"+(System.currentTimeMillis()-t)+"ms]");
						return "支付失败，订单号非法!";
					}
				}
				
			} else {
				order.setResponseTime(System.currentTimeMillis());
				order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
				order.setResponseResult(OrderForm.RESPONSE_FAILED);
				order.setPayMoney(addAmount+0l);
				OrderFormManager.getInstance().update(order);
				PlatformSavingCenter.logger.info("[充值调用] [支付失败] [产生订单失败] [支付平台:YUNYOUSDK] [方式:"+mediumName+"] [用户:"
						+ passport.getUserName() + "] [amount:" + addAmount + "分] [订单号:" + orderID + "] [os:"+os+"] [渠道:"+channel+"] [服务器名称:"+servername+"] [sid:"+sid+"] [cost:"+(System.currentTimeMillis()-t)+"ms]");
				return "";
			}
		} catch (Exception e) {
			PlatformSavingCenter.logger.info("[充值调用] [支付失败] [出现异常] [支付平台:YUNYOUSDK] [方式:" + mediumName + "] [用户:" + passport.getUserName() + "] [amount:" + addAmount
					+ "分] [订单号:-----] [服务器名称:"+servername+"] [sid:"+sid+"] [cost:"+(System.currentTimeMillis()-t)+"ms]", e);
		}
		return "";
	}
	
	
	public String yunyouSdkSaving(Passport passport, String sid,String channel, String servername, int addAmount, String os,String[]others) {
		long t = System.currentTimeMillis();

		String medinfo = "充值额:" + addAmount;
		String mediumName = "云游币";

		try {
			// 先生成一个订单
			OrderForm order = new OrderForm();

			java.util.Date cdate = new java.util.Date();

			order.setCreateTime(t);
			order.setPassportId(passport.getId());
			order.setServerName(servername);

			order.setSavingPlatform("YUNYOUSDK");


			order.setSavingMedium(mediumName);
			order.setMediumInfo(medinfo);
			order.setPayMoney(new Long(addAmount));
			order.setHandleResult(OrderForm.HANDLE_RESULT_NOBACK);
			order.setUserChannel(channel);
			order.setMemo1(others[0]);
			
			order.setResponseResult(OrderForm.RESPONSE_NOBACK);
			//设置是否通知游戏服状态 设为false
			order.setNotified(false);
			//设置通知游戏服是否成功 设为false
			order.setNotifySucc(false);
			if(others.length > 1){
				order.setChargeValue(others[1]);
			}
			if(others.length > 2){
				order.setChargeType(others[2]);
			}
			order = OrderFormManager.getInstance().createOrderForm(order);

			long id = order.getId();
			String orderID = "yy-"  + DateUtil.formatDate(cdate, "yyyyMMddHH") + "-" + id;
			order.setOrderId(orderID);
			OrderFormManager.getInstance().update(order);

			if (order.getId() > 0 && order.getOrderId() != null) {
				
				GamePay gamePay = new GamePay();
				String channelOrderid = gamePay.getToken(sid, 1+"", addAmount+"");
				
				if(!StringUtils.isEmpty(channelOrderid))
				{
					order.setChannelOrderId(channelOrderid);
					boolean isValid = gamePay.order(channelOrderid);
					if(isValid)
					{
						if(gamePay.confimOrder("", channelOrderid, ""))
						{
							//更新订单信息为成功
							order.setResponseTime(System.currentTimeMillis());
							order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
							order.setResponseResult(OrderForm.RESPONSE_SUCC);
							order.setPayMoney(addAmount+0l);
							order.setChannelOrderId(channelOrderid);
							OrderFormManager.getInstance().update(order);
							PlatformSavingCenter.logger.info("[充值调用] [支付成功] [支付平台:" + order.getSavingPlatform() + "] [方式:" + order.getSavingMedium() + "] [用户:"
									+ passport.getUserName() + "] [amount:" + order.getPayMoney() + "分] [订单号:" + orderID + "] [os:"+os+"] [渠道订单号:"+channelOrderid+"] [服务器名称:"+servername+"] [sid:"+sid+"] [角色id:"+others[0]+"] [others:"+(others==null?"nul":Arrays.toString(others))+"] [cost:"+(System.currentTimeMillis()-t)+"ms]");
							return "支付成功！";
						}
						else
						{
							//更新订单信息为失败 
							String payCode = gamePay.getPayCode();
							order.setResponseTime(System.currentTimeMillis());
							order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
							order.setResponseResult(OrderForm.RESPONSE_FAILED);
							order.setPayMoney(addAmount+0l);
							order.setChannelOrderId(channelOrderid);
							OrderFormManager.getInstance().update(order);
							PlatformSavingCenter.logger.info("[充值调用] [支付失败] [支付平台:" + order.getSavingPlatform() + "] [方式:" + order.getSavingMedium() + "] [用户:"
									+ passport.getUserName() + "] [amount:" + order.getPayMoney() + "分] [订单号:" + orderID + "] [os:"+os+"] [渠道订单号:"+channelOrderid+"] [payCode:"+payCode+"] [错误描述:"+infoMap.get(payCode)+"] [服务器名称:"+servername+"] [sid:"+sid+"] [角色id:"+others[0]+"] [others:"+(others==null?"nul":Arrays.toString(others))+"] [cost:"+(System.currentTimeMillis()-t)+"ms]");
							return infoMap.get(payCode);
						}
					}
					else
					{
						//更新订单信息为失败 订单号非法
						//更新订单信息为失败 
						order.setResponseTime(System.currentTimeMillis());
						order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
						order.setResponseResult(OrderForm.RESPONSE_FAILED);
						order.setPayMoney(addAmount+0l);
						order.setChannelOrderId(channelOrderid);
						OrderFormManager.getInstance().update(order);
						PlatformSavingCenter.logger.info("[充值调用] [支付失败] [支付平台:" + order.getSavingPlatform() + "] [方式:" + order.getSavingMedium() + "] [用户:"
								+ passport.getUserName() + "] [amount:" + order.getPayMoney() + "分] [订单号:" + orderID + "] [os:"+os+"] [渠道订单号:"+channelOrderid+"] [错误描述:渠道产生的订单号验证非法] [服务器名称:"+servername+"] [sid:"+sid+"] [角色id:"+others[0]+"] [others:"+(others==null?"nul":Arrays.toString(others))+"] [cost:"+(System.currentTimeMillis()-t)+"ms]");
						return "支付失败，订单号非法!";
					}
				}
				
			} else {
				order.setResponseTime(System.currentTimeMillis());
				order.setHandleResult(OrderForm.HANDLE_RESULT_FAILED);
				order.setResponseResult(OrderForm.RESPONSE_FAILED);
				order.setPayMoney(addAmount+0l);
				OrderFormManager.getInstance().update(order);
				PlatformSavingCenter.logger.info("[充值调用] [支付失败] [产生订单失败] [支付平台:YUNYOUSDK] [方式:"+mediumName+"] [用户:"
						+ passport.getUserName() + "] [amount:" + addAmount + "分] [订单号:" + orderID + "] [os:"+os+"] [渠道:"+channel+"] [服务器名称:"+servername+"] [sid:"+sid+"] [角色id:"+others[0]+"] [others:"+(others==null?"nul":Arrays.toString(others))+"] [cost:"+(System.currentTimeMillis()-t)+"ms]");
				return "";
			}
		} catch (Exception e) {
			PlatformSavingCenter.logger.info("[充值调用] [支付失败] [出现异常] [支付平台:YUNYOUSDK] [方式:" + mediumName + "] [用户:" + passport.getUserName() + "] [amount:" + addAmount
					+ "分] [订单号:-----] [服务器名称:"+servername+"] [sid:"+sid+"] [角色id:"+others[0]+"] [others:"+(others==null?"nul":Arrays.toString(others))+"] [cost:"+(System.currentTimeMillis()-t)+"ms]", e);
		}
		return "";
	}
	
	

	public static synchronized YunyouSdkSavingManager getInstance() {
		if(m_self == null)
		{
			m_self = new YunyouSdkSavingManager();
		}
		return m_self;
	}
}
