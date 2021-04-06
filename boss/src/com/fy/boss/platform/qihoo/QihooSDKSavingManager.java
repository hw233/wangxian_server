package com.fy.boss.platform.qihoo;

import java.util.Date;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.platform.qihoo.QihooSDKSavingManager;
import com.xuanzhi.tools.text.DateUtil;

public class QihooSDKSavingManager {

	protected static QihooSDKSavingManager m_self = null;
	
	public void initialize() throws Exception {
		m_self = this;
		System.out.println("[" + this.getClass().getName() + "] [initialized]");
	}
	
//	public  String callbackUrl = "http://124.248.40.21:9110/mieshi_game_boss/qihooResult";
	public  String callbackUrl = "http://116.213.192.216:9110/mieshi_game_boss/qihooResult";

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
	public String qihooSaving(Passport passport, String channel, String servername, int addAmount, String os) {
		long startTime = System.currentTimeMillis();
		String savingMediumName = "奇虎360充值";
		try {
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			//生成订单
			OrderForm order = new OrderForm();
			order.setCreateTime(new Date().getTime());
			//设置充值平台
			order.setSavingPlatform("360SDK");
			order.setSavingMedium(savingMediumName);
			//设置充值人
			order.setPassportId(passport.getId());
			//设置消费金额
			order.setPayMoney( new Long(addAmount));
			//设置serverid
			order.setServerName(servername);
			order.setHandleResult(OrderForm.HANDLE_RESULT_NOBACK);
			order.setHandleResultDesp("新生成订单");
			//设置订单responseResult
			order.setResponseResult(OrderForm.RESPONSE_NOBACK);
			order.setResponseDesp("新生成订单");
			//设置是否通知游戏服状态 设为false
			order.setNotified(false);
			//设置通知游戏服是否成功 设为false
			order.setNotifySucc(false);
			order.setUserChannel(channel);
			order.setMemo1(os);
			//先存入到数据库中
			order = orderFormManager.createOrderForm(order);
			//设置订单号
			if(channel.toLowerCase().contains("360newsdk"))
			{
				order.setOrderId("qinew"+"-"+DateUtil.formatDate(new Date(), "yyyyMMdd")  + "-"+ order.getId());
			}
			else
			{
				order.setOrderId("qihoo"+"-"+DateUtil.formatDate(new Date(), "yyyyMMdd")  + "-"+ order.getId());
			}
				
			//orderFormManager.getEm().notifyFieldChange(order, "orderId");
			//orderFormManager.updateOrderForm(order);
			orderFormManager.updateOrderForm(order, "orderId");
			if (order.getId() > 0 && order.getOrderId() != null) 
			{
				if(PlatformSavingCenter.logger.isInfoEnabled())
					PlatformSavingCenter.logger.info("[360SDK充值订单生成] [成功] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:360SDK] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return order.getOrderId()+"@"+callbackUrl;
			}
			else
			{
				PlatformSavingCenter.logger.error("[360SDK充值订单生成] [失败] [订单格式不合法] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:360SDK] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return null;
			}
		
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[360SDK充值订单生成] [失败] [生成订单出现异常] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:360SDK] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			return null;
		} 
	}
	
	
	public String qihooSaving(Passport passport, String channel, String servername, int addAmount, String os,String[]others) {
		long startTime = System.currentTimeMillis();
		String savingMediumName = "奇虎360充值";
		try {
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			//生成订单
			OrderForm order = new OrderForm();
			order.setCreateTime(new Date().getTime());
			//设置充值平台
			order.setSavingPlatform("360SDK");
			order.setSavingMedium(savingMediumName);
			//设置充值人
			order.setPassportId(passport.getId());
			//设置消费金额
			order.setPayMoney( new Long(addAmount));
			//设置serverid
			order.setServerName(servername);
			order.setHandleResult(OrderForm.HANDLE_RESULT_NOBACK);
			order.setHandleResultDesp("新生成订单");
			//设置订单responseResult
			order.setResponseResult(OrderForm.RESPONSE_NOBACK);
			order.setResponseDesp("新生成订单");
			//设置是否通知游戏服状态 设为false
			order.setNotified(false);
			//设置通知游戏服是否成功 设为false
			order.setNotifySucc(false);
			order.setUserChannel(channel);
			order.setMemo1(others[0]);
			if(others.length > 1){
				order.setChargeValue(others[1]);
			}
			if(others.length > 2){
				order.setChargeType(others[2]);
			}
			//先存入到数据库中
			order = orderFormManager.createOrderForm(order);
			//设置订单号
			//设置订单号
			if(channel.toLowerCase().contains("360newsdk"))
			{
				order.setOrderId("qinew"+"-"+DateUtil.formatDate(new Date(), "yyyyMMdd")  + "-"+ order.getId());
			}
			else
			{
				order.setOrderId("qihoo"+"-"+DateUtil.formatDate(new Date(), "yyyyMMdd")  + "-"+ order.getId());
			}
			//orderFormManager.getEm().notifyFieldChange(order, "orderId");
			//orderFormManager.updateOrderForm(order);
			orderFormManager.updateOrderForm(order, "orderId");
			if (order.getId() > 0 && order.getOrderId() != null) 
			{
				if(PlatformSavingCenter.logger.isInfoEnabled())
					PlatformSavingCenter.logger.info("[360SDK充值订单生成] [成功] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:360SDK] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return order.getOrderId()+"@"+callbackUrl;
			}
			else
			{
				PlatformSavingCenter.logger.error("[360SDK充值订单生成] [失败] [订单格式不合法] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:360SDK] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return null;
			}
		
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[360SDK充值订单生成] [失败] [生成订单出现异常] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:360SDK] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			return null;
		} 
	}
	

	public static QihooSDKSavingManager getInstance() {
    	if(m_self == null)
    	{
    		m_self = new QihooSDKSavingManager();
    	}
		return m_self;
	}
}
