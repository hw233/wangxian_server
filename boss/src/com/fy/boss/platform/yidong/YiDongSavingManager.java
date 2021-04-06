package com.fy.boss.platform.yidong;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.platform.yidong.YiDongSavingManager;

public class YiDongSavingManager {

	protected static YiDongSavingManager m_self = null;
	
	public void initialize() throws Exception {
		m_self = this;
		System.out.println("[" + this.getClass().getName() + "] [initialized]");
	}
	
	public static String freeType = "12";
//	public static String cpId = "741018";
	public static String cpId = "741272";
	//public static String CPServiceID = "601810061314";
	public static String CPServiceID = "627210066786";
	public static String fid_139 = "1000";
	public static String fid_adhome = "1001";
	public static String PackageID = "000000000000";
	public static String CPSign = "000000";

	public static Map<String,String> fidMap = new HashMap<String,String>();
	
	static
	{
		fidMap.put("139SDK01_MIESHI", "1214");
		fidMap.put("139SDK02_MIESHI", "1213");
		fidMap.put("139SDK03_MIESHI", "1328");
		fidMap.put("139SDK04_MIESHI", "1217");
		fidMap.put("139SDK05_MIESHI", "1329");
		fidMap.put("139SDK06_MIESHI", "1210");
		fidMap.put("139SDK07_MIESHI", "1211");
		fidMap.put("139SDK08_MIESHI", "1215");
		fidMap.put("139SDK09_MIESHI", "1216");
	}
	
	/**
 		移动充值
	 * @return
	 */
	public String yidongSaving(Passport passport, String channel, String servername, int addAmount,String consumecode, String os) {
		long startTime = System.currentTimeMillis();
		String savingMediumName = "移动短信";
		try {
			
			String fid = fid_adhome;
			if(channel.toLowerCase().indexOf("139sdk") >= 0)
			{
				
				fid = fid_139;
			}
			else if(channel.toLowerCase().indexOf("adhome") >= 0)
			{
				
				fid = fid_adhome;
			}
			
			String tempfid = fidMap.get(channel);
			if(!StringUtils.isEmpty( tempfid ))
			{
				fid = tempfid;
			}
			
			
			
			
			
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			//生成订单
			OrderForm order = new OrderForm();
			order.setCreateTime(new Date().getTime());
			//设置充值平台
			order.setSavingPlatform("移动");
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
					PlatformSavingCenter.logger.info("[移动充值订单生成] [成功] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [fid:"+fid+"] [充值平台:移动] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [返回值:"+returnValue+"] [channel:"+channel+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return returnValue;
			}
			else
			{
				PlatformSavingCenter.logger.error("[移动充值订单生成] [失败] [订单格式不合法] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [fid:"+fid+"] [充值平台:移动] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [返回内容:"+returnContent+"] [channel:"+channel+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return null;
			}
		
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[移动充值订单生成] [失败] [生成订单出现异常] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [fid:--] [充值平台:移动] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			return null;
		} 
	}

	public String yidongSaving(Passport passport, String channel, String servername, int addAmount,String consumecode, String os,String[]others) {
		long startTime = System.currentTimeMillis();
		String savingMediumName = "移动短信";
		try {
			
			String fid = fid_adhome;
			if(channel.toLowerCase().indexOf("139sdk") >= 0)
			{
				
				fid = fid_139;
			}
			else if(channel.toLowerCase().indexOf("adhome") >= 0)
			{
				
				fid = fid_adhome;
			}
			
			
			OrderFormManager orderFormManager = OrderFormManager.getInstance();
			//生成订单
			OrderForm order = new OrderForm();
			order.setCreateTime(new Date().getTime());
			//设置充值平台
			order.setSavingPlatform("移动");
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
			
			PlatformSavingCenter.logger.info("[3"+channel+"] ["+fid+"] ["+consumecode+"]");
			if (order.getId() > 0 && order.getOrderId() != null) 
			{
				String returnValue = returnContent;
				if(PlatformSavingCenter.logger.isInfoEnabled())
					PlatformSavingCenter.logger.info("[移动充值订单生成] [成功] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [fid:"+fid+"] [充值平台:移动] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [返回值:"+returnValue+"] [channel:"+channel+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return returnValue;
			}
			else
			{
				PlatformSavingCenter.logger.error("[移动充值订单生成] [失败] [订单格式不合法] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [fid:"+fid+"] [充值平台:移动] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [返回内容:"+returnContent+"] [channel:"+channel+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return null;
			}
		
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[移动充值订单生成] [失败] [生成订单出现异常] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [fid:--] [充值平台:移动] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			return null;
		} 
	}
	
	
	public static YiDongSavingManager getInstance() {
		
		if(m_self == null)
		{
			m_self = new YiDongSavingManager();
		}
		
		return m_self;
	}
	
	
	public static void main(String[] args) {
		String style = "000";
		DecimalFormat df = new DecimalFormat();
		df.applyPattern(style);
		String orderid =  System.currentTimeMillis()+df.format((long)(new Random().nextInt(1000)));
		System.out.println("orderid:"+orderid);
	}
}
