package com.fy.boss.platform.kunlun;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.platform.xiaomi.XiaomiSdkSavingManager;
import com.fy.boss.platform.kunlun.MaLaiSavingManager;
import com.xuanzhi.tools.text.DateUtil;

public class MaLaiSavingManager {

	protected static MaLaiSavingManager m_self = null;
	
	private static Map<String,String> serverMap = new HashMap<String, String>();
	
	public void initialize() throws Exception {
		m_self = this;
		System.out.println("[" + this.getClass().getName() + "] [initialized]");
	}
	
	
	static
	{
		serverMap.put("马来测试服", "525001");
		serverMap.put("凤舞九天", "525002");
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
	public String kunlunSaving(Passport passport, String channel, String servername, int addAmount, String os) {
		long startTime = System.currentTimeMillis();
		String savingMediumName = "MalaiAndroid";
		try {
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			//生成订单
			OrderForm order = new OrderForm();
			order.setCreateTime(new Date().getTime());
			//设置充值平台
			order.setSavingPlatform("昆仑");
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
			order.setOrderId("malai"+"-"+DateUtil.formatDate(new Date(), "yyyyMMdd")  + "-"+ order.getId());
			//orderFormManager.getEm().notifyFieldChange(order, "orderId");
			//orderFormManager.updateOrderForm(order);
			orderFormManager.updateOrderForm(order, "orderId");
			if (order.getId() > 0 && order.getOrderId() != null) 
			{
				if(PlatformSavingCenter.logger.isInfoEnabled())
					PlatformSavingCenter.logger.info("[昆仑充值订单生成] [成功] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:昆仑] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [渠道:"+channel+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return order.getOrderId()+"@"+serverMap.get(servername);
			}
			else
			{
				PlatformSavingCenter.logger.error("[昆仑充值订单生成] [失败] [订单格式不合法] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:昆仑] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [渠道:"+channel+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return null;
			}
		
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[昆仑充值订单生成] [失败] [生成订单出现异常] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:昆仑] [渠道:"+channel+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			return null;
		} 
	}
	
	
	public String kunlunSaving(Passport passport, String channel, String servername, int addAmount, String os,String[]others) {
		long startTime = System.currentTimeMillis();
		String savingMediumName = "MalaiAndroid";
		try {
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			//生成订单
			OrderForm order = new OrderForm();
			order.setCreateTime(new Date().getTime());
			//设置充值平台
			order.setSavingPlatform("昆仑");
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
			//先存入到数据库中
			if(others.length > 1){
				order.setChargeValue(others[1]);
			}
			if(others.length > 2){
				order.setChargeType(others[2]);
			}
			order = orderFormManager.createOrderForm(order);
			//设置订单号
			order.setOrderId("malai"+"-"+DateUtil.formatDate(new Date(), "yyyyMMdd")  + "-"+ order.getId());
			//orderFormManager.getEm().notifyFieldChange(order, "orderId");
			//orderFormManager.updateOrderForm(order);
			orderFormManager.updateOrderForm(order, "orderId");
			if (order.getId() > 0 && order.getOrderId() != null) 
			{
				if(PlatformSavingCenter.logger.isInfoEnabled())
					PlatformSavingCenter.logger.info("[昆仑充值订单生成] [成功] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:昆仑] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [渠道:"+channel+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return order.getOrderId()+"@"+serverMap.get(servername);
			}
			else
			{
				PlatformSavingCenter.logger.error("[昆仑充值订单生成] [失败] [订单格式不合法] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:昆仑] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [渠道:"+channel+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return null;
			}
		
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[昆仑充值订单生成] [失败] [生成订单出现异常] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:昆仑] [渠道:"+channel+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			return null;
		} 
	}

	public static MaLaiSavingManager getInstance() {
	  	if(m_self == null)
    	{
    		m_self = new MaLaiSavingManager();
    	}
		return m_self;
	}
}
