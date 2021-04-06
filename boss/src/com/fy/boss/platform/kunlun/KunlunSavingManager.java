package com.fy.boss.platform.kunlun;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.fy.boss.platform.kunlun.KunlunSavingManager;
import com.xuanzhi.tools.text.DateUtil;

public class KunlunSavingManager {

	protected static KunlunSavingManager m_self = null;
	
	private static Map<String,String> serverMap = new HashMap<String, String>();
	
	public void initialize() throws Exception {
		m_self = this;
		System.out.println("[" + this.getClass().getName() + "] [initialized]");
	}
	
	
	static
	{
		serverMap.put("昆侖仙境", "474001");
		serverMap.put("飄渺王城", "474002");
		serverMap.put("千年古城", "474003");
		serverMap.put("華山之巔", "474004");
		serverMap.put("雪域冰城", "474005");
		serverMap.put("天降神兵", "474006");
		serverMap.put("斬龍屠魔", "474007");
		serverMap.put("皇圖霸業", "474008");
		serverMap.put("人間仙境", "474009");
		serverMap.put("伏虎沖天", "474010");
		serverMap.put("無相如來", "474011");
		serverMap.put("仙侶奇緣", "474012");
		serverMap.put("仙人指路", "474013");
		serverMap.put("瓊漿玉露", "474014");
		serverMap.put("道骨仙風", "474015");
		serverMap.put("仙姿玉貌", "474016");
		serverMap.put("步雲登仙", "474017");
		serverMap.put("蜀山之道", "474018"); 
		serverMap.put("禦劍伏魔", "474019");
		serverMap.put("縱橫九州", "474020");
		serverMap.put("傲視遮天", "474021");
		serverMap.put("神魔仙界", "474022");
		serverMap.put("裂天之刃", "474023");
		serverMap.put("氣衝霄漢", "474024");
		serverMap.put("煙暖初妝", "474025");
		serverMap.put("君臨天下", "474026");
		serverMap.put("如煙若夢", "474027");
		serverMap.put("天下無雙", "474028");
		serverMap.put("風華流沙", "474029");
		serverMap.put("九天攬月", "474030");
		serverMap.put("仙尊降世", "474949");
		serverMap.put("再續仙緣", "474029");
		serverMap.put("守望仙風", "474029");
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
		String savingMediumName = "昆仑Android";
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
			order.setOrderId("kunlun"+"-"+DateUtil.formatDate(new Date(), "yyyyMMdd")  + "-"+ order.getId());
			//orderFormManager.getEm().notifyFieldChange(order, "orderId");
			//orderFormManager.updateOrderForm(order);
			orderFormManager.updateOrderForm(order, "orderId");
			if (order.getId() > 0 && order.getOrderId() != null) 
			{
				if(PlatformSavingCenter.logger.isInfoEnabled())
					PlatformSavingCenter.logger.info("[昆仑充值订单生成] [成功] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:昆仑] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return order.getOrderId()+"@"+serverMap.get(servername);
			}
			else
			{
				PlatformSavingCenter.logger.error("[昆仑充值订单生成] [失败] [订单格式不合法] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:昆仑] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return null;
			}
		
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[昆仑充值订单生成] [失败] [生成订单出现异常] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:昆仑] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			return null;
		} 
	}
	
	
	public String kunlunSaving(Passport passport, String channel, String servername, int addAmount, String os,String[]others) {
		long startTime = System.currentTimeMillis();
		String savingMediumName = "昆仑Android";
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
			order.setOrderId("kunlun"+"-"+DateUtil.formatDate(new Date(), "yyyyMMdd")  + "-"+ order.getId());
			//orderFormManager.getEm().notifyFieldChange(order, "orderId");
			//orderFormManager.updateOrderForm(order);
			orderFormManager.updateOrderForm(order, "orderId");
			if (order.getId() > 0 && order.getOrderId() != null) 
			{
				if(PlatformSavingCenter.logger.isInfoEnabled())
					PlatformSavingCenter.logger.info("[昆仑充值订单生成] [成功] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:昆仑] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return order.getOrderId()+"@"+serverMap.get(servername);
			}
			else
			{
				PlatformSavingCenter.logger.error("[昆仑充值订单生成] [失败] [订单格式不合法] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:昆仑] [订单 的ID主键:"+order.getId()+" ] [订单Id:"+order.getOrderId()+"] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				return null;
			}
		
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[昆仑充值订单生成] [失败] [生成订单出现异常] [用户id:"+passport.getId()+"] [用户名:"+passport.getUserName()+"] [充值金额:"+addAmount+"] [游戏服务器名称:"+servername+"] [手机平台:"+os+"] [充值平台:昆仑] [角色id:"+others[0]+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			return null;
		} 
	}
	

	public static KunlunSavingManager getInstance() {
	  	if(m_self == null)
    	{
    		m_self = new KunlunSavingManager();
    	}
		return m_self;
	}
}
