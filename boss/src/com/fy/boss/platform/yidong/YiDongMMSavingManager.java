package com.fy.boss.platform.yidong;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Random;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.platform.yidong.YiDongMMSavingManager;

public class YiDongMMSavingManager {

	protected static YiDongMMSavingManager m_self = null;
	
	public void initialize() throws Exception {
		m_self = this;
		System.out.println("[" + this.getClass().getName() + "] [initialized]");
	}
	
	
	/**
 		移动MM充值
	 * @return
	 */
	public String yidongSaving(Passport passport, String channel, String servername, int addAmount,String consumecode, String os) {
		long startTime = System.currentTimeMillis();
		String savingMediumName = "移动MM充值";
		try {
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			//生成订单
			OrderForm order = new OrderForm();
			order.setCreateTime(new Date().getTime());
			//设置充值平台
			order.setSavingPlatform("移动MM");
			order.setSavingMedium(savingMediumName);
			//设置充值人
			order.setPassportId(passport.getId());
			//设置消费金额
			order.setPayMoney( new Long(addAmount));
			//设置serverid
			order.setServerName(servername);
			order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
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
			String style = "000";
			DecimalFormat df = new DecimalFormat();
			df.applyPattern(style);
			String orderid =  System.currentTimeMillis()+df.format((long)(new Random().nextInt(1000)));
			order.setOrderId(orderid);
			//orderFormManager.getEm().notifyFieldChange(order, "orderId");
			//orderFormManager.updateOrderForm(order);
			orderFormManager.updateOrderForm(order, "orderId");
		
			
			
			
			
		
		//	String returnContent = freeType+cpId+CPServiceID+consumecode+fid+PackageID+CPSign+orderid;
			String returnContent = orderid;
			

			if (order.getId() > 0 && order.getOrderId() != null) 
			{
				String returnValue = returnContent;
				if(PlatformSavingCenter.logger.isInfoEnabled())
					PlatformSavingCenter.logger.info("[移动MM充值订单生成] [成功] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:移动] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [返回值:"+returnValue+"] [channel:"+channel+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return returnValue;
			}
			else
			{
				PlatformSavingCenter.logger.error("[移动MM充值订单生成] [失败] [订单格式不合法] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:移动] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [返回内容:"+returnContent+"] [channel:"+channel+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return null;
			}
		
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[移动MM充值订单生成] [失败] [生成订单出现异常] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [fid:--] [充值平台:移动] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			return null;
		} 
	}

	public String yidongSaving(Passport passport, String channel, String servername, int addAmount,String consumecode, String os,String[]others) {
		long startTime = System.currentTimeMillis();
		String savingMediumName = "移动MM充值";
		try {
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			//生成订单
			OrderForm order = new OrderForm();
			order.setCreateTime(new Date().getTime());
			//设置充值平台
			order.setSavingPlatform("移动MM");
			order.setSavingMedium(savingMediumName);
			//设置充值人
			order.setPassportId(passport.getId());
			//设置消费金额
			order.setPayMoney( new Long(addAmount));
			//设置serverid
			order.setServerName(servername);
			order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
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
			String style = "000";
			DecimalFormat df = new DecimalFormat();
			df.applyPattern(style);
			String orderid =  System.currentTimeMillis()+df.format((long)(new Random().nextInt(1000)));
			order.setOrderId(orderid);
			//orderFormManager.getEm().notifyFieldChange(order, "orderId");
			//orderFormManager.updateOrderForm(order);
			orderFormManager.updateOrderForm(order, "orderId");
		
			
			
			
			
		
			//String returnContent = freeType+cpId+CPServiceID+consumecode+fid+PackageID+CPSign+orderid;
			String returnContent = orderid;
			
			if (order.getId() > 0 && order.getOrderId() != null) 
			{
				String returnValue = returnContent;
				if(PlatformSavingCenter.logger.isInfoEnabled())
					PlatformSavingCenter.logger.info("[移动MM充值订单生成] [成功] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:移动] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [返回值:"+returnValue+"] [channel:"+channel+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return returnValue;
			}
			else
			{
				PlatformSavingCenter.logger.error("[移动MM充值订单生成] [失败] [订单格式不合法] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:移动] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [返回内容:"+returnContent+"] [channel:"+channel+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return null;
			}
		
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[移动MM充值订单生成] [失败] [生成订单出现异常] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [fid:--] [充值平台:移动] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			return null;
		} 
	}
	
	
	public static YiDongMMSavingManager getInstance() {
		
		if(m_self == null)
		{
			m_self = new YiDongMMSavingManager();
		}
		
		return m_self;
	}
}
